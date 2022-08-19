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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.databinding.ActivityDevolucionEmpaqueUnicoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DevolucionEmpaqueUnico extends AppCompatActivity  implements frgmnt_taskbar_AXC.interfazTaskbar {

    private ProgressBarHelper p;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(DevolucionEmpaqueUnico.this);
    ActivityDevolucionEmpaqueUnicoBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    String documento, partida, numParte, cantTotal, cantRec, um, cantEmp, empxpallet;
    Handler h = new Handler();
    int contador;
    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevolucionEmpaqueUnicoBinding.inflate(getLayoutInflater());
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
        //new DevolucionEmpaqueUnico.SegundoPlano("ConsultarPartida").execute();
        ejecutarTarea("ConsultarPartida");
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
            getMenuInflater().inflate(R.menu.devolucion_unico_toolbar, menu);
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

            if (id == R.id.numserie){
                if (binding.linearReg.getVisibility() == View.VISIBLE){
                    binding.linearReg.setVisibility(View.GONE);
                    binding.linearTabla.setVisibility(View.VISIBLE);
                }else{
                    binding.linearReg.setVisibility(View.VISIBLE);
                    binding.linearTabla.setVisibility(View.GONE);
                }
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
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devolución");
        getSupportActionBar().setSubtitle("Empaque único");
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
                if (keyCode== KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.ACTION_DOWN){
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
                        }else
                            ejecutarTarea("ConsultaEmpaque");

                    }
                    catch (Exception e){
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        binding.edtxNumSerie.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        if(binding.edtxNumSerie.getText().equals("")){
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxNumSerie.setText("");
                                    binding.edtxNumSerie.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese el código el número de serie", "true", true, true);
                            return false;
                        }

                        //    new SegundoPlano("ConsultaEmpaque").execute();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        binding.edtxConfirmar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        if(binding.edtxEmpaque.getText().equals("")){
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxConfirmar.setText("");
                                    binding.edtxConfirmar.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese el código del empaque", "false", true, true);
                            return false;
                        }

                        if(!binding.edtxEmpaque.getText().toString().trim().equals(binding.edtxConfirmar.getText().toString().trim())) {
                            new popUpGenerico(contexto, getCurrentFocus(), "Los empaques no coinciden", "false", true, true);
                            binding.edtxConfirmar.setText("");
                            binding.edtxConfirmar.requestFocus();
                        }
                        else{
                            //new DevolucionEmpaqueUnico.SegundoPlano("RegistrarEmpaque").execute();
                            ejecutarTarea("RegistrarEmpaque");
                        }

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
        Intent intent = new Intent(DevolucionEmpaqueUnico.this, SeleccionPartidaDevolucion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        b.putString("Documento", documento);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
    // ******************************************************** EXECUTOR ****************************************************************

    void ejecutarTarea(String tarea){
        ExecutorService service = Executors.newSingleThreadExecutor();
        final DataAccessObject[] dao = new DataAccessObject[1];
        final cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(contexto);

        service.execute(new Runnable() {
            @Override
            public void run() {

                // ******************************************************** PRE EXECUTE ****************************************
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p.ActivarProgressBar(tarea);
                    }
                });
                // ********************************************************* IN BACKGROUND ************************************
                try{
                      switch (tarea) {
                        case "ConsultarPartida":
                            dao[0]= ca.c_ConsultaPartidaDevolucion(documento,partida);
                            break;

                          case "ConsultaEmpaque":
                              dao[0] = ca.c_ConsultaEmpaqueDev(binding.edtxEmpaque.getText().toString());
                              break;

                        case "RegistrarEmpaque":
                            dao[0] = ca.c_OCRegistrarEmpaqueUnicoDevolucion(documento, partida, binding.edtxEmpaque.getText().toString(), "", binding.edtxNumSerie.getText().toString());
                            break;
                    }

                }
                catch (Exception e){
                    dao[0] = new DataAccessObject(e);
                    e.printStackTrace();
                }

                // ******************************************************* POST EXECUTE ***************************************
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (dao[0].iscEstado()) {
                                switch (tarea) {
                                    case "ConsultarPartida":
                                        binding.tvArticulo.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));
                                        binding.tvCantTotal.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendienteTotal"));
                                        binding.tvCantReg.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                                        binding.tvUM.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("UM"));
                                        break;

                                    case "ConsultaEmpaque":
                                        binding.edtxNumSerie.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("NumSerie"));
                                        binding.edtxSKU.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("SKU"));
                                        binding.edtxCantidad.requestFocus();
                                        break;

                                    case "RegistrarEmpaque" :
                                        //new popUpGenerico(contexto, getCurrentFocus(), "Empaque registrado con éxito", "Aviso", true, true);
                                        binding.edtxEmpaque.requestFocus();
                                        binding.edtxEmpaque.setText("");
                                        binding.edtxNumSerie.setText("");
                                        binding.edtxConfirmar.setText("");

                                        if (dao[0].getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1")) {
                                            pop.popUpGenericoDefault(vista, "Orden cerrada con éxito", true);
                                            documento = "";
                                        }else{
                                            ejecutarTarea("ConsultarPartida");
                                        }

                                        break;
                                }

                            }  else if(dao[0].getcMensaje().contains("NO")){
                                Toast.makeText(DevolucionEmpaqueUnico.this, "No existe",Toast.LENGTH_SHORT).show();
                                binding.edtxSKU.setEnabled(true);
                                binding.edtxNumSerie.setEnabled(true);
                                binding.edtxNumSerie.requestFocus();
                            }

                            else {
                                pop.popUpGenericoDefault(vista, dao[0].getcMensaje(), false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            pop.popUpGenericoDefault(vista, e.getMessage(), false);
                        }
                        p.DesactivarProgressBar(tarea);
                    }
                });
            }
        });
    }

}