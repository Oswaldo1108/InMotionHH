package com.automatica.AXCPT.c_Inventarios.CreacionMaterial;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.InvActRegPalletNuevoNeBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Inventarios_RegPalletNuevoNE extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar,frgmnt_SKU_Conteo.OnFragmentInteractionListener
{
    //region variables
    private TextView txtv_Inventario, txtv_Posicion;


    private ProgressBarHelper progressBarHelper;
    private DatePickerFragment newFragment;
    private EditText edtx_Producto, edtx_Cantidad, edtxSku,edtxCantidadXEmpque,edtxPedimento,edtxClavePedimento,edtxFactura,edtxFechaPedimento,edtxFechaRecepcion;
    private Spinner spnr_Prod,spnr_Impresora;

    private TextView txtv_EmpaquesRegistrados,txtv_PalletReg;
    private Bundle b;
    private String UbicacionIntent, IdInventario;
    private Context contexto = this;

    frgmnt_taskbar_AXC taskbar_axc;

    private InvActRegPalletNuevoNeBinding binding;
    //endregion
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            binding = InvActRegPalletNuevoNeBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(getString(R.string.Inventarios_PalletNuevo));
            this.getSupportActionBar().setSubtitle("No Etiquetado");

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

            taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
            getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();

            declararVariables();
            AgregaListeners();
            SacaExtrasIntent();

            new SegundoPlano("ConsultaPallet").execute();
            new SegundoPlano("ListaImpresoras").execute();

            edtx_Producto.requestFocus();
            Log.i("PRUEBA",IdInventario);
            txtv_Inventario.setText(IdInventario);
            txtv_Posicion.setText(UbicacionIntent);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
            return true;
        } catch (Exception ex)
        {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if ((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto, null);
        }
        if ((id == R.id.borrar_datos))
        {

            reiniciaVariables();
        }

        return super.onOptionsItemSelected(item);
    }

    private void reiniciaVariables()
    {
        edtx_Producto.setText("");
        edtx_Cantidad.setText("");
        edtxSku.setText("");
        edtx_Cantidad.setText("");
        edtxCantidadXEmpque.setText("");
        spnr_Prod.setAdapter(null);
        edtxPedimento.setText("");
        edtxClavePedimento.setText("");
        edtxFactura.setText("");
        edtxFechaPedimento.setText("");
        edtxFechaRecepcion.setText("");
    }

    private void declararVariables()
    {
        try
        {
            progressBarHelper= new ProgressBarHelper(this);
            edtx_Producto = (EditText) findViewById(R.id.txtv_Producto);
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_CodigoPallet);
            edtxSku = (EditText) findViewById(R.id.edtx_sku);
            edtx_Producto.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtxSku.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtxPedimento = findViewById(R.id.edtxPedimiento);
            edtxPedimento.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtxCantidadXEmpque = findViewById(R.id.edtxCantidadXEmpque);

            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpaquesReg);
            txtv_PalletReg = (TextView) findViewById(R.id.txtv_PalletReg);
            spnr_Prod = findViewById(R.id.vw_spinner_prod).findViewById(R.id.spinner);
            spnr_Impresora = findViewById(R.id.vw_spinner_imp).findViewById(R.id.spinner);

            txtv_Inventario = (TextView) findViewById(R.id.txtv_Inventario);
            txtv_Posicion = (TextView) findViewById(R.id.txtv_Posicion);

            edtxClavePedimento = findViewById(R.id.edtxclavepedimiento);
            edtxClavePedimento.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtxFactura = findViewById(R.id.edtxFactura);
            edtxFactura.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtxFechaPedimento = findViewById(R.id.edtxFechaPedimiento);
            edtxFechaRecepcion = findViewById(R.id.edtxFechaRecepcion);

        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }

    }

    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            UbicacionIntent = b.getString("UbicacionIntent");
            IdInventario = b.getString("IdInv");
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    private void AgregaListeners()
    {
        try
        {
            edtx_Producto.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_Producto.getText().toString().equals(""))
                        {
                            new SegundoPlano("ConsultaProducto").execute();
                            edtxCantidadXEmpque.requestFocus();
                        } else {

                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese un código de producto correcto.", "Advertencia", true, true);

                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_Producto.setText("");
                                            edtx_Producto.requestFocus();
                                        }
                                    }
                            );
                        }
                    }
                    return false;
                }
            });


            edtx_Cantidad.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_Cantidad.getText().toString().equals("")) {
                            //Toast.makeText(Inventario_RegPalletNuevo.this,  "Ingrese una cantida correcta.", Toast.LENGTH_SHORT).show();

                            handler.post(
                                    new Runnable() {
                                        public void run() {

                                            edtxSku.requestFocus();
                                        }
                                    }
                            );
                        } else {
                            new popUpGenerico(contexto, null, getString(R.string.ingrese_cantidad), "Advertencia", true, true);
                            Log.i("PRUEBA",((Constructor_Dato)spnr_Prod.getSelectedItem()).getTag2());

                            edtx_Cantidad.setText("");
                            edtx_Cantidad.requestFocus();

                        }
                    }
                    return false;
                }
            });
            edtx_Cantidad.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {

                    if(edtx_Producto.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,edtx_Producto,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                        return false;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_Producto.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();


                    return true;
                }
            });



            edtx_Cantidad.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if (Float.parseFloat(edtx_Cantidad.getText().toString()) > 999999)
                            {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_Cantidad.requestFocus();
                                                edtx_Cantidad.setText("");
                                            }
                                        });
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                            }
                        } catch (NumberFormatException ex)
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();

                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                        }

                        if (edtx_Cantidad.getText().toString().equals("")||Double.parseDouble(edtx_Cantidad.getText().toString())<=0)
                        {

                            new popUpGenerico(contexto, null, getString(R.string.ingrese_cantidad), "Advertencia", true, true);

                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_Cantidad.setText("");
                                            edtx_Cantidad.requestFocus();
                                        }
                                    }
                            );

                            handler.post(
                                    new Runnable()
                                    {
                                        public void run()
                                        {

                                            edtxSku.requestFocus();
                                        }
                                    }
                            );
                            return false;

                        } else{
                            if(((Constructor_Dato)spnr_Prod.getSelectedItem()).getTag2().equals(edtxSku.getText().toString())){

                                new SegundoPlano("RegistrarEmpaqueNuevo").execute();

                            }else{
                                new popUpGenerico(contexto,getCurrentFocus(),"SKU diferente al producto seleccionado",false,true,true);
                                edtxSku.setText("");
                                edtxSku.requestFocus();
                            }
                        }
                    }
                    return false;
                }
            });

            edtxSku.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(TextUtils.isEmpty(edtxSku.getText().toString())){

                        new popUpGenerico(contexto, getCurrentFocus(), "Ingrese SKU", "Advertencia", true, true);

                    }else{


                    }

                    return false;
                }
            });
            edtxPedimento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(TextUtils.isEmpty(edtxPedimento.getText().toString())){

                        new popUpGenerico(contexto, getCurrentFocus(), "Ingrese un pedimento", "Advertencia", true, true);

                    }else{


                    }

                    return false;
                }
            });
            edtxClavePedimento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(TextUtils.isEmpty(edtxClavePedimento.getText().toString())){

                        new popUpGenerico(contexto, getCurrentFocus(), "Ingrese una clave de pedimento", "Advertencia", true, true);

                    }else{


                    }

                    return false;
                }
            });
            edtxFactura.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(TextUtils.isEmpty(edtxFactura.getText().toString())){

                        new popUpGenerico(contexto, getCurrentFocus(), "Ingrese una factura", "Advertencia", true, true);

                    }else{

                        new esconderTeclado(Inventarios_RegPalletNuevoNE.this);
                        edtxFechaPedimento.callOnClick();

                    }

                    return false;
                }
            });
            edtxFechaPedimento.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            final String selectedDate = year  + "/" + (month + 1) + "/" + day;
                            edtxFechaPedimento.setText(selectedDate);
                            edtxFechaRecepcion.callOnClick();
                        }
                    });
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            });
            edtxFechaRecepcion.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            final String selectedDate = year  + "/" + (month + 1) + "/" + day;
                            edtxFechaRecepcion.setText(selectedDate);
                            edtx_Cantidad.requestFocus();
                        }
                    });
                    newFragment.show(getSupportFragmentManager(), "datePicker");

                }
            });
        } catch (Exception e)
        {
            new popUpGenerico(contexto, null, e.getMessage(), "Advertencia", true, true);
        }
    }

    @Override
    public void BotonDerecha() {

        if (txtv_EmpaquesRegistrados.getText().toString().equals("-"))
        {
            handler.post(
                    new Runnable()
                    {
                        public void run()
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cerrar_pallet_sin_empaques), false, true, true);
                        }
                    });
            return;
        }

        if (spnr_Impresora.getAdapter() == null)
        {
            handler.post(
                    new Runnable()
                    {
                        public void run()
                        {
                            new popUpGenerico(contexto, getCurrentFocus(),"No hay una impresora seleccionada.", false, true, true);
                        }
                    });
            return;
        }

        new CreaDialogos("¿Cerrar tarima [" + txtv_PalletReg.getText().toString() + "]?",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        new SegundoPlano("RegistraPalletNuevo").execute();
                    }
                }, null,Inventarios_RegPalletNuevoNE.this);

    }

    @Override
    public void BotonIzquierda() {
          onBackPressed();
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada) {
        edtx_Cantidad.setText(strCantidadEscaneada);
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        String tarea;
        DataAccessObject dao;
        adInventarios ca = new adInventarios(contexto);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            progressBarHelper.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case "ConsultaPallet":
                        dao = ca.c_ConsultaPalletAbiertoInv_NE(IdInventario);
                        break;

                    case "ListaImpresoras":
                        dao= ca.c_ListaImpresoras();
                        break;
                    case "ConsultaProducto":
                        dao = ca.c_ConsultaCoincidenciaArticulo(edtx_Producto.getText().toString());
                        break;
                    case "RegistrarEmpaqueNuevo":
                        dao = ca.c_RegistraEmpaqueNuevoPalletInventario_NE(IdInventario,
                                edtx_Cantidad.getText().toString(),
                                spnr_Prod.getSelectedItem().toString(),
                                edtxCantidadXEmpque.getText().toString(),
                                txtv_Posicion.getText().toString(),
                                edtxPedimento.getText().toString(),
                                edtxClavePedimento.getText().toString(),
                                edtxFactura.getText().toString(),
                                edtxFechaPedimento.getText().toString(),
                                edtxFechaRecepcion.getText().toString());
                        break;
                    case "RegistraPalletNuevo":
                        dao = ca.c_CierraPalletInventario(IdInventario,txtv_PalletReg.getText().toString(),spnr_Impresora.getSelectedItem().toString());
                        break;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
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
                        case "ConsultaProducto":
                            spnr_Prod.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item,
                                    dao.getcTablasSorteadas("NumParte", "UnidadMedida","NumParte")));
                            edtxCantidadXEmpque.requestFocus();
                            break;

                        case "ListaImpresoras":
                            spnr_Impresora.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item,
                                    dao.getcTablasSorteadas("Impresora", "Impresora")));
                            break;
                        case "ConsultaPallet":
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_PalletReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Pallet"));
                            edtx_Producto.requestFocus();
                            break;
                        case "RegistrarEmpaqueNuevo":
                            edtx_Cantidad.setText("");
                            new SegundoPlano("ConsultaPallet").execute();
                            edtxSku.requestFocus();
                            break;

                        case "RegistraPalletNuevo":
                            edtx_Producto.setText("");
                            edtx_Cantidad.setText("");
                            edtxSku.setText("");
                            edtxCantidadXEmpque.setText("");
                            txtv_EmpaquesRegistrados.setText("");
                            txtv_PalletReg.setText("");
                            edtxPedimento.setText("");
                            edtxClavePedimento.setText("");
                            edtxFactura.setText("");
                            edtxFechaPedimento.setText("");
                            edtxFechaRecepcion.setText("");
                            spnr_Prod.setAdapter(null);

                            new popUpGenerico(contexto, getCurrentFocus(), "Pallet [" + dao.getcMensaje() + "] cerrado con éxito.", dao.iscEstado(), true, true);
                            break;
                    }
                } else
                {
                    switch (tarea)
                    {
                        case "ConsultaProducto":
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);

                            edtx_Producto.requestFocus();
                            edtx_Producto.setText("");
                            break;

                        case "RegistrarEmpaqueNuevo":

                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);

                            edtxSku.setText("");
                            edtx_Cantidad.setText("");
                            edtxSku.requestFocus();
                            break;

                        default:
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            break;
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(), false, true, true);
            }
            progressBarHelper.DesactivarProgressBar(tarea);
        }
    }

    @Override
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
}
