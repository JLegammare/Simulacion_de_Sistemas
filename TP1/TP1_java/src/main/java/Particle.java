public class Particle {
    private double x;
    private double y;
    private double radius;
    public Particle(int L, double radius) {
        this.x= Math.random()*L;
        this.y= Math.random()*L;
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", y=" + y +
                ", radius=" + radius +
                '}';
    }
}
