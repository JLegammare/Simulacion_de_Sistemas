package ar.edu.itba.simulacion.models;

public class TripResult implements Comparable<TripResult>{
    private final double distanceTraveled;
    private final double time;
    private final TripStatus ts;
    private double minDistance;

    public TripResult( double distanceTraveled, double time, TripStatus ts) {
        this.distanceTraveled = distanceTraveled;
        this.time = time;
        this.ts = ts;

    }
    public TripStatus getTs() {
        return ts;
    }



    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public double getTime() {
        return time;
    }

    public boolean isFinished() {
        return !this.ts.equals(TripStatus.TRAVELLING);
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    @Override
    public int compareTo(TripResult o) {
        return Double.compare(minDistance,o.minDistance);
    }


}
