package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;
import ar.edu.itba.simulacion.utils.ParticleGenerator;
import ar.edu.itba.simulacion.utils.ResultsGenerator;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class VibratingSilo {

    final static double W = 20.0;
    final static double L = 70.0;
    final static int D = 3;
    final static double w = 5;
    final static double A = 0.15;
    final static double G = 5;
    final static int NUMBER_OF_PARTICLES = 200;
    final static int kN = 250;
    final static int kT = 2 * kN;
    final static double DT = 1E-3;
    final static double FINAL_T = 5.0;

    private static final String RESULTS_DIRECTORY = "simulation_results/test";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final String TIME_FILE = "Time.txt";

    public static void main(String[] args) throws IOException {

//        List<Particle> particles = ParticleGenerator.generateRandomParticles(NUMBER_OF_PARTICLES, L, W);
        List<Particle> particles = ParticleGenerator.parseParticles("simulation_results/Static.txt");
        ResultsGenerator rg = new ResultsGenerator(DYNAMIC_FILE, STATIC_FILE, TIME_FILE, RESULTS_DIRECTORY);
        rg.fillStaticFile(particles);

        vibratingSilo(rg, particles, w, D);
    }

    public static void vibratingSilo(ResultsGenerator rg, List<Particle> particles, double w, double D) throws IOException {
        //0.CALCULAR VELOCIDADES INICIALES Y FUERZAS (EXPRESIONES N.2 Y T.3 DE LA DIAPOSITIVA 15 DE LA TEORICA 5) PARA t=0;

        Map<Particle, List<Pair<Double, Double>>> currentRs = initParticleRs(particles);
        Map<Particle, List<Pair<Double, Double>>> previousRS = eulerParticleRs(currentRs, -DT);

        rg.addStateToDynamicFile(currentRs, W, L, D, w, A, 0);
        int it = 1;
        for (double t = DT; t <= FINAL_T; t += DT, it += 1) {

            Map<Particle, List<Pair<Double, Double>>> nextRs = beemanRs(previousRS, currentRs, DT, t, w, D);

            List<Particle> reinsertParticles = new ArrayList<>();
            nextRs.forEach((k, v) -> {
                if (v.get(0).getY_value() < -L / 10.0) {
                    reinsertParticles.add(k);
                }
            });
            if (reinsertParticles.size() > 0) {
                for (Particle k : reinsertParticles) {
                    nextRs.remove(k);
                    rg.addTimeToFile(t);
                }
                //las mando arriba
                List<Particle> rangeParticles = getParticlesInRange(new ArrayList<>(nextRs.keySet()), 40, 70);
                toTop(reinsertParticles, rangeParticles);
                Map<Particle, List<Pair<Double, Double>>> reinsertedRs = initParticleRs(reinsertParticles);
                Map<Particle, List<Pair<Double, Double>>> prevReinsertedRs = eulerParticleRs(reinsertedRs, -DT);
                reinsertedRs = beemanRs(prevReinsertedRs, reinsertedRs, DT, t, w, D);
                nextRs.putAll(reinsertedRs);
                rangeParticles.clear();
            }

            System.out.println(t);
            if((it+1)%100==0){
                rg.addStateToDynamicFile(nextRs,W,L,D,w,A,t);
            }
            currentRs = nextRs;
        }
    }

    private static Map<Particle, List<Pair<Double, Double>>> eulerParticleRs(Map<Particle, List<Pair<Double, Double>>> currentRs, double dt) {

        Map<Particle, List<Pair<Double, Double>>> rsMap = new HashMap<>();

        currentRs.forEach((k, v) -> {
            List<Pair<Double, Double>> particleRs = new ArrayList<>();

            Pair<Double, Double> r0 = new Pair(
                    v.get(0).getX_value()
                            + v.get(1).getX_value() * dt
                            + v.get(2).getX_value() * k.getMass() * (dt * dt / (2 * k.getMass())),
                    v.get(0).getY_value()
                            + v.get(1).getY_value() * dt
                            + v.get(2).getY_value() * k.getMass() * (dt * dt / (2 * k.getMass()))
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
            double t,
            double w,
            double D
    ) {
        Map<Particle, List<Pair<Double, Double>>> newRsMap = new HashMap<>();

        previousRs.forEach((p, prevParticleRs) -> {

            List<Pair<Double, Double>> newParticleRs = new ArrayList<>();

            List<Pair<Double, Double>> currentParticleRs = currentRs.get(p);

            Pair<Double, Double> position = new Pair<>(
                    currentParticleRs.get(0).getX_value()
                            + currentParticleRs.get(1).getX_value() * dt
                            + ((2.0 / 3.0) * currentParticleRs.get(2).getX_value()
                            - (1.0 / 6.0) * prevParticleRs.get(2).getX_value()) * dt * dt,
                    currentParticleRs.get(0).getY_value()
                            + currentParticleRs.get(1).getY_value() * dt
                            + ((2.0 / 3.0) * currentParticleRs.get(2).getY_value()
                            - (1.0 / 6.0) * prevParticleRs.get(2).getY_value()) * dt * dt
            );
            newParticleRs.add(0, position);

            Pair<Double, Double> predictedVelocity = new Pair<>(
                    currentParticleRs.get(1).getX_value()
                            + ((3.0 / 2.0) * currentParticleRs.get(2).getX_value()
                            - (1.0 / 2.0) * prevParticleRs.get(2).getX_value()) * dt,
                    currentParticleRs.get(1).getY_value()
                            + ((3.0 / 2.0) * currentParticleRs.get(2).getY_value()
                            - (1.0 / 2.0) * prevParticleRs.get(2).getY_value()) * dt
            );
            newParticleRs.add(1, predictedVelocity);

            newRsMap.put(p, newParticleRs);

        });

        List<Pair<Double, Double>> siloCurrentRS = new ArrayList<>();

        siloCurrentRS.add(0, new Pair<>(0.0, A * sin(w * t)));
        siloCurrentRS.add(1, new Pair<>(0.0, A * w * cos(w * t)));

        currentRs.forEach((p, currentParticleRs) -> {

            Pair<Double, Double> predictedAcc = calcAcceleration(p, newRsMap, siloCurrentRS, D);
            newRsMap.get(p).set(1, new Pair<>(
                    currentParticleRs.get(1).getX_value()
                            + ((1.0 / 3.0) * predictedAcc.getX_value()
                            + (5.0 / 6.0) * currentParticleRs.get(2).getX_value()
                            - (1.0 / 6.0) * previousRs.get(p).get(2).getX_value()) * dt,
                    currentParticleRs.get(1).getY_value()
                            + ((1.0 / 3.0) * predictedAcc.getY_value()
                            + (5.0 / 6.0) * currentParticleRs.get(2).getY_value()
                            - (1.0 / 6.0) * previousRs.get(p).get(2).getY_value()) * dt
            ));
        });

        newRsMap.forEach((p, particleRs) -> {
            Pair<Double, Double> newAcceleration = calcAcceleration(p, newRsMap, siloCurrentRS, D);
            newRsMap.get(p).add(2, newAcceleration);

        });

        return newRsMap;
    }

    private static Pair<Double, Double> calcAcceleration(Particle selectedParticle,
                                                         Map<Particle, List<Pair<Double, Double>>> particlesRs,
                                                         List<Pair<Double, Double>> siloRs, double D) {

        Pair<Double, Double> totalForce = new Pair<>(0.0, 0.0);

        particlesRs.entrySet().forEach(e -> {

            Particle p = e.getKey();

            if (!selectedParticle.equals(p)) {
                Pair<Double, Double> force = collisionForce(
                        selectedParticle.getRadius(), p.getRadius(),
                        particlesRs.get(selectedParticle), particlesRs.get(p));
                totalForce.setX_value(totalForce.getX_value() + force.getX_value());
                totalForce.setY_value(totalForce.getY_value() + force.getY_value());
            }
        });


        List<Pair<Double, Double>> pRs = particlesRs.get(selectedParticle);
        List<Pair<Double, Double>> leftWallRs = new ArrayList<>();
        List<Pair<Double, Double>> rightWallRs = new ArrayList<>();
        List<Pair<Double, Double>> bottomWallRs = new ArrayList<>();
        List<Pair<Double, Double>> leftBorderRendija = new ArrayList<>();
        List<Pair<Double, Double>> rightBorderRendija = new ArrayList<>();

        List<Pair<Double, Double>> wallForces = new ArrayList<>();

        if (pRs.get(0).getX_value() - selectedParticle.getRadius() <= 0) {
            leftWallRs.add(0, new Pair<>(0.0, particlesRs.get(selectedParticle).get(0).getY_value()));
            leftWallRs.add(1, siloRs.get(1));
            wallForces.add(collisionForce(selectedParticle.getRadius(), 0, pRs, leftWallRs));
        }

        if (pRs.get(0).getX_value() + selectedParticle.getRadius() >= W) {
            rightWallRs.add(0, new Pair<>(W, particlesRs.get(selectedParticle).get(0).getY_value()));
            rightWallRs.add(1, siloRs.get(1));
            wallForces.add(collisionForce(selectedParticle.getRadius(), 0, pRs, rightWallRs));
        }

        if (pRs.get(0).getY_value() <= selectedParticle.getRadius()) {

            double xPosition = pRs.get(0).getX_value();
            if (xPosition > W / 2.0 - D / 2.0 && xPosition < W / 2.0 + D / 2.0) {
                leftBorderRendija.add(0, new Pair<>(W / 2.0 - D / 2.0, 0.0));
                rightBorderRendija.add(0, new Pair<>(W / 2.0 + D / 2.0, 0.0));
                leftBorderRendija.add(1, new Pair<>(0.0, siloRs.get(1).getY_value()));
                rightBorderRendija.add(1, new Pair<>(0.0, siloRs.get(1).getY_value()));
                wallForces.add(collisionForce(selectedParticle.getRadius(), 0, pRs, leftBorderRendija));
                wallForces.add(collisionForce(selectedParticle.getRadius(), 0, pRs, rightBorderRendija));
            } else {
                bottomWallRs.add(0, new Pair<>(pRs.get(0).getX_value(), siloRs.get(0).getY_value()));
                bottomWallRs.add(1, new Pair<>(0.0, siloRs.get(1).getY_value()));
                wallForces.add(collisionForce(selectedParticle.getRadius(), 0, pRs, bottomWallRs));
            }
        }

        wallForces.forEach(f -> {
            totalForce.setX_value(totalForce.getX_value() + f.getX_value());
            totalForce.setY_value(totalForce.getY_value() + f.getY_value());
        });


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
        double distance = hypot(deltaPosition.getX_value(), deltaPosition.getY_value());
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
            force.setY_value(FN * en.getY_value() + FT * et.getY_value());
        }

        return force;

    }

    private static void toTop(List<Particle> particles, List<Particle> rangeParticles) {
        int i = 0;
        while (i < particles.size()) {
            Particle p = particles.get(i);
            //List<Particle> auxParticles = new ArrayList<>(particles);
            //auxParticles.remove(i);
            p.setPosition(new Pair<>(Math.random() * W, 40 + (Math.random() * (30))));
            p.setVelocity(new Pair<>(0.0, 0.0));
            if (i == 0 || ParticleGenerator.particleSeparated(p.getRadius(), p.getPosition(), W, L, rangeParticles)) {
                i++;
                rangeParticles.add(p);
            }
        }
    }

    private static List<Particle> getParticlesInRange(List<Particle> particles, int min, int max) {
        List<Particle> rangeParticles = new ArrayList<>();
        for (Particle p : particles) {
            if (p.getPosition().getY_value() >= min && p.getPosition().getY_value() <= max)
                rangeParticles.add(p);
        }
        return rangeParticles;
    }


}
