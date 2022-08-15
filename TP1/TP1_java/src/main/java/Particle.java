import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Particle implements Comparable<Particle> {
    private int id;
    private Pair<Double, Double> coordinates;
    private double radius;
    private double property;

//    public Particle(int id, double boardLength, double radius,double property) {
//        this.id=id;
//        this.x= Math.random()*boardLength;
//        this.y= Math.random()*boardLength;
//        this.radius = radius;
//        this.property = property;
//    }

    public Particle(int id, double x, double y, double radius, double property, int L, int M) {
        this.id = id;
        this.coordinates = new Pair<>(x, y);
        this.radius = radius;
        this.property = property;
    }

    public double getProperty() {
        return property;
    }

    public Particle(int id, double x, double y, double radius, double property) {
        this.id = id;
        this.coordinates = new Pair<>(x, y);
        this.radius = radius;
        this.property = property;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", x=" + coordinates.getX_value() +
                ", y=" + coordinates.getY_value() +
                ", radius=" + radius +
                ", property=" + property +
                '}';
    }

    public double calculateDistance(Particle p) {
        double x = coordinates.getX_value();
        double y = coordinates.getY_value();
        return Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2)) - p.radius - radius;
    }

    public boolean checkIfNeighbor(Particle p, double rc) {
        return !this.equals(p) && calculateDistance(p) <= rc;
    }


    public double getX() {
        return coordinates.getX_value();
    }

    public double getY() {
        return coordinates.getY_value();
    }

    public double getRadius() {
        return radius;
    }

    public int getId() {
        return id;
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

    @Override
    public int compareTo(Particle p) {
        return this.id-p.id;
    }
}
