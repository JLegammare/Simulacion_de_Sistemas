package utils;

import models.Particle;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator {


    public static List<Particle> generateRandomParticles(int numberOfParticles,
                                                         double l,
                                                         double particleRadius,
                                                         double property,
                                                         double mass,
                                                         Particle bigParticle,
                                                         Color particlesColor
    ) {
        List<Particle> particles = new ArrayList<>();
        particles.add(bigParticle);
        int particleId = 1;
        while (particles.size() < numberOfParticles){
            double x = Math.random() * l;
            double y = Math.random() * l;
            double initialSpeed = Math.random()*2;
            double omega = Math.random() * 2* Math.PI;
            if(particleSeparated(x, y, particleRadius, l,particles)) {
                particles.add(new Particle(particleId++, x, y, particleRadius,property,initialSpeed,omega,mass,particlesColor));
            }
        }

        return particles;

    }

    private static boolean particleSeparated(
            double x,
            double y,
            double particleRadius,
            double boardLength,
            List<Particle> particles) {

        for (Particle particle: particles) {
            if(Math.pow(x - particle.getX(), 2) + Math.pow(y - particle.getY(), 2) <=
                    Math.pow(particleRadius + particle.getRadius(), 2) || wallCollision(x,y,particleRadius, boardLength)) {
                return false;
            }
        }
        return true;
    }

    private static boolean wallCollision(double x, double y, double particleRadius, double boardLength){
        return x - particleRadius <= 0 || x + particleRadius >= boardLength || y - particleRadius <= 0
                || y + particleRadius >= boardLength;
    }

    public static void generateFiles(List<Particle> particles,
                                     String dynamicFileName,
                                     String staticFileName,
                                     String directoryPath) throws IOException {

        File directory = new File(directoryPath);
        directory.mkdir();

        File dymFile = new File(String.format("%s/%s", directoryPath, dynamicFileName));
        if (dymFile.exists())
            dymFile.delete();

        File stcFile = new File(String.format("%s/%s", directoryPath, staticFileName));
        if (stcFile.exists())
            stcFile.delete();

        FileWriter fwd = new FileWriter(dymFile, false);
        BufferedWriter bwd = new BufferedWriter(fwd);
        PrintWriter pwd = new PrintWriter(bwd);

        FileWriter fws = new FileWriter(stcFile, false);
        BufferedWriter bws = new BufferedWriter(fws);
        PrintWriter pws = new PrintWriter(bws);

        StringBuilder staticSB = new StringBuilder();
        StringBuilder dynamicSB = new StringBuilder();

        dynamicSB.append("0\n");
        staticSB.append(String.format("%d\n",particles.size()));
        for (Particle p : particles) {
            dynamicSB.append(String.format("%d %f %f %f %f\n",
                    p.getID(),
                    p.getX(),
                    p.getY(),
                    p.getVelocityModule(),
                    p.getOmega()));
            staticSB.append(String.format("%f %f\n", p.getRadius(), p.getProperty()));
        }
        pwd.print(dynamicSB);
        pwd.close();
        pws.println(staticSB);
        pws.close();

    }
}
