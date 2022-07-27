package com.automatica.AXCPT.c_Produccion.Produccion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Almacen_Armado_Pallets_Liquidos_PyU;
import com.automatica.AXCPT.databinding.ActivityAlmacenArmadoPalletsNeBinding;
import com.automatica.AXCPT.databinding.ActivityAlmacenArmadoPalletsPyUBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Armado_Pallets_PyU extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar {

    Spinner spnr_Maquinas;
    String str_Maquina = "@";
    Toolbar toolbar;
    String [][] arrayDatosTabla;
    Handler handler = new Handler();
    String[] HEADER = {"Empaque"};
    String[][] DATA_TO_SHOW;
    int palletRegistradosVar = 0;
    int cantInt =0;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    SortableTableView tabla;
    View vista;
    Context contexto = this;
    DatePickerFragment newFragment;
    String EmpaqueBaja;
    boolean recargar;
    private static final String frgmnt_tag = "tag";
    int cantidadEmpaques = 0;
    private ProgressBarHelper p;
    ActivityAlmacenArmadoPalletsPyUBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    popUpGenerico pop = new popUpGenerico(Almacen_Armado_Pallets_PyU.this);
    SimpleTableHeaderAdapter sthd;
    Boolean seleccionado,ReiniciarTabla= false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenArmadoPalletsPyUBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new cambiaColorStatusBar(contexto, R.color.VerdeStd, Almacen_Armado_Pallets_PyU.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Armado de Pallets");
        agregaListeners();
        sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        spnr_Maquinas = (Spinner) findViewById(R.id.spnr_Maquinas).findViewById(R.id.spinner);
        tabla = (SortableTableView) findViewById(R.id.tableView);
        String Doc= null;
        try {
            Doc = getIntent().getStringExtra("Documento");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Doc!=null){
            binding.edtxOrdenProduccion.setText(Doc);
        }
        new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaMaquinas").execute();
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

    @Override
    protected void onResume() {

        new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaMaquinas").execute();
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
                    new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaOrdenProduccion").execute();
                    new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaMaquina").execute();
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

                            new esconderTeclado(Almacen_Armado_Pallets_PyU.this);
                        }
                    },null,contexto);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }



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

    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new Almacen_Armado_Pallets_PyU.cambiaColorTablaSeleccionada());


                tabla.getDataAdapter().notifyDataSetChanged();

                binding.btnCerrarTarima.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new Almacen_Armado_Pallets_PyU.cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                renglonAnterior=-1;
            }
            ReiniciarTabla = false;
        }

    }
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
            EmpaqueBaja = clickedData[0];

            new CreaDialogos("¿Eliminar el empaque [" + EmpaqueBaja + "]?",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                            new Almacen_Armado_Pallets_PyU.SegundoPlano("BajaEmpaque").execute();
                        }
                    },null,contexto);

            return false;
        }
    }
    private class cambiaColorTablaSeleccionada implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {

            if(rowIndex == renglonSeleccionado)
            {
                Color = R.color.RengSelStd;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                seleccionado = true;
            }
            else
            {
                Color = R.color.Transparente;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            }

            return new ColorDrawable(getResources().getColor(Color));
        }
    }
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {

            Toast.makeText(contexto, HEADER[columnIndex], Toast.LENGTH_SHORT).show();
            Log.d("SoapResponse", HEADER[columnIndex]);
        }
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


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao ;
        cAccesoADatos_RegistroPT cad = new cAccesoADatos_RegistroPT(Almacen_Armado_Pallets_PyU.this);
        cAccesoADatos_Consultas consAd = new cAccesoADatos_Consultas(Almacen_Armado_Pallets_PyU.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
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
                switch (tarea)
                {
                    case "ConsultaOrdenProduccion":
                        dao =   cad.c_ConsultaOrdenProduccion(binding.edtxOrdenProduccion.getText().toString());
                        break;
                    case "ConsultarTarima":
                        dao =   cad.c_ConsultaEmpaquesArmadoProd(binding.edtxOrdenProduccion.getText().toString());
                        break;


                    case "ConsultaMaquinas":
                        dao = consAd.c_ConsultaMaquinas(str_Maquina);
                        break;

                    case "RegistrarEmpaque":

                        dao =   cad.c_RegistraEmpaqueEnPalletPrimeraYUltima(binding.edtxEmpaque.getText().toString(),binding.edtxEmpaque3.getText().toString(),binding.edtxOrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato(),"1");
                        break;
                    case "RegistraCaducidad":
                        dao =   cad.c_RegistraFechaCadOrdenProd(binding.edtxOrdenProduccion.getText().toString(), "");
                        break;

                    case "CerrarTarima":
                        dao =   cad.c_CerrarRegistroPallet(binding.edtxOrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato());
                        break;
                    case "CancelarTarima":
                        dao =    cad.c_CancelarRegistroPallet(binding.edtxOrdenProduccion.getText().toString() );
                        break;
                    case "CerrarOrden":
                        dao =    cad.c_CerrarOrdenProduccion_v2(binding.edtxOrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato(),"");
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
                            if(!dao.getcMensaje().equals("@"))
                            {
                                str_Maquina = dao.getcMensaje();
                            }
                            new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultarTarima").execute();

                            break;
                        case "ConsultaMaquinas":

                            spnr_Maquinas.setAdapter(new CustomArrayAdapter(Almacen_Armado_Pallets_PyU.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("DLinea","Linea")));

                            break;

                        case "ConsultarTarima":
                            arrayDatosTabla = dao.getcTablaUnica();

                            tabla.getDataAdapter().clear();
                            tabla.getDataAdapter().notifyDataSetChanged();

                            cantInt = 0;

                            if (arrayDatosTabla != null)
                            {

                                if(!dao.getcEncabezado()[0].contains("Pallet"))
                                {
                                    cantInt = arrayDatosTabla.length;
                                    tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(Almacen_Armado_Pallets_PyU.this, dao.getcEncabezado()));
                                    tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Armado_Pallets_PyU.this, arrayDatosTabla));
                                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                    sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                                    ReiniciarTabla = true;
                                    tabla.setDataRowBackgroundProvider(new Almacen_Armado_Pallets_PyU.cambiaColorTablaClear());
                                }
                            }


                            binding.txtvCantidadEmpaques.setText(String.valueOf(cantInt));
                            binding.edtxEmpaque.setText("");
                            binding.edtxEmpaque3.setText("");
                            binding.edtxEmpaque.requestFocus();
                            break;
                        case "RegistrarEmpaque":
                            str_Maquina = ((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato();// agarro la maquina del ultimo registro correcto
                            new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaOrdenProduccion").execute();
                            break;

                        case "CerrarTarima":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + dao.getcMensaje() + "] con éxito.", dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaOrdenProduccion").execute();
                            if (binding.txtvCantidad.getText().toString().equals(binding.txtvCantidadReg.getText().toString()))
                            {
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), getString(R.string.orden_prod_completada_exito), dao.iscEstado(), true, true);
                            }
                            //ReiniciarVariables("Reiniciar");
                            break;

                        case "RegistraCaducidad":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.fecha_cad_reg_exito), dao.iscEstado(), true, true);

                            new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaOrdenProduccion").execute();
                            break;

                        case "CancelarTarima":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), getString(R.string.cancelar_tarima), dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaOrdenProduccion").execute();
                            break;

                        case "CerrarOrden":

                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_prod_cerrar_exito), dao.iscEstado(), true, true);

                            break;
                        case "BajaEmpaque":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                            new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaOrdenProduccion").execute();
                            new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultarTarima").execute();
                            EmpaqueBaja=null;
                            break;

                    }
                }

                else
                {

                    ReiniciarVariables(tarea);
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                }
            }catch (Exception e)
            {
                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);
                e.printStackTrace();
            }
            p.DesactivarProgressBar(tarea);
        }
    }

    // MÉTODOS ADICIONALES
    private void agregaListeners()
    {
        binding.edtxOrdenProduccion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!binding.edtxOrdenProduccion.getText().toString().equals(""))
                    {
                        Almacen_Armado_Pallets_PyU.SegundoPlano sp = new Almacen_Armado_Pallets_PyU.SegundoPlano("ConsultaOrdenProduccion");
                        sp.execute();
                    }else
                    {

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                binding.edtxOrdenProduccion.setText("");
                                binding.edtxOrdenProduccion.requestFocus();
                            }
                        });
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_orden_produccion) ,false ,true , true);
                    }
                    new esconderTeclado(Almacen_Armado_Pallets_PyU.this);
                    return true;
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
                    if(binding.edtxEmpaque.getText().toString().equals(""))
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), false, true, true);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                binding.edtxEmpaque.setText("");
                                binding.edtxEmpaque.requestFocus();
                            }
                        });
                        return false;
                    }

                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            binding.edtxEmpaque3.requestFocus();
                            binding.edtxEmpaque3.setText("");
                            //    Toast.makeText(contexto, "HOA", Toast.LENGTH_LONG).show();

                        }
                    },10);

                }
                return false;
            }
        });


        binding.edtxEmpaque3.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(! binding.edtxEmpaque3.getText().toString().equals(""))
                    {

                        if( binding.edtxOrdenProduccion.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,v ,getString(R.string.error_ingrese_orden_produccion) ,false ,true , true);
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxOrdenProduccion.setText("");
                                    binding.edtxOrdenProduccion.requestFocus();
                                }
                            });
                            return false;
                        }

                        if(binding.edtxEmpaque.getText().toString().equals(""))
                        {
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,v ,getString(R.string.error_ingrese_empaque) +" (Primer empaque)."  ,false ,true , true);
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    binding.edtxOrdenProduccion.setText("");
                                    binding.edtxOrdenProduccion.requestFocus();
                                }
                            });
                            return false;
                        }


//                        CreaDialogos("Ingrese la cantidad de tarimas registradas:", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                Toast.makeText(Almacen_Armado_Pallets_Liquidos_PyU.this,editText.getText().toString(), Toast.LENGTH_LONG).show();
//                                SegundoPlano sp = new SegundoPlano("RegistrarEmpaque");
//                                sp.execute();
//                            }
//                        }, new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                return;
//                            }
//                        }, Almacen_Armado_Pallets_Liquidos_PyU.this);



                        Almacen_Armado_Pallets_PyU.SegundoPlano sp = new Almacen_Armado_Pallets_PyU.SegundoPlano("RegistrarEmpaque");
                        sp.execute();


                    }else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,false ,true , true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                binding.edtxEmpaque.setText("");
                                binding.edtxEmpaque.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Armado_Pallets_PyU.this);
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

                                Almacen_Armado_Pallets_PyU.SegundoPlano sp = new Almacen_Armado_Pallets_PyU.SegundoPlano("CerrarTarima");
                                sp.execute();
                                new esconderTeclado(Almacen_Armado_Pallets_PyU.this);
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

                                new Almacen_Armado_Pallets_PyU.SegundoPlano("CancelarTarima").execute();
                                new esconderTeclado(Almacen_Armado_Pallets_PyU.this);
                            }
                        },null,contexto);
                    }
                    else
                    {
                        new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista ,getString(R.string.error_cancelar_pallet_sin_empaques) , false,true , true);
                    }
            }
        });

//        tabla.addDataClickListener(new Almacen_Armado_Pallets_PyU.ListenerClickTabla());
//        tabla.addHeaderClickListener(new Almacen_Armado_Pallets_PyU.headerClickListener());
//        tabla.addDataLongClickListener(new Almacen_Armado_Pallets_PyU.ListenerLongClickTabla());
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
                binding.edtxOrdenProduccion.requestFocus();
                binding.edtxEmpaque3.setText("");
                break;
            case "ConsultarTarima":
                tabla.getDataAdapter().clear();
                binding.edtxEmpaque.setText("");
                binding.txtvCantidadEmpaques.setText("-");
                binding.edtxEmpaque.requestFocus();
                binding.edtxEmpaque3.setText("");
                break;
            case "RegistrarEmpaque":
                binding.edtxEmpaque.setText("");
                binding.edtxEmpaque.requestFocus();
                binding.edtxEmpaque3.setText("");

                break;
            case "Reiniciar":
                binding.edtxOrdenProduccion.setText("" );
                binding.txtvProducto.setText("");
                binding.txtvLote.setText("");
                binding.txtvCantidad.setText("");
                binding.txtvCantidadReg.setText("");
                binding.txtvCantidadEmpaques.setText("-");
                tabla.getDataAdapter().clear();
                binding.edtxOrdenProduccion.requestFocus();
                binding.edtxEmpaque3.setText("");
                break;
        }
       /* edtx_OrdenProduccion.setText("");
        edtx_Empaque.setText("");
        edtx_OrdenProduccion.setText("");
        txtv_Producto.setText("");
        txtv_Lote.setText("");
        txtv_Cantidad.setText("");
        edtx_OrdenProduccion.requestFocus();*/
    }



}