package com.automatica.AXCPT.c_Recepcion.Recepcion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Validacion.ValidacionPalletCalidad;
import com.automatica.AXCPT.Validacion.ValidarColocar;
import com.automatica.AXCPT.databinding.ActivityRecepcionPegadoEtiquetaBinding;
import com.automatica.AXCPT.databinding.ActivityValidacionPalletCalidadBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class RecepcionPegadoEtiqueta extends AppCompatActivity implements  frgmnt_taskbar_AXC.interfazTaskbar{

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    private static String strIdTabla = "strIdTablaTotales";
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(RecepcionPegadoEtiqueta.this);
    boolean OrdenCompraSeleccionada, Recarga = true;
    ActivityRecepcionPegadoEtiquetaBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecepcionPegadoEtiquetaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        agregarListener();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pegado de");
        getSupportActionBar().setSubtitle("etiquetas");
        binding.edtxPallet.requestFocus();
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

    @Override
    protected void onResume() {
        if(!binding.edtxPallet.getText().toString().isEmpty()){
            new RecepcionPegadoEtiqueta.SegundoPlano("ConsultarPallet").execute();
        }
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
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

                new RecepcionPegadoEtiqueta.SegundoPlano("ConsultaPallet").execute();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(RecepcionPegadoEtiqueta.this);
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

                    case "ConsultaPallet":
                        dao = ca.c_ConsultaInfoPallet(binding.edtxPallet.getText().toString());
                        break;

                    case"ConsultaPegado":
                        dao = ca.c_ConsultarPegadoEtiqueta(binding.edtxPallet.getText().toString(), binding.edtxPrimerEmpaque.getText().toString(), binding.edtxSegundo.getText().toString());
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


                        case "ConsultaPallet":
                            new esconderTeclado(RecepcionPegadoEtiqueta.this);
                            binding.tvDocumento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Documento"));
                            binding.tvArticulo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.tvFecha.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Fecha"));
                            binding.tvPiezas.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.tvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.edtxPrimerEmpaque.requestFocus();
                            break;

                        case "ConsultaPegado":
                            if (dao.iscEstado()){
                                new com.automatica.axc_lib.Servicios.popUpGenerico(RecepcionPegadoEtiqueta.this, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            }
                            ReiniciarVariables();
                            break;

                    }
                } else {

                    pop.popUpGenericoDefault(vista, dao.getcMensaje(), false);
                    ReiniciarVariables();

                }

            } catch (Exception e) {
                e.printStackTrace();
                pop.popUpGenericoDefault(vista, e.getMessage(), false);

            }
            p.DesactivarProgressBar(Tarea);
        }
    }


    private void agregarListener() {

        binding.edtxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if(binding.edtxPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,null, "Ingrese el pallet.", false, true, true);
                        return false;
                    }
                    else{
                        new RecepcionPegadoEtiqueta.SegundoPlano("ConsultaPallet").execute();
                        binding.edtxSegundo.requestFocus();
                    }

                }
                return false;
            }
        });

        binding.edtxSegundo.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if(binding.edtxPallet.getText().toString().equals("") ||
                            binding.edtxPrimerEmpaque.getText().toString().equals("") ||
                            binding.edtxSegundo.getText().toString().equals("") )
                    {
                        new popUpGenerico(contexto,null, "No puede dejar campos vacíos", false, true, true);
                        return false;
                    }

                    else{
                        new RecepcionPegadoEtiqueta.SegundoPlano("ConsultaPegado").execute();
                    }

                }
                return false;
            }
        });

    }

    public void ReiniciarVariables(){
        binding.edtxPallet.setText("");
        binding.tvDocumento.setText("-");
        binding.tvArticulo.setText("-");
        binding.tvFecha.setText("-");
        binding.tvPiezas.setText("-");
        binding.tvCantidad.setText("-");
        binding.edtxPrimerEmpaque.setText("");
        binding.edtxSegundo.setText("");
        binding.edtxPallet.requestFocus();
        new esconderTeclado(RecepcionPegadoEtiqueta.this);
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
        Intent intent = new Intent(RecepcionPegadoEtiqueta.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

}