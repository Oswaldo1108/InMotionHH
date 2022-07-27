package com.automatica.AXCMP.c_Surtido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

public class Surtido_Entrega extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_CodigoPallet,edtx_CodigoLinea;

    SortableTableView tabla_Salidas;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_Pedido,txtv_Partida,txtv_UnidadMedida,txtv_Empaques,txtv_Producto;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    int renglonSeleccionado;
    SimpleTableDataAdapter st;
    String[] HEADER = {"Empaque", "Producto", "Lote", "Cantidad Actual"};
    String[][] DATA_TO_SHOW;
    int renglonAnterior = -1;
    Boolean seleccionado, ReiniciarTabla = false;
    ArrayList<datosTabla> arrayDatosTabla;
    String Pedido,Partida,UnidadMedida,Producto,Empaques,Cantidad,Estatus,Caducidad;



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
        setContentView(R.layout.surtido_activity_entraga);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Entrega.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declararVariables();
        agregaListeners();
    }
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.recepcion_surtido_entrega));
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoLinea= (EditText) findViewById(R.id.edtx_CodigoLinea);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoLinea.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtv_Pedido= (TextView) findViewById(R.id.txtv_Pedido);
        txtv_Partida= (TextView) findViewById(R.id.txtv_Partida);
        txtv_Producto= (TextView) findViewById(R.id.txtv_Producto);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);
        txtv_UnidadMedida= (TextView) findViewById(R.id.txtv_UnidadMedida);

        tabla_Salidas= (SortableTableView) findViewById(R.id.tableView_Salida);
        tabla_Salidas.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));

    }
    private void agregaListeners()
    {
        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoPallet.requestFocus();
                            }
                        });
                        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                        sp.execute();


                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),"false",true,true);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoPallet.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Surtido_Entrega.this);
                }

                return false;
            }
        });

        edtx_CodigoLinea.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoLinea.getText().toString().equals(""))
                    {

                        if(edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {

                                    edtx_CodigoPallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_pallet),"false",true,true);
                            return false;
                        }


                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoLinea.requestFocus();
                            }
                        });
                        SegundoPlano sp = new SegundoPlano("SurtidoPallet");
                        sp.execute();

                    }else
                    {
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_linea),"false",true,true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoLinea.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Surtido_Entrega.this);
                }

                return false;
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
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
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
        edtx_CodigoPallet.setText("");
        edtx_CodigoLinea.setText("");

        tabla_Salidas.getDataAdapter().notifyDataSetChanged();
        tabla_Salidas.getDataAdapter().clear();
        edtx_CodigoPallet.requestFocus();
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
            if ((id == R.id.borrar_datos))
            {
                ReiniciarVariables();
            }
        }
        return super.onOptionsItemSelected(item);
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
//                PalletSeleccionadoTabla = clickedData[1];
//                btn_CerrarTarima.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla_Salidas.getDataAdapter().notifyDataSetChanged();
               // PalletSeleccionadoTabla = null;
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
            new popUpGenerico(contexto,getCurrentFocus(), DataToShow, "Información", true, false);

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
            try {
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
                recargar = true;
                LastView = getCurrentFocus();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBarHolder.requestFocus();
                    }
                }, 10);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try {


                switch (tarea) {
                    case "ConsultaPallet":
                          sa.SOAPConsultaPalletEntregaSurtido(edtx_CodigoPallet.getText().toString(),contexto);
                        break;
                    case "SurtidoPallet":
                          sa.SOAPEntragaPalletSurtido(edtx_CodigoPallet.getText().toString(),edtx_CodigoLinea.getText().toString(),contexto);
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

            try {

                if(LastView!=null)
                {
                    LastView.requestFocus();
                }
                recargar = false;
                if (decision.equals("true")) {
                    switch (tarea) {
                        case "ConsultaPallet":
                            tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                            tabla_Salidas.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_Entrega.this, DATA_TO_SHOW));
                            ReiniciarTabla = true;
                            tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                            edtx_CodigoLinea.requestFocus();
                            break;

                        case "SurtidoPallet":

                            ReiniciarVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_colocado), decision, true, true);
                            break;

                    }

                }

                if (decision.equals("false"))
                {
                    ReiniciarVariables();
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), decision, true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

        }
    }
    private void sacaDatos(String tarea)
    {
        try {
            SoapObject tabla = sa.parser();

            arrayDatosTabla = null;
            arrayDatosTabla = new ArrayList<>();

            if (tabla != null)
            {
                for (int i = 0; i < tabla.getPropertyCount(); i++)
                {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    switch (tarea)
                    {
                        case "ConsultaPallet":

                            Log.d("SoapResponse", tabla1.toString());

                            arrayDatosTabla.add(new datosTabla(tabla1.getPrimitivePropertyAsString("CodigoEmpaque"),
                                    tabla1.getPrimitivePropertyAsString("Producto"),
                                    tabla1.getPrimitivePropertyAsString("Lote"),
                                    tabla1.getPrimitivePropertyAsString("CantidadActual")));
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
