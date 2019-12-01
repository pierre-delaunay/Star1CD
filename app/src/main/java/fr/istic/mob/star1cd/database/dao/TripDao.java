package fr.istic.mob.star1cd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.Trip;

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
}
