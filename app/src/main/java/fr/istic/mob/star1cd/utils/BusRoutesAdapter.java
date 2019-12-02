package fr.istic.mob.star1cd.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.star1cd.R;
import fr.istic.mob.star1cd.database.AppDatabase;
import fr.istic.mob.star1cd.database.model.BusRoute;

/**
 * Bus Routes Adapter
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class BusRoutesAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> busRoutes;
    private ArrayList<String> colors;

    public BusRoutesAdapter(Activity context, int resource, ArrayList<String> busRoutes) {
        super(context, resource, busRoutes);
        this.context = context;
        this.busRoutes = busRoutes;
        this.colors = new ArrayList<>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
                List<BusRoute> busRoutes = appDatabase.busRouteDao().getAll();
                for (BusRoute busRoute : busRoutes) {
                    colors.add("#" + busRoute.getRouteColor());
                }
            }
        });
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.busroute_spinner_item, parent, false);
        }

        String item = busRoutes.get(position);
        String colorItem = colors.get(position);

        if (item != null) {
            final TextView text1 = (TextView) row.findViewById(R.id.itemId);

            text1.setText(item);
            try {
                text1.setTextColor(Color.parseColor(colorItem));
            } catch (IllegalArgumentException e) {
                text1.setTextColor(Color.parseColor("#3F51B5"));
            }
        }

        return row;
    }
}
