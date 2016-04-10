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
import android.widget.TextView;

import com.anhlt.maddiscover.Configuration;
import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.entities.Report;
import com.anhlt.maddiscover.entities.Venue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 2/26/16.
 */

public class ReportListAdapter extends BaseAdapter{

    Context context;
    List<Report> reports;
    SparseBooleanArray mCheckStates;

    public ReportListAdapter(Context context, List<Report> reports) {
        this.context = context;
        this.reports = reports;
    }


    @Override
    public int getCount() {

        return reports.size();
    }

    @Override
    public Object getItem(int position) {

        return reports.get(position);
    }

    @Override
    public long getItemId(int position) {

        return reports.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_report_item, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.report_date);
        SimpleDateFormat format = new SimpleDateFormat(Configuration.dateTimeFormat);
        date.setText(format.format(reports.get(position).getCreateDate()));
        TextView report = (TextView) convertView.findViewById(R.id.report_text);
        report.setText(reports.get(position).getDetail());


        return convertView;

    }

}

