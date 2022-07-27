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

import java.util.ArrayList;

public class consultas_ConsultaPallet extends AppCompatActivity
{
        //region generados
    Context contexto = this;
    View vista;
    SoapAction sa = new SoapAction();
    ListView tablaPallet;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    EditText edtx_pallet;
    String pallet;
    ArrayList<constructorTablaEntradaAlmacen> lista = null; //las variables tomadas del arraylist que las recupera de accion soap, son puestas en esta lista para ser impresas en la tabla
    private Handler handlerRequestFocus = new Handler();
    adaptadorTabla adaptador;
    Boolean datosEnTabla = false,recarga;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_consulta_pallet);

        Toolbar toolbar = findViewById(R.id.toolbar);
        edtx_pallet = findViewById(R.id.edtx_CodigoPallet);
        edtx_pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        tablaPallet = findViewById(R.id.lstv_Pallet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Consultas_Pallet));


        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new cambiaColorStatusBar(contexto, R.color.AzulStd, consultas_ConsultaPallet.this);

        adaptador = new adaptadorTabla(consultas_ConsultaPallet.this, R.layout.list_item_2datos, lista);
        edtx_pallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {


                    SegundoPlanoPallet tarea = new SegundoPlanoPallet();
                    tarea.execute();

                    new esconderTeclado(consultas_ConsultaPallet.this);
                    return true;
                }
                return false;

            }

        });

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
        int id = item.getItemId();

        if(!recarga)
        {
            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }

            if ((id == R.id.borrar_datos)) {
                reiniciaCampos();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void reiniciaCampos()
    {
        if(datosEnTabla)adaptador.clear();
        edtx_pallet.setText("");
        datosEnTabla = false;
    }

    private class SegundoPlanoPallet extends AsyncTask<Void,Void,Void>
    {


        String mensaje, estado, tabla;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            recarga=true;
            lista = null;
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                pallet = edtx_pallet.getText().toString();
                sa.SOAPConsultaPallet(pallet, contexto);

                if (pallet.equals("")) {
                    estado = "false";
                    mensaje = "Debe capturar un codigo de miaxc_consulta_pallet";
                } else {
                    mensaje = sa.getMensaje();
                    estado = sa.getDecision();


                    if (estado.equals("true")) {
                        lista = new ArrayList<>();


                        if (sa.getArrayListCaso3().get(3).equals("anyType{}")) {
                            sa.getArrayListCaso3().set(3, "Sin Orden");
                        }

                        constructorTablaEntradaAlmacen IdRecibo = new constructorTablaEntradaAlmacen("Orden de producción", sa.getArrayListCaso3().get(3));
                        constructorTablaEntradaAlmacen NumParte = new constructorTablaEntradaAlmacen("Producto", sa.getArrayListCaso3().get(4));
                        constructorTablaEntradaAlmacen LoteProveedor = new constructorTablaEntradaAlmacen("Lote", sa.getArrayListCaso3().get(5));
                        constructorTablaEntradaAlmacen LoteAXC = new constructorTablaEntradaAlmacen("Lote AXC", sa.getArrayListCaso3().get(6));
                        constructorTablaEntradaAlmacen CantidadOriginal = new constructorTablaEntradaAlmacen("Cantidad Original", sa.getArrayListCaso3().get(7));
                        constructorTablaEntradaAlmacen CantidadActual = new constructorTablaEntradaAlmacen("Cantidad Actual", sa.getArrayListCaso3().get(8));
                        constructorTablaEntradaAlmacen Empaques = new constructorTablaEntradaAlmacen("Empaques", sa.getArrayListCaso3().get(9));
                        constructorTablaEntradaAlmacen DescStatus = new constructorTablaEntradaAlmacen("Estatus", sa.getArrayListCaso3().get(12));
                        constructorTablaEntradaAlmacen Ubicacion = new constructorTablaEntradaAlmacen("Ubicación", sa.getArrayListCaso3().get(13));
                        constructorTablaEntradaAlmacen Fecha = new constructorTablaEntradaAlmacen("Fecha", sa.getArrayListCaso3().get(15));


                        lista.add(IdRecibo);
                        lista.add(NumParte);
                        lista.add(LoteProveedor);

                        lista.add(CantidadOriginal);
                        lista.add(CantidadActual);
                        lista.add(Empaques);
                        lista.add(DescStatus);
                        lista.add(Ubicacion);
                        lista.add(Fecha);

                        datosEnTabla = true;
                    } else if (estado.equals("false")) {
                        mensaje = sa.getMensaje();

                    }
                    Log.i("SoapResponse", " coloca palet " + mensaje + " " + estado + " ");

                }
            }catch (Exception e)
            {
                mensaje = e.getMessage();
                estado = "false";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {

                recarga = false;
                outAnimation = new AlphaAnimation(1f, 0f);
                if (estado.equals("true")) {
                    tablaPallet = (ListView) findViewById(R.id.lstv_Pallet);
                    adaptador = new adaptadorTabla(consultas_ConsultaPallet.this, R.layout.list_item_2datos, lista);
                    tablaPallet.setAdapter(adaptador);

                } else if (estado.equals("false"))
                {

                    edtx_pallet.setText("");
                    edtx_pallet.requestFocus();
                    reiniciaCampos();

                    new popUpGenerico(contexto, vista, mensaje, "Advertencia", true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, vista, e.getMessage(), "Advertencia", true, true);
            }
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}

