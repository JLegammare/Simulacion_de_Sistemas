package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.methods.Beeman;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;

public class HarmonicOscillator {

    //parameters
    private static final double m = 70.0; //kg
    private static final double k = Math.pow(10.0, 4); //N/mts
    private static final double gamma = 100.0; //kg/s
    private static final  double tf = 5.0; //s
    //initial conditions
    private static final double r = 1.0; //mts
    private static final double v = -gamma/(2*m); //mts/s
    //En realidad v = - A*gamma/(2*m) pero no se que es A



    public static void main(String[] args) {

//        Particle particle = new Particle(0,)
       
    }

    public static double calcForce(Double pos, Double vel){
        return -k*pos - gamma*vel;
    }
}
