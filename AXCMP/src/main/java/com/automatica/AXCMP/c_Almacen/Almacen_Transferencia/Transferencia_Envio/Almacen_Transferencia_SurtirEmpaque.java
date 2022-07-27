package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

import android.content.Context;
import android.content.DialogInterface;
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
import com.automatica.AXCMP.Servicios.CreaDialogos;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Almacen_Transferencia_SurtirEmpaque extends AppCompatActivity
{


    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;


    View vista;
    Context contexto = this;
    Handler h = new Handler();

    EditText edtx_Empaque,edtx_ConfirmaEmpaque;
    TextView txtv_Producto,txtv_Cantidad,txtv_Lote;
    Button btn_CerrarPallet;
    String Producto,Empaques,Contenido,Lote,Estatus;
    String Transferencia,Partida,Pallet;
    boolean Recarga;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_transferencia_surtir_empaque_v3);
        declaraVariables();
        agregaListeners();
        Bundle b = getIntent().getExtras();
        Transferencia = b.getString("Transferencia");
        Partida= b.getString("Partida");
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

        if(!Recarga)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, getCurrentFocus());
            }

            if ((id == R.id.recargar)) {
                if (!edtx_ConfirmaEmpaque.getText().toString().equals("")) {
                    SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                    sp.execute();
                }
            }
            if ((id == R.id.borrar_datos)) {
                reiniciarCampos();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void declaraVariables()
    {
        try
        {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Transferencia_Envio_Empaque));
        toolbar.setLogo(R.mipmap.logo_axc);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                Almacen_Transferencia_SurtirEmpaque.this);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Empaque= (EditText) findViewById(R.id.edtx_CodigoEmpaque);
        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Cantidad= (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Lote = (TextView) findViewById(R.id.txtv_FechaCaducidad);

        edtx_ConfirmaEmpaque= (EditText)findViewById(R.id.edtx_ConfirmaEmpaque);

        edtx_ConfirmaEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        btn_CerrarPallet = (Button) findViewById(R.id.btn_CerrarPallet);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }


    }
    private void agregaListeners()
    {
        try {
            edtx_Empaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (!edtx_Empaque.getText().toString().equals("")) {
                            SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                            sp.execute();
                            new esconderTeclado(Almacen_Transferencia_SurtirEmpaque.this);
                        } else {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                        }

                        return true;
                    }
                    return false;

                }

            });
            edtx_ConfirmaEmpaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        if (edtx_Empaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                            new esconderTeclado(Almacen_Transferencia_SurtirEmpaque.this);
                            reiniciarCampos();
                            return false;
                        }

                        if (edtx_ConfirmaEmpaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                            new esconderTeclado(Almacen_Transferencia_SurtirEmpaque.this);
                            reiniciarCampos();
                            return false;
                        }

                            if (!edtx_Empaque.getText().toString().equals(edtx_ConfirmaEmpaque.getText().toString()))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaques_no_coinciden), "false", true, true);
                                new esconderTeclado(Almacen_Transferencia_SurtirEmpaque.this);

                                reiniciarCampos();
                                return false;
                            }


                                SegundoPlano sp = new SegundoPlano("ConfirmaPallet");
                                sp.execute();
                                new esconderTeclado(Almacen_Transferencia_SurtirEmpaque.this);




                        return true;
                    }
                    return false;

                }

            });

            btn_CerrarPallet.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    new CreaDialogos("¿Cerrar miaxc_consulta_pallet?", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            SegundoPlano sp = new SegundoPlano("CerrarPallet");
                            sp.execute();
                        }
                    },null,contexto);
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }

    private void reiniciarCampos()
    {
        try
        {
        edtx_ConfirmaEmpaque.setText("");
        txtv_Producto.setText("");
        txtv_Cantidad.setText("");
        txtv_Lote.setText("");
        edtx_Empaque.setText("");
        edtx_Empaque.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
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
            Recarga = true;
            if(getCurrentFocus()!=null)
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        LastView = getCurrentFocus();
                        progressBarHolder.requestFocus();
                    }
                },1);
            }
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {

                //    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();

                switch (tarea) {

                    case "ConsultaPallet":
                        sa.SOAPConsultaEmpaqueRegular(edtx_Empaque.getText().toString(), contexto);

                        break;

                    case "ConfirmaPallet":
                        sa.SOAPRegistrarEmpaqueTraspasoEnvio(Transferencia, Partida, edtx_ConfirmaEmpaque.getText().toString(), contexto);

                        break;
                    case "CerrarPallet":
                        sa.SOAPCierraPalletTraspasoEnvio(Transferencia, contexto);

                        break;


                }
                decision = sa.getDecision();
                mensaje = sa.getMensaje();
                if (decision.equals("true")) {
                    sacaDatos(tarea);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                decision = "false";
                mensaje=e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(LastView!=null)
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        LastView.requestFocus();
                    }
                },1);
            }
            if (decision.equals("true"))
            {



                switch (tarea)
                {
                    case "ConsultaPallet":
                        txtv_Producto.setText(Producto);
                        txtv_Cantidad.setText(Contenido);
                        txtv_Lote.setText(Lote);
                        edtx_ConfirmaEmpaque.requestFocus();
                        break;
                    case "ConfirmaPallet":
                    //    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.envio_empaque_registro_exito),decision, true, true);
                        reiniciarCampos();
                        edtx_Empaque.requestFocus();
                        break;
                    case "CerrarPallet":
                        new popUpGenerico(contexto, getCurrentFocus(), "Pallet ["+Pallet+"] cerrado con exito.",decision, true, true);
                        reiniciarCampos();
                        edtx_Empaque.requestFocus();
                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, getCurrentFocus(), mensaje,decision, true, true);
                  reiniciarCampos();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            Recarga = false;
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
                            Producto = tabla1.getPrimitivePropertyAsString("NumParte");

                            Contenido= tabla1.getPrimitivePropertyAsString("CantidadActual");
                            Lote = tabla1.getPrimitivePropertyAsString("Lote");


                        }
                        Log.d("SoapResponse", tabla.toString());



                    } catch (Exception e)
                    {

                        e.printStackTrace();
                    }


                    break;
                case "CerrarPallet":


                    try {

                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                        {


                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());
                           Pallet  = tabla1.getPrimitivePropertyAsString("Texto");



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
