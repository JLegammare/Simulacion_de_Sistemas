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
        double rc = Float.parseFloat(cmd.getOptionValue("i_radius"));
        int m = Integer.parseInt(cmd.getOptionValue("m_cells"));

        boolean periodicCondition = false;

        List<Particle> particles = parser.parseParticles(staticInputFilePath, dynamicInputFilePath, l, m);

        if (particles.size() != n) {
            //TODO: Throw exception and show a message
            System.exit(1);
        }

        Map<Cell, List<Cell>> cellsBoard = new TreeMap<>();

        createCells(cellsBoard, m, periodicCondition);
        addParticlesToBoard(particles, cellsBoard,periodicCondition, m, l);

//
//        //todo: borrar
//        for (Cell[] cell : cells) {
//            for (Cell value : cell) {
//                System.out.printf("(%d,%d)\t\t", value.getX(), value.getY());
//            }
//            System.out.println();
//        }

////
          Map<Particle, List<Particle>> neighborhoods = getAllNeighbors(particles,m, cellsBoard, rc, periodicCondition);
////
        parser.generateOutput(neighborhoods, outputFilePath);
//
    }

//        private static void borderParticles(Map<Cell, List<Cell>> cellsBoard, int m, int l) {
//
//        double distance = m * l;
//        int incM = m + 1;
//        for (int k = 1; k < incM; k++) {
//            int finalK = k;
//            //first line
//            Cell c = cellsBoard[m][k];
//            cellsBoard[0][finalK].addParticles(c.getParticles().stream().map(p -> new Particle(
//                    p.getId(),
//                    p.getX(),
//                    p.getY() - distance,
//                    p.getRadius(),
//                    p.getProperty())).collect(Collectors.toList()));
//            //last line
//            c = cellsBoard[1][k];
//            cellsBoard[incM][finalK].addParticles(c.getParticles().stream().map(p -> new Particle(
//                    p.getId(),
//                    p.getX(),
//                    p.getY() + distance,
//                    p.getRadius(),
//                    p.getProperty())).collect(Collectors.toList()));
//
//            //first column
//            c = cellsBoard[k][m];
//            cellsBoard[incM][finalK].addParticles(c.getParticles().stream().map(p -> new Particle(
//                    p.getId(),
//                    p.getX() - distance,
//                    p.getY(),
//                    p.getRadius(),
//                    p.getProperty())).collect(Collectors.toList()));
//
//            //last column
//            c = cellsBoard[k][1];
//            cellsBoard[k][incM].addParticles(c.getParticles().stream().map(p -> new Particle(
//                    p.getId(),
//                    p.getX() + distance,
//                    p.getY(),
//                    p.getRadius(),
//                    p.getProperty())).collect(Collectors.toList()));
//
//        }
//        //top left corner
//        cellsBoard[0][0].addParticles(cellsBoard[m][m].getParticles().stream().map(p -> new Particle(
//                p.getId(),
//                p.getX() - distance,
//                p.getY() + distance,
//                p.getRadius(),
//                p.getProperty())).collect(Collectors.toList()));
//
//        //top right corner
//        cellsBoard[0][incM].addParticles(cellsBoard[m][1].getParticles().stream().map(p -> new Particle(
//                p.getId(),
//                p.getX() + distance,
//                p.getY() + distance,
//                p.getRadius(),
//                p.getProperty())).collect(Collectors.toList()));
//
//        //bottom left corner
//        cellsBoard[incM][0].addParticles(cellsBoard[1][m].getParticles().stream().map(p -> new Particle(
//                p.getId(),
//                p.getX() - distance,
//                p.getY() - distance,
//                p.getRadius(),
//                p.getProperty())).collect(Collectors.toList()));
//
//        //bottom right corner
//        cellsBoard[incM][incM].addParticles(cellsBoard[1][1].getParticles().stream().map(p -> new Particle(
//                p.getId(),
//                p.getX() + distance,
//                p.getY() - distance,
//                p.getRadius(),
//                p.getProperty())).collect(Collectors.toList()));
//
//    }



    private static Map<Particle, List<Particle>> getAllNeighbors(List<Particle> particles,int m, Map<Cell, List<Cell>> cellsBoard, double rc, boolean periodicCondition) {

        Set <Cell> cells = cellsBoard.keySet();
        Map<Particle, List<Particle>> neighborhoods = new TreeMap<>();

        for (Cell cell: cells) {
            List<Particle> possibleNeighbors = new ArrayList<>();
            cellsBoard.get(cell).stream().map(Cell::getParticles).forEach(possibleNeighbors::addAll);
            possibleNeighbors.addAll(cell.getParticles());
            possibleNeighbors.forEach(particle -> neighborhoods.put(particle,
                    possibleNeighbors.stream().filter(particle1 ->
                        particle1.checkIfNeighbor(particle,rc)).collect(Collectors.toList())));
        }

        completeNeighborsList(neighborhoods,particles);
        return neighborhoods;
    }


    private static void completeNeighborsList(Map<Particle, List<Particle>> neighborhoods, List<Particle> particles) {
        for (Particle particle : particles) {
            for (Particle neighbor : neighborhoods.get(particle)) {
                if (!neighborhoods.get(neighbor).contains(particle))
                    neighborhoods.get(neighbor).add(particle);
            }
        }
    }

    private static void createCells(Map<Cell, List<Cell>> cellsBoard, int m, boolean periodicCondition) {

        int lastElementPosition = periodicCondition ? m + 1 : m;

        for (int i = periodicCondition ? 1 : 0; i < lastElementPosition; i++) {
            for (int j = periodicCondition ? 1 : 0; j < lastElementPosition; j++) {
                cellsBoard.put(new Cell(i, j), new ArrayList<>());
            }
        }

        if (periodicCondition) {

            int newMax = m + 1;

            //laterales
            for (int k = 1; k < newMax; k++) {
                cellsBoard.put(new Cell(0, k), new ArrayList<>());
                cellsBoard.put(new Cell(newMax, k), new ArrayList<>());
                cellsBoard.put(new Cell(k, 0), new ArrayList<>());
                cellsBoard.put(new Cell(k, newMax), new ArrayList<>());
            }

            //esquinas
            cellsBoard.put(new Cell(0, 0), new ArrayList<>());
            cellsBoard.put(new Cell(0, newMax), new ArrayList<>());
            cellsBoard.put(new Cell(newMax, 0), new ArrayList<>());
            cellsBoard.put(new Cell(newMax, newMax), new ArrayList<>());
        }

        cellsBoard.keySet().forEach(cell -> addHalfNeighborsCells(cell, cellsBoard));

    }


    private static void addHalfNeighborsCells(Cell cell, Map<Cell, List<Cell>> cellsBoard) {

        Set<Cell> cellsSet = cellsBoard.keySet();
        List<Cell> selectedNeighborhood = cellsBoard.get(cell);

        int cellY = cell.getY();
        int cellX = cell.getX();

        selectedNeighborhood.addAll(cellsSet.stream().filter(cell2 -> {
            int cell2Y = cell2.getY();
            int cell2X = cell2.getX();

            // right
            if(cell2Y == cellY && cell2X == cellX+1){
                return true;
            }
            // top right
            else if(cell2Y == cellY +1  && cell2X == cellX+1){
                return true;
            }
            // top
            else if(cell2Y == cellY +1  && cell2X == cellX){
                return true;
            }
            //bottom right
            else return cell2Y == cellY - 1 && cell2X == cellX + 1;

        }).collect(Collectors.toList()));


//         if (periodicCondition) {
//            cellList.add(cells.get(new Pair<>(x + 1, y + 1)));
//            cellList.add(cells.get(new Pair<>(x - 1, y + 1)));
//            cellList.add(cells.get(new Pair<>(x - 1, y)));
//            cellList.add(cells.get(new Pair<>(x + 1, y + 1)));


    }

    private static void addParticlesToBoard(List<Particle> particles, Map<Cell, List<Cell>> cellsBoard, boolean periodicCondition, int m, int l) {
        Set<Cell> cells = cellsBoard.keySet();
        for (Particle particle : particles) {
            for (Cell cell : cells) {
                if (cell.getCellCoordinates().equals(getParticleCell(particle, m, l))) {
                    cell.addParticle(particle);
                }
            }
        }

//        if(periodicCondition){
//            addBorderParticles();
//        }
    }

    //TODO: refactor getParticleCell
    public static Pair<Integer, Integer> getParticleCell(Particle p, int m, double l) {
        double x = p.getX();
        double y = p.getY();

        int col = (int) (x / l);
        int row = (int) (y / l);

        return new Pair<>(row, col);

    }

}
