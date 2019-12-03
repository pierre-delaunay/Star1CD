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

        if (isNetworkAvailable(this)) {
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
     * isNetworkAvailable
     *
     * @param context Context
     * @return boolean, true if network available
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true;
            }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true;
            }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true;
            }
            return false;
        } else {
            // getActiveNetworkInfo is deprecated on API 29
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo.isConnected();
        }
    }

    public void switchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
