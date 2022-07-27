package com.automatica.AXCMP.c_Almacen.Almacen_Cuarentena;

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

public class Almacen_Cuarentena_Marcar_Pallet extends AppCompatActivity
{
    SoapAction sa = new SoapAction();
    TextView txtv_Producto, txtv_Empaques, txtv_Cantidad, txtv_Estatus,txtv_UnidadMedida;
    EditText edtx_CodigoPallet, edtx_ConfirmaPallet;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    View vista;
    Context contexto = this;
    Handler h = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_cuarentena_marcar_pallet);
        declararVariables();
        agregaListeners();
        edtx_CodigoPallet.requestFocus();
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Cuarentena_Marcar_Pallet.this);
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

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }
        if((id == R.id.borrar_datos))
        {
            borrarDatos();
        }

        return super.onOptionsItemSelected(item);
    }
    private void borrarDatos()
    {

        edtx_ConfirmaPallet.setText("");
        edtx_CodigoPallet.setText("");
        txtv_Estatus.setText("");
        txtv_Empaques.setText("");
        txtv_Producto.setText("");
        txtv_Cantidad.setText("");
        edtx_CodigoPallet.requestFocus();
        txtv_UnidadMedida.setText("");

    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.cuarentena_pallet));

        //toolbar.setLogo(R.mipmap.logo_axc);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_ConfirmaPallet = (EditText) findViewById(R.id.edtx_Ubicacion);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_ConfirmaPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Estatus = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_UnidadMedida= (TextView) findViewById(R.id.txtv_Empaques);

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
                        new popUpGenerico(contexto, vista,getString(R.string.error_ingrese_pallet),"Advertencia", true, true);
                        borrarDatos();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoPallet.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Cuarentena_Marcar_Pallet.this);
                }
                return false;
            }
        });
        edtx_ConfirmaPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPallet.getText().toString().equals(""))
                    {

                        if(edtx_CodigoPallet.getText().toString().equals(edtx_ConfirmaPallet.getText().toString()))
                        {
                            SegundoPlano sp = new SegundoPlano("RegistrarPallet");
                            sp.execute();

                        }
                        else
                        {
                            new popUpGenerico(contexto, vista,getString(R.string.pallets_no_coinciden),"Advertencia", true, true);
                            borrarDatos();
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_ConfirmaPallet.requestFocus();
                                }
                            });
                        }
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista,getString(R.string.error_ingrese_pallet),"Advertencia", true, true);
                        borrarDatos();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_ConfirmaPallet.requestFocus();
                            }
                        });
                    }

                }
                new esconderTeclado(Almacen_Cuarentena_Marcar_Pallet.this);
                return false;
            }
        });
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        String Producto,Empaques,Cantidad,Ubicacion,UnidadMedida;
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
                    case "ConsultaPallet":
                        sa.SOAPConsultaPallet(edtx_CodigoPallet.getText().toString(), contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();
                        break;
                    case "RegistrarPallet":

                            sa.SOAPCuarentenaMarcaPallet(edtx_ConfirmaPallet.getText().toString(), contexto);
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
                    Ubicacion = objeto.getPrimitivePropertyAsString("DescStatus");
                    UnidadMedida = objeto.getPrimitivePropertyAsString("Revision");
                }
            }catch (Exception e)
            {
                mensaje = e.getMessage();
                decision = "false";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (decision.equals("true")) {
                    switch (tarea) {
                        case "ConsultaPallet":
                            txtv_Producto.setText(Producto);
                            txtv_Empaques.setText(Empaques);
                            txtv_Cantidad.setText(Cantidad);
                            txtv_Estatus.setText(Ubicacion);
                            txtv_UnidadMedida.setText(UnidadMedida);


                            break;
                        case "RegistrarPallet":
                            new popUpGenerico(contexto, vista, getString(R.string.pallet_cuarentena_exito), "Registrado", true, true);
                            borrarDatos();
                            break;

                    }
                }

                if (decision.equals("false")) {
                    new popUpGenerico(contexto, vista, mensaje, "Advertencia", true, true);
                    borrarDatos();

                }

                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), "Advertencia", true, true);
                e.printStackTrace();
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }
        }
    }


}
