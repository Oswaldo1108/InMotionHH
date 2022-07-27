package com.automatica.AXCPT.c_Inventarios.ValidacionMaterial;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.InventariosActivityConfirmarEmpaqueNeBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class Inventarios_ConfirmarEmpaqueNE extends AppCompatActivity  implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener
{
    public static final String EDITAR_EMPAQUE = "EditarEmpaque";
    public static final String REGISTRA_EMPAQUE_NORMAL = "RegistraEmpaqueNormal";
    public static final String LLENAR_TABLA = "llenarTabla";
    public static final String BAJA_PALLET = "bajaPallet";
    public static final String CONSULTA_PRODUCTO = "ConsultaProducto";
    private EditText edtx_Unidades, edtx_CantidadEmpaques;
    private EditText edtx_Producto;
    private TextView txtv_Pallet;
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

    frgmnt_taskbar_AXC taskbar_axc;
    InventariosActivityConfirmarEmpaqueNeBinding binding;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);

            binding = InventariosActivityConfirmarEmpaqueNeBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle("Validar");
            this.getSupportActionBar().setSubtitle("Por Empaque NE");


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

            txtv_Pallet.setText(Pallet);
            edtx_Producto.setEnabled(false);
            edtx_Unidades.setEnabled(false);
            txtv_Inventario.setText(IdInventario);
            txtv_Posicion.setText(UbicacionIntent);

            new SegundoPlano("llenarTabla").execute();
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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

    private void reiniciaVariables()
    {
        try
        {
            edtx_CantidadEmpaques.setText("");
            spnr_Prod.setAdapter(null);
            edtx_Unidades.setText("");
            edtx_Producto.setText("");
            chk_Editar.setChecked(false);
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    private void DeclararVariables()
    {
        try
        {

            edtx_Unidades = (EditText) findViewById(R.id.edtx_Unidades);
            edtx_CantidadEmpaques = (EditText) findViewById(R.id.edtx_CantidadEmpaques);
            edtx_Producto = (EditText) findViewById(R.id.edtx_Producto);
            edtx_Unidades.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CantidadEmpaques.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_Unidades.setEnabled(false);
            edtx_CantidadEmpaques.setEnabled(true);

            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);

            tabla = (SortableTableView) findViewById(R.id.tableView_OC);
            chk_Editar = (CheckBox) findViewById(R.id.chkb_ConfirmarEmpaque);

            txtv_Inventario = (TextView) findViewById(R.id.txtv_Inventario);
            txtv_Posicion = (TextView) findViewById(R.id.txtv_Posicion);

            progressBarHelper = new ProgressBarHelper(this);
            spnr_Prod = findViewById(R.id.vw_spinner_prod).findViewById(R.id.spinner);


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
            edtx_Unidades.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_Unidades.getText().toString().equals("")||Double.parseDouble(edtx_Unidades.getText().toString())>0)
                        {
                            edtx_CantidadEmpaques.setEnabled(true);
                            edtx_CantidadEmpaques.requestFocus();

                        } else {
                            Handler handler = new Handler();
                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_Unidades.setText("");
                                            edtx_Unidades.requestFocus();
                                        }
                                    }
                            );
                            new popUpGenerico(contexto, null, getString(R.string.error_cantidad_valida), false, true, true);
                        }
                        new esconderTeclado(Inventarios_ConfirmarEmpaqueNE.this);
                    }
                    return false;
                }
            });
            edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {

                            if (edtx_Producto.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, edtx_Producto, getString(R.string.error_ingrese_producto), false, true, true);
                                return false;
                            }

                            if (edtx_Unidades.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, edtx_Unidades, getString(R.string.error_ingrese_cantidad), false, true, true);
                                return false;
                            }

                            if(chk_Editar.isChecked())
                            {
                                new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                                        .setTitle("¿Agregar nuevo producto?").setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int id)
                                            {

                                                new SegundoPlano(EDITAR_EMPAQUE).execute();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                            }else
                            {
                                new SegundoPlano(REGISTRA_EMPAQUE_NORMAL).execute();
                            }

                            new esconderTeclado(Inventarios_ConfirmarEmpaqueNE.this);
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

                            new esconderTeclado(Inventarios_ConfirmarEmpaqueNE.this);
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
                        if(!edtx_Producto.getText().toString().equals(""))
                        {
                            new SegundoPlano(CONSULTA_PRODUCTO).execute();
                        }
                    }else
                    {
                        spnr_Prod.setAdapter(null);
                    }
                }
            });

            edtx_Unidades.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {

                    if(edtx_Producto.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,edtx_Producto,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                        return false;
                    }
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
        new CreaDialogos("¿Registrar baja? [" + clickedData[1] + "," + clickedData[6] + "]", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                new SegundoPlano(BAJA_PALLET).execute(clickedData[1],clickedData[6]);
                new esconderTeclado(Inventarios_ConfirmarEmpaqueNE.this);
            }
        },null,contexto);
    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {
        if(Seleccionado)
        {
            edtx_Producto.setText(clickedData[2]);
            edtx_Unidades.setText(clickedData[4]);

            edtx_CantidadEmpaques.requestFocus();
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
                progressBarHelper.ActivarProgressBar();
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
                        dao = ca.c_ConsultaEmpaquesPorPalletInventario_NE(IdInventario, txtv_Pallet.getText().toString());
                        break;
                    case REGISTRA_EMPAQUE_NORMAL:
                        dao = ca.c_RegistraEmpaqueInventario_NE(IdInventario,
                                txtv_Pallet.getText().toString(),
                                edtx_Unidades.getText().toString(),
                                edtx_CantidadEmpaques.getText().toString(),
                                edtx_Producto.getText().toString(),
                                "",
                                UbicacionIntent);
                        break;
                    case BAJA_PALLET:
                        dao = ca.c_BajaEmpaquesInventario_NE(IdInventario,txtv_Pallet.getText().toString(),params[0],params[1],"1");
                        break;
                    case EDITAR_EMPAQUE:
                        if(ConfigTabla.renglonEstaSelec()){
                            dao = ca.c_NuevoRegistroEmpaqueInventario_NE(IdInventario,
                                    txtv_Pallet.getText().toString(),
                                    edtx_Producto.getText().toString(),
                                    UbicacionIntent,
                                    edtx_Unidades.getText().toString(),
                                    edtx_CantidadEmpaques.getText().toString(),
                                    "");
                        }else{
                            dao = ca.c_NuevoRegistroEmpaqueInventario_NE(IdInventario,
                                    txtv_Pallet.getText().toString(),
                                    spnr_Prod.getSelectedItem().toString(),
                                    UbicacionIntent,
                                    edtx_Unidades.getText().toString(),
                                    edtx_CantidadEmpaques.getText().toString(),
                                    "");
                        }

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
                                ConfigTabla =  new TableViewDataConfigurator(0, TableViewDataConfigurator.ESTATUS_INVENTARIO_STD(),tabla, dao, Inventarios_ConfirmarEmpaqueNE.this);
                            }else
                            {
                                ConfigTabla.CargarDatosTabla(dao);
                            }
                            break;
                        case EDITAR_EMPAQUE:
                        case REGISTRA_EMPAQUE_NORMAL:
                            new SegundoPlano("llenarTabla").execute();
                            edtx_CantidadEmpaques.setText("");
                            edtx_Producto.setText("");
                            edtx_Unidades.setText("");
                            chk_Editar.setChecked(false);
                            break;
                        case BAJA_PALLET:
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                            new SegundoPlano("llenarTabla").execute();
                            reiniciaVariables();
                            break;
                    }

                }
                else
                {
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                    edtx_CantidadEmpaques.setText("");
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
            progressBarHelper.DesactivarProgressBar();
        }
    }

    @Override
    public void onBackPressed(){
        if(!taskbar_axc.toggle()){
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
