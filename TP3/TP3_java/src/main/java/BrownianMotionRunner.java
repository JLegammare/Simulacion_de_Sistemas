import models.Pair;
import models.Particle;
import utils.ParticleGenerator;
import utils.ResultsGenerator;

import java.awt.*;
import java.io.IOException;
import java.util.List;


public class BrownianMotionRunner {

    private static final double DEFAULT_PARTICLE_PROPERTY = 1;
    private static final String RESULTS_DIRECTORY =  "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String PARTICLE_FILE = "BigParticlePosition.txt";
    private static final String COLLISION_FILE = "CollisionsTime.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final int NUMBER_OF_SMALL_PARTICLES = 100;
    private static final Double BOARD_LENGTH = 6.0;
    private static final int BIG_PARTICLE_VELOCITY = 0;
    private static final double BIG_PARTICLE_MASS = 2;
    private static final double BIG_PARTICLE_RADIUS = 0.7;
    private static final Color bigParticleColor = Color.RED;
    private static final Color smallParticlesColor = Color.BLUE;
    private static final double SMALL_PARTICLE_RADIUS = 0.2;
    private static final double SMALL_PARTICLE_MASS = 0.9;

    public static void main(String[] args) throws IOException {

        Pair<Double,Double> centerPosition = new Pair<>(BOARD_LENGTH/2,BOARD_LENGTH/2);



        for(int i = 1; i < 5;i++){

            int nParts = NUMBER_OF_SMALL_PARTICLES + i*10;
            String staticFilePath = String.format("%s/%d/%s", RESULTS_DIRECTORY,nParts, STATIC_FILE);
            String dynamicFilePath = String.format("%s/%d/%s", RESULTS_DIRECTORY,nParts, DYNAMIC_FILE);
            String particleResultsFilePath = String.format("%s/%d/%s", RESULTS_DIRECTORY,nParts, PARTICLE_FILE);
            String collisionResultsFilePath = String.format("%s/%d/%s", RESULTS_DIRECTORY,nParts, COLLISION_FILE);
            String resultsDirectory = String.format("%s/%d", RESULTS_DIRECTORY,nParts);

            Particle bigParticle = new Particle(
                    0,
                    centerPosition.getX_value(),
                    centerPosition.getY_value(),
                    BIG_PARTICLE_RADIUS,
                    DEFAULT_PARTICLE_PROPERTY,
                    BIG_PARTICLE_VELOCITY,
                    0,
                    BIG_PARTICLE_MASS,
                    bigParticleColor);


            List<Particle> particles = ParticleGenerator.generateRandomParticles(
                    nParts,
                    BOARD_LENGTH,
                    SMALL_PARTICLE_RADIUS,
                    DEFAULT_PARTICLE_PROPERTY,
                    SMALL_PARTICLE_MASS,
                    bigParticle,
                    smallParticlesColor
            );

            ResultsGenerator rg = new ResultsGenerator(
                    dynamicFilePath,
                    staticFilePath,
                    particleResultsFilePath,
                    collisionResultsFilePath,
                    resultsDirectory);

            BrownianMotion.brownianMotionMethod(particles,rg);



        }



    }



}
