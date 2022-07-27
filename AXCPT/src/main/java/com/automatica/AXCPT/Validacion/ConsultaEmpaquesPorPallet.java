package com.automatica.AXCPT.Validacion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.FragmentoValidarEmpaques;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionar;
import com.automatica.AXCPT.databinding.ActivityConsultaEmpaquesPorPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;
import de.codecrafters.tableview.SortableTableView;

public class ConsultaEmpaquesPorPallet extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, TableViewDataConfigurator.TableClickInterface{

    private ActivityConsultaEmpaquesPorPalletBinding binding;
    private ProgressBarHelper p;
    private ActivityHelpers activityHelpers;
    Context contexto = ConsultaEmpaquesPorPallet.this;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tablaOrdenes;
    Toolbar toolbar;
    Bundle b = new Bundle();
    String Orden,Pallet;
    private Spinner spPallets;

    popUpGenerico pop = new popUpGenerico(ConsultaEmpaquesPorPallet.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityConsultaEmpaquesPorPalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        p = new ProgressBarHelper(this);
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(ConsultaEmpaquesPorPallet.this,R.id.Pantalla_principal);
        activityHelpers.AgregarTaskBar(ConsultaEmpaquesPorPallet.this,R.id.Pantalla_principal,false);

        spPallets = binding.vwSpinner.findViewById(R.id.spinner);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Empaques a validar");
        tablaOrdenes = findViewById(R.id.customtableview).findViewById(R.id.tableView_OC);
        SacaDatosIntent();
        Funcionalidad();

        new SegundoPlano("ConsultaOrden").execute();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (p.ispBarActiva()){
            int id = item.getItemId();
            switch (id){

                case R.id.InformacionDispositivo:
                    new sobreDispositivo(ConsultaEmpaquesPorPallet.this,getCurrentFocus());

                    break;

            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void SacaDatosIntent(){

        try {

            b = getIntent().getExtras();
            Orden = b.getString("Orden");
            Pallet = b.getString("Pallet");


        }catch (Exception e){
            e.printStackTrace();
            Log.i("PRUEBAS","AQUI TRUENA");
            pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);
        }

    }

    private void Funcionalidad(){
        try{

                spPallets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        new SegundoPlano("ConsultaEmpaques").execute();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        }catch (Exception e){
            e.printStackTrace();
            Log.i("PRUEBAS","AQUI TRUENAX2");
            pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);

        }
    }

    private void BorrarDatos(){
        tablaOrdenes.getDataAdapter().clear();
        tablaOrdenes.getDataAdapter().notifyDataSetChanged();


    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

  /*      getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.Pantalla_principal, FragmentoValidarEmpaques.newInstance("",clickedData[0]),"","","").addToBackStack("")
                .commit();*/

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

        b.putString("Pallet",clickedData[0]);

    }

    private void ValidacionFinal(){
        if(ConfigTabla_Totales.renglonEstaSelec()){
            Intent intent = new Intent(ConsultaEmpaquesPorPallet.this, RecepcionSeleccionar.class);
            intent.putExtras(b);
            startActivity(intent);
        }else{
            pop.popUpGenericoDefault(getCurrentFocus(),"Seleccione un pallet para validar",false);
        }
    }

    private class SegundoPlano extends AsyncTask<String, Void, Void>{
        String Tarea;
        DataAccessObject dao;
        cAccesoADatos ca = new cAccesoADatos(contexto);

        public SegundoPlano (String tarea){ Tarea = tarea;}

        @Override
        protected void onPreExecute() {
            p.ActivarProgressBar(Tarea);
        }

        @Override
        protected Void doInBackground(String... strings) {

            try{
                switch (Tarea){

                    case "ConsultaOrden":

                        dao = ca.cConsultaOrdenesParaValidarSpinner(Orden);

                        break;

                    case "ConsultaEmpaques":

                        dao = ca.cListaEmpaquesXOrdenRecepcion(Orden,((Constructor_Dato)spPallets.getSelectedItem()).getDato());

                        break;

                    default:
                        dao = new DataAccessObject();
                }

            }catch (Exception e){
                Log.i("PRUEBAS","AQUI TRUENAX3");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try{
                if(dao.iscEstado()){

                    switch (Tarea){

                        case "ConsultaOrden":

                            if( dao.getcTablas() != null){


                                spPallets.setAdapter(new CustomArrayAdapter(ConsultaEmpaquesPorPallet.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("Pallet","Articulo","Cantidad")));

                            } else{

                                spPallets.setAdapter(null);
                            }

                            int PalletSeleccionado = -2;
                            PalletSeleccionado = CustomArrayAdapter.getIndex(spPallets,Pallet);

                            switch (PalletSeleccionado){

                                case -2:
                                    pop.popUpGenericoDefault(getCurrentFocus(),"Error interno.",false);
                                    new esconderTeclado(ConsultaEmpaquesPorPallet.this);
                                    return;
                                case -1:
                                    pop.popUpGenericoDefault(getCurrentFocus(),"No se encontró el pallet dentro del listado", false);
                                    Log.i("PRUEBAS",Pallet);
                                    new esconderTeclado(ConsultaEmpaquesPorPallet.this);
                                    return;
                                case -3:
                                    spPallets.setSelection(0);
                                    return;
                                default:
                                    spPallets.setSelection(PalletSeleccionado);
                            }
                            new esconderTeclado(ConsultaEmpaquesPorPallet.this);
                            Pallet = "DEFAULT";

                            break;

                        case "ConsultaEmpaques":

                            if(ConfigTabla_Totales == null){

                                ConfigTabla_Totales = new TableViewDataConfigurator(tablaOrdenes, dao, ConsultaEmpaquesPorPallet.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }

                            break;
                    }

                } else{
                    pop.popUpGenericoDefault(getCurrentFocus(),dao.getcMensaje(),false);
                    BorrarDatos();
                }

            }catch (Exception e){
                Log.i("PRUEBAS","AQUI TRUENAX4");
                e.printStackTrace();
                pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);
            }
            p.DesactivarProgressBar(Tarea);
        }
    }

    @Override
    public void BotonDerecha() {
        ValidacionFinal();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }
}