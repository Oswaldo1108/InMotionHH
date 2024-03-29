package com.automatica.AXCPT.c_Produccion.Produccion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.databinding.AlmacenArmadoPalletBinding;
import com.automatica.AXCPT.databinding.AlmacenArmadoPalletEmpaquesBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Armado_Pallets_Empaque extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar {

    //region variables

    Spinner spnr_Maquinas;
    String str_Maquina = "@";
    //Toolbar toolbar;

    SortableTableView tabla;
    SortableTableView tabla2;
    String orden, serial;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    Button btn_CerrarTarima,btn_CancelarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    boolean recargar;
    Context contexto = this;
    DatePickerFragment newFragment;
    String EmpaqueBaja;
    String [][] arrayDatosTabla;
    Handler handler = new Handler();
    String[] HEADER = {"Código Paquete"};
    String[][] DATA_TO_SHOW;
    int palletRegistradosVar = 0;
    int cantInt =0;
    int renglonSeleccionado;
    frgmnt_taskbar_AXC taskbar_axc;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st;
    SimpleTableHeaderAdapter sthd;
    AlmacenArmadoPalletEmpaquesBinding binding;
    private static final String frgmnt_tag = "tag";
    private ProgressBarHelper p;
    popUpGenerico pop = new popUpGenerico(Almacen_Armado_Pallets_Empaque.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =   AlmacenArmadoPalletEmpaquesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
     //   SacaDatosIntent();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new cambiaColorStatusBar(contexto, R.color.VerdeStd, Almacen_Armado_Pallets_Empaque.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Empaque único");
        AgregarListeners();
        declararVariables();
        binding.edtxOrdenProduccion.requestFocus();

        String Doc= null;
        try {
            Doc = getIntent().getStringExtra("Documento");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Doc!=null){
           // binding.edtxOrdenProduccion.setText(Doc);
        }
        new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaMaquinas").execute();
        p = new ProgressBarHelper(this);

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
      //  new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
        new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaMaquinas").execute();
       // new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultarTarima").execute();
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

                if (!binding.edtxOrdenProduccion.getText().toString().equals(""))
                {
                    tabla.getDataAdapter().clear();
                    new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
                    new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaMaquina").execute();
                    new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultarTarima").execute();
                }
            }
            if ((id == R.id.cancelar_pallets))
            {

                if (!binding.edtxOrdenProduccion.getText().toString().equals(""))
                {
                    if(arrayDatosTabla!=null)
                        if(!(arrayDatosTabla.length<=0))
                        {
                            new CreaDialogos(getString(R.string.pregunta_cancelar_pallet), new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id) {

                                    new Almacen_Armado_Pallets_Empaque.SegundoPlano("CancelarTarima").execute();
                                    new esconderTeclado(Almacen_Armado_Pallets_Empaque.this);
                                }
                            },null,contexto);
                        }
                        else
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista ,getString(R.string.error_cancelar_pallet_sin_empaques) , false,true , true);
                        }
                }
            }


        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void BotonDerecha() {
        validacionFinal();
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

    }

    private class cambiaColorTablaClear implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {
            Color = R.color.Transparente;
            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            return new ColorDrawable(getResources().getColor(Color));
        }
    }


    // CLASE PARA LLAMAR A LOS WEBSERVICES
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao ;
        cAccesoADatos_RegistroPT cad = new cAccesoADatos_RegistroPT(Almacen_Armado_Pallets_Empaque.this);
        cAccesoADatos_Consultas consAd = new cAccesoADatos_Consultas(Almacen_Armado_Pallets_Empaque.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            recargar = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            palletRegistradosVar=0;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case "ConsultaOrdenProduccion":
                        dao =  cad.c_ConsultaOrdenProduccion(binding.edtxOrdenProduccion.getText().toString());
                        break;

                    case "ConsultaMaquinas":
                        dao = consAd.c_ConsultaMaquinas(str_Maquina);
                        break;

                    case "ConsultarTarima":
                        dao =   cad.c_ConsultaEmpaquesArmadoProdUnico(binding.edtxOrdenProduccion.getText().toString());
                        break;
                    case "RegistrarEmpaque":
                        dao = cad.c_RegistraEmpaqueEnPalletUnico(binding.edtxEmpaque.getText().toString(), binding.edtxOrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato(),"0", binding.edtxNumSerie.getText().toString());
                        break;

                    case "CerrarTarima":
                        dao =   cad.c_CerrarRegistroPallet(binding.edtxOrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato());
                        break;
                    case "CancelarTarima":
                        dao = cad.c_CancelarRegistroPallet(binding.edtxOrdenProduccion.getText().toString() );
                        break;
                    case "CerrarOrden":
                        dao =    cad.c_CerrarOrdenProduccion_v2(binding.edtxOrdenProduccion.getText().toString(),"","");
                        break;

                    case "BajaEmpaque":
                        dao =    cad.c_BajaEmpaqueArmadoTarimas(binding.edtxOrdenProduccion.getText().toString(),EmpaqueBaja );
                        break;
                    default:
                        dao = new DataAccessObject();
                }
            }catch (Exception e)
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
                            binding.txtvCantidadReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRegistrada"));
                            binding.txtvProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Producto"));
                            binding.tvCodMaterial.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Codigo"));


                            break;

                        case "ConsultaMaquinas":

                            spnr_Maquinas.setAdapter(new CustomArrayAdapter(Almacen_Armado_Pallets_Empaque.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("DLinea","Linea")));

                            break;


                        case "ConsultarTarima":
                            if (ConfigTabla_Totales == null) {

                                ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao, Almacen_Armado_Pallets_Empaque.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }
                            arrayDatosTabla = dao.getcTablaUnica();

                            tabla.getDataAdapter().clear();
                            tabla.getDataAdapter().notifyDataSetChanged();

                            cantInt = 0;

                            if (arrayDatosTabla != null)
                            {

                                if(!dao.getcEncabezado()[0].contains("Pallet"))
                                {
                                    cantInt = arrayDatosTabla.length;
                                    tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(Almacen_Armado_Pallets_Empaque.this, dao.getcEncabezado()));
                                    tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Armado_Pallets_Empaque.this, arrayDatosTabla));
                                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                    sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                                   // ReiniciarTabla = true;
                                   // tabla.setDataRowBackgroundProvider(new Almacen_Armado_Pallets_Liquidos.cambiaColorTablaClear());
                                }
                            }
                           // binding.edtxEmpaque.requestFocus();


                          //  binding.txtvCantidadEmpaques.setText(String.valueOf(cantInt));
                            binding.edtxEmpaque.setText("");

                            break;


                        case "RegistrarEmpaque":
                            binding.edtxEmpaque.setText("");
                            str_Maquina = ((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato();// agarro la maquina del ultimo registro correcto
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultarTarima").execute();
                            binding.edtxNumSerie.setText("");
                            binding.edtxNumSerie.requestFocus();
                            break;

                        case "CerrarTarima":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + dao.getcMensaje() + "] con éxito.", dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
                            if (binding.txtvCantidad.getText().toString().equals(binding.txtvCantidadReg.getText().toString()))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), getString(R.string.orden_prod_completada_exito), dao.iscEstado(), true, true);
                            }
                            //ReiniciarVariables("Reiniciar");
                            break;


                        case "CancelarTarima":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), getString(R.string.cancelar_tarima), dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
                            break;

                        case "CerrarOrden":

                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_prod_cerrar_exito), dao.iscEstado(), true, true);

                            break;
                        case "BajaEmpaque":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultarTarima").execute();
                            EmpaqueBaja=null;
                            break;

                    }
                }

                else
                {

                    binding.edtxNumSerie.setText("");
                    binding.edtxEmpaque.setText("");
                    //ReiniciarVariables(tarea);
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                }
            }catch (Exception e)
            {
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), e, dao.iscEstado(), true, true);
                e.printStackTrace();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = false;
        }
    }


    // MÉTODOS ADICIONALES


    private void SacaDatosIntent(){

    }

    private void AgregarListeners() {

        binding.edtxOrdenProduccion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!binding.edtxOrdenProduccion.getText().toString().equals(""))
                    {

                        if(binding.edtxOrdenProduccion.getText().toString().startsWith("(D)")){
                            orden = binding.edtxOrdenProduccion.getText().toString().split("\\|")[0];
                            serial = binding.edtxOrdenProduccion.getText().toString().split("\\|")[1];
                            if(orden.contains("(D)")){
                                orden = orden.replace("(D)", "");
                                serial = serial.replace("(S)", "");
                                binding.edtxOrdenProduccion.setText(orden);
                                binding.edtxNumSerie.setText(serial);
                                new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
                                new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultarTarima").execute();
                                binding.edtxEmpaque.requestFocus();

                            }
                            else{

                            }

                        }else
                        {
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();
                            new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultarTarima").execute();
                            binding.edtxNumSerie.requestFocus();
                        }


                    }else
                    {

                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_orden_produccion) ,false ,true , true);
                    }
                    new esconderTeclado(Almacen_Armado_Pallets_Empaque.this);
                    return true;
                }
                return false;
            }
        });


        binding.edtxNumSerie.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!binding.edtxNumSerie.getText().toString().equals(""))
                    {

                        if(binding.edtxNumSerie.getText().toString().startsWith("(D)")){
                            orden = binding.edtxNumSerie.getText().toString().split("\\|")[0];
                            serial = binding.edtxNumSerie.getText().toString().split("\\|")[1];
                            if(orden.contains("(D)")){
                                orden = orden.replace("(D)", "");
                                serial = serial.replace("(S)", "");
                            }
                            binding.edtxNumSerie.setText(serial);
                            //
                        }
                      //  binding.edtxEmpaque.requestFocus();
                    }
                    else
                    {
                       // binding.edtxEmpaque.requestFocus();
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
                    if(! binding.edtxEmpaque.getText().toString().equals(""))
                    {
                        new Almacen_Armado_Pallets_Empaque.SegundoPlano("RegistrarEmpaque").execute();
                        new Almacen_Armado_Pallets_Empaque.SegundoPlano("ConsultaOrdenProduccion").execute();

                     //   binding.edtxNumSerie.requestFocus();
                    }else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,false ,true , true);

                    }
                    new esconderTeclado(Almacen_Armado_Pallets_Empaque.this);
                    return  true;
                }
                return false;
            }
        });


        binding.btnCerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(arrayDatosTabla!=null)
                    if(!(arrayDatosTabla.length<=0))
                    {
                        new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id) {

                                new Almacen_Armado_Pallets_Empaque.SegundoPlano("CerrarTarima").execute();
                                new esconderTeclado(Almacen_Armado_Pallets_Empaque.this);
                            }
                        },null,contexto);
                    }
                    else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , false,true , true);
                    }
            }
        });
        binding.btnCancelarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(arrayDatosTabla!=null)
                    if(!(arrayDatosTabla.length<=0))
                    {
                        new CreaDialogos(getString(R.string.pregunta_cancelar_pallet), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id) {

                                new Almacen_Armado_Pallets_Empaque.SegundoPlano("CancelarTarima").execute();
                                new esconderTeclado(Almacen_Armado_Pallets_Empaque.this);
                            }
                        },null,contexto);
                    }
                    else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista ,getString(R.string.error_cancelar_pallet_sin_empaques) , false,true , true);
                    }
            }
        });



    }
    private void ReiniciarVariables(String tarea)
    {
        switch (tarea)
        {
            case "ConsultaOrdenProduccion":
                binding.edtxOrdenProduccion.setText("" );
                binding.txtvProducto.setText("");
                binding.txtvLote.setText("");
                binding.txtvCantidad.setText("");
                tabla.getDataAdapter().clear();
                binding.txtvCantidadReg.setText("");


                break;
            case "ConsultarTarima":

                tabla.getDataAdapter().clear();
                binding.edtxEmpaque.setText("");
                binding.txtvCantidadEmpaques.setText("-");
              //  binding.edtxEmpaque.requestFocus();
                break;
            case "RegistrarEmpaque":

                binding.edtxEmpaque.setText("");
                str_Maquina = ((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato();// agarro la maquina del ultimo registro correcto
                binding.edtxNumSerie.setText("");
              //  binding.edtxNumSerie.requestFocus();
                break;
            case "Reiniciar":
                binding.edtxOrdenProduccion.setText("" );
                binding.txtvProducto.setText("");
                binding.txtvLote.setText("");
                binding.txtvCantidad.setText("");
                binding.txtvCantidadReg.setText("");
                binding.txtvCantidadEmpaques.setText("-");
                tabla.getDataAdapter().clear();
               // binding.edtxOrdenProduccion.requestFocus();
                break;
        }
    }

    private void declararVariables()
    {
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
      //  edtx_Empaque= (EditText) findViewById(R.id.edtx_Empaque);
       // edtx_OrdenProduccion = (EditText) findViewById(R.id.edtx_OrdenProduccion);
        tabla2 = (SortableTableView) findViewById(R.id.tableView);
        spnr_Maquinas = (Spinner) findViewById(R.id.spnr_Maquinas).findViewById(R.id.spinner);
      //  edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
       // edtx_OrdenProduccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        tabla2.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla2.setHeaderAdapter(sthd);
      //  btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);
      //  btn_CancelarTarima = (Button) findViewById(R.id.btn_CancelarTarima);
      //  btn_CerrarTarima.setEnabled(true);
    }

    public  void  validacionFinal(){
        if(arrayDatosTabla!=null)
            if(!(arrayDatosTabla.length<=0))
            {
                new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {

                        new Almacen_Armado_Pallets_Empaque.SegundoPlano("CerrarTarima").execute();
                        new esconderTeclado(Almacen_Armado_Pallets_Empaque.this);
                    }
                },null,contexto);
            }
            else
            {
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , false,true , true);
            }
    }


}