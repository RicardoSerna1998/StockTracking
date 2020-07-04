package com.example.stocktracking.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase database) {
        script(database);
    }


    public static void script(SQLiteDatabase database) {
        String cmd2 = "CREATE TABLE " + DBvariables.SYMBOLS + " (" +
                DBvariables.Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBvariables.Columns.SYMBOL_NAME + " TEXT, " +
                DBvariables.Columns.PRICE + " TEXT, " +
                DBvariables.Columns.HIGH+ " TEXT, " +
                DBvariables.Columns.LOW + " TEXT)";
        database.execSQL(cmd2);
    }

    //@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            clean(db);
            onCreate(db);
    }

    public static void clean(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + DBvariables.SYMBOLS);
        script(db);
    }

}
