package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.methods.Beeman;
import ar.edu.itba.simulacion.methods.Euler;
import ar.edu.itba.simulacion.methods.GearPredictoO5;
import ar.edu.itba.simulacion.methods.OriginalVerlet;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class HarmonicOscillator {

    //parameters
    private static final double m = 70.0; //kg
    private static final double k = Math.pow(10.0, 4); //N/mts
    private static final double gamma = 100.0; //kg/s
    private static final  double tf = 5.0; //s
    private static final double DT = 0.02;
    //initial conditions
    private static final double r0 = 1.0; //mts
    private static final double v0 = -gamma/(2*m); //mts/s
    //En realidad v = - A*gamma/(2*m) pero no se que es A



    public static void main(String[] args) {


        //BEEMAN:

        ArrayList<Pair<Double,Double>> positions = new ArrayList<>();
        ArrayList<Pair<Double,Double>> velocities = new ArrayList<>();

        Pair<Double,Double> initPosition = new Pair<>(r0,0.0);
        positions.add(initPosition);
        Pair<Double,Double> initVelocity = new Pair<>(v0,0.0);
        velocities.add(initVelocity);

        Pair<Double,Double> initAcceleration = new Pair<>(
                calcForce(initPosition.getX_value(),initVelocity.getX_value())/m,
                0.0);

//        BEEMAN:

//        for (int i = 0; i <= tf ; i++) {
//             double t=i*0.02;
//             int size = positions.size();
//             positions.add(Beeman.position(positions.get(size-1),velocities.get(size-1),currentAcc,t))
//             velocities.add(Beeman.velocity())
//        }

//        GEAR PREDICT O5
//
//        Pair <Double,Double> currentAcc = initAcceleration;
//
//        List<Double> rs;
//
//        for (int t = 0; t <=tf ; t+=DT) {
//            int size = positions.size();
//            rs = GearPredictoO5.predict(positions.get(size-1),velocities.get(size-1),currentAcc,DT,m);
//            currentAcc = new Pair<>(rs.get(2),0.0);
//            positions.add(new Pair<>(rs.get(0),0.0));
//            velocities.add(new Pair<>(rs.get(1),0.0));
//        }

//        VERLET

        //DUDA: POSITION ANTERIOR CON EULER ? YES OR NO ?
        Pair<Double,Double> prevVel = Euler.prevVel(initVelocity,new Pair<>(
                calcForce(initPosition.getX_value(),initVelocity.getX_value()),0.0),
                DT,
                m
                );

         Pair<Double,Double> prevPos = Euler.prevPosition(initPosition,initVelocity,new Pair<>(
                calcForce(initPosition.getX_value(),initVelocity.getX_value()),0.0),
                DT,
                m);

         positions.add(0,prevPos);

        for (int t = 0; t <=tf ; t+=DT) {
            int size = positions.size();
            positions.add(OriginalVerlet.position(positions,DT,new Pair<>(
                calcForce(initPosition.getX_value(),initVelocity.getX_value()),0.0),m));
        }
        int a = 0;

       
    }

    public static double calcForce(Double pos, Double vel){
        return -k*pos - gamma*vel;
    }
}
