package ar.edu.itba.simulacion.models.utils;

import ar.edu.itba.simulacion.models.Particle;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {

    public CommandLine parseArguments(String[] args){
        Options options = new Options();

        Option n = new Option("n", "noise", true, "noise parameter of the simulation");
        n.setRequired(true);
        options.addOption(n);

        Option l = new Option("l", "length", true, "length of the cells");
        l.setRequired(true);
        options.addOption(l);

        Option np = new Option(
                "np",
                "particles_number",
                true,
                "number of particles to be created");
        np.setRequired(false);
        options.addOption(np);

        Option v = new Option(
                "v",
                "initial_speed",
                true,
                "uniform initial speed for all particles");
        v.setRequired(false);
        options.addOption(v);

        Option r = new Option(
                "r",
                "particle_radius",
                false,
                "particle radius for all particles");
        r.setRequired(false);
        options.addOption(r);

        Option rc = new Option("rc", "i_radius", true, "particle interaction radius");
        rc.setRequired(true);
        options.addOption(rc);

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
                double speed = dynamicScanner.nextDouble();
                double omega = dynamicScanner.nextDouble();

                double noise = Math.random()*eta-eta/2;

                particles.add(new Particle(id,posX,posY,radius,property,speed,omega,noise));
            }

        }
            return particles;
    }

}
