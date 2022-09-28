package ar.edu.itba.simulacion.methods;

import ar.edu.itba.simulacion.models.Pair;

public class Beeman {

    public Pair<Double,Double> position(Pair<Double, Double> currentPos,
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
                    dt)
    );
    }

    public Pair<Double,Double> velocity(Pair<Double, Double> currentAcc,
                                        Pair<Double, Double> previousAcc,
                                        Pair<Double, Double> nextAcc,
                                        double dt,
                                        Pair<Double, Double> currentVel) {

        return new Pair<>(
                calcVelocity(
                        currentAcc.getX_value(),
                        previousAcc.getX_value(),
                        nextAcc.getX_value(),
                        dt,
                        currentVel.getX_value()
                ),
                calcVelocity(
                        currentAcc.getY_value(),
                        previousAcc.getY_value(),
                        nextAcc.getY_value(),
                        dt,
                        currentVel.getY_value()
                )
        );
    }

    private Double calcPosition(Double currPos, Double currVel, Double currAcc, Double prevAcc, double dt){
        return currPos + currVel * dt + 2/3 * currAcc * Math.pow(dt, 2) - 1/6 * prevAcc * Math.pow(dt, 2);
    }

    private Double calcVelocity(Double currentAcc,
                                Double previousAcc,
                                Double nextAcc,
                                double dt,
                                Double currentVel) {
        return currentVel + nextAcc * dt / 3 + 5 * currentAcc * dt / 6 - previousAcc * dt / 6;
    }

}
