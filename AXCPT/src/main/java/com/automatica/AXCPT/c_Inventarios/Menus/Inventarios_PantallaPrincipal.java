package com.automatica.AXCPT.c_Inventarios.Menus;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Inventarios.Granel.Inventarios_ConfirmarEmpaqueGranel;
import com.automatica.AXCPT.c_Inventarios.CreacionMaterial.Inventarios_Menu_TipoRegNuevo;
import com.automatica.AXCPT.c_Inventarios.ValidacionMaterial.Inventarios_ValidacionPallet;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;

import java.util.HashMap;

import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Inventarios_PantallaPrincipal extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar
{
    //region variables
    private Toolbar toolbar;
    private EditText edtx_CodigoUbicacion;
    private SortableTableView tabla;
    private Button btn_Nuevo, btn_Baja, btn_Pallet;
    private TextView txtv_PalletsRegistrados;
    private Context contexto = this;
    private Bundle b;
    private String UbicacionIntent, IdInventario;
    private String Actividad;
    private TableViewDataConfigurator ConfigTabla = null;
    private ProgressBarHelper progressBarHelper;
    private  Boolean seleccion = false;
    frgmnt_taskbar_AXC taskbar_axc;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarios_activity_inventiario_ciclico_por_posicion_det);
        new cambiaColorStatusBar(contexto, R.color.RojoStd, Inventarios_PantallaPrincipal.this);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();
        edtx_CodigoUbicacion.setText(UbicacionIntent);

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
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();


    }

    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            UbicacionIntent = b.getString("UbicacionIntent");
            IdInventario = b.getString("IdInv");
            Log.i("PRUEBA", IdInventario +" "+ UbicacionIntent);
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.REGISTRO);
    }
    private void declararVariables()
    {
        try
        {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle("Inventario");

            edtx_CodigoUbicacion = (EditText) findViewById(R.id.edtx_Cantidad);
            edtx_CodigoUbicacion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            tabla = (SortableTableView) findViewById(R.id.tableView_OC);
            txtv_PalletsRegistrados = (TextView) findViewById(R.id.txtv_PalletsRegistrados);
            progressBarHelper = new ProgressBarHelper(this);
            btn_Baja = findViewById(R.id.btn_Baja);
            btn_Baja.setEnabled(false);
            btn_Nuevo = findViewById(R.id.btn_Nuevo);
            btn_Pallet = findViewById(R.id.btn_Pallet);
            btn_Pallet.setEnabled(true);

        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    private void agregaListeners()
    {
        edtx_CodigoUbicacion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if (edtx_CodigoUbicacion.getText().toString().equals(""))
                    {

                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_ubicacion), "false", true, true);
                        edtx_CodigoUbicacion.requestFocus();
                        edtx_CodigoUbicacion.setText("");
                        return false;
                    }
                    UbicacionIntent = edtx_CodigoUbicacion.getText().toString();
                    new SegundoPlano("llenarTabla").execute();
                    btn_Nuevo.setEnabled(true);
                    new esconderTeclado(Inventarios_PantallaPrincipal.this);
                }
                return false;
            }
        });
        btn_Nuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try
                {
                    if (edtx_CodigoUbicacion.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_ubicacion), "false", true, true);
                        edtx_CodigoUbicacion.requestFocus();
                        edtx_CodigoUbicacion.setText("");
                        return;
                    }
                    Intent intent = new Intent(Inventarios_PantallaPrincipal.this, Inventarios_Menu_TipoRegNuevo.class);
                    b.putString("UbicacionIntent", edtx_CodigoUbicacion.getText().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                } catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }

            }
        });

        btn_Baja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ConfigTabla.renglonEstaSelec()) {
                    new CreaDialogos("¿Dar de baja Pallet?", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id) {
                            new SegundoPlano("BajaPallet").execute();
                            new esconderTeclado(Inventarios_PantallaPrincipal.this);
                        }
                    },null,contexto);

                }else{
                    new popUpGenerico(contexto, getCurrentFocus(),"Seleccione un Pallet para dar de baja", "false", true, true);

                }


            }
        });
        btn_Pallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try
                {

                    Intent intent = new Intent(Inventarios_PantallaPrincipal.this, Inventarios_ValidacionPallet.class);
                    if (ConfigTabla.renglonEstaSelec())
                    {
                        intent.putExtra("CodigoPallet", Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(), "Código pallet").getDato());
                    }
                    b.putString("UbicacionIntent", edtx_CodigoUbicacion.getText().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        } catch (Exception ex)
        {
            Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        try
        {
            if(!edtx_CodigoUbicacion.getText().toString().equals(""))
            {
                new SegundoPlano("llenarTabla").execute();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    @Override
    protected void onPostResume() {

        super.onPostResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if ((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto, null);

            if(ConfigTabla.renglonEstaSelec())
            {
                Toast.makeText(Inventarios_PantallaPrincipal.this,Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(),"Producto").getDato(), Toast.LENGTH_SHORT).show();
            }

        }
        if ((id == R.id.recargar))
        {
            if (!edtx_CodigoUbicacion.getText().toString().equals(""))
            {
                tabla.getDataAdapter().clear();
                new SegundoPlano("llenarTabla").execute();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {

    }

    @Override
    public void BotonDerecha() {

        if(TextUtils.isEmpty(edtx_CodigoUbicacion.getText().toString()) ){
            new popUpGenerico(contexto,getCurrentFocus(),"Ingrese una posición ",false,true,true);
        }else{

            new PopUpMenuAXC().newInstance(taskbar_axc.getView().findViewById(R.id.BotonDer), Inventarios_PantallaPrincipal.this, R.menu.menu_inventarios_fisicototal, new PopUpMenuAXC.ContextMenuListener() {
                @Override
                public void listenerItem(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.baja_inventarios:
                            if(seleccion){
                                btn_Baja.callOnClick();
                            }
                            break;
                        case R.id.alta_inventarios:
                            btn_Nuevo.callOnClick();
                            break;
                        case R.id.validar_inventarios:
                            if(seleccion){
                                btn_Pallet.callOnClick();
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

        }


    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();

    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
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
            progressBarHelper.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case "llenarTabla":
                        dao = ca.c_INVConsultaUbicacion(IdInventario, UbicacionIntent);
                        break;
                    case "BajaPallet":
                        dao = ca.c_BajaPalletInventario(IdInventario, Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(),"Código pallet").getDato());
                        break;
                }
            } catch (Exception e)
            {
                dao = new DataAccessObject(e);
                e.printStackTrace();
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
                        case "llenarTabla":


                            seleccion = true;
                            if(ConfigTabla == null)
                            {
                                HashMap<String,Integer> ColoresStatus = new HashMap<>();
                                ColoresStatus.put("SIN REGISTRO",R.color.AmarilloRenglon);
                                ColoresStatus.put("1.- AJUSTE(+)",R.color.VerdeRenglon);
                                ColoresStatus.put("2.-AJUSTE(+)",R.color.VerdeRenglon);
                                ColoresStatus.put("1.-AJUSTE(-)",R.color.VerdeRenglon);
                                ColoresStatus.put("2.-AJUSTE(-)",R.color.VerdeRenglon);
                                ColoresStatus.put("LECTURA NORMAL",R.color.VerdeRenglon);
                                ColoresStatus.put("EDITADO",R.color.VerdeRenglon);
                                ColoresStatus.put("1.-BAJA",R.color.RojoRenglon);
                                ColoresStatus.put("2.-BAJA",R.color.RojoRenglon);
                                ColoresStatus.put("DESCARTADO",R.color.RojoRenglon);


                                ConfigTabla =  new TableViewDataConfigurator(0, ColoresStatus, tabla, dao, Inventarios_PantallaPrincipal.this);

                                HashMap<Integer,Integer> sizes = new HashMap<>();
                                sizes.put(1,0);

                                ConfigTabla.customRowsLength(sizes);
                            }else
                            {
                                ConfigTabla.CargarDatosTabla(dao);
                            }



                            txtv_PalletsRegistrados.setText(dao.getcMensaje());
                            break;

                        case "BajaPallet":
                            new popUpGenerico(contexto, null, "Pallet dado de baja con éxito.", true, true, true);
                            new SegundoPlano("llenarTabla").execute();
                            break;
                    }
                } else
                {
                    ConfigTabla.CargarDatosTabla(null);
                    new popUpGenerico(contexto, null, dao.getcMensaje(), false, true, true);

                }
            } catch (Exception e)
            {
                e.printStackTrace();
                //new popUpGenerico(contexto, null,e.getMessage(), false, true, true);

            }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
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
}

