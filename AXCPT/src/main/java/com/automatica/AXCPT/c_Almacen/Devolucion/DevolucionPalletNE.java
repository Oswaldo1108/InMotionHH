package com.automatica.AXCPT.c_Almacen.Devolucion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.Devolucion_NE;
import com.automatica.AXCPT.databinding.ActivityDevolucionEmpaqueUnicoBinding;
import com.automatica.AXCPT.databinding.ActivityDevolucionPalletNeBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

public class DevolucionPalletNE extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar {

    private ProgressBarHelper p;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(DevolucionPalletNE.this);
    ActivityDevolucionPalletNeBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    String documento, partida, numParte, cantTotal, cantRec, um, cantEmp, empxpallet;

    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevolucionPalletNeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sacarDatosIntent();
        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();

        try {
           // new DevolucionPalletNE.SegundoPlano("ConsultarOrden").execute();
           // new DevolucionPalletNE.SegundoPlano("ListarLotes").execute();
           // new DevolucionPalletNE.SegundoPlano("ConsultarPallet").execute();
        }catch (Exception e){
            e.printStackTrace();
            new popUpGenerico(DevolucionPalletNE.this,getCurrentFocus(),e.getMessage(),false,true,true);
        }
    }
    @Override
    protected void onResume() {
        new DevolucionPalletNE.SegundoPlano("ConsultarOrden").execute();
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
        getSupportActionBar().setSubtitle("Pallet NE");
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
        Intent intent = new Intent(DevolucionPalletNE.this, SeleccionPartidaDevolucion.class);
        b.putString("Documento", binding.tvDocumento.getText().toString());
        intent.putExtras(b);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    // ********************************************************* SEGUNDO PLANO **********************************************************

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(DevolucionPalletNE.this);

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

                    case "ConsultarPallet":
                        dao = ca.c_ConsultaPalletAbiertoDev(documento,partida);
                        break;
                    case "ListarLotes":
                        /**
                         * dao listar Lotes
                         */
                        dao= ca.c_ListarLotesDev(documento,partida,numParte);
                        break;
                    case "ConsultarOrden":
                        /**
                         * dao consultarOrden
                         */
                        dao= ca.c_ConsultaPartidaDevolucion(documento,partida);
                        break;
                    case "RegistrarEmpaques":
                        dao= ca.c_CreaEmpaqueDevSE(
                                documento,
                                partida,
                                "",
                                binding.edtxCantidad.getText().toString(),
                                binding.edtxEmpxpallet.getText().toString());
                        break;

                    case "CreaLoteRecepcionDev":

                        dao = ca.c_CreaLoteRecepcionDev(documento,
                                partida,
                                "",
                                "1");
                        break;
                    case "CerrarTarima":
                        /**
                         * dao cerrar tarima
                         */
                        dao = ca.c_CierraPalletDevolucion(binding.tvPallet.getText().toString());
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
                            binding.tvArticulo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));
                            binding.tvCantTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendienteTotal"));
                            binding.tvCantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            binding.tvUM.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));
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