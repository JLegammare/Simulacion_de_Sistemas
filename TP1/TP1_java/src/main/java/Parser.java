import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {

    public List<Particle> parseParticles(String staticFilePath, String dynamicFilePath, int l, int m) throws IOException {

        File staticFile = new File(staticFilePath);
        File dynamicFile = new File(dynamicFilePath);

        Scanner staticScanner = new Scanner(staticFile);
        Scanner dynamicScanner = new Scanner(dynamicFile);

        int N = Integer.parseInt(staticScanner.next());
        int L = Integer.parseInt(staticScanner.next());
        ArrayList<Integer> times= new ArrayList<>();
        times.add(dynamicScanner.nextInt());
        List<Particle> particles = new ArrayList<>();

        for (int j = 0; staticScanner.hasNextLine() && dynamicScanner.hasNextLine(); j++) {
            float radius = staticScanner.nextFloat();
            float property = staticScanner.nextFloat();
            float posX = dynamicScanner.nextFloat();
            float posY = dynamicScanner.nextFloat();
            particles.add(new Particle(j,posX,posY,radius,property));
        }

        return particles;
    }

    public void generateOutput(Map<Particle,List<Particle>> neighborhoods, String path) throws IOException {

        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file,false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);


        neighborhoods.forEach((k,v)->{
            StringBuilder sb = new StringBuilder();
            sb.append(k.getId());
            for (Particle p:v) {
                sb.append(String.format(" %d",p.getId()));
            }
            pw.println(sb);
        });
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

        Option m = new Option("m", "m_cells", true, "number of cells");
        m.setRequired(true);
        options.addOption(m);

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


}
