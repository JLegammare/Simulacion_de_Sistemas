import models.*;
import utils.ParticleGenerator;
import utils.ResultsGenerator;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BrownianMotion {

    private static final double DEFAULT_PARTICLE_PROPERTY = 1;
    private static final String RESULTS_DIRECTORY =  "simulation_results";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String PARTICLE_FILE = "BigParticlePosition.txt";
    private static final String COLLISION_FILE = "CollisionsTime.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final int NUMBER_OF_SMALL_PARTICLES = 140;
    private static final Double BOARD_LENGTH = 6.0;
    private static final int BIG_PARTICLE_VELOCITY = 0;
    private static final double BIG_PARTICLE_MASS = 2; //todo volver a correr
    private static final double BIG_PARTICLE_RADIUS = 0.7;
    private static final Color bigParticleColor = Color.RED;
    private static final Color smallParticlesColor = Color.BLUE;
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
                BIG_PARTICLE_MASS,
                bigParticleColor);

        List<Particle> particles = ParticleGenerator.generateRandomParticles(
                NUMBER_OF_SMALL_PARTICLES,
                BOARD_LENGTH,
                SMALL_PARTICLE_RADIUS,
                DEFAULT_PARTICLE_PROPERTY,
                SMALL_PARTICLE_MASS,
                bigParticle,
                smallParticlesColor
        );

        String dynamicResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, DYNAMIC_FILE);
        String staticResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, STATIC_FILE);
        String particleResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, PARTICLE_FILE);
        String collisionResultsFilePath = String.format("%s/%s", RESULTS_DIRECTORY, COLLISION_FILE);

        ResultsGenerator rg = new ResultsGenerator(
                dynamicResultsFilePath,
                staticResultsFilePath,
                particleResultsFilePath,
                collisionResultsFilePath,
                RESULTS_DIRECTORY);

        BrownianMotionMethod(particles,rg);


    }

    private static void BrownianMotionMethod(List<Particle> particles, ResultsGenerator rg) throws IOException {


        Board board = new Board(BrownianMotion.BOARD_LENGTH, 0, SMALL_PARTICLE_RADIUS);

        board.addParticlesToBoard(particles);
        rg.fillStaticFile(particles,BOARD_LENGTH);
        rg.addStateToDynamicFile(particles,0.0);
        boolean bigParticleWallCollision = false;

        while(!bigParticleWallCollision){
            List<Collision> firstCollisions = calculateNextCollision(particles);
            double collisionTime = firstCollisions.get(0).getCollisionTime();
            particlesEvolution(particles,collisionTime);
            rg.addStateToDynamicFile(particles,collisionTime);
            bigParticleWallCollision = collisionOperation(firstCollisions,particles.get(0),rg);
        }


    }

    private static boolean collisionOperation(List<Collision> firstCollisions,Particle bigParticle, ResultsGenerator rg) throws IOException {
        List <Collision> bigCollision = firstCollisions.stream()
                .filter(c-> c.getParticle().equals(bigParticle))
                .collect(Collectors.toList());
        if(!bigCollision.isEmpty())
            rg.addBigParticleMovement(bigParticle,bigCollision.get(0).getCollisionTime());

        if(firstCollisions.size() == 1){

            if(firstCollisions.get(0).getParticle().equals(bigParticle))
                return true;

            CollisionType ct = firstCollisions.get(0).getCollisionType();
            Particle p = firstCollisions.get(0).getParticle();

            if(ct == CollisionType.BORDER_Y_COLLISION){

                p.setVy(-p.getVy());
            }
            else{
                p.setVx(-p.getVx());
            }
        }
        else {

            Particle firstParticle = firstCollisions.get(0).getParticle();
            Particle secondParticle = firstCollisions.get(1).getParticle();

            double o = firstParticle.getRadius() + secondParticle.getRadius();

            Pair<Double,Double> deltaR = new Pair<>(
                    firstParticle.getX()-secondParticle.getX(),
                    firstParticle.getY()-secondParticle.getY());

            Pair<Double,Double> deltaV = new Pair<>(
                    firstParticle.getVx()-secondParticle.getVx(),
                    firstParticle.getVy()-secondParticle.getVy());

            double deltaVXdeltaR = deltaV.getX_value()*deltaR.getX_value() + deltaV.getY_value() * deltaR.getY_value();

            double j = (2 * firstParticle.getMass() * secondParticle.getMass() * deltaVXdeltaR)/
                    (o * (firstParticle.getMass() + secondParticle.getMass()));
            double jx = j * deltaR.getX_value()/o;
            double jy = j * deltaR.getY_value()/o;

            double vxdFirstParticle = firstParticle.getVx() - jx/firstParticle.getMass();
            double vydFirstParticle = firstParticle.getVy() - jy/firstParticle.getMass();
            double vxdSecondParticle = secondParticle.getVx() + jx/secondParticle.getMass();
            double vydSecondParticle = secondParticle.getVy() + jy/secondParticle.getMass();

            firstParticle.setVy(vydFirstParticle);
            firstParticle.setVx(vxdFirstParticle);

            secondParticle.setVy(vydSecondParticle);
            secondParticle.setVx(vxdSecondParticle);

        }

        return false;

    }

    private static void particlesEvolution(List<Particle> particles, double nextCollision) {
        particles.forEach((p)-> {

            double newXvalue = p.getX() + p.getVx()*nextCollision;
            double newYvalue = p.getY() + p.getVy()*nextCollision;

            p.updatePosition(newXvalue,newYvalue);

        });

    }

    private static List<Collision> calculateNextCollision(List<Particle> particles) {

        Map<Double, List<Collision>> collisions = new TreeMap<>();

        particles.forEach((p)->{


                Collision nextParticleCollision = particlesCollisionTime(p,particles);
                addCollision(collisions,nextParticleCollision);

                List<Collision> wallCollisions = wallCollisions(p);
                wallCollisions.forEach(collision -> addCollision(collisions,collision));

        });

        Double minKey = collisions.keySet().stream()
                .filter(Double::isFinite)
                .min(Double::compareTo).get();

        return collisions.get(minKey);

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

        double tcX = Double.POSITIVE_INFINITY, tcY = Double.POSITIVE_INFINITY;

        if(p.getVx() != 0){
            if(p.getVx()>0)
                tcX =  (BOARD_LENGTH - p.getRadius() - p.getX()) / p.getVx();
            else
                tcX = (0 + p.getRadius() - p.getX()) / p.getVx();
        }

        if(p.getVy()!= 0){
            if(p.getVy()>0)
                tcY = (BOARD_LENGTH - p.getRadius() - p.getY()) / p.getVy();
            else
                tcY= (0 + p.getRadius() - p.getY()) / p.getVy();
        }

        List<Collision> cList = new ArrayList<>();

        if (tcX <= tcY)
            cList.add(new Collision(CollisionType.BORDER_X_COLLISION,p,tcX));
        else if ( tcX >= tcY )
            cList.add(new Collision(CollisionType.BORDER_Y_COLLISION,p,tcY));

        return cList.stream().filter(c->Double.isFinite(c.getCollisionTime())).collect(Collectors.toList());

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
                        p.getVx()-selectedParticle.getVx(),
                        p.getVy()-selectedParticle.getVy());

                double deltaRXdeltaR = Math.pow(deltaR.getX_value(),2) + Math.pow(deltaR.getY_value(),2);
                double deltaVXdeltaV = Math.pow(deltaV.getX_value(),2) + Math.pow(deltaV.getY_value(),2);
                double deltaRXdeltaV = deltaR.getX_value()*deltaV.getX_value() + deltaR.getY_value()*deltaV.getY_value();

                double d = Math.pow(deltaRXdeltaV,2)-deltaVXdeltaV*(deltaRXdeltaR-Math.pow(o,2));

                if (d>=0 && deltaRXdeltaV< 0){

                    double tc = -(deltaRXdeltaV + Math.sqrt(d)) / deltaVXdeltaV;

                    if(tc <= collision[0].getCollisionTime())
                        collision[0] = new Collision(CollisionType.PARTICLE_COLLISION,selectedParticle,tc);
                }
            }
        });

        return collision[0];
    }


}
