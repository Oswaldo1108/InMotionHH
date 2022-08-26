package com.automatica.AXCPT.c_Traspasos.Envio;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivitySurtidoTraspasoPalletCompletoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

public class SurtidoTraspasoPalletCompleto extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar {

    private Toolbar toolbar;
    private ProgressBarHelper p;
    private Context contexto = this;
    private String Pedido, Partida, NumParte, CantidadPendiente, CantidadSurtida;
    private SortableTableView tabla;
    private TableViewDataConfigurator ConfigTabla = null;
    private Handler handler = new Handler();
    private Bundle b;
    private Spinner sp_partidas;
    private String TipoReg = null;
    private ActivityHelpers activityHelpers;
    frgmnt_taskbar_AXC taskbar_axc;


    ActivitySurtidoTraspasoPalletCompletoBinding binding;

    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySurtidoTraspasoPalletCompletoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sacarDatosIntent();
        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
      //  activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
         //   getMenuInflater().inflate(R.menu.change_view_toolbar, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume() {
      //  Recarga();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
        return super.onOptionsItemSelected(item);
    }

    // ************************************************ MÉTODOS IMPLEMENTADOS **********************************************

    @Override
    public void BotonDerecha() {

    }
    @Override
    public void BotonIzquierda() {

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

    // ******************************************************** MÉTODOS CREADOS *********************************************************

    private void Recarga()
    {

    }

    private void sacarDatosIntent(){
        try
        {

        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }
    }

    private void declararVariables() {

    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Surtir traspaso");
        getSupportActionBar().setSubtitle("Empaque NE");
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

    private void agregarListener() {
        try{

        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, null, e.getMessage(), false, true, true);
        }
    }

    private void validacionFinal() {

    }

    // ********************************************************* OnBackPressed **********************************************************
    public void onBackPressed() {
        if (!taskbar_axc.toggle()) {
            return;
        } else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta") != null) {
            if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta") != null) {
                taskbar_axc.cerrarFragmento();
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        Intent intent = new Intent(SurtidoTraspasoPalletCompleto.this, SeleccionPartidaTraspasoEnvio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close, R.anim.slide_left_out_close);
    }

    // ********************************************************* SEGUNDO PLANO *********************************************************
    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_RegistroPT cadEmb = new cAccesoADatos_RegistroPT(SurtidoTraspasoPalletCompleto.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try
            {
                p.ActivarProgressBar(tarea);
            }catch (Exception e)
            {
                e.printStackTrace();
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {

                switch (tarea)
                {
                    case"ConsultaPedidoSurtido":
                        dao = cadEmb.cad_ListarPartidasProdSpinner(Pedido);
                        break;
                    case "ConsultaPedidoDet":

                        if(params[0]!="@")
                        {
                            Partida = params[0];
                        }

                        dao = cadEmb.cad_ConsultaSurtidoProdDetPartida(Pedido,Partida);
                        break;

                    case "SugierePallet":
                        dao = cadEmb.cad_ConsultaPalletSugeridoProd(Pedido,Partida);
                        break;

                    case "ConsultaPallet":
                        dao = cadEmb.cad_ConsultaPalletSurtidoProd(Pedido,Partida,binding.edtxEmpaque.getText().toString());
                        break;

                    case "RegistrarPallet":
                      //  dao = cadEmb.cad_RegistroPalletSurtidoProd(Pedido,Partida,binding.edtxCantidad.getText().toString());
                        break;
                    case "RegistrarPalletMultPart":
                        dao = cadEmb.cad_RegistroPalletSurtidoProdMultPart(Pedido,binding.edtxCantidad.getText().toString());
                        break;
                    default:
                        dao = new DataAccessObject();
                }

            }catch (Exception e)
            {
                dao = new DataAccessObject(e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {
                if (dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case"ConsultaPedidoSurtido":

                            if(dao.getcTablas() != null)
                            {
                                sp_partidas.setAdapter(new CustomArrayAdapter(SurtidoTraspasoPalletCompleto.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("Partida","Partida")));

                                sp_partidas.setSelection(0);
                                //sp_partidas.setSelection(CustomArrayAdapter.getIndex(sp_partidas,Partida));
                            }else
                            {
                                sp_partidas.setAdapter(null);
                            }
                            break;
                        case "ConsultaPedidoDet":
                            binding.tvCantidadReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));
                            binding.tvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                            binding.tvProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            new SurtidoTraspasoPalletCompleto.SegundoPlano("SugierePallet").execute();
                            break;

                        case "SugierePallet":

                            binding.txtvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            binding.txtvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                            binding.txtvPosicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                            binding.txtvSugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.txtvTipoPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("TipoReg"));


                            binding.edtxEmpaque.requestFocus();
                            break;

                        case "ConsultaPallet":
                            if(dao.getcTablaUnica()!=null)
                            {
                                if(ConfigTabla == null)
                                {
                                    ConfigTabla =  new TableViewDataConfigurator(tabla, dao, SurtidoTraspasoPalletCompleto.this);
                                }else
                                {
                                    ConfigTabla.CargarDatosTabla(dao);
                                }
                            }
                            else
                            {
                                tabla.getDataAdapter().clear();
                                tabla.getDataAdapter().notifyDataSetChanged();
                            }
                            TipoReg = dao.getcMensaje();// MultiplesPartidas o PartidaUnica
                            binding.edtxCantidad.requestFocus();
                            break;

                        case "RegistrarPalletMultPart":
                        case "RegistrarPallet":
                         //   ReiniciarVariables(tarea);
                            new SurtidoTraspasoPalletCompleto.SegundoPlano("ConsultaPedidoDet").execute("@");

                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxEmpaque.requestFocus();
                                }
                            },10);

                            break;
                    }
                }
                else
                {
                   // ReiniciarVariables(tarea);
                    if(dao.getcMensaje().contains("partida no válido"))
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), "Partida completada con exíto.", true, true, true);
                        new SurtidoTraspasoPalletCompleto.SegundoPlano("ConsultaPedidoSurtido").execute();
                    }
                    else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), false, true, true);

                    }
                }
            }catch (Exception e)
            {
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), false, true, true);
                e.printStackTrace();
            }
            p.DesactivarProgressBar(tarea);
        }
    }


}