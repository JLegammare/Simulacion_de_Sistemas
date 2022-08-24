package utils;

import models.Particle;

import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator {


    public static List<Particle> generateRandomParticles(int numberOfParticles,
                                                         int m,
                                                         double l,
                                                         double n,
                                                         double particleRadius,
                                                         double initialSpeed
    ) {
        double boardSideLength = l;
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < numberOfParticles; i++) {
            double x = Math.random() * boardSideLength;
            double y = Math.random() * boardSideLength;
            double omega = Math.random() * 2* Math.PI;
            double deltaOmega = Math.random()*n-n/2;

            particles.add(new Particle(i, x, y, particleRadius, 1,initialSpeed,omega,deltaOmega));

        }

            return particles;

    }
}
