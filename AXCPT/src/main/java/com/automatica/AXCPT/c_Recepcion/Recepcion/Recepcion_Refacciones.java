package com.automatica.AXCPT.c_Recepcion.Recepcion;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Almacen_Armado_Pallets_Polvos;
import com.automatica.AXCPT.databinding.ActivityRecepcionDapBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionRefaccionesBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Recepcion_Refacciones extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, TableViewDataConfigurator.TableClickInterface
{
    private frgmnt_taskbar_AXC taskbar_axc;
    private popUpGenerico pop = new popUpGenerico(Recepcion_Refacciones.this);
    private ActivityRecepcionRefaccionesBinding binding;
    private EditText edtx_SKU,edtx_Piezas;
    private SortableTableView StvTabla;
    private TableViewDataConfigurator ConfigTabla_Totales = null;

    private ProgressBarHelper p;
    private TextView txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet;

    private Bundle b;
    private Context contexto = this;
    private String OrdenCompra,PartidaERP,NumParte,SKU,UM,CantidadTotal,CantidadRecibida;
    private TextView txtv_OrdenCompra;
    private Handler h = new Handler();
    private Spinner sp_Partidas;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityRecepcionRefaccionesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recepción");
        getSupportActionBar().setSubtitle("Multiples SKU");
        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();

        SacaExtrasIntent();
        declararVariables();
        AgregaListeners();

        new SegundoPlano("Tabla").execute();

        binding.swSKU.setChecked(true);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_REEMPAQUE);

        new SegundoPlano("ConsultaPallet").execute();
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
                new sobreDispositivo(contexto, null);
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

            edtx_Piezas = (EditText) findViewById(R.id.edtx_Piezas);

            edtx_SKU  = (EditText) findViewById(R.id.edtx_SKU);
            edtx_SKU.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            StvTabla = findViewById(R.id.tableView_OC);

            txtv_Partida = (TextView) findViewById(R.id.txtv_Partida);
            txtv_UM = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_CodigoAbierto);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);

            txtv_Partida.setText(PartidaERP);
            txtv_UM.setText(UM);
            txtv_Producto.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_OrdenCompra.setText(OrdenCompra);

        }catch(Exception e)
        {
            e.printStackTrace();

        }
    }


    private void AgregaListeners()
    {


        binding.swSKU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                edtx_SKU.setText("");
                edtx_Piezas.setText("");
                edtx_SKU.requestFocus();


                binding.txtvRegPiezas.setEnabled(b);
                edtx_Piezas.setEnabled(!b);

                if(!b)
                {
                    edtx_Piezas.setHint("Capturar piezas");
                }else
                {
                    edtx_Piezas.setHint("");
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
                        edtx_Piezas.setText("");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, edtx_SKU, e.getMessage(), "false", true, true);
                    }
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

                        edtx_SKU.setText(edtx_SKU.getText().toString().replace(" ","").replace("\t","").replace("\n",""));

                        int SKUSel = -2;
                        SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,edtx_SKU.getText().toString());

                        switch(SKUSel)
                        {
                            case -2:
                                new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                new esconderTeclado(Recepcion_Refacciones.this);

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
                                        new esconderTeclado(Recepcion_Refacciones.this);
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
                                        new esconderTeclado(Recepcion_Refacciones.this);
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


                        /*
                        REGISTRO DE PIEZAS
                         */

                        if(binding.swSKU.isChecked())
                        {
                                new SegundoPlano("RegistrarSKU").execute();
                        }else
                        {
                            h.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    h.post(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            edtx_Piezas.requestFocus();
                                            edtx_Piezas.setText("");
                                        }
                                    });
                                }
                            },100);

                        }
                        new esconderTeclado(Recepcion_Refacciones.this);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });





        edtx_Piezas.setOnKeyListener(new View.OnKeyListener()
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
                                new esconderTeclado(Recepcion_Refacciones.this);

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
                                        new esconderTeclado(Recepcion_Refacciones.this);
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
                                        new esconderTeclado(Recepcion_Refacciones.this);
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


                        /*
                        REGISTRO DE PIEZAS
                         */




                        if(binding.swSKU.isChecked())
                        {
                            new SegundoPlano("RegistrarSKU").execute();

                        }else
                        {
                            try
                            {
                                if (Float.parseFloat(edtx_Piezas.getText().toString()) > 999999)
                                {
                                    h.post(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            h.post(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    edtx_Piezas.requestFocus();
                                                    edtx_Piezas.setText("");
                                                }
                                            });
                                        }
                                    });
                                    new popUpGenerico(contexto, edtx_Piezas, getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                    return false;
                                }
                            }catch (NumberFormatException ex)
                            {
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {  edtx_Piezas.setText("");
                                        edtx_Piezas.requestFocus();

                                    }
                                });
                                new popUpGenerico(contexto,edtx_Piezas,getString(R.string.error_cantidad_valida),"false",true,true);
                            }

                            new SegundoPlano("RegistrarSKUPiezas").execute();


                        }

                        new esconderTeclado(Recepcion_Refacciones.this);

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

                new SegundoPlano("DetallePartida").execute();

                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        edtx_SKU.requestFocus();

                    }
                }, 250);
                new esconderTeclado(Recepcion_Refacciones.this);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

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

        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }
    private void reiniciaVariables()
    {
        try
        {

            binding.swSKU.setChecked(true);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {
        new CreaDialogos("Cancelar piezas del armado?", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {

                new popUpGenerico(contexto,edtx_SKU,"Opción deshabilitada temporalmente." , true, true, true);
                new esconderTeclado(Recepcion_Refacciones.this);
                return;

//                new SegundoPlano("CancelarPiezas").execute();
            }
        },null,contexto);
    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {

    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(Recepcion_Refacciones.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            p.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {
                switch (tarea)
                {
                    case"Tabla":
                        dao = ca.c_ListarPartidasOCEnProceso(OrdenCompra);
                        break;

                    case"DetallePartida":
                        dao = ca.c_detalleReciboPartida(OrdenCompra,txtv_Partida.getText().toString());
                        break;

                    case "ConsultaPallet":
                        dao= ca.c_ConsultaPalletArmadoOC(OrdenCompra);
                        break;


                    case "RegistrarSKU":

                        dao = ca.c_RegistrarRefaccionesSKU(
                                OrdenCompra,
                                txtv_Partida.getText().toString(),
                                "1",
                                edtx_SKU.getText().toString());
                        break;

                    case "RegistrarSKUPiezas":

                        dao = ca.c_RegistrarRefaccionesSKUPiezas(
                                OrdenCompra,
                                txtv_Partida.getText().toString(),
                                edtx_Piezas.getText().toString(),
                                edtx_SKU.getText().toString());
                        break;
                    case "RegistraPalletNuevo":

                        dao = ca.c_OCCierraPalletCompraRefacciones(txtv_Pallet.getText().toString());
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

                if(dao.iscEstado())
                {

                    switch (tarea)
                    {
                        case"Tabla":
                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(Recepcion_Refacciones.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }


                                h.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        edtx_SKU.requestFocus();

                                    }
                                },250);

                            int SKUSel = -2;
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU);

                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                    new esconderTeclado(Recepcion_Refacciones.this);
                                    edtx_SKU.setText("");
                                    edtx_SKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    new popUpGenerico(contexto,edtx_SKU,"No se encontro el SKU dentro del listado de partidas a recibir, verifique que sea correcto. [" + SKU +"]" , false, true, true);
                                    new esconderTeclado(Recepcion_Refacciones.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;

                                default:
                                    sp_Partidas.setSelection(SKUSel);

                            }

                            new esconderTeclado(Recepcion_Refacciones.this);

                            SKU = "DEFAULT";
                            break;

                        case"DetallePartida":
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProducto"));

                            h.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {

                                    edtx_SKU.requestFocus();

                                }
                            },250);
                            break;

                        case "RegistrarSKU":
                        case "RegistrarSKUPiezas":
                            txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            edtx_Piezas.setText("");
                            edtx_SKU.setText("");
                            edtx_SKU.requestFocus();

                            new esconderTeclado(Recepcion_Refacciones.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new  popUpGenerico(contexto, edtx_SKU, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, edtx_SKU, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new SegundoPlano("Tabla").execute();

                            }

                            new SegundoPlano("ConsultaPallet").execute(txtv_Pallet.getText().toString());


                            break;
                        case "ConsultaPallet":

                            if (dao.getcTablaUnica()!=null)
                            {
                                if(ConfigTabla_Totales == null)
                                {
                                    ConfigTabla_Totales = new TableViewDataConfigurator(StvTabla, dao, Recepcion_Refacciones.this);
                                }else
                                {
                                    ConfigTabla_Totales.CargarDatosTabla(dao);
                                }
                            }

                            binding.txtvCodigoAbierto.setText(dao.getcMensaje());
                            new esconderTeclado(Recepcion_Refacciones.this);
                            edtx_SKU.requestFocus();
                            break;

                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, null,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);

                            if (dao.getcTablaUnica()!=null)
                            {
                                if(ConfigTabla_Totales == null)
                                {
                                    ConfigTabla_Totales = new TableViewDataConfigurator(StvTabla,null, Recepcion_Refacciones.this);
                                }else
                                {
                                    ConfigTabla_Totales.CargarDatosTabla(null);
                                }
                            }

                            break;


                        case "CerrarRecepcion":

                            new popUpGenerico(contexto, null,dao.getcMensaje(),dao.iscEstado(), true, true);

                            break;
                    }
                }
                else
                {

                    switch (tarea)
                    {
                        case "RegistrarSKU":
                        case "RegistrarSKUPiezas":
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
                                edtx_SKU.setText("");
                                edtx_Piezas.setText("");
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

        try {
            new CreaDialogos("¿Cerrar empaque?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new SegundoPlano("RegistraPalletNuevo").execute();
                        }
                    }, null, contexto);
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }

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