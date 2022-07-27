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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Recepcion_Registro_Transferencias_Por_Empaque;
import com.automatica.AXCPT.c_Produccion.Produccion.Almacen_Armado_Pallets;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionPalletNe;
import com.automatica.AXCPT.c_Traspasos.MenuTraspaso;
import com.automatica.AXCPT.databinding.ActivityRecepcionEmpaquesBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionTraspasoEmpaqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class RecepcionTraspasoEmpaque extends AppCompatActivity implements  frgmnt_taskbar_AXC.interfazTaskbar {

    popUpGenerico pop = new popUpGenerico(RecepcionTraspasoEmpaque.this);
    View vista;
    Context contexto = this;
    String NumSerie;
    ActivityRecepcionTraspasoEmpaqueBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    private ProgressBarHelper p;
    String TAG = "SoapResponse";
    String Cantidad;
    Bundle b = new Bundle();
    String documento, partida, articulo;
    String OrdenCompra, PartidaERP, NumParte, UM, CantidadTotal, CantidadRecibida, CantidadEmpaques, EmpaquesPallet,SKU;
    boolean recargar;
    Handler h = new Handler();
    private Spinner sp_Partidas;
    private Spinner sp_NumSerie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            binding = ActivityRecepcionTraspasoEmpaqueBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            configuracionToolbar();
            configuracionTaskbar();
            sacarDatosIntent();
            declararVariables();
            declararListener();

            h.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    binding.edtxSKU.requestFocus();

                }
            },150);

        } catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista, e.getMessage(), false);
        }

    }

    private void declararListener() {


        binding.edtxCantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus && binding.edtxCantidad.getText().toString().equals("0")) {
                    try {
                        binding.edtxCantidad.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }

            }
        });

        binding.edtxEmpxPallet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus &&  binding.edtxEmpxPallet.getText().toString().equals("0")) {
                    try {
                        binding.edtxEmpxPallet.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
            }
        });

        binding.edtxCantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        if (! binding.edtxCantidad.getText().toString().equals("")) {
                            try {
                                if (!(Float.parseFloat( binding.edtxCantidad.getText().toString()) > 999999)) {
                                    binding.edtxEmpxPallet.requestFocus();
                                } else {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            h.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    binding.edtxCantidad.requestFocus();
                                                    binding.edtxCantidad.setText("");
                                                }
                                            });
                                        }
                                    });
                                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);

                                }
                            } catch (NumberFormatException ex) {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.edtxCantidad.setText("");
                                        binding.edtxCantidad.requestFocus();

                                    }
                                });
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                            }
                        } else {

                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxCantidad.setText("");
                                    binding.edtxCantidad.requestFocus();

                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "Advertencia", true, true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                }
                return false;
            }
        });

        binding.edtxEmpxPallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        if (! binding.edtxEmpxPallet.getText().toString().equals("")) {

                            try {
                                if (!(Integer.parseInt( binding.edtxEmpxPallet.getText().toString()) > 999999)) {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.edtxEmpxPallet.requestFocus();
                                        }
                                    });
                                } else {
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.edtxEmpxPallet.setText("");
                                            binding.edtxEmpxPallet.requestFocus();
                                        }
                                    });
                                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "Advertencia", true, true);
                                }
                            } catch (NumberFormatException ex) {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.edtxEmpxPallet.setText("");
                                        binding.edtxEmpxPallet.requestFocus();
                                    }
                                });
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                            }
                        } else {
                            binding.edtxEmpxPallet.setText("");
                            binding.edtxEmpxPallet.requestFocus();
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "Advertencia", true, true);
                        }
                        //   edtx_EmpxPallet.requestFocus();
                    } catch (Exception e) {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        if (binding.tvPallet.getText().toString().equals("")) {
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxEmpaque.setText("");
                                    binding.edtxEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese candidad de empaques", "false", true, true);
                            return false;
                        }

                        if (Integer.parseInt(binding.edtxEmpxPallet.getText().toString()) <= 0) {
                            new popUpGenerico(contexto, getCurrentFocus(), "La cantidad ingresada no puede ser menor o igual a cero.", false, true, true);
                            return false;
                        }
                        if (Float.parseFloat(binding.edtxCantidad.getText().toString()) <= 0) {
                            new popUpGenerico(contexto, getCurrentFocus(), "La cantidad de empaques no puede ser menor o igual a cero.", false, true, true);
                            return false;
                        }


                        if (binding.edtxCantidad.getText().toString().equals("")) {
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxEmpaque.setText("");
                                    binding.edtxEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "false", true, true);
                            return false;
                        }
                        if (binding.edtxEmpaque.getText().toString().equals("")) {
                            new popUpGenerico(RecepcionTraspasoEmpaque.this, binding.edtxEmpaque, "Código de empaque vacio", false, true, true);
                            return false;
                        }
                        new RecepcionTraspasoEmpaque.SegundoPlano("RegistrarEmpaqueNuevo").execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                        binding.edtxNumSerie.setText("");
                        binding.edtxEmpaque.setText("");
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        sp_Partidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                binding.tvPartida.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag1());
                //txtv_Producto.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag2());
                binding.tvCantTotal.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag3());


                Log.i("Tag1",((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag1());
//                if(!edtx_SKU.getText().toString().equals(((Constructor_Dato) sp_Partidas.getSelectedItem()).getDato()))
//                {
                new RecepcionTraspasoEmpaque.SegundoPlano("DetallePartida").execute();
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    @Override
    protected void onResume() {
        if (!binding.tvDocumento.getText().toString().equals("-"))
            new RecepcionTraspasoEmpaque.SegundoPlano("ConsultaPallet").execute();
      //  new RecepcionTraspasoEmpaque.SegundoPlano("Tabla").execute();
        super.onResume();
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
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


    // Métodos para la configuración inicial
    private void declararVariables() {
        p = new ProgressBarHelper(this);
        sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);
     //   sp_NumSerie = binding.vwSpinner2.findViewById(R.id.spinner);
    }

    private void configuracionTaskbar() {
        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();
    }

    private void sacarDatosIntent() {
        try {
            b = getIntent().getExtras();
            documento = b.getString("Orden");
            partida = b.getString("Partida");
            articulo = b.getString("Producto");

            binding.tvDocumento.setText(documento);
            binding.tvPartida.setText(partida);
            binding.tvArticulo.setText(articulo);

        } catch (Exception e) {
            e.printStackTrace();

        }

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

    @Override
    public void BotonDerecha() {
        validacionFinal();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    private void validacionFinal(){
        try {

            new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) {
                    if (!binding.tvEmpReg.getText().toString().equals("")&&!binding.tvEmpReg.getText().toString().equals("-"))
                    {
                        if (Integer.parseInt(binding.tvEmpReg.getText().toString()) > 0)
                        {
                            new RecepcionTraspasoEmpaque.SegundoPlano("RegistraPalletNuevo").execute();
                        } else
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                        }
                    } else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                    }
                }
            },null,contexto);


        } catch (Exception e) {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(RecepcionTraspasoEmpaque.this);

        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        View LastView;

        @Override
        protected void onPreExecute() {

           p.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                switch (tarea) {

                    case "Tabla":
                        dao = cad.c_ListarPartidasTransEnProceso(documento);
                        break;

                    case "ConsultaPallet":
                        dao = cad.cad_ConsultaPalletAbiertoTraspaso(documento, partida);
                        break;

                    case "RegistrarEmpaqueNuevo":

                        dao = cad.cad_RegistraEmpaqueTraspaso(binding.tvDocumento.getText().toString(),
                                partida,
                                binding.edtxEmpaque.getText().toString(),
                                binding.edtxCantidad.getText().toString(),
                                binding.edtxEmpxPallet.getText().toString(),
                                binding.edtxNumSerie.getText().toString());

                        break;
                    case "RegistraPalletNuevo":
                        dao = cad.cad_OCCierraPalletTraspaso(binding.tvPallet.getText().toString());

                        break;

                    case "Almacenes":
                        dao = cad.c_ListaMercados();
                        break;

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
                        case "Tabla":
                            if(dao.getcTablas() != null){
                                CustomArrayAdapter c;
//                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(RecepcionTraspasoEmpaque.this,
//                                        android.R.layout.simple_spinner_item,
//                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }
                            int SKUSel = -2;

                        break;

                        case "RegistrarEmpaqueNuevo":

                            binding.tvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            binding.tvEmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            binding.tvCantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            binding.edtxEmpaque.setText("");
                            binding.edtxEmpaque.requestFocus();

                            new esconderTeclado(RecepcionTraspasoEmpaque.this);

                            if ( dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, null, getString(R.string.traspaso_completado), dao.iscEstado(), true, true);
                                new RecepcionTraspasoEmpaque.SegundoPlano("RegistraPalletNuevo").execute();

                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, null, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new RecepcionTraspasoEmpaque.SegundoPlano("RegistraPalletNuevo").execute();

                            }
                            else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                            {
                                new RecepcionTraspasoEmpaque.SegundoPlano("RegistraPalletNuevo").execute();
                            }

                            break;


                        case "ConsultaPallet":
                            binding.tvEmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.tvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Pallet"));
                            binding.tvCantTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTot"));
                            binding.tvCantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadReg"));

                            break;

                        case "RegistraPalletNuevo":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, null,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            binding.edtxEmpaque.setText("");
                            binding.edtxNumSerie.setText("");
                            binding.edtxEmpxPallet.setText("");
                            binding.tvEmpReg.setText("-");
                            binding.tvPallet.setText("-");
                            new RecepcionTraspasoEmpaque.SegundoPlano("ConsultaPallet").execute();
                            break;

                        case "Almacenes":
                           // spnr_Almacen.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Mercado","IdMercado")));
                            break;

                    }
                } else {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                }
                p.DesactivarProgressBar(tarea);
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage() , "false", true, true);
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
        Intent intent = new Intent(RecepcionTraspasoEmpaque.this, SeleccionPartidaTraspasoRecepcion.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}