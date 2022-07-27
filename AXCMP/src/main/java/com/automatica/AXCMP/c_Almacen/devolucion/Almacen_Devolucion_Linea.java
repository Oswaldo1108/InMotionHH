package com.automatica.AXCMP.c_Almacen.devolucion;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class Almacen_Devolucion_Linea extends AppCompatActivity
{
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    View vista;
    Context contexto = this;

    EditText edtx_Pallet,edtx_Producto, edtx_Cantidad, edtx_ConfirmaPallet;
    TextView txtv_UnidadMedida;
    String Producto,Contenido,Lote;
    Spinner spn_Resultados;
    ArrayAdapter<String> spinnerArrayAdapter;
    Handler handler = new Handler();
    //String[] DATA_TO_SHOW;
    ArrayList<String> DATA_TO_SHOW;
    ArrayList<SpinnerData> ArraySpinnerData;
    class SpinnerData
    {
        String NumParte,UnidadMedida;

        public SpinnerData(String numParte, String unidadMedida)
        {
            NumParte = numParte;
            UnidadMedida = unidadMedida;
        }

        public String getNumParte() {

            return NumParte;
        }

        public String getUnidadMedida() {
            return UnidadMedida;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_devolucion_linea);
        try
        {
            declaraVariables();
            agregaListeners();

        }catch (Exception e)
        {
            e.printStackTrace();
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
        getSupportActionBar().setTitle(getString(R.string.devolucion_linea));

        //toolbar.setLogo(R.mipmap.logo_axc);

        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                Almacen_Devolucion_Linea.this);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Pallet = (EditText)findViewById(R.id.edtx_Ubicacion);
        edtx_Producto = (EditText)findViewById(R.id.edtx_Producto_Empaque);
        edtx_Cantidad = (EditText)findViewById(R.id.edtx_Empaque);
        edtx_ConfirmaPallet = (EditText)findViewById(R.id.edtx_Ubicacion);
        spn_Resultados = (Spinner) findViewById(R.id.spn_Motivo);

        txtv_UnidadMedida = (TextView) findViewById(R.id.txtv_Caducidad);

        edtx_Pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_ConfirmaPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Producto.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Cantidad.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


    }
    private void agregaListeners()
    {
        edtx_Pallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Pallet.getText().toString().equals(""))
                    {

                        new esconderTeclado(Almacen_Devolucion_Linea.this);
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_pallet),"Advertencia", true, true);

                        handler.post(
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        edtx_Pallet.setText("");
                                        edtx_Pallet.requestFocus();
                                    }
                                }
                        );

                    }

                    return true;
                }
                return false;

            }

        });
        edtx_ConfirmaPallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_ConfirmaPallet.getText().toString().equals(""))
                    {
                        if (edtx_ConfirmaPallet.getText().toString().equals(edtx_Pallet.getText().toString()))
                        {
                            SegundoPlano sp = new SegundoPlano("ConfirmaPallet");
                            sp.execute();
                            new esconderTeclado(Almacen_Devolucion_Linea.this);
                        }
                        else
                        {
                            new popUpGenerico(contexto, vista, getString(R.string.pallets_no_coinciden),"Advertencia", true, true);

                            handler.post(
                                    new Runnable()
                                    {
                                        public void run()
                                        {
                                            edtx_ConfirmaPallet.setText("");
                                            edtx_ConfirmaPallet.requestFocus();
                                        }
                                    }
                            );
                            reiniciarCampos();
                        }
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, "Debe ingresar un empaque","Advertencia", true, true);
                        reiniciarCampos();
                    }

                    return true;
                }
                return false;

            }

        });
        edtx_Producto.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Producto.getText().toString().equals(""))
                    {

                            SegundoPlano sp = new SegundoPlano("BuscarArticulos");
                            sp.execute();
                            new esconderTeclado(Almacen_Devolucion_Linea.this);


                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_producto),"Advertencia", true, true);
                        handler.post(
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        edtx_Producto.setText("");
                                        edtx_Producto.requestFocus();
                                    }
                                }
                        );

                    }

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
                        handler.post(
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        edtx_ConfirmaPallet.setText("");
                                        edtx_ConfirmaPallet.requestFocus();
                                    }
                                }
                        );


                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);
                        handler.post(
                                new Runnable()
                                {
                                    public void run()
                                    {
                                        edtx_Cantidad.setText("");
                                        edtx_Cantidad.requestFocus();
                                    }
                                }
                        );

                    }
                    return true;
                }
                return false;

            }

        });


        spn_Resultados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                txtv_UnidadMedida.setText("");
            }

            @Override

                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String UM = ArraySpinnerData.get(position).getUnidadMedida();
                txtv_UnidadMedida.setText(UM);
            }
        });
    }

    private void reiniciarCampos()
    {
        edtx_ConfirmaPallet.setText("");
        edtx_Producto.setText("");
        edtx_Cantidad.setText("");
        edtx_Pallet.setText("");
        edtx_Pallet.requestFocus();
        DATA_TO_SHOW.clear();
        spn_Resultados.setAdapter(null);
        txtv_UnidadMedida.setText("");

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



                case "ConfirmaPallet" ://Se usa para llenar el Spinner
            sa.SOAPDevolucionPalletLinea(edtx_Pallet.getText().toString(),spn_Resultados.getSelectedItem().toString(),edtx_Cantidad.getText().toString(),contexto);

                    break;
                case "BuscarArticulos":
                        sa.SOAPBuscarArticulos(edtx_Producto.getText().toString(),contexto);
                    break;


            }
            decision =   sa.getDecision();
            mensaje = sa.getMensaje();

            if(decision.equals("true"))
            {
                sacaDatos(tarea);

                if (tarea.equals("BuscarArticulos"))
                {
                   // DATA_TO_SHOW = new String[ArraySpinnerData.size()];
                    DATA_TO_SHOW = new ArrayList<>();
                    spinnerArrayAdapter = new ArrayAdapter<String>
                            (Almacen_Devolucion_Linea.this, android.R.layout.simple_spinner_item,
                                    DATA_TO_SHOW); //selected item will look like a spinner set from XML

                    for(SpinnerData a:ArraySpinnerData)
                    {
                        DATA_TO_SHOW.add(a.getNumParte());

                    }
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);


                }
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



                    case "ConfirmaPallet":
                        new popUpGenerico(contexto, vista, getString(R.string.devolucion_linea_exito),"Registrado", true, true);
                        break;

                    case "BuscarArticulos":
                        spn_Resultados.setAdapter(spinnerArrayAdapter);
                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);

            }
            reiniciarCampos();
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

                case "ConfirmaPallet":
                    for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        try {

                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());

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
                            Producto = tabla1.getPrimitivePropertyAsString("IdMotivo");

                            Contenido= tabla1.getPrimitivePropertyAsString("CantidadActual");
                            Lote = tabla1.getPrimitivePropertyAsString("Lote");


                        }
                        Log.d("SoapResponse", tabla.toString());



                    } catch (Exception e)
                    {

                        e.printStackTrace();
                    }


                    break;
                case "BuscarArticulos":


                    try {
                        ArraySpinnerData = new ArrayList<>();
                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {

                            String NumParte,UnidadMedida;

                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            NumParte= tabla1.getPrimitivePropertyAsString("IdMotivo");
                            UnidadMedida = tabla1.getPrimitivePropertyAsString("Motivo");
                            Log.d("SoapResponse", tabla1.toString());

                            SpinnerData s = new SpinnerData(NumParte,UnidadMedida);
                            ArraySpinnerData.add(s);



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
