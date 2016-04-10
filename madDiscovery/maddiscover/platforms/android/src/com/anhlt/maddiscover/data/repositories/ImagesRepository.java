package com.anhlt.maddiscover.data.repositories;

import android.content.Context;
import android.database.Cursor;
import com.anhlt.maddiscover.data.sqlStatement.SQLStatement;
import com.anhlt.maddiscover.data.tables.EventImages;
import com.anhlt.maddiscover.entities.EventImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 4/3/16.
 */
public class ImagesRepository extends BasicRepository{

    public ImagesRepository(Context context) {
        super(context);
    }

    public List<String> getEventImage(Long eventId){
        List<String> images = new ArrayList<String>();
        Cursor cursor = databaseHelper.select(SQLStatement.getEventImages(eventId), null, null);
        while (cursor.moveToNext()){
            images.add(cursor.getString(0));
        }
        return images;
    }

    public void create(EventImage image) {
        create(EventImages.TABLE_NAME, image);
    }

    public void update(EventImage image){
        update(EventImages.TABLE_NAME, image);
    }

    public void delete(long id){ delete(EventImages.TABLE_NAME, id);}

}
