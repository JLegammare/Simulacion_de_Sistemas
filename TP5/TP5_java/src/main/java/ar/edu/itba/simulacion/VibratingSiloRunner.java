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
    final static int NUMBER_OF_PARTICLES = 1;
    final static double DT = 1E-3;
    final static double FINAL_T = 10.0;

    private static final String RESULTS_DIRECTORY = "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";

    public static void main(String[] args) throws IOException {
        ArrayList<Integer> wValues = new ArrayList<>(Arrays.asList(5, 10, 15, 20, 30, 50));
        for(Integer w : wValues) {

            List<Particle> particles = ParticleGenerator.generateRandomParticles(NUMBER_OF_PARTICLES, L, W);
            ResultsGenerator rg = new ResultsGenerator(DYNAMIC_FILE, STATIC_FILE, RESULTS_DIRECTORY);
            rg.fillStaticFile(particles);

            VibratingSilo.vibratingSilo(rg, particles, w);
        }
    }
}
