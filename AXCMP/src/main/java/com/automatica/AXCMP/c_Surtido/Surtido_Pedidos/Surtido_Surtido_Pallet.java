package com.automatica.AXCMP.c_Surtido.Surtido_Pedidos;

import android.content.Context;
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
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class Surtido_Surtido_Pallet extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_ConfirmarEmpaque;
    SortableTableView tabla_Salidas;
    Button btn_CerrarTarima,btn_CancelarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    String PalletSeleccionadoTabla;

    TextView txtv_Pedido,txtv_Producto,txtv_CantPend,txtv_CantReg,txtv_SugPallet,txtv_SugLote,txtv_SugPosicion,txtv_ConsProd,txtv_ConsLote,txtv_ConsCant,txtv_ConsCantEmp,txtv_ConsEstatus,txtv_CantEmpReg,txtv_SugEmpaque;
    String Pedido,Partida,NumParte,UM,CantidadTotal,CantidadPendiente,CantidadSurtida,Linea,CodigoPallet,SugPallet,SugLote,SugPosicion;
    ConstraintLayout cl_TablaRegistro,cl_EmpaqueRegistro;
    String ConsProd,ConsLote,ConsCantidad,ConsEmpaques,ConsEstatus;

    Bundle b;

    ArrayList<datosTabla> arrayDatosTabla;
    Handler handler = new Handler();
    String[] HEADER = {"Empaque","Producto_Empaque","Cantidad"};
    String[][] DATA_TO_SHOW;
    int palletRegistradosVar = 0;
    int cantInt =0;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st;

    Boolean seleccionado,ReiniciarTabla= false;
    String LoteOP,ProductoOP,CantidadOP, FechaCadOP;

    String CantidadRegOP;
    boolean recargar;
    class datosTabla
    {
        String Producto, Cantidad, Empaque;

        public datosTabla(String producto, String cantidad, String empaque)
        {
            Producto = producto;
            Cantidad = cantidad;
            Empaque = empaque;
        }

        public String getProducto()
        {
            return Producto;
        }
        public String getCantidad() {
            return Cantidad;
        }
        public String getEmpaque() {
            return Empaque;
        }
    }
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_surtido_pallet);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Surtido_Pallet.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();
    }
    @Override
    protected void onResume()
    {
        if(!txtv_Pedido.getText().toString().equals("-"))
        {
            SegundoPlano sp = new SegundoPlano("ConsultaPedidoDet");
            sp.execute();
            sp = new SegundoPlano("SugerenciaPallet");
            sp.execute();
        }
        super.onResume();
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
            Linea= b.getString("lINEA");

            Log.e("SoapResponse", "SacaExtrasIntent: ");



        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido));
        getSupportActionBar().setSubtitle("Pallet");
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Empaque= (EditText) findViewById(R.id.edtx_Empaque);
        edtx_ConfirmarEmpaque = (EditText) findViewById(R.id.edtx_ConfirmarEmpaque);

        tabla_Salidas= (SortableTableView) findViewById(R.id.tableView_Salida);

        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_ConfirmarEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtv_Pedido =(TextView) findViewById(R.id.txtv_Pedido);
        txtv_Producto =(TextView) findViewById(R.id.txtv_Producto);
        txtv_CantPend =(TextView) findViewById(R.id.txtv_Cantidad);
        txtv_CantReg =(TextView) findViewById(R.id.txtv_CantidadReg);
        txtv_SugPallet =(TextView) findViewById(R.id.txtv_Pallet);
        txtv_SugLote =(TextView) findViewById(R.id.txtv_Lote);
        txtv_SugPosicion =(TextView) findViewById(R.id.txtv_Posicion);
        txtv_ConsProd =(TextView) findViewById(R.id.txtv_Pallet_Producto);
        txtv_ConsLote =(TextView) findViewById(R.id.txtv_Pallet_Lote);
        txtv_ConsCant =(TextView) findViewById(R.id.txtv_Pallet_Cantidad);
        txtv_ConsEstatus =(TextView) findViewById(R.id.txtv_Estatus);
        txtv_ConsCantEmp=(TextView) findViewById(R.id.txtv_Pallet_Cantidad_Empaques);


        txtv_CantEmpReg =(TextView) findViewById(R.id.txtv_CantidadEmpaquesRegistrados);
        txtv_SugEmpaque =(TextView) findViewById(R.id.txtv_SugEmpaque);

        cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_RegistroEmpaque);
        cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

        cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
        cl_TablaRegistro.setVisibility(View.GONE);

        btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btn_CancelarTarima = (Button) findViewById(R.id.btn_CancelarTarima);

        txtv_Pedido.setText(Pedido);
        txtv_Producto.setText(NumParte);
        txtv_CantPend.setText(CantidadPendiente);
        txtv_CantReg.setText(CantidadSurtida);

        // edtx_ConfirmarEmpaque.requestFocus();
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
                    if(!edtx_ConfirmarEmpaque.getText().toString().equals(edtx_Empaque.getText().toString()))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Empaque.setText("");
                                edtx_ConfirmarEmpaque.setText("");
                                edtx_Empaque.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.pallets_no_coinciden) ,"false" ,true , true);

                    }

                        if(!edtx_ConfirmarEmpaque.getText().toString().equals(""))
                         {
                        SegundoPlano sp = new SegundoPlano("RegistrarPallet");
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
                                new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet) ,"false" ,true , true);
                            }
                    new esconderTeclado(Surtido_Surtido_Pallet.this);
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
                        SegundoPlano sp = new SegundoPlano("ConsultarPallet");
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
                    new esconderTeclado(Surtido_Surtido_Pallet.this);
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
        int id = item.getItemId();
        try
        {
            if(!recargar)
                {
                    if ((id == R.id.InformacionDispositivo))
                        {
                            new sobreDispositivo(contexto, vista);
                        }
                    if ((id == R.id.recargar))
                        {
                            if(!txtv_Pedido.getText().toString().equals("-"))
                            {
                                SegundoPlano sp = new SegundoPlano("SugerenciaPallet");
                                sp.execute();
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

                    tabla_Salidas.getDataAdapter().clear();

                    break;
                case "ConsultarTarima":
                    tabla_Salidas.getDataAdapter().clear();
                    edtx_Empaque.setText("");

                    edtx_Empaque.requestFocus();
                    break;
                case "RegistrarEmpaque":
                    edtx_Empaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "Reiniciar":

                    tabla_Salidas.getDataAdapter().clear();

                    break;
            }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        View LastView;
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
            palletRegistradosVar=0;

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
                        sa.SOAPConsultaPedidoDetPartida(Pedido,Partida, contexto);
                        break;

                    case "SugerenciaPallet":
                       // Thread.sleep(5000);
                        sa.SOAPSugierePalletSurtido(Pedido,Partida, contexto);
                        break;
                    case "ConsultarPallet":
                        sa.SOAPConsultaPalletSurtido(Pedido,Partida,edtx_Empaque.getText().toString(),contexto);
                        break;
                    case "RegistrarPallet":

                        sa.SOAPRegistraPalletSurtido(Pedido,Partida,edtx_ConfirmarEmpaque.getText().toString(),contexto );
                        break;



                }
                mensaje = sa.getMensaje();
                decision = sa.getDecision();

                if (decision.equals("true"))
                {
                    sacaDatos(tarea);
                    int i = 0;
                    DATA_TO_SHOW = new String[arrayDatosTabla.size()][1];
                    for (datosTabla a : arrayDatosTabla)
                    {
                        DATA_TO_SHOW[i][0] = a.getEmpaque();
                        i++;
                    }

                }
            }catch (Exception e)
            {
                mensaje = e.getMessage();
                decision = "false";
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

                    SegundoPlano sp;
                    if (decision.equals("true"))
                        {
                            switch (tarea) {
                                case "ConsultaPedidoDet":

                                    txtv_CantReg.setText(CantidadSurtida);
                                    txtv_CantPend.setText(CantidadPendiente);
                                    break;

                                case "SugerenciaPallet":

                                    txtv_SugPallet.setText(SugPallet);
                                    txtv_SugLote.setText(SugLote);
                                    txtv_SugPosicion.setText(SugPosicion);

                                    break;

                                case "ConsultarPallet":


                                    txtv_ConsProd.setText(ConsProd);
                                    txtv_ConsCant.setText(ConsCantidad);
                                    txtv_ConsLote.setText(ConsLote);
                                    txtv_ConsEstatus.setText(ConsEstatus);
                                    txtv_ConsCantEmp.setText(ConsEmpaques);


                                        edtx_ConfirmarEmpaque.requestFocus();
                                    break;


                                case "RegistrarPallet":
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_registrado_exito), decision, true, true);

                                  /*  if(!txtv_Pedido.getText().toString().equals("-"))
                                    {
                                  */
                                        sp = new SegundoPlano("ConsultaPedidoDet");
                                        sp.execute();
                                        sp = new SegundoPlano("SugerenciaPallet");
                                        sp.execute();
                                    //}
                                    edtx_Empaque.requestFocus();
                                    edtx_Empaque.setText("");
                                    edtx_ConfirmarEmpaque.setText("");

                                    txtv_ConsCantEmp.setText("");
                                    txtv_ConsCant.setText("");
                                    txtv_ConsEstatus.setText("");
                                    txtv_ConsLote.setText("");
                                    txtv_ConsProd.setText("");
                                    break;



                                }
                        }

                    if (decision.equals("false"))
                        {

                            ReiniciarVariables(tarea);
                            new popUpGenerico(contexto,getCurrentFocus(), mensaje, "false", true, true);
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
    private void sacaDatos(String tarea)
    {
        try {
            SoapObject tabla = sa.parser();
            String Empaque;

            arrayDatosTabla = null;
            arrayDatosTabla = new ArrayList<>();
            datosTabla d;
            if (tabla != null)
                for (int i = 0; i < tabla.getPropertyCount(); i++)
                {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    switch(tarea)
                    {
                        case "ConsultaPedidoDet":
                            CantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                            CantidadSurtida = tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                            break;

                        case "SugerenciaPallet":
                            SugLote= tabla1.getPrimitivePropertyAsString("Lote");
                            SugPallet= tabla1.getPrimitivePropertyAsString("CodigoPallet");
                            SugPosicion= tabla1.getPrimitivePropertyAsString("CodigoPosicion");

                            break;
                        case "ConsultarPallet":


                            Log.d("SoapResponse", tabla1.toString());

                            ConsProd= tabla1.getPrimitivePropertyAsString("Producto");
                            ConsLote= tabla1.getPrimitivePropertyAsString("Lote");
                            ConsCantidad= tabla1.getPrimitivePropertyAsString("CantidadActual");
                            ConsEmpaques= tabla1.getPrimitivePropertyAsString("Empaques");
                            ConsEstatus= tabla1.getPrimitivePropertyAsString("UM");

                            break;
                        case "RegistrarEmpaque":

                            Log.d("SoapResponse", tabla1.toString());

                            Empaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");

                            d = new datosTabla("", "", Empaque);
                            arrayDatosTabla.add(d);
                            break;

                    }



                }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
