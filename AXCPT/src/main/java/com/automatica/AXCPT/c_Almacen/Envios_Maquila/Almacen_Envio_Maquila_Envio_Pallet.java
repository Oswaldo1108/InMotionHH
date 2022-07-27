package com.automatica.AXCPT.c_Almacen.Envios_Maquila;

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


import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Almacen_Envio_Maquila_Envio_Pallet extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    Handler h = new Handler();

    View vista;
    Context contexto = this;
    String Partida,OrdenCompra,Producto,Cantidad;
    EditText edtx_ConfirmaPallet;
    TextView txtv_Pallet,txtv_OrdenCompra,txtv_Caducidad,txtv_Producto,txtv_Empaques,txtv_Contenido,txtv_Lote,txtv_Estatus;
    String ProductoConsultaPallet,CaducidadConsultaPallet,Empaques,ContenidoConsultaPAllet,LoteConsultaPallet,Estatus,SugierePallet;
    String Transferencia;
    TextView txtv_Partida,txtv_Articulo,txtv_Cantidad,txtv_SugierePallet;
    SegundoPlano segundoPlano;
    boolean recarga;
    //endregion
    boolean debug = true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_envio_maquila__surtir__pallet);
        try
        {
            declaraVariables();
            agregaListeners();

            SacaDatosIntent();
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }

    }
    private void SacaDatosIntent() throws NullPointerException
    {
        Bundle b = getIntent().getExtras();



        Partida = b.getString("Partida");
        OrdenCompra = b.getString("OrdenCompra");
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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume()
    {
        if(debug == false)
        {
            SegundoPlano sp = new SegundoPlano("ConsultaOrdenCompra");
            sp.execute();
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {try
    {
        if(recarga)
        {
            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }

            if ((id == R.id.recargar))
            {
                if (!edtx_ConfirmaPallet.getText().toString().equals("")) {
                    SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                    sp.execute();
                }
            }
            if ((id == R.id.borrar_datos))
            {
                reiniciarCampos();
            }
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
            this.getSupportActionBar().setTitle(" "+getString(R.string.Transferencia_Envio_Pallet));
            // toolbar.setSubtitle(" Envio de Pallet");
//            toolbar.setLogo(R.mipmap.logo_axc);//      toolbar.setLogo(R.drawable.axc_logo_toolbar);

            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                    Almacen_Envio_Maquila_Envio_Pallet.this);
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            txtv_Pallet= (TextView)findViewById(R.id.txtv_PalletSugerido);

            txtv_Partida= (TextView) findViewById(R.id.txtv_Partida);
            txtv_Articulo = (TextView) findViewById(R.id.txtv_Producto);
                    txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);


            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_Caducidad= (TextView) findViewById(R.id.txtv_Producto);
            txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
            txtv_Empaques= (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Contenido= (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_Lote= (TextView) findViewById(R.id.txtv_Lote);
            txtv_Estatus= (TextView) findViewById(R.id.txtv_Cantidad);

            txtv_SugierePallet = (TextView) findViewById(R.id.txtv_PalletSugerido);

            edtx_ConfirmaPallet = (EditText)findViewById(R.id.edtx_CantPorEmpaque);


        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }

    }
    private void agregaListeners()
    {
        try
        {

            edtx_ConfirmaPallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_ConfirmaPallet.getText().toString().equals(""))
                        {
                            if (txtv_Pallet.getText().toString().equals(edtx_ConfirmaPallet.getText().toString()))
                            {
                                SegundoPlano sp = new SegundoPlano("ConfirmaPallet");
                                sp.execute();
                                new esconderTeclado(Almacen_Envio_Maquila_Envio_Pallet.this);
                            }
                            else
                            {
                                new popUpGenerico(contexto, vista, getString(R.string.pallets_no_coinciden),"Advertencia", true, true);
                                reiniciarCampos();
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        reiniciarCampos();
                                        edtx_ConfirmaPallet.requestFocus();
                                    }
                                });
                                new esconderTeclado(Almacen_Envio_Maquila_Envio_Pallet.this);
                            }
                        }
                        else
                        {
                            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_pallet),"Advertencia", true, true);

                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    reiniciarCampos();
                                    edtx_ConfirmaPallet.requestFocus();
                                }
                            });
                            new esconderTeclado(Almacen_Envio_Maquila_Envio_Pallet.this);
                        }

                        return true;
                    }
                    return false;

                }

            });

        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
            e.printStackTrace();
        }


    }

    private void reiniciarCampos()
    {
        try
        {
            edtx_ConfirmaPallet.setText("");
            txtv_OrdenCompra.setText("");
            txtv_Caducidad.setText("");
            txtv_Contenido.setText("");
            txtv_Producto.setText("");
            txtv_Empaques.setText("");
            txtv_Estatus.setText("");
            txtv_Lote.setText("");
           edtx_ConfirmaPallet.requestFocus();
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
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            //    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
            try
            {
                switch (tarea)
                {

                    case "ConsultaOrdenCompra":

                        sa.SOAPConsultarPartidaEnvioMaquila(OrdenCompra,Partida,contexto);

                        break;

                    case "SugierePallet":

                        sa.SOAPSugierePalletEnvioMaquila(OrdenCompra,Partida,contexto);
                        break;

                    case "ConsultaPallet":

                        sa.SOAPConsultaPallet(txtv_Pallet.getText().toString(),contexto);

                        break;

                    case "ConfirmaPallet" :
                        sa.SOAPEnviaPalletMaquila(OrdenCompra,Partida,edtx_ConfirmaPallet.getText().toString(),"",contexto);

                        break;


                }

                decision =   sa.getDecision();
                mensaje = sa.getMensaje();

                if(decision.equals("true"))
                {
                    sacaDatos(tarea);
                }
            }catch (Exception e)
            {
               decision = "false";
               mensaje = e.getMessage();
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

                        txtv_SugierePallet.setText(SugierePallet);
                        break;

                    case "ConsultaPallet":

                        txtv_OrdenCompra.setText("");
                        txtv_Caducidad.setText("");
                        txtv_Contenido.setText(ContenidoConsultaPAllet);
                        txtv_Producto.setText(Producto);
                        txtv_Empaques.setText(Empaques);
                        txtv_Estatus.setText(Estatus);

                        txtv_Lote.setText(LoteConsultaPallet);

                        edtx_ConfirmaPallet.requestFocus();
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
        }
    }


    public void sacaDatos(String Tarea)
    {
        SoapObject tabla = sa.parser();

        if (tabla == null) tabla = sa.getTablaSoap();



        if (tabla != null)
        {

            switch (Tarea) {

                case "ConfirmaPallet":
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
                         /*   Log.d("SoapResponse", tabla.toString());


                            Producto = tabla.getPrimitivePropertyAsString("NumParte");
                            Empaques = tabla.getPrimitivePropertyAsString("Empaques");
                            Contenido= tabla.getPrimitivePropertyAsString("CantidadActual");
                            Lote = tabla.getPrimitivePropertyAsString("LoteProveedor");
                            Estatus = tabla.getPrimitivePropertyAsString("DescStatus");*/
                        } catch (Exception e)
                        {

                            e.printStackTrace();
                        }


                    break;
            }

        }
    }

}
