import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;

public class Parser {

    private double maxRadius = 0;

    private final double n = Math.PI;


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

        Option n = new Option("n", "noise", true, "noise parameter of the simulation");
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
        String dynamicFilePath = cmd.getOptionValue("dynamic-input");
        File file = new File(dynamicFilePath);
        if(file.exists()){
            file.delete();
        }

        return cmd;
    }

    public double getMaxRadius() {
        return maxRadius;
    }
}
