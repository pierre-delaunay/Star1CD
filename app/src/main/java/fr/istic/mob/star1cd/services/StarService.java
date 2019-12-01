package fr.istic.mob.star1cd.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.istic.mob.star1cd.MainActivity;
import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.model.BusRoute;
import fr.istic.mob.star1cd.database.model.Calendar;
import fr.istic.mob.star1cd.database.model.Stop;
import fr.istic.mob.star1cd.database.model.StopTime;
import fr.istic.mob.star1cd.database.model.Trip;
import fr.istic.mob.star1cd.utils.ZipManager;

public class StarService extends IntentService {

    // adb forward tcp:8081 tcp:8081
    private AppDatabase appDatabase;
    private String urlZip;
    private int statusCode;
    private InputStream inputStream;
    private HttpURLConnection urlConnection;
    public static final String URL_VERSION =
            "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";
    private final static String zipFileName = "star.zip";
    private int i, k;
    private List<StopTime> stopTimes = new ArrayList<StopTime>();
    private List<Trip> trips = new ArrayList<Trip>();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StarService(String name) {
        super(name);
    }

    /**
     * Default constructor
     */
    public StarService() {
        super(StarService.class.getName());
    }

    /**
     * L'IntentService appelle la méthode par défaut du « worker thread » avec
     * l'intent passé en paramètre de la méthode. Quand cette méthode est terminée
     * le service s'arrête de lui-même
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String url = URL_VERSION;
        appDatabase = AppDatabase.getDatabase(this);

        try {
            statusCode = statusCodeFromJsonRequest(url);
            Log.i("StarService", "Status code of version request : " + String.valueOf(statusCode));
            //MainActivity.getInstance().setProgress(5, "Checking new version");

            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = inputStreamToString(inputStream);

                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("fields");
                String dateFinValidite = jsonObject.getString("finvalidite");

                if (!isAfterExpirationDate(dateFinValidite)) {
                    Log.i("StarService", "Downloading first json object");
                    urlZip = jsonObject.getString("url");

                } else {
                    Log.i("StarService", "Downloading second json object");
                    urlZip = jsonArray.getJSONObject(1).getJSONObject("fields").getString("url");
                }

                this.setProgress(10, "Downloading new zip");
                downloadZip(urlZip);

                this.setProgress(15, "Unzipping in progress");
                ZipManager.unpackZip(getFilesDir().getPath() + File.separator, zipFileName);

                this.setProgress(20, "Inserting bus routes");
                appDatabase.busRouteDao().deleteAll();
                readTxtFile("routes.txt");

                this.setProgress(25, "Inserting calendar");
                appDatabase.calendarDAO().deleteAll();
                readTxtFile("calendar.txt");

                this.setProgress(30, "Inserting stops");
                appDatabase.stopDao().deleteAll();
                readTxtFile("stops.txt");


                this.setProgress(35, "Inserting trips");
                appDatabase.tripDao().deleteAll();
                readTxtFile("trips.txt");
                // Insert trips that remain in the list
                if (trips.size() != 0) {
                    appDatabase.tripDao().insertAll(trips);
                }

                this.setProgress(40, "Inserting stop times");
                appDatabase.stopTimeDao().deleteAll();
                readTxtFile("stop_times.txt");
                // Insert stop times that remain in the list
                if (stopTimes.size() != 0) {
                    appDatabase.stopTimeDao().insertAll(stopTimes);
                }

                this.setProgress(100, "Done with database inserts");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Download the zip archive and store it in external storage (path : /DCIM/Star/)
     *
     * @param url String 'http://ftp.keolis-rennes.com[...]'
     */
    private void downloadZip(String url) {
        try {
            URL urlDownloadZip = new URL(url);

            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();

            final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Star");
            if (!f.exists()) {
                f.mkdir();
            }

            HttpURLConnection urlConnection = (HttpURLConnection) urlDownloadZip.openConnection();
            urlConnection.connect();

            // Getting file length
            int lenghtOfFile = urlConnection.getContentLength();
            Log.i("StarService", String.valueOf(lenghtOfFile));

            // Input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(urlDownloadZip.openStream(), 8192);

            // Output stream to write file
            OutputStream output = new FileOutputStream(getFilesDir().getPath() + File.separator + zipFileName); // zipPath + zipFileName
            byte data[] = new byte[1024];

            int count;
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // Flushing output
            output.flush();

            // Closing streams
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the status code of a GET Json request
     *
     * @param url request
     * @return status code, -1 if the request failed
     */
    private int statusCodeFromJsonRequest(String url) {
        try {
            URL targetUrl = new URL(url);
            urlConnection = (HttpURLConnection) targetUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            return urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Converts an InputStream to a String
     *
     * @param inputStream source
     * @return String converted
     * @throws IOException IOException
     */
    private String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Compare today date with the expiration date
     * Source : https://stackoverflow.com/questions/10774871/best-way-to-compare-dates-in-android
     *
     * @param expirationDateString example "2000-01-01"
     * @return true if today date is after expiration date
     */
    private static boolean isAfterExpirationDate(String expirationDateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date expirationDate = sdf.parse(expirationDateString);
            Date todayDate = new Date();
            return (todayDate.after(expirationDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Read .txt file (/files/ directory in internal storage)
     *
     * @param fileName name of the file
     */
    private void readTxtFile(String fileName) {
        File dir = new File(getFilesDir().getPath() + File.separator);

        if (dir.exists()) {
            File file = new File(dir, fileName);
            FileOutputStream os = null;
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                br.readLine(); // skip first line
                int id = 1;

                while ((line = br.readLine()) != null) {
                    //text.append(line.replace("\"", ""));
                    //text.append('\n');
                    String tmp[] = line.replace("\"", "").split(",");
                    //Log.i("BDD", "size of " + tmp.length);
                    insertLineInDB(id, tmp, fileName);
                    //text.append(tmp[2]);
                    //text.append('\n');
                    id++;
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Insert line from the file into the Database with Room library
     *
     * @param id       primary key
     * @param line     String line
     * @param fileName name of the file
     */
    private void insertLineInDB(Integer id, String line[], String fileName) {
        switch (fileName) {
            case "routes.txt":
                BusRoute busRoute = new BusRoute();
                busRoute.setId(id);
                busRoute.setRouteShortName(line[2]);
                busRoute.setRouteLongName(line[3]);
                busRoute.setRouteDescription(line[4]);
                busRoute.setRouteType(line[5]);
                busRoute.setRouteColor(line[7]);
                busRoute.setRouteTextColor(line[8]);
                appDatabase.busRouteDao().insertAll(busRoute);
                break;
            case "calendar.txt":
                Calendar calendar = new Calendar();
                calendar.setId(Integer.valueOf(line[0]));
                calendar.setMonday(Integer.valueOf(line[1]));
                calendar.setTuesday(Integer.valueOf(line[2]));
                calendar.setWednesday(Integer.valueOf(line[3]));
                calendar.setThursday(Integer.valueOf(line[4]));
                calendar.setFriday(Integer.valueOf(line[5]));
                calendar.setSaturday(Integer.valueOf(line[6]));
                calendar.setSunday(Integer.valueOf(line[7]));
                calendar.setStartDate(Integer.valueOf(line[8]));
                calendar.setEndDate(Integer.valueOf(line[9]));
                appDatabase.calendarDAO().insertAll(calendar);
                break;
            case "stop_times.txt":
                StopTime stopTime = new StopTime();
                stopTime.setId(id);
                stopTime.setTripId(Integer.valueOf(line[0]));
                stopTime.setArrivalTime(line[1]);
                stopTime.setDepartureTime(line[2]);
                stopTime.setStopId(line[3]);
                stopTime.setStopSequence(Integer.valueOf(line[4]));
                insertStopTime(stopTime);
                break;
            case "stops.txt":
                Stop stop = new Stop();
                stop.setId(line[0]);
                stop.setStopName(line[2]);
                stop.setStopDesc(line[3]);
                stop.setStopLat(line[4]);
                stop.setStopLon(line[5]);
                stop.setWheelchairBoarding(Integer.parseInt(line[11]));
                appDatabase.stopDao().insertAll(stop);
                break;
            case "trips.txt":
                Trip trip = new Trip();
                trip.setId(id);
                trip.setRouteId(Integer.valueOf(line[0]));
                trip.setServiceId(Integer.valueOf(line[1]));
                trip.setTripHeadsign(line[3]);
                trip.setDirectionId(line[5]);
                trip.setBlockId(line[6]);
                trip.setWheelchairAccessible(Integer.valueOf(line[8]));
                insertTrip(trip);
                break;
        }
    }

    /**
     * Insert in database using List (batch inserts for better performance)
     *
     * @param stopTime object
     */
    public void insertStopTime(StopTime stopTime) {
        stopTimes.add(stopTime);
        i++;
        if (i == 500) {
            appDatabase.stopTimeDao().insertAll(stopTimes);
            i = 0;
            stopTimes.clear();
        }
    }

    /**
     * Insert in database using List (batch inserts for better performance)
     *
     * @param trip object
     */
    public void insertTrip(Trip trip) {
        trips.add(trip);
        k++;
        if (k == 500) {
            appDatabase.tripDao().insertAll(trips);
            k = 0;
            trips.clear();
        }
    }

    /**
     * Set new progress for the progress bar
     *
     * @param progress int progress value
     * @param status   message that will be displayed
     */
    private void setProgress(final int progress, final String status) {
        MainActivity.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.getInstance().setProgress(progress, status);
            }
        });
    }

    /**
     * Returns database status
     *
     * @return true if database is empty (no rows)
     */
    private boolean isDatabaseEmpty() {
        return appDatabase.busRouteDao().findAny().size() == 0;
    }
}
