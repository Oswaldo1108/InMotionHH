package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Fragmento_Ingresar_Textos;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Recepcion_Registro_Transferencia_NE_SelAlm extends AppCompatActivity implements Fragmento_Ingresar_Textos.OnFragmentInteractionListener
{

    TextView tv_cantEmp,tv_pallet;
    Handler handler = new Handler();
    String NumParte2;
    Button btn_CerrarTarima;
    EditText edtx_EmpxPallet, edtx_Cantidad;
    TextView txtv_Traspaso,txtv_Lote,txtv_Partida, txtv_OrdenCompra,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,tv_Numparte;
    Bundle b;
    Context contexto = this;
    String CodigoPallet,Partida;
    String FechaCaducidad,LoteTrans,Transferencia;
    String NumParte,CantidadTotal,CantidadRecibida;

    ProgressBarHelper progressBarHelper;


    //    FrameLayout progressBarHolder;
//    AlphaAnimation inAnimation;
//    AlphaAnimation outAnimation;
    TextView txtv_Caducidad;
    Spinner spnr_Almacen;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_transfer_ne_selalm);
            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Recepcion_Registro_Transferencia_NE_SelAlm.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();
//            new SegundoPlano("ConsultaPallet").execute();
            new SegundoPlano("Almacenes").execute();


            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(Recepcion_Registro_Transferencia_NE_SelAlm.this));

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }

    }

    @Override
    protected void onResume() {
//        if(!txtv_Traspaso.getText().toString().equals("-"))
//        {
//        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.textos_recarga_toolbar, menu);
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
        int id = item.getItemId();

        if(progressBarHelper.ispBarActiva())
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, null);
            }
            if ((id == R.id.borrar_datos))
            {
                reiniciaVariables();
            }
            if ((id == R.id.recargar))
            {
                new SegundoPlano("ConsultaPallet").execute();
            }
            if ((id == R.id.IngresarTextos))
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.cl, Fragmento_Ingresar_Textos.newInstance(Transferencia), "IngresarTextos").addToBackStack("IngresarTextos")
                        .commit();
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private void reiniciaVariables()
    {
        //txtv_EmpaquesRegistrados.setText("");
        try
        {
            edtx_Cantidad .setText("");
            edtx_EmpxPallet.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }
    public void declararVariables(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("No etiquetado");
        spnr_Almacen = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);
//        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        progressBarHelper = new ProgressBarHelper(this);

        edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
        btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);
        edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        txtv_Traspaso = (TextView) findViewById( R.id.txtv_Traspaso);
        txtv_Partida = (TextView) findViewById( R.id.txtv_Partida);
        txtv_Caducidad = (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_UM = (TextView) findViewById(R.id.txtv_UnidadMedida);
        txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
        tv_Numparte = findViewById(R.id.tv_detalle);
        txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
        txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_CantidadRegistrada);
        txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
        txtv_Lote = (TextView) findViewById( R.id.txtv_Lote);
        tv_cantEmp= findViewById(R.id.tv_cantEmp);
        tv_pallet= findViewById(R.id.tv_pallet);

        txtv_Partida.setText(Partida);
//        txtv_Caducidad.setText(FechaCaducidad);
        txtv_Lote.setText(LoteTrans);
        tv_Numparte.setText(NumParte2);
        txtv_Traspaso.setText(Transferencia);
        txtv_Producto.setText(NumParte);
        txtv_CantidadOriginal.setText(CantidadTotal);
        txtv_CantidadRegistrada.setText(CantidadRecibida);
    }

    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            NumParte= b.getString("Detalle");
            NumParte2= b.getString("Producto");
            CantidadTotal= b.getString("CantSolicitada");
            CantidadRecibida= b.getString("CantRecibida");
            FechaCaducidad = b.getString("FechaCad");
            LoteTrans = b.getString("Lote");
            Transferencia = b.getString("Transferencia");
            Partida = b.getString("Partida");


        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras, " + e.getMessage());
            e.printStackTrace();
        }

    }
    private void AgregaListeners()
    {

        spnr_Almacen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {


            }
        });

        btn_CerrarTarima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(tv_pallet.getText())){
                    new popUpGenerico(Recepcion_Registro_Transferencia_NE_SelAlm.this,getCurrentFocus(),"No hay un pallet abierto para ser cerrado",false,true,true);
                }else {
                    new SegundoPlano("RegistraPalletNuevo").execute();
                }
            }
        });
        edtx_Cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {

                try
                {
                    if(hasFocus&&edtx_Cantidad.getText().toString().equals("0"))
                    {
                        try
                        {
                            edtx_Cantidad.setText("");
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }
        });
        edtx_EmpxPallet.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus&&edtx_EmpxPallet.getText().toString().equals("0"))
                {
                    try
                    {
                        edtx_EmpxPallet.setText("");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
            }
        });
        edtx_Cantidad.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {

                        if (!edtx_Cantidad.getText().toString().equals("")) {
                            try {
                                if (Float.parseFloat(edtx_Cantidad.getText().toString()) > 999999) {

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_Cantidad.setText("");
                                            edtx_Cantidad.requestFocus();
                                        }
                                    });

                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_EmpxPallet.requestFocus();
                                        }
                                    });

                                }
                            } catch (NumberFormatException ex) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_Cantidad.setText("");
                                        edtx_Cantidad.requestFocus();

                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                            }
                        }else
                        {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_cantidad_valida),"false", true, true);

                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                }
                return false;
            }
        });

        edtx_EmpxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {


                        if(edtx_Cantidad.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad)+" - Cantidad", "false", true, true);
                            return true;
                        }
                        if(edtx_EmpxPallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Empaques por Pallet", "false", true, true);
                            return true;
                        }

                        try
                            {
                                if (Integer.parseInt(edtx_EmpxPallet.getText().toString()) > 999999)
                                    {
                                        edtx_EmpxPallet.setText("");
                                        edtx_EmpxPallet.requestFocus();
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                    }

                            }catch (NumberFormatException ex)
                            {
                                new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_cantidad_valida),"false",true,true);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_EmpxPallet.setText("");
                                        edtx_EmpxPallet.requestFocus();
                                    }
                                });
                            }


                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_EmpxPallet.requestFocus();

                            }
                        });

                         new SegundoPlano("RegistrarEmpaqueNuevo").execute();
                        //validacionFinal();
                        new esconderTeclado(Recepcion_Registro_Transferencia_NE_SelAlm.this);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }

                return false;
            }
        });
    }
    private void reiniciarVariables()
    {
        try {

            edtx_Cantidad.setText("");
            edtx_EmpxPallet.setText("");
            edtx_Cantidad.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }
    @Override
    public boolean RegistrarTextos()
    {
        new popUpGenerico(contexto,null, "Textos registrados con éxito.", true, true, true);
        return false;
    }

    @Override
    public void EstadoCarga(Boolean Estado)
    {
        if(Estado)
        {
//            inAnimation = new AlphaAnimation(0f, 1f);
//            inAnimation.setDuration(200);
//            progressBarHolder.setAnimation(inAnimation);
//            progressBarHolder.setVisibility(View.VISIBLE);
            progressBarHelper.ActivarProgressBar();
        }else
        {
//            outAnimation = new AlphaAnimation(1f, 0f);
//            outAnimation.setDuration(200);
//            progressBarHolder.setAnimation(outAnimation);
//            progressBarHolder.setVisibility(View.GONE);
            progressBarHelper.DesactivarProgressBar();
        }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Recepcion_Registro_Transferencia_NE_SelAlm.this);
        cAccesoADatos_Consultas cadcons = new cAccesoADatos_Consultas(Recepcion_Registro_Transferencia_NE_SelAlm.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        View LastView;
        @Override
        protected void onPreExecute()
        {
//            inAnimation = new AlphaAnimation(0f, 1f);
//            inAnimation.setDuration(200);
//            progressBarHolder.setAnimation(inAnimation);
//            progressBarHolder.setVisibility(View.VISIBLE);

            try
            {
//                handler.postDelayed(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        LastView = getCurrentFocus();
//                        progressBarHolder.requestFocus();
//                        recargar = true;
//                    }
//                },10);

                progressBarHelper.ActivarProgressBar(tarea);

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                switch (tarea)
                {
                    case "ConsultaPallet":
                        dao = cad.cad_ConsultaPalletAbiertoTraspaso(Transferencia, Partida);
                        break;

                    case "RegistrarEmpaqueNuevo":
                        dao = cad. ad_CreaEmpaqueTraspasoNESelAlm(Transferencia,
                                Partida,
                                edtx_Cantidad.getText().toString(),
                                edtx_EmpxPallet.getText().toString(),
                                ((Constructor_Dato)spnr_Almacen.getSelectedItem()).toString());
                        break;
                    case "RegistrarEmpaqueNuevoSA":
                        dao = cad. ad_CreaEmpaqueTraspasoNESelAlm_SA(Transferencia,
                                Partida,
                                edtx_Cantidad.getText().toString(),
                                edtx_EmpxPallet.getText().toString(),
                                ((Constructor_Dato)spnr_Almacen.getSelectedItem()).toString());
                        break;
                    case "RegistraPalletNuevo":
                        dao = cad.cad_OCCierraPalletTraspaso(tv_pallet.getText().toString());

                        break;
                    case "Almacenes":
                        dao = cad.c_ListaMercados();
                        break;
                    default:
                        dao = new DataAccessObject();
                 }

            }catch (Exception e)
            {
                e.printStackTrace();
                dao = new DataAccessObject(e);
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


                if(dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case "RegistrarEmpaqueNuevo":
                        case "RegistrarEmpaqueNuevoSA":

                            CodigoPallet = dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet");
                            tv_pallet.setText(CodigoPallet);
                            tv_cantEmp.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));

                            new esconderTeclado(Recepcion_Registro_Transferencia_NE_SelAlm.this);

                            if ( dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, LastView, getString(R.string.traspaso_completado), dao.iscEstado(), true, true);
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);

                                new SegundoPlano("RegistraPalletNuevo").execute();

                            }
                            else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                            {
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }
                            break;


                        case "ConsultaPallet":

                            tv_cantEmp.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            tv_pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Pallet"));
                            txtv_CantidadOriginal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTot"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadReg"));

                            break;

                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, LastView,"Pallet"+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            reiniciarVariables();
                            tv_pallet.setText("");
                            new SegundoPlano("ConsultaPallet").execute();
                            break;
                        case "CierraPalletSinConsulta":
                            new popUpGenerico(contexto, LastView,"Pallet"+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);

                            reiniciarVariables();
                            tv_pallet.setText("");
                            break;
                        case "Almacenes":
                            spnr_Almacen.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Mercado","IdMercado")));
                            new SegundoPlano("ConsultaPallet").execute();

                            break;
                    }
                }
                else
                {

                    switch (tarea)
                    {
                        case "RegistrarEmpaqueNuevo":
                            if (dao.getcMensaje().contains("Error SAP"))
                            {
                                CreaDialogos cd = new CreaDialogos(contexto);


                                String sourceString = "<p>Se ha presentado un error al registrar en sap.</p> <p>" + dao.getcMensaje() +"</p>  <p><b>¿Registrar de todas maneras en AXC?</b></p>";

                                cd.dialogoDefault("VALIDE LA INFORMACIÓN", Html.fromHtml(sourceString),
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                new SegundoPlano("RegistrarEmpaqueNuevoSA").execute();
                                            }
                                        }, null);
                                break;
                            }

                        default:
                            if(!dao.getcMensaje().contains("Nota de entrega")||!dao.getcMensaje().contains("texto de cabecera"))
                            {
                                reiniciaVariables();
                            }
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                    }

                }
//                outAnimation = new AlphaAnimation(1f, 0f);
//                outAnimation.setDuration(200);
//                progressBarHolder.setAnimation(outAnimation);
//                progressBarHolder.setVisibility(View.GONE);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,null,e.getMessage(),"false",true,true);
            }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
    }
}