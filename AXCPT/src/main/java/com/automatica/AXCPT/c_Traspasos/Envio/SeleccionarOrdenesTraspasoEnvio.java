package com.automatica.AXCPT.c_Traspasos.Envio;

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
import android.view.WindowManager;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionOrdenDevolucion;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Seleccion_Orden;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionar;
import com.automatica.AXCPT.c_Traspasos.MenuTraspaso;
import com.automatica.AXCPT.c_Traspasos.Recibe.SeleccionOrdenTraspasoRecepcion;
import com.automatica.AXCPT.databinding.ActivitySeleccionarOrdenesTraspasoEnvioBinding;
import com.automatica.AXCPT.objetos.ObjetoEtiquetaSKU;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class SeleccionarOrdenesTraspasoEnvio extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar{

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    private Handler h = new Handler();
    popUpGenerico pop = new popUpGenerico(SeleccionarOrdenesTraspasoEnvio.this);
    ActivitySeleccionarOrdenesTraspasoEnvioBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    boolean DocumentoSeleccionado = false;
    String documento = "@";

    // ****************************************************** CICLO DE VIDA *********************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeleccionarOrdenesTraspasoEnvioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();


    }

    @Override
    protected void onResume() {
        new SeleccionarOrdenesTraspasoEnvio.SegundoPlano("LlenarTabla").execute();
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.SIGUIENTE);
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
                new SeleccionarOrdenesTraspasoEnvio.SegundoPlano("LlenarTabla").execute();
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
        DocumentoSeleccionado = true;
        binding.edtxDocumento.setText(clickedData[0]);
        b.putString("Documento", clickedData[0]);
        Log.d("Mensaje1", clickedData[0]);


    }

    // ******************************************************** MÉTODOS CREADOS *********************************************************
    private void declararVariables() {
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
    }

    private  void agregarListener(){
        binding.edtxDocumento.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (binding.edtxDocumento.getText().toString().equals("")) {
                        new popUpGenerico(contexto, binding.edtxDocumento, getString(R.string.error_ingrese_pedido), "false", true, true);
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.edtxDocumento.requestFocus();
                                binding.edtxDocumento.setText("");
                            }
                        });

                        return false;
                    }


                    new SeleccionarOrdenesTraspasoEnvio.SegundoPlano("LlenarTabla").execute();

                    new com.automatica.AXCPT.Servicios.esconderTeclado(SeleccionarOrdenesTraspasoEnvio.this);

                }
                return false;
            }
        });
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Órdenes de Traspaso E");
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

        intent = new Intent(contexto, SeleccionPartidaTraspasoEnvio.class);
        if(DocumentoSeleccionado)
            intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
        Log.d("Mensaje1", "Hola");
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
        Intent intent = new Intent(SeleccionarOrdenesTraspasoEnvio.this, MenuTraspaso.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }


    // ********************************************************* SEGUNDO PLANO *********************************************************
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(SeleccionarOrdenesTraspasoEnvio.this);

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
                        dao = ca.c_ListaOrdenesTraspaso(documento);
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
                            new esconderTeclado(SeleccionarOrdenesTraspasoEnvio.this);
                            try
                            {
                                if (ConfigTabla_Totales == null) {

                                    ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao, SeleccionarOrdenesTraspasoEnvio.this);
                                } else{
                                    ConfigTabla_Totales.CargarDatosTabla(dao);
                                }
                            } catch (Exception e) {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), "No existen traspasos asignados" ,"false", true, true);
                                e.printStackTrace();
                            }
                            documento = "@";
                            break;
                    }
                } else {
                    pop.popUpGenericoDefault(vista, dao.getcMensaje(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(),"false", true, true);

            }
            p.DesactivarProgressBar(Tarea);
        }
    }
}