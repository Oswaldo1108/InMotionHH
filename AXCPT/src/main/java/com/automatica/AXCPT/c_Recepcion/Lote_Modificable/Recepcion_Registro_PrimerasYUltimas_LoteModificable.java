package com.automatica.AXCPT.c_Recepcion.Lote_Modificable;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Recepcion_Registro_PrimerasYUltimas_LoteModificable extends AppCompatActivity
{
    //region variables
    Handler handler = new Handler();
    EditText edtx_EmpxPallet, edtx_Cantidad, edtx_PrimerEmpaque,edtx_UltimoEmpaque,edtx_CantidadEmpaques,edtx_LoteNuevo;
    String Pallet,Producto, Cantidad,Lote,EmpaquesRegistrados;
    String TAG = "SoapResponse";
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_EmpaquesRegistrados,txtv_Pallet, txtv_OrdenCompra,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_CantRegLote;
    Bundle b;
    View vista;
    Context contexto = this;
    String OrdenCompra,Factura,ModificaCant,PartidaERP,IdRecepcion,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    Handler h = new Handler();
    Button btnCerrarTarima;
    String mercado;

    Spinner spnr_OrdenesCompra,spnr_Mercado;

    int registroAnteriorSpinner=0;
    boolean recarga;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_oc_primerasyultimas);

            new cambiaColorStatusBar(contexto, R.color.VerdeStd, Recepcion_Registro_PrimerasYUltimas_LoteModificable.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();
            edtx_PrimerEmpaque.requestFocus();
            new SegundoPlano("ConsultaPallet").execute();
            new SegundoPlano("ListarOrdenesCompra").execute();
            new SegundoPlano("ListaMercados").execute();
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
        try {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {
                reiniciarVariables();
            }
        }catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
        return super.onOptionsItemSelected(item);
    }
    private void declararVariables()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.recepcion_primera_y_ultima));
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            //    edtx_Factura = (EditText) findViewById(R.id.edtx_Factura);
            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_PrimerEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_PrimerEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CantidadEmpaques = (EditText) findViewById(R.id.edtx_CantidadEmpaques);
            edtx_CantidadEmpaques.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_UltimoEmpaque = (EditText) findViewById(R.id.edtx_UltimoEmpaque);
            edtx_UltimoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);

            edtx_LoteNuevo = (EditText) findViewById(R.id.edtx_LoteNuevo);
            edtx_LoteNuevo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

            txtv_Partida = (TextView) findViewById(R.id.txtv_Partida);
            txtv_UM = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_CantRegLote = (TextView) findViewById(R.id.txtv_RegLote);
            edtx_Cantidad.setText(CantidadEmpaques);
            edtx_EmpxPallet.setText(EmpaquesPallet);
            spnr_OrdenesCompra = (Spinner) findViewById(R.id.spnr_OrdenesCompra);
            txtv_Partida.setText(PartidaERP);
            txtv_UM.setText(UM);
            txtv_Producto.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_OrdenCompra.setText(OrdenCompra);
            spnr_Mercado = findViewById(R.id.vw_spinner2).findViewById(R.id.spinner);


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
                OrdenCompra= b.getString("Orden");
                PartidaERP= b.getString("PartidaERP");
                NumParte= b.getString("NumParte");
                UM= b.getString("UM");
                CantidadTotal= b.getString("CantidadTotal");
                CantidadRecibida= b.getString("CantidadRecibida");
                CantidadEmpaques= b.getString("CantidadEmpaques");
                EmpaquesPallet= b.getString("EmpaquesPallet");
            }catch (Exception e)
            {
                Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
            }

    }
    private void AgregaListeners()
    {
        spnr_Mercado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mercado = ((Constructor_Dato)spnr_Mercado.getSelectedItem()).getDato();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        //edtx_EmpxPallet.requestFocus();
                        // new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_Spinner.this);
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
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_LoteModificable.this);

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
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_LoteModificable.this);
                    }catch (Exception e) {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Empaques por Pallet", "false", true, true);
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


                        if(Integer.parseInt(edtx_EmpxPallet.getText().toString()) <=0)
                            {
                                new popUpGenerico(contexto,getCurrentFocus(),"La cantidad ingresada no puede ser menor o igual a cero." , false, true, true);
                                return false;
                            }
                        if(Float.parseFloat(edtx_Cantidad.getText().toString()) <=0)
                            {
                                new popUpGenerico(contexto,getCurrentFocus(),"La cantidad de empaques no puede ser menor o igual a cero." , false, true, true);
                                return false;
                            }



                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CantidadEmpaques.requestFocus();

                            }
                        });

                        if(spnr_OrdenesCompra.getAdapter() == null)
                            {
                                new CreaDialogos("Se generará lote nuevo.",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                new SegundoPlano("RegistrarEmpaqueNuevo").execute();
                                            }
                                        }, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        edtx_CantidadEmpaques.setText("");

                                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_lote_desc) , "false", true, true);

                                    }
                                }, contexto);
                            }else{

                            new SegundoPlano("RegistrarEmpaqueNuevo").execute();

                        }


//                        SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
//                        sp.execute();
                        //validacionFinal();
                        new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_LoteModificable.this);
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
        spnr_OrdenesCompra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    txtv_CantRegLote.setText( ((Constructor_Dato)spnr_OrdenesCompra.getSelectedItem()).getTag2());
                    txtv_CantidadOriginal.setText( ((Constructor_Dato)spnr_OrdenesCompra.getSelectedItem()).getTag1());
                    txtv_CantidadRegistrada.setText( ((Constructor_Dato)spnr_OrdenesCompra.getSelectedItem()).getTag3());
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }




            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                try
                    {
                    txtv_CantRegLote.setText("-");
                }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                    }
            }
        });










        edtx_LoteNuevo.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                    {
                        try
                            {
                                edtx_LoteNuevo.setText("");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                            }
                    }
            }
        });

        edtx_LoteNuevo.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                            {
                                if(edtx_LoteNuevo.getText().toString().equals(""))
                                    {
                                        h.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                edtx_LoteNuevo.setText("");
                                                edtx_LoteNuevo.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto,getCurrentFocus(),"Ingrese un lote." , false, true, true);
                                        return false;
                                    }

                                new CreaDialogos("¿Crear lote? [" + edtx_LoteNuevo.getText().toString() + "]",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                new SegundoPlano("CreaLoteRecepcionOC").execute();
                                            }
                                        },null,contexto);



                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }
                    }
                return false;
            }
        });


        btnCerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if(!txtv_EmpaquesRegistrados.getText().toString().equals(""))
                    {
                        if (Integer.parseInt(txtv_EmpaquesRegistrados.getText().toString())>0)
                        {
                            new CreaDialogos("¿Cerrar tarima?",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            new SegundoPlano("RegistraPalletNuevo").execute();
                                        }
                                    },null,contexto);
                        }
                        else
                        {
                            new popUpGenerico(contexto, null, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                        }
                    }else
                    {
                        new popUpGenerico(contexto, null, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }
        });





    }
    private void reiniciarVariables()
    {
        try {
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
    private void validacionFinal()
    {
        if(edtx_Cantidad.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad)+" - Cantidad", "false", true, true);
            return;
        }
        if(edtx_EmpxPallet.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Empaques por Pallet", "false", true, true);
            return;
        }
        if(edtx_PrimerEmpaque.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Primer empaque", "false", true, true);
            return;
        }
        if(edtx_UltimoEmpaque.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Ultimo empaque", "false", true, true);
            return;
        }
        if(edtx_CantidadEmpaques.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Validación" +  " cantidad empaques", "false", true, true);
            return;
        }
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_CantidadEmpaques.requestFocus();

            }
        });
        SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
        sp.execute();
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {

        DataAccessObject dao;
        cAccesoADatos_Recepcion ca  = new cAccesoADatos_Recepcion(Recepcion_Registro_PrimerasYUltimas_LoteModificable.this);
        String tarea;
        View LastView;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                Log.d(TAG, "onPostExecute() " + "PASO");
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LastView = getCurrentFocus();
                        progressBarHolder.requestFocus();
                        recarga = true;
                    }
                }, 10);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
         /*   LastView = getCurrentFocus();
            progressBarHolder.requestFocus();
            recarga=true;*/
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {

                switch (tarea)
                {
                    case "ListaMercados":
                        dao = ca.c_ListaMercados();
                        break;
                    case "ConsultaPallet":

                        dao= ca.c_ConsultaPalletAbiertoOC(OrdenCompra, PartidaERP);
                        break;

                    case "ListarOrdenesCompra":

                        dao = ca.c_ListarLotesOC(OrdenCompra, PartidaERP, "");
                        break;

                    case "RegistrarEmpaqueNuevoSA":

                        if(spnr_OrdenesCompra.getAdapter() == null)
                        {
                            dao = ca.c_RegistraMPPrimerasYUltimas_Mercado_SA(OrdenCompra,
                                    PartidaERP,
                                    "",
                                    edtx_Cantidad.getText().toString(),
                                    edtx_EmpxPallet.getText().toString(),
                                    edtx_PrimerEmpaque.getText().toString(),
                                    edtx_UltimoEmpaque.getText().toString(),
                                    edtx_CantidadEmpaques.getText().toString(),mercado);
                        }
                        else{
                            dao = ca.c_RegistraMPPrimerasYUltimas_Mercado_SA(OrdenCompra,
                                    PartidaERP,
                                    ((Constructor_Dato)spnr_OrdenesCompra.getSelectedItem()).getDato(),
                                    edtx_Cantidad.getText().toString(),
                                    edtx_EmpxPallet.getText().toString(),
                                    edtx_PrimerEmpaque.getText().toString(),
                                    edtx_UltimoEmpaque.getText().toString(),
                                    edtx_CantidadEmpaques.getText().toString(),mercado);
                        }
                        break;
                    case "RegistrarEmpaqueNuevo":




                        if(spnr_OrdenesCompra.getAdapter() == null)
                            {
                                dao = ca.c_RegistraMPPrimerasYUltimas_Mercado(OrdenCompra,
                                        PartidaERP,
                                        "",
                                        edtx_Cantidad.getText().toString(),
                                        edtx_EmpxPallet.getText().toString(),
                                        edtx_PrimerEmpaque.getText().toString(),
                                        edtx_UltimoEmpaque.getText().toString(),
                                        edtx_CantidadEmpaques.getText().toString(),mercado);
                            }
                            else{
                                dao = ca.c_RegistraMPPrimerasYUltimas_Mercado(OrdenCompra,
                                        PartidaERP,
                                        ((Constructor_Dato)spnr_OrdenesCompra.getSelectedItem()).getDato(),
                                        edtx_Cantidad.getText().toString(),
                                        edtx_EmpxPallet.getText().toString(),
                                        edtx_PrimerEmpaque.getText().toString(),
                                        edtx_UltimoEmpaque.getText().toString(),
                                        edtx_CantidadEmpaques.getText().toString(),mercado);
                                 }
                        break;

                    case "RegistraPalletNuevo":

                        dao = ca.c_CierraPalletCompra(txtv_Pallet.getText().toString());
                        break;

                    case "CreaLoteRecepcionOC":

                        dao = ca.c_CreaLoteRecepcionOC(OrdenCompra,
                                PartidaERP,
                                edtx_LoteNuevo.getText().toString(),
                                "1");
                        break;

                    case "CerrarRecepcion":

                        dao = ca.c_CerrarRecepcion(OrdenCompra);

                        break;
                    default:

                        dao =new DataAccessObject();

                        break;

                }
            }catch (Exception e)
            {
                e.printStackTrace();
                dao = new  DataAccessObject(e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {

                recarga = false;
                if(LastView!=null)
                {
                    LastView.requestFocus();
                }
                if(dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case "ListaMercados":
                            spnr_Mercado.setAdapter(new CustomArrayAdapter(Recepcion_Registro_PrimerasYUltimas_LoteModificable.this, android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("Mercado","DMercado")));
                            break;

                        case "RegistrarEmpaqueNuevoSA":
                        case "RegistrarEmpaqueNuevo":
                            txtv_CantidadRegistrada.setText(CantidadRecibida);
                            edtx_PrimerEmpaque.setText("");
                            edtx_UltimoEmpaque.setText("");
                            edtx_CantidadEmpaques.setText("");
                            edtx_PrimerEmpaque.requestFocus();

                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));

                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");

                            new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_LoteModificable.this);

                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                             //   new SegundoPlano("CerrarRecepcion").execute();
                                new SegundoPlano("RegistraPalletNuevo").execute();
                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);

                               new SegundoPlano("RegistraPalletNuevo").execute();
                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1") )
                            {
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }

                            new SegundoPlano("ListarOrdenesCompra").execute();

                            break;


                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, getCurrentFocus(),"Pallet ["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            new SegundoPlano("ListarOrdenesCompra").execute();
                            new SegundoPlano("ConsultaPallet").execute();

                            reiniciarVariables();
                            break;
                        case "ListarOrdenesCompra":

                            if(dao.getcTablas() != null)
                                {
                                    registroAnteriorSpinner = spnr_OrdenesCompra.getSelectedItemPosition();
                                    spnr_OrdenesCompra.setAdapter(new CustomArrayAdapter(Recepcion_Registro_PrimerasYUltimas_LoteModificable.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("LoteAXC","CantidadTotalPartida","CantidadRegistrada","CantidadTotalRegPartida")));
                                    spnr_OrdenesCompra.setSelection(registroAnteriorSpinner);
                                }else
                                {
                                    spnr_OrdenesCompra.setAdapter(null);
                                }
                            //("LoteAXC","CantidadTotalPartida","CantidadRegistrada","CantidadTotalRegPartida")   "LoteAXC","CantidadRecibida","CantidadRegistrada","FechaCaducidad"

                            break;

                        case "CreaLoteRecepcionOC":
                            new SegundoPlano("ListarOrdenesCompra").execute();

                            break;

                        case "CerrarRecepcion":

                            new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);

                            break;
                        case "ConsultaPallet":

                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));
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
                            reiniciarVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}
