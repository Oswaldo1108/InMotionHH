package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
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
import com.automatica.AXCPT.Servicios.creaNotificacion;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.Devolucion_PYU;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Fragmento_Cancelar_Tarima;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Fragmento_Ingresar_Textos;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.ClasesSerializables.DocumentIDs;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Recepcion_Registro_Transferencias_Por_Empaque_SelAlm extends AppCompatActivity implements Fragmento_Ingresar_Textos.OnFragmentInteractionListener
{
    //region variables

    EditText  edtx_EmpxPallet, edtx_Cantidad,edtx_CodigoEmpaque,edtx_Lote;

    Button btnCerrarTarima;
//    FrameLayout progressBarHolder;
//    AlphaAnimation inAnimation;
//    AlphaAnimation outAnimation;


    ProgressBarHelper progressBarHelper;

    String NumParte2;
    TextView tv_detalle;
    TextView txtv_Lote,txtv_Traspaso,txtv_EmpaquesRegistrados, txtv_Caducidad,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet;
    View vista;
    Context contexto = this;
    String Transferencia, FechaCaducidad,PartidaERP,NumParte,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,LoteTrans;

    TextView txtv_OrdenCompra;
    Spinner spnr_Almacen;

    Handler h = new Handler();


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_transfer_porempaque_selalm);
            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Recepcion_Registro_Transferencias_Por_Empaque_SelAlm.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();

            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(Recepcion_Registro_Transferencias_Por_Empaque_SelAlm.this));


        }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista,e.getMessage()+" "+e.getCause() ,"false" ,true,true);
            }
    }

    @Override
    protected void onResume()
    {
        if(!txtv_Traspaso.getText().toString().equals("-"))
        {
            new SegundoPlano("Almacenes").execute();
        }
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
                new sobreDispositivo(contexto, vista);
            }else
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
    private void declararVariables()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.recepcion_por_empaque));

//            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            progressBarHelper = new ProgressBarHelper(this);

            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_CodigoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Lote = (EditText) findViewById(R.id.edtx_Traspaso);//


            btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

            txtv_Traspaso = (TextView) findViewById( R.id.txtv_Traspaso);
            txtv_Partida = (TextView) findViewById( R.id.txtv_Partida);
            txtv_Caducidad = (TextView) findViewById(R.id.txtv_Caducidad);//
            txtv_UM = (TextView) findViewById(R.id.txtv_UnidadMedida);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_CantidadRegistrada);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_Lote = (TextView) findViewById( R.id.txtv_Lote);
            tv_detalle= findViewById(R.id.tv_detalle);
            edtx_Cantidad.setText(CantidadEmpaques);
            edtx_EmpxPallet.setText(EmpaquesPallet);
            spnr_Almacen = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);

            btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);

            txtv_Partida.setText(PartidaERP);
            txtv_Producto.setText(NumParte2);
//            txtv_Caducidad.setText(FechaCaducidad);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_Lote.setText(LoteTrans);
            txtv_Traspaso.setText(Transferencia);
            tv_detalle.setText(NumParte);
        }catch(Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }
    private void AgregaListeners()
    {
        try
        {

            edtx_Cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {


                    if (hasFocus && edtx_Cantidad.getText().toString().equals("0")) {
                        try {
                            edtx_Cantidad.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }
                    }

                }
            });
            edtx_EmpxPallet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && edtx_EmpxPallet.getText().toString().equals("0")) {
                        try {
                            edtx_EmpxPallet.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }
                    }
                }
            });


            edtx_Cantidad.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        try {
                            if (!edtx_Cantidad.getText().toString().equals("")) {
                                try {
                                    if (!(Float.parseFloat(edtx_Cantidad.getText().toString()) > 999999)) {
                                        edtx_EmpxPallet.requestFocus();
                                    } else {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                h.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        edtx_Cantidad.requestFocus();
                                                        edtx_Cantidad.setText("");
                                                    }
                                                });
                                            }
                                        });
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);

                                    }
                                } catch (NumberFormatException ex) {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_Cantidad.setText("");
                                            edtx_Cantidad.requestFocus();

                                        }
                                    });
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                                }
                            } else {

                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_Cantidad.setText("");
                                        edtx_Cantidad.requestFocus();

                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "Advertencia", true, true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }

                    }
                    return false;
                }
            });
            edtx_EmpxPallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        try {
                            if (!edtx_EmpxPallet.getText().toString().equals("")) {

                                try {
                                    if (!(Integer.parseInt(edtx_EmpxPallet.getText().toString()) > 999999)) {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_CodigoEmpaque.requestFocus();
                                            }
                                        });
                                    } else {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_EmpxPallet.setText("");
                                                edtx_EmpxPallet.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "Advertencia", true, true);
                                    }
                                } catch (NumberFormatException ex) {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_EmpxPallet.setText("");
                                            edtx_EmpxPallet.requestFocus();
                                        }
                                    });
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                                }
                            } else {
                                edtx_EmpxPallet.setText("");
                                edtx_EmpxPallet.requestFocus();
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "Advertencia", true, true);
                            }
                            //   edtx_EmpxPallet.requestFocus();
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }
                    }
                    return false;
                }
            });
            edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        try {
                            if (edtx_EmpxPallet.getText().toString().equals("")) {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), "Ingrese candidad de empaques", "false", true, true);
                                return false;

                            }

                            if (edtx_Cantidad.getText().toString().equals("")) {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "false", true, true);
                                return false;

                            }

                            if(Integer.parseInt(edtx_EmpxPallet.getText().toString()) <=0)
                                {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            edtx_CodigoEmpaque.setText("");
                                            edtx_CodigoEmpaque.requestFocus();
                                        }
                                    });
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "false", true, true);
                                    return false;

                                }
                            if(Float.parseFloat(edtx_Cantidad.getText().toString()) <=0)
                                {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            edtx_CodigoEmpaque.setText("");
                                            edtx_CodigoEmpaque.requestFocus();
                                        }
                                    });
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "false", true, true);
                                    return false;
                                }

                            if (!edtx_CodigoEmpaque.getText().toString().equals(""))
                            {
                                new SegundoPlano("RegistrarEmpaqueNuevo").execute();
                                btnCerrarTarima.setEnabled(true);

                            } else
                            {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }
                    }
                    return false;
                }
            });

            btnCerrarTarima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!txtv_EmpaquesRegistrados.getText().toString().equals("")&&!txtv_EmpaquesRegistrados.getText().toString().equals("-"))
                        {
                            if (Integer.parseInt(txtv_EmpaquesRegistrados.getText().toString()) > 0)
                            {
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            } else
                                {
                                new popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                                }
                        } else
                            {
                            new popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
            });
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
                Bundle b;
                b = getIntent().getExtras();
                NumParte= b.getString("Producto");
                CantidadTotal= b.getString("CantSolicitada");
                CantidadRecibida= b.getString("CantRecibida");
                FechaCaducidad = b.getString("FechaCad");
                LoteTrans = b.getString("Lote");
                Transferencia = b.getString("Transferencia");
                PartidaERP = b.getString("Partida");
                NumParte2 = b.getString("Detalle");
            }catch (Exception e)
            {
                Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
            }

    }
    private void reiniciaVariables()
    {
        //txtv_EmpaquesRegistrados.setText("");
        try
        {
        edtx_CodigoEmpaque .setText("");
        edtx_CodigoEmpaque.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    @Override
    public boolean RegistrarTextos()
    {
        new popUpGenerico(contexto, null, "Textos registrados con éxito.", true, true, true);
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
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Recepcion_Registro_Transferencias_Por_Empaque_SelAlm.this);

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
//            h.postDelayed(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    LastView = getCurrentFocus();
//                    progressBarHolder.requestFocus();
//                    recargar = true;
//                }
//            },10);

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
                        dao = cad.cad_ConsultaPalletAbiertoTraspaso(Transferencia, PartidaERP);
                        break;

                    case "RegistrarEmpaqueNuevo":

                        dao = cad.cad_RegistraEmpaqueTraspasoSelAlm(Transferencia,
                                                            PartidaERP,
                                                            edtx_CodigoEmpaque.getText().toString(),
                                                            edtx_Cantidad.getText().toString(),
                                                            edtx_EmpxPallet.getText().toString(),
                                                            ((Constructor_Dato)spnr_Almacen.getSelectedItem()).toString());

                        break;

                    case "RegistrarEmpaqueNuevoSA":

                        dao = cad.cad_RegistraEmpaqueTraspasoSelAlm_SA(Transferencia,
                                                            PartidaERP,
                                                            edtx_CodigoEmpaque.getText().toString(),
                                                            edtx_Cantidad.getText().toString(),
                                                            edtx_EmpxPallet.getText().toString(),
                                                            ((Constructor_Dato)spnr_Almacen.getSelectedItem()).toString());
                        break;

                    case "RegistraPalletNuevo":
                        dao = cad.cad_OCCierraPalletTraspaso(txtv_Pallet.getText().toString());

                        break;

                        case "Almacenes":
                            dao = cad.c_ListaMercados();
                            break;
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
                                        txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                                        txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();

                                        new esconderTeclado(Recepcion_Registro_Transferencias_Por_Empaque_SelAlm.this);

                                        if ( dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                                            {
                                                new popUpGenerico(contexto, null, getString(R.string.traspaso_completado), dao.iscEstado(), true, true);
                                                new SegundoPlano("RegistraPalletNuevo").execute();

                                            }
                                        else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                                            {
                                                new popUpGenerico(contexto, null, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                                new SegundoPlano("RegistraPalletNuevo").execute();

                                            }
                                        else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                                            {
                                                new SegundoPlano("RegistraPalletNuevo").execute();
                                            }

                                        break;


                                    case "ConsultaPallet":
                                        txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                        txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Pallet"));
                                        txtv_CantidadOriginal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTot"));
                                        txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadReg"));

                                        break;

                                    case "RegistraPalletNuevo":
                                        new popUpGenerico(contexto, null,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);

                                        new SegundoPlano("ConsultaPallet").execute();
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

//                    outAnimation = new AlphaAnimation(1f, 0f);
//                    outAnimation.setDuration(200);
//                    progressBarHolder.setAnimation(outAnimation);
//                    progressBarHolder.setVisibility(View.GONE);

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,null,e.getMessage(),"false",true,true);
                }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
    }
}
