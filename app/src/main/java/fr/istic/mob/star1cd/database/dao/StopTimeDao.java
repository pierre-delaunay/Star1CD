package fr.istic.mob.star1cd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.StopTime;

@Dao
public interface StopTimeDao {

    @Insert
    void insertAll(StopTime... stopTimes);

    @Delete
    void delete(StopTime stopTime);

    @Query("DELETE FROM stoptime")
    void deleteAll();

    @Query("SELECT * FROM stoptime")
    List<StopTime> getAll();

    @Query("SELECT * FROM stoptime WHERE _id = :id")
    StopTime findById(int id);
}
