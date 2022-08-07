public class Particle {
    private double x;
    private double y;
    public Particle(int L) {
        this.x= Math.random()*L;
        this.y= Math.random()*L;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
