package com.automatica.AXCPT.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DaoSQLite extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION= 1;
    public static final String DATABASE_NAME= "AXCPTSqlite";


    public static class Entradas implements BaseColumns {
        public static final String NombreTabla="t_Notificaciones";
        public static final String columndaID="ID";
        public static final String columnaMensaje="Mensaje";
        public static  final String columnaTipo="Tipo";
        public static final String columnaStatus="Status";
        public  static  final String columnaIDWS="IDws";
        public static final String columnaDocumento="Documento";
        public static final String columnaFecha="Fecha";
    }

    public DaoSQLite(Context contexto){
        super(contexto,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ Entradas.NombreTabla+
                "(" + Entradas.columndaID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Entradas.columnaMensaje+" TEXT,"
                    + Entradas.columnaTipo+" TEXT,"
                    + Entradas.columnaStatus+" TEXT,"
                    + Entradas.columnaIDWS+" TEXT, "
                    + Entradas.columnaDocumento+" TEXT, "
                    + Entradas.columnaFecha+" TEXT"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE "+ Entradas.NombreTabla);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
