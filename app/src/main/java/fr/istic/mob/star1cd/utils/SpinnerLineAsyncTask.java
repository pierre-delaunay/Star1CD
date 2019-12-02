package fr.istic.mob.star1cd.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.star1cd.R;
import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.model.BusRoute;

public class SpinnerLineAsyncTask extends AsyncTask<String, String, ArrayAdapter<String>> {

    private Activity activity;
    private Spinner spinnerBusLine;

    public SpinnerLineAsyncTask(Activity activity, Spinner spinnerBusLine) {
        this.activity = activity;
        this.spinnerBusLine = spinnerBusLine;
    }

    @Override
    protected ArrayAdapter<String> doInBackground(String... strings) {
        List<BusRoute> busRouteList = AppDatabase.getDatabase(activity).busRouteDao().getAll();

        ArrayList<String> arrayList = new ArrayList<>();
        for (BusRoute busRoute : busRouteList) {
            arrayList.add(busRoute.getRouteShortName());
        }
        return new ArrayAdapter<String>(activity, R.layout.busroute_spinner_item, arrayList);
    }
}
