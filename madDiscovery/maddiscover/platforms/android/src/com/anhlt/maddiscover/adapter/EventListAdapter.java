package com.anhlt.maddiscover.adapter;

import android.app.Activity;
import android.app.LauncherActivity;
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
import android.widget.TextView;

import com.anhlt.maddiscover.Configuration;
import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.entities.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anhlt on 2/26/16.
 */

public class EventListAdapter extends BaseAdapter implements Filterable, CompoundButton.OnCheckedChangeListener {

    Context context;
    public static List<Event> events;
    List<Event> fixEventList;
    public SparseBooleanArray mCheckStates;
    public static List<Long> checkedEvent = new ArrayList<Long>();
    CheckBox eventItem;
    TextView startDate;

    public EventListAdapter(Context context, List<Event> events) {
        this.context = context;
        fixEventList = events;
        this.events = events;
        mCheckStates = new SparseBooleanArray(fixEventList.size());
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return events.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_event_item, null);
        }

        eventItem = (CheckBox) convertView.findViewById(R.id.checkbox);
        eventItem.setText(events.get(position).getEventName());
        eventItem.setTag(position);
        eventItem.setOnCheckedChangeListener(this);

        startDate = (TextView) convertView.findViewById(R.id.list_event_start_date);
        SimpleDateFormat format = new SimpleDateFormat(Configuration.dateFormat);
        String date = String.valueOf(events.get(position).getStartDate());
        startDate.setText(!date.equals("null")?format.format(events.get(position).getStartDate()):"N/A");

        return convertView;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                events = (List<Event>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<Event> filterEvent;

                if (constraint == null || constraint.length() == 0) {
                    results.count = fixEventList.size();
                    results.values = fixEventList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    filterEvent = new ArrayList<Event>();
                    events = fixEventList;
                    for (int i = 0; i < events.size(); i++) {
                        Event event = events.get(i);
                        if (event.getEventName().toLowerCase().startsWith(constraint.toString()))  {
                            filterEvent.add(event);
                        }
                    }

                    results.count = filterEvent.size();
                    results.values = filterEvent;
                }

                return results;
            }
        };

        return filter;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked){
            mCheckStates.put((Integer) buttonView.getTag(), isChecked);
            checkedEvent.add(events.get((Integer)buttonView.getTag()).getId());
        } else{
            mCheckStates.delete((Integer) buttonView.getTag());
            checkedEvent.remove(events.get((Integer)buttonView.getTag()).getId());
        }

    }

    public void removeDeletedItem(){
        List<Event> removeEvent = new ArrayList<Event>();
        for (int i =0 ; i < events.size() ; i++ ) {
            Event event = events.get(i);
            if (checkedEvent.contains(event.getId())){
                removeEvent.add(event);
            }
        }
        mCheckStates.clear();
        checkedEvent.clear();
        events.removeAll(removeEvent);
        fixEventList.removeAll(removeEvent);
        notifyDataSetChanged();
    }

    public List<Event> getFixEventList() {
        return fixEventList;
    }

    public void setFixEventList(List<Event> fixEventList) {
        this.fixEventList = fixEventList;
    }
}

