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

    public Cell(int number, int x, int y) {
        this.number = number;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addParticle(Particle p){
        particles.add(p);
    }

    public void addParticles(List<Particle> new_particles){
        particles.addAll(new_particles);
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
