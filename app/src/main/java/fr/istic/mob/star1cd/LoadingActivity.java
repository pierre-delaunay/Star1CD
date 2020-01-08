package fr.istic.mob.star1cd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.model.Trip;
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

                Cursor cursor = appDatabase.stopDao().findStops(trip.getId(), 5);
                while (cursor.moveToNext()) {
                    int c = cursor.getColumnIndex("stop_name");
                    Log.i("Stops ", cursor.getString(1));
                }

            }
        });
        thread.start();
        */

        /*
        if (StarService.isNetworkAvailable(this)) {
            Log.i("StarService", "Network is available");
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, StarService.class);
            startService(intent);
        }
        */
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
