package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;
import com.automatica.AXCPT.c_Almacen.Devolucion.DevolucionEmpaqueUnico;
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionOrdenDevolucion;
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionPartidaDevolucion;
import com.automatica.AXCPT.c_Recepcion.Recepcion.Recepcion_Refacciones;
import com.automatica.AXCPT.databinding.ActivityAlmacenAjustesMultipleSkuBinding;
import com.automatica.AXCPT.databinding.ActivityDevolucionEmpaqueUnicoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class Almacen_Ajustes_MultipleSKU extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_Seleccion_Producto.OnFragmentInteractionListener  {

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    View vista;
    Context contexto = this;
    popUpGenerico pop = new popUpGenerico(Almacen_Ajustes_MultipleSKU.this);
    ActivityAlmacenAjustesMultipleSkuBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    String documento, partida, numParte, cantTotal, cantRec, um, cantEmp, empxpallet;
    Handler h = new Handler();
    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenAjustesMultipleSkuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_REEMPAQUE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        } catch (Exception e) {
            Toast.makeText(contexto, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (p.ispBarActiva()) {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar) {
                // Botón de recargar
                new Almacen_Ajustes_MultipleSKU.SegundoPlano("LlenarTabla").execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // ************************************************ MÉTODOS IMPLEMENTADOS **********************************************

    @Override
    public void BotonDerecha() {validacionFinal(); }

    @Override
    public void BotonIzquierda() {onBackPressed();}

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

    }

    // ******************************************************** MÉTODOS CREADOS *********************************************************
    private void declararVariables() {
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ajuste contenedor");
        getSupportActionBar().setSubtitle("Múltiples SKU");
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
    }

    private void configurarTaskbar() {
        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("", "");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal, taskbar_axc, "FragmentoTaskBar").commit();
    }

    private void agregarListener(){

        binding.swSKU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                binding.edtxSKU.setText("");
                binding.edtxCantidad.setText("");
                binding.edtxSKU.requestFocus();


                binding.tvCant.setEnabled(b);
                binding.edtxCantidad.setEnabled(!b);

                if(!b)
                {
                    binding.edtxCantidad.setHint("Capturar piezas");
                }else
                {
                    binding.edtxCantidad.setHint("");
                }
            }
        });

        binding.edtxSKU.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v) {
                final Vibrator vibratorService = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
                //vibratorService.vibrate(150);

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.Pantalla_principal, frgmnt_Seleccion_Producto.newInstance(null,""), "ElegirPallet").addToBackStack("ElegirPallet")
                        .commit();
                return true;
            }
        });

        binding.edtxSKU.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
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


        binding.edtxSKU.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(binding.edtxSKU.getText().toString().equals(""))
                        {

                            binding.edtxSKU.setText("");
                            binding.edtxSKU.requestFocus();

                            new popUpGenerico(contexto,binding.edtxSKU,"Ingrese un SKU." , false, true, true);
                            return false;
                        }


                        /*
                        REGISTRO DE PIEZAS
                         */

                        if(binding.swSKU.isChecked())
                        {
                            new Almacen_Ajustes_MultipleSKU.SegundoPlano("RegistrarSKU").execute();
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
                                            binding.edtxCantidad.requestFocus();
                                            binding.edtxCantidad.setText("");
                                        }
                                    });
                                }
                            },100);

                        }
                        new esconderTeclado(Almacen_Ajustes_MultipleSKU.this);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });

        binding.edtxCantidad.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(binding.edtxSKU.getText().toString().equals(""))
                        {

                            binding.edtxSKU.setText("");
                            binding.edtxSKU.requestFocus();

                            new popUpGenerico(contexto,binding.edtxSKU,"Ingrese un SKU." , false, true, true);
                            return false;
                        }

                        binding.edtxSKU.setText(binding.edtxSKU.getText().toString().replace(" ","").replace("\t","").replace("\n",""));

                        /*
                        REGISTRO DE PIEZAS
                         */




                        if(binding.swSKU.isChecked())
                        {
                            new Almacen_Ajustes_MultipleSKU.SegundoPlano("RegistrarSKU").execute();

                        }else
                        {
                            try
                            {
                                if (Float.parseFloat(binding.edtxCantidad.getText().toString()) > 999999)
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
                                                    binding.edtxCantidad.requestFocus();
                                                    binding.edtxCantidad.setText("");
                                                }
                                            });
                                        }
                                    });
                                    new popUpGenerico(contexto, binding.edtxCantidad, getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                    return false;
                                }
                            }catch (NumberFormatException ex)
                            {
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {  binding.edtxCantidad.setText("");
                                        binding.edtxCantidad.requestFocus();

                                    }
                                });
                                new popUpGenerico(contexto,binding.edtxCantidad,getString(R.string.error_cantidad_valida),"false",true,true);
                            }

                            new Almacen_Ajustes_MultipleSKU.SegundoPlano("RegistrarSKUPiezas").execute();


                        }

                        new esconderTeclado(Almacen_Ajustes_MultipleSKU.this);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });



    }

    private void validacionFinal(){
        try {
            new CreaDialogos("¿Cerrar empaque?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Almacen_Ajustes_MultipleSKU.SegundoPlano("CerrarReempaque").execute();
                        }
                    }, null, contexto);
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }

    }


    // ********************************************************* OnBackPressed *********************************************************
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
        Intent intent = new Intent(Almacen_Ajustes_MultipleSKU.this, Almacen_Ajustes_Menu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {
        try
        {
            Handler h = new Handler();
            binding.edtxSKU.setText(prmProducto);

            if(binding.edtxSKU.getText().toString().equals(""))
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        binding.edtxSKU.requestFocus();

                    }
                },100);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // ********************************************************* SEGUNDO PLANO **********************************************************

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Ajustes_MultipleSKU.this);

        private SegundoPlano(String Tarea) {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute() {
            p.ActivarProgressBar(Tarea);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                switch (Tarea) {

                    case "LlenarTabla":
                        dao= ca.c_ConsultaPalletArmadoAjuste();
                        break;

                    case "RegistrarSKU":
                        dao = ca.c_RegistrarAjusteRefaccionesSKU("1",binding.edtxSKU.getText().toString());
                        break;

                    case "RegistrarSKUPiezas":
                        dao = ca.c_RegistrarAjusteRefaccionesSKUPiezas(binding.edtxCantidad.getText().toString(), binding.edtxSKU.getText().toString());
                        break;

                    case "CerrarReempaque":
                        dao = ca.c_OCCierraPalletAjusteRefacciones(binding.tvPallet.getText().toString());
                        break;
                }
            } catch (Exception e) {
                dao = new DataAccessObject(e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (dao.iscEstado()) {
                    switch (Tarea) {
                        case "LlenarTabla":
                            if (ConfigTabla_Totales == null) {

                                ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao,Almacen_Ajustes_MultipleSKU.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }
                            binding.tvPallet.setText(dao.getcMensaje());
                            break;

                        case "RegistrarSKUPiezas":
                            binding.tvPallet.setText(dao.getcMensaje());
                            new Almacen_Ajustes_MultipleSKU.SegundoPlano("LlenarTabla").execute();
                            binding.edtxSKU.setText("");
                            binding.edtxCantidad.setText("");
                            binding.edtxSKU.requestFocus();
                            break;

                        case "CerrarReempaque":
                            binding.tvPallet.setText("");
                            tabla.getDataAdapter().clear();
                            break;


                    }
                } else {
                    pop.popUpGenericoDefault(vista, dao.getcMensaje(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                pop.popUpGenericoDefault(vista, e.getMessage(), false);
            }
            p.DesactivarProgressBar(Tarea);
        }
    }


}