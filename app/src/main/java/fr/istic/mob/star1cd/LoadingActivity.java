package fr.istic.mob.star1cd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.istic.mob.star1cd.services.StarService;

public class LoadingActivity extends AppCompatActivity {

    private static LoadingActivity mInstance;
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    private TextView textViewProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mInstance = this;
        relativeLayout = findViewById(R.id.relativeLayoutId);
        relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        progressBar = findViewById(R.id.loadingBar);
        progressBar.setScaleY(3f);
        textViewProgressBar = findViewById(R.id.textViewLoadingBar);

        /*
        final AppDatabase appDatabase = AppDatabase.getDatabase(this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Trip trip = appDatabase.tripDao().find(5, 1);
                Log.i("TRIP", " route id : " + trip.getId());


                String queryString = "SELECT * FROM Stop, StopTime, Trip " +
                        "WHERE StopTime.trip_id = Trip._id " +
                        "AND Stop._id = StopTime.stop_id " +
                        "AND Trip.route_id = 5 " +
                        "AND Trip._id = 12800";

                String queryString3 = "SELECT DISTINCT Stop.stop_name FROM Stop, StopTime, Trip, BusRoute " +
                        "WHERE StopTime.trip_id = Trip._id " +
                        "AND Stop._id = StopTime.stop_id " +
                        "AND BusRoute._id = Trip.route_id " +
                        "AND BusRoute._id = 50";

                SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString);
                List<Stop> myList = appDatabase.stopDao().getStops(query);
                Log.i("eee", "size : " + myList.size());
                for (Stop stop : myList) {
                    Log.i("eee", stop.getStopName());
                }



                Cursor cursor = appDatabase.stopDao().findStops(trip.getId(), 50);
                while (cursor.moveToNext()) {
                    int c = cursor.getColumnIndex("stop_name");
                    Log.i("Stops ", cursor.getString(1));
                }


            }
        });
        thread.start();
        */

        if (StarService.isNetworkAvailable(this)) {
            Log.i("StarService", "Network is available");
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, StarService.class);
            startService(intent);
        }

    }

    public static LoadingActivity getInstance() {
        return mInstance;
    }

    /**
     * Set progress bar
     *
     * @param progress value of the progress
     * @param status   description of the current task
     */
    public void setProgress(int progress, String status) {
        this.progressBar.setProgress(progress);
        this.textViewProgressBar.setText(status);
    }

    /**
     * Start the main activity before finishing this one
     */
    public void switchToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
