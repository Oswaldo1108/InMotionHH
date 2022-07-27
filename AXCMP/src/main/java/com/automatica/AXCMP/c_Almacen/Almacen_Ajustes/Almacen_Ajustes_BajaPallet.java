package com.automatica.AXCMP.c_Almacen.Almacen_Ajustes;

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

public class Almacen_Ajustes_BajaPallet extends AppCompatActivity
{


    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_CodigoPallet,edtx_CodigoPalletConfirmacion;

    TextView txtv_Empaques,txtv_Producto,txtv_Cantidad,txtv_Lote;


    Spinner spnr_Ajuste;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    View vista;
    Context contexto = this;


    String IdAjuste,Producto,Cantidad,CantReg,Lote,Empaques,Estatus,FechaCrea;
    String Descripcion;
    String CantidadAConsultar;
    String[] TipoAjuste; //= {"ALTA POR INVENTARIO","ALTA POR DEVOLUCIÓN","PRUEBA"};
    String CodigoPallet, PalletRegistrados;
    Handler h = new Handler();
    ArrayAdapter<String> spinnerArrayAdapter;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_ajustes__baja_pallet);
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_BajaPallet.this);
        declaraVariables();
        agregaListeners();
        SegundoPlano sp = new SegundoPlano("ListarAjustes");
        sp.execute();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
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
        try {

            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {
                reiniciaCampos();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" " + getString(R.string.Ajustes_Baja_Pallet));
            toolbar.setLogo(R.mipmap.logo_axc);
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
            edtx_CodigoPalletConfirmacion = (EditText) findViewById(R.id.edtx_ConfirmacionPallet);
            edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoPalletConfirmacion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Lote = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Cantidad = (TextView) findViewById(R.id.txtv_CantPend);
            txtv_Empaques = (TextView) findViewById(R.id.txtv_Caducidad);


            spnr_Ajuste = (Spinner) findViewById(R.id.spnr_Ajuste);


            // Spinner spinner = new Spinner(this);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }

    }
    private void agregaListeners()
    {
        try
        {
        edtx_CodigoPalletConfirmacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPalletConfirmacion.getText().toString().equals(""))
                    {
                        if(edtx_CodigoPalletConfirmacion.getText().toString().equals(edtx_CodigoPallet.getText().toString()))
                        {
                            SegundoPlano sp = new SegundoPlano("AjusteBajaPallet");
                            sp.execute();

                        }else
                        {
                            new popUpGenerico(contexto,vista,"Los códigos de miaxc_consulta_pallet no coinciden.","Advertencia",true,true);
                           h.post(new Runnable() {
                               @Override
                               public void run() {
                                   edtx_CodigoPalletConfirmacion.setText("");
                                   edtx_CodigoPalletConfirmacion.requestFocus();
                               }
                           });

                        }
                    }else
                    {
                        new popUpGenerico(contexto,vista,"Debes capturar un código de miaxc_consulta_pallet.","Advertencia",true,true);
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoPalletConfirmacion.setText("");
                                edtx_CodigoPalletConfirmacion.requestFocus();    }
                        });

                    }

                    new esconderTeclado(Almacen_Ajustes_BajaPallet.this);
                }
                return false;
            }
        });



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

                    }else
                    {
                        new popUpGenerico(contexto,vista,"Debes capturar una cantidad valida","Advertencia",true,true);
                        edtx_CodigoPallet.setText("");
                        edtx_CodigoPallet.requestFocus();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoPallet.setText("");
                                edtx_CodigoPallet.requestFocus();    }
                        });
                    }

                    new esconderTeclado(Almacen_Ajustes_BajaPallet.this);
                }
                return false;
            }
        });


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }
    }
    private void reiniciaCampos()
    {
        try
        {
        edtx_CodigoPallet.setText("");
        edtx_CodigoPalletConfirmacion.setText("");
        edtx_CodigoPallet.requestFocus();
        txtv_Cantidad.setText("");
        txtv_Empaques.setText("");
        txtv_Lote.setText("");
        txtv_Producto.setText("");
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

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
            try {
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
        try{
            String CodigoPallet = edtx_CodigoPallet.getText().toString();

            switch (tarea)
            {

                case "ConsultaPallet":
                    sa.SOAPConsultaPallet(CodigoPallet,contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();
                    break;

                case "ListarAjustes"://Se usa para llenar el Spinner
                    sa.SOAPListarConceptosAjuste("2",contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();

                    break;
                case "AjusteBajaPallet" :
                    String TipoEvento = spnr_Ajuste.getSelectedItem().toString();
                    sa.SOAPAjusteBajaPallet(CodigoPallet,TipoEvento,contexto);
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
            e.printStackTrace();
            decision = "false";
            mensaje = e.getMessage();
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
                            txtv_Lote.setText(Lote);
                            txtv_Cantidad.setText(Cantidad);
                            txtv_Empaques.setText(Empaques);

                            break;
                        case "ListarAjustes":
                            spinnerArrayAdapter = new ArrayAdapter<String>
                                    (Almacen_Ajustes_BajaPallet.this, android.R.layout.simple_spinner_item,
                                            TipoAjuste); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            spnr_Ajuste.setAdapter(spinnerArrayAdapter);
                            break;
                        case "AjusteBajaPallet":
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_baja), decision, true, true);
                            reiniciaCampos();
                            break;
                    }
                }

                if (decision.equals("false")) {
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                    reiniciaCampos();
                }
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);

                    }catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), decision, true, true);

                        }
            }
    }
    public void sacaDatos(String Tarea)
    {
        try
        {
        SoapObject tabla = sa.parser();
        if (tabla == null) tabla = sa.getTablaSoap();

        if (Tarea.equals("ListarAjustes")) TipoAjuste = new String[tabla.getPropertyCount()];


        if (tabla != null)


            switch (Tarea)
            {
                case "ListarAjustes":
                    for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        try
                        {


                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());
                            TipoAjuste[i] = tabla1.getPrimitivePropertyAsString("Descripcion");
                        } catch (Exception e)
                        {

                            e.printStackTrace();
                        }
                    }
                    break;

                case "ConsultaPallet":
                    Producto = tabla.getPrimitivePropertyAsString("NumParte");
                    Lote = tabla.getPrimitivePropertyAsString("LoteProveedor");
                    Cantidad = tabla.getPrimitivePropertyAsString("CantidadActual");
                    Empaques = tabla.getPrimitivePropertyAsString("Empaques");

                    break;
            }


            }catch (Exception e)
                {
                    e.printStackTrace();

                }
    }
}
