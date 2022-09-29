package ar.edu.itba.simulacion.methods;

import ar.edu.itba.simulacion.models.Pair;
import java.util.List;

public class OriginalVerlet {

    public static Pair<Double,Double> position(List<Pair<Double, Double>> positions,
                                        double dt,
                                        Pair <Double,Double> force,
                                        double m){

        int listSize = positions.size();
        Pair<Double,Double> tMinusDt = positions.get(listSize-2);
        Pair<Double,Double> currentT = positions.get(listSize-1);

        return new Pair<>(
                calcPosition(
                        currentT.getX_value(),
                        tMinusDt.getX_value(),
                        dt,
                        force.getX_value(),
                        m),
                calcPosition(
                        currentT.getY_value(),
                        tMinusDt.getY_value(),
                        dt,
                        force.getY_value(),
                        m));
    }

    private static Double calcPosition(Double currentT, Double tMinusDt, double dt, double f, double m){
        return 2 * currentT - tMinusDt + Math.pow(dt, 2) * f / m;
    }

    public Pair<Double,Double> velocity(List<Pair<Double,Double>>particlePosition,
                                        double dt,
                                        Pair <Double,Double> force,
                                        double m){
        Pair<Double,Double> nextPosition = position( particlePosition,dt,force,m);
        Pair<Double,Double> previousPosition = particlePosition.get(particlePosition.size()-2);
        return new Pair<>(
                calcVelocity(nextPosition.getX_value(), previousPosition.getX_value(),dt),
                calcVelocity(nextPosition.getY_value(), previousPosition.getY_value(), dt)
        );
    }

    private Double calcVelocity(Double nextPosition, Double previousPosition, double dt) {
        return  (nextPosition-previousPosition)/(2*dt);
    }

}
