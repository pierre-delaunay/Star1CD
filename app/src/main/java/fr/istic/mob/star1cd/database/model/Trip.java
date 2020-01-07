package fr.istic.mob.star1cd.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import fr.istic.mob.star1cd.database.StarContract;

/**
 * Trip model
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
@Entity(foreignKeys = {
        @ForeignKey(
                entity = BusRoute.class,
                parentColumns = StarContract.BusRoutes.BusRouteColumns._ID,
                childColumns = StarContract.Trips.TripColumns.ROUTE_ID
        ),
        @ForeignKey(
                entity = Calendar.class,
                parentColumns = StarContract.Calendar.CalendarColumns._ID,
                childColumns = StarContract.Trips.TripColumns.SERVICE_ID
        )
})
public class Trip {
    
    @PrimaryKey
    @ColumnInfo(name = StarContract.Trips.TripColumns._ID)
    private int id;
    @ColumnInfo(name = StarContract.Trips.TripColumns.ROUTE_ID)
    private int routeId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.SERVICE_ID)
    private int serviceId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.HEADSIGN)
    private String tripHeadsign;
    @ColumnInfo(name = StarContract.Trips.TripColumns.DIRECTION_ID)
    private String directionId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.BLOCK_ID)
    private String blockId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE)
    private int wheelchairAccessible;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public void setTripHeadsign(String tripHeadsign) {
        this.tripHeadsign = tripHeadsign;
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public int getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(int wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }
}