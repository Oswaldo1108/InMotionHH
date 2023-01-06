package com.automatica.AXCPT.c_Recepcion.Recepcion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityRecepcionContenedorBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class RecepcionContenedor extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar {

    private frgmnt_taskbar_AXC taskbar_axc;
    private popUpGenerico pop = new popUpGenerico(RecepcionContenedor.this);
    private ActivityRecepcionContenedorBinding binding;
    private EditText edtx_CantidadPiezasoSKU,edtx_CanXPaqt, edtx_Posicion,edtx_Contenedor;
    private String  Cantidad,CodigoEmpaque;
    private ProgressBarHelper p;
    private Switch Switch;
    private TextView txtv_EmpaquesRegistrados,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet ,txtv_CantRegLote,tvCambio;
    private Bundle b;
    private View vista;
    private Context contexto = this;
    private String OrdenCompra,PartidaERP,NumParte,UM,SKU,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    private TextView txtv_OrdenCompra;
    private Handler h = new Handler();
    private int registroAnteriorSpinner=0;
    private Spinner sp_Partidas;
    String stringSKU;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {


            super.onCreate(savedInstanceState);
            binding = ActivityRecepcionContenedorBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Recepción");
            getSupportActionBar().setSubtitle("Contenedor");
            View logoView = getToolbarLogoIcon(toolbar);
            logoView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null)
                    {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                                .add(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                    } else
                    {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });

            taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("", "");
            getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal, taskbar_axc, "FragmentoTaskBar").commit();

            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();

            new SegundoPlano("Tabla").execute();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
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
        try
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Recepción");
            p = new ProgressBarHelper(this);

            edtx_CantidadPiezasoSKU = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_CantidadPiezasoSKU.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_Posicion = (EditText) findViewById(R.id.edtx_Posicion);
            edtx_Posicion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Contenedor = findViewById(R.id.edtx_contenedor);
            edtx_Contenedor.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);

            tvCambio = findViewById(R.id.tvcambio);
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
            Switch = findViewById(R.id.Checkboxsku);
        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }

    private void AgregaListeners()
    {
        Switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Switch.isChecked()){

                    edtx_CantidadPiezasoSKU.setHint("Capture SKU");
                    edtx_CantidadPiezasoSKU.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                    edtx_CantidadPiezasoSKU.setText("");
                    tvCambio.setText("SKU");



                }else{
                    edtx_CantidadPiezasoSKU.setHint("Capture cantidad");
                    edtx_CantidadPiezasoSKU.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                    edtx_CantidadPiezasoSKU.setText("");
                    tvCambio.setText("Piezas");


                }

            }
        });

        edtx_CantidadPiezasoSKU.setOnKeyListener(new View.OnKeyListener()
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
                            new popUpGenerico(contexto,getCurrentFocus(),"Ingrese posición de piso." , false, true, true);
                            return false;
                        }

                        if (edtx_CantidadPiezasoSKU.getText().toString().equals(""))
                        {

                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {  edtx_CantidadPiezasoSKU.setText("");
                                    edtx_CantidadPiezasoSKU.requestFocus();

                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad) +  "Cantidad Paquetes",false, true, true);

                        }

                        try {
                            if(Switch.isChecked())
                            {
                                if(TextUtils.isEmpty(edtx_CantidadPiezasoSKU.getText().toString()))
                                {
                                    new popUpGenerico(contexto, edtx_CantidadPiezasoSKU, "Ingrese un SKU", false, true, true);
                                    return false;
                                }

                                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                                {
                                    try
                                    {
                                        if(edtx_CantidadPiezasoSKU.getText().toString().equals(""))
                                        {

                                            edtx_CantidadPiezasoSKU.setText("");
                                            edtx_CantidadPiezasoSKU.requestFocus();

                                            new popUpGenerico(contexto,edtx_CantidadPiezasoSKU,"Ingrese un SKU." , false, true, true);
                                            return false;
                                        }

                                        int SKUSel = -2;
                                        SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,edtx_CantidadPiezasoSKU.getText().toString());
                                        Log.i("Retorno", String.valueOf(SKUSel));

                                        switch(SKUSel)
                                        {
                                            case -2:
                                                new popUpGenerico(contexto,edtx_CantidadPiezasoSKU,"Error interno." , false, true, true);
                                                new esconderTeclado(RecepcionContenedor.this);
                                                edtx_CantidadPiezasoSKU.setText("");
                                                edtx_CantidadPiezasoSKU.requestFocus();
                                                return false;
                                            case -1:
                                                int UPCsel= -2;
                                                UPCsel = CustomArrayAdapter.getIndex(sp_Partidas,edtx_CantidadPiezasoSKU.getText().toString(),CustomArrayAdapter.TAG2);
                                                Log.i("Retorno2", String.valueOf(UPCsel));
                                                switch (UPCsel){
                                                    case -2:
                                                        new popUpGenerico(contexto,edtx_CantidadPiezasoSKU,"Error interno." , false, true, true);
                                                        new esconderTeclado(RecepcionContenedor.this);
                                                        edtx_CantidadPiezasoSKU.setText("");
                                                        edtx_CantidadPiezasoSKU.requestFocus();
                                                        return false;
                                                    case -1:
                                                        new popUpGenerico(contexto,edtx_CantidadPiezasoSKU,"No se encontro el SKU dentro del listado de partidas, verifique que sea correcto. [" + edtx_CantidadPiezasoSKU.getText().toString() +"]" , false, true, true);
                                                        new esconderTeclado(RecepcionContenedor.this);
                                                        edtx_CantidadPiezasoSKU.setText("");
                                                        edtx_CantidadPiezasoSKU.requestFocus();
                                                        return false;
                                                    default:
                                                        sp_Partidas.setSelection(UPCsel);
                                                        break;
                                                }
                                                break;
                                                default:
                                                sp_Partidas.setSelection(SKUSel);
                                                break;
                                        }

                                        new esconderTeclado(RecepcionContenedor.this);
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                                    }
                                }
                            } else
                            {

                                if (TextUtils.isEmpty(edtx_CantidadPiezasoSKU.getText().toString()))
                                {
                                    new popUpGenerico(contexto, edtx_CantidadPiezasoSKU, getString(R.string.error_ingrese_cantidad), false, true, true);
                                    return false;
                                }

                                if (Float.parseFloat(edtx_CantidadPiezasoSKU.getText().toString()) > 999999)
                                {
                                    edtx_CantidadPiezasoSKU.requestFocus();
                                    edtx_CantidadPiezasoSKU.setText("");
                                    new popUpGenerico(contexto,edtx_CantidadPiezasoSKU, getString(R.string.error_cantidad_mayor_999999), false, true, true);
                                    return false;
                                }

                            }
                            new SegundoPlano("RegistrarEmpaqueNuevo").execute();


                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
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
                        edtx_Contenedor.requestFocus();

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        edtx_Contenedor.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(edtx_Contenedor.getText().toString().equals(""))
                        {
                            edtx_Contenedor.setText("");
                            edtx_Contenedor.requestFocus();
                            new popUpGenerico(contexto,edtx_Contenedor,"Ingrese posición de piso." , "false", true, true);
                            return false;
                        }

                        new SegundoPlano("ConsultaContenedor").execute();
                        edtx_CantidadPiezasoSKU.requestFocus();

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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
                Log.i("Tag1",((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag2());
                txtv_Partida.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag1());
                //txtv_Producto.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag2());
                txtv_CantidadOriginal.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag3());
                stringSKU = ((Constructor_Dato) sp_Partidas.getSelectedItem()).getDato();
                Log.i("SKU",stringSKU);

                new SegundoPlano("DetallePartida").execute();
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

                new popUpGenerico(contexto,getCurrentFocus(),"Se debe crear un pallet para poder continuar",false,true,true);
            }else{
                String[] datos = { txtv_Pallet.getText().toString() };
                taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_POSICION), FragmentoConsulta.TAG);
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
            SKU= b.getString("SKU");
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
            txtv_Pallet.setText("-");
            txtv_EmpaquesRegistrados.setText("-");
            edtx_Contenedor.setText("");
            edtx_CantidadPiezasoSKU.setText("");
            edtx_Posicion.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(RecepcionContenedor.this);
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

                    case "ConsultaContenedor":
                        dao = ca.cConsultaContenedor(edtx_Contenedor.getText().toString());
                        break;

                    case "ConsultarPosicion":
                        dao= ca.c_ConsultaPosicionPisoPalletOC(OrdenCompra, PartidaERP,params[0]);
                        break;


                    case "RegistrarEmpaqueNuevo":
                        CodigoEmpaque = edtx_Posicion.getText().toString();
                        Cantidad = edtx_CantidadPiezasoSKU.getText().toString();

                        if(Switch.isChecked()){
                            //REGISTRAR SKU

                            dao = ca.cOCContenedorSKU(
                                    OrdenCompra,
                                    txtv_Partida.getText().toString(),
                                    edtx_Contenedor.getText().toString(),
                                    "",
                                    stringSKU,txtv_Pallet.getText().toString());


                        }else{
                            //REGISTRAR CANTIDAD

                            dao = ca.cOCContenedor(
                                    OrdenCompra,
                                    txtv_Partida.getText().toString(),
                                    edtx_Contenedor.getText().toString(),
                                    "",
                                    edtx_CantidadPiezasoSKU.getText().toString(),txtv_Pallet.getText().toString());

                        }

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
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(RecepcionContenedor.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }

                            edtx_Posicion.requestFocus();


                            int SKUSel = -2;
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU);

                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,edtx_Contenedor,"Error interno." , false, true, true);
                                    new esconderTeclado(RecepcionContenedor.this);
                                    edtx_Contenedor.setText("");
                                    edtx_Contenedor.requestFocus();
                                    return;
                                case -1:
                                    new popUpGenerico(contexto,edtx_Contenedor,"No se encontro el SKU dentro del listado de partidas, verifique que sea correcto. [" + SKU +"]" , false, true, true);
                                    new esconderTeclado(RecepcionContenedor.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            SKU = "DEFAULT";

                            break;

                        case"DetallePartida":
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProducto"));
                            break;





                        case "ConsultaContenedor":
                            edtx_CantidadPiezasoSKU.requestFocus();

                            binding.txtvSKUCont.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("SKU"));
                            binding.txtvCantCont.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.txtvUMCont.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));

                            break;
                        case "RegistrarEmpaqueNuevo":
//                                        txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");

                            edtx_Posicion.setText("");
                            edtx_Posicion.requestFocus();
                            edtx_CantidadPiezasoSKU.setText("");



                            new esconderTeclado(RecepcionContenedor.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new  popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);


                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);
                                new SegundoPlano("Tabla").execute();

//                                                new SegundoPlano("RegistraPalletNuevo").execute();
                                break;
                            }


                            new SegundoPlano("ConsultaContenedor").execute();

                            edtx_CantidadPiezasoSKU.requestFocus();
                            break;
                        case "ConsultarPosicion":

                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));
                            new esconderTeclado(RecepcionContenedor.this);
                            edtx_Contenedor.requestFocus();
                            break;

                    }
                }
                else
                {

                    switch (tarea)
                    {
                        case "RegistrarEmpaqueNuevo":
                            edtx_CantidadPiezasoSKU.setText("");
                            edtx_CantidadPiezasoSKU.requestFocus();
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                            break;

                        case "ConsultaContenedor":
                            edtx_CantidadPiezasoSKU.setText("");

                            binding.txtvSKUCont.setText("");
                            binding.txtvCantCont.setText("");
                            binding.txtvUMCont.setText("");
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                            break;
                        default:
                            reiniciaVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
            p.DesactivarProgressBar(tarea);
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