package com.anhlt.maddiscover.data.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by anhlt on 2/19/16.
 */
public class EventImages extends BaseTable {

    public static final String id = COLUMN_NAME_ID;
    public static final String url = "url";
    public static final String fileName = "fileName";
    public static final String eventId = "eventId";
    public static String TABLE_NAME = "images";

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
        createTable.append("`"+url+"` VARCHAR(255), ");
        createTable.append("`"+fileName+"` VARCHAR(255), ");
        createTable.append("`"+eventId+"` VARCHAR(255) ");
        createTable.append(");");
        return createTable.toString();
    }
}
