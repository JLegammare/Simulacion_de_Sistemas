import org.apache.commons.cli.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CellIndexMethod {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        CommandLine cmd = parser.parseArguments(args);

        String staticInputFilePath = cmd.getOptionValue("static-input");
        String dynamicInputFilePath = cmd.getOptionValue("dynamic-input");
        String outputFilePath = cmd.getOptionValue("output");
        int n = Integer.parseInt(cmd.getOptionValue("n_particles"));
        int l = Integer.parseInt(cmd.getOptionValue("length"));
        int rc = Integer.parseInt(cmd.getOptionValue("i_radius"));

        int m = Integer.parseInt(cmd.getOptionValue("m_cells"));
        Set<Particle> possibleNeighbors;
        List<Cell> cells = new ArrayList<>();

        double boardLength = l*m;

        List<Particle> particles = parser.parseParticles(staticInputFilePath,dynamicInputFilePath, l, m);

        if (particles.size() != n){
            //TODO: Throw exception and show a message
            System.exit(1);
        }

        for(int i = 0; i < m*m; i++)
            cells.add(new Cell(i));

        for (int i = 0; i < n; i++) {
            Particle p = particles.get(i);
            cells.get(p.getCellNum()).addParticle(p);
        }

        for(int i = 0; i < m*m; i++) {
            Cell c = cells.get(i);
            c.addHalfNeighbors(cells, m);

            possibleNeighbors = c.findPossibleNeighbors();
            for (Particle p : c.getParticles()) {
                p.setNeighbors(possibleNeighbors.stream().filter(particle ->
                        particle.checkIfNeighbor(p, rc)).collect(Collectors.toList()));

                if(!particles.contains(p))
                    particles.add(p);
            }
        }
        completeNeighborsList(particles);

//        Map<Particle,List<Particle>> neighborsMap = new HashMap<>();
//
//        for (Particle p: particles) {
//            neighborsMap.put(p,particles.stream().filter(particle ->
//                particle.checkIfNeighbor(p,rc)
//            ).collect(Collectors.toList()));
//        }

        particles.forEach((i) -> {
            System.out.println(i.getId() + "->" + i.getNeighbors().stream().map(Particle::getId).sorted().collect(Collectors.toList()));
        });



       //parser.generateOutput(neighborsMap,outputFilePath);

    }

    //completes the list of neighbors a particle has
    private static void completeNeighborsList(List<Particle> particles){
        for(Particle particle : particles){
            for(Particle neighbor : particle.getNeighbors()){
                if(!neighbor.getNeighbors().contains(particle))
                    neighbor.addNeighbor(particle);
            }
        }
    }


}
