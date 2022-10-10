package ar.edu.itba.simulacion.models;

import java.time.LocalDate;
import java.util.Date;

public class TripResult implements Comparable<TripResult>{
    private final double distanceTraveled;
    private Date startDate;
    private double time;
    private final TripStatus ts;
    private double minDistance;
    private double initSpaceshipSpeed;

    public double getInitSpaceshipSpeed() {
        return initSpaceshipSpeed;
    }

    public void setInitSpaceshipSpeed(double initSpaceshipSpeed) {
        this.initSpaceshipSpeed = initSpaceshipSpeed;
    }

    public TripResult(double distanceTraveled, double time, TripStatus ts) {
        this.distanceTraveled = distanceTraveled;
        this.time = time;
        this.ts = ts;

    }
    public TripStatus getTs() {
        return ts;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
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


    public Date getStartDate() {
        return startDate;
    }
}
