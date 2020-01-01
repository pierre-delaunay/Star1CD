package fr.istic.mob.star1cd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.model.BusRoute;
import fr.istic.mob.star1cd.services.StarService;
import fr.istic.mob.star1cd.utils.BusRoutesAdapter;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mInstance;
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "Notification Channel";
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

        this.spinnerBusLine = findViewById(R.id.spinnerBusLine);
        this.spinnerBusDirection = findViewById(R.id.spinnerBusDirection);
        this.dateButton = findViewById(R.id.dateButton);
        this.timeButton = findViewById(R.id.timeButton);
        this.searchButton = findViewById(R.id.searchButton);
        this.dateEditText = findViewById(R.id.dateEditText);
        this.timeEditText = findViewById(R.id.timeEditText);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        // Hide spinner until a line has been selected by the user
        spinnerBusDirection.setVisibility(View.GONE);
        timeEditText.setFocusable(false);
        dateEditText.setFocusable(false);

        createNotification();

        if (StarService.isNetworkAvailable(this)) {
            Log.i("StarService", "Network is available");
        }

        initSpinnerBusLine();
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
        Intent intent = new Intent(this, LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.title_notification))
                .setContentText(getResources().getString(R.string.content_notification))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_app))
                .setSmallIcon(R.drawable.ic_notification_version)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        //.addAction(R.drawable.ic_accept, "Download", pendingIntent);

        notificationManager.notify(1, notifBuilder.build());
    }

    /**
     * Initiliaze the bus line spinner with an async task
     * Because we can't perform DB operations on main thread
     */
    public void initSpinnerBusLine() {
        try {

            final AppDatabase appDatabase = AppDatabase.getDatabase(this);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    List<BusRoute> busRoutes = appDatabase.busRouteDao().getAll();
                    ArrayList<String> busRoutesStr = new ArrayList<>();
                    for (BusRoute busRoute : busRoutes) {
                        busRoutesStr.add(busRoute.getRouteShortName());
                    }

                    final ArrayAdapter<String> adapter = new BusRoutesAdapter(
                            mInstance, R.layout.busroute_spinner_item,
                            busRoutesStr);
                    spinnerBusLine.setAdapter(adapter);
                }
            });
            thread.start();

            spinnerBusLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selection = (String) parent.getItemAtPosition(position);
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
                String[] splits = shortName.split(" <> ");
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(splits[0]);
                arrayList.add(splits[splits.length - 1]);
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
     *
     * @param view View
     */
    public void search(View view) {
        Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_LONG).show();
    }

    /**
     * Handle click on date button
     * Source : https://github.com/trulymittal/DateTimePicker
     */
    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("dd/MM/yyyy", calendar1).toString();
                dateEditText.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    /**
     * Handle click on time button
     */
    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i("HandleTimeButton", "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                timeEditText.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }

    /**
     * Block back press button
     */
    @Override
    public void onBackPressed() {
        // do nothing
    }
}
