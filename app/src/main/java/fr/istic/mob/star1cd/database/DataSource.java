package fr.istic.mob.star1cd.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    public DataSource(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void restart() {
        this.databaseHelper.onUpgrade(database, 1, 1);
    }

    public void open() throws SQLException {
         this.database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        database.close();
    }

}
