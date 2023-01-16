package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Surtido_Surtido_Picking_NE_Armado extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface
{
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_ConfirmarEmpaque;
    TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg, txtv_SugLote, txtv_SugPosicion, txtv_ConsProd, txtv_ConsLote, txtv_ConsCant, txtv_ConsEstatus, txtv_SugEmpaque, txtv_PalletAbierto,txtv_ConsUM,txtv_CantXEmp;
    String RegEmpPalletAbierto;
    SortableTableView tabla_Salidas;
    Button btn_CerrarTarima;
    ProgressBarHelper progressBarHelper;
    Context contexto = this;
    String Pedido, Partida, NumParte, UM, CantidadTotal, CantidadPendiente, CantidadSurtida, Linea;
    ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    Handler handler = new Handler();
    Bundle b;
    TableViewDataConfigurator ConfigTabla = null;
    int registroAnteriorSpinner=0;
    Spinner sp_ArmadoPallets,sp_partidas;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_surtido_picking_ne_armado);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, this);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();

        edtx_Empaque.requestFocus();

        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));

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
            if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
            {
                new SegundoPlano("ConsultaPedidoSurtido").execute("@");

            } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
            {
                if(sp_ArmadoPallets.getSelectedItem() == null)
                {
                    new SegundoPlano("ConsultaPalletAbierto").execute();
                }
                else
                {
                    new SegundoPlano("PalletsArmadoPedido").execute();
                }

            }
        }
    }

    private void declararVariables()
    {
        try {


            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido));
            getSupportActionBar().setSubtitle("Paquete NE");
            progressBarHelper = new ProgressBarHelper(this);
            edtx_Empaque = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_ConfirmarEmpaque = (EditText) findViewById(R.id.edtx_ConfirmarEmpaque);

            tabla_Salidas = (SortableTableView) findViewById(R.id.tableView_OC);
            edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_ConfirmarEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            txtv_Pedido = (TextView) findViewById(R.id.txtv_Pedido);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_CantPend = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_CantReg = (TextView) findViewById(R.id.txtv_CantidadReg);
            txtv_SugLote = (TextView) findViewById(R.id.txtv_Lote);
            txtv_SugPosicion = (TextView) findViewById(R.id.txtv_Posicion);

            txtv_ConsProd = (TextView) findViewById(R.id.txtv_Empaque_Producto);
            txtv_ConsLote = (TextView) findViewById(R.id.txtv_Empaque_Lote);
            txtv_ConsCant = (TextView) findViewById(R.id.txtv_Empaque_Cantidad);
            txtv_ConsEstatus = (TextView) findViewById(R.id.txtv_Estatus);
            txtv_ConsUM = (TextView) findViewById(R.id.txtv_Empaque_UM);
            txtv_SugEmpaque = (TextView) findViewById(R.id.txtv_SugEmpaque);
            txtv_PalletAbierto = (TextView) findViewById(R.id.txtv_PalletAbierto);
            txtv_CantXEmp = (TextView) findViewById(R.id.txtv_CantXEmp);

            cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_RegistroEmpaque);
            cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

            cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
            cl_TablaRegistro.setVisibility(View.GONE);
            btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);

            sp_ArmadoPallets = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);
            sp_partidas = findViewById(R.id.vw_spinner_partidas).findViewById(R.id.spinner);


            txtv_Pedido.setText(Pedido);
            txtv_Producto.setText(NumParte);
            txtv_CantPend.setText(CantidadPendiente);
            txtv_CantReg.setText(CantidadSurtida);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
            UM= b.getString("UM");
            CantidadTotal= b.getString("CantidadTotal");
            CantidadPendiente= b.getString("CantidadPendiente");
            CantidadSurtida= b.getString("CantidadSurtida");
            Linea= b.getString("Linea");

            Log.e("SoapResponse", "SacaExtrasIntent: ");



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
                new SegundoPlano("ConsultaPedidoDet").execute(((Constructor_Dato) sp_partidas.getSelectedItem()).getDato());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        sp_ArmadoPallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                new SegundoPlano("ConsultaPalletAbierto").execute();
                txtv_PalletAbierto.setText(((Constructor_Dato) sp_ArmadoPallets.getSelectedItem()).getDato());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        edtx_ConfirmarEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_ConfirmarEmpaque.getText().toString().equals(""))
                    {
                        if(edtx_Empaque.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Empaque.setText("");
                                    edtx_Empaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_cantidad) ,"false" ,true , true);
                            return false;
                        }
                        new SegundoPlano("RegistrarEmpaque").execute();
                    }else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_ConfirmarEmpaque.setText("");
                                edtx_ConfirmarEmpaque.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_cantidad) ,"false" ,true , true);
                    }
                    new esconderTeclado(Surtido_Surtido_Picking_NE_Armado.this);
                    return true;
                }
                return false;
            }
        });




        edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Empaque.getText().toString().equals(""))
                    {
                        new SegundoPlano("ConsultaEmpaque").execute();
                    }else
                    {
                        new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Empaque.setText("");
                                edtx_Empaque.requestFocus();

                            }
                        });
                    }
                    new esconderTeclado(Surtido_Surtido_Picking_NE_Armado.this);
                    return  true;
                }
                return false;
            }
        });


        btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!txtv_PalletAbierto.getText().toString().equals(""))
                {
                    new CreaDialogos(getString(R.string.pregunta_cierre_pallet),
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    new SegundoPlano("CerrarTarima").execute();
                                }
                            },null,contexto);

                }
                else
                {
                    //     new popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , "false",true , true);
                }
            }
        });


    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header,String IdConfigurador)
    {
        Toast.makeText(contexto, Header, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData,String MensajeCompleto,String IdConfigurador)
    {
        new popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);
    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData,boolean Seleccionado,String IdConfigurador)
    {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.change_view_toolbar, menu);
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

            if (progressBarHelper.ispBarActiva())
            {
                if ((id == R.id.InformacionDispositivo))
                {
                    new sobreDispositivo(contexto, null);
                }
                if ((id == R.id.recargar))
                {
                    Recarga();
                }
                if ((id == R.id.CambiarVista))
                {
                    if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
                    {
                        cl_EmpaqueRegistro.setVisibility(View.GONE);
                        cl_TablaRegistro.setVisibility(View.VISIBLE);
                        item.setIcon(R.drawable.ic_add_box);
                        item.setChecked(true);
                        if(sp_ArmadoPallets.getSelectedItem() == null)
                        {
                            new SegundoPlano("ConsultaPalletAbierto").execute();
                        }
                        else
                        {
                            new SegundoPlano("PalletsArmadoPedido").execute();
                        }
                    } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
                    {
                        cl_TablaRegistro.setVisibility(View.GONE);
                        cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
                        item.setIcon(R.drawable.ic_change_view);
                        new SegundoPlano("ConsultaPedidoDet").execute("@");
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }
        return super.onOptionsItemSelected(item);
    }
    private void ReiniciarVariables(String tarea)
    {
        switch (tarea)
        {
            case "ConsultaOrdenProduccion":
                edtx_ConfirmarEmpaque.setText("");
                tabla_Salidas.getDataAdapter().clear();
                edtx_ConfirmarEmpaque.requestFocus();

                break;
            case "ConsultarTarima":
                tabla_Salidas.getDataAdapter().clear();
                edtx_Empaque.setText("");
                edtx_Empaque.requestFocus();
                break;
            case "ConsultaEmpaque":
                edtx_Empaque.setText("");
                txtv_ConsProd.setText("");
                txtv_ConsCant.setText("");
                txtv_ConsEstatus.setText("");
                txtv_ConsUM.setText("");
                txtv_ConsLote.setText("");
                txtv_CantXEmp.setText("");
                edtx_Empaque.requestFocus();
                break;
            case "RegistrarEmpaque":
                txtv_ConsProd.setText("");
                txtv_ConsEstatus.setText("");
                txtv_ConsCant.setText("");
                txtv_ConsLote.setText("");
                txtv_ConsUM.setText("");
                edtx_Empaque.setText("");
                edtx_ConfirmarEmpaque.setText("");
                txtv_CantXEmp.setText("");

                edtx_Empaque.requestFocus();
                break;
            case "Reiniciar":
                edtx_Empaque.setText("");
                edtx_ConfirmarEmpaque.setText("");
                tabla_Salidas.getDataAdapter().clear();
                edtx_Empaque.requestFocus();
                break;
        }
    }
    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Embarques cadEmb = new cAccesoADatos_Embarques(Surtido_Surtido_Picking_NE_Armado.this);
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
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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
                        dao = cadEmb.cad_ConsultaPedidoSurtido(Pedido);
                        break;
                    case "ConsultaPedidoDet":

                        if(params[0]!="@")
                        {
                            Partida = params[0];
                        }

                        dao = cadEmb.cad_ConsultaSurtidoDetPartida(Pedido,Partida);
                        break;

                    case "SugiereEmpaque":
                        dao = cadEmb.cad_ConsultaPisoSugerido(Pedido,Partida);
                        break;

                    case "ConsultaEmpaque":
                        dao = cadEmb.cad_ConsultaPisoSurtido(Pedido,Partida,edtx_Empaque.getText().toString());
                        break;


                    case "RegistrarEmpaque":

                        if(sp_ArmadoPallets.getSelectedItem() == null)
                        {
                            dao = cadEmb.cad_RegistroPisoSurtido(Pedido,Partida,edtx_Empaque.getText().toString(),edtx_ConfirmarEmpaque.getText().toString(),"@");
                        }
                        else
                        {
                            dao = cadEmb.cad_RegistroPisoSurtido(Pedido,Partida,edtx_Empaque.getText().toString(),edtx_ConfirmarEmpaque.getText().toString(),((Constructor_Dato)sp_ArmadoPallets.getSelectedItem()).getTag1());
                        }
                        break;

                    case "PalletsArmadoPedido":
                        dao= cadEmb.c_PalletsArmadoPedido(Pedido);
                        break;

                    case "ConsultaPalletAbierto":

                        if(sp_ArmadoPallets.getSelectedItem() == null)
                        {
                            dao = cadEmb.cad_ConsultaTarimaConsolidada_NE(Pedido,"@");

                        }
                        else
                        {
                            dao = cadEmb.cad_ConsultaTarimaConsolidada_NE(Pedido,((Constructor_Dato)sp_ArmadoPallets.getSelectedItem()).getTag1());
                        }
                        break;

                    case "CerrarTarima":
                        dao = cadEmb.cad_CierraPalletSurtido(RegEmpPalletAbierto);
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
                                sp_partidas.setAdapter(new CustomArrayAdapter(Surtido_Surtido_Picking_NE_Armado.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("Partida","Partida")));

                                sp_partidas.setSelection(CustomArrayAdapter.getIndex(sp_partidas,Partida));
                            }else
                            {
                                sp_partidas.setAdapter(null);
                            }
                            break;

                        case "ConsultaPedidoDet":
                            txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));
                            txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            new SegundoPlano("SugiereEmpaque").execute();
                            break;

                        case "SugiereEmpaque":
                            txtv_SugLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                            txtv_SugPosicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                            txtv_SugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            new SegundoPlano("PalletsArmadoPedido").execute();

                            break;

                        case "ConsultaEmpaque":

                            txtv_ConsProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            txtv_ConsLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                            txtv_ConsCant.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            txtv_ConsEstatus.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_ConsUM.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));
                            txtv_CantXEmp.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadXEmpaque"));

                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run() {
                                    edtx_ConfirmarEmpaque.requestFocus();

                                }
                            },1);
                            break;

                        case "ConsultaPalletAbierto":

                            if(dao.getcTablaUnica()!=null)
                            {
                                if(ConfigTabla == null)
                                {
                                    ConfigTabla =  new TableViewDataConfigurator(2,"Listo",
                                            "En proceso","-",tabla_Salidas, dao, Surtido_Surtido_Picking_NE_Armado.this);
                                }else
                                {
                                    ConfigTabla.CargarDatosTabla(dao);
                                }

                                RegEmpPalletAbierto =  dao.getcMensaje();
                            }
                            else
                            {
                                tabla_Salidas.getDataAdapter().clear();
                                tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                            }

                            txtv_PalletAbierto.setText(RegEmpPalletAbierto);
                            edtx_Empaque.setText("");
                            edtx_Empaque.requestFocus();
                            break;
                        case "RegistrarEmpaque":

                            RegEmpPalletAbierto = dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletAbierto");
                            txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                            txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));

                            ReiniciarVariables(tarea);
                            new SegundoPlano("ConsultaPedidoDet").execute(((Constructor_Dato) sp_partidas.getSelectedItem()).getDato());
                            break;

                        case "CerrarTarima":
                            new popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + RegEmpPalletAbierto + "] con éxito.", dao.iscEstado(), true, true);

                            new SegundoPlano("ConsultaPalletAbierto").execute();
                            break;
                        case "PalletsArmadoPedido":

                            if(dao.getcTablas() != null)
                            {

                                if(sp_ArmadoPallets.getSelectedItem() != null)
                                {
                                    registroAnteriorSpinner = sp_ArmadoPallets.getSelectedItemPosition();
                                }
                                sp_ArmadoPallets.setAdapter(new CustomArrayAdapter(Surtido_Surtido_Picking_NE_Armado.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SpinnerData","NumPallet","Codigo")));
                                sp_ArmadoPallets.setSelection(registroAnteriorSpinner);

                                txtv_PalletAbierto.setText(dao.getcTablasSorteadas("SpinnerData","NumPallet","Codigo").get(registroAnteriorSpinner).getTag2());
                            }else
                            {
                                sp_ArmadoPallets.setAdapter(null);
                            }
                            break;
                    }
                }
                else
                {
                    ReiniciarVariables(tarea);
                    if(dao.getcMensaje().contains("SURTIDA"))
                    {
                        new popUpGenerico(contexto, null, "Partida completada con éxito.", "true", true, true);
                    }
                    else
                    {
                        new popUpGenerico(contexto, null, dao.getcMensaje(), "false", true, true);

                    }
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto,null, e.getMessage(), "false", true, true);
                e.printStackTrace();
            }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
    }
}