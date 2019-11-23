package fr.istic.mob.star1cd.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.istic.mob.star1cd.database.dao.BusRouteDao;
import fr.istic.mob.star1cd.database.dao.CalendarDao;
import fr.istic.mob.star1cd.database.dao.StopDao;
import fr.istic.mob.star1cd.database.dao.StopTimeDao;
import fr.istic.mob.star1cd.database.dao.TripDao;
import fr.istic.mob.star1cd.database.model.BusRoute;
import fr.istic.mob.star1cd.database.model.Calendar;
import fr.istic.mob.star1cd.database.model.Stop;
import fr.istic.mob.star1cd.database.model.StopTime;
import fr.istic.mob.star1cd.database.model.Trip;

@Database(entities = {BusRoute.class, Calendar.class, Stop.class, StopTime.class, Trip.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BusRouteDao busRouteDao();
    public abstract CalendarDao calendarDAO();
    public abstract StopDao stopDao();
    public abstract StopTimeDao stopTimeDao();
    public abstract TripDao tripDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context, AppDatabase.class, "star_test")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
