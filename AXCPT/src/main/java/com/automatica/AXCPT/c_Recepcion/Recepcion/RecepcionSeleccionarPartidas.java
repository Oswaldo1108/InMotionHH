package com.automatica.AXCPT.c_Recepcion.Recepcion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;


import com.automatica.AXCPT.Fragmentos.Fragmento_Cancelar_Tarima;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.ReabastecimientoPicking.ReabPK_Seleccion_Material;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.databinding.ActivityRecepcionSeleccionarPartidasBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class RecepcionSeleccionarPartidas extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar, Fragmento_Cancelar_Tarima.OnFragmentInteractionListener {


    SortableTableView StvTabla;

    Intent intent;
    popUpGenerico pop = new popUpGenerico(RecepcionSeleccionarPartidas.this);
    String IdRecepcion = "";
    View vista;
    Context contexto = this;
    String Orden, Tipo;
    Handler h = new Handler();
    ActivityRecepcionSeleccionarPartidasBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    private ProgressBarHelper p;
    Bundle b;
    boolean RengonSeleccionado,Recarga = true;
    TableViewDataConfigurator ConfigTabla_Totales = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityRecepcionSeleccionarPartidasBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        RengonSeleccionado = false;
        SacaDatosIntent();
        DeclararVariables();
        AgregarListeners();

        String Doc = null;
        try
        {
            Doc = getIntent().getStringExtra("Documento");
        }catch (Exception  e)
        {
            e.printStackTrace();
        }
        if(Doc != null)
        {
            binding.edtxOrden.setText(Doc);
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recepción");
        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null)
                {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else
                    {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();

    }

    @Override
    protected void onResume() {

        if(!TextUtils.isEmpty(binding.edtxOrden.getText().toString())){

            new SegundoPlano("Tabla").execute();

        }

        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.RECEPCION);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.ciclicos_cancelar_pallet, menu);
            return true;

        } catch (Exception e) {
            Toast.makeText(contexto, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (p.ispBarActiva()){

            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo)) {

                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar) {

                new SegundoPlano("Tabla").execute();
            }
            if (id == R.id.cancelar_pallets)
            {
                if(!binding.edtxOrden.getText().toString().equals(""))
                {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.Pantalla_principal,Fragmento_Cancelar_Tarima.newInstance(binding.edtxOrden.getText().toString(), null,""), "FRAGTAG111").addToBackStack("FRAGTAG111")
                            .commit();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void DeclararVariables(){

        try{
            binding.edtxOrden.setText(Orden);
            StvTabla = findViewById(R.id.tableView_OC);
            p = new ProgressBarHelper(this);


        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }

    }

    private void AgregarListeners(){

        try{
            binding.edtxOrden.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if(( event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){

                        if( !TextUtils.isEmpty(binding.edtxOrden.getText().toString())){

                            new SegundoPlano("Tabla").execute();
                        } else{

                            pop.popUpGenericoDefault(vista,"Ingrese orden de recepción",false);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxOrden.requestFocus();
                                    binding.edtxOrden.setText("");
                                }
                            });

                        }

                        new esconderTeclado(RecepcionSeleccionarPartidas.this);
                    }
                    return false;
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }

    }

    private void SacaDatosIntent(){
        try
        {
            b  = getIntent().getExtras();
            Orden = b.getString("Orden");
            Log.e("Error", Orden);
            Tipo = b.getString("Tipo");
            Log.i("AHHHHHHH",Orden +" "+ Tipo);
            binding.edtxOrden.setText(Orden);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void ValidacionFinal(){

        try{
            if(binding.edtxOrden.getText().toString().equals(""))
            {
                new popUpGenerico(contexto,vista,getString(R.string.error_seleccionar_registro),"Advertencia",true,false);

                return;
            }
            if(!ConfigTabla_Totales.renglonEstaSelec())
            {
                new popUpGenerico(contexto,vista ,getString(R.string.error_seleccionar_registro) , false, true,true );
                return;
            }

            b.putString("Orden", binding.edtxOrden.getText().toString());
            b.putString("FechaCaducidad", "-");


            new PopUpMenuAXC().newInstance(taskbar_axc.getView().findViewById(R.id.BotonDer), RecepcionSeleccionarPartidas.this, R.menu.popup_etiquetados_vde, new PopUpMenuAXC.ContextMenuListener() {
                @Override
                public void listenerItem(MenuItem item) {
                    try{
                        switch (item.getItemId()){
                            case R.id.PyU:
                                intent= new Intent(contexto, PrimerayUltima.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.etiquetado:
                                intent= new Intent(contexto, RecepcionEmpaques.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.registrorefacciones:
                                intent= new Intent(contexto, Recepcion_Refacciones.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.DAP:
                                intent= new Intent(contexto, RecepcionDAP.class);
                                intent.putExtras(b);
                                startActivity(intent);                                    
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.contenedor:
                                intent= new Intent(contexto, RecepcionContenedor.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.palletne:
                                intent= new Intent(contexto, RecepcionPalletNe.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            default:
                                pop.popUpGenericoDefault(vista,"Seleccione una opción",false);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        pop.popUpGenericoDefault(vista,e.getMessage(),false);
                    }
                }
            });
            //new PopUpMenuAXC(taskbar_axc.getView().findViewById(R.id.BotonDer),RecepcionSeleccionarPartidas.this,R.menu.popup_etiquetados);

        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    @Override
    public void BotonDerecha() {
        ValidacionFinal();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {



    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {
        b.putString("IdRecepcion", IdRecepcion);
        b.putString("PartidaERP", clickedData[0]);
        b.putString("NumParte", clickedData[2]);
        b.putString("SKU", clickedData[10]);

        b.putString("CantidadTotal", clickedData[3]);
        b.putString("CantidadRecibida", clickedData[5]);
        b.putString("UM", clickedData[7]);
        b.putString("CantidadEmpaques", clickedData[8]);
        b.putString("EmpaquesPallet", clickedData[9]);
        RengonSeleccionado = true;
    }

    @Override
    public boolean AceptarOrden() {
        if(!TextUtils.isEmpty(binding.edtxOrden.getText())){
            new SegundoPlano("Tabla").execute();
        }
        return false;
    }

    @Override
    public void EstadoCarga(Boolean Estado) {

    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>{

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(contexto);
        private SegundoPlano (String Tarea){ this.Tarea = Tarea;}

        @Override
        protected void onPreExecute(){p.ActivarProgressBar(Tarea);}

        @Override
        protected Void doInBackground(String... params) {
            try{

                switch (Tarea)
                {
                    case"Tabla":

                        dao = ca.c_ListarPartidasOCLiberadas(binding.edtxOrden.getText().toString());
                        break;
                    case"CierreOC":

                        dao = ca.c_CerrarRecepcion(binding.edtxOrden.getText().toString());
                        break;
                }

            }catch (Exception e){
                dao = new DataAccessObject(e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                if (dao.iscEstado())
                {
                    switch (Tarea)
                    {
                        case "Tabla":

                            if (dao.getcTablaUnica()!=null)
                            {
                                if(ConfigTabla_Totales == null)
                                {
                                    ConfigTabla_Totales = new TableViewDataConfigurator(6,"RECIBIDA","LIBERADA","",StvTabla, dao, RecepcionSeleccionarPartidas.this);
                                }else
                                {
                                    ConfigTabla_Totales.CargarDatosTabla(dao);
                                }
                            }
                            RengonSeleccionado = false;

                            break;
                        case "CierreOC":

                            pop.popUpGenericoDefault(vista,dao.getcMensaje(),true);
                            break;
                    }
                }else{
                    pop.popUpGenericoDefault(vista,dao.getcMensaje(),false);
                }
            }catch (Exception e)
            {
                pop.popUpGenericoDefault(vista,e.getMessage(),false);
                e.printStackTrace();
            }
            p.DesactivarProgressBar(Tarea);

        }
    }
}