package fr.istic.mob.star1cd.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import fr.istic.mob.star1cd.database.model.Stop;

/**
 * Stop DAO
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
@Dao
public interface StopDao {

    @Delete
    void delete(Stop stop);

    @Query("DELETE FROM stop")
    void deleteAll();

    @Insert
    void insertAll(Stop... stops);

    @Query("SELECT * FROM stop")
    List<Stop> getAll();

    @Query("SELECT * FROM stop WHERE _id = :id")
    Stop findById(int id);

    @Query("SELECT * FROM Stop, StopTime, Trip " +
            "WHERE StopTime.trip_id = Trip._id " +
            "AND StopTime.stop_id = Stop._id " +
            "AND Trip.route_id = :routeId " +
            "AND Trip._id = :tripId " +
            "ORDER BY StopTime.arrival_time")
    Cursor findStops(int tripId, int routeId);

    @RawQuery
    List<Stop> getStops(SupportSQLiteQuery query);
}
