package fr.istic.mob.star1cd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.services.StarService;
import fr.istic.mob.star1cd.utils.SpinnerLineAsyncTask;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mInstance;
    private ProgressBar progressBar;
    private TextView textViewProgressBar;
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "Notification Channel";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private Spinner spinnerBusLine, spinnerBusDirection;
    private Button searchButton, dateButton, timeButton;
    private EditText dateEditText, timeEditText;

    public static MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        mInstance = this;

        this.progressBar = findViewById(R.id.progressBar);
        this.textViewProgressBar = findViewById(R.id.textViewProgressBar);
        this.spinnerBusLine = findViewById(R.id.spinnerBusLine);
        this.spinnerBusDirection = findViewById(R.id.spinnerBusDirection);
        this.dateButton = findViewById(R.id.dateButton);
        this.timeButton = findViewById(R.id.timeButton);
        this.searchButton = findViewById(R.id.searchButton);
        this.dateEditText = findViewById(R.id.dateEditText);
        this.timeEditText = findViewById(R.id.timeEditText);

        // Hide spinner until a line has been selected by the user
        spinnerBusDirection.setVisibility(View.GONE);
        timeEditText.setFocusable(false);
        dateEditText.setFocusable(false);

        createNotification();

        if (isNetworkAvailable(this)) {
            verifyStoragePermissions(this);
            Log.i("StarService", "Network is available");
            //Intent intent = new Intent(Intent.ACTION_SYNC, null, this, StarService.class);
            //startService(intent);
        }
        initSpinnerBusLine();
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

    /**
     * Create notification channel, API 26+
     */
    private void createNotificationChannel() {
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
     */
    public void createNotification() {

        Intent intent = new Intent(this, StarService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.new_version_available))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_app))
                .setSmallIcon(R.drawable.ic_notification_version)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_accept, "Download", pendingIntent);

        notificationManager.notify(1, notifBuilder.build());
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity, current activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
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
     * Initiliaze the bus line spinner with an async task
     * Because we can't perform DB operations on main thread
     */
    public void initSpinnerBusLine() {
        try {
            ArrayAdapter<String> arrayAdapter = new SpinnerLineAsyncTask(this, spinnerBusLine).execute().get();
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBusLine.setAdapter(arrayAdapter);
            spinnerBusLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selection = (String) parent.getItemAtPosition(position);
                    //Log.i("spinnerSelection", selection);
                    initSpinnerBusDirection(selection);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the spinner with the possible directions for this route
     *
     * @param routeShortName selected route
     */
    private void initSpinnerBusDirection(final String routeShortName) {
        this.spinnerBusDirection.setVisibility(View.VISIBLE);
        final AppDatabase appDatabase = AppDatabase.getDatabase(this);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String shortName = appDatabase.busRouteDao().findRouteLongName(routeShortName);
                //Log.i("Runnable", shortName);
                String[] splits = shortName.split(" <> ");
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(splits[0]); arrayList.add(splits[splits.length-1]);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
                MainActivity.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinnerBusDirection.setAdapter(arrayAdapter);
                    }
                });
            }
        });
        thread.start();
    }

    /**
     * After a click on search button
     * @param view View
     */
    public void search(View view) {
        Toast.makeText(getApplicationContext(),"Not yet implemented", Toast.LENGTH_LONG).show();
    }
}
