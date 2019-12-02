package fr.istic.mob.star1cd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.Stop;

/**
 * Stop DAO
 * @version 1.0.1
 * @author Charly C, Pierre D
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
}
