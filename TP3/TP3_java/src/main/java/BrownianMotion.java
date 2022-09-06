import models.Pair;
import models.Particle;
import org.apache.commons.cli.CommandLine;
import utils.Parser;
import utils.ParticleGenerator;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BrownianMotion {

    private static final int TOTAL_ITERATIONS = 20000;
    private static final double DEFAULT_PARTICLE_PROPERTY = 1;
    private static final String RESULTS_DIRECTORY =  "simulation_results";
    private static final String INPUTS_DIRECTORY =  "simulation_input_files";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    public static final int BIG_PARTICLE_VELOCITY = 0;
    public static final double BIG_PARTICLE_MASS = 0.9;
    public static final double BIG_PARTICLE_RADIUS = 0.7;
    public static final int NUMBER_OF_SMALL_PARTICLES = 120;
    public static final Double BOARD_LENGTH = 6.0;
    public static final double SMALL_PARTICLE_RADIUS = 0.2;
    public static final double SMALL_PARTICLE_MASS = 0.9;

    public static void main(String[] args) throws IOException {

//        TODO: fix Parser
//        Parser parser = new Parser();
//        CommandLine cmd = parser.parseArguments(args);
//        int np = Integer.parseInt(cmd.getOptionValue("particles_number"));
//        double r1 = Double.parseDouble(cmd.getOptionValue("small_particle_radius"));
//        double r2 = Double.parseDouble(cmd.getOptionValue("particle_radius_2"));
//        double m2 = Double.parseDouble(cmd.getOptionValue("particle_mass_2"));
//        double m1 = Double.parseDouble(cmd.getOptionValue("particle_mass_1"));
//        double l = Double.parseDouble(cmd.getOptionValue("length"));

        List<Particle> particles = ParticleGenerator.generateRandomParticles(
                NUMBER_OF_SMALL_PARTICLES,
                BOARD_LENGTH,
                0,
                SMALL_PARTICLE_RADIUS,
                DEFAULT_PARTICLE_PROPERTY,
                SMALL_PARTICLE_MASS
        );

        Optional<Integer> maxIdOp = particles.stream().map(Particle::getID).max(Comparator.comparingInt(a -> a));
        Pair<Double,Double> centerPosition = new Pair<>(BOARD_LENGTH/2,BOARD_LENGTH/2);

        if(maxIdOp.isPresent()){
            Particle bigParticle = new Particle(
                    maxIdOp.get()+1,
                    centerPosition.getX_value(),
                    centerPosition.getY_value(),
                    BIG_PARTICLE_RADIUS,
                    DEFAULT_PARTICLE_PROPERTY,
                    BIG_PARTICLE_VELOCITY,
                    0,
                    0,
                    BIG_PARTICLE_MASS);
            particles.add(bigParticle);
        }

        String dynamicResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, DYNAMIC_FILE);
        String staticResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, STATIC_FILE);

        BrownianMotionMethod();
    }

    private static void BrownianMotionMethod(){

    }

}
