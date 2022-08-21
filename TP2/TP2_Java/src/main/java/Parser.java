import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;

public class Parser {

    private double maxRadius = 0;

    private final double n = Math.PI;

    public List<Particle> parseParticles(String staticFilePath, String dynamicFilePath,String outputFile) throws IOException {

        File staticFile = new File(staticFilePath);
        File dynamicFile = new File(dynamicFilePath);

        Scanner staticScanner = new Scanner(staticFile);
        Scanner dynamicScanner = new Scanner(dynamicFile);

        int N = Integer.parseInt(staticScanner.next());
        int L = Integer.parseInt(staticScanner.next());

        //skipping t0
        dynamicScanner.nextInt();

        List<Particle> particles = new ArrayList<>();

        for (int j = 0; staticScanner.hasNextLine() && dynamicScanner.hasNextLine(); j++) {
            double radius = staticScanner.nextDouble();
            double property = staticScanner.nextDouble();
            double posX = dynamicScanner.nextDouble();
            double posY = dynamicScanner.nextDouble();
            double velocity = dynamicScanner.nextDouble();
            double omega = dynamicScanner.nextDouble();
            omega = Math.random()*2*Math.PI;
            double deltaOmega = Math.random()*n-n/2;

            if(radius > maxRadius)
                maxRadius = radius;
            particles.add(new Particle(j,posX,posY,radius,property,velocity,omega,deltaOmega));
        }

        File file = new File(outputFile);
        if(file.exists()){
            file.delete();
        }

        return particles;
    }

    public void generateOutput(Map<Particle,Set<Particle>> neighborhoods, String path) throws IOException {

        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file,false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);


        neighborhoods.forEach((k,v)->{
            StringBuilder sb = new StringBuilder();
            sb.append(k.getID());
            for (Particle p:v) {
                sb.append(String.format(" %d",p.getID()));
            }
            pw.println(sb);
        });
        pw.close();
    }

    public void addIterationToOutput(int tn, List<Particle> particles, String path) throws IOException {

        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\na\n",400));
        for (Particle p: particles) {
            sb.append(String.format("%d %f %f %f %f %f\n",p.getID(), p.getX(),p.getY(), 0f,p.getVelocityModule(),p.getOmega()));
        }
        pw.print(sb);

        pw.close();
    }

    public CommandLine parseArguments(String[] args){
        Options options = new Options();

        Option dynamicInput = new Option("d", "dynamic-input", true, "dynamic input file path");
        dynamicInput.setRequired(true);
        options.addOption(dynamicInput);

        Option staticInput = new Option("s", "static-input", true, "static input file path");
        staticInput.setRequired(true);
        options.addOption(staticInput);

        Option output = new Option("o", "output", true, "output file path");
        output.setRequired(true);
        options.addOption(output);

        Option n = new Option("n", "n_particles", true, "number of particles");
        n.setRequired(true);
        options.addOption(n);

        Option l = new Option("l", "length", true, "length of the cells");
        l.setRequired(true);
        options.addOption(l);

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

    public double getMaxRadius() {
        return maxRadius;
    }
}
