package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Alm_Registro_Seleccion_Lote extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();


    SortableTableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String OrdenCompra;
    EditText edtx_Transferencia;
    TableColumnDpWidthModel columnModel;
    SimpleTableDataAdapter st ;
    SimpleTableHeaderAdapter sthd;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Partida","Destino","Producto","Lote","Cant. Solicitada","Cant. Surtida","Cant. Pendiente","Fecha Producción","Fecha Caducidad"};
    String[][] DATA_TO_SHOW;
    ArrayList<OrdenTraspaso> arrayListPosiciones;
        Bundle b = new Bundle();
    SegundoPlano sp;


    Handler h = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;

    boolean OrdenCompraSeleccionada,Recarga=true;
    class OrdenTraspaso
    {
        String Partida;
        String Origen;
        String Producto;
        String Lote;
        String CantidadSolicitada;
        String CantidadSurtida;
        String CantidadPendiente;
        String FechaProduccion;

        public String getFechaProduccion() {
            return FechaProduccion;
        }

        String FechaCaducidad;


        public OrdenTraspaso(String partida,String origen, String producto, String lote, String cantidadSolicitada, String cantidadSurtida, String cantidadPendiente,String FechaProduccion, String fechaCaducidad) {
            Partida = partida;
            Origen = origen;
            Producto = producto;
            Lote = lote;
            CantidadSolicitada = cantidadSolicitada;
            CantidadSurtida = cantidadSurtida;
            CantidadPendiente = cantidadPendiente;
            FechaCaducidad = fechaCaducidad;
            this.FechaProduccion=FechaProduccion;
        }

        public String getOrigen() {
            return Origen;
        }

        public String getProducto() {
            return Producto;
        }

        public String getLote() {
            return Lote;
        }

        public String getCantidadSolicitada() {
            return CantidadSolicitada;
        }

        public String getCantidadSurtida() {
            return CantidadSurtida;
        }

        public String getCantidadPendiente() {
            return CantidadPendiente;
        }
        public String getFechaCaducidad() {
            return FechaCaducidad;
        }
        public String getPartida() {
            return Partida;
        }

    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity_registro_transferer_seleccion_orden);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Alm_Registro_Seleccion_Lote.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declaraVariables();
        ajustarTamañoColumnas();

        AgregaListeners();


        tabla.setClickable(true);
        Log.d("SoapResponse ",String.valueOf(tabla.isClickable()));

    }

    @Override
    protected void onResume()
    {
        if(!edtx_Transferencia.getText().toString().equals(""))
        {
            sp = new SegundoPlano("Tabla");
            sp.execute();
        }
        super.onResume();
    }

    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.almacen_traspaso));

        tabla = (SortableTableView) findViewById(R.id.tableView_OC);
        tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(contexto,HEADER));
        edtx_Transferencia  = (EditText) findViewById(R.id.edtx_Transferencia);
        btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

    }
    private void AgregaListeners()
    {

        edtx_Transferencia.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Transferencia.getText().toString().equals(""))
                    {

                        SegundoPlano sp = new SegundoPlano("Tabla");
                        sp.execute();
                        new esconderTeclado(Alm_Registro_Seleccion_Lote.this);

                    }
                    else
                    {

                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Transferencia.requestFocus();
                                edtx_Transferencia.setText("");
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus(), getString(R.string.error_ingrese_orden_transferencia),false, true, true);
                    }
                    new esconderTeclado(Alm_Registro_Seleccion_Lote.this);
                    return true;
                }
                return false;

            }

        });


        btnConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidacionFinal();
            }
        });
        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());
        tabla.addDataLongClickListener(new ListenerLongClickTabla());
    }

    private void ValidacionFinal()
    {

        if(OrdenCompraSeleccionada)
        {

            Intent intent = new Intent(Alm_Registro_Seleccion_Lote.this, Almacen_Transferencia_Menu_Decision.class);
            intent.putExtras(b);
            startActivity(intent);
        }else
        {
            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_seleccionar_registro) ,false ,true,true );
        }

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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void ajustarTamañoColumnas()
    {
        columnModel = new TableColumnDpWidthModel(contexto,10,180);
        columnModel.setColumnWidth(0,100);
        columnModel.setColumnWidth(1,100);
        columnModel.setColumnWidth(8,0);

        tabla.getDataAdapter().notifyDataSetChanged();
        tabla.setColumnModel(columnModel);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(Recarga)
        {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar)
            {
                if(!edtx_Transferencia.getText().toString().equals(""))
                {
                    SegundoPlano sp = new SegundoPlano("Tabla");
                    sp.execute();

                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                b.putString("Transferencia",edtx_Transferencia.getText().toString());
                b.putString("Partida",clickedData[0]);
                b.putString("Origen",clickedData[1]);
                b.putString("Producto",clickedData[2]);
                b.putString("Lote",clickedData[3]);
                b.putString("CantSolicitada",clickedData[4]);
                b.putString("CantSurtida",clickedData[5]);
                b.putString("CantPendiente",clickedData[6]);
                b.putString("FechaProd",clickedData[7]);
                b.putString("FechaCad",clickedData[8]);
                btnConfirmar.setEnabled(true);
                OrdenCompraSeleccionada = true;
            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                renglonAnterior=-1;
                OrdenCompraSeleccionada = false;
            }
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

            }
            else
            {
                Color = R.color.Transparente;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            }
            return new ColorDrawable(getResources().getColor(Color));
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
    private void reiniciarDatos()
    {


        tabla.getDataAdapter().clear();
        b.clear();

    }
    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String decision,mensaje,Tarea;

        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {
            Recarga = false;

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {

                switch (Tarea)
                {
                    case"Tabla":
                        sa.SOAPListarPartidasTraspaso(edtx_Transferencia.getText().toString(),contexto);
                        break;



                }

                decision = sa.getDecision();
                mensaje = sa.getMensaje();

                if(decision.equals("true")&&Tarea.equals("Tabla"))
                {
                    sacaDatos();
                    if(decision.equals("true")&&arrayListPosiciones!=null)
                    {
                        int i = 0;
                        DATA_TO_SHOW = new String[arrayListPosiciones.size()][9];
                        for(OrdenTraspaso a: arrayListPosiciones)
                        {
                            DATA_TO_SHOW[i][0] = a.getPartida();
                            DATA_TO_SHOW[i][1] = a.getOrigen();
                            DATA_TO_SHOW[i][2] = a.getProducto();
                            DATA_TO_SHOW[i][3] = a.getLote();
                            DATA_TO_SHOW[i][4] = a.getCantidadSolicitada();
                            DATA_TO_SHOW[i][5] = a.getCantidadSurtida();
                            DATA_TO_SHOW[i][6] = a.getCantidadPendiente();
                            DATA_TO_SHOW[i][7] = a.getFechaProduccion();
                            DATA_TO_SHOW[i][8] = a.getFechaCaducidad();



                            i++;
                        }
                    }
                }
            }catch (Exception e)
            {
                Log.e("RegistroOC", "doInBackground: " + e.getMessage() );
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
         {
            try {
                if (decision.equals("true")) {
                    switch (Tarea)
                    {
                        case "Tabla":

                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(Alm_Registro_Seleccion_Lote.this, sa.getDao().getcTabla()));
                            tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,sa.getDao().getcEncabezado()));
                            ajustarTamañoColumnas();
                            tabla.setColumnModel(columnModel);
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                            tabla.getDataAdapter().notifyDataSetChanged();
                            arrayListPosiciones.clear();
                            OrdenCompraSeleccionada = false;
                            renglonAnterior = -1;

                            break;

                    }
                }

                if (decision.equals("false")) {
                    new popUpGenerico(contexto, vista, mensaje, decision, true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
                Log.e("SoapResponse", "onPostExecute: "+mensaje );
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            Recarga = true;
        }

        @Override
        protected void onCancelled()
        {

                Log.e("SP", "onCancelled: hilo terminado" );

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            return;

        }
    }
    public void sacaDatos()
    {
        SoapObject tabla = sa.parser();
        String Orden,Tipo,Fecha,Proveedor;

        arrayListPosiciones = new ArrayList<>();
        OrdenTraspaso p;

        for(int i = 0; i<tabla.getPropertyCount();i++)
        {
            try {

                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse",tabla1.toString());

                p= new OrdenTraspaso(tabla1.getPrimitivePropertyAsString("Partida"),
                                     tabla1.getPrimitivePropertyAsString("Origen"),
                                     tabla1.getPrimitivePropertyAsString("Producto"),
                                     tabla1.getPrimitivePropertyAsString("Lote"),
                                     tabla1.getPrimitivePropertyAsString("CantidadSolicitada"),
                                     tabla1.getPrimitivePropertyAsString("CantidadSurtida"),
                                     tabla1.getPrimitivePropertyAsString("CantidadPendiente"),
                                     tabla1.getPrimitivePropertyAsString("FechaProduccion"),
                                     tabla1.getPrimitivePropertyAsString("FechaCaducidad"));

                arrayListPosiciones.add(p);




            }catch (Exception e)
            {

                e.printStackTrace();

                //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        if(sp!=null)
        {
            sp.cancel(true);
        }
        super.onBackPressed();
    }
}
