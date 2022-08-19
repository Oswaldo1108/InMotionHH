package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes;

import android.app.DatePickerDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.os.Vibrator;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionar;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Almacen_Ajustes_NuevoEmpaque extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar,TableViewDataConfigurator.TableClickInterface, frgmnt_SKU_Conteo.OnFragmentInteractionListener,frgmnt_Seleccion_Producto.OnFragmentInteractionListener
{
    //region variables
    private EditText edtx_SKU,edtx_CodigoEmpaque,edtx_CodigoPosicion,edtx_CantXEmp, edtxNumSerie;
    private frgmnt_taskbar_AXC taskbar_axc;
    private SortableTableView tabla;
    private TableViewDataConfigurator ConfigTabla_Totales = null;
    private Spinner spnr_Ajuste;
    private View vista;
    CheckBox checkNumSerie;
    private Context contexto = this;
    private DatePickerFragment newFragment;

    private CheckBox cb_DatosPedimento,cb_EditarCantidad;
    private EditText edtx_Pedimento,edtx_ClavePedimento,edtx_Factura,edtx_FechaPedimento,edtx_FechaRecibo;

    Handler h = new Handler();

    private ProgressBarHelper p;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_ajustes__nuevo_empaque_cant_mod);
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_NuevoEmpaque.this);
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
                            .add(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(findViewById(R.id.Pantalla_principal).getId(),taskbar_axc,"FragmentoTaskBar").commit();

        tabla = findViewById(R.id.tableView_OC);

        edtx_CodigoPosicion.requestFocus();

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
            edtx_CodigoPosicion.setText("");
            edtx_CodigoPosicion.requestFocus();

        }

        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setTitle(" "+getString(R.string.Ajustes_nuevo_empaque));

        p= new ProgressBarHelper(this);
        edtx_CodigoPosicion= (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_SKU = (EditText) findViewById(R.id.edtx_SKU);
        edtx_CodigoPosicion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_SKU.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtxNumSerie = (EditText) findViewById(R.id.edtxNumSerie);
        cb_DatosPedimento= (CheckBox) findViewById(R.id.cb_DatosPedimento);
        edtx_ClavePedimento     = (EditText) findViewById(R.id.edtx_ClavePedimento);
        edtx_Factura            = (EditText) findViewById(R.id.edtx_Factura);
        edtx_FechaPedimento     = (EditText) findViewById(R.id.edtx_FechaPedimento);
        edtx_FechaRecibo        = (EditText) findViewById(R.id.edtx_FechaRecibo);
        checkNumSerie = (CheckBox) findViewById(R.id.checkNumSerie);

        cb_EditarCantidad= (CheckBox) findViewById(R.id.cb_EditarCantidad);
        edtx_CantXEmp = (EditText) findViewById(R.id.edtx_CantXEmp);

        edtx_Pedimento = (EditText) findViewById(R.id.edtx_Pedimento);

        spnr_Ajuste= (Spinner) findViewById(R.id.spnr_Ajuste);

        edtx_Pedimento.setEnabled(false);
        edtx_ClavePedimento.setEnabled(false);
        edtx_Factura.setEnabled(false);
        edtx_FechaPedimento.setEnabled(false);
        edtx_FechaRecibo.setEnabled(false);
        // Spinner spinner = new Spinner(this);

        edtx_CantXEmp.setText("1");
        edtx_CantXEmp.setEnabled(false);

    }
    private void agregaListeners()
    {

        cb_EditarCantidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                edtx_CantXEmp.setEnabled(b);
                if(b)
                {
                    edtx_CantXEmp.setText("");
                }else
                {
                    edtx_CantXEmp.setText("1");
                }
            }
        });



        cb_DatosPedimento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                edtx_Pedimento.setEnabled(b);
                edtx_ClavePedimento.setEnabled(b);
                edtx_Factura.setEnabled(b);
                edtx_FechaPedimento.setEnabled(b);
                edtx_FechaRecibo.setEnabled(b);
            }
        });


        edtx_FechaPedimento.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final String selectedDate = year  + "/" + (month + 1) + "/" + day;
                        edtx_FechaPedimento.setText(selectedDate);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
                return false;
            }
        });

        edtx_FechaRecibo.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        final String selectedDate = year  + "/" + (month + 1) + "/" + day;
                        edtx_FechaRecibo.setText(selectedDate);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
                return false;
            }
        });


        edtx_ClavePedimento.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_ClavePedimento.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,edtx_ClavePedimento,getString(R.string.error_ingrese_clave_pedimento),"Advertencia",true,true);
                        edtx_ClavePedimento.setText("");
                        edtx_ClavePedimento.requestFocus();
                        return false;
                    }

                    edtx_Factura.requestFocus();

                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);

                }
                return false;
            }
        });

        edtx_Factura.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_Factura.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_factura_pedimento),"Advertencia",true,true);
                        edtx_Factura.setText("");
                        edtx_Factura.requestFocus();
                        return false;
                    }

                    edtx_SKU.requestFocus();

                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);

                }
                return false;
            }
        });

        checkNumSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNumSerie.isChecked())
                    edtxNumSerie.setEnabled(true);
                else
                    edtxNumSerie.setEnabled(false);
            }
        });



/*        edtx_CodigoPosicion.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                if(b)
                {

                    if (ConfigTabla_Totales != null)
                    {
                        edtx_CodigoPosicion.setText("");
                        ConfigTabla_Totales.CargarDatosTabla(null);
                    }
                }
            }
        });*/

        edtx_SKU.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });

/*        edtx_SKU.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                if(b)
                {
                    edtx_SKU.setText("");
                    edtx_CodigoEmpaque.setText("");

                    edtx_SKU.requestFocus();
                }
            }
        });*/

        edtx_CodigoPosicion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPosicion.getText().toString().equals(""))
                    {
                       SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                       sp.execute();

                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_nueva_posicion),"Advertencia",true,true);
                        edtx_CodigoPosicion.setText("");
                        edtx_CodigoPosicion.requestFocus();
                    }

                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);

                }
                return false;
            }
        });

        edtx_SKU.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v) {
                final Vibrator vibratorService = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.Pantalla_principal, frgmnt_Seleccion_Producto.newInstance(null,""), "ElegirPallet").addToBackStack("ElegirPallet")
                        .commit();
                return true;
            }
        });

        edtx_SKU.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {


                    if(edtx_SKU.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,vista,"Ingrese un SKU o UPC.","false",true,true);
                        edtx_SKU.setText("");
                        edtx_CodigoEmpaque.setText("");
                        edtx_SKU.requestFocus();
                        return false;
                    }

                    h.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            edtx_CodigoEmpaque.requestFocus();
                            edtx_CodigoEmpaque.setText("");
                        }
                    },100);

                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);
                }
                return false;
            }
        });


        edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    if(edtx_SKU.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,vista,"Ingrese un SKU o UPC.",false,true,true);
                        edtx_SKU.setText("");
                        edtx_CodigoEmpaque.setText("");
                        edtx_SKU.requestFocus();
                        return false;
                    }

                    if(edtx_CodigoEmpaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,vista,"Ingrese un código serial." ,false,true,true);
                        edtx_SKU.setText("");
                        edtx_CodigoEmpaque.setText("");
                        edtx_SKU.requestFocus();
                        return false;
                    }

                    if(cb_DatosPedimento.isChecked())
                    {



                        if(edtx_Pedimento.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_Pedimento,getString(R.string.error_ingrese_pedimento),false,true,true);
                            return false;
                        }

                        if(edtx_ClavePedimento.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_ClavePedimento,getString(R.string.error_ingrese_clave_pedimento),false,true,true);
                            return false;
                        }
                        if(edtx_Factura.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_Factura,getString(R.string.error_ingrese_factura_pedimento),false,true,true);
                            return false;
                        }
                        if(edtx_FechaPedimento.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_FechaPedimento,getString(R.string.error_ingrese_fecha_pedimento),false,true,true);
                            return false;
                        }
                        if(edtx_FechaRecibo.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,edtx_FechaRecibo,getString(R.string.error_ingrese_fecha_rec_pedimento),false,true,true);
                            return false;
                        }

                    }

                    if(edtx_SKU.getText().toString().contains("WTU"))
                    {

                        MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.beep);
                        mp.start();

                        Vibrator vibra = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);

                        vibra.vibrate(150);

                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_SKU.setText("");
                                edtx_CodigoEmpaque.setText("");
                                edtx_SKU.requestFocus();
                            }
                        },100);
                        return false;
                    }

                    if(!edtx_CodigoEmpaque.getText().toString().contains("WTU")&&!edtx_CodigoEmpaque.getText().toString().startsWith("P"))
                    {

                        MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.beep);
                        mp.start();

                        Vibrator vibra = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);

                        vibra.vibrate(150);

                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_SKU.setText("");
                                edtx_CodigoEmpaque.setText("");
                                edtx_SKU.requestFocus();
                            }
                        },100);
                        return false;
                    }
                    if(edtx_CodigoEmpaque.getText().toString().length()<12&&!(edtx_CodigoEmpaque.getText().toString().startsWith("P")&&edtx_CodigoEmpaque.getText().toString().length()==10))
                    {
                        new popUpGenerico(contexto,edtx_SKU,"Valide que haya escaneado un código serial ó de empaque. ["  + edtx_CodigoEmpaque.getText().toString()+ "]",false,true,true);

                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_SKU.setText("");
                                edtx_CodigoEmpaque.setText("");
                                edtx_SKU.requestFocus();
                            }
                        },100);
                        return false;
                    }

                    if(cb_DatosPedimento.isChecked())
                    {
                        new SegundoPlano("AjusteNuevoEmpaquePalletExistentePedimento").execute();

                    }else
                    {
                        new SegundoPlano("AjusteNuevoEmpaquePalletExistente").execute();
                    }


                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);
                }
                return false;
            }
        });


        edtx_CantXEmp.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {

                if(edtx_SKU.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto,edtx_SKU,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                    return false;
                }
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                        .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_SKU.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();


                return true;
            }
        });



        edtx_CantXEmp.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
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
            edtx_SKU.setText("");
            edtx_CodigoEmpaque.setText("");
            edtxNumSerie.setText("");
            //edtx_CodigoPosicion.setText("");
            edtx_CodigoEmpaque.requestFocus();
//            txtv_Empaques.setText("");
//            txtv_Lote.setText("");
//            txtv_Cantidad.setText("");
//            txtv_Empaques.setText("");
//            txtv_Producto.setText("");
        }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return false;
    }

    @Override
    public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {
        try
        {
            Handler h = new Handler();
            edtx_SKU.setText(prmProducto);

            if(edtx_SKU.getText().toString().equals(""))
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        edtx_SKU.requestFocus();

                    }
                },100);
            }

            edtxNumSerie.requestFocus();




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada)
    {
        edtx_CantXEmp.setText(strCantidadEscaneada);
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_CodigoEmpaque.requestFocus();
            }
        },100);
    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Ajustes_NuevoEmpaque.this);

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

            String CodigoPallet = edtx_CodigoPosicion.getText().toString();

            switch (tarea)
            {

                case "ConsultaPallet":
                    //sa.SOAPConsultaPallet(CodigoPallet,contexto);
                    dao = ca.c_ConsultaPosicion(CodigoPallet);
                    break;

                case "ListarAjustes"://Se usa para llenar el Spinner
                    //sa.SOAPListarConceptosAjuste("1",contexto);
                    dao= ca.c_ListarConceptosAjuste("1");
                    break;
                case "AjusteNuevoEmpaquePalletExistente":
                    dao = ca.c_AjusteNuevoEmpaquePalletExistente(
                                    edtx_CodigoEmpaque.getText().toString(),
                                    CodigoPallet,
                                    edtx_SKU.getText().toString(),
                                    edtx_CantXEmp.getText().toString(),
                                    "",
                                    spnr_Ajuste.getSelectedItem().toString(),
                                    edtxNumSerie.getText().toString());
                    break;

                case "AjusteNuevoEmpaquePalletExistentePedimento":
                    dao = ca.c_AjusteNuevoEmpaquePalletExistentePedimento(
                            edtx_CodigoEmpaque.getText().toString(),
                            CodigoPallet,
                            edtx_SKU.getText().toString(),
                            edtx_CantXEmp.getText().toString(),
                            edtx_Pedimento.getText().toString(),
                            edtx_ClavePedimento.getText().toString(),
                            edtx_Factura.getText().toString(),
                            edtx_FechaPedimento.getText().toString(),
                            edtx_FechaRecibo.getText().toString(),
                            spnr_Ajuste.getSelectedItem().toString());
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
//                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
//                            txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteAXC"));
//                            txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
//                            txtv_Empaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));

                            if (ConfigTabla_Totales == null)
                            {

                                ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao, Almacen_Ajustes_NuevoEmpaque.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }


                            h.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_SKU.requestFocus();

                                }
                            },250);

                            break;
                        case "ListarAjustes":
                            spnr_Ajuste.setAdapter(new CustomArrayAdapter(Almacen_Ajustes_NuevoEmpaque.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","Tag1","")));
                            break;
                        case "AjusteNuevoEmpaquePalletExistente":
                            new SegundoPlano("ConsultaPallet").execute();
                           // edtx_SKU.setText("");
                            edtx_CodigoEmpaque.setText("");
                            edtxNumSerie.setText("");
                            if (checkNumSerie.isChecked()){
                                edtxNumSerie.requestFocus();
                            }else
                                edtx_CodigoEmpaque.requestFocus();
                            //edtx_SKU.requestFocus();



                            MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.tilin);
                            mp.start();

                            Vibrator vibra = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);

                            vibra.vibrate(150);

                            break;
                    }
                }else
                {

                    switch (tarea)
                    {


                        case "ConsultaPallet":
                            new popUpGenerico(contexto, edtx_CodigoPosicion, dao.getcMensaje(),false, true, true);
                            edtx_CodigoPosicion.setText("");
                            edtx_CodigoPosicion.requestFocus();
                            break;
                        case "ListarAjustes":
                            new popUpGenerico(contexto, null, dao.getcMensaje(),false, true, true);
                            break;
                        case "AjusteNuevoEmpaquePalletExistente":
                            new popUpGenerico(contexto, edtx_SKU, dao.getcMensaje(),false, true, true);
                            edtx_SKU.setText("");
                            edtx_CodigoEmpaque.setText("");
                            edtxNumSerie.setText("");
                            edtx_SKU.requestFocus();
                            break;
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
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