package com.automatica.AXCPT.c_Recepcion.Base;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.automatica.AXCPT.R;

import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;


public class Recepcion_Registro_PalletSinEtiqueta extends AppCompatActivity
{

    //region variables
    String TAG = "SoapResponse";

    EditText  edtx_Cantidad,edtx_EmpxPallet;
    String  Cantidad,CodigoEmpaque;
    Button btnCerrarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    TextView txtv_EmpaquesRegistrados, txtv_Caducidad,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet,txtv_CantidadLote,txtv_CantRegLote;
    Bundle b;

    View vista;
    Context contexto = this;
    String OrdenCompra, FechaCaducidad,ModificaCant,PartidaERP,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    Spinner spnr_Lotes;
    TextView txtv_OrdenCompra;
    boolean verificarCierre,recargar;
    String TipoRecepcion;
    Handler h = new Handler();
    int registroAnteriorSpinner=0;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_recepcion__registro__pallet_sin_etiqueta);
                new cambiaColorStatusBar(contexto, R.color.VerdeStd, Recepcion_Registro_PalletSinEtiqueta.this);
                SacaExtrasIntent();
                declararVariables();
                AgregaListeners();
                new SegundoPlano("ConsultaPallet").execute();
                new SegundoPlano("ListarOrdenesCompra").execute();


            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista,e.getMessage()+" "+e.getCause() ,"false" ,true,true);
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
        int id = item.getItemId();

        if(!recargar)
            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, vista);
                    }
                if ((id == R.id.borrar_datos))
                    {
                        reiniciaVariables();
                    }
            }
        return super.onOptionsItemSelected(item);
    }
    private void declararVariables()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.recepcion_por_empaque));
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            //  edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Factura);
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            txtv_CantidadLote = (TextView) findViewById(R.id.txtv_CantidadLote);

            btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

            txtv_Caducidad = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Partida = (TextView) findViewById(R.id.txtv_Partida);
            txtv_UM = (TextView) findViewById(R.id.txtv_Caducidad);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_CantRegLote = (TextView) findViewById(R.id.txtv_RegLote);



            edtx_Cantidad.setText(CantidadEmpaques);
            edtx_EmpxPallet.setText(EmpaquesPallet);

            btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);


            txtv_Caducidad.setText(FechaCaducidad);
            txtv_Partida.setText(PartidaERP);
            txtv_UM.setText(UM);
            txtv_Producto.setText(NumParte);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_OrdenCompra.setText(OrdenCompra);

            spnr_Lotes = (Spinner) findViewById(R.id.spnr_OrdenesCompra);


        }catch(Exception e)
            {
                e.printStackTrace();

            }
    }
    private void AgregaListeners()
    {
        edtx_Cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {


                if(hasFocus&&edtx_Cantidad.getText().toString().equals("0"))
                    {
                        try
                            {
                                edtx_Cantidad.setText("");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }
                    }

            }
        });
        edtx_EmpxPallet.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus&&edtx_EmpxPallet.getText().toString().equals("0"))
                    {
                        try
                            {
                                edtx_EmpxPallet.setText("");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }
                    }
            }
        });


        edtx_Cantidad.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                            {
                                if (edtx_Cantidad.getText().toString().equals(""))
                                    {

                                        h.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {  edtx_Cantidad.setText("");
                                                edtx_Cantidad.requestFocus();

                                            }
                                        });
                                        new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);

                                    }
                                        try {
                                            if (!(Float.parseFloat(edtx_Cantidad.getText().toString()) > 999999)) {
                                                edtx_EmpxPallet.requestFocus();
                                            } else {
                                                h.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        h.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                edtx_Cantidad.requestFocus();
                                                                edtx_Cantidad.setText("");
                                                            }
                                                        });
                                                    }
                                                });
                                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);

                                            }
                                        }catch (NumberFormatException ex)
                                            {
                                                h.post(new Runnable()
                                                {
                                                    @Override
                                                    public void run()
                                                    {  edtx_Cantidad.setText("");
                                                        edtx_Cantidad.requestFocus();

                                                    }
                                                });
                                                new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_cantidad_valida),"false",true,true);
                                            }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }

                    }
                return false;
            }
        });

        edtx_EmpxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                            {
                                if(edtx_EmpxPallet.getText().toString().equals(""))
                                    {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_EmpxPallet.setText("");
                                                edtx_EmpxPallet.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto,getCurrentFocus(),"Ingrese candidad de empaques" , "false", true, true);
                                        return false;
                                    }




                                if(Integer.parseInt(edtx_EmpxPallet.getText().toString()) <=0)
                                    {
                                        new popUpGenerico(contexto,getCurrentFocus(),"La cantidad ingresada no puede ser menor o igual a cero." , false, true, true);
                                        return false;
                                    }
                                if(Float.parseFloat(edtx_Cantidad.getText().toString()) <=0)
                                    {
                                        new popUpGenerico(contexto,getCurrentFocus(),"La cantidad de empaques no puede ser menor o igual a cero." , false, true, true);
                                        return false;
                                    }


                                if(edtx_Cantidad.getText().toString().equals(""))
                                    {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_EmpxPallet.setText("");
                                                edtx_EmpxPallet.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_cantidad)  , "false", true, true);
                                        return false;
                                    }
                                if (edtx_EmpxPallet.getText().toString().equals(""))
                                    {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_EmpxPallet.setText("");
                                                edtx_EmpxPallet.requestFocus();
                                            }
                                        });

                                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_cantidad) , false, true, true);
                                        return false;
                                    }


                                         new SegundoPlano("RegistrarEmpaqueNuevo").execute();
                                        btnCerrarTarima.setEnabled(true);

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }
                    }
                return false;
            }
        });

        btnCerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {
                        if(!txtv_EmpaquesRegistrados.getText().toString().equals(""))
                            {
                                if (Integer.parseInt(txtv_EmpaquesRegistrados.getText().toString())>0)
                                    {
                                        new SegundoPlano("RegistraPalletNuevo").execute();
                                    }
                                else
                                    {
                                        new popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                                    }
                            }else
                            {
                                new popUpGenerico(contexto, vista, getString(R.string.error_pallet_no_seleccionado), "false", true, true);
                            }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
            }
        });

        spnr_Lotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try {
                    txtv_CantidadLote.setText( ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getTag1());
                    txtv_CantRegLote.setText( ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getTag2());
                    txtv_Caducidad.setText( ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getTag3());
                }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                try {
                    txtv_Caducidad.setText("-");
                    txtv_CantRegLote.setText("-");
                }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                    }
            }
        });
    }
    private void SacaExtrasIntent()
    {
        try
            {
                b = getIntent().getExtras();
                OrdenCompra= b.getString("Orden");
                PartidaERP= b.getString("PartidaERP");
                NumParte= b.getString("NumParte");
                UM= b.getString("UM");
                CantidadTotal= b.getString("CantidadTotal");
                CantidadRecibida= b.getString("CantidadRecibida");
                CantidadEmpaques= b.getString("CantidadEmpaques");
                EmpaquesPallet= b.getString("EmpaquesPallet");

            }catch (Exception e)
            {
                Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
            }

    }
    private void reiniciaVariables()
    {
        //txtv_EmpaquesRegistrados.setText("");
        try
            {
                edtx_EmpxPallet .setText("");
                edtx_EmpxPallet.requestFocus();
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(Recepcion_Registro_PalletSinEtiqueta.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        View LastView;
        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

            try
                {
                    h.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            LastView = getCurrentFocus();
                            progressBarHolder.requestFocus();
                            recargar = true;
                        }
                    },10);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
//            LastView = getCurrentFocus();
//            progressBarHolder.requestFocus();
//            recargar=true;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                switch (tarea)
                    {
                        case "ListarOrdenesCompra":

                            dao = ca.c_ListarLotesOC(OrdenCompra, PartidaERP, "");
                            break;
                        case "ConsultaPallet":

                            dao= ca.c_ConsultaPalletAbiertoOC(OrdenCompra, PartidaERP);
                            break;
                        case "RegistrarEmpaqueNuevo":

                            CodigoEmpaque = edtx_EmpxPallet.getText().toString();
                            Cantidad = edtx_Cantidad.getText().toString();

                    /*        dao = ca.c_RegistrarPalletCompra_NE(OrdenCompra,
                                    PartidaERP,
                                    ((Constructor_Dato)spnr_Lotes.getSelectedItem()).getDato(),
                                    edtx_Cantidad.getText().toString(),
                                    edtx_EmpxPallet.getText().toString()
                                    );*/

                            break;
                        case "RegistraPalletNuevo":

                            dao = ca.c_CierraPalletCompra(txtv_Pallet.getText().toString());
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
            try
                {
                    if(LastView!=null)
                        {
                            LastView.requestFocus();
                        }
                    if(dao.iscEstado())
                        {

                            switch (tarea)
                                {
                                    case "RegistrarEmpaqueNuevo":
                                        txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                                        txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                                        edtx_EmpxPallet.setText("");
                                        edtx_EmpxPallet.requestFocus();
                                        CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                                        txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                                        new esconderTeclado(Recepcion_Registro_PalletSinEtiqueta.this);
                                        if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                                            {
                                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_completada), String.valueOf(dao.iscEstado()), true, true);
                                                new SegundoPlano("RegistraPalletNuevo").execute();
                                                //  reiniciaVariables();
                                                return;
                                            }
                                        else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                                            {
                                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);

                                                new SegundoPlano("RegistraPalletNuevo").execute();

                                                return;
                                            }
                                        else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                                            {

                                                Log.d(TAG, "onPostExecute() Pallet cerrado " + dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado"));
                                                new SegundoPlano("RegistraPalletNuevo").execute();

                                            }
                                        new SegundoPlano("ListarOrdenesCompra").execute();

                                        break;
                                    case "ConsultaPallet":

                                        Log.i("DAO", dao.getSoapObject_parced().toString());
                                        txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_EmpaquesRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));
                                        break;
                                    case "RegistraPalletNuevo":
                                        new popUpGenerico(contexto, LastView,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",String.valueOf(dao.iscEstado()), true, true);
                                        new SegundoPlano("ListarOrdenesCompra").execute();
                                        new SegundoPlano("ConsultaPallet").execute();
                                        break;

                                    case "ListarOrdenesCompra":


                                        if(dao.getcTablas() != null)
                                            {
                                                registroAnteriorSpinner = spnr_Lotes.getSelectedItemPosition();
                                                spnr_Lotes.setAdapter(new CustomArrayAdapter(Recepcion_Registro_PalletSinEtiqueta.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("LoteAXC","CantidadRecibida","CantidadRegistrada","FechaCaducidad")));
                                                spnr_Lotes.setSelection(registroAnteriorSpinner);
                                            }else
                                            {
                                                spnr_Lotes.setAdapter(null);
                                            }

                                        break;
                                }
                        }
                    else
                        {
                            reiniciaVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                        }
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                }
            recargar = false;
        }
    }
    @Override
    public void onBackPressed()
    {
        Recepcion_Registro_PalletSinEtiqueta.this.finish();
        super.onBackPressed();
    }
}
