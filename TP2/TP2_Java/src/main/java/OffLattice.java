import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;

public class OffLattice {

    private static int DEFAULT_M = 4;
    private static final int TOTAL_ITERATIONS = 25000;
    private static final Boolean PERIODIC_CONDITION = true;
    private static final double DEFAULT_PARTICLE_RADIUS = 0.01;
    private static final double DEFAULT_INITIAL_SPEED = 0.03;
    private static final int NUMBER_OF_PARTICLES = 400;


    public static void main(String[] args) throws IOException {

        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        String staticFilePath = cmd.getOptionValue("static-file");
        String dynamicFilePath = cmd.getOptionValue("dynamic-file");
        String vaFilePath = cmd.getOptionValue("va-file");

        double n = Double.parseDouble(cmd.getOptionValue("noise"));
        double l = Double.parseDouble(cmd.getOptionValue("length"));
        double rc = Double.parseDouble(cmd.getOptionValue("i_radius"));

        List<Particle> particles = ParticleGenerator.generateRandomParticles(NUMBER_OF_PARTICLES, DEFAULT_M, l, n, DEFAULT_PARTICLE_RADIUS, DEFAULT_INITIAL_SPEED);
        Board board = new Board(DEFAULT_M, l, PERIODIC_CONDITION,rc);
        board.addParticlesToBoard(particles);

        ResultsGenerator rg = new ResultsGenerator(dynamicFilePath,vaFilePath,staticFilePath);
        rg.fillStaticFile(particles,l);
        rg.addStateToDynamicFile(particles);

        Map<Integer,Double> orderParameterMap= new TreeMap<>();

        for (int i = 1; i < TOTAL_ITERATIONS; i++) {
            Map<Particle, Set<Particle>> neighborhoods = board.getAllNeighbors(rc);
            double va = calculateOrderParameter(particles, DEFAULT_INITIAL_SPEED);
            orderParameterMap.put(i,va);
            particles.forEach(p -> tempEvolution(p, neighborhoods.get(p), l, DEFAULT_M,n));
            board.addParticlesToBoard(particles);
            rg.addStateToDynamicFile(particles);
        }

        rg.generateVaTimeFile(orderParameterMap);

    }

    private static double calculateOrderParameter(List<Particle> particles, double v) {

        double vx = particles.stream().mapToDouble(Particle::getXVelocity).sum();
        double vy = particles.stream().mapToDouble(Particle::getYVelocity).sum();
        double norm = sqrt(pow(vx, 2) + pow(vy, 2));

        return norm / (particles.size() * v);

    }


    private static void tempEvolution(Particle particle, Set<Particle> neighbors, double l, int m, double n) {
        setNewOmega(particle,neighbors);
        checkPeriodicMovement(particle,m,l);
        particle.updateDeltaOmega(n);

    }

    private static void checkPeriodicMovement(Particle particle, int m, double l){

        double newXposition = particle.getX() + particle.getXVelocity();
        double newYposition = particle.getY() + particle.getYVelocity();

        double boardLength = m * l;
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
            sinAvg += sin(particle.getOmega());
            cosAvg += cos(particle.getOmega());

            sinAvg = sinAvg / (neighbors.size() + 1);
            cosAvg = cosAvg / (neighbors.size() + 1);

            omegaAvg = atan2(sinAvg, cosAvg);
            particle.setOmega(omegaAvg + particle.getDeltaOmega());
        }
    }


}
