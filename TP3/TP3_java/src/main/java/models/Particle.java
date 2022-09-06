package models;

import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Particle implements Comparable<Particle> {
    private final int ID;
    private final Pair<Double, Double> coordinates;
    private final double radius;
    private final double property;
    private final double mass;
    private final double velocity;
    private double omega;
    private double deltaOmega;


    public Particle(int id, double x, double y, double radius, double property, double velocity, double omega, double deltaOmega, double mass) {
        this.ID = id;
        this.coordinates = new Pair<>(x, y);
        this.radius = radius;
        this.property = property;
        this.velocity = velocity;
        this.omega = omega;
        this.deltaOmega = deltaOmega;
        this.mass = mass;
    }



    public double getVelocityModule() {
        return velocity;
    }

    public double getXVelocity(){
        return cos(omega)*velocity;
    }

    public double getYVelocity(){
        return sin(omega)*velocity;
    }

    public void updatePosition(double newXValue, double newYValue, double boardLength){
          //si se va por la derecha
        if (newXValue > boardLength) {
            setX(newXValue - boardLength);
        }//si se va por izquierda
        else if (newXValue < 0)
            setX(boardLength + newXValue);
        else
            setX(newXValue);
        //si se va por arriba
        if (newYValue > boardLength) {
            setY(newYValue - boardLength);
        }//si se va por abajo
        else if (newYValue < 0)
            setY(boardLength + newYValue);
        else
            setY(newYValue);
    }

    public double getDeltaOmega() {
        return deltaOmega;
    }

    public void updateDeltaOmega(double n){
        this.deltaOmega = Math.random()*n-n/2;
    }

    public double getOmega() {
        return omega;
    }

    public void setOmega(double omega){
        this.omega = omega;
    }

    public double getProperty() {
        return property;
    }

    public double getMass() {
        return mass;
    }

    @Override
    public String toString() {
        return "models.Particle{" +
                "id=" + ID +
                ", coordinates=" + coordinates +
                ", radius=" + radius +
                ", property=" + property +
                ", velocity=" + velocity +
                ", omega=" + omega +
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

    public void setX(double x) {
        coordinates.setX_value(x);
    }
    public void setY(double y) {
        coordinates.setY_value(y);
    }

    public double getRadius() {
        return radius;
    }

    public int getID() {
        return ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Particle particle = (Particle) o;
        return ID == particle.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public int compareTo(Particle p) {
        return this.ID - p.ID;
    }
}
