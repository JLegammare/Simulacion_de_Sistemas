package ar.edu.itba.simulacion.utils;

import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

//    public static void generateFiles(List<Particle> particles,
//                                     String dynamicFileName,
//                                     String staticFileName,
//                                     String directoryPath) throws IOException {
//
//        File directory = new File(directoryPath);
//        directory.mkdir();
//
//        File dymFile = new File(String.format("%s/%s", directoryPath, dynamicFileName));
//        if (dymFile.exists())
//            dymFile.delete();
//
//        File stcFile = new File(String.format("%s/%s", directoryPath, staticFileName));
//        if (stcFile.exists())
//            stcFile.delete();
//
//        FileWriter fwd = new FileWriter(dymFile, false);
//        BufferedWriter bwd = new BufferedWriter(fwd);
//        PrintWriter pwd = new PrintWriter(bwd);
//
//        FileWriter fws = new FileWriter(stcFile, false);
//        BufferedWriter bws = new BufferedWriter(fws);
//        PrintWriter pws = new PrintWriter(bws);
//
//        StringBuilder staticSB = new StringBuilder();
//        StringBuilder dynamicSB = new StringBuilder();
//
//        dynamicSB.append("0\n");
//        staticSB.append(String.format("%d\n",particles.size()));
//        for (Particle p : particles) {
//            dynamicSB.append(String.format("%d %f %f %f %f\n",
//                    p.getID(),
//                    p.getPosition(),
//                    p.getY(),
//                    p.getVelocityModule(),
//                    p.getOmega()));
//            staticSB.append(String.format("%f %f\n", p.getRadius(), p.getProperty()));
//        }
//        pwd.print(dynamicSB);
//        pwd.close();
//        pws.println(staticSB);
//        pws.close();
//
//    }
}
