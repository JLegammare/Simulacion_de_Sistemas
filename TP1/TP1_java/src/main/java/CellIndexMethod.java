import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        int m = Integer.parseInt(cmd.getOptionValue("m_cells"));

        boolean periodicCondition = false;

        List<Particle> particles = parser.parseParticles(staticInputFilePath, dynamicInputFilePath, l, m);

        if (particles.size() != n) {
            //TODO: Throw exception and show a message
            System.exit(1);
        }

        Board board = new Board(m, l, periodicCondition);

        board.addParticlesToBoard(particles);

        Map<Particle, List<Particle>> neighborhoods = board.getAllNeighbors(particles, rc);
        parser.generateOutput(neighborhoods, outputFilePath);
    }


}
