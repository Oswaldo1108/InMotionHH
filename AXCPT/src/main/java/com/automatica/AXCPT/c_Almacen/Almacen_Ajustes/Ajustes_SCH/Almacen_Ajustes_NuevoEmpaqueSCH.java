package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.InputFilter;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_NuevoEmpaque;
import com.automatica.AXCPT.c_Recepcion.Rec_Registro_Seleccion_Partida;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Almacen_Ajustes_NuevoEmpaqueSCH extends AppCompatActivity implements frgmnt_Seleccion_Producto.OnFragmentInteractionListener,frgmnt_taskbar_AXC.interfazTaskbar,frgmnt_SKU_Conteo.OnFragmentInteractionListener {

    //region variables
    EditText edtx_Cantidad,edtx_CodigoEmpaque,edtx_CodigoPallet,edtx_Producto_fragm, edtx_Caducidad;
    SortableTableView tabla;
    SimpleTableHeaderAdapter sthd;
    Handler handler = new Handler();
    SimpleTableDataAdapter st;
    int renglonSeleccionado;
    DatePickerFragment newFragment;
    int renglonAnterior=-1;
    boolean Seleccionado;
    Spinner spnr_Ajuste;

    frgmnt_taskbar_AXC taskbar_axc;


    String[] HEADER={"tabla","tabla1","tabla2","tabla3","tabla4","tabla5","tabla6"};
    View vista;
    Context contexto = this;
    private ProgressBarHelper p;

    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen__ajustes__nuevo_empaque_s_c_h);
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_NuevoEmpaqueSCH.this);
        declaraVariables();
        agregaListeners();
        SegundoPlano sp = new SegundoPlano("ListarAjustes");
        sp.execute();
        View logoView = getToolbarLogoIcon((Toolbar) findViewById(R.id.toolbar));
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .replace(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(findViewById(R.id.FrameLayout).getId(),taskbar_axc,"FragmentoTaskBar").commit();


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
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
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }
        if((id == R.id.borrar_datos))
        {
            reiniciaCampos();
            edtx_CodigoPallet.setText("");
            edtx_CodigoPallet.requestFocus();

        }

        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {
try
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setTitle(getString(R.string.Ajustes_nuevo_empaque));
        p = new ProgressBarHelper(this);

        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_CodigoEmpaque);
        tabla = findViewById(R.id.tableView_OC);
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtx_CodigoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        spnr_Ajuste = (Spinner) findViewById(R.id.spnr_Ajuste);
        edtx_Producto_fragm = findViewById(R.id.edtx_Producto_fragm);
        edtx_Caducidad = findViewById(R.id.edtx_Caducidad);
    }catch (Exception e)
    {
        e.printStackTrace();
        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

    }
    }
    private void agregaListeners()
    {
/*        edtx_Caducidad.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                try {

                    if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                final String selectedDate = day + "-" + (month + 1) + "-" + year;
                                edtx_Caducidad.setText(selectedDate);





                            }
                        });

                        newFragment.show(getSupportFragmentManager(), "datePicker");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }

                return false;
            }
        });*/


        edtx_Cantidad.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View edtx, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(edtx_Cantidad.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, edtx_Cantidad, getString(R.string.error_ingrese_cantidad), false, true, true);
                                return false;
                            }
                        edtx_CodigoEmpaque.requestFocus();

                        new esconderTeclado(Almacen_Ajustes_NuevoEmpaqueSCH.this);

                    }
                return false;
            }
        });




        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View edtx, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, edtx, getString(R.string.error_ingrese_pallet), false, true, true);
                            edtx_CodigoPallet.setText("");
                            edtx_CodigoPallet.requestFocus();
                            return false;
                        }
                    new SegundoPlano("ConsultaPallet").execute();


                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaqueSCH.this);

                }
                return false;
            }
        });
        edtx_Cantidad.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {

                if(edtx_Producto_fragm.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto,edtx_Producto_fragm,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                    return false;
                }
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                        .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_Producto_fragm.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();


                return true;
            }
        });
        edtx_Producto_fragm.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v) {
                final Vibrator vibratorService = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
                vibratorService.vibrate(150);

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.cl, frgmnt_Seleccion_Producto.newInstance(null,""), "ElegirPallet").addToBackStack("ElegirPallet")
                        .commit();
                return true;
            }
        });
        edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View edtx, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_CodigoPallet,getString(R.string.error_ingrese_pallet),false,true,true);
                            return false;
                        }
                    if(edtx_Producto_fragm.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_Producto_fragm,getString(R.string.error_ingrese_producto),false,true,true);
                            return false;
                        }

              /*      if(edtx_Caducidad.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_Caducidad,getString(R.string.error_ingrese_caducidad),false,true,true);
                            return false;
                        }*/
                /*    if(edtx_Caducidad.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_Caducidad,getString(R.string.error_ingrese_caducidad),false,true,true);
                            return false;
                        }*/
                    if(edtx_Cantidad.getText().toString().equals(""))
                        {
                           new popUpGenerico(contexto,edtx_Cantidad,getString(R.string.error_ingrese_cantidad),false,true,true);
                            return false;
                        }
                    if(edtx_CodigoEmpaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx,getString(R.string.error_ingrese_empaque),false,true,true);
                            return false;
                        }
                    if(Integer.valueOf(edtx_CodigoEmpaque.getText().toString())<=0)
                    {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), false, true, true);

                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_CodigoEmpaque.requestFocus();
                                    edtx_CodigoEmpaque.setText("");
                                }
                            });
                            return false;
                    }

                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaqueSCH.this);
                    new SegundoPlano("AjusteNuevoEmpaquePalletExistente").execute();
                }
                return false;
            }
        });

        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());
        tabla.addDataLongClickListener(new ListenerLongClickTabla());

        edtx_Producto_fragm.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
        {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

    }
    private void reiniciaCampos()
    {
        edtx_Cantidad.setText("");
        edtx_CodigoEmpaque.setText("");
        edtx_Producto_fragm.setText("");
        tabla.getDataAdapter().clear();
        edtx_CodigoEmpaque.requestFocus();
       // edtx_Caducidad.setText("");
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada) {
        edtx_Cantidad.setText(strCantidadEscaneada);
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_CodigoEmpaque.requestFocus();
            }
        },100);
    }

    @Override
    public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {
        try {
            edtx_Producto_fragm.setText(prmProducto);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
            {
                Seleccionado = true;
                renglonAnterior = rowIndex;
                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                edtx_Producto_fragm.setText(clickedData[0]);
                edtx_CodigoEmpaque.setText(clickedData[2]);
                edtx_Cantidad.setText(clickedData[3]);
                edtx_Caducidad.setText(clickedData[4]);

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                renglonAnterior=-1;
                Seleccionado = false;
                edtx_Producto_fragm.setText("");
                edtx_CodigoEmpaque.setText("");
                edtx_Cantidad.setText("");
                edtx_Caducidad.setText("");
            }
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

            }
            else
            {
                Color = R.color.Transparente;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            }
            return new ColorDrawable(getResources().getColor(Color));
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
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {
            String notifyText = HEADER[columnIndex];
            Toast.makeText(contexto, notifyText, Toast.LENGTH_SHORT).show();

        }
    }
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
            String DataToShow="";
            int i =0;
            for(String data:clickedData)
            {
                DataToShow +=HEADER[i] +" - "+ data + "\n";
                i++;
            }
            new popUpGenerico(contexto, vista, DataToShow, "Información", true, false);

            return false;
        }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao = new DataAccessObject();
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Ajustes_NuevoEmpaqueSCH.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
           p.ActivarProgressBar(tarea);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            String CodigoPallet = edtx_CodigoPallet.getText().toString();

            switch (tarea)
            {

                case "ConsultaPallet":
                    //sa.SOAPConsultaPallet(CodigoPallet,contexto);
                    dao = ca.c_ConsultaEmpaquesPallet_NE(CodigoPallet);
                    break;

                case "ListarAjustes"://Se usa para llenar el Spinner
                    //sa.SOAPListarConceptosAjuste("1",contexto);
                    dao= ca.c_ListarConceptosAjuste("1");
                    break;
                case "AjusteNuevoEmpaquePalletExistente":
                    String Cantidad = edtx_Cantidad.getText().toString();
                    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                    String TipoEvento = spnr_Ajuste.getSelectedItem().toString();
                    String Revision ="";
                    //sa.SOAPAjusteNuevoEmpaquePalletExistente(CodigoEmpaque,CodigoPallet,Producto,Cantidad,Revision,TipoEvento,contexto);
                    dao = ca.c_AjusteNuevoEmpaque_NE(CodigoEmpaque,edtx_CodigoPallet.getText().toString(),edtx_Producto_fragm.getText().toString(),Cantidad,TipoEvento,"");
                    break;


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try{
                if (dao.iscEstado())
                {
                    switch (tarea)
                    {


                        case "ConsultaPallet":
                            HEADER = dao.getcEncabezado();
                            tabla.setDataAdapter(st= new SimpleTableDataAdapter(Almacen_Ajustes_NuevoEmpaqueSCH.this,dao.getcTablaUnica()));
                            tabla.setHeaderAdapter(sthd= new SimpleTableHeaderAdapter(Almacen_Ajustes_NuevoEmpaqueSCH.this,dao.getcEncabezado()));
                            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            tabla.getDataAdapter().notifyDataSetChanged();
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                            break;
                        case "ListarAjustes":
                            spnr_Ajuste.setAdapter(new CustomArrayAdapter(Almacen_Ajustes_NuevoEmpaqueSCH.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","Tag1","")));
                            break;
                        case "AjusteNuevoEmpaquePalletExistente":
                            new popUpGenerico(contexto, vista, "Empaque registrado con éxito.","Registrado", true, true);
                            new SegundoPlano("ConsultaPallet").execute();
                            reiniciaCampos();
                            break;
                    }
                }else
                {
                    new popUpGenerico(contexto, vista, dao.getcMensaje(),"Advertencia", true, true);
                    reiniciaCampos();
                }
            }catch (Exception e){
                e.printStackTrace();
                //new popUpGenerico(Almacen_Ajustes_NuevoEmpaqueSCH.this,getCurrentFocus(),dao.getcMensaje(),dao.iscEstado(),true,true);
            }

            p.DesactivarProgressBar(tarea);
        }
    }
    @Override
    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
            taskbar_axc.cerrarFragmento();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}