package com.automatica.AXCPT.c_Produccion.Surtido;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;
import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.newInstance;

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
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.c_Almacen.Devolucion.DevolucionPalletNE;
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionOrdenDevolucion;
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionPartidaDevolucion;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion.Validacion_PorPallet;
import com.automatica.AXCPT.databinding.ActivityRechazarSurtidoBinding;
import com.automatica.AXCPT.databinding.ActivitySeleccionOrdenDevolucionBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class RechazarSurtido extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar  {

    ActivityRechazarSurtidoBinding binding;
    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    View vista;
    Handler h = new Handler();
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(RechazarSurtido.this);
    frgmnt_taskbar_AXC taskbar_axc;
    private static String strIdTabla = "strIdTablaTotales";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRechazarSurtidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sacarDatosIntent();
        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();
    }

    @Override
    protected void onResume() {
        if (!binding.edtxDocumento.getText().toString().equals(""))
            new SegundoPlano("ConsultarOrden").execute();

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
    private void sacarDatosIntent(){
        try
        {
            b  = getIntent().getExtras();

        }
        catch (Exception e)
        {
            Log.e("Error", "SacaDatosIntent: Hubo un error sacando datos de el Bundle -" + e.getStackTrace() );
        }
    }

    private void declararVariables() {
        p = new ProgressBarHelper(this);
        tabla = findViewById(R.id.tableView_OC);
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rechazar");
        getSupportActionBar().setSubtitle("Empaques surtidos");
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

        binding.edtxDocumento.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.ACTION_DOWN){
                    try{
                        if (binding.edtxDocumento.getText().toString().equals("")){
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese orden de producción", false, true, true);
                            return false;
                        }
                        else
                            new SegundoPlano("ConsultarOrden").execute();
                    }catch (Exception e){

                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);


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
        Intent intent = new Intent(RechazarSurtido.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }



    // ********************************************************* SEGUNDO PLANO **********************************************************

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Embarques ca= new cAccesoADatos_Embarques(contexto);

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

                    case "ConsultarOrden":
                        dao = ca.c_ConsultaEmpaquesOP(binding.edtxDocumento.getText().toString());
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
                    switch (Tarea){
                        case "ConsultarOrden":
                            if(ConfigTabla_Totales == null)
                            {
                                ConfigTabla_Totales = new TableViewDataConfigurator(strIdTabla,0, "VALIDADO", "SURTIDA", "4", tabla, dao, RechazarSurtido.this);
                            }else
                            {
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