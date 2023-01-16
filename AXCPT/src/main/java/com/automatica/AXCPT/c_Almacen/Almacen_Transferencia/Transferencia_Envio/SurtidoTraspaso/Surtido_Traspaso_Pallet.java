package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.OLD.Almacen_Transferencia_Surtir_Pallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen.Recepcion_Registro_Transferencia_NE_SelAlm;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class Surtido_Traspaso_Pallet extends AppCompatActivity   implements TableViewDataConfigurator.TableClickInterface
{
    Toolbar toolbar;
    EditText edtx_Pallet, edtx_ConfirmarPallet;
    TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg, txtv_SugPallet, txtv_SugLote, txtv_SugPosicion,txtv_TipoPallet,txtv_SugEmpaque;
    ProgressBarHelper progressBarHelper;
    Context contexto = this;
    String Documento, Partida;
    SortableTableView tabla;
    TableViewDataConfigurator ConfigTabla = null;
    Handler handler = new Handler();
    Bundle b;
    Spinner sp_partidas;
    String TipoReg = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traspaso_surtido_pallet);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, this);
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
        if(!txtv_Pedido.getText().toString().equals("-"))
        {
            new SegundoPlano("ConsultaDocumentoSurtido").execute();
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
            txtv_Pedido.setText(Documento);
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
                Documento= b.getString("Transferencia");
                Partida= b.getString("Partida");
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
                new SegundoPlano("ConsultaDocumentoDet").execute(((Constructor_Dato) sp_partidas.getSelectedItem()).getDato());
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
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet) ,"false" ,true , true);
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
                                new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet_confirmacion) ,"false" ,true , true);
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
                                new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.pallets_no_coinciden) ,false ,true , true);
                                return false;
                            }



                        if(TipoReg == null)
                        {
                            new popUpGenerico(contexto,getCurrentFocus() ,"Consulte la tarima antes de registrar." ,false ,true , true);
                            return false;
                        }


                        if(TipoReg.equals("PartidaUnica"))
                        {
                            Log.i("TipoReg","PartidaUnica");
                            new SegundoPlano("RegistrarPallet").execute();
                        }
                        else if(TipoReg.equals("MultiplesPartidas"))
                        {
                            Log.i("TipoReg","MultiplesPartidas");


                            CreaDialogos cd = new CreaDialogos(contexto);


                            String sourceString = "<p>Este pallet contiene multiples productos.</p> <b>¿Surtir por completo?</b>";

                            cd.dialogoDefault("VALIDE LA INFORMACIÓN", Html.fromHtml(sourceString),
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            new SegundoPlano("RegistrarPalletMultPart").execute();
                                            new esconderTeclado(Surtido_Traspaso_Pallet.this);
                                        }
                                    }, null);
                        }
                        else
                        {
                            new popUpGenerico(contexto,getCurrentFocus() ,"Consulte la tarima antes de registrar." ,false ,true , true);
                            return false;
                        }

                        new esconderTeclado(Surtido_Traspaso_Pallet.this);
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
                                new SegundoPlano("ConsultaPallet").execute();
                            }else
                            {
                                new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet) ,"false" ,true , true);

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
                        new esconderTeclado(Surtido_Traspaso_Pallet.this);
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

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Surtido_Traspaso_Pallet.this);
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
                            case"ConsultaDocumentoSurtido":
                                dao = cad.cad_ListarPartidasRecepcionTraspaso(Documento);
                                break;
                            case "ConsultaDocumentoDet":

                                if(params[0]!="@")
                                {
                                    Partida = params[0];
                                }
                                dao = cad.cad_DetallePartidaTraspaso(Documento,Partida);
                                break;

                            case "SugierePallet":
                                dao = cad.cad_ConsultaPalletSugeridoTraspasoEnvio(Documento,Partida);
                                break;

                            case "ConsultaPallet":
                                dao = cad.cad_ConsultaPalletSurtido(Documento,Partida,edtx_Pallet.getText().toString());
                                break;

                            case "RegistrarPallet":
                                dao = cad.cad_RegistroTraspasoPalletEnvio(Documento,Partida,edtx_ConfirmarPallet.getText().toString());

                                break;
                            case "RegistrarPalletMultPart":
                                dao = cad.cad_RegistroPalletTraspasoMultPart(Documento, edtx_ConfirmarPallet.getText().toString());
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
                                    case"ConsultaDocumentoSurtido":
                                        if(dao.getcTablas() != null)
                                        {
                                            sp_partidas.setAdapter(new CustomArrayAdapter(Surtido_Traspaso_Pallet.this,
                                                    android.R.layout.simple_spinner_item,
                                                    dao.getcTablasSorteadas("Partida","Partida")));

                                            sp_partidas.setSelection(CustomArrayAdapter.getIndex(sp_partidas,Partida));
                                        }else
                                        {
                                            sp_partidas.setAdapter(null);
                                        }
                                        break;
                                    case "ConsultaDocumentoDet":
                                        txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));
                                        txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                                        txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Descripcion"));
                                        new SegundoPlano("SugierePallet").execute();
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
                                                ConfigTabla =  new TableViewDataConfigurator(tabla, dao, Surtido_Traspaso_Pallet.this);
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
                                        new SegundoPlano("ConsultaDocumentoDet").execute("@");
                                        break;
                                }
                        }

                    else
                        {

                            ReiniciarVariables(tarea);
                            if(dao.getcMensaje().contains("partida no válido"))
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), "Partida completada con éxito.", "true", true, true);
                                }
                            else
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), "false", true, true);

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
