package ar.edu.itba.simulacion.models;

import java.util.Objects;

public class State implements Comparable<State> {

    private Pair<Double,Double> position;
    private Pair<Double,Double> velocity;


    public State(Pair<Double, Double> position, Pair<Double, Double> velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Pair<Double, Double> getPosition() {
        return position;
    }

    public void setPosition(Pair<Double, Double> position) {
        this.position = position;
    }

    public Pair<Double, Double> getVelocity() {
        return velocity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(position, state.position) && Objects.equals(velocity, state.velocity);
    }

    @Override
    public String toString() {
        return "State{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, velocity);
    }

    public void setVelocity(Pair<Double, Double> velocity) {
        this.velocity = velocity;
    }

    @Override
    public int compareTo(State o) {

        int compToPosition = this.position.compareTo(o.position);

        if(compToPosition==0)
            return this.velocity.compareTo(o.position);

        return compToPosition;
    }
}
