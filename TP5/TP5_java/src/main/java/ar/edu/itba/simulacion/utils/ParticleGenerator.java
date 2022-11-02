package ar.edu.itba.simulacion.utils;

import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParticleGenerator {

    final static double r0 = 0.85;
    final static double rn = 1.15;
    final static double PARTICLE_MASS = 1.0;
    final static Color PARTICLE_COLOR = Color.RED;


    public static List<Particle> generateRandomParticles(int numberOfParticles,
                                                         double L,
                                                         double W
    ) {
        List<Particle> particles = new ArrayList<>();
        int particleId = 0;
        while (particles.size() < numberOfParticles){
            Pair<Double,Double> initPosition = new Pair<>(Math.random() * W,Math.random() * L);
            Pair<Double,Double> initialSpeed = new Pair<>(0.0,0.0);
            double radius = Math.random()*(rn-r0) + r0;
            if(particleSeparated(radius,initPosition,W,L,particles)) {
                particles.add(new Particle(particleId++, initPosition,initialSpeed, radius, PARTICLE_MASS, PARTICLE_COLOR));
            }
        }

        return particles;

    }

        public static List<Particle> parseParticles(String staticFilePath) throws IOException {

        File staticFile = new File(staticFilePath);
        Scanner staticScanner = new Scanner(staticFile);

        int numberOfParticles = staticScanner.nextInt();
        List<Particle> particles = new ArrayList<>();

        for (int j = 0; j<numberOfParticles; j++) {
            int id = staticScanner.nextInt();
            double radius = staticScanner.nextDouble();
            double mass = staticScanner.nextDouble();
            Double x = staticScanner.nextDouble();
            Double y = staticScanner.nextDouble();
            particles.add(new Particle(id,new Pair<>(x,y),new Pair<>(0.0,0.0),radius,mass,Color.RED));
        }

        return particles;
    }

    public static boolean particleSeparated(
            
            double particleRadius,
            Pair<Double,Double> initPosition,
            double W,
            double L,
            List<Particle> particles) {

        for (Particle particle: particles) {
            Pair<Double,Double> pPosition = particle.getPosition();
            if(Math.pow(initPosition.getX_value() - pPosition.getX_value(), 2)
                    + Math.pow(initPosition.getY_value() - pPosition.getY_value(), 2)
                    <= Math.pow(particleRadius + particle.getRadius(), 2)
                    || wallCollision(initPosition.getX_value(),initPosition.getY_value(),particleRadius, W,L)) {
                return false;
            }
        }
        return true;
    }

    private static boolean wallCollision(double x, double y, double particleRadius, double W, double L){
        return x - particleRadius <= 0 || x + particleRadius >= W || y - particleRadius <= 0
                || y + particleRadius >= L;
    }

}
