package com.automatica.AXCPT.c_Almacen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Vibrator;
import android.text.InputFilter;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import org.ksoap2.serialization.SoapObject;

public class Almacen_Colocar extends AppCompatActivity
{
    //region variables
    Toolbar toolbar;
    EditText edtx_CodigoPallet,edtx_CodigoUbicacion;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_Producto,txtv_DescProd, txtv_Empaques,txtv_Cantidad,txtv_Caducidad,txtv_Estatus,txtv_UbicacionSugerida;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;

    boolean recargar;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_colocar);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Colocar.this);
        declararVariables();
        agregaListeners();
    }

    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Colocar));
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        edtx_CodigoUbicacion = (EditText) findViewById(R.id.edtx_Ubicacion);
        edtx_CodigoUbicacion .setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        txtv_Producto= (TextView) findViewById(R.id.txtv_Producto);
        txtv_DescProd = (TextView) findViewById(R.id.txtv_DescProd);
        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);

        txtv_UbicacionSugerida = (TextView) findViewById(R.id.txtv_UbicacionSugerida);
        txtv_Caducidad = (TextView) findViewById(R.id.txtv_FechaCaducidad);
        txtv_Estatus = (TextView) findViewById(R.id.txtv_Estatus);


        //edtx_CodigoPallet.setTextIsSelectable(false);


        edtx_CodigoPallet.setCustomSelectionActionModeCallback(new ActionMode.Callback2() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        edtx_CodigoPallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Vibrator vibratorService = (Vibrator) Almacen_Colocar.this.getSystemService(VIBRATOR_SERVICE);
                vibratorService.vibrate(150);
                String[] armar = {edtx_CodigoPallet.getText().toString()};
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.constraintLayout7, frgmnt_Seleccion_Producto.newInstance(armar, "", new frgmnt_Seleccion_Producto.OnFragmentInteractionListener() {
                            @Override
                            public boolean ActivaProgressBar(Boolean estado) {
                                return false;
                            }

                            @Override
                            public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {
                                edtx_CodigoPallet.setText(prmProducto);
                                new SegundoPlano("").execute();
                            }
                        }), "ElegirProducto").commit();
                return true;

            }
        });
        edtx_CodigoPallet.requestFocus();

    }
    private void agregaListeners()
    {
        edtx_CodigoPallet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!edtx_CodigoPallet.getText().toString().equals(""))
                {
                    SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                    sp.execute();


                }else
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            edtx_CodigoPallet.requestFocus();
                            edtx_CodigoPallet.setText("");
                        }
                    });
                    new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),"false",true,true);
                }
                new esconderTeclado(Almacen_Colocar.this);
                return false;
            }
        });


        edtx_CodigoUbicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoUbicacion.getText().toString().equals(""))
                    {

                        if(edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_pallet),"false",true,true);
                            return false;
                        }

                        SegundoPlano sp = new SegundoPlano("ColocacionPallet");
                        sp.execute();

                    }else
                    {

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoUbicacion.requestFocus();
                                edtx_CodigoUbicacion.setText("");
                            }
                        });
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_ubicacion),"false",true,true);
                    }
                    new esconderTeclado(Almacen_Colocar.this);
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
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void ReiniciarVariables()
    {
        edtx_CodigoPallet.setText("");
        edtx_CodigoUbicacion.setText("");

        txtv_Empaques.setText("");
        txtv_Producto.setText("");
        txtv_DescProd.setText("");
        txtv_Cantidad.setText("");
        txtv_Estatus.setText("");
        txtv_Caducidad.setText("");
        txtv_UbicacionSugerida.setText("");
        edtx_CodigoPallet.requestFocus();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(recargar = true)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                ReiniciarVariables();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Colocar.this);
        cAccesoADatos_Consultas caCons = new cAccesoADatos_Consultas(Almacen_Colocar.this);

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

            recargar = false;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
                {


                switch (tarea) {
                    case "ConsultaPallet":
                        //sa.SOAPConsultaPallet(edtx_CodigoPallet.getText().toString(), contexto);
                        dao = caCons.c_ConsultarPalletPT(edtx_CodigoPallet.getText().toString());
                        break;
                    case "ConsultaUbicacionSugerida":

                        //sa.SOAPSugierePosicion(edtx_CodigoPallet.getText().toString(), contexto);
                        dao = ca.c_SugierePosicion(edtx_CodigoPallet.getText().toString());

                        break;
                    case "ColocacionPallet":

                        //sa.SOAPcolocaPallet(edtx_CodigoPallet.getText().toString(), edtx_CodigoUbicacion.getText().toString(), contexto);

                        dao= ca.c_colocaPallet(edtx_CodigoPallet.getText().toString(),edtx_CodigoUbicacion.getText().toString(),txtv_UbicacionSugerida.getText().toString());
                        break;


                }
            }catch (Exception e)
            {
                e.printStackTrace();
                dao = new DataAccessObject(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (dao.iscEstado())
                {
                    switch (tarea)
                        {
                        case "ConsultaPallet":
                            txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            txtv_Empaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
//                            txtv_DescProd.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                            txtv_DescProd.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                            txtv_Caducidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                            txtv_Estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));

                            new SegundoPlano("ConsultaUbicacionSugerida").execute();
                            break;
                        case "ConsultaUbicacionSugerida":
                            txtv_UbicacionSugerida.setText(dao.getcMensaje());
                            break;

                        case "ColocacionPallet":

                            ReiniciarVariables();
                            new popUpGenerico(contexto, edtx_CodigoPallet, getString(R.string.pallet_colocado), dao.iscEstado(), true, true);
                            break;

                    }

                }else
                {
                    ReiniciarVariables();
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), String.valueOf(dao.iscEstado()), true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), String.valueOf(dao.iscEstado()), true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = true;
        }
    }
}
