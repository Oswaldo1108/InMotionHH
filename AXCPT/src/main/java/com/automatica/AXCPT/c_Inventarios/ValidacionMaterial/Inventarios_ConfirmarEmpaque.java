package com.automatica.AXCPT.c_Inventarios.ValidacionMaterial;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Inventarios.Granel.Inventarios_ConfirmarEmpaqueGranel;
import com.automatica.AXCPT.databinding.ActivityMenuBinding;
import com.automatica.AXCPT.databinding.InventariosActivityConfirmarEmpaqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class Inventarios_ConfirmarEmpaque extends AppCompatActivity  implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar,frgmnt_SKU_Conteo.OnFragmentInteractionListener {


    private DatePickerFragment newFragment;
    public static final String EDITAR_EMPAQUE = "EditarEmpaque";
    public static final String REGISTRAR_NUEVO_EMPAQUE = "RegistrarNuevoEmpaque";
    public static final String REGISTRA_EMPAQUE_NORMAL = "RegistraEmpaqueNormal";
    public static final String BAJA_PALLET = "bajaPallet";
    public static final String CONSULTA_EMPAQUE = "ConsultaEmpaque";
    public static final String LLENAR_TABLA = "llenarTabla";
    public static final String CONSULTA_PRODUCTO = "ConsultaProducto";


    private EditText edtx_Empaque, edtx_Unidades, edtx_ConfirmarEmpaque,edtxPedimento,edtxClavePedimento,edtxFactura,edtxFechaPedimento,edtxFechaRecepcion;
    private EditText edtx_Producto;
    private EditText txtv_Pallet, edtxLote;
    private CheckBox chk_Editar;
    private String Pallet;
    private String UbicacionIntent, IdInventario;
    private Bundle b;
    private SortableTableView tabla;
    private TableViewDataConfigurator ConfigTabla = null;
    private ProgressBarHelper progressBarHelper;
    private Spinner spnr_Prod;
    private TextView txtv_Inventario, txtv_Posicion;
    private Context contexto = this;
    private boolean EmpaqueNuevo;

    Handler handler = new Handler();
    frgmnt_taskbar_AXC taskbar_axc;
    InventariosActivityConfirmarEmpaqueBinding binding;


    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            binding = InventariosActivityConfirmarEmpaqueBinding.inflate(getLayoutInflater());
            View view  = binding.getRoot();
            setContentView(view);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle("Validar");
            this.getSupportActionBar().setSubtitle("Por Empaque");


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
            getSupportFragmentManager().beginTransaction().add(findViewById(R.id.Pantalla_principal).getId(),taskbar_axc,"FragmentoTaskBar").commit();

            DeclararVariables();
            SacaExtrasIntent();
            AgregaListeners();

            new SegundoPlano(LLENAR_TABLA).execute();

            txtv_Pallet.setText(Pallet);
            edtx_Producto.setEnabled(false);
            edtx_Unidades.setEnabled(false);


            txtv_Inventario.setText(IdInventario);
            txtv_Posicion.setText(UbicacionIntent);
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
            return true;
        } catch (Exception ex)
        {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
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
                new sobreDispositivo(contexto, null);
            }
            if ((id == R.id.borrar_datos))
            {

                reiniciaVariables();
            }
            if ((id == R.id.recargar))
            {

                new SegundoPlano(LLENAR_TABLA).execute();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    private void reiniciaVariables()
    {
        try
        {
            edtx_ConfirmarEmpaque.setText("");
            spnr_Prod.setAdapter(null);
            edtx_Empaque.setText("");
            edtx_Unidades.setText("");
            //edtx_Producto.setText("");
            chk_Editar.setChecked(false);
            EmpaqueNuevo = false;
            edtxClavePedimento.setText("");
            edtxFactura.setText("");
            edtxFechaPedimento.setText("");
            edtxPedimento.setText("");
            //edtxLote.setText("");
            edtx_Empaque.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    private void DeclararVariables()
    {
        try
        {

            edtx_Empaque = (EditText) findViewById(R.id.edtx_CodigoPallet);
            edtx_Unidades = (EditText) findViewById(R.id.edtx_Unidades);
            edtx_ConfirmarEmpaque = (EditText) findViewById(R.id.edtx_ConfirmarEmpaque);
            edtx_Producto = (EditText) findViewById(R.id.edtx_Producto);

            edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Unidades.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_ConfirmarEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_Unidades.setEnabled(false);
            edtx_ConfirmarEmpaque.setEnabled(true);

            txtv_Pallet = (EditText) findViewById(R.id.txtv_Pallet);

            tabla = (SortableTableView) findViewById(R.id.tableView_OC);
            chk_Editar = (CheckBox) findViewById(R.id.chkb_ConfirmarEmpaque);

            txtv_Inventario = (TextView) findViewById(R.id.txtv_Inventario);
            txtv_Posicion = (TextView) findViewById(R.id.txtv_Posicion);

            progressBarHelper = new ProgressBarHelper(this);
            spnr_Prod = findViewById(R.id.vw_spinner_prod).findViewById(R.id.spinner);

            edtxPedimento = findViewById(R.id.edtxPedimiento);
            edtxPedimento.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtxLote = findViewById(R.id.edtxLote);
            edtxLote.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtxClavePedimento = findViewById(R.id.edtxclavepedimiento);
            edtxFactura = findViewById(R.id.edtxFactura);
            edtxFechaPedimento = findViewById(R.id.edtxFechaPedimiento);
            edtxFechaRecepcion = findViewById(R.id.edtxFechaRecepcion);


            edtxLote.setEnabled(false);
            edtxFactura.setEnabled(false);
            edtxClavePedimento.setEnabled(false);
            edtxPedimento.setEnabled(false);
            edtxFechaPedimento.setEnabled(false);
            edtxFechaRecepcion.setEnabled(false);

        } catch (Exception e)
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
            UbicacionIntent = b.getString("UbicacionIntent");
            IdInventario = b.getString("IdInv");
            Pallet = b.getString("CodigoPallet");

        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    private void AgregaListeners()
    {
        try
        {
            edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        try {

                            if (!edtx_Empaque.getText().toString().equals(""))
                            {
                                new SegundoPlano(CONSULTA_EMPAQUE).execute();

                            } else {
                                Handler handler = new Handler();
                                handler.post(
                                        new Runnable() {
                                            public void run() {
                                                edtx_Empaque.setText("");
                                                edtx_Empaque.requestFocus();
                                            }
                                        }
                                );
                                new popUpGenerico(contexto, null, getString(R.string.error_ingrese_empaque), false, true, true);
                            }
                            new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

                        }
                    }
                    return false;
                }
            });
            edtx_Unidades.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_Unidades.getText().toString().equals("")||Double.parseDouble(edtx_Unidades.getText().toString())>0)
                        {
                            if(EmpaqueNuevo){

                            }else{
                                edtx_ConfirmarEmpaque.requestFocus();
                            }

                        } else {

                            new popUpGenerico(contexto, null, getString(R.string.ingrese_cantidad), false, true, true);
                        }
                        new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                    }
                    return false;
                }
            });

/*            edtx_Unidades.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_Producto.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();



                    return true;
                }
            });*/

            edtx_Unidades.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
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
            edtxPedimento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    return false;
                }
            });
            edtxClavePedimento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    return false;
                }
            });
            edtxFactura.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                    return false;
                }
            });
            edtxFechaPedimento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            final String selectedDate = year  + "/" + (month + 1) + "/" + day;
                            edtxFechaPedimento.setText(selectedDate);
                        }
                    });
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            });
            edtxFechaRecepcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            final String selectedDate = year  + "/" + (month + 1) + "/" + day;
                            edtxFechaRecepcion.setText(selectedDate);
                            edtx_ConfirmarEmpaque.requestFocus();
                        }
                    });
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            });
            edtx_ConfirmarEmpaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if (!edtx_Empaque.getText().toString().equals(edtx_ConfirmarEmpaque.getText().toString()))
                            {
                                new popUpGenerico(contexto, edtx_ConfirmarEmpaque, getString(R.string.empaques_no_coinciden), false, true, true);
                                return false;
                            }

                            if(chk_Editar.isChecked())
                            {
                                if(EmpaqueNuevo)
                                {
                                    new SegundoPlano(REGISTRAR_NUEVO_EMPAQUE).execute();
                                }else
                                {
                                    new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                                            .setTitle("¿Editar información del paquete?").setCancelable(false)
                                            .setPositiveButton("Si", new DialogInterface.OnClickListener()
                                            {
                                                public void onClick(DialogInterface dialog, int id)
                                                {
                                                    new SegundoPlano(EDITAR_EMPAQUE).execute();
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                }
                            }else
                            {
                                new SegundoPlano(REGISTRA_EMPAQUE_NORMAL).execute();
                            }

                            new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

                        }
                    }
                    return false;
                }
            });


            edtx_Producto.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        try {

                            if (edtx_Producto.getText().toString().equals(""))
                            {

                                new popUpGenerico(contexto, edtx_Producto, getString(R.string.error_ingrese_producto), false, true, true);
                                return false;
                            }

                            new SegundoPlano(CONSULTA_PRODUCTO).execute();

                            new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

                        }
                    }
                    return false;
                }
            });

            chk_Editar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    edtx_Unidades.setEnabled(isChecked);

                    if (isChecked)
                    {
                        if(EmpaqueNuevo){
                            chk_Editar.setText("Nuevo");
                            edtxFactura.setEnabled(true);
                            edtxClavePedimento.setEnabled(true);
                            edtxPedimento.setEnabled(true);
                            edtxFechaPedimento.setEnabled(true);
                            edtxFechaRecepcion.setEnabled(true);
                        }else {
                            chk_Editar.setText("Editar");
                            edtxFactura.setEnabled(false);
                            edtxClavePedimento.setEnabled(false);
                            edtxPedimento.setEnabled(false);
                            edtxFechaPedimento.setEnabled(false);
                            edtxFechaRecepcion.setEnabled(false);
                        }
                        if(!edtx_Empaque.getText().toString().equals(""))
                        {
                            if(!edtx_Producto.getText().toString().equals(""))
                            {
                                new SegundoPlano(CONSULTA_PRODUCTO).execute();
                            }
                        }

                    }else
                    {
                        spnr_Prod.setAdapter(null);
                        EmpaqueNuevo = false;
                        chk_Editar.setText("Editar");
                    }
                }
            });



            edtx_Unidades.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
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

        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }

    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex,final String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {
        new CreaDialogos("¿Registrar baja de paquete? [" + clickedData[1] + "]", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                new SegundoPlano(BAJA_PALLET).execute(clickedData[1]);
                new esconderTeclado(Inventarios_ConfirmarEmpaque.this);
            }
        },null,contexto);
    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {
        if(Seleccionado)
        {
            edtx_Empaque.setText(clickedData[1]);
            edtx_Producto.setText(clickedData[2]);
            edtx_Unidades.setText(clickedData[3]);
            edtxLote.setText(clickedData[4]);

            new SegundoPlano(CONSULTA_EMPAQUE).execute();

        }else
        {
            reiniciaVariables();
        }
//        chk_Editar.setChecked(Seleccionado);
    }

    @Override
    public void BotonDerecha() {

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
        edtx_Unidades.setText(strCantidadEscaneada);
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_ConfirmarEmpaque.requestFocus();
            }
        },100);
    }


    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {
        String tarea;
        DataAccessObject dao;
        adInventarios ca = new adInventarios(contexto);

        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            try
            {
                progressBarHelper.ActivarProgressBar(tarea);
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }

        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {
                switch (tarea)
                {
                    case CONSULTA_PRODUCTO:
                        dao = ca.c_ConsultaCoincidenciaArticulo(edtx_Producto.getText().toString());
                        break;
                    case LLENAR_TABLA:

                        dao = ca.c_ConsultaEmpaquesPorPalletInventario(IdInventario, txtv_Pallet.getText().toString());
                        break;

                    case CONSULTA_EMPAQUE:
                        dao = ca.c_ConsultaEmpaqueInventario(IdInventario, edtx_Empaque.getText().toString());
                        break;


                    case REGISTRA_EMPAQUE_NORMAL:
                        dao = ca.c_RegistraEmpaqueInventario(
                                IdInventario,
                                edtx_Empaque.getText().toString(),
                                txtv_Pallet.getText().toString(),
                                "",
                                UbicacionIntent);
                        break;

                    case EDITAR_EMPAQUE:
                        dao = ca.c_EditaRegistroEmpaqueInventario(IdInventario,
                                txtv_Pallet.getText().toString(),
                                edtx_Empaque.getText().toString(),
                                UbicacionIntent,
                                edtx_Unidades.getText().toString());
                        break;

                    case REGISTRAR_NUEVO_EMPAQUE:

                        dao = ca.c_RegistraNuevoEmpaquePalletInventario(
                                IdInventario,
                                txtv_Pallet.getText().toString(),
                                edtx_Empaque.getText().toString(),
                                edtx_Unidades.getText().toString(),
                                UbicacionIntent,
                                "",
                                "",
                                "1900-01-01",
                                "1900-01-01"
                        );
                        break;


                    case BAJA_PALLET:
                        dao = ca.c_BajaEmpaqueInventario(IdInventario,params[0]);
                        break;


                }
            } catch (Exception e)
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
                if (dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case CONSULTA_PRODUCTO:
                            spnr_Prod.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item,
                                    dao.getcTablasSorteadas("NumParte", "UnidadMedida")));

                            break;
                        case LLENAR_TABLA:
                            if(ConfigTabla == null)
                            {
                                ConfigTabla =  new TableViewDataConfigurator(0, TableViewDataConfigurator.ESTATUS_INVENTARIO_STD(),tabla, dao, Inventarios_ConfirmarEmpaque.this);
                            }else
                            {
                                ConfigTabla.CargarDatosTabla(dao);
                            }
                            edtx_Empaque.requestFocus();
                            break;

                        case CONSULTA_EMPAQUE:
                            edtx_Unidades.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Unidades"));
                            edtx_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumeroParte"));
                            edtx_ConfirmarEmpaque.requestFocus();


                            break;
                        case EDITAR_EMPAQUE:
                        case REGISTRAR_NUEVO_EMPAQUE:
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_alta), dao.iscEstado(), true, true);
                            edtxFactura.setEnabled(false);
                            edtxClavePedimento.setEnabled(false);
                            edtxPedimento.setEnabled(false);
                            edtxFechaPedimento.setEnabled(false);
                            edtxFechaRecepcion.setEnabled(false);
                            edtx_Empaque.setText("");
                            //edtx_Producto.setText("");
                            edtx_Unidades.setText("");
                            edtxFactura.setText("");
                            edtxClavePedimento.setText("");
                            edtxPedimento.setText("");
                            //edtxLote.setText("");
                            edtxFechaPedimento.setText("");
                            edtxFechaRecepcion.setText("");
                        case REGISTRA_EMPAQUE_NORMAL:
                            new SegundoPlano(LLENAR_TABLA).execute();
                            edtx_ConfirmarEmpaque.setText("");
                            edtx_Empaque.setText("");
                            //edtx_Producto.setText("");
                            edtx_Unidades.setText("");
                            edtxFactura.setText("");
                            edtxClavePedimento.setText("");
                            edtxPedimento.setText("");
                            //edtxLote.setText("");
                            edtxFechaPedimento.setText("");
                            edtxFechaRecepcion.setText("");
                            edtx_Empaque.requestFocus();

                            EmpaqueNuevo = false;
                            chk_Editar.setChecked(false);
                            break;

                        case BAJA_PALLET:
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                            new SegundoPlano(LLENAR_TABLA).execute();
                            reiniciaVariables();
                            break;
                    }

                }
                else
                {

                    if (tarea.equals(CONSULTA_EMPAQUE) && dao.getcMensaje().equals("0")) {
                        new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                                .setTitle("Empaque no encontrado, ¿Dar de alta?").setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        spnr_Prod.setAdapter(null);
                                        edtx_Unidades.setText("");
                                        edtx_Unidades.requestFocus();
                                        EmpaqueNuevo = true;
                                        chk_Editar.setChecked(true);
                                        chk_Editar.setText("Nuevo");
                                        edtxFactura.setEnabled(true);
                                        edtxClavePedimento.setEnabled(true);
                                        edtxPedimento.setEnabled(true);
                                        edtxFechaPedimento.setEnabled(true);
                                        edtxFechaRecepcion.setEnabled(true);


                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }  else if (tarea.equals(CONSULTA_EMPAQUE) && dao.getcMensaje().equals("1")) {
                    new popUpGenerico(contexto, getCurrentFocus(), "No se puede agregar contenedores a un pallet ", dao.iscEstado(), true, true);

                    } else if (tarea.equals(LLENAR_TABLA)) {
                        edtx_Empaque.requestFocus();
                    } else {
                        new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        //reiniciaVariables();
                        edtx_ConfirmarEmpaque.setText("");
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
                progressBarHelper.DesactivarProgressBar(tarea);

        }
    }

    @Override
    public void onBackPressed(){
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