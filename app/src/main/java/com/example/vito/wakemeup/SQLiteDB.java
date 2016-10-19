package com.example.vito.wakemeup;

/**
 * Created by vito on 19/10/16.
 *
 *
 * Création de notre BDD qui regroupe toutes nos activités
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteDB extends SQLiteOpenHelper {

    private static final String TABLE_ACTIVITIES = "table_activities";
    private static final String COL_ID = "ID";
    private static final String COL_ACTIVITY1 = "ACTIVITY1";
    private static final String COL_ACTIVITY2 = "ACTIVITY2";
    private static final String COL_ACTIVITY3 = "ACTIVITY3";

    private static final String CREATE_DB = "CREATE TABLE " + TABLE_ACTIVITIES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_ACTIVITY1 + " TEXT NOT NULL, "
            + COL_ACTIVITY2 + " TEXT NOT NULL, "+ COL_ACTIVITY3 + " TEXT NOT NULL );";

    public SQLiteDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Création table à partir de CREATE_BDD
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + TABLE_ACTIVITIES + ";");
        onCreate(db);
    }

}