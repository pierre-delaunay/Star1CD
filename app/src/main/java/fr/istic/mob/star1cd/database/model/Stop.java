package fr.istic.mob.star1cd.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fr.istic.mob.star1cd.database.StarContract;

/**
 * Stop model
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
@Entity(indices = {@Index(value = {StarContract.Stops.StopColumns._ID}, unique = true)})
public class Stop {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = StarContract.Stops.StopColumns._ID)
    private String id;
    @ColumnInfo(name = StarContract.Stops.StopColumns.NAME)
    private String stopName;
    @ColumnInfo(name = StarContract.Stops.StopColumns.DESCRIPTION)
    private String stopDesc;
    @ColumnInfo(name = StarContract.Stops.StopColumns.LATITUDE)
    private String stopLat;
    @ColumnInfo(name = StarContract.Stops.StopColumns.LONGITUDE)
    private String stopLon;
    @ColumnInfo(name = StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING)
    private int wheelchairBoarding;

    public Stop() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopDesc() {
        return stopDesc;
    }

    public void setStopDesc(String stopDesc) {
        this.stopDesc = stopDesc;
    }

    public String getStopLat() {
        return stopLat;
    }

    public void setStopLat(String stopLat) {
        this.stopLat = stopLat;
    }

    public String getStopLon() {
        return stopLon;
    }

    public void setStopLon(String stopLon) {
        this.stopLon = stopLon;
    }

    public int getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    public void setWheelchairBoarding(int wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
    }

}