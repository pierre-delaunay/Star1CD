package fr.istic.mob.star1cd.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.istic.mob.star1cd.database.DatabaseHelper;

public class StarService extends IntentService {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private String urlZip;
    private int statusCode;
    private InputStream inputStream;
    private HttpURLConnection urlConnection;

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

                // PROGRESS BAR https://stackoverflow.com/questions/45373007/progressdialog-is-deprecated-what-is-the-alternate-one-to-use
                // http://www.androiddeft.com/android-download-file-from-url-with-progress-bar/

                //statusCode = statusCodeFromJsonRequest(urlZip);
                //Log.i("StarService", "Status code ZIP file : " + String.valueOf(statusCode));
                //downloadZip(urlZip);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void downloadZip(String url) {
        try {
            URL urlDownloadZip = new URL(url);
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
        } catch (Exception e) {
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
}
