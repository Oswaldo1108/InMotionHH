package com.automatica.AXCPT.c_Almacen.Devolucion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.Devolucion_NE;

import com.automatica.AXCPT.c_Traspasos.Recibe.RecepcionTraspasoPalletNE;
import com.automatica.AXCPT.databinding.ActivityDevolucionEmpaqueUnicoBinding;
import com.automatica.AXCPT.databinding.ActivityDevolucionPalletNeBinding;

import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class DevolucionPalletNE extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener {

    frgmnt_taskbar_AXC taskbar_axc;
    popUpGenerico pop = new popUpGenerico(DevolucionPalletNE.this);
    @NonNull ActivityDevolucionPalletNeBinding binding;
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
    String documento, FechaCaducidad,ModificaCant,PartidaERP,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,SKU;
    TextView txtv_OrdenCompra;
    boolean recargar;
    Handler h = new Handler();
    int registroAnteriorSpinner=0;
    CreaDialogos creaDialogos = new CreaDialogos(DevolucionPalletNE.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);
            binding = ActivityDevolucionPalletNeBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Devolución");
            getSupportActionBar().setSubtitle("Pallet No Etiquetado");
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

            //new DevolucionPalletNE.SegundoPlano("Tabla").execute();
            edtx_SKU.requestFocus();

        }catch (Exception e){
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,e.getMessage()+" "+e.getCause() ,"false" ,true,true);
        }

    }

    @Override
    protected void onResume() {
        new DevolucionPalletNE.SegundoPlano("Tabla").execute();
        new DevolucionPalletNE.SegundoPlano("ConsultaPallet").execute();
        super.onResume();
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

            //txtv_Producto.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_OrdenCompra.setText(documento);

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
                                new esconderTeclado(DevolucionPalletNE.this);

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
                                        new esconderTeclado(DevolucionPalletNE.this);
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
                                        new popUpGenerico(contexto,edtx_SKU,"No se encontro el SKU dentro del listado de partidas, verifique que sea correcto. [" + edtx_SKU.getText().toString() +"]" , false, true, true);
                                        new esconderTeclado(DevolucionPalletNE.this);
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

                        new esconderTeclado(DevolucionPalletNE.this);


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
                txtv_CantidadOriginal.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag3());
                new DevolucionPalletNE.SegundoPlano("ConsultarPartida").execute();



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


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        edtx_Cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener()
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
                        if (edtx_Cantidad.getText().toString().equals(""))
                        {

                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();

                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);

                        }
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
                        }catch (NumberFormatException ex)
                        {
                            h.post(new Runnable()
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
                            h.post(new Runnable() {
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
                            h.post(new Runnable() {
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
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_EmpxPallet.setText("");
                                    edtx_EmpxPallet.requestFocus();
                                }
                            });

                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_cantidad) , false, true, true);
                            return false;
                        }
                        new DevolucionPalletNE.SegundoPlano("RegistrarEmpaqueNuevo").execute();


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
            documento= b.getString("Documento");
            PartidaERP= b.getString("Partida");
            NumParte= b.getString("NumParte");
            SKU = b.getString("NumParte");
            UM= b.getString("UM");
            CantidadTotal= b.getString("CantidadTotal");
            CantidadRecibida= b.getString("CantidadRecibida");
            if (!documento.isEmpty()){
                binding.txtvOC.setText(documento);
                binding.txtvPartida.setText(PartidaERP);
            }

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
            edtx_EmpxPallet .setText("");
            edtx_Cantidad.requestFocus();
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


                    new CreaDialogos("¿Cerrar tarima?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DevolucionPalletNE.SegundoPlano("RegistraPalletNuevo").execute();
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
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(DevolucionPalletNE.this);

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
                    case "Tabla":
                        dao = ca.c_ListarPartidasDevEnProceso(documento);
                        break;


                    case "ConsultarPartida":
                        dao= ca.c_ConsultaPartidaDevolucion(documento,binding.txtvPartida.getText().toString());
                        break;

                    case "ConsultaPallet":
                        dao = ca.c_ConsultaPalletAbiertoDev(documento, PartidaERP);
                        break;

                    case "RegistrarEmpaqueNuevo":
                      /*  if(!binding.toggleNumSerie.isChecked()){
                            NumSerie = binding.edtxNumSerie.getText().toString();
                        }
                        else{
                            NumSerie = ((Constructor_Dato)sp_NumSerie.getSelectedItem()).getDato();
                        }*/


                        CodigoEmpaque = edtx_EmpxPallet.getText().toString();
                        Cantidad = edtx_Cantidad.getText().toString();


                        dao = ca.c_CreaEmpaqueDevSE(documento,
                                binding.txtvPartida.getText().toString(),
                                "",
                                binding.edtxEmpaque.getText().toString(),
                                binding.edtxPrimerEmpaque.getText().toString()
                        );

                        break;
                    case "RegistraPalletNuevo":

                        dao=ca.c_CierraPalletDevolucion(binding.txtvPallet.getText().toString());
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
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(DevolucionPalletNE.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }


                            int SKUSel = -2;
                            SKU = SKU.replace(" ", "");
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU.trim());
                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,binding.edtxSKU,"Error interno." , false, true, true);
                                    new esconderTeclado(DevolucionPalletNE.this);
                                    binding.edtxSKU.setText("");
                                    binding.edtxSKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    new popUpGenerico(contexto,binding.edtxSKU,"La partida con el SKU: [" + SKU +"]" + " ya fué completada" , false, true, true);
                                    new esconderTeclado(DevolucionPalletNE.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            new esconderTeclado(DevolucionPalletNE.this);
                            SKU = "DEFAULT";

                            break;


                        case "ConsultaPallet":

                            String CodigoPallet, Empaques;
                            String[] mensaje= dao.getcMensaje().split("#");
                            CodigoPallet = mensaje[0];
                            Empaques = mensaje[1];
                            if(!CodigoPallet.equals(""))
                            {
                                binding.txtvPallet.setText(CodigoPallet);
                                binding.txtvEmpReg.setText(Empaques);
                            }else {
                                binding.txtvPallet.setText("-");
                                binding.txtvEmpReg.setText("-");
                            }
                            break;


                        case "ConsultarPartida":
                            binding.txtvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            binding.txtvProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));
                            binding.txtvCaducidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));
                            break;

                        case "RegistrarEmpaqueNuevo":
                            /*txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");*/
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                creaDialogos.CerrarDia("Orden de devolución completada.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new DevolucionPalletNE.SegundoPlano("RegistraPalletNuevo").execute();
                                        Cerrar();
                                    }
                                },null,contexto);

                                //new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);
                                return;
                            }
                            else {
                                if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1")) {
                                    new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                    new DevolucionPalletNE.SegundoPlano("Tabla").execute();
                                    new DevolucionPalletNE.SegundoPlano("ConsultarPartida").execute();
                                    new DevolucionPalletNE.SegundoPlano("RegistraPalletNuevo").execute();

                                }

                             /*   else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1"))) {
                                    new DevolucionPalletNE.SegundoPlano("RegistraPalletNuevo").execute();
                                }*/
                                binding.edtxSKU.setText("");
                                binding.edtxEmpaque.setText("");
                                binding.edtxPrimerEmpaque.setText("");
                                binding.edtxSKU.requestFocus();
                                new DevolucionPalletNE.SegundoPlano("ConsultaPallet").execute();

                            }
                            break;

                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, LastView,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            new DevolucionPalletNE.SegundoPlano("ConsultaPallet").execute();
                            break;


                       /* case "CerrarRecepcion":

                            new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);

                            break;*/
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

    public void Cerrar() {
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
        Intent intent = new Intent(DevolucionPalletNE.this, SeleccionOrdenDevolucion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}