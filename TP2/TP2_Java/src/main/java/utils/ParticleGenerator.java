package utils;

import models.Particle;
import org.apache.commons.cli.CommandLine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator {

    private static final String GENERATION_DIRECTORY = "simulation_input_files";
    private static final String STATIC_FILE_NAME = "Static.txt";
    private static final String DYNAMIC_FILE_NAME = "Dynamic.txt";
    private static final int PARTICLE_RADIUS = 0;
    private static final double DEFAULT_PROPERTY = 1;
    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        int np = Integer.parseInt(cmd.getOptionValue("particles_number"));
        double l = Double.parseDouble(cmd.getOptionValue("length"));
        double eta = Double.parseDouble(cmd.getOptionValue("noise"));
        double v = Double.parseDouble(cmd.getOptionValue("initial_speed"));

        List<Particle> particles = generateRandomParticles(np,l,eta,PARTICLE_RADIUS,v,DEFAULT_PROPERTY);
        generateFiles(particles);

    }

    public static List<Particle> generateRandomParticles(int numberOfParticles,
                                                         double l,
                                                         double n,
                                                         double particleRadius,
                                                         double initialSpeed,
                                                         double property
    ) {
        double boardSideLength = l;
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < numberOfParticles; i++) {

            double x = Math.random() * boardSideLength;
            double y = Math.random() * boardSideLength;
            double omega = Math.random() * 2* Math.PI;
            double deltaOmega = Math.random()*n-n/2;

            particles.add(new Particle(i, x, y, particleRadius,property,initialSpeed,omega,deltaOmega));

        }

        return particles;

    }

    public static void generateFiles(List<Particle> particles ) throws IOException {

        File directory = new File(GENERATION_DIRECTORY);
        directory.mkdir();

        File dymFile = new File(String.format("%s/%s", GENERATION_DIRECTORY, DYNAMIC_FILE_NAME));
        if (dymFile.exists())
            dymFile.delete();

        File stcFile = new File(String.format("%s/%s", GENERATION_DIRECTORY, STATIC_FILE_NAME));
        if (stcFile.exists())
            stcFile.delete();

        FileWriter fwd = new FileWriter(stcFile, false);
        BufferedWriter bwd = new BufferedWriter(fwd);
        PrintWriter pwd = new PrintWriter(bwd);

        FileWriter fws = new FileWriter(dymFile, false);
        BufferedWriter bws = new BufferedWriter(fws);
        PrintWriter pws = new PrintWriter(bws);

        StringBuilder staticSB = new StringBuilder();
        StringBuilder dynamicSB = new StringBuilder();

        dynamicSB.append("0\n");
        staticSB.append(String.format("%d\n",particles.size()));
        for (Particle p : particles) {
            dynamicSB.append(String.format("%d %f %f %f %f %f %f\n",
                    p.getID(),
                    p.getX(),
                    p.getY(),
                    0f,
                    p.getVelocityModule(),
                    p.getXVelocity(),
                    p.getYVelocity()));
            staticSB.append(String.format("%f %f\n", p.getRadius(), p.getProperty()));
        }
        pwd.print(dynamicSB);
        pwd.close();
        pws.println(staticSB);
        pws.close();

    }
}
