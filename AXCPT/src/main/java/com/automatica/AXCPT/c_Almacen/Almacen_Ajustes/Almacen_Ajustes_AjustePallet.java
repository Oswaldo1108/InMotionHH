package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_AjustePalletSCH;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion.Validacion_PorPallet;
import com.automatica.AXCPT.databinding.ActivityAlmacenAjustesAjustePalletBinding;
import com.automatica.AXCPT.objetos.ObjetoEtiquetaSKU;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Almacen_Ajustes_AjustePallet extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener
{
    //region variables
    EditText edtx_Cantidad,edtx_CodigoEmpaque, edtx_Producto, edtxNumSerie;
    TextView txtv_EmpaquesRegistrados,txtv_Pallet;
    frgmnt_taskbar_AXC taskbar_axc;
    Button btn_CerrarTarima;
    Spinner spnr_Ajuste, spnr_NumParte, spnr_imp;
    CheckBox checkNumSerie;
    String Ajuste, ImpresoraSelec;
    View vista;
    Context contexto = this;
    Handler handler = new Handler();
    private ProgressBarHelper p;
    private ActivityHelpers activityHelpers;
    ActivityAlmacenAjustesAjustePalletBinding binding;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenAjustesAjustePalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try
        {
            new cambiaColorStatusBar(contexto,R.color.doradoLetrastd,Almacen_Ajustes_AjustePallet.this);
            declaraVariables();
            agregaListeners();

            SegundoPlano spe = new SegundoPlano("ConsultaPallet");
            spe.execute();
            spe= new SegundoPlano("ListarAjustes");
            spe.execute();

            activityHelpers = new ActivityHelpers();
            activityHelpers.AgregarMenus(Almacen_Ajustes_AjustePallet.this,R.id.Pantalla_principal,false);

        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
            e.printStackTrace();
        }


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
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
        if (p.ispBarActiva()){
            int id = item.getItemId();
            try
            {
                if((id == R.id.InformacionDispositivo))
                {
                    new sobreDispositivo(contexto,vista);
                }
                if((id == R.id.borrar_datos))
                {
                    reiniciarCampos();

                }

            }catch (Exception e)
            {
                new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
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
            this.getSupportActionBar().setTitle(getString(R.string.Ajustes_nuevo_pallet));
            this.getSupportActionBar().setSubtitle("Etiquetado");

            // toolbar.setSubtitle("  Ajuste Pallet");
//            toolbar.setLogo(R.mipmap.logo_axc);//   toolbar.setLogo(R.drawable.axc_logo_toolbar);

            p = new ProgressBarHelper(this);
            edtx_CodigoEmpaque = findViewById(R.id.edtx_CodigoPallet);
          //  edtx_CodigoEmpaque.setEnabled(false);
            edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            edtx_Cantidad = findViewById(R.id.edtx_Empaque);
            edtx_Producto = findViewById(R.id.txtv_Producto);
            edtxNumSerie = findViewById(R.id.edtxNumSerie);
           // edtx_Producto.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            btn_CerrarTarima= findViewById(R.id.btn_CerrarTarima);
            checkNumSerie = findViewById(R.id.checkNumSerie);
            txtv_EmpaquesRegistrados= findViewById(R.id.txtv_EmpaquesReg);
            txtv_Pallet = findViewById(R.id.txtv_Pallet);

            spnr_Ajuste = findViewById(R.id.spnr_Ajuste);
            spnr_NumParte= findViewById(R.id.spnr_NumParte);
            spnr_imp = findViewById(R.id.spnr_imp);
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }

    }
    private void agregaListeners()
    {

        try
        {
            edtx_Cantidad.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_Cantidad.getText().toString().equals(""))
                        {
                            if(Double.parseDouble(edtx_Cantidad.getText().toString())>999999)
                            {
                                new popUpGenerico(contexto,vista,getString(R.string.error_cantidad_menor_que),"Advertencia",true,true);
                                edtx_Cantidad.setText("");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_Cantidad.requestFocus();
                                    }
                                });


                            }else
                            {
                                edtx_CodigoEmpaque.setEnabled(true);
                              }

                            //CantidadAConsultar = edtx_Cantidad.getText().toString();
                        }else
                        {
                            new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_cantidad),"false",true,true);
                            edtx_Cantidad.setText("");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_Cantidad.requestFocus();
                                }
                            });
                        }


                    }
                    return false;
                }
            });
            edtx_Producto.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_Producto.getText().toString().equals(""))
                        {


                            ObjetoEtiquetaSKU obj1 = new ObjetoEtiquetaSKU(edtx_Producto.getText().toString());
                            /*ObjetoEtiquetaSKU obj2;
                            obj2 = obj1.incrByTen();*/
                            edtx_Producto.setText(String.valueOf(obj1.sku));
                            edtxNumSerie.setText(String.valueOf(obj1.numeroSerie));
                            SegundoPlano sp = new SegundoPlano("BusquedaNumeroParte");
                            sp.execute();

                        }else
                        {
                            new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_producto),"false",true,true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_Producto.requestFocus();
                                }
                            });
                        }


                    }
                    return false;
                }
            });

            edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    try {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                        {

                            if(edtx_CodigoEmpaque.getText().toString().length()<9)
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_CodigoEmpaque.requestFocus();
                                            edtx_CodigoEmpaque.setText("");
                                        }
                                    });
                                    return false;
                                }

                            if (spnr_NumParte.getSelectedItem().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_seleccionar_numero_parte), "false", true, true);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_CodigoEmpaque.requestFocus();
                                        edtx_CodigoEmpaque.setText("");
                                    }
                                });
                                return false;
                            }

                            if (edtx_Cantidad.getText().toString().equals("")) {

                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad), "false", true, true);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_CodigoEmpaque.requestFocus();
                                        edtx_Cantidad.setText("");
                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.setText("");
                                    }
                                });
                                return false;
                            }

                            if (!edtx_CodigoEmpaque.getText().toString().equals(""))
                            {
                                SegundoPlano sp = new SegundoPlano("RegistraEmpaqueNvoPallet");
                                sp.execute();
                                edtxNumSerie.setText("");


                            } else {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_CodigoEmpaque.requestFocus();
                                    }
                                });
                            }

                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

                    }
                    return false;
                }
            });

            checkNumSerie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edtxNumSerie.setEnabled(checkNumSerie.isChecked());
                }
            });

            btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                    if(txtv_Pallet.getText().toString().equals("")&&txtv_EmpaquesRegistrados.getText().toString().equals(""))
                    {
        //                if((spnr_Ajuste== null)/*||(spnr_Ajuste.getSelectedItem().toString().equals(""))*/)
        //                {
        //                    new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_seleccionar_ajuste),"false",true,true);
        //                    return;
        //                }
        //                if((spnr_NumParte == null)/*||(spnr_NumParte.getSelectedItem().toString().equals(""))*/)
        //                {
        //                    new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_seleccionar_numero_parte),"false",true,true);
        //                    return;
        //                }
        //                if((edtx_Cantidad == null)||(edtx_Cantidad.getText().toString().equals("")))
        //                {
        //                    new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"false",true,true);
        //                    return;
        //                }
        //                if((edtx_CodigoEmpaque == null)||(edtx_CodigoEmpaque.getText().toString().equals("")))
        //                {
        //                    new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_empaque),"false",true,true);
        //                    return;
        //                }
                        new AlertDialog.Builder(Almacen_Ajustes_AjustePallet.this).setIcon(R.drawable.ic_warning_black_24dp)

                                .setTitle("¿Crear pallet con un solo empaque?").setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SegundoPlano sp = new SegundoPlano("RegistroEmpaqueUnico");
                                        sp.execute();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }else
                    {
                        SegundoPlano sp= new SegundoPlano("CierraPallet");
                        sp.execute();
                    }


                }catch (Exception e)
                {
                e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

                }
                }
            });
//            spnr_NumParte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//            {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
//                {
//                    try
//                    {
//                    }catch (Exception e)
//                    {
//                        e.printStackTrace();
//                        new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
//
//                    }
//
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
            spnr_Ajuste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  try
                  {
                      Ajuste = spnr_Ajuste.getSelectedItem().toString();
                  }catch (Exception e)
                  {
                      e.printStackTrace();
                      new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

                  }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spnr_imp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ImpresoraSelec = spnr_imp.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            }); //stand by por ahora

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


            edtx_Cantidad.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
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



        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }

    }

    @Override
    public void BotonDerecha() {
        btn_CerrarTarima.callOnClick();
    }


    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada)
    {
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


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Ajustes_AjustePallet.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                p.ActivarProgressBar(tarea);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
            {
                String Cantidad = edtx_Cantidad.getText().toString();
                String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();

                switch (tarea)
                {
                    case "RegistroEmpaqueUnico":
                        dao= ca.c_RegistraEmpaqueUnico(((Constructor_Dato)spnr_NumParte.getSelectedItem()).getTag1(),edtx_Cantidad.getText().toString()
                                ,spnr_Ajuste.getSelectedItem().toString(),"");
                        break;

                    case "ConsultaPallet":
                        dao = ca.c_ConsultaPalletAbiertoSinAfecta();
                        break;
                    case "ListarAjustes":
                        String TipoAjuste = "1";
                        //sa.SOAPListarConceptosAjuste(TipoAjuste,contexto);
                        dao= ca.c_ListarConceptosAjuste(TipoAjuste);
                        break;
                    case "BusquedaNumeroParte":
                        //sa.SOAPBuscarArticulos(edtx_Producto.getText().toString(),contexto);
                        dao= ca.c_ConsultaCoincidenciaArticulo(edtx_Producto.getText().toString());
                        break;
                    case "RegistraEmpaqueNvoPallet":
                        //sa.SOAPRegistraEmpaqueNuevoPallet(edtx_CodigoEmpaque.getText().toString(),NumParte,edtx_Cantidad.getText().toString(),
                          //      Ajuste,edtx_Caducidad.getText().toString(),edtx_Lote.getText().toString(),contexto);
                        dao= ca.c_RegistraEmpaqueNvoPalletv2(edtx_CodigoEmpaque.getText().toString(),((Constructor_Dato)spnr_NumParte.getSelectedItem()).getTag1(),edtx_Cantidad.getText().toString(),
                                ((Constructor_Dato)spnr_Ajuste.getSelectedItem()).getDato(),"",
                                binding.edtxLote.getText().toString(), "");
                        break;
                    case "CierraPallet":
                        //sa.SOAPCierraPalletAjuste(txtv_Pallet.getText().toString(),contexto);
                        dao= ca.c_CierraPalletAjuste(txtv_Pallet.getText().toString());
                        break;

                }
            }catch (Exception e)
            {
               e.printStackTrace();
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

                        case "RegistroEmpaqueUnico":
                            reiniciarCampos();
                            new popUpGenerico(contexto,vista,getString(R.string.pallet_empaque_unico_exito) + "[" + dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet") + "].",dao.iscEstado(),true,true);
                            break;

                        case "ConsultaPallet":
                                String[] mensaje = dao.getcMensaje().split("#");
                                txtv_EmpaquesRegistrados.setText(mensaje[1]);
                                txtv_Pallet.setText(mensaje[0]);
                            break;

                        case "RegistraEmpaqueNvoPallet":
                            btn_CerrarTarima.setEnabled(true);
                            //   new popUpGenerico(contexto, vista, mensaje,decision, true, true);
                            edtx_CodigoEmpaque.setText("");
                            txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (checkNumSerie.isChecked())
                                        edtxNumSerie.requestFocus();
                                    else
                                        edtx_CodigoEmpaque.requestFocus();
                                }
                            },150);
                            break;
                        case "CierraPallet":
                            reiniciarCampos();
                            new popUpGenerico(contexto, vista,"Se ha registrado correctamente el Pallet" + "["+dao.getcMensaje()+"]",dao.iscEstado(), true, true);


                            txtv_EmpaquesRegistrados.setText("-");
                            txtv_Pallet.setText("");
                            break;
                        case "ListarAjustes":
                            spnr_Ajuste.setAdapter(new CustomArrayAdapter(Almacen_Ajustes_AjustePallet.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","IdAjuste","")));
                            break;
                        case "BusquedaNumeroParte":
                            spnr_NumParte.setAdapter(new CustomArrayAdapter(Almacen_Ajustes_AjustePallet.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("NumParte","NumParte","")));
                            edtx_Cantidad.requestFocus();
                            break;

                    }

                }

                else
                {
                    new popUpGenerico(contexto, vista, dao.getcMensaje(),"false", true, true);
                    edtx_CodigoEmpaque.setText("");

                    switch(tarea)
                    {
                        case "BusquedaNumeroParte":
                            edtxNumSerie.setText("");
                            edtx_Producto.setText("");
                            edtx_Producto.requestFocus();
                            break;
                        case "RegistraEmpaqueNvoPallet":

                            edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();
                            break;
                    }
                }
                p.DesactivarProgressBar(tarea);
            }catch (Exception e)
            {
                new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
                e.printStackTrace();
            }

        }
    }

    private void reiniciarCampos()
    {
        try
        {
            edtx_Producto.setText("");
            spnr_NumParte.setAdapter(null);
       /* txtv_EmpaquesRegistrados.setText("");
        txtv_Pallet.setText("");*/
            edtx_CodigoEmpaque.setText("");
            edtx_Cantidad.setText("");
            edtxNumSerie.setText("");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    edtx_Producto.requestFocus();
                }
            });
        }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

            }
    }

    @Override
    public void BotonIzquierda() {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }
    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

}
