import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Cell {

    private int number;
    private int x;
    private int y;
    private final List<Particle> particles = new ArrayList<>();
    private final Set<Particle> possibleNeighbors = new HashSet<>();

    public Cell(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public void addParticle(Particle p){
        particles.add(p);
    }

    public void addHalfNeighbors(Cell[][] cells, int M){


        //add right cell
        if((x+1) % M > 0 && x > 0){
            halfNeighborCells.add(cells[y][x+1]);
        }
        //add diagonal down right cell
        if((number + M) < M*M && (number + M + 1) % M > 0 ) {
          //  halfNeighborCells.add(cells.get(number + M + 1));
        }
        //add upper cell
        if( y > 0 && ((number - M) % M >= 0 || (number - M) == 0)){
         //   halfNeighborCells.add(cells.get(number - M));
        }
        //add diagonal up cell
        if((number - M + 1) % M != 0 && number - M >= 0){
       //     halfNeighborCells.add(cells.get(number - M + 1));
        }

        System.out.println(number + "--->" + halfNeighborCells.stream().map(Cell::getNumber).sorted().collect(Collectors.toList()) );

    }

//    public void addHalfNeighborsNoBorders(List<Cell> cells, int M){
//
//
//        //right || left
//        if((number + 1) % M == 0){
//            halfNeighborCells.add(cells.get(number - M + 1));
//        }
//        else if(number == 0 || (number - 1) % M  == (M-1)){
//            halfNeighborCells.add(cells.get(number - M + 1));
//        }
//        //up || down
//        if((number + M) > M*M ) {
//            halfNeighborCells.add(cells.get(number - M * (M - 1)));
//        }
//        else if( number - M < 0 ){
//            halfNeighborCells.add(cells.get(number + M * (M - 1)));
//        }
//
//        //add diagonal up cell
//        if((number - M + 1) % M != 0 && number - M >= 0){
//            halfNeighborCells.add(cells.get(number - M + 1));
//        }
//
//    }


    public Set<Particle> findPossibleNeighbors(){
        if(!particles.isEmpty()) {
            for (Cell cell : halfNeighborCells) {

                possibleNeighbors.addAll(cell.particles);
                possibleNeighbors.addAll(particles);
            }
        }
        return possibleNeighbors;

    }

    //TODO: hacer lo mismo pero sin bordes

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "number=" + number +
                ", particles=" + particles +
//                ", possibleNeighbors=" + possibleNeighbors +
//                ", halfNeighborCells=" + halfNeighborCells +
                '}';
    }
}
