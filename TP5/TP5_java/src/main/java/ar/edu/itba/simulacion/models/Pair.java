package ar.edu.itba.simulacion.models;

import java.util.Objects;

public class Pair<X extends Comparable<X>,Y extends Comparable<Y> > implements Comparable<Pair<X,Y>>{
    private  X x_value;
    private  Y y_value;

    public Pair(X x_value, Y y_value) {
        this.x_value = x_value;
        this.y_value = y_value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "x_value=" + x_value +
                ", y_value=" + y_value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return x_value.equals(pair.x_value) && y_value.equals(pair.y_value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x_value, y_value);
    }

    public X getX_value() {
        return x_value;
    }

    public void setX_value(X x_value) {
        this.x_value = x_value;
    }

    public Y getY_value() {
        return y_value;
    }

    public void setY_value(Y y_value) {
        this.y_value = y_value;
    }

    @Override
    public int compareTo(Pair<X,Y> o) {
        int xComparation = x_value.compareTo(o.x_value);
        return xComparation != 0?xComparation:  y_value.compareTo(o.y_value);
    }
}
