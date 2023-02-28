package com.automatica.AXCPT.Principal;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragNotificaciones;
import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.Fragmento_Seleccion_Area;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Prueba.Settings_app;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.SQLite.dbNotificaciones;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.HiloNotificaciones;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.creaNotificacion;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.PrincipalActivityMenuBinding;

import java.util.ArrayList;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;


/*
* Para agregar opciones nuevas al menu dinamico, se debe agregar una entrada al arrayList de Intents llamado "ActionsList"
* Y una su entrada a el Array list de constructor Tabla menu principal
* Se debe respetar el orden en que se agrega, procurar agregarlo al final para no revolver las entradas
*
* */


public class Inicio_Menu_Dinamico extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar,Fragmento_Seleccion_Area.InterfazSeleccionArea
{
    String TAG = "InicioMenu";
    View vista;
    Menu menuGET;
    Context contexto = this;
    Intent Servicio;
    dbNotificaciones dbNotificaciones;
    BroadCastBADGE badge = new BroadCastBADGE();
    PrincipalActivityMenuBinding binding;
    private ActivityHelpers activityHelpers;
    private ProgressBarHelper progressBarHelper;
    static final String ACTION_BADGE = "com.example.axc.notification.BADGE";
    Fragmento_Seleccion_Area seleccion_area;
    Bundle b;
    boolean seleccionArea;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            binding = PrincipalActivityMenuBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Inicio_Menu_Dinamico.this);
            int id= pref.getInt("IdNotificacion",0);
            dbNotificaciones = new dbNotificaciones(getApplicationContext());
            if (id==0){
                SharedPreferences.Editor editor= pref.edit();
                editor.putInt("IdNotificacion",0);
                editor.apply();
            }
            if (!servicioActivo(HiloNotificaciones.class)){
                Servicio = new Intent(getApplicationContext(), HiloNotificaciones.class);
                /**
                 * aqui se inicializa el servicio si es que no esta activo
                 */
                if (pref.getBoolean("booleanNotificaciones",false)){
                    startService(Servicio);

                }else {
                    stopService(Servicio);
                }
                //
                Log.i("Estado Servicio", String.valueOf(servicioActivo(HiloNotificaciones.class)));
            }
            progressBarHelper = new ProgressBarHelper(this);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.menu_principal));
            activityHelpers = new ActivityHelpers();
            activityHelpers.AgregarMenus(Inicio_Menu_Dinamico.this,R.id.Pantalla_principal,false);
            binding.tvArea.setText(String.valueOf(pref.getString("area","")));


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }
    }

    @Override
    public void BotonDerecha() {
        Toast.makeText(contexto, "Prueba", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    try{
            b = getIntent().getExtras();
            seleccionArea = b.getBoolean("Area");
            /*if (seleccionArea){
                seleccion_area=Fragmento_Seleccion_Area.newInstance("","");
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left,android.R.anim.slide_in_left,R.anim.slide_out_left)
                        .replace(R.id.FrameLayout, seleccion_area,"FragmentoCambioArea").addToBackStack("").commit();
                seleccionArea = false;
            }*/
        }
    catch (Exception e){
        e.printStackTrace();
        }

      activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        if (estado){
            progressBarHelper.ActivarProgressBar();
        }else {
            progressBarHelper.DesactivarProgressBar();
        }
        return false;
    }

    /**
     * BroadcasReciber, al recibir la action BADGE realiza la actualización de la medalla de mensajeria
     */
    public class BroadCastBADGE extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            //Toast.makeText(context, log, Toast.LENGTH_LONG).show();
            if (intent.getAction().equals(ACTION_BADGE)){
                try{
                    int textoAnterior = 0;
                    if (menuGET != null) {
                        RelativeLayout medalla = (RelativeLayout) menuGET.findItem(R.id.Mensajeria).getActionView();
                        TextView tv = medalla.findViewById(R.id.texto_medalla);
                        int Cantidad = dbNotificaciones.qryAvisos().size();
                        if (Cantidad > 0) {
                            if (tv != null) {
                                textoAnterior = Integer.parseInt(tv.getText().toString());
                                tv.setText(String.valueOf(Cantidad));
                                tv.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (tv != null) {
                                textoAnterior = Integer.parseInt(tv.getText().toString());
                                tv.setText(String.valueOf(Cantidad));
                                tv.setVisibility(View.GONE);
                            }
                        }
                    }
                    if (getSupportFragmentManager().findFragmentByTag("FragmentoNoti") != null) {
                        int Cantidad = dbNotificaciones.qryAvisos().size();
                        FragNotificaciones notificaciones = (FragNotificaciones) getSupportFragmentManager().findFragmentByTag("FragmentoNoti");
                        if (notificaciones != null) {
                            notificaciones.MandarMensaje(Cantidad);
                            if (textoAnterior != Cantidad) {
                                notificaciones.resetearRecycler();
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        new creaNotificacion(contexto,"Contacte a AXC","La información ingresada ha generado una excepción",e.getMessage());
                    }
                }
            }
        }
    }


    /**
     * Se revisa cuales servicios estan activos en el momento de activar el metodo, se usa para validar que el
     * servicio de notificaciones se encuentre activo y si no, lanzarlo
     * @param serviceClass
     * @return
     */
    private boolean servicioActivo(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Servicio","Activo");
                return true;
            }
        }
        Log.i("Servicio","inactivo");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_BADGE);
        registerReceiver(badge,filter);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Inicio_Menu_Dinamico.this);
        binding.tvArea.setText(String.valueOf(pref.getString("area","")));

    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") != null) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (!activityHelpers.getTaskbar_axc().toggle()) {
            return;
        } else {
            activityHelpers.getTaskbar_axc().toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("FragmentoNoti") != null) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta") != null) {

            activityHelpers.getTaskbar_axc().cerrarFragmento();
            //getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_black_24dp)

                    .setTitle("¿Desea cerrar la sesión?").setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
                            SharedPreferences.Editor edit = pref.edit();

                            edit.putString("usuario", "--");
                            edit.apply();

                            // String usuarioPref = pref.getString("usuario", "null");
                            Inicio_Menu_Dinamico.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.menu_principal_toolbar_notificaciones, menu);

            menuGET= menu;
            final MenuItem item = menuGET.findItem(R.id.Mensajeria);
            ImageView iv=item.getActionView().findViewById(R.id.medalla_imagen);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuGET.performIdentifierAction(item.getItemId(),0);
                }
            });
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();
            if((id == R.id.AppSettings))
            {
                Intent intent = new Intent(contexto, Settings_app.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            if((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto,vista);
            }
        /*if((id == R.id.Mensajeria))
        {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left,android.R.anim.slide_in_left,R.anim.slide_out_left)
                    .replace(R.id.Pantalla_principal,FragNotificaciones.newInstance(),"FragmentoNoti").addToBackStack("").commit();
            return false;
        }*/

            if (id == R.id.CambioAreaDeTrabajo){
//-----------------------------------------------Cambio de area-------------------------------------------------\\
                if (getSupportFragmentManager().findFragmentByTag("FragmentoCambioArea")==null){
                    seleccion_area=Fragmento_Seleccion_Area.newInstance("","");
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left,android.R.anim.slide_in_left,R.anim.slide_out_left)
                            .replace(R.id.FrameLayout, seleccion_area,"FragmentoCambioArea").addToBackStack("").commit();
                }else {
                    getSupportFragmentManager().popBackStack();
                }

                return false;
            }
        }catch  (Exception ex){
            Toast.makeText( this, "Error en Toolbar", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {

        super.onPostResume();

    }

    @Override
    protected void onPause() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Inicio_Menu_Dinamico.this);
        binding.tvArea.setText(String.valueOf(pref.getString("area","")));
        super.onPause();
        unregisterReceiver(badge);
    }


}
