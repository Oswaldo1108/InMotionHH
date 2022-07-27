package com.automatica.AXCPT.c_Produccion.Produccion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.c_Recepcion.Recepcion.PrimerayUltima;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionContenedor;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionDAP;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionPalletNe;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionarHC;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionarPartidaHC;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionarPartidas;
import com.automatica.AXCPT.c_Recepcion.Recepcion.Recepcion_Refacciones;
import com.automatica.AXCPT.databinding.ActivityRecepcionSeleccionarHcBinding;
import com.automatica.AXCPT.databinding.ActivitySeleccionarOrdenProdBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class SeleccionarOrdenProd extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar{
    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    private static String strIdTabla = "strIdTablaTotales";
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(SeleccionarOrdenProd.this);
    boolean OrdenCompraSeleccionada, Recarga = true;
    ActivitySeleccionarOrdenProdBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeleccionarOrdenProdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Órdenes de producción");

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

        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("", "");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal, taskbar_axc, "FragmentoTaskBar").commit();

    }
    // MÉTODOS DEL CICLO DE VIDA DE ACTIVITY
    @Override
    protected void onResume() {
        new SeleccionarOrdenProd.SegundoPlano("LlenarTabla").execute();
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.SIGUIENTE);
    }

    // MÉTODOS PARA LA TOOLBAR
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

        if (p.ispBarActiva())
        {

            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo)) {

                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar) {

                new SeleccionarOrdenProd.SegundoPlano("LlenarTabla").execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // MÉTODOS PARA EL MENÚ INFERIOR
    @Override
    public void BotonDerecha() { ValidacionFinal(); }

    @Override
    public void BotonIzquierda() {onBackPressed();  }

    // EVENTOS PARA LA TABLA
    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {
        b.putString("Documento",clickedData[0]);
        binding.edtxDocumento.setText(clickedData[0].trim());
        OrdenCompraSeleccionada = true;
    }

    // CLASE PARA LLAMAR A LOS WEBSERVICES
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(SeleccionarOrdenProd.this);
        String Tarea;
        DataAccessObject dao;

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

                        dao = ca.c_ListarOrdenesProduccionLiberadas();
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

                                ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao,SeleccionarOrdenProd.this);
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

    // MÉTODOS ADICIONALES
    private void ValidacionFinal() {
        try{
/*
            if(binding.edtxDocumento.getText().toString().equals(""))
            {
                new popUpGenerico(contexto,vista,getString(R.string.error_seleccionar_registro),"Advertencia",true,false);

                return;
            }
            if(!ConfigTabla_Totales.renglonEstaSelec())
            {
                new popUpGenerico(contexto,vista ,getString(R.string.error_seleccionar_registro) , false, true,true );
                return;
            }
*/
            new PopUpMenuAXC().newInstance(taskbar_axc.getView().findViewById(R.id.BotonDer), SeleccionarOrdenProd.this, R.menu.popup_produccion_vde, new PopUpMenuAXC.ContextMenuListener() {
                @Override
                public void listenerItem(MenuItem item) {
                    try{
                        switch (item.getItemId()){
                            case R.id.EmpaqueU:
                                intent= new Intent(contexto, Almacen_Armado_Pallets_Empaque.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.etiquetado:
                                intent= new Intent(contexto, Almacen_Armado_Pallets.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.palletne:
                                intent= new Intent(contexto, Almacen_Armado_Pallets_NE.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.cantidad_conf:
                                intent= new Intent(contexto, Almacen_Armado_Pallets_Conf.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            default:
                            //    pop.popUpGenericoDefault(vista,"Seleccione una opción",false);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        pop.popUpGenericoDefault(vista,e.getMessage(),false);
                    }
                }
            });
            //new PopUpMenuAXC(taskbar_axc.getView().findViewById(R.id.BotonDer),RecepcionSeleccionarPartidas.this,R.menu.popup_etiquetados);

        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }




    }

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
        Intent intent = new Intent(SeleccionarOrdenProd.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}