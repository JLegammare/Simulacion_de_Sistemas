import models.Board;
import models.Pair;
import models.Particle;
import models.Wall;
import utils.ParticleGenerator;
import utils.ResultsGenerator;

import java.io.IOException;
import java.util.*;

public class BrownianMotion {

    private static final int TOTAL_ITERATIONS = 20000;
    private static final double DEFAULT_PARTICLE_PROPERTY = 1;
    private static final String RESULTS_DIRECTORY =  "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final int BIG_PARTICLE_VELOCITY = 0;
    private static final double BIG_PARTICLE_MASS = 0.9;
    private static final double BIG_PARTICLE_RADIUS = 0.7;
    private static final int NUMBER_OF_SMALL_PARTICLES = 120;
    private static final double RC = 1.0;

    private static final Double BOARD_LENGTH = 6.0;
    private static final double SMALL_PARTICLE_RADIUS = 0.2;
    private static final double SMALL_PARTICLE_MASS = 0.9;

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

        Pair<Double,Double> centerPosition = new Pair<>(BOARD_LENGTH/2,BOARD_LENGTH/2);
        Particle bigParticle = new Particle(
                0,
                centerPosition.getX_value(),
                centerPosition.getY_value(),
                BIG_PARTICLE_RADIUS,
                DEFAULT_PARTICLE_PROPERTY,
                BIG_PARTICLE_VELOCITY,
                0,
                BIG_PARTICLE_MASS);



        List<Particle> particles = ParticleGenerator.generateRandomParticles(
                NUMBER_OF_SMALL_PARTICLES,
                BOARD_LENGTH,
                0,
                SMALL_PARTICLE_RADIUS,
                DEFAULT_PARTICLE_PROPERTY,
                SMALL_PARTICLE_MASS,
                bigParticle
        );

        BrownianMotionMethod(particles);
    }

    private static void BrownianMotionMethod(List<Particle> particles) throws IOException {

        String dynamicResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, DYNAMIC_FILE);
        String staticResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, STATIC_FILE);

        ResultsGenerator rg = new ResultsGenerator(dynamicResultsFilePath,staticResultsFilePath,RESULTS_DIRECTORY);

        Board board = new Board(BrownianMotion.BOARD_LENGTH, 0, SMALL_PARTICLE_RADIUS);

        board.addParticlesToBoard(particles);
        rg.fillStaticFile(particles,BOARD_LENGTH);
        rg.addStateToDynamicFile(particles,0);
        double nextCollision = calculateNextCollision(particles,board);
//        particles.forEach(p->tempEvolution(p));

        System.out.println(nextCollision);
    }

    private static double calculateNextCollision(List<Particle> particles,Board board) {

        //TODO: adapt cellIndexMethod
//        Map<Particle, Set<Particle>> neighborhoods = board.getAllNeighbors(RC);
        List<Double> collisionsTimes = new ArrayList<>();

        particles.forEach((p)->{

            if (p.getVelocityModule()!=0){

                Map<Double,Pair<Particle,Particle>> particleCollisions =particlesCollisionTime(p,particles);
                Optional<Double> minTcOpt = particleCollisions.keySet().stream().min(Double::compareTo);

                minTcOpt.ifPresent(collisionsTimes::add);
                double wallCollisionTime = wallCollisionTime(p);

                collisionsTimes.add(wallCollisionTime);
            }
        });

        return collisionsTimes.stream().min(Double::compareTo).get();

    }

    private static double wallCollisionTime(Particle p){

        double tcX, tcY;

        if(p.getXVelocity() > 0){
            tcX = (BOARD_LENGTH - p.getRadius() - p.getX()) / p.getXVelocity();
        }else
            tcX = (0 + p.getRadius() - p.getX()) / p.getXVelocity();

        if(p.getYVelocity() > 0){
            tcY = (BOARD_LENGTH - p.getRadius() - p.getY()) / p.getYVelocity();
        }else
            tcY = (0 + p.getRadius() - p.getY()) / p.getYVelocity();

        return Math.min(tcX, tcY);


    }


    private static Map<Double,Pair<Particle,Particle>> particlesCollisionTime(Particle selectedParticle, List<Particle> particles){

        Map<Double,Pair<Particle,Particle>> timesMap = new TreeMap<>();

        particles.forEach(p-> {

            double o = selectedParticle.getRadius() + p.getRadius();

            Pair<Double,Double> deltaR = new Pair<>(p.getX()-selectedParticle.getX(), p.getY()-selectedParticle.getY());
            Pair<Double,Double> deltaV = new Pair<>(p.getXVelocity()-selectedParticle.getXVelocity(),p.getYVelocity()-selectedParticle.getYVelocity());

            double deltaRXdeltaR = Math.pow(deltaR.getX_value(),2) + Math.pow(deltaR.getY_value(),2);
            double deltaVXdeltaV = Math.pow(deltaV.getX_value(),2) + Math.pow(deltaV.getY_value(),2);
            double deltaRXdeltaV = deltaR.getX_value()*deltaV.getX_value()+deltaR.getY_value();

            double d = Math.pow(deltaRXdeltaV,2)-deltaVXdeltaV*(deltaRXdeltaR-Math.pow(o,2));
            if (d>=0 && deltaRXdeltaV< 0){
                double tc = -(deltaRXdeltaV + Math.sqrt(d)) / deltaVXdeltaV;
                timesMap.put(tc,new Pair<>(selectedParticle,p));
            }
        });

        return timesMap;
    }

//    private static void tempEvolution(Particle particle) {
//        setNewOmega(particle,neighbors);
//        checkPeriodicMovement(particle,l,dt);
//        particle.updateDeltaOmega(n);
//    }
//
//    private static void checkPeriodicMovement(Particle particle,double l, int dt){
//
//        double newXvalue = particle.getX() + particle.getXVelocity()*dt;
//        double newYvalue = particle.getY() + particle.getYVelocity()*dt;
//
//        particle.updatePosition(newXvalue,newYvalue,l);
//
//    }
//
//    private static void setNewOmega(Particle particle,Set<Particle> neighbors ){
//        double sinAvg = 0;
//        double cosAvg = 0;
//        double omegaAvg;
//
//        if (!neighbors.isEmpty()) {
//            for (Particle p : neighbors) {
//                sinAvg += sin(p.getOmega());
//                cosAvg += cos(p.getOmega());
//            }
//        }
//
//        sinAvg += sin(particle.getOmega());
//        cosAvg += cos(particle.getOmega());
//
//        sinAvg = sinAvg / (neighbors.size() + 1);
//        cosAvg = cosAvg / (neighbors.size() + 1);
//
//        omegaAvg = atan2(sinAvg, cosAvg);
//        particle.setOmega(omegaAvg + particle.getDeltaOmega());
//    }
//

}
