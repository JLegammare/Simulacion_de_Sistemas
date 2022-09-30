package ar.edu.itba.simulacion.methods;

import ar.edu.itba.simulacion.models.Pair;

public class Euler {
    //to get the previous positions and velocities we use -dt

    public static Pair<Double, Double> prevVel(Pair<Double, Double> vel, Pair<Double, Double> force, double dt, double m){
        Double vX = vel.getX_value() + dt * force.getX_value()/m;
        Double vY = vel.getY_value() + dt * force.getY_value()/m;
        return new Pair<>(vX, vY);

    }

    //TODO: no entendi bien si deberiamos usar la velocidad calculada arriba o cual
    public static Pair<Double, Double> prevPosition(Pair<Double, Double> pos, Pair<Double, Double> vel, Pair<Double, Double> force, double dt, double m){
        Double posX = pos.getX_value() + dt*vel.getX_value() + Math.pow(dt,2)*force.getX_value()/(2*m);
        Double posY = pos.getY_value() + dt*vel.getY_value() + Math.pow(dt,2)*force.getY_value()/(2*m);
        return new Pair<>(posX, posY);
    }
}
