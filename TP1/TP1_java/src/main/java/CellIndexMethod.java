import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CellIndexMethod {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        String staticInputFilePath = cmd.getOptionValue("static-input");
        String dynamicInputFilePath = cmd.getOptionValue("dynamic-input");
        String outputFilePath = cmd.getOptionValue("output");

        int n = Integer.parseInt(cmd.getOptionValue("n_particles"));
        int l = Integer.parseInt(cmd.getOptionValue("length"));
        double rc = Float.parseFloat(cmd.getOptionValue("i_radius"));

        boolean periodicCondition = true;

        List<Particle> particles = parser.parseParticles(staticInputFilePath, dynamicInputFilePath);

        if (particles.size() != n) {
            //TODO: Throw exception and show a message
            System.exit(1);
        }

        int m = (int) (l/(rc + 2*parser.getMaxRadius()) + 1);
        if((double)l/m <= rc ){
           System.exit(1);
        }

        Board board = new Board(m, l, periodicCondition);

        board.addParticlesToBoard(particles);

        Map<Particle, Set<Particle>> neighborhoods = board.getAllNeighbors(rc);
        parser.generateOutput(neighborhoods, outputFilePath);
    }


}
