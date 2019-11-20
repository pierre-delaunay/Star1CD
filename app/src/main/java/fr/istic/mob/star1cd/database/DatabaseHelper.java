package fr.istic.mob.star1cd.database;

import android.content.Context;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;

    private static final String DATABASE_NAME = "star.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BUS_ROUTE = StarContract.BusRoutes.CONTENT_PATH;
    private static final String TABLE_TRIP = StarContract.Trips.CONTENT_PATH;
    private static final String TABLE_STOP = StarContract.Stops.CONTENT_PATH;
    private static final String TABLE_STOP_TIME = StarContract.StopTimes.CONTENT_PATH;
    private static final String TABLE_CALENDAR = StarContract.Calendar.CONTENT_PATH;

    private static final String CREATE_TABLE_BUS_ROUTE = "CREATE TABLE IF NOT EXISTS " + TABLE_BUS_ROUTE + " (" +
            StarContract.BusRoutes.BusRouteColumns._ID + " INTEGER PRIMARY KEY," +
            StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + " TEXT," +
            StarContract.BusRoutes.BusRouteColumns.LONG_NAME + " TEXT," +
            StarContract.BusRoutes.BusRouteColumns.DESCRIPTION +" TEXT," +
            StarContract.BusRoutes.BusRouteColumns.TYPE + " INTEGER," +
            StarContract.BusRoutes.BusRouteColumns.COLOR + " TEXT," +
            StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + " TEXT );";

    private static final String CREATE_TABLE_TRIP = "CREATE TABLE IF NOT EXISTS " + TABLE_TRIP +" (" +
            StarContract.Trips.TripColumns._ID + " INTEGER PRIMARY KEY, " +
            StarContract.Trips.TripColumns.SERVICE_ID + " INTEGER, " +
            StarContract.Trips.TripColumns.ROUTE_ID + " INTEGER, " +
            StarContract.Trips.TripColumns.HEADSIGN + " TEXT, " +
            StarContract.Trips.TripColumns.DIRECTION_ID + " INTEGER, " +
            StarContract.Trips.TripColumns.BLOCK_ID + " TEXT, " +
            StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE + " INTEGER );";

    private static final String CREATE_TABLE_STOP = "CREATE TABLE IF NOT EXISTS " + TABLE_STOP +" (" +
            StarContract.Stops.StopColumns._ID + " TEXT PRIMARY KEY, " +
            StarContract.Stops.StopColumns.DESCRIPTION  + " TEXT, " +
            StarContract.Stops.StopColumns.LATITUDE + " REAL, " +
            StarContract.Stops.StopColumns.LONGITUDE+ " REAL, " +
            StarContract.Stops.StopColumns.NAME+ " TEXT, " +
            StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING + " INTEGER );";

    private static final String CREATE_TABLE_STOP_TIME = "CREATE TABLE IF NOT EXISTS " + TABLE_STOP_TIME +" (" +
            StarContract.StopTimes.StopTimeColumns._ID+" INTEGER PRIMARY KEY," +
            StarContract.StopTimes.StopTimeColumns.TRIP_ID+" INTEGER," +
            StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME+" TEXT," +
            StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME+" TEXT," +
            StarContract.StopTimes.StopTimeColumns.STOP_ID+" TEXT," +
            StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE+" INTEGER );";

    private static final String CREATE_TABLE_CALENDAR = "CREATE TABLE IF NOT EXISTS " + TABLE_CALENDAR +" (" +
            StarContract.Calendar.CalendarColumns._ID +" INTEGER PRIMARY KEY," +
            StarContract.Calendar.CalendarColumns.MONDAY+" INTEGER," +
            StarContract.Calendar.CalendarColumns.TUESDAY+" INTEGER," +
            StarContract.Calendar.CalendarColumns.WEDNESDAY+" INTEGER," +
            StarContract.Calendar.CalendarColumns.THURSDAY+" INTEGER," +
            StarContract.Calendar.CalendarColumns.FRIDAY+" INTEGER," +
            StarContract.Calendar.CalendarColumns.SATURDAY+" INTEGER," +
            StarContract.Calendar.CalendarColumns.SUNDAY+" INTEGER," +
            StarContract.Calendar.CalendarColumns.START_DATE+" INTEGER," +
            StarContract.Calendar.CalendarColumns.END_DATE +" INTEGER );";


    /**
     * Abstract Factory
     * @param ctx
     * @return
     */
    public static DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * DatabaseHelper constructor
     * @param context Context
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    /**
     * onCreate
     * @param database SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_BUS_ROUTE);
        database.execSQL(CREATE_TABLE_TRIP);
        database.execSQL(CREATE_TABLE_STOP);
        database.execSQL(CREATE_TABLE_STOP_TIME);
        database.execSQL(CREATE_TABLE_CALENDAR);
    }

    /**
     * onUpgrade
     * @param database SQLiteDatabase
     * @param oldVersion int older version
     * @param newVersion int new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_ROUTE);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STOP);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STOP_TIME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CALENDAR);
        onCreate(database);
    }
}
