package com.automatica.AXCMP.Principal;

import android.Manifest;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.provider.Settings;
import androidx.annotation.RequiresApi;

import com.automatica.AXCMP.Coflex.MainActivity_CFLX;
import com.automatica.AXCMP.Maulec.MainActivity_MLC;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;


import com.automatica.AXCMP.MiAXC.MainActivity;
import com.automatica.AXCMP.MiAXC.frgmnt_Registra_Configuracion;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.CreaDialogos;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.preferencias;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.RunnableScheduledFuture;


public class Login extends AppCompatActivity implements frgmnt_Registra_Configuracion.OnFragmentInteractionListener
{

    private EditText et_contraseña, et_usuario;

    private TextView txtv_UsuarioGuardado;

    private Button btnIngresar, btn_cambiarUsuario;
    private TextInputLayout til;

    public Context Contexto = this;

    Context contexto;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String IMEI;
    int ClicksOpciones = 0, ClicksEsperados = 7;
    View vista;
    boolean editar = false;
    Integer PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    Integer PERMISO_CAMARA= 1;

    private   SharedPreferences pref;
    boolean debug = false;
    boolean recarga = false;
    Handler h = new Handler();
    boolean waitingForPermission = true;

    private static final String frgtag_InicioFrag= "FRGIF";

    ArrayList<String> arr_Bienvenidas = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_login);
        contexto = this;
//
//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .replace(R.id.linearLayout, intro_fragment.newInstance(null, null),frgtag_InicioFrag)
//                .commit();

    }

    @Override
    protected void onStart()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(null);
       // getSupportActionBar().setTitle(getString(R.string.toolbar_title));

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        et_contraseña = (EditText) findViewById(R.id.Contraeña);
        et_usuario = (EditText) findViewById(R.id.Usuario);
        txtv_UsuarioGuardado = (TextView) findViewById(R.id.txtv_Bienvenida);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btn_cambiarUsuario = (Button) findViewById(R.id.btn_Cambiarusuario);
        til = (TextInputLayout) findViewById(R.id.til);//TEXT INPUT LAYOUT DEL CAMPO DE USUARIO

        et_usuario.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if (et_usuario.getText().toString().equals(getString(R.string.contraseña_preferencias))&&editar)
                    {
                    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            lanzarPreferencias();
                        }
                    } else {
                        editar = false;
                        //et_usuario.setHint("Usuario:");
                    }


                }
                return false;
            }
        });
        btnIngresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto).;
                if(PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean("DebugMode", false)&&PreferenceManager.getDefaultSharedPreferences(contexto).getString("usuario", "NULL").equals("1937"))
                {
                    lanzarMenu();
                    return;
                }


                if (et_usuario.getText().toString().equals("") || et_contraseña.getText().toString().equals(""))
                {

                    new popUpGenerico(contexto, vista, getString(R.string.error_usuario_contraseña), "false", true, true);

                } else
                    {

                    SegundoPlano tarea = new SegundoPlano();
                    tarea.execute();
                }
                new esconderTeclado(Login.this);
            }
        });
        et_contraseña.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if (et_usuario.getText().toString().equals("") || et_contraseña.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,vista ,getString(R.string.error_usuario_contraseña) ,"false" , true, true);
                       // btnIngresar.setEnabled(true);
                    } else
                        {
                        SegundoPlano tarea = new SegundoPlano();
                        tarea.execute();
                       // btnIngresar.setEnabled(false);
                    }


                }
                return false;
            }
        });

        btn_cambiarUsuario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                et_usuario.setText("");
                txtv_UsuarioGuardado.setVisibility(View.VISIBLE);
                txtv_UsuarioGuardado.setText("Por favor, dame tus credenciales.");
                et_usuario.setVisibility(View.VISIBLE);
                til.setVisibility(View.VISIBLE);
                et_usuario.requestFocus();
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


if(PreferenceManager.getDefaultSharedPreferences(Login.this).getBoolean("MostrarConfiguracionPrimerInicio", false))
    {
        if (!PreferenceManager.getDefaultSharedPreferences(Login.this).getBoolean("ConfiguracionInicial", false))
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.cl_Login, frgmnt_Registra_Configuracion.newInstance(null, ""), frgtag_InicioFrag)
                        .commit();
            }
    }

        super.onStart();
    }

    @Override
    protected void onResume()
    {
       // et_usuario.setHint("Usuario:");
        //et_usuario.setText("");
        et_contraseña.setText("");
        et_usuario.requestFocus();
        editar = false;
        ClicksOpciones = 0;

      pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        String usuarioPrefs = pref.getString("usuarioNombre",null);



        arr_Bienvenidas.add("Bienvenido,");
        arr_Bienvenidas.add("Hola,");
        arr_Bienvenidas.add("Hola de nuevo,");

        Random r = new Random();
        //r.nextInt(3);
        if(usuarioPrefs==null)
            {
                et_usuario.setVisibility(View.VISIBLE);
                til.setVisibility(View.VISIBLE);

                txtv_UsuarioGuardado.setVisibility(View.VISIBLE);
                //txtv_UsuarioGuardado.setText();
                et_usuario.requestFocus();

            }else
                    {
                        et_usuario.setVisibility(View.GONE);
                        til.setVisibility(View.GONE);

                        et_usuario.setText(pref.getString("usuario","SIN_USUARIO_SHP") );
                        txtv_UsuarioGuardado.setVisibility(View.VISIBLE);
                        txtv_UsuarioGuardado.setText(arr_Bienvenidas.get(r.nextInt(3))+" " + usuarioPrefs);
                        et_contraseña.requestFocus();
                    }

        requestPermisos();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.login_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if(!recarga)
            {
                int id = item.getItemId();
                if (id == R.id.configuracion)
                {




                    if (ClicksOpciones == ClicksEsperados)
                    {
                        editarPreferencias();
                        ClicksOpciones = 0;
                        return true;

                    } else if (ClicksOpciones < ClicksEsperados || ClicksOpciones > ClicksEsperados) {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.cl_Login, frgmnt_Registra_Configuracion.newInstance(null, ""), frgtag_InicioFrag)
                                .addToBackStack(frgtag_InicioFrag).commit();
                        ClicksOpciones = 0;
                        editar = false;
                        return true;
                    }



                    return true;
                }

                if((id == R.id.InformacionDispositivo))
                {
                    new sobreDispositivo(contexto,vista);
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

    public void lanzarPreferencias()
    {
        Intent intentLanzarPreferencias = new Intent(this, preferencias.class);
        startActivity(intentLanzarPreferencias);

    }

    private void requestPermisos()
    {
        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, Manifest.permission.READ_PHONE_STATE))
                    {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.


                        new CreaDialogos("MiAXC necesita los permisos solicitados para funcionar.", new DialogInterface.OnClickListener()
                        {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                           //     requestPermisos();


                                if(!shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE))
                                    {
                                        return;
                                    }
                                ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},PERMISO_CAMARA);

                            }
                        }, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), "No se aceptaron los permisos para el funcionamiento de MiAXC, cuando necesites aceptarlos tendras que hacerlo desde la configuración de tu telefono.",false , true,true);

                            }
                        }, Login.this);

                    } else
                    {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},PERMISO_CAMARA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
            } else
            {

            }
    }

    public void lanzarMenu()
    {
        try
            {
                new esconderTeclado(Login.this);

                Intent intent = null;
                String CodigoValidacion = PreferenceManager.getDefaultSharedPreferences(contexto).getString("codigoEmpresa", "SINCODIGO");
                switch (CodigoValidacion)
                    {
                        case "52637825A2":
                            intent = new Intent(Login.this, MainActivity.class);
                            break;

                        case "Maulec2020":
                            intent = new Intent(Login.this, MainActivity_MLC.class);
                            break;

                        case "Coflex2021":
                            intent = new Intent(Login.this, MainActivity_CFLX.class);
                            break;

                        default:
                                new popUpGenerico(contexto, getCurrentFocus(),"El código de empresa que ha ingresado no es correcto, \n favor de contactar al personal de AUTOMATICA TECHNOLOGIES. \n [" + CodigoValidacion  + "]" , false, true, true);
                                return;
                    }
                if(intent!=null)
                    {
                        startActivity(intent);
                    }

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Log.e("SoapResponse", "onRequestPermissionsResult: Jalo el permiso");
        }

        if(!PreferenceManager.getDefaultSharedPreferences(Login.this).getBoolean("ConfiguracionInicial", false))
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.cl_Login, frgmnt_Registra_Configuracion.newInstance(null, ""), frgtag_InicioFrag)
                        .commit();
            }


        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Login.this).edit();

        editor.putBoolean("MostrarConfiguracionPrimerInicio", true);
        editor.apply();

    }

    private void editarPreferencias()
    {
   //     et_usuario.setHint("Clave:");
        editar = true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        cAccesoADatos ca = new cAccesoADatos(contexto);
        DataAccessObject dao = null;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Login.this);
        String usuario = et_usuario.getText().toString();
        String contraseña = et_contraseña.getText().toString();
        View LastView;

        @Override
        protected void onPreExecute()
        {
            recarga = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

            h.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    LastView = getCurrentFocus();
                    progressBarHolder.requestFocus();

                }
            },10);

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            if(!this.isCancelled())
                {
                    try
                        {
                            TelephonyManager mtelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                                {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

                                        {
                                            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
                                        }
                                }
                                {

                                    /**Esto es necesario por que a partir de Android 10 no se puede solicitar el IMEI del telefono*/
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                        {


                                            IMEI = pref.getString("ID_AND10",null);

                                            if(IMEI == null)
                                                {
                                                    IMEI = UUID.randomUUID().toString().replace("-","");
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("ID_AND10", IMEI);
                                                    editor.apply();
                                                }
                                        }
                                    else
                                        {

                                            IMEI = mtelephonyManager.getDeviceId();

                                        }


                                    if(IMEI ==null)
                                        {
                                            IMEI  = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                        }
                                }
                            dao = ca.SOAPLogin(usuario, contraseña, IMEI);


                        } catch (Exception e)
                        {
                         dao = new DataAccessObject(false,e.getMessage(),null);
                            e.printStackTrace();
                        }
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

            try {
            h.postAtFrontOfQueue(new Runnable()
            {
                @Override
                public void run()
                {
                 //   LastView.requestFocus();
                 //   recarga = true;
                }
            });

                if(dao.iscEstado())
                    {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("usuario", usuario);
                        editor.putString("usuarioNombre", dao.getSoapObject().getPrimitivePropertyAsString("Resultado"));

                        editor.apply();
                        et_contraseña.setText("");
                        et_usuario.setText("");
                        lanzarMenu();
                    }
                else
                    {
                        new popUpGenerico(Login.this,vista,dao.getcMensaje(),dao.iscEstado(),true,true);
                        et_contraseña.setText("");
                        if(et_usuario.getVisibility()==View.VISIBLE)
                            {
                                et_usuario.setText("");
                            }
                        et_usuario.requestFocus();
                    }


            }catch (Exception e)
            {
                new popUpGenerico(Login.this,getCurrentFocus(),e.getMessage(),false,true,true);
            }
            recarga = false;
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }
    }


    boolean salir = false;
    @Override
    public void onBackPressed()
    {

        if(getSupportFragmentManager().findFragmentByTag(frgtag_InicioFrag)!=null)
            {
               super.onBackPressed();
            }
        if(!salir)
            {
                Toast.makeText(this, "Para salir de la aplicación, vuelva a tocar atras.", Toast.LENGTH_SHORT).show();
                salir = true;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        salir = false;
                    }
                },3000);

            }
        else
            {
//                finish();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                    {
//                        finishAndRemoveTask();
//                    }else
//                    {
//                        this.finishAffinity();
//                    }


                startActivity(new Intent(Intent.ACTION_MAIN).addCategory( Intent.CATEGORY_HOME ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }




    }
}

