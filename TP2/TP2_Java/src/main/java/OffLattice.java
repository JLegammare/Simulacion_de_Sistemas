import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.*;

public class OffLattice {

   private static int DEFAULT_M = 4;
   private static final int TOTAL_ITERATIONS = 100;
   public static void main(String[] args) throws IOException {
      Parser parser = new Parser();
      CommandLine cmd = parser.parseArguments(args);

      String staticInputFilePath = cmd.getOptionValue("static-input");
      String dynamicInputFilePath = cmd.getOptionValue("dynamic-input");
      String outputFilePath = cmd.getOptionValue("output");

      double n = Double.parseDouble(cmd.getOptionValue("n_particles"));
      int l = Integer.parseInt(cmd.getOptionValue("length"));
      double rc = Float.parseFloat(cmd.getOptionValue("i_radius"));
      int m = DEFAULT_M;
      boolean periodicCondition = true;

      List<Particle> particles = ParticleGenerator.generateRandomParticles(100,m,l,n,0.1,0.03);
      Board board = new Board(m, l, periodicCondition);
      board.addParticlesToBoard(particles);

      parser.addIterationToOutput(0,particles,outputFilePath);
      List<Double> orderParameterList = new ArrayList<>();

      for (int i = 1; i < TOTAL_ITERATIONS; i++) {
         Map<Particle, Set<Particle>> neighborhoods = board.getAllNeighbors(rc);
         double va = calculateOrderParameter(particles,0.03);
         orderParameterList.add(va);
         particles.forEach(p->tempEvolution(p, neighborhoods.get(p),l, m));
         board.addParticlesToBoard(particles);
         parser.addIterationToOutput(i,particles,outputFilePath);

      }

   }

   private static double calculateOrderParameter(List<Particle> particles, double v) {

        double vx = particles.stream().mapToDouble(Particle::getXVelocity).sum();
        double vy = particles.stream().mapToDouble(Particle::getYVelocity).sum();
        double norm = sqrt(pow(vx, 2) + pow(vy, 2));

        return norm / (particles.size() * v);

   }

   private static int optimizedM(int l, int n, double rc, double maxRadius){

      int m = (int) (l/(rc + 2*maxRadius) + 1);
      if((double)l/m <= rc ){
         System.exit(1);
      }

      return m;
   }

   private static void tempEvolution(Particle particle, Set<Particle> neighbors, int l, int m){

      double sinAvg = 0;
      double cosAvg = 0;
      double omegaAvg;
      int boardLength = m*l;

      if(!neighbors.isEmpty()) {
         for (Particle n : neighbors) {
            sinAvg += sin(n.getOmega());
            cosAvg += cos(n.getOmega());
         }
         sinAvg += sin(particle.getOmega());
         cosAvg += cos(particle.getOmega());

         sinAvg = sinAvg / (neighbors.size() + 1);
         cosAvg = cosAvg / (neighbors.size() + 1);

         omegaAvg = atan2(sinAvg, cosAvg);
         particle.setOmega(omegaAvg + particle.getDeltaOmega());
      }

      double newXposition = particle.getX() + particle.getXVelocity();
      double newYposition = particle.getY() + particle.getYVelocity();


      //si se va por la derecha
      if(newXposition > boardLength){
         particle.setX(newXposition - boardLength);
      }//si se va por izquierda
      else if(newXposition < 0)
         particle.setX(boardLength + newXposition);
      else
         particle.setX(newXposition);
      //si se va por arriba
      if(newYposition > boardLength){
         particle.setY(newYposition - boardLength);
      }//si se va por abajo
      else if(newYposition < 0)
         particle.setY(boardLength + newYposition);
      else
         particle.setY(newYposition);

      particle.updateOmega(PI);

   }



}
