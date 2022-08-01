package com.automatica.AXCPT.c_Traspasos.Recibe;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Rec_Traspaso_Con_Etiquetas;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionConteo;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionPegadoEtiqueta;
import com.automatica.AXCPT.c_Traspasos.Envio.SeleccionPartidaTraspasoEnvio;

import com.automatica.AXCPT.c_Traspasos.Envio.SurtidoTraspasoEmpaque;
import com.automatica.AXCPT.c_Traspasos.Envio.SurtidoTraspasoEmpaqueNE;
import com.automatica.AXCPT.c_Traspasos.MenuTraspaso;
import com.automatica.AXCPT.databinding.ActivityRecepcionPegadoEtiquetaBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionTraspasoPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class RecepcionTraspasoPallet extends AppCompatActivity implements  frgmnt_taskbar_AXC.interfazTaskbar {

    private ProgressBarHelper progressBarHelper;
    private Spinner sp_partidas;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(RecepcionTraspasoPallet.this);
    ActivityRecepcionTraspasoPalletBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Handler h = new Handler();
    boolean recargar;
    String transferencia, partida, orden;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            binding = ActivityRecepcionTraspasoPalletBinding.inflate(getLayoutInflater());

            setContentView(binding.getRoot());
            configuracionToolbar();
            configuracionTaskbar();
            declararVariables();
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
        binding.edtxConfirmar.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!binding.edtxConfirmar.getText().toString().equals(""))
                    {
                        if(binding.edtxPallet.getText().toString().equals(""))
                        {
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxPallet.setText("");
                                    binding.edtxPallet.requestFocus();
                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,"Ingrese un codigo de pallet" ,"false" ,true , true);
                            return false;
                        }
                        if(!binding.edtxPallet.getText().toString().equals(binding.edtxConfirmar.getText().toString()))
                        {
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxConfirmar.setText("");

                                    //    binding.edtxConfirmar.requestFocus();
                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,binding.edtxPallet ,"Los codigos de pallet no coinciden" ,"false" ,true , true);
                            return false;
                        }

                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,"Registrado con exito" ,"true" ,true , true);

                    }else
                    {
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                binding.edtxConfirmar.setText("");
                                binding.edtxConfirmar.requestFocus();
                            }
                        });
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,"false" ,true , true);
                    }
                    new esconderTeclado(RecepcionTraspasoPallet.this);
                    return true;
                }
                return false;
            }
        });




        binding.edtxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!binding.edtxPallet.getText().toString().equals(""))
                    {
                        new RecepcionTraspasoPallet.SegundoPlano("ConsultaPallet").execute();
                    }else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,"Ingrese un Codigo de Pallet" ,"false" ,true , true);

                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                binding.edtxPallet.setText("");
                                binding.edtxPallet.requestFocus();

                            }
                        });
                    }
                    new esconderTeclado(RecepcionTraspasoPallet.this);
                    return  true;
                }
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        if(!binding.edtxPallet.getText().toString().isEmpty()){
            new RecepcionTraspasoPallet.SegundoPlano("ConsultaPallet").execute();

        }
        new RecepcionTraspasoPallet.SegundoPlano("ConsultaPedidoSurtido").execute();
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

        if (progressBarHelper.ispBarActiva()) {
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

    private void declararVariables()
    {
        try
        {
            sp_partidas = findViewById(R.id.vw_spinner_partidas).findViewById(R.id.spinner);
            progressBarHelper = new ProgressBarHelper(this);
        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }
    }

    private void sacarDatosIntent(){
        try
        {
            Bundle b = getIntent().getExtras();
            orden = b.getString("Orden");
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
        View LastView;

        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(RecepcionTraspasoPallet.this);
        cAccesoADatos_Consultas cadcons = new cAccesoADatos_Consultas(RecepcionTraspasoPallet.this);



        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute() {

            try
            {
                progressBarHelper.ActivarProgressBar(tarea);
            }catch (Exception e)
            {
                e.printStackTrace();
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                switch (tarea) {

                    case"ConsultaPedidoSurtido":
                        dao = cad.cad_ListarPartidasTrasSpinner(partida);
                        break;

                    case "ConsultaPallet":
                        dao = cad.c_ConsultarPalletPT(partida,"1",binding.edtxPallet.getText().toString());
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

                        case"ConsultaPedidoSurtido":
                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_partidas.setAdapter(c = new CustomArrayAdapter(RecepcionTraspasoPallet.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("Partida","Partida")));

                                // sp_partidas.setSelection(0);
                                sp_partidas.setSelection(CustomArrayAdapter.getIndex(sp_partidas,partida));
                            }else
                            {
                                sp_partidas.setAdapter(null);
                            }
                            break;

                        case "ConsultaPallet":
                            binding.tvProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.tvPaquetes.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.tvCantidad.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.tvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteAXC"));
                            binding.tvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                            binding.edtxConfirmar.requestFocus();
                            break;

                        case "ConfirmaPallet":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.envio_pallet_registro_exito),dao.iscEstado(), true, true);
                            break;


                    }
                } else {
                    binding.edtxPallet.setText("");
                    binding.edtxPallet.requestFocus();
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,null, dao.getcMensaje(), false, true, true);

                }

                progressBarHelper.DesactivarProgressBar(tarea);
            } catch (Exception e)
            {
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), "false", true, true);
                e.printStackTrace();
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
        Intent intent = new Intent(RecepcionTraspasoPallet.this, SeleccionOrdenTraspasoRecepcion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }



}