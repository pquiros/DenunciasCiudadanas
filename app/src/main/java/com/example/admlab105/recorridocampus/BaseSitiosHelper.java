package com.example.admlab105.recorridocampus;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.util.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Esta clase se encargar de poblar la base de datos y prooveer metodos de acceso a la base.
 */

public class BaseSitiosHelper extends SQLiteOpenHelper {

    private static BaseSitiosHelper sInstance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BaseSitios.db";
    Context context;
//https://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
    //Para compartir la db
    public static synchronized BaseSitiosHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new BaseSitiosHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private BaseSitiosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    /**
     * Crear las tablas y cargar la base, se ejecuta solo una vez
     * @param dB base de datos
     */
    public void onCreate(SQLiteDatabase dB) {

        dB.execSQL("CREATE TABLE " + BaseSitiosContract.SitioBase.TABLE_NAME + " (" +
                BaseSitiosContract.SitioBase._ID + " INTEGER PRIMARY KEY," +
                BaseSitiosContract.SitioBase.COLUMN_NOMBRE + " TEXT" + "," +
                BaseSitiosContract.SitioBase.COLUMN_COORDENADA_X + " REAL" + "," +
                BaseSitiosContract.SitioBase.COLUMN_COORDENADA_Y + " REAL" + "," +
                BaseSitiosContract.SitioBase.COLUMN_RADIO + " REAL" + "," +
                BaseSitiosContract.SitioBase.COLUMN_VISITADO + " INTEGER" + " )");

        dB.execSQL("CREATE TABLE " + BaseSitiosContract.Foto.TABLE_NAME + " (" +
                BaseSitiosContract.Foto._ID + " INTEGER PRIMARY KEY," +
                BaseSitiosContract.Foto.ID_SITIO + " TEXT " + "," +
                BaseSitiosContract.Foto.RUTA + " TEXT " + " )");

        dB.execSQL("CREATE TABLE " + BaseSitiosContract.Video.TABLE_NAME + " (" +
                BaseSitiosContract.Video._ID + " INTEGER PRIMARY KEY," +
                BaseSitiosContract.Video.ID_SITIO + " INTEGER " + "," +
                BaseSitiosContract.Video.RUTA + " TEXT " + " )");

        dB.execSQL("CREATE TABLE " + BaseSitiosContract.Audio.TABLE_NAME + " (" +
                BaseSitiosContract.Audio._ID + " INTEGER PRIMARY KEY," +
                BaseSitiosContract.Audio.ID_SITIO + " INTEGER " + "," +
                BaseSitiosContract.Audio.RUTA + " TEXT " + " )");

        dB.execSQL("CREATE TABLE " + BaseSitiosContract.Texto.TABLE_NAME + " (" +
                BaseSitiosContract.Texto._ID + " INTEGER PRIMARY KEY," +
                BaseSitiosContract.Texto.NOMBRE+ " TEXT" + "," +
                BaseSitiosContract.Texto.RUTA + " TEXT " + " )");

        llenaBase(dB);
    }

    /**
     * Este metodo actualiza la base de datos
     * @param dB base de datos que se quiere actualizar
     * @param oVersion numero anterior de version de base de datos
     * @param nVersion numero actual de version de base de datos.
     */
    public void onUpgrade(SQLiteDatabase dB, int oVersion, int nVersion) {
        dB.execSQL("DROP TABLE IF EXISTS " + BaseSitiosContract.SitioBase.TABLE_NAME);
       // dB.execSQL("DROP TABLE IF EXISTS " + BaseSitiosContract.Usuario.TABLE_NAME);
        dB.execSQL("DROP TABLE IF EXISTS " + BaseSitiosContract.Foto.TABLE_NAME);
        dB.execSQL("DROP TABLE IF EXISTS " + BaseSitiosContract.Video.TABLE_NAME);
        dB.execSQL("DROP TABLE IF EXISTS " + BaseSitiosContract.Texto.TABLE_NAME);
        dB.execSQL("DROP TABLE IF EXISTS " + BaseSitiosContract.Audio.TABLE_NAME);
        onCreate(dB);
    }

    /**
     * Llena la base de datos con la informacion de sitios en archivo llamado coordenadas
     * @param dB base de datos
     */
    void llenaBase(SQLiteDatabase dB) {

        //coordenadas
        try
        {
            InputStream fraw =  context.getResources().openRawResource(R.raw.coordenadas);
            BufferedReader br = new BufferedReader(new InputStreamReader(fraw));
            String linea ="";
            String[] sitioPartes = null;
            int count = 0;
                while ((linea = br.readLine()) != null) {
                    sitioPartes = linea.split(",");    //nombre,coordenada x , coordenaday , numero de fotos, nombres de fotos..,
                    ContentValues values = new ContentValues();
                    values.put(BaseSitiosContract.SitioBase.COLUMN_NOMBRE, sitioPartes[0]);
                    values.put(BaseSitiosContract.SitioBase.COLUMN_COORDENADA_X, sitioPartes[2]);
                    values.put(BaseSitiosContract.SitioBase.COLUMN_COORDENADA_Y, sitioPartes[3]);
                    values.put(BaseSitiosContract.SitioBase.COLUMN_RADIO, sitioPartes[4]);
                    values.put(BaseSitiosContract.SitioBase.COLUMN_VISITADO, 0);
                    long newRowId = dB.insert(BaseSitiosContract.SitioBase.TABLE_NAME, null, values);
                    String pp="0";

                    //carga las imagenes de cada siti mmnmo
                    //int cantidadFotos = Integer.parseInt(sitioPartes[3]);
                    if(Integer.parseInt(sitioPartes[5])>0) {
                        for (int i = 0; i < Integer.parseInt(sitioPartes[5]); i++) {
                            ContentValues values3 = new ContentValues();
                            values3.put(BaseSitiosContract.Foto.ID_SITIO, sitioPartes[0]); //IMPORTANTE aqui en vez de un id va el nombre del sitio para mas facilidad
                            values3.put(BaseSitiosContract.Foto.RUTA, sitioPartes[6+i]);
                            //Toast.makeText(context,sitioPartes[4+i],Toast.LENGTH_SHORT).show();
                            long iRowId = dB.insert(BaseSitiosContract.Foto.TABLE_NAME, null, values3);
                        }
                    }
                    ContentValues values2 = new ContentValues();
                    values2.put(BaseSitiosContract.Texto.NOMBRE, sitioPartes[0]);
                    values2.put(BaseSitiosContract.Texto.RUTA, sitioPartes[1]);
                    long no = dB.insert(BaseSitiosContract.Texto.TABLE_NAME, null, values2);


                }

            fraw.close();
            br.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde recurso raw");
        }




    }


    /**
     * Devuelve un cursor que da acceso a todas las tuplas en los campos nombre ,coordenadaX y coordenadaY de la tabla sitio.
     * @return cursor
     */
    public Cursor obtenerLugares() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c=null;

        if (db != null) {
            c = db.rawQuery(" SELECT nombre,coordenadaX,coordenadaY FROM sitio ", null);
        }
        return c;
    }
    public double obtengaX(String s){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c=null;
        if (db != null) {
        c = db.rawQuery(" SELECT coordenadaX FROM sitio WHERE nombre = \""+s+"\"", null);
        c.moveToFirst();
        } return (c.getColumnCount()!=0)? c.getDouble(0): 99.0 ;
    }
    public double obtengaY(String s){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c=null;
        if (db != null) {
            c = db.rawQuery("SELECT coordenadaY FROM sitio WHERE nombre = \"" + s + "\"" , null);
            c.moveToFirst();
        } return (c.getColumnCount()!=0)? c.getDouble(0): 99.0 ;
    }
    public String obtengaTexto(String s){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c=null;
        if (db != null) {
            c = db.rawQuery("SELECT ruta FROM Texto WHERE nombre = \""+s+"\"", null);
            int o =0;
            c.moveToFirst();
        } return (c.getColumnCount()!=0)? c.getString(0): "edificio_de_la_facultad_de_educacion" ;
    }


    /**
     * Devuelve un cursor con el nombre de las imagenes de el sitio indicado
     * @param id_nombreSitio del sitio del cual se desean recuperar las imagenes
     * @return c
     */
    public Cursor obtenerImagenesDeSitio(String id_nombreSitio) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c=null;
        if (db != null) {
            c = db.rawQuery(" SELECT ruta FROM Foto WHERE id_sitio = \"" + id_nombreSitio + "\"", null);
        }
        return c;
    }


}