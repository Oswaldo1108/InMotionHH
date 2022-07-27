package com.automatica.AXCPT.Servicios;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.automatica.AXCPT.SQLite.DaoSQLite;
import com.automatica.AXCPT.SQLite.dbNotificaciones;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adUtilidades;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HiloNotificaciones extends Service {
    /**
     * Consulta cada 10 segundos si hay notificaciones nuevas desde el administrador
     */
    String Mensaje,Tipo,Fecha,Documento;
    SharedPreferences pref;
    DaoSQLite sqlHelper;
    SQLiteDatabase database;
    TimerTask timerTask;
    dbNotificaciones dbNotificaciones;
    ScheduledExecutorService scheduleTaskExecutor;
    public interfazHilo hilo;
    public void onCreate(){
        super.onCreate();
        //Log.d("Tarea","Servicio creado...");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
             sqlHelper=new DaoSQLite(getApplicationContext());
             database =sqlHelper.getWritableDatabase();
             try {
                 sqlHelper.onCreate(database);
             }catch (Exception e){
                 try {
                     e.printStackTrace();
                     sqlHelper.onUpgrade(database,0,1);
                 }catch (Exception f){
                     f.printStackTrace();
                 }
             }
            dbNotificaciones = new dbNotificaciones(getApplicationContext());
             if (database==null){
                 Toast.makeText(getApplicationContext(),"No se creo base de datos", Toast.LENGTH_LONG).show();
                 this.stopSelf();
             }
        }catch (Exception e){
            e.printStackTrace();
            new creaNotificacion(getApplicationContext(),"Contacte a AXC","La información ingresada ha generado una excepción",e.getMessage());

        }
        try{
            hilo= new interfazHilo() {
                @Override
                public void ComunicacionFragMensajes() {

                }
            };
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Timertask para realizar la consulta cada 10 segundos
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("Tarea","Servicio iniciado...");
        Log.i("Booleano", String.valueOf(pref.getBoolean("booleanNotificaciones",false)));
        if (pref.getBoolean("booleanNotificaciones",false)){
            getSystemService(ACTIVITY_SERVICE);
            Timer timer = new Timer();

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (pref.getBoolean("booleanNotificaciones",false)){
                        new SegundoPlano("ConsultaNoficacion").execute();
                    }else {
                        stopSelf();
                    }
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 10000);
            return START_STICKY;
        }else {
            stopSelf();
            Log.i("Estado", "Se intenta detener");
            return START_NOT_STICKY;
        }
    }
    private class  SegundoPlano extends AsyncTask<String, Void, Void> {
        String Tarea;
        DataAccessObject dao, dao2;
        adUtilidades ca= new adUtilidades(getApplicationContext());
        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                switch (Tarea) {
                    case "ConsultaID":
                        break;
                    case "ConsultaNoficacion":
                        dao= ca.cConsultarNotificaciones(String.valueOf(pref.getInt("IdNotificacion",0)));
                        break;
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try{
                    if (dao.getcTablaUnica()!=null){
                        Log.e("Proceso", "ArmarNotificaciones2()");
                    ArmarNotificaciones2(dao);

                }else {
                    Log.i("Estado Tabla","Vacio");
                }
                Intent BADGE = new Intent();
                BADGE.setAction("com.example.axc.notification.BADGE");
                sendBroadcast(BADGE);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public String ObtenerTipo(String Tipo){
        switch (Tipo){
            case "1":
                Tipo="Aviso";
                break;
            case "2":
                Tipo="Almacén";
                break;
            case "3":
                Tipo="Precarga";
                break;
            case "4":
                Tipo="Surtido";
                break;
            case "5":
                Tipo="Reempaque sin Etiqueta";
                break;
            case "6":
                Tipo="Valida";
                break;
            case "7":
                Tipo="Embarque";
                break;
            case "8":
                Tipo="Inventarios";
                break;
            case "9":
                Tipo="Envios";
                break;
            case "10":
                Tipo="Registro de Prod.";
                break;
            case "11":
                Tipo="Recepción";
                break;
            default:
                Tipo="Otro";
                break;
        }
        return Tipo;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ArmarNotificaciones2(DataAccessObject dao){
        String[][] tabla= dao.getcTablaUnica();
        Tipo = tabla[0][1];
        Mensaje=tabla[0][2];
        Documento= tabla[0][3];
        Fecha=tabla[0][4];
        Log.e("Documento", Documento);
        Log.e("Documento", Mensaje);
        Log.e("Documento", Tipo);
 /*       int idInt;
        String[][] tabla= dao.getcTablaUnica();
        for (int i=0;i<dao.getcTablaUnica().length;i++){
            Id = tabla[i][0];
            Log.e("IdBD", Id);
            idInt= Integer.parseInt(Id);
            Tipo = tabla[i][1];
            Mensaje=tabla[i][2];
            Documento= tabla[i][3];
            Fecha=tabla[i][4];
            if(idInt> pref.getInt("IdNotificacion",0)){
                SharedPreferences.Editor editor= pref.edit();
                editor.putInt("IdNotificacion",idInt);
                editor.apply();
                Log.i("IDPAranota", String.valueOf(idInt));
                dbNotificaciones.insertarAviso(Mensaje,Tipo,"1",Id,Documento,Fecha);
            }
        }
        ArrayList<Constructor_Dato> lista =dbNotificaciones.qryAvisos();
        Log.e("Lista", lista.size()+"");
        if (lista.size()==0){
            for (int i=lista.size()-1;i<lista.size();i++){
                Constructor_Dato dato;
                dato= lista.get(i);
                int Tipo= Integer.parseInt(dato.getDato());
                Log.i("Tipo", String.valueOf(Tipo));
                Log.e("inst", i+"");
                if (i>0){
                    Log.e("Proceso", "creaNotificacion()");
                    new creaNotificacion(getApplicationContext(),ObtenerTipo(dato.getDato()),ObtenerTipo(dato.getDato()), Integer.parseInt(dato.getTag2()),dato.getTitulo(),Tipo,dato.getTag3(),1);

                }else {
                    Log.e("Proceso", "creaNotificacion() else");
                    new creaNotificacion(getApplicationContext(),ObtenerTipo(dato.getDato()),ObtenerTipo(dato.getDato()), Integer.parseInt(dato.getTag2()),dato.getTitulo(),Tipo,dato.getTag3());

                }
            }
        }*/

        new creaNotificacion(getApplicationContext(),"Aviso", Mensaje, Mensaje);
        SharedPreferences.Editor editor = pref.edit();
        int id =pref.getInt("IdNotificacion",0);
        try {
            editor.putInt("IdNotificacion",id+1);
        }catch (Exception e){
            e.printStackTrace();
        }
        editor.apply();
    }
    public void onDestroy(){
        try {
            timerTask.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public interface interfazHilo{
        public void ComunicacionFragMensajes();
    }



}
