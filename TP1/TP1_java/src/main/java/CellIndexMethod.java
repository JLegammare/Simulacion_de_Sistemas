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
        float rc = Float.parseFloat(cmd.getOptionValue("i_radius"));
        int m = Integer.parseInt(cmd.getOptionValue("m_cells"));

        boolean periodicCondition = false;

        List<Particle> particles = parser.parseParticles(staticInputFilePath, dynamicInputFilePath, l, m);


        if (particles.size() != n) {
            //TODO: Throw exception and show a message
            System.exit(1);
        }

        Map<Pair<Integer, Integer>, Cell> cells = new TreeMap<>();

        createCells(cells, m, periodicCondition);

        Map<Particle, List<Particle>> neighborhoods = new TreeMap<>();

        for (int i = 0; i < n; i++) {
            Particle p = particles.get(i);
            addParticle(p, cells, periodicCondition, m, l);
        }

//        if (periodicCondition)
//            borderParticles(cells, m, l);
//
//        //todo: borrar
//        for (Cell[] cell : cells) {
//            for (Cell value : cell) {
//                System.out.printf("(%d,%d)\t\t", value.getX(), value.getY());
//            }
//            System.out.println();
//        }

//
        getAllNeighbors(m, cells, particles, rc, neighborhoods, periodicCondition);
//
        parser.generateOutput(neighborhoods, outputFilePath);

    }

    //    private static void borderParticles(Map<Pair<Integer, Integer>, Cell> cellsBoard, int m, int l) {
//
//        float distance = m * l;
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
//
//
//    //completes the list of neighbors a particle has
//
    private static void getAllNeighbors(int m, Map<Pair<Integer, Integer>, Cell> cells, List<Particle> particles, float rc, Map<Particle, List<Particle>> neighborhoods, boolean periodicCondition) {
        List<Particle> possibleNeighbors;
        Map<Cell, List<Cell>> cellsMap = new HashMap<>();
        int aux_m = periodicCondition ? m + 1 : m;


        for (int i = periodicCondition ? 1 : 0; i < aux_m; i++) {
            for (int j = periodicCondition ? 1 : 0; j < aux_m; j++) {
                Cell c = cells.get(new Pair<>(i, j));
                cellsMap.put(c, addHalfNeighbors(i, j, cells, m, periodicCondition));
                possibleNeighbors = findPossibleNeighbors(c, cellsMap.get(c));
                for (Particle p : c.getParticles()) {
                    neighborhoods.put(p, possibleNeighbors.stream().filter(particle ->
                            particle.checkIfNeighbor(p, rc)).collect(Collectors.toList()));
                }
            }
        }
        completeNeighborsList(particles, neighborhoods);


    }

    //
    public static List<Cell> addHalfNeighbors(int x, int y, Map<Pair<Integer, Integer>, Cell> cells, int m, boolean periodicCondition) {

        List<Cell> cellList = new ArrayList<>();
        if (periodicCondition) {
            cellList.add(cells.get(new Pair<>(x + 1, y + 1)));
            cellList.add(cells.get(new Pair<>(x - 1, y + 1)));
            cellList.add(cells.get(new Pair<>(x - 1, y)));
            cellList.add(cells.get(new Pair<>(x + 1, y + 1)));
        } else {
            if (y + 1 < m) {
                cellList.add(cells.get(new Pair<>(x, y + 1)));
                if (x + 1 < m){
                    cellList.add(cells.get(new Pair<>(x + 1, y + 1)));
                     cellList.add(cells.get(new Pair<>(x+1, y)));
                }

            } else if (y - 1 >= 0 && x + 1 < m)
                cellList.add(cells.get(new Pair<>(x + 1, y - 1)));


        }

        return cellList;
    }


    private static void completeNeighborsList(List<Particle> particles, Map<Particle, List<Particle>> neighborhoods) {
        for (Particle particle : particles) {
            for (Particle neighbor : neighborhoods.get(particle)) {
                if (!neighborhoods.get(neighbor).contains(particle))
                    neighborhoods.get(neighbor).add(particle);
            }
        }
    }

    private static void createCells(Map<Pair<Integer, Integer>, Cell> cellsBoard, int m, boolean periodicCondition) {

        int lastElementPosition = periodicCondition ? m + 1 : m;
        int matrixDim = periodicCondition ? m + 2 : m;
        int id = 0;
        int auxM = periodicCondition ? m : m - 1;

        for (int i = periodicCondition ? 1 : 0; i < lastElementPosition; i++) {
            for (int j = periodicCondition ? 1 : 0; j < lastElementPosition; j++) {
                cellsBoard.put(new Pair<>(i, j), new Cell(i, j));
            }

        }

        if (periodicCondition) {

            int newMax = m + 1;
            //laterales
            for (int k = 1; k < newMax; k++) {
                cellsBoard.put(new Pair<>(0, k), new Cell(0, k));
                cellsBoard.put(new Pair<>(newMax, k), new Cell(newMax, k));
                cellsBoard.put(new Pair<>(k, 0), new Cell(k, 0));
                cellsBoard.put(new Pair<>(k, newMax), new Cell(k, newMax));
            }

            //esquinas

            cellsBoard.put(new Pair<>(0, 0), new Cell(0, 0));
            cellsBoard.put(new Pair<>(0, newMax), new Cell(0, newMax));
            cellsBoard.put(new Pair<>(newMax, 0), new Cell(newMax, 0));
            cellsBoard.put(new Pair<>(newMax, newMax), new Cell(newMax, newMax));
        }

    }

    private static void addParticle(Particle particle, Map<Pair<Integer, Integer>, Cell> cellsBoard, boolean periodicCondition, int m, int l) {
        int incM = periodicCondition ? m + 1 : m;
        int auxM = periodicCondition ? m : m - 1;
        for (int i = periodicCondition ? 1 : 0; i < incM; i++) {
            for (int j = periodicCondition ? 1 : 0; j < incM; j++) {
                Pair<Integer, Integer> p = new Pair<>(i, j);
                Cell cell = cellsBoard.get(p);
                if (cell.getCellCoordinates().equals(getParticleCell(particle, m, l))) {
                    cellsBoard.get(cell.getCellCoordinates()).addParticle(particle);
                    return;
                }
            }
        }

    }

    public static List<Particle> findPossibleNeighbors(Cell cell, List<Cell> neighborCells) {
        List<Particle> possibleNeighbors = new ArrayList<>();
        if (!cell.getParticles().isEmpty()) {
            possibleNeighbors.addAll(cell.getParticles());
            for (Cell c : neighborCells) {
                possibleNeighbors.addAll(c.getParticles());
            }
        }
        return possibleNeighbors;
    }

    //TODO: refactor getParticleCell
    public static Pair<Integer, Integer> getParticleCell(Particle p, int m, float l) {
        float x = p.getX();
        float y = p.getY();

        int col = (int) (x / l);
        int row = (int) (y / l);

        return new Pair<>(row, col);

    }

}
