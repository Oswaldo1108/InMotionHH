package com.automatica.AXCMP.c_Almacen.devolucion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
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

public class Almacen_Devolucion_Proveedor_Empaque extends AppCompatActivity
{
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    View vista;
    Context contexto = this;

    EditText edtx_Empaque, edtx_ConfirmaPiezas, edtx_PalletConsulta;
    TextView txtv_UnidadMedida,txtv_NumParte,txtv_OrdenCompra,txtv_Lote,txtv_Cantidad,txtv_Estatus,txtv_Empaques;
    String Producto,OrdenCompra,Lote,Cantidad,Estatus,Empaques,Ubicacion;
    TextView txtv_labelEmpaques,txtv_labelUbicacion;
    Spinner spn_Resultados;
    ArrayAdapter<String> spinnerArrayAdapter;
    Handler handler = new Handler();
    TextView txtv_Tipo,txtv_Confirmar;
    ArrayList<String> DATA_TO_SHOW;
    ArrayList<SpinnerData> ArraySpinnerData;
    class SpinnerData
    {
        String IdMotivo, Motivo;

        public SpinnerData(String idMotivo, String motivo)
        {
            IdMotivo = idMotivo;
            Motivo = motivo;
        }

        public String getIdMotivo() {

            return IdMotivo;
        }

        public String getMotivo() {
            return Motivo;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_devolucion_proveedor_pallet);
        try
        {
          declaraVariables();
          agregaListeners();
          SegundoPlano sp = new SegundoPlano("llenaSpinner");
          sp.execute();
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
            if(!edtx_ConfirmaPiezas.getText().toString().equals(""))
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
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.devolucion));
            getSupportActionBar().setSubtitle(getString(R.string.devolucion_proveedor_empaque));

            //toolbar.setLogo(R.mipmap.logo_axc);

            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                    Almacen_Devolucion_Proveedor_Empaque.this);

            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            spn_Resultados = (Spinner) findViewById(R.id.spn_Motivo);
            edtx_Empaque = (EditText) findViewById(R.id.edtx_Pallet);
            edtx_ConfirmaPiezas = (EditText) findViewById(R.id.edtx_Ubicacion);
            edtx_PalletConsulta = (EditText) findViewById(R.id.edtx_ConfirmaPallet);

            txtv_NumParte = (TextView) findViewById(R.id.txtv_Producto);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_Empaques);
            txtv_Lote = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Cantidad = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Estatus = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_Empaques = (TextView) findViewById(R.id.txtv_Emp);

            txtv_labelEmpaques = (TextView) findViewById(R.id.txtv_Empaques_Label);
            txtv_labelUbicacion = (TextView) findViewById(R.id.txtv_Ubicacion);


            edtx_ConfirmaPiezas.setInputType(InputType.TYPE_CLASS_NUMBER);

            edtx_ConfirmaPiezas.setHint(getString(R.string.ingrese_cantidad));
            edtx_Empaque.setHint(getString(R.string.escanear_empaque));
            txtv_Tipo = (TextView) findViewById(R.id.txtv_TipoConsulta);
            txtv_Confirmar = (TextView) findViewById(R.id.txtv_Confirmar);

            txtv_labelUbicacion.setText("Pallet:");
            txtv_labelEmpaques.setText("Ubicación:");


            txtv_Tipo.setText("Empaque:");
            txtv_Confirmar.setText("Cantidad a\nDevolver");

            //txtv_UnidadMedida = (TextView) findViewById(R.id.txtv_Cantidad);

            edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_ConfirmaPiezas.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
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
                        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                        sp.execute();
                        new esconderTeclado(Almacen_Devolucion_Proveedor_Empaque.this);
                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque),"Advertencia", true, true);
                        reiniciarCampos();
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

                        reiniciarCampos();
                    }

                    return true;
                }
                return false;

            }

        });
        edtx_ConfirmaPiezas.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_ConfirmaPiezas.getText().toString().equals(""))
                    {

                            SegundoPlano sp = new SegundoPlano("ConfirmaPallet");
                            sp.execute();
                            new esconderTeclado(Almacen_Devolucion_Proveedor_Empaque.this);

                    }
                    else
                    {
                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);
                        reiniciarCampos();
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
                String UM = ArraySpinnerData.get(position).getMotivo();

            }
        });
    }

    private void reiniciarCampos()
    {
        try {

            new esconderTeclado(Almacen_Devolucion_Proveedor_Empaque.this);
            edtx_ConfirmaPiezas.setText("");
            edtx_Empaque.setText("");
            edtx_Empaque.requestFocus();
            edtx_PalletConsulta.setText("");

            txtv_NumParte.setText("");
            txtv_OrdenCompra.setText("");
            txtv_Lote.setText("");
            txtv_Cantidad.setText("");
            txtv_Estatus.setText("");
            txtv_Empaques.setText("");

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


                case "llenaSpinner":
                    sa.SOAPConsultaMotivosDevolucion(contexto);

                    break;
                case "ConfirmaPallet" :
                    sa.SOAPDevuelveEmpaque(ArraySpinnerData.get(spn_Resultados.getSelectedItemPosition()).getIdMotivo(),
                            edtx_Empaque.getText().toString(), edtx_ConfirmaPiezas.getText().toString(),contexto);
                    break;
                case "ConsultaPallet" :
                    sa.SOAPConsultaEmpaque(edtx_Empaque.getText().toString(),contexto);

                    break;


            }
            decision =   sa.getDecision();
            mensaje = sa.getMensaje();

            if(decision.equals("true"))
            {
                sacaDatos(tarea);

                if (tarea.equals("llenaSpinner"))
                {
                    // DATA_TO_SHOW = new String[ArraySpinnerData.size()];
                    DATA_TO_SHOW = new ArrayList<>();
                    spinnerArrayAdapter = new ArrayAdapter<String>
                            (Almacen_Devolucion_Proveedor_Empaque.this, android.R.layout.simple_spinner_item,
                                    DATA_TO_SHOW); //selected item will look like a spinner set from XML

                    for(SpinnerData a:ArraySpinnerData)
                    {
                        DATA_TO_SHOW.add(a.getMotivo());

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
                        reiniciarCampos();
                        break;

                    case "llenaSpinner":
                        spn_Resultados.setAdapter(spinnerArrayAdapter);
                        break;
                    case "ConsultaPallet":
                        txtv_NumParte.setText(Producto);
                        txtv_OrdenCompra.setText(OrdenCompra);
                        txtv_Lote.setText(Lote);
                        txtv_Cantidad.setText(Cantidad);
                        txtv_Estatus.setText(Estatus);
                        txtv_Empaques.setText(Empaques);
                        edtx_PalletConsulta.setText(Ubicacion);
                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
                reiniciarCampos();
            }
            //reiniciarCampos();
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
                            try {

                                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                                Producto = tabla1.getPrimitivePropertyAsString("NumParte");
                                OrdenCompra = tabla1.getPrimitivePropertyAsString("OrdenCompra");
                                Lote = tabla1.getPrimitivePropertyAsString("LoteProveedor");
                                Cantidad = tabla1.getPrimitivePropertyAsString("CantidadActual");
                                Estatus = tabla1.getPrimitivePropertyAsString("DescStatus");
                                Empaques =  tabla1.getPrimitivePropertyAsString("Ubicacion");
                                Ubicacion = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                Log.d("SoapResponse", tabla1.toString());

                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }


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
                case "llenaSpinner":


                    try {
                        ArraySpinnerData = new ArrayList<>();
                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {

                            String IdMotivo,Motivo;

                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            IdMotivo= tabla1.getPrimitivePropertyAsString("IdMotivo");
                            Motivo = tabla1.getPrimitivePropertyAsString("DMotivo");
                            Log.d("SoapResponse", tabla1.toString());

                         SpinnerData s = new SpinnerData(IdMotivo,Motivo);
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
