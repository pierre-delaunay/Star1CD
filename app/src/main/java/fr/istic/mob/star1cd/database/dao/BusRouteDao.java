package fr.istic.mob.star1cd.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.BusRoute;

/**
 * Bus Route DAO
 *
 * @author Charly C, Pierre D
 * @version 1.0.1
 */
@Dao
public interface BusRouteDao {

    @Insert
    void insertAll(BusRoute... busRoute);

    @Delete
    void delete(BusRoute busRoute);

    @Query("DELETE FROM busroute WHERE _id = :id")
    void deleteById(long id);

    @Query("DELETE FROM busroute")
    void deleteAll();

    @Query("SELECT * FROM busroute")
    Cursor selectAll();

    @Query("SELECT * FROM busroute WHERE _id = :id")
    Cursor selectById(int id);

    @Query("SELECT * FROM busroute")
    List<BusRoute> getAll();

    @Query("SELECT * FROM busroute WHERE _id = :id")
    BusRoute findById(int id);

    @Query("SELECT * FROM busroute LIMIT 5")
    List<BusRoute> findAny();

    @Query("SELECT route_long_name FROM busroute WHERE route_short_name = :name")
    String findRouteLongName(String name);

    @Query("SELECT route_text_color FROM busroute WHERE route_short_name = :name")
    String findRouteTextColor(String name);

    @Query("SELECT route_color FROM busroute WHERE route_short_name = :name")
    String findRouteColor(String name);
}
