import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CellIndexMethod {
    //N: cantidad de particulas
    private static int N = 10;
    //N_ITERATIONS: cantidad de iteraciones en el tiempo
    private static final int N_ITERATIONS = 1;
//    R: radio de la particula
    private static final double R= 0.37;
//    L: largo de una celda
    private static int L;
//    M: Cantidad de celdas del board cuadrado (MxM)
    private static final int M= 6;
//    RC: Radio de interaccion
    private static final int RC = 5;
    private static final String STATIC_FILE = "TP1/TP1_java/assets/Static100.txt";
    private static final String DYNAMIC_FILE = "TP1/TP1_java/assets/Dynamic100.txt";
    private static final String OUTPUT_FILE = "TP1/TP1_java/assets/Output.txt";

    public static void main(String[] args) throws IOException {

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

        String staticInputFilePath = cmd.getOptionValue("static-input");
        String dynamicInputFilePath = cmd.getOptionValue("dynamic-input");
        String outputFilePath = cmd.getOptionValue("output");


        List<Particle> particles = Parser.parseParticles(staticInputFilePath,dynamicInputFilePath);

        Map<Particle,List<Particle>> neighborsMap = new HashMap<>();

        for (Particle p: particles) {
            neighborsMap.put(p,particles.stream().filter(particle ->
                particle.checkIfNeighbor(p,RC)
            ).collect(Collectors.toList()));
        }

       Parser.generateOutput(neighborsMap,outputFilePath);

    }


      public static void setL(int l) {
        L = l;
    }

    public static void setN(int n) {
        N = n;
    }
}
