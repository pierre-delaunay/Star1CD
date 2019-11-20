package fr.istic.mob.star1cd.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import fr.istic.mob.star1cd.database.DataSource;
import fr.istic.mob.star1cd.database.DatabaseHelper;
import fr.istic.mob.star1cd.database.model.BusRoute;
import fr.istic.mob.star1cd.database.model.Calendar;
import fr.istic.mob.star1cd.database.model.Stop;
import fr.istic.mob.star1cd.database.model.Trip;
import fr.istic.mob.star1cd.utils.DownloadAsyncTask;
import fr.istic.mob.star1cd.utils.ZipManager;

public class StarService extends IntentService {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private String urlZip;
    private int statusCode;
    private InputStream inputStream;
    private HttpURLConnection urlConnection;
    private final static String zipPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Star/";
    private final static String zipFileName = "star.zip";

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
        String url = intent.getStringExtra("url");

        try {
            statusCode = statusCodeFromJsonRequest(url);
            Log.i("StarService", "Status code of version request : " + String.valueOf(statusCode));

            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = inputStreamToString(inputStream);
                //Log.i("StarService", response);

                JSONArray jsonArray = new JSONArray(response);
                //Log.i("StarService", jsonArray.getJSONObject(0).toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("fields");
                urlZip = jsonObject.getString("url");
                Log.i("StarService", urlZip);

                //downloadZip(urlZip);

                //ZipManager.unzip(zipPath + zipFileName, zipPath);

                //readTxtFile("routes.txt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Download the zip archive and store it in external storage (path : /DCIM/Star/)
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

            HttpURLConnection urlConnection = (HttpURLConnection)  urlDownloadZip.openConnection();
            urlConnection.connect();

            // getting file length
            int lenghtOfFile = urlConnection.getContentLength();
            Log.i("AsyncTask", String.valueOf(lenghtOfFile));

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(urlDownloadZip.openStream(), 8192);


            // Output stream to write file
            OutputStream output = new FileOutputStream(zipPath + zipFileName);
            byte data[] = new byte[1024];

            int count;
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the status code of a GET Json request
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
     * @param inputStream source
     * @return String
     * @throws IOException
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
     *  Compare two dates
     *  Source : https://stackoverflow.com/questions/10774871/best-way-to-compare-dates-in-android
     * @param strDate1 example "1/1/1990"
     * @param strDate2 example "1/1/1990"
     * @return true if date1 is more recent than date2
     */
    private static boolean isAfter(String strDate1, String strDate2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(strDate1);
            Date date2 = sdf.parse(strDate2);
            return (date1.after(date2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void readTxtFile(String fileName) {

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Star/");

        if (dir.exists()) {
            File file = new File(dir, fileName);
            FileOutputStream os = null;
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                br.readLine(); // skip first line
                DataSource dataSource = new DataSource(this);
                int id = 1;
                databaseHelper = DatabaseHelper.getInstance(this);
                database = databaseHelper.getWritableDatabase();
                //database.beginTransaction(); // db performance
                while ((line = br.readLine()) != null){
                    //text.append(line.replace("\"", ""));
                    //text.append('\n');
                    String tmp[] = line.replace("\"", "").split(",");
                    //Log.i("BDD", "size of " + tmp.length);
                    insertLineInDB(id, dataSource, tmp, fileName);
                    //text.append(tmp[2]);
                    //text.append('\n');
                    id++;
                }
                br.close();

                //database.setTransactionSuccessful(); // db performance
                //database.endTransaction(); // db performance
            } catch (IOException e) {

            }
            Log.i("readTxtFile", text.toString());
        }

    }

    private void insertLineInDB(Integer id, DataSource dataSource, String line[], String fileName) {

        switch (fileName) {
            case "routes.txt" :
                // insert OK
                BusRoute busRoute = new BusRoute();
                busRoute.setId(id);
                busRoute.setRouteShortName(line[2]);
                busRoute.setRouteLongName(line[3]);
                busRoute.setRouteDescription(line[4]);
                busRoute.setRouteType(line[5]);
                busRoute.setRouteColor(line[7]);
                busRoute.setRouteTextColor(line[8]);
                dataSource.insertBusRoute(busRoute);

                break;
            case "calendar.txt" :
                // insert OK
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
                dataSource.insertCalendar(calendar);

                break;
            case "stop_times.txt" :
                break;
            case "stops.txt" :
                // insert ok
                Stop stop = new Stop();
                stop.setId(line[0]);
                stop.setStopName(line[2]);
                stop.setStopDesc(line[3]);
                stop.setStopLat(line[4]);
                stop.setStopLon(line[5]);
                stop.setWheelchairBoarding(Integer.parseInt(line[11]));
                dataSource.insertStop(stop);
                break;
            case "trips" :
                // not tested
                Trip trip = new Trip();
                trip.setId(id);
                trip.setRouteId(Integer.valueOf(line[0]));
                trip.setServiceId(Integer.valueOf(line[1]));
                trip.setTripHeadsign(line[3]);
                trip.setDirectionId(line[5]);
                trip.setBlockId(line[6]);
                trip.setWheelchairAccessible(Integer.valueOf(line[8]));
                dataSource.insertTrip(trip);
                break;
        }
    }


}
