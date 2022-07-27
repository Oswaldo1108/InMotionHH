package com.automatica.AXCPT.Validacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Embarques.Reempaque.Embarques_Reempaque;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionar;
import com.automatica.AXCPT.databinding.ActivityConsultaOrdenesBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class ConsultaOrdenes extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, TableViewDataConfigurator.TableClickInterface{

    private ActivityConsultaOrdenesBinding binding;
    private ProgressBarHelper p;
    private ActivityHelpers activityHelpers;
    Context contexto = ConsultaOrdenes.this;
    View vista;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tablaOrdenes;
    Toolbar toolbar;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(ConsultaOrdenes.this);
    boolean Sel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityConsultaOrdenesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        p = new ProgressBarHelper(this);
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(ConsultaOrdenes.this,R.id.Pantalla_principal);
        activityHelpers.AgregarTaskBar(ConsultaOrdenes.this,R.id.Pantalla_principal,true);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Consulta Ordenes");
        tablaOrdenes = findViewById(R.id.customtableview).findViewById(R.id.tableView_OC);

        //Funcionalidad();

    }

    @Override
    protected void onResume() {
        new ConsultaOrdenes.SegundoPlano("LlenarTabla").execute();
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.SELECCIONAR);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_borrar_datos,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (p.ispBarActiva())
        {

            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo)) {

                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar) {

                new ConsultaOrdenes.SegundoPlano("LlenarTabla").execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }



    private void Funcionalidad(){
        try{

           binding.etOrden.setOnEditorActionListener(new TextView.OnEditorActionListener() {
               @Override
               public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                   if(!TextUtils.isEmpty(binding.etOrden.getText())){
                       new SegundoPlano("LlenarTabla").execute();
                   }
                   new esconderTeclado(ConsultaOrdenes.this);
                   return false;
               }
           });

        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);

        }
    }

    private void BorrarDatos(){
        binding.etOrden.setText("");
        tablaOrdenes.getDataAdapter().clear();
        tablaOrdenes.getDataAdapter().notifyDataSetChanged();
        binding.etOrden.requestFocus();
        b.clear();

    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {
        Sel = Seleccionado;
        b.putString("Pallet",clickedData[0]);
        b.putString("Orden", binding.etOrden.getText().toString());
    }

    private void ValidacionFinal(){
        if(Sel){
            Intent intent = new Intent(ConsultaOrdenes.this, ConsultaEmpaquesPorPallet.class);
            intent.putExtras(b);
            startActivity(intent);
        }else{
            pop.popUpGenericoDefault(vista,"Selecciona un pallet",false);
            return;
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

                    case "LlenarTabla":
                       // dao = ca.c_ListarValidacionCalidad();
                        break;

                }

            }catch (Exception e){
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

                        case "LlenarTabla":

                            if(ConfigTabla_Totales == null){

                                ConfigTabla_Totales = new TableViewDataConfigurator(tablaOrdenes, dao, ConsultaOrdenes.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }

                            binding.etOrden.requestFocus();

                            break;

                    }

                } else{
                    pop.popUpGenericoDefault(getCurrentFocus(),dao.getcMensaje(),false);
                    //BorrarDatos();
                }

            }catch (Exception e){
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