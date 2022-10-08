package ar.edu.itba.simulacion.models;

public class TripResult implements Comparable<TripResult>{
    private final double distanceTraveled;
    private final double time;
    private final TripStatus ts;
    private final Body bodyCollision;
    private double minDistance;

    public TripResult( double distanceTraveled, double time, TripStatus ts, Body collisionBodies) {
        this.distanceTraveled = distanceTraveled;
        this.time = time;
        this.ts = ts;
        this.bodyCollision = collisionBodies;

    }
    public TripStatus getTs() {
        return ts;
    }

    public Body getBodyCollision() {
        return bodyCollision;
    }


    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public double getTime() {
        return time;
    }

    public boolean isFinished() {
        return !this.ts.equals(TripStatus.TRAVELLING) && !this.ts.equals(TripStatus.LEAVING_EARTH);
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    @Override
    public int compareTo(TripResult o) {
        return 0;
    }

    public enum TripStatus{
        TRAVELLING,
        LEAVING_EARTH,
        VENUS_SUCCESS,
        OTHER_COLLISION,
        SPACESHIP_LOST
    }
}
