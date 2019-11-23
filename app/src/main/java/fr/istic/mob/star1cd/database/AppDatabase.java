package fr.istic.mob.star1cd.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.istic.mob.star1cd.database.dao.BusRouteDao;
import fr.istic.mob.star1cd.database.model.BusRoute;

@Database(entities = BusRoute.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BusRouteDao busRouteDao();

}
