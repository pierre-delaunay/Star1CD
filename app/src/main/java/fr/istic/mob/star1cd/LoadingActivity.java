package fr.istic.mob.star1cd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
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
