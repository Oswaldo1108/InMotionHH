package com.automatica.AXCPT.c_Produccion.Produccion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Almacen_Armado_Pallets_Ne;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionConteo;
import com.automatica.AXCPT.databinding.ActivityAlmacenArmadoPalletsNeBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionConteoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Almacen_Armado_Pallets_NE extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar {

    Spinner spnr_Maquinas;
    String str_Maquina = "@";
    Toolbar toolbar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    String serial, orden;
    Context contexto = this;
    DatePickerFragment newFragment;
    String EmpaqueBaja;
    Handler handler = new Handler();
    int palletRegistradosVar = 0;
    TextView txtv_Producto, txtv_Cantidad, txtv_Lote, txtv_CantidadEmpaques, txtv_CantidadRegistrada,txtv_PalletAbierto;
    boolean recargar;
    private static final String frgmnt_tag = "tag";
    int cantidadEmpaques = 0;
    private ProgressBarHelper p;
    ActivityAlmacenArmadoPalletsNeBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    popUpGenerico pop = new popUpGenerico(Almacen_Armado_Pallets_NE.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenArmadoPalletsNeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new cambiaColorStatusBar(contexto, R.color.VerdeStd, Almacen_Armado_Pallets_NE.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        p = new ProgressBarHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Armado de tarimas");
        getSupportActionBar().setSubtitle("NE");
        agregaListeners();
        binding.edtxOrdenProduccion.requestFocus();
        spnr_Maquinas = (Spinner) findViewById(R.id.spnr_Maquinas).findViewById(R.id.spinner);
        String Doc= null;
        try {
            Doc = getIntent().getStringExtra("Documento");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Doc!=null){
            binding.edtxOrdenProduccion.setText(Doc);
        }
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



    // MÉTODOS DEL CICLO DE VIDA DE ACTIVITY
    @Override
    protected void onResume() {
    //    new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
        new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaMaquinas").execute();
    //    new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaTarima").execute();
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
    }

    // MÉTODOS PARA LA TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.cerrar_op_toolbar_cancelar, menu);
            return true;

        } catch (Exception e) {
            Toast.makeText(contexto, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (p.ispBarActiva())
//        {
//
//            int id = item.getItemId();
//            if ((id == R.id.InformacionDispositivo)) {
//
//                new sobreDispositivo(contexto, vista);
//            }
//            if (id == R.id.recargar) {
//                new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
//                new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaMaquina").execute();
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (p.ispBarActiva())
        {

            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo)) {

                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar) {

                if (!binding.edtxOrdenProduccion.getText().toString().equals(""))
                {
                    new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
                    new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaMaquina").execute();
                }
            }
            if ((id == R.id.CerrarOC))
            {

                if (!binding.edtxOrdenProduccion.getText().toString().equals(""))
                {
//

                    new CreaDialogos(getString(R.string.pregunta_cierre_orden), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id) {

                            new esconderTeclado(Almacen_Armado_Pallets_NE.this);
                        }
                    },null,contexto);
                }
            }

            if ((id == R.id.cancelar_pallets)) {
                cancelarPallet();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelarPallet() {
        if (binding.edtxOrdenProduccion.getText().toString().equals(""))
        {
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_ingrese_orden_produccion), "false", true, true);
            return;
        }


        if (binding.txtvCantidadEmpaques.getText().toString().equals(""))
        {
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_cerrar_pallet_sin_empaques), "false", true, true);
            return;
        }

        new CreaDialogos(getString(R.string.pregunta_cancelar_pallet), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                new Almacen_Armado_Pallets_NE.SegundoPlano("CancelarTarima").execute();
                new esconderTeclado(Almacen_Armado_Pallets_NE.this);
            }
        }, null,contexto);
    }

    @Override
    public void BotonDerecha() { validacionFinal();

    }

    private void validacionFinal() {
        if (binding.edtxOrdenProduccion.getText().toString().equals(""))
        {
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_ingrese_orden_produccion), "false", true, true);
            return;
        }


        if (binding.txtvCantidad.getText().toString().equals(""))
        {
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_cerrar_pallet_sin_empaques), "false", true, true);
            return;
        }
        new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Almacen_Armado_Pallets_NE.SegundoPlano("CerrarTarima").execute();
                new esconderTeclado(Almacen_Armado_Pallets_NE.this);
            }
        },null,Almacen_Armado_Pallets_NE.this);

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    // CLASE PARA LLAMAR A LOS WEBSERVICES
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_RegistroPT cad = new cAccesoADatos_RegistroPT(Almacen_Armado_Pallets_NE.this);
        cAccesoADatos_Consultas consAd = new cAccesoADatos_Consultas(Almacen_Armado_Pallets_NE.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            try {
                p.ActivarProgressBar(tarea);
            }catch(Exception e){
                e.printStackTrace();
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea) {
                    case "ConsultaOrdenProduccion":
                        dao = cad.c_ConsultaOrdenProduccion(binding.edtxOrdenProduccion.getText().toString());
                        break;

                    case "ConsultaMaquinas":
                        dao = consAd.c_ConsultaMaquinas(str_Maquina);
                        break;

                    case "ConsultaTarima":
                        dao = cad.c_ConsultaEmpaquesArmadoProd_NE(binding.edtxOrdenProduccion.getText().toString());
                        break;

                    case "RegistrarEmpaque":
                        dao = cad.c_CreaEmpaqueNE(binding.edtxOrdenProduccion.getText().toString(),binding.edtxEmpaque.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato(), "");
                        break;

                    case "CerrarTarima":
                        dao = cad.c_CerrarRegistroPallet(binding.edtxOrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato());
                        break;

                    case "CancelarTarima":
                        dao = cad.c_CancelarRegistroPallet(binding.edtxOrdenProduccion.getText().toString());
                        break;

                    default:
                        dao = new DataAccessObject();
                }

            } catch (Exception e)
            {
                dao = new DataAccessObject(e);

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
                        case "ConsultaOrdenProduccion":
                            binding.txtvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                            binding.txtvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTotal"));
                            binding.txtvProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Producto"));
                            binding.txtvCantidadReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRegistrada"));

                            if(!dao.getcMensaje().equals("@"))
                            {
                                str_Maquina = dao.getcMensaje();
                            }



                            break;


                        case "ConsultaMaquinas":


                            spnr_Maquinas.setAdapter(new CustomArrayAdapter(Almacen_Armado_Pallets_NE.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("DLinea","Linea")));

                            break;

                        case "RegistrarEmpaque":
                            new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
                            str_Maquina = ((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato();// agarro la maquina del ultimo registro correcto
                            binding.txtvCantidadEmpaques.setText(dao.getcMensaje());
                            binding.txtvPalletAbierto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            binding.edtxEmpaque.setText("");

                            break;

                        case "ConsultaTarima":
                            binding.txtvPalletAbierto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            binding.txtvCantidadEmpaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            break;
                        case "CerrarTarima":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + dao.getcMensaje() + "] con éxito.", dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
                            binding.edtxEmpaque.setText("");
                            binding.txtvCantidadEmpaques.setText("");
                            binding.txtvPalletAbierto.setText("");

                            break;

                        case "RegistrarCaducidad":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.fecha_cad_reg_exito), dao.iscEstado(), true, true);

                            new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
                            break;

                        case "CancelarTarima":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.cancelar_tarima), dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
                            binding.edtxEmpaque.setText("");
                            binding.txtvCantidadEmpaques.setText("");
                            binding.txtvPalletAbierto.setText("");
                            break;

                        case "CerrarOrden":

                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_prod_cerrar_exito), dao.iscEstado(), true, true);

                            break;

                        case "BajaEmpaque":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
//                                new SegundoPlano("ConsultaTarima").execute();
                            EmpaqueBaja = null;
                            break;

                    }
                } else
                {
                    ReiniciarVariables(tarea);
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                }
            } catch (Exception e) {
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);
                e.printStackTrace();
            }
             p.DesactivarProgressBar(tarea);
        }
    }

    // MÉTODOS ADICIONALES
    private void agregaListeners() {
        binding.edtxOrdenProduccion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction()==KeyEvent.ACTION_DOWN) && (i==KeyEvent.KEYCODE_ENTER)){
                    if(!binding.edtxOrdenProduccion.getText().toString().equals(""))
                    {

                        if(binding.edtxOrdenProduccion.getText().toString().startsWith("(D)")){
                            orden = binding.edtxOrdenProduccion.getText().toString().split("\\|")[0];
                            serial = binding.edtxOrdenProduccion.getText().toString().split("\\|")[1];
                            if(orden.contains("(D)")){
                                //binding.edtxEmpaque.requestFocus();
                                orden = orden.replace("(D)", "");
                                serial = serial.replace("(S)", "");
                                binding.edtxOrdenProduccion.setText(orden);
                               // binding.edtxNumSerie.setText(serial);
                                new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();
                                new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaMaquinas").execute();
                                new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaTarima").execute();
                                binding.edtxEmpaque.requestFocus();

                            }
                            else{
                                binding.edtxEmpaque.requestFocus();
                            }
                            binding.edtxEmpaque.requestFocus();
                        }else
                        {
                            new Almacen_Armado_Pallets_NE.SegundoPlano("ConsultaOrdenProduccion").execute();;
                        }


                    }else
                    {

                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_orden_produccion) ,false ,true , true);
                    }
                    new esconderTeclado(Almacen_Armado_Pallets_NE.this);
                    return true;
                }

                return false;
            }
        });



        binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {


                        if ( binding.edtxOrdenProduccion.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxEmpaque.setText("");
                                    binding.edtxEmpaque.requestFocus();
                                }
                            });
                        }

                        if ( binding.edtxEmpaque.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.edtxEmpaque.setText("");
                                    binding.edtxEmpaque.requestFocus();
                                }
                            });
                        }


                        new Almacen_Armado_Pallets_NE.SegundoPlano("RegistrarEmpaque").execute();
                        //txtv_CantidadEmpaques.setText("");

                        new esconderTeclado(Almacen_Armado_Pallets_NE.this);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
                }
                return false;
            }
        });

        binding.btnCerrarTarima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (binding.edtxOrdenProduccion.getText().toString().equals(""))
                {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                    return;
                }


                if (binding.txtvCantidad.getText().toString().equals(""))
                {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_cerrar_pallet_sin_empaques), "false", true, true);
                    return;
                }
                new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Almacen_Armado_Pallets_NE.SegundoPlano("CerrarTarima").execute();
                        new esconderTeclado(Almacen_Armado_Pallets_NE.this);
                    }
                },null,Almacen_Armado_Pallets_NE.this);


            }
        });

        binding.btnCancelarTarima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.edtxOrdenProduccion.getText().toString().equals(""))
                {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                    return;
                }


                if (binding.txtvCantidadEmpaques.getText().toString().equals(""))
                {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,getString(R.string.error_cerrar_pallet_sin_empaques), "false", true, true);
                    return;
                }

                new CreaDialogos(getString(R.string.pregunta_cancelar_pallet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new Almacen_Armado_Pallets_NE.SegundoPlano("CancelarTarima").execute();
                        new esconderTeclado(Almacen_Armado_Pallets_NE.this);
                    }
                }, null,contexto);
            }
        });

    }

    private void ReiniciarVariables(String tarea)
    {
        try {
            switch (tarea) {
                case "ConsultaOrdenProduccion":
                    binding.edtxOrdenProduccion.setText("");
                    binding.txtvProducto.setText("");
                    binding.txtvLote.setText("");
                    binding.txtvCantidad.setText("");
//                    edtx_FechaCaducidad.setText("");
                    //           tabla.getDataAdapter().clear();
                    binding.txtvCantidadReg.setText("");
                   // binding.edtxOrdenProduccion.requestFocus();
                    break;

                case "ConsultaTarima":

                    binding.edtxEmpaque.setText("");
                    binding.txtvCantidadEmpaques.setText("-");
                   // binding.edtxEmpaque.requestFocus();
                    break;

                case "RegistrarEmpaque":

                    binding.edtxEmpaque.setText("");
                    binding.edtxEmpaque.requestFocus();
                    break;

                case "Reiniciar":
                    binding.edtxOrdenProduccion.setText("");
                    binding.txtvProducto.setText("");
                    binding.txtvLote.setText("");
                    binding.txtvCantidad.setText("");
                    binding.txtvCantidadReg.setText("");
                    binding.txtvCantidadEmpaques.setText("-");
//                    edtx_FechaCaducidad.setText("");
                   // binding.edtxOrdenProduccion.requestFocus();
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }
    }

}