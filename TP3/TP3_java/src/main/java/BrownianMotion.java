import models.*;
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
        Collision firtCollition = calculateNextCollision(particles,board);
        particlesEvolution(particles,nextCollision);
        colision(particles,)
//        particles.forEach(p->tempEvolution(p));

        System.out.println(nextCollision);
    }

    private static void particlesEvolution(List<Particle> particles, double nextCollision) {
        particles.forEach((p)-> {

            double newXvalue = p.getX() + p.getXVelocity()*nextCollision;
            double newYvalue = p.getY() + p.getYVelocity()*nextCollision;

            p.updatePosition(newXvalue,newYvalue);

        });

    }

    private static List<Collision> calculateNextCollision(List<Particle> particles, Board board) {

        Map<Double, List<Collision>> collisions = new TreeMap<>();

        particles.forEach((p)->{

            if (p.getVelocityModule() != 0){

                Collision nextParticleCollision = particlesCollisionTime(p,particles);
                addCollision(collisions,nextParticleCollision);

                List<Collision> wallCollisions = wallCollisions(p);
                wallCollisions.forEach(collision -> addCollision(collisions,collision));

            }
        });

        return collisions.get(collisions.keySet().stream().min(Double::compareTo).get());

    }

    private static void addCollision(Map<Double, List<Collision>> collisions, Collision collision){

        double collisionTime = collision.getCollisionTime();
        if(collisions.containsKey(collisionTime))
            collisions.get(collisionTime).add(collision);
        else {
            collisions.put(collisionTime,new ArrayList<>());
            collisions.get(collisionTime).add(collision);
        }

    }

    private static List<Collision> wallCollisions(Particle p){

        double tcX, tcY;
        Wall wallY,wallX;

        if(p.getXVelocity() > 0){
            tcX = (BOARD_LENGTH - p.getRadius() - p.getX()) / p.getXVelocity();
            wallX = Wall.RIGHT;

        }else {
            tcX = (0 + p.getRadius() - p.getX()) / p.getXVelocity();
            wallX = Wall.LEFT;
        }

        if(p.getYVelocity() > 0){
            tcY = (BOARD_LENGTH - p.getRadius() - p.getY()) / p.getYVelocity();
            wallY = Wall.TOP;
        }else{
            tcY = (0 + p.getRadius() - p.getY()) / p.getYVelocity();
            wallY = Wall.BOTTOM;
        }

        List<Collision> cList = new ArrayList<>();

        CollisionType cType;


        if (tcX <= tcY){
            if(wallX.equals(Wall.LEFT))
                cList.add(new Collision(CollisionType.LEFT_BORDER_COLLISION,p,tcX));
            else
                cList.add(new Collision(CollisionType.RIGHT_BORDER_COLLISION,p,tcX));
        }
        else if ( tcX >= tcY ){
            if(wallY.equals(Wall.TOP))
                cList.add(new Collision(CollisionType.TOP_BORDER_COLLISION,p,tcX));
            else
                cList.add(new Collision(CollisionType.BOTTOM_BORDER_COLLISION,p,tcX));
        }

        return cList;

    }


    private static Collision particlesCollisionTime(Particle selectedParticle, List<Particle> particles){

        final Collision[] collision = {new Collision(
                CollisionType.PARTICLE_COLLISION,
                selectedParticle,
                Double.POSITIVE_INFINITY)};

        particles.forEach( p-> {

            if(!p.equals(selectedParticle)){


                double o = selectedParticle.getRadius() + p.getRadius();

                Pair<Double,Double> deltaR = new Pair<>(
                        p.getX()-selectedParticle.getX(),
                        p.getY()-selectedParticle.getY());

                Pair<Double,Double> deltaV = new Pair<>(
                        p.getXVelocity()-selectedParticle.getXVelocity(),
                        p.getYVelocity()-selectedParticle.getYVelocity());

                double deltaRXdeltaR = Math.pow(deltaR.getX_value(),2) + Math.pow(deltaR.getY_value(),2);
                double deltaVXdeltaV = Math.pow(deltaV.getX_value(),2) + Math.pow(deltaV.getY_value(),2);
                double deltaRXdeltaV = deltaR.getX_value()*deltaV.getX_value()+deltaR.getY_value();

                double d = Math.pow(deltaRXdeltaV,2)-deltaVXdeltaV*(deltaRXdeltaR-Math.pow(o,2));

                if (d>=0 && deltaRXdeltaV< 0){

                    double tc = -(deltaRXdeltaV + Math.sqrt(d)) / deltaVXdeltaV;

                    if(tc < collision[0].getCollisionTime())
                        collision[0] = new Collision(CollisionType.PARTICLE_COLLISION,selectedParticle,tc);
                }
            }
        });

        return collision[0];
    }


}
