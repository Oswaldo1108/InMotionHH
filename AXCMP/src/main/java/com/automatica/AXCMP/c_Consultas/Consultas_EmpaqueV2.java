package com.automatica.AXCMP.c_Consultas;

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
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.adaptadorTabla;
import com.automatica.AXCMP.constructorTablaEntradaAlmacen;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class Consultas_EmpaqueV2 extends AppCompatActivity
{
    //region generados
    Context contexto = this;
    View vista;
    SoapAction sa = new SoapAction();
    ListView tablaPallet;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    EditText edtx_Empaque;
    String pallet;
    constructorTablaEntradaAlmacen cd;
    ArrayList<constructorTablaEntradaAlmacen> lista = null;
    private Handler handlerRequestFocus = new Handler();
    adaptadorTabla adaptador;
    Boolean datosEnTabla = false,recarga;
    Bundle b;

   ArrayList<ConstructorDatoTitulo> ArrayData;


    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.consultas_activity_consulta_empaque);

            Toolbar toolbar = findViewById(R.id.toolbar);
            edtx_Empaque = findViewById(R.id.edtx_CodigoEmpaque);
            edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            tablaPallet = findViewById(R.id.lstv_Pallet);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.Consultas_Pallet));


            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            new cambiaColorStatusBar(contexto, R.color.AzulStd, Consultas_EmpaqueV2.this);

            adaptador = new adaptadorTabla(Consultas_EmpaqueV2.this, R.layout.list_item_2datos, lista);
            edtx_Empaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        try {

                            if (edtx_Empaque.getText().toString().equals("")) {

                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                                edtx_Empaque.requestFocus();
                                return true;
                            }
                            SegundoPlano tarea = new SegundoPlano();
                            tarea.execute();

                            new esconderTeclado(Consultas_EmpaqueV2.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);

                        }
                        return true;
                    }
                    return false;

                }

            });
        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            int id = item.getItemId();

            if (!recarga) {
                if ((id == R.id.InformacionDispositivo)) {
                    new sobreDispositivo(contexto, vista);
                }

                if ((id == R.id.borrar_datos)) {
                    reiniciaCampos();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }
        return super.onOptionsItemSelected(item);
    }

    public void reiniciaCampos()
    {
        try {
            if (adaptador != null) {
                adaptador.clear();
            }
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


        String mensaje, estado;

        View LastView;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            recarga=true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);

            progressBarHolder.setVisibility(View.VISIBLE);
            LastView = getCurrentFocus();
            progressBarHolder.requestFocus();

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {

                sa.SOAPConsultaPalletReg(edtx_Empaque.getText().toString(), contexto);

                mensaje = sa.getMensaje();
                estado = sa.getDecision();

                    if (estado.equals("true"))
                    {
                        sacaDatos();
                    }

            } catch (Exception e) {
                mensaje = e.getMessage();
                estado = "false";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                recarga=false;
                LastView.requestFocus();
                if (estado.equals("true"))
                {
                    tablaPallet = (ListView) findViewById(R.id.lstv_Pallet);
                    adaptador = new adaptadorTabla(Consultas_EmpaqueV2.this, R.layout.list_item_2datos, lista);
                    tablaPallet.setAdapter(adaptador);

                } else if (estado.equals("false"))
                {

                   // reiniciaCampos();
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, "false", true, true);
                }

                edtx_Empaque.setText("");
                edtx_Empaque.requestFocus();
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }

    private void sacaDatos()
    {
        try
        {

            lista = new ArrayList<>();
            PropertyInfo pi;
            ArrayData = new ArrayList<>();
            SoapObject tabla = sa.parser();
            if(tabla!=null)
            {
                for (int i = 0; i < tabla.getPropertyCount(); i++)
                {
                    SoapObject tabla11 = (SoapObject) tabla.getProperty(i);

                    for(int t=0;t<tabla11.getPropertyCount();t++)
                    {
                        pi = new PropertyInfo();
                        tabla11.getPropertyInfo(t,pi);
                        String name = pi.name;
                        if(name.equals("FechaCrea"))
                        {
                            name = "Fecha Creación";
                        }
                        if(name.equals("FechaCad"))
                        {
                            name = "Fecha Caducidad";
                        }
                        if(name.equals("LoteProveedor"))
                        {
                            name = "Lote Proveedor";
                        }
                        if(name.equals("CantidadOriginal"))
                        {
                            name = "Cantidad Original";
                        }
                        if(name.equals("CantidadActual"))
                        {
                            name = "Cantidad Actual";
                        }
                        if(name.equals("CantidadOriginal"))
                        {
                            name = "CantidadOriginal";
                        }
                        if(name.equals("Estacion"))
                        {
                            name = "Estación";
                        }
                        if(name.equals("Revision"))
                        {
                            name = "Revisión";
                        }
                        cd = new constructorTablaEntradaAlmacen(name,tabla11.getPropertyAsString(t));
                        lista.add(cd);
                    }
                    // UbicacionSugerida = tabla11.getPrimitivePropertyAsString("UbicacionSugerida");
                    Log.d("SoapResponse", tabla11.toString());
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}