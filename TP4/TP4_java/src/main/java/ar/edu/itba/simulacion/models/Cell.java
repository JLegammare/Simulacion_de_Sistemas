package ar.edu.itba.simulacion.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cell implements Comparable<Cell> {

    private final Pair<Integer,Integer> cellCoordinates;
    private final List<Particle> particles = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return cellCoordinates.equals(cell.cellCoordinates) && Objects.equals(particles, cell.particles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellCoordinates, particles);
    }

    public Pair<Integer, Integer> getCellCoordinates() {
        return cellCoordinates;
    }

    @Override
    public String toString() {
        return "ar.edu.itba.simulacion.models.Cell{" +
                "coordinates=" + cellCoordinates +
                ", particles=" + particles +
                '}';
    }

    public Cell(int x, int y) {
        this.cellCoordinates = new Pair<>(x,y);
    }

    public int getX() {
        return cellCoordinates.getX_value();
    }

    public int getY() {
        return cellCoordinates.getY_value();
    }

    public void addParticle(Particle p){
        particles.add(p);
    }

    public void addParticles(List<Particle> new_particles){
        particles.addAll(new_particles);
    }


    public List<Particle> getParticles() {
        return particles;
    }

    @Override
    public int compareTo(Cell o) {
        int xComparation = this.getX() - o.getX();
        return xComparation != 0?xComparation: this.getY() - o.getY();
    }
}
