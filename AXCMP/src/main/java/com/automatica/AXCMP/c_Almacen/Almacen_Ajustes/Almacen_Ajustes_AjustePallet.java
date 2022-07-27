package com.automatica.AXCMP.c_Almacen.Almacen_Ajustes;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.DatePickerFragment;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class Almacen_Ajustes_AjustePallet extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_Cantidad,edtx_CodigoEmpaque, edtx_Producto,edtx_Caducidad,edtx_Lote;
    TextView txtv_NoAjuste,txtv_Cantidad,txtv_Registrados;
    TextView txtv_EmpaquesRegistrados,txtv_Pallet;

    DatePickerFragment newFragment;


    Button btn_CerrarTarima;
    Spinner spnr_Ajuste, spnr_NumParte;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    String Ajuste;
    String NumParte;

    View vista;
    Context contexto = this;
    Bundle b;
    String IdAjusteBundle;
    String CantReg;
    Handler handler = new Handler();
    String CantidadAConsultar;
    String[] TipoAjuste  ;//{" ","LINEA","EVENTO"};
    String CodigoPallet, EmpaquesRegistrados;
    ArrayList<Ajustes> ArrayAjustes;
    ArrayList<Ajustes> ArrayProductos;
    ArrayAdapter<String> spinnerArrayAdapter;
    class Ajustes
    {
        String IdAjuste, Descripción;

        public Ajustes(String idAjuste, String descripción) {
            IdAjuste = idAjuste;
            Descripción = descripción;
        }

        public String getIdAjuste() {
            return IdAjuste;
        }

        public String getDescripción() {
            return Descripción;
        }
    }
    //endregion
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen__ajustes__ajuste_pallet);
        try
        {
            new cambiaColorStatusBar(contexto,R.color.doradoLetrastd,Almacen_Ajustes_AjustePallet.this);
            declaraVariables();
            agregaListeners();



            SegundoPlano spe = new SegundoPlano("ConsultaPallet");
            spe.execute();

            spe= new SegundoPlano("ListarAjustes");
            spe.execute();

            handler.post(new Runnable() {
                @Override
                public void run() {
                edtx_Producto.requestFocus();
                }
            });

        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"Advertencia",true,true);
            e.printStackTrace();
        }


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



        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {


        try
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(getString(R.string.Ajustes_nuevo_pallet));

            // toolbar.setSubtitle("  Ajuste Pallet");
            toolbar.setLogo(R.mipmap.logo_axc);//   toolbar.setLogo(R.drawable.axc_logo_toolbar);

            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_CodigoPallet);
          //  edtx_CodigoEmpaque.setEnabled(false);

            edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Producto = (EditText) findViewById(R.id.txtv_Producto);
            edtx_Lote  = (EditText) findViewById(R.id.edtx_Lote);
            edtx_Caducidad = (EditText) findViewById(R.id.edtx_Caducidad);
            edtx_Producto.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

            btn_CerrarTarima= (Button) findViewById(R.id.btn_CerrarTarima);




            txtv_EmpaquesRegistrados= (TextView) findViewById(R.id.txtv_EmpaquesReg);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);


            spnr_Ajuste = (Spinner) findViewById(R.id.spnr_Ajuste);
            spnr_NumParte= (Spinner) findViewById(R.id.spnr_NumParte);



        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }





    }
    private void agregaListeners()
    {

        try
        {


            edtx_Caducidad.setOnTouchListener(new View.OnTouchListener()
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
            });


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

                            if (edtx_Caducidad.getText().toString().equals(""))
                                {

                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.capturar_caducidad), "false", true, true);

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


                            if (edtx_Lote.getText().toString().equals(""))
                                {

                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_lote), "false", true, true);

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


                            if (!edtx_CodigoEmpaque.getText().toString().equals("")) {
                                SegundoPlano sp = new SegundoPlano("RegistraEmpaqueNvoPallet");
                                sp.execute();

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

                                .setTitle("¿Crear miaxc_consulta_pallet con un solo empaque?").setCancelable(false)
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
            spnr_NumParte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        NumParte = ArrayProductos.get(position).getIdAjuste();
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
            spnr_Ajuste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  try
                  {
                      Ajuste = ArrayAjustes.get(position).getIdAjuste();
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
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }

    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
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
                        sa.SOAPRegistraEmpaqueUnico(spnr_NumParte.getSelectedItem().toString(),edtx_Cantidad.getText().toString()
                                ,spnr_Ajuste.getSelectedItem().toString(),contexto);
                        break;

                    case "ConsultaPallet":
                        sa.SOAPConsultaPalletAbiertoSinAfecta(contexto);
                        decision =   sa.getDecision();
                        mensaje = sa.getMensaje();
                        if(decision.equals("true"))
                        {
                            String[] arrayMensaje;

                            arrayMensaje = mensaje.split("#");
                            CodigoPallet = arrayMensaje[0];
                            EmpaquesRegistrados = arrayMensaje[1];
                        }
                        break;

                    case "RegistrarAjusteNuevoPallet":

                        sa.SOAPRegistrarAjusteNuevoPallet(CodigoEmpaque,Cantidad,IdAjusteBundle,contexto);

                        break;

                    case "ListarAjustes":
                        ArrayAjustes = new ArrayList<>();
                        String TipoAjuste = "1";
                        sa.SOAPListarConceptosAjuste(TipoAjuste,contexto);

                        break;
                    case "BusquedaNumeroParte":
                        ArrayProductos = new ArrayList<>();
                        sa.SOAPBuscarArticulos(edtx_Producto.getText().toString(),contexto);
                        break;
                    case "RegistraEmpaqueNvoPallet":
                        sa.SOAPRegistraEmpaqueNuevoPallet(edtx_CodigoEmpaque.getText().toString(),NumParte,edtx_Cantidad.getText().toString(),
                                Ajuste,edtx_Caducidad.getText().toString(),edtx_Lote.getText().toString(),contexto);
                        break;
                    case "RegistrarEmpaqueUnico":
                        sa.SOAPRegistraEmpaqueUnico(NumParte,edtx_Cantidad.getText().toString(),Ajuste,contexto);
                        break;
                    case "CierraPallet":
                        sa.SOAPCierraPalletAjuste(txtv_Pallet.getText().toString(),contexto);
                        break;

                }
                decision = sa.getDecision();
                mensaje = sa.getMensaje();

                if(decision.equals("true"))
                {
                    sacaDatos(tarea);
                    if(tarea.equals("ListarAjustes"))
                    {
                        TipoAjuste = new String[ArrayAjustes.size()];
                        int i = 0;
                        for(Ajustes a:ArrayAjustes)
                        {
                            TipoAjuste[i] = a.getDescripción();
                            i++;
                        }


                        spinnerArrayAdapter = new ArrayAdapter<String>
                                (Almacen_Ajustes_AjustePallet.this, android.R.layout.simple_spinner_item,
                                        TipoAjuste); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);

                    }
                    if(tarea.equals("BusquedaNumeroParte"))
                    {
                        TipoAjuste = new String[ArrayProductos.size()];
                        int i = 0;
                        for(Ajustes a:ArrayProductos)
                        {
                            TipoAjuste[i] = a.getIdAjuste();
                            i++;
                        }


                        spinnerArrayAdapter = new ArrayAdapter<String>
                                (Almacen_Ajustes_AjustePallet.this, android.R.layout.simple_spinner_item,
                                        TipoAjuste); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);

                    }
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
                if (decision.equals("true"))
                {
                    switch (tarea)
                    {

                        case "RegistroEmpaqueUnico":
                            reiniciarCampos();
                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.pallet_empaque_unico_exito) + "[" + CodigoPallet + "].",decision,true,true);
                            break;

                        case "ConsultaPallet":
                            if(!CodigoPallet.equals(""))
                            {
                                txtv_EmpaquesRegistrados.setText(EmpaquesRegistrados);
                                txtv_Pallet.setText(CodigoPallet);
                            }
                            break;

                        case "RegistraEmpaqueNvoPallet":
                            btn_CerrarTarima.setEnabled(true);
                            //   new popUpGenerico(contexto, vista, mensaje,decision, true, true);
                            edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();
                            txtv_EmpaquesRegistrados.setText(EmpaquesRegistrados);
                            txtv_Pallet.setText(CodigoPallet);
                            break;
                        case "CierraPallet":
                            reiniciarCampos();
                            new popUpGenerico(contexto, vista,"Se ha registrado correctamente el Pallet" + "["+mensaje+"]",decision, true, true);
                            txtv_EmpaquesRegistrados.setText("");
                            txtv_Pallet.setText("");
                            break;
                        case "ListarAjustes":
                            spnr_Ajuste.setAdapter(spinnerArrayAdapter);
                            break;
                        case "BusquedaNumeroParte":
                            spnr_NumParte.setAdapter(spinnerArrayAdapter);
                            break;

                    }

                }

                if(decision.equals("false"))
                {
                    new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
                    edtx_CodigoEmpaque.setText("");

                    switch(tarea)
                    {
                        case "BusquedaNumeroParte":
                            edtx_Producto.setText("");
                            edtx_Producto.requestFocus();
                            break;
                    }
                }
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
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

    public void sacaDatos(String tarea) {
        try {
            SoapObject tabla = sa.parser();


            if (tabla != null)
                for (int i = 0; i < tabla.getPropertyCount(); i++) {
                    try {

                        SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                        Log.d("SoapResponse", tabla1.toString());

                        Ajustes a;
                        switch (tarea) {
                            case "RegistroEmpaqueUnico":
                                CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                break;
                            case "RegistraEmpaqueNvoPallet":
                                CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                EmpaquesRegistrados = tabla1.getPrimitivePropertyAsString("CantEmpaques");

                                break;
                            case "ConsultaPallet":
                                CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                EmpaquesRegistrados = tabla1.getPrimitivePropertyAsString("CantEmpaques");
                                CantReg = tabla1.getPrimitivePropertyAsString("CantReg");
                                break;
                            case "ListarAjustes":

                                String Ajuste, idAjuste;
                                Ajuste = tabla1.getPrimitivePropertyAsString("Descripcion");
                                idAjuste = tabla1.getPrimitivePropertyAsString("IdAjuste");
                                a = new Ajustes(idAjuste, Ajuste);
                                ArrayAjustes.add(a);

                                break;
                            case "BusquedaNumeroParte":
                                String NumParte, UnidadMedida;
                                NumParte = tabla1.getPrimitivePropertyAsString("NumParte");
                                UnidadMedida = tabla1.getPrimitivePropertyAsString("UnidadMedida");
                                a = new Ajustes(NumParte, UnidadMedida);
                                ArrayProductos.add(a);

                                break;

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
