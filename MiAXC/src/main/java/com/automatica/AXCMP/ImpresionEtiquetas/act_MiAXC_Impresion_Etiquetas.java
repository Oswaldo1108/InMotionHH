package com.automatica.AXCMP.ImpresionEtiquetas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCMP.MiAXC.ConsultaTransacciones.frgmnt_ConsultaTransacciones;
import com.automatica.AXCMP.MiAXC.frgmt_Consulta_Camera_view;
import com.automatica.AXCMP.Principal.intro_fragment;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.EdtxLongClickListener;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.adaptadorTabla_Reimpresion_Empaques;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class act_MiAXC_Impresion_Etiquetas extends AppCompatActivity implements frgmnt_Imprime_Etiquetas.OnFragmentInteractionListener,
                                                                                    frgmt_Consulta_Camera_view.dataTransfer,
                                                                                    frgmnt_Consulta_Pallet_Reimpresion.OnFragmentInteractionListener,
                                                                                        intro_fragment.OnFragmentInteractionListener,
                                                                                                adaptadorTabla_Reimpresion_Empaques.ListenerBoton,
        EdtxLongClickListener.DataTransfer




{
    //region variables


     Context contexto = this;

    String str_Orden,str_Linea;



    //Views
    CheckBox cb_FiltroFecha,cb_FiltroUsuario,cb_FiltroTransaccion;
    Toolbar toolbar;
//    EditText edtx_Desde,edtx_Hasta,edtx_FiltroUser,edtx_FiltroTransaccion;
//    ImageButton imgb_Buscar;
//
//    ImageView imgv_BusquedaUser,imgv_BusquedaTrans;
//    Spinner sp_Usuario,sp_Transaccion;

    Button btn_EtiquetaPallets, btn_EtiquetasEmpaque, btn_ReimpEmpaque, btn_ReimpPallet;
    FrameLayout progressBarHolder;
//    DatePickerFragment newFragment;


    View vista;



    private static final String frgtag_Indicadores_Por_Turno = "frgtag_Indicadores_Por_Turno";

    boolean recargar;

    //endregion
    private int RequestSoliticer = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_menu_impresiones);
        declararVariables();
        agregaListeners();


        toolbar.setSubtitle("Impresión de etiquetas");


        new esconderTeclado(act_MiAXC_Impresion_Etiquetas.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(null);



        btn_EtiquetaPallets   = (Button) findViewById(R.id.btn_EtiquetaPallets);
        btn_EtiquetasEmpaque  = (Button) findViewById(R.id.btn_EtiquetasEmpaque);
        btn_ReimpEmpaque      = (Button) findViewById(R.id.btn_ReimpEmpaque);
        btn_ReimpPallet       = (Button) findViewById(R.id.btn_ReimpPallet);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

    }

    private void agregaListeners()
    {

        btn_EtiquetaPallets.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {

                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.cl, frgmnt_Imprime_Etiquetas.newInstance(null, null),frgtag_Indicadores_Por_Turno).addToBackStack(frgtag_Indicadores_Por_Turno)
                                .commit();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        });

        btn_EtiquetasEmpaque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {

                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.cl, frgmnt_Imprime_Etiquetas.newInstance(null, null),frgtag_Indicadores_Por_Turno).addToBackStack(frgtag_Indicadores_Por_Turno)
                                .commit();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        });

        btn_ReimpEmpaque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


            }
        });

        btn_ReimpPallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {

                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.cl, frgmnt_Consulta_Pallet_Reimpresion.newInstance(null, null),frgtag_Indicadores_Por_Turno).addToBackStack(frgtag_Indicadores_Por_Turno)
                                .commit();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
        });



    }

    @Override
    public void ValidateLaunchedView(int ViewId)
    {
        RequestSoliticer = ViewId;
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
//                refresh();
            }
            if ((id == R.id.borrar_datos))
                {

                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return webmPlayer(estado);
    }



    private static final String frgtag_Loading= "FRGLOAD";

    private boolean webmPlayer(final boolean estado)
    {
        try
            {
                if (estado)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.cl, intro_fragment.newInstance(null, null), frgtag_Loading)
                                .commit();
                    } else
                    {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(frgtag_Loading);
                        if (fragment != null)
                            {
                                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                            }

                    }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        return true;
    }



//    @Override
//    public void clickBotonMasInfo(String[] datos)
//    {
////        getSupportFragmentManager().beginTransaction()
////                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
////                .replace(R.id.constraintLayout7, frgmnt_Timeline_Orden_Individual.newInstance(null, null),frgtag_Timeline_Orden)
////                .commit();
//    }
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

    @Override
    public String ReturnData(String[] dataToReturn)
    {
        return null;
    }

    @Override
    public void ClickBotonReimprimir(String prmCodigoEmpaque)
    {
//            Toast.makeText(contexto, prmCodigoEmpaque, Toast.LENGTH_LONG).show();

        ((frgmnt_Consulta_Pallet_Reimpresion)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ImprimirEmpaque(prmCodigoEmpaque);

    }

//    @Override
//    public void ValidateLaunchedView(int ViewId)
//    {
//
//    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {

        DataAccessObject dao;
        String tarea;
        View view;

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_Impresion_Etiquetas.this);

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


                                    case "ConsultaDocumentos":
                                        String Usuarioc = "@", transaccionc = "@", Desdec = "@", Hastac = "@";

                                        if(Params[0]!=null)
                                            {
                                                Usuarioc = Params[0];
                                            }
                                        if(Params[1]!=null)
                                            {
                                                transaccionc = Params[1];
                                            }
                                        if(Params[2]!=null)
                                            {
                                                Desdec = Params[2];
                                            }
                                        if(Params[3]!=null)
                                            {
                                                Hastac = Params[3];
                                            }

                                        dao = ca.C_ConsultaTransacciones(Usuarioc,transaccionc,Desdec,Hastac);

                                        break;

                                    case "ListaUsuarios":

                                        String Usuario = "@", grupo = "@";

                                        if(Params[0]!=null)
                                            {
                                                Usuario = Params[0];
                                            }
                                        if(Params[1]!=null)
                                            {
                                                grupo = Params[1];
                                            }

                                        dao = ca.C_ListaUsuarios(Usuario,grupo);

                                        break;

                                    case "ListaTransaccion":

                                        String Transaccion = "@";

                                        if(Params[0]!=null)
                                            {
                                                Transaccion  = Params[0];
                                            }


                                        dao = ca.C_ListaTransaccion(Transaccion);

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




                                    case "ConsultaDocumentos":

                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData(dao.getcTablas());

                                        break;

                                    case "ListaUsuarios":

                                        break;


                                    case "ListaTransaccion":

                                        break;



                                }
                        }else
                        {
                            new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null)
            {
                return;
            }
        if (result.getContents() == null)
            {
                return;
            }


        ((frgmnt_Consulta_Pallet_Reimpresion)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ConsultarEscaneo(result.getContents());

        super.onActivityResult( requestCode,  resultCode, data);
    }

}
