import models.Board;
import models.Particle;
import org.apache.commons.cli.CommandLine;
import utils.Parser;
import utils.ParticleGenerator;
import utils.ResultsGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.lang.Math.*;

public class OffLattice {

    private static final int DEFAULT_M = 4;
    private static final int TOTAL_ITERATIONS = 1000;
    private static final Boolean PERIODIC_CONDITION = true;
    private static final double DEFAULT_PARTICLE_RADIUS = 0.01;
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
//        TODO: fix NullPointerException
//        double rc = Double.parseDouble(cmd.getOptionValue("i_radius"));
        double rc = 1f;

        String staticFilePath = String.format("%s/%s",INPUTS_DIRECTORY,STATIC_FILE);
        String dynamicFilePath = String.format("%s/%s",INPUTS_DIRECTORY,DYNAMIC_FILE);

        List<Particle> particles = parser.parseParticles(staticFilePath,dynamicFilePath,eta);

        for (int i = 0 ; i < 8 ; i++) {
            double eta_it = i * PI/4;
            String vaOutputFilePath = String.format("%s/VaTime%d.txt",RESULTS_DIRECTORY,i);
            String dynamicResultsFilePath = String.format("%s/Dynamic%d.txt",RESULTS_DIRECTORY,i);
            String staticResultsFilePath = String.format("%s/Static%d.txt",RESULTS_DIRECTORY,i);
            OffLatticeMethod(eta_it, l, rc, staticResultsFilePath, dynamicResultsFilePath, vaOutputFilePath, particles);
            i++;
        }
    }

    private static void OffLatticeMethod(double eta,
                                         double l,
                                         double rc,
                                         String staticFilePath,
                                         String dynamicFilePath,
                                         String vaFilePath,
                                         List<Particle> particles) throws IOException {

        int m = (int) (l / rc + 2 * DEFAULT_PARTICLE_RADIUS);
        Board board = new Board(m, l, PERIODIC_CONDITION,rc);
        board.addParticlesToBoard(particles);

        ResultsGenerator rg = new ResultsGenerator(dynamicFilePath,vaFilePath,staticFilePath,RESULTS_DIRECTORY);
        rg.fillStaticFile(particles,l);
        rg.addStateToDynamicFile(particles,0);

        Map<Integer,Double> orderParameterMap= new TreeMap<>();

        for (int i = 1; i < TOTAL_ITERATIONS; i++) {
            Map<Particle, Set<Particle>> neighborhoods = board.getAllNeighbors(rc);
            double va = calculateOrderParameter(particles, DEFAULT_INITIAL_SPEED);
            orderParameterMap.put(i,va);
            particles.forEach(p -> tempEvolution(p, neighborhoods.get(p), l, DEFAULT_M,eta,DEFAULT_DT));
            board.addParticlesToBoard(particles);
            rg.addStateToDynamicFile(particles,i);
        }

        rg.generateVaTimeFile(orderParameterMap);

    }

    private static double calculateOrderParameter(List<Particle> particles, double v) {

        double vx = particles.stream().mapToDouble(Particle::getXVelocity).sum();
        double vy = particles.stream().mapToDouble(Particle::getYVelocity).sum();
        double norm = sqrt(pow(vx, 2) + pow(vy, 2));

        return norm / (particles.size() * v);

    }


    private static void tempEvolution(Particle particle, Set<Particle> neighbors, double l, int m, double n, int dt) {
        setNewOmega(particle,neighbors);
        checkPeriodicMovement(particle,m,l,dt);
        particle.updateDeltaOmega(n);
    }

    private static void checkPeriodicMovement(Particle particle, int m, double l, int dt){

        double newXposition = particle.getX() + particle.getXVelocity()*dt;
        double newYposition = particle.getY() + particle.getYVelocity()*dt;

        double boardLength = l;
        //si se va por la derecha
        if (newXposition > boardLength) {
            particle.setX(newXposition - boardLength);
        }//si se va por izquierda
        else if (newXposition < 0)
            particle.setX(boardLength + newXposition);
        else
            particle.setX(newXposition);
        //si se va por arriba
        if (newYposition > boardLength) {
            particle.setY(newYposition - boardLength);
        }//si se va por abajo
        else if (newYposition < 0)
            particle.setY(boardLength + newYposition);
        else
            particle.setY(newYposition);
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
