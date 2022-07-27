package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.automatica.AXCPT.Servicios.CreaDialogos;
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

public class Almacen_Armado_Pallets_Polvos_Primera_Y_Ultima extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_Empaque,edtx_UltimoEmpaque, edtx_OrdenProduccion;
    SortableTableView tabla;
    Button btn_CerrarTarima,btn_CancelarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista= getCurrentFocus();
    Context contexto = this;

    String PalletSeleccionadoTabla;

    String CantidadRegOP, FechaCaducidadOP;
    int cantInt=0;
    String[] HEADER = {"Empaque"};
    String[][] DATA_TO_SHOW;
    int palletRegistradosVar = 0;
    ArrayList<datosTabla> arrayDatosTabla;
    Handler handler = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st;

    Boolean seleccionado,ReiniciarTabla= false;
    String LoteOP,ProductoOP,CantidadOP,EmpaqueBaja;
    TextView txtv_Producto,txtv_Cantidad, txtv_Lote,txtv_CantidadEmpaques,txtv_CantidadRegistrada,txtv_FechaCaducidad;
    String Tipo = "Polvos";
    boolean recargar = false;
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
        setContentView(R.layout.almacen_armado_pallet_polvos_primera_y_ultima);

        new cambiaColorStatusBar(contexto, R.color.VerdeStd, Almacen_Armado_Pallets_Polvos_Primera_Y_Ultima.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        declararVariables();
        agregaListeners();


    }
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.almacen_armado_tarimas));
        getSupportActionBar().setSubtitle(getString(R.string.almacen_armado_tarimas_polvos));

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


        edtx_Empaque= (EditText) findViewById(R.id.edtx_Empaque);
        edtx_UltimoEmpaque= (EditText) findViewById(R.id.edtx_Empaque2);
        edtx_OrdenProduccion = (EditText) findViewById(R.id.edtx_OrdenProduccion);

        tabla = (SortableTableView) findViewById(R.id.tableView);

        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Lote = (TextView) findViewById(R.id.txtv_Lote);
        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_CantidadEmpaques = (TextView) findViewById(R.id.txtv_CantidadEmpaques);
        txtv_CantidadRegistrada =(TextView) findViewById(R.id.txtv_CantidadReg);
        txtv_FechaCaducidad = (TextView) findViewById(R.id.txtv_FechaCaducidad);


        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_OrdenProduccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);


        btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btn_CancelarTarima = (Button) findViewById(R.id.btn_CancelarTarima);
        btn_CerrarTarima.setEnabled(true);

        edtx_OrdenProduccion.requestFocus();
    }
    private void agregaListeners()
    {
        edtx_OrdenProduccion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {


                    if(!edtx_OrdenProduccion.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                        sp.execute();
                    }else
                        {

                          new popUpGenerico(contexto,v,getString(R.string.error_ingrese_orden_produccion) ,"false" ,true , true);

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
                    new esconderTeclado(Almacen_Armado_Pallets_Polvos_Primera_Y_Ultima.this);
                    return true;
                }
                return false;
            }
        });


        edtx_UltimoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Empaque.getText().toString().equals(""))
                    {

                        if(edtx_OrdenProduccion.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,v ,getString(R.string.error_ingrese_orden_produccion) ,"false" ,true , true);
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_OrdenProduccion.setText("");
                                    edtx_OrdenProduccion.requestFocus();
                                }
                            });
                            return false;
                        }

                        if(edtx_UltimoEmpaque.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto,v ,getString(R.string.error_ingrese_empaque) +" (Primer empaque)."  ,"false" ,true , true);
                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_OrdenProduccion.setText("");
                                        edtx_OrdenProduccion.requestFocus();
                                    }
                                });
                                return false;
                            }

                        SegundoPlano sp = new SegundoPlano("RegistrarEmpaque");
                        sp.execute();
                    }else
                        {
                            new popUpGenerico(contexto,v ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);

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
                    new esconderTeclado(Almacen_Armado_Pallets_Polvos_Primera_Y_Ultima.this);
                    return  true;
                }
                return false;
            }
        });

//        edtx_Empaque.setOnFocusChangeListener(new View.OnFocusChangeListener()
//        {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus)
//            {
//                    if(hasFocus && !edtx_Empaque.getText().toString().equals(""))
//                    {
//                            edtx_Empaque.setText("");
//                    }
//            }
//        });

        edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(edtx_Empaque.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                    }
                                });
                                return false;
                            }

                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_UltimoEmpaque.requestFocus();
                                edtx_UltimoEmpaque.setText("");
                              //  Toast.makeText(contexto, "HOA", Toast.LENGTH_LONG).show();
                            }
                        },10);

                    }
                return false;
            }
        });


        btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               try
                { if (arrayDatosTabla != null)
                    if(!(arrayDatosTabla.size()<=0))
                     {


                         new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener()
                         {
                             public void onClick(DialogInterface dialog, int id) {

                                 SegundoPlano sp = new SegundoPlano("CerrarTarima");
                                 sp.execute();
                                 new esconderTeclado(Almacen_Armado_Pallets_Polvos_Primera_Y_Ultima.this);
                             }
                         },null,contexto);
                     }
                     else
                     {
                         new popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , "false",true , true);
                     }
                }catch(Exception e)
                {
                    new popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
                }
            }
        });

        btn_CancelarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try{
                        if (arrayDatosTabla != null)
                            if (!(arrayDatosTabla.size() <= 0))
                                {
                                    new CreaDialogos(getString(R.string.pregunta_cancelar_pallet), new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id) {

                                            SegundoPlano sp = new SegundoPlano("CancelarTarima");
                                            sp.execute();
                                            new esconderTeclado(Almacen_Armado_Pallets_Polvos_Primera_Y_Ultima.this);
                                        }
                                    },null,contexto);
                                } else
                                {
                                    new popUpGenerico(contexto, vista, getString(R.string.error_cancelar_pallet_sin_empaques), "false", true, true);
                                }
                    }catch(Exception e)
                    {
                        new popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
                    }

            }

        });

        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());
        tabla.addDataLongClickListener(new ListenerLongClickTabla());
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

        if(!recargar)
            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, vista);
                    }
                if ((id == R.id.recargar))
                    {

                        if (!edtx_OrdenProduccion.getText().toString().equals(""))
                            {
                                tabla.getDataAdapter().clear();
                                SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                                sp.execute();
                            }
                    }
            }

        return super.onOptionsItemSelected(item);
    }
    private void ReiniciarVariables(String tarea)
    {
        switch (tarea)
            {
                case "ConsultaOrdenProduccion":
                    edtx_OrdenProduccion.setText("" );
                    txtv_Producto.setText("");
                    txtv_Lote.setText("");
                    txtv_CantidadRegistrada.setText("");
                    txtv_Cantidad.setText("");
                    tabla.getDataAdapter().clear();
                    edtx_OrdenProduccion.requestFocus();
                    break;
                case "ConsultarTarima":

                    tabla.getDataAdapter().clear();
                    edtx_Empaque.setText("");
                    txtv_CantidadEmpaques.setText("-");
                    edtx_Empaque.requestFocus();
                    break;
                case "RegistrarEmpaque":
                    edtx_Empaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "Reiniciar":
                    edtx_OrdenProduccion.setText("" );
                    txtv_Producto.setText("");
                    txtv_Lote.setText("");
                    txtv_Cantidad.setText("");
                    txtv_CantidadRegistrada.setText("");
                    txtv_CantidadEmpaques.setText("-");
                    tabla.getDataAdapter().clear();
                    edtx_OrdenProduccion.requestFocus();
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
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
           EmpaqueBaja = clickedData[0];

            new CreaDialogos("¿Eliminar el empaque [" + EmpaqueBaja + "]?",
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                    SegundoPlano sp = new SegundoPlano("BajaEmpaque");
                    sp.execute();
                }
            },null,contexto);

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
        View view;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {

                recargar = true;
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);

//                handler.postDelayed(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        try {
//                            view = getCurrentFocus();
//                            progressBarHolder.requestFocus();
//                        }catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                },10);

                palletRegistradosVar = 0;
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
                    case "ConsultaOrdenProduccion":
                        sa.SOAPConsultaOrdenProduccion_V2(edtx_OrdenProduccion.getText().toString(),Tipo,contexto );
                        break;
                    case "ConsultarTarima":
                        sa.SOAPConsultaEmpaquesRegistroProduccion(edtx_OrdenProduccion.getText().toString(), contexto);
                        break;
                    case "RegistrarEmpaque":
                        sa.SOAPRegistraEmpaquesEnPalletPrimeraYUltima(edtx_Empaque.getText().toString(),edtx_UltimoEmpaque.getText().toString(),/*"",*/ edtx_OrdenProduccion.getText().toString(),Tipo,"1",contexto );
                        break;
                    case "CerrarTarima":
                        sa.SOAPCerrarRegistroPallet(edtx_OrdenProduccion.getText().toString(),"", contexto);
                        break;
                    case "CancelarTarima":
                        sa.SOAPCancelarRegistroPallet(edtx_OrdenProduccion.getText().toString(),contexto );
                        break;
                     case "BajaEmpaque":
                        sa.SOAPBajaEmpaqueArmadoTarimas(edtx_OrdenProduccion.getText().toString(),EmpaqueBaja,contexto );
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
                e.printStackTrace();
                mensaje = e.getMessage();
                decision = "false";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {

//                handler.postDelayed(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        if(view!=null)
//                        {
//                            try {
//                                view.requestFocus();
//                            }catch (Exception e)
//                            {
//                                e.printStackTrace();
//                                mensaje=e.getMessage();
//                                decision = "false";
//                            }
//                        }
//                    }
//                },10);

                SegundoPlano sp;
                if (decision.equals("true")) {
                    switch (tarea) {

                        case "ConsultaOrdenProduccion":
                            txtv_Lote.setText(LoteOP);
                            txtv_Cantidad.setText(CantidadOP);
                            txtv_Producto.setText(ProductoOP);
                            txtv_CantidadRegistrada.setText(CantidadRegOP);
                            txtv_FechaCaducidad.setText(FechaCaducidadOP);
                              sp = new SegundoPlano("ConsultarTarima");
                                sp.execute();

                            break;

                        case "ConsultarTarima":
                            tabla.getDataAdapter().notifyDataSetChanged();
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Armado_Pallets_Polvos_Primera_Y_Ultima.this, DATA_TO_SHOW));

                                if(arrayDatosTabla!=null)
                               {
                                   cantInt = arrayDatosTabla.size();
                               }else
                               {
                                cantInt = 0;
                               }

                            txtv_CantidadEmpaques.setText(String.valueOf(cantInt));

                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                            edtx_UltimoEmpaque.setText("");
                            edtx_Empaque.setText("");
                            edtx_Empaque.requestFocus();
                            break;
                        case "RegistrarEmpaque":
                            sp = new SegundoPlano("ConsultarTarima");
                            sp.execute();
                            break;

                        case "CerrarTarima":
                            new popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + mensaje + "] con éxito.", decision, true, true);
                            sp = new SegundoPlano("ConsultaOrdenProduccion");
                            sp.execute();
                            if (CantidadOP.equals(CantidadRegOP))
                                {
                                    new popUpGenerico(contexto, vista, getString(R.string.orden_prod_completada_exito), decision, true, true);
                                }
                            //ReiniciarVariables("Reiniciar");
                            break;

                        case "CancelarTarima":
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.cancelar_tarima), decision, true, true);
                            sp = new SegundoPlano("ConsultaOrdenProduccion");
                            sp.execute();
                            break;

                        case "BajaEmpaque":
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), decision, true, true);
                            sp = new SegundoPlano("ConsultarTarima");
                            sp.execute();
                            EmpaqueBaja=null;
                            break;
                    }
                }

                if (decision.equals("false"))
                {

                    ReiniciarVariables(tarea);
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, "false", true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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
                            LoteOP = tabla1.getPrimitivePropertyAsString("Lote");
                            ProductoOP = tabla1.getPrimitivePropertyAsString("Producto");
                            CantidadOP = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                            CantidadRegOP = tabla1.getPrimitivePropertyAsString("CantidadRegistrada");
                            FechaCaducidadOP = tabla1.getPrimitivePropertyAsString("FechaCaducidad");
                            break;
                        case "ConsultarTarima":

                            Log.d("SoapResponse", tabla1.toString());

                            Empaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");


                            d = new datosTabla("", "", Empaque);
                            arrayDatosTabla.add(d);
                            break;
                        case "RegistrarEmpaque":

                            Log.d("SoapResponse", tabla1.toString());

                            Empaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");


                            d = new datosTabla("", "", Empaque);
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
