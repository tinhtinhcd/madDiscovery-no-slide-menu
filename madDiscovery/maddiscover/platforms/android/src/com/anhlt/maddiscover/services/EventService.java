package com.anhlt.maddiscover.services;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import com.anhlt.maddiscover.Configuration;
import com.anhlt.maddiscover.entities.Organizer;
import com.anhlt.maddiscover.entities.Venue;
import com.anhlt.maddiscover.form.EventForm;
import com.anhlt.maddiscover.fragments.event.CreateEvent;
import com.anhlt.maddiscover.adapter.EventListAdapter;
import com.anhlt.maddiscover.entities.Event;
import com.anhlt.maddiscover.fragments.event.EditEvent;
import com.anhlt.maddiscover.fragments.event.EventDetails;
import com.anhlt.maddiscover.fragments.event.ListEvent;
import com.anhlt.maddiscover.data.repositories.EventRepository;
import com.anhlt.maddiscover.data.repositories.OrganizerRepository;
import com.anhlt.maddiscover.data.repositories.VenueRepository;

import org.apache.cordova.LOG;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by anhlt on 2/19/16.
 */
public class EventService {

    Context context;
    EventRepository eventRepository;
    BaseService baseService;
    OrganizerRepository organizerRepository;
    VenueRepository venueRepository;
    Venue v;
    Organizer og;

    public EventService(Context context) {
        this.context = context;
        eventRepository = new EventRepository(context);
        venueRepository = new VenueRepository(context);
        organizerRepository = new OrganizerRepository(context);
    }

    public EventForm getEventInfo(long eventId){
        EventForm form = new EventForm();
        Event event = findById(eventId);
        Long ogId = event.getOrganizer();
        Long vId = event.getVenueId();

        if(ogId!=null && ogId>0)
             og = organizerRepository.findById(ogId);

        if (vId!=null && vId>0)
            v = venueRepository.findById(vId);

        form.setEventName(event.getEventName());
        try {
            SimpleDateFormat format = new SimpleDateFormat(Configuration.dateFormat);
            form.setStartDate(format.format(event.getStartDate()));
        }catch (Exception ex){
            LOG.e("",ex.toString());
        }
        try {
            form.setDateOfEvent(event.getDateOfEvent().toString());
        }catch (Exception e){

        }
        form.setRemark(event.getRemark());
        form.setOrganizer(og!=null?og.getName():"");
        form.setVenue(v!=null?v.getName():"");

        return form;
    }

    public Event findById(long eventId){
        return eventRepository.findById(eventId);
    }

    public List<Event> getEventList(){
        return eventRepository.getAllEvent();
    }

    public void createEventForm(FragmentManager fm, Context context){
        baseService = new BaseService(fm,context);
        baseService.replaceFragment(CreateEvent.getInstance());
    }

    public void viewEvent(FragmentManager fm, Context context,long eventId){

        EventDetails details = new EventDetails();
        Bundle args = new Bundle();
        args.putLong("eventId", eventId);
        details.setArguments(args);

        baseService = new BaseService(fm,context);
        baseService.replaceFragment(details);
    }

    public void saveNewEvent(Event event){
        eventRepository.create(event);
    }

    public void editEvent(FragmentManager fm, Context context,long eventId){
        EditEvent editEvent = new EditEvent();
        Bundle args = new Bundle();
        args.putLong("eventId", eventId);
        editEvent.setArguments(args);

        baseService = new BaseService(fm,context);
        baseService.replaceFragment(editEvent);
    }

    public void editEventFromListEvent(FragmentManager fm,Fragment lf){
        ListEvent listEvent = (ListEvent)lf;
        EventListAdapter list = (EventListAdapter)listEvent.getListAdapter();
        if(list==null || list.checkedEvent ==null ||list.checkedEvent.size() == 0){
            ApplicationService.showErrorDialog("ERROR", "Please select event to edit", lf.getActivity());
        }else if(list.checkedEvent.size() >1){
            ApplicationService.showErrorDialog("ERROR", "Cannot edit many event at the same time.", lf.getActivity());
        }else{
            editEvent(fm,context,list.checkedEvent.get(0));
        }
    }

    public void deleteEvent(FragmentManager fm, Fragment lf){
        ListEvent listEvent = (ListEvent)lf;
        EventListAdapter list = (EventListAdapter)listEvent.getListAdapter();
        deleteEventIds(list.checkedEvent);
        list.removeDeletedItem();
        baseService = new BaseService(fm,context);
        baseService.replaceFragment(new ListEvent());
    }

    private void deleteEventIds(List<Long> events){
        for (Long event : events) {
            eventRepository.delete(event);
        }
    }

    public void deleteEventId(Long eventId){
        eventRepository.delete(eventId);
    }

    public void searchEvent(){
//        eventRepository.findByName(eventTitle);
    }

    public boolean validEvent(String eventName, String venueName, String organizerName, Long eventId){
        return eventRepository.validEvent(eventName,venueName,organizerName, eventId);
    }

    public boolean validEventVenue(String eventName, String venueName, Long eventId){
        return eventRepository.validEventVenue(eventName, venueName, eventId);
    }

    public boolean validEventOrganization(String eventName, String organizerName, Long eventId){
        return eventRepository.validEventOrganization(eventName, organizerName, eventId);
    }

    public boolean validEventName(String eventName, Long eventId){
        return eventRepository.validEventName(eventName, eventId);
    }

    public void updateEventData(Event event){
        eventRepository.update(event);
    }
}
