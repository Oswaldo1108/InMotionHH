package com.automatica.AXCMP.c_Almacen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
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

public class Almacen_Colocar extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_CodigoPallet,edtx_CodigoUbicacion;


    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_Producto, txtv_Empaques,txtv_Cantidad,txtv_Caducidad,txtv_Estatus,txtv_UbicacionSugerida;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;



    String Producto,Empaques,Cantidad,Estatus,Caducidad;
    String UbicacionSugerida;

    boolean recargar;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_colocar);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Colocar.this);
        declararVariables();
        agregaListeners();
    }

    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Colocar));
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoUbicacion = (EditText) findViewById(R.id.edtx_Ubicacion);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoUbicacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtv_Producto= (TextView) findViewById(R.id.txtv_Producto);
        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);
        txtv_UbicacionSugerida = (TextView) findViewById(R.id.txtv_UbicacionSugerida);
        txtv_Caducidad = (TextView) findViewById(R.id.txtv_FechaCaducidad);
        txtv_Estatus = (TextView) findViewById(R.id.txtv_Estatus);

    }
    private void agregaListeners()
    {
        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoPallet.requestFocus();
                            }
                        });
                        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                        sp.execute();


                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),false,true,true);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoPallet.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Colocar.this);
                }

                return false;
            }
        });

        edtx_CodigoUbicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoUbicacion.getText().toString().equals(""))
                    {

                        if(edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {

                                    edtx_CodigoPallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),false,true,true);
                            return false;
                        }

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoUbicacion.requestFocus();
                            }
                        });
                        SegundoPlano sp = new SegundoPlano("ColocacionPallet");
                        sp.execute();

                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_ubicacion),false,true,true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoUbicacion.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Colocar.this);
                }

                return false;
            }
        });

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

    private void ReiniciarVariables()
    {
        edtx_CodigoPallet.setText("");
        edtx_CodigoUbicacion.setText("");

        txtv_Empaques.setText("");
        txtv_Producto.setText("");
        txtv_Cantidad.setText("");
        txtv_Estatus.setText("");
        txtv_Caducidad.setText("");
        txtv_UbicacionSugerida.setText("");
        edtx_CodigoPallet.requestFocus();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(!recargar)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                ReiniciarVariables();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        View LastView;

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
            recargar = true;

            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    LastView = getCurrentFocus();
                    LastView.requestFocus();
                    progressBarHolder.requestFocus();
                }
            },10);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try {


                switch (tarea) {
                    case "ConsultaPallet":
                      //  Thread.sleep(5000);
                        sa.SOAPConsultaPallet(edtx_CodigoPallet.getText().toString(), contexto);

                        break;
                    case "ConsultaUbicacionSugerida":

                        sa.SOAPSugierePosicion(edtx_CodigoPallet.getText().toString(), contexto);

                        break;
                    case "ColocacionPallet":

                        sa.SOAPcolocaPallet(edtx_CodigoPallet.getText().toString(), edtx_CodigoUbicacion.getText().toString(), contexto);

                        break;


                }
                mensaje = sa.getMensaje();
                decision = sa.getDecision();
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
                LastView.requestFocus();
                recargar = false;
                if (decision.equals("true")) {
                    switch (tarea) {
                        case "ConsultaPallet":
                            txtv_Cantidad.setText(Cantidad);
                            txtv_Empaques.setText(Empaques);
                            txtv_Producto.setText(Producto);
                            txtv_Caducidad.setText(Caducidad);
                            txtv_Estatus.setText(Estatus);

                            SegundoPlano sp = new SegundoPlano("ConsultaUbicacionSugerida");
                            sp.execute();
                            break;
                        case "ConsultaUbicacionSugerida":
                            txtv_UbicacionSugerida.setText(mensaje);
                            edtx_CodigoUbicacion.requestFocus();
                            break;

                        case "ColocacionPallet":

                            ReiniciarVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_colocado), decision, true, true);
                            break;

                    }

                }

                if (decision.equals("false"))
                {
                    ReiniciarVariables();
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

        }
    }
    private void sacaDatos(String Tarea)
    {
        try
        {



            switch (Tarea)
            {
                case "ConsultaPallet":
                    SoapObject tabla1= sa.getTablaSoap();

                    Producto = tabla1.getPrimitivePropertyAsString("NumParte");
                    Cantidad = tabla1.getPrimitivePropertyAsString("CantidadActual");
                    Empaques = tabla1.getPrimitivePropertyAsString("Empaques");
                    Estatus = tabla1.getPrimitivePropertyAsString("DescStatus");
                    Caducidad= tabla1.getPrimitivePropertyAsString("LoteAXC");
                    break;

                case "ConsultaUbicacionSugerida":
                    SoapObject tabla = sa.parser();
                    if(tabla!=null)
                    {
                        for(int i = 0; i<tabla.getPropertyCount();i++)

                            try {
                                SoapObject tabla11 = (SoapObject) tabla.getProperty(i);

                                UbicacionSugerida = tabla11.getPrimitivePropertyAsString("UbicacionSugerida");
                                Log.d("SoapResponse",tabla11.toString());


                            }catch (Exception e)
                            {

                                e.printStackTrace();
                            }

                    break;
            }


            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }




}
