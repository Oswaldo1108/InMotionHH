package com.automatica.AXCMP.c_Surtido;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import com.automatica.AXCMP.Servicios.CreaDialogos;
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

public class Surtido_Valida extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_OrdenProd,edtx_CodigoEmpaque,edtx_CodigoMaquina;
    Button btn_ValidarTarima,btn_CerrarOrden;
    TextView txtv_CantEmpReg;
    SortableTableView tabla_Salidas,tabla_OrdenProd;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    SimpleTableDataAdapter st,st2;

    ConstraintLayout cl_TablaRegistro, cl_OrdenProdVal;
    String[] HEADER = {"Empaque", "Producto", "Lote", "Cantidad Actual"};
    String[] HEADER2 = {"Partida", "Producto", "Cantidad Total", "Cantidad Surtida","Pendiente"};

    String[][] DATA_TO_SHOW;
    int renglonSeleccionado;
    int renglonAnterior = -1;

    int renglonSeleccionado2;
    int renglonAnterior2 = -1;
    Boolean seleccionado, ReiniciarTabla = false;
    ArrayList<datosTabla> arrayDatosTabla;
    boolean recargar;
    class datosTabla
    {
        String CodigoEmpaque, Producto, Cantidad, Lote,Tag1;

        public datosTabla(String CodigoEmpaque, String producto, String Lote, String cantidad,String Tag1)
        {
            this.CodigoEmpaque = CodigoEmpaque;
            Producto = producto;
            Cantidad = cantidad;
            this.Lote = Lote;
            this.Tag1 = Tag1;
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

        public String getTag1()
        {
            return Tag1;
        }
    }
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_activity_valida);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Valida.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declararVariables();
        agregaListeners();
    }
    private void declararVariables()
    {
        try {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.recepcion_surtido_validacion));
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            edtx_OrdenProd= (EditText) findViewById(R.id.edtx_OrdenProd);
            edtx_CodigoMaquina= (EditText) findViewById(R.id.edtx_CodigoMaquina);
            edtx_CodigoEmpaque= (EditText) findViewById(R.id.edtx_CodigoEmpaque);

            edtx_OrdenProd.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoMaquina.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            btn_ValidarTarima = (Button) findViewById(R.id.btn_CerrarTarima);
            btn_CerrarOrden = (Button) findViewById(R.id.btn_CerrarOrden);

            txtv_CantEmpReg = (TextView) findViewById(R.id.txtv_CantEmpReg);

            cl_OrdenProdVal = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);
            cl_TablaRegistro  = (ConstraintLayout) findViewById(R.id.cl_ValidacionEmpaques);

            cl_TablaRegistro.setVisibility(View.VISIBLE);
            cl_OrdenProdVal.setVisibility(View.GONE);



            tabla_Salidas = (SortableTableView) findViewById(R.id.tableView_Salida);
            tabla_OrdenProd = (SortableTableView) findViewById(R.id.tableView_OrdenProd);

            tabla_Salidas.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, HEADER));
            tabla_OrdenProd.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, HEADER2));

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }

    }
    private void agregaListeners()
    {
        try {
            edtx_OrdenProd.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        if (!edtx_OrdenProd.getText().toString().equals(""))
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    edtx_CodigoMaquina.requestFocus();
                                   // edtx_OrdenProd.setText("");
                                }
                            });




                            if (cl_OrdenProdVal.getVisibility() == View.VISIBLE)
                                {
                                    SegundoPlano sp = new SegundoPlano("ConsultaOrden");
                                    sp.execute();

                                } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
                                {

                                    SegundoPlano sp = new SegundoPlano("ConsultaOrdenAValidar");
                                    sp.execute();

                                }



//
//                            SegundoPlano sp = new SegundoPlano("ConsultaOrden");
//                            sp.execute();

                        } else {
                            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    edtx_OrdenProd.requestFocus();
                                    edtx_OrdenProd.setText("");

                                }
                            });
                        }
                        new esconderTeclado(Surtido_Valida.this);
                    }

                    return false;
                }
            });

            edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_CodigoEmpaque.getText().toString().equals(""))
                        {
                            if (edtx_OrdenProd.getText().toString().equals(""))
                            {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_OrdenProd.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                                return false;
                            }

                            if (edtx_CodigoMaquina.getText().toString().equals(""))
                            {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_CodigoMaquina.requestFocus();
                                        edtx_CodigoMaquina.setText("");
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_maquina), "false", true, true);
                                return false;
                            }

                            SegundoPlano sp = new SegundoPlano("ValidarEmpaque");
                            sp.execute();

                        } else
                            {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    edtx_CodigoEmpaque.requestFocus();
                                }
                            });
                        }
                        new esconderTeclado(Surtido_Valida.this);
                    }

                    return false;
                }
            });


            edtx_CodigoMaquina.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_CodigoMaquina.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_CodigoEmpaque.requestFocus();
                                }
                            });
                        } else {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_maquina), "false", true, true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    edtx_CodigoMaquina.requestFocus();
                                }
                            });
                        }
                        new esconderTeclado(Surtido_Valida.this);
                    }

                    return false;
                }
            });

            btn_ValidarTarima.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (edtx_OrdenProd.getText().toString().equals(""))
                    {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_OrdenProd.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                        return;
                    }


                    new CreaDialogos("¿Validar pallet?",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    SegundoPlano sp = new SegundoPlano("ValidarPallet");
                                    sp.execute();
                                }
                            },
                            null,contexto);
                    new esconderTeclado(Surtido_Valida.this);
                }
            });


            btn_CerrarOrden.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (edtx_OrdenProd.getText().toString().equals(""))
                    {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_OrdenProd.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                        return;
                    }


                    new CreaDialogos("¿Cerrar Orden de producción?",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    SegundoPlano sp = new SegundoPlano("CerrarOrden");
                                    sp.execute();
                                }
                            },
                            null,contexto);
                    new esconderTeclado(Surtido_Valida.this);
                }
            });





            tabla_Salidas.addDataClickListener(new ListenerClickTabla());
            tabla_Salidas.addDataLongClickListener(new ListenerLongClickTabla());
            tabla_Salidas.addHeaderClickListener(new headerClickListener());

//            tabla_OrdenProd.addDataClickListener(new ListenerClickTablaOrdenProd());
//            tabla_OrdenProd.addDataLongClickListener(new ListenerLongClickTabla());
//            tabla_OrdenProd.addHeaderClickListener(new headerClickListener());


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
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
    private void ReiniciarVariables(String tarea)
    {
        try {
//            edtx_OrdenProd.requestFocus();
//            edtx_CodigoEmpaque.setText("");
//            edtx_CodigoMaquina.setText("");
//         //   txTV_CantEmpReg.setTexto("")
            switch (tarea) {
                case "ValidarEmpaque":

                    edtx_CodigoEmpaque.setText("");
                    edtx_CodigoEmpaque.requestFocus();
                    break;
                case "ConsultaOrden":

                    edtx_OrdenProd.setText("");
                   // edtx_CodigoMaquina.setText("");
                    edtx_CodigoEmpaque.setText("");
                    tabla_Salidas.getDataAdapter().clear();
                    ReiniciarTabla = true;
                    tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                    tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                    txtv_CantEmpReg.setText("-");
                    edtx_OrdenProd.requestFocus();
                    break;

                case "ValidarPallet":


                 //   edtx_OrdenProd.setText("");
                    edtx_CodigoMaquina.setText("");
                    edtx_CodigoEmpaque.setText("");
                    tabla_Salidas.getDataAdapter().clear();
                    tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                    ReiniciarTabla = true;
                    tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                    txtv_CantEmpReg.setText("-");
                    edtx_OrdenProd.requestFocus();

                    break;

                case "ConsultaOrdenAValidar":

                    //edtx_OrdenProd.setText("");
                    //edtx_CodigoEmpaque.setText("");
                    tabla_OrdenProd.getDataAdapter().clear();
                    ReiniciarTabla = true;
                    tabla_OrdenProd.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                    tabla_OrdenProd.getDataAdapter().notifyDataSetChanged();
                    txtv_CantEmpReg.setText("-");
                    //edtx_OrdenProd.requestFocus();
                    break;


            }

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try
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
                ReiniciarVariables("ValidarPallet");
            }



            if ((id == R.id.recargar))
                {
                    if (cl_OrdenProdVal.getVisibility() == View.VISIBLE)
                        {


                            if(edtx_OrdenProd.getText().toString().equals(""))
                                {
                                    return false;
                                }
                            SegundoPlano sp = new SegundoPlano("ConsultaOrden");
                            sp.execute();

                        } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
                        {

                            if(edtx_OrdenProd.getText().toString().equals(""))
                                {
                                    return false;
                                }
                            SegundoPlano sp = new SegundoPlano("ConsultaOrdenAValidar");
                            sp.execute();

                        }
                }

            if ((id == R.id.CambiarVista))
            {
                if (cl_OrdenProdVal.getVisibility() == View.VISIBLE)
                {

                    cl_OrdenProdVal.setVisibility(View.GONE);
                    cl_TablaRegistro.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_add_box);
                    item.setChecked(true);
                    if(edtx_OrdenProd.getText().toString().equals(""))
                    {
                        return false;
                    }
                    SegundoPlano sp = new SegundoPlano("ConsultaOrden");
                    sp.execute();

                } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
                {
                    cl_TablaRegistro.setVisibility(View.GONE);
                    cl_OrdenProdVal.setVisibility(View.VISIBLE);
                    item.setIcon(R.drawable.ic_change_view);

                    if(edtx_OrdenProd.getText().toString().equals(""))
                    {
                        return false;
                    }
                    SegundoPlano sp = new SegundoPlano("ConsultaOrdenAValidar");
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
//                btn_CerrarTarima.setTextColor(getApplication().getResources().getColor(R.color.negroLetrastd));

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
                st.setTextColor(getResources().getColor(R.color.negroLetrastd));

            }

            return new ColorDrawable(getResources().getColor(Color));
        }
    }

    private class cambiaColorTablaClearStatus implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {
//         Color = R.color.Transparente;
//         st.setTextColor(getResources().getColor(R.color.negroLetrastd));
            if(rowData[6].equals(String.valueOf(3)))
                {
                    Color = R.color.VerdeRenglon;
                }
            else //if(rowData[6].equals(String.valueOf(2)))
                {
                    Color = R.color.AmarilloRenglon;
                }
//            else
//                {
//
//                    Color = R.color.Transparente;
//                }
            st.setTextColor(getResources().getColor(R.color.negroLetrastd));
            return new ColorDrawable(getResources().getColor(Color));
        }
    }
    private class ListenerClickTablaOrdenProd implements TableDataClickListener<String[]>
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
                {

                    renglonAnterior2 = rowIndex;
                    renglonSeleccionado2 = rowIndex;
                    tabla_OrdenProd.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionadaStatus());
                    tabla_OrdenProd.getDataAdapter().notifyDataSetChanged();
//                PalletSeleccionadoTabla = clickedData[1];
//                btn_CerrarTarima.setTextColor(getApplication().getResources().getColor(R.color.negroLetrastd));

                }else if(renglonAnterior == rowIndex)
                {
                    renglonSeleccionado2 = -1;
                    tabla_OrdenProd.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionadaStatus());
                    tabla_OrdenProd.getDataAdapter().notifyDataSetChanged();
                    // PalletSeleccionadoTabla = null;
                    renglonAnterior2=-1;
                }
            ReiniciarTabla = false;
        }

    }
    private class cambiaColorTablaSeleccionadaStatus implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {

            Log.i("SoapResponse", "getRowBackground: "+ rowData[1]);
            if(rowIndex == renglonSeleccionado)
                {
                    Color = R.color.RengSelStd;
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                }
            else
                {
//            Color = R.color.Transparente;
//            st.setTextColor(getResources().getColor(R.color.negroLetrastd));
                    if(rowData[6].equals(String.valueOf(5)))
                        {
                            Color = R.color.VerdeRenglon;
                        }
                    else //if(rowData[6].equals(String.valueOf(2)))
                        {
                            Color = R.color.AmarilloRenglon;
                        }
//            else
//                {
//
//                    Color = R.color.Transparente;
//                }
                    st.setTextColor(getResources().getColor(R.color.negroLetrastd));

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
            st.setTextColor(getResources().getColor(R.color.negroLetrastd));
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
            try
            {
                switch (tarea)
                {
                    case "ConsultaOrden":
                          sa.SOAPConsultaOrdenProduccionValida(edtx_OrdenProd.getText().toString(),contexto);
                        break;
                    case "ValidarEmpaque":
                          sa.SOAPValidaEmpaque(edtx_CodigoEmpaque.getText().toString(),edtx_OrdenProd.getText().toString(),edtx_CodigoMaquina.getText().toString(),contexto);
                        break;
                    case "ValidarPallet":
                         sa.SOAPCierreValidacionPallet(edtx_OrdenProd.getText().toString(),contexto);
                        break;
                    case "CerrarOrden":
                        sa.SOAPCerrarOrdenProduccionValidacion(edtx_OrdenProd.getText().toString(),contexto);
                        break;
                    case "ConsultaOrdenAValidar":
                        sa.SOAPConsultaPartidasOrdenProduccionValidacion(edtx_OrdenProd.getText().toString(),contexto);
                        break;
                }
                mensaje = sa.getMensaje();
                decision = sa.getDecision();

                if (decision.equals("true"))
                {
                    sacaDatos(tarea);
                    int i = 0;
                    DATA_TO_SHOW = new String[arrayDatosTabla.size()][5];
                    for (datosTabla a : arrayDatosTabla)
                    {
                        DATA_TO_SHOW[i][0] = a.getCodigoEmpaque();
                        DATA_TO_SHOW[i][1] = a.getProducto();
                        DATA_TO_SHOW[i][2] = a.getLote();
                        DATA_TO_SHOW[i][3] = a.getCantidad();
                        DATA_TO_SHOW[i][4] = a.getTag1();

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
                recargar = false;
                if (decision.equals("true"))
                {
                    switch (tarea)
                    {
                        case "ValidarEmpaque":
                            SegundoPlano sp = new SegundoPlano("ConsultaOrden");
                            sp.execute();
                            edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();
                            break;
                        case "ConsultaOrden":
                            tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                            tabla_Salidas.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_Valida.this, DATA_TO_SHOW));
                            ReiniciarTabla = true;
                            tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                            if(arrayDatosTabla !=null)
                            {
                                txtv_CantEmpReg.setText(String.valueOf(arrayDatosTabla.size()));
                            }else
                            {
                                txtv_CantEmpReg.setText("0");
                            }
                            if(edtx_CodigoMaquina.getText().toString().equals(""))
                            {
                                edtx_CodigoMaquina.requestFocus();
                            }
                            else
                            {
                                edtx_CodigoEmpaque.requestFocus();
                            }
                            break;
                        case "ValidarPallet":
                            ReiniciarVariables(tarea);
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_registrado_exito), decision, true, true);
                            break;

                        case "CerrarOrden":
                            ReiniciarVariables(tarea);
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_prod_completada_exito), decision, true, true);
                            break;

                        case "ConsultaOrdenAValidar":
                            tabla_OrdenProd.getDataAdapter().notifyDataSetChanged();
                            tabla_OrdenProd.setDataAdapter(st2 = new SimpleTableDataAdapter(Surtido_Valida.this, DATA_TO_SHOW));
                           // tabla_OrdenProd.setDataRowBackgroundProvider(new cambiaColorTablaClearStatus());
                            break;

                    }

                }
                if (decision.equals("false"))
                {
                    ReiniciarVariables(tarea);
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
                        case "ConsultaOrden":
                            Log.d("SoapResponse", tabla1.toString());

                            arrayDatosTabla.add(new datosTabla(tabla1.getPrimitivePropertyAsString("Empaque"),
                                    tabla1.getPrimitivePropertyAsString("Producto"),
                                    tabla1.getPrimitivePropertyAsString("Lote"),
                                    tabla1.getPrimitivePropertyAsString("Cantidad"),
                                    null));
                            break;

                        case "ConsultaOrdenAValidar":


                            Log.d("SoapResponse", tabla1.toString());

                            arrayDatosTabla.add(new datosTabla(tabla1.getPrimitivePropertyAsString("Partida"),
                                    tabla1.getPrimitivePropertyAsString("Producto"),
                                    tabla1.getPrimitivePropertyAsString("Solicitado"),
                                    tabla1.getPrimitivePropertyAsString("Surtido"),
                                    tabla1.getPrimitivePropertyAsString("Pendiente")));
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

