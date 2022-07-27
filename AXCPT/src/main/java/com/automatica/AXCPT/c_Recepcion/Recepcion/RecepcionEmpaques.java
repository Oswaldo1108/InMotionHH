package com.automatica.AXCPT.c_Recepcion.Recepcion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.ReabastecimientoPicking.ReabPK_Seleccion_Material;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityRecepcionEmpaquesBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;
import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.newInstance;

public class RecepcionEmpaques extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener
{

    popUpGenerico pop = new popUpGenerico(RecepcionEmpaques.this);
    View vista;
    Context contexto = this;
    String NumSerie, sku;
    ActivityRecepcionEmpaquesBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    private ProgressBarHelper p;
    String TAG = "SoapResponse";
    EditText edtx_EmpxPallet, edtx_Cantidad, edtx_CodigoEmpaque,edtx_SKU;
    String Cantidad;
    TextView txtv_EmpaquesRegistrados, txtv_Partida, txtv_UM, txtv_CantidadOriginal, txtv_CantidadRegistrada, txtv_Producto, txtv_Pallet;
    Bundle b;
    String OrdenCompra, PartidaERP, NumParte, UM, CantidadTotal, CantidadRecibida, CantidadEmpaques, EmpaquesPallet,SKU;
    TextView txtv_OrdenCompra;
    boolean recargar;
    Handler h = new Handler();

    private Spinner sp_Partidas;
    private Spinner sp_NumSerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            binding = ActivityRecepcionEmpaquesBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Recepción");
            getSupportActionBar().setSubtitle("Empaques");

            sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);
            sp_NumSerie = binding.vwSpinner2.findViewById(R.id.spinner);
            View logoView = getToolbarLogoIcon(toolbar);
            logoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                                .add(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });

            taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
            getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();

            SacaDatosIntent();
            DeclararVariables();
            AgregarListeners();
            new SegundoPlano("ConsultaPallet").execute();
            new SegundoPlano("Tabla").execute();

            h.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    edtx_SKU.requestFocus();

                }
            },150);
        } catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista, e.getMessage(), false);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (p.ispBarActiva()) {
            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {
                ReiniciaVariables();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void SacaDatosIntent() {

        try {
            b = getIntent().getExtras();
            OrdenCompra = b.getString("Orden");
            PartidaERP = b.getString("PartidaERP");
            NumParte = b.getString("NumParte");
            UM = b.getString("UM");
            SKU= b.getString("SKU");
            CantidadTotal = b.getString("CantidadTotal");
            CantidadRecibida = b.getString("CantidadRecibida");
            CantidadEmpaques = b.getString("CantidadEmpaques");
            EmpaquesPallet = b.getString("EmpaquesPallet");

        } catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista, e.getMessage(), false);
        }

    }


    private void DeclararVariables() {

        try {
            p = new ProgressBarHelper(this);

            //  edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Factura);
            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_CodigoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            txtv_Partida = (TextView) findViewById(R.id.txtv_Partida);
            txtv_UM = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);

            txtv_Partida.setText(PartidaERP);
            txtv_UM.setText(UM);
            txtv_Producto.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_OrdenCompra.setText(OrdenCompra);


            edtx_SKU  = (EditText) findViewById(R.id.edtx_SKU);

        } catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista, e.getMessage(), false);
        }

    }


    private void AgregarListeners() {



        binding.toggleNumSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.toggleNumSerie.isChecked()){
                    binding.edtxnumSerie.setVisibility(View.VISIBLE);
                    binding.vwSpinner2.setVisibility(View.GONE);
                }else{
                    binding.edtxnumSerie.setVisibility(View.GONE);
                    binding.vwSpinner2.setVisibility(View.VISIBLE);
                }
            }
        });


        edtx_Cantidad.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                Log.e("Hola", edtx_CodigoEmpaque.getText().toString());
                return true;
/*
                if(edtx_SKU.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto,edtx_SKU,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                    return false;
                }
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                        .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_SKU.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();

                */

            }
        });


        edtx_Cantidad.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
        {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


        binding.checkNumSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkNumSerie.isChecked())
                    binding.edtxnumSerie.setEnabled(true);
                else
                    binding.edtxnumSerie.setEnabled(false);
            }
        });

        edtx_SKU.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(edtx_SKU.getText().toString().equals(""))
                        {

                            edtx_SKU.setText("");
                            edtx_SKU.requestFocus();

                            new popUpGenerico(contexto,edtx_SKU,"Ingrese un SKU." , false, true, true);
                            return false;
                        }

                        edtx_SKU.setText(edtx_SKU.getText().toString().replace(" "," ").replace("\t","").replace("\n",""));

                        int SKUSel = -2;
                        SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,edtx_SKU.getText().toString());

                        switch(SKUSel)
                        {
                            case -2:
                                new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                new esconderTeclado(RecepcionEmpaques.this);

                                h.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_SKU.setText("");
                                        edtx_SKU.requestFocus();
                                    }
                                },100);
                                return false;
                            case -1:
                                int UPCsel= -2;
                                UPCsel = CustomArrayAdapter.getIndex(sp_Partidas,edtx_SKU.getText().toString(),CustomArrayAdapter.TAG2);
                                switch (UPCsel){
                                    case -2:
                                        new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                        new esconderTeclado(RecepcionEmpaques.this);
                                        h.postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                               edtx_SKU.setText("");
                                                edtx_SKU.requestFocus();
                                            }
                                        },100);
                                        break;
                                    case -1:
                                        new popUpGenerico(contexto,edtx_SKU,"No se encontró el SKU dentro del listado de partidas, verifique que sea correcto. [" + edtx_SKU.getText().toString() +"]" , false, true, true);
                                        new esconderTeclado(RecepcionEmpaques.this);
                                        h.postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                               edtx_SKU.setText("");
                                                edtx_SKU.requestFocus();
                                            }
                                        },100);
                                        break;
                                    default:
                                        sp_Partidas.setSelection(UPCsel);
                                        break;
                                }
                                break;
                            default:
                                sp_Partidas.setSelection(SKUSel);
                        }

//                        if(binding.edtxSKU.getText().toString().startsWith("(P)")){
//                            sku = binding.edtxSKU.getText().toString().split("\\|")[0];
//                           // NumSerie = binding.edtxSKU.get
//                        }
                        new esconderTeclado(RecepcionEmpaques.this);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });

        sp_Partidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                txtv_Partida.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag1());
                //txtv_Producto.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag2());
                txtv_CantidadOriginal.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag3());


                Log.i("Tag1",((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag1());
//                if(!edtx_SKU.getText().toString().equals(((Constructor_Dato) sp_Partidas.getSelectedItem()).getDato()))
//                {
                new SegundoPlano("DetallePartida").execute();
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        edtx_Cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && edtx_Cantidad.getText().toString().equals("0")) {
                    try {
                        //edtx_Cantidad.setText("");
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
                       // edtx_EmpxPallet.setText("");
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
                                   // edtx_EmpxPallet.requestFocus();
                                } else {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            h.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    edtx_Cantidad.requestFocus();
                                                   // edtx_Cantidad.setText("");
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
                                       // edtx_Cantidad.setText("");
                                        edtx_Cantidad.requestFocus();

                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                            }
                        } else {

                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                  //  edtx_Cantidad.setText("");
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
                                            new esconderTeclado(RecepcionEmpaques.this);
                                            if (binding.checkNumSerie.isChecked()){
                                                binding.edtxnumSerie.requestFocus();
                                            }else{
                                                edtx_CodigoEmpaque.requestFocus();
                                            }
                                            //
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
                           edtx_EmpxPallet.requestFocus();
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

                        if (Integer.parseInt(edtx_EmpxPallet.getText().toString()) <= 0) {
                            new popUpGenerico(contexto, getCurrentFocus(), "La cantidad ingresada no puede ser menor o igual a cero.", false, true, true);
                            return false;
                        }
                        if (Float.parseFloat(edtx_Cantidad.getText().toString()) <= 0) {
                            new popUpGenerico(contexto, getCurrentFocus(), "La cantidad de empaques no puede ser menor o igual a cero.", false, true, true);
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
                        if (edtx_CodigoEmpaque.getText().toString().equals("")) {
                            new popUpGenerico(RecepcionEmpaques.this, edtx_CodigoEmpaque, "Código de empaque vacio", false, true, true);
                            return false;
                        }
                            new SegundoPlano("RegistrarEmpaqueNuevo").execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        txtv_Pallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(txtv_Pallet.getText().toString().trim().equals("")|| txtv_Pallet.getText().toString().trim().equals("-")){
                    new popUpGenerico(contexto,getCurrentFocus(),"Se debe crear un pallet para podder continuar",false,true,true);

                }else{
                    String[] datos = { txtv_Pallet.getText().toString() };
                    taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_PALLET), FragmentoConsulta.TAG);
                    
                }

                return false;
            }
        });

    }

    private void ReiniciaVariables() { //int registro
        //txtv_EmpaquesRegistrados.setText("");
        try {
            edtx_CodigoEmpaque.setText("");
            edtx_Cantidad.setText("");
            edtx_EmpxPallet.setText("");
            edtx_Cantidad.requestFocus();
            binding.edtxnumSerie.setText("");
           // edtx_CodigoEmpaque.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista, e.getMessage(), false);
        }
    }

    @Override
    public void BotonDerecha() {

        try {
            if (!txtv_EmpaquesRegistrados.getText().toString().equals("")) {
                if (Integer.parseInt(txtv_EmpaquesRegistrados.getText().toString()) > 0) {


                    new CreaDialogos("¿Cerrar tarima?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new SegundoPlano("RegistraPalletNuevo").execute();
                                }
                            }, null, contexto);


                } else {
                    new popUpGenerico(contexto, null, getString(R.string.error_empaque_no_seleccionado), "false", true, true);
                }
            } else {
                new popUpGenerico(contexto, null, getString(R.string.error_empaque_no_seleccionado), "false", true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!=null){
            if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
                taskbar_axc.cerrarFragmento();
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada)
    {
        edtx_Cantidad.setText(strCantidadEscaneada);
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_CodigoEmpaque.requestFocus();
            }
        },100);
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(RecepcionEmpaques.this);

        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        View LastView;

        @Override
        protected void onPreExecute() {

            p.ActivarProgressBar(tarea);

//            LastView = getCurrentFocus();
//            progressBarHolder.requestFocus();
//            recargar=true;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                switch (tarea) {

                    case"Tabla":
                        dao = ca.c_ListarPartidasOCEnProceso(OrdenCompra);
                        break;

                    case"DetallePartida":
                        dao = ca.c_detalleReciboPartida(OrdenCompra,txtv_Partida.getText().toString());
                        break;

                    case "ConsultaPallet":
                        dao = ca.c_ConsultaPalletAbiertoOC(OrdenCompra, PartidaERP);
                        break;

                    case "RegistrarEmpaqueNuevo":


                        Cantidad = edtx_Cantidad.getText().toString();
                        EmpaquesPallet = edtx_EmpxPallet.getText().toString();

                            dao = ca.c_RegistrarEmpaqueCompra(OrdenCompra,
                                    txtv_Partida.getText().toString(),
                                    edtx_CodigoEmpaque.getText().toString(),
                                    " ",
                                    edtx_Cantidad.getText().toString(),
                                    edtx_EmpxPallet.getText().toString(), binding.edtxnumSerie.getText().toString()
                            );


                        break;
                    case "RegistraPalletNuevo":

                        dao = ca.c_CierraPalletCompra(txtv_Pallet.getText().toString());
                        break;

                    case "CerrarRecepcion":

                        dao = ca.c_CerrarRecepcion(OrdenCompra);

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                dao = new DataAccessObject(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (LastView != null) {
                    LastView.requestFocus();
                }
                if (dao.iscEstado()) {

                    switch (tarea) {

                        case"Tabla":
                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(RecepcionEmpaques.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }


                            int SKUSel = -2;
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU);

                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                    new esconderTeclado(RecepcionEmpaques.this);
                                    edtx_SKU.setText("");
                                    edtx_SKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    new popUpGenerico(contexto,edtx_SKU,"La partida con el SKU: [" + SKU +"]" + " ya fué completada" , false, true, true);
                                    new esconderTeclado(RecepcionEmpaques.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            new esconderTeclado(RecepcionEmpaques.this);
                            SKU = "DEFAULT";

                            break;

                        case"DetallePartida":
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProducto"));

                            break;


                        case "RegistrarEmpaqueNuevo":
                           // edtx_EmpxPallet.setText("");
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                            new esconderTeclado(RecepcionEmpaques.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1")) {
                                new popUpGenerico(contexto, LastView, dao.getcMensaje(), String.valueOf(dao.iscEstado()), true, true);
                                new SegundoPlano("RegistraPalletNuevo").execute();
//                                new SegundoPlano("CerrarRecepcion").execute();
                                //  reiniciaVariables();
                                break;
                            } else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1")) {
                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);
                                new SegundoPlano("Tabla").execute();

                                new SegundoPlano("RegistraPalletNuevo").execute();

                                break;
                            } else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1"))) {

                                Log.d(TAG, "onPostExecute() Pallet cerrado " + dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado"));
                                new SegundoPlano("RegistraPalletNuevo").execute();

                            }else {
                                edtx_CodigoEmpaque.setText("");
                                binding.edtxnumSerie.setText("");
                               if (binding.checkNumSerie.isChecked())
                                   binding.edtxnumSerie.requestFocus();
                               else
                                   edtx_CodigoEmpaque.requestFocus();



                            }

                            break;
                        case "ConsultaPallet":

                            Log.i("DAO", dao.getSoapObject_parced().toString());
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));
                            break;
                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, LastView, "Pallet " + "[" + dao.getcMensaje() + "] Cerrado con éxito", String.valueOf(dao.iscEstado()), true, true);
                            new SegundoPlano("ConsultaPallet").execute();
                            ReiniciaVariables();
                            break;
                        case "CierraPalletSinConsulta":
                            new popUpGenerico(contexto, LastView, "Pallet " + "[" + dao.getcMensaje() + "] Cerrado con éxito", String.valueOf(dao.iscEstado()), true, true);
                            ReiniciaVariables();
                            txtv_Pallet.setText("");
                            break;

                        case "CerrarRecepcion":
                        default:

                            new popUpGenerico(contexto, LastView, dao.getcMensaje(), dao.iscEstado(), true, true);

                            break;

                    }
                } else {
                    switch (tarea) {
                        case "RegistrarEmpaqueNuevo":
                            if (dao.getcMensaje().contains("Error SAP")) {
                                CreaDialogos cd = new CreaDialogos(contexto);


                                String sourceString = "<p>Se ha presentado un error al registrar en sap.</p> <p>" + dao.getcMensaje() + "</p>  <p><b>¿Registrar de todas maneras en AXC?</b></p>";

                                cd.dialogoDefault("VALIDE LA INFORMACIÓN", Html.fromHtml(sourceString),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                new SegundoPlano("RegistrarEmpaqueNuevoSA").execute();
                                            }
                                        }, null);
                                break;
                            }

                        default:
                            //ReiniciaVariables();
                            new esconderTeclado(RecepcionEmpaques.this);
                            edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();

                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), String.valueOf(dao.iscEstado()), true, true);
                    }
                }

                p.DesactivarProgressBar(tarea);
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
            recargar = false;
        }
    }
}