package com.automatica.AXCPT.c_Produccion.Surtido;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Validacion.ValidacionPalletCalidad;
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionPartidaDevolucion;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion.Validacion_PorPallet;
import com.automatica.AXCPT.c_Produccion.Produccion.Almacen_Armado_Pallets;
import com.automatica.AXCPT.c_Produccion.Produccion.Almacen_Armado_Pallets_Empaque;
import com.automatica.AXCPT.databinding.ActivitySurtidoProdEmpaqueBinding;
import com.automatica.AXCPT.databinding.ActivityValidarOrdenSurtidoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class ValidarOrdenSurtido extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar {

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    View vista;
    String str_Maquina = "@";
    Spinner spnr_Maquinas;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(ValidarOrdenSurtido.this);
    ActivityValidarOrdenSurtidoBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    String mensaje, documento, empaques, estatus;

    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValidarOrdenSurtidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        agregarListener();
        sacarDatosIntent();
        declararVariables();
        configurarToolbar();
        configurarTaskbar();
    }

    @Override
    protected void onResume() {

        new ValidarOrdenSurtido.SegundoPlano("ConsultaMaquinas").execute();
       if (!binding.edtxCarrito.equals(""))
           Log.e("SegundoPlano", "ConsultarCarrito");
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.VALIDAR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.cerrar_op_toolbar_cancelar, menu);
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

            if((id == R.id.CambiarVista)){
                if (binding.LinearPrincipal.getVisibility()==View.VISIBLE){
                    binding.LinearPrincipal.setVisibility(View.GONE);
                    binding.linearOrden.setVisibility(View.VISIBLE);
                }
                else{
                    binding.LinearPrincipal.setVisibility(View.VISIBLE);
                    binding.linearOrden.setVisibility(View.GONE);
                }
            }


            if ((id == R.id.cancelar_pallets))
            {
                rechazarPallet();
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
        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);
    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

    }


    // ******************************************************** MÉTODOS CREADOS *********************************************************
    private void sacarDatosIntent() {

    }

    private void declararVariables() {
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        spnr_Maquinas = (Spinner) findViewById(R.id.spnr_Maquinas).findViewById(R.id.spinner);
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Validar surtido");
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
        new CreaDialogos("¿Desea validar el surtido?", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {

                new ValidarOrdenSurtido.SegundoPlano("Validar").execute();
                new esconderTeclado(ValidarOrdenSurtido.this);
            }
        },null,contexto);
    }

    private void rechazarPallet(){
        new CreaDialogos("¿Desea rechazar el surtido?", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {
                new ValidarOrdenSurtido.SegundoPlano("Rechazar").execute();
                new esconderTeclado(ValidarOrdenSurtido.this);
            }
        },null,contexto);
    }

    private void agregarListener(){
        binding.edtxCarrito.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if (binding.edtxCarrito.getText().toString().equals("")){
                        new popUpGenerico(contexto,getCurrentFocus() ,"Ingrese un código de carrito" ,"false" ,true , true);
                        return false;
                    }else
                        new ValidarOrdenSurtido.SegundoPlano("ConsultarCarrito").execute();
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
        Intent intent = new Intent(ValidarOrdenSurtido.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    // ********************************************************* SegundoPlano *********************************************************
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Embarques adEmb = new cAccesoADatos_Embarques(ValidarOrdenSurtido.this);

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
                    case "ConsultarCarrito":
                        dao = adEmb.cad_ConsultaCarrito(binding.edtxCarrito.getText().toString());
                        break;
                    case "ConsultaMaquinas":
                        dao = adEmb.c_ConsultaMaquinas(str_Maquina);
                        break;

                /*    case "ConsultaDocumento":
                        dao = adEmb.c_ConsultaValidadosOP(binding.edtxOP.getText().toString());
                        break;*/
                    case "Validar":
                        dao = adEmb.cad_ValidarCarrito(binding.edtxCarrito.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato());
                        break;

                    case "Rechazar":
                        dao = adEmb.c_RechazaSurtidoProduccion(binding.tvDocActual.getText().toString(),binding.edtxCarrito.getText().toString());
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
                        case "ConsultarCarrito":
                            mensaje = dao.getcMensaje();
                           // binding.edtxOP.setText(mensaje.split("\\|")[0]);
                            binding.tvDocActual.setText(mensaje.split("\\|")[0]);
                            binding.tvEmpaques.setText(mensaje.split("\\|")[1]);
                            binding.tvEstatus.setText(mensaje.split("\\|")[2]);
                            if (ConfigTabla_Totales == null) {

                                //ConfigTabla_Totales = new TableViewDataConfigurator("strIdTablaTotales",4, "VALIDADA", "PENDIENTE", "4", tabla, dao, ValidarOrdenSurtido.this);
                                ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao,ValidarOrdenSurtido.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }
                           // new SegundoPlano("ConsultaDocumento").execute();
                            Log.e("mensaje", mensaje);
                            break;

                        case "ConsultaMaquinas":
                           spnr_Maquinas.setAdapter(new CustomArrayAdapter(ValidarOrdenSurtido.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("DLinea","Linea")));
                            break;

                        case "Validar":
                            pop.popUpGenericoDefault(vista, "Carrito validado", true);
                            tabla.getDataAdapter().clear();
                            binding.edtxCarrito.setText("");
                            binding.tvDocActual.setText("-");
                            binding.tvEmpaques.setText("-");
                            binding.tvEstatus.setText("-");
                            break;

                        case "Rechazar":
                            pop.popUpGenericoDefault(vista, "Carrito rechazado", true);
                            binding.edtxCarrito.setText("");
                            binding.tvDocActual.setText("-");
                            binding.tvEmpaques.setText("-");
                            binding.tvEstatus.setText("-");
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
            new esconderTeclado(ValidarOrdenSurtido.this);
            p.DesactivarProgressBar(Tarea);
        }
    }

}