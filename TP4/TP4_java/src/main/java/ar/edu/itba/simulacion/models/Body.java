package ar.edu.itba.simulacion.models;

import java.util.Objects;

public class Body {

    private final int id;
    private final double radius;
    private State state;
    private double mass;

    public Body(int id, Pair<Double, Double> position, Pair<Double, Double> velocity, double radius, double mass) {
        this.id = id;
        this.state = new State(position, velocity);
        this.radius = radius;
        this.mass = mass;
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

}
