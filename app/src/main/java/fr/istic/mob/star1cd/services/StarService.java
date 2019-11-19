package fr.istic.mob.star1cd.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
            URL urlVersion = new URL(url);

            urlConnection = (HttpURLConnection) urlVersion.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("GET");
            statusCode = urlConnection.getResponseCode();
            Log.i("StarService", String.valueOf(statusCode));

            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = inputStreamToString(inputStream);
                //Log.i("StarService", response);

                JSONArray jsonArray = new JSONArray(response);
                Log.i("StarService", jsonArray.getJSONObject(0).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
