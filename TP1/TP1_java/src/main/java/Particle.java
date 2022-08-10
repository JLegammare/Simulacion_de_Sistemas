import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle {
    private int id;
    private double x;
    private double y;
    private double radius;
    private int cellNum;
    private List<Particle> neighbors = new ArrayList<>();

    private double property;

    public Particle(int id, double boardLength, double radius,double property) {
        this.id=id;
        this.x= Math.random()*boardLength;
        this.y= Math.random()*boardLength;
        this.radius = radius;
        this.property = property;
    }

        public Particle(int id, double x, double y, double radius,double property, int L, int M) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.property = property;
        this.cellNum = calculateCellNum(x, y, L, M);
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                ", property=" + property +
                '}';
    }

    public double calculateDistance(Particle p){
        double a = Math.sqrt(Math.pow(x-p.x,2) + Math.pow(y-p.y,2))-p.radius-radius;
        return a;
    }

    public boolean checkIfNeighbor(Particle p,double rc){
        return !this.equals(p) && calculateDistance(p) <= rc;
    }

    public void addNeighbor(Particle p){
        neighbors.add(p);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public int getId() {
        return id;
    }

    public int getCellNum() {
        return cellNum;
    }

    public List<Particle> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Particle> neighbors) {
        this.neighbors = neighbors;
    }

    public int calculateCellNum(double x, double y, int L, int M){
        int col = (int) x/L;
        int row = (int) y/L;

        return col + M*(row % M);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return id == particle.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
