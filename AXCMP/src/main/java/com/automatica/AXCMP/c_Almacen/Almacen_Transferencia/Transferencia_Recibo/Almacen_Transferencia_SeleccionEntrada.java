package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Recibo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;


import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class Almacen_Transferencia_SeleccionEntrada extends AppCompatActivity
{

    //region variables


    Button BotonSurtirPartida;
    Spinner spnr_Transfer;


    SoapAction sa = new SoapAction();

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    View vista;
    Context contexto = this;


    String[] TipoAjuste;
    String Transferencia;
    ArrayAdapter<String> spinnerArrayAdapter;


    class datosTabla
    {
        String Producto,Lote,CantidadSolicitada,CantidadSurtida,CantidadPendiente;

        public datosTabla(String producto, String lote, String cantidadSolicitada, String cantidadSurtida, String cantidadPendiente)
        {
            Producto = producto;
            Lote = lote;
            CantidadSolicitada = cantidadSolicitada;
            CantidadSurtida = cantidadSurtida;
            CantidadPendiente = cantidadPendiente;
        }

        public String getProducto() {
            return Producto;
        }

        public String getLote() {
            return Lote;
        }

        public String getCantidadSolicitada() {
            return CantidadSolicitada;
        }

        public String getCantidadSurtida() {
            return CantidadSurtida;
        }

        public String getCantidadPendiente() {
            return CantidadPendiente;
        }
    }
    ArrayList<datosTabla> arrayDatosTabla;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_transferencia__seleccion_entrada);
        declararVariables();
        agregaListeners();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Transferencia_SeleccionEntrada.this);

        //BotonSurtirPartida.setEnabled(false);

        SegundoPlano sp = new SegundoPlano("llenaDropdown");
        sp.execute();
    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Reubicar));


        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        BotonSurtirPartida = (Button) findViewById(R.id.btn_Consultar);

        spnr_Transfer = (Spinner) findViewById(R.id.spnr_Transferencia);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
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


            // BotonSurtirPartida.setEnabled(false);
        }

        return super.onOptionsItemSelected(item);
    }


    private void agregaListeners()
    {

        BotonSurtirPartida.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


            }
        });
        spnr_Transfer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText( Almacen_Transferencia_Envio.this,String.valueOf(position), Toast.LENGTH_SHORT).show();

                SegundoPlano sp = new SegundoPlano("ConsultaTransfer");
                sp.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

            //    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();

            switch (tarea)
            {

                case "ConsultaTransfer":
                    String Interorg = spnr_Transfer.getSelectedItem().toString();
                    Transferencia = Interorg;
                    sa.SOAPConsultaPalletSalidaLiberado(Interorg,contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();
                    break;

                case "llenaDropdown" ://Se usa para llenar el Spinner
                    sa.SOAPListarTransferenciasARecibir(contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();

                    break;


            }

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


                    case "ConsultaTransfer":

                        //BotonSurtirPartida.setEnabled(false);


                        break;
                    case "llenaDropdown":
                        if(TipoAjuste!=null)
                        {

                            spinnerArrayAdapter = new ArrayAdapter<String>
                                    (Almacen_Transferencia_SeleccionEntrada.this, android.R.layout.simple_spinner_item,
                                            TipoAjuste); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            spnr_Transfer.setAdapter(spinnerArrayAdapter);
                        }else
                        {
                           decision = "false";
                           mensaje = "No hay transferencias a recibir abiertas";
                        }
                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
                //   reiniciaCampos();
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

        if (Tarea.equals("llenaDropdown")) TipoAjuste = new String[tabla.getPropertyCount()];


        if (tabla != null)
        {
            arrayDatosTabla = new ArrayList<>();
            switch (Tarea) {
                case "llenaDropdown":
                    for (int i = 0; i < tabla.getPropertyCount(); i++) {
                        try {


                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());
                            TipoAjuste[i] = tabla1.getPrimitivePropertyAsString("Transfer");
                        } catch (Exception e)
                        {

                            e.printStackTrace();
                            TipoAjuste = null;
                        }
                    }
                    break;

                case "ConsultaTransfer":
                    for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        SoapObject tabla1 = (SoapObject) tabla.getProperty(i);

                        try {
                            String Producto, Lote, CantidadSolicitada, CantidadSurtida, CantidadPendiente;
                            Producto = tabla1.getPrimitivePropertyAsString("Producto");
                            Lote = tabla1.getPrimitivePropertyAsString("Lote");
                            CantidadSolicitada = tabla1.getPrimitivePropertyAsString("CantidadSolicitada");
                            CantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                            CantidadSurtida = tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                            datosTabla dt = new datosTabla(Producto, Lote, CantidadSolicitada, CantidadSurtida, CantidadPendiente);
                            arrayDatosTabla.add(dt);
                            Log.d("SoapResponse", tabla1.toString());
                        } catch (Exception e)
                        {

                            e.printStackTrace();
                        }

                    }
                    break;
            }

        }
    }



}
