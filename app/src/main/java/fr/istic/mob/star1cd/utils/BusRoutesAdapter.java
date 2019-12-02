package fr.istic.mob.star1cd.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.star1cd.R;
import fr.istic.mob.star1cd.database.model.BusRoute;

public class BusRoutesAdapter extends ArrayAdapter<BusRoute> {

    private Activity context;
    private ArrayList<BusRoute> busRoutes;

    public BusRoutesAdapter(Activity context, int resource, List<BusRoute> busRoutes)
    {
        super(context, resource, new ArrayList<BusRoute>(busRoutes));
        this.context = context;
        this.busRoutes = new ArrayList<BusRoute>(busRoutes);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            //inflate your customlayout for the textview
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.busroute_spinner_item, parent, false);
        }
        //put the data in it
        BusRoute item = busRoutes.get(position);
        if(item != null)
        {
            TextView text1 = (TextView) row.findViewById(R.id.itemId);
            text1.setTextColor(Color.parseColor(item.getRouteTextColor()));
            text1.setText(item.getRouteShortName());
        }

        return row;
    }


}
