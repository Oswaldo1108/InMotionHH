package com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.DevolucionesCIESA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro_Granel_NE_LoteModificable;
import com.automatica.AXCPT.databinding.ActivityDevolucionDAPBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Devolucion_DAP extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{
    String TAG = "SoapResponse";

    EditText edtx_CantidadPaquetes,edtx_CanXPaqt, edtx_Posicion,edtx_LoteNuevo;
    String  Cantidad,CodigoEmpaque;
    //    Button btnCerrarTarima;
    FrameLayout progressBarHolder;
    String mercado;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    frgmnt_taskbar_AXC taskbar_axc;
    TextView txtv_EmpaquesRegistrados,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet ,txtv_CantRegLote;
    Bundle b;

    View vista;
    Context contexto = this;
    String OrdenCompra,PartidaERP,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    Spinner spnr_Lotes,spnr_Mercado;
    TextView txtv_OrdenCompra;
    boolean verificarCierre,recargar;
    String TipoRecepcion;
    Handler h = new Handler();
    int registroAnteriorSpinner=0;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recepcion_granel);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Devolución");
            new cambiaColorStatusBar(contexto, R.color.VerdeStd, Devolucion_DAP.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();
//                new SegundoPlano("ConsultaPallet").execute();
            new SegundoPlano("ListarOrdenesCompra").execute();
            new SegundoPlano("ListaMercados").execute();
            View logoView = getToolbarLogoIcon((Toolbar) findViewById(R.id.toolbar));
            logoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                                .replace(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });

            taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
            getSupportFragmentManager().beginTransaction().add(findViewById(R.id.FrameLayout).getId(),taskbar_axc,"FragmentoTaskBar").commit();


        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,e.getMessage()+" "+e.getCause() ,"false" ,true,true);
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
        int id = item.getItemId();

        if(!recargar)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                reiniciaVariables();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void declararVariables()
    {
        try {

            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            //  edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Factura);
            edtx_CantidadPaquetes = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_CantidadPaquetes.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CanXPaqt = (EditText) findViewById(R.id.edtx_CantXEmp);
            edtx_CanXPaqt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Posicion = (EditText) findViewById(R.id.edtx_Posicion);
            edtx_Posicion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

//            btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);
            edtx_LoteNuevo = (EditText) findViewById(R.id.edtx_LoteNuevo);
            edtx_LoteNuevo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            txtv_Partida = (TextView) findViewById(R.id.txtv_Partida);
            txtv_UM = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_CantRegLote = (TextView) findViewById(R.id.txtv_RegLote);



//            edtx_CantidadPaquetes.setText(CantidadEmpaques);
//            edtx_Posicion.setText(EmpaquesPallet);

//            btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);

            txtv_Partida.setText(PartidaERP);
            txtv_UM.setText(UM);
            txtv_Producto.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_OrdenCompra.setText(OrdenCompra);

            spnr_Lotes = (Spinner) findViewById(R.id.spnr_OrdenesCompra);
            spnr_Mercado = findViewById(R.id.vw_spinner2).findViewById(R.id.spinner);


        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }
    private void AgregaListeners()
    {
        edtx_CanXPaqt.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {


                if(hasFocus&&edtx_CanXPaqt.getText().toString().equals("0"))
                {
                    try
                    {
                        edtx_CanXPaqt.setText("");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }

            }
        });
        edtx_Posicion.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus&& edtx_Posicion.getText().toString().equals("0"))
                {
                    try
                    {
                        edtx_Posicion.setText("");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
            }
        });


        edtx_CantidadPaquetes.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {

                        if(txtv_Pallet.getText().toString().equals("")||txtv_Pallet.getText().toString().equals("-"))
                        {
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_Posicion.setText("");
                                    edtx_Posicion.requestFocus();
                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),"Ingrese posición de piso." , "false", true, true);
                            return false;
                        }

                        if (edtx_CantidadPaquetes.getText().toString().equals(""))
                        {

                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_CantidadPaquetes.setText("");
                                    edtx_CantidadPaquetes.requestFocus();

                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad) +  "Cantidad Paquetes","Advertencia", true, true);

                        }

                        if (edtx_CanXPaqt.getText().toString().equals(""))
                        {

                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_CanXPaqt.setText("");
                                    edtx_CanXPaqt.requestFocus();

                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad) + "Cantidad por Paquete","Advertencia", true, true);

                        }

                        try {

                            if (!(Float.parseFloat(edtx_CanXPaqt.getText().toString()) > 999999))
                            {
                                edtx_Posicion.requestFocus();
                            } else
                            {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_CanXPaqt.requestFocus();
                                                edtx_CanXPaqt.setText("");
                                            }
                                        });
                                    }
                                });
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                            }


                            if (!(Float.parseFloat(edtx_CantidadPaquetes.getText().toString()) > 999999))
                            {
                                edtx_Posicion.requestFocus();
                            } else {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_CantidadPaquetes.requestFocus();
                                                edtx_CantidadPaquetes.setText("");
                                            }
                                        });
                                    }
                                });
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);

                            }




                            if(spnr_Lotes.getAdapter() == null)
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
                                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_lote_desc) , "false", true, true);

                                    }
                                }, contexto);
                            }else{

                                new SegundoPlano("RegistrarEmpaqueNuevo").execute();

                            }

                        }catch (NumberFormatException ex)
                        {
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_CanXPaqt.setText("");
                                    edtx_CanXPaqt.requestFocus();

                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_cantidad_valida),"false",true,true);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                }
                return false;
            }
        });
        spnr_Mercado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mercado = ((Constructor_Dato)spnr_Mercado.getSelectedItem()).getDato();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtx_Posicion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(edtx_Posicion.getText().toString().equals(""))
                        {
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_Posicion.setText("");
                                    edtx_Posicion.requestFocus();
                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),"Ingrese posición de piso." , "false", true, true);
                            return false;
                        }



                        new SegundoPlano("ConsultarPosicion").execute(edtx_Posicion.getText().toString());
//                                        btnCerrarTarima.setEnabled(true);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

//        btnCerrarTarima.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                try
//                    {
//                        if(!txtv_EmpaquesRegistrados.getText().toString().equals(""))
//                            {
//                                if (Integer.parseInt(txtv_EmpaquesRegistrados.getText().toString())>0)
//                                    {
//                                        new SegundoPlano("RegistraPalletNuevo").execute();
//                                    }
//                                else
//                                    {
//                                        new popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
//                                    }
//                            }else
//                            {
//                                new popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
//                            }
//                    }catch (Exception e)
//                    {
//                        e.printStackTrace();
//                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
//                    }
//            }
//        });

        spnr_Lotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    txtv_CantRegLote.setText( ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getTag2());
                    txtv_CantidadOriginal.setText( ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getTag1());
                    txtv_CantidadRegistrada.setText( ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getTag3());
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
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
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),"Ingrese un lote." , false, true, true);
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
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

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
    private void reiniciaVariables()
    {
        //txtv_EmpaquesRegistrados.setText("");
        try
        {
            edtx_Posicion.setText("");
            edtx_Posicion.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
    onBackPressed();
    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Devolucion_DAP.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        View LastView;
        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

            try
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        LastView = getCurrentFocus();
                        progressBarHolder.requestFocus();
                        recargar = true;
                    }
                },10);
            }catch (Exception e)
            {
                e.printStackTrace();
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
//            LastView = getCurrentFocus();
//            progressBarHolder.requestFocus();
//            recargar=true;
        }

        @Override
        protected Void doInBackground(String... params)
        {
            try {
                switch (tarea)
                {
                    case "ListarOrdenesCompra":

                        //dao = ca.c_ListarLotesOC(OrdenCompra, PartidaERP, "");
                        break;
                    case "ConsultaPallet":
                        //dao= ca.c_ConsultaPalletAbiertoOC(OrdenCompra, PartidaERP);
                        break;
/*
                    case "ConsultarPosicion":
                        dao= ca.c_ConsultaPosicionPisoOC(OrdenCompra, PartidaERP,params[0]);
                        break;

                    case "RegistrarEmpaqueNuevoSA":
                        CodigoEmpaque = edtx_Posicion.getText().toString();
                        Cantidad = edtx_CantidadPaquetes.getText().toString();
                        if(spnr_Lotes.getAdapter() == null)
                        {
                            dao = ca.c_RegistrarPalletGranel_Mercado_SA(
                                    OrdenCompra,
                                    PartidaERP,
                                    "",
                                    edtx_CanXPaqt.getText().toString(),
                                    edtx_CantidadPaquetes.getText().toString(),
                                    txtv_Pallet.getText().toString(),mercado
                            );
                        }else{

                            dao = ca.c_RegistrarPalletGranel_Mercado_SA(
                                    OrdenCompra,
                                    PartidaERP,
                                    ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getDato(),
                                    edtx_CanXPaqt.getText().toString(),
                                    edtx_CantidadPaquetes.getText().toString(),
                                    txtv_Pallet.getText().toString(),mercado

                            );
                        }

                        break;*/

                    case "RegistrarEmpaqueNuevo":
                        CodigoEmpaque = edtx_Posicion.getText().toString();
                        Cantidad = edtx_CantidadPaquetes.getText().toString();
                        if(spnr_Lotes.getAdapter() == null)
                        {
                          /*
                            dao = ca.c_RegistrarPalletGranel_Mercado(
                                    OrdenCompra,
                                    PartidaERP,
                                    "",
                                    edtx_CanXPaqt.getText().toString(),
                                    edtx_CantidadPaquetes.getText().toString(),
                                    txtv_Pallet.getText().toString(),mercado
                            );*/

                        }else{
                        /*
                            dao = ca.c_RegistrarPalletGranel_Mercado(
                                    OrdenCompra,
                                    PartidaERP,
                                    ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getDato(),
                                    edtx_CanXPaqt.getText().toString(),
                                    edtx_CantidadPaquetes.getText().toString(),
                                    txtv_Pallet.getText().toString(),mercado
                            );
                            */
                        }

                        break;
                    case "RegistraPalletNuevo":

                        //dao = ca.c_CierraPalletCompra(txtv_Pallet.getText().toString());
                        break;

                    case  "ListaMercados":
                        dao= ca.c_ListaMercados();
                        break;

                    case "CreaLoteRecepcionOC":

                        //dao = ca.c_CreaLoteRecepcionOC(OrdenCompra,
                                //PartidaERP,
                                //edtx_LoteNuevo.getText().toString(),
                               // "1");
                        break;

                    case "CerrarRecepcion":

                        //dao = ca.c_CerrarRecepcion(OrdenCompra);

                        break;
                    default:

                        dao =new DataAccessObject();

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
                        case "ListaMercados":
                            spnr_Mercado.setAdapter(new CustomArrayAdapter(Devolucion_DAP.this, android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("Mercado","DMercado")));
                            break;

                        case "RegistrarEmpaqueNuevoSA":
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            edtx_Posicion.setText("");
                            edtx_Posicion.requestFocus();
                            edtx_CantidadPaquetes.setText("");



                            new esconderTeclado(Devolucion_DAP.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, LastView, dao.getcMensaje(), String.valueOf(dao.iscEstado()), true, true);
//                                                new SegundoPlano("RegistraPalletNuevo").execute();
                                //  reiniciaVariables();
//                                                new SegundoPlano("CerrarRecepcion").execute();

                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);

//                                                new SegundoPlano("RegistraPalletNuevo").execute();
                                break;
                            }

                            if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("ActualizarLotes").equals("true")))
                            {

                                new SegundoPlano("ListarOrdenesCompra").execute();
                            }

                            new SegundoPlano("ConsultarPosicion").execute(txtv_Pallet.getText().toString());

                            break;
                        case "RegistrarEmpaqueNuevo":
//                                        txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            edtx_Posicion.setText("");
                            edtx_Posicion.requestFocus();
                            edtx_CantidadPaquetes.setText("");



                            new esconderTeclado(Devolucion_DAP.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, LastView, dao.getcMensaje(), String.valueOf(dao.iscEstado()), true, true);
//                                                new SegundoPlano("RegistraPalletNuevo").execute();
                                //  reiniciaVariables();
//                                                new SegundoPlano("CerrarRecepcion").execute();

                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);

//                                                new SegundoPlano("RegistraPalletNuevo").execute();
                                break;
                            }

                            if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("ActualizarLotes").equals("true")))
                            {

                                new SegundoPlano("ListarOrdenesCompra").execute();
                            }

                            new SegundoPlano("ConsultarPosicion").execute(txtv_Pallet.getText().toString());


                            break;
                        case "ConsultaPallet":
                        case "ConsultarPosicion":

                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));

                            new esconderTeclado(Devolucion_DAP.this);
                            edtx_CantidadPaquetes.requestFocus();
                            break;

                        case "RegistraPalletNuevo":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, LastView,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",String.valueOf(dao.iscEstado()), true, true);
                            new SegundoPlano("ListarOrdenesCompra").execute();
                            new SegundoPlano("ConsultaPallet").execute();
                            break;

                        case "ListarOrdenesCompra":


                            if(dao.getcTablas() != null)
                            {
                                registroAnteriorSpinner = spnr_Lotes.getSelectedItemPosition();
                                spnr_Lotes.setAdapter(new CustomArrayAdapter(Devolucion_DAP.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("LoteAXC","CantidadTotalPartida","CantidadRegistrada","CantidadTotalRegPartida")));
                                spnr_Lotes.setSelection(registroAnteriorSpinner);


                                //("LoteAXC","CantidadTotalPartida","CantidadRegistrada","CantidadTotalRegPartida")   "LoteAXC","CantidadRecibida","CantidadRegistrada","FechaCaducidad"
                            }else
                            {
                                spnr_Lotes.setAdapter(null);
                            }

                            break;


                        case "CreaLoteRecepcionOC":
                            new SegundoPlano("ListarOrdenesCompra").execute();

                            break;
                        case "CerrarRecepcion":

                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);

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
                            reiniciaVariables();
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                    }
                }
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
            recargar = false;
        }
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu")!=null){
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("FragmentoNoti")!=null||getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!=null){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}