package com.automatica.AXCMP.c_Inventarios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Inventarios_InventiarioCiclicoPorPosicionDet extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText CodigoUbicacion;
    TableView tabla;
    Button btnNuevo,btnBaja,btnPallet;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView PalletsRegistrados;
    TableColumnDpWidthModel columnModel;
    View vista;
    Context contexto = this;
    Bundle b ;
    String UbicacionIntent,IdInventario;
    String PalletSeleccionadoTabla;
    String Actividad;
    String[] HEADER = {"Tipo","Código de Pallet","Producto","Lote","Cantidad Actual","Empaques"};
    String[][] DATA_TO_SHOW;
    int palletRegistradosVar = 0;
    ArrayList<datosTabla> arrayDatosTabla;

    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;
    Boolean primerSeleccion = false;
    Boolean seleccionado;

    class datosTabla
    {
        String Tipo,IdStatus,CodigoPallet,Producto,Lote,CantidadActual,Empaques;

        public datosTabla(String tipo, String idStatus, String codigoPallet, String producto, String lote, String cantidadActual, String empaques)
        {
            Tipo = tipo;
            IdStatus = idStatus;
            CodigoPallet = codigoPallet;
            Producto = producto;
            Lote = lote;
            CantidadActual = cantidadActual;
            Empaques = empaques;
        }

        public String getTipo()
        {
            return Tipo;
        }

        public String getIdStatus()
        {
            return IdStatus;
        }

        public String getCodigoPallet()
        {
            return CodigoPallet;
        }

        public String getProducto()
        {
            return Producto;
        }

        public String getLote()
        {
            return Lote;
        }

        public String getCantidadActual() {
            return CantidadActual;
        }

        public String getEmpaques() {
            return Empaques;
        }
    }
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarios_activity_inventiario_ciclico_por_posicion_det);

        new cambiaColorStatusBar(contexto, R.color.RojoStd,Inventarios_InventiarioCiclicoPorPosicionDet.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();
        CodigoUbicacion.setText(UbicacionIntent);

        SegundoPlano sp;

        if(!Actividad.equals("llenarTabla"))
        {
            sp = new SegundoPlano(Actividad);
            sp.execute();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         //   btnNuevo.setEnabled(false);
            //toolbar.setSubtitle("  " +"Cíclico por Lote" /*Actividad*/);
            this.getSupportActionBar().setTitle(" "+Actividad);
            if(Actividad.equals("CiclicoPorLote"))
            {
                this.getSupportActionBar().setTitle(" "+getString(R.string.Inventarios_CiclicoPorLote));

            }
            if(Actividad.equals("Fisico Total"))
            {
                this.getSupportActionBar().setTitle(" "+getString(R.string.Inventarios_FisicoTotal));

            }
            if(Actividad.equals("Producto sin Ubicacion"))
            {
                this.getSupportActionBar().setTitle(" "+getString(R.string.Inventarios_CiclicoPorProducto));
            }


        }
        else
        {
            if(UbicacionIntent!=null)
            {
                sp = new SegundoPlano("llenarTabla");
                sp.execute();
            }
            toolbar.setSubtitle("  Inventario ");
        }


    }
    private void SacaExtrasIntent()
    {

       b = getIntent().getExtras();
       UbicacionIntent = b.getString("UbicacionIntent");
       IdInventario = b.getString("IdInventario");
       Actividad = b.getString("Actividad");
    }
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      //  toolbar.setLogo(R.mipmap.logo_axc);// toolbar.setLogo(R.drawable.axc_logo_toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        CodigoUbicacion = (EditText) findViewById(R.id.edtx_Cantidad);
        CodigoUbicacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        tabla = (TableView) findViewById(R.id.tableView_CiclicoUbicacion);
        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        PalletsRegistrados = (TextView) findViewById(R.id.txtv_PalletsRegistrados);
        btnBaja = findViewById(R.id.btn_Baja);
         btnBaja.setEnabled(false);
        btnNuevo = findViewById(R.id.btn_Nuevo);
        btnPallet = findViewById(R.id.btn_CerrarTarima);
        btnPallet.setEnabled(true);

    }
    private void agregaListeners()
    {
        CodigoUbicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(CodigoUbicacion.getText().toString().equals(""))
                    {

                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_ubicacion),"false",true,true);
                        CodigoUbicacion.requestFocus();
                        CodigoUbicacion.setText("");
                        return false;
                    }
                    UbicacionIntent = CodigoUbicacion.getText().toString();
                    SegundoPlano sp = new SegundoPlano("llenarTabla");
                    sp.execute();
                    btnNuevo.setEnabled(true);
                    new esconderTeclado(Inventarios_InventiarioCiclicoPorPosicionDet.this);
                }
                return false;
            }
        });
        btnNuevo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(CodigoUbicacion.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_ubicacion),"false",true,true);
                    CodigoUbicacion.requestFocus();
                    CodigoUbicacion.setText("");
                    return;
                }
                Intent intent = new Intent(Inventarios_InventiarioCiclicoPorPosicionDet.this,Inventario_RegPalletNuevo.class);
                b.putString("UbicacionIntent",CodigoUbicacion.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnBaja.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                        .setTitle("¿Dar Pallet de Baja?").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            SegundoPlano sp = new SegundoPlano("BajaPallet");
                            sp.execute();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        btnPallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Inventarios_InventiarioCiclicoPorPosicionDet.this,Inventarios_CiclosPosicionReg.class);
                b.putString("CodigoPallet",PalletSeleccionadoTabla);
                b.putString("UbicacionIntent",CodigoUbicacion.getText().toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        tabla.addDataClickListener(new ListenerClickTabla());
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
//    @Override
//    protected void onResume()
//    {
//        SegundoPlano sp = new SegundoPlano("llenarTabla");
//        sp.execute();
//        super.onResume();
//    }
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

            if(!CodigoUbicacion.getText().toString().equals(""))
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
                btnBaja.setEnabled(true);
                btnPallet.setEnabled(true);
                btnBaja.setTextColor(getApplication().getResources().getColor(R.color.RojoClaroStd));
                btnPallet.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                PalletSeleccionadoTabla = null;
                btnBaja.setEnabled(false);
                btnPallet.setEnabled(true);
                btnBaja.setTextColor(R.color.grisAXC);
                btnPallet.setTextColor(R.color.grisAXC  );
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
            try {
                switch (tarea) {
                    case "llenarTabla":
                        if (!UbicacionIntent.equals("")) {
                            sa.SOAPConsultaUbicacion(IdInventario, UbicacionIntent, contexto);
                            mensaje = sa.getMensaje();
                            decision = sa.getDecision();
                        } else {
                            decision = "true";
                        }
                        break;
                    case "BajaPallet":
                        sa.SOAPBajaPalletInventario(IdInventario, PalletSeleccionadoTabla, contexto);
                        mensaje = sa.getMensaje();
                        decision = sa.getDecision();
                        break;
                    case "Producto":
                        if (!UbicacionIntent.equals("")) {
                            sa.SOAPConsultaUbicacion(IdInventario, UbicacionIntent, contexto);
                            mensaje = sa.getMensaje();
                            decision = sa.getDecision();
                        } else {
                            decision = "true";
                        }
                        break;
                    case "CiclicoPorLote":
                        if (!UbicacionIntent.equals("")) {
                            sa.SOAPConsultaUbicacion(IdInventario, UbicacionIntent, contexto);
                            mensaje = sa.getMensaje();
                            decision = sa.getDecision();
                        }else
                        {
                            decision = "true";
                        }
                        break;
                    case "Fisico Total":
                        decision = "true";
                        break;
               /* case"Producto sin Ubicacion":
                    decision = "true";
                    break;*/
                }


                if (decision.equals("true") && ((tarea.equals("llenarTabla") || tarea.equals("Producto") || tarea.equals("CiclicoPorLote")))) {
                    sacaDatos();
                    int i = 0;
                    DATA_TO_SHOW = new String[arrayDatosTabla.size()][6];
                    for (datosTabla a : arrayDatosTabla) {

                        DATA_TO_SHOW[i][0] = a.getTipo();

                        if (!(a.getIdStatus().equals("1") || a.getIdStatus().equals("7") || a.getIdStatus().equals("8"))) {
                            Log.d("SoapResponse", "doInBackground: " + a.getIdStatus());
                            palletRegistradosVar++;
                        }
                        DATA_TO_SHOW[i][1] = a.getCodigoPallet();
                        DATA_TO_SHOW[i][2] = a.getProducto();
                        DATA_TO_SHOW[i][3] = a.getLote();
                        DATA_TO_SHOW[i][4] = a.getCantidadActual();
                        DATA_TO_SHOW[i][5] = a.getEmpaques();
                        i++;
                        //  Log.i("SoapResponse", " SOAPDATOS" + DATA_TO_SHOW.toString());
                    }

                }
            }catch (Exception e)
            {
                e.printStackTrace();
                decision = "false";
                mensaje = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if (decision.equals("true"))
            {
                switch (tarea) {
                    case "llenarTabla":

                        if(!UbicacionIntent.equals(""))
                        {
                            //ajustarTamañoColumnas();
                            tabla.getDataAdapter().notifyDataSetChanged();
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(contexto, DATA_TO_SHOW));
                            tabla.getDataAdapter().notifyDataSetChanged();
                           // tabla.setColumnModel(columnModel);
                            arrayDatosTabla.clear();
                            PalletsRegistrados.setText(String.valueOf(palletRegistradosVar));
                        }
                        break;
                    case "CiclicoPorLote":
                        if(!UbicacionIntent.equals(""))
                        {
                           // ajustarTamañoColumnas();
                            tabla.getDataAdapter().notifyDataSetChanged();
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(contexto, DATA_TO_SHOW));
                          //  tabla.setColumnModel(columnModel);
                            tabla.getDataAdapter().notifyDataSetChanged();
                            arrayDatosTabla.clear();
                            PalletsRegistrados.setText(String.valueOf(palletRegistradosVar));
                            btnNuevo.setEnabled(true);
                        }
                        break;
                    case "BajaPallet":
                        new popUpGenerico(contexto, vista,"Pallet dado de baja con exito.","Registrado", true, true);
                        SegundoPlano sp = new SegundoPlano("llenarTabla");
                        sp.execute();
                        break;
                    case "Fisico Total":
                        CodigoUbicacion.requestFocus();

                        break;
                    case"Producto":
                        if(!UbicacionIntent.equals(""))
                        {
                            tabla.getDataAdapter().notifyDataSetChanged();
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(contexto, DATA_TO_SHOW));
                            tabla.getDataAdapter().notifyDataSetChanged();
                            arrayDatosTabla.clear();
                            PalletsRegistrados.setText(String.valueOf(palletRegistradosVar));
                            //
                            btnNuevo.setEnabled(true);
                        }
                        break;

                }
            }

            if(decision.equals("false"))
            {

                    new popUpGenerico(contexto, vista, mensaje, "Advertencia", true, true);

            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
//    public void ajustarTamañoColumnas()
//    {
//        float Dp = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.txtSize),
//                contexto.getResources().getDisplayMetrics() );
//        int size1=0,size2=0,size3=0,size4=0,size5=0,size6=0;
//        for(datosTabla a : arrayDatosTabla)
//        {
//
//            if(a.getTipo()!=null)
//            if(a.getTipo().length()>=size1)
//            {
//                size1 = a.getTipo().length();
//                if(HEADER.length>size1)
//                {
//                    size1 = HEADER[0].length();
//                }
//            }
//
//            if(a.getCodigoPallet()!=null)
//                if(a.getCodigoPallet().length()>=size2)
//                {
//                    size2 = a.getCodigoPallet().length();
//                    if(HEADER.length>size2)
//                    {
//                        size2 = HEADER[1].length();
//                    }
//                }
//
//            if(a.getProducto()!=null)
//                if(a.getProducto().length()>=size3)
//                {
//                    size3 = a.getProducto().length();
//                    if(HEADER.length>size3)
//                    {
//                        size3 = HEADER[2].length();
//                    }
//                }
//
//            if(a.getLote()!=null)
//                if(a.getLote().length()>=size4)
//                {
//                    size4 = a.getLote().length();
//                    if(HEADER.length>size4)
//                    {
//                        size4 = HEADER[3].length();
//                    }
//                }
//
//            if(a.getLote()!=null)
//                if(a.getLote().length()>=size5)
//                {
//                    size5 = a.getLote().length();
//                    if(HEADER.length>size5)
//                    {
//                        size5 = HEADER[4].length();
//                    }
//                }
//            if(a.getTipo()!=null)
//                if(a.getTipo().length()>=size6)
//                {
//                    size6 = a.getEmpaques().length();
//                    if(HEADER.length>size6)
//                    {
//                        size6 = HEADER[5].length();
//                    }
//                }
//
//        }
//        //Numeros elegidos con prueba y error
//        columnModel = new TableColumnDpWidthModel(contexto, 6, 100);
//        columnModel.setColumnWidth(0,Math.round(10+size1*Dp/5));
//        columnModel.setColumnWidth(1,Math.round(10+size2*Dp/5));
//        columnModel.setColumnWidth(2,Math.round(10+size3*Dp/5));
//        columnModel.setColumnWidth(3,Math.round(10+size4*Dp/5));
//        columnModel.setColumnWidth(4,Math.round(10+size5*Dp/5));
//        columnModel.setColumnWidth(5,Math.round(10+size6*Dp/5));
//
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size1*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size2*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size3*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size4*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size5*Dp/5));
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " +Math.round(size6*Dp/5));
//
//
//      // tabla.setMinimumWidth(size1+size2+size3+size4+size5+size6);
//
//        int d = Math.round(10+size1*Dp/5)+Math.round(10+size2*Dp/5)+Math.round(10+size3*Dp/5)+Math.round(10+size4*Dp/5)+Math.round(10+size5*Dp/5)+Math.round(10+size6*Dp/5);
//
//        View vista = findViewById(R.id.LL);
//        vista.setMinimumWidth(d+100);
//
//        //vista.setLayoutParams(new FrameLayout.LayoutParams(d, FrameLayout.LayoutParams.MATCH_PARENT));
//
//        //Math.round(10+size1*Dp/5)+Math.round(10+size2*Dp/5)+Math.round(10+size3*Dp/5)+Math.round(10+size4*Dp/5)+Math.round(10+size5*Dp/5)+Math.round(10+size6*Dp/5);
///*
//        tabla.setLayoutParams(new LinearLayout.LayoutParams(d, LinearLayout.LayoutParams.MATCH_PARENT));
//        tabla.setLayoutParams(new LinearLayout.LayoutParams(d, 12));*/
//        Log.d("SoapResponse", "ajustarTamañoColumnas: " + Integer.valueOf(d));
//    }
    private void sacaDatos()
    {

        SoapObject tabla = sa.parser();
        String Tipo,IdStatus,CodigoPallet,Producto,Lote,CantidadActual,Empaques;

        arrayDatosTabla = null;
        arrayDatosTabla = new ArrayList<>();
        if(tabla!=null)
        for(int i = 0; i<tabla.getPropertyCount();i++)
        {
            try {


                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse",tabla1.toString());

                Empaques = tabla1.getPrimitivePropertyAsString("Empaques");
                Tipo =  tabla1.getPrimitivePropertyAsString("Tipo");
                IdStatus= tabla1.getPrimitivePropertyAsString("IdStatus");
                CodigoPallet= tabla1.getPrimitivePropertyAsString("CodigoPallet");
                Producto = tabla1.getPrimitivePropertyAsString("NumParte");
                Lote= tabla1.getPrimitivePropertyAsString("Revision");
                CantidadActual = tabla1.getPrimitivePropertyAsString("CantidadActual");

                datosTabla d = new datosTabla(Tipo,IdStatus,CodigoPallet,Producto,Lote,CantidadActual,Empaques);
                arrayDatosTabla.add(d);
            }catch (Exception e)
            {

                e.printStackTrace();
            }
        }
    }

}
