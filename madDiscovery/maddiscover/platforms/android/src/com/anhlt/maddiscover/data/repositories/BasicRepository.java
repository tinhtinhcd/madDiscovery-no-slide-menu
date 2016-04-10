package com.anhlt.maddiscover.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.anhlt.maddiscover.data.DatabaseHelper;
import com.anhlt.maddiscover.data.sqlStatement.SQLStatement;
import com.anhlt.maddiscover.data.tables.BaseTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by anhlt on 2/21/16.
 */
public class BasicRepository {

    DatabaseHelper databaseHelper;
    String columnId = BaseTable.COLUMN_NAME_ID;
    Context context = null;

    public BasicRepository(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    protected Object findById(String tableName,Object o, long oId){

        String[] id = {String.valueOf(oId)};
        Cursor cursor = databaseHelper.select(SQLStatement.findById(tableName), id, null);
        while (cursor.moveToNext()){
            getObjectFromCursor(cursor, o);
        }
        return o;
    }

    protected Object findByName(String tableName,Object o, String name){

        String[] names = {String.valueOf(name)};
        Cursor cursor = databaseHelper.select(SQLStatement.findByName(tableName), names, null);
        while (cursor.moveToNext()){
            getObjectFromCursor(cursor, o);
        }
        return o;
    }

    protected List<Object> getAll(String tableName){
        Cursor cursor = databaseHelper.select(SQLStatement.getAll(tableName), null, null);
        List<Object> objects = new ArrayList<Object>();
        if(cursor!=null && cursor.moveToFirst()){
            do{
                Object o = new Object();
                getObjectFromCursor(cursor, o);
                objects.add(o);
            }while (cursor.moveToNext());
        }
        return objects;
    }

    protected void create(String tableName, Object object) {

        ContentValues values = new ContentValues();
        try {
            for (Field field: object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if(value!=null)
                    values.put(field.getName(), value.toString());
            }
        }catch (Exception e){
            Log.e("error:" , e.getMessage());
        }

        databaseHelper.insert(tableName, values);
    }

    protected void delete(String tableName, long objectId){
        String[] id = {String.valueOf(objectId)};
        deletes(tableName, id);
    }

    protected void deletes(String tableName, String[] ids){
        removeRelationship(ids);
        databaseHelper.delete(tableName, columnId + " IN (?)", ids);
    }

    protected void removeRelationship(String[] id){

    }

    protected void update(String tableName, Object o){

        String[] id = new String[0];

        ContentValues values = new ContentValues();
        try {
            for (Field field: o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(o);
                if(value!=null){
                    values.put(field.getName(), value.toString());
                }
                if(field.getName().equalsIgnoreCase("id")) {
                    id = new String[]{values.get("id").toString()};
                }
            }
        }catch (Exception e){
            Log.e("",e.getMessage());
        }

        databaseHelper.update(tableName, values, columnId + " = ?", id);
    }

    protected void getObjectFromCursor(Cursor cursor, Object object){
        try {
            for (Field obj : object.getClass().getDeclaredFields()) {
                obj.setAccessible(true);
                try {
                    Date value = new Date(Date.parse(cursor.getString(cursor.getColumnIndex(obj.getName())).toString()));
                    obj.set(object,value);
                }catch (IllegalArgumentException ex){
                    try {
                        Long value = Long.valueOf(cursor.getString(cursor.getColumnIndex(obj.getName())));
                        if(value!=null)
                            obj.set(object,value);
                    }catch (NumberFormatException e){
                        String value = cursor.getString(cursor.getColumnIndex(obj.getName()));
                        obj.set(object,value);
                    }catch (IllegalArgumentException el){
                        String value = cursor.getString(cursor.getColumnIndex(obj.getName()));
                        obj.set(object,value);
                        Log.e("error on: ",object +"-"+ value);
                    }
                }catch (Exception ex){
                    String value = cursor.getString(cursor.getColumnIndex(obj.getName()));
                    obj.set(object,value);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
