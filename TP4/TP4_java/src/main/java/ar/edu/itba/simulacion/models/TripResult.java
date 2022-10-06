package ar.edu.itba.simulacion.models;

public class TripResult {
    private final boolean success;
    private final double distanceTraveled;
    private final double time;
    private final boolean finished;
    private final TripStatus ts;

    public TripResult(boolean finished, boolean success, double distanceTraveled, double time, TripStatus ts) {
        this.finished = finished;
        this.success = success;
        this.distanceTraveled = distanceTraveled;
        this.time = time;
        this.ts = ts;

    }
    public TripStatus getTs() {
        return ts;
    }

    public boolean isSuccess() {
        return success;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public double getTime() {
        return time;
    }

    public boolean isFinished() {
        return finished;
    }

    public enum TripStatus{
        TRAVELLING,
        LEAVING_EARTH,
        VENUS_SUCCESS,
        OTHER_COLLISION,
        SPACESHIP_LOST
    }
}
