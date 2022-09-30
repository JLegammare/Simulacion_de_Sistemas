package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.methods.Beeman;
import ar.edu.itba.simulacion.methods.Euler;
import ar.edu.itba.simulacion.methods.GearPredictoO5;
import ar.edu.itba.simulacion.methods.OriginalVerlet;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.utils.ResultsGenerator;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

public class HarmonicOscillator {

    //parameters
    private static final String RESULTS_DIRECTORY = "simulation_results";
    private static final String POSITION_FILE = "Position.txt";
    private static final double M = 70.0; //kg
    private static final double k = Math.pow(10.0, 4); //N/mts
    private static final double gamma = 100.0; //kg/s
    private static final double tf = 5.0; //s
    private static final double DT = 0.000001;
    private static final String Met = "VERLET";
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


//TODO: Euler para el paso anterior

    public static void main(String[] args) throws IOException {


        ArrayList<Pair<Double, Double>> positions = new ArrayList<>();
        ArrayList<Pair<Double, Double>> velocities = new ArrayList<>();
        ArrayList<Pair<Double, Double>> accelerations = new ArrayList<>();

        Pair<Double, Double> prevPos = Euler.prevPosition(initPosition, initVelocity, initForce, -DT, M);
        Pair<Double, Double> prevVel = Euler.prevVel(initVelocity, initForce, -DT, M);
        Pair<Double, Double> prevAcc = new Pair<>(
                        calcForce(prevPos.getX_value(), prevVel.getX_value()) / M,
                        0.0);

        ArrayList<Double> times = new ArrayList<>();
        String positionsResultsFilePath;
        ResultsGenerator rg = null;

        switch (Met) {


            case ("BEEMAN"):

                positionsResultsFilePath = String.format("%s/Beeman%s", RESULTS_DIRECTORY, POSITION_FILE);

                rg = new ResultsGenerator(
                        positionsResultsFilePath,
                        RESULTS_DIRECTORY);

                prevPos = Euler.prevPosition(initPosition, initVelocity, initForce, -DT, M);
                prevVel = Euler.prevVel(initVelocity, initForce, -DT, M);
                prevAcc = new Pair<>(
                        calcForce(prevPos.getX_value(), prevVel.getX_value()) / M,
                        0.0);

                positions.add(prevPos);
                velocities.add(prevVel);
                accelerations.add(prevAcc);

                velocities.add(initVelocity);
                positions.add(initPosition);
                accelerations.add(initAcceleration);


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
                            DT
                    );

                    Pair<Double, Double> newVel = Beeman.velocity(
                            currentAcc,
                            previousAcc,
                            newPosition,
                            DT,
                            currentVel,
                            M
                    );

                    Pair<Double, Double> newAcc = new Pair<>(
                            calcForce(newPosition.getX_value(),
                                    newVel.getX_value()) / M, 0.0);
                    accelerations.add(newAcc);
                    positions.add(newPosition);
                    velocities.add(newVel);
                    times.add(t);
                }
                positions.remove(0);
                rg.addStateToPositionFile(times, positions);

                break;

            case ("GEAR"):

                positionsResultsFilePath = String.format("%s/Gear%s", RESULTS_DIRECTORY, POSITION_FILE);

                rg = new ResultsGenerator(
                        positionsResultsFilePath,
                        RESULTS_DIRECTORY);

                List<Double> rs;

                prevPos = Euler.prevPosition(initPosition, initVelocity, initForce, -DT, M);
                prevVel = Euler.prevVel(initVelocity, initForce, -DT, M);
                prevAcc = new Pair<>(
                        calcForce(prevPos.getX_value(), prevVel.getX_value()) / M,
                        0.0);

                positions.add(prevPos);
                velocities.add(prevVel);
                accelerations.add(prevAcc);

                velocities.add(initVelocity);
                positions.add(initPosition);

                for (double t = 0; t <= tf; t += DT) {

                    int positionsSize = positions.size();
                    int velocitiesSize = velocities.size();


                    Pair<Double, Double> currentPos = positions.get(positionsSize - 1);
                    Pair<Double, Double> currentVel = velocities.get(velocitiesSize - 1);

                    rs = GearPredictoO5.predict(
                            currentPos,
                            currentVel,
                            DT, M);


                    positions.add(new Pair<>(rs.get(0), 0.0));
                    velocities.add(new Pair<>(rs.get(1), 0.0));
                    times.add(t);
                }

                break;

            case ("VERLET"):

                positionsResultsFilePath = String.format("%s/Verlet%s", RESULTS_DIRECTORY, POSITION_FILE);

                rg = new ResultsGenerator(
                        positionsResultsFilePath,
                        RESULTS_DIRECTORY);

                prevPos = Euler.prevPosition(initPosition, initVelocity, initForce, -DT, M);
                prevVel = Euler.prevVel(initVelocity, initForce, -DT, M);
                prevAcc = new Pair<>(
                        calcForce(prevPos.getX_value(), prevVel.getX_value()) / M,
                        0.0);

                positions.add(prevPos);
                velocities.add(prevVel);
                accelerations.add(prevAcc);

                velocities.add(initVelocity);
                positions.add(initPosition);
                accelerations.add(initAcceleration);

                for (double t = 0; t <= tf; t += DT) {

                    int positionsSize = positions.size();
                    int accelerationSize = accelerations.size();
                    int velocitiesSize = velocities.size();

                    Pair<Double, Double> currentPos = positions.get(positionsSize - 1);
                    Pair<Double, Double> currentVel = velocities.get(velocitiesSize - 1);


                    Pair<Double, Double> currForce = new Pair<>(
                            calcForce(currentPos.getX_value(),
                                    currentVel.getX_value()), calcForce(currentPos.getY_value(),
                            currentVel.getY_value()));

                    Pair<Double, Double> newPos = OriginalVerlet.position(positions, DT, currForce, M);
                    positions.add(newPos);
                    Pair<Double, Double> newVel = OriginalVerlet.velocity(positions, DT, currForce, M);
                    velocities.add(newVel);
                    times.add(t);

                }
                break;

            default:
                throw new InvalidObjectException("ENUM NOT FOUND");

        }

        rg.addStateToPositionFile(times, positions);
    }

    public static Double calcForce(Double pos, Double vel) {
        return -k * pos - gamma * vel;
    }

    private static Double analiticSolution(double dt) {
        return Math.exp(-(gamma / 2 * M) * dt) * Math.cos(Math.pow(k / M - (Math.pow(gamma, 2) / (4 * M * M)), 0.5) * dt);
    }
}
