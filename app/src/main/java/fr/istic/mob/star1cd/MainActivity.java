package fr.istic.mob.star1cd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Objects;

import fr.istic.mob.star1cd.database.DatabaseHelper;
import fr.istic.mob.star1cd.utils.DownloadAsyncTask;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    public static final String URL_VERSION =
            "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";

    private ProgressBar progressBar;
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "Notification Channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * isNetworkAvailable
     * @param context Context
     * @return boolean, true if network available
     */
    public boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) { return true; }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) { return true; }
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) { return true; }
            return false;
        } else {
            // getActiveNetworkInfo is deprecated on API 29
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return  activeNetworkInfo.isConnected();
        }
    }

    /**
     * Download file from web
     * @param url String
     */
    public void downloadFileFromWeb(String url) {
        if (isNetworkAvailable(this)) {
            new DownloadAsyncTask(this, "a.txt").execute(url);
        }
        else {
            Log.e("Download", "Connexion réseau indisponible.");
        }
    }

    /**
     * Create notification channel, API 26+
     */
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription("Notification channel description");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }

    /**
     * Show new notification - new version available
     * @param view View
     */
    public void showNotificationNewVersion(View view) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.new_version_available))
                .setSmallIcon(R.drawable.ic_notification_version)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(1, notifBuilder.build());
    }
}
