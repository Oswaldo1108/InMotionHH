package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Armado_Pallets_old extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_Cantidad,edtx_Empaque, edtx_OrdenProduccion;
    SortableTableView tabla;
    Button btn_CerrarTarima,btn_CancelarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    String Tipo = "Polvos";
    String PalletSeleccionadoTabla;

    String[] HEADER = {"Producto","Cantidad","Empaque"};
    String[][] DATA_TO_SHOW;
    int palletRegistradosVar = 0;
    ArrayList<datosTabla> arrayDatosTabla;
    Handler handler = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st;

    Boolean seleccionado,ReiniciarTabla= false;
    String EstatusOP,ProductoOP,CantidadOP;
    TextView txtv_Producto,txtv_Cantidad,txtv_Estatus;
    String Pallet;
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
        setContentView(R.layout.almacen_armado_pallet);

        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Armado_Pallets_old.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        declararVariables();
        agregaListeners();


    }

    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.almacen_armado_tarimas));

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Cantidad =(EditText) findViewById(R.id.edtx_Cantidad);
        edtx_Empaque= (EditText) findViewById(R.id.edtx_Empaque);
        edtx_OrdenProduccion = (EditText) findViewById(R.id.edtx_OrdenProduccion);

                tabla = (SortableTableView) findViewById(R.id.tableView);

        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Estatus = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);

        edtx_Cantidad.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_OrdenProduccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);


        btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btn_CancelarTarima = (Button) findViewById(R.id.btn_CancelarTarima);
        btn_CerrarTarima.setEnabled(true);

    }
    private void agregaListeners()
    {
        edtx_OrdenProduccion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_OrdenProduccion.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                        sp.execute();
                    }else
                        {
                            new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_orden_produccion) ,"false" ,true , true);

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_OrdenProduccion.setText("");
                                    edtx_OrdenProduccion.requestFocus();
                                }
                            });
                        }
                    new esconderTeclado(Almacen_Armado_Pallets_old.this);
                    return true;
                }
                return false;
            }
        });
        edtx_Cantidad.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Cantidad.getText().toString().equals(""))
                    {
                      edtx_Empaque.requestFocus();
                    }else
                        {
                            new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_cantidad) ,"false" ,true , true);

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();
                                }
                            });

                        }
                    new esconderTeclado(Almacen_Armado_Pallets_old.this);
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
                        SegundoPlano sp = new SegundoPlano("RegistrarEmpaque");
                        sp.execute();
                    }else
                        {
                            new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);

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
                    new esconderTeclado(Almacen_Armado_Pallets_old.this);
                    return true;
                }
                return false;
            }
        });


        btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
             if(!(arrayDatosTabla.size()<=0))
             {
                 SegundoPlano sp = new SegundoPlano("CerrarTarima");
                 sp.execute();
             }
             else
             {
                 new popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , "false",true , true);
             }
            }
        });
        btn_CancelarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            if(arrayDatosTabla!=null)
                if(!(arrayDatosTabla.size()<=0))
                {
                    SegundoPlano sp = new SegundoPlano("CancelarTarima");
                    sp.execute();
                }
                else
                {
                    new popUpGenerico(contexto,vista ,getString(R.string.error_cancelar_pallet_sin_empaques) , "false",true , true);
                }
            }
        });

        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addDataLongClickListener(new ListenerLongClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());
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

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }
        if((id == R.id.recargar))
        {

            if(!edtx_Cantidad.getText().toString().equals(""))
            {
                tabla.getDataAdapter().clear();
                SegundoPlano sp = new SegundoPlano("llenarTabla");
                sp.execute();
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
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tabla.getDataAdapter().notifyDataSetChanged();
                PalletSeleccionadoTabla = clickedData[1];

                btn_CerrarTarima.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
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
            if(ReiniciarTabla)
            {

            }else
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


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
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
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {

                switch (tarea)
                {
                    case "ConsultaOrdenProduccion":
                        sa.SOAPConsultaOrdenProduccion_V2(edtx_OrdenProduccion.getText().toString(),Tipo,contexto );
                        break;
                    case "ConsultarTarima":
                       // sa.SOAPConsultaPAlletRegistroProduccion(Pallet, contexto);
                        break;
                    case "RegistrarEmpaque":
                        //sa.SOAPRegistraEmpaqueEnPallet(edtx_Empaque.getText().toString(), edtx_OrdenProduccion.getText().toString(),contexto );
                        break;
                    case "CerrarTarima":
                        sa.SOAPCerrarRegistroPallet(edtx_OrdenProduccion.getText().toString(),"", contexto);
                        break;
                    case "CancelarTarima":
                        sa.SOAPCancelarRegistroPallet(edtx_OrdenProduccion.getText().toString(),contexto );
                        break;


                }
                mensaje = sa.getMensaje();
                decision = sa.getDecision();

                if (decision.equals("true"))
                {
                    sacaDatos(tarea);
                    int i = 0;
                    DATA_TO_SHOW = new String[arrayDatosTabla.size()][3];
                    for (datosTabla a : arrayDatosTabla)
                    {


                        DATA_TO_SHOW[i][0] = a.getProducto();
                        DATA_TO_SHOW[i][1] = a.getCantidad();
                        DATA_TO_SHOW[i][2] = a.getEmpaque();
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
            if (decision.equals("true"))
            {
                switch (tarea)
                {

                    case "ConsultaOrdenProduccion":
                        txtv_Estatus.setText(EstatusOP);
                        txtv_Cantidad.setText(CantidadOP);
                        txtv_Producto.setText(ProductoOP);
                        if(!mensaje.equals("")&&!mensaje.equals(null))
                        {
                            Pallet = mensaje;
                            SegundoPlano sp = new SegundoPlano("ConsultaTarima");
                            sp.execute();
                        }
                        break;

                    case "ConsultarTarima":
                    case "RegistrarEmpaque":
                        tabla.getDataAdapter().notifyDataSetChanged();
                        tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Armado_Pallets_old.this,DATA_TO_SHOW));
                        ReiniciarTabla = true;
                        tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                        ReiniciarTabla = false;
                        edtx_Empaque.setText("");
                        edtx_Empaque.requestFocus();
                        break;

                    case "CerrarTarima":
                        new popUpGenerico(contexto, vista, "Tarima cerrada ["+mensaje+"] con éxito.", decision, true, true);
                        break;

                    case "CancelarTarima":
                        new popUpGenerico(contexto, vista, getString(R.string.cancelar_tarima), decision, true, true);
                        break;

                }
            }

            if(decision.equals("false"))
            {

                    new popUpGenerico(contexto, vista, mensaje, "Advertencia", true, true);
                    txtv_Producto.requestFocus();
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
            String Producto, Cantidad, Empaque;

            arrayDatosTabla = null;
            arrayDatosTabla = new ArrayList<>();
            datosTabla d;
            if (tabla != null)
                for (int i = 0; i < tabla.getPropertyCount(); i++)
                {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    switch(tarea)
                    {
                        case "ConsultaOrdenProduccion":
                            EstatusOP = tabla1.getPrimitivePropertyAsString("Estatus");
                            ProductoOP = tabla1.getPrimitivePropertyAsString("Producto");
                            CantidadOP = tabla1.getPrimitivePropertyAsString("Cantidad");

                            break;
                        case "ConsultarTarima":

                            Log.d("SoapResponse", tabla1.toString());

                            Empaque = tabla1.getPrimitivePropertyAsString("Empaque");
                            Producto = tabla1.getPrimitivePropertyAsString("Producto");
                            Cantidad = tabla1.getPrimitivePropertyAsString("Cantidad");

                            d = new datosTabla(Producto, Cantidad, Empaque);
                            arrayDatosTabla.add(d);
                            break;
                        case "RegistrarEmpaque":

                            Log.d("SoapResponse", tabla1.toString());

                            Empaque = tabla1.getPrimitivePropertyAsString("Empaque");
                            Producto = tabla1.getPrimitivePropertyAsString("Producto");
                            Cantidad = tabla1.getPrimitivePropertyAsString("Cantidad");

                            d = new datosTabla(Producto, Cantidad, Empaque);
                            arrayDatosTabla.add(d);
                            break;
                        case "CerrarTarima":

                            break;
                        case "CancelarTarima":

                            break;
                    }



                }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
