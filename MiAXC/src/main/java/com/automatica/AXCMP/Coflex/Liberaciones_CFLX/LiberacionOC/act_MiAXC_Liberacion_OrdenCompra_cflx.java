package com.automatica.AXCMP.Coflex.Liberaciones_CFLX.LiberacionOC;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.automatica.axc_lib.views.Adaptador_RV_Lotes_OC;
import com.automatica.axc_lib.views.Adaptador_RV_Partidas_Orden_Compra;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class act_MiAXC_Liberacion_OrdenCompra_cflx extends AppCompatActivity
        implements frgmnt_ConsultaTransacciones.OnFragmentInteractionListener, Adaptador_RV_Partidas_Orden_Compra.onClickRV,intro_fragment.OnFragmentInteractionListener, frgmnt_Seleccion_Lotes_CFLX.OnFragmentInteractionListener, frgmnt_Seleccion_Estacion.OnFragmentInteractionListener,
        Adaptador_RV_Lotes_OC.onClickRV

{
    //region variables


    Context contexto = this;

    String str_Orden,str_Linea;

    ArrayList<Embarque> arrPartidas;

    //Views

    Toolbar toolbar;

    EditText edtx_Documento,edtx_Proveedor,edtx_Factura,edtx_Referencia,edtx_FechaCrea,edtx_FechaRecibo;
    EditText edtx_Total,edtx_Moneda,edtx_TipoCambio,edtx_ClavePedimento,edtx_NumeroAgenteAduanal,edtx_NumeroPedimento,edtx_FechaPedimento;

    ImageButton imgb_Buscar;

    Button btn_LiberarEmbarque;

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
        setContentView(R.layout.act_miaxc_liberacion_orden_compra_cflx);
        declararVariables();
        agregaListeners();
//        toolbar.setSubtitle("Kardex");

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.constraintLayout, frgmnt_ConsultaTransacciones.newInstance(null, str_Orden),frgtag_Indicadores_Por_Turno)
                .commit();


        Date FechaActual = null;
        Calendar calendar = Calendar.getInstance();
        FechaActual =  calendar.getTime();

        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        edtx_FechaPedimento.setText(timeStamp);

        edtx_Documento.requestFocus();



        new esconderTeclado(act_MiAXC_Liberacion_OrdenCompra_cflx.this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setLogo(null);
        getSupportActionBar().setTitle("Liberación de Orden de compra");
        getSupportActionBar().setSubtitle("Para actualizar las ordenes, toque el botón de actualizar a la derecha.");

        edtx_Documento  = (EditText) findViewById(R.id.edtx_Embarque);




        //perdoname por hacer esto Dios
        edtx_Proveedor= (EditText) findViewById(R.id.edtx_Embarque2);
        edtx_Factura= (EditText) findViewById(R.id.edtx_Embarque4);
        edtx_Referencia= (EditText) findViewById(R.id.edtx_Embarque6);
        edtx_FechaCrea= (EditText) findViewById(R.id.edtx_Embarque5);
        edtx_FechaRecibo= (EditText) findViewById(R.id.edtx_Embarque7);
        edtx_Total= (EditText) findViewById(R.id.edtx_Embarque10);
        edtx_Moneda= (EditText) findViewById(R.id.edtx_Embarque11);
        edtx_TipoCambio= (EditText) findViewById(R.id.edtx_Embarque12);
        edtx_ClavePedimento= (EditText) findViewById(R.id.edtx_Embarque14);
        edtx_NumeroAgenteAduanal= (EditText) findViewById(R.id.edtx_Embarque16);
        edtx_NumeroPedimento= (EditText) findViewById(R.id.edtx_Embarque15);
        edtx_FechaPedimento= (EditText) findViewById(R.id.edtx_Embarque17);





        btn_LiberarEmbarque = (Button) findViewById(R.id.btn_LiberarEmbarque);


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
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese documento de Compra.", false, true, true);
                                LimpiarPantalla();
                                return;
                            }



                        lanzarConsulta();


                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(), false, true, true);
                    }
            }
        });


        edtx_FechaRecibo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {

                        newFragment = DatePickerFragment.newInstance(
                                new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                    {
                                        final String selectedDate = day + "/" + (month + 1) + "/" + year;
                                        edtx_FechaRecibo.setText(selectedDate);
                                    }
                                });

                        newFragment.show(getSupportFragmentManager(), "datePicker");


                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                return;
            }});

        edtx_FechaPedimento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {

                        newFragment = DatePickerFragment.newInstance(
                                new DatePickerDialog.OnDateSetListener()
                                {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                    {
                                        final String selectedDate = day + "/" + (month + 1) + "/" + year;
                                        edtx_FechaPedimento.setText(selectedDate);
                                    }
                                });

                        newFragment.show(getSupportFragmentManager(), "datePicker");


                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                return;
            }});



        btn_LiberarEmbarque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {


                        if(edtx_Documento.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese el documento de compra.", false, true, true);
                                return;
                            }

                        if(edtx_Proveedor.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"No hay un proveedor ingresado.", false, true, true);
                                return;
                            }
                        if(edtx_Factura.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese la factura de compra.", false, true, true);
                                return;
                            }
                        if(edtx_Referencia.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese una referencia.", false, true, true);
                                return;
                            }
                        if(edtx_FechaRecibo.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese fecha de recibo.", false, true, true);
                                return;
                            }



                         arrPartidas = ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).getArrayPartidasOrdenCompra();

                        if(arrPartidas == null)
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"No hay información de partidas.", false, true, true);
                                return;
                            }




                        new SegundoPlano("LiberaRecepcion").execute();


                    } catch (Exception e)
                    {
                        e.printStackTrace();
//                        lanzarConsulta();

                        new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(), false, true, true);
                    }
            }
        });




    }

    private void lanzarConsulta()
    {
            try
                {


                    String Estacion= null, TipoAjuste = null;


                    if(edtx_Documento.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(),"Ingrese Documento.", false, true, true);

                            ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).setSwipeRefreshStatus(false);

                            return;
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
//                refresh();
                new SegundoPlano("ActualizaOrdenCompra").execute(edtx_Documento.getText().toString());

            }
            if ((id == R.id.borrar_datos))
                {
                   LimpiarPantalla();

                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    private void LimpiarPantalla()
    {
        edtx_Documento.setText("");
        edtx_Proveedor.setText("");
        edtx_Factura.setText("");
        edtx_Referencia.setText("");
        edtx_Total.setText("");
        edtx_Moneda.setText("");
        edtx_TipoCambio.setText("");
        edtx_FechaCrea.setText("");
        edtx_FechaRecibo.setText("");
        edtx_ClavePedimento.setText("");
        edtx_NumeroAgenteAduanal.setText("");
        edtx_ClavePedimento.setText("");
        edtx_NumeroPedimento.setText("");
        edtx_FechaPedimento.setText("");


        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Liberacion_Orden_Prod_Limpiar();
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



    private void LiberarRecepcion()
    {

        try
            {

                new SegundoPlano("LiberaRecepcion").execute();
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,null,"Para agregar lotes, primero debe agregar una cantidad a recibir.",false, true, true);
            }
    }


    //region ELEGIR LOTE
    @Override//LANZA FRAGMENTO SELECTOR DE LOTE
    public String EditarLote(String Partida,int PosicionPartida,String prmProducto,int Cantidad,final ArrayList<Embarque> prmLotes)
    {

        if(Cantidad <= 0)
            {
                new popUpGenerico(contexto,null,"Para agregar lotes, primero debe agregar una cantidad a recibir.",false, true, true);
                return null;
            }


        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.constraintLayout7, frgmnt_Seleccion_Lotes_CFLX.newInstance(Partida,prmProducto,PosicionPartida,Cantidad),frgtag_SelLote)
                .addToBackStack(frgtag_SelLote)
                .commit();

        Handler h = new Handler();

        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ((frgmnt_Seleccion_Lotes_CFLX)(getSupportFragmentManager().findFragmentByTag(frgtag_SelLote))).AgregarLotes(prmLotes,true);
            }
        }, 600);



        return null;
    }

    @Override
    public void GuardarLotesPartida(ArrayList<Embarque> LotesPartida,int PosicionPartida)
    {
        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).AgregaLotes(LotesPartida,PosicionPartida);

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

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_Liberacion_OrdenCompra_cflx.this);

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


                                        dao = ca.c_ListarOrdenesRecepcionMiAXC(Orden);

                                        break;
                                    case "ActualizaOrdenCompra":

                                        dao = ca.c_ActualizaOrdenCompraMiAXC(Params[0]);


                                        break;
                                    case "LiberaRecepcion":

                                                dao = ca.c_LiberaOrdenCompraMiAXC(
                                                        edtx_Documento.getText().toString(),
                                                            edtx_Factura.getText().toString(),
                                                        edtx_Total.getText().toString(),
                                                        edtx_TipoCambio.getText().toString(),
                                                        edtx_Referencia.getText().toString(),
                                                        edtx_Moneda.getText().toString(),
                                                        edtx_FechaRecibo.getText().toString(),
                                                        edtx_ClavePedimento.getText().toString(),
                                                        edtx_NumeroAgenteAduanal.getText().toString(),
                                                        edtx_NumeroPedimento.getText().toString(),
                                                        edtx_FechaPedimento.getText().toString(),
                                                        arrPartidas,
                                                        null
                                                        );

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


                                    case "ActualizaOrdenCompra":

                                        new popUpGenerico(contexto,getCurrentFocus(),"Orden actualizada con éxito", dao.iscEstado(), true, true);


                                        if(!edtx_Documento.getText().toString().equals(""))
                                            {
                                                new SegundoPlano("ConsultaDocumentos").execute(edtx_Documento.getText().toString());
                                            }

                                        break;

                                    case "ConsultaDocumentos":

                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Liberacion_Orden_Compra(dao.getcTablas());

                                        edtx_Proveedor.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NomProveedor"));
                                        edtx_FechaCrea.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("FechaCrea"));

                                        edtx_Total.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("SubTotal"));
                                        edtx_Moneda.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Moneda"));
                                        edtx_NumeroPedimento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("TipoCambio"));
                                        edtx_FechaRecibo.setText("");
                                        edtx_ClavePedimento.setText("");
                                        edtx_NumeroAgenteAduanal.setText("");
                                        edtx_NumeroPedimento.setText("");
                                        edtx_FechaPedimento.setText("");
//                                        edtx_FechaPedimento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("FechaCrea"));
//                                        edtx_FechaRecibo.setText(dao.getSoapObject().getPrimitivePropertyAsString("FechaCrea"));
////                                        cb_FiltroEstacion.setChecked(false);
//                                        cb_Lote.setChecked(false);

                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).setSwipeRefreshStatus(false);

                                        break;

                                    case "ListaProductos":


                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Liberacion_Orden_Compra(dao.getcTablas());




                                        break;

                                    case "LiberaRecepcion":

                                        new popUpGenerico(contexto,getCurrentFocus(), "Recepción liberada con éxito.", dao.iscEstado(), true, true);


                                        break;


                                    case "ListaEstaciones":

//                                        sp_Estacion.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_OrdenCompra.this,
//                                                android.R.layout.simple_spinner_item,
//                                                dao.getcTablasSorteadas("Estacion","Estacion")));

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





}
