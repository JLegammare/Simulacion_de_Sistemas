package ar.edu.itba.simulacion.methods;

import ar.edu.itba.simulacion.models.Pair;

import java.util.ArrayList;
import java.util.List;

public class GearPredictoO5 {

    private static int order = 5;
    private static final double gamma = 100.0;
    private static final double k = Math.pow(10.0, 4);
    private static final double[] alpha = {3.0/16, 251.0/360, 1.0, 11.0/18, 1.0/6, 1.0/60};

    public static Double predict(Pair<Double, Double> position, Pair<Double, Double> vel, Pair<Double, Double> force, double dt, double m){
        double r0 = position.getX_value();
        double r1 = vel.getX_value();
        double r2 = force.getX_value()/m;
        double r3 = -k*r1/m - gamma*r2/m;
        double r4 = -k*r2/m - gamma*r3/m;
        double r5 = -k*r3/m - gamma*r4/m;

        List<Double> predictedRs = new ArrayList<>();
        double rp = 0.0;
        for(int i = 0; i <= order; i++){
            switch(i){
                case 0:
                   rp = r0 + r1*dt + r2*(Math.pow(dt,2)/fact(2.0)) + r3*(Math.pow(dt, 3)/fact(3.0)) + r4*(Math.pow(dt, 4)/fact(4.0)) + r5*(Math.pow(dt, 5)/fact(5.0));
                   break;
                case 1:
                    rp = r1 + r2*dt + r3*(Math.pow(dt, 2)/fact(2.0)) + r4*(Math.pow(dt, 2)/fact(2.0)) + r5*(Math.pow(dt, 2)/fact(2.0));
                    break;
                case 2:
                    rp = r2 + r3*dt + r4*(Math.pow(dt,2)/fact(2.0)) + r5*(Math.pow(dt, 3)/fact(3.0));
                    break;
                case 3:
                    rp = r3 + r4*dt + r5*(Math.pow(dt,2)/fact(2.0));
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
    }

    private static Double fact (Double n) {
        if (n==0.0)
            return 1.0;
        else
            return n * fact(n-1);
    }

}
