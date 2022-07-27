package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

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

public class Almacen_Transferencia_Surtir_Pallet extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    Handler h = new Handler();
    View vista;
    Context contexto = this;

    EditText edtx_Pallet,edtx_ConfirmaPallet;
    TextView txtv_Producto,txtv_Empaques,txtv_Contenido,txtv_Lote,txtv_Estatus;
    String Producto,Empaques,Contenido,Lote,Estatus;
    String Transferencia,Partida;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_transferencia_surtir_pallet_v2);
        try
        {
            declaraVariables();
            agregaListeners();
            Bundle b = getIntent().getExtras();
            Transferencia = b.getString("Transferencia");
            Partida =  b.getString("Partida");
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),false,true,true);
        }

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
    {try
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
    }catch (Exception e)
    {
        new popUpGenerico(contexto,vista,e.getMessage(),false,true,true);
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
          //  toolbar.setLogo(R.mipmap.logo_axc);//      toolbar.setLogo(R.drawable.axc_logo_toolbar);

            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,
                    Almacen_Transferencia_Surtir_Pallet.this);
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


            edtx_Pallet= (EditText)findViewById(R.id.edtx_CodigoPallet);
            edtx_Pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);
            txtv_Contenido = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_Lote = (TextView)findViewById(R.id.txtv_FechaCaducidad);
            txtv_Estatus = (TextView) findViewById(R.id.txtv_Estatus);
            edtx_ConfirmaPallet = (EditText)findViewById(R.id.edtx_ConfirmaPallet);
            edtx_ConfirmaPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

      /*      edtx_Producto.setEnabled(false);
            edtx_Empaques.setEnabled(false);
            edtx_Contenido.setEnabled(false);
            edtx_Lote.setEnabled(false);
            edtx_Estatus.setEnabled(false);
*/
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),false,true,true);
        }

    }
    private void agregaListeners()
    {
        try
        {
            edtx_Pallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_Pallet.getText().toString().equals(""))
                        {
                            SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                            sp.execute();
                            new esconderTeclado(Almacen_Transferencia_Surtir_Pallet.this);
                        }
                        else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet),false, true, true);
                            new esconderTeclado(Almacen_Transferencia_Surtir_Pallet.this);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    reiniciarCampos();
                                    edtx_Pallet.requestFocus();
                                }
                            });
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
                        if(!edtx_Pallet.getText().toString().equals(""))
                        {
                            if (edtx_Pallet.getText().toString().equals(edtx_ConfirmaPallet.getText().toString()))
                            {
                                SegundoPlano sp = new SegundoPlano("ConfirmaPallet");
                                sp.execute();
                                new esconderTeclado(Almacen_Transferencia_Surtir_Pallet.this);
                            }
                            else
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallets_no_coinciden),false, true, true);
                                new esconderTeclado(Almacen_Transferencia_Surtir_Pallet.this);
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        reiniciarCampos();
                                        edtx_Pallet.requestFocus();
                                    }
                                });
                            }
                        }
                        else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet),false, true, true);
                            new esconderTeclado(Almacen_Transferencia_Surtir_Pallet.this);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    reiniciarCampos();
                                    edtx_Pallet.requestFocus();
                                }
                            });
                        }

                        return true;
                    }
                    return false;

                }

            });

        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }


    }

    private void reiniciarCampos()
    {
        try
        {
            edtx_ConfirmaPallet.setText("");
            txtv_Producto.setText("");
            txtv_Contenido.setText("");
            txtv_Empaques.setText("");
            txtv_Estatus.setText("");
            txtv_Lote.setText("");

            edtx_Pallet.setText("");
            edtx_Pallet.requestFocus();
        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
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

                    case "ConsultaPallet":

                        sa.SOAPConsultaPallet(edtx_Pallet.getText().toString(),contexto);
                        decision =   sa.getDecision();
                        mensaje = sa.getMensaje();
                        break;

                    case "ConfirmaPallet" :
                        sa.SOAPRegistrarPalletTraspasoEnvio(Transferencia,Partida,edtx_ConfirmaPallet.getText().toString(),contexto);
                        decision =   sa.getDecision();
                        mensaje = sa.getMensaje();

                        break;


                }

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
            try
            {
            if (decision.equals("true"))
            {
                switch (tarea)
                {


                    case "ConsultaPallet":

                        txtv_Producto.setText(Producto);
                        txtv_Empaques.setText(Empaques);
                        txtv_Contenido.setText(Contenido);
                        txtv_Lote.setText(Lote);
                        txtv_Estatus.setText(Estatus);
                        edtx_ConfirmaPallet.requestFocus();
                        break;
                    case "ConfirmaPallet":
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.envio_pallet_registro_exito),decision, true, true);
                        reiniciarCampos();
                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, getCurrentFocus(), mensaje,decision, true, true);
                reiniciarCampos();
                edtx_Pallet.requestFocus();
            }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(),"false", true, true);
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

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                    break;

                case "ConsultaPallet":


                        try {

                            Log.d("SoapResponse", tabla.toString());


                            Producto = tabla.getPrimitivePropertyAsString("NumParte");
                            Empaques = tabla.getPrimitivePropertyAsString("Empaques");
                            Contenido= tabla.getPrimitivePropertyAsString("CantidadActual");
                            Lote = tabla.getPrimitivePropertyAsString("LoteProveedor");
                            Estatus = tabla.getPrimitivePropertyAsString("DescStatus");
                        } catch (Exception e)
                        {

                            e.printStackTrace();
                        }


                    break;
            }

        }
    }

}
