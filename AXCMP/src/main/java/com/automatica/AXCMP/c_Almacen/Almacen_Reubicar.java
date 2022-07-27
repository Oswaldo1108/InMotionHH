package com.automatica.AXCMP.c_Almacen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
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

public class Almacen_Reubicar extends AppCompatActivity
{
    SoapAction sa = new SoapAction();
    TextView txtv_Producto, txtv_Empaques, txtv_Cantidad, txtv_Ubicacion;
    EditText edtx_CodigoPallet,edtx_NuevaUbicacion;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    boolean recargar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_reubicar);
        declararVariables();
        agregaListeners();
        edtx_CodigoPallet.requestFocus();
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Reubicar.this);
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
        if(!recargar)

            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, vista);
                    }
                if ((id == R.id.borrar_datos))
                    {
                        borrarDatos();
                    }
            }
        return super.onOptionsItemSelected(item);
    }
    private void borrarDatos()
    {
    try
        {


            edtx_NuevaUbicacion.setText("");
            edtx_CodigoPallet.setText("");
            txtv_Ubicacion.setText("");
            txtv_Empaques.setText("");
            txtv_Producto.setText("");
            txtv_Cantidad.setText("");
            edtx_CodigoPallet.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista ,e.getMessage() , false, true, true);
        }
    }
    private void declararVariables()
    {
        try
            {


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Reubicar));
        //toolbar.setSubtitle(" Reubicar Pallet");
        toolbar.setLogo(R.mipmap.logo_axc);//      toolbar.setLogo(R.drawable.axc_logo_toolbar);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_NuevaUbicacion = (EditText) findViewById(R.id.edtx_NuevaUbicacion);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_NuevaUbicacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);
        txtv_Cantidad = (TextView) findViewById(R.id.txtv_CantPend);
        txtv_Ubicacion = (TextView) findViewById(R.id.txtv_Ubicacion);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() , false, true, true);
            }

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
                    SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                    sp.execute();
                }
                else
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            edtx_CodigoPallet.setText("");
                            edtx_CodigoPallet.requestFocus();
                        }
                    });
                    new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_pallet),false, true, true);
                }
                new esconderTeclado(Almacen_Reubicar.this);
            }
            return false;
        }
    });
        edtx_NuevaUbicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_pallet),false, true, true);
                        new esconderTeclado(Almacen_Reubicar.this);
                        return false;
                    }


                    if(!edtx_NuevaUbicacion.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("RegistroNuevaUbicacion");
                        sp.execute();

                    }
                    else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_NuevaUbicacion.setText("");
                                edtx_NuevaUbicacion.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_ubicacion),false, true, true);
                    }

                    new esconderTeclado(Almacen_Reubicar.this);
                }
                return false;
            }
        });
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        String Producto,Empaques,Cantidad,Ubicacion;
        View LastView;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            recargar = true;

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    LastView = getCurrentFocus();
                    LastView.requestFocus();
                    progressBarHolder.requestFocus();
                  //  recargar = false;
                }
            },10);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    String CodigoPallet = edtx_CodigoPallet.getText().toString();
                    String NuevaUbicacion = edtx_NuevaUbicacion.getText().toString();
                    switch (tarea)
                        {
                            case "ConsultaPallet":
                              //  Thread.sleep(5000);
                                sa.SOAPConsultaPallet(CodigoPallet, contexto);
                                decision = sa.getDecision();
                                mensaje = sa.getMensaje();
                                break;
                            case "RegistroNuevaUbicacion":
                                sa.SOAPReubicarEmbalaje(CodigoPallet, NuevaUbicacion, contexto);
                                decision = sa.getDecision();
                                mensaje = sa.getMensaje();
                                break;
                        }


                    if (decision.equals("true") && tarea.equals("ConsultaPallet"))
                        {
                            SoapObject objeto = sa.getTablaSoap();
                            Producto = objeto.getPrimitivePropertyAsString("NumParte");
                            Empaques = objeto.getPrimitivePropertyAsString("Empaques");
                            Cantidad = objeto.getPrimitivePropertyAsString("CantidadActual");
                            Ubicacion = objeto.getPrimitivePropertyAsString("Ubicacion");
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    decision = "false";
                    mensaje = e.getMessage();
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
                {
                LastView.requestFocus();

                    if (decision.equals("true"))
                        {
                            switch (tarea)
                                {
                                    case "ConsultaPallet":
                                        txtv_Producto.setText(Producto);
                                        txtv_Empaques.setText(Empaques);
                                        txtv_Cantidad.setText(Cantidad);
                                        txtv_Ubicacion.setText(Ubicacion);


                                        break;
                                    case "RegistroNuevaUbicacion":
                                        borrarDatos();
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_reubicado_exito), decision, true, true);
                                        break;

                                }
                        }

                    if (decision.equals("false"))
                        {
                            borrarDatos();
                            new popUpGenerico(contexto, getCurrentFocus(), mensaje, "Advertencia", true, true);

                        }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico( contexto,getCurrentFocus(),e.getMessage() ,false , true, true);
                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = false;
        }
    }

}
