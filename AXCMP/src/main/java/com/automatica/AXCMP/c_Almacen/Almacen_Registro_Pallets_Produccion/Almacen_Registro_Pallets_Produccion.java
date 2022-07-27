package com.automatica.AXCMP.c_Almacen.Almacen_Registro_Pallets_Produccion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.CreaDialogos;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Almacen_Registro_Pallets_Produccion extends AppCompatActivity
{


    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    View vista;
    Context contexto = this;

    TextView txtv_Caducidad,txtv_Producto, txtv_CantidadTotal,txtv_Lote;
    TextView txtv_CantidadRegistrada,txtv_Sobrante;
    EditText edtx_OrdenProduccion, edtx_Cantidad;
    String Producto, CantidadTotal,CantidadRegistrada,Sobrante,Lote,Caducidad,OrdenCompra;

    Handler h = new Handler();

    Bundle b = new Bundle();


    Button btn_RegistrarSobrantes, btn_CerrarOrden;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.almacen_registro_pallets_produccion);
            declaraVariables();
            agregaListeners();
            Bundle b = getIntent().getExtras();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, vista,e.getMessage() + " " + e.getCause() , "false", true, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }

        if((id == R.id.recargar))
        {
            /*if(!edtx_Cantidad.getText().toString().equals(""))
            {
                SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                sp.execute();
            }*/
        }
        if((id == R.id.borrar_datos))
        {
            reiniciarCampos();
        }
        return super.onOptionsItemSelected(item);
    }

    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.Registro_Pallets_Produccion));


        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                Almacen_Registro_Pallets_Produccion.this);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
        txtv_Caducidad= (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_CantidadTotal = (TextView) findViewById(R.id.txtv_CantidadTotal);

        txtv_CantidadRegistrada= (TextView) findViewById(R.id.txtv_CantidadRegistrada);
        txtv_Sobrante= (TextView) findViewById(R.id.txtv_Sobrante);
        txtv_Lote= (TextView) findViewById(R.id.txtv_Lote);

        edtx_OrdenProduccion = (EditText) findViewById(R.id.edtx_OrdenProduccion);
        edtx_Cantidad = (EditText)findViewById(R.id.edtx_Cantidad);

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
                        new esconderTeclado(Almacen_Registro_Pallets_Produccion.this);

                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_orden_produccion),"false", true, true);
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
                    }
                    new esconderTeclado(Almacen_Registro_Pallets_Produccion.this);
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
                    if(!edtx_Cantidad.getText().toString().equals(""))
                    {

                        SegundoPlano sp = new SegundoPlano("RegistraCantidad");
                        sp.execute();
                        new esconderTeclado(Almacen_Registro_Pallets_Produccion.this);

                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad),"false", true, true);
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
                    }
                    new esconderTeclado(Almacen_Registro_Pallets_Produccion.this);
                    return true;
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


                    new esconderTeclado(Almacen_Registro_Pallets_Produccion.this);
                }else
                {
                    new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_orden_produccion) , "false", true, true);
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



                    if (txtv_Sobrante.getText().toString().equals("0"))
                    {

                       new CreaDialogos(getString(R.string.pregunta_cierre_orden_produccion),
                         new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                b.putString("OrdenProduccion", edtx_OrdenProduccion.getText().toString());
                                Intent intent = new Intent(Almacen_Registro_Pallets_Produccion.this,Almacen_Registro_Pallets_Registro_Sobrante.class);
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
                                                SegundoPlano sp = new SegundoPlano("CerrarOrden");
                                                sp.execute();
                                                new esconderTeclado(Almacen_Registro_Pallets_Produccion.this);



                                            }
                                        },null,contexto);

                                    }
                                }
                        ,contexto);

                    }else
                    {
                        new AlertDialog.Builder(Almacen_Registro_Pallets_Produccion.this).setIcon(R.drawable.ic_warning_black_24dp)

                                .setTitle("¿Cerrar orden?").setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {



                                                new popUpGenerico(contexto, vista, "Prueba si", "true", true, true);
                                        SegundoPlano sp = new SegundoPlano("CerrarOrden");
                                        sp.execute();
                                        new esconderTeclado(Almacen_Registro_Pallets_Produccion.this);



                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();

                    }

                }else
                {
                    new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_orden_produccion),"false", true, true);
                }

            }
        });


    }
    private void iniciaRegistroSobrante()
    {
        b.putString("OrdenProduccion", edtx_OrdenProduccion.getText().toString());
        Intent intent = new Intent(Almacen_Registro_Pallets_Produccion.this,Almacen_Registro_Pallets_Registro_Sobrante.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void reiniciarCampos()
    {
        txtv_Producto.setText("");
        txtv_Caducidad.setText("");
        txtv_CantidadTotal.setText("");
        txtv_CantidadRegistrada.setText("");
        txtv_Sobrante.setText("");
        txtv_Lote.setText("");

        edtx_Cantidad.setText("");
        edtx_OrdenProduccion.setText("");
        edtx_OrdenProduccion.requestFocus();
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


                            sa.SOAPConsultaOrdenProduccion(edtx_OrdenProduccion.getText().toString(),contexto);
                        break;

                    case "RegistraCantidad":


                            sa.SOAPRegistrarCantidadOrdenProduccion(edtx_OrdenProduccion.getText().toString(), edtx_Cantidad.getText().toString(), contexto);
                        break;

                    case "CerrarOrden":
                        sa.SOAPCerrarOrdenProduccion(edtx_OrdenProduccion.getText().toString(), contexto);


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
            if (decision.equals("true"))
            {
                switch (tarea)
                {


                    case "ConsultaOrdenProduccion":

                        txtv_Producto.setText(Producto);
                        txtv_Caducidad.setText(Caducidad);
                        txtv_CantidadTotal.setText(CantidadTotal);
                        txtv_CantidadRegistrada.setText(CantidadRegistrada);
                        txtv_Sobrante.setText(Sobrante);
                        txtv_Lote.setText(Lote);


                        edtx_Cantidad.requestFocus();
                        break;

                    case "RegistraCantidad":

                       txtv_CantidadTotal.setText(CantidadTotal);
                       txtv_CantidadRegistrada.setText(CantidadRegistrada);

                        break;

                    case "CerrarOrden" :

                        new popUpGenerico(contexto, vista, mensaje,decision, true, true);

                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,decision, true, true);
                  reiniciarCampos();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
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

                            Producto = tabla.getPrimitivePropertyAsString("Producto");
                            Caducidad= tabla.getPrimitivePropertyAsString("Caducidad");
                            CantidadTotal= tabla.getPrimitivePropertyAsString("CantidadTotal");
                            CantidadRegistrada= tabla.getPrimitivePropertyAsString("CantidadRegistrada");
                            Sobrante= tabla.getPrimitivePropertyAsString("Sobrante");
                            Lote= tabla.getPrimitivePropertyAsString("Lote");



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
}
