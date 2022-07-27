package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Surtido_Surtido_Empaque_NE extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface
{
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_ConfirmarEmpaque;
    TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg, txtv_SugPallet, txtv_SugLote, txtv_SugPosicion, txtv_ConsProd, txtv_ConsLote, txtv_ConsCant, txtv_ConsEstatus, txtv_CantEmpReg, txtv_SugEmpaque, txtv_PalletAbierto,txtv_ConsUM;
    String RegEmpPalletAbierto;
    SortableTableView tabla_Salidas;
    Button btn_CerrarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    String Pedido, Partida, NumParte, UM, CantidadTotal, CantidadPendiente, CantidadSurtida, Linea;
    ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    Handler handler = new Handler();
    String[] HEADER = {"Empaque", "Producto", "Lote", "Cantidad Actual"};
    Bundle b;
    Boolean ReiniciarTabla = false;
    boolean recargar;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_surtido_empaque_ne);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Surtido_Empaque_NE.this);
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


            } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE) {
                new SegundoPlano("ConsultaPalletAbierto").execute();
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
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

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

//            tabla_Salidas.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, HEADER));
            SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
            sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            tabla_Salidas.setHeaderAdapter(sthd);
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
                    new esconderTeclado(Surtido_Surtido_Empaque_NE.this);
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
                    new esconderTeclado(Surtido_Surtido_Empaque_NE.this);
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

            if (!recargar) {
                if ((id == R.id.InformacionDispositivo))
                {
                    new sobreDispositivo(contexto, vista);
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

                        SegundoPlano sp = new SegundoPlano("ConsultaPedidoDet");
                        sp.execute();

                    }

//                    if (!edtx_ConfirmarEmpaque.getText().toString().equals(""))
//                    {
//                        tabla_Salidas.getDataAdapter().clear();
//                        SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
//                        sp.execute();
//                    }
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
                txtv_ConsLote.setText("");
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
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        View LastView;

        DataAccessObject dao;
        cAccesoADatos_Embarques cadEmb = new cAccesoADatos_Embarques(Surtido_Surtido_Empaque_NE.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            try
            {
                LastView = getCurrentFocus();

                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        progressBarHolder.requestFocus();
                        recargar = true;
                    }
                },1);
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
                        dao = cadEmb.cad_ConsultaEmpaqueSugerido_NE(Pedido,Partida);
                        break;

                    case "ConsultaEmpaque":
                        dao = cadEmb.cad_ConsultaPalletSurtido_NE(Pedido,Partida,edtx_Empaque.getText().toString());
                        break;


                    case "RegistrarEmpaque":
                        dao = cadEmb.cad_RegistroEmpaqueSurtido_NE(Pedido,Partida,edtx_Empaque.getText().toString(),edtx_ConfirmarEmpaque.getText().toString(),"@");
                        break;

                    case "ConsultaPalletAbierto":
                        dao = cadEmb.cad_ConsultaTarimaConsolidada_NE(Pedido,"");
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
                            txtv_SugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));

                            break;

                        case "ConsultaEmpaque":

                            txtv_ConsProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            txtv_ConsLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                            txtv_ConsCant.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            txtv_ConsEstatus.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
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

                            new TableViewDataConfigurator(tabla_Salidas, dao,Surtido_Surtido_Empaque_NE.this);

                            String[] datos =  dao.getcMensaje().split("#");


                            if(datos.length >=2)
                                {
                                    RegEmpPalletAbierto = datos[0];
                                    txtv_CantEmpReg.setText(datos[1]);
                                }
                            else
                                {
                                    RegEmpPalletAbierto = "";
                                    txtv_CantEmpReg.setText("");
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
                        new popUpGenerico(contexto, getCurrentFocus(), "Partida completada con exíto.", true, true, true);
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
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = false;
        }
    }
}