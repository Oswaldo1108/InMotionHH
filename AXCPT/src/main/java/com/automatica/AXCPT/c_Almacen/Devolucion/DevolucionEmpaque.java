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
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Login;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.Devolucion_Empaques;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.c_Traspasos.Recibe.RecepcionTraspasoEmpaque;
import com.automatica.AXCPT.databinding.ActivityDevolucionEmpaqueBinding;
import com.automatica.AXCPT.databinding.ActivitySeleccionPartidaDevolucionBinding;
import com.automatica.AXCPT.objetos.ObjetoEtiquetaSKU;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

    public class DevolucionEmpaque extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    private ProgressBarHelper p;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(DevolucionEmpaque.this);
    ActivityDevolucionEmpaqueBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    String documento, partida, numParte, cantTotal, cantRec, um, cantEmp, empxpallet, SKU;
    private Spinner sp_Partidas, sp_NumSerie;
    Handler h = new Handler();
    CreaDialogos creaDialogos = new CreaDialogos(DevolucionEmpaque.this);

    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevolucionEmpaqueBinding.inflate(getLayoutInflater());
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
        new DevolucionEmpaque.SegundoPlano("Tabla").execute();
        new DevolucionEmpaque.SegundoPlano("ConsultarPallet").execute();

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
            SKU = b.getString("NumParte");
            cantTotal = b.getString("CantidadTotal");
            cantRec = b.getString("CantidadRecibida");
            um = b.getString("UM");
            /*cantEmp = b.getString("CantidadEmpaques");
            empxpallet = b.getString("EmpaquesPallet");
            */
            numParte = b.getString("NumParte");
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
        sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devolución");
        getSupportActionBar().setSubtitle("Empaques");
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

    private void consultarSKU(){
        try
        {
            if(binding.edtxSKU.getText().toString().equals(""))
            {

                new popUpGenerico(contexto,binding.edtxSKU,"Ingrese un SKU." , false, true, true);
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.edtxSKU.setText("");
                        binding.edtxSKU.requestFocus();
                    }
                }, 100);


            }
            else {

                ObjetoEtiquetaSKU obj1 = new ObjetoEtiquetaSKU(binding.edtxSKU.getText().toString());
                binding.edtxSKU.setText(String.valueOf(obj1.sku));
                binding.edtxNumSerie.setText(String.valueOf(obj1.numeroSerie));


                binding.edtxSKU.setText(binding.edtxSKU.getText().toString().replace(" ", " ").replace("\t", "").replace("\n", ""));

                int SKUSel = -2;
                SKUSel = CustomArrayAdapter.getIndex(sp_Partidas, binding.edtxSKU.getText().toString());

                switch (SKUSel) {
                    case -2:
                        new popUpGenerico(contexto, binding.edtxSKU, "Error interno.", false, true, true);
                        new esconderTeclado(DevolucionEmpaque.this);

                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.edtxSKU.setText("");
                                binding.edtxSKU.requestFocus();
                            }
                        }, 100);

                    case -1:
                        int UPCsel = -2;
                        UPCsel = CustomArrayAdapter.getIndex(sp_Partidas, binding.edtxSKU.getText().toString(), CustomArrayAdapter.TAG2);
                        switch (UPCsel) {
                            case -2:
                                new popUpGenerico(contexto, binding.edtxSKU, "Error interno.", false, true, true);
                                new esconderTeclado(DevolucionEmpaque.this);
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.edtxSKU.setText("");
                                        binding.edtxSKU.requestFocus();
                                    }
                                }, 100);
                                break;
                            case -1:
                                new popUpGenerico(contexto, binding.edtxSKU, "No se encontró el SKU dentro del listado de partidas, verifique que sea correcto. [" + binding.edtxSKU.getText().toString() + "]", false, true, true);
                                new esconderTeclado(DevolucionEmpaque.this);
                                h.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.edtxSKU.setText("");
                                        binding.edtxEmpaque.setText("");
                                        binding.edtxSKU.requestFocus();
                                    }
                                }, 100);
                                break;
                            default:
                                sp_Partidas.setSelection(UPCsel);
                                break;
                        }
                        break;
                    default:
                        sp_Partidas.setSelection(SKUSel);
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }
    }

    private void agregarListener(){

       /* binding.checkNumSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkNumSerie.isChecked())
                    binding.edtxNumSerie.setEnabled(true);
                else
                    binding.edtxNumSerie.setEnabled(false);
            }
        });*/




        binding.edtxSKU.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    consultarSKU();

                }
                return false;
            }
        });


        binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
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
                        }

                        new SegundoPlano("ConsultaEmpaque").execute();
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
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    try{
                        if (!binding.edtxEmpaque.getText().toString().equals(binding.edtxConfirmar.getText().toString())){
                            new popUpGenerico(contexto, getCurrentFocus(), "Los códigos de empaque no coinciden", "false", true, true);
                        }

                        new DevolucionEmpaque.SegundoPlano("RegistrarEmpaque").execute();
                    }
                    catch (Exception e){
                        e.printStackTrace();
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
                binding.tvCantTotal.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag3());
                new DevolucionEmpaque.SegundoPlano("ConsultarPartida").execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });


    }



    private void validacionFinal(){

        try {
            if (!binding.tvEmpReg.getText().toString().equals("")) {
                if (Integer.parseInt(binding.tvEmpReg.getText().toString()) > 0) {


                    new CreaDialogos("¿Cerrar tarima?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new DevolucionEmpaque.SegundoPlano("CerrarTarima").execute();
                                }
                            }, null, contexto);


                } else {
                    new popUpGenerico(contexto, null, getString(R.string.error_empaque_no_seleccionado), "false", true, true);
                }
            } else {
                new popUpGenerico(contexto, null, getString(R.string.error_empaque_no_seleccionado), "false", true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    private void reiniciarDatos(){
        binding.edtxEmpaque.setText("");
        binding.edtxNumSerie.setText("");
        binding.edtxNumSerie.setEnabled(false);
        binding.edtxSKU.setText("");
        binding.edtxSKU.setEnabled(false);
        binding.edtxCantidad.setText("");
        binding.edtxConfirmar.setText("");

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
        Intent intent = new Intent(DevolucionEmpaque.this, SeleccionPartidaDevolucion.class);
        b.putString("Documento", binding.tvDocumento.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(b);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }


        public void Cerrar() {
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
            Intent intent = new Intent(DevolucionEmpaque.this, SeleccionOrdenDevolucion.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtras(b);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
        }

    // ********************************************************* SEGUNDO PLANO **********************************************************

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(DevolucionEmpaque.this);

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
                    case "Tabla":
                        dao = ca.c_ListarPartidasDevEnProceso(documento);
                        break;

                    case "ConsultarPartida":
                        dao= ca.c_ConsultaPartidaDevolucion(documento,binding.tvPartida.getText().toString());
                        break;

                    case "ConsultarPallet":
                        dao= ca.c_ConsultaPalletAbiertoDev(documento,partida);
                        break;

                    case "ConsultaEmpaque":
                        dao = ca.c_ConsultaEmpaqueDev(binding.edtxEmpaque.getText().toString());
                        break;

                    case "RegistrarEmpaque":
                        dao = ca.c_OCRegistrarEmpaqueDevolucion(documento,
                                                                binding.tvPartida.getText().toString(),
                                                                binding.edtxConfirmar.getText().toString(),
                                                                "",
                                                                binding.edtxNumSerie.getText().toString(),
                                                                binding.edtxCantidad.getText().toString(),
                                                                "1");
                        break;

                    case "CerrarTarima":
                        dao = ca.c_CierraPalletDevolucion(binding.tvPallet.getText().toString());
                        break;

                    default:
                        dao = new DataAccessObject();


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

                        case"Tabla":
                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(DevolucionEmpaque.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }


                            int SKUSel = -2;
                            SKU = SKU.replace(" ", "");
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU.trim());
                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,binding.edtxSKU,"Error interno." , false, true, true);
                                    new esconderTeclado(DevolucionEmpaque.this);
                                    binding.edtxSKU.setText("");
                                    binding.edtxSKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    new popUpGenerico(contexto,binding.edtxSKU,"La partida con el SKU: [" + SKU +"]" + " ya fué completada" , false, true, true);
                                    new esconderTeclado(DevolucionEmpaque.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            new esconderTeclado(DevolucionEmpaque.this);
                            SKU = "DEFAULT";

                            break;




                        case "ConsultarPallet":
                            String CodigoPallet, Empaques;
                            String[] mensaje= dao.getcMensaje().split("#");
                            CodigoPallet = mensaje[0];
                            Empaques = mensaje[1];
                            if(!CodigoPallet.equals(""))
                            {
                                binding.tvPallet.setText(CodigoPallet);
                                binding.tvEmpReg.setText(Empaques);
                            }else {
                                binding.tvPallet.setText("-");
                                binding.tvEmpReg.setText("-");
                            }
                            break;

                        case "ConsultarPartida":
                            binding.tvCantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            binding.tvArticulo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));
                            binding.tvUM.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));
                            break;



                        case "ConsultaEmpaque":
                            binding.edtxNumSerie.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumSerie"));
                            binding.edtxSKU.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("SKU"));
                            binding.edtxSKU.setEnabled(false);
                            binding.edtxNumSerie.setEnabled(false);
                            consultarSKU();
                            binding.edtxCantidad.requestFocus();
                            break;

                        case "RegistrarEmpaque":

                            binding.tvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));


                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {

                                //new popUpGenerico(DevolucionEmpaque.this, getCurrentFocus(), getString(R.string.orden_compra_completada),dao.iscEstado(), true, true);
                                creaDialogos.CerrarDia("Orden de devolución completada.", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new DevolucionEmpaque.SegundoPlano("CerrarTarima").execute();
                                        Cerrar();
                                    }
                                },null,contexto);

                                return;

                            }
                            else {
                                if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1")) {
                                    new popUpGenerico(DevolucionEmpaque.this, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                    new DevolucionEmpaque.SegundoPlano("CerrarTarima").execute();
                                    reiniciarDatos();
                                    binding.edtxEmpaque.requestFocus();
                                    return;
                                }
                                   /* else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1") )
                                    {
                                        new DevolucionEmpaque.SegundoPlano("CerrarTarima").execute();
                                    }*/
                                    new DevolucionEmpaque.SegundoPlano("Tabla").execute();
                                    new DevolucionEmpaque.SegundoPlano("ConsultarPartida").execute();
                                    new DevolucionEmpaque.SegundoPlano("ConsultarPallet").execute();
                                    reiniciarDatos();
                                    binding.edtxEmpaque.requestFocus();
                            }
                            break;

                        case "CerrarTarima":
                            new popUpGenerico(DevolucionEmpaque.this,getCurrentFocus(),getString(R.string.orden_compra_pallet_cerrado) + " [" +dao.getcMensaje() + "]"  ,true,true,true);
                            new DevolucionEmpaque.SegundoPlano("ConsultarPallet").execute();
                            //new DevolucionEmpaque.SegundoPlano("ConsultarPartida").execute();
                            reiniciarDatos();
                            break;
                    }


                }
                else if(dao.getcMensaje().contains("NO")){
                    Toast.makeText(DevolucionEmpaque.this, "No existe",Toast.LENGTH_SHORT).show();
                    binding.edtxSKU.setEnabled(true);
                    binding.edtxNumSerie.setEnabled(true);
                    binding.edtxSKU.requestFocus();
                    binding.edtxNumSerie.setText("");
                    binding.edtxSKU.setText("");
                }
                else {
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