package fr.istic.mob.star1cd.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.StopTime;
/**
 * StopTime DAO
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
@Dao
public interface StopTimeDao {

    @Insert
    void insertAll(StopTime... stopTimes);

    @Insert
    void insertAll(List<StopTime> stopTimes);

    @Delete
    void delete(StopTime stopTime);

    @Query("DELETE FROM stoptime")
    void deleteAll();

    @Query("SELECT * FROM stoptime")
    List<StopTime> getAll();

    @Query("SELECT * FROM stoptime WHERE _id = :id")
    StopTime findById(int id);

    @Query("SELECT * FROM StopTime, Stop, Trip, Calendar " +
            "WHERE Stop._id = StopTime.stop_id " +
            "AND StopTime.trip_id = Trip._id " +
            "AND Calendar._id = Trip.service_id " +
            "AND Stop._id = :stopId " +
            "AND Trip.route_id = :routeId " +
            "AND Calendar.end_date <= :endDate " +
            "AND Calendar.end_date > (:endDate - 1) " +
            "AND StopTime.arrival_time >= :arrivalTime")
    Cursor findStopTimesAtStop(String stopId, String routeId, long endDate, String arrivalTime);

    @Query("SELECT * FROM StopTime, Stop, Trip " +
            "WHERE Stop._id = StopTime.stop_id " +
            "AND StopTime.trip_id = Trip._id " +
            "AND Trip._id = :tripId " +
            "AND StopTime.arrival_time >= :arrivalTime")
    Cursor getRouteDetail(String tripId, String arrivalTime);
}
