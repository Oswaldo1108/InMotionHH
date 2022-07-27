package com.automatica.AXCMP.c_Carrito_Surtido;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_MenuPrincipal;

import java.util.ArrayList;

public class act_Surt_Carrito_Ordenes extends AppCompatActivity implements frgmnt_Ordenes.OnFragmentInteractionListener, Adaptador_RV_MenuPrincipal.onClickRV,
                                                                    frgmnt_taskbar.OnFragmentInteractionListener,frgmnt_Agregar_Carrito_Orden.OnFragmentInteractionListener,
        frgmnt_Imprime_Etiquetas.OnFragmentInteractionListener
{
    private static final String frgtag_Ordenes_Activas = "FRGOA";
    private static final String frgtag_ActionBar = "FRGAB";
    private static final String frgtag_AgregarContenedor = "FRGCON";
    private static final String frgtag_ImpEtiqueta= "FRGIMP";

    private boolean recarga,reiniciaCampos;
    private Context contexto= this;
    private String str_TipoConsulta;
    private FrameLayout progressBarHolder;
    private boolean Estado ;
    private String DocumentoSeleccionado;
    private String Contenedor;

    private boolean permitirRegresar;
    @Override
    public boolean LastActivity()
    {
       // finish();

        if(!permitirRegresar)

            {
                new CreaDialogos("Ya se inició el surtido, ¿Salir?",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                finish();

                            }
                        },
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        },
                        act_Surt_Carrito_Ordenes.this);
            }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_act__surt_ordenes_activas);

                setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
                getSupportActionBar().setTitle("Cart Moving - Detalle");



                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.actionbar, frgmnt_taskbar.newInstance("Regresar", "Surtido"), frgtag_ActionBar)
                        .commit();

            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    @Override
    protected void onResume()
    {
      new SegundoPlano("ConsultaDocumentos").execute();


        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.layout_area_trabajo, frgmnt_Ordenes.newInstance(null, null), frgtag_Ordenes_Activas)
                .commit();

        super.onResume();
    }

    @Override
    public boolean NextActivity()
    {
        new SegundoPlano("IniciaSurtido").execute();
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
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
            getMenuInflater().inflate(R.menu.surtido_cm_toolbar, menu);
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

                            case R.id.recargar:

                                getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                        .replace(R.id.layout_area_trabajo, frgmnt_Ordenes.newInstance(null, null), frgtag_Ordenes_Activas)
                                        .commit();
                                break;



                            case R.id.ReimprimeEtiqueta:

                                        int CantOrdenes = 0;

                                Fragment fragment = (frgmnt_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas);
                                if (fragment != null)
                                    {
                                        //         Toast.makeText(contexto, "SE ENVIO EL CONTENEDOR AL FRAGMENTO LISTA", Toast.LENGTH_SHORT).show();

                                        CantOrdenes = ((frgmnt_Ordenes)fragment).getCantidadDocs();
                                    }


                                getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                        .add(R.id.layout_area_trabajo, frgmnt_Imprime_Etiquetas.newInstance(new String[]{String.valueOf(CantOrdenes)}, "RegistroContenedor"), frgtag_ImpEtiqueta)
                                        .commit();

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

    @Override
    public boolean ConfirmaAgregarContenedor(String Contenedor)
    {
    //    Toast.makeText(contexto, "HOLA2", Toast.LENGTH_SHORT).show();
        this.Contenedor = Contenedor;

        new SegundoPlano("AgregarCarrito").execute();
        return true;
    }

    @Override
    public void clickBotonMasInfo(String[] datos)
    {

    //    Toast.makeText(contexto, datos[0] +" " + datos[1] +" " +datos[2],Toast.LENGTH_SHORT ).show();
        DocumentoSeleccionado = datos[1];

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.layout_area_trabajo, frgmnt_Agregar_Carrito_Orden.newInstance(datos, "RegistroContenedor"), frgtag_AgregarContenedor)
                .commit();
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
                                    case "AgregarCarrito":

                                        dao = ca.C_RegContenedorOrdenProd(DocumentoSeleccionado,Contenedor);
                                        break;

                                    case "IniciaSurtido":

                                        dao = ca.C_IniciaSurtidoCM();
                                        break;
                                    default:
                                        dao = new DataAccessObject(false,"Operacion no soportada.",null);
                                        break;
                                    case "ConsultaDocumentos":
                                        dao = ca.C_OrdenesPorEstacionCM();
                                        //dao = new DataAccessObject(true,"Operación no soportada",null);

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
                         //   SoapObject so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                            switch (tarea)
                                {
                                    case "AgregarCarrito":

                                        //[spUpdRegContenedorOrdenProd]

                                        Fragment fragment = (frgmnt_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas);
                                        if (fragment != null)
                                            {
                                       //         Toast.makeText(contexto, "SE ENVIO EL CONTENEDOR AL FRAGMENTO LISTA", Toast.LENGTH_SHORT).show();

                                                ((frgmnt_Ordenes)fragment).setCardStatus(Contenedor,0);//La variable card siempre no se uso
                                            }

                                        break;


                                    case "IniciaSurtido":


                                        Intent i = new Intent(act_Surt_Carrito_Ordenes.this, act_Surtir_Ordenes_Seleccionadas.class);
                                        startActivity(i);

                                        break;



                                    case "ConsultaDocumentos":


                                        for(ArrayList<Constructor_Dato> d : dao.getcTablas())
                                            {
                                                for(Constructor_Dato a: d)
                                                    {

                                                        Log.i("ConsultaDocumentos", a.toString());

                                                        if(a.getTitulo().equals("Surtido")&&!a.getDato().equals("0"))
                                                        {
                                                             permitirRegresar = false;
                                                            Log.i("ConsultaDocumentos", a.toString() + "SE ENCONTRO VALOR DIFERENTE A CERO");

                                                        }
                                                    }
                                            }
                                        break;

                                }
                        }else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(),dao.getcMensaje(), false, true, true);

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



}
