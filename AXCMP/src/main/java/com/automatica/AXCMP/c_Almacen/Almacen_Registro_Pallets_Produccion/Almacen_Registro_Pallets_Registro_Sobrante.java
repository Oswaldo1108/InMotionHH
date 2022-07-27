package com.automatica.AXCMP.c_Almacen.Almacen_Registro_Pallets_Produccion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Almacen_Registro_Pallets_Registro_Sobrante extends AppCompatActivity
{


    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    View vista;
    Context contexto = this;

    TextView txtv_Empaque, txtv_OrdenCompra,txtv_Caducidad,txtv_Producto, txtv_CantidadTotal,txtv_Lote;
    TextView txtv_CantidadRegistrada,txtv_Sobrante;

    EditText edtx_OrdenProduccion,edtx_Cantidad,edtx_Empaque;
    String Producto,Contenido,Lote,Caducidad, OrdenProduccion;
    String CantidadRegistrada,CantidadTotal,Sobrante;
    String Transferencia;
    Handler h = new Handler();
    Button btn_RegistrarSobrantes;
    TextView txtv_Partida,txtv_Articulo,txtv_Cantidad;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.almacen_registro_pallets_sobrante);
            declaraVariables();
            agregaListeners();


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, vista,e.getMessage() + " " + e.getCause() , "false", true, true);
        }
    }

    @Override
    protected void onResume()
    {
        if(!edtx_OrdenProduccion.getText().toString().equals(""))
        {
            SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
            sp.execute();
        }
        super.onResume();
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
        this.getSupportActionBar().setTitle(getString(R.string.registrar_sobrante));


        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                Almacen_Registro_Pallets_Registro_Sobrante.this);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


        txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
        txtv_Caducidad= (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_CantidadTotal = (TextView) findViewById(R.id.txtv_CantidadTotal);

        txtv_CantidadRegistrada= (TextView) findViewById(R.id.txtv_CantidadRegistrada);
        txtv_Sobrante= (TextView) findViewById(R.id.txtv_Sobrante);
        txtv_Lote= (TextView) findViewById(R.id.txtv_Lote);

        edtx_OrdenProduccion = (EditText)findViewById(R.id.edtx_OrdenProduccion);
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Cantidad);
        edtx_Empaque = (EditText) findViewById(R.id.edtx_Empaque);

        btn_RegistrarSobrantes = (Button) findViewById(R.id.btn_CerrarOrden);
        Bundle b = getIntent().getExtras();
        OrdenProduccion = b.getString("OrdenProduccion");

        edtx_OrdenProduccion.setText(OrdenProduccion );
    }
    private void agregaListeners()
    {

        edtx_Cantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Cantidad.getText().toString().equals(""))
                    {

                        SegundoPlano sp = new SegundoPlano("RegistrarSobrante");
                        sp.execute();
                        new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);

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
                    new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);
                    return true;
                }

                return false;

            }

        });


        edtx_OrdenProduccion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_OrdenProduccion.getText().toString().equals(""))
                    {

                       SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                        sp.execute();
                        new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);

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
                    new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);
                    return true;
                }
                return false;

            }

        });

        edtx_Empaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_OrdenProduccion.getText().toString().equals(""))
                    {

                       /* SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
                        sp.execute();
                        new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);*/

                       h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Cantidad.requestFocus();

                            }
                        });

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
                                edtx_Empaque.requestFocus();
                                edtx_Empaque.setText("");
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);
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
                if(edtx_OrdenProduccion.getText().toString().equals(""))
                {

                    new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_orden_produccion),"false", true, true);
                    new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);
                    return;
                }
                if(edtx_Empaque.getText().toString().equals(""))
                {

                    new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque),"false", true, true);
                    new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);
                    return;
                }

                if(edtx_Cantidad.getText().toString().equals(""))
                {

                    new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad),"false", true, true);
                    new esconderTeclado(Almacen_Registro_Pallets_Registro_Sobrante.this);
                    return;
                }
                SegundoPlano sp = new SegundoPlano("RegistraSobrante");
                sp.execute();
            }
        });

    }

    private void reiniciarCampos()
    {
        edtx_Cantidad.setText("");
        edtx_Empaque.setText("" );
        edtx_Empaque.requestFocus();
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
                        sa.SOAPConsultaOrdenProduccion(OrdenProduccion, contexto);
                        break;

                    case "RegistraSobrante":
                        sa.SOAPRegistrarSobranteOrdenProduccion(OrdenProduccion, edtx_Cantidad.getText().toString(),edtx_Empaque.getText().toString(),contexto);
                        break;


                }
                decision = sa.getDecision();
                mensaje = sa.getMensaje();

                if (decision.equals("true")) {
                    sacaDatos(tarea);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                mensaje = e.getMessage();
                decision= "false";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (decision.equals("true")) {
                    switch (tarea) {


                        case "ConsultaOrdenProduccion":
                            txtv_Producto.setText(Producto);
                            txtv_Caducidad.setText(Caducidad);
                            txtv_CantidadTotal.setText(CantidadTotal);
                            txtv_CantidadRegistrada.setText(CantidadRegistrada);
                            txtv_Sobrante.setText(Sobrante);
                            txtv_Lote.setText(Lote);

                            edtx_Empaque.requestFocus();
                            break;

                        case "RegistraSobrante":
                            new popUpGenerico(contexto, vista, mensaje, decision, true, true);

                            reiniciarCampos();

                            break;

                    }
                }

                if (decision.equals("false")) {
                    new popUpGenerico(contexto, vista, mensaje, decision, true, true);
                    reiniciarCampos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
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

                            Producto = tabla1.getPrimitivePropertyAsString("Producto");
                            Caducidad= tabla1.getPrimitivePropertyAsString("Caducidad");
                            CantidadTotal= tabla1.getPrimitivePropertyAsString("CantidadTotal");
                            CantidadRegistrada= tabla1.getPrimitivePropertyAsString("CantidadRegistrada");
                            Sobrante= tabla1.getPrimitivePropertyAsString("Sobrante");
                            Lote= tabla1.getPrimitivePropertyAsString("Lote");



                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                    break;

                case "RegistraSobrante":


                    try {

                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {


                                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                                Log.d("SoapResponse", tabla1.toString());

                            CantidadTotal= tabla1.getPrimitivePropertyAsString("CantidadTotal");
                            CantidadRegistrada= tabla1.getPrimitivePropertyAsString("CantidadRegistrada");
                            Sobrante= tabla1.getPrimitivePropertyAsString("Sobrante");


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
