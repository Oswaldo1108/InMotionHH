package com.automatica.AXCMP.c_Consultas;

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
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;


import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class consultas_BusquedaExistencia extends AppCompatActivity {

    EditText Producto;
    Spinner Resultados;
    Button Buscar;
    Toolbar toolbar;
    Context contexto = this;
    View vista;

    SoapAction sa = new SoapAction();


    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    TableColumnDpWidthModel columnModel;
    TableView<String[]> tabla;
    ArrayList<String> datosSpinner = new ArrayList<>();

    ArrayList<constructorBusquedaExistencia> arrayTabla = new ArrayList<>();

    public String[] TABLE_HEADERS = { "Ubicación", "Pallet", "Producto", "Lote","Empaques", "Cantidad Actual","LoteAXC"};
    public String[][] DATA_TO_SHOW;
    Handler handlerRequestFocus = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_busqueda_existencia);
        try
        {
            creaToolbar(toolbar," Existencias");
         //   this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            new cambiaColorStatusBar(contexto,R.color.AzulStd,consultas_BusquedaExistencia.this);
            declaraVariables();
        }catch (Exception e)
        {

            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }



    }
    public void declaraVariables()
    {
        try
        {
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            Producto = (EditText) findViewById(R.id.txtv_Producto);
            Producto.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            Resultados = (Spinner) findViewById(R.id.spn_Motivo);
            Buscar = (Button) findViewById(R.id.btn_ConsultaProducto);
            Buscar.setEnabled(false);
            tabla = (TableView<String[]>) findViewById(R.id.tblv_ResultadosProducto);
            tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(consultas_BusquedaExistencia.this, TABLE_HEADERS));
            tabla.addDataClickListener(new ListenerClickTabla());
            Producto.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        datosSpinner.clear();
                        SegundoPlanoBuscaProductos spb = new SegundoPlanoBuscaProductos();
                        spb.execute();
                        new esconderTeclado(consultas_BusquedaExistencia.this);

                    }
                    return false;
                }
            });

            Buscar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SegundoPlanoMuestraTabla spb = new SegundoPlanoMuestraTabla();
                    spb.execute();
                    new esconderTeclado(consultas_BusquedaExistencia.this);
                }
            });
        }catch (Exception e)
        {
            e.getMessage();
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }


    }

    public void creaToolbar(Toolbar toolbar, String Titulo)
    {
        try
        {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" "+getString(R.string.Consultas_Existencias));
            //toolbar.setSubtitle("  "+Titulo);
            toolbar.setLogo(R.mipmap.logo_axc);//    toolbar.setLogo(R.drawable.axc_logo_toolbar);
        }catch (Exception e)
        {

            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }


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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
        {
            int id = item.getItemId();

            if((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto,vista);
            }

            if((id == R.id.borrar_datos))
            {

                reiniciarVariables();
            }
        }catch (Exception e)
        {
            e.getMessage();
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }


        return super.onOptionsItemSelected(item);
    }

    public void reiniciarVariables()
    {
        try
        {
            tabla.getDataAdapter().clear();
            tabla.getDataAdapter().notifyDataSetChanged();
            requestFocusTextFields();
            datosSpinner.clear();
            Resultados.setAdapter(null);
            Buscar.setEnabled(false);
        }catch (Exception e)
        {
            e.getMessage();
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }

    }
    public void ajustarTamañoColumnas()
    {

        try
        {
            columnModel = new TableColumnDpWidthModel(contexto,6,120);
            columnModel.setColumnWidth(1,200);
            columnModel.setColumnWidth(3,250);
            columnModel.setColumnWidth(5,200);
        }catch (Exception e)
        {
            e.getMessage();
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }
    }
    public void requestFocusTextFields()
    {
        try
        {
            handlerRequestFocus.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Producto.clearFocus();       //Aqui hago request focus a el textfield, para seguir escaneando
                    Producto.requestFocus();
                    Producto.setText("");

                }
            });

        }catch (Exception e)
        {
            e.getMessage();
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
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
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tabla.getDataAdapter().notifyDataSetChanged();

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();

                renglonAnterior=-1;
            }

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
    class SegundoPlanoBuscaProductos extends AsyncTask<Void,Void,Void>
    {
        String ConsultaProducto;
        String  decision,mensaje,tabla;
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(
                consultas_BusquedaExistencia.this,
                android.R.layout.simple_spinner_item,
                datosSpinner);
        @Override
        protected void onPreExecute()
        {

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
                ConsultaProducto = Producto.getText().toString();
                sa.SOAPConsultaProducto(ConsultaProducto,contexto);
                decision = sa.getDecision();
                mensaje = sa.getMensaje();


                if(ConsultaProducto.equals(""))
                {
                    decision = "false";
                    mensaje = "Debe capturar un producto valido";


                }else

                {
                    if(decision.equals("true"))
                    {
                        sacaDatos();

                        adaptador = new ArrayAdapter<String>(consultas_BusquedaExistencia.this,android.R.layout.simple_spinner_item,datosSpinner);
                        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    }
                }


            }catch (Exception e)
            {
                e.getMessage();
                decision = "false";
                mensaje = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            try
            {
                if (decision.equals("true"))
                {

                    Resultados.setAdapter(adaptador);
                    new esconderTeclado(consultas_BusquedaExistencia.this);
                    Buscar.setEnabled(true);
                }
                if (decision.equals("false"))
                {
//
                    new popUpGenerico(contexto,vista,mensaje,"Advertencia",true,true);
                    requestFocusTextFields();
                    reiniciarVariables();
                }

            }catch (Exception e)
            {
                e.getMessage();
                new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }

    class SegundoPlanoMuestraTabla extends  AsyncTask<Void,Void,Void>
    {
        String NumParte,Revision = "", mensaje, decision;

        @Override
        protected void onPreExecute()
        {
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
                NumParte = Resultados.getSelectedItem().toString();

                if(NumParte.equals("")||(NumParte.length()<5))
                {
                    decision = "false";
                    mensaje = "Debe capturar un codigo de posición";
              /*  handlerRequestFocus.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        new popUpGenerico(contexto,vista,mensaje,"Error",true,true);
                    }
                })*/;

                }else
                {
                    sa.SOAPConsultaPalletArticulo(NumParte,Revision,contexto);
                    mensaje =sa.getMensaje();
                    decision = sa.getDecision();
                    if (decision.equals("true"))
                    {
                        sacaDatosTabla();
                        DATA_TO_SHOW = new String[arrayTabla.size()][7];
                        int i = 0;
                        for (constructorBusquedaExistencia a : arrayTabla) {

                            DATA_TO_SHOW[i][0] = a.getUbicacion();
                            DATA_TO_SHOW[i][1] = a.getPallet();
                            DATA_TO_SHOW[i][2] = a.getProducto();
                            DATA_TO_SHOW[i][3] = a.getLote();
                            DATA_TO_SHOW[i][4] = a.getEmpaques();
                            DATA_TO_SHOW[i][5] = a.getCantidadActual();
                            DATA_TO_SHOW[i][6] = a.getLoteAXC();


                            i++;


                        }
                    }

                    Log.i("SoapResponse", mensaje + decision);
                    Log.i("SoapResponse", NumParte + Revision);
                }


            }catch (Exception e)
            {
                e.getMessage();
                decision = "false";
                mensaje = e.getMessage();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {
                if (decision.equals("true"))
                {
                    ajustarTamañoColumnas();
                    tabla.setDataAdapter(st = new SimpleTableDataAdapter(consultas_BusquedaExistencia.this, DATA_TO_SHOW));
                    tabla.getDataAdapter().notifyDataSetChanged();
                    tabla.setColumnModel(columnModel);
                    arrayTabla.clear();
                }
                if (decision.equals("false"))
                {
                    new popUpGenerico(contexto,vista,mensaje,"Error",true,true);
                    requestFocusTextFields();
                    reiniciarVariables();
                }
            }catch (Exception e)
            {
                e.getMessage();
                new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }

    //region Soap
    public void sacaDatos()
    {
        SoapObject tabla = sa.parser();


        PropertyInfo pi = new PropertyInfo();
        Log.i("SoapResponse", "sacaDatos: " +tabla.toString() + tabla.getPropertyCount());

        for (int j = 0; j <= tabla.getPropertyCount(); j++)
        {
            try
            {
                SoapObject tabla1 = (SoapObject) tabla.getProperty(j);
                String dato = tabla1.getPrimitivePropertyAsString("NumParte");
                datosSpinner.add(dato);

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
    public void sacaDatosTabla()
    {
        String dato,dato1,dato2,dato3,dato4,dato5,dato6,dato7;
        SoapObject tabla = sa.parser();
        //  Log.i("SoapResponse", "sacaDatos: " + tabla.toString());
        //  arrayAdaptador.clear();
        if(tabla!=null) {
            Log.i("SoapResponse", "sacaDatos: " + tabla.toString());
            for (int i = 0; i < tabla.getPropertyCount(); i++) {

                try {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    dato = tabla1.getPrimitivePropertyAsString("Ubicacion");
                    dato1 = tabla1.getPrimitivePropertyAsString("Pallet");
                    dato2 = tabla1.getPrimitivePropertyAsString("NumParte");
                    dato3 = tabla1.getPrimitivePropertyAsString("LoteAXC");
                    dato4 = tabla1.getPrimitivePropertyAsString("Empaques");
                    dato5 = tabla1.getPrimitivePropertyAsString("CantidadActual");
                    dato6 = tabla1.getPrimitivePropertyAsString("LoteAXC");


                    constructorBusquedaExistencia c = new constructorBusquedaExistencia(dato, dato1, dato2, dato3, dato4, dato5,dato6);
                    arrayTabla.add(c);
                    Log.i("SoapResponse", "sacaDatos: " + arrayTabla.get(i).getPallet());


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }
//endregion

}

