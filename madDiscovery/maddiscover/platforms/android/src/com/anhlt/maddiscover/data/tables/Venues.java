package com.anhlt.maddiscover.data.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anhlt.maddiscover.data.sqlStatement.SQLStatement;

/**
 * Created by anhlt on 2/19/16.
 */
public class Venues extends BaseTable{

    public static final String id = COLUMN_NAME_ID;
    public static final String name = "name";
    public static final String address = "address";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String postal_code = "postal_code";
    public static String TABLE_NAME = "venue";

    public static void onCreate(Context pContext, SQLiteDatabase pSQLiteDatabase) {
        pSQLiteDatabase.execSQL(createStatement());
    }

    public static void onUpgrade(Context pContext, SQLiteDatabase pSQLiteDatabase, int pOldVersion, int pNewVersion) {

    }

    protected static String createStatement(){
        StringBuilder createTable = new StringBuilder();
        createTable.append("Create Table `venue` (`");
        createTable.append(COLUMN_NAME_ID);
        createTable.append("` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        createTable.append("`name` VARCHAR(255), ");
        createTable.append("`address` VARCHAR(255), ");
        createTable.append("`latitude` DOUBLE, ");
        createTable.append("`longitude` DOUBLE, ");
        createTable.append("`postal_code` VARCHAR(255) ");
        createTable.append(");");
        return createTable.toString();
    }
}
