package com.automatica.AXCPT.c_Almacen.Cuarentena;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityRecuperarEmpaqueBinding;
import com.automatica.AXCPT.databinding.ActivityRecuperarPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

public class RecuperarPallet extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{
    Context contexto = RecuperarPallet.this;
    private ActivityHelpers activityHelpers;
    private ProgressBarHelper p;
    ActivityRecuperarPalletBinding binding;
    Toolbar toolbar;
    popUpGenerico pop = new popUpGenerico(RecuperarPallet.this);
    EditText edtxEmpaque, etUbicacion;
    MediaPlayer mediaPlayer;
    TextView tvArticulo, tvSku,tvCantidad, tvPallet, tvEstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityRecuperarPalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        p = new ProgressBarHelper(this);

        try{

            activityHelpers = new ActivityHelpers();
            activityHelpers.AgregarMenu(RecuperarPallet.this,R.id.Pantalla_principal);
            activityHelpers.AgregarTaskBar(RecuperarPallet.this,R.id.Pantalla_principal,false);
            DeclararVariables();
            AgregarListeners();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(p.ispBarActiva())
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, getCurrentFocus());
            }
            if ((id == R.id.borrar_datos))
            {
                Reinicio();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void DeclararVariables(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recuperar");
        getSupportActionBar().setSubtitle("Pallet");

        edtxEmpaque = findViewById(R.id.edtxCodigoEmpaque);
        etUbicacion = findViewById(R.id.edtxConfirmaEmpaque);

        edtxEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etUbicacion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        tvArticulo = findViewById(R.id.tvArticulo);
        tvCantidad = findViewById(R.id.tvCantidad);
        tvPallet = findViewById(R.id.tvPallet);
        tvEstatus = findViewById(R.id.tvEstatus);
        tvSku = findViewById(R.id.tvSKU);
        edtxEmpaque.requestFocus();

    }

    protected void AgregarListeners(){

        edtxEmpaque.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(TextUtils.isEmpty(edtxEmpaque.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Debe escanear un código de empaque",false);
                }else{
                    new SegundoPlano("ConsultaPallet").execute();
                }

                return false;
            }
        });

        etUbicacion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(TextUtils.isEmpty(etUbicacion.getText())){
                    pop.popUpGenericoDefault(etUbicacion,"Debe escanear un código de posición",false);
                }else {
                    if(TextUtils.isEmpty(edtxEmpaque.getText())){
                        pop.popUpGenericoDefault(edtxEmpaque,"Debe escanear un código de empaque",false);
                    }else{
                        new  SegundoPlano("CuarentenaPallet").execute();
                    }

                }

                return false;
            }
        });

        tvPallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(tvPallet.getText().toString().trim().equals("") || tvPallet.getText().toString().trim().equals("-")){
                    new popUpGenerico(contexto,getCurrentFocus(),"Se debe registrar un pallet para poder continuar", false, true, true);
                }else{
                    String[] datos = { tvPallet.getText().toString()};
                    activityHelpers.getTaskbar_axc().abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_PALLET), FragmentoConsulta.TAG);
                }
                return false;
            }
        });

    }

    protected void Reinicio(){
        tvArticulo.setText("-");
        tvSku.setText("-");
        tvEstatus.setText("-");
        tvPallet.setText("-");
        tvCantidad.setText("-");
        edtxEmpaque.setText("");
        etUbicacion.setText("");
        edtxEmpaque.requestFocus();
    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>{

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(contexto);

        public SegundoPlano(String tarea){Tarea = tarea;}

        @Override
        protected void onPreExecute() {

            p.ActivarProgressBar();

        }
        @Override
        protected Void doInBackground(String... strings) {

            try{
                switch (Tarea){
                    case "ConsultaPallet":
                        dao = ca.c_ConsultarPalletPT(edtxEmpaque.getText().toString());
                        break;

                    case "CuarentenaPallet":
                        dao = ca.cMoverPalletADisponible((edtxEmpaque.getText().toString()),etUbicacion.getText().toString());
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
                        case "ConsultaPallet":
                            tvArticulo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            tvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            tvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                            tvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            tvSku.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            etUbicacion.requestFocus();
                            break;

                        case "CuarentenaPallet":
                            mediaPlayer = MediaPlayer.create(contexto,R.raw.tilin);
                            mediaPlayer.start();
                            Reinicio();
                            new esconderTeclado(RecuperarPallet.this);
                            break;
                    }

                }else{
                    pop.popUpGenericoDefault(getCurrentFocus(),dao.getcMensaje(),false);
                    Reinicio();
                }

            }catch (Exception e){
                e.printStackTrace();
                pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);
                dao = new DataAccessObject();
                Reinicio();
            }
            p.DesactivarProgressBar();
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
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}