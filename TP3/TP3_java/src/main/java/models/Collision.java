package models;

import java.util.Objects;

public class Collision implements Comparable<Collision>{

    private  final CollisionType cType;
    private final Particle particle;
    private final double collisionTime;

    public double getCollisionTime() {
        return collisionTime;
    }

    public Collision(CollisionType cType, Particle particle, double collisionTime) {
        this.cType = cType;
        this.particle = particle;
        this.collisionTime = collisionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collision collision = (Collision) o;
        return Double.compare(collision.collisionTime, collisionTime) == 0 && cType == collision.cType && Objects.equals(particle, collision.particle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cType, particle, collisionTime);
    }

    @Override
    public int compareTo(Collision o) {
        return Double.compare(this.collisionTime,o.collisionTime);
    }
}
