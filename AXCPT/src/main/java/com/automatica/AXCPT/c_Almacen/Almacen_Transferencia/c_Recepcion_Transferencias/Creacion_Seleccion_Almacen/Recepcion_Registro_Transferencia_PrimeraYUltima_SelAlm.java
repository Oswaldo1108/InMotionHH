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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Fragmento_Ingresar_Textos;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm extends AppCompatActivity implements Fragmento_Ingresar_Textos.OnFragmentInteractionListener
{
    //region variables
    Handler handler = new Handler();
    String NumParte2;
    EditText edtx_EmpxPallet, edtx_Cantidad, edtx_PrimerEmpaque,edtx_UltimoEmpaque,edtx_CantidadEmpaques;
    TextView txtv_Pallet,txtv_EmpReg;
    Button btn_CerrarTarima;
    TextView tv_detalle,txtv_Traspaso,txtv_Partida,txtv_Lote, txtv_OrdenCompra,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto;
    Bundle b;
    Context contexto = this;
    String Partida;
    String FechaCaducidad,LoteTrans,Transferencia;
    String NumParte,CantidadTotal,CantidadRecibida;
    Spinner spnr_Almacen;
    ProgressBarHelper progressBarHelper;
    TextView txtv_Caducidad;
    //endregion
     @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_transfer_primerayultima_selalm);

            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();
            edtx_PrimerEmpaque.requestFocus();
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm.this));

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
        try
        {
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
        try
        {
            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, null);
            }
            if ((id == R.id.borrar_datos)) {

                reiniciarVariables();
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

        }catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
        return super.onOptionsItemSelected(item);
    }
    private void declararVariables()
    {
        try
            {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.recepcion_primera_y_ultima));
            progressBarHelper = new ProgressBarHelper(this);
            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            spnr_Almacen = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);

            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpReg = (TextView) findViewById(R.id.txtv_EmpReg);

            edtx_PrimerEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_PrimerEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CantidadEmpaques = (EditText) findViewById(R.id.edtx_CantidadEmpaques);
            edtx_CantidadEmpaques.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_UltimoEmpaque = (EditText) findViewById(R.id.edtx_UltimoEmpaque);
            edtx_UltimoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            tv_detalle = findViewById(R.id.tv_detalle);
            txtv_Partida = (TextView) findViewById( R.id.txtv_Partida);
            txtv_Traspaso = (TextView) findViewById( R.id.txtv_Traspaso);
            txtv_Caducidad = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_UM = (TextView) findViewById(R.id.txtv_UnidadMedida);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_CantidadRegistrada);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_Lote = (TextView) findViewById( R.id.txtv_Lote);
            btn_CerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

            txtv_Partida.setText(Partida);
            txtv_Lote.setText(LoteTrans);
            txtv_Traspaso.setText(Transferencia);
            txtv_Producto.setText(NumParte2);
            tv_detalle.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }

    }
    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            NumParte= b.getString("Producto");
            CantidadTotal= b.getString("CantSolicitada");
            CantidadRecibida= b.getString("CantRecibida");
            FechaCaducidad = b.getString("FechaCad");
            LoteTrans = b.getString("Lote");
            Transferencia = b.getString("Transferencia");
            Partida = b.getString("Partida");
            NumParte2= b.getString("Detalle");


        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras, " + e.getMessage());
            e.printStackTrace();
        }

    }
    private void AgregaListeners()
    {
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
        edtx_EmpxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (!edtx_EmpxPallet.getText().toString().equals(""))
                        {


                            try {
                                if (Integer.parseInt(edtx_EmpxPallet.getText().toString()) > 999999)
                                {
                                    edtx_EmpxPallet.setText("");
                                    edtx_EmpxPallet.requestFocus();
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                } else {

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_PrimerEmpaque.requestFocus();
                                        }
                                    });
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
                        }else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"false", true, true);


                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_EmpxPallet.requestFocus();
                                    edtx_EmpxPallet.setText("");
                                }
                            });

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
        edtx_PrimerEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (!edtx_PrimerEmpaque.getText().toString().equals(""))
                        {
                        }else
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_PrimerEmpaque.setText("");
                                    edtx_PrimerEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, edtx_PrimerEmpaque, getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm.this);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
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
                    try
                    {
                        if (!edtx_UltimoEmpaque.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_CantidadEmpaques.requestFocus();

                                }
                            });
                        }else
                        {


                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_UltimoEmpaque.setText("");
                                    edtx_UltimoEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,edtx_UltimoEmpaque, getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm.this);

                    }catch (Exception e) {
                        e.printStackTrace();
                        new popUpGenerico(contexto, null, e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
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
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Paquetespor Pallet", "false", true, true);
                            return true;
                        }
                        if(edtx_PrimerEmpaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Primer empaque", "false", true, true);
                            return true;
                        }
                        if(edtx_UltimoEmpaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Ultimo empaque", "false", true, true);
                            return true;
                        }
                        if(edtx_CantidadEmpaques.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Validación " +  " cantidad empaques", "false", true, true);
                            return true;
                        }
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CantidadEmpaques.requestFocus();

                            }
                        });

                       new SegundoPlano("RegistrarEmpaqueNuevo").execute();

                       new esconderTeclado(Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm.this);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto,edtx_CantidadEmpaques, e.getMessage(), "false", true, true);
                    }
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
                {
                    if (txtv_Pallet.getText().toString().equals("-"))
                    {
                        new popUpGenerico(contexto, null, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                        return;
                    }
                        new SegundoPlano("RegistraPalletNuevo").execute();

                } catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }
        });
    }
    private void reiniciarVariables()
    {
        try
        {
            edtx_PrimerEmpaque.setText("");
            edtx_UltimoEmpaque.setText("");
            edtx_CantidadEmpaques.setText("");
            edtx_PrimerEmpaque.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(),"false",true,true);
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
            progressBarHelper.ActivarProgressBar();

        }else
        {
            progressBarHelper.DesactivarProgressBar();

        }
    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try
            {
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
            try
            {
                switch (tarea)
                {
                    case "ConsultaPallet":
                        dao = cad.cad_ConsultaPalletAbiertoTraspaso(Transferencia, Partida);
                        break;
                    case "RegistrarEmpaqueNuevo":
                        dao = cad.cad_PrimeraUltimaTraspasoSelAlm(
                                                Transferencia,
                                                Partida,
                                                edtx_Cantidad.getText().toString(),
                                                edtx_EmpxPallet.getText().toString(),
                                                edtx_PrimerEmpaque.getText().toString(),
                                                edtx_UltimoEmpaque.getText().toString(),
                                                edtx_CantidadEmpaques.getText().toString(),
                                ((Constructor_Dato)spnr_Almacen.getSelectedItem()).toString());
                        break;
                    case "RegistrarEmpaqueNuevoSA":
                        dao = cad.cad_PrimeraUltimaTraspasoSelAlm_SA(
                                Transferencia,
                                Partida,
                                edtx_Cantidad.getText().toString(),
                                edtx_EmpxPallet.getText().toString(),
                                edtx_PrimerEmpaque.getText().toString(),
                                edtx_UltimoEmpaque.getText().toString(),
                                edtx_CantidadEmpaques.getText().toString(),
                                ((Constructor_Dato)spnr_Almacen.getSelectedItem()).toString());
                        break;
                    case "RegistraPalletNuevo":
                       dao = cad.cad_OCCierraPalletTraspaso(txtv_Pallet.getText().toString());
                        break;

                    case "Almacenes":
                        dao = cad.c_ListaMercados();
                        break;
                    default:
                        dao = new DataAccessObject();
                }

            }catch (Exception e)
            {
              dao = new DataAccessObject(e);
              e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {
                if(dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case "RegistrarEmpaqueNuevo":
                        case "RegistrarEmpaqueNuevoSA":

                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));

                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            edtx_PrimerEmpaque.setText("");
                            edtx_UltimoEmpaque.setText("");
                            edtx_CantidadEmpaques.setText("");
                            edtx_PrimerEmpaque.requestFocus();

                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.traspaso_completado), dao.iscEstado(), true, true);
                                new SegundoPlano("RegistraPalletNuevo").execute();

                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }
                            else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                            {
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }

                            break;

                        case "RegistraPalletNuevo":

                            new popUpGenerico(contexto, getCurrentFocus(),"Pallet ["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            reiniciarVariables();
                            new SegundoPlano("ConsultaPallet").execute();
                            break;
                        case "ConsultaPallet":

                            txtv_EmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Pallet"));
                            txtv_CantidadOriginal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTot"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadReg"));
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
                                reiniciarVariables();
                            }
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,null,e.getMessage(),false,true,true);
            }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
    }
}
