package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Registro_Pallets_Produccion;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.frgmnt_Cierre_Orden;

import org.ksoap2.serialization.SoapObject;

public class Almacen_Registro_Pallets_Produccion_v2 extends AppCompatActivity implements frgmnt_Cierre_Orden.OnFragmentInteractionListener
{
    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    DatePickerFragment newFragment;
    View vista;
    Context contexto = this;

    String Tipo = "Polvos";

    TextView txtv_Caducidad,txtv_Producto, txtv_CantidadTotal,txtv_Lote;
    TextView txtv_CantidadRegistrada,txtv_Sobrante;
    EditText edtx_OrdenProduccion, edtx_Cantidad,edtx_FechaCaducidad;
    String Producto, CantidadTotal,CantidadRegistrada,Sobrante,Lote,Caducidad,OrdenCompra;

    Handler h = new Handler();

    Bundle b = new Bundle();


    Button btn_RegistrarSobrantes, btn_CerrarOrden;

    private static final String frgmnt_tag = "tag";

    boolean recarga = false;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.almacen_registro_pallets_produccion_v2);
            declaraVariables();
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
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, vista,e.getMessage() + " " + e.getCause() , false, true, true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    protected void onResume()
    {
        if(!edtx_OrdenProduccion.getText().toString().equals(""))
        {
            SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
            sp.execute();
        }
        else
        {
            edtx_OrdenProduccion.requestFocus();
        }


        super.onResume();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(!recarga)
            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, vista);
                    }

                if ((id == R.id.recargar))
                    {
                        if (!edtx_OrdenProduccion.getText().toString().equals(""))
                            {
                                SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                                sp.execute();
                            }
                    }
                if ((id == R.id.borrar_datos))
                    {
                        reiniciarCampos();
                    }
            }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.Registro_Pallets_Produccion));
        new cambiaColorStatusBar(contexto, R.color.VerdeStd,
                Almacen_Registro_Pallets_Produccion_v2.this);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
        txtv_Caducidad= (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_CantidadTotal = (TextView) findViewById(R.id.txtv_CantidadTotal);

        txtv_CantidadRegistrada= (TextView) findViewById(R.id.txtv_CantidadRegistrada);
        txtv_Sobrante= (TextView) findViewById(R.id.txtv_Sobrante);
        txtv_Lote= (TextView) findViewById(R.id.txtv_Lote);

        edtx_OrdenProduccion = (EditText) findViewById(R.id.edtx_Cantidad);
        edtx_Cantidad = (EditText)findViewById(R.id.edtx_Empaque);
        edtx_FechaCaducidad = (EditText) findViewById(R.id.edtx_FechaCad);

        btn_CerrarOrden = (Button) findViewById(R.id.btn_CerrarOrden);
        btn_RegistrarSobrantes= (Button) findViewById(R.id.btn_Sobrante);


    }
    private void agregaListeners()
    {
        edtx_OrdenProduccion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_OrdenProduccion.getText().toString().equals(""))
                    {

                        SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                        sp.execute();
                        new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);

                    }
                    else
                    {
                        reiniciarCampos();
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_OrdenProduccion.requestFocus();
                                edtx_OrdenProduccion.setText("");
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion),false, true, true);
                    }
                    new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);
                    return true;
                }
                return false;

            }

        });


        edtx_Cantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_OrdenProduccion.getText().toString().equals(""))
                    {

                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_OrdenProduccion.requestFocus();
                                edtx_OrdenProduccion.setText("");
                            }
                        });
                        new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion),false, true, true);
                        return true;
                    }
                    if(!edtx_Cantidad.getText().toString().equals(""))
                    {

                        SegundoPlano sp = new SegundoPlano("RegistraCantidad");
                        sp.execute();
                        new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);

                    }
                    else
                    {
                        reiniciarCampos();
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Cantidad.requestFocus();
                                edtx_Cantidad.setText("");
                            }
                        });
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad),false, true, true);
                    }
                    new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);
                    return true;
                }

                return false;

            }

        });

        edtx_FechaCaducidad.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(!edtx_OrdenProduccion.getText().toString().equals(""))
                {
                    try {

                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            newFragment = DatePickerFragment.newInstance(
                                    new DatePickerDialog.OnDateSetListener()
                                    {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                {
                                    final String selectedDate = day + "-" + (month + 1) + "-" + year;


                                    new CreaDialogos("¿Registrar fecha de producción? [" + selectedDate+"]",
                                            new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    edtx_FechaCaducidad.setText(selectedDate);
                                                    SegundoPlano sp = new SegundoPlano("RegistraCaducidad");
                                                    sp.execute();
                                                }
                                            }, new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                                            sp.execute();
                                        }
                                    },contexto);

                                }
                            });

                            newFragment.show(getSupportFragmentManager(), "datePicker");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });


        btn_RegistrarSobrantes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!edtx_OrdenProduccion.getText().toString().equals(""))
                {
                    iniciaRegistroSobrante();


                    new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);
                }else
                {
                    new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_orden_produccion) , false, true, true);
                }
            }
        });




        btn_CerrarOrden.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {



                if(!edtx_OrdenProduccion.getText().toString().equals(""))
                {

//                    if(edtx_FechaCaducidad.getText().toString().equals(""))
//                        {
//                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.fecha_cad_error) , false, true, true);
//                            return;
//                        }




                        if (txtv_Sobrante.getText().toString().equals("0"))
                  {

                       new CreaDialogos(getString(R.string.pregunta_cierre_orden_produccion),
                         new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                b.putString("OrdenProduccion", edtx_OrdenProduccion.getText().toString());
                                Intent intent = new Intent(Almacen_Registro_Pallets_Produccion_v2.this,Almacen_Registro_Pallets_Registro_Sobrante.class);
                                intent.putExtras(b);
                                startActivity(intent);
                            }
                        },
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {


                                new CreaDialogos(getString(R.string.pregunta_cierre_orden), new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int id) {
                                               //new popUpGenerico(contexto, vista, "Prueba si", "true", true, true);







                                                getSupportFragmentManager().beginTransaction()
                                                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                                        .replace(R.id.clayout, frgmnt_Cierre_Orden.newInstance(new String[]{edtx_OrdenProduccion.getText().toString(),edtx_FechaCaducidad.getText().toString()}, null), frgmnt_tag)
                                                        .commit();
//
//                                                SegundoPlano sp = new SegundoPlano("CerrarOrden");
//                                                sp.execute();
                                                new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);



                                            }
                                        },null,contexto);

                                    }
                                }
                        ,contexto);

                    }else
                    {
                        new AlertDialog.Builder(Almacen_Registro_Pallets_Produccion_v2.this).setIcon(R.drawable.ic_warning_black_24dp)

                                .setTitle("¿Cerrar orden?").setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {



                                        //getSupportFragmentManager().beginTransaction().add(frgmnt_Cierre_Orden.newInstance(null, null), frgmnt_tag).commit();
                                        getSupportFragmentManager().beginTransaction()
                                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                                .replace(R.id.clayout, frgmnt_Cierre_Orden.newInstance(new String[]{edtx_OrdenProduccion.getText().toString(),edtx_FechaCaducidad.getText().toString()}, null), frgmnt_tag)
                                                .commit();

//                                        SegundoPlano sp = new SegundoPlano("CerrarOrden");
//                                        sp.execute();
                                        new esconderTeclado(Almacen_Registro_Pallets_Produccion_v2.this);



                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();

                    }

                }else
                {
                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion),false, true, true);
                }

            }
        });


    }
    private void iniciaRegistroSobrante()
    {
        b.putString("OrdenProduccion", edtx_OrdenProduccion.getText().toString());
        Intent intent = new Intent(Almacen_Registro_Pallets_Produccion_v2.this,Almacen_Registro_Pallets_Registro_Sobrante.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void reiniciarCampos()
    {
        txtv_Producto.setText("");
      //txtv_Caducidad.setText("");
        edtx_FechaCaducidad.setText("");

        txtv_CantidadTotal.setText("");
        txtv_CantidadRegistrada.setText("");
        txtv_Sobrante.setText("");
        txtv_Lote.setText("");
        edtx_Cantidad.setText("");
        edtx_OrdenProduccion.setText("");
        edtx_OrdenProduccion.requestFocus();
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {

        if(estado)
            {
             //   recargar = true;
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
               // recargar = false;
            }


        return false;
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
            recarga = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {


                switch (tarea) {
                    case "ConsultaOrdenProduccion":
                            sa.SOAPConsultaOrdenProduccion_V2(edtx_OrdenProduccion.getText().toString(),Tipo,contexto);
                        break;

                    case "RegistraCantidad":
                            sa.SOAPRegistrarCantidadOrdenProduccion(edtx_OrdenProduccion.getText().toString(), edtx_Cantidad.getText().toString(), contexto);
                        break;

                    case "RegistraCaducidad":
                        sa.SOAPRegistraFechaCadOrdenProd(edtx_OrdenProduccion.getText().toString(), edtx_FechaCaducidad.getText().toString(), contexto);
                        break;

                    case "CerrarOrden":
                            sa.SOAPCerrarOrdenProduccion_v2(edtx_OrdenProduccion.getText().toString(),edtx_FechaCaducidad.getText().toString(),Tipo, contexto);
                        break;


                }
                decision = sa.getDecision();
                mensaje = sa.getMensaje();
                if (decision.equals("true"))
                {
                    sacaDatos(tarea);
                }
            }catch (Exception e)
            {
                mensaje = e.getMessage();
                decision = "false";

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                SegundoPlano sp;
                if (decision.equals("true")) {
                    switch (tarea) {


                        case "ConsultaOrdenProduccion":

                            txtv_Producto.setText(Producto);
                            //txtv_Caducidad.setText(Caducidad);

                            //SE DESACTIVO ESTE CAMPO EL 05 06 2020 POR EL CAMBIO DE REGISTRO AUTOMATICO DE FECHA DE CADUCIDAD
                      //      edtx_FechaCaducidad.setText(Caducidad);

                            txtv_CantidadTotal.setText(CantidadTotal);
                            txtv_CantidadRegistrada.setText(CantidadRegistrada);
                            txtv_Sobrante.setText(Sobrante);
                            txtv_Lote.setText(Lote);

                            edtx_Cantidad.setText("");
                            edtx_Cantidad.requestFocus();
                            break;

                        case "RegistraCantidad":
                            sp = new SegundoPlano("ConsultaOrdenProduccion");
                            sp.execute();
                            break;

                        case "RegistraCaducidad":
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.fecha_cad_reg_exito), decision, true, true);

                            sp = new SegundoPlano("ConsultaOrdenProduccion");
                            sp.execute();
                            break;

                        case "CerrarOrden":

                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_prod_cerrar_exito), decision, true, true);

                            break;

                    }
                }

                if (decision.equals("false")) {
                    reiniciarCampos();
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), decision, true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recarga = false;
        }
    }
    public void sacaDatos(String Tarea) {
        SoapObject tabla = sa.parser();

        if (tabla == null) tabla = sa.getTablaSoap();



        if (tabla != null)
        {

            switch (Tarea) {

                case "ConsultaOrdenProduccion":
                    for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        try {

                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());

                            Producto = tabla1.getPrimitivePropertyAsString("Producto");
                            Caducidad= tabla1.getPrimitivePropertyAsString("FechaCaducidad");
                            CantidadTotal= tabla1.getPrimitivePropertyAsString("CantidadPedida");
                            CantidadRegistrada= tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                            Sobrante= tabla1.getPrimitivePropertyAsString("Sobrante");
                            Lote= tabla1.getPrimitivePropertyAsString("Lote");



                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                    break;

                case "RegistraCantidad":


                    try {

                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {


                                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                                Log.d("SoapResponse", tabla1.toString());
                            CantidadTotal= tabla.getPrimitivePropertyAsString("CantidadTotal");
                            CantidadRegistrada= tabla.getPrimitivePropertyAsString("CantidadRegistrada");
                            Sobrante= tabla.getPrimitivePropertyAsString("Sobrante");


                        }
                        Log.d("SoapResponse", tabla.toString());



                    } catch (Exception e)
                    {

                        e.printStackTrace();
                    }


                    break;
            }

        }
    }


    @Override
    public void onBackPressed()
    {
        Fragment f = getSupportFragmentManager().findFragmentByTag(frgmnt_tag);
         if(f!=null)
             {
                 getSupportFragmentManager().beginTransaction().remove(f).commit();
                 return;
             }
        super.onBackPressed();
    }
}
