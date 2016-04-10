package com.anhlt.maddiscover.data.repositories;

import android.content.Context;
import android.database.Cursor;

import com.anhlt.maddiscover.data.sqlStatement.SQLStatement;
import com.anhlt.maddiscover.data.tables.Venues;
import com.anhlt.maddiscover.entities.Venue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 2/19/16.
 */
public class VenueRepository extends BasicRepository{

    public VenueRepository(Context context) {
        super(context);
    }

    public List<Venue> getAllVenue(){

        Cursor cursor = databaseHelper.select(SQLStatement.getAll(Venues.TABLE_NAME), null, null);
        List<Venue> venues = new ArrayList<Venue>();
        Venue venue;

        if (cursor.moveToFirst()){
            do{
                venue = new Venue();
                getObjectFromCursor(cursor, venue);
                venues.add(venue);
            }
            while (cursor.moveToNext());
        }

        return venues;
    }

    public Venue findByName(String name){

        String[] vName = {name};
        Cursor cursor = databaseHelper.select(SQLStatement.find(Venues.TABLE_NAME, Venues.name), vName, null);
        Venue vn = new Venue();

        while (cursor.moveToNext()){
            getObjectFromCursor(cursor, vn);
        }

        return vn;
    }

    public Venue findById(long id){
        return (Venue)findById(Venues.TABLE_NAME,new Venue(),id);
    }

    public void create(Venue org) {
        create(Venues.TABLE_NAME, org);
    }

    public void update(Venue org){
        update(Venues.TABLE_NAME, org);
    }

    public void delete(long id){
        delete(Venues.TABLE_NAME, id);
    }

    public void deletes(String[] ids){
        deletes(Venues.TABLE_NAME, ids);
    }

    @Override
    protected void removeRelationship(String[] vns){

    }

    public List<String> getListVenueName(){
        List<String> venueNames = new ArrayList<String>();
        Cursor cursor = databaseHelper.select(SQLStatement.getColumns(Venues.TABLE_NAME, Venues.name, null),null,null);
        if(cursor.moveToFirst()){
            do{
                venueNames.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return venueNames;
    }
}
