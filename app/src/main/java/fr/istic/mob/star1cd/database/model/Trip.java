package fr.istic.mob.star1cd.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
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
                childColumns = StarContract.Trips.TripColumns.ROUTE_ID,
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Calendar.class,
                parentColumns = StarContract.Calendar.CalendarColumns._ID,
                childColumns = StarContract.Trips.TripColumns.SERVICE_ID,
                onDelete = ForeignKey.CASCADE
        )
}, indices = {@Index(value = {StarContract.Trips.TripColumns._ID}, unique = true)})
public class Trip {
    
    @PrimaryKey
    @ColumnInfo(name = StarContract.Trips.TripColumns._ID)
    private long id;
    @ColumnInfo(name = StarContract.Trips.TripColumns.ROUTE_ID)
    private int routeId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.SERVICE_ID)
    private long serviceId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.HEADSIGN)
    private String tripHeadsign;
    @ColumnInfo(name = StarContract.Trips.TripColumns.DIRECTION_ID)
    private String directionId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.BLOCK_ID)
    private String blockId;
    @ColumnInfo(name = StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE)
    private int wheelchairAccessible;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
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