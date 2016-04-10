package com.anhlt.maddiscover.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.anhlt.maddiscover.Configuration;
import com.anhlt.maddiscover.data.tables.EventImages;
import com.anhlt.maddiscover.data.tables.Events;
import com.anhlt.maddiscover.data.tables.Organizers;
import com.anhlt.maddiscover.data.tables.Reports;
import com.anhlt.maddiscover.data.tables.Venues;

/**
 * Created by anhlt on 2/19/16.
 */

class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 8;

    private Context context;

    public MySQLiteOpenHelper(Context context){
        super(context, Configuration.DB_NAME, null, MySQLiteOpenHelper.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Events.onCreate(context, sqLiteDatabase);
        Venues.onCreate(context, sqLiteDatabase);
        Reports.onCreate(context, sqLiteDatabase);
        EventImages.onCreate(context, sqLiteDatabase);
        Organizers.onCreate(context, sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int oldVersion, int newVersion) {
        Events.onUpgrade(context, sqlLiteDatabase, oldVersion, newVersion);
        Venues.onUpgrade(context, sqlLiteDatabase, oldVersion, newVersion);
        Reports.onUpgrade(context, sqlLiteDatabase, oldVersion, newVersion);
        EventImages.onUpgrade(context, sqlLiteDatabase, oldVersion, newVersion);
        Organizers.onUpgrade(context, sqlLiteDatabase, oldVersion, newVersion);
    }
}
