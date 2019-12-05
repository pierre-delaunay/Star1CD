package fr.istic.mob.star1cd.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.StarContract;
import fr.istic.mob.star1cd.database.model.BusRoute;

public class BusRouteProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return StarContract.BusRoutes.CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        if (getContext() != null){
            final AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
            appDatabase.busRouteDao().insertAll(BusRoute.fromContentValues(contentValues));
            if (id != 0){
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (getContext() != null){
            final AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
            appDatabase.busRouteDao().deleteById(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return 0;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    // https://openclassrooms.com/fr/courses/4568746-gerez-vos-donnees-localement-pour-avoir-une-application-100-hors-ligne/5106571-exposez-vos-donnees-avec-un-contentprovider
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
