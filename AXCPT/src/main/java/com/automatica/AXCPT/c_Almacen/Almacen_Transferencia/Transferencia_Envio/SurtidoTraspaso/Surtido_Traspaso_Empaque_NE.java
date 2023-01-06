package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.ClasesSerializables.DocumentIDs;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class Surtido_Traspaso_Empaque_NE extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface
{
    //region variables
    Toolbar toolbar;
    EditText edtx_Pallet, edtx_CantidadEmpaques;
    TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg,txtv_CantXEmp, txtv_SugPallet, txtv_SugLote, txtv_SugPosicion, txtv_ConsProd, txtv_ConsLote, txtv_ConsCant, txtv_ConsEstatus, txtv_SugEmpaque, txtv_PalletAbierto,txtv_ConsUM;
    String RegEmpPalletAbierto;
    Button btn_CerrarTarima;
    ProgressBarHelper progressBarHelper;
    Context contexto = this;
    String Documento, Partida, NumParte;
    ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    Handler handler = new Handler();
    Spinner sp_partidas;
    Bundle b;
    SortableTableView tabla_Salidas;
    TableViewDataConfigurator ConfigTabla = null;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traspaso_surtido_empaque_ne);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Traspaso_Empaque_NE.this);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();
        edtx_Pallet.requestFocus();
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
            getSupportActionBar().setSubtitle("Empaque No Et.");
            progressBarHelper = new ProgressBarHelper(this);

            edtx_Pallet = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_CantidadEmpaques = (EditText) findViewById(R.id.edtx_ConfirmarEmpaque);

            tabla_Salidas = (SortableTableView) findViewById(R.id.tableView_OC);
            edtx_Pallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CantidadEmpaques.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            txtv_Pedido = (TextView) findViewById(R.id.txtv_Pedido);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_CantPend = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_CantReg = (TextView) findViewById(R.id.txtv_CantidadReg);
            txtv_CantXEmp = (TextView) findViewById(R.id.txtv_CantXEmp);
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

            cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_RegistroEmpaque);
            cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

            cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
            cl_TablaRegistro.setVisibility(View.GONE);

            btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);
            sp_partidas = findViewById(R.id.vw_spinner_partidas).findViewById(R.id.spinner);

            txtv_Pedido.setText(Documento);
            txtv_Producto.setText(NumParte);

            edtx_Pallet.requestFocus();
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
            Documento= b.getString("Transferencia");
            Partida= b.getString("Partida");
            NumParte= b.getString("Producto");
        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }
    private void agregaListeners()
    {

        edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CantidadEmpaques.getText().toString().equals(""))
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
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_cantidad_empaques) ,"false" ,true , true);
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
                                edtx_CantidadEmpaques.setText("");
                                edtx_CantidadEmpaques.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);
                    }
                    new esconderTeclado(Surtido_Traspaso_Empaque_NE.this);
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
                        new SegundoPlano("ConsultaEmpaque").execute();
                    }else
                    {
                        new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CantidadEmpaques.setText("");
                                edtx_CantidadEmpaques.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Surtido_Traspaso_Empaque_NE.this);
                    return false;
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

            if (progressBarHelper.ispBarActiva()) {
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
                edtx_CantidadEmpaques.setText("");
                tabla_Salidas.getDataAdapter().clear();
                edtx_CantidadEmpaques.requestFocus();

                break;
            case "ConsultarTarima":
                tabla_Salidas.getDataAdapter().clear();
                edtx_Pallet.setText("");
                edtx_Pallet.requestFocus();
                break;
            case "ConsultaEmpaque":
                edtx_Pallet.setText("");
                txtv_ConsProd.setText("");
                txtv_ConsCant.setText("");
                txtv_ConsEstatus.setText("");
                txtv_ConsUM.setText("");
                edtx_Pallet.requestFocus();
                txtv_CantXEmp.setText("");

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
                txtv_CantXEmp.setText("");

                edtx_Pallet.setText("");
                edtx_CantidadEmpaques.setText("");
                edtx_Pallet.requestFocus();
                break;
            case "Reiniciar":
                edtx_Pallet.setText("");
                edtx_CantidadEmpaques.setText("");
                tabla_Salidas.getDataAdapter().clear();
                edtx_Pallet.requestFocus();
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

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Surtido_Traspaso_Empaque_NE.this);
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
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
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
                        dao = cad.cad_ListarPartidasRecepcionTraspaso(Documento);
                        break;
                    case "ConsultaPedidoDet":

                        if(params[0]!="@")
                        {
                            Partida = params[0];
                        }

                        dao = cad.cad_DetallePartidaTraspaso(Documento,Partida);

                        break;

                    case "SugiereEmpaque":
                        dao = cad.cad_ConsultaPalletSugeridoTraspasoEnvio_NE(Documento,Partida);
                        break;

                    case "ConsultaEmpaque":
                        dao = cad.c_ConsultarPalletTraspaso_NE(Documento,Partida,edtx_Pallet.getText().toString());
                        break;

                    case "RegistrarEmpaque":
                        dao = cad.cad_RegistroTraspasoEmpaqueEnvio_NE(Documento,Partida,edtx_Pallet.getText().toString(),edtx_CantidadEmpaques.getText().toString());
                        break;

                    case "ConsultaPalletAbierto":
                        dao = cad.cad_ConsultaTarimaArmadoTraspaso_NE(Documento);
                        break;

                    case "CerrarTarima":
                        dao = cad.cad_CierrePalletTraspasoEnvio(Documento);
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
                                sp_partidas.setAdapter(new CustomArrayAdapter(Surtido_Traspaso_Empaque_NE.this,
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
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Descripcion"));
                            new SegundoPlano("SugiereEmpaque").execute();
                            break;

                        case "SugiereEmpaque":

                            txtv_SugPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_SugLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                            txtv_SugPosicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                            txtv_SugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run() {
                                    edtx_Pallet.requestFocus();
                                }
                            },1);
                            break;

                        case "ConsultaEmpaque":

                            txtv_ConsProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                            txtv_ConsLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                            txtv_ConsCant.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            txtv_ConsEstatus.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_ConsUM.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));
                            txtv_CantXEmp.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadXEmpaque"));

                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run() {
                                    edtx_CantidadEmpaques.requestFocus();
                                }
                            },1);
                            break;

                        case "ConsultaPalletAbierto":
                            if(dao.getcTablaUnica()!=null)
                            {
                                if(ConfigTabla == null)
                                {
                                    ConfigTabla =  new TableViewDataConfigurator(tabla_Salidas, dao, Surtido_Traspaso_Empaque_NE.this);
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
                            edtx_Pallet.setText("");
                            edtx_Pallet.requestFocus();

                            break;

                        case "RegistrarEmpaque":
//
//                            RegEmpPalletAbierto = dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletAbierto");
//                            txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
//                            txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));

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
                        new popUpGenerico(contexto, getCurrentFocus(), "Partida completada con éxito.", "true", true, true);
                    }
                    else
                    {
                        new popUpGenerico(contexto,null, dao.getcMensaje(), false, true, true);
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
