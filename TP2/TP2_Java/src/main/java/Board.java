import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private final Map<Cell, List<Cell>> neighborCells = new TreeMap<>();
    private final Map<Pair<Integer, Integer>, Cell> cellMap = new TreeMap<>();

    private final double l;
    private final int m;
    private final boolean periodicCondition;

    private final double boardSideLength;

    public Board(int m, double l, boolean periodicCondition,double rc) {
        validateParams(m,l,rc);
        this.periodicCondition = periodicCondition;
        this.m = m;
        this.l = l;
        this.boardSideLength = m * l;
        createCells(periodicCondition);
    }

    private void validateParams(int m,double l, double rc ) {
        if (l / m <= rc) {
            throw new RuntimeException("CONDITION IS NOT SATISFIED!");
        }
    }

    private void createCells(boolean periodicCondition) {

        int lastIndex;
        int initialIndex;

        if (periodicCondition) {
            initialIndex = -1;
            lastIndex = m;
        } else {
            initialIndex = 0;
            lastIndex = m - 1;
        }
        for (int i = initialIndex; i <= lastIndex; i++) {
            for (int j = initialIndex; j <= lastIndex; j++) {
                Cell cell = new Cell(i, j);
                neighborCells.put(cell, new ArrayList<>());
                cellMap.put(new Pair<>(i, j), cell);
            }
        }

        neighborCells.keySet().forEach(cell -> addHalfNeighborsCells(cell, neighborCells));

    }

    private void addHalfNeighborsCells(Cell cell, Map<Cell, List<Cell>> cellsBoard) {

        Set<Cell> cellsSet = cellsBoard.keySet();
        List<Cell> selectedNeighborhood = cellsBoard.get(cell);

        int cellY = cell.getY();
        int cellX = cell.getX();

        selectedNeighborhood.addAll(cellsSet.stream().filter(cell2 -> {
            int cell2Y = cell2.getY();
            int cell2X = cell2.getX();

            // right
            if (cell2Y == cellY && cell2X == cellX + 1) {
                return true;
            }
            // top right
            else if (cell2Y == cellY + 1 && cell2X == cellX + 1) {
                return true;
            }
            // top
            else if (cell2Y == cellY + 1 && cell2X == cellX) {
                return true;
            }
            //bottom right
            else return cell2Y == cellY - 1 && cell2X == cellX + 1;

        }).collect(Collectors.toList()));


    }

    public void addParticlesToBoard(List<Particle> particles) {

        neighborCells.keySet().forEach(cell -> cell.getParticles().clear());
        Set<Cell> cells = neighborCells.keySet();

        for (Particle particle : particles) {
            for (Cell cell : cells) {
                if (cell.getCellCoordinates().equals(getParticleCell(particle, l))) {
                    cell.addParticle(particle);
                }
            }
        }

        if (periodicCondition) {

            int lastIndex = m - 1;

            for (int i = 0; i <= lastIndex; i++) {

                //bottom side
                addPeriodicParticles(new Pair<>(i, -1), new Pair<>(i, lastIndex), 0, -boardSideLength);
                //top side
                addPeriodicParticles(new Pair<>(i, m), new Pair<>(i, 0), 0, boardSideLength);
                //left side
                addPeriodicParticles(new Pair<>(-1, i), new Pair<>(lastIndex, i), -boardSideLength, 0);
                // right side
                addPeriodicParticles(new Pair<>(m, i), new Pair<>(0, i), boardSideLength, 0);

            }

            //corners
            addPeriodicParticles(new Pair<>(-1, -1), new Pair<>(lastIndex, lastIndex), -boardSideLength, -boardSideLength);
            addPeriodicParticles(new Pair<>(m, m), new Pair<>(0, 0), boardSideLength, boardSideLength);
            addPeriodicParticles(new Pair<>(-1, m), new Pair<>(lastIndex, m), -boardSideLength, boardSideLength);
            addPeriodicParticles(new Pair<>(m, -1), new Pair<>(0, lastIndex), boardSideLength, -boardSideLength);

        }
    }

    private void addPeriodicParticles(Pair<Integer, Integer> borderCoordinates, Pair<Integer, Integer> cellCoordinates, double xTranslation, double yTranslation) {

        Cell cell = cellMap.get(cellCoordinates);
        Cell borderCell = cellMap.get(borderCoordinates);
        borderCell.addParticles(cell.getParticles().stream().map(
                        p -> new Particle(
                                p.getID(),
                                p.getX() + xTranslation,
                                p.getY() + yTranslation, p.getRadius(),
                                p.getProperty(),p.getVelocityModule(),p.getOmega(),p.getDeltaOmega()))
                .collect(Collectors.toList()));
    }


    private static Pair<Integer, Integer> getParticleCell(Particle p, double l) {
        double x = p.getX();
        double y = p.getY();

        int col = (int) (y / l);
        int row = (int) (x / l);

        return new Pair<>(row, col);

    }

    public Map<Particle, Set<Particle>> getAllNeighbors(double rc) {

        Set<Cell> cells = neighborCells.keySet();
        Map<Particle, Set<Particle>> neighborhoods = new TreeMap<>();

        for (Cell cell : cells) {
            List<Particle> possibleNeighbors = new ArrayList<>();
            neighborCells.get(cell).stream().map(Cell::getParticles).forEach(possibleNeighbors::addAll);
            possibleNeighbors.addAll(cell.getParticles());
            possibleNeighbors.forEach(particle -> {
                Set<Particle> p = neighborhoods.get(particle);
                neighborhoods.put(particle, possibleNeighbors.stream().filter(particle1 ->
                        particle1.checkIfNeighbor(particle, rc)).collect(Collectors.toSet()));
                if(p!=null){
                    neighborhoods.get(particle).addAll(p);
                }
            });
        }

        return neighborhoods;
    }


}
