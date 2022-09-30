package ar.edu.itba.simulacion.methods;

import ar.edu.itba.simulacion.HarmonicOscillator;
import ar.edu.itba.simulacion.models.Pair;

public class Beeman {

    public static Pair<Double, Double> position(Pair<Double, Double> currentPos,
                                        Pair<Double, Double> currentVel,
                                        Pair<Double, Double> currentAcc,
                                        Pair<Double, Double> previousAcc,
                                        double dt
                                        ) {

        return new Pair<>(
                calcPosition(currentPos.getX_value(),
                             currentVel.getX_value(),
                             currentAcc.getX_value(),
                             previousAcc.getX_value(),
                             dt),
                calcPosition(currentPos.getY_value(),
                             currentVel.getY_value(),
                             currentAcc.getY_value(),
                             previousAcc.getY_value(),
                             dt));
    }

    public static Pair<Double, Double> velocity(Pair<Double, Double> currentAcc,
                                        Pair<Double, Double> previousAcc,
                                        Pair<Double, Double> newPosition,
                                        double dt,
                                        Pair<Double, Double> currentVel,
                                        double m) {

        Pair<Double, Double> predictedVel = new Pair<>(
                calcPredictedV(currentVel.getX_value(), currentAcc.getX_value(), previousAcc.getX_value(), dt),
                calcPredictedV(currentVel.getY_value(), currentAcc.getY_value(), previousAcc.getY_value(), dt));


        Pair<Double, Double> newForce = new Pair<>(
                HarmonicOscillator.calcForce(newPosition.getX_value(), predictedVel.getX_value()),
                HarmonicOscillator.calcForce(newPosition.getY_value(), predictedVel.getY_value()));

        //nextAcc is calculated from newPos and predictedV
        Pair<Double, Double> correctedV = new Pair<>(
                calcCorrectedV(currentVel.getX_value(), newForce.getX_value()/m, currentAcc.getX_value(), previousAcc.getX_value(), dt),
                calcCorrectedV(currentVel.getY_value(), newForce.getY_value()/m, currentAcc.getY_value(), previousAcc.getY_value(), dt));

        return correctedV;

    }

    private static Double calcPosition(Double currPos, Double currVel, Double currAcc, Double prevAcc, double dt){
        return currPos + currVel * dt + (2.0 * currAcc / 3 - prevAcc / 6) * Math.pow(dt, 2);
    }

    private static Double calcPredictedV(Double v, Double currAcc, Double prevAcc, double dt){
        return v + 3.0 * currAcc * dt / 2 - prevAcc * dt / 2;

    }
    
    private static Double calcCorrectedV(Double v, Double nextAcc, Double acc, Double prevAcc, double dt){
        return v + 1.0 * nextAcc * dt / 3 + 5.0 * acc * dt / 6 - 1.0 * prevAcc * dt / 6;
    }

}
