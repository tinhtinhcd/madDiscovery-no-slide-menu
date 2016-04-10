package com.anhlt.maddiscover.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
import com.anhlt.maddiscover.entities.Event;
import com.anhlt.maddiscover.entities.Organizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 2/26/16.
 */

public class OrganizerListAdapter extends BaseAdapter implements Filterable, CompoundButton.OnCheckedChangeListener {

    Context context;
    public static List<Organizer> organizers;
    List<Organizer> fixOrganizers;
    SparseBooleanArray mCheckStates;

    public OrganizerListAdapter(Context context, List<Organizer> organizers) {
        this.context = context;
        this.fixOrganizers = organizers;
        this.organizers = organizers;
        mCheckStates = new SparseBooleanArray(fixOrganizers.size());
    }

    @Override
    public int getCount() {
        return organizers.size();
    }

    @Override
    public Object getItem(int position) {
        return organizers.get(position);
    }

    @Override
    public long getItemId(int position) {

        return organizers.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_organizer_item, null);
        }

        CheckBox organizerItem = (CheckBox) convertView.findViewById(R.id.organizer_item);
        organizerItem.setText(organizers.get(position).getName());
        organizerItem.setOnCheckedChangeListener(this);
        organizerItem.setTag(position);
        return convertView;

    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                organizers = (List<Organizer>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Organizer> filter;

                if (constraint == null || constraint.length() == 0) {
                    results.count = fixOrganizers.size();
                    results.values = fixOrganizers;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    filter = new ArrayList<Organizer>();
                    organizers = fixOrganizers;
                    for (int i = 0; i < organizers.size(); i++) {
                        Organizer organizer = organizers.get(i);
                        if (organizer.getName().toLowerCase().startsWith(constraint.toString()))  {
                            filter.add(organizer);
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

    }
}

