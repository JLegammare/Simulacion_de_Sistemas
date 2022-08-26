import models.Particle;
import org.apache.commons.cli.CommandLine;
import utils.Parser;

import java.io.IOException;
import java.util.List;

import static java.lang.Math.PI;

public class OffLatticeRunner {

    private static final String RESULTS_DIRECTORY = "simulation_results/Eta_simulations";
    private static final String INPUTS_DIRECTORY = "simulation_input_files";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final double STEP = PI / 4;

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        double l = Double.parseDouble(cmd.getOptionValue("length"));
        double eta = Double.parseDouble(cmd.getOptionValue("noise"));
        double rc = Double.parseDouble(cmd.getOptionValue("i_radius"));

        String staticFilePath = String.format("%s/%s", INPUTS_DIRECTORY, STATIC_FILE);
        String dynamicFilePath = String.format("%s/%s", INPUTS_DIRECTORY, DYNAMIC_FILE);

        List<Particle> particles = parser.parseParticles(staticFilePath, dynamicFilePath, eta);

        for (int i = 0; i < 8; i++) {
            double eta_it = i * STEP;
            String vaOutputFilePath = String.format("%s/VaTime%d.txt", RESULTS_DIRECTORY, i);
            String dynamicResultsFilePath = String.format("%s/Dynamic%d.txt", RESULTS_DIRECTORY, i);
            String staticResultsFilePath = String.format("%s/Static%d.txt", RESULTS_DIRECTORY, i);
            OffLattice.OffLatticeMethod(eta_it, l, rc, RESULTS_DIRECTORY,staticResultsFilePath, dynamicResultsFilePath, vaOutputFilePath, particles);
        }


    }

}
