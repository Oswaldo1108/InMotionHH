package com.automatica.AXCPT.c_Produccion.Surtido;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionPartidaDevolucion;
import com.automatica.AXCPT.c_Embarques.Reempaque.Reempaque_Ciesa.Reempaque_Reempaque;
import com.automatica.AXCPT.c_Recepcion.Recepcion.Recepcion_Refacciones;
import com.automatica.AXCPT.databinding.ActivityReempaqueProdBinding;
import com.automatica.AXCPT.databinding.ActivitySurtidoProdEmpaqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class ReempaqueProd extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar {

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(ReempaqueProd.this);
    ActivityReempaqueProdBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    private Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReempaqueProdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sacarDatosIntent();
        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();
    }

    @Override
    protected void onResume() {
        new ReempaqueProd.SegundoPlano("ConsultarPalletAbierto").execute();

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
    private void sacarDatosIntent() {
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void declararVariables() {
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        binding.swSKU.setChecked(true);
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Orden de Surtido");
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

    private void validacionFinal(){
        intent = new Intent(contexto, SeleccionPartidaDevolucion.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);

    }

    private void agregarListener(){

        binding.swSKU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                binding.edtxSKU.setText("");
                binding.edtxPiezas.setText("");
                binding.edtxSKU.requestFocus();

                binding.edtxPiezas.setEnabled(!b);

                if(!b)
                {
                    binding.edtxPiezas.setHint("Capturar piezas");
                }else
                {
                    binding.edtxPiezas.setHint("");
                }
            }
        });

        binding.edtxSKU.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
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
                            new ReempaqueProd.SegundoPlano("RegistrarSKU").execute();
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
                                            binding.edtxPiezas.setText("");
                                        }
                                    });
                                }
                            },100);

                        }
                        new esconderTeclado(ReempaqueProd.this);

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
        Intent intent = new Intent(ReempaqueProd.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    // ********************************************************* SEGUNDO PLANO *********************************************************
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_RegistroPT ca = new cAccesoADatos_RegistroPT(ReempaqueProd.this);

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
                    case "ConsultarPalletAbierto":
                        dao = ca.cCreaPalletReempaqueProd();
                        break;

                    case "RegistrarSKU":
                        dao = ca.cRegistraReempaqueConsProdSKU(
                                binding.edtxPallet.getText().toString(),
                                binding.tvReempaque.getText().toString(),
                                binding.edtxSKU.getText().toString(),
                                binding.edtxSKU.getText().toString());
                        break;

                    case "RegistrarSKUPiezas":

                        break;

                    case "RegistrarEmpaque":

                        break;

                    case "CerrarReempaque":

                        break;

                    case "ConsultaEmpaquesPalletCons":
                        dao = ca.cConsultaPalletConsProd(binding.edtxPallet.getText().toString());
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

                        case "ConsultarPalletAbierto":
                            binding.tvReempaque.setText(dao.getcMensaje());
                           // new ReempaqueProd.SegundoPlano("ConsultaEmpaquesPalletCons").execute();
                            break;

                        case "RegistrarSKU":

                            break;

                        case "RegistrarSKUPiezas":

                            break;


                        case "ConsultaEmpaquesPalletCons":
                            if (ConfigTabla_Totales == null) {

                                ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao,ReempaqueProd.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }

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