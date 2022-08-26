package utils;

import models.Particle;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.asin;

public class Parser {

    public CommandLine parseArguments(String[] args){
        Options options = new Options();

        Option n = new Option("n", "noise", true, "noise parameter of the simulation");
        n.setRequired(true);
        options.addOption(n);

        Option l = new Option("l", "length", true, "length of the cells");
        l.setRequired(true);
        options.addOption(l);

        Option rc = new Option(
                "rc",
                "i_radius",
                false,
                "particle interaction radius");
        rc.setRequired(true);
        options.addOption(rc);

        Option np = new Option(
                "np",
                "particles_number",
                true,
                "number of particles to be created");
        rc.setRequired(true);
        options.addOption(np);

        Option v = new Option(
                "v",
                "initial_speed",
                true,
                "uniform initial speed for all particles");
        rc.setRequired(true);
        options.addOption(v);

        Option r = new Option(
                "r",
                "particle_radius",
                false,
                "particle radius for all particles");
        rc.setRequired(true);
        options.addOption(r);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try{
            cmd = parser.parse(options,args);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name",options);
            throw new RuntimeException(e);
        }

        return cmd;
    }


    public List<Particle> parseParticles(String staticFilePath, String dynamicFilePath, double eta) throws IOException {

        File staticFile = new File(staticFilePath);
        File dynamicFile = new File(dynamicFilePath);

        Scanner staticScanner = new Scanner(staticFile);
        Scanner dynamicScanner = new Scanner(dynamicFile);

        int n = Integer.parseInt(staticScanner.next());
        int t0 = Integer.parseInt(dynamicScanner.next());

        List<Particle> particles = new ArrayList<>();

        for (int i = 0; i < n ; i++) {

            if( staticScanner.hasNextLine() && dynamicScanner.hasNextLine()){
                double radius = staticScanner.nextDouble();
                double property = staticScanner.nextDouble();
                int id = dynamicScanner.nextInt();
                double posX = dynamicScanner.nextDouble();
                double posY = dynamicScanner.nextDouble();
                double posZ = dynamicScanner.nextDouble();
                double speed = dynamicScanner.nextDouble();
                double vX = dynamicScanner.nextDouble();
                double vY = dynamicScanner.nextDouble();

                double omega = calculateOmega(vX,speed);
                double noise = Math.random()*eta-eta/2;
                particles.add(new Particle(id,posX,posY,radius,property,speed,omega,noise));
            }

        }
            return particles;
    }


    private double calculateOmega(double vx,double speed){
        return asin(vx/speed);
    }

}
