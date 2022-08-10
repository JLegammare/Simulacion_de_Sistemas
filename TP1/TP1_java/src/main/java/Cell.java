import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cell {

    private int number;
    private final List<Particle> particles = new ArrayList<>();
    private final Set<Particle> possibleNeighbors = new HashSet<>();
    private final List<Cell> halfNeighborCells = new ArrayList<>(); //with borders

    public Cell(int number) {
        this.number = number;
    }

    public void addParticle(Particle p){
        particles.add(p);
    }

    public void addHalfNeighbors(List<Cell> cells, int M){

        //TODO: Cambiar las repeticiones de number + M o similares por una variable asi no se repite tanto la misma operacion
        //add right cell
        if((number + 1) % M > 0){
            halfNeighborCells.add(cells.get(number + 1));
        }
        //add diagonal down right cell
        if((number + M) < M*M && (number + M + 1) % M > 0 ) {
            halfNeighborCells.add(cells.get(number + M + 1));
        }
        //add upper cell
        if( number - M >= 0 && ((number - M) % M > 0 || (number - M) == 0)){
            halfNeighborCells.add(cells.get(number - M));
        }
        //add diagonal up cell
        if((number - M + 1) % M != 0 && number - M >= 0){
            halfNeighborCells.add(cells.get(number - M + 1));
        }

    }

   /* public void addHalfNeighborsNoBorders(List<Cell> cells, int M){

        //TODO: Cambiar las repeticiones de number + M o similares por una variable asi no se repite tanto la misma operacion
        //add right cell
        if((number + 1) % M == 0){
            halfNeighborCells.add(cells.get(number - M + 1));
        }
        //add diagonal down right cell
        if((number + M) < M*M && (number + M + 1) % M > 0 ) {
            halfNeighborCells.add(cells.get(number + M + 1));
        }
        //add upper cell
        if( number - M >= 0 && ((number - M) % M > 0 || (number - M) == 0)){
            halfNeighborCells.add(cells.get(number - M));
        }
        //add diagonal up cell
        if((number - M + 1) % M != 0 && number - M >= 0){
            halfNeighborCells.add(cells.get(number - M + 1));
        }

    }
*/

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

}
