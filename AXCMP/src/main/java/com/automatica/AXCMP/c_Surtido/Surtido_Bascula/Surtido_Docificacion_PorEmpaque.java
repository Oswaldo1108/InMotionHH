package com.automatica.AXCMP.c_Surtido.Surtido_Bascula;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Surtido_Docificacion_PorEmpaque extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_ConfirmarEmpaque;


    TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg,txtv_Area,txtv_SugPallet, txtv_SugLote, txtv_SugPosicion, txtv_ConsProd, txtv_ConsLote, txtv_ConsCant, txtv_CantEmpReg, txtv_SugEmpaque, txtv_PalletAbierto,txtv_Lote;
    String RegEmpCantidadPedida, RegEmpCantidadSurtida, RegEmpCantidadPendiente, RegEmpStatus, RegEmpPalletAbierto,Lote;


    SortableTableView tabla_Salidas;
    Button btn_CerrarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    String PalletSeleccionadoTabla, SugPallet, SugLote, SugPosicion, SugEmpaque;
    String Pedido, Partida, NumParte, UM, CantidadTotal, CantidadPendiente, CantidadSurtida, Linea, CodigoPallet,Area;

    ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    ArrayList<datosTabla> arrayDatosTabla;
    Handler handler = new Handler();
    String[] HEADER = {"Empaque", "Producto", "Lote", "Cantidad Actual"};
    String[][] DATA_TO_SHOW;
    int palletRegistradosVar = 0;
    int cantInt = 0;
    int renglonSeleccionado;
    int renglonAnterior = -1;
    SimpleTableDataAdapter st;
    Bundle b;
    Boolean seleccionado, ReiniciarTabla = false;
    String LoteOP, Producto_Empaque, Cantidad_Empaque, FechaCadOP;
    String AreaDesc;

    boolean recargar;

    class datosTabla {
        String CodigoEmpaque, Producto, Cantidad, Lote;

        public datosTabla(String CodigoEmpaque, String producto, String Lote, String cantidad) {
            this.CodigoEmpaque = CodigoEmpaque;
            Producto = producto;
            Cantidad = cantidad;
            this.Lote = Lote;
        }

        public String getCodigoEmpaque() {
            return CodigoEmpaque;
        }

        public String getProducto() {
            return Producto;
        }

        public String getCantidad() {
            return Cantidad;
        }

        public String getLote() {
            return Lote;
        }
    }

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_docificacion);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Docificacion_PorEmpaque.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();
    }
    @Override
    protected void onResume()
    {
        Recarga();

        super.onResume();
    }
    private void Recarga()
    {
        if(!txtv_Pedido.getText().toString().equals("-"))
        {
            if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
            {

                SegundoPlano sp = new SegundoPlano("ConsultaPedidoDet");
                sp.execute();
//                sp = new SegundoPlano("SugiereEmpaque");
//                sp.execute();

            } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE) {
                SegundoPlano sp = new SegundoPlano("ConsultaPalletAbierto");
                sp.execute();


            }
        }
    }
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido));
        getSupportActionBar().setSubtitle("Empaque");
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Empaque= (EditText) findViewById(R.id.edtx_Empaque);
        edtx_ConfirmarEmpaque= (EditText) findViewById(R.id.edtx_ConfirmarEmpaque);

        tabla_Salidas= (SortableTableView) findViewById(R.id.tableView_Salida);
        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_ConfirmarEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

                txtv_Pedido =(TextView) findViewById(R.id.txtv_Pedido);
                txtv_Producto =(TextView) findViewById(R.id.txtv_Producto);
                txtv_CantPend =(TextView) findViewById(R.id.txtv_Cantidad);
                txtv_CantReg =(TextView) findViewById(R.id.txtv_CantidadReg);
                txtv_Area = (TextView) findViewById(R.id.txtv_Area);
                txtv_SugPallet =(TextView) findViewById(R.id.txtv_Pallet);
                txtv_SugLote =(TextView) findViewById(R.id.txtv_Lote);
                txtv_SugPosicion =(TextView) findViewById(R.id.txtv_Posicion);

                txtv_ConsProd =(TextView) findViewById(R.id.txtv_Empaque_Producto);
                txtv_ConsLote =(TextView) findViewById(R.id.txtv_Empaque_Lote);
                txtv_ConsCant =(TextView) findViewById(R.id.txtv_Empaque_Cantidad);

       //         txtv_ConsEstatus =(TextView) findViewById(R.id.txtv_Estatus);
                txtv_CantEmpReg =(TextView) findViewById(R.id.txtv_CantidadEmpaquesRegistrados);
                txtv_SugEmpaque =(TextView) findViewById(R.id.txtv_SugEmpaque);
                txtv_PalletAbierto = (TextView) findViewById(R.id.txtv_PalletAbierto);
//                txtv_Lote = (TextView) findViewById(R.id.txtv_Lote);

        cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_RegistroEmpaque);
        cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

        cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
        cl_TablaRegistro.setVisibility(View.GONE);

        tabla_Salidas.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));

        btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);


        txtv_Pedido.setText(Pedido);
        txtv_Producto.setText(NumParte);
        txtv_CantPend.setText(CantidadPendiente);
        txtv_CantReg.setText(CantidadSurtida);
        txtv_Area.setText(AreaDesc);

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
            Area = b.getString("Area");
            AreaDesc = b.getString("AreaDesc");

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
                                    edtx_ConfirmarEmpaque.requestFocus();
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
                    new esconderTeclado(Surtido_Docificacion_PorEmpaque.this);
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
                                    ReiniciarVariables("ConsultaEmpaque");
                                }
                            });
                        }
                    new esconderTeclado(Surtido_Docificacion_PorEmpaque.this);
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
                if(arrayDatosTabla!=null)
                    if(!(arrayDatosTabla.size()<=0))
                    {
                         SegundoPlano sp = new SegundoPlano("CerrarTarima");
                         sp.execute();
                    }
                     else
                     {
                    //     new popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , "false",true , true);
                     }
            }
        });




        tabla_Salidas.addDataClickListener(new ListenerClickTabla());
        tabla_Salidas.addDataLongClickListener(new ListenerLongClickTabla());
        tabla_Salidas.addHeaderClickListener(new headerClickListener());
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

                   //   Recarga();
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
                        SegundoPlano sp = new SegundoPlano("ConsultaPalletAbiertoOP");
                        sp.execute();

                    } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
                    {
                        cl_TablaRegistro.setVisibility(View.GONE);
                        cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
                        item.setIcon(R.drawable.ic_change_view);

                        SegundoPlano sp = new SegundoPlano("SugiereEmpaque");
                        sp.execute();

                    }

                    if (!edtx_ConfirmarEmpaque.getText().toString().equals(""))
                    {
                        tabla_Salidas.getDataAdapter().clear();
                        SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
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
                    txtv_ConsLote.setText("");
                   // txtv_ConsEstatus.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "RegistrarEmpaque":
                    txtv_ConsProd.setText("");
               //     txtv_ConsEstatus.setText("");
                    txtv_ConsCant.setText("");
                    txtv_ConsLote.setText("");
                    edtx_Empaque.setText("");
                    edtx_ConfirmarEmpaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "Reiniciar":
                    edtx_Empaque.setText("");
                    edtx_ConfirmarEmpaque.setText("");
                    tabla_Salidas.getDataAdapter().clear();
                    edtx_ConfirmarEmpaque.requestFocus();
                    break;
            }
    }
    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;
                renglonSeleccionado = rowIndex;
                tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                PalletSeleccionadoTabla = clickedData[1];
                btn_CerrarTarima.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                PalletSeleccionadoTabla = null;
                renglonAnterior=-1;
            }
            ReiniciarTabla = false;
        }

    }
    private class cambiaColorTablaSeleccionada implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {

                if(rowIndex == renglonSeleccionado)
                {
                    Color = R.color.RengSelStd;
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                    seleccionado = true;
                }
                else
                {
                    Color = R.color.Transparente;
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                }

            return new ColorDrawable(getResources().getColor(Color));
        }
    }
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
            String DataToShow="";
            int i =0;
            for(String data:clickedData)
            {
                DataToShow +=HEADER[i] +" - "+ data + "\n";
                i++;
            }
            new popUpGenerico(contexto, vista, DataToShow, "Información", true, false);

            return false;
        }
    }
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {

            Toast.makeText(contexto, HEADER[columnIndex], Toast.LENGTH_SHORT).show();
            Log.d("SoapResponse", HEADER[columnIndex]);
        }
    }
    private class cambiaColorTablaClear implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {
            Color = R.color.Transparente;
            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            return new ColorDrawable(getResources().getColor(Color));
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
                        sa.SOAPConsultarPartidaOrdenesSurtido(Pedido,Partida,contexto);
                        break;
                    case "SugiereEmpaque":
                        sa.SOAPSugerirEmpaqueDocificacion(Pedido,Partida,Area,contexto);
                        break;
                    case "ConsultaEmpaque":
                        sa.SOAPConsultaEmpaqueSurtido(Pedido,Partida,edtx_Empaque.getText().toString(),contexto );
                        break;
                    case "RegistrarEmpaque":
                        sa.SOAPSurtirEmpaqueConImpresion(Pedido,Partida, edtx_ConfirmarEmpaque.getText().toString(),Area,contexto);
                        break;
                    case "ConsultaPalletAbierto":
                        sa.SOAPConsultaTarimaConsolidada(Pedido, contexto);
                        break;
                    case "CerrarTarima":
                        sa.SOAPCierraPalletSurtido(RegEmpPalletAbierto, contexto);
                        break;
                    case "ConsultaPalletAbiertoOP":
                        sa.SOAPConsultaTarimaConsolidadaOP(Pedido,contexto);
                        break;
                }
                mensaje = sa.getMensaje();
                decision = sa.getDecision();

                if (decision.equals("true"))
                {
                    sacaDatos(tarea);
                    int i = 0;
                    DATA_TO_SHOW = new String[arrayDatosTabla.size()][4];
                    for (datosTabla a : arrayDatosTabla)
                    {
                        DATA_TO_SHOW[i][0] = a.getCodigoEmpaque();
                        DATA_TO_SHOW[i][1] = a.getProducto();
                        DATA_TO_SHOW[i][2] = a.getLote();
                        DATA_TO_SHOW[i][3] = a.getCantidad();

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
                    SegundoPlano sp;
                    if(LastView!=null)
                    {
                        LastView.requestFocus();
                    }

                    if (decision.equals("true"))
                        {
                            switch (tarea)
                                {

                                    case "ConsultaPedidoDet":
                                        txtv_CantReg.setText(CantidadSurtida);
                                        txtv_CantPend.setText(CantidadPendiente);
                                        txtv_CantPend.setText(CantidadPendiente);
                                        txtv_CantReg.setText(CantidadSurtida);
                                //        PesoEsperado = Double.parseDouble(CantidadPendiente);

                                        sp = new SegundoPlano("SugiereEmpaque");
                                        sp.execute();
                                        break;

                                    case "SugiereEmpaque":

                                        //txtv_SugPallet.setText(SugPallet);
                                        txtv_SugLote.setText(SugLote);
                                        //txtv_SugPosicion.setText(SugPosicion);
                                        txtv_SugEmpaque.setText(SugEmpaque);

                                        break;

                                    case "ConsultaEmpaque":
                                        txtv_ConsProd.setText(Producto_Empaque);
                                        txtv_ConsLote.setText(Lote);
                                        txtv_ConsCant.setText(Cantidad_Empaque);
                                       // txtv_ConsEstatus.setText(CodigoPallet);

                                        edtx_ConfirmarEmpaque.requestFocus();
                                        break;

                                    case "ConsultaPalletAbierto":
                                        tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                                        tabla_Salidas.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_Docificacion_PorEmpaque.this, DATA_TO_SHOW));
                                        ReiniciarTabla = true;
                                        tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                                        if (arrayDatosTabla != null)
                                            {
                                                cantInt = arrayDatosTabla.size();
                                            } else
                                            {
                                                cantInt = 0;
                                            }
                                        RegEmpPalletAbierto = mensaje;

                                        txtv_CantEmpReg.setText(String.valueOf(cantInt));
                                        txtv_PalletAbierto.setText(mensaje);
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                        break;

                                    case "ConsultaPalletAbiertoOP":
                                        //      new popUpGenerico(contexto,getCurrentFocus(),decision,decision,true,true);

                                        tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                                        tabla_Salidas.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_Docificacion_PorEmpaque.this, DATA_TO_SHOW));
                                        ReiniciarTabla = true;
                                        tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                                        if (arrayDatosTabla != null)
                                        {
                                            cantInt = arrayDatosTabla.size();
                                        } else
                                        {
                                            cantInt = 0;
                                        }
                                        RegEmpPalletAbierto = mensaje;

                                        txtv_CantEmpReg.setText(String.valueOf(cantInt));
                                        txtv_PalletAbierto.setText(mensaje);
//                                        edtx_CodigoEmpaque.setText("");
////                                        edtx_CodigoEmpaque.requestFocus();

                                        break;
                                    case "RegistrarEmpaque":
                                        txtv_CantPend.setText(RegEmpCantidadPedida);
                                        txtv_CantReg.setText(RegEmpCantidadSurtida);
                                        ReiniciarVariables(tarea);
                                        sp = new SegundoPlano("ConsultaPedidoDet");
                                        sp.execute();
                                        break;

                                    case "CerrarTarima":
                                        new popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + RegEmpPalletAbierto + "] con éxito.", decision, true, true);

                                        sp = new SegundoPlano("ConsultaPalletAbierto");
                                        sp.execute();
//                                        if (Cantidad_Empaque.equals("0"))
//                                            {
//                                                new popUpGenerico(contexto,getCurrentFocus(), getString(R.string.orden_prod_completada_exito), decision, true, true);
//                                            }
                                        //ReiniciarVariables("Reiniciar")
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
            {
                for (int i = 0; i < tabla.getPropertyCount(); i++)
                {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                        switch (tarea)
                        {

                            case "ConsultaPedidoDet":
                                CantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPend");
                                CantidadSurtida = tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                                break;
                            case "SugiereEmpaque":
                                Log.d("SoapResponse", tabla1.toString());
                                SugPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                SugLote = tabla1.getPrimitivePropertyAsString("LoteAXC");
                                SugPosicion = tabla1.getPrimitivePropertyAsString("CodigoPosicion");
                                SugEmpaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");
                                break;

                            case "ConsultaPalletAbiertoOP":

                                Log.d("SoapResponse", tabla1.toString());

                            arrayDatosTabla.add(new datosTabla(tabla1.getPrimitivePropertyAsString("CodigoEmpaque"),
                                                                tabla1.getPrimitivePropertyAsString("Producto"),
                                                                tabla1.getPrimitivePropertyAsString("Lote"),
                                                                tabla1.getPrimitivePropertyAsString("CantidadActual")));
                                break;
                            case "RegistrarEmpaque":

                                Log.d("SoapResponse", tabla1.toString());

                                RegEmpCantidadPedida = tabla1.getPrimitivePropertyAsString("CantidadPedida");
                                RegEmpCantidadSurtida= tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                                RegEmpCantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                                RegEmpStatus= tabla1.getPrimitivePropertyAsString("IdStatus");
                                RegEmpPalletAbierto = tabla1.getPrimitivePropertyAsString("PalletAbierto");

                                break;
                            case "CerrarTarima":

                                break;
                            case "CancelarTarima":

                                break;
                            case "ConsultaEmpaque":

                                Empaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");
                                Producto_Empaque = tabla1.getPrimitivePropertyAsString("Producto");
                                UM = tabla1.getPrimitivePropertyAsString("UM");
                                Cantidad_Empaque = tabla1.getPrimitivePropertyAsString("CantidadActual");
                                CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                Lote = tabla1.getPrimitivePropertyAsString("Lote");
                                break;

                        }

                    }

                }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
