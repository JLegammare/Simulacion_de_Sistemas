package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Body;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.utils.PlanetsResultsGenerator;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class VenusTrip {

    private static final String RESULTS_DIRECTORY = "simulation_results/Venus_Mission";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final Color SUN_COLOR = Color.yellow;
    private static final Color EARTH_COLOR = Color.BLUE;
    private static final Color VENUS_COLOR = Color.ORANGE;
    private static final Color SPACESHIP_COLOR = Color.MAGENTA;
    private static final double G = 6.693E-20;
    private static final double DT = 0.001;
    private static final double TF = 70;
    private static final int ORDER = 5;
    private static final double GAMMA = 100.0;
    private static final double k = Math.pow(10.0, 4);
    private static final double[] alpha = {3.0/16, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60};
    //SPACESHIP INPUT VALUES:
    private static final double SPACESHIP_INIT_DISTANCE_FROM_EARTH = 1500.0;
    private static final double SPACESHIP_ORBITAL_VELOCITY = 7.12;
    private static final double SPACESHIP_TAKE_OFF_VELOCITY = 8;
    private static final double SPACESHIP_MASS = 2E+5;
    private static final double SPACESHIP_RADIUS = 1;
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


    public static void main(String[] args) throws IOException, ParseException {

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


    public static void venusTripMethod(PlanetsResultsGenerator rg, List<Body> bodies, double dt,double tf) throws IOException {

        Body spaceship = getSpaceship(bodies.get(0),bodies.get(2));
        bodies.add(spaceship);
        rg.fillStaticFile(bodies);

        int i = 0;

        //Aceleraciones en t=0
        Map<Body, Pair<Double, Double>> initAccelerations = new TreeMap<>();

        bodies.forEach(b -> {
            initAccelerations.put(b, calcAcceleration(b, bodies));
        });

        rg.addStateToDynamicFile(bodies,0);

        for (double t = dt; t <= tf; t += dt, i += 1) {
//            //TODO: IMPLEMENTAR EL GEAR PREDICT O5/BEEMAN/VERLET PARA CALCULAR LAS POSICIONES

//
            rg.addStateToDynamicFile(bodies, t);
        }

    }

     public static Body getSpaceship(Body sun, Body earth) {

        double earthSunDistance = distance(sun.getPosition(), earth.getPosition());

        Pair<Double, Double> sunEarthVersor = new Pair<>(
                (earth.getPosition().getX_value() - sun.getPosition().getX_value()) / earthSunDistance,
                (earth.getPosition().getY_value() - sun.getPosition().getY_value()) / earthSunDistance);


        Pair<Double, Double> spaceshipInitPosition = new Pair<>(
                SPACESHIP_INIT_DISTANCE_FROM_EARTH * -sunEarthVersor.getX_value()
                        + EARTH_RADIUS + earth.getPosition().getX_value(),
                SPACESHIP_INIT_DISTANCE_FROM_EARTH * -sunEarthVersor.getY_value()
                        + EARTH_RADIUS + earth.getPosition().getY_value()
        );

        Pair<Double, Double> spaceshipVersor = new Pair<>(-sunEarthVersor.getY_value(), sunEarthVersor.getX_value());
        double earthTangentialVelocity = -SPACESHIP_ORBITAL_VELOCITY - SPACESHIP_TAKE_OFF_VELOCITY
                + earth.getVelocity().getX_value() * spaceshipVersor.getX_value()
                + earth.getVelocity().getY_value() * spaceshipVersor.getY_value();

        Pair<Double, Double> spaceshipInitVelocity = new Pair<>(
                earthTangentialVelocity * spaceshipVersor.getX_value(),
                earthTangentialVelocity * spaceshipVersor.getY_value()
        );
        return new Body(3, "SPACESHIP", spaceshipInitPosition, spaceshipInitVelocity, SPACESHIP_RADIUS, SPACESHIP_MASS,SPACESHIP_COLOR);
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

//    private static List<List<Pair<Double, Double>>> predict(List<Body> bodies, double dt){
//        //r0: posicion
//        //a , b y c
//        //calulo r0 para los 3,
//        //calulas r1 para los 3,
//        //calcular r2 para los 3,
//        //calculo r3 para los 3,
//        //
//
//         Function<Pair<Pair<Double,Double>,Body>,Body> bodyTransform = p -> new Body(
//                p.getY_value().getId(),
//                p.getY_value().getName(),
//                p.getY_value().getVelocity(),
//                p.getX_value(),
//                p.getY_value().getRadius(),
//                p.getY_value().getMass());
//
//        List<Pair<Double,Double>> r0 = bodies.stream().map(Body::getPosition).collect(Collectors.toList());
//        List<Pair<Double,Double>> r1 = bodies.stream().map(Body::getVelocity).collect(Collectors.toList());
//        List<Pair<Double,Double>> r2 = bodies.stream().map(b->{
//            List<Body> otherBodies = bodies.stream().filter(b2->!b2.equals(b)).collect(Collectors.toList());
//            return calcAcceleration(b,otherBodies);
//        }).collect(Collectors.toList());
//
//        List<>
//
//        List<Pair<Double,Double>> r3 = bodies.stream().map(bodyTransform);
//
//
//        Pair<Double,Double> position = body.getPosition();
//        Pair<Double,Double> vel = body.getVelocity();
//        double m = body.getMass();
//
//
//        Pair<Double,Double> r0 = body.getPosition();
//        Pair<Double,Double> r1 = body.getVelocity();
//        Pair<Double,Double> r2 = calcAcceleration(body,bodies);
//
//        Map <Body,Pair<Double,Double>> map = new TreeMap<>();
//
//        map.put(body,calcAcceleration(body,bodies));
//        bodies.forEach(b-> map.put(b,calcAcceleration(bo)));
//
//        List<Body> newBodies = bodies.stream().map().collect(Collectors.toList());
//
//        Pair<Double,Double> r3 = calcAcceleration()
//        Pair<Double,Double> r4 =
//        Pair<Double,Double> r5 =
//
//        //r3 y de mas: no relevantes
//        //en la ecuacion orignal -k*pos/m -GAMMA*v/m
//        State st = new State(new Pair<>(r1x,r2y),new Pair<>(r2x,r2y));
//
//        double r3x = -k*r1x/m - GAMMA * r2x/m;
//        double r3y = -k*r1y/m - GAMMA * r2y/m;
//
//        State st2= new State(new Pair<>(r2x,r2y),new Pair<>(r2x,r2y));
//
//        double r4x = -k*r2x/m - GAMMA * r3x/m;
//        double r4y = -k*r2y/m - GAMMA * r3y/m;
//
//        State st3= new State(new Pair<>(r3x,r3y),new Pair<>(r2x,r2y));
//
//        double r5x = -k*r3x/m - GAMMA * r4x/m;
//        double r5y = -k*r3y/m - GAMMA * r4y/m;
//
//
//        //predict:
//        List<Pair<Double,Double>> predictedRs = new ArrayList<>();
//        double rpx = 0.0;
//        double rpy = 0.0;
//        for(int i = 0; i <= ORDER; i++){
//            switch(i){
//                case 0:
//                   rpx = r0x + r1x*dt + r2x*(Math.pow(dt,2)/fact(2)) + r3x*(Math.pow(dt, 3)/fact(3)) + r4x*(Math.pow(dt, 4)/fact(4)) + r5x*(Math.pow(dt, 5)/fact(5));
//                   rpy = r0y + r1y*dt + r2y*(Math.pow(dt,2)/fact(2)) + r3y*(Math.pow(dt, 3)/fact(3)) + r4y*(Math.pow(dt, 4)/fact(4)) + r5y*(Math.pow(dt, 5)/fact(5));
//                   break;
//                case 1:
//                    rpx = r1x + r2x*dt + r3x*(Math.pow(dt, 2)/fact(2)) + r4x*(Math.pow(dt, 3)/fact(3)) + r5x*(Math.pow(dt, 4)/fact(4));
//                    rpy = r1y + r2y*dt + r3y*(Math.pow(dt, 2)/fact(2)) + r4y*(Math.pow(dt, 3)/fact(3)) + r5y*(Math.pow(dt, 4)/fact(4));
//                    break;
//                case 2:
//                    rpx = r2x + r3x*dt + r4x*(Math.pow(dt,2)/fact(2)) + r5x*(Math.pow(dt, 3)/fact(3));
//                    rpy = r2y + r3y*dt + r4y*(Math.pow(dt,2)/fact(2)) + r5y*(Math.pow(dt, 3)/fact(3));
//                    break;
//                case 3:
//                    rpx = r3x + r4x*dt + r5x*(Math.pow(dt,2)/fact(2));
//                    rpy = r3y + r4y*dt + r5y*(Math.pow(dt,2)/fact(2));
//                    break;
//                case 4:
//                    rpx = r4x + r5x*dt;
//                    rpy = r4y + r5y*dt;
//                    break;
//                case 5:
//                    rpx = r5x;
//                    rpy = r5y;
//                    break;
//            }
//            predictedRs.add(new Pair<>(rpx,rpy));
//        }
//        //evaluate
//
//        Pair<Double,Double> acc = VenusTrip.calcAcceleration(body, bodies);
//
//        double deltaR2x = ((acc.getX_value() - predictedRs.get(2).getX_value()) * (Math.pow(dt, 2))) / 2;
//        double deltaR2y = ((acc.getY_value() - predictedRs.get(2).getY_value()) * (Math.pow(dt, 2))) / 2;
//
//        //correct
//        List<Pair<Double, Double>> correctedRs = new ArrayList<>();
//        correctedRs.add(new Pair<>(predictedRs.get(0).getX_value() + alpha[0]*deltaR2x, predictedRs.get(0).getY_value() + alpha[0]*deltaR2y));
//        for(int i = 1; i <= ORDER; i++){
//            double rcx = predictedRs.get(i).getX_value() + alpha[i]*deltaR2x*fact(i)/Math.pow(dt,i); //el 2do es la velocidad corregida. Los demas no se usan
//            double rcy = predictedRs.get(i).getY_value() + alpha[i]*deltaR2y*fact(i)/Math.pow(dt,i);
//            correctedRs.add(new Pair<>(rcx, rcy));
//        }
//        return correctedRs;
//    }

    private static int fact (int n) {
        if (n==0)
            return 1;
        else
            return n * fact(n-1);
    }




}
