package com.anhlt.maddiscover.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.app.DialogFragment;
import android.widget.TextView;

import com.anhlt.maddiscover.Configuration;
import com.anhlt.maddiscover.fragments.event.CreateEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.TimePicker;

/**
 * Created by anhlt on 3/13/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, OnTimeSetListener {

    TextView textView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int tv = getArguments().getInt("textView");
        switch (tv){
            case 1:
                textView = CreateEvent.startDatetxt;
                return new DatePickerDialog(getActivity(), this, year, month, day);
            case 2:
                textView = CreateEvent.dateOfEvent;
                return new TimePickerDialog(getActivity(),this, Calendar.HOUR_OF_DAY,Calendar.MINUTE,true);
            default:
                return new DatePickerDialog(getActivity(), this, year, month, day);
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat(Configuration.dateFormat);
        String formattedDate = sdf.format(c.getTime());
        textView.setText(formattedDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        textView.setText(time);
    }
}