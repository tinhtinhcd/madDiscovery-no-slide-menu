package com.anhlt.maddiscover.data.tables;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by anhlt on 2/19/16.
 */
public class Organizers extends BaseTable{

    public static final String id = COLUMN_NAME_ID;
    public static final String name = "name";
    public static final String mobile = "mobile";
    public static final String email = "email";
    public static final String address = "address";
    public static final String about = "about";
    public static String TABLE_NAME = "organizers";

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
        createTable.append("`name` VARCHAR(255), ");
        createTable.append("`mobile` VARCHAR(255), ");
        createTable.append("`email` VARCHAR(255), ");
        createTable.append("`address` VARCHAR(255), ");
        createTable.append("`about` VARCHAR(10000) ");
        createTable.append(");");
        return createTable.toString();
    }
}
