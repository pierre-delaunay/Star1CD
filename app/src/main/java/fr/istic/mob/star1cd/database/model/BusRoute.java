package fr.istic.mob.star1cd.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import fr.istic.mob.star1cd.database.StarContract;

/**
 * Bus Route Model
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
@Entity
public class BusRoute {

    @PrimaryKey
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns._ID)
    private int id;
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.SHORT_NAME)
    private String routeShortName;
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.LONG_NAME)
    private String routeLongName;
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.DESCRIPTION)
    private String routeDescription;
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.TYPE)
    private String routeType;
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.COLOR)
    private String routeColor;
    @ColumnInfo(name = StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR)
    private String routeTextColor;

    public BusRoute() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public void setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public void setRouteColor(String routeColor) {
        this.routeColor = routeColor;
    }

    public String getRouteTextColor() {
        return routeTextColor;
    }

    public void setRouteTextColor(String routeTextColor) {
        this.routeTextColor = routeTextColor;
    }

}