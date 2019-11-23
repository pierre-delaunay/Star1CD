package fr.istic.mob.star1cd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import fr.istic.mob.star1cd.database.model.BusRoute;

@Dao
public interface BusRouteDao {

    @Insert
    void insertAll(BusRoute... busRoute);

    @Delete
    void delete(BusRoute busRoute);

    @Query("SELECT * FROM busroute")
    List<BusRoute> getAll();

    @Query("SELECT * FROM busroute WHERE _id = :id")
    BusRoute findById(int id);
}
