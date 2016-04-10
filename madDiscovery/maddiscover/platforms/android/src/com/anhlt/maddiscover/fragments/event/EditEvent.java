package com.anhlt.maddiscover.fragments.event;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.entities.Event;
import com.anhlt.maddiscover.form.EventForm;
import com.anhlt.maddiscover.services.EventService;

/**
 * Created by anhlt on 3/13/16.
 */
public class EditEvent extends CreateEvent {

    Context context;
    EventService eventService;
    public boolean back = false;
    Long eventId;
    Event event;

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getEventService().viewEvent(getFragmentManager(),getContext(),eventId);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        eventId = getArguments().getLong("eventId");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = getActivity().getApplicationContext();
        eventService = new EventService(context);
        return inflater.inflate(R.layout.event_form, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_edit_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_edit){
           saveEdit();
        }if(item.getItemId() == R.id.cancel_edit){
            back = true;
            back(2,eventId);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        loadData();
    }

    private void saveEdit(){
        String validData = validData();
        if (eventTxt.getText().toString().isEmpty()){
            showErrorDialog("Error", "Please enter event name");
        }else if(validData.length()>0){
            actionOnInvalidData(validData, event);
        }else{
            if(validEvent(eventId)){
                save(event);
            }else{
                showErrorDialog("Error", "This event has been create by the same organizer on this location!");
            }
        }
    }

    private void loadData(){
        TextView textView = (TextView)getView().findViewById(R.id.create_edit_event);
        textView.setText("Edit event");
        event = eventService.findById(eventId);
        EventForm form =  eventService.getEventInfo(eventId);
        eventTxt.setText(form.getEventName());
        organizerTxt.setText(form.getOrganizer());
        venueTxt.setText(form.getVenue());
        startDatetxt.setText(form.getStartDate());
        remarkTxt.setText(form.getRemark());
        dateOfEvent.setText(event.getDateOfEvent());
    }

    public EventService getEventService(){
        if(eventService == null)
            eventService = new EventService(context);
        return eventService;
    }
}
