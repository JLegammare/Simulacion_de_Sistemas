import models.Particle;
import org.apache.commons.cli.CommandLine;
import utils.Parser;
import utils.ParticleGenerator;

import java.io.IOException;
import java.util.List;

import static java.lang.Math.PI;

public class OffLatticeRunner {

    private static final String RESULTS_DIRECTORY = "simulation_results/noise_simulations";
    private static final String INPUTS_DIRECTORY = "simulation_input_files";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final double STEP = PI / 4;
    private static final int PARTICLE_RADIUS = 0;
    private static final double DEFAULT_PROPERTY = 1;
    public static final int NOISE_ITERATIONS = 8;

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        double l = Double.parseDouble(cmd.getOptionValue("length"));
        int np = Integer.parseInt(cmd.getOptionValue("particles_number"));
        double eta = Double.parseDouble(cmd.getOptionValue("noise"));
        double rc = Double.parseDouble(cmd.getOptionValue("i_radius"));
        double v = Double.parseDouble(cmd.getOptionValue("initial_speed"));


        ParticleGenerator.generateInputParticlesFiles(np,l,eta,PARTICLE_RADIUS,v,
                DEFAULT_PROPERTY,DYNAMIC_FILE,STATIC_FILE,INPUTS_DIRECTORY);


        String staticFilePath = String.format("%s/%s", INPUTS_DIRECTORY, STATIC_FILE);
        String dynamicFilePath = String.format("%s/%s", INPUTS_DIRECTORY, DYNAMIC_FILE);
        List<Particle> particles = parser.parseParticles(staticFilePath, dynamicFilePath, eta);


        for (int i = 0; i < NOISE_ITERATIONS; i++) {
            double eta_it = i * STEP;
            particles.forEach(p->p.updateDeltaOmega(eta_it));
            String vaOutputFilePath = String.format("%s/VaTime%d.txt", RESULTS_DIRECTORY, i);
            String dynamicResultsFilePath = String.format("%s/Dynamic%d.txt", RESULTS_DIRECTORY, i);
            String staticResultsFilePath = String.format("%s/Static%d.txt", RESULTS_DIRECTORY, i);
            OffLattice.OffLatticeMethod(
                    eta_it,
                    l,
                    rc,
                    RESULTS_DIRECTORY,
                    staticResultsFilePath,
                    dynamicResultsFilePath,
                    vaOutputFilePath,
                    particles);
        }


    }

}
