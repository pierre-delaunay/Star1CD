package fr.istic.mob.star1cd.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownloadAsyncTask extends AsyncTask<String, String, String> {

    private String file;

    public DownloadAsyncTask(Activity activity, String fileName) {
        this.file = fileName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        int count;
        try {
            String root = Environment.getExternalStorageDirectory().toString();

            Log.i("AsyncTask", "Downloading");
            URL url = new URL(strings[0]);

            HttpURLConnection urlConnection = (HttpURLConnection)  url.openConnection();
            urlConnection.connect();


            // getting file length
            int lenghtOfFile = urlConnection.getContentLength();
            Log.i("AsyncTask", String.valueOf(lenghtOfFile));

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            //InputStream input = urlConnection.getInputStream();

            // Output stream to write file

            OutputStream output = new FileOutputStream(root + "/file.json");
            byte data[] = new byte[1024];

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
            Log.e("AsyncTask Error", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String string) {
        Log.i("AsyncTask", "Downloaded");
        super.onPostExecute(string);
    }

}
