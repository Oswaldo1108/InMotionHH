package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.frgmnt_Cierre_Orden;
import com.automatica.AXCPT.c_Recepcion.Rec_Registro_Seleccion_OC;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionarHC;
import com.automatica.AXCPT.databinding.ActivityRecepcionConteoBinding;
import com.automatica.AXCPT.databinding.AlmacenArmadoPalletLiquidosBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.fragment.app.Fragment;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Armado_Pallets_Liquidos extends AppCompatActivity implements  frgmnt_Cierre_Orden.OnFragmentInteractionListener
{
    //region variables
    Spinner spnr_Maquinas;
    String str_Maquina = "@";
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_OrdenProduccion;
    SortableTableView tabla;
    Button btn_CerrarTarima,btn_CancelarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
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
    Boolean seleccionado,ReiniciarTabla= false;
    TextView txtv_Producto,txtv_Cantidad, txtv_Lote,txtv_CantidadEmpaques,txtv_CantidadRegistrada;
    boolean recargar;
    SimpleTableHeaderAdapter sthd;
    AlmacenArmadoPalletLiquidosBinding binding;
    private static final String frgmnt_tag = "tag";
    private ProgressBarHelper p;
    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        if(estado)
            {
                recargar = true;
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
            }else
            {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
                recargar = false;
            }
        return false;
    }

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        binding = AlmacenArmadoPalletLiquidosBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new cambiaColorStatusBar(contexto, R.color.VerdeStd, Almacen_Armado_Pallets_Liquidos.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        declararVariables();
        agregaListeners();

        String Doc= null;
        try {
            Doc = getIntent().getStringExtra("Documento");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Doc!=null){
            edtx_OrdenProduccion.setText(Doc);
        }
        new SegundoPlano("ConsultaMaquinas").execute();
        p = new ProgressBarHelper(this);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Hoja de conteo");


  /*      View logoView = getToolbarLogoIcon(toolbar);
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
*/
//        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("", "");
  //      getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal, taskbar_axc, "FragmentoTaskBar").commit();

    }

    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.almacen_armado_tarimas));
        getSupportActionBar().setSubtitle(getString(R.string.almacen_armado_tarimas_liquidos));

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


        edtx_Empaque= (EditText) findViewById(R.id.edtx_Empaque);
        edtx_OrdenProduccion = (EditText) findViewById(R.id.edtx_OrdenProduccion);
        tabla = (SortableTableView) findViewById(R.id.tableView);
        spnr_Maquinas = (Spinner) findViewById(R.id.spnr_Maquinas).findViewById(R.id.spinner);


        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Lote = (TextView) findViewById(R.id.txtv_Lote);
        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_CantidadEmpaques = (TextView) findViewById(R.id.txtv_CantidadEmpaques);
        txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_CantidadReg);
        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_OrdenProduccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);


        btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btn_CancelarTarima = (Button) findViewById(R.id.btn_CancelarTarima);
        btn_CerrarTarima.setEnabled(true);

        edtx_OrdenProduccion.requestFocus();
    }


    private void agregaListeners()
    {
        edtx_OrdenProduccion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_OrdenProduccion.getText().toString().equals(""))
                    {
                       new SegundoPlano("ConsultaOrdenProduccion").execute();
                    }else
                        {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_OrdenProduccion.setText("");
                                    edtx_OrdenProduccion.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_orden_produccion) ,false ,true , true);
                        }
                    new esconderTeclado(Almacen_Armado_Pallets_Liquidos.this);
                    return true;
                }
                return false;
            }
        });


        edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Empaque.getText().toString().equals(""))
                    {
                       new SegundoPlano("RegistrarEmpaque").execute();
                    }else
                        {
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_empaque) ,false ,true , true);

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Empaque.setText("");
                                    edtx_Empaque.requestFocus();
                                }
                            });
                        }
                    new esconderTeclado(Almacen_Armado_Pallets_Liquidos.this);
                    return  true;
                }
                return false;
            }
        });


        btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
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

                                new SegundoPlano("CerrarTarima").execute();
                                new esconderTeclado(Almacen_Armado_Pallets_Liquidos.this);
                            }
                        },null,contexto);
                    }
                     else
                     {
                         new popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , false,true , true);
                     }
            }
        });
        btn_CancelarTarima.setOnClickListener(new View.OnClickListener()
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

                           new SegundoPlano("CancelarTarima").execute();
                            new esconderTeclado(Almacen_Armado_Pallets_Liquidos.this);
                        }
                    },null,contexto);
                }
                else
                {
                    new popUpGenerico(contexto,vista ,getString(R.string.error_cancelar_pallet_sin_empaques) , false,true , true);
                }
            }
        });

        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());
        tabla.addDataLongClickListener(new ListenerLongClickTabla());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.cerrar_oc_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(!recargar)
            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, vista);
                    }
                if ((id == R.id.recargar))
                    {

                        if (!edtx_OrdenProduccion.getText().toString().equals(""))
                            {
                                tabla.getDataAdapter().clear();
                                new SegundoPlano("ConsultaOrdenProduccion").execute();
                            }
                    }
                if ((id == R.id.CerrarOC))
                {

                    if (!edtx_OrdenProduccion.getText().toString().equals(""))
                    {
//                        if(edtx_FechaCaducidad.getText().toString().equals(""))
//                        {
//                            new popUpGenerico(contexto,getCurrentFocus(),"Ingrese fecha de caducidad",false,true,true);
//                            return super.onOptionsItemSelected(item);
//                        }

                        new CreaDialogos(getString(R.string.pregunta_cierre_orden), new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id) {


//                                getSupportFragmentManager().beginTransaction()
//                                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                                        .replace(R.id.clayout,
//                                                frgmnt_Cierre_Orden.newInstance(new String[]{edtx_OrdenProduccion.getText().toString(),
//                                                        edtx_FechaCaducidad.getText().toString()}, null), frgmnt_tag).addToBackStack(frgmnt_tag)
//                                        .commit();


//                                SegundoPlano sp = new SegundoPlano("CerrarOrden");
//                                sp.execute();
                                new esconderTeclado(Almacen_Armado_Pallets_Liquidos.this);
                            }
                        },null,contexto);
                    }
                }
            }
        return super.onOptionsItemSelected(item);
    }

    private void ReiniciarVariables(String tarea)
    {
        switch (tarea)
            {
                case "ConsultaOrdenProduccion":
                    edtx_OrdenProduccion.setText("" );
                    txtv_Producto.setText("");
                    txtv_Lote.setText("");
                    txtv_Cantidad.setText("");
                    tabla.getDataAdapter().clear();
                    txtv_CantidadRegistrada.setText("");
                    edtx_OrdenProduccion.requestFocus();

                    break;
                case "ConsultarTarima":

                    tabla.getDataAdapter().clear();
                    edtx_Empaque.setText("");
                    txtv_CantidadEmpaques.setText("-");
                    edtx_Empaque.requestFocus();
                    break;
                case "RegistrarEmpaque":
                    edtx_Empaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "Reiniciar":
                    edtx_OrdenProduccion.setText("" );
                    txtv_Producto.setText("");
                    txtv_Lote.setText("");
                    txtv_Cantidad.setText("");
                    txtv_CantidadRegistrada.setText("");
                    txtv_CantidadEmpaques.setText("-");
                    tabla.getDataAdapter().clear();
                    edtx_OrdenProduccion.requestFocus();
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
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tabla.getDataAdapter().notifyDataSetChanged();
                btn_CerrarTarima.setTextColor(getApplication().getResources().getColor(R.color.blancoLetraStd));

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
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

                           new SegundoPlano("BajaEmpaque").execute();
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    //    taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!TextUtils.isEmpty(edtx_OrdenProduccion.getText())){
            new SegundoPlano("ConsultaOrdenProduccion").execute();
        }
    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao ;
        cAccesoADatos_RegistroPT cad = new cAccesoADatos_RegistroPT(Almacen_Armado_Pallets_Liquidos.this);
        cAccesoADatos_Consultas consAd = new cAccesoADatos_Consultas(Almacen_Armado_Pallets_Liquidos.this);

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
                        dao =  cad.c_ConsultaOrdenProduccion(edtx_OrdenProduccion.getText().toString());
                        break;

                    case "ConsultaMaquinas":
                        dao = consAd.c_ConsultaMaquinas(str_Maquina);
                        break;

                    case "ConsultarTarima":
                        dao =   cad.c_ConsultaEmpaquesArmadoProd(edtx_OrdenProduccion.getText().toString());
                        break;
                    case "RegistrarEmpaque":
                        //dao = cad.c_RegistraEmpaqueEnPallet(edtx_Empaque.getText().toString(), edtx_OrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato(),"0", binding.);
                        break;

                    case "CerrarTarima":
                        dao =   cad.c_CerrarRegistroPallet(edtx_OrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato());
                        break;
                    case "CancelarTarima":
                        dao = cad.c_CancelarRegistroPallet(edtx_OrdenProduccion.getText().toString() );
                        break;
                    case "CerrarOrden":
                        dao =    cad.c_CerrarOrdenProduccion_v2(edtx_OrdenProduccion.getText().toString(),"","");
                        break;

                    case "BajaEmpaque":
                        dao =    cad.c_BajaEmpaqueArmadoTarimas(edtx_OrdenProduccion.getText().toString(),EmpaqueBaja );
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
                                        txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                        txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTotal"));
                                        txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRegistrada"));
                                        txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Producto"));

                                        if(!dao.getcMensaje().equals("@"))
                                            {
                                                str_Maquina = dao.getcMensaje();
                                            }
                                        new SegundoPlano("ConsultaMaquinas").execute();
                                        new SegundoPlano("ConsultarTarima").execute();

                                        break;

                                    case "ConsultaMaquinas":

                                         spnr_Maquinas.setAdapter(new CustomArrayAdapter(Almacen_Armado_Pallets_Liquidos.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("DLinea","Linea")));

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
                                                        tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(Almacen_Armado_Pallets_Liquidos.this, dao.getcEncabezado()));
                                                        tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Armado_Pallets_Liquidos.this, arrayDatosTabla));
                                                        st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                                        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                                                        ReiniciarTabla = true;
                                                        tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                                                    }
                                            }


                                        txtv_CantidadEmpaques.setText(String.valueOf(cantInt));
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                        break;
                                    case "RegistrarEmpaque":
                                        str_Maquina = ((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato();// agarro la maquina del ultimo registro correcto
                                        new SegundoPlano("ConsultaOrdenProduccion").execute();
                                        break;

                                    case "CerrarTarima":
                                        new popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + dao.getcMensaje() + "] con éxito.", dao.iscEstado(), true, true);
                                        new SegundoPlano("ConsultaOrdenProduccion").execute();
                                        if (txtv_Cantidad.getText().toString().equals(txtv_CantidadRegistrada.getText().toString()))
                                            {
                                                new popUpGenerico(contexto,getCurrentFocus(), getString(R.string.orden_prod_completada_exito), dao.iscEstado(), true, true);
                                            }
                                        //ReiniciarVariables("Reiniciar");
                                        break;


                                    case "CancelarTarima":
                                        new popUpGenerico(contexto,getCurrentFocus(), getString(R.string.cancelar_tarima), dao.iscEstado(), true, true);
                                        new SegundoPlano("ConsultaOrdenProduccion").execute();
                                        break;

                                    case "CerrarOrden":

                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_prod_cerrar_exito), dao.iscEstado(), true, true);

                                        break;
                                    case "BajaEmpaque":
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                                        new SegundoPlano("ConsultaOrdenProduccion").execute();
                                        new  SegundoPlano("ConsultarTarima").execute();
                                        EmpaqueBaja=null;
                                        break;

                                }
                        }

                  else
                        {

                            ReiniciarVariables(tarea);
                            new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                }catch (Exception e)
                {
                    new popUpGenerico(contexto,getCurrentFocus(), e, dao.iscEstado(), true, true);
                    e.printStackTrace();
                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = false;
        }
    }


    public void onBackPressed()
    {
        Intent intent = new Intent(Almacen_Armado_Pallets_Liquidos.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }


}
