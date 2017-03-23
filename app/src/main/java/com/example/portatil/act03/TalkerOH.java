package com.example.portatil.act03;

/**
 * Created by Portatil on 24/02/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TalkerOH {

    // Database fields
    private SQLiteDatabase baseDadesLlegir,baseDadesEscriure;
    private MyOpenHelper bdAjudant;
    private Cursor cursor;

    public TalkerOH(Context context) {
        //per poder comunucarnos amb la base de dades
        bdAjudant = new MyOpenHelper(context);
        //creme dos database per lleguir i modifica la bsase de dades
        baseDadesLlegir= bdAjudant.getReadableDatabase();
        baseDadesEscriure=bdAjudant.getWritableDatabase();
    }
    //per tancar la comunicacio de la base de dades
    @Override
    protected void finalize(){
        baseDadesLlegir.close();
        baseDadesEscriure.close();
    }

    //METODES QUE SERVEIXEN PER LLEGUIR CAMPS DE LA BASE DE DADES\\
    //metode que retorna un curso amb tota la informacio de totes les columnes que te la base de dades
    public Cursor carregaTotaLaTaula() {
        // Retorem totes les tasques
        return baseDadesLlegir.query(bdAjudant.TABLE_PRODUCTES, new String[]{bdAjudant.COLUMN_ID,bdAjudant.COLUMN_CODI,bdAjudant.COLUMN_DESCRIPCIO,bdAjudant.COLUMN_PVP,bdAjudant.COLUMN_STOCK},
                null, null,
                null, null, bdAjudant.COLUMN_ID);
    }
    //metode que retorna els valors de una sola row, aixo volr dir que busca la PK y torna la info dels camps de aquella PK
    public Cursor carregaPerId(long id) {
        // Retorna un cursor només amb el id indicat
        return baseDadesLlegir.query(bdAjudant.TABLE_PRODUCTES, new String[]{bdAjudant.COLUMN_ID,bdAjudant.COLUMN_CODI,bdAjudant.COLUMN_DESCRIPCIO,bdAjudant.COLUMN_PVP,bdAjudant.COLUMN_STOCK},
                bdAjudant.COLUMN_ID+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }
    public Cursor filtre() {
        // Retornem les tasques que el camp DONE = 0
        return baseDadesLlegir.query(bdAjudant.TABLE_PRODUCTES, new String[]{bdAjudant.COLUMN_ID,bdAjudant.COLUMN_CODI,bdAjudant.COLUMN_DESCRIPCIO,bdAjudant.COLUMN_PVP,bdAjudant.COLUMN_STOCK},
                bdAjudant.COLUMN_DESCRIPCIO+ "=?", new String[]{String.valueOf("")},
                null, null, null);
    }

    public Cursor codiRepetit(String codi) {
        // Retornem les tasques que el camp DONE = 0
        return baseDadesLlegir.query(bdAjudant.TABLE_PRODUCTES, new String[]{bdAjudant.COLUMN_ID,bdAjudant.COLUMN_CODI,bdAjudant.COLUMN_DESCRIPCIO,bdAjudant.COLUMN_PVP,bdAjudant.COLUMN_STOCK},
                bdAjudant.COLUMN_CODI+ "=?", new String[]{String.valueOf(codi)},
                null, null, null);
    }
    ///////PART PER EL HISTORIAL
    //tota la taula carregada del historial
    public Cursor carregaTotaLaTaulaHistorial() {
        // Retorem totes les tasques
        return baseDadesLlegir.query(bdAjudant.TABLE_MOVIMENTS, new String[]{bdAjudant.MOVIMENTS_ID,bdAjudant.MOVIMENTS_CODI,bdAjudant.MOVIMENTS_DIA,bdAjudant.MOVIMENTS_QUANTITAT,bdAjudant.MOVIMENTS_TIPUS,bdAjudant.MOVIMENT_DATA},
                null, null,
                null, null, bdAjudant.MOVIMENTS_ID);
    }
    //busquem una row per la seu codi
    public int carregarCodi(String codi) {

        Integer stock=0;
        // Retorna un cursor només amb el id indicat
        cursor= baseDadesLlegir.query(bdAjudant.TABLE_PRODUCTES, new String[]{bdAjudant.COLUMN_STOCK},
                bdAjudant.COLUMN_CODI + "=?", new String[]{String.valueOf(codi)},
                null, null, null);
        if(cursor.moveToFirst()){
            stock= cursor.getInt(0);
        }

        return stock;
        //
    }
    //busquem una row per la seu codi
    public Cursor historicCodi(String codi) {

        // Retorna un cursor només amb el id indicat
        return  cursor= baseDadesLlegir.query(bdAjudant.TABLE_MOVIMENTS, new String[]{bdAjudant.MOVIMENTS_ID,bdAjudant.MOVIMENTS_CODI,bdAjudant.MOVIMENTS_DIA,bdAjudant.MOVIMENTS_QUANTITAT,bdAjudant.MOVIMENTS_TIPUS,bdAjudant.MOVIMENT_DATA},
                bdAjudant.MOVIMENTS_CODI + "=?", new String[]{String.valueOf(codi)},
                null, null, null);

        //
    }
    //busquem per la data!!!
    public Cursor historicData(String codi) {

        // Retorna un cursor només amb el id indicat
        return  cursor= baseDadesLlegir.query(bdAjudant.TABLE_MOVIMENTS, new String[]{bdAjudant.MOVIMENTS_ID,bdAjudant.MOVIMENTS_CODI,bdAjudant.MOVIMENTS_DIA,bdAjudant.MOVIMENTS_QUANTITAT,bdAjudant.MOVIMENTS_TIPUS,bdAjudant.MOVIMENT_DATA},
                bdAjudant.MOVIMENTS_DIA + "=?", new String[]{String.valueOf(codi)},
                null, null, null);

        //
    }

    //METODES QUE SERVEIXEN PER MODIFICAR LA BASE DE DADES \\
//metode que fa insert a la base de dades en la taula productes
    public long AfegirProducte(String codi, String descripcio, double pvp , int stock){

        ContentValues valors =new ContentValues();

        valors.put(bdAjudant.COLUMN_CODI, codi);
        valors.put(bdAjudant.COLUMN_DESCRIPCIO, descripcio);
        valors.put(bdAjudant.COLUMN_PVP, pvp);
        valors.put(bdAjudant.COLUMN_STOCK, stock);

        return baseDadesEscriure.insert(bdAjudant.TABLE_PRODUCTES,null,valors);
    }
    //metode per fer updates dels camps que deixem que es puguin cambiar mitjançant la PK de la row
    public void ModificarProducte(long id, String descripcio, double pvp , int stock) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues valors = new ContentValues();
        valors.put(bdAjudant.COLUMN_DESCRIPCIO, descripcio);
        valors.put(bdAjudant.COLUMN_PVP, pvp);
        valors.put(bdAjudant.COLUMN_STOCK, stock);

        baseDadesEscriure.update(bdAjudant.TABLE_PRODUCTES,valors, bdAjudant.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }
    //metode per eliminar una row atraves de la seva id=PK
    public void ElminarProducte(long id) {
        // Eliminem la task amb clau primària "id"
        baseDadesEscriure.delete(bdAjudant.TABLE_PRODUCTES,bdAjudant.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }
    /////METODES DE LA NOVA PART DEL ACT02 CONTROL HISTORIAL
    //metode que fa insert a la base de dades en la taula productes
    public long AfegirHistorial(String codi, String dia, int stock , String tipus, String data){

        ContentValues valors =new ContentValues();

        valors.put(bdAjudant.MOVIMENTS_CODI, codi);
        valors.put(bdAjudant.MOVIMENTS_DIA, dia);
        valors.put(bdAjudant.MOVIMENTS_QUANTITAT, stock);
        valors.put(bdAjudant.MOVIMENTS_TIPUS, tipus);
        valors.put(bdAjudant.MOVIMENT_DATA, data);

        return baseDadesEscriure.insert(bdAjudant.TABLE_MOVIMENTS,null,valors);
    }
    public void CambisEnElStock(String codi , int stock) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues valors = new ContentValues();

        valors.put(bdAjudant.COLUMN_STOCK, stock);

        baseDadesEscriure.update(bdAjudant.TABLE_PRODUCTES,valors, bdAjudant.COLUMN_CODI + " = ?", new String[] { String.valueOf(codi) });
    }
/////METODES DE LA NOVA PART DEL ACT03 CONTROL CIUTATS JSON
    //metode per carregar tots els camps de la taula ciutat retornan un cursor que carregarem a la llista mostran les ciutats
       public Cursor carregarCiutats() {
        // Retorem totes les tasques
        return baseDadesLlegir.query(bdAjudant.TABLE_TEMPS, new String[]{bdAjudant.TEMPS_ID,bdAjudant.TEMPS_CIUTAT},
                null, null,
                null, null, bdAjudant.TEMPS_ID);
    }
    //metode que fa un insert a la taula de ciutats quan volem posar una nova ciutat a la nostra app
    public void AfegirCiutat(String ciutat){

        ContentValues valors =new ContentValues();

        valors.put(bdAjudant.TEMPS_CIUTAT, ciutat);

        baseDadesEscriure.insert(bdAjudant.TABLE_TEMPS,null,valors);
    }
    //metode que retorna un curso que farem servir per mostrar el nom de la ciutat que em clickat en la llita en un altre activity
    public Cursor carregaPerIdNomCiutat(long id) {
        // Retorna un cursor només amb el id indicat
        return baseDadesLlegir.query(bdAjudant.TABLE_TEMPS, new String[]{bdAjudant.TEMPS_ID,bdAjudant.TEMPS_CIUTAT},
                bdAjudant.TEMPS_ID+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }
}

