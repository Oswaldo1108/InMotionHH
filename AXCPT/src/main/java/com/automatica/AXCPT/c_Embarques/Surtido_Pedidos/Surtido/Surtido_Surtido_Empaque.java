package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class Surtido_Surtido_Empaque extends AppCompatActivity  implements TableViewDataConfigurator.TableClickInterface
{
    //region variables
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_ConfirmarEmpaque;
    TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg, txtv_SugPallet, txtv_SugLote, txtv_SugPosicion, txtv_ConsProd, txtv_ConsLote, txtv_ConsCant, txtv_ConsEstatus, txtv_CantEmpReg, txtv_SugEmpaque, txtv_PalletAbierto,txtv_ConsUM;
    String RegEmpPalletAbierto;
    SortableTableView tabla_Salidas;
    Button btn_CerrarTarima;
    ProgressBarHelper progressBarHelper;
    Context contexto = this;
    String Pedido, Partida, NumParte, UM, CantidadTotal, CantidadPendiente, CantidadSurtida, Linea;
    ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    Handler handler = new Handler();
    Bundle b;
    boolean recargar;
    int registroAnteriorSpinner=0;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_surtido_empaque);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Surtido_Empaque.this);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();
        edtx_Empaque.requestFocus();
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

                new SegundoPlano("ConsultaPedidoDet").execute();

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

            tabla_Salidas = (SortableTableView) findViewById(R.id.tableView_Salida);
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

            txtv_CantEmpReg = (TextView) findViewById(R.id.txtv_CantidadEmpaquesRegistrados);
            txtv_SugEmpaque = (TextView) findViewById(R.id.txtv_SugEmpaque);
            txtv_PalletAbierto = (TextView) findViewById(R.id.txtv_PalletAbierto);

            cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_RegistroEmpaque);
            cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

            cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
            cl_TablaRegistro.setVisibility(View.GONE);

            btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);

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
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.empaques_no_coinciden) ,"false" ,true , true);
                            return false;
                        }

                        SegundoPlano sp = new SegundoPlano("RegistrarEmpaque");
                        sp.execute();
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
                    new esconderTeclado(Surtido_Surtido_Empaque.this);
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
                        SegundoPlano sp = new SegundoPlano("ConsultaEmpaque");
                        sp.execute();
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
                    new esconderTeclado(Surtido_Surtido_Empaque.this);
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

            if (!recargar) {
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
//                        if(RegEmpPalletAbierto==null)
//                        {
//                            return false;
//                        }
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

                         new SegundoPlano("ConsultaPedidoDet").execute();

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

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        View LastView;

        DataAccessObject dao;
        cAccesoADatos_Embarques cadEmb = new cAccesoADatos_Embarques(Surtido_Surtido_Empaque.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {

            try
            {

                recargar = true;
                progressBarHelper.ActivarProgressBar();

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }


        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {

                switch (tarea)
                {
                    case "ConsultaPedidoDet":
                        dao = cadEmb.cad_ConsultaSurtidoDetPartida(Pedido,Partida);
                        break;

                    case "SugiereEmpaque":
                        dao = cadEmb.cad_ConsultaEmpaqueSugerido(Pedido,Partida);
                        break;

                    case "ConsultaEmpaque":
                        dao = cadEmb.cad_ConsultaEmpaqueSurtido(Pedido,Partida,edtx_Empaque.getText().toString());
                        break;


                    case "RegistrarEmpaque":
                        dao = cadEmb.cad_RegistroEmpaqueSurtido(Pedido,Partida,edtx_Empaque.getText().toString(),"");
                        break;

                    case "ConsultaPalletAbierto":
                        dao = cadEmb.cad_ConsultaTarimaConsolidada(Pedido);
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

                                    case "ConsultaPedidoDet":
                                        txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));
                                        txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                                       new SegundoPlano("SugiereEmpaque").execute();
                                        break;

                                    case "SugiereEmpaque":

                                        txtv_SugPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_SugLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                        txtv_SugPosicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                                        txtv_SugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoEmpaque"));

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
                                            txtv_CantEmpReg.setText( dao.getcTablaUnica().length);
                                            new TableViewDataConfigurator(tabla_Salidas, dao,Surtido_Surtido_Empaque.this);
                                            RegEmpPalletAbierto =  dao.getcMensaje();
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
                                        new SegundoPlano("ConsultaPedidoDet").execute();
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
                            if(dao.getcMensaje().contains("SURTIDA"))
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
        progressBarHelper.DesactivarProgressBar();
            recargar = false;
        }
    }
}
