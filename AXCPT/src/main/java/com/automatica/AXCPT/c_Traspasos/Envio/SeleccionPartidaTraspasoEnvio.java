package com.automatica.AXCPT.c_Traspasos.Envio;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdEmpaque;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdEmpaque_NE;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdPallet;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdPiezas;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdSeleccionOrden;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdSeleccionPartida;
import com.automatica.AXCPT.databinding.ActivitySeleccionPartidaTraspasoEnvioBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class SeleccionPartidaTraspasoEnvio extends AppCompatActivity  implements  TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar{

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    private Handler h = new Handler();
    popUpGenerico pop = new popUpGenerico(SeleccionPartidaTraspasoEnvio.this);
    ActivitySeleccionPartidaTraspasoEnvioBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    private String strCarritoActual = "";
    boolean DocumentoSeleccionado;
    private String tipoSurtido;
    private boolean usarCarrito = false;
    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeleccionPartidaTraspasoEnvioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new esconderTeclado(this);
        sacarDatosIntent();
        declararVariables();
        configurarToolbar();
        configurarTaskbar();
        agregarListener();
    }

    @Override
    protected void onResume() {
        if (!binding.edtxDocumento.getText().toString().equals(""))
            new SeleccionPartidaTraspasoEnvio.SegundoPlano("LlenarTabla").execute();

        if (!binding.edtxCarrito.getText().toString().equals(""))
            new SeleccionPartidaTraspasoEnvio.SegundoPlano("ConsultaCarrito").execute();

        super.onResume();


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.SELECCIONAR);
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
                new SeleccionPartidaTraspasoEnvio.SegundoPlano("LlenarTabla").execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // ************************************************ MÉTODOS IMPLEMENTADOS **********************************************

    @Override
    public void BotonDerecha() {validacionFinal(); }

    @Override
    public void BotonIzquierda() {onBackPressed();}

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {
        Toast.makeText(contexto, Header, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {
        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);
    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {
        DocumentoSeleccionado = true;
        b.putString("Pedido", binding.edtxDocumento.getText().toString());
        b.putString("Partida", clickedData[0]);
        b.putString("NumParte", clickedData[1]);
        b.putString("UM", clickedData[2]);
        b.putString("CantidadTotal", clickedData[3]);
        b.putString("CantidadPendiente", clickedData[4]);
        b.putString("CantidadSurtida", clickedData[5]);
        b.putString("Linea", clickedData[6]);
    }


    // ******************************************************** MÉTODOS CREADOS *********************************************************

    private void sacarDatosIntent() {
        try {
            b = getIntent().getExtras();
            Log.e("Documento", b.getString("Documento"));
            if (!b.getString("Documento").equals("")){
                binding.edtxDocumento.setText(b.getString("Documento"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void declararVariables() {
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Selección Partida");
        getSupportActionBar().setSubtitle("Traspaso");
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
        try {
            binding.edtxDocumento.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (binding.edtxDocumento.getText().toString().equals("")) {
                            new popUpGenerico(contexto, binding.edtxDocumento, getString(R.string.error_ingrese_pedido), "false", true, true);
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxDocumento.requestFocus();
                                    binding.edtxDocumento.setText("");
                                }
                            });

                            return false;
                        }


                        new SeleccionPartidaTraspasoEnvio.SegundoPlano("LlenarTabla").execute();

                        new esconderTeclado(SeleccionPartidaTraspasoEnvio.this);

                    }
                    return false;
                }
            });

            binding.edtxCarrito.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    binding.edtxCarrito.setText("");
                    binding.tvEmpaques.setText("");
                    binding.tvDocActual.setText("");
                    binding.tvEstatus.setText("");
                    return false;
                }
            });

            binding.edtxCarrito.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(binding.edtxCarrito.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,binding.edtxCarrito ,getString(R.string.error_ingrese_ubicacion) ,"false" , true, true);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxCarrito.requestFocus();
                                    binding.edtxCarrito.setText("");
                                }
                            });

                            return false;
                        }


                        new SeleccionPartidaTraspasoEnvio.SegundoPlano("ConsultaCarrito").execute();

                        new esconderTeclado(SeleccionPartidaTraspasoEnvio.this);

                    }
                    return false;
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,null ,e.getMessage() ,false , true, true);
        }
    }

    private void validacionFinal(){

//        if (binding.edtxCarrito.getText().toString().equals("")){
//            new popUpGenerico(contexto,binding.edtxCarrito ,"Para surtir debe de ingresar un código de carrito" , false, true,true );
//            return;
//        }

        Log.e("Carrito", binding.edtxCarrito.getText().toString());
        if(binding.edtxDocumento.getText().toString().equals(""))
        {
            new popUpGenerico(contexto,binding.edtxDocumento,getString(R.string.error_seleccionar_registro),false,true,false);
            // reiniciarDatos();
            return;
        }
        if(ConfigTabla_Totales==null)
        {
            new popUpGenerico(contexto,null ,getString(R.string.error_seleccionar_registro) , false, true,true );
            return;
        }

        if(!ConfigTabla_Totales.renglonEstaSelec())
        {
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,null ,getString(R.string.error_seleccionar_registro) , false, true,true );
            return;
        }




        new PopUpMenuAXC().newInstance(taskbar_axc.getView().findViewById(R.id.BotonDer),
                contexto, R.menu.popup_surtido,
                new PopUpMenuAXC.ContextMenuListener()
                {
                    @Override
                    public void listenerItem(MenuItem item)
                    {

                        String strCarritoSel = binding.edtxCarrito.getText().toString();

                        if(Constructor_Dato.getValue(ConfigTabla_Totales.getRenglonSeleccionado(), "Tipo").getDato().equals("BIK"))
                        {
                            strCarritoSel = "";
                            binding.edtxCarrito.setText("");
                            binding.tvEmpaques.setText("");
                            binding.tvEstatus.setText("");
                            binding.tvDocActual.setText("");
                        }else
                        {
                            if(!strCarritoActual.equals(binding.edtxCarrito.getText().toString()))
                            {
                                new popUpGenerico(contexto,binding.edtxCarrito ,"Consulte el carrito de nuevo." , false, true,true );
                                return;
                            }
                        }

                        Intent intent;

                        switch (item.getItemId())
                        {
                            case R.id.Empaque:
                                if (tipoSurtido.equals("PICKING") || tipoSurtido.equals("TODO")) {
                                    intent = new Intent(contexto, SurtidoTraspasoEmpaque.class);
                                    usarCarrito = true;
                                }else{
                                    new popUpGenerico(contexto,binding.edtxCarrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                                    return;
                                }

                                break;
                            case R.id.EmpaqueNE:
                                if (tipoSurtido.equals("PICKING") || tipoSurtido.equals("TODO")) {
                                    intent = new Intent(contexto, SurtidoTraspasoEmpaqueNE.class);
                                    usarCarrito = true;
                                }else{
                                    new popUpGenerico(contexto,binding.edtxCarrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                                    return;
                                }
                                break;
                            case R.id.Piezas:
                                if (tipoSurtido.equals("PICKING") || tipoSurtido.equals("TODO")) {
                                    intent = new Intent(contexto, SurtidoTraspasoPiezas.class);
                                    usarCarrito = true;
                                }else{
                                    new popUpGenerico(contexto,binding.edtxCarrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                                    return;
                                }
                                break;
                            case R.id.PalletCompleto:
                                if (tipoSurtido.equals("PALLET") || tipoSurtido.equals("TODO")) {
                                    intent = new Intent(contexto, SurtidoTraspasoPallet.class);
                                    usarCarrito = false;
                                }else{
                                    new popUpGenerico(contexto,binding.edtxCarrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                                    return;
                                }
                                break;
                            default:
                                intent = new Intent();
                        }
                        if (usarCarrito) {
                            if (binding.edtxCarrito.getText().toString().equals("")) {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, binding.edtxCarrito, "Para surtir empaques/piezas debe de ingresar un código de carrito.", false, true, true);
                                return;
                            }
                        }


                        b.putString("Carrito", strCarritoSel);
                        intent.putExtras(b);
                        overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                        startActivity(intent);
                    }
                });

    }

    // ********************************************************* OnBackPressed **********************************************************
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
        Intent intent = new Intent(SeleccionPartidaTraspasoEnvio.this, SeleccionarOrdenesTraspasoEnvio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    // ********************************************************* SEGUNDO PLANO *********************************************************
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(SeleccionPartidaTraspasoEnvio.this);

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
                    case "LlenarTabla":
                        dao = ca.c_ListarPartidasEnvioTraspaso(binding.edtxDocumento.getText().toString());
                        break;

                    case"ConsultaCarrito":
                        dao = ca.c_ConsultaCarritoSurtidoTras(binding.edtxDocumento.getText().toString() ,binding.edtxCarrito.getText().toString());
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
                        case "LlenarTabla":
                            if (ConfigTabla_Totales == null) {
                                ConfigTabla_Totales =  new TableViewDataConfigurator( 9, "SURTIDA","LIBERADA TOTAL","10",tabla, dao,SeleccionPartidaTraspasoEnvio.this);
                                Log.e("Tablas",dao.getSoapObject().toString());
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }
                            binding.edtxCarrito.requestFocus();
                            new esconderTeclado(SeleccionPartidaTraspasoEnvio.this);
                            tipoSurtido = dao.getcMensaje();
                            tipoSurtido = "TODO";
                            Log.e("TipoSurtido", tipoSurtido);

                            break;

                        case"ConsultaCarrito":
                            strCarritoActual = dao.getcMensaje();
                            binding.tvEmpaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Paquetes"));
                            binding.tvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Estatus"));
                            binding.tvDocActual.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Documento"));
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