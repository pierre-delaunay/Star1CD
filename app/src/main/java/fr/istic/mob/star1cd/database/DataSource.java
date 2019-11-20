package fr.istic.mob.star1cd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import fr.istic.mob.star1cd.database.model.BusRoute;
import fr.istic.mob.star1cd.database.model.Calendar;
import fr.istic.mob.star1cd.database.model.Stop;
import fr.istic.mob.star1cd.database.model.StopTime;
import fr.istic.mob.star1cd.database.model.Trip;

public class DataSource {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    public DataSource(Context context) {
        this.databaseHelper = DatabaseHelper.getInstance(context);
        open();
    }

    public void restart() {
        this.databaseHelper.onUpgrade(database, 1, 1);
    }

    public void open() throws SQLException {
         this.database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        database.close();
    }

    public void insertBusRoute(BusRoute busRoute){
        ContentValues values = new ContentValues();

        values.put(StarContract.BusRoutes.BusRouteColumns._ID,busRoute.getId());
        values.put(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME,busRoute.getRouteShortName());
        values.put(StarContract.BusRoutes.BusRouteColumns.LONG_NAME,busRoute.getRouteLongName());
        values.put(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION,busRoute.getRouteDescription());
        values.put(StarContract.BusRoutes.BusRouteColumns.TYPE,busRoute.getRouteType());
        values.put(StarContract.BusRoutes.BusRouteColumns.COLOR,busRoute.getRouteColor());
        values.put(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR,busRoute.getRouteTextColor());

        database.insert(StarContract.BusRoutes.CONTENT_PATH,null,values);
    }

    public void insertTrip(Trip trip) {
        ContentValues values = new ContentValues();

        values.put(StarContract.Trips.TripColumns._ID,trip.getId());
        values.put(StarContract.Trips.TripColumns.SERVICE_ID,trip.getServiceId());
        values.put(StarContract.Trips.TripColumns.ROUTE_ID,trip.getRouteId());
        values.put(StarContract.Trips.TripColumns.HEADSIGN,trip.getTripHeadsign());
        values.put(StarContract.Trips.TripColumns.DIRECTION_ID,trip.getDirectionId());
        values.put(StarContract.Trips.TripColumns.BLOCK_ID,trip.getBlockId());
        values.put(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE,trip.getWheelchairAccessible());

        database.insert(StarContract.Trips.CONTENT_PATH,null,values);
    }

    public void insertStop(Stop stop) {
        ContentValues values = new ContentValues();

        values.put(StarContract.Stops.StopColumns._ID,stop.getId());
        values.put(StarContract.Stops.StopColumns.DESCRIPTION,stop.getStopDesc());
        values.put(StarContract.Stops.StopColumns.LATITUDE,stop.getStopLat());
        values.put(StarContract.Stops.StopColumns.LONGITUDE,stop.getStopLon());
        values.put(StarContract.Stops.StopColumns.NAME,stop.getStopName());
        values.put(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING,stop.getWheelchairBoarding());

        database.insert(StarContract.Stops.CONTENT_PATH,null,values);
    }

    public void insertStopTime(StopTime stopTime) {
        ContentValues values = new ContentValues();

        values.put(StarContract.StopTimes.StopTimeColumns._ID,stopTime.getId());
        values.put(StarContract.StopTimes.StopTimeColumns.TRIP_ID,stopTime.getTripId());
        values.put(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME,stopTime.getArrivalTime());
        values.put(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME,stopTime.getDepartureTime());
        values.put(StarContract.StopTimes.StopTimeColumns.STOP_ID,stopTime.getStopId());
        values.put(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE,stopTime.getStopSequence());

        database.insert(StarContract.StopTimes.CONTENT_PATH,null,values);
    }

    public void insertCalendar(Calendar calendar) {
        ContentValues values = new ContentValues();

        values.put(StarContract.Calendar.CalendarColumns._ID,calendar.getId());
        values.put(StarContract.Calendar.CalendarColumns.MONDAY,calendar.getMonday());
        values.put(StarContract.Calendar.CalendarColumns.TUESDAY,calendar.getTuesday());
        values.put(StarContract.Calendar.CalendarColumns.WEDNESDAY,calendar.getWednesday());
        values.put(StarContract.Calendar.CalendarColumns.THURSDAY,calendar.getThursday());
        values.put(StarContract.Calendar.CalendarColumns.FRIDAY,calendar.getFriday());
        values.put(StarContract.Calendar.CalendarColumns.SATURDAY,calendar.getSaturday());
        values.put(StarContract.Calendar.CalendarColumns.SUNDAY,calendar.getSunday());
        values.put(StarContract.Calendar.CalendarColumns.START_DATE,calendar.getStartDate());
        values.put(StarContract.Calendar.CalendarColumns.END_DATE,calendar.getEndDate());

        database.insert(StarContract.Calendar.CONTENT_PATH,null,values);
    }
}
