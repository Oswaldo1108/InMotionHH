package com.automatica.AXCPT.Servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.SQLite.dbNotificaciones;
import com.automatica.AXCPT.c_Almacen.Almacen_Menu;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.Alm_Registro_Seleccion_Lote;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.RegistroPT_Menu;
import com.automatica.AXCPT.c_Embarques.Embarques_Embarque;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido.Surtido_Seleccion_Partida;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_Menu;
import com.automatica.AXCPT.c_Recepcion.Rec_Registro_Seleccion_Partida;

import java.util.Objects;

public class BroadCastIntent extends BroadcastReceiver
{

    /**
     * Broadcast que activa los intents al dar clic en las notificaciones e iniciar la siguietne actividad
     */
    private static final String TAG ="BroadCastIntent";
    String ValorIntent;
    String IDWS,Documento=null;
    Context context;
    String log="";
    dbNotificaciones dbNotificaciones;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("ValorIntent", "ValorIntent");
        try
        {


                this.context= context;
                final PendingResult pendingResult = goAsync();
                ValorIntent=intent.getAction();

                IDWS=intent.getStringExtra("IDWS");
                try{
                    Documento=intent.getStringExtra("Documento");
                }catch (Exception e){
                    e.printStackTrace();
                }

//                Log.i("Recibido", IDWS);
                Task asyncTask = new Task(pendingResult, intent);
                asyncTask.execute();
                dbNotificaciones = new dbNotificaciones(context);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;

        private Task(PendingResult pendingResult, Intent intent) {
            this.pendingResult = pendingResult;
            this.intent = intent;
        }

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                StringBuilder sb = new StringBuilder();
                sb.append("Action: " + intent.getAction() + "\n");
                sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
                log = sb.toString();
                Log.d(TAG, log);
                Log.i("Lanzado","Lanzado");

            }catch (Exception e){
                e.printStackTrace();
            }
            return log;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("ValorIntent2", ValorIntent);
                switch (ValorIntent){
                    case "1":
                        Intent ToMenu = new Intent(context, Inicio_Menu_Dinamico.class);
                        ToMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        context.startActivity(ToMenu);

                        break;
                    case "2":
                        Intent ToAlmacen = new Intent(context, Almacen_Menu.class);
                        ToAlmacen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        context.startActivity(ToAlmacen);
                        break;
                    case "4":
                        Intent ToSurtido = new Intent(context, Surtido_Seleccion_Partida.class);
                        ToSurtido.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        ToSurtido.putExtra("Documento",Documento);
                        context.startActivity(ToSurtido);
                        break;

                    case "7":
                        Intent ToEmbarques = new Intent(context, Embarques_Embarque.class);
                        ToEmbarques.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        context.startActivity(ToEmbarques);
                        break;
                    case "8":
                        Intent ToInventarios = new Intent(context, Inventarios_Menu.class);
                        ToInventarios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        context.startActivity(ToInventarios);
                        break;
                    case "9":
                        Intent ToEnvios = new Intent(context, Alm_Registro_Seleccion_Lote.class);
                        ToEnvios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        ToEnvios.putExtra("Documento",Documento);
                        Log.i("Documento",Documento);
                        Objects.requireNonNull(context).startActivity(ToEnvios);
                        break;
                    case "10":
                        Intent ToRegistro = new Intent(context, RegistroPT_Menu.class);
                        ToRegistro.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        ToRegistro.putExtra("Documento",Documento);
                        Log.i("Documento",Documento);
                        Objects.requireNonNull(context).startActivity(ToRegistro);
                        break;
                    case "11":
                        Intent ToRecepcion = new Intent(context, Rec_Registro_Seleccion_Partida.class);
                        ToRecepcion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        dbNotificaciones.actualizarAviso(IDWS);
                        ToRecepcion.putExtra("Documento",Documento);
                        Log.i("Documento",Documento);
                        Objects.requireNonNull(context).startActivity(ToRecepcion);
                        break;
                    default:
                        Log.i("Error", String.valueOf(ValorIntent));
                        break;
                }
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingResult.finish();
            }catch (Exception e){
                e.printStackTrace();
            }
            // Must call finish() so the BroadcastReceiver can be recycled.

        }
    }
}
