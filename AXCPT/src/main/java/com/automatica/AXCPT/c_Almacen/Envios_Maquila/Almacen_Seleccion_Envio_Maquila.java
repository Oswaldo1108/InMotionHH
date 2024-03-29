package com.automatica.AXCPT.c_Almacen.Envios_Maquila;

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


import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;

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

public class Almacen_Seleccion_Envio_Maquila extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();


    SortableTableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String OrdenCompra;
    TableColumnDpWidthModel columnModel;
    EditText edtx_OrdenCompra;
    SimpleTableDataAdapter st;
    SimpleTableHeaderAdapter sthd;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Partida","Producto","Destino","Fecha"};
    String[][] DATA_TO_SHOW;
    ArrayList<OrdenesCompra> arrayListPosiciones;
        Bundle b = new Bundle();

    Handler h = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;

    boolean ConsultaInicial;
    boolean OrdenCompraSeleccionada,Recarga=true;
    class OrdenesCompra
    {
        String Orden,Tipo,Fecha,Proveedor;


        public OrdenesCompra(String orden, String tipo, String fecha, String proveedor) {
            Orden = orden;
            Tipo = tipo;
            Fecha = fecha;
            Proveedor = proveedor;
        }

        public String getOrden() {
            return Orden;
        }

        public String getTipo() {
            return Tipo;
        }

        public String getFecha() {
            return Fecha;
        }

        public String getProveedor() {
            return Proveedor;
        }
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity_registro_seleccion_envio_maquila);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Seleccion_Envio_Maquila.this);
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
        if(ConsultaInicial)
        {
            SegundoPlano sp = new SegundoPlano("Tabla");
            sp.execute();
        }
        super.onResume();
    }

    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.recepcion_envio_maquila));



        tabla = (SortableTableView) findViewById(R.id.tableView_OC);
//        tabla.setHeaderAdapter( sthd = new SimpleTableHeaderAdapter(contexto,HEADER));

        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);


        edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Cantidad);

        btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

    }
    private void AgregaListeners()
    {
        edtx_OrdenCompra.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER)) {
                    if (!edtx_OrdenCompra.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("Tabla");
                        sp.execute();
                    } else
                        {
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_OrdenCompra.setText("");
                                    edtx_OrdenCompra.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_orden_compra) ,"false" ,true , true);
                    }
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

        //if(OrdenCompraSeleccionada)
        {
            /*PRUEBA*/


          //  b.putString("FechaCaducidad", "FechaCaducidad");
            Intent intent = new Intent(Almacen_Seleccion_Envio_Maquila.this, Almacen_Registro_Seleccion_Tipo_Envio.class);
            b.putString("OrdenCompra", edtx_OrdenCompra.getText().toString());
            b.putString("FechaCaducidad", "Fechacaducidad");
            intent.putExtras(b);
            startActivity(intent);
        }/*else
        {
            new popUpGenerico(contexto,vista ,getString(R.string.error_seleccionar_registro) ,"false" ,true,true );
        }*/

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
        columnModel = new TableColumnDpWidthModel(contexto,10,150);
        columnModel.setColumnWidth(0,185);
        columnModel.setColumnWidth(1,150);

        tabla.getDataAdapter().notifyDataSetChanged();
        tabla.setColumnModel(columnModel);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(Recarga == true)
        {
            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }


            if (id == R.id.recargar) {
                SegundoPlano sp = new SegundoPlano("Tabla");
                sp.execute();
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


                b.putString("Partida",clickedData[0]);
                b.putString("Tipo",clickedData[1]);
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
                        sa.SOAPListarPartidasEnvioMaquila(edtx_OrdenCompra.getText().toString(),contexto);

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
                        DATA_TO_SHOW = new String[arrayListPosiciones.size()][4];
                        for(OrdenesCompra a: arrayListPosiciones)
                        {
                            DATA_TO_SHOW[i][0] = a.getOrden();
                            DATA_TO_SHOW[i][1] = a.getTipo();
                            DATA_TO_SHOW[i][2] = a.getProveedor();
                            DATA_TO_SHOW[i][3] = a.getFecha();



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
                    switch (Tarea) {
                        case "Tabla":

                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Seleccion_Envio_Maquila.this, DATA_TO_SHOW));
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                            tabla.getDataAdapter().notifyDataSetChanged();
                            arrayListPosiciones.clear();
                            OrdenCompraSeleccionada = false;
                            renglonAnterior = -1;

                            break;


                    }
                    ConsultaInicial = true;
                }

                if (decision.equals("false")) {
                    new popUpGenerico(contexto, vista, mensaje, "Advertencia", true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), "Advertencia", true, true);
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
        OrdenesCompra p;

        for(int i = 0; i<tabla.getPropertyCount();i++)
        {
            try {

                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse",tabla1.toString());
                Orden=  tabla1.getPrimitivePropertyAsString("OrdenCompra");
                Tipo = tabla1.getPrimitivePropertyAsString("Tipo");
                Proveedor= tabla1.getPrimitivePropertyAsString("NomProveedor");
                Fecha= tabla1.getPrimitivePropertyAsString("erpFechaRecibe");
                //FechaCaducidad = tabla1.getPrimitivePropertyAsString("FechaCaducidad");

                p= new OrdenesCompra(Orden,Tipo,Fecha,Proveedor);

                arrayListPosiciones.add(p);




            }catch (Exception e)
            {

                e.printStackTrace();

                //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
            }
        }
    }

}
