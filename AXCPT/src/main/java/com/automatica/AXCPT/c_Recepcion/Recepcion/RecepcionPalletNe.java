package com.automatica.AXCPT.c_Recepcion.Recepcion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro_Pallet_NE_LoteModificable;
import com.automatica.AXCPT.databinding.ActivityRecepcionContenedorBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionPalletNeBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
//import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class RecepcionPalletNe extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener {

    frgmnt_taskbar_AXC taskbar_axc;
    popUpGenerico pop; //popUpGenerico pop = new popUpGenerico(RecepcionPalletNe.this);
    ActivityRecepcionPalletNeBinding binding;
    EditText edtx_Cantidad,edtx_EmpxPallet,edtx_SKU, edtx_numSerie;
    CheckBox checkNumSerie;
    String  Cantidad,CodigoEmpaque;
    String mercado;
    private ProgressBarHelper p;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private Spinner sp_Partidas;
    String NumSerie;
    private Spinner sp_NumSerie;
    Handler handler = new Handler();
    TextView txtv_EmpaquesRegistrados,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet ,txtv_CantRegLote;
    Bundle b;

    View vista;
    Context contexto = this;
    String OrdenCompra, FechaCaducidad,ModificaCant,PartidaERP,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,SKU;
    TextView txtv_OrdenCompra;
    boolean recargar;
    int registroAnteriorSpinner=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);
            binding = ActivityRecepcionPalletNeBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Recepción");
            getSupportActionBar().setSubtitle("Pallet no etiquetado");
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


            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();
            new SegundoPlano("ConsultaPallet").execute();
            new SegundoPlano("Tabla").execute();
            edtx_SKU.requestFocus();

        }catch (Exception e){
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,e.getMessage()+" "+e.getCause() ,"false" ,true,true);
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
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

        if (p.ispBarActiva())
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
            p = new ProgressBarHelper(this);
            edtx_numSerie = (EditText) findViewById(R.id.edtx_numSerie);
            //  edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Factura);
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


            txtv_Partida = (TextView) findViewById(R.id.txtv_Partida);
            txtv_UM = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_CantRegLote = (TextView) findViewById(R.id.txtv_RegLote);

            txtv_Partida.setText(PartidaERP);
            txtv_UM.setText(UM);

            txtv_Producto.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_OrdenCompra.setText(OrdenCompra);

            checkNumSerie = binding.checkNumSerie.findViewById(R.id.checkNumSerie);

            edtx_SKU = findViewById(R.id.edtx_SKU);
            sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);
            sp_NumSerie = binding.vwSpinner2.findViewById(R.id.spinner);
        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }

    private void AgregaListeners()
    {

        binding.toggleNumSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.toggleNumSerie.isChecked()){
                    binding.edtxNumSerie.setVisibility(View.VISIBLE);
                    binding.vwSpinner2.setVisibility(View.GONE);
                }else{
                    binding.edtxNumSerie.setVisibility(View.GONE);
                    binding.vwSpinner2.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.edtxSKU.setOnKeyListener(new View.OnKeyListener()
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

                            new popUpGenerico(contexto,edtx_SKU,"Ingrese un artículo." , false, true, true);
                            return false;
                        }

                        edtx_SKU.setText(edtx_SKU.getText().toString());

                        int SKUSel = -2;
                        SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,edtx_SKU.getText().toString());

                        switch(SKUSel)
                        {
                            case -2:
                                new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                new esconderTeclado(RecepcionPalletNe.this);

                                handler.postDelayed(new Runnable()
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
                                        new esconderTeclado(RecepcionPalletNe.this);
                                        handler.postDelayed(new Runnable()
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
                                        new popUpGenerico(contexto,edtx_SKU,"No se encontro el artículo dentro del listado de partidas, verifique que sea correcto. [" + edtx_SKU.getText().toString() +"]" , false, true, true);
                                        new esconderTeclado(RecepcionPalletNe.this);
                                        handler.postDelayed(new Runnable()
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
                                break;
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.edtxLote.requestFocus();
                            }
                        });
                        //new esconderTeclado(RecepcionPalletNe.this);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });

        binding.edtxLote.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER)){
                            /*if(binding.edtxLote.getText().toString().equals("")){

                            }*/
                        //binding.edtxEmpaque.requestFocus();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.edtxEmpaque.requestFocus();
                                //new esconderTeclado(RecepcionPalletNe.this);
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
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
                //new SegundoPlano("DetallePartida").execute();
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        sp_NumSerie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

//                txtv_Partida.setText(((Constructor_Dato) sp_NumSerie.getSelectedItem()).getTag1());
//                //txtv_Producto.setText(((Constructor_Dato) sp_NumSerie.getSelectedItem()).getTag2());
//                txtv_CantidadOriginal.setText(((Constructor_Dato) sp_NumSerie.getSelectedItem()).getTag3());
//
//
//                Log.i("Tag1",((Constructor_Dato) sp_NumSerie.getSelectedItem()).getTag1());
////                if(!edtx_SKU.getText().toString().equals(((Constructor_Dato) sp_Partidas.getSelectedItem()).getDato()))
////                {
//                new SegundoPlano("DetallePartida").execute();
////                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        /*edtx_Cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
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
        });*/


        edtx_Cantidad.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (edtx_Cantidad.getText().toString().equals(""))
                        {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();

                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);
                            return false;

                        }
                        try {
                            if (!(Float.parseFloat(edtx_Cantidad.getText().toString()) > 999999)) {

                                edtx_EmpxPallet.requestFocus();

                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_Cantidad.requestFocus();
                                                edtx_Cantidad.setText("");
                                            }
                                        });
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                return  false;

                            }
                        }catch (NumberFormatException ex)
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();

                                }
                            });
                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_cantidad_valida),"false",true,true);
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
                        if(edtx_EmpxPallet.getText().toString().equals(""))
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_EmpxPallet.setText("");
                                    edtx_EmpxPallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,getCurrentFocus(),"Ingrese candidad de empaques" , "false", true, true);
                            return false;
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


                        if(edtx_Cantidad.getText().toString().equals(""))
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_EmpxPallet.setText("");
                                    edtx_EmpxPallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_cantidad)  , "false", true, true);
                            return false;
                        }
                        if (edtx_EmpxPallet.getText().toString().equals(""))
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_EmpxPallet.setText("");
                                    edtx_EmpxPallet.requestFocus();
                                }
                            });

                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_cantidad) , false, true, true);
                            return false;
                        }
                        new SegundoPlano("RegistrarEmpaqueNuevo").execute();


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });


        edtx_Cantidad.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {

              /*  if(edtx_SKU.getText().toString().equals(""))
                {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,edtx_SKU,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                    return false;
                }
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                        .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_SKU.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();*/


                return true;
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


        txtv_Pallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(txtv_Pallet.getText().toString().trim().equals("")||txtv_Pallet.getText().toString().trim().equals("-")){

                    new popUpGenerico(contexto,getCurrentFocus(),"Se debe crear un pallet para poder continuar",false,true,true);
                }else{
                    String[] datos={txtv_Pallet.getText().toString()};
                    taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_PALLET),FragmentoConsulta.TAG);
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
            SKU= b.getString("SKU");
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
            edtx_Cantidad.setText("");
            edtx_EmpxPallet.setText("");
            binding.edtxLote.setText("");
            binding.edtxLote.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }


    @Override
    public void BotonDerecha() {
        try {
            if (!txtv_EmpaquesRegistrados.getText().toString().equals("")) {
                if (Integer.parseInt(txtv_EmpaquesRegistrados.getText().toString()) > 0) {


                    new CreaDialogos("¿Cerrar pallet?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new RecepcionPalletNe.SegundoPlano("RegistraPalletNuevo").execute();
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
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada) {

        edtx_Cantidad.setText(strCantidadEscaneada);
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_EmpxPallet.requestFocus();
            }
        },100);

    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(RecepcionPalletNe.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        View LastView;
        @Override
        protected void onPreExecute()
        {

            p.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                switch (tarea)
                {

                    case"Tabla":
                        dao = ca.c_ListarPartidasOCEnProceso(OrdenCompra);
                        break;

                    case"DetallePartida":
                        dao = ca.c_detalleReciboPartida(OrdenCompra,txtv_Partida.getText().toString());
                        break;

                    case "ConsultaPallet":

                        dao= ca.c_ConsultaPalletAbiertoOC(OrdenCompra, PartidaERP);
                        break;
                    case "RegistrarEmpaqueNuevo":
                        if(!binding.toggleNumSerie.isChecked()){
                            NumSerie = binding.edtxNumSerie.getText().toString();
                        }
                        else{
                            NumSerie = ((Constructor_Dato)sp_NumSerie.getSelectedItem()).getDato();
                        }


                        CodigoEmpaque = edtx_EmpxPallet.getText().toString();
                        Cantidad = edtx_Cantidad.getText().toString();


                            dao = ca.c_RegistrarPalletCompra_NE(OrdenCompra,
                                    txtv_Partida.getText().toString(),
                                    binding.edtxLote.getText().toString(),
                                    edtx_Cantidad.getText().toString(),
                                    edtx_EmpxPallet.getText().toString(), NumSerie);

                        break;
                    case "RegistraPalletNuevo":

                        dao = ca.c_CierraPalletCompra(txtv_Pallet.getText().toString());
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

                        case"Tabla":
                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(RecepcionPalletNe.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("Artículo","Partida","Artículo","Cant. Total")));

                                sp_NumSerie.setAdapter(c = new CustomArrayAdapter(RecepcionPalletNe.this,
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
                                    new esconderTeclado(RecepcionPalletNe.this);
                                    edtx_SKU.setText("");
                                    edtx_SKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    new popUpGenerico(contexto,edtx_SKU,"No se encontro el artículo dentro del listado de partidas, verifique que sea correcto. [" + SKU +"]" , false, true, true);
                                    new esconderTeclado(RecepcionPalletNe.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            new esconderTeclado(RecepcionPalletNe.this);

                            SKU = "DEFAULT";
                            break;

                        case"DetallePartida":
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProducto"));

                                new esconderTeclado(RecepcionPalletNe.this);


                            break;

                        case "RegistrarEmpaqueNuevo":
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            new esconderTeclado(RecepcionPalletNe.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);
//                                                new SegundoPlano("CerrarRecepcion").execute();
                                new SegundoPlano("RegistraPalletNuevo").execute();
                                //  reiniciaVariables();
                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new SegundoPlano("Tabla").execute();

                                new SegundoPlano("RegistraPalletNuevo").execute();

                                break;
                            }
                            else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                            {

                                new SegundoPlano("RegistraPalletNuevo").execute();

                            }

                            edtx_Cantidad.setText("");
                            edtx_EmpxPallet.setText("");
                            edtx_Cantidad.requestFocus();

                            break;
                        case "ConsultaPallet":

                            Log.i("DAO", dao.getSoapObject_parced().toString());
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));
                            break;
                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, LastView,"Pallet "+"["+dao.getcMensaje()+"] cerrado con éxito.",dao.iscEstado(), true, true);
                            new SegundoPlano("ConsultaPallet").execute();
                            break;


                        case "CerrarRecepcion":

                            new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);

                            break;
                    }
                }
                else
                {
                    reiniciaVariables();
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                }

                p.DesactivarProgressBar(tarea);
            }catch (Exception e)
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
                                           // new SegundoPlano("RegistrarEmpaqueNuevoSA").execute();
                                        }
                                    }, null);
                            break;
                        }

                    default:
                        reiniciaVariables();
                        new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                }
            }
            recargar = false;
        }
    }


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
}