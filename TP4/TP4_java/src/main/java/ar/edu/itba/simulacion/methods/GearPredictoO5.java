package ar.edu.itba.simulacion.methods;

import ar.edu.itba.simulacion.HarmonicOscillator;
import ar.edu.itba.simulacion.models.Pair;

import java.util.ArrayList;
import java.util.List;

public class GearPredictoO5 {

    private static final int ORDER = 5;
    private static final double GAMMA = 100.0;
    private static final double k = Math.pow(10.0, 4);
    private static final double[] alpha = {3.0/16, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60};

    //todo: refactor
    //devuelvo una lista con pos y  velocidad en x. Lista[0]=posicion, Lista[1]=velocidad
    public static List<Double> predict(Pair<Double, Double> position, Pair<Double, Double> vel, double dt, double m){
        //r0: posicion
        double r0 = position.getX_value();
        //r1: velocidad
        double r1 = vel.getX_value();
        //r2: aceleracion
        double r2 = -k*r0/m - GAMMA * r1/m;
        //r3 y de mas: no relevantes
        double r3 = -k*r1/m - GAMMA * r2/m;
        double r4 = -k*r2/m - GAMMA * r3/m;
        double r5 = -k*r3/m - GAMMA * r4/m;


        //predict:
        List<Double> predictedRs = new ArrayList<>();
        double rp = 0.0;
        for(int i = 0; i <= ORDER; i++){
            switch(i){
                case 0:
                   rp = r0 + r1*dt + r2*(Math.pow(dt,2)/fact(2)) + r3*(Math.pow(dt, 3)/fact(3)) + r4*(Math.pow(dt, 4)/fact(4)) + r5*(Math.pow(dt, 5)/fact(5));
                   break;
                case 1:
                    rp = r1 + r2*dt + r3*(Math.pow(dt, 2)/fact(2)) + r4*(Math.pow(dt, 3)/fact(3)) + r5*(Math.pow(dt, 4)/fact(4));
                    break;
                case 2:
                    rp = r2 + r3*dt + r4*(Math.pow(dt,2)/fact(2)) + r5*(Math.pow(dt, 3)/fact(3));
                    break;
                case 3:
                    rp = r3 + r4*dt + r5*(Math.pow(dt,2)/fact(2));
                    break;
                case 4:
                    rp = r4 + r5*dt;
                    break;
                case 5:
                    rp = r5;
                    break;
            }
            predictedRs.add(rp);
        }
        //evaluate
        double acc = HarmonicOscillator.calcForce(predictedRs.get(0), predictedRs.get(1))/m;
        double deltaR2 = ((acc - predictedRs.get(2)) * (Math.pow(dt, 2))) / 2;
        
        //correct
        List<Double> correctedRs = new ArrayList<>();
        correctedRs.add(predictedRs.get(0) + alpha[0]*deltaR2); //el primero es la posicion corregida y no se divide por dt
        for(int i = 1; i <= ORDER; i++){
            double rc = predictedRs.get(i) + alpha[i]*deltaR2*fact(i)/Math.pow(dt,i); //el 2do es la velocidad corregida. Los demas no se usan
            correctedRs.add(rc);
        }
        return correctedRs;
    }

    private static int fact (int n) {
        if (n==0)
            return 1;
        else
            return n * fact(n-1);
    }

}
