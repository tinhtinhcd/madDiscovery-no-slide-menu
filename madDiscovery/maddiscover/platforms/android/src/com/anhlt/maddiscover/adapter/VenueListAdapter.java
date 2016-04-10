package com.anhlt.maddiscover.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.entities.Organizer;
import com.anhlt.maddiscover.entities.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 2/26/16.
 */

public class VenueListAdapter extends BaseAdapter implements Filterable, CompoundButton.OnCheckedChangeListener {

    Context context;
    public  static  List<Venue> venues;
    List<Venue> fixVenues;
    SparseBooleanArray mCheckStates;

    public VenueListAdapter(Context context, List<Venue> venues) {
        this.context = context;
        this.fixVenues = venues;
        this.venues = venues;
        mCheckStates = new SparseBooleanArray(fixVenues.size());
    }

    @Override
    public int getCount() {

        return venues.size();
    }

    @Override
    public Object getItem(int position) {

        return venues.get(position);
    }

    @Override
    public long getItemId(int position) {

        return venues.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_venue_item, null);
        }

        CheckBox venueItem = (CheckBox) convertView.findViewById(R.id.venue_item);
        venueItem.setText(venues.get(position).getName());
        venueItem.setTag(position);
        return convertView;

    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                venues = (List<Venue>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Venue> filter;

                if (constraint == null || constraint.length() == 0) {
                    results.count = fixVenues.size();
                    results.values = fixVenues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    filter = new ArrayList<Venue>();
                    venues = fixVenues;
                    for (int i = 0; i < venues.size(); i++) {
                        Venue venue = venues.get(i);
                        if (venue.getName().toLowerCase().startsWith(constraint.toString()))  {
                            filter.add(venue);
                        }
                    }

                    results.count = filter.size();
                    results.values = filter;
                }

                return results;
            }
        };

        return filter;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }
}

