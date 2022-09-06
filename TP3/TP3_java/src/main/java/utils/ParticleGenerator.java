package utils;

import models.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator {


    public static void generateInputParticlesFiles(int numberOfParticles,
                                                   double l,
                                                   double n,
                                                   double particleRadius,
                                                   double property,
                                                   double mass,
                                                   String dynamicFileName,
                                                   String staticFileName,
                                                   String directoryPath) throws IOException {

        List<Particle> particles= generateRandomParticles(
                numberOfParticles,
                l,
                n,
                particleRadius,
                property,mass);

        generateFiles(particles,dynamicFileName,staticFileName,directoryPath);
    }

    public static List<Particle> generateRandomParticles(int numberOfParticles,
                                                         double l,
                                                         double n,
                                                         double particleRadius,
                                                         double property,
                                                         double mass
    ) {
        List<Particle> particles = new ArrayList<>();
        int particleId = 0;
        while (particles.size() < numberOfParticles){
            double x = Math.random() * l;
            double y = Math.random() * l;
            double initialSpeed = Math.random()*2;
            double omega = Math.random() * 2* Math.PI;
            if(particleSeparated(x, y, particleRadius, particles)) {
                particles.add(new Particle(particleId++, x, y, particleRadius,property,initialSpeed,omega,mass));
            }
        }

        return particles;

    }

    private static boolean particleSeparated(double x, double y, double particleRadius, List<Particle> particles) {
        for (Particle particle: particles) {
            if(Math.pow(x - particle.getX(), 2) + Math.pow(y - particle.getY(), 2) <=
                    Math.pow(particleRadius + particle.getRadius(), 2)) {
                return false;
            }
        }
        return true;
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
