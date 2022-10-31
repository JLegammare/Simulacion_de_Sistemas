package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;
import ar.edu.itba.simulacion.utils.ParticleGenerator;
import ar.edu.itba.simulacion.utils.ResultsGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VibratingSiloRunner {

    final static double W = 20.0;
    final static double L = 70.0;
    final static int NUMBER_OF_PARTICLES = 50;
    final static double D = 3;

    private static final String RESULTS_DIRECTORY = "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final String EXIT_FILE = "Times.txt";

    public static void main(String[] args) throws IOException {
        ArrayList<Double> wValues = new ArrayList<>(Arrays.asList(5.0, 10.0, 15.0, 20.0, 30.0, 50.0));
        List<Particle> particles = ParticleGenerator.generateRandomParticles(NUMBER_OF_PARTICLES, L, W);

        for(Double w : wValues) {
            String directory = String.format("%s/%f",RESULTS_DIRECTORY, w);
            ResultsGenerator rg = new ResultsGenerator(DYNAMIC_FILE, STATIC_FILE,EXIT_FILE, directory);
            rg.fillStaticFile(particles);
            VibratingSilo.vibratingSilo(rg, particles, w,D);
        }
    }
}
