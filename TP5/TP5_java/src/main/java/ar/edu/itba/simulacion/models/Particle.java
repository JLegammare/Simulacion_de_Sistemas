package ar.edu.itba.simulacion.models;

import java.awt.*;
import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class  Particle implements Comparable<Particle> {
    private final int ID;
    private State state;
    private final Color particleColor;
    private final double radius;
    private final double mass;


    public Particle(int id, Pair<Double,Double> initPosition,Pair<Double,Double> initVelocity, double radius, double mass, Color particleColor) {
        this.ID = id;
        this.state = new State(initPosition,initVelocity);
        this.radius = radius;
        this.mass = mass;
        this.particleColor = particleColor;
    }

    public Color getParticleColor() {
        return particleColor;
    }

    public State getState() {
        return state;
    }

    public Pair<Double,Double> getPosition(){
        return this.state.getPosition();
    }

    public Pair<Double,Double> getVelocity(){
        return this.state.getVelocity();
    }

    public void setVelocity(Pair<Double,Double> velocity){
        this.state.setVelocity(velocity);
    }

    public void setPosition(Pair<Double,Double> position){
        this.state.setPosition(position);
    }

    public double getMass() {
        return mass;
    }

    @Override
    public String toString() {
        return "Particle{" +
                "ID=" + ID +
                ", state=" + state +
                ", particleColor=" + particleColor +
                ", radius=" + radius +
                ", mass=" + mass +
                '}';
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
