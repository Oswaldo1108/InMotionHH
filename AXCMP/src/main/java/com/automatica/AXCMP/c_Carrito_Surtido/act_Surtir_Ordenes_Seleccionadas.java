package com.automatica.AXCMP.c_Carrito_Surtido;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_PedidosActivos;

import org.ksoap2.serialization.SoapObject;

public class act_Surtir_Ordenes_Seleccionadas extends AppCompatActivity implements frgmnt_Surtiendo_Ordenes.OnFragmentInteractionListener, Adaptador_RV_PedidosActivos.onClickRV,
                                                                    frgmnt_taskbar.OnFragmentInteractionListener,frgmnt_Agregar_Carrito_Orden.OnFragmentInteractionListener,
                                                                    frgmnt_Imprime_Etiquetas.OnFragmentInteractionListener,frgmnt_Cierre_Contenedor.OnFragmentInteractionListener
{

    private static final String frgtag_Ordenes_Activas = "FRGOA";
    private static final String frgtag_ActionBar = "FRGAB";
    private static final String frgtag_ImpEtiqueta= "FRGIMP";
    private static final String frgtag_CierrePallet= "FRGCP";

    private boolean recarga,reiniciaCampos;
    private Context contexto= this;
    private String str_TipoConsulta;
    private FrameLayout progressBarHolder;
    private boolean Estado ;
    private String Posicion;
    private String PiezasFaltantes;
    private EditText edtx_Empaque,edtx_Contenedor;
    Handler h = new Handler();

    private String Pallet;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_act__surtir_ordenes_seleccionadas);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                //getSupportActionBar().setTitle(getString(R.string.Surtido_CartMoving));
                setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
                getSupportActionBar().setTitle("Cart Moving");


                edtx_Contenedor = (EditText)findViewById(R.id.edtx_Contenedor);
                edtx_Empaque = (EditText)findViewById(R.id.edtx_Empaque);

                edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


                edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                            {
                                if(edtx_Empaque.getText().toString().equals(""))
                                    {
                                        new popUpGenerico(act_Surtir_Ordenes_Seleccionadas.this, getCurrentFocus(), getString(R.string.error_ingrese_empaque), false, true, true);
                                        edtx_Empaque.requestFocus();
                                        return false;
                                    }
//
//                                h.postDelayed(new Runnable()
//                                {
//                                    @Override
//                                    public void run()
//                                    {
//                                        edtx_Contenedor.requestFocus();
//                                    }
//                                }, 100);
                            }
                        return false;
                    }
                });


                edtx_Contenedor.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))

                            {
                                if(edtx_Contenedor.getText().toString().equals(""))
                                    {
                                        new popUpGenerico(act_Surtir_Ordenes_Seleccionadas.this, getCurrentFocus(), getString(R.string.error_ingrese_empaque), false, true, true);
                                        return false;
                                    }


                                new SegundoPlano("RegistrarEmpaque").execute();
                            }
                            return false;
                    }
                });




                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.actionbar, frgmnt_taskbar.newInstance("Regresar", "Terminar"), frgtag_ActionBar)
                        .commit();


                new esconderTeclado(act_Surtir_Ordenes_Seleccionadas.this);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }


    @Override
    protected void onResume()
    {

        new SegundoPlano("ConsultarOrdenes").execute();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.layout_area_trabajo, frgmnt_Surtiendo_Ordenes.newInstance(null, null), frgtag_Ordenes_Activas)
                .commit();
        super.onResume();
    }

    @Override
    public boolean NextActivity()
    {
        return false;
    }


    @Override
    public boolean LastActivity()
    {

        new CreaDialogos("Se regresará al detalle del surtido, ¿Continuar?",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                        //spQryIniciaSurtidoCM Estacion Usuario
                    }
                },
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                },
                act_Surtir_Ordenes_Seleccionadas.this);
        return false;
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI()
    {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI()
    {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.cerrar_oc_toolbar, menu);
            return true;
        }
        catch (Exception ex)
            {
                Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
            }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            int id = item.getItemId();

            if (!recarga)
            {
                switch (id)
                    {
                        case R.id.InformacionDispositivo:
                            new sobreDispositivo(contexto, null);

                            break;

                        case R.id.borrar_datos:

                            break;

                        case R.id.CancelarPosicion:
                            new CreaDialogos("Marcar posición como vacia: [" + Posicion + "]",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            new SegundoPlano("RegistrarPosicionVacia").execute();
                                        }
                                    },
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                        }
                                    },
                                    act_Surtir_Ordenes_Seleccionadas.this);
                            break;


                        case R.id.ReimprimeEtiqueta:

                            int   CantOrdenes = 0;
                            Fragment fragment = (frgmnt_Surtiendo_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas);
                            if (fragment != null)
                                {
                                    //         Toast.makeText(contexto, "SE ENVIO EL CONTENEDOR AL FRAGMENTO LISTA", Toast.LENGTH_SHORT).show();

                                 CantOrdenes = ((frgmnt_Surtiendo_Ordenes)fragment).getCantidadDocs();
                                }



                            getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.cl_SurtidoCM, frgmnt_Imprime_Etiquetas.newInstance(new String[]{String.valueOf(CantOrdenes)}, "RegistroContenedor"), frgtag_ImpEtiqueta)
                            .commit();

                            break;


                        case R.id.CierraPallet:
                            new CreaDialogos("¿Cerrar contenedor" + Pallet + "?",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            new SegundoPlano("RegistrarPosicionVacia").execute();
                                        }
                                    },
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                        }
                                    },
                                    act_Surtir_Ordenes_Seleccionadas.this);
                            break;


                    }
            }



        }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

            }
        return super.onOptionsItemSelected(item);
    }



//    @Override
//    public boolean ConfirmaAgregarContenedor(String Contenedor)
//    {
//        Toast.makeText(contexto, "HOLA2", Toast.LENGTH_SHORT).show();
//
//        Fragment fragment = (frgmnt_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas);
//        if (fragment != null)
//            {
//                Toast.makeText(contexto, "SE ENVIO EL CONTENEDOR AL FRAGMENTO LISTA", Toast.LENGTH_SHORT).show();
//
//                ((frgmnt_Ordenes)fragment).setCardStatus(Contenedor, 1);
//            }
//        return true;
//    }



    @Override
    public void clickBotonMasInfo(final String[] datos)
    {
      //  Toast.makeText(contexto, datos[0] +" " + datos[1] +" ",Toast.LENGTH_SHORT ).show();
//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .add(R.id.layout_area_trabajo, frgmnt_Agregar_Carrito_Orden.newInstance(datos, "RegistroContenedor"), frgtag_AgregarContenedor)
//                .commit();
//
//        frgmnt_Agregar_Carrito_Orden f= (frgmnt_Agregar_Carrito_Orden) getSupportFragmentManager().findFragmentByTag(frgtag_AgregarContenedor);
//
//       if(f!=null)
//           {
//               f.hola();
//           }


        new CreaDialogos("¿Cerrar contenedor " + datos[1] + "?",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.cl_SurtidoCM, frgmnt_Cierre_Contenedor.newInstance(datos, ""), frgtag_CierrePallet)
                                .commit();

//                        new SegundoPlano("RegistrarPosicionVacia").execute();
//                        Pallet = datos[1];
                    }
                },
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                },
                act_Surtir_Ordenes_Seleccionadas.this);


    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(contexto);
        DataAccessObject dao;
        String tarea;
        View view;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try
                {
            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    if(!this.isCancelled())
                        {


                            switch (tarea)
                                {
                                    case "RegistrarEmpaque":
                                        dao = ca.C_SurtirEmpaque(edtx_Contenedor.getText().toString(),edtx_Empaque.getText().toString());
                                        Log.i("SoapResponse", "RegistrarEmpaque On Post");

                                        break;

                                    case "RegistrarPzas":
                                        dao = ca.C_SurtirPzas(edtx_Contenedor.getText().toString(),edtx_Empaque.getText().toString(),PiezasFaltantes);
                                        break;

                                    case "ConsultarOrdenes":
                                        dao = ca.C_PedidosActivos();
                                        break;


                                    case "RegistrarPosicionVacia":
                                        dao = ca.C_RegistrarPosicionVacia(Posicion);

                                        break;

                                    case "CierreContenedor":
                                        dao = ca.C_CierraPalletSurtido(Pallet);

                                        break;
                                    default:
                                        dao = new DataAccessObject(false,"Operacion no soportada.",null);
                                        break;
                                }

                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    dao = new DataAccessObject(false,e.getMessage(),null);
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
                {
                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {
                                    case "ConsultarOrdenes":

                                        SoapObject so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);

                                        Posicion = so_ResultSet.getPrimitivePropertyAsString("Posicion");

                                        ((TextView)findViewById(R.id.txtv_Ubicacion)).setText(Posicion);
                                        ((TextView)findViewById(R.id.txtv_Articulos)).setText(so_ResultSet.getPrimitivePropertyAsString("NumParte") + " - " + so_ResultSet.getPrimitivePropertyAsString("Desc"));
                                        ((TextView)findViewById(R.id.txtv_Pedidos)).setText(String.valueOf(dao.getSoapObject().getPropertyCount()-1));

                                        ((TextView)findViewById(R.id.txtv_Existencia)).setText(so_ResultSet.getPrimitivePropertyAsString("Cantidad"));
                                        ((TextView)findViewById(R.id.txtv_Empaques)).setText(so_ResultSet.getPrimitivePropertyAsString("Empaques"));
                                        new esconderTeclado(act_Surtir_Ordenes_Seleccionadas.this);
                                        break;


                                    case "RegistrarEmpaque":
                                    //    new popUpGenerico(contexto, getCurrentFocus(),"Registro correcto.", dao.iscEstado(), true, true);
                                        new SegundoPlano("ConsultarOrdenes").execute();

                                        getSupportFragmentManager().beginTransaction()
                                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                                .replace(R.id.layout_area_trabajo, frgmnt_Surtiendo_Ordenes.newInstance(null, null), frgtag_Ordenes_Activas)
                                                .commit();

                                      //  ((frgmnt_Surtiendo_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas)).RecargarLista();

                                        Log.i("SoapResponse", "RegistrarEmpaque On Post");

                                        edtx_Contenedor.setText("");
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                        new esconderTeclado(act_Surtir_Ordenes_Seleccionadas.this);
                                        break;


                                    case "RegistrarPzas":
                                   //     new popUpGenerico(contexto, getCurrentFocus(),"Registro correcto.", dao.iscEstado(), true, true);
                                        new SegundoPlano("ConsultarOrdenes").execute();

//                                        ((frgmnt_Surtiendo_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas)).RecargarLista();
                                        getSupportFragmentManager().beginTransaction()
                                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                                .replace(R.id.layout_area_trabajo, frgmnt_Surtiendo_Ordenes.newInstance(null, null), frgtag_Ordenes_Activas)
                                                .commit();

                                        edtx_Contenedor.setText("");
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                        new esconderTeclado(act_Surtir_Ordenes_Seleccionadas.this);

                                        break;


                                    case "RegistrarPosicionVacia":

                                      //  ((frgmnt_Surtiendo_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas)).RecargarLista();
                                        getSupportFragmentManager().beginTransaction()
                                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                                .replace(R.id.layout_area_trabajo, frgmnt_Surtiendo_Ordenes.newInstance(null, null), frgtag_Ordenes_Activas)
                                                .commit();

                                        new SegundoPlano("ConsultarOrdenes").execute();
                                        new esconderTeclado(act_Surtir_Ordenes_Seleccionadas.this);

                                        break;

                                }
                        }else
                        {
                         //   new popUpGenerico(contexto, getCurrentFocus(),dao.getcMensaje(), false, true, true);

                            switch (tarea)
                                {
                                    case "ConsultarOrdenes":
                                        new popUpGenerico(contexto, getCurrentFocus(),dao.getcMensaje(), false, true, true);
                                        break;
                                    case "RegistrarEmpaque":

                                        if(dao.getcMensaje().contains("DIF "))
                                            {
                                              PiezasFaltantes = dao.getcMensaje().replace("DIF ", "");
                                                new CreaDialogos("Tomar " + PiezasFaltantes+ " piezas.","Listo","Cancelar",
                                                        new DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {
                                                                    new SegundoPlano("RegistrarPzas").execute();
                                                            }
                                                        },
                                                        new DialogInterface.OnClickListener()
                                                        {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {

                                                            }
                                                        }, act_Surtir_Ordenes_Seleccionadas.this);


                                            }


                                        break;
                                    case "RegistrarPzas":
                                        new popUpGenerico(contexto, getCurrentFocus(),dao.getcMensaje(), dao.iscEstado(), true, true);
                                        break;

                                }
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

                }

            //swipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected void onCancelled()
        {
            new popUpGenerico(contexto, getCurrentFocus(), "Cancelado", false, true, true);
            super.onCancelled();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return false;
    }

    @Override
    public boolean ActivaProgressBar(boolean estado)
    {
        try
            {
                AlphaAnimation inAnimation;
                if (progressBarHolder == null)
                    {
                        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
                    }
                AlphaAnimation outAnimation;
                if (Estado)
                    {
                        //ENTRADA
                        inAnimation = new AlphaAnimation(0f, 1f);
                        inAnimation.setDuration(200);
                        progressBarHolder.setAnimation(inAnimation);
                        progressBarHolder.setVisibility(View.VISIBLE);
                        progressBarHolder.requestFocus();
                    } else
                    {
                        //SALIDA
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        progressBarHolder.setAnimation(outAnimation);
                        progressBarHolder.setVisibility(View.GONE);
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        return true;
    }

    @Override
    public boolean ConfirmaAgregarContenedor(String Contenedor)
    {

        return false;
    }


}
