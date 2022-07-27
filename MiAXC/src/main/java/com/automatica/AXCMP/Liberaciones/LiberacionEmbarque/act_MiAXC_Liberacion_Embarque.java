package com.automatica.AXCMP.Liberaciones.LiberacionEmbarque;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCMP.MiAXC.ConsultaTransacciones.frgmnt_ConsultaTransacciones;
import com.automatica.AXCMP.Principal.intro_fragment;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.DatePickerFragment;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.axc_lib.ClasesSerializables.Embarque;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_Partidas;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class act_MiAXC_Liberacion_Embarque extends AppCompatActivity
        implements frgmnt_ConsultaTransacciones.OnFragmentInteractionListener, Adaptador_RV_Partidas.onClickRV,intro_fragment.OnFragmentInteractionListener, frgmnt_Seleccion_Lote.OnFragmentInteractionListener, frgmnt_Seleccion_Estacion.OnFragmentInteractionListener
{
    //region variables


     Context contexto = this;

    String str_Orden,str_Linea;

    ArrayList<Embarque> arrPartidas;

    //Views
    CheckBox cb_FiltroEstacion,cb_Lote;
    Toolbar toolbar;

    EditText edtx_Documento;

    ImageButton imgb_Buscar;

    Button btn_LiberarEmbarque;

    Spinner sp_Estacion;

    FrameLayout progressBarHolder;
    DatePickerFragment newFragment;


    View vista;


    private static final String frgtag_SelLote= "SelLote";
    private static final String frgtag_Selest= "SeleST";


    boolean recargar;
    private static final String frgtag_Indicadores_Por_Turno = "frgtag_Indicadores_Por_Turno";

    //endregion

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_miaxc_liberacion_embarques);
        declararVariables();
        agregaListeners();

//        toolbar.setSubtitle("Kardex");

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.constraintLayout, frgmnt_ConsultaTransacciones.newInstance(null, str_Orden),frgtag_Indicadores_Por_Turno)
                .commit();




        new esconderTeclado(act_MiAXC_Liberacion_Embarque.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Liberación de Embarques");
        edtx_Documento  = (EditText) findViewById(R.id.edtx_Embarque);
        sp_Estacion = findViewById(R.id.vw_Spinner_Estacion).findViewById(R.id.spinner);
        btn_LiberarEmbarque = (Button) findViewById(R.id.btn_LiberarEmbarque);
        cb_FiltroEstacion= (CheckBox) findViewById(R.id.cb_FiltroUser);
        cb_Lote= (CheckBox) findViewById(R.id.cb_Lote);
        sp_Estacion.setEnabled(false);
        imgb_Buscar = (ImageButton) findViewById(R.id.imgb_Buscar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);



    }

    private void agregaListeners()
    {


        imgb_Buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {


                        if(edtx_Documento.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese documento de embarque.", false, true, true);
                                return;
                            }


                        lanzarConsulta();


                    } catch (Exception e)
                    {
                        e.printStackTrace();
//                        lanzarConsulta();

                        new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(), false, true, true);
                    }
            }
        });


        btn_LiberarEmbarque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {


                        if(edtx_Documento.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese documento de embarque.", false, true, true);
                                return;
                            }





                         arrPartidas = ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).getArrayPartidasEmbarque();

                        if(arrPartidas == null)
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"No hay información de partidas.", false, true, true);
                                return;
                            }




                        new SegundoPlano("LiberaEmbarque").execute();


                    } catch (Exception e)
                    {
                        e.printStackTrace();

                        new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(), false, true, true);
                    }
            }
        });



        cb_Lote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(!isChecked)
                    {
                        LoteElegido(-1,"");
                    }
            }
        });



        cb_FiltroEstacion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_FiltroEstacion.isChecked())
                    {
                        new SegundoPlano("ListaEstaciones").execute(null,null);
                    }
                else
                    {
                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaEstacion(-1, "");
                        sp_Estacion.setAdapter(null);
                    }

                sp_Estacion.setEnabled(cb_FiltroEstacion.isChecked());
            }
        });



        sp_Estacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(!cb_FiltroEstacion.isChecked())
                    {
                        return;
                    }
                ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaEstacion(-1, sp_Estacion.getSelectedItem().toString());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaEstacion(-1,"");

            }
        });




    }


    private void lanzarConsulta()
    {
            try
                {


                    String Estacion= null, TipoAjuste = null;


                    if (sp_Estacion.getAdapter() != null)
                        {
                            Estacion = ((CustomArrayAdapter) sp_Estacion.getAdapter()).getSelectedExtra(sp_Estacion.getSelectedItemPosition());
                        }

                    new SegundoPlano("ConsultaDocumentos").execute(
                            edtx_Documento.getText().toString(),
                            "@",
                            "@",
                            "@");


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
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
                refresh();
            }
            if ((id == R.id.borrar_datos))
                {
                    edtx_Documento.setText("");

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
        return false;
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
        lanzarConsulta();

    }

    @Override
    public void setParametros(String Existencia, String Inicial, String Salidas, String Entradas)
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









    //region ELEGIR LOTE
    @Override//LANZA FRAGMENTO SELECTOR DE LOTE
    public String EditarLote(int PosicionPartida,String prmProducto)
    {

        if(!cb_Lote.isChecked())
            {
                return null;
            }


        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.constraintLayout7, frgmnt_Seleccion_Lote.newInstance(null, prmProducto,PosicionPartida),frgtag_SelLote)
                .addToBackStack(frgtag_SelLote)
                .commit();

        return null;
    }

    @Override//LANZA REGISTRO DE LOTE EN EL ARREGLO DE PARTIDAS
    public void LoteElegido(int Partida,String prmLote)
    {
        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaLote(Partida, prmLote);

    }
    //endregion








    //region ELEGIR ESTACION

    @Override
    public String EditarEstacion(int PosicionPartida,String prmEstacion)
    {

//        if(cb_FiltroEstacion.isChecked())
//            {
//                return null;
//            }


        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.constraintLayout7, frgmnt_Seleccion_Estacion.newInstance(null, prmEstacion,PosicionPartida),frgtag_Selest)
                .addToBackStack(frgtag_SelLote)
                .commit();



        return null;
    }

    @Override
    public void EstacionElegida(int Partida, String prmEstacion)
    {
        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaEstacion(Partida, prmEstacion);

    }


    //endregion
















    @Override
    public String EditarCantidad(int PosicionPartida, String prmCantidad)
    {
//
//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .add(R.id.constraintLayout7, frgmnt_Seleccion_Lote.newInstance(null, prmCantidad,PosicionPartida),frgtag_SelLote)
//                .addToBackStack(frgtag_SelLote)
//                .commit();


        return null;
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

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_Liberacion_Embarque.this);

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
                                        String Orden = "@",Status = "@", Desdec = "@", Hastac = "@";

                                        if(Params[0]!=null)
                                            {
                                                Orden = Params[0];
                                            }
                                        if(Params[1]!=null)
                                            {
                                                Status = Params[1];
                                                if(Status.equals(""))
                                                    Status = "@";
                                            }


                                        if(Params[2]!=null)
                                            {
                                                Desdec = Params[2];
                                            }
                                        if(Params[3]!=null)
                                            {
                                                Hastac = Params[3];
                                            }

                                        dao = ca.c_ListaOrdenesEmbarqueDetMiAXC(Orden,Status,Desdec,Hastac);

                                        break;

                                    case "LiberaEmbarque":


                                        if(cb_Lote.isChecked())
                                            {
                                                dao = ca.c_LiberaOrdenEmbarqueMiAXC("1", edtx_Documento.getText().toString(), arrPartidas);
                                            }else
                                            {
                                                dao = ca.c_LiberaOrdenEmbarqueMiAXC("0", edtx_Documento.getText().toString(), arrPartidas);
                                            }



                                        break;

                                    case "ListaEstaciones":

                                        dao = ca.c_ListaEstacionesMiAXC();

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

                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_LiberacionEmbarque(dao.getcTablas());
                                        cb_FiltroEstacion.setChecked(false);
                                        cb_Lote.setChecked(false);
                                        break;

                                    case "ListaProductos":


                                        break;

                                    case "LiberaEmbarque":

                                        new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);


                                        break;


                                    case "ListaEstaciones":

                                        sp_Estacion.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_Embarque.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Estacion","Estacion")));

                                        break;



                                }


//                            XmlSerializer






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





}
