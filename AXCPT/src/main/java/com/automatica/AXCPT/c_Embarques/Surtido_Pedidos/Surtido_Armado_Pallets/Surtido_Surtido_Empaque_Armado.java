package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
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

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Surtido_Surtido_Empaque_Armado extends AppCompatActivity  implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar
{
    //region variables
    private Toolbar toolbar;
    private EditText edtx_Empaque, edtx_ConfirmarEmpaque;
    private TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg, txtv_SugPallet, txtv_SugLote, txtv_SugPosicion, txtv_ConsProd, txtv_ConsLote, txtv_ConsCant, txtv_ConsEstatus, txtv_SugEmpaque, txtv_PalletAbierto,txtv_PalletAbierto2,txtv_ConsUM;
    private String RegEmpPalletAbierto;
    private ProgressBarHelper progressBarHelper;
    private Context contexto = this;
    private ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    private String Pedido, Partida, NumParte, CantidadPendiente, CantidadSurtida,Carrito;
    private Handler handler = new Handler();
    private Spinner sp_partidas;
    private Bundle b;
    private SortableTableView tabla_Salidas;
    private TableViewDataConfigurator ConfigTabla = null;
    private ActivityHelpers activityHelpers;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_surtido_empaque_armado);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Surtido_Empaque_Armado.this);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();
        edtx_Empaque.requestFocus();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.CERRAR_CARRITO);
        super.onPostCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        Recarga();

        super.onResume();
    }

    private void Recarga()
    {
        if(!txtv_Pedido.getText().toString().equals("-")) {


            if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
            {

                new SegundoPlano("ConsultaPedidoSurtido").execute("@");

            } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
            {

                    new SegundoPlano("ConsultaPalletAbierto").execute();
            }
        }
    }

    private void declararVariables()
    {
        try
            {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido));
            getSupportActionBar().setSubtitle("Paquete");
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
            txtv_SugPallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_SugLote = (TextView) findViewById(R.id.txtv_Lote);
            txtv_SugPosicion = (TextView) findViewById(R.id.txtv_Posicion);

            txtv_ConsProd = (TextView) findViewById(R.id.txtv_Empaque_Producto);
            txtv_ConsLote = (TextView) findViewById(R.id.txtv_Empaque_Lote);
            txtv_ConsCant = (TextView) findViewById(R.id.txtv_Empaque_Cantidad);
            txtv_ConsEstatus = (TextView) findViewById(R.id.txtv_Estatus);
            txtv_ConsUM = (TextView) findViewById(R.id.txtv_Empaque_UM);

            txtv_SugEmpaque = (TextView) findViewById(R.id.txtv_SugEmpaque);
            txtv_PalletAbierto = (TextView) findViewById(R.id.txtv_PalletAbierto);
            txtv_PalletAbierto2 = (TextView) findViewById(R.id.txtv_PalletAbierto2);

            cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_RegistroEmpaque);
            cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

            cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
            cl_TablaRegistro.setVisibility(View.GONE);

            sp_partidas = findViewById(R.id.vw_spinner_partidas).findViewById(R.id.spinner);

            txtv_Pedido.setText(Pedido);
            txtv_Producto.setText(NumParte);
            txtv_CantPend.setText(CantidadPendiente);
            txtv_CantReg.setText(CantidadSurtida);

            txtv_PalletAbierto.setText(Carrito);
            txtv_PalletAbierto2.setText(Carrito);



            activityHelpers = new ActivityHelpers();
            activityHelpers.AgregarMenus(this,R.id.cl,false);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
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
            CantidadPendiente= b.getString("CantidadPendiente");
            CantidadSurtida= b.getString("CantidadSurtida");
            Carrito= b.getString("Carrito");
        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }
    private void agregaListeners()
    {

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
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);
                            return false;
                        }
                        if(!edtx_Empaque.getText().toString().equals(edtx_ConfirmarEmpaque.getText().toString()))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_ConfirmarEmpaque.setText("");
                                    ReiniciarVariables("RegistrarEmpaque");
                                //    edtx_ConfirmarEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,edtx_Empaque ,getString(R.string.empaques_no_coinciden) ,"false" ,true , true);
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
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);
                        }
                    new esconderTeclado(Surtido_Surtido_Empaque_Armado.this);
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
                    new esconderTeclado(Surtido_Surtido_Empaque_Armado.this);
                    return  true;
                }
                return false;
            }
        });

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

        txtv_SugPallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (txtv_SugPallet.getText().toString().equals("-")|| TextUtils.isEmpty(txtv_SugPallet.getText())){
                    new popUpGenerico(contexto,getCurrentFocus(),"Consulta un pallet para continuar",false,true,true);
                    return false;
                }
                String[] datos={txtv_SugPallet.getText().toString()};
                activityHelpers.getTaskbar_axc().abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_PALLET),FragmentoConsulta.TAG);
                return true;
            }
        });
        txtv_SugEmpaque.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (txtv_SugEmpaque.getText().toString().equals("-")|| TextUtils.isEmpty(txtv_SugEmpaque.getText())){
                    new popUpGenerico(contexto,getCurrentFocus(),"Consulta un pallet para continuar",false,true,true);
                    return false;
                }
                String[] datos={txtv_SugEmpaque.getText().toString()};
                activityHelpers.getTaskbar_axc().abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_EMPAQUE),FragmentoConsulta.TAG);
                return true;
            }
        });
        txtv_SugPosicion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (txtv_SugPosicion.getText().toString().equals("-")|| TextUtils.isEmpty(txtv_SugPosicion.getText())){
                    new popUpGenerico(contexto,getCurrentFocus(),"Consulta una posición para continuar",false,true,true);
                    return false;
                }
                String[] datos={txtv_SugPosicion.getText().toString()};
                activityHelpers.getTaskbar_axc().abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_POSICION),FragmentoConsulta.TAG);
                return true;
            }
        });



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
                        new SegundoPlano("ConsultaPalletAbierto").execute();

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
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
                    edtx_Empaque.requestFocus();

                    break;
                case "SugiereEmpaque":
                    txtv_SugEmpaque.setText("");
                    txtv_SugLote.setText("");
                    txtv_SugPallet.setText("");
                    txtv_SugPosicion.setText("");
                    break;
                case "RegistrarEmpaque":
                    txtv_ConsProd.setText("");
                    txtv_ConsEstatus.setText("");
                    txtv_ConsCant.setText("");
                    txtv_ConsLote.setText("");
                    txtv_ConsUM.setText("");

                    edtx_Empaque.setText("");
                    edtx_ConfirmarEmpaque.setText("");
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

    @Override
    public void onTableHeaderClick(int columnIndex, String Header,String IdConfigurador)
    {
        Toast.makeText(contexto, Header, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto,String IdConfigurador)
    {
        new popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado,String IdConfigurador)
    {

    }

    @Override
    public void BotonDerecha()
    {
        if(txtv_PalletAbierto.getText().toString().equals(""))
        {
            new popUpGenerico(contexto,null, getString(R.string.error_cerrar_pallet_sin_empaques) , "false",true , true);
            return;
        }

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

    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

    @Override
    public void BotonIzquierda()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        View LastView;

        DataAccessObject dao;
        cAccesoADatos_Embarques cadEmb = new cAccesoADatos_Embarques(Surtido_Surtido_Empaque_Armado.this);
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
                String tmpCarrito = "@";
                if(!Carrito.equals(""))
                {
                    tmpCarrito = Carrito;
                }

                switch (tarea)
                {
                    case"ConsultaPedidoSurtido":
                        dao = cadEmb.cad_ListarPartidasSpinner(Pedido);
                        break;
                    case "ConsultaPedidoDet":

                        if(params[0]!="@")
                        {
                           Partida = params[0];
                        }

                        dao = cadEmb.cad_ConsultaSurtidoDetPartida(Pedido,Partida);

                        break;

                    case "SugiereEmpaque":
                        dao = cadEmb.cad_ConsultaEmpaqueSugerido(Pedido,Partida);
                        break;

                    case "ConsultaEmpaque":
                        dao = cadEmb.cad_ConsultaEmpaqueSurtido(Pedido,Partida,edtx_Empaque.getText().toString());
                        break;

                    case "RegistrarEmpaque":

                            dao = cadEmb.cad_RegistroEmpaqueSurtido(Pedido,Partida,edtx_Empaque.getText().toString(),tmpCarrito);

                        break;

                    case "ConsultaPalletAbierto":


                            dao = cadEmb.cad_ConsultaTarimaConsolidada(Pedido,tmpCarrito);

                        break;

                    case "CerrarTarima":
                        dao = cadEmb.cad_CierraPalletSurtido(txtv_PalletAbierto.getText().toString());
                        break;

                    default:
                        dao = new DataAccessObject(false,tarea,null);
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
                    if(LastView!=null)
                    {
                        LastView.requestFocus();
                    }

                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {
                                    case"ConsultaPedidoSurtido":
                                        if(dao.getcTablas() != null)
                                        {
                                            CustomArrayAdapter c;
                                            sp_partidas.setAdapter(c = new CustomArrayAdapter(Surtido_Surtido_Empaque_Armado.this,
                                                    android.R.layout.simple_spinner_item,
                                                    dao.getcTablasSorteadas("Partida","Partida")));

                                           // sp_partidas.setSelection(0);
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

                                        txtv_SugPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_SugLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                        txtv_SugPosicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                                        txtv_SugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoEmpaque"));


                                            new SegundoPlano("ConsultaPalletAbierto").execute();

                                        break;

                                    case "ConsultaEmpaque":

                                        txtv_ConsProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Producto"));
                                        txtv_ConsLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                        txtv_ConsCant.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_ConsEstatus.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_ConsUM.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));

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
                                                                                    "En proceso","-",tabla_Salidas, dao, Surtido_Surtido_Empaque_Armado.this);
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

//                                        txtv_PalletAbierto.setText(RegEmpPalletAbierto);
//                                        txtv_PalletAbierto2.setText(RegEmpPalletAbierto);
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
//                                        new SegundoPlano("PalletsArmadoPedido").execute();

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

                                }
                        }
                   else
                        {
                            ReiniciarVariables(tarea);
                            if(dao.getcMensaje().contains("SURTIDA")||dao.getcMensaje().contains("Estatus de partida no "))
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), "Partida completada con exíto.", "true", true, true);
                                    new SegundoPlano("ConsultaPedidoSurtido").execute();
                                }
                            else
                                {
                                    new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), false, true, true);
                                }
                        }
                }catch (Exception e)
                {
                    new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), "false", true, true);
                    e.printStackTrace();
                }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
    }
}
