import java.util.Objects;

public class Particle {
    private int id;
    private double x;
    private double y;
    private double radius;

    private double property;

    public Particle(int id, double boardLength, double radius,double property) {
        this.id=id;
        this.x= Math.random()*boardLength;
        this.y= Math.random()*boardLength;
        this.radius = radius;
        this.property = property;
    }

        public Particle(int id, double x, double y, double radius,double property) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
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
