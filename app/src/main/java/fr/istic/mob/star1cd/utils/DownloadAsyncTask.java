package fr.istic.mob.star1cd.utils;

import android.app.Activity;
import android.os.AsyncTask;

public class DownloadAsyncTask extends AsyncTask<String, Integer, byte[]> {

    private String file;

    public DownloadAsyncTask(Activity activity, String fileName) {
        this.file = fileName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(String... strings) {
        return new byte[0];
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
    }

}
