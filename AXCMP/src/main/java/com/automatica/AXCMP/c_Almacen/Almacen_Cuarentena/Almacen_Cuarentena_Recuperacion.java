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

public class Almacen_Cuarentena_Recuperacion extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    View vista;
    Context contexto = this;

    EditText edtx_Empaque, edtx_ConfirmaPallet, edtx_CantidadEmpaques;
    EditText edtx_EmpReg,edtx_EmpaqueNuevo;
    TextView txtv_UnidadMedida,txtv_NumParte,txtv_Cantidad;

    String Producto,UnidadMedida,Cantidad;
    String PalletRecuperado;
    String EmpReg,Pallet;
    Button btn_CerrarTarima;

    Handler handler = new Handler();
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_cuarentena_recuperacion);
        try
        {

          declaraVariables();
          agregaListeners();

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
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

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }

        if((id == R.id.recargar))
        {
            if(!edtx_ConfirmaPallet.getText().toString().equals(""))
            {
              SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                sp.execute();
            }
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
        getSupportActionBar().setTitle(getString(R.string.devolucion_proveedor_pallet));

        //toolbar.setLogo(R.mipmap.logo_axc);

        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                Almacen_Cuarentena_Recuperacion.this);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Empaque = (EditText)findViewById(R.id.edtx_Cantidad);

        edtx_CantidadEmpaques = (EditText)findViewById(R.id.edtx_Empaque);
        edtx_EmpaqueNuevo = (EditText)findViewById(R.id.edtx_Ubicacion);

        edtx_EmpReg = (EditText) findViewById(R.id.edtx_CantidadEmpaques);

        edtx_ConfirmaPallet = (EditText)findViewById(R.id.edtx_ConfirmaPallet);


        txtv_NumParte = (TextView) findViewById(R.id.txtv_Producto);
        txtv_UnidadMedida = (TextView) findViewById(R.id.txtv_Empaques);
        txtv_Cantidad= (TextView) findViewById(R.id.txtv_Caducidad);


        btn_CerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);



        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CantidadEmpaques.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_EmpaqueNuevo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_ConfirmaPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


    }
    private void agregaListeners()
    {
        edtx_Empaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Empaque.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("ConsultarEmpaque");
                        sp.execute();
                        new esconderTeclado(Almacen_Cuarentena_Recuperacion.this);
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque),"Advertencia", true, true);

                        handler.post(
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                    }
                                }
                        );

                    }

                    return true;
                }
                return false;

            }

        });
        edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CantidadEmpaques.getText().toString().equals(""))
                    {

                        if(!(Double.valueOf(edtx_CantidadEmpaques.getText().toString())>999999))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_EmpaqueNuevo.requestFocus();
                                }
                            });
                        }else
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_CantidadEmpaques.setText("");
                                    edtx_CantidadEmpaques.requestFocus();
                                }
                            });


                            new popUpGenerico(contexto, vista, getString(R.string.error_cantidad_mayor_999999),"Advertencia", true, true);
                            //reiniciarCampos();

                        }

                            new esconderTeclado(Almacen_Cuarentena_Recuperacion.this);

                    }
                    else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CantidadEmpaques.setText("");
                                edtx_CantidadEmpaques.requestFocus();
                            }
                        });


                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);
                        //reiniciarCampos();

                    }

                    return true;
                }
                return false;

            }

        });

        edtx_EmpaqueNuevo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_EmpaqueNuevo.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("RecuperaEmpaque");
                        sp.execute();
                        new esconderTeclado(Almacen_Cuarentena_Recuperacion.this);

                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque),"Advertencia", true, true);
                      //  reiniciarCampos();
                    }

                    return true;
                }
                return false;

            }

        });

        btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if((edtx_Empaque== null)||(edtx_Empaque.getText().toString().equals("")))
                {
                    new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"Advertencia",true,true);
                    return;
                }
                    if((edtx_CantidadEmpaques == null)||(edtx_CantidadEmpaques.getText().toString().equals("")))
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_cantidad),"Advertencia",true,true);
                        return;
                    }
                    if((edtx_EmpaqueNuevo == null)||(edtx_EmpaqueNuevo.getText().toString().equals("")))
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"Advertencia",true,true);
                        return;
                    }


                    SegundoPlano sp= new SegundoPlano("CierraPallet");
                    sp.execute();

            }
        });
    }

    private void reiniciarCampos()
    {
        try {


            edtx_ConfirmaPallet.setText("");
            edtx_Empaque.setText("");
            edtx_Empaque.requestFocus();
            edtx_CantidadEmpaques.setText("");

            txtv_UnidadMedida.setText("");
            txtv_NumParte.setText("");
            txtv_Cantidad.setText("");
            edtx_EmpaqueNuevo.setText("");
            edtx_EmpReg.setText("");

            edtx_Empaque.requestFocus();

        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
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

            switch (tarea)
            {


                case "ConsultarEmpaque":
                    sa.SOAPConsultarEmpaque(edtx_Empaque.getText().toString(),contexto);

                    break;
                case "RecuperaEmpaque" :
                    sa.SOAPCuarentenaRecuperaEmpaque(edtx_Empaque.getText().toString(),edtx_EmpaqueNuevo.getText().toString(),edtx_CantidadEmpaques.getText().toString(),contexto);

                    break;
                case "CierraPallet" :
                    sa.SOAPCierraPalletMPCuarentena(edtx_ConfirmaPallet.getText().toString(),contexto);

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
            try {
                if (decision.equals("true")) {
                    switch (tarea) {


                        case "CierraPallet":
                            new popUpGenerico(contexto, vista, getString(R.string.pallet_recuperado_exito) +"["+PalletRecuperado+"]", "Registrado", true, true);
                            reiniciarCampos();
                            break;

                        case "ConsultarEmpaque":
                            txtv_NumParte.setText(Producto);
                            txtv_UnidadMedida.setText(UnidadMedida);
                            txtv_Cantidad.setText(Cantidad);
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_CantidadEmpaques.requestFocus();
                                }
                            });
                            break;
                        case "RecuperaEmpaque":
                            edtx_EmpReg.setText(EmpReg);
                            edtx_ConfirmaPallet.setText(Pallet);
                            break;


                    }
                }

                if (decision.equals("false")) {
                    new popUpGenerico(contexto, vista, mensaje, "Advertencia", true, true);

                }
                //reiniciarCampos();
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() ,"false" ,true , true);
            }

        }
    }
    public void sacaDatos(String Tarea) {
        SoapObject tabla = sa.parser();

        if (tabla == null) tabla = sa.getTablaSoap();



        if (tabla != null)
        {

            switch (Tarea) {


                case "RecuperaEmpaque":


                    for (int j = 0; j < tabla.getPropertyCount(); j++)
                    {
                        try {

                            SoapObject tabla1 = (SoapObject) tabla.getProperty(j);
                            Log.d("SoapResponse", tabla1.toString());
                            Pallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                            EmpReg = tabla1.getPrimitivePropertyAsString("CantEmpaques");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }

                    break;

                case "CierraPallet":

                        try {


                            Log.d("SoapResponse", tabla.toString());

                            PalletRecuperado = tabla.getPrimitivePropertyAsString("Texto");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    break;

                case "ConsultarEmpaque":


                    try {
                            Producto = tabla.getPrimitivePropertyAsString("NumParte");
                            UnidadMedida = tabla.getPrimitivePropertyAsString("Revision");
                            Cantidad = tabla.getPrimitivePropertyAsString("Cantidad");


                      /*  for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {


                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());



                        }*/
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
