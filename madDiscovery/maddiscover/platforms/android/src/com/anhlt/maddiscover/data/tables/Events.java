package com.anhlt.maddiscover.data.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by anhlt on 2/19/16.
 */
public class Events extends BaseTable{

    public static final String id = COLUMN_NAME_ID;
    public static final String venueId = "venueId";
    public static final String eventName = "eventName";
    public static final String createDate = "createDate";
    public static final String startDate = "startDate";
    public static final String organizer = "organizer";
    public static final String remark = "remark";
    public static final String dateOfEvent = "dateOfEvent";
    public static String TABLE_NAME = "events";

    public static void onCreate(Context pContext, SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(createStatement());
    }

    public static void onUpgrade(Context pContext, SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {

    }

    protected static String createStatement(){
        StringBuilder createTable = new StringBuilder();
        createTable.append("Create Table `"+TABLE_NAME+"` (`");
        createTable.append(COLUMN_NAME_ID);
        createTable.append("` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        createTable.append("`"+venueId+"` BIGINT, ");
        createTable.append("`"+eventName+"` VARCHAR(255), ");
        createTable.append("`"+createDate+"` DATETIME, ");
        createTable.append("`"+startDate+"` DATETIME, ");
        createTable.append("`"+dateOfEvent+"` VARCHAR(255), ");
        createTable.append("`"+organizer+"` BIGINT, ");
        createTable.append("`"+remark+"` VARCHAR(255)");
        createTable.append(");");
        return createTable.toString();
    }
}
