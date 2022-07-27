package com.automatica.AXCMP.MiAXC.ConsultaPosiciones;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCMP.ImpresionEtiquetas.frgmnt_Imprime_Etiquetas;
import com.automatica.AXCMP.MiAXC.frgmnt_Consulta_Documento_Pallets;
import com.automatica.AXCMP.MiAXC.frgmnt_Consulta_Pallet_Det;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;

import com.automatica.axc_lib.views.Adaptador_RV_Posiciones;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class act_Mapa_Almacen extends AppCompatActivity implements frgmnt_Ordenes.OnFragmentInteractionListener,  com.automatica.axc_lib.views.Adaptador_RV_MenuPrincipal.onClickRV,com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal.onClickRV,
                                                                frgmnt_Imprime_Etiquetas.OnFragmentInteractionListener, frgmnt_Consulta_Documento_Pallets.OnFragmentInteractionListener,frgmnt_Consulta_Pallet_Det.OnFragmentInteractionListener,
        Adaptador_RV_Posiciones.onClickRV
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

    private Spinner sp_Almacen,sp_Rack;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_act__surt_ordenes_activas);

                setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
                getSupportActionBar().setTitle("MiAXC");
                getSupportActionBar().setSubtitle("Mapa del almacén");

                DeclararVariables();
                new SegundoPlano("ListaAlmacenes").execute();

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(act_Mapa_Almacen.this,null,e.getMessage(),false,true,true);
            }
    }



    private void DeclararVariables()
    {
        sp_Almacen = findViewById(R.id.vw_Spinner_almacen).findViewById(R.id.spinner);
        sp_Rack = findViewById(R.id.vw_Spinner_rack).findViewById(R.id.spinner);


        sp_Almacen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                new SegundoPlano("ListaRacksAlmacen").execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sp_Rack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
//                new SegundoPlano("ListaRacksAlmacen").execute();

                MostrarRack(sp_Rack.getSelectedItem().toString(),"@");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
    @Override
    protected void onResume()
    {
     // new SegundoPlano("ConsultaDocumentos").execute();
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.layout_area_trabajo, frgmnt_Ordenes.newInstance(null, null), frgtag_Ordenes_Activas)
                .commit();



        super.onResume();
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
            getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);
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
    public void clickBotonMasInfo(String[] datos)
    {

//        Toast.makeText(contexto, datos[0] +" " + datos[1] +" " +datos[2],Toast.LENGTH_SHORT ).show();
        DocumentoSeleccionado = datos[1];
//
        if(datos[0].equals("ConsultaRacks"))
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.layout_area_trabajo, frgmnt_Consulta_Documento_Pallets.newInstance(datos[1], "PRUEBA"), frgtag_AgregarContenedor)
                        .addToBackStack(frgtag_AgregarContenedor)
                        .commit();
            }else if(datos[0].equals("ConsultaPallets"))
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .add(R.id.layout_area_trabajo, frgmnt_Consulta_Pallet_Det.newInstance(new String[]{"PT",datos[2]}, "ConsultaPallets"), frgtag_AgregarContenedor)
                        .addToBackStack(frgtag_AgregarContenedor)
                        .commit();
            }
    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
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
        protected Void doInBackground(String... params)
        {
            try
                {
                    if(!this.isCancelled())
                        {


                            switch (tarea)
                                {
                                    case "ListaAlmacenes":

                                        dao = ca.C_ListaAlmacenesMiAXC();
                                        break;

                                    case "ListaRacksAlmacen":

                                        dao = ca.C_ListaRacksAlmacen(((CustomArrayAdapter)sp_Almacen.getAdapter()).getSelectedExtra(sp_Almacen.getSelectedItemPosition()));
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
                                    case "ListaAlmacenes":

                                        //[spUpdRegContenedorOrdenProd]
//
//

                                        sp_Almacen.setAdapter( new CustomArrayAdapter(act_Mapa_Almacen.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("DescAlmacen","IdAlmacen")));


//                                        new SegundoPlano("ListaRacksAlmacen").execute();
                                        break;


                                    case "ListaRacksAlmacen":

                                        sp_Rack.setAdapter( new CustomArrayAdapter(act_Mapa_Almacen.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Rack","Rack")));

                                        MostrarRack(sp_Rack.getSelectedItem().toString(),"@");

                                        break;



                                    case "ConsultaDocumentos":


                                        for(ArrayList<Constructor_Dato> d : dao.getcTablas())
                                            {
                                                for(Constructor_Dato a : d)
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

    private void MostrarRack(String prmRack,String prmLado)
    {
        Fragment fragment = (frgmnt_Ordenes) getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas);
        if (fragment != null)
            {

                ((frgmnt_Ordenes)fragment).ActualizarPosiciones(prmRack,prmLado);
            }
    }
}
