package com.automatica.AXCPT.c_Inventarios.Granel;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Embarques.Reempaque.Embarques_Reempaque;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_PantallaPrincipal;
import com.automatica.AXCPT.c_Inventarios.ValidacionMaterial.Inventarios_ConfirmarEmpaque;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionPalletNe;
import com.automatica.AXCPT.databinding.ActivityInventariosConfirmarEmpaqueGranelBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import java.util.HashMap;

import de.codecrafters.tableview.SortableTableView;

public class Inventarios_ConfirmarEmpaqueGranel extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener{
    private DatePickerFragment newFragment;
    public static final String EDITAR_EMPAQUE = "EditarEmpaque";
    public static final String EDITAR_CONTENEDOR = "EditarContenedor";
    public static final String REGISTRA_EMPAQUE_NORMAL = "RegistraEmpaqueNormal";
    public static final String REGISTRAR_NUEVO_EMPAQUE = "RegistrarNuevoEmpaque";
    public static final String LLENAR_TABLA = "llenarTabla";
    public static final String BAJA_PALLET = "bajaPallet";
    public static final String CONSULTA_PRODUCTO = "ConsultaProducto";
    public static final String CONSULTA_EMPAQUE = "ConsultaEmpaque";
    public static final String CONSULTA_PEDIMENTO = "ConsultaPedimento";
    public static final String DETALLE_PEDIMENTO = "DetallePedimento";
    private EditText edtx_Posicion,edtx_Empaque, edtx_Unidades, edtx_ConfirmarEmpaque,edtxPedimento,edtxClavePedimento,edtxFactura,edtxFechaPedimento,edtxFechaRecepcion;
    private EditText edtx_Producto, edtx_Lote;
    private CheckBox chk_Editar;
    private ActivityHelpers activityHelpers;
    private String Pallet;
    private String UbicacionIntent, IdInventario;
    private Bundle b;
    private SortableTableView tabla;
    private TableViewDataConfigurator ConfigTabla = null;
    private ProgressBarHelper progressBarHelper;
    private Spinner spnr_Prod;
    private TextView txtv_Inventario, txtvConfirmar;
    private Context contexto = this;
    private boolean EmpaqueNuevo, Tipo;
    Handler handler = new Handler();

    ActivityInventariosConfirmarEmpaqueGranelBinding binding;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            binding = ActivityInventariosConfirmarEmpaqueGranelBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle("Inventario");
            this.getSupportActionBar().setSubtitle("Pos. Picking");
            activityHelpers = new ActivityHelpers();
            activityHelpers.AgregarMenu(Inventarios_ConfirmarEmpaqueGranel.this,R.id.Pantalla_principal);
            activityHelpers.AgregarTaskBar(Inventarios_ConfirmarEmpaqueGranel.this,R.id.Pantalla_principal,false);
            DeclararVariables();
            SacaExtrasIntent();
            AgregaListeners();

            edtx_Producto.setEnabled(false);
            edtx_Unidades.setEnabled(false);
            edtx_Lote.setEnabled(false);
            txtv_Inventario.setText(IdInventario);
            edtx_Posicion.setText(UbicacionIntent);

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

                new SegundoPlano("llenarTabla").execute();
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
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        new SegundoPlano("llenarTabla").execute();
    }

    private void reiniciaVariables()
    {
        try
        {
            edtx_ConfirmarEmpaque.setText("");
            spnr_Prod.setAdapter(null);
            edtx_Empaque.setText("");
            edtx_Unidades.setText("");
            edtx_Producto.setText("");
            edtx_Lote.setText("");
            chk_Editar.setChecked(false);
            EmpaqueNuevo = false;
            edtxClavePedimento.setText("");
            edtxFactura.setText("");
            edtxFechaPedimento.setText("");
            edtxFechaRecepcion.setText("");
            edtxPedimento.setText("");
            edtx_Empaque.requestFocus();
            EmpaqueNuevo = false;
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    private void DeclararVariables()
    {
        try
        {

            edtx_Posicion = (EditText) findViewById(R.id.edtx_Posicion);
            edtx_Posicion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_Lote = (EditText) findViewById(R.id.edtx_Lote);
            edtx_Lote.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            txtv_Inventario = (TextView) findViewById(R.id.txtv_Inventario);


            edtx_Empaque = (EditText) findViewById(R.id.edtx_CodigoPallet);
            edtx_Unidades = (EditText) findViewById(R.id.edtx_Unidades);
            edtx_ConfirmarEmpaque = (EditText) findViewById(R.id.edtx_ConfirmarEmpaque);
            edtx_Producto = (EditText) findViewById(R.id.edtx_Producto);

            edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Unidades.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_ConfirmarEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_Unidades.setEnabled(false);
            edtx_ConfirmarEmpaque.setEnabled(true);
            tabla = (SortableTableView) findViewById(R.id.tableView_OC);
            chk_Editar = (CheckBox) findViewById(R.id.chkb_ConfirmarEmpaque);


            progressBarHelper = new ProgressBarHelper(this);
            spnr_Prod = findViewById(R.id.vw_spinner_prod).findViewById(R.id.spinner);

            edtxPedimento = findViewById(R.id.edtxPedimiento);
            edtxPedimento.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtxClavePedimento = findViewById(R.id.edtxclavepedimiento);
            edtxFactura = findViewById(R.id.edtxFactura);
            edtxFechaPedimento = findViewById(R.id.edtxFechaPedimiento);
            edtxFechaRecepcion = findViewById(R.id.edtxFechaRecepcion);


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
                                new SegundoPlano("ConsultaEmpaque").execute();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_ConfirmarEmpaque.requestFocus();
                                    }
                                },100);

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
                            new esconderTeclado(Inventarios_ConfirmarEmpaqueGranel.this);
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
                        if (!edtx_Unidades.getText().toString().equals(""))
                        {
                            if(EmpaqueNuevo){

                            }else{
                                edtx_ConfirmarEmpaque.requestFocus();
                            }

                        } else {

                            new popUpGenerico(contexto, null, getString(R.string.ingrese_cantidad), false, true, true);
                        }
                        new esconderTeclado(Inventarios_ConfirmarEmpaqueGranel.this);
                    }
                    return false;
                }
            });

            edtx_Unidades.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_Producto.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();



                    return true;
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
                    new esconderTeclado(Inventarios_ConfirmarEmpaqueGranel.this);
                    edtxFechaPedimento.callOnClick();
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
                            edtxFechaRecepcion.callOnClick();
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
                            if (Double.parseDouble(edtx_Unidades.getText().toString())<1){
                                new popUpGenerico(contexto, edtx_ConfirmarEmpaque, getString(R.string.error_cantidad_valida), false, true, true);
                                return false;
                            }
                            if (edtx_Empaque.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, edtx_ConfirmarEmpaque, getString(R.string.error_ingrese_empaque), false, true, true);
                                return false;
                            }

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
                                                    if(Tipo){

                                                        new SegundoPlano(EDITAR_CONTENEDOR).execute();

                                                    }else{

                                                        new SegundoPlano(EDITAR_EMPAQUE).execute();
                                                    }

                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                }
                            }else
                            {
                                new SegundoPlano(REGISTRA_EMPAQUE_NORMAL).execute();
                            }

                            new esconderTeclado(Inventarios_ConfirmarEmpaqueGranel.this);
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

                            new esconderTeclado(Inventarios_ConfirmarEmpaqueGranel.this);
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
                    edtx_Producto.setEnabled(isChecked);

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

            edtx_Posicion.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        try {

                            if (edtx_Posicion.getText().toString().equals(""))
                            {

                                new popUpGenerico(contexto, edtx_Posicion, getString(R.string.error_ingrese_ubicacion), false, true, true);
                                return false;
                            }

                            new SegundoPlano(LLENAR_TABLA).execute();

                            new esconderTeclado(Inventarios_ConfirmarEmpaqueGranel.this);
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

                        }
                    }
                    return false;
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
        new CreaDialogos("¿Registrar baja? [" + clickedData[1] + "," + clickedData[3] + "]", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                new SegundoPlano(BAJA_PALLET).execute(clickedData[1]);
                new esconderTeclado(Inventarios_ConfirmarEmpaqueGranel.this);
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
            edtx_Lote.setText(clickedData[4]);
            if(clickedData[7].equals("C")){
                Tipo = true;
            }else{
                Tipo = false;
            }

            new SegundoPlano(DETALLE_PEDIMENTO).execute();
        }else
        {

            reiniciaVariables();
        }
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }
    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada) {
        edtx_Unidades.setText(strCantidadEscaneada);
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

                        dao = ca.c_ConsultaEmpaquesPosicionPickingInv(IdInventario, edtx_Posicion.getText().toString());
                        break;

                    case CONSULTA_EMPAQUE:
                        dao = ca.c_ConsultaEmpaqueInventario(IdInventario, edtx_Empaque.getText().toString());
                        break;


                    case REGISTRA_EMPAQUE_NORMAL:
                        dao = ca.c_RegistraEmpaqueInventarioPK(IdInventario,
                                edtx_Empaque.getText().toString(),
                                edtx_Posicion.getText().toString(),
                                "",
                                UbicacionIntent);
                        break;

                    case EDITAR_EMPAQUE:
                        dao = ca.c_EditaRegistroEmpaqueInventarioPK(
                                IdInventario,
                                edtx_Posicion.getText().toString(),
                                edtx_Empaque.getText().toString(),
                                UbicacionIntent,
                                edtx_Unidades.getText().toString());
                        break;


                    case REGISTRAR_NUEVO_EMPAQUE:
                        dao = ca.c_RegistraNuevoEmpaquePalletInventarioPicking(
                                IdInventario,
                                edtx_Posicion.getText().toString(),
                                edtx_Empaque.getText().toString(),
                                edtx_Unidades.getText().toString(),
                                UbicacionIntent,
                                edtx_Lote.getText().toString(),
                                "",
                                "",
                                "1900-01-01",
                                "1900-01-01",
                                spnr_Prod.getSelectedItem().toString());
                        break;


                    case BAJA_PALLET:
                        dao = ca.c_BajaEmpaqueInventario(IdInventario,params[0]);
                        break;

                    case EDITAR_CONTENEDOR:
                        dao = ca.c_EditaRegistroContenedorInventario(IdInventario,edtx_Posicion.getText().toString(),edtx_Empaque.getText().toString(),UbicacionIntent,edtx_Unidades.getText().toString(),spnr_Prod.getSelectedItem().toString(),edtxPedimento.getText().toString());
                        break;

                   /*  case CONSULTA_PEDIMENTO:
                        dao = ca.c_ConsultarPedimentos(edtx_Empaque.getText().toString());
                        break;
                        */

                    case DETALLE_PEDIMENTO:
                        dao = ca.c_DetallePedimento(edtx_Empaque.getText().toString(),edtxPedimento.getText().toString());
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
                            if(dao.getcTablas() == null  ){
                                new popUpGenerico(contexto, getCurrentFocus(), "Posición vacía", false, true, true);
                                Log.i("PRUEBA",edtx_Posicion.getText().toString());
                                break;
                            }else{
                                if(ConfigTabla == null)
                                {
                                    ConfigTabla =  new TableViewDataConfigurator(0, TableViewDataConfigurator.ESTATUS_INVENTARIO_STD(),tabla, dao, Inventarios_ConfirmarEmpaqueGranel.this);

                                }else
                                {
                                    ConfigTabla.CargarDatosTabla(dao);
                                }
                                edtx_Empaque.requestFocus();
                            }

                            break;

                        case CONSULTA_EMPAQUE:
                            edtx_Unidades.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Unidades"));
                            edtx_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumeroParte"));

                            edtxPedimento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Pedimento"));
                            edtxClavePedimento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("ClavePedimento"));
                            edtxFactura.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Factura"));
                            edtxFechaPedimento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("FechaPedimento"));
                            edtxFechaRecepcion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("FechaRecepcion"));


                            break;
                        case EDITAR_CONTENEDOR:
                        case EDITAR_EMPAQUE:
                        case REGISTRAR_NUEVO_EMPAQUE:
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_alta), dao.iscEstado(), true, true);
                            edtxFactura.setEnabled(false);
                            edtxClavePedimento.setEnabled(false);
                            edtxPedimento.setEnabled(false);
                            edtxFechaPedimento.setEnabled(false);
                            edtxFechaRecepcion.setEnabled(false);
                            reiniciaVariables();
                            new  SegundoPlano("llenarTabla").execute();
                            break;
                        case REGISTRA_EMPAQUE_NORMAL:
                            new  SegundoPlano("llenarTabla").execute();
                            edtx_ConfirmarEmpaque.setText("");
                            edtx_Empaque.setText("");
                            edtx_Producto.setText("");
                            edtx_Unidades.setText("");
                            edtx_Empaque.requestFocus();

                            EmpaqueNuevo = false;
                            chk_Editar.setChecked(false);
                            break;

                        case BAJA_PALLET:
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                            new SegundoPlano("llenarTabla").execute();
                            reiniciaVariables();
                            break;



                        case DETALLE_PEDIMENTO:
                            if(dao.getcTablas() == null){
                                edtxClavePedimento.setText("SIN CLAVE");
                                edtxFactura.setText("SIN FACTURA");
                                edtxFechaPedimento.setText("SIN FECHA");
                                edtxFechaRecepcion.setText("SIN FECHA");

                            }else{
                                edtxClavePedimento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("ClavePedimento"));
                                edtxFactura.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Factura"));
                                edtxFechaPedimento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("FechaPedimento"));
                                edtxFechaRecepcion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("FechaRecepcion"));
                            }
                            edtx_ConfirmarEmpaque.requestFocus();

                            break;
                        default:
                            break;
                    }

                }
                else
                {


                    switch (tarea)
                    {
                        case LLENAR_TABLA:
                            if(ConfigTabla != null)
                            {
                                ConfigTabla.CargarDatosTabla(null);
                            }
                            new popUpGenerico(contexto, edtx_Posicion, dao.getcMensaje(), dao.iscEstado(), true, true);

                            break;

                        case CONSULTA_EMPAQUE:
                            if(dao.getcMensaje().equals("0"))
                            {
                                new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                                        .setTitle("Empaque no encontrado, ¿Dar de alta?").setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                edtx_Producto.setText("");
                                                spnr_Prod.setAdapter(null);
                                                edtx_Unidades.setText("");
                                                edtx_Unidades.setText("");
                                                chk_Editar.setChecked(true);
                                                chk_Editar.setText("Nuevo");
                                                edtxFactura.setEnabled(true);
                                                edtxClavePedimento.setEnabled(true);
                                                edtxPedimento.setEnabled(true);
                                                edtxFechaPedimento.setEnabled(true);
                                                edtxFechaRecepcion.setEnabled(true);
                                                edtxFactura.setText("");
                                                edtxPedimento.setText("");
                                                edtxClavePedimento.setText("");
                                                edtxFechaPedimento.setText("");
                                                edtxFechaRecepcion.setText("");
                                                edtx_Producto.requestFocus();
                                                EmpaqueNuevo = true;
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                break;
                            }

                            if (dao.getcMensaje().equals("1")){
                                new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                                        .setTitle("Contenedor Vacio ¿Llenar?").setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                edtx_Producto.setText("");
                                                spnr_Prod.setAdapter(null);
                                                edtx_Unidades.setText("");
                                                edtx_Unidades.setText("");
                                                chk_Editar.setChecked(true);
                                                chk_Editar.setText("Nuevo");
                                                edtxFactura.setEnabled(true);
                                                edtxClavePedimento.setEnabled(true);
                                                edtxPedimento.setEnabled(true);
                                                edtxFechaPedimento.setEnabled(true);
                                                edtxFechaRecepcion.setEnabled(true);
                                                edtxFactura.setText("");
                                                edtxPedimento.setText("");
                                                edtxClavePedimento.setText("");
                                                edtxFechaPedimento.setText("");
                                                edtxFechaRecepcion.setText("");
                                                edtx_Producto.requestFocus();
                                                EmpaqueNuevo = true;
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                break;
                            }
                        default:
                            new popUpGenerico(contexto, edtx_ConfirmarEmpaque, dao.getcMensaje(), dao.iscEstado(), true, true);
                            break;
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


}