package ar.edu.itba.simulacion.methods;

import ar.edu.itba.simulacion.models.Pair;

public class SpaceTrip {

    private static final double EARTH_R = 6371.01;
    private static final double SPACE_STATION_DISTANCE = 1500.0; //Distancia desde la superficie terrestre
    private static final double SPACE_STATION_ORB_VEL = 7.12; //km/s


    public static void main(String[] args) {

        Pair<Double, Double> sunPos = new Pair<>(0.0, 0.0);

        //initial positions
        Pair<Double, Double> earthPos = new Pair<>(Double.parseDouble("1.501409394622880E+08"), Double.parseDouble("-9.238096308876731E+05"));
        Pair<Double, Double> venusPos = new Pair<>(Double.parseDouble("-1.014319519875520E+08"), Double.parseDouble("3.525562675248842E+07"));

        //planets velocities
        Pair<Double, Double> earthVel = new Pair<>(Double.parseDouble("-2.949925999285836E-01"), Double.parseDouble("2.968579130065282E+01"));
        Pair<Double, Double> venusVel = new Pair<>(Double.parseDouble("-1.166353075744313E+01"), Double.parseDouble("-3.324015683726970E+01"));

        //space station position (initial spaceship position)
        double D = Math.sqrt(Math.pow(earthPos.getX_value() - sunPos.getX_value(), 2) + Math.pow(earthPos.getY_value() - sunPos.getY_value(), 2));
        //TODO: CHECK
        double stationX = earthPos.getX_value() - (SPACE_STATION_DISTANCE + EARTH_R)/D * (sunPos.getX_value() - earthPos.getX_value());
        double stationY = earthPos.getY_value() - (SPACE_STATION_DISTANCE + EARTH_R)/D * (sunPos.getY_value() - earthPos.getY_value());

        //space station velocity (initial spaceship velocity)




    }

}
