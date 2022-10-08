package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Body;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.TripResult;
import ar.edu.itba.simulacion.utils.PlanetsResultsGenerator;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class VenusTrip {

    private static final String RESULTS_DIRECTORY = "simulation_results/Venus_Mission";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final Color SUN_COLOR = Color.yellow;
    private static final Color EARTH_COLOR = Color.BLUE;
    private static final Color VENUS_COLOR = Color.RED;
    private static final Color SPACESHIP_COLOR = Color.MAGENTA;
    private static final double G = 6.693E-20;
    private static final double DT = 300;
    private static final double TF = 31557600;
    private static final double TAKE_OFF_TIME = 518400;
    private static final int ORDER = 5;
    private static final double[] alpha = {3.0/20, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60};
    //SPACESHIP INPUT VALUES:
    private static final double SPACESHIP_INIT_DISTANCE_FROM_EARTH = 1500.0;
    private static final double SPACESHIP_ORBITAL_VELOCITY = 7.12;
    private static final double SPACESHIP_TAKE_OFF_VELOCITY = 8;
    private static final double SPACESHIP_MASS = 2E+5;
    private static final double SPACESHIP_RADIUS = 100;
    //SUN INPUT VALUES:
    private static final Pair<Double, Double> sunPosition = new Pair<>(0.0, 0.0);
    private static final Pair<Double, Double> sunVelocity = new Pair<>(0.0, 0.0);
    private static final double SUN_RADIUS = 696000;
    private static final double SUN_MASS = 1988500E+24;
    //EARTH INPUT VALUES:
    private static final Pair<Double, Double> earthInitPosition = new Pair<>(
            1.501409394622880E+08,
            -9.238096308876731E+05);
    private static final Pair<Double, Double> earthInitVelocity = new Pair<>(
            -2.949925999285836E-01,
            2.968579130065282E+01);
    private static final double EARTH_RADIUS = 6378.137;
    private static final double EARTH_MASS = 5.97219E+24;
    //VENUS INPUT VALUES:
    private static final Pair<Double, Double> venusInitPosition = new Pair<>(
            -1.014319519875520E+08,
            3.525562675248842E+07);
    private static final Pair<Double, Double> venusInitVelocity = new Pair<>(
            -1.166353075744313E+01,
            -3.324015683726970E+01);
    private static final double VENUS_RADIUS = 6051.84;
    private static final double VENUS_MASS = 48.685E+23;

    public static void main(String[] args) throws IOException{

        Body sun = new Body(0, "SUN",sunPosition, sunVelocity, SUN_RADIUS, SUN_MASS,SUN_COLOR);
        Body venus = new Body(1, "VENUS",venusInitPosition, venusInitVelocity, VENUS_RADIUS, VENUS_MASS,VENUS_COLOR);
        Body earth = new Body(2, "EARTH", earthInitPosition, earthInitVelocity, EARTH_RADIUS, EARTH_MASS,EARTH_COLOR);

        List<Body> bodies = new ArrayList<>();
        bodies.add(sun);
        bodies.add(venus);
        bodies.add(earth);

        PlanetsResultsGenerator rg = new PlanetsResultsGenerator(DYNAMIC_FILE, STATIC_FILE, RESULTS_DIRECTORY);

        venusTripMethod(rg, bodies, DT,TF);

    }

    public static TripResult venusTripMethod(PlanetsResultsGenerator rg, List<Body> bodies, double dt, double tf) throws IOException {

        Body spaceship = getSpaceship(bodies.get(0),bodies.get(2));
        bodies.add(spaceship);
        rg.fillStaticFile(bodies);


        Body venus = bodies.get(1);

        int i = 0;
        double t;
        double distanceTraveled = 0;

        Map<Body,List<Pair<Double,Double>>> initRs = initBodiesRs(bodies);

        rg.addStateToDynamicFile(initRs,0);
        TripResult tr = new TripResult(
                0,
                0,
                TripResult.TripStatus.LEAVING_EARTH,null);

        Pair<Double,Double> spaceshipInitPosition = initRs.get(spaceship).get(0);
        Pair<Double,Double> previusPosition = spaceshipInitPosition;

        double minDistanceToVenus = distance(initRs.get(venus).get(0),initRs.get(spaceship).get(0));

        for (t = dt; !tr.isFinished(); t += dt, i += 1) {
            initRs = gearPredict05(initRs,dt);
            rg.addStateToDynamicFile(initRs , t);
            distanceTraveled+= distance(previusPosition,initRs.get(spaceship).get(0));
            double newDistance = distance(initRs.get(venus).get(0),initRs.get(spaceship).get(0));
            if(newDistance<minDistanceToVenus){
                minDistanceToVenus = newDistance;
            }
            tr = checkEndCondition(initRs,bodies,t,tf,distanceTraveled);
            previusPosition = initRs.get(spaceship).get(0);
        }
        tr.setMinDistance(minDistanceToVenus);
        System.out.println(tr.getTs());
        System.out.println(tr.getMinDistance());
        return tr;
    }

    private static TripResult checkEndCondition(Map<Body,List<Pair<Double,Double>>> initRs, List<Body> bodies, double t, double tf, double distanceTraveled){

        Body spaceship = bodies.get(3);
        Body earth = bodies.get(2);
        Body venus = bodies.get(1);

        List<Body> othersBodies = initRs.keySet().stream().filter(b-> !b.equals(spaceship) && !b.equals(earth)).collect(Collectors.toList());
        Body collisionedBody = null;
        for(Body b :  othersBodies) {
            if(collisionBetweenBodies(initRs.get(b).get(0), b.getRadius(), initRs.get(spaceship).get(0), spaceship.getRadius())) {
                collisionedBody = b;
            }
        }

        TripResult.TripStatus ts = TripResult.TripStatus.TRAVELLING;

        if(collisionedBody!=null){
            if(collisionedBody.equals(venus)){
                return new TripResult(distanceTraveled,t, TripResult.TripStatus.VENUS_SUCCESS, collisionedBody);
            }
            else {

                return new TripResult(distanceTraveled,t, TripResult.TripStatus.OTHER_COLLISION, collisionedBody);
            }
        }
        boolean timeExceeded = t >= tf;

        return new TripResult(distanceTraveled,t,
                timeExceeded? TripResult.TripStatus.SPACESHIP_LOST: ts,null);
    }

    private static boolean collisionBetweenBodies(Pair<Double,Double> positionBody1, Double radiusBody1, Pair<Double,Double> positionBody2, Double radiusBody2) {
        double distanceBetweenBodies = distance(positionBody1,positionBody2);
        double maxDistance = radiusBody1 + radiusBody2;
        return maxDistance > distanceBetweenBodies;
    }


    public static Body getSpaceship(Body sun, Body earth) {

        double earthSunDistance = distance(sun.getPosition(), earth.getPosition());

        Pair<Double, Double> sunEarthVersor = new Pair<>(
                (earth.getPosition().getX_value() - sun.getPosition().getX_value()) / earthSunDistance,
                (earth.getPosition().getY_value() - sun.getPosition().getY_value()) / earthSunDistance);


        Pair<Double, Double> spaceshipInitPosition = new Pair<>(
                (SPACESHIP_INIT_DISTANCE_FROM_EARTH + earth.getRadius())* -sunEarthVersor.getX_value()
                        + earth.getPosition().getX_value(),
                (SPACESHIP_INIT_DISTANCE_FROM_EARTH + earth.getRadius()) * -sunEarthVersor.getY_value()
                        + earth.getPosition().getY_value()
        );

        Pair<Double, Double> spaceshipVersor = new Pair<>(-sunEarthVersor.getY_value(), sunEarthVersor.getX_value());
        double earthTangentialVelocity = -SPACESHIP_ORBITAL_VELOCITY - SPACESHIP_TAKE_OFF_VELOCITY
                + earth.getVelocity().getX_value() * spaceshipVersor.getX_value()
                + earth.getVelocity().getY_value() * spaceshipVersor.getY_value();

        Pair<Double, Double> spaceshipInitVelocity = new Pair<>(
                earthTangentialVelocity * spaceshipVersor.getX_value(),
                earthTangentialVelocity * spaceshipVersor.getY_value()
        );
        return new Body(3, "SPACESHIP", spaceshipInitPosition, spaceshipInitVelocity, SPACESHIP_RADIUS,
                SPACESHIP_MASS,SPACESHIP_COLOR);
    }

    public static Pair<Double, Double> calcAcceleration(Body bodySelected, List<Body> bodies) {

        List<Pair<Double, Double>> forces = bodies.stream().filter(b -> !b.equals(bodySelected))
                .map(body -> gravityForce(bodySelected, body))
                .collect(Collectors.toList());

        Pair<Double, Double> result = new Pair<>(0.0, 0.0);

        forces.forEach(f -> {
            result.setX_value(result.getX_value() + f.getX_value());
            result.setY_value(result.getY_value() + f.getY_value());
        });

        return new Pair<>(result.getX_value() / bodySelected.getMass(),
                result.getY_value() / bodySelected.getMass());
    }

    private static double distance(Pair<Double, Double> firstPosition, Pair<Double, Double> secondPosition) {
        return sqrt(
                pow(firstPosition.getX_value() - secondPosition.getX_value(), 2)
                        + pow(firstPosition.getY_value() - secondPosition.getY_value(), 2));
    }

    private static Pair<Double, Double> gravityForce(Body bodyA, Body bodyB) {

        Pair<Double, Double> bodyAPosition = bodyA.getPosition();
        Pair<Double, Double> bodyBPosition = bodyB.getPosition();

        double scalar = (G * bodyA.getMass() * bodyB.getMass()) / pow(distance(bodyAPosition, bodyBPosition), 2);
        Pair<Double, Double> eij = calculateEn(bodyAPosition,bodyBPosition);

        return new Pair<>(scalar * eij.getX_value(), scalar * eij.getY_value());

    }

    private static Pair<Double, Double> calculateEn(Pair<Double, Double> firstPosition, Pair<Double, Double> secondPosition) {

//        el versor apunta de  (x1,y1) a(x2,y2)
        double norm = distance(firstPosition, secondPosition);
        double x = (secondPosition.getX_value() - firstPosition.getX_value());
        double y = (secondPosition.getY_value() - firstPosition.getY_value());
        return new Pair<>(x / norm, y / norm);
    }

    private static Map<Body,List<Pair<Double,Double>>> initBodiesRs(List<Body> bodies){

    List<Pair<Double, Double>> r0 = bodies.stream().map(Body::getPosition).collect(Collectors.toList());
    List<Pair<Double, Double>> r1 = bodies.stream().map(Body::getVelocity).collect(Collectors.toList());
    List<Pair<Double, Double>> r2 = bodies.stream().map(b -> {
        List<Body> otherBodies = bodies.stream().filter(b2 -> !b2.equals(b)).collect(Collectors.toList());
            return calcAcceleration(b, otherBodies);
        }).collect(Collectors.toList());

        List<Pair<Double, Double>> r3 = List.of(
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0)
        );

        List<Pair<Double, Double>> r4 = List.of(
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0)
        );

        List<Pair<Double, Double>> r5 = List.of(
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0),
                new Pair<>(0.0, 0.0)
        );
        Map<Body,List<Pair<Double,Double>>> rsMap = new TreeMap<>();
        for (int i = 0; i <bodies.size() ; i++) {
            Body body = bodies.get(i);
            rsMap.put(body,new ArrayList<>());
            rsMap.get(body).add(r0.get(i));
            rsMap.get(body).add(r1.get(i));
            rsMap.get(body).add(r2.get(i));
            rsMap.get(body).add(r3.get(i));
            rsMap.get(body).add(r4.get(i));
            rsMap.get(body).add(r5.get(i));
        }

        return rsMap;
    }
    private static Map<Body,List<Pair<Double, Double>>> gearPredict05(Map<Body,List<Pair<Double,Double>>> bodiesRs, double dt) {

        List<Body> bodies = new ArrayList<>(bodiesRs.keySet());

        Map<Body, List<Pair<Double, Double>>> bodyrMap = new TreeMap<>();

        for (Body body : bodies) {
            bodyrMap.put(body, new ArrayList<>());
            for (int j = 0; j <= ORDER; j++) {
                bodyrMap.get(body).add(bodiesRs.get(body).get(j));
            }
        }

        //Calculo las predicciones:
        Map<Body, List<Pair<Double, Double>>> predictedRs = new TreeMap<>();

        bodyrMap.forEach((p, r) -> {

            List<Pair<Double, Double>> predicts = new ArrayList<>();

            for (int i = 0; i <= ORDER; i++) {
                Pair<Double, Double> rp = new Pair<>(0.0, 0.0);
                for (int j = i; j <= ORDER; j++) {
                    Pair<Double, Double> rj = r.get(j);
                    rp.setX_value(rp.getX_value() + rj.getX_value() * pow(dt, j - i) / fact(j - i));
                    rp.setY_value(rp.getY_value() + rj.getY_value() * pow(dt, j - i) / fact(j - i));
                }
                predicts.add(rp);
            }
            predictedRs.put(p, predicts);
        });

        Map<Body,Pair<Double,Double>> bodyAcc = new TreeMap<>();
        bodies.forEach(b->{
            List<Body> otherBodies = bodies.stream().filter(b2->!b2.equals(b)).collect(Collectors.toList());
            Pair<Double,Double> f = new Pair<>(0.0,0.0);
            Pair<Double,Double> b1Position = predictedRs.get(b).get(0);
            otherBodies.forEach(b2->{
                Pair<Double,Double> b2Position = predictedRs.get(b2).get(0);
                Pair<Double,Double> deltaR0 = new Pair<>(
                        b2Position.getX_value()-b1Position.getX_value(),
                        b2Position.getY_value()-b1Position.getY_value()
                );
                double distance =  distance(b1Position,b2Position);
                Pair<Double,Double> eij = new Pair<>(
                        deltaR0.getX_value()/distance,
                        deltaR0.getY_value()/distance);
                double constant = G*b.getMass()*b2.getMass()/pow(distance,2);
                f.setX_value(f.getX_value()+constant*eij.getX_value());
                f.setY_value(f.getY_value()+constant*eij.getY_value());
            });
            bodyAcc.put(b,new Pair<>(f.getX_value()/b.getMass(),f.getY_value()/b.getMass()));
        });

        //calculos los Delta R2
        Map<Body,Pair<Double,Double>> bodyR2Map = new TreeMap<>();
        predictedRs.forEach((k,v)->{
            Pair<Double,Double> detltaR2 = new Pair<>(
                    (bodyAcc.get(k).getX_value()-predictedRs.get(k).get(2).getX_value())*pow(dt,2)/fact(2),
                    (bodyAcc.get(k).getY_value()-predictedRs.get(k).get(2).getY_value())*pow(dt,2)/fact(2)
                    );
            bodyR2Map.put(k,detltaR2);
        });
        //se corrige
        Map<Body,List<Pair<Double,Double>>> correctionsMap = new TreeMap<>();
        predictedRs.forEach((k,v)->{
            List<Pair<Double,Double>> correctedRp = new ArrayList<>();
            for (int i = 0; i <= ORDER; i++) {
                Pair<Double,Double> predictedRp = v.get(i);
                Pair<Double,Double> rc = new Pair<>(
                        predictedRp.getX_value()+alpha[i]*bodyR2Map.get(k).getX_value()*fact(i)/pow(dt,i),
                        predictedRp.getY_value()+alpha[i]*bodyR2Map.get(k).getY_value()*fact(i)/pow(dt,i)
                );
                correctedRp.add(rc);
            }
            correctionsMap.put(k,correctedRp);
        });

        return correctionsMap;
    }
    private static int fact (int n) {
        if (n==0)
            return 1;
        else
            return n * fact(n-1);
    }

}
