package com.example.vito.wakemeup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by vito on 26/10/16.
 */

public class TransactionsDBSleepTimes {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "SleepTimes.db";

    // VARIABLES POUR TABLE HEURES DE SOMMEIL
    private static final String TABLE_SLEEP = "table_sleep";
    private static final String SLEEP_ID = "ID";
    private static final int NUM_COL_SLEEP_ID = 0;
    //Variable numéro de semaine
    private static final String SLEEP_WEEK = "WEEK";
    private static final int NUM_COL_SLEEP_WEEK = 1;
    //variables heure couché/heure réveil
    private static final String SLEEP_TIME1 = "TIME1";
    private static final int NUM_COL_SLEEP_TIME1 = 2;
    private static final String SLEEP_TIME2 = "TIME2";
    private static final int NUM_COL_SLEEP_TIME2 = 3;

    private SQLiteDatabase db;

    private SQLiteDB SQLiteDB;

    public TransactionsDBSleepTimes(Context context){
        //creation db & table
        SQLiteDB = new SQLiteDB(context, DB_NAME, null, DB_VERSION);
    }

    public void open(){
        //on ouvre la BDD en écriture
        db = SQLiteDB.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        db.close();
    }

    public SQLiteDatabase getBDD(){
        return db;
    }

    public long insertSleepTimes(SleepTimes sleeptimes){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(SLEEP_WEEK, sleeptimes.getWeek());
        values.put(SLEEP_TIME1, sleeptimes.getTime1());
        values.put(SLEEP_TIME2, sleeptimes.getTime2());
        //on insère l'objet dans la BDD via le ContentValues
        return db.insert(TABLE_SLEEP, null, values);
    }

    public int updateSleepTimes(int id, SleepTimes sleepTimes){
        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle journée on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(SLEEP_WEEK, sleepTimes.getWeek());
        values.put(SLEEP_WEEK, sleepTimes.getTime1());
        values.put(SLEEP_WEEK, sleepTimes.getTime2());
        return db.update(TABLE_SLEEP, values, SLEEP_ID + " = " +id, null);
    }

    public int removeSleepTimesById(int id){
        //Suppression des journées de sommeil de la DB grâce à l'ID
        return db.delete(TABLE_SLEEP, SLEEP_ID + " = " +id, null);
    }

    public SleepTimes getSleepTimesById(int id){
        //Récupère dans un Cursor les valeur correspondant
        Cursor c = db.query(TABLE_SLEEP, new String[] {SLEEP_ID, SLEEP_WEEK, SLEEP_TIME1, SLEEP_TIME2}, SLEEP_ID + " LIKE \"" + id +"\"", null, null, null, null);
        return cursorToSleepTimes(c);
    }

    //Cette méthode permet de convertir un cursor
    private SleepTimes cursorToSleepTimes(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé une journée de sommeil
        SleepTimes sleepTimes = new SleepTimes();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        sleepTimes.setId(c.getInt(NUM_COL_SLEEP_ID));
        sleepTimes.setWeek(c.getString(NUM_COL_SLEEP_WEEK));
        sleepTimes.setTime1(c.getString(NUM_COL_SLEEP_TIME1));
        sleepTimes.setTime2(c.getString(NUM_COL_SLEEP_TIME2));
        //On ferme le cursor
        c.close();

        //On retourne les sleepTimes
        return sleepTimes;
    }
}
