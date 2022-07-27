package com.automatica.AXCPT.c_Traspasos.Recibe;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Recepcion_Registro_Transferencia_PrimeraYUltima;
import com.automatica.AXCPT.c_Recepcion.Recepcion.PrimerayUltima;
import com.automatica.AXCPT.databinding.ActivityPrimerayUltimaBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionTraspasoEmpaqueBinding;
import com.automatica.AXCPT.databinding.ActivityRecepcionTraspasoPyUBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class RecepcionTraspasoPyU extends AppCompatActivity implements  frgmnt_taskbar_AXC.interfazTaskbar{

    popUpGenerico pop = new popUpGenerico(RecepcionTraspasoPyU.this);
    ActivityRecepcionTraspasoPyUBinding binding;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    frgmnt_taskbar_AXC taskbar_axc;
    Bundle b = new Bundle();
    String documento, partida, articulo;
    private ProgressBarHelper p;
    int registroAnteriorSpinner=0;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            binding = ActivityRecepcionTraspasoPyUBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            configuracionToolbar();
            configuracionTaskbar();
            declararVariables();
            sacarDatosIntent();
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
        binding.edtxEmpaque.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {

                try
                {
                    if(hasFocus&& binding.edtxEmpaque.getText().toString().equals("0"))
                    {
                        try
                        {
                            binding.edtxEmpaque.setText("");
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }
        });
        binding.edtxEmpxPallet.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus&& binding.edtxEmpxPallet.getText().toString().equals("0"))
                {
                    try
                    {
                        binding.edtxEmpxPallet.setText("");
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
            }
        });
        binding.edtxEmpxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (! binding.edtxEmpxPallet.getText().toString().equals(""))
                        {


                            try {
                                if (Integer.parseInt( binding.edtxEmpxPallet.getText().toString()) > 999999)
                                {
                                    binding.edtxEmpxPallet.setText("");
                                    binding.edtxEmpxPallet.requestFocus();
                                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                } else {

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.edtxPrimerEmpaque.requestFocus();
                                        }
                                    });
                                }
                            }catch (NumberFormatException ex)
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_cantidad_valida),"false",true,true);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.edtxEmpxPallet.setText("");
                                        binding.edtxEmpxPallet.requestFocus();
                                    }
                                });
                            }
                        }else
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"false", true, true);


                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxEmpxPallet.requestFocus();
                                    binding.edtxEmpxPallet.setText("");
                                }
                            });

                        }

                        //edtx_EmpxPallet.requestFocus();
                        // new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_Spinner.this);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });
        binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {

                        if (! binding.edtxEmpaque.getText().toString().equals("")) {
                            try {
                                if (Float.parseFloat( binding.edtxEmpaque.getText().toString()) > 999999) {

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.edtxEmpaque.setText("");
                                            binding.edtxEmpaque.requestFocus();
                                        }
                                    });

                                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.edtxEmpxPallet.requestFocus();
                                        }
                                    });

                                }
                            } catch (NumberFormatException ex) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.edtxEmpaque.setText("");
                                        binding.edtxEmpaque.requestFocus();

                                    }
                                });
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                            }
                        }else
                        {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxEmpaque.setText("");
                                    binding.edtxEmpaque.requestFocus();
                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_cantidad_valida),"false", true, true);

                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                }
                return false;
            }
        });
        binding.edtxPrimerEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (!binding.edtxPrimerEmpaque.getText().toString().equals(""))
                        {
                        }else
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxPrimerEmpaque.setText("");
                                    binding.edtxPrimerEmpaque.requestFocus();
                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, binding.edtxPrimerEmpaque, getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(RecepcionTraspasoPyU.this);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        binding.edtxUltimoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (!binding.edtxUltimoEmpaque.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxCantidadEmpaques.requestFocus();

                                }
                            });



                        }else
                        {


                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxUltimoEmpaque.setText("");
                                    binding.edtxUltimoEmpaque.requestFocus();
                                }
                            });
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,binding.edtxUltimoEmpaque, getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(RecepcionTraspasoPyU.this);

                    }catch (Exception e) {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });




        binding.edtxCantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {


                        if(binding.edtxEmpaque.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad)+" - Cantidad", "false", true, true);
                            return true;
                        }
                        if(binding.edtxEmpxPallet.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Paquetespor Pallet", "false", true, true);
                            return true;
                        }
                        if(binding.edtxPrimerEmpaque.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Primer empaque", "false", true, true);
                            return true;
                        }
                        if(binding.edtxUltimoEmpaque.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Ultimo empaque", "false", true, true);
                            return true;
                        }
                        if(binding.edtxCantidadEmpaques.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Validación " +  " cantidad empaques", "false", true, true);
                            return true;
                        }
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                binding.edtxCantidadEmpaques.requestFocus();

                            }
                        });

                        new RecepcionTraspasoPyU.SegundoPlano("RegistrarEmpaqueNuevo").execute();

                        new esconderTeclado(RecepcionTraspasoPyU.this);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,binding.edtxCantidadEmpaques, e.getMessage(), "false", true, true);
                    }
                }

                return false;
            }
        });

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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {
            }
        }catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
    }

    // Métodos para la configuración inicial
    private void declararVariables() {
      //  sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);
       // sp_NumSerie = binding.vwSpinner2.findViewById(R.id.spinner);
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
            binding.txtvPartida.setText(partida);
            binding.txtvProd.setText(articulo);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void configuracionToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recepción traspaso");
        getSupportActionBar().setSubtitle("Primera y Última");
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

    }

    @Override
    public void BotonIzquierda() {

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
        Intent intent = new Intent(RecepcionTraspasoPyU.this, RecepcionTraspasoMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(RecepcionTraspasoPyU.this);

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

                    case "ConsultaPallet":
                        dao = cad.cad_ConsultaPalletAbiertoTraspaso(documento, partida);
                        break;
                    case "RegistrarEmpaqueNuevo":
                        dao = cad.cad_PrimeraUltimaTraspaso(
                                documento,
                                partida,
                                binding.edtxEmpaque.getText().toString(),
                                binding.edtxEmpxPallet.getText().toString(),
                                binding.edtxPrimerEmpaque.getText().toString(),
                                binding.edtxUltimoEmpaque.getText().toString(),
                                binding.edtxCantidadEmpaques.getText().toString()
                        );
                        break;
                    case "RegistraPalletNuevo":
                        dao = cad.cad_OCCierraPalletTraspaso(binding.txtvPallet.getText().toString());
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
                        case "RegistrarEmpaqueNuevo":

                            binding.txtvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            binding.txtvEmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            binding.tvCantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            binding.edtxPrimerEmpaque.setText("");
                            binding.edtxUltimoEmpaque.setText("");
                            binding.edtxCantidadEmpaques.setText("");
                            binding.edtxPrimerEmpaque.requestFocus();

                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.traspaso_completado), dao.iscEstado(), true, true);
                                new RecepcionTraspasoPyU.SegundoPlano("RegistraPalletNuevo").execute();

                            }

                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new RecepcionTraspasoPyU.SegundoPlano("RegistraPalletNuevo").execute();
                            }

                            else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                            {
                                new RecepcionTraspasoPyU.SegundoPlano("RegistraPalletNuevo").execute();
                            }

                            break;

                        case "RegistraPalletNuevo":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(),"Pallet ["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            new RecepcionTraspasoPyU.SegundoPlano("ConsultaPallet").execute();
                            break;

                        case "ConsultaPallet":

                            binding.txtvEmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.txtvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Pallet"));
                            binding.txtvCantidadTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTot"));
                            binding.tvCantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadReg"));
                            break;
                    }
                }
                else
                {
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                }


                p.DesactivarProgressBar(tarea);
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage() , "false", true, true);
            }

        }
    }
}