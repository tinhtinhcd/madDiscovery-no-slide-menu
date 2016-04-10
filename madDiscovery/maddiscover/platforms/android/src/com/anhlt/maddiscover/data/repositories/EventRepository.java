package com.anhlt.maddiscover.data.repositories;

import android.content.Context;
import android.database.Cursor;

import com.anhlt.maddiscover.data.sqlStatement.SQLStatement;
import com.anhlt.maddiscover.data.tables.Events;
import com.anhlt.maddiscover.entities.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 2/19/16.
 */
public class EventRepository extends BasicRepository{

    public EventRepository(Context context) {
        super(context);
    }

    public List<Event> getAllEvent(){

        Cursor cursor = databaseHelper.select(SQLStatement.getAll(Events.TABLE_NAME), null, null);
        List<Event> events = new ArrayList<Event>();
        Event event;

        if (cursor.moveToFirst()){
            do{
                event = new Event();
                getObjectFromCursor(cursor, event);
                events.add(event);
            }
            while (cursor.moveToNext());
        }

        return events;
    }

    public List<String> getListEventName(){
        List<String> eventNames = new ArrayList<String>();
        Cursor cursor = databaseHelper.select(SQLStatement.getColumns(Events.TABLE_NAME, Events.eventName, null),null,null);
        if(cursor.moveToFirst()){
            do{
                eventNames.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return eventNames;
    }

    public Event findByName(String name){

        String[] eventName = {name};
        Cursor cursor = databaseHelper.select(SQLStatement.find(Events.TABLE_NAME, Events.eventName), eventName, null);
        Event event = new Event();

        while (cursor.moveToNext()){
            getObjectFromCursor(cursor, event);
        }

        return event;
    }

    public Event findById(long eventId){
        return (Event)findById(Events.TABLE_NAME,new Event(),eventId);
    }

    public void create(Event event) {
        create(Events.TABLE_NAME, event);
    }

    public void update(Event event){
        update(Events.TABLE_NAME, event);
    }

    public void delete(long id){
        delete(Events.TABLE_NAME, id);
    }

    public void deletes(String[] ids){
        deletes(Events.TABLE_NAME, ids);
    }

    @Override
    protected void removeRelationship(String[] eventIds){

    }

    public boolean validEvent(String eventName, String venueName, String organizerName, Long eventId){
        return !databaseHelper.select(SQLStatement.validEvent(eventName,venueName,organizerName,eventId),null,null).moveToFirst();
    }

    public boolean validEventVenue(String eventName, String venueName, Long eventId){
        return !databaseHelper.select(SQLStatement.validEvent(eventName,venueName,null,eventId),null,null).moveToFirst();
    }

    public boolean validEventOrganization(String eventName, String organizerName, Long eventId){
        return !databaseHelper.select(SQLStatement.validEvent(eventName,null,organizerName,eventId),null,null).moveToFirst();
    }

    public boolean validEventName(String eventName, Long eventId){
        return !databaseHelper.select(SQLStatement.validEvent(eventName,null,null,eventId),null,null).moveToFirst();
    }
}
