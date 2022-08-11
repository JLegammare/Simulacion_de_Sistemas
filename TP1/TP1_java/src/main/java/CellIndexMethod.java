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
        float rc = Integer.parseInt(cmd.getOptionValue("i_radius"));
        int m = Integer.parseInt(cmd.getOptionValue("m_cells"));

        boolean borders = false;

        List<Particle> particles = parser.parseParticles(staticInputFilePath,dynamicInputFilePath, l, m);


         if (particles.size() != n){
            //TODO: Throw exception and show a message
            System.exit(1);
        }

        Cell[][] cells = createCells(m,borders);

        Map<Particle,List<Particle>> neighborhoods= new HashMap<>();

        for (int i = 0; i < n; i++) {
            Particle p = particles.get(i);
            addParticle(p, cells, borders, m);
        }

//        //todo: borrar
//        for (Cell[] cell : cells) {
//            for (Cell value : cell) {
//                System.out.print(value);
//            }
//            System.out.println();
//        }

        getAllNeighbors(m,cells,particles,rc,neighborhoods,borders);
//
        parser.generateOutput(neighborhoods,outputFilePath);

        particles.forEach((i) -> {
            System.out.println(i.getId() + "->" + neighborhoods.get(i).stream().map(Particle::getId).sorted().collect(Collectors.toList()));
        });

    }

    //completes the list of neighbors a particle has

    private static void getAllNeighbors(int m, Cell[][] cells, List<Particle> particles, float rc, Map<Particle,List<Particle>> neighborhoods, boolean borders){
        List<Particle> possibleNeighbors;
        Map<Cell,List<Cell>> cellsMap = new HashMap<>();
        int aux_m = borders?m+1:m;


        for(int i= borders?1:0; i < aux_m; i++){
            for(int j= borders?1:0; j < aux_m; j++){
                Cell c = cells[i][j];
                cellsMap.put(c,addHalfNeighbors(i,j, cells, m,borders));
                possibleNeighbors = findPossibleNeighbors(c, cellsMap.get(c));
                for (Particle p : c.getParticles()) {
                    neighborhoods.put(p,possibleNeighbors.stream().filter(particle ->
                            particle.checkIfNeighbor(p, rc)).collect(Collectors.toList()));
                }
            }
        }
        completeNeighborsList(particles, neighborhoods);



    }

    public static List<Cell> addHalfNeighbors(int x,int y ,Cell[][] cells, int m, boolean borders){

        List<Cell> cellList = new ArrayList<>();
        if(borders){
            cellList.add(cells[x][y+1]);
            cellList.add(cells[x-1][y+1]);
            cellList.add(cells[x-1][y]);
            cellList.add(cells[x+1][y+1]);
        }
        else{
            if( y+1 < m) {
                cellList.add(cells[x][y + 1]);
                if( x+1 < m)
                    cellList.add(cells[x+1][y+1]);
                else if (x - 1 >= 0)
                    cellList.add(cells[x - 1][y + 1]);
            }
            if(x-1 >= 0)
                cellList.add(cells[x-1][y]);
        }

        return cellList;
    }


    private static void completeNeighborsList(List<Particle> particles, Map<Particle,List<Particle>> neighborhoods){
            for(Particle particle : particles){
                for(Particle neighbor : neighborhoods.get(particle)){
                    if(!neighborhoods.get(neighbor).contains(particle))
                        neighborhoods.put(neighbor, neighborhoods.get(neighbor));
                }
            }
        }

    private static Cell[][] createCells(int m, boolean borders){

            int aux_m = borders?m+1:m;
            int id = 0;
            Cell[][] cellsBoard = new Cell[aux_m+1][aux_m+1];
            for(int i= borders?1:0; i < aux_m; i++){
                for(int j= borders?1:0; j < aux_m; j++){
                    cellsBoard[i][j] = new Cell(id++, j, i);
                }

            }

            if(borders){

                int new_max = m+1;
                //laterales
                for (int k = 1; k < new_max ; k++) {
                    cellsBoard[0][k] = cellsBoard[m][k];
                    cellsBoard[new_max][k] = cellsBoard[1][k];
                    cellsBoard[k][0] = cellsBoard[k][m];
                    cellsBoard[k][new_max] = cellsBoard[k][1];
                }

                //esquinas
                cellsBoard[0][0]=cellsBoard[m][m];
                cellsBoard[0][new_max]=cellsBoard[m][1];
                cellsBoard[new_max][0]=cellsBoard[1][m];
                cellsBoard[new_max][new_max]= cellsBoard[1][1];
            }

            return cellsBoard;
        }

    private static void addParticle(Particle particle, Cell[][] cells,boolean borders,int m){
            int aux_m = borders?m+1:m;
            for (int i= borders?1:0; i <aux_m; i++) {
                for (int j= borders?1:0; j <aux_m; j++) {
                    Cell cell = cells[i][j];
                    if(cell.getNumber() == particle.getCellNum()){
                        cell.addParticle(particle);
                        return;
                    }
                }
            }

        }

    public static List<Particle> findPossibleNeighbors(Cell cell, List<Cell> neighborCells){
        List<Particle> possibleNeighbors = new ArrayList<>();
        if(!cell.getParticles().isEmpty()){
            possibleNeighbors.addAll(cell.getParticles());
            for(Cell c: neighborCells){
                possibleNeighbors.addAll(c.getParticles());
            }
        }
        return possibleNeighbors;
    }

}
