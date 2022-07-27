package com.automatica.AXCMP.c_Inventarios;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Inventarios_ConfirmarEmpaque extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    EditText edtx_Empaque,edtx_Unidades,edtx_ConfirmarEmpaque,edtx_Lote;

    EditText edtx_Producto;

    TextView txtv_Producto,txtv_Pallet;
    Button btnBaja;
    CheckBox chk_Editar;

    String Pallet;
    String UbicacionIntent,IdInventario;
    Bundle b;
    String Estado, CodigoEmaque,Producto,Cantidad,LoteProveedor;
    TableView tabla;
    String NumeroParte, Revision, Unidades, Lote;
    String Empaque;
    String TipoActividad="RegistraEmpaqueNormal";

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String EmpaqueSeleccionado, EmpaqueConfirmado, CantidadEditada;
    int RenglonSeleccionado;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Estado","Código de Empaque","Producto","Cantidad","Lote"};
    String [][] DATA_TO_SHOW;
    ArrayList<data> allenar;
    /*int renglonSeleccionado;
    int renglonAnterior=-1;*/
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;
    TableColumnDpWidthModel columnModel;
    Handler handler  = new Handler();

    class data
    {
        String Estado, CodigoEmaque,Producto,Cantidad,LoteProveedor;

        public data(String estado, String codigoEmaque, String producto, String cantidad, String loteProveedor)
        {
            Estado = estado;
            CodigoEmaque = codigoEmaque;
            Producto = producto;
            Cantidad = cantidad;
            LoteProveedor = loteProveedor;
        }

        public String getEstado()
        {
            return Estado;
        }

        public String getCodigoEmaque()
        {
            return CodigoEmaque;
        }

        public String getProducto()
        {
            return Producto;
        }

        public String getCantidad()
        {
            return Cantidad;
        }

        public String getLoteProveedor()
        {
            return LoteProveedor;
        }
    }
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.inventarios_activity_confirmar_empaque);
                new cambiaColorStatusBar(contexto, R.color.RojoStd, Inventarios_ConfirmarEmpaque.this);
                //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                DeclararVariables();
                SacaExtrasIntent();
                AgregaListeners();

                SegundoPlano sp = new SegundoPlano("llenarTabla");
                sp.execute();
                txtv_Pallet.setText(Pallet);



                edtx_Lote.setEnabled(false);
                edtx_Producto.setEnabled(false);
                edtx_Unidades.setEnabled(false);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
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

                        reiniciaVariables();
                    }
                if((id == R.id.recargar))
                    {
                        SegundoPlano sp = new SegundoPlano("llenarTabla");
                        sp.execute();
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }


        return super.onOptionsItemSelected(item);
    }
    private  void reiniciaVariables()
    {
        try
            {
                edtx_ConfirmarEmpaque.setText("");

                edtx_Empaque.setText("");
                edtx_Unidades.setText("");

                edtx_Lote.setText("" );
                edtx_Producto.setText("" );
                edtx_Lote.setText("" );

                edtx_Empaque.requestFocus();
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }

    }
    private void DeclararVariables()
    {
        try
            {
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                this.getSupportActionBar().setTitle(" "+getString(R.string.Inventarios_ConfirmaEmpaque));
                //   toolbar.setSubtitle("  Confirmar Empaque");
                toolbar.setLogo(R.mipmap.logo_axc);//  toolbar.setLogo(R.drawable.axc_logo_toolbar);
                progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

                edtx_Empaque = (EditText) findViewById(R.id.edtx_CodigoPallet);
                edtx_Unidades = (EditText) findViewById(R.id.edtx_Unidades);
                edtx_ConfirmarEmpaque = (EditText) findViewById(R.id.edtx_ConfirmarEmpaque);
                edtx_Lote = (EditText) findViewById(R.id.edtx_Lote);
                edtx_Producto = (EditText) findViewById(R.id.edtx_Producto);

                edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                edtx_Unidades.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                edtx_ConfirmarEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

                edtx_Unidades.setEnabled(false);
                edtx_ConfirmarEmpaque.setEnabled(true);

                txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
                txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);

                btnBaja = (Button) findViewById(R.id.btn_Baja);
                btnBaja.setEnabled(false);
                tabla = (TableView) findViewById(R.id.tableView_ConfirmarEmpaque);
                tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, HEADER));
                chk_Editar = (CheckBox) findViewById(R.id.chkb_ConfirmarEmpaque);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }


    }
    private void SacaExtrasIntent()
    {
        try
            {
                b = getIntent().getExtras();
                UbicacionIntent = b.getString("UbicacionIntent");
                IdInventario = b.getString("IdInventario");
                Pallet = b.getString("CodigoPallet");
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }

    }
    private void AgregaListeners()
    {
        try{
            edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {try
                            {

                                if (!edtx_Empaque.getText().toString().equals(""))
                                    {
                                        SegundoPlano sp = new SegundoPlano("ConsultaEmpaque");
                                        sp.execute();

                                    } else
                                    {
                                        Handler handler = new Handler();
                                        handler.post(
                                                new Runnable()
                                                {
                                                    public void run()
                                                    {
                                                        edtx_Empaque.setText("");
                                                        edtx_Empaque.requestFocus();
                                                    }
                                                }
                                        );
                                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque), "Advertencia", true, true);
                                    }
                                new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "Advertencia", true, true);

                            }
                        }
                    return false;
                }
            });
            edtx_Unidades.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            if(!edtx_Unidades.getText().toString().equals(""))
                                {
                                    edtx_ConfirmarEmpaque.setEnabled(true);
                                    edtx_ConfirmarEmpaque.requestFocus();

                                }else
                                {
                                    Handler handler = new Handler();
                                    handler.post(
                                            new Runnable()
                                            {
                                                public void run()
                                                {
                                                    edtx_Unidades.setText("");
                                                    edtx_Unidades.requestFocus();
                                                }
                                            }
                                    );
                                    new popUpGenerico(contexto,vista,getString(R.string.ingrese_cantidad),"Advertencia",true,true);
                                }
                            new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                        }
                    return false;
                }
            });
            edtx_ConfirmarEmpaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            try
                                {
                                    if(edtx_Empaque.getText().toString().equals(edtx_Empaque.getText().toString()))
                                        {
                                            SegundoPlano sp = new SegundoPlano(TipoActividad);
                                            sp.execute();

                                        }else
                                        {
                                            Handler handler = new Handler();
                                            handler.post(
                                                    new Runnable()
                                                    {
                                                        public void run()
                                                        {
                                                            edtx_ConfirmarEmpaque.setText("");
                                                            edtx_ConfirmarEmpaque.requestFocus();
                                                        }
                                                    }
                                            );
                                            new popUpGenerico(contexto,vista,getString(R.string.empaques_no_coinciden),"Advertencia",true,true);
                                        }
                                    new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "Advertencia", true, true);

                                }
                        }
                    return false;
                }
            });

            btnBaja.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                        {
                            new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                                    .setTitle("¿Dar de baja empaque?").setCancelable(false)
                                    .setPositiveButton("Si", new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id) {
                                            SegundoPlano sp = new SegundoPlano("bajaPallet");
                                            sp.execute();
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "Advertencia", true, true);

                        }
                }
            });

            tabla.addDataClickListener(new ListenerClickTabla());

            chk_Editar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                        {
                            //edtx_Unidades.setEnabled(true);
                            TipoActividad ="EditarEmpaque";

                            // edtx_Producto.setText("");
//                    edtx_Lote.setText("");
//                    edtx_Unidades.setText("");

                            edtx_Unidades.setEnabled(true);
                            edtx_Lote.setEnabled(true);
                            edtx_Producto.setEnabled(true);

                        }else if(!isChecked)
                        {
                            TipoActividad = "RegistraEmpaqueNormal";

                            edtx_Producto.setText("");
                            edtx_Lote.setText("");
                            edtx_Unidades.setText("");

                            edtx_Empaque.setText("");
                            edtx_Empaque.requestFocus();

                            edtx_Unidades.setEnabled(false);
                            edtx_Lote.setEnabled(false);
                            edtx_Producto.setEnabled(false);

                        }
                }
            });


        }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }

    }
    public void ajustarTamañoColumnas()
    {
//        float Dp = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.txtsize),
//                contexto.getResources().getDisplayMetrics() );
//        int size1=0,size2=0,size3=0,size4=0,size5=0,size6=0;
//        for(data a : allenar)
//            {
//                int i =0;
//                if(a.getEstado()!=null)
//                    if(a.getEstado().length()>=size1)
//                        {
//                            size1 = a.getEstado().length();
//                            if(HEADER[i].length()>size1)
//                                {
//                                    size1 = HEADER[i].length();
//                                }
//                        }
//                i++;
//                if(a.getCodigoEmaque()!=null)
//                    if(a.getCodigoEmaque().length()>=size2)
//                        {
//                            size2 = a.getCodigoEmaque().length();
//                            if(HEADER[i].length()>size2)
//                                {
//                                    size2 = HEADER[i].length();
//                                }
//                        }
//                i++;
//                if(a.getProducto()!=null)
//                    if(a.getProducto().length()>=size3)
//                        {
//                            size3 = a.getProducto().length();
//                            if(HEADER[i].length()>size3)
//                                {
//                                    size3 = HEADER[i].length();
//                                }
//                        }
//                i++;
//                if(a.getCantidad()!=null)
//                    if(a.getCantidad().length()>=size4)
//                        {
//                            size4 = a.getCantidad().length();
//                            if(HEADER[i].length()>size4)
//                                {
//                                    size4 = HEADER[i].length();
//                                }
//                        }
//                i++;
//                if(a.getLoteProveedor()!=null)
//                    if(a.getLoteProveedor().length()>=size5)
//                        {
//                            size5 = a.getLoteProveedor().length();
//                            if(HEADER[i].length()>size5)
//                                {
//                                    size5 = HEADER[i].length();
//                                }
//                        }
//
//            }
//        //Numeros elegidos con prueba y error
      //  columnModel = new TableColumnDpWidthModel(contexto, 5, 300);
//        columnModel.setColumnWidth(0,Math.round(10+size1*Dp/5));
//        columnModel.setColumnWidth(1,Math.round(10+size2*Dp/5));
//        columnModel.setColumnWidth(2,Math.round(10+size3*Dp/5));
//        columnModel.setColumnWidth(3,Math.round(10+size4*Dp/5));
//        columnModel.setColumnWidth(4,Math.round(10+size5*Dp/5));
//
//
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size1*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size2*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size3*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size4*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size5*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size6*Dp/5));


        // tabla.setMinimumWidth(size1+size2+size3+size4+size5+size6);

    //    int d = Math.round(10+size1*Dp/5)+Math.round(10+size2*Dp/5)+Math.round(10+size3*Dp/5)+Math.round(10+size4*Dp/5)+Math.round(10+size5*Dp/5)+Math.round(10+size6*Dp/5);

//        View vista = findViewById(R.id.LL);
//        vista.setMinimumWidth(d+100);

        //vista.setLayoutParams(new FrameLayout.LayoutParams(d, FrameLayout.LayoutParams.MATCH_PARENT));

        //Math.round(10+size1*Dp/5)+Math.round(10+size2*Dp/5)+Math.round(10+size3*Dp/5)+Math.round(10+size4*Dp/5)+Math.round(10+size5*Dp/5)+Math.round(10+size6*Dp/5);
/*
        tabla.setLayoutParams(new LinearLayout.LayoutParams(d, LinearLayout.LayoutParams.MATCH_PARENT));
        tabla.setLayoutParams(new LinearLayout.LayoutParams(d, 12));*/
      //  Log.d("SoapResponse", "ajustarTamañoColumnas: " + Integer.valueOf(d));
    }
    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            try{
                RenglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                btnBaja.setEnabled(true);
                EmpaqueSeleccionado= clickedData[1];

                if(renglonAnterior != rowIndex)
                    {

                        renglonAnterior = rowIndex;
                        renglonSeleccionado = rowIndex;

                        tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                        tabla.getDataAdapter().notifyDataSetChanged();

                        EmpaqueSeleccionado= clickedData[1];
                        btnBaja.setEnabled(true);
                        btnBaja.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));



                    }else if(renglonAnterior == rowIndex)
                    {
                        renglonSeleccionado = -1;
                        renglonAnterior=-1;

                        tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                        tabla.getDataAdapter().notifyDataSetChanged();


                        btnBaja.setEnabled(false);
                        EmpaqueSeleccionado= clickedData[1];
                        btnBaja.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));

                    }
            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }


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
    private class cambiaColorTablaSeleccionada implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {
            if(rowIndex == renglonSeleccionado)
                {
                    Color = R.color.AzulStd;
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));


                }
            else
                {
                    Color = R.color.Transparente;
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                }


           /* if(RenglonSeleccionado== rowIndex )
            {
                Color = R.color.RengSelStd;
            }
            else
            {
                Color = R.color.blancoLetraStd;
            }*/
            return new ColorDrawable(getResources().getColor(Color));
        }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea, decision, mensaje;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            try{
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            //Pallet = edtx_Pallet.getText().toString();
          /*
            Cantidad = edtx_Cantidad.getText().toString();
            Lote =*/
            Empaque = edtx_Empaque.getText().toString();
            try {
                switch (tarea) {
                    case "llenarTabla":

                        sa.SOAPConsultaEmpaquePorPalletInventario(IdInventario, Pallet, contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();
                        if (decision.equals("true")) sacaDatos("llenarTabla");

                        break;

                    case "ConsultaEmpaque":
                        if (!Empaque.equals(""))
                            {
                                sa.SOAPConsultaEmpaqueInventario(IdInventario, Empaque, contexto);
                                decision = sa.getDecision();
                                mensaje = sa.getMensaje();
                                if (decision.equals("true")) sacaDatos("ConsultaEmpaque");
                            }
                        break;
                    case "RegistrarNuevoEmpaque":

                        if (!edtx_Empaque.getText().toString().equals(edtx_ConfirmarEmpaque.getText().toString()))
                            {
                                decision = "false";
                                mensaje = getString(R.string.empaques_no_coinciden);
                                break;
                            }

                        EmpaqueConfirmado = edtx_ConfirmarEmpaque.getText().toString();
                        CantidadEditada = edtx_Unidades.getText().toString();
                        //sa.SOAPRegistraNuevoEmpaqueInventario(IdInventario, Pallet, Empaque, CantidadEditada, UbicacionIntent, contexto);
                        sa.SOAPRegistraNuevoEmpaqueInventarioConLote(IdInventario, Pallet,edtx_Producto.getText().toString(), Empaque, CantidadEditada,edtx_Lote.getText().toString(), UbicacionIntent, contexto);

                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();

                        break;
                    case "RegistraEmpaqueNormal":

                        if (!edtx_Empaque.getText().toString().equals(edtx_ConfirmarEmpaque.getText().toString()))
                            {
                                mensaje = getString(R.string.empaques_no_coinciden);
                                decision = "false";
                                return null;
                            }

                        sa.SOAPRegistraEmpaqueInventario(IdInventario, Pallet, Empaque, UbicacionIntent, contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();

                        break;
                    case "bajaPallet":

                        sa.SOAPBajaEmpaqueInventario(IdInventario, EmpaqueSeleccionado, contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();

                        break;
                    case "EditarEmpaque":


                        if (!edtx_Empaque.getText().toString().equals(edtx_ConfirmarEmpaque.getText().toString()))
                            {
                                mensaje = getString(R.string.empaques_no_coinciden);
                                decision = "false";
                                return null;
                            }

                        EmpaqueConfirmado = edtx_ConfirmarEmpaque.getText().toString();
                        CantidadEditada = edtx_Unidades.getText().toString();
                        //sa.SOAPEditaRegistroEmpaqueInventario(IdInventario, Pallet, EmpaqueConfirmado, UbicacionIntent, CantidadEditada, contexto);
                        sa.SOAPEditaRegistroEmpaqueInventarioConLote(IdInventario, Pallet, EmpaqueConfirmado, UbicacionIntent, CantidadEditada,edtx_Lote.getText().toString(), contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();
                        break;
                }

                if (decision.equals("true") && tarea.equals("llenarTabla"))
                    {

                        int i = 0;
                        DATA_TO_SHOW = new String[allenar.size()][5];
                        for (data a : allenar) {
                            DATA_TO_SHOW[i][0] = a.getEstado();
                            DATA_TO_SHOW[i][1] = a.getCodigoEmaque();
                            DATA_TO_SHOW[i][2] = a.getProducto();
                            DATA_TO_SHOW[i][3] = a.getCantidad();
                            DATA_TO_SHOW[i][4] = a.getLoteProveedor();

                            i++;
                        }
                    }
            } catch (Exception e)
                {
                    mensaje = e.getMessage();
                    decision = "false";
                    e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try{
                SegundoPlano sp;
                if (decision.equals("true"))
                    {
                        switch (tarea)
                            {

                                case "llenarTabla":
                                    tabla.setDataAdapter(st =new SimpleTableDataAdapter(Inventarios_ConfirmarEmpaque.this, DATA_TO_SHOW));
                                    tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                                    ajustarTamañoColumnas();
                                    columnModel = new TableColumnDpWidthModel(contexto, 5, 150);
                                    columnModel.setColumnWidth(1,200);
                                    columnModel.setColumnWidth(4,200);
                                    tabla.setColumnModel(columnModel);
                                    tabla.getDataAdapter().notifyDataSetChanged();
                                    allenar.clear();
                                    edtx_Empaque.requestFocus();


                                    break;

                                case "ConsultaEmpaque":
                                    if (decision.equals("true"))
                                        {
                                            edtx_Unidades.setText(Unidades);
                                            edtx_Producto.setText(NumeroParte);
                                            edtx_Lote.setText(Lote);
                                            edtx_ConfirmarEmpaque.requestFocus();
                                            //    chk_Editar.setEnabled(true);
                                        }

                                    break;
                                case "RegistrarNuevoEmpaque":
                                    new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.empaque_alta), decision, true, true);
                                    sp = new SegundoPlano("llenarTabla");
                                    sp.execute();
                                    edtx_ConfirmarEmpaque.setText("");
                                    edtx_Empaque.setText("" );
                                    edtx_Producto.setText("" );
                                    edtx_Unidades.setText("");
                                    edtx_Lote.setText("");
                                    edtx_Empaque.requestFocus();
                                    break;
                                case "RegistraEmpaqueNormal":
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_alta), decision, true, true);
                                    sp = new SegundoPlano("llenarTabla");
                                    sp.execute();
                                    edtx_ConfirmarEmpaque.setText("");
                                    edtx_Empaque.setText("" );
                                    edtx_Producto.setText("" );
                                    edtx_Unidades.setText("");
                                    edtx_Lote.setText("");
                                    edtx_Empaque.requestFocus();
                                    break;
                                case "bajaPallet":
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), decision, true, true);
                                    sp = new SegundoPlano("llenarTabla");
                                    sp.execute();

                                    break;
                                case "EditarEmpaque":
                                    new popUpGenerico(contexto, getCurrentFocus(),  getString(R.string.empaque_alta), decision, true, true);
                                    edtx_ConfirmarEmpaque.setText("");
                                    edtx_Empaque.setText("");
                                    edtx_Producto.setText("");
                                    edtx_Unidades.setText("");
                                    edtx_Lote.setText("");
                                    edtx_Empaque.requestFocus();
                                    sp = new SegundoPlano("llenarTabla");
                                    sp.execute();
                                    break;


                            }

                    }


                chk_Editar.setChecked(false);
                if (decision.equals("false"))
                    {

                        if (tarea.equals("ConsultaEmpaque") && mensaje.equals("0"))
                            {
                                new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                                        .setTitle("Empaque no encontrado, ¿Dar de alta?").setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                TipoActividad = "RegistrarNuevoEmpaque";

                                                edtx_Unidades.setEnabled(true);
                                                edtx_Lote.setEnabled(true);
                                                edtx_Producto.setEnabled(true);

                                                edtx_Producto.requestFocus();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            } else
                        if(tarea.equals("llenarTabla"))
                            {
                                edtx_Empaque.requestFocus();
                            }
                        else{
                            new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                            //reiniciaVariables();
                            edtx_ConfirmarEmpaque.setText("");
                        }
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
        }
    }
    private void sacaDatos(String tarea)
    {
        try{
            SoapObject tabla = sa.parser();


            allenar = new ArrayList<>();
            data d;
            if(tabla!=null)
                for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        try {
                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            switch(tarea)
                                {
                                    case "llenarTabla":

                                        Log.d("SoapResponse", tabla1.toString());

                                        Producto = tabla1.getPrimitivePropertyAsString("Producto");

                                        if(Producto == null)
                                            {

                                            }

                                        Estado = tabla1.getPrimitivePropertyAsString("Estado");
                                        CodigoEmaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");
                                        Cantidad = tabla1.getPrimitivePropertyAsString("Cantidad");
                                        LoteProveedor = tabla1.getPrimitivePropertyAsString("Lote");
                                        d = new data(Estado, CodigoEmaque, Producto, Cantidad, LoteProveedor);
                                        allenar.add(d);
                                        break;
                                    case "ConsultaEmpaque":
                                        NumeroParte= tabla1.getPrimitivePropertyAsString("NumeroParte");
                                        Revision= tabla1.getPrimitivePropertyAsString("Revision");
                                        Unidades= tabla1.getPrimitivePropertyAsString("Unidades");
                                        Lote= tabla1.getPrimitivePropertyAsString("Lote");
                                        break;
                                    case "RegistrarEmpaqueNormal":


                                        break;
                                }
                        } catch (Exception e)
                            {

                                e.printStackTrace();
                            }

                    }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

