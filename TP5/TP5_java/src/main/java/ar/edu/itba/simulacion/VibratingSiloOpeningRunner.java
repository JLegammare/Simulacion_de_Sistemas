package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Particle;
import ar.edu.itba.simulacion.utils.ParticleGenerator;
import ar.edu.itba.simulacion.utils.ResultsGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VibratingSiloOpeningRunner {

    final static double W = 20.0;
    final static double L = 70.0;
    final static int NUMBER_OF_PARTICLES = 200;
    final static double w = 5;

    private static final String RESULTS_DIRECTORY = "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final String EXIT_FILE = "Times.txt";
    public static void main(String[] args) throws IOException {
        ArrayList<Double> dValues = new ArrayList<>(Arrays.asList(3.0, 4.0, 5.0, 6.0));
        List<Particle> particles = ParticleGenerator.parseParticles("simulation_results/Static.txt");
        List<Particle> auxParticles = new ArrayList<>();

        for(Particle p : particles){
            auxParticles.add(new Particle(p.getID(), p.getPosition(), p.getVelocity(), p.getRadius(), p.getMass(),p.getParticleColor()));
        }

        for(Double d : dValues) {
            String directory = String.format("%s/%f",RESULTS_DIRECTORY, w);
            ResultsGenerator rg = new ResultsGenerator(DYNAMIC_FILE, STATIC_FILE,EXIT_FILE, directory);
            rg.fillStaticFile(particles);
            VibratingSilo.vibratingSilo(rg, particles, w,d);
            particles.clear();
            for(Particle p : auxParticles){
                particles.add(new Particle(p.getID(), p.getPosition(), p.getVelocity(), p.getRadius(), p.getMass(),p.getParticleColor()));
            }

        }
    }
}
