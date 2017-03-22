package com.example.portatil.act03;

/**
 * Created by Portatil on 24/02/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyOpenHelper extends SQLiteOpenHelper {
    //nova taula que necesitem per la pract02, per fer l'historic dels productes
    public static final String TABLE_MOVIMENTS = "moviments";
    public static final String MOVIMENTS_ID = "_id";
    public static final String MOVIMENTS_CODI = "codi";
    public static final String MOVIMENTS_DIA = "dia";
    public static final String MOVIMENTS_QUANTITAT = "quantitat";
    public static final String MOVIMENTS_TIPUS = "tipus";
    public static final String MOVIMENT_DATA="data_mostrar";

    //camps de la taula y les columnes
    public static final String TABLE_PRODUCTES = "productes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CODI = "codi";
    public static final String COLUMN_DESCRIPCIO = "descripcio";
    public static final String COLUMN_PVP = "pvp";
    public static final String COLUMN_STOCK = "stock";

    //camps de la taula y les columnes per la taula del temps de les ciutats
    public static final String TABLE_TEMPS = "temps";
    public static final String TEMPS_ID = "_id";
    public static final String TEMPS_CIUTAT = "ciutat";



    //nom de la bs i la seva versio
    private static final String DATABASE_NAME = "productes.db";
    private static final int DATABASE_VERSION = 3;//aumentem la versio ja que tenim la seguent

    //Creacio de la base de dades de la taula de productes
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PRODUCTES + "( " +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_CODI + " text not null, " +
            COLUMN_DESCRIPCIO + " text not null, " +
            COLUMN_PVP + " float not null, " +
            COLUMN_STOCK + " integer not null);";
    //Creacio de la taula de moviment, control historic
    private static final String DATABASE_CREATE_MOVIMENTS = "create table "
            + TABLE_MOVIMENTS + "( " +
            MOVIMENTS_ID + " integer primary key autoincrement, " +
            MOVIMENTS_CODI + " text not null, " +
            MOVIMENTS_DIA + " text not null, " +
            MOVIMENTS_QUANTITAT + " integer not null, " +
            MOVIMENTS_TIPUS + " text not null, " +
            MOVIMENT_DATA + " text not null);";
    //Creacio de les ciutats i el temps
    private static final String DATABASE_CREATE_TEMPS = "create table "
            + TABLE_TEMPS + "( " +
            TEMPS_ID + " integer primary key autoincrement, " +
            TEMPS_CIUTAT + " text not null);";
    //metode  constructor del openhelper per quan necesitem acedir a la BD
    public MyOpenHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //en el on create genera la nova base de dades si es el primer cop, sino tindriem que mirar version actuals
    //y fer un upgrade de la base de dades
    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE_MOVIMENTS);
        database.execSQL(DATABASE_CREATE_TEMPS);
        CiutatsPerDefecte(database);

    }
    //de moment no serveix per res ja que la versio sempre es la 1
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* aqui ira codigo sobre updates de la bd nuevas actualizaciones*/
        //si es versio 2
        switch(oldVersion) {
            case 1:
                db.execSQL(DATABASE_CREATE_MOVIMENTS);
            case 2:
                db.execSQL(DATABASE_CREATE_TEMPS);
                CiutatsPerDefecte(db);
                // we want both updates, so no break statement here...
                break;
        }
    }
    //control per fer les entrades per defecte en la base de dades
    public void CiutatsPerDefecte(SQLiteDatabase db){
        ContentValues valors =new ContentValues();
        valors.put(TEMPS_CIUTAT,"Barcelona");
        db.insert(TABLE_TEMPS,null,valors);
        valors.put(TEMPS_CIUTAT,"Girona");
        db.insert(TABLE_TEMPS,null,valors);
        valors.put(TEMPS_CIUTAT,"Tarregona");
        db.insert(TABLE_TEMPS,null,valors);
        valors.put(TEMPS_CIUTAT,"Lleida");
        db.insert(TABLE_TEMPS,null,valors);
    }
}
