package com.example.vito.wakemeup;

/**
 * Created by vito on 19/10/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TransactionsDB {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "hobbies.db";

    private static final String TABLE_ACTIVITIES = "table_activities";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_ACTIVITY1 = "ACTIVITY1";
    private static final int NUM_COL_ACTIVITY1 = 1;
    private static final String COL_ACTIVITY2 = "ACTIVITY2";
    private static final int NUM_COL_ACTIVITY2 = 2;
    private static final String COL_ACTIVITY3 = "ACTIVITY3";
    private static final int NUM_COL_ACTIVITY3 = 3;

    private SQLiteDatabase db;

    private SQLiteDB SQLiteDB;

    public TransactionsDB(Context context){
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

    public SQLiteDatabase getDB(){
        return db;
    }

    public long insertHobbies(Hobbies hobbies){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_ID,1);
        values.put(COL_ACTIVITY1, hobbies.getActivity1());
        values.put(COL_ACTIVITY2, hobbies.getActivity2());
        values.put(COL_ACTIVITY3, hobbies.getActivity3());
        //on insère l'objet dans la BDD via le ContentValues
        return db.insert(TABLE_ACTIVITIES, null, values);
    }

    public int updateHobbies(int id, Hobbies hobbies){
        //La mise à jour d'un hobbie dans la DB fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle hobbie on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_ACTIVITY1, hobbies.getActivity1());
        values.put(COL_ACTIVITY2, hobbies.getActivity2());
        values.put(COL_ACTIVITY3, hobbies.getActivity3());
        return db.update(TABLE_ACTIVITIES, values, COL_ID + " = " +id, null);
    }

    public int removeHobbiesById(int id){
        //Suppression des hobbies de la DB grâce à l'ID
        return db.delete(TABLE_ACTIVITIES, COL_ID + " = " +id, null);
    }

    public Hobbies getHobbiesById(int id){
        //Récupère dans un Cursor les valeur correspondant
        Cursor c = db.query(TABLE_ACTIVITIES, new String[] {COL_ID, COL_ACTIVITY1, COL_ACTIVITY2, COL_ACTIVITY3}, COL_ID + " LIKE \"" + id +"\"",null,null,null,null);
        return cursorToHobbies(c);
    }

    //Cette méthode permet de convertir un cursor en hobbie
    private Hobbies cursorToHobbies(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un hobbie
        Hobbies hobbies = new Hobbies();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        hobbies.setId(c.getInt(NUM_COL_ID));
        hobbies.setActivity1(c.getString(NUM_COL_ACTIVITY1));
        hobbies.setActivity2(c.getString(NUM_COL_ACTIVITY2));
        hobbies.setActivity3(c.getString(NUM_COL_ACTIVITY3));
        //On ferme le cursor
        c.close();

        //On retourne les hobbies
        return hobbies;
    }
}
