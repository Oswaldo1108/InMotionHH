package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.CreaDialogos;
import com.automatica.AXCMP.Servicios.InputDialogFragment;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.c_Surtido.Surtido_Bascula.ModBusConnection;
import com.automatica.AXCMP.c_Surtido.Surtido_ReimpresionEtiqueta;

import org.ksoap2.serialization.SoapObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

public class Almacen_Traspaso_Docificado extends AppCompatActivity
{
    //region variables
    String TAG = Almacen_Traspaso_Docificado.class.getSimpleName();
    Button btn_enviar,btn_CerrarTarima;
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    Context contexto = this;
    EditText edtx_OrdenProd,edtx_Pesaje,edtx_Bascula,edtx_CodigoEmpaque;
    View vista ;
    Handler h;
    HiloDatosTCP hrd;
    ImageView ic_conexion;
    Spinner spnr_Areas;
    Handler handler = new Handler();
    ArrayList<String> ArrayAreas;
    ArrayList<String> ArrayIdAreas;
    InputDialogFragment inputDialogFragment;

    TextView txtv_peso,txtv_lbl_Peso,txtv_lbl_Medida,txtv_CantEmpReg,txtv_PalletAbierto,txtv_Area;
    RadioGroup rdgrp_pesaje;
    ConstraintLayout cl_semaforo;
    TextView txtv_Pedido,txtv_Producto,txtv_CantPend,txtv_CantReg,txtv_SugEmpaque,txtv_SugLote;
    String RegEmpPalletAbierto;
    int renglonSeleccionado;
    int renglonAnterior = -1;
    Boolean seleccionado, ReiniciarTabla = false;
    SortableTableView tabla_Salidas;
    SimpleTableDataAdapter st;
    String Pedido, Partida, NumParte, UM, CantidadTotal, CantidadPendiente, CantidadSurtida, CodigoPallet,Lote;
    public String Area,AreaDesc;
    ArrayList<datosTabla> arrayDatosTabla;
    String[][] DATA_TO_SHOW;
    String TipoPesaje;
    String[] HEADER = {"Empaque", "Producto", "Lote", "Cantidad Actual"};
    String DireccionBascula, PuertoBascula,DireccionTorreta,PuertoTorreta,SolicitudPeso,EmpaqueSeleccionadoTabla;
    String SugLote,SugEmpaque;
    String Modelo;
    String BasculaActual;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    BufferedReader bufferedReader;
    boolean recargar,registrarPesaje;
    ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    Socket  s = null;
    Bundle b;

    double Varianza = .005;
    double PesoEsperado = 5;
    double limite;
    int cantInt = 0;
    ModBusConnection modBusConnection;
    int modBusROJO = 11;
    int modBusVERDE = 10;
    int modBusAPAGAR = 0;
    int modBusPRENDERCORTO = 1;
    int modBusPRENDERLARGO = 2;
    int modBusColorAnterior =0;
    int ContadorPesaje=0;
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



        try {
            setContentView(R.layout.activity_bascula_docificado);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Traspaso_Docificado.this);
            getSupportActionBar().setTitle(getString(R.string.recepcion_bascula));
            SacaExtrasIntent();
            declaraVariables();
            agregaListeners();

            SegundoPlano sp = new SegundoPlano("ConsultaAreas");
            sp.execute();
            Log.d("SoapResponse", "On create" );

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.change_view_toolbar_solo_reimp_emp, menu);

            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume()
    {
//        SegundoPlano sp = new SegundoPlano("ConsultarDetallePartida");
//        sp.execute();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        edtx_Bascula.setText("");
        edtx_CodigoEmpaque.setText("");
        edtx_Bascula.requestFocus();
        rdgrp_pesaje.check(R.id.rdbtn_pesaje_automatico);
        terminarPesaje();
        super.onPause();
    }

    private void declaraVariables()
    {
        try {
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            btn_enviar = (Button) findViewById(R.id.btn_CerrarPesaje);
            btn_CerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

            spnr_Areas = (Spinner) findViewById(R.id.spnr_OrdenesCompra);

            edtx_Pesaje = (EditText) findViewById(R.id.edtx_Peso);
            edtx_Pesaje.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            edtx_Bascula = (EditText) findViewById(R.id.edtx_Bascula);
            edtx_Bascula.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            ic_conexion = (ImageView) findViewById(R.id.img_conectado);
            txtv_peso = (TextView) findViewById(R.id.txtv_peso);
            rdgrp_pesaje = (RadioGroup) findViewById(R.id.radioGroup_pesaje);
            cl_semaforo = (ConstraintLayout) findViewById(R.id.ly_semaforo);

            txtv_Pedido = (TextView) findViewById(R.id.txtv_Pedido);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_CantPend = (TextView) findViewById(R.id.txtv_Cantidad);

//            txtv_SugEmpaque= (TextView) findViewById(R.id.txtv_SugEmpaque);
//            txtv_SugLote = (TextView) findViewById(R.id.txtv_Lote);
//            txtv_PalletAbierto = (TextView) findViewById(R.id.txtv_PalletAbierto);


            txtv_lbl_Medida = (TextView) findViewById(R.id.txtv_lbl_Medida);
            txtv_lbl_Peso = (TextView) findViewById(R.id.txtv_lbl_Peso);

            cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_LayouPesaje);
         //   cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

            cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
        //    cl_TablaRegistro.setVisibility(View.GONE);

            rdgrp_pesaje.check(R.id.rdbtn_pesaje_automatico);


//            tabla_Salidas= (SortableTableView) findViewById(R.id.tableView_Salida);
//
//            tabla_Salidas.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));


            txtv_Pedido.setText(Pedido);
            txtv_Producto.setText(NumParte);
            txtv_CantPend.setText(Lote);


            TipoPesaje = "Automatico";

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }

    private void agregaListeners()
    {
        try
        {
        btn_enviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                  //  new popUpGenerico(contexto,vista ,"PESAJE CANCELADO","false" ,true ,true );
                    if(edtx_CodigoEmpaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_empaque),"false" ,true ,true );
                        edtx_CodigoEmpaque.setText("");
                        edtx_CodigoEmpaque.requestFocus();
                        return;
                    }

//
//                    if(registrarPesaje == false)
//                    {
//                        new popUpGenerico(contexto,getCurrentFocus(),"El peso debe de estar dentro de lo permitido para poder ser registrado (la torreta debe estar en color verde).","false" ,true ,true );
//                    return;
//                    }

                    if (txtv_peso.getVisibility() == View.VISIBLE)
                    {
                        TipoPesaje = "Automatico";

                    } else if (edtx_Pesaje.getVisibility() == View.VISIBLE)
                    {
                        TipoPesaje = "Manual";
                    }


                    new CreaDialogos("¿Registrar pesaje?",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Log.i("SoapResponse",TipoPesaje +" " + edtx_Pesaje.getText().toString()+" "+ txtv_peso.getText().toString());

                                    if(TipoPesaje.equals("Manual") && edtx_Pesaje.getVisibility() == View.VISIBLE)
                                    {

                                        if(edtx_Pesaje.getText().toString().equals(""))
                                        {
                                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_peso),"false" ,true ,true );
                                            return;
                                        }



                                        SegundoPlano sp = new SegundoPlano("RegistrarPesaje");
                                        sp.execute();
                                        return;
                                    }

                                    if(TipoPesaje.equals("Automatico") &&txtv_peso.getVisibility() == View.VISIBLE)
                                    {
                                        if(txtv_peso.getText().toString().equals("----"))
                                        {
                                            new popUpGenerico(contexto,getCurrentFocus(),"Debe estar conectado a la bascula para registrar el peso automaticamente.","false" ,true ,true );

                                        }
                                        if(registrarPesaje)
                                        {
                                            SegundoPlano sp = new SegundoPlano("RegistrarPesaje");
                                            sp.execute();
                                        }else
                                        {
                                            new popUpGenerico(contexto,getCurrentFocus(),"El peso debe de estar dentro de lo permitido para " +
                                                    "poder ser registrado (la torreta debe estar en color verde).","false" ,true ,true );
                                        }
                                    }
                                   /* else
                                    {
                                        new popUpGenerico(contexto,getCurrentFocus(),"Debe estar conectado a la bascula para registrar el peso automaticamente.","false" ,true ,true );
                                    }*/

                                }
                            },null,contexto);
//                    handler.post(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                           terminarPesaje();
//
//                        }
//                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
        {
                @Override
                public void onClick(View v)
                {
                    try
                    {


                                new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id) {

                                        SegundoPlano sp = new SegundoPlano("CerrarTarima");
                                        sp.execute();
                                        new esconderTeclado(Almacen_Traspaso_Docificado.this);
                                    }
                                },null,contexto);


                    }catch(Exception e)
                    {
                        new popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
                    }


                }
            });


        edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    if(!edtx_CodigoEmpaque.getText().toString().equals(""))
                    {


                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"false",true,true);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoEmpaque.setText("");
                                edtx_CodigoEmpaque.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Traspaso_Docificado.this);
                }
                return false;
            }
        });



        edtx_Bascula.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    if(!edtx_Bascula.getText().toString().equals(""))
                    {

                        if(hrd!=null)
                            {
                                terminarPesaje();
                                //edtx_Bascula.setText("");
                            }

                        SegundoPlano sp = new SegundoPlano("ConsultarBascula");
                        sp.execute();


                    }else
                    {
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_bascula),"false",true,true);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_Bascula.setText("");
                                edtx_Bascula.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Traspaso_Docificado.this);
                }
                    return false;
            }
        });


            rdgrp_pesaje.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {

                    Log.e("SoapResponse", "onCheckedChanged: "+checkedId);
                    RadioButton rdb = (RadioButton) rdgrp_pesaje.findViewById(checkedId);
                    int index = rdgrp_pesaje.indexOfChild(rdb);

                    switch (index)
                    {
                        case 0:
                            TipoPesaje = "Automatico";
                            break;

                        case 1:
                            TipoPesaje = "Manual";
                            break;
                    }
                    cambiarTipoRegistro(TipoPesaje);
                }

            });

//        tabla_Salidas.addDataClickListener(new ListenerClickTabla());
//        tabla_Salidas.addDataLongClickListener(new ListenerLongClickTabla());
//        tabla_Salidas.addHeaderClickListener(new headerClickListener());
//

    }catch(Exception e)
    {
        new popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
    }
    }

    private void cambiarTipoRegistro(String Registro)
    {

        switch (Registro)
        {

            case "Automatico":
                cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_amarillo);
                cl_semaforo.setVisibility(View.VISIBLE);
                edtx_Pesaje.setVisibility(View.GONE);
                txtv_peso.setVisibility(View.VISIBLE);
                edtx_Pesaje.setEnabled(false);
                txtv_lbl_Medida.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                txtv_lbl_Peso.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                edtx_Bascula.setText("");
                edtx_Bascula.setEnabled(true);
                edtx_CodigoEmpaque.setText("");
                edtx_Bascula.requestFocus();

                break;

            case "Manual":

                terminarPesaje();
                cl_semaforo.setBackgroundResource(R.drawable.orilla_layout_input_group);
                cl_semaforo.setVisibility(View.VISIBLE);
                txtv_peso.setVisibility(View.GONE);
                edtx_Pesaje.setVisibility(View.VISIBLE);
                edtx_Pesaje.setEnabled(true);
                txtv_lbl_Medida.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                txtv_lbl_Peso.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                edtx_Bascula.setText("");
                edtx_Bascula.setEnabled(false);
                edtx_CodigoEmpaque.setText("");
                edtx_CodigoEmpaque.requestFocus();

                break;

            default:
                cl_semaforo.setVisibility(View.VISIBLE);
                txtv_peso.setVisibility(View.VISIBLE);
                edtx_Pesaje.setVisibility(View.GONE);
                txtv_lbl_Medida.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                txtv_lbl_Peso.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                break;
        }
    }

    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            Pedido= b.getString("Transferencia");
            Partida= b.getString("Partida");
            NumParte= b.getString("Producto");
           Lote= b.getString("Lote");

            Log.e("SoapResponse", "SacaExtrasIntent:");

        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {


            int id = item.getItemId();

            if (!recargar)
            {
                if ((id == R.id.InformacionDispositivo))
                {
                    new sobreDispositivo(contexto, vista);
                }
                if ((id == R.id.recargar))
                {
//                    inputDialogFragment = new InputDialogFragment();
//                    inputDialogFragment.show(getSupportFragmentManager(), "Dialogo imput");
                     Recarga();
                }
                if ((id == R.id.ReimprimeEtiqueta))
                    {
                        terminarPesaje();

                      //  Thread.sleep(100);

                        Bundle bundlefragment = new Bundle();
                        bundlefragment.putString("Area",Area);

                        Intent intent = new Intent(Almacen_Traspaso_Docificado.this,Surtido_ReimpresionEtiqueta.class);
                        intent.putExtras(bundlefragment);
                        startActivity(intent);
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
            case "ConsultarBascula":

              edtx_Bascula.setText("");
              edtx_CodigoEmpaque.setText("");
              rdgrp_pesaje.check(R.id.rdbtn_pesaje_automatico);
              edtx_Bascula.requestFocus();

                break;
            case "ConsultarDetallePartida":
               // tabla_Salidas.getDataAdapter().clear();

                break;
            case "SugerirEmpaqueUnidad":
              txtv_SugEmpaque.setText("");
              txtv_SugLote.setText("");
                break;
            case "RegistrarPesaje":
                edtx_CodigoEmpaque.setText("");
                edtx_Pesaje.setText("");
                edtx_CodigoEmpaque.requestFocus();
                break;
            case "CerrarPesaje":
                edtx_CodigoEmpaque.setText("");
                edtx_Pesaje.setText("");
                edtx_CodigoEmpaque.requestFocus();
                break;
        }
    }

    public class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        String decision,mensaje;
        SpinnerAdapter spinnerAdapter;
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
           ic_conexion.setImageResource(R.drawable.ic_conectado);
            Log.i("SoapResponse", "OnPreex");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Log.i("SoapResponse", "Antes switch Do in backgound");
                try
                {
                    switch (tarea)
                    {
                        case "ConsultarBascula":
                            sa.SOAPConsultaBascula(edtx_Bascula.getText().toString(),contexto);
                            break;
                        case "DesconectarBascula":
                            sa.SOAPDesconectarBascula(BasculaActual,contexto);
                            break;
                        case   "ConsultarDetallePartida":
                            sa.SOAPConsultarPartidaOrdenesSurtido(Pedido,Partida,contexto);
                            break;
                        case "SugerirEmpaqueUnidad":
                            sa.SOAPSugerirEmpaqueDocificacion(Pedido,Partida,Area,contexto);
                            break;

                        case"ConsultaAreas":
                            ArrayAreas = new ArrayList<>();
                            ArrayIdAreas = new ArrayList<>();
                            sa.SOAPConsultaAreasPesaje(contexto);
                            break;

                        case "RegistrarPesaje":
                            String peso =null;
                            if(TipoPesaje.equals("Automatico"))
                            {

                                peso = txtv_peso.getText().toString();
                            }else if(TipoPesaje.equals("Manual"))
                            {
                                peso = edtx_Pesaje.getText().toString();
                                limite = PesoEsperado *Varianza;

                                Log.i("SoapResponse",PesoEsperado+" "+limite+" peso -"+peso);

                                //if(!(Double.valueOf(peso)< PesoEsperado + limite && Double.valueOf(peso) > PesoEsperado - limite))
                                         if(PesoEsperado > Double.valueOf(edtx_Pesaje.getText().toString()))
                                    {
                                    //    new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_peso_excedente),"false" ,true ,true );
                                        mensaje = getString(R.string.error_peso_excedente);
                                        decision = "false";
                                        return null;
                                    }



                            }
                            sa.SOAPSurtirPiezasTraspaso(Pedido,Partida,edtx_CodigoEmpaque.getText().toString(),peso,ArrayIdAreas.get(spnr_Areas.getSelectedItemPosition()),contexto);

                   //         sa.SOAPSurtirPiezas2(Pedido,Partida,edtx_CodigoEmpaque.getText().toString(),peso,Area,contexto);
                            Log.i("SoapResponse",Pedido+" "+Partida+" empaque-"+edtx_CodigoEmpaque.getText().toString()+" peso -"+peso);
                            break;
                        case "CerrarPesaje":

                            break;
                        case "CerrarTarima":
                            sa.SOAPCierraPalletTraspasoEnvio(Pedido, contexto);

                            break;

                    }
                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();

                    if(decision.equals("true"))
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
                    Log.i("SoapResponse", "Do in backgound");
                } catch (Exception e)
                {
                    e.printStackTrace();
                    mensaje = e.getMessage();
                }

            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {
            try
                {
                    SegundoPlano sp;

                    if (decision.equals("true"))
                        {
                            switch (tarea)
                                {
                                    case   "ConsultarDetallePartida":
                                        txtv_CantPend.setText(CantidadPendiente);
                                        txtv_CantReg.setText(CantidadSurtida);
                                        PesoEsperado = Double.parseDouble(CantidadPendiente);

                                        sp = new SegundoPlano("SugerirEmpaqueUnidad");
                                        sp.execute();
                                        break;

                                    case "SugerirEmpaqueUnidad":
                                        txtv_SugEmpaque.setText(SugEmpaque);
                                        txtv_SugLote.setText(SugLote);

                                        break;

                                    case "ConsultarBascula":

                                        rdgrp_pesaje.check(R.id.rdbtn_pesaje_automatico);
                                        hrd = new HiloDatosTCP(DireccionBascula, PuertoBascula, DireccionTorreta, PuertoTorreta);
                                        hrd.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        BasculaActual = edtx_Bascula.getText().toString();
                                        break;
                                    case "DesconectarBascula":
                                        Toast.makeText(contexto,"Bascula Desconectada.",Toast.LENGTH_LONG).show();
                                        break;

                                    case "RegistrarPesaje":
                                            txtv_CantPend.setText(CantidadPendiente);
                                            txtv_CantReg.setText(CantidadSurtida);
                                            edtx_Pesaje.setText("");
                                            edtx_CodigoEmpaque.setText("");
                                            edtx_CodigoEmpaque.requestFocus();
                                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.pesaje_registrado_exito),decision,true,true);

                                        break;
                                    case "ConsultaAreas":
                                        //   new popUpGenerico(contexto, vista, "Orden de compra cerrada con exito.", "false", true, true);
                                        //   registroAnteriorSpinner = spnr_Areas.getSelectedItemPosition();
                                        spinnerAdapter= new ArrayAdapter<String>(
                                                Almacen_Traspaso_Docificado.this,
                                                android.R.layout.simple_spinner_item,
                                                ArrayAreas);
                                        ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spnr_Areas.setAdapter(spinnerAdapter);
                                        //spnr_Areas.setSelection(registroAnteriorSpinner);

                                        if(Area != null)
                                            {
                                                spnr_Areas.setSelection(Integer.parseInt(Area)-1);
                                            }

                                        break;
//                                    case "ConsultaPalletAbiertoOP":
//                                  //      new popUpGenerico(contexto,getCurrentFocus(),decision,decision,true,true);
//
//                                        tabla_Salidas.getDataAdapter().notifyDataSetChanged();
//                                        tabla_Salidas.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Traspaso_Docificado.this, DATA_TO_SHOW));
//                                        ReiniciarTabla = true;
//                                        tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaClear());
//
//                                        if (arrayDatosTabla != null)
//                                        {
//                                            cantInt = arrayDatosTabla.size();
//                                        } else
//                                        {
//                                            cantInt = 0;
//                                        }
//                                        RegEmpPalletAbierto = mensaje;
//
//                                        txtv_CantEmpReg.setText(String.valueOf(cantInt));
//                                        txtv_PalletAbierto.setText(mensaje);
//                                        edtx_CodigoEmpaque.setText("");
//                                        edtx_CodigoEmpaque.requestFocus();
//
//                                        break;

                                    case "CerrarTarima":
                                        new popUpGenerico(contexto,getCurrentFocus(),"Pallet ["+mensaje+"] cerrado con exito.",decision,true,true);

                                        terminarPesaje();

                                        edtx_Bascula.setText("");
                                        edtx_CodigoEmpaque.setText("");
                                        break;

                                }
                        }
                    if (decision.equals("false"))
                        {
                            if(!tarea.equals("DesconectarBascula")&&!mensaje.equals(""))
                           {
                                ReiniciarVariables(tarea);
                                new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                                terminarPesaje();
                           }
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(),"false" ,true,true);
                    terminarPesaje();
                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

        }
    }

    private  class HiloDatosTCP extends AsyncTask<Void,Void,Void>
    {

        String DireccionBascula,DireccionTorreta,PuertoBascula,PuertoTorreta;
        public HiloDatosTCP(String DireccionBascula,String PuertoBascula,String DireccionTorreta,String PuertoTorreta)
        {
            this.DireccionBascula = DireccionBascula;
            this.DireccionTorreta = DireccionTorreta;
            this.PuertoBascula = PuertoBascula;
            this.PuertoTorreta = PuertoTorreta;
        }
        String mensaje;
        ObjectInputStream ois = null;
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPreExecute()
        { Log.d("SoapResponse", "HRD on preexecute ");
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
              //  limite = PesoEsperado * Varianza;
                 Log.e("SoapResponse", "Varianza calculada");

                //String oldName = Thread.currentThread().getName();
                //Thread.currentThread().setName("Enviar");
                modBusConnection = new ModBusConnection(DireccionTorreta, Integer.valueOf(PuertoTorreta));
                s =   new Socket(DireccionBascula, Integer.valueOf(PuertoBascula));

                if(s.isConnected())
                {
                    Log.e("Socket", "Is connected " + s.getInetAddress() + " " + s.getPort());
                }
                else
                {
                    Log.e("Socket", "Not connected");
                }
                OutputStreamWriter osw ;//= new OutputStreamWriter(s.getOutputStream(), "UTF-8");
                //String SolicitudPesaje= "P\r";
                //String SolicitudPesaje= "P";
                //osw.write(data,0 ,data.length());
                modBusConnection.IniciarConexion();
                /*modBusConnection = new ModBusConnection("192.168.1.102", 8899);
                s =   new Socket("192.168.1.108", 8899);
                modBusConnection.IniciarConexion();*/

             //   Torreta(modBusROJO);
                while(!isCancelled())
                {
                    try
                    {
                        if(SolicitudPeso!=null||!SolicitudPeso.equals(""))
                        {
                            osw = new OutputStreamWriter(s.getOutputStream(), "ISO-8859-1");
                            osw.write(SolicitudPeso, 0, SolicitudPeso.length());
                            osw.flush();
                        }
                        //Envio de petición del peso
                        //Thread.sleep(30);
                        bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));

                        final String dato= bufferedReader.readLine();           //Lectura del dato de peso

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    String pesoParseado = dato.replaceAll("[|KG|M|O|P|?|kg|N|E|NT|  | ]", "");
                                    Log.i("Torreta", pesoParseado + " "+ dato);
                                    Float pesoExtraido;
                                    int colorActual=-1;
                                    if (!pesoParseado.equals(""))
                                    {
                                            pesoExtraido = Float.parseFloat(pesoParseado);


                                    txtv_peso.setText(String.valueOf(pesoExtraido));
                                   //     txtv_peso.setText(dato);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                    {
                                        if (pesoExtraido > 0)
                                        {
                                            cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_verde);
                                            colorActual = modBusROJO;
                                            registrarPesaje = true;
                                        }
                                        else
                                            {
                                                registrarPesaje = false;
                                                cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_amarillo);
                                                colorActual = modBusROJO;
                                            }
                                    } else
                                    {
                                        registrarPesaje = false;
                                    }
                                        Torreta(colorActual);
                                        Log.d("Torreta", "doInBackground: "+ colorActual);
                                    }

                                }
                                catch(NumberFormatException e)
                                {
                                    txtv_peso.setText(dato);
                                    registrarPesaje = false;
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                               //     apagarTorreta();
                                    Log.e("Torreta", "doInBackground: CATCH"  + e.getMessage());
                                    registrarPesaje = false;
                                }
                            }
                        });
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        s.close();
                        registrarPesaje = false;
                    }
                }
                if(isCancelled())
                {
                    s.close();
                    ois.close();
                }
            //Thread.currentThread().setName(oldName);
            }catch (Exception e)
            {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
            finally
            {
                try {
                    if (ois != null) ois.close();
                    if (s != null) s.close();
                    Log.e("SoapResponse", "DataReceiver: conexion cerrada por servidor ");
                    System.out.println("");
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {
            new popUpGenerico(contexto,getCurrentFocus(),mensaje + "ON post" ,"false" ,true ,true );
            txtv_peso.setText("----");
            apagarTorreta();
        }

        @Override
        protected void onCancelled()
        {
            Log.e("SoapResponse", "onCancelled: Cancelado" );
            try {
                txtv_peso.setText("----");
             terminarPesaje();

            }catch (Exception e)
            {

                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() ,"false" ,true ,true );
            }
            super.onCancelled();

        }
    }

    private boolean terminarPesaje()
    {
        try {
//            ContadorPesaje++;
//            txtv_lbl_Peso.setText(String.valueOf(ContadorPesaje));
            if(BasculaActual!=null)
            {
                if (!BasculaActual.equals(""))
                {
                    modBusColorAnterior = 0;
                    SegundoPlano sp = new SegundoPlano("DesconectarBascula");
                    sp.execute();
                    // cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_amarillo);

                }
            }
            if(hrd!=null)
            {

                hrd.cancel(true);
              //  modBusConnection.destroyInstance();

            }


            if(s!=null)s.close();
            apagarTorreta();
           // modBusConnection = null;
            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista ,e.getMessage() ,"false" , true,true );
        return false;
        }

    }

    private void Recarga()
    {
        SegundoPlano sp = new SegundoPlano("ConsultarDetallePartida");
        sp.execute();

            if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
            {
              sp = new SegundoPlano("SugiereEmpaque");
              sp.execute();

            } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE) {
             sp = new SegundoPlano("ConsultaPalletAbierto");
             sp.execute();

            }

    }

    private void Torreta(int Color)
    {
        if(Color!=modBusColorAnterior)
        {
            if(Color == 10)
            {
                modBusConnection.writeRegisterClickEvent(vista,Color, modBusPRENDERLARGO);
                Log.i("Torreta", "verde prendido");
            }
            if(Color == 11)
            {
                modBusConnection.writeRegisterClickEvent(vista, Color, modBusPRENDERCORTO);
                Log.i("Torreta", "rojo prendido");
            }
            modBusConnection.writeRegisterClickEvent(vista, modBusColorAnterior, modBusAPAGAR);
        }
        modBusColorAnterior = Color;

    }

    private void apagarTorreta()
    {
        if(modBusConnection!=null)
        {
            modBusConnection.writeRegisterClickEvent(vista, modBusROJO, modBusAPAGAR);
            modBusConnection.writeRegisterClickEvent(vista, modBusVERDE, modBusAPAGAR);

           // modBusConnection.destroyInstance();
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_black_24dp)

                    .setTitle("¿Terminar pesaje en curso?").setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                          terminarPesaje();
                            Almacen_Traspaso_Docificado.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            //   super.onBackPressed();
        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus() ,e.getMessage() ,"false" ,true , true);
        }
    }

    @Override
    protected void onDestroy()
    {
        terminarPesaje();
        super.onDestroy();
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

                        case "ConsultarBascula":

                      //      SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse",tabla1.toString());
                            DireccionBascula=  tabla1.getPrimitivePropertyAsString("DireccionIP");
                            DireccionTorreta=  tabla1.getPrimitivePropertyAsString("DireccionIPTorreta");
                            PuertoBascula = tabla1.getPrimitivePropertyAsString("Puerto");
                            PuertoTorreta= tabla1.getPrimitivePropertyAsString("PuertoTorreta");
                            Modelo = tabla1.getPrimitivePropertyAsString("Modelo");
                            SolicitudPeso = tabla1.getPrimitivePropertyAsString("SolicitudPeso");
                            Log.d("SoapResponse",DireccionBascula+" "+DireccionTorreta + " " + SolicitudPeso);
                            break;

                        case "ConsultaAreas":
                            ArrayAreas.add( tabla1.getPrimitivePropertyAsString("Descripcion"));
                            ArrayIdAreas.add(tabla1.getPrimitivePropertyAsString("IdArea"));
                            break;

                            case "ConsultarDetallePartida":
                            CantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPend");
                            CantidadSurtida = tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                            break;

                        case "SugerirEmpaqueUnidad":
                            Log.d("SoapResponse", tabla1.toString());
                            SugLote = tabla1.getPrimitivePropertyAsString("LoteAXC");
                            SugEmpaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");
                            //SugPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                            //SugPosicion = tabla1.getPrimitivePropertyAsString("CodigoPosicion");
                            break;

                        case "ConsultaPalletAbiertoOP":

                            Log.d("SoapResponse", tabla1.toString());

                            arrayDatosTabla.add(new datosTabla(tabla1.getPrimitivePropertyAsString("CodigoEmpaque"),
                                    tabla1.getPrimitivePropertyAsString("Producto"),
                                    tabla1.getPrimitivePropertyAsString("Lote"),
                                    tabla1.getPrimitivePropertyAsString("CantidadActual")));
                            break;

                     /*   case "RegistrarEmpaque":

                            Log.d("SoapResponse", tabla1.toString());
                            RegEmpCantidadPedida = tabla1.getPrimitivePropertyAsString("CantidadPedida");
                            RegEmpCantidadSurtida= tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                            RegEmpCantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                            RegEmpStatus= tabla1.getPrimitivePropertyAsString("IdStatus");
                            RegEmpPalletAbierto = tabla1.getPrimitivePropertyAsString("PalletAbierto");
                            break;*/
                        case "CerrarTarima":

                            break;
                      /*  case "ConsultaEmpaque":

                            Empaque = tabla1.getPrimitivePropertyAsString("CodigoEmpaque");
                            Producto_Empaque = tabla1.getPrimitivePropertyAsString("Producto");
                            UM = tabla1.getPrimitivePropertyAsString("UM");
                            Cantidad_Empaque = tabla1.getPrimitivePropertyAsString("CantidadActual");
                            CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                            Lote = tabla1.getPrimitivePropertyAsString("Lote");
                            break;*/
                        case "RegistrarPesaje":

                            CantidadTotal = tabla1.getPrimitivePropertyAsString("CantidadTotalMP");
                            CantidadSurtida = tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                            CantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                            CodigoPallet = tabla1.getPrimitivePropertyAsString("PalletPK");

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
