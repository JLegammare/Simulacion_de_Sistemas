package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.methods.Beeman;
import ar.edu.itba.simulacion.methods.Euler;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.utils.ResultsGenerator;

import java.io.IOException;
import java.util.ArrayList;

public class HarmonicOscillator {

    //parameters
    private static final String RESULTS_DIRECTORY =  "simulation_results";
    private static final String POSITION_FILE = "Position.txt";
    private static final double M = 70.0; //kg
    private static final double k = Math.pow(10.0, 4); //N/mts
    private static final double gamma = 100.0; //kg/s
    private static final double tf = 5.0; //s
    private static final double DT = 0.002;
    //initial conditions
    private static final double r0 = 1.0; //mts
    private static final double v0 = -gamma / (2 * M); //mts/s
    private static final Pair<Double, Double> initPosition = new Pair<>(r0, 0.0);
    private static final Pair<Double, Double> initVelocity = new Pair<>(v0, 0.0);

    private static final Pair<Double, Double> initForce = new Pair<>(
            calcForce(initPosition.getX_value(), initVelocity.getX_value()),
            0.0);
    private static final Pair<Double, Double> initAcceleration = new Pair<>(
            initForce.getX_value() / M,
            0.0);
    //En realidad v = - A*gamma/(2*m) pero no se que es A

//TODO: Euler para el paso anterior

    public static void main(String[] args) throws IOException {

        String positionsResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, POSITION_FILE);

        ResultsGenerator rg = new ResultsGenerator(
                positionsResultsFilePath,
                RESULTS_DIRECTORY);



        //BEEMAN:

        ArrayList<Pair<Double, Double>> positions = new ArrayList<>();
        ArrayList<Pair<Double, Double>> velocities = new ArrayList<>();
        ArrayList<Pair<Double, Double>> accelerations = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();

        Pair<Double,Double> prevPos = Euler.prevPosition(initPosition, initVelocity, initForce,-DT,M);
        Pair<Double,Double> prevVel = Euler.prevVel(initVelocity,initForce,-DT, M);
        Pair<Double,Double> prevAcc = new Pair<>(
                calcForce(prevPos.getX_value(),prevVel.getX_value())/M,
                0.0);

        positions.add(prevPos);
        velocities.add(prevVel);
        accelerations.add(prevAcc);

        velocities.add(initVelocity);
        positions.add(initPosition);
        accelerations.add(initAcceleration);

        BEEMAN:

        for (double t = 0; t <= tf; t += DT) {
            int positionsSize = positions.size();
            int accelerationSize = accelerations.size();
            int velocitiesSize = velocities.size();

            Pair<Double, Double> currentPos = positions.get(positionsSize - 1);
            Pair<Double, Double> currentAcc = accelerations.get(accelerationSize - 1);
            Pair<Double, Double> previousAcc = accelerations.get(accelerationSize - 2);
            Pair<Double, Double> currentVel = velocities.get(velocitiesSize - 1);

            Pair<Double, Double> newPosition = Beeman.position(
                    currentPos,
                    currentVel,
                    currentAcc,
                    previousAcc,
                    t
            );

            Pair<Double, Double> newVel = Beeman.velocity(
                    currentAcc,
                    previousAcc,
                    newPosition,
                    DT,
                    currentVel,
                    M
            );

            Pair<Double,Double> newAcc = new Pair<>(
                    calcForce(newPosition.getX_value(),
                            newVel.getX_value())/M,0.0);
            accelerations.add(newAcc);
            positions.add(newPosition);
            velocities.add(newVel);
            times.add(t);
        }
        positions.remove(0);
        rg.addStateToPositionFile(times,positions);

//        GEAR PREDICT O5
//
//        Pair <Double,Double> currentAcc = initAcceleration;
//
//        List<Double> rs;
//
//        for (double t = 0; t <=tf ; t+=DT) {
//            int size = positions.size();
//            rs = GearPredictoO5.predict(positions.get(size-1),velocities.get(size-1),currentAcc,DT,m);
//            currentAcc = new Pair<>(rs.get(2),0.0);
//            positions.add(new Pair<>(rs.get(0),0.0));
//            velocities.add(new Pair<>(rs.get(1),0.0));
//        }


//        VERLET

        //DUDA: POSITION ANTERIOR CON EULER ? YES OR NO ?


//        Pair<Double,Double> prevVel = Euler.prevVel(initVelocity,new Pair<>(
//                        calcForce(initPosition.getX_value(),initVelocity.getX_value()),0.0),
//                DT,
//                m
//        );
//        Pair<Double,Double> prevPos = Euler.prevPosition(initPosition,initVelocity,new Pair<>(
//                calcForce(initPosition.getX_value(),initVelocity.getX_value()),0.0),
//                DT,
//                m);
//
//        positions.add(0,prevPos);
//
//        for (double t = 0; t <=tf ; t+=DT) {
//            int size = positions.size();
//            positions.add(OriginalVerlet.position(positions,DT,new Pair<>(
//                calcForce(initPosition.getX_value(),initVelocity.getX_value()),0.0),m));
//        }
        int a = 0;


    }

    public static Double calcForce(Double pos, Double vel) {
        return -k * pos - gamma * vel;
    }

    private static Double analiticSolution(double dt) {
        return Math.exp(-(gamma / 2 * M) * dt) * Math.cos(Math.pow(k / M - (Math.pow(gamma, 2) / (4 * M * M)), 0.5) * dt);
    }
}
