package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_Ciesa;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;
import com.automatica.AXCPT.databinding.ActivityAjustesAjustesContenedorBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Ajustes_AjustesContenedorBaja extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar,frgmnt_Seleccion_Producto.OnFragmentInteractionListener
{
    //region variables
    EditText edtx_CodigoEmpaque,edtx_CodigoPallet,edtx_Producto_fragm;
    Spinner spnr_Alta,spnr_Baja;
    ActivityAjustesAjustesContenedorBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    TextView txtv_Producto,txtv_Cantidad;
    private ProgressBarHelper p;

    View vista;
    Context contexto = this;
    popUpGenerico pop;

    private ActivityHelpers activityHelpers;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding= ActivityAjustesAjustesContenedorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Ajustes_AjustesContenedorBaja.this);
        pop = new popUpGenerico(Ajustes_AjustesContenedorBaja.this);
        declaraVariables();
        agregaListeners();
        //new SegundoPlano("ListarAjustesAltas").execute();
        new SegundoPlano("ListarAjustesBajas").execute();
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenus(this,R.id.Pantalla_principal,false);


        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.i("LogCheck", String.valueOf(checkedId));
                switch (checkedId)
                {
                    case R.id.radio_alta:
                        binding.spinnerBaja.setEnabled(false);
                        binding.spinnerAlta.setEnabled(true);
                        binding.edtxProductoFragm.setEnabled(true);
                        binding.spinnerBaja.findViewById(R.id.spinner).setEnabled(false);
                        binding.spinnerAlta.findViewById(R.id.spinner).setEnabled(true);
                        reiniciaCampos();
                        binding.switch1.setEnabled(true);
                        binding.switch1.setChecked(true);
                        binding.edtxUbicacion.requestFocus();

                        binding.switch1.setEnabled(false);
                        binding.switch1.setChecked(false);
                        binding.switch1.setVisibility(View.GONE);
                        binding.edtxProductoFragm.setEnabled(true);
                        binding.spinnerAlta.setEnabled(true);
                        binding.spinnerBaja.setEnabled(false);
                        binding.spinnerBaja.findViewById(R.id.spinner).setEnabled(false);
                        binding.spinnerAlta.findViewById(R.id.spinner).setEnabled(true);
                        reiniciaCampos();
                        binding.edtxUbicacion.requestFocus();
                        break;
                    case R.id.radio_baja:

                        break;
                }
            }
        });
        binding.radioAlta.setChecked(true);


        binding.switch1.setChecked(true);
    }


    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);
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
        if (p.ispBarActiva())
        {
            int id = item.getItemId();

            if((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto,vista);
            }
            if((id == R.id.borrar_datos))
            {
                reiniciaCampos();
                binding.edtxUbicacion.setText("");
                binding.edtxUbicacion.requestFocus();

            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {
        try
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            this.getSupportActionBar().setTitle("Ajustes contenedor");
            p = new ProgressBarHelper(this);
            binding.edtxUbicacion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
            edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_CodigoEmpaque);
            edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Producto_fragm = findViewById(R.id.edtx_Producto_fragm);

            txtv_Producto= (TextView) findViewById(R.id.txtv_Producto);
            txtv_Cantidad= (TextView) findViewById(R.id.txtv_CantPend);
            spnr_Alta= binding.spinnerAlta.findViewById(R.id.spinner);
            spnr_Baja = binding.spinnerBaja.findViewById(R.id.spinner);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

        }
    }
    private void agregaListeners()
    {

        edtx_CodigoPallet.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                edtx_CodigoPallet.setText("");
                txtv_Producto.setText("");
                edtx_Producto_fragm.setText("");
                txtv_Cantidad.setText("");
                edtx_Producto_fragm.setText("");
                binding.txtvEstatus.setText("");
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
                        new popUpGenerico(contexto, edtx, "Ingrese código de contenedor", false, true, true);
                        edtx_CodigoPallet.setText("");
                        edtx_CodigoPallet.requestFocus();
                        return false;
                    }
                    new SegundoPlano("ConsultaPallet").execute();


                    new esconderTeclado(Ajustes_AjustesContenedorBaja.this);

                }
                return false;
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


        edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View edtx, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if (edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto, edtx_CodigoPallet, "Ingrese código de contenedor", false, true, true);
                        return false;
                    }
                    if (edtx_Producto_fragm.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto, edtx_Producto_fragm, getString(R.string.error_ingrese_producto), false, true, true);
                        return false;
                    }
                    if (edtx_CodigoEmpaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto, edtx, "Ingrese una cantidad valida", false, true, true);
                        edtx_CodigoEmpaque.requestFocus();
                        edtx_CodigoEmpaque.setText("");
                        return false;
                    }

                    if (TextUtils.isEmpty(binding.edtxUbicacion.getText()))
                    {
                        new popUpGenerico(contexto, edtx, "Ingrese una Ubicación", false, true, true);
                        binding.edtxUbicacion.requestFocus();
                        binding.edtxUbicacion.setText("");
                        return false;
                    }

                    if (!binding.switch1.isChecked())
                    {
                        if (Float.parseFloat(edtx_CodigoEmpaque.getText().toString()) > 99999)
                        {
                            new popUpGenerico(contexto, edtx, "Ingrese una cantidad valida", false, true, true);
                            edtx_CodigoEmpaque.requestFocus();
                            edtx_CodigoEmpaque.setText("");
                            return false;
                        }
                    }
                    if (binding.radioAlta.isChecked())
                    {
                        if (binding.switch1.isChecked())
                        {
                            new SegundoPlano("AjusteContenedorSKU").execute();
                        } else
                        {
                            new SegundoPlano("AjusteContenedorNumParte").execute();
                        }
                    } else
                    {
                        new SegundoPlano("AjusteContenedorNegativo").execute();
                    }
                    new esconderTeclado(Ajustes_AjustesContenedorBaja.this);

                }
                return false;
            }
        });

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
//                    binding.edtxProductoFragm.setEnabled(false);
                    binding.textView76.setText("SKU: ");
                    binding.edtxCodigoEmpaque.setHint("Capturar SKU");
                    binding.edtxCodigoEmpaque.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                }else
                {
                    binding.textView76.setText("Cantidad\nde piezas:");
                    binding.edtxCodigoEmpaque.setHint("Capturar cantidad");
                    binding.edtxCodigoEmpaque.setInputType(InputType.TYPE_CLASS_NUMBER);

                }
            }
        });

        binding.txtvProducto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (binding.txtvProducto.getText().toString().equals("-")|| TextUtils.isEmpty(binding.txtvProducto.getText())){
                    new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,getCurrentFocus(),"Consulta un contenedor para continuar",false,true,true);
                    return false;
                }
                String[] datos={txtv_Producto.getText().toString()};
                taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_EXISTENCIA),FragmentoConsulta.TAG);
                return true;
            }
        });

    }
    private void reiniciaCampos()
    {
        edtx_CodigoEmpaque.setText("");
        edtx_Producto_fragm.setText("");
        binding.edtxUbicacion.requestFocus();
        binding.edtxUbicacion.setText("");
        txtv_Producto.setText("");
        txtv_Cantidad.setText("");
        binding.edtxCodigoPallet.setText("");
        binding.txtvEstatus.setText("");
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {
        try
        {
            Handler h = new Handler();
            edtx_Producto_fragm.setText(prmProducto);

            if(edtx_CodigoPallet.getText().toString().equals(""))
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        edtx_CodigoPallet.requestFocus();

                    }
                },100);
            }

            else if(binding.edtxUbicacion.getText().toString().equals(""))
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        binding.edtxUbicacion.requestFocus();
                    }
                },100);
            }

            else if(edtx_CodigoEmpaque.getText().toString().equals(""))
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        edtx_CodigoEmpaque.requestFocus();
                    }
                },100);

            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void BotonDerecha() {

    }


    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

    @Override
    public void BotonIzquierda()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao = new DataAccessObject();
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Ajustes_AjustesContenedorBaja.this);
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
            try
            {
                switch (tarea)
                {

                    case "ConsultaPallet":
                        dao = ca.c_ConsultaContenedorAjustes(edtx_CodigoPallet.getText().toString());
                        break;

                    case "ListarAjustesAltas"://Se usa para llenar el Spinner
                        dao = ca.c_ListarConceptosAjuste("1");
                        break;

                    /*case "ListarAjustesBajas"://Se usa para llenar el Spinner
                        dao = ca.c_ListarConceptosAjuste("2");
                        break;
                    case "AjusteContenedor":
                        dao = ca.c_AjusteContenedorPositivo(edtx_CodigoPallet.getText().toString(), edtx_CodigoEmpaque.getText().toString(),
                                binding.edtxUbicacion.getText().toString(), ((Constructor_Dato) spnr_Alta.getSelectedItem()).getDato());
                        break;*/

                    case "AjusteContenedorNumParte":
                        dao = ca.c_AjusteContenedorPositivoNumParte(edtx_CodigoPallet.getText().toString(), edtx_CodigoEmpaque.getText().toString(),
                                binding.edtxUbicacion.getText().toString(), ((Constructor_Dato) spnr_Alta.getSelectedItem()).getDato(), edtx_Producto_fragm.getText().toString());
                        break;
                    /*case "AjusteContenedorNegativo":
                        dao = ca.c_AjusteContenedorNegativo(edtx_CodigoPallet.getText().toString(), edtx_CodigoEmpaque.getText().toString(),
                                binding.edtxUbicacion.getText().toString(), ((Constructor_Dato) spnr_Baja.getSelectedItem()).getDato());
                        break;*/
                    case "AjusteContenedorSKU":
                        dao = ca.c_AjusteContenedorPositivoSKU(edtx_CodigoPallet.getText().toString(), edtx_CodigoEmpaque.getText().toString(),
                                binding.edtxUbicacion.getText().toString(), ((Constructor_Dato) spnr_Alta.getSelectedItem()).getDato(), edtx_Producto_fragm.getText().toString());
                        break;
                }
            }catch (Exception e)
            {
                dao = new DataAccessObject(e);
                e.printStackTrace();
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
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            edtx_Producto_fragm.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            edtx_Producto_fragm.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.txtvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                            Log.i("Cantidad", String.valueOf(Float.parseFloat(txtv_Cantidad.getText().toString())));

                            if (Float.parseFloat(txtv_Cantidad.getText().toString())>0)
                            {
                                edtx_Producto_fragm.setEnabled(false);
                            }else
                                {
                                edtx_Producto_fragm.setEnabled(true);
                                edtx_Producto_fragm.setText("");
                            }

                            edtx_CodigoEmpaque.requestFocus();
                            break;
                        case "ListarAjustesAltas":
                            ((Spinner)binding.spinnerAlta.findViewById(R.id.spinner)).setAdapter(new CustomArrayAdapter(Ajustes_AjustesContenedorBaja.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","Tag1","")));
                            break;
                        /*case "ListarAjustesBajas":
                            ((Spinner)binding.spinnerBaja.findViewById(R.id.spinner)).setAdapter(new CustomArrayAdapter(Ajustes_AjustesContenedor.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","Tag1","")));
                            break;*/
                        case "AjusteContenedor":
                        case "AjusteContenedorNumParte":
                        case "AjusteContenedorSKU":
                        case "AjusteContenedorNegativo":

                            MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.tilin);
                            mp.start();
                            Vibrator vibra = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
                            vibra.vibrate(150);

                            new SegundoPlano("ConsultaPallet").execute();
                            edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();
                            break;

                    }
                }else
                {
                    switch (tarea)
                    {
                        case "ConsultaPallet":
                            txtv_Producto.setText("");
                            edtx_Producto_fragm.setText("");
                            txtv_Cantidad.setText("");
                            edtx_Producto_fragm.setText("");
                            binding.txtvEstatus.setText("");

                            edtx_CodigoPallet.requestFocus();
                            edtx_CodigoPallet.setText("");
                        case "ListarAjustesAltas":
                        case "ListarAjustesBajas":
                            new popUpGenerico(contexto, vista, dao.getcMensaje(),false, true, true);
                            break;
                        case "AjusteContenedor":
                        case "AjusteContenedorNumParte":
                        case "AjusteContenedorSKU":
                        case "AjusteContenedorNegativo":
                            new popUpGenerico(contexto, vista, dao.getcMensaje(),false, true, true);

                            edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();
                            break;

                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, vista, e.getMessage(),false, true, true);
            }
            p.DesactivarProgressBar(tarea);
        }
    }
}