package com.automatica.AXCMP.Principal;

import android.Manifest;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.provider.Settings;
import androidx.core.app.ActivityCompat;
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


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.preferencias;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;


public class Login extends AppCompatActivity {

    private EditText et_contraseña, et_usuario;
    private Button btnIngresar;
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
    boolean debug = false;
    boolean recarga = false;
    Handler h = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_login);
        contexto = this;


    }

    @Override
    protected void onStart()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(null);
        getSupportActionBar().setTitle(getString(R.string.toolbar_title));

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        et_contraseña = (EditText) findViewById(R.id.Contraeña);
        et_usuario = (EditText) findViewById(R.id.Usuario);

        btnIngresar = (Button) findViewById(R.id.btnIngresar);

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

                    new popUpGenerico(contexto, vista, getString(R.string.error_usuario_contraseña), "false", true, true);

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
                        new popUpGenerico(contexto,vista ,getString(R.string.error_usuario_contraseña) ,"false" , true, true);
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
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        et_usuario.setHint("Usuario:");
        et_usuario.setText("" );
        et_contraseña.setText("");
        et_usuario.requestFocus();
        editar = false;
        ClicksOpciones = 0;
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

                    } else if (ClicksOpciones < ClicksEsperados || ClicksOpciones > ClicksEsperados) {
                        ClicksOpciones = 0;
                        editar = false;
                        et_usuario.setHint("Usuario:");
                      //  Toast.makeText(Login.this, "No tiene autorización para realizar cambios al sistema.", Toast.LENGTH_SHORT).show();

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

    public void lanzarMenu()
    {

        new esconderTeclado(Login.this);
        Intent intent = new Intent(Login.this, Inicio_Menu_Dinamico.class);
        Bundle b = new Bundle();
        b.putString("Apertura", "11111");
        intent.putExtras(b);
        startActivity(intent);

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
    }

    private void editarPreferencias()
    {
        et_usuario.setHint("Clave:");
        editar = true;
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Login.this);

        SoapAction sa = new SoapAction();
        String mensaje = null, estado = null;
        String usuario = et_usuario.getText().toString();
        String contraseña = et_contraseña.getText().toString();

        String Handheld = pref.getString("estacion", "E102");
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

            try {


                TelephonyManager mtelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(contexto, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            {
                                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
                            }
                    }
                    IMEI = mtelephonyManager.getDeviceId();


                    //
                    //              Si el IMEI regresado es nulo, se enviara el Id de android como validador.
                    //

                    if(IMEI ==null)
                        {
                            IMEI  = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        }
                sa.SOAPLogin(usuario, contraseña, Handheld,IMEI, Contexto);
                mensaje = sa.getMensaje();
                estado = sa.getDecision();


            }catch(Exception e)
            {
                mensaje = e.getMessage();
                estado = "false";
                e.printStackTrace();
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


                if (estado.equals("true"))
                {
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("usuario", usuario);
                    editor.putString("apertura","111111111,11111111111,1111111111111,1111111111,1111111111");
                    editor.apply();
                    et_contraseña.setText("");
                    et_usuario.setText("");
                    lanzarMenu();

                } else if (estado.equals("false"))
                {
                  //  Toast.makeText(Login.this,  mensaje + , Toast.LENGTH_SHORT).show();
                    new popUpGenerico(Login.this,vista,mensaje,estado,true,true);
                    et_contraseña.setText("");
                    et_usuario.setText("");
                    et_usuario.requestFocus();

                }
                btnIngresar.setEnabled(true);
            }catch (Exception e)
            {

                new popUpGenerico(Login.this,vista,e.getMessage(),"false",true,true);
            }
            recarga = false;
        }
    }




}

