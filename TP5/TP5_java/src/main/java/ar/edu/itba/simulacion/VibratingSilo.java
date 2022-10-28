package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;
import ar.edu.itba.simulacion.utils.ParticleGenerator;
import ar.edu.itba.simulacion.utils.ResultsGenerator;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class VibratingSilo {

    final static int W = 20;
    final static int L = 70;
    final static int D = 3;
    final static double w = 5;
    final static double A = 0.15;
    final static double G = 5;
    final static int NUMBER_OF_PARTICLES = 200;
    final static int kN = 250;
    final static int kT = 2 * kN;
    final static double DT = 1E-3;
    final static double FINAL_T = 10.0;

    private static final String RESULTS_DIRECTORY = "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final String TIME_FILE = "Time.txt";

    public static void main(String[] args) throws IOException {

        List<Particle> particles = ParticleGenerator.generateRandomParticles(NUMBER_OF_PARTICLES, L, W);
        ResultsGenerator rg = new ResultsGenerator(DYNAMIC_FILE, STATIC_FILE, RESULTS_DIRECTORY);
        rg.fillStaticFile(particles);

        //0.CALCULAR VELOCIDADES INICIALES Y FUERZAS (EXPRESIONES N.2 Y T.3 DE LA DIAPOSITIVA 15 DE LA TEORICA 5) PARA t=0;

        Map<Particle, List<Pair<Double, Double>>> currentRs = initParticleRs(particles);
        Map<Particle, List<Pair<Double, Double>>> previousRS = eulerParticleRs(currentRs, -DT);

        rg.addStateToDynamicFile(currentRs, 0);
        int it = 1;
        for (double t = DT; t <= FINAL_T; t += DT, it+=1) {

            currentRs = beemanRs(previousRS, currentRs, DT, t);
            //2.CONDICIONES DE CONTORNO: SI SE PASA L/10 POR DEBAJO DE LA SALIDA REINYECTARLAS POR ARRIBA
            System.out.println(t);
                rg.addStateToDynamicFile(currentRs, t);
            previousRS = currentRs;
        }

    }

    private static Map<Particle, List<Pair<Double, Double>>> eulerParticleRs(Map<Particle, List<Pair<Double, Double>>> currentRs, double dt) {

        Map<Particle, List<Pair<Double, Double>>> rsMap = new HashMap<>();

        currentRs.forEach((k, v) -> {
            List<Pair<Double, Double>> particleRs = new ArrayList<>();

            Pair<Double, Double> r0 = new Pair(
                    v.get(0).getX_value()
                            + v.get(1).getX_value() * dt
                            + v.get(2).getX_value() * k.getMass() * dt * dt / (2 * k.getMass()),
                    v.get(0).getY_value()
                            + v.get(1).getY_value() * dt
                            + v.get(2).getY_value() * k.getMass() * dt * dt / (2 * k.getMass())
            );
            particleRs.add(0, r0);

            Pair<Double, Double> r1 = new Pair<>(
                    v.get(1).getX_value()
                            + v.get(2).getX_value() * k.getMass() * (dt / k.getMass()),
                    v.get(1).getY_value()
                            + v.get(2).getY_value() * k.getMass() * (dt / k.getMass())
            );
            particleRs.add(1, r1);

            Pair<Double, Double> r2 = new Pair<>(0.0, -G);
            particleRs.add(2, r2);

            rsMap.put(k, particleRs);

        });

        return rsMap;

    }

    private static Map<Particle, List<Pair<Double, Double>>> initParticleRs(List<Particle> bodies) {

        List<Pair<Double, Double>> r0 = bodies.stream().map(Particle::getPosition).collect(Collectors.toList());
        List<Pair<Double, Double>> r1 = bodies.stream().map(Particle::getVelocity).collect(Collectors.toList());

        Map<Particle, List<Pair<Double, Double>>> rsMap = new TreeMap<>();
        for (int i = 0; i < bodies.size(); i++) {
            Particle body = bodies.get(i);
            rsMap.put(body, new ArrayList<>());
            rsMap.get(body).add(r0.get(i));
            rsMap.get(body).add(r1.get(i));
            rsMap.get(body).add(new Pair<>(0.0, -G));
        }

        return rsMap;
    }

    private static Map<Particle, List<Pair<Double, Double>>> beemanRs(
            Map<Particle, List<Pair<Double, Double>>> previousRs,
            Map<Particle, List<Pair<Double, Double>>> currentRs,
            double dt,
            double t
    ) {
        Map<Particle, List<Pair<Double, Double>>> newRsMap = new TreeMap<>();

        previousRs.forEach((p, prevParticleRs) -> {

            List<Pair<Double, Double>> newParticleRs = new ArrayList<>();

            List<Pair<Double, Double>> currentParticleRs = currentRs.get(p);

            Pair<Double, Double> position = new Pair<>(
                    currentParticleRs.get(0).getX_value()
                            + currentParticleRs.get(1).getX_value() * dt
                            + 2.0 / 3 * currentParticleRs.get(2).getX_value()
                            - 1.0 / 6 * prevParticleRs.get(2).getX_value() * dt * dt,
                    currentParticleRs.get(0).getY_value()
                            + currentParticleRs.get(1).getY_value() * dt
                            + 2.0 / 3 * currentParticleRs.get(2).getY_value()
                            - 1.0 / 6 * prevParticleRs.get(2).getY_value() * dt * dt
            );
            newParticleRs.add(0, position);

            Pair<Double, Double> predictedVelocity = new Pair<>(
                    currentParticleRs.get(1).getX_value()
                            + 3.0 / 2 * currentParticleRs.get(2).getX_value()
                            - 1.0 / 2 * prevParticleRs.get(2).getX_value() * dt,
                    currentParticleRs.get(1).getY_value()
                            + 3.0 / 2 * currentParticleRs.get(2).getY_value()
                            - 1.0 / 2 * prevParticleRs.get(2).getY_value() * dt
            );
            newParticleRs.add(1, predictedVelocity);

            newRsMap.put(p, newParticleRs);

        });

        List<Pair<Double, Double>> siloCurrentRS = new ArrayList<>();

        siloCurrentRS.add(0, new Pair<>(0.0, A * sin(w * t)));
        siloCurrentRS.add(1, new Pair<>(0.0, A * w * cos(w * t)));

        currentRs.forEach((p, currentParticleRs) -> {

            Pair<Double, Double> predictedAcc = calcAcceleration(p, newRsMap, siloCurrentRS);
            newRsMap.get(p).set(1, new Pair<>(
                    currentParticleRs.get(1).getX_value()
                            + 1.0 / 3 * predictedAcc.getX_value()
                            + 5.0 / 6 * currentParticleRs.get(2).getX_value()
                            - 1.0 / 6 * previousRs.get(p).get(2).getX_value() * dt,
                    currentParticleRs.get(1).getY_value()
                            + 1.0 / 3 * predictedAcc.getY_value()
                            + 5.0 / 6 * currentParticleRs.get(2).getY_value()
                            - 1.0 / 6 * previousRs.get(p).get(2).getY_value() * dt
            ));
        });

        newRsMap.forEach((p, particleRs) -> {
            Pair<Double, Double> newAcceleration = calcAcceleration(p, newRsMap, siloCurrentRS);
            newRsMap.get(p).add(2, newAcceleration);

        });

        return newRsMap;
    }

    private static Pair<Double, Double> calcAcceleration(Particle selectedParticle,
                                                         Map<Particle, List<Pair<Double, Double>>> particlesRs,
                                                         List<Pair<Double, Double>> siloRs) {

        Pair<Double, Double> totalForce = new Pair<>(0.0, 0.0);

        particlesRs.entrySet().forEach(e -> {

            Particle p = e.getKey();

            if (!selectedParticle.equals(p)) {
                Pair<Double, Double> force = collisionForce(selectedParticle.getRadius(), p.getRadius(), particlesRs.get(selectedParticle), particlesRs.get(p));
                totalForce.setX_value(totalForce.getX_value() + force.getX_value());
                totalForce.setX_value(totalForce.getY_value() + force.getY_value());
            }

        });
//        TODO: VER CHOQUE CON PAREDES
        List<Pair<Double, Double>> leftWall = new ArrayList<>();
        List<Pair<Double, Double>> rightWall = new ArrayList<>();
        List<Pair<Double, Double>> bottomWall = new ArrayList<>();

        totalForce.setY_value(totalForce.getY_value() - G * selectedParticle.getMass());

        return new Pair<>(totalForce.getX_value() / selectedParticle.getMass(),
                totalForce.getY_value() / selectedParticle.getMass());

    }

    private static Pair<Double, Double> collisionForce(double particleRadiusA,
                                                       double particleRadiusB,
                                                       List<Pair<Double, Double>> particleArs,
                                                       List<Pair<Double, Double>> particleBrs) {

        Pair<Double, Double> force = new Pair<>(0.0, 0.0);
        Pair<Double, Double> deltaPosition = new Pair<>(
                particleBrs.get(0).getX_value() - particleArs.get(0).getX_value(),
                particleBrs.get(0).getY_value() - particleArs.get(0).getY_value());
        double distance = distance(particleArs.get(0), particleBrs.get(0));
        double overlapping = particleRadiusA + particleRadiusB - distance;

        if (overlapping >= 0) {
            Pair<Double, Double> en = new Pair<>(
                    deltaPosition.getX_value() / distance,
                    deltaPosition.getY_value() / distance);
            Pair<Double, Double> et = new Pair<>(-en.getY_value(), en.getX_value());
            double FN = -kN * overlapping;
            Pair<Double, Double> relativeSpeed = new Pair<>(
                    particleArs.get(1).getX_value() - particleBrs.get(1).getX_value(),
                    particleArs.get(1).getY_value() - particleBrs.get(1).getY_value()
            );
            double FT = -kT *
                    overlapping *
                    (relativeSpeed.getX_value() * et.getX_value() + relativeSpeed.getY_value() * et.getY_value());

            force.setX_value(FN * en.getX_value() + FT * et.getX_value());
            force.setX_value(FN * en.getX_value() + FT * et.getY_value());
        }

        return force;

    }

    private static double distance(Pair<Double, Double> firstPosition, Pair<Double, Double> secondPosition) {
        return sqrt(
                pow(firstPosition.getX_value() - secondPosition.getX_value(), 2)
                        + pow(firstPosition.getY_value() - secondPosition.getY_value(), 2));
    }


}
