package fr.istic.mob.star1cd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.Trip;

/**
 * Trip DAO
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
@Dao
public interface TripDao {

    @Insert
    void insertAll(Trip... trips);

    @Insert
    void insertAll(List<Trip> trips);

    @Delete
    void delete(Trip trip);

    @Query("DELETE FROM trip")
    void deleteAll();

    @Query("SELECT * FROM trip")
    List<Trip> getAll();

    @Query("SELECT * FROM trip WHERE _id = :id")
    Trip findById(int id);

    @Query("SELECT * FROM trip WHERE route_id = :routeId AND direction_id = :directionId LIMIT 1")
    Trip find(int routeId, int directionId);
}
