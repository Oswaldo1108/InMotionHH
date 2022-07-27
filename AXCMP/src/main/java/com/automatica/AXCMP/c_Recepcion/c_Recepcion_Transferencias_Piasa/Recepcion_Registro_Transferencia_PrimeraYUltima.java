package com.automatica.AXCMP.c_Recepcion.c_Recepcion_Transferencias_Piasa;

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

import java.util.ArrayList;

public class Recepcion_Registro_Transferencia_PrimeraYUltima  extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Handler handler = new Handler();
    EditText edtx_EmpxPallet, edtx_Cantidad, edtx_PrimerEmpaque,edtx_UltimoEmpaque,edtx_CantidadEmpaques;
    String Pallet,Producto, Cantidad,Lote,CodigoEmpaque,EmpaquesRegistrados;

    String TAG = "SoapResponse";
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_Traspaso,txtv_Lote,txtv_EmpaquesRegistrados, txtv_OrdenCompra,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet,txtv_CantidadLote,txtv_CantRegLote;
    Bundle b;
    View vista;
    Context contexto = this;
    String OrdenCompra,Factura,ModificaCant,PartidaERP,IdRecepcion,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    String Estado,Texto,CodigoPallet,PartidaCerrada,OrdenCerrada,PalletCerrado,FechaProd;
    String FechaCaducidad,LoteTrans,Transferencia;
    // Spinner spnr_OrdenesCompra;

    TextView txtv_Caducidad;
    ArrayList<String> ArrayLotes= new ArrayList<>();
    ArrayList<String> ArrayCaducidades = new ArrayList<>();
    ArrayList<Lote> ListaLotes = new ArrayList<>();
    int registroAnteriorSpinner=0;
    boolean recarga;
    //endregion
    private class Lote
    {   String LoteAXC;
        String LoteCantidadRegistrada, FechaCaducidad,CantidadLote;
        public Lote(String LoteAXC,String LoteCantidadRegistrada, String FechaCaducidad, String CantidadLote)
        {
            this.LoteAXC = LoteAXC;
            this.CantidadLote = CantidadLote;
            this.FechaCaducidad = FechaCaducidad;
            this.LoteCantidadRegistrada = LoteCantidadRegistrada;
        }


        public String getLoteAXC() {
            return LoteAXC;
        }


        public String getLoteCantidadRegistrada() {
            return LoteCantidadRegistrada;
        }


        public String getFechaCaducidad() {
            return FechaCaducidad;
        }



        public String getCantidadLote() {
            return CantidadLote;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_transfer_primerayultima);

            new cambiaColorStatusBar(contexto, R.color.VerdeStd, Recepcion_Registro_Transferencia_PrimeraYUltima.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();


            edtx_PrimerEmpaque.requestFocus();

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }

    }
    @Override
    protected void onResume()
    {
        if(!txtv_Traspaso.getText().toString().equals("-"))
        {
//            SegundoPlano sp = new SegundoPlano("");
//            sp.execute();
        }
        super.onResume();
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

            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {

                reiniciarVariables();
            }
        }catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
        return super.onOptionsItemSelected(item);
    }
    private void declararVariables()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.recepcion_primera_y_ultima));
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


            //    edtx_Factura = (EditText) findViewById(R.id.edtx_Factura);
            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_PrimerEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_PrimerEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CantidadEmpaques = (EditText) findViewById(R.id.edtx_CantidadEmpaques);
            edtx_CantidadEmpaques.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_UltimoEmpaque = (EditText) findViewById(R.id.edtx_UltimoEmpaque);
            edtx_UltimoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


            txtv_Traspaso = (TextView) findViewById( R.id.txtv_Traspaso);//
            txtv_Caducidad = (TextView) findViewById(R.id.txtv_Caducidad);//
            txtv_UM = (TextView) findViewById(R.id.txtv_UnidadMedida);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Prod);
            txtv_CantidadOriginal = (TextView) findViewById(R.id.txtv_CantidadTotal);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_CantidadRegistrada);
            txtv_Pallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
            txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
            txtv_Lote = (TextView) findViewById( R.id.txtv_Lote);



            txtv_Producto.setText(NumParte);
            txtv_Caducidad.setText(FechaCaducidad);
            txtv_CantidadOriginal.setText(CantidadTotal);
            txtv_CantidadRegistrada.setText(CantidadRecibida);
            txtv_Lote.setText(LoteTrans);
            txtv_Traspaso.setText(Transferencia);
            txtv_UM.setText(FechaProd);


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }
    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();

            NumParte= b.getString("Producto");
            CantidadTotal= b.getString("CantSolicitada");
            CantidadRecibida= b.getString("CantSurtida");
            FechaCaducidad = b.getString("FechaCad");
            FechaProd= b.getString("FechaProd");
            LoteTrans = b.getString("Lote");
            Transferencia = b.getString("Transferencia");
            PartidaERP = b.getString("Partida");


        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras, " + e.getMessage());
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

                try
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
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
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
        edtx_EmpxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (!edtx_EmpxPallet.getText().toString().equals(""))
                        {


                            try {
                                if (Integer.parseInt(edtx_EmpxPallet.getText().toString()) > 999999)
                                {
                                    edtx_EmpxPallet.setText("");
                                    edtx_EmpxPallet.requestFocus();
                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                } else {

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_PrimerEmpaque.requestFocus();
                                        }
                                    });
                                }
                            }catch (NumberFormatException ex)
                            {
                                new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_cantidad_valida),"false",true,true);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_EmpxPallet.setText("");
                                        edtx_EmpxPallet.requestFocus();
                                    }
                                });
                            }
                        }else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"false", true, true);


                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_EmpxPallet.requestFocus();
                                    edtx_EmpxPallet.setText("");
                                }
                            });

                        }

                        //edtx_EmpxPallet.requestFocus();
                        // new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_Spinner.this);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
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

                        if (!edtx_Cantidad.getText().toString().equals("")) {
                            try {
                                if (Float.parseFloat(edtx_Cantidad.getText().toString()) > 999999) {

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_Cantidad.setText("");
                                            edtx_Cantidad.requestFocus();
                                        }
                                    });

                                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                } else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtx_EmpxPallet.requestFocus();
                                        }
                                    });

                                }
                            } catch (NumberFormatException ex) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_Cantidad.setText("");
                                        edtx_Cantidad.requestFocus();

                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_valida), "false", true, true);
                            }
                        }else
                        {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_cantidad_valida),"false", true, true);

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
        edtx_PrimerEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (!edtx_PrimerEmpaque.getText().toString().equals(""))
                        {
                        }else
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_PrimerEmpaque.setText("");
                                    edtx_PrimerEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(Recepcion_Registro_Transferencia_PrimeraYUltima.this);

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });

        edtx_UltimoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if (!edtx_UltimoEmpaque.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_CantidadEmpaques.requestFocus();

                                }
                            });



                        }else
                        {


                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_UltimoEmpaque.setText("");
                                    edtx_UltimoEmpaque.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                        }
                        new esconderTeclado(Recepcion_Registro_Transferencia_PrimeraYUltima.this);

                    }catch (Exception e) {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }
                return false;
            }
        });




        edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {


                        if(edtx_Cantidad.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad)+" - Cantidad", "false", true, true);
                            return true;
                        }
                        if(edtx_EmpxPallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Empaquespor Pallet", "false", true, true);
                            return true;
                        }
                        if(edtx_PrimerEmpaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Primer empaque", "false", true, true);
                            return true;
                        }
                        if(edtx_UltimoEmpaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Ultimo empaque", "false", true, true);
                            return true;
                        }
                        if(edtx_CantidadEmpaques.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Validación " +  " cantidad empaques", "false", true, true);
                            return true;
                        }
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CantidadEmpaques.requestFocus();

                            }
                        });

                        SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
                        sp.execute();
                        //validacionFinal();
                        new esconderTeclado(Recepcion_Registro_Transferencia_PrimeraYUltima.this);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }
                }

                return false;
            }
        });

        /*spnr_OrdenesCompra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    txtv_Caducidad.setText(ListaLotes.get(position).getFechaCaducidad());
                    txtv_CantidadLote.setText((ListaLotes.get(position).getCantidadLote()));
                    txtv_CantRegLote.setText(ListaLotes.get(position).getLoteCantidadRegistrada());
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                try
                {

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                }
            }
        });
*/
    }
    private void reiniciarVariables()
    {
        try {

            edtx_PrimerEmpaque.setText("");
            edtx_UltimoEmpaque.setText("");
            edtx_CantidadEmpaques.setText("");
            edtx_PrimerEmpaque.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }
    private void validacionFinal()
    {
        if(edtx_Cantidad.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad)+" - Cantidad", "false", true, true);
            return;
        }
        if(edtx_EmpxPallet.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Empaquespor Pallet", "false", true, true);
            return;
        }
        if(edtx_PrimerEmpaque.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Primer empaque", "false", true, true);
            return;
        }
        if(edtx_UltimoEmpaque.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque) +" - Ultimo empaque", "false", true, true);
            return;
        }
        if(edtx_CantidadEmpaques.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_cantidad) +" - Validación" +  " cantidad empaques", "false", true, true);
            return;
        }
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_CantidadEmpaques.requestFocus();

            }
        });

        SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
        sp.execute();
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
      //  SpinnerAdapter spinnerAdapter;
        View LastView;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                Log.d(TAG, "onPostExecute() " + "PASO");

                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LastView = getCurrentFocus();
                        progressBarHolder.requestFocus();
                        recarga = true;
                    }
                }, 10);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }

         /*   LastView = getCurrentFocus();
            progressBarHolder.requestFocus();
            recarga=true;*/
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {

                switch (tarea)
                {


                    case "RegistrarEmpaqueNuevo":


                        Cantidad = edtx_Cantidad.getText().toString();
                        EmpaquesPallet = edtx_EmpxPallet.getText().toString();
                        String PrimerEmpaque = edtx_PrimerEmpaque.getText().toString();
                        String UltimoEmpaque= edtx_UltimoEmpaque.getText().toString();
                        String CantidadEmpaques = edtx_CantidadEmpaques.getText().toString();


                        if(!(Integer.parseInt(edtx_EmpxPallet.getText().toString()) <=0))
                        {
                            if(!(Float.parseFloat(edtx_Cantidad.getText().toString()) <=0))
                            {


                                    sa.SOAPRegistraPrimeraYUltimaTraspaso(Transferencia, PartidaERP,  Cantidad, EmpaquesPallet, PrimerEmpaque, UltimoEmpaque, CantidadEmpaques, contexto);
                                    mensaje = sa.getMensaje();
                                    decision = sa.getDecision();

                            }else
                            {
                                decision = "false";
                                mensaje = getString(R.string.error_cantidad_menor_0);

                            }
                        }else
                        {
                            decision = "false";
                            mensaje = getString(R.string.error_empxpal_menor_0);

                        }
                        break;
                    case "RegistraPalletNuevo":

                        //   Thread.sleep(5000);
                        sa.SOAPCierraPalletTraspaso(CodigoPallet,contexto);
                        mensaje = sa.getMensaje();
                        decision = sa.getDecision();
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
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            try
            {

               SegundoPlano sp;
                recarga = false;
                if(LastView!=null)
                {
                    LastView.requestFocus();
                }

                if(decision.equals("true"))
                {
                    switch (tarea)
                    {
                        case "RegistrarEmpaqueNuevo":

                            txtv_CantidadRegistrada.setText(CantidadEmpaques);
                            edtx_PrimerEmpaque.setText("");
                            edtx_UltimoEmpaque.setText("");
                            edtx_CantidadEmpaques.setText("");
                            edtx_PrimerEmpaque.requestFocus();

                            new esconderTeclado(Recepcion_Registro_Transferencia_PrimeraYUltima.this);
                            if (OrdenCerrada.equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_compra_completada), decision, true, true);

                                Log.d(TAG, "onPostExecute() Orden cerrada: " + OrdenCerrada);
                                sp = new SegundoPlano("RegistraPalletNuevo");
                                sp.execute();

                                return;
                            }

                            else if (PartidaCerrada.equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), decision, true, true);

                                Log.d(TAG, "onPostExecute() Partida cerrada " + PartidaCerrada);


                                Log.d(TAG, "onPostExecute() Partida cerrada - Pallet cerrado " + PartidaCerrada + "  " + PalletCerrado);

                                sp = new SegundoPlano("RegistraPalletNuevo");
                                sp.execute();



                                return;
                            }

                            else if ((PalletCerrado.equals("1")) && !(tarea.equals("CompraPalletUnicoMP") && !(tarea.equals("RegistraPalletNuevo")))) {


                                Log.d(TAG, "onPostExecute() Pallet cerrado " + PalletCerrado);

                                sp = new SegundoPlano("RegistraPalletNuevo");
                                sp.execute();

                            }

                            break;


                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, getCurrentFocus(),"Pallet ["+mensaje+"] Cerrado con éxito",decision, true, true);

                            reiniciarVariables();
                            break;

                    }
                }
                if(decision.equals("false"))
                {
                    reiniciarVariables();
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje,decision, true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

    }
    public void sacaDatos(String tarea)
    {

        try
        {
            SoapObject tabla = sa.parser();


            // arrayListPosiciones = new ArrayList<>();
//        Rec_RegistroOC.posiciones p;
            if(tabla!=null)
                for(int i = 0; i<tabla.getPropertyCount();i++)
                {
                    try {

                        SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                        Log.d("SoapResponse",tabla1.toString());

                        switch (tarea)
                        {
                            case "RegistrarEmpaqueNuevo":

                                Log.d("SoapResponse", tabla1.toString());
                                Estado = tabla1.getPrimitivePropertyAsString("Estado");
                                Texto = tabla1.getPrimitivePropertyAsString("Texto");
                                CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                PartidaCerrada = tabla1.getPrimitivePropertyAsString("PartidaCerrada");
                                OrdenCerrada = tabla1.getPrimitivePropertyAsString("OrdenCerrada");
                                CantidadEmpaques = tabla1.getPrimitivePropertyAsString("CantEmpaques");
                                CantidadRecibida = tabla1.getPrimitivePropertyAsString("CantRecibida");
                                PalletCerrado = tabla1.getPrimitivePropertyAsString("PalletCerrado");

                                break;

                            case "ListarOrdenesCompra":

                                Log.d("SoapResponse", "Listar    "+tabla1.toString());
                                ArrayLotes.add(tabla1.getPrimitivePropertyAsString("LoteProveedor"));
                                // ArrayCaducidades.add(tabla1.getPrimitivePropertyAsString("FechaCaducidad"));

                                Log.d("SoapResponse", tabla1.getPrimitivePropertyAsString("LoteAXC"));

                                ListaLotes.add(
                                        new Lote(
                                                tabla1.getPrimitivePropertyAsString("LoteAXC"),
                                                tabla1.getPrimitivePropertyAsString("CantidadRecibida"),
                                                tabla1.getPrimitivePropertyAsString("FechaCaducidad"),
                                                tabla1.getPrimitivePropertyAsString("CantidadRegistrada"))
                                );

                                break;
                            case "CompraPalletUnicoMP":
                                CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                PartidaCerrada = tabla1.getPrimitivePropertyAsString("PartidaCerrada");
                                OrdenCerrada = tabla1.getPrimitivePropertyAsString("OrdenCerrada");
                                CantidadEmpaques = tabla1.getPrimitivePropertyAsString("CantEmpaques");
                                CantidadRecibida = tabla1.getPrimitivePropertyAsString("CantRecibida");
                                PalletCerrado = tabla1.getPrimitivePropertyAsString("PalletCerrado");
                                break;
                        }

                    }catch (Exception e)
                    {

                        e.printStackTrace();


                    }
                }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
