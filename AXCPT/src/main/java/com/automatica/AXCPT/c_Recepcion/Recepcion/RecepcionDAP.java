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
import android.text.InputType;
import android.text.TextUtils;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
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


import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Surtido_Empaque_Armado;
import com.automatica.AXCPT.databinding.ActivityRecepcionDapBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class RecepcionDAP extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar , frgmnt_SKU_Conteo.OnFragmentInteractionListener{
    private frgmnt_taskbar_AXC taskbar_axc;
    private popUpGenerico pop = new popUpGenerico(RecepcionDAP.this);
    private ActivityRecepcionDapBinding binding;
    private EditText edtx_Empaque,edtx_CanXPaqt, edtx_Posicion,edtx_SKU;
    private ProgressBarHelper p;
    private TextView txtv_EmpaquesRegistrados,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet ,txtv_CantRegLote;
    private Bundle b;
    private View vista;
    private Context contexto = this;
    private String OrdenCompra,PartidaERP,NumParte,SKU,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    private TextView txtv_OrdenCompra;
    private Handler h = new Handler();
    private Spinner sp_Partidas;
    private CheckBox cb_EditarCantidad;
    boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecepcionDapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recepción");
        getSupportActionBar().setSubtitle("Directo a Posición");
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

        new SegundoPlano("Tabla").execute();



    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.toolbar_ctrlz, menu);
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
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            p = new ProgressBarHelper(this);

            edtx_Empaque = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CanXPaqt = (EditText) findViewById(R.id.edtx_CantXEmp);
            edtx_CanXPaqt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Posicion = (EditText) findViewById(R.id.edtx_Posicion);
            edtx_Posicion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_SKU  = (EditText) findViewById(R.id.edtx_SKU);

            cb_EditarCantidad = (CheckBox) findViewById(R.id.cb_EditarCantidad);
            check = cb_EditarCantidad.isChecked();

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

            edtx_CanXPaqt.setText("1");
            edtx_CanXPaqt.setEnabled(false);

        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }


    private void AgregaListeners()
    {



        cb_EditarCantidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                edtx_CanXPaqt.setEnabled(b);
                if(b)
                {

                    edtx_CanXPaqt.setText("");


                }else
                {

                    edtx_CanXPaqt.setText("1");

                }
            }
        });



        edtx_SKU.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {


                if(hasFocus&&!edtx_SKU.getText().toString().equals(""))
                {
                    try
                    {
                        edtx_SKU.setText("");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, edtx_SKU, e.getMessage(), "false", true, true);
                    }
                }

            }
        });

        edtx_Posicion.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                try
                {
                    edtx_Posicion.setText("");
                    txtv_Pallet.setText("");
                    txtv_EmpaquesRegistrados.setText("");
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
                return false;
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

                        edtx_SKU.setText(edtx_SKU.getText().toString().replace(" ","").replace("\t","").replace("\n",""));

                        int SKUSel = -2;
                        SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,edtx_SKU.getText().toString());

                        switch(SKUSel)
                        {
                            case -2:
                                new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                new esconderTeclado(RecepcionDAP.this);

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
                                        new esconderTeclado(RecepcionDAP.this);
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
                                        new esconderTeclado(RecepcionDAP.this);
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

                        new esconderTeclado(RecepcionDAP.this);
                        if(edtx_Posicion.getText().toString().equals(""))
                        {
                            edtx_Posicion.requestFocus();
                        }else
                        {
                            h.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {

                                    edtx_Empaque.requestFocus();

                                }
                            },250);
                            new esconderTeclado(RecepcionDAP.this);

                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });


        edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
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
                            new popUpGenerico(contexto,getCurrentFocus(),"Ingrese una posición." , "false", true, true);
                            return false;
                        }

                        if(edtx_SKU.getText().toString().equals(""))
                        {

                            edtx_SKU.setText("");
                            edtx_SKU.requestFocus();

                            new popUpGenerico(contexto,getCurrentFocus(),"Ingrese un SKU." , false, true, true);
                            return false;
                        }
                        if (edtx_Empaque.getText().toString().equals(""))
                        {

                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_Empaque.setText("");
                                    edtx_Empaque.requestFocus();

                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad) +  "Cantidad Empaques","Advertencia", true, true);

                        }

                        if (edtx_CanXPaqt.getText().toString().equals("") && !cb_EditarCantidad.isChecked())
                        {

                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_CanXPaqt.setText("");
                                    edtx_CanXPaqt.requestFocus();

                                }
                            });
                            new popUpGenerico(contexto, edtx_CanXPaqt,getString(R.string.error_ingrese_cantidad) + "Cantidad por Empaque","Advertencia", true, true);

                        }

                        try {

                            if (!(Float.parseFloat(edtx_CanXPaqt.getText().toString()) > 999999))
                            {
                                new SegundoPlano("RegistrarEmpaqueNuevo").execute();
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
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
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

                            edtx_Posicion.setText("");
                            edtx_Posicion.requestFocus();

                            new popUpGenerico(contexto,getCurrentFocus(),"Ingrese posición de piso." , "false", true, true);
                            return false;
                        }

                        new SegundoPlano("ConsultarPosicion").execute(edtx_Posicion.getText().toString());

                        edtx_SKU.requestFocus();

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        edtx_CanXPaqt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(TextUtils.isEmpty(edtx_CanXPaqt.getText().toString()))
                {
                    pop.popUpGenericoDefault(vista,"Ingrese una cantidad por empaques",false);
                    edtx_CanXPaqt.requestFocus();
                    return false;
                }

                return false;
            }
        });



        edtx_CanXPaqt.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {

                if(edtx_SKU.getText().toString().equals(""))
                {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,edtx_SKU,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                    return false;
                }
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                        .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_SKU.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();


                return false;
            }
        });


        binding.edtxEmpaque.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
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

                if(edtx_Posicion.getText().toString().equals(""))
                {
                    edtx_Posicion.requestFocus();
                }else
                {
                    h.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            edtx_Empaque.requestFocus();

                        }
                    },250);
                    new esconderTeclado(RecepcionDAP.this);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


        txtv_Pallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(txtv_Pallet.getText().toString().trim().equals("") || txtv_Pallet.getText().toString().trim().equals("-")){

                    new popUpGenerico(contexto,getCurrentFocus(),"Se debe registrar una posición primero ", false,true,true);
                }else{

                    String[] datos = { txtv_Pallet.getText().toString() };
                    taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance( datos, FragmentoConsulta.TIPO_POSICION), FragmentoConsulta.TAG);
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
            edtx_Posicion.setText("");
            edtx_Posicion.requestFocus();
            edtx_Empaque.setText("");
            edtx_CanXPaqt.setText("1");
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada)
    {
        edtx_CanXPaqt.setText(strCantidadEscaneada);
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_Empaque.requestFocus();
            }
        },100);
    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(RecepcionDAP.this);
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
        protected Void doInBackground(String... params)
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

                    case "ConsultarPosicion":
                        dao= ca.c_ConsultaPosicionPisoPalletOC(OrdenCompra, PartidaERP,params[0]);
                        break;


                    case "RegistrarEmpaqueNuevo":

                        if(cb_EditarCantidad.isChecked()){

                            dao = ca.cRegistroEmpaqueDaP(
                                    OrdenCompra,
                                    txtv_Partida.getText().toString(),
                                    edtx_Empaque.getText().toString(),
                                    "",
                                    edtx_CanXPaqt.getText().toString(),
                                    "1",txtv_Pallet.getText().toString()
                            );


                        }else{

                            dao = ca.cRegistroEmpaqueDaP(
                                    OrdenCompra,
                                    txtv_Partida.getText().toString(),
                                    edtx_Empaque.getText().toString(),
                                    "",
                                    edtx_CanXPaqt.getText().toString(),
                                    "1",txtv_Pallet.getText().toString()
                            );
                        }



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
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(RecepcionDAP.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }

                            if(edtx_Posicion.getText().toString().equals(""))
                            {
                                edtx_Posicion.requestFocus();
                            }else
                            {
                                h.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        edtx_Empaque.requestFocus();

                                    }
                                },250);

                            }

                            int SKUSel = -2;
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU);

                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                    new esconderTeclado(RecepcionDAP.this);
                                    edtx_SKU.setText("");
                                    edtx_SKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    new popUpGenerico(contexto,edtx_SKU,"No se encontro el SKU dentro del listado de partidas, verifique que sea correcto. [" + SKU +"]" , false, true, true);
                                    new esconderTeclado(RecepcionDAP.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            new esconderTeclado(RecepcionDAP.this);

                            SKU = "DEFAULT";
                            break;

                        case"DetallePartida":
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProducto"));

                            if(edtx_Posicion.getText().toString().equals(""))
                            {
                                edtx_Posicion.requestFocus();
                            }else
                            {
                                h.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        edtx_Empaque.requestFocus();

                                    }
                                },250);
                                new esconderTeclado(RecepcionDAP.this);

                            }
                            break;

                        case "RegistrarEmpaqueNuevo":
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            edtx_SKU.setText("");
                            edtx_SKU.requestFocus();
                            edtx_Empaque.setText("");




                            new esconderTeclado(RecepcionDAP.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new  popUpGenerico(contexto, edtx_Empaque, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);


                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, edtx_Empaque, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new SegundoPlano("Tabla").execute();

//                                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }

                            new SegundoPlano("ConsultarPosicion").execute(txtv_Pallet.getText().toString());


                            break;
                        case "ConsultaPallet":
                        case "ConsultarPosicion":

                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));

                            new esconderTeclado(RecepcionDAP.this);
                            edtx_SKU.requestFocus();
                            break;

                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, LastView,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            new SegundoPlano("ConsultaPallet").execute();
                            break;


                        case "CerrarRecepcion":

                            new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);

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

                                            }
                                        }, null);
                                break;
                            }else
                            {
                                edtx_Empaque.setText("");
                                edtx_SKU.setText("");
                                edtx_SKU.requestFocus();
                                new popUpGenerico(contexto,edtx_SKU, dao.getcMensaje(),dao.iscEstado(), true, true);
                                break;
                            }

                        default:
                            reiniciaVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                    }
                }

                p.DesactivarProgressBar(tarea);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
        }
    }


    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();

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