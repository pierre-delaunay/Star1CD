package fr.istic.mob.star1cd.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import fr.istic.mob.star1cd.database.StarContract;

/**
 * Stop Time model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
@Entity
public class StopTime {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns._ID)
    private int id;
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.TRIP_ID)
    private int tripId;
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME)
    private String arrivalTime;
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME)
    private String departureTime;
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.STOP_ID)
    private String stopId;
    @ColumnInfo(name = StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE)
    private int stopSequence;

    public StopTime () {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }
}