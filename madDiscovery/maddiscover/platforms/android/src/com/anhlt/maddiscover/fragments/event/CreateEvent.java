package com.anhlt.maddiscover.fragments.event;

import com.anhlt.maddiscover.fragments.BaseFragment;
import com.anhlt.maddiscover.entities.Event;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.entities.Organizer;
import com.anhlt.maddiscover.entities.Venue;
import com.anhlt.maddiscover.services.BaseService;
import com.anhlt.maddiscover.services.EventService;
import com.anhlt.maddiscover.services.OrganizerService;
import com.anhlt.maddiscover.services.VenueService;
import com.anhlt.maddiscover.utils.DatePickerFragment;
import java.util.Date;
import java.util.List;
import android.app.DialogFragment;
import android.widget.TextView;

public class CreateEvent extends BaseFragment {

    Context context;
    protected BaseService baseService;
    protected VenueService venueService;
    protected OrganizerService organizerService;
    protected AutoCompleteTextView venueTxt;
    protected AutoCompleteTextView organizerTxt;
    protected EventService eventService;
    protected EditText eventTxt,remarkTxt;
    public static TextView startDatetxt,dateOfEvent;
    public boolean back = false;

    public static CreateEvent getInstance(){
        return new CreateEvent();
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
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
        inflater.inflate(R.menu.menu_create_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == R.id.save){
            back = true;
            saveNewEvent();
        }if (item.getItemId()  == R.id.save_more){
            saveNewEvent();
        }if (item.getItemId() == R.id.cancel){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    protected void saveNewEvent(){
        String validData = validData();
        if (eventTxt.getText().toString().isEmpty()){
            showErrorDialog("Error", "Please enter event name");
        }else if(validData.length()>0){
            actionOnInvalidData(validData,null);
        }else{
            if(validEvent(null)){
                save(null);
            }else{
                showErrorDialog("Error", "This event has been create by the same organizer on this location!");
            }
        }
    }

    protected void actionOnInvalidData(String validData,final Event event){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle("Invalid Data!");
        builder.setMessage(validData);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                save(event);
            }
        }).setCancelable(true);
        builder.show();
    }

    protected void back(int page,Long eventId){
        if(back){
            baseService = new BaseService(getActivity().getFragmentManager(),context);
            if(page  == 1)
                baseService.replaceFragment(new ListEvent());
            if(page  == 2)
                getEventService().viewEvent(getFragmentManager(), context, eventId);
        }else{
            resetForm();
        }
    }

    protected void save(Event event){

        if(event == null)
            event = new Event();

        event.setEventName(eventTxt.getText().toString());

        Long venueId = venueId();
        String venueName =  venueTxt.getText().toString();

        Long organizerId = oganizerId();
        String organizerName =  organizerTxt.getText().toString();

        if(venueId!=null && venueId > 0L){
            event.setVenueId(venueId);
        }else if(venueId == null){
            Venue venue = new Venue();
            venue.setName(venueName);
            venueService.saveNewVenue(venue);
            event.setVenueId(venueService.findByName(venueName).getId());
        }

        if(organizerId!=null && organizerId > 0L){
            event.setOrganizer(organizerId);
        }else if(organizerId == null){
            Organizer organizer = new Organizer();
            organizer.setName(organizerName);
            organizerService.saveNewOrganizer(organizer);
            event.setOrganizer(organizerService.findByName(organizerName).getId());
        }

        String startDate = startDatetxt.getText().toString();
            if(startDate.length()>0){try {
                event.setStartDate(new Date(startDate));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        String time = dateOfEvent.getText().toString();
        try {
            event.setDateOfEvent(time);
        }catch (Exception e){
            e.printStackTrace();
        }


        String remark = remarkTxt.getText().toString();
        if(remark.length()>0)
            event.setRemark(remark);

        if(event.getId() == null)
            getEventService().saveNewEvent(event);
        else{
            getEventService().updateEventData(event);
        }

        if(event.getId() != null && event.getId()>0){
            back = true;
            back(2,event.getId());
        }
        else
            back(1,null);

    }

    protected void resetForm(){

        eventTxt.setText("");
        organizerTxt.setText("");
        venueTxt.setText("");
        startDatetxt.setText("");
        dateOfEvent.setText("");
        remarkTxt.setText("");
        dateOfEvent.setText("");

    }

    protected String validData(){
        StringBuilder error = new StringBuilder();
//        if(venueId()==null)
//            error.append("This venue has not exists. Would you like to quick create with this name?");
//        if(oganizerId()==null)
//            error.append("This organizer has not exists. Would you like to quick create with this name?");
        return error.toString();
    }

    protected Long venueId(){
        String venue = venueTxt.getText().toString();
        if(venue == null || venue.isEmpty())
            return 0L;
        else
            try {
                return venueService.findByName(venue).getId();
            }catch (Exception e){
                return null;
            }
    }

    protected Long oganizerId(){
        String organizer = organizerTxt.getText().toString();
        if(organizer == null || organizer.isEmpty())
            return 0L;
        else
            try {
                return organizerService.findByName(organizer).getId();
            }catch (Exception e){
                return null;
            }
    }

    protected boolean validEvent(Long eventId){
        if(!venueTxt.getText().toString().isEmpty()&&!organizerTxt.getText().toString().isEmpty()){
            return getEventService().validEvent(eventTxt.getText().toString(), venueTxt.getText().toString(), organizerTxt.getText().toString(), eventId);
        }
        else if(!venueTxt.getText().toString().isEmpty()){
            return getEventService().validEventVenue(eventTxt.getText().toString(), venueTxt.getText().toString(), eventId);
        }
        else if(!organizerTxt.getText().toString().isEmpty()){
            return getEventService().validEventOrganization(eventTxt.getText().toString(), organizerTxt.getText().toString(), eventId);
        }else if(venueTxt.getText().toString().isEmpty()&&organizerTxt.getText().toString().isEmpty()){
            return getEventService().validEventName(eventTxt.getText().toString(), eventId);
        }else
            return true;
    }

    protected void initData(){
        loadVenue();
        loadOrganizer();
        initComponent();
    }

    protected void initComponent(){
        eventTxt = (EditText)getView().findViewById(R.id.event_name);
        startDatetxt = (TextView)getView().findViewById(R.id.start_date);
        startDatetxt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DialogFragment picker = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putInt("textView", 1);
                picker.setArguments(args);
                picker.show(getFragmentManager(), "datePicker");
                return false;
            }
        });

        dateOfEvent = (TextView)getView().findViewById(R.id.time);
        dateOfEvent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DialogFragment picker = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putInt("textView", 2);
                picker.setArguments(args);
                picker.show(getFragmentManager(), "timePicker");
                return false;
            }
        });

        remarkTxt = (EditText)getView().findViewById(R.id.remark);
    }

    protected void loadVenue(){

        venueService = new VenueService(getContext());

        List<String> venueNames = venueService.listVenueName();
        String[] array = new String[venueNames.size()];
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, venueNames.toArray(array));

        venueTxt = (AutoCompleteTextView)getView().findViewById(R.id.venue);
        venueTxt.setAdapter(adapter);
        venueTxt.setThreshold(1);

    }

    protected void loadOrganizer(){

        organizerService = new OrganizerService(getContext());

        List<String> organizerNames = organizerService.listOrganizerName();
        String[] array = new String[organizerNames.size()];
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, organizerNames.toArray(array));

        organizerTxt = (AutoCompleteTextView)getView().findViewById(R.id.organizer);
        organizerTxt.setAdapter(adapter);
        organizerTxt.setThreshold(1);

    }

    public Context getContext(){
        if(context == null)
            context = getActivity().getApplicationContext();
        return context;
    }

    public EventService getEventService(){
        if(eventService == null)
            eventService = new EventService(getContext());
        return eventService;
    }
}
