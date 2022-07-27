package com.automatica.AXCMP.MiAXC.ConsultaReferencia;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.automatica.AXCMP.MiAXC.ConsultaTransacciones.frgmnt_ConsultaTransacciones;
import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.Principal.intro_fragment;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class act_MiAXC_ConsultaReferencia extends AppCompatActivity
        implements frgmnt_ConsultaTransacciones.OnFragmentInteractionListener, Adaptador_RV_MenuPrincipal.onClickRV,intro_fragment.OnFragmentInteractionListener
{
    //region variables


     Context contexto = this;

    String str_Orden,str_Linea;



    //Views

    Toolbar toolbar;
    EditText edtx_Referencia;
    ImageButton imgb_Buscar;

    FrameLayout progressBarHolder;

    View vista;



    private static final String frgtag_Indicadores_Por_Turno = "frgtag_Indicadores_Por_Turno";

    boolean recargar;

    //endregion

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_miaxc_referencia_);
        declararVariables();
        agregaListeners();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.constraintLayout, frgmnt_ConsultaTransacciones.newInstance(null, str_Orden),frgtag_Indicadores_Por_Turno)
                .commit();

        toolbar.setSubtitle("Consulta de referencia");


        new esconderTeclado(act_MiAXC_ConsultaReferencia.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(null);

        edtx_Referencia  = (EditText) findViewById(R.id.edtx_Referencia);


        imgb_Buscar = (ImageButton) findViewById(R.id.imgb_Buscar);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

    }

    private void agregaListeners()
    {


        edtx_Referencia.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(edtx_Referencia.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto,getCurrentFocus() ,"Ingrese una referencia." ,false ,true , true);
                                return false;
                            }

                        new SegundoPlano("ConsultaReferencia").execute(edtx_Referencia.getText().toString());
                    }
                return false;
            }
        });



        imgb_Buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {


//                        lanzarConsulta();
                        new SegundoPlano("ConsultaReferencia").execute(edtx_Referencia.getText().toString());


                    } catch (Exception e)
                    {
                        e.printStackTrace();
//                        lanzarConsulta();

                        new popUpGenerico(contexto, getCurrentFocus(),"Ingrese fechas de consulta.", false, true, true);
                    }
            }
        });








    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void ReiniciarVariables()
    {
        edtx_Referencia.setText("");
        ((frgmnt_ConsultaTransacciones) (getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Referencia_Limpiar();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(!recargar)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.recargar))
            {
                refresh();
            }
            if ((id == R.id.borrar_datos))
                {
                    ReiniciarVariables();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(boolean estado)
    {
        return webmPlayer(estado);
    }

    private static final String frgtag_Loading= "FRGLOAD";

    private boolean webmPlayer(final boolean estado)
    {

        if(estado)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.constraintLayout7, intro_fragment.newInstance(null, null),frgtag_Loading)
                        .commit();
            }else
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(frgtag_Loading);
                if (fragment != null)
                    {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }

            }
        return true;
    }


    @Override
    public void refresh()
    {
       if(edtx_Referencia.getText().toString().equals(""))
           {
               return;
           }

        new SegundoPlano("ConsultaReferencia").execute(edtx_Referencia.getText().toString());

    }

    @Override
    public void setParametros(String Inicial, String Entradas, String Salidas, String Existencia)
    {

    }

    @Override
    public void clickBotonMasInfo(String[] datos)
    {
//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .replace(R.id.constraintLayout7, frgmnt_Timeline_Orden_Individual.newInstance(null, null),frgtag_Timeline_Orden)
//                .commit();
    }
    @Override
    protected void onResume()
    {

    //    new SegundoPlano("ConsultaDocumentos").execute();
        super.onResume();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {

        DataAccessObject dao;
        String tarea;
        View view;

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_ConsultaReferencia.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
               ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), false, true, true);
                }
        }

        @Override
        protected Void doInBackground(String...Params)
        {
            try
                {
                    if(!this.isCancelled())
                        {
                            switch (tarea)
                                {


                                    case "ConsultaReferencia":

                                        dao = ca.C_ConsultaReferencia(Params[0]);

                                        break;


                                    default:
                                        dao = new DataAccessObject(false,"Operación no soportada",null);
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
                                    case "ConsultaReferencia":




                                        new esconderTeclado(act_MiAXC_ConsultaReferencia.this);
                                        if(dao.getcTablas() != null)
                                            {
                                                ((frgmnt_ConsultaTransacciones) (getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Referencia(dao.getcTablas());
                                            }
                                        else
                                            {

                                                ReiniciarVariables();
                                                new popUpGenerico(contexto,getCurrentFocus(), "No se han encontrado coincidencias.", dao.iscEstado(), true, true);
                                            }
                                        break;


                                }
                        }else
                        {
                            new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            ReiniciarVariables();
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }

                    ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            ActivaProgressBar(false);
            new popUpGenerico(contexto,getCurrentFocus(), "Cancelado", false, true, true);
            super.onCancelled();
        }
    }


}
