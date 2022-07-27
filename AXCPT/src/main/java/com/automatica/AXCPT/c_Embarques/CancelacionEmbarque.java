package com.automatica.AXCPT.c_Embarques;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Cancelar_Tarima;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;

import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.databinding.ActivityCancelacionEmbarqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class CancelacionEmbarque extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, TableViewDataConfigurator.TableClickInterface {

    private ActivityCancelacionEmbarqueBinding binding;
    private ProgressBarHelper p;
    private ActivityHelpers activityHelpers;
    SortableTableView tabla;
    TableViewDataConfigurator ConfigTabla = null;
    Handler h = new Handler();
    Context context = this;
    popUpGenerico pop = new popUpGenerico(CancelacionEmbarque.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCancelacionEmbarqueBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        p = new ProgressBarHelper(this);
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(CancelacionEmbarque.this, R.id.Pantalla_principal);
        activityHelpers.AgregarTaskBar(CancelacionEmbarque.this,R.id.Pantalla_principal,true);
        binding.etOrden.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        binding.etOrdenConfirmacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        setSupportActionBar(binding.toolbar.getRoot());
        getSupportActionBar().setTitle("Cancelación");
        getSupportActionBar().setSubtitle("Embarque");
        tabla = (SortableTableView) view.findViewById(R.id.tableView_OC);
        AgregarListeners();


    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try{
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos,menu);
            return true;
        }catch (Exception ex){
            Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       
       int id = item.getItemId();
       if (p.ispBarActiva()){
           
           if ((id == R.id.InformacionDispositivo)){
               
               new sobreDispositivo(getApplicationContext(),getCurrentFocus());
           }
           if ((id == R.id.borrar_datos)){
               
               Reiniciar();
           }
       }
        return super.onOptionsItemSelected(item);
    }

    private void Reiniciar() {
        binding.etOrden.setText("");
        binding.etOrden.requestFocus();
        tabla.getDataAdapter().clear();
        tabla.getDataAdapter().notifyDataSetChanged();
        binding.etMotivo.setText("");
        binding.etOrdenConfirmacion.setText("");
    }

    private void AgregarListeners() {
        try{
            binding.etOrden.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(TextUtils.isEmpty(binding.etOrden.getText())){
                        pop.popUpGenericoDefault(binding.etOrden,"Ingrese una orden",false);
                        binding.etOrden.requestFocus();
                    }else{
                        new SegundoPlano("LlenarTabla").execute();
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                binding.etMotivo.requestFocus();
                            }
                        });
                    }
                    return false;
                }
            });

            binding.etMotivo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(TextUtils.isEmpty(binding.etMotivo.getText())){
                        pop.popUpGenericoDefault(binding.etMotivo,"Ingrese un motivo de cancelación",false);
                        binding.etMotivo.requestFocus();
                    }else{
                        binding.etOrdenConfirmacion.requestFocus();
                    }
                    return false;
                }
            });
            binding.etOrdenConfirmacion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ( TextUtils.isEmpty(binding.etOrdenConfirmacion.getText()) ){
                        pop.popUpGenericoDefault(binding.etOrdenConfirmacion,"Ingrese la confirmación de la orden",false);
                        binding.etOrdenConfirmacion.requestFocus();
                    }else{
                        if( TextUtils.isEmpty(binding.etMotivo.getText()) ){
                            pop.popUpGenericoDefault(binding.etOrdenConfirmacion,"Ingrese un motivo de cancelación",false);
                            binding.etMotivo.requestFocus();
                        }else{
                            if(!binding.etOrden.getText().toString().equals(binding.etOrdenConfirmacion.getText().toString())){
                                pop.popUpGenericoDefault(binding.etOrdenConfirmacion,"Los números de orden son diferentes",false);
                                binding.etOrdenConfirmacion.requestFocus();
                            }
                            else{
                                new CreaDialogos("¿Cancelar Embarque? ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new SegundoPlano("CancelarEmbarque").execute();
                                    }
                                },null,context);
                            }

                        }
                    }
                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>{

        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Embarques ca = new cAccesoADatos_Embarques(CancelacionEmbarque.this);
        public SegundoPlano(String tarea){this.tarea = tarea;}

        protected void onPreExecute(){
            try{
                    p.ActivarProgressBar(tarea);
            }catch (Exception e){
                e.printStackTrace();
                pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);
            }
        }


        @Override
        protected Void doInBackground(String... params) {

            try{
                switch (tarea){
                    case "LlenarTabla":
                        dao = ca.cListaEmbarqueValidadasDet(binding.etOrden.getText().toString());
                        break;
                    case "CancelarEmbarque":
                        dao = ca.cCancelaEmbarqueValidado(binding.etOrden.getText().toString(),binding.etMotivo.getText().toString());
                        break;
                    default:
                        dao = new DataAccessObject(false,tarea,null);
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
                dao = new DataAccessObject(e);
            }

            return null;
        }

        protected void onPostExecute(Void aVoid){
            try {
                    if(dao.iscEstado()){
                        switch (tarea){
                            case "LlenarTabla":
                                if(dao.getcTablaUnica()!=null){
                                    if (ConfigTabla == null){
                                        ConfigTabla = new TableViewDataConfigurator(tabla,dao,CancelacionEmbarque.this);
                                    }else{
                                        ConfigTabla.CargarDatosTabla(dao);
                                        binding.etMotivo.requestFocus();
                                    }
                                }else{
                                    tabla.getDataAdapter().clear();
                                    tabla.getDataAdapter().notifyDataSetChanged();
                                }
                                break;
                            case "CancelarEmbarque":
                                    Reiniciar();
                                    pop.popUpGenericoDefault(getCurrentFocus(),"Orden cancelada con éxito",true);
                                    binding.etOrden.requestFocus();
                                break;
                            default:
                                dao = new DataAccessObject(false,tarea,null);
                                break;
                        }
                    }else{
                        Reiniciar();
                        pop.popUpGenericoDefault(getCurrentFocus(),dao.getcMensaje(),false);


                    }
            }catch (Exception e){
                pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);
                e.printStackTrace();
            }
            p.DesactivarProgressBar(tarea);
        }
    }

    @Override
    public void BotonDerecha() {

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