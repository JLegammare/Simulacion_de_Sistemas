package ar.edu.itba.simulacion.models;

import java.awt.*;

public class Body implements Comparable<Body> {

    private final int id;
    private final String name;
    private final double radius;
    private State state;
    private double mass;
    private final Color bodyColor;

    public Body(int id,String name, Pair<Double, Double> position, Pair<Double, Double> velocity, double radius, double mass, Color bodyColor) {
        this.id = id;
        this.name = name;
        this.state = new State(position, velocity);
        this.radius = radius;
        this.mass = mass;
        this.bodyColor = bodyColor;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public int getId() {
        return id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
    public Pair<Double, Double> getVelocity() {
        return state.getVelocity();
    }
    public Pair<Double, Double> getPosition() {
        return state.getPosition();
    }
    public void updateState(Pair<Double, Double> position, Pair<Double, Double> velocity) {
        this.state.setPosition(position);
        this.state.setVelocity(velocity);
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public int compareTo(Body o) {
        return this.id-o.id;
    }
}
