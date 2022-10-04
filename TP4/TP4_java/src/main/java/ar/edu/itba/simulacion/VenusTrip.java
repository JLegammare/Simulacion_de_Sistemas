package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Body;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.utils.PlanetsResultsGenerator;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
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
    private static final Color VENUS_COLOR = Color.ORANGE;
    private static final Color SPACESHIP_COLOR = Color.MAGENTA;
    private static final double G = 6.693E-20;
    private static final double DT = 0.001;
    private static final double TF = 70;
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

//        for (double t = dt; t <= tf; t += dt, i += 1) {
//            //TODO: IMPLEMENTAR EL GEAR PREDICT O5/BEEMAN/VERLET PARA CALCULAR LAS POSICIONES
//
//            rg.addStateToDynamicFile(bodies, t);
//        }

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

    private static Pair<Double, Double> calcAcceleration(Body bodySelected, List<Body> bodies) {

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

    public static double distance(Pair<Double, Double> firstPosition, Pair<Double, Double> secondPosition) {
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


}
