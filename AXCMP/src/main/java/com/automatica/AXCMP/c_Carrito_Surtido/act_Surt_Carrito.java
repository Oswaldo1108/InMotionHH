package com.automatica.AXCMP.c_Carrito_Surtido;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_MenuPrincipal;

public class act_Surt_Carrito extends AppCompatActivity implements frgmnt_Menu_Inicio.OnFragmentInteractionListener,
        frgmnt_Detalle_Partida.OnFragmentInteractionListener, Adaptador_RV_MenuPrincipal.onClickRV, frgmnt_taskbar.OnFragmentInteractionListener
{
    private static final String frgtag_Menu_Inicio = "FRGMI";
    private static final String frgtag_C = "FRGMC";
    private static final String frgtag_ActionBar = "FRGAB";

    private boolean recarga,reiniciaCampos;
    private Context contexto= this;
    private FrameLayout progressBarHolder = null;

    @Override
    public boolean LastActivity()
    {
        finish();
        return false;
    }

    boolean Estado;
    private Button btn_AgregarOrden;

    private String AgregarAOrdenar;
    TextView txtv_CantidadPedidos;





    private void setTextNavigator(String Next,String Last)
    {
        ((frgmnt_taskbar)getSupportFragmentManager().findFragmentByTag(frgtag_ActionBar)).setTextNavigator(Next, Last);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_act__surt__carrito);

                //Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
                getSupportActionBar().setTitle("Cart Moving - Selección de pedidos");


                DeclararVariables();
                AgregarListeners();

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.layout_ordenes, frgmnt_Menu_Inicio.newInstance(null, null), frgtag_Menu_Inicio)
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.layout_area_trabajo, frgmnt_Detalle_Partida.newInstance(new String[]{"INICIALIZACION"}, null), frgtag_C)
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.actionbar, frgmnt_taskbar.newInstance("Menú\nprincipal","Detalle"), frgtag_ActionBar)
                        .commit();

              //  ((frgmnt_taskbar)getSupportFragmentManager().findFragmentByTag(frgtag_ActionBar)).setTextNavigator("Menu\nprincipal","Detalle");

                //setTextNavigator("Menu\nprincipal","Detalle");

                new SegundoPlano("ConsultarPedidosSeleccionados").execute();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }


    private void DeclararVariables()
    {
        btn_AgregarOrden = findViewById(R.id.btn_AgregarOrden);
        txtv_CantidadPedidos = findViewById(R.id.txtv_CantidadPedidos);
    }

    private void AgregarListeners()
    {
        btn_AgregarOrden.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                frgmnt_Detalle_Partida f = (frgmnt_Detalle_Partida) getSupportFragmentManager().findFragmentByTag(frgtag_C);
                AgregarAOrdenar = f.getDocumento();
                new SegundoPlano("AgregarPedido").execute();

            }
        });
    }


    @Override
    public boolean NextActivity()
    {
        Intent intent = new Intent(act_Surt_Carrito.this,act_Surt_Carrito_Ordenes.class);
        startActivity(intent);
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
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
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

            if (!recarga) {
                if ((id == R.id.InformacionDispositivo)) {
                    new sobreDispositivo(contexto, null);
                }

                if ((id == R.id.borrar_datos)) {
                    //reiniciaCampos();
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
      //  Toast.makeText(contexto, "HOLA", Toast.LENGTH_SHORT).show();
        frgmnt_Detalle_Partida f = (frgmnt_Detalle_Partida) getSupportFragmentManager().findFragmentByTag(frgtag_C);
        f.ConsultarOrden(datos);
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return CambiarEstadocarga(estado);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(boolean estado)
    {
        return CambiarEstadocarga(estado);
    }


    public boolean CambiarEstadocarga(boolean Estado)
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

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(act_Surt_Carrito.this);
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
//                mListener.ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.axc_lib.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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

                                    case "ConsultarPedidosSeleccionados":
                                        dao = ca.C_PedidosPorEstacion();
                                        break;


                                    case "AgregarPedido":
                                        dao = ca.C_AgregaPedidoCM(AgregarAOrdenar);
                                        break;



                                    default:
                                        dao = new DataAccessObject(false,"Operación no soportada. ["+ tarea+ "]",null);
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

                                    case "ConsultarPedidosSeleccionados":

                                        txtv_CantidadPedidos.setText(dao.getcTablas().get(0).get(0).getDato());
                                        break;

                                    case "AgregarPedido":
                                        new SegundoPlano("ConsultarPedidosSeleccionados").execute();
                                        frgmnt_Menu_Inicio fmi = (frgmnt_Menu_Inicio) getSupportFragmentManager().findFragmentByTag(frgtag_Menu_Inicio);
                                        fmi.RecargarDatos();
                                        break;

                                }
                        }else
                        {
                            new com.automatica.axc_lib.Servicios.popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.axc_lib.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }
      //      mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
           // mListener.ActivaProgressBar(false);
            super.onCancelled();
        }
    }


}
