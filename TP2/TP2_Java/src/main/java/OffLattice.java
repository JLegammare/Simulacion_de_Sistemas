import ar.edu.itba.simulacion.models.Board;
import ar.edu.itba.simulacion.models.Particle;
import org.apache.commons.cli.CommandLine;
import ar.edu.itba.simulacion.models.utils.Parser;
import ar.edu.itba.simulacion.models.utils.ParticleGenerator;
import ar.edu.itba.simulacion.models.utils.ResultsGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.lang.Math.*;

public class OffLattice {

    private static final int TOTAL_ITERATIONS = 20000;
    private static final Boolean PERIODIC_CONDITION = true;
    private static final double DEFAULT_PARTICLE_RADIUS = 0;
    private static final double DEFAULT_PARTICLE_PROPERTY = 1;
    private static final double DEFAULT_INITIAL_SPEED = 0.03;
    private static final int DEFAULT_DT = 1;
    private static final String RESULTS_DIRECTORY =  "simulation_results";
    private static final String INPUTS_DIRECTORY =  "simulation_input_files";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final String VA_TIME_FILE = "VaTime.txt";

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        double eta = Double.parseDouble(cmd.getOptionValue("noise"));
        double l = Double.parseDouble(cmd.getOptionValue("length"));
        double rc = Double.parseDouble(cmd.getOptionValue("i_radius"));
        int np = Integer.parseInt(cmd.getOptionValue("particles_number"));

        String staticFilePath = String.format("%s/%s", INPUTS_DIRECTORY, STATIC_FILE);
        String dynamicFilePath = String.format("%s/%s", INPUTS_DIRECTORY, DYNAMIC_FILE);

        ParticleGenerator.generateInputParticlesFiles(np,l,eta,DEFAULT_PARTICLE_RADIUS,DEFAULT_INITIAL_SPEED,DEFAULT_PARTICLE_PROPERTY,DYNAMIC_FILE,STATIC_FILE,INPUTS_DIRECTORY);
        List<Particle> particles = parser.parseParticles(staticFilePath, dynamicFilePath, eta);

        String vaOutputFilePath = String.format("%s/%s", RESULTS_DIRECTORY, VA_TIME_FILE);
        String dynamicResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, DYNAMIC_FILE);
        String staticResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, STATIC_FILE);

        OffLatticeMethod(eta, l, rc,RESULTS_DIRECTORY, staticResultsFilePath, dynamicResultsFilePath, vaOutputFilePath, particles);
    }

    public static void OffLatticeMethod(double eta,
                                        double l,
                                        double rc,
                                        String resultsDirectoryPath,
                                        String staticFilePath,
                                        String dynamicFilePath,
                                        String vaFilePath,
                                        List<Particle> particles) throws IOException {

        Board board = new Board(l, PERIODIC_CONDITION,rc,DEFAULT_PARTICLE_RADIUS);
        board.addParticlesToBoard(particles);

        ResultsGenerator rg = new ResultsGenerator(dynamicFilePath,vaFilePath,staticFilePath,resultsDirectoryPath);
        rg.fillStaticFile(particles,l);
        rg.addStateToDynamicFile(particles,0);

        Map<Integer,Double> orderParameterMap= new TreeMap<>();

        orderParameterMap.put(0,calculateOrderParameter(particles,DEFAULT_INITIAL_SPEED));

        for (int i = 1; exitCondition(i); i++) {
            Map<Particle, Set<Particle>> neighborhoods = board.getAllNeighbors(rc);
            double va = calculateOrderParameter(particles, DEFAULT_INITIAL_SPEED);
            orderParameterMap.put(i,va);
            particles.forEach(p -> tempEvolution(p, neighborhoods.get(p), l,eta,DEFAULT_DT));
            board.addParticlesToBoard(particles);
            rg.addStateToDynamicFile(particles,i);
        }

        rg.generateVaTimeFile(orderParameterMap);

    }

    private static boolean exitCondition(int i) {

        return i < TOTAL_ITERATIONS;

    }

    private static double calculateOrderParameter(List<Particle> particles, double v) {

        double vx = particles.stream().mapToDouble(Particle::getXVelocity).sum();
        double vy = particles.stream().mapToDouble(Particle::getYVelocity).sum();
        double norm = sqrt(pow(vx, 2) + pow(vy, 2));

        return norm / (particles.size() * v);

    }


    private static void tempEvolution(Particle particle, Set<Particle> neighbors, double l, double n, int dt) {
        setNewOmega(particle,neighbors);
        checkPeriodicMovement(particle,l,dt);
        particle.updateDeltaOmega(n);
    }

    private static void checkPeriodicMovement(Particle particle,double l, int dt){

        double newXvalue = particle.getX() + particle.getXVelocity()*dt;
        double newYvalue = particle.getY() + particle.getYVelocity()*dt;

        particle.updatePosition(newXvalue,newYvalue,l);

    }

    private static void setNewOmega(Particle particle,Set<Particle> neighbors ){
        double sinAvg = 0;
        double cosAvg = 0;
        double omegaAvg;

        if (!neighbors.isEmpty()) {
            for (Particle p : neighbors) {
                sinAvg += sin(p.getOmega());
                cosAvg += cos(p.getOmega());
            }
        }

        sinAvg += sin(particle.getOmega());
        cosAvg += cos(particle.getOmega());

        sinAvg = sinAvg / (neighbors.size() + 1);
        cosAvg = cosAvg / (neighbors.size() + 1);

        omegaAvg = atan2(sinAvg, cosAvg);
        particle.setOmega(omegaAvg + particle.getDeltaOmega());
    }

}
