package fr.istic.mob.star1cd.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.StarContract;
import fr.istic.mob.star1cd.database.model.Trip;

/**
 * Star Provider
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
public class StarProvider extends ContentProvider {

    // https://stackoverflow.com/questions/46804775/room-persistence-library-and-content-provider

    private static final int ALL_BUS_ROUTES = 1;
    private static final int BUS_ROUTE_BY_ID = 2;
    private static final int BUS_ROUTE_STOPS = 3;
    private static final int STOP_TIMES_AT_STOP = 4;
    private static final int ROUTE_DETAIL = 5;

    private static final UriMatcher URI_MATCHER =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH, ALL_BUS_ROUTES);
        //URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH + "/#", BUS_ROUTE_BY_ID);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH, BUS_ROUTE_STOPS);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.StopTimes.CONTENT_PATH, STOP_TIMES_AT_STOP);
        //URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH, ROUTE_DETAIL);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] selectionArgs, @Nullable String s1) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case ALL_BUS_ROUTES:
                return AppDatabase.getDatabase(getContext()).busRouteDao().selectAll();
            case BUS_ROUTE_BY_ID:
                int id = Integer.valueOf(uri.getLastPathSegment());
                return AppDatabase.getDatabase(getContext()).busRouteDao().selectById(id);
            case BUS_ROUTE_STOPS:
                String routeId = selectionArgs[0];
                String directionId = selectionArgs[1];
                Trip trip = AppDatabase.getDatabase(getContext()).tripDao().find(Integer.valueOf(routeId), Integer.valueOf(directionId));

                return AppDatabase.getDatabase(getContext()).stopDao().findStops(trip.getId(), Integer.valueOf(routeId));
            case STOP_TIMES_AT_STOP:
                // selectionArgs[0] : stop_id
                // selectionArgs[1] : route_id
                // selectionArgs[2] : endDate
                // selectionArgs[3] : arrivalTime
                return AppDatabase.getDatabase(getContext()).stopTimeDao().findStopTimesAtStop(selectionArgs[0],
                        selectionArgs[1],
                        Long.valueOf(selectionArgs[2]),
                        selectionArgs[3]);
            //case ROUTE_DETAIL:
                // selectionArgs[0] : trip_id
                // selectionArgs[1] : arrivalTime
                //return AppDatabase.getDatabase(getContext()).stopTimeDao().getRouteDetail(selectionArgs[0],selectionArgs[1]);
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ALL_BUS_ROUTES:
                return StarContract.BusRoutes.CONTENT_TYPE;
            case BUS_ROUTE_BY_ID:
                return StarContract.BusRoutes.CONTENT_TYPE;
            case BUS_ROUTE_STOPS:
                return StarContract.Stops.CONTENT_TYPE;
            case STOP_TIMES_AT_STOP:
                return StarContract.StopTimes.CONTENT_TYPE;
            case ROUTE_DETAIL:
                return StarContract.StopTimes.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
