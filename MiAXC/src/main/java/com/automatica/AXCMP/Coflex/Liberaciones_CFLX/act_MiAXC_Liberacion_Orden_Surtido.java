package com.automatica.AXCMP.Coflex.Liberaciones_CFLX;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCMP.Coflex.Liberaciones_CFLX.LiberacionOrdenSurtido.frgmnt_Seleccion_Revision;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.AXCMP.Liberaciones.LiberacionEmbarque.frgmnt_Seleccion_Estacion;
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

public class act_MiAXC_Liberacion_Orden_Surtido extends AppCompatActivity
        implements frgmnt_ConsultaTransacciones.OnFragmentInteractionListener, Adaptador_RV_Partidas.onClickRV,intro_fragment.OnFragmentInteractionListener, frgmnt_Seleccion_Revision.OnFragmentInteractionListener, frgmnt_Seleccion_Estacion.OnFragmentInteractionListener
{
    //region variables

    Context contexto = this;

    String str_Orden,str_Linea;

    ArrayList<Embarque> arrPartidas;

    //Views
    CheckBox cb_FiltroEstacion;
    Toolbar toolbar;

    ImageButton imgb_Buscar;

    Button btn_LiberarEmbarque;

    Spinner sp_OrdenProduccion,sp_Planta,sp_Almacen,sp_Estacion;

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
        setContentView(R.layout.act_miaxc_liberacion_orden_prod);
        declararVariables();
        agregaListeners();

//        toolbar.setSubtitle("Kardex");

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.constraintLayout, frgmnt_ConsultaTransacciones.newInstance(null, str_Orden),frgtag_Indicadores_Por_Turno)
                .commit();


        new SegundoPlano("ListaPlantas").execute();

        new SegundoPlano("ListaOrdenesProd").execute();




        new esconderTeclado(act_MiAXC_Liberacion_Orden_Surtido.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setLogo(null);
        getSupportActionBar().setTitle("Liberación de orden de producción");

//        edtx_FiltroEstacion  = (EditText) findViewById(R.id.edtx_FiltroUser);

        sp_OrdenProduccion = findViewById(R.id.vw_Spinner_OP).findViewById(R.id.spinner);
        sp_Planta = findViewById(R.id.vw_Spinner_Planta).findViewById(R.id.spinner);
        sp_Almacen = findViewById(R.id.vw_Spinner_Almacen).findViewById(R.id.spinner);
        sp_Estacion = findViewById(R.id.vw_Spinner_Estacion).findViewById(R.id.spinner);

        btn_LiberarEmbarque = (Button) findViewById(R.id.btn_LiberarEmbarque);

        cb_FiltroEstacion= (CheckBox) findViewById(R.id.cb_FiltroUser);
        sp_Estacion.setEnabled(false);
//        edtx_FiltroEstacion  = (EditText) findViewById(R.id.edtx_FiltroUser);
//        edtx_FiltroEstacion.setEnabled(false);
//        imgv_BusquedaEstacion.setEnabled(false);

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


                        if(sp_OrdenProduccion.getAdapter() == null)
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese orden de producción.", false, true, true);
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


                        if(sp_OrdenProduccion.getAdapter() == null)
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
//                        lanzarConsulta();

                        new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(), false, true, true);
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

        sp_Planta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
//                if(!cb_FiltroEstacion.isChecked())
//                    {
//                        return;
//                    }
//                ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaEstacion(-1, sp_Estacion.getSelectedItem().toString());

                new SegundoPlano("ListaAlmacenes").execute();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
//                ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaEstacion(-1,"");

            }
        });


        sp_OrdenProduccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                ((EditText)findViewById(R.id.edtx_Embarque8)) .setText(((Constructor_Dato) sp_OrdenProduccion.getSelectedItem()).getTag1());//producto
                ((EditText)findViewById(R.id.edtx_Embarque9)) .setText(((Constructor_Dato) sp_OrdenProduccion.getSelectedItem()).getTag2());//CantidadTotal

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
//                ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).ActualizaEstacion(-1,"");

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



//                    String NumParte = "@",prmLote = "@",prmTipoMovimiento = "@",TipoAjuste = "@", Desdec = "@", Hastac = "@";

                    new SegundoPlano("ConsultaDocumentos").execute(
                            sp_OrdenProduccion.getSelectedItem().toString(),
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
        try
        {
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
                        .replace(R.id.constraintLayout7, intro_fragment.newInstance(null, null),frgtag_Loading)
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




        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.constraintLayout7, frgmnt_Seleccion_Revision.newInstance(null, prmProducto,PosicionPartida),frgtag_SelLote)
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

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_Liberacion_Orden_Surtido.this);

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
                                        String Orden = "@";

                                        if(Params[0]!=null)
                                            {
                                                Orden = Params[0];
                                            }
                                        dao = ca.c_ListaOrdenesProdDetMiAXC(Orden);

                                        break;




                                    case "ListaOrdenesProd":

                                    dao = ca.c_ListaOrdenesProduccion();

                                    break;


                                    case "LiberaEmbarque":

                                        dao = ca.c_LiberaLiberaOrdenProdMiAXC
                                                (
                                                        sp_OrdenProduccion.getSelectedItem().toString(),
                                                        ((EditText)findViewById(R.id.edtx_Embarque8)).getText().toString(),
                                                        ((EditText)findViewById(R.id.edtx_Embarque9)).getText().toString(),
                                                        arrPartidas
                                                );

                                        break;

                                    case "ListaAlmacenes":

                                        dao = ca.c_ListaAlmacenesFiltroMiAXC(sp_Planta.getSelectedItem().toString());

                                        break;


                                    case "ListaPlantas":

                                        dao = ca.C_ListaPlantasMiAXC();

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

                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Liberacion_Orden_Prod(dao.getcTablas());
                                        cb_FiltroEstacion.setChecked(false);
                                        break;

                                    case "ListaProductos":


                                        break;

                                    case "LiberaEmbarque":

                                        new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Liberacion_Orden_Prod_Limpiar();

                                        new SegundoPlano("ListaOrdenesProd").execute();

                                        break;


                                    case "ListaOrdenesProd":
                                        sp_OrdenProduccion.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_Orden_Surtido.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("OrdenProd","NumParte","CantidadTotal")));

                                        break;


                                    case "ListaPlantas":

                                        sp_Planta.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_Orden_Surtido.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Planta","Planta")));
                                        break;

                                    case "ListaAlmacenes":

                                        sp_Almacen.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_Orden_Surtido.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Almacen","Almacen")));

                                        break;
                                    case "ListaEstaciones":

                                        sp_Estacion.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_Orden_Surtido.this,
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
