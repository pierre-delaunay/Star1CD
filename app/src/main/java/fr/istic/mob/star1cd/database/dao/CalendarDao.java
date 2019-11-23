package fr.istic.mob.star1cd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.Calendar;

@Dao
public interface CalendarDao {

    @Insert
    void insertAll(Calendar... calendars);

    @Delete
    void delete(Calendar calendar);

    @Query("DELETE FROM calendar")
    void deleteAll();

    @Query("SELECT * FROM calendar")
    List<Calendar> getAll();

}
