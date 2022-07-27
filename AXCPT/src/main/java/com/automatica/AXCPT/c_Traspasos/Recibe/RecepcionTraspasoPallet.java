package com.automatica.AXCPT.c_Traspasos.Recibe;

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
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Rec_Traspaso_Con_Etiquetas;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionConteo;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionPegadoEtiqueta;
import com.automatica.AXCPT.c_Traspasos.MenuTraspaso;
import com.automatica.AXCPT.databinding.ActivityRecepcionPegadoEtiquetaBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionTraspasoPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import de.codecrafters.tableview.SortableTableView;

public class RecepcionTraspasoPallet extends AppCompatActivity implements  frgmnt_taskbar_AXC.interfazTaskbar {

    private ProgressBarHelper p;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(RecepcionTraspasoPallet.this);
    ActivityRecepcionTraspasoPalletBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Handler h = new Handler();
    boolean recargar;
    String transferencia, partida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
        super.onCreate(savedInstanceState);
        binding = ActivityRecepcionTraspasoPalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configuracionToolbar();
        configuracionTaskbar();
        agregarListener();
        sacarDatosIntent();
            h.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    binding.edtxPallet.requestFocus();

                }
            },150);

        } catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista, e.getMessage(), false);
        }
    }

    private void agregarListener() {
        binding.edtxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    new RecepcionTraspasoPallet.SegundoPlano("ConsultarPallet").execute();
                }
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        if(!binding.edtxPallet.getText().toString().isEmpty()){
            new RecepcionTraspasoPallet.SegundoPlano("ConsultarPallet").execute();
        }
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
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (p.ispBarActiva()) {
            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {

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


    private void configuracionToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recepción traspaso");
        getSupportActionBar().setSubtitle("Pallet");
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

    private void configuracionTaskbar() {
        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();
    }

    private void sacarDatosIntent(){
        try
        {
            Bundle b = getIntent().getExtras();
            transferencia = b.getString("Transferencia");
            partida =  b.getString("Partida");
        }
        catch (Exception e)
        {
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(RecepcionTraspasoPallet.this);
        cAccesoADatos_Consultas cadcons = new cAccesoADatos_Consultas(RecepcionTraspasoPallet.this);

        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        View LastView;

        @Override
        protected void onPreExecute() {

           // p.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                switch (tarea) {
                    case "ConsultaPallet":
                        dao = cadcons.c_ConsultarPalletPT(binding.edtxPallet.getText().toString());
                        break;

                    case "ConfirmaPallet" :
                        dao = cad.cad_RegistroTraspasoPalletConEtiquetas(transferencia,partida,binding.edtxConfirmar.getText().toString());

                        break;
                    default:
                        dao = new DataAccessObject();

                }
            } catch (Exception e) {
                e.printStackTrace();
                dao = new DataAccessObject(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {

                if (dao.iscEstado()) {

                    switch (tarea) {
                        case "ConsultaPallet":
                            binding.tvProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.tvPaquetes.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.tvCantidad.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.tvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                            binding.tvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                            binding.edtxConfirmar.requestFocus();
                            break;

                        case "ConfirmaPallet":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.envio_pallet_registro_exito),dao.iscEstado(), true, true);
                            break;


                    }
                } else {
                    switch (tarea) {

                    }
                }

                //p.DesactivarProgressBar(tarea);
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
            recargar = false;
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
        Intent intent = new Intent(RecepcionTraspasoPallet.this, MenuTraspaso.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }



}