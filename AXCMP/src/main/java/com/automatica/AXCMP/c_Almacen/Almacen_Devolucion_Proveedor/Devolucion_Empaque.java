package com.automatica.AXCMP.c_Almacen.Almacen_Devolucion_Proveedor;

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

public class Devolucion_Empaque extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    TextView txtv_Empaque, txtv_OrdenCompra, txtv_Caducidad, txtv_Producto, txtv_Contenido, txtv_Lote;
    TextView txtv_PalletSugerido;
    EditText edtx_ConfirmaEmpaque, edtx_Comentario;
    String Producto, Contenido, Lote, Caducidad, OrdenCompra,Cantidad;
    Handler h = new Handler();
    String ProductoConsultaPallet,CaducidadConsultaPallet,ContenidoConsultaPAllet,LoteConsultaPallet,Estatus;
    TextView txtv_Partida, txtv_Articulo, txtv_Cantidad;
    String Partida,PalletSugerido;
    SegundoPlano segundoPlano;
    boolean recarga;
    //endregion
    boolean debug = true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.devolucion_empaque);
            declaraVariables();
            agregaListeners();
            SacaDatosIntent();

        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, vista, e.getMessage() + " " + e.getCause(), "false", true, true);
        }
    }


    @Override
    protected void onResume()
    {
        if(!debug)
        {
            SegundoPlano sp = new SegundoPlano("ConsultaOrdenCompra");
            sp.execute();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void SacaDatosIntent() throws NullPointerException
    {
        Bundle b = getIntent().getExtras();



        Partida = b.getString("Partida");
        OrdenCompra = b.getString("OrdenCompra");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(recarga) {
            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }

            if ((id == R.id.recargar))
            {
            /*if(!edtx_ConfirmaEmpaque.getText().toString().equals(""))
            {
                SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                sp.execute();
            }*/
            }
            if ((id == R.id.borrar_datos)) {
                reiniciarCampos();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void declaraVariables()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(getString(R.string.devolucion_proveedor_empaque));


            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                    Devolucion_Empaque.this);

            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            txtv_Partida = (TextView) findViewById(R.id.txtv_Partida);
            txtv_Articulo = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);


            txtv_Empaque = (TextView) findViewById(R.id.txtv_PalletSugerido);


            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_Caducidad = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_Contenido = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_Lote = (TextView) findViewById(R.id.txtv_Lote);

            txtv_PalletSugerido = (TextView) findViewById(R.id.txtv_PalletSugerido);
            edtx_ConfirmaEmpaque = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Comentario = (EditText) findViewById(R.id.edtx_Comentario);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }

    }
    private void agregaListeners()
    {

        edtx_ConfirmaEmpaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_ConfirmaEmpaque.getText().toString().equals(""))
                    {
                        if (edtx_ConfirmaEmpaque.getText().toString().equals(edtx_ConfirmaEmpaque.getText().toString()))
                        {
                          SegundoPlano sp = new SegundoPlano("ConfirmaPallet");
                            sp.execute();
                            new esconderTeclado(Devolucion_Empaque.this);
                        }
                        else
                        {
                            new popUpGenerico(contexto, vista, getString(R.string.empaques_no_coinciden),"false", true, true);
                            reiniciarCampos();
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_ConfirmaEmpaque.requestFocus();
                                }
                            });
                        }
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque),"false", true, true);
                        reiniciarCampos();
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_ConfirmaEmpaque.requestFocus();
                            }
                        });
                    }

                    return true;
                }
                return false;

            }

        });


    }

    private void reiniciarCampos()
    {
        edtx_ConfirmaEmpaque.setText("");
        txtv_Producto.setText("");
        txtv_Contenido.setText("");
        txtv_Lote.setText("");

        edtx_ConfirmaEmpaque.requestFocus();
        txtv_Caducidad.setText("");
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
            recarga = false;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            //    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();

            switch (tarea)
            {
                case "ConsultaOrdenCompra":

                    sa.SOAPConsultarPartidaDevolucionProveedor(OrdenCompra,Partida,contexto);

                    break;
                case "SugierePallet":

                    sa.SOAPSugierePalletDevolucionProveedor(OrdenCompra,Partida,contexto);
                    break;

                case "ConsultaPallet":

                    sa.SOAPConsultaEmpaqueRegular(txtv_Empaque.getText().toString(),contexto);

                    break;

                case "ConfirmaPallet" :
                    sa.SOAPDevolucionEmpaqueProveedor(OrdenCompra, Partida, edtx_ConfirmaEmpaque.getText().toString(), "",contexto );

                    break;


            }

            decision =   sa.getDecision();
            mensaje = sa.getMensaje();

            if(decision.equals("true"))
            {
                sacaDatos(tarea);
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

                    case "ConsultaOrdenCompra":

                      txtv_OrdenCompra.setText(OrdenCompra);
                      txtv_Partida.setText(Partida);
                      txtv_Producto.setText(Producto);
                      txtv_Cantidad.setText(Cantidad);
                      segundoPlano = new SegundoPlano("SugierePallet");
                        segundoPlano.execute();
                        break;

                    case "SugierePallet":

                        txtv_PalletSugerido.setText(PalletSugerido);
                        break;

                        case "ConsultaPallet":

                        txtv_Producto.setText(Producto);


                        txtv_Producto.setText(Producto);
                        txtv_Contenido.setText(Contenido);
                        txtv_Lote.setText(Lote);
                        txtv_Caducidad.setText(Caducidad);
                        txtv_OrdenCompra.setText(OrdenCompra);


                        edtx_ConfirmaEmpaque.requestFocus();
                        break;
                    case "ConfirmaPallet":
                        new popUpGenerico(contexto, vista, mensaje,"Registrado", true, true);
                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
                  reiniciarCampos();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recarga = true;
        }
    }


    public void sacaDatos(String Tarea)
    {
        SoapObject tabla = sa.parser();

        if (tabla == null) tabla = sa.getTablaSoap();



        if (tabla != null)
        {

            switch (Tarea) {

                case "ConsultaOrdenCompra":
                    for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        try {

                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());

                            Producto = tabla1.getPrimitivePropertyAsString("Producto");
                            Cantidad= tabla1.getPrimitivePropertyAsString("Cantidad");


                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                    break;

                case "ConsultaPallet":


                    try {

                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {


                                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                                Log.d("SoapResponse", tabla1.toString());


                            ProductoConsultaPallet = tabla1.getPrimitivePropertyAsString("Producto");
                            CaducidadConsultaPallet= tabla1.getPrimitivePropertyAsString("Caducidad");
                            //Empaques= tabla1.getPrimitivePropertyAsString("Empaques");
                            ContenidoConsultaPAllet = tabla1.getPrimitivePropertyAsString("Contenido");
                            LoteConsultaPallet = tabla1.getPrimitivePropertyAsString("Lote");
                            Estatus = tabla1.getPrimitivePropertyAsString("Estatus");


                        }
                        Log.d("SoapResponse", tabla.toString());



                    } catch (Exception e)
                    {

                        e.printStackTrace();
                    }


                    break;
                case "SugierePallet":
                    try {

                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {


                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());

                        PalletSugerido  = tabla1.getPrimitivePropertyAsString("PalletSugerido");



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
