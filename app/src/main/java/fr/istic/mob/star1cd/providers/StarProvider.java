package fr.istic.mob.star1cd.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.StarContract;

/**
 * Star Provider
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
public class StarProvider extends ContentProvider {

    // https://stackoverflow.com/questions/46804775/room-persistence-library-and-content-provider

    private static final int QUERY_ALL_BR = 1;
    private static final int QUERY_BY_BR_ID = 2;
    private static final int QUERY_ALL_STOPS = 3;
    //private static final int QUERY_STOP_TIMES = 4;

    private static final UriMatcher URI_MATCHER =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH, QUERY_ALL_BR);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.BusRoutes.CONTENT_PATH + "/#", QUERY_BY_BR_ID);
        URI_MATCHER.addURI(StarContract.AUTHORITY, StarContract.Stops.CONTENT_PATH, QUERY_ALL_STOPS);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        switch (URI_MATCHER.match(uri)) {
            case QUERY_ALL_BR:
                return AppDatabase.getDatabase(getContext()).busRouteDao().selectAll();
            case QUERY_BY_BR_ID:
                int id = Integer.valueOf(uri.getLastPathSegment());
                return AppDatabase.getDatabase(getContext()).busRouteDao().selectById(id);
            case QUERY_ALL_STOPS:
                return null;
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case QUERY_ALL_BR:
                return StarContract.BusRoutes.CONTENT_TYPE;
            case QUERY_BY_BR_ID:
                return StarContract.BusRoutes.CONTENT_ITEM_TYPE;
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
