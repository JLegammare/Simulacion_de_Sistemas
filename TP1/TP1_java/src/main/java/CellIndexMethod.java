import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CellIndexMethod {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        String staticInputFilePath = cmd.getOptionValue("static-input");
        String dynamicInputFilePath = cmd.getOptionValue("dynamic-input");
        String outputFilePath = cmd.getOptionValue("output");
        int n = Integer.parseInt(cmd.getOptionValue("n_particles"));
        int l = Integer.parseInt(cmd.getOptionValue("length"));
        int rc = Integer.parseInt(cmd.getOptionValue("i_radius"));

        List<Particle> particles = parser.parseParticles(staticInputFilePath,dynamicInputFilePath);

        if (particles.size() != n){
            //TODO: Throw exception and show a message
            System.exit(1);
        }

        Map<Particle,List<Particle>> neighborsMap = new HashMap<>();

        for (Particle p: particles) {
            neighborsMap.put(p,particles.stream().filter(particle ->
                particle.checkIfNeighbor(p,rc)
            ).collect(Collectors.toList()));
        }

       parser.generateOutput(neighborsMap,outputFilePath);

    }

}
