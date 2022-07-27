package com.automatica.AXCMP.c_Almacen.Almacen_Cuarentena;

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

public class Almacen_Cuarentena_Marcar_Empaque extends AppCompatActivity
{

    //Esta actividad utiliza el mismo layout que Almacen_Cuarentena_Marcar_Empaque


    SoapAction sa = new SoapAction();
    TextView txtv_Producto, txtv_Empaques, txtv_Cantidad, txtv_Estatus,txtv_UnidadMedida;
    TextView txtv_label_codigo, txtv_label_confirma,txtv_label_empaques;

    EditText edtx_CodigoEmpaque, edtx_ConfirmaEmpaque;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String Producto,Empaques,Cantidad,Ubicacion,UnidadMedida;
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
        edtx_CodigoEmpaque.requestFocus();
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Cuarentena_Marcar_Empaque.this);
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

    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.cuarentena_pallet));

        //toolbar.setLogo(R.mipmap.logo_axc);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_ConfirmaEmpaque = (EditText) findViewById(R.id.edtx_Ubicacion);



        edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_ConfirmaEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Estatus = (TextView) findViewById(R.id.txtv_Cantidad);

        txtv_UnidadMedida= (TextView) findViewById(R.id.txtv_Empaques);
        txtv_label_codigo = (TextView) findViewById(R.id.txtv_label_codigo);
        txtv_label_confirma = (TextView) findViewById(R.id.txtv_label_Confirma);
        txtv_label_empaques = (TextView) findViewById(R.id.txtv_label_empaques);


        edtx_CodigoEmpaque.setHint(getString(R.string.escanear_empaque));
        edtx_ConfirmaEmpaque.setHint(getString(R.string.volver_escanear_empaque));

        txtv_label_codigo.setText(getString(R.string.codigo_empaque));
        txtv_label_confirma.setText(getString(R.string.confirma_empaque));
        txtv_label_empaques.setText("Pallet:");
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

        edtx_ConfirmaEmpaque.setText("");
        edtx_CodigoEmpaque.setText("");
        txtv_Estatus.setText("");
        txtv_Empaques.setText("");
        txtv_Producto.setText("");
        txtv_Cantidad.setText("");
        edtx_CodigoEmpaque.requestFocus();
        txtv_UnidadMedida.setText("");

    }
    private void agregaListeners()
    {
        edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoEmpaque.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("ConsultaEmpaque");
                        sp.execute();
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista,getString(R.string.error_ingrese_empaque),"Advertencia", true, true);
                        borrarDatos();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoEmpaque.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Almacen_Cuarentena_Marcar_Empaque.this);
                }
                return false;
            }
        });
        edtx_ConfirmaEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoEmpaque.getText().toString().equals(""))
                    {

                        if(edtx_CodigoEmpaque.getText().toString().equals(edtx_ConfirmaEmpaque.getText().toString()))
                        {
                            SegundoPlano sp = new SegundoPlano("RegistrarEmpaque");
                            sp.execute();

                        }
                        else
                        {
                            new popUpGenerico(contexto, vista,getString(R.string.empaques_no_coinciden),"Advertencia", true, true);
                            borrarDatos();
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_ConfirmaEmpaque.requestFocus();
                                }
                            });
                        }
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista,getString(R.string.error_ingrese_empaque),"Advertencia", true, true);
                        borrarDatos();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_ConfirmaEmpaque.requestFocus();
                            }
                        });
                    }

                }
                new esconderTeclado(Almacen_Cuarentena_Marcar_Empaque.this);
                return false;
            }
        });
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
                    case "ConsultaEmpaque":
                        sa.SOAPConsultaEmpaque(edtx_CodigoEmpaque.getText().toString(), contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();
                        break;
                    case "RegistrarEmpaque":

                        sa.SOAPCuarentenaMarcaEmpaque(edtx_ConfirmaEmpaque.getText().toString(), contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();

                        break;
                }


                if (decision.equals("true") && tarea.equals("ConsultaEmpaque"))
                {
                        sacaDatos(tarea);
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
                        case "ConsultaEmpaque":
                            txtv_Producto.setText(Producto);
                            txtv_Empaques.setText(Empaques);
                            txtv_Cantidad.setText(Cantidad);
                            txtv_Estatus.setText(Ubicacion);
                            txtv_UnidadMedida.setText(UnidadMedida);


                            break;
                        case "RegistrarEmpaque":
                            new popUpGenerico(contexto, vista, getString(R.string.empaque_cuarentena_exito), "Registrado", true, true);
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




    public void sacaDatos(String Tarea) {
        SoapObject tabla = sa.parser();

        if (tabla == null) tabla = sa.getTablaSoap();



        if (tabla != null)
        {
            switch (Tarea) {
                case "ConsultaEmpaque":
                    try {
                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {
                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());
                            Producto = tabla1.getPrimitivePropertyAsString("NumParte");
                            Empaques = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                            Cantidad = tabla1.getPrimitivePropertyAsString("CantidadActual");
                            Ubicacion = tabla1.getPrimitivePropertyAsString("DescStatus");
                            UnidadMedida = tabla1.getPrimitivePropertyAsString("Revision");


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
