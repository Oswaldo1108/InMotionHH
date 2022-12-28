package com.automatica.AXCPT.Principal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.SQLite.DaoSQLite;
import com.automatica.AXCPT.Servicios.HiloDescargas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.UUID;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;


public class Login extends AppCompatActivity implements frgmnt_Registra_Configuracion.OnFragmentInteractionListener, HiloDescargas.Interfaz {

    private EditText et_contraseña, et_usuario;
    private Button btnIngresar;
    public Context Contexto = this;
    Context contexto;
    FrameLayout progressBarHolder;
    SharedPreferences pref;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    DaoSQLite sqlHelper;
    SQLiteDatabase database;
    String IMEI, Version, idNotifica;
    int ClicksOpciones = 0, ClicksEsperados = 7;
    View vista;
    boolean editar = false;
    boolean debug = false;
    boolean recarga = false;
    Handler h = new Handler();
    private static final String frgtag_InicioFrag = "FRGIF";
    CreaDialogos creaDialogos = new CreaDialogos(Login.this);
    popUpGenerico pop = new popUpGenerico(Login.this);
    Integer PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    Integer PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 69;
    Integer PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_INICIO = 90929;
    Integer PERMISSIONS_INSTALL_PACKAGES = 40;
    Intent Servicio;
    FirebaseInstallations firebaseInstallations;
    int id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(Login.this);
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_login);
        contexto = this;
        int first =pref.getInt("IdNotificacion",0);
        Log.i("Inicio", String.valueOf(first));
        try{

            sqlHelper=new DaoSQLite(getApplicationContext());
            database =sqlHelper.getWritableDatabase();
            database.close();
            pedirPermisos();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void pedirPermisos() {
        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }
        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_INICIO);
            }
        }
    }

    public String Licenciamiento() {
        TelephonyManager mtelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        {
            /**Esto es necesario por que a partir de Android 10 no se puede solicitar el IMEI del telefono*/
            if (VERSION.SDK_INT >= VERSION_CODES.P) {

                IMEI = pref.getString("ID_AND10", null);

                if (IMEI != null) {
//                    Log.i("SOAP", "ID 10 PREFERENCIAS" + IMEI);
                }


                if (IMEI == null) {
                    IMEI = UUID.randomUUID().toString().replace("-", "");
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("ID_AND10", IMEI);
//                    Log.i("SOAP", "ID_AND10 GENERADO" + IMEI);

                    editor.apply();
                }
                Log.i("SOAP", "ANDROID 10 ID " + IMEI);

            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    //return TODO;
                }
                IMEI = mtelephonyManager.getDeviceId();
//                Log.i("SOAP","DEVICE ID IMEI " + IMEI);

            }
            if (IMEI == null) {
                IMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//                Log.i("SOAP","ANDROID ID IMEI " + IMEI);
            }
        }
//        Log.i("SOAP", "IDENTIFICADOR FINAL " + IMEI);
        return IMEI;
    }

    @Override
    protected void onStart()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(null);
        getSupportActionBar().setTitle(getString(R.string.app_name_titles));

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        et_contraseña = (EditText) findViewById(R.id.pass);
        et_usuario = (EditText) findViewById(R.id.Usuario);

        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        et_usuario.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (et_usuario.getText().toString().equals(getString(R.string.contraseña_preferencias)+"") && editar) {
                        //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            lanzarPreferencias();
                        }
                    } else {
                        editar = false;
                        et_usuario.setHint("Usuario:");
                    }


                }
                return false;
            }
        });
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_usuario.getText().toString().equals("") || et_contraseña.getText().toString().equals("")) {

                    new popUpGenerico(contexto, vista, getString(R.string.error_usuario_contraseña)+"", "false", true, true);

                } else {

                    SegundoPlano tarea = new SegundoPlano();
                    tarea.execute();
                }
                new esconderTeclado(Login.this);
            }
        });
        et_contraseña.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (et_usuario.getText().toString().equals("") || et_contraseña.getText().toString().equals("")) {
                        new popUpGenerico(contexto, vista, getString(R.string.error_usuario_contraseña)+"", "false", true, true);
                        btnIngresar.setEnabled(true);
                    } else {
                        SegundoPlano tarea = new SegundoPlano();
                        tarea.execute();
                        btnIngresar.setEnabled(false);
                    }


                }
                return false;
            }
        });


        ImageView iv = (ImageView) findViewById(R.id.imgv_AXC);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClicksOpciones++;
                // Toast.makeText(Login.this, String.valueOf(ClicksOpciones), Toast.LENGTH_SHORT).show();
            }
        });
        try{
            Licenciamiento();
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onStart();
    }
    private boolean servicioActivo(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    protected void onResume() {
        et_usuario.setHint("Usuario:");
        et_usuario.setText("");
        et_contraseña.setText("");
        et_usuario.requestFocus();
        editar = false;
        ClicksOpciones = 0;
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        /*try{
            String[] valores={IMEI};
            if (PreferenceManager.getDefaultSharedPreferences(Login.this).getBoolean("MostrarConfiguracionPrimerInicio", false)) {
                if (!PreferenceManager.getDefaultSharedPreferences(Login.this).getBoolean("ConfiguracionInicial", false)) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.linearLayout, frgmnt_Registra_Configuracion.newInstance(valores, ""), frgtag_InicioFrag)
                            .commit();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!recarga) {
            int id = item.getItemId();
            if (id == R.id.configuracion) {

                if (ClicksOpciones == ClicksEsperados) {
                    editarPreferencias();
                    ClicksOpciones = 0;

                } else if (ClicksOpciones < ClicksEsperados || ClicksOpciones > ClicksEsperados) {
                    /*getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.linearLayout, frgmnt_Registra_Configuracion.newInstance(null, ""), frgtag_InicioFrag)
                            .addToBackStack(frgtag_InicioFrag).commit();*/
                    ClicksOpciones = 0;
                    editar = false;
                    return true;
                }
                return true;
            }

            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }
            if((id == R.id.Actualizar)){
                creaDialogos.dialogoConIcono(getString(R.string.actualizar), getString(R.string.pregunta_actualizar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SegundoPlanover sp = new SegundoPlanover();
                        sp.execute();
                    }
                },null,R.mipmap.logo_x_150x150_);
            }

               /* if (id == R.id.InicioRapido)
                    {

                        if (debug)
                            {
                                Intent intent = new Intent(Login.this, Inicio_Menu_Dinamico.class);
                                startActivity(intent);
                            } else
                            {


                                et_usuario.setText("1937");
                                et_contraseña.setText("19377");

                                SegundoPlano sp = new SegundoPlano();
                                sp.execute();

                            }

                      }*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void lanzarPreferencias() {
        String valores[] = {IMEI};
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.linearLayout, frgmnt_Registra_Configuracion.newInstance(valores, ""), frgtag_InicioFrag)
                .addToBackStack(frgtag_InicioFrag).commit();
        et_usuario.setText("");
        et_usuario.setHint(getString(R.string.txtUsuario));
        new esconderTeclado(Login.this);
        editar = false;

    }

    public void lanzarMenu() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Login.this).edit();
        editor.putBoolean("MostrarSeleccionArea", true);
        new esconderTeclado(Login.this);
        Intent intent = new Intent(Login.this, Inicio_Menu_Dinamico.class);
        Bundle b = new Bundle();
        b.putString("Apertura", "11111");
        b.putString("Version", "11111");
        b.putBoolean("Area",true);
        intent.putExtras(b);
        startActivity(intent);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.e("SoapResponse", "onRequestPermissionsResult: Jalo el permiso");
            Licenciamiento();
         final   String[] valores ={IMEI};
            if (!PreferenceManager.getDefaultSharedPreferences(Login.this).getBoolean("ConfiguracionInicial", false)) {

                Handler h = new Handler();
                h.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.linearLayout, frgmnt_Registra_Configuracion.newInstance(valores, ""), frgtag_InicioFrag)
                                .commit();
                    }
                });

            }


            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Login.this).edit();

            editor.putBoolean("MostrarConfiguracionPrimerInicio", true);
            editor.apply();
        }
        if (requestCode== PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Servicio = new Intent(getApplicationContext(), HiloDescargas.class);
            startService(Servicio);
        }
        try {
            if (requestCode== PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_INICIO && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!getPackageManager().canRequestPackageInstalls()) {
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), 1234);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void editarPreferencias() {
        et_usuario.setHint(getString(R.string.txt_clave));
        editar = true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void matarAplicacion() {
        finishAndRemoveTask();
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        DataAccessObject dao;
        cAccesoADatos ca;
        String usuario = et_usuario.getText().toString();
        String contraseña = et_contraseña.getText().toString();

        String Handheld = pref.getString("estacion", "E102");
        View LastView;

        @Override
        protected void onPreExecute() {

            recarga = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LastView = getCurrentFocus();
                    progressBarHolder.requestFocus();

                }
            }, 10);


        }


        @SuppressLint("ObsoleteSdkInt")
        @Override
        protected Void doInBackground(Void... params) {
            if (!this.isCancelled()) {
                try {
                    ca = new cAccesoADatos(Login.this);
                    dao = ca.SOAPLogin(usuario, contraseña, IMEI);
                    if (pref.getBoolean("booleanNotificaciones",false)){
                        Log.e("BooleanNotifica", pref.getBoolean("booleanNotificaciones",false)+"" );
                    }


                } catch (Exception e) {
                    dao = new DataAccessObject(false, e.getMessage(), null);
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

            try {
                h.postAtFrontOfQueue(new Runnable() {
                    @Override
                    public void run() {
                        //   LastView.requestFocus();
                        //   recarga = true;
                    }
                });


                if (dao.iscEstado())
                {
                    pref.getString("Vversion","1.0.0.0");
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("usuario", usuario);
                    editor.putString("apertura", "111111111,11111111111,1111111111111,1111111111,1111111111");

                    et_contraseña.setText("");
                    et_usuario.setText("");
                    Version = dao.getSoapObject_parced().getPrimitivePropertyAsString("Vversion");
                    idNotifica = dao.getSoapObject_parced().getPrimitivePropertyAsString("idNotifica");
                    id = Integer.parseInt(idNotifica)+1;
                    try {
                        editor.putInt("IdNotificacion",id);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.e("Notifica", id+"");
                    editor.putString("Vversion",Version);
                    editor.apply();
                    if(!Version.equals(getString(R.string.app_version))){
                        new esconderTeclado(Login.this);
                        creaDialogos.dialogoConIcono(getString(R.string.nueva_version), getString(R.string.pregunta_nueva_version), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!servicioActivo(HiloDescargas.class)){
                                    if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                                    {

                                        if(VERSION.SDK_INT >= VERSION_CODES.M)
                                        {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                                        }
                                    }
                                    if (ActivityCompat.checkSelfPermission(contexto,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                        Servicio = new Intent(getApplicationContext(), HiloDescargas.class);
                                        startService(Servicio);
                                    }
                                }else {
                                    pop.popUpGenericoDefault(getCurrentFocus(),getString(R.string.descarga_encurso),false);
                                }
                            }
                        },null,R.mipmap.logo_x_150x150_);


                    }else{
                        lanzarMenu();
                    }
//
//                    String version = "";
//
//                    if(!dao.getSoapObject_parced().getPrimitivePropertyAsString("Version").equals(version))
//                        {
//                            if(dao.getSoapObject_parced().getPrimitivePropertyAsString("Obligatoria").equals("1"))
//                                {
//                                    new popUpGenerico(Login.this, vista,"No se puede iniciar la aplicacion hasta que se actualice.", dao.iscEstado(), true, true);
//                                }
//                            else
//                                {
//                                    lanzarMenu();
//                                }
//                        }

                } else
                    {
                    //  Toast.makeText(Login.this,  mensaje + , Toast.LENGTH_SHORT).show();
                    new popUpGenerico(Login.this, vista, dao.getcMensaje(), dao.iscEstado(), true, true);
                    et_contraseña.setText("");
                    et_usuario.setText("");
                    et_usuario.requestFocus();

                }
                btnIngresar.setEnabled(true);
            } catch (Exception e) {

                new popUpGenerico(Login.this, vista, e.getMessage(), "false", true, true);
            }
            recarga = false;
        }
    }


    boolean salir = false;

    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentByTag(frgtag_InicioFrag) != null) {
            super.onBackPressed();
        }
        if (!salir) {
            Toast.makeText(this, getString(R.string.salir_aplicacion), Toast.LENGTH_SHORT).show();
            salir = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    salir = false;
                }
            }, 3000);

        } else {
//                finish();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    {
//                        finishAndRemoveTask();
//                    }else
//                    {
//                        this.finishAffinity();
//                    }


            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }


    }

    private class SegundoPlanover extends AsyncTask<Void,Void,Void>{

        DataAccessObject daon;
        cAccesoADatos can;
        String verapp = getString(R.string.app_version)+"";


        protected void onPreExecute()
        {
            recarga = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);


        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {

                Log.e("TOKEN", firebaseInstallations.getId().toString());
                can = new cAccesoADatos(contexto);
                daon = can.cConsultaversion(verapp);
            }catch (Exception e){
                e.printStackTrace();
                new popUpGenerico(Login.this,vista,e.getMessage(),false,true,true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);

                if (daon.iscEstado()) {
                    Version = daon.getSoapObject_parced().getPrimitivePropertyAsString("Vversion");

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Vversion",Version);
                    editor.apply();

                    if (!servicioActivo(HiloDescargas.class)) {
                        if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        }
                        if (ActivityCompat.checkSelfPermission(contexto, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            Servicio = new Intent(getApplicationContext(), HiloDescargas.class);
                            startService(Servicio);
                        }
                    } else {
                        pop.popUpGenericoDefault(getCurrentFocus(), getString(R.string.descarga_encurso), false);
                    }
                } else {
                    new popUpGenerico(Login.this, vista, daon.getcMensaje(), daon.iscEstado(), true, true);
                }
            }catch (Exception e){
                e.printStackTrace();
                new popUpGenerico(Login.this,vista,e.getMessage(),false,true,true);
            }
            recarga = false;
        }
    }
}
