package com.automatica.AXCPT.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.ArrayList;

public class dbNotificaciones extends DaoSQLite{
    Context contexto;
    SQLiteDatabase db;
    public dbNotificaciones(Context contexto) {
        super(contexto);
        this.contexto= contexto;
    }

    public long insertarAviso(String Mensaje, String Tipo, String Status, String IDWS, String Documento, String Fecha){
        long id = 0;
        DaoSQLite daoSQLite = new DaoSQLite(contexto);
        SQLiteDatabase db = daoSQLite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Entradas.columnaMensaje,Mensaje);
        values.put(Entradas.columnaTipo,Tipo);
        values.put(Entradas.columnaStatus,Status);
        values.put(Entradas.columnaIDWS,IDWS);
        values.put(Entradas.columnaDocumento,Documento);
        values.put(Entradas.columnaFecha,Fecha);
        try {
            id = db.insert(Entradas.NombreTabla,null,values);
        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public boolean actualizarAviso(String id){
        boolean accion=true;
        try {
            DaoSQLite daoSQLite = new DaoSQLite(contexto);
            db = daoSQLite.getWritableDatabase();
            db.execSQL("UPDATE "+Entradas.NombreTabla+" SET "+Entradas.columnaStatus+" =2 where "+ Entradas.columnaIDWS+
                    " = "+id);
        }catch (Exception e){
            e.printStackTrace();
            accion=false;
        }finally {
            db.close();
        }
        return accion;
    }

    public ArrayList<Constructor_Dato> qryAvisos(){
        ArrayList<Constructor_Dato> lista= new ArrayList<>();
        boolean accion=true;

        try {
            DaoSQLite daoSQLite = new DaoSQLite(contexto);
            db = daoSQLite.getWritableDatabase();
            String query= "SELECT * FROM "+Entradas.NombreTabla+" WHERE "+Entradas.columnaStatus+"= 1"+" ";
            Cursor cursor= db.rawQuery(query,null);
            if (cursor!=null){
                if (cursor.moveToFirst()){
                    do {
                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                            lista.add(new Constructor_Dato(
                                    cursor.getString(1),//Mensaje Titulo
                                    cursor.getString(2),//Tipo   Dato
                                    cursor.getString(3),//Estatus Tag1
                                    cursor.getString(4),//IDWS   Tag2
                                    cursor.getString(5),//Documento  Tag3
                                    cursor.getString(6)//Fecha  Tag4
                            ));
                            //Log.i("Elemento",lista.get(i).toString());
                        }
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }

        return lista;
    }


    public ArrayList<Constructor_Dato> qryTablaNotificaciones(){
        ArrayList<Constructor_Dato> lista= new ArrayList<>();
        boolean accion=true;
        DaoSQLite daoSQLite = new DaoSQLite(contexto);
        db = daoSQLite.getWritableDatabase();
        try {
            String query= "SELECT * FROM "+Entradas.NombreTabla;
            Cursor cursor= db.rawQuery(query,null);
            if (cursor!=null){
                if (cursor.moveToFirst()){
                    do {
                        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                            lista.add(new Constructor_Dato(
                                    cursor.getString(1),//Mensaje
                                    cursor.getString(2),//Tipo
                                    cursor.getString(3),//Estatus
                                    cursor.getString(4),//IDWS
                                    cursor.getString(5),//Documento
                                    cursor.getString(6)//Fecha
                            ));
                            //Log.i("Elemento",lista.get(i).toString());
                            /**
                            Titulo = Mensaje
                             Dato= Tipo
                             Tag1= Status
                             Tag2= Id del WS
                             Tag3= Id Interno
                             */
                        }
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        Log.i("Lista",lista.toString());
        return lista;
    }

}
