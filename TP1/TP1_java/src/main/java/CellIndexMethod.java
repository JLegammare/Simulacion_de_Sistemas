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

        Cell[][] cells = createCells(m, periodicCondition);

        Map<Particle, List<Particle>> neighborhoods = new HashMap<>();

        for (int i = 0; i < n; i++) {
            Particle p = particles.get(i);
            addParticle(p, cells, periodicCondition, m, l);
        }

        if (periodicCondition)
            borderParticles(cells, m, l);

        //todo: borrar
        for (Cell[] cell : cells) {
            for (Cell value : cell) {
                System.out.printf("%d-(%d,%d)\t\t",value.getNumber(),value.getX(),value.getY());
            }
            System.out.println();
        }

//        32	16	20	24	28	33
//        18	0	1	2	3	19
//        22	4	5	6	7	23
//        26	8	9	10	11	27
//        30	12	13	14	15	31
//        34	17	21	25	29	35

        getAllNeighbors(m, cells, particles, rc, neighborhoods, periodicCondition);
//
        parser.generateOutput(neighborhoods, outputFilePath);

    }

    private static void borderParticles(Cell[][] cellsBoard, int m, int l) {

        float distance = m * l;
        int incM = m + 1;
        for (int k = 1; k < incM; k++) {
            int finalK = k;
            //first line
            Cell c = cellsBoard[m][k];
            cellsBoard[0][finalK].addParticles(c.getParticles().stream().map(p -> new Particle(
                    p.getId(),
                    p.getX(),
                    p.getY() - distance,
                    p.getRadius(),
                    p.getProperty(),
                    cellsBoard[0][finalK].getNumber())).collect(Collectors.toList()));
            //last line
            c = cellsBoard[1][k];
            cellsBoard[incM][finalK].addParticles(c.getParticles().stream().map(p -> new Particle(
                    p.getId(),
                    p.getX(),
                    p.getY() + distance,
                    p.getRadius(),
                    p.getProperty(),
                    cellsBoard[incM][finalK].getNumber())).collect(Collectors.toList()));

            //first column
            c = cellsBoard[k][m];
            cellsBoard[incM][finalK].addParticles(c.getParticles().stream().map(p -> new Particle(
                    p.getId(),
                    p.getX() - distance,
                    p.getY(),
                    p.getRadius(),
                    p.getProperty(),
                    cellsBoard[incM][finalK].getNumber())).collect(Collectors.toList()));

            //last column
            c = cellsBoard[k][1];
            cellsBoard[k][incM].addParticles(c.getParticles().stream().map(p -> new Particle(
                    p.getId(),
                    p.getX() + distance,
                    p.getY(),
                    p.getRadius(),
                    p.getProperty(),
                    cellsBoard[finalK][incM].getNumber())).collect(Collectors.toList()));

        }
        //top left corner
        cellsBoard[0][0].addParticles(cellsBoard[m][m].getParticles().stream().map(p -> new Particle(
                p.getId(),
                p.getX() - distance,
                p.getY() + distance,
                p.getRadius(),
                p.getProperty(),
                cellsBoard[0][0].getNumber())).collect(Collectors.toList()));

        //top right corner
        cellsBoard[0][incM].addParticles(cellsBoard[m][1].getParticles().stream().map(p -> new Particle(
                p.getId(),
                p.getX() + distance,
                p.getY() + distance,
                p.getRadius(),
                p.getProperty(),
                cellsBoard[0][incM].getNumber())).collect(Collectors.toList()));

        //bottom left corner
        cellsBoard[incM][0].addParticles(cellsBoard[1][m].getParticles().stream().map(p -> new Particle(
                p.getId(),
                p.getX() - distance,
                p.getY() - distance,
                p.getRadius(),
                p.getProperty(),
                cellsBoard[incM][0].getNumber())).collect(Collectors.toList()));

        //bottom right corner
        cellsBoard[incM][incM].addParticles(cellsBoard[1][1].getParticles().stream().map(p -> new Particle(
                p.getId(),
                p.getX() + distance,
                p.getY() - distance,
                p.getRadius(),
                p.getProperty(),
                cellsBoard[incM][incM].getNumber())).collect(Collectors.toList()));

    }



    //completes the list of neighbors a particle has

    private static void getAllNeighbors(int m, Cell[][] cells, List<Particle> particles, float rc, Map<Particle, List<Particle>> neighborhoods, boolean periodicCondition) {
        List<Particle> possibleNeighbors;
        Map<Cell, List<Cell>> cellsMap = new HashMap<>();
        int aux_m = periodicCondition ? m + 1 : m;


        for (int i = periodicCondition ? 1 : 0; i < aux_m; i++) {
            for (int j = periodicCondition ? 1 : 0; j < aux_m; j++) {
                Cell c = cells[i][j];
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

    public static List<Cell> addHalfNeighbors(int x, int y, Cell[][] cells, int m, boolean periodicCondition) {

        List<Cell> cellList = new ArrayList<>();
        if (periodicCondition) {
            cellList.add(cells[x][y + 1]);
            cellList.add(cells[x - 1][y + 1]);
            cellList.add(cells[x - 1][y]);
            cellList.add(cells[x + 1][y + 1]);
        } else {
            if (y + 1 < m) {
                cellList.add(cells[x][y + 1]);
                if (x + 1 < m)
                    cellList.add(cells[x + 1][y + 1]);
                else if (x - 1 >= 0)
                    cellList.add(cells[x - 1][y + 1]);
            }
            if (x - 1 >= 0)
                cellList.add(cells[x - 1][y]);
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

    private static Cell[][] createCells(int m, boolean periodicCondition) {

        int lastElementPosition = periodicCondition ? m + 1 : m;
        int matrixDim = periodicCondition ? m + 2 : m;
        int id = 0;
        Cell[][] cellsBoard = new Cell[matrixDim][matrixDim];

        for (int i = periodicCondition ? 1 : 0; i < lastElementPosition; i++) {
            for (int j = periodicCondition ? 1 : 0; j < lastElementPosition; j++) {
                cellsBoard[i][j] = new Cell(id++, i, j);
            }

        }

        if (periodicCondition) {

            int newMax = m + 1;
            //laterales
            for (int k = 1; k < newMax; k++) {
                cellsBoard[0][k] = new Cell(cellsBoard[m][k].getNumber(), 0, k);
                cellsBoard[newMax][k] = new Cell(cellsBoard[1][k].getNumber(), newMax, k);
                cellsBoard[k][0] = new Cell(cellsBoard[k][m].getNumber(), k, 0);
                cellsBoard[k][newMax] = new Cell(cellsBoard[k][1].getNumber(), k, newMax);
            }

            //esquinas
            cellsBoard[0][0] = new Cell(id++, 0, 0);
            cellsBoard[0][newMax] = new Cell(id++, 0, newMax);
            cellsBoard[newMax][0] = new Cell(id++, newMax, 0);
            cellsBoard[newMax][newMax] = new Cell(id++, newMax, newMax);
        }

        return cellsBoard;
    }

    private static void addParticle(Particle particle, Cell[][] cellsBoard, boolean periodicCondition, int m, int l) {
        int incM = periodicCondition ? m + 1 : m;
        for (int i = periodicCondition ? 1 : 0; i < incM; i++) {
            for (int j = periodicCondition ? 1 : 0; j < incM; j++) {
                Cell cell = cellsBoard[i][j];
                if (cell.getNumber() == particle.getCellNum()) {
                    cell.addParticle(particle);
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

}
