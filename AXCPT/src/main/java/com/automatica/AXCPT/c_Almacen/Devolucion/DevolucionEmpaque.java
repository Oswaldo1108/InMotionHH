package com.automatica.AXCPT.c_Almacen.Devolucion;

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
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.Devolucion_Empaques;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.databinding.ActivityDevolucionEmpaqueBinding;
import com.automatica.AXCPT.databinding.ActivitySeleccionPartidaDevolucionBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

    public class DevolucionEmpaque extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    private ProgressBarHelper p;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(DevolucionEmpaque.this);
    ActivityDevolucionEmpaqueBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    String documento, partida, numParte, cantTotal, cantRec, um, cantEmp, empxpallet, SKU;
    private Spinner sp_Partidas, sp_NumSerie;
    Handler h = new Handler();

    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevolucionEmpaqueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sacarDatosIntent();
        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();

        binding.edtxEmpaque.requestFocus();

    }
    @Override
    protected void onResume() {
        new DevolucionEmpaque.SegundoPlano("ConsultarPartida").execute();
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
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


    // ******************************************************** MÉTODOS CREADOS *********************************************************
    private void sacarDatosIntent(){
       try
        {
            b  = getIntent().getExtras();
            documento = b.getString("Documento");
            partida = b.getString("Partida");
            numParte = b.getString("NumParte");
            cantTotal = b.getString("CantidadTotal");
            cantRec = b.getString("CantidadRecibida");
            um = b.getString("UM");
            cantEmp = b.getString("CantidadEmpaques");
            empxpallet = b.getString("EmpaquesPallet");
            SKU = b.getString("SKU");
            if (!documento.isEmpty()){
                binding.tvDocumento.setText(documento);
                binding.tvPartida.setText(partida);
            }
        }
        catch (Exception e)
        {
            Log.e("Error", "SacaDatosIntent: Hubo un error sacando datos de el Bundle -" + e.getStackTrace() );
        }
    }

    private void declararVariables() {
        p = new ProgressBarHelper(this);
        sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devolución");
        getSupportActionBar().setSubtitle("Empaques");
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
        binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        if(binding.edtxEmpaque.getText().equals("")){
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxEmpaque.setText("");
                                    binding.edtxEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese el código del empaque", "false", true, true);
                            return false;
                        }

                        new SegundoPlano("ConsultaEmpaque").execute();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });
    }

    private void validacionFinal(){


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
        Intent intent = new Intent(DevolucionEmpaque.this, SeleccionPartidaDevolucion.class);
        b.putString("Documento", binding.tvDocumento.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    // ********************************************************* SEGUNDO PLANO **********************************************************

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(DevolucionEmpaque.this);

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
                    case "ConsultaEmpaque":
                        dao = ca.c_ConsultaEmpaqueDev(binding.edtxEmpaque.getText().toString());
                        break;

                    case "ConsultarPartida":
                        dao= ca.c_ConsultaPartidaDevolucion(documento,partida);
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

                        case "ConsultarPartida":
                            binding.tvArticulo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));
                            binding.tvCantTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendienteTotal"));
                            binding.tvCantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            binding.tvUM.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));

                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(DevolucionEmpaque.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","PartidaERP","NumParte","CantidadPendienteTotal")));
                                Log.e("Spinner","Si data");
                            }else
                            {
                                Log.e("Spinner","No data");
                                sp_Partidas.setAdapter(null);
                            }
                            break;

                        case "ConsultaEmpaque":
                            binding.edtxNumSerie.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumSerie"));
                            binding.edtxSKU.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("SKU"));
                            binding.edtxCantidad.requestFocus();
                            break;
                    }
                }
                else if(dao.getcMensaje().contains("NO")){
                    Toast.makeText(DevolucionEmpaque.this, "No existe",Toast.LENGTH_SHORT).show();
                    binding.edtxSKU.setEnabled(true);
                    binding.edtxNumSerie.setEnabled(true);
                    binding.edtxNumSerie.requestFocus();
                }
                else {
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