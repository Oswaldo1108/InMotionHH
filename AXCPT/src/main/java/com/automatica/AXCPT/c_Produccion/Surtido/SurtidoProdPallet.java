package com.automatica.AXCPT.c_Produccion.Surtido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Surtido_Pallet_MultiplesProd;
import com.automatica.AXCPT.databinding.ActivitySurtidoProdPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class SurtidoProdPallet extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar{

    private Toolbar toolbar;
    private EditText edtx_Pallet, edtx_ConfirmarPallet;
    private TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg, txtv_SugPallet, txtv_SugLote, txtv_SugPosicion,txtv_TipoPallet,txtv_SugEmpaque;
    private ProgressBarHelper progressBarHelper;
    private Context contexto = this;
    private String Pedido, Partida, NumParte, CantidadPendiente, CantidadSurtida, Carrito;
    private SortableTableView tabla;
    private TableViewDataConfigurator ConfigTabla = null;
    private Handler handler = new Handler();
    private Bundle b;
    private Spinner sp_partidas;
    private String TipoReg = null;
    private ActivityHelpers activityHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_surtido_prod_pallet);
            new cambiaColorStatusBar(contexto, R.color.MoradoStd, this);
            SacaExtrasIntent();
            declararVariables();
            agregaListeners();

            new esconderTeclado(this);

            edtx_Pallet.requestFocus();

            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));

        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Recarga();

        super.onResume();
    }
    private void Recarga()
    {
        if(!txtv_Pedido.getText().toString().equals("-"))
        {
            new SurtidoProdPallet.SegundoPlano("ConsultaPedidoSurtido").execute();
        }
    }


    private void declararVariables()
    {
        try
        {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido));
            getSupportActionBar().setSubtitle("Por pallet");
            progressBarHelper = new ProgressBarHelper(this);

            edtx_Pallet = (EditText) findViewById(R.id.edtx_Pallet);
            edtx_ConfirmarPallet = (EditText) findViewById(R.id.edtx_ConfirmarPallet);
            tabla = (SortableTableView) findViewById(R.id.tableView_OC);
            edtx_Pallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_ConfirmarPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            txtv_Pedido = (TextView) findViewById(R.id.txtv_Pedido);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_CantPend = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_CantReg = (TextView) findViewById(R.id.txtv_CantidadReg);
            txtv_SugPallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_SugLote = (TextView) findViewById(R.id.txtv_Lote);
            txtv_SugPosicion = (TextView) findViewById(R.id.txtv_Posicion);
            txtv_TipoPallet = (TextView) findViewById(R.id.txtv_TipoPallet);
            txtv_SugEmpaque = (TextView) findViewById(R.id.txtv_SugEmpaque);
            sp_partidas = findViewById(R.id.vw_spinner_partidas).findViewById(R.id.spinner);
            txtv_Pedido.setText(Pedido);
            txtv_Producto.setText(NumParte);
            txtv_CantPend.setText(CantidadPendiente);
            txtv_CantReg.setText(CantidadSurtida);

            activityHelpers = new ActivityHelpers();
            activityHelpers.AgregarMenus(this,R.id.PantallaPrincipal,false);

        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }
    private void SacaExtrasIntent()
    {
        try
        {

            b = getIntent().getExtras();
            Pedido= b.getString("Pedido");
            Partida= b.getString("Partida");
            NumParte= b.getString("NumParte");
            Carrito = b.getString("Carrito");
            CantidadPendiente= b.getString("CantidadPendiente");
            CantidadSurtida= b.getString("CantidadSurtida");

        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }
    private void agregaListeners()
    {


        sp_partidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                new SurtidoProdPallet.SegundoPlano("ConsultaPedidoDet").execute(((Constructor_Dato) sp_partidas.getSelectedItem()).getDato());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        edtx_ConfirmarPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    if(edtx_Pallet.getText().toString().equals(""))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Pallet.setText("");
                                edtx_Pallet.requestFocus();
                            }
                        });
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet) ,"false" ,true , true);
                        return false;
                    }
                    if(edtx_ConfirmarPallet.getText().toString().equals(""))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_ConfirmarPallet.setText("");
                                edtx_ConfirmarPallet.requestFocus();
                            }
                        });
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet_confirmacion) ,"false" ,true , true);
                    }

                    if(!edtx_Pallet.getText().toString().equals(edtx_ConfirmarPallet.getText().toString()))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Pallet.setText("");
                                edtx_Pallet.requestFocus();
                            }
                        });
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.pallets_no_coinciden) ,false ,true , true);
                        return false;
                    }



                    if(TipoReg == null)
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,"Consulte la tarima antes de registrar." ,false ,true , true);
                        return false;
                    }


                    if(TipoReg.equals("PartidaUnica"))
                    {
                        Log.i("TipoReg","PartidaUnica");
                        new SurtidoProdPallet.SegundoPlano("RegistrarPallet").execute();
                    }
                    else if(TipoReg.equals("MultiplesPartidas"))
                    {
                        Log.i("TipoReg","MultiplesPartidas");
                        new SurtidoProdPallet.SegundoPlano("RegistrarPalletMultPart").execute();
                    }
                    else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,"Consulte la tarima antes de registrar." ,false ,true , true);
                        return false;
                    }

                    new esconderTeclado(SurtidoProdPallet.this);
                    return true;
                }
                return false;
            }
        });




        edtx_Pallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Pallet.getText().toString().equals(""))
                    {
                        new SurtidoProdPallet.SegundoPlano("ConsultaPallet").execute();
                    }else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet) ,"false" ,true , true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Pallet.setText("");
                                edtx_Pallet.requestFocus();

                            }
                        });
                    }
                    new esconderTeclado(SurtidoProdPallet.this);
                    return  true;
                }
                return false;
            }
        });
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
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {


            int id = item.getItemId();

            if (progressBarHelper.ispBarActiva()) {
                if ((id == R.id.InformacionDispositivo))
                {
                    new sobreDispositivo(contexto, null);
                }
                if ((id == R.id.recargar))
                {

                    Recarga();
                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
        return super.onOptionsItemSelected(item);
    }
    private void ReiniciarVariables(String tarea)
    {
        switch (tarea)
        {
            case "ConsultaOrdenProduccion":
                edtx_ConfirmarPallet.setText("");
                edtx_ConfirmarPallet.requestFocus();

                break;
            case "ConsultarTarima":
                edtx_Pallet.setText("");
                edtx_Pallet.requestFocus();
                break;

            case "SugierePallet":
                edtx_Pallet.setText("");
                txtv_SugPallet.setText("");
                txtv_SugEmpaque.setText("");
                txtv_SugLote.setText("");
                txtv_SugPosicion.setText("");
                break;
            case "ConsultaPallet":
                edtx_Pallet.setText("");
                tabla.getDataAdapter().clear();
                tabla.getDataAdapter().notifyDataSetChanged();
                edtx_Pallet.requestFocus();
                break;
            case "RegistrarPallet":
            case "RegistrarPalletMultPart":
                tabla.getDataAdapter().clear();
                tabla.getDataAdapter().notifyDataSetChanged();
                edtx_Pallet.setText("");
                edtx_ConfirmarPallet.setText("");
                edtx_Pallet.requestFocus();
                break;
            case "Reiniciar":
                edtx_Pallet.setText("");
                edtx_ConfirmarPallet.setText("");
                edtx_Pallet.requestFocus();
                break;
        }
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {

    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_RegistroPT cadEmb = new cAccesoADatos_RegistroPT(SurtidoProdPallet.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try
            {
                progressBarHelper.ActivarProgressBar(tarea);
            }catch (Exception e)
            {
                e.printStackTrace();
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {

                switch (tarea)
                {
                    case"ConsultaPedidoSurtido":
                        dao = cadEmb.cad_ListarPartidasProdSpinner(Pedido);
                        break;
                    case "ConsultaPedidoDet":

                        if(params[0]!="@")
                        {
                            Partida = params[0];
                        }

                        dao = cadEmb.cad_ConsultaSurtidoProdDetPartida(Pedido,Partida);
                        break;

                    case "SugierePallet":
                        dao = cadEmb.cad_ConsultaPalletSugeridoProd(Pedido,Partida);
                        break;

                    case "ConsultaPallet":
                        dao = cadEmb.cad_ConsultaPalletSurtidoProd(Pedido,Partida,edtx_Pallet.getText().toString());
                        break;

                    case "RegistrarPallet":
                        dao = cadEmb.cad_RegistroPalletSurtidoProd(Pedido,Partida,edtx_ConfirmarPallet.getText().toString(), Carrito);
                        break;
                    case "RegistrarPalletMultPart":
                        dao = cadEmb.cad_RegistroPalletSurtidoProdMultPart(Pedido,edtx_ConfirmarPallet.getText().toString());
                        break;
                    default:
                        dao = new DataAccessObject();
                }

            }catch (Exception e)
            {
                dao = new DataAccessObject(e);
                e.printStackTrace();
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
                        case"ConsultaPedidoSurtido":

                            if(dao.getcTablas() != null)
                            {
                                sp_partidas.setAdapter(new CustomArrayAdapter(SurtidoProdPallet.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("Partida","Partida")));

                                sp_partidas.setSelection(0);
                                //sp_partidas.setSelection(CustomArrayAdapter.getIndex(sp_partidas,Partida));
                            }else
                            {
                                sp_partidas.setAdapter(null);
                            }
                            break;
                        case "ConsultaPedidoDet":
                            txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));
                            txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            new SurtidoProdPallet.SegundoPlano("SugierePallet").execute();
                            break;

                        case "SugierePallet":

                            txtv_SugPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_SugLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                            txtv_SugPosicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                            txtv_SugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_TipoPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("TipoReg"));


                            edtx_Pallet.requestFocus();
                            break;

                        case "ConsultaPallet":
                            if(dao.getcTablaUnica()!=null)
                            {
                                if(ConfigTabla == null)
                                {
                                    ConfigTabla =  new TableViewDataConfigurator(tabla, dao, SurtidoProdPallet.this);
                                }else
                                {
                                    ConfigTabla.CargarDatosTabla(dao);
                                }
                            }
                            else
                            {
                                tabla.getDataAdapter().clear();
                                tabla.getDataAdapter().notifyDataSetChanged();
                            }
                            TipoReg = dao.getcMensaje();// MultiplesPartidas o PartidaUnica
                            edtx_ConfirmarPallet.requestFocus();
                            break;

                        case "RegistrarPalletMultPart":
                        case "RegistrarPallet":
                            ReiniciarVariables(tarea);
                            new SurtidoProdPallet.SegundoPlano("ConsultaPedidoDet").execute("@");

                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Pallet.requestFocus();
                                }
                            },10);

                            break;
                    }
                }
                else
                {
                    ReiniciarVariables(tarea);
                    if(dao.getcMensaje().contains("partida no válido"))
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), "Partida completada con exíto.", true, true, true);
                        new SurtidoProdPallet.SegundoPlano("ConsultaPedidoSurtido").execute();
                    }
                    else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), false, true, true);

                    }
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), false, true, true);
                e.printStackTrace();
            }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
    }
}