package com.automatica.AXCMP.c_Recepcion;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

public class Recepcion_Registro_PorEmpaque_Spinner extends AppCompatActivity
{

    //region variables
    String TAG = "SoapResponse";

    SoapAction sa = new SoapAction();


    EditText  edtx_EmpxPallet, edtx_Cantidad,edtx_CodigoEmpaque;
    String  Cantidad,CodigoEmpaque;
    Button btnCerrarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    TextView txtv_EmpaquesRegistrados, txtv_Caducidad,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet,txtv_CantidadLote,txtv_CantRegLote;
    Bundle b;

    ArrayList<String> ArrayLotes= new ArrayList<>();
    // ArrayList<String> ArrayFechaCaducidad = new ArrayList<>();
    ArrayList<Lote> ListaLotes = new ArrayList<>();
    View vista;
    Context contexto = this;
    String OrdenCompra, FechaCaducidad,ModificaCant,PartidaERP,IdRecepcion,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    String Estado,Texto,CodigoPallet,PartidaCerrada="",OrdenCerrada="",PalletCerrado="";
    Spinner spnr_Lotes;
    TextView txtv_OrdenCompra;
    String CantEmpaquesRegistrados;

    boolean verificarCierre,recargar;
    String TipoRecepcion;
    Handler h = new Handler();
    int registroAnteriorSpinner=0;
    //endregion
    private class Lote
    {
        String LoteAXC,LoteCantidadRegistrada, FechaCaducidad,CantidadLote;
        public Lote(String LoteAXC,String LoteCantidadRegistrada, String FechaCaducidad, String CantidadLote)
        {
            this.LoteAXC = LoteAXC;
            this.CantidadLote = CantidadLote;
            this.FechaCaducidad = FechaCaducidad;
            this.LoteCantidadRegistrada = LoteCantidadRegistrada;
        }

        public String getLoteCantidadRegistrada() {
            return LoteCantidadRegistrada;
        }


        public String getFechaCaducidad() {
            return FechaCaducidad;
        }


        public String getLoteAXC() {
            return LoteAXC;
        }

        public String getCantidadLote() {
            return CantidadLote;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_oc_porproveedor);
            new cambiaColorStatusBar(contexto, R.color.VerdeStd, Recepcion_Registro_PorEmpaque_Spinner.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();
            SegundoPlano sp = new SegundoPlano("ConsultaPallet");
            sp.execute();
            sp = new SegundoPlano("ListarOrdenesCompra");
            sp.execute();


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
            if (TipoRecepcion.equals("Proveedor")) {
         //       getSupportActionBar().setSubtitle(getString(R.string.recepcion_recepcion_por_empaque_proveedor));
            } else if (TipoRecepcion.equals("Maquila")) {
          //      getSupportActionBar().setSubtitle(getString(R.string.recepcion_recepcion_por_empaque_maquila));
            }
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            //  edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Factura);
            edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
            edtx_EmpxPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_Cantidad.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
            edtx_CodigoEmpaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

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
            if (ModificaCant.equals("1")) {
                edtx_Cantidad.setEnabled(true);
                edtx_EmpxPallet.setEnabled(true);
                edtx_CodigoEmpaque.requestFocus();
            } else if (ModificaCant.equals("0")) {
                edtx_Cantidad.setEnabled(false);
                edtx_EmpxPallet.setEnabled(false);
                edtx_CodigoEmpaque.requestFocus();
            }

       /* if(Cantidad.equals("0"))
        {
            txtv_EmpaquesRegistrados.setText("");

        }*/
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
                        if (!edtx_Cantidad.getText().toString().equals(""))
                            {
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
                            }else
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
                        if (!edtx_EmpxPallet.getText().toString().equals(""))
                            {

                                try {
                                    if (!(Integer.parseInt(edtx_EmpxPallet.getText().toString()) > 999999)) {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_CodigoEmpaque.requestFocus();
                                            }
                                        });
                                    } else {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_EmpxPallet.setText("");
                                                edtx_EmpxPallet.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cantidad_mayor_999999), "Advertencia", true, true);
                                    }
                                }catch (NumberFormatException ex)
                                    {
                                        h.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                edtx_EmpxPallet.setText("");
                                                edtx_EmpxPallet.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_cantidad_valida),"false",true,true);
                                    }
                            }else
                            {
                                edtx_EmpxPallet.setText("");
                                edtx_EmpxPallet.requestFocus();
                                new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);
                            }
                        //   edtx_EmpxPallet.requestFocus();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                        }
                    }
                return false;
            }
        });
        edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
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

                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto,getCurrentFocus(),"Ingrese candidad de empaques" , "false", true, true);
                                return false;

                            }

                            if(edtx_Cantidad.getText().toString().equals(""))
                            {
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_cantidad)  , "false", true, true);
                                return false;

                            }


                            if (!edtx_CodigoEmpaque.getText().toString().equals(""))
                                {

                                    SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
                                    sp.execute();
                                    btnCerrarTarima.setEnabled(true);
                           /* edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();*/

                                }else {

                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_empaque) , "false", true, true);
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
                                    SegundoPlano sp = new SegundoPlano("RegistraPalletNuevo");
                                    sp.execute();
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
                    txtv_Caducidad.setText(ListaLotes.get(position).getFechaCaducidad());
                    txtv_CantidadLote.setText((ListaLotes.get(position).getCantidadLote()));
                    txtv_CantRegLote.setText(ListaLotes.get(position).getLoteCantidadRegistrada());
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
                FechaCaducidad = b.getString("FechaCaducidad");
                ModificaCant= b.getString("ModificaCant");
                PartidaERP= b.getString("PartidaERP");
                IdRecepcion= b.getString("IdRecepcion");
                NumParte= b.getString("NumParte");
                UM= b.getString("UM");
                CantidadTotal= b.getString("CantidadTotal");
                CantidadRecibida= b.getString("CantidadRecibida");
                CantidadEmpaques= b.getString("CantidadEmpaques");
                Log.e("SoapResponse", "SacaExtrasIntent: "+CantidadEmpaques );
                EmpaquesPallet= b.getString("EmpaquesPallet");
                TipoRecepcion = b.getString("Tipo");


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
        edtx_CodigoEmpaque .setText("");
        edtx_CodigoEmpaque.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        SpinnerAdapter spinnerAdapter;
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
                switch (tarea) {
                    case "ListarOrdenesCompra":
                        sa.SOAPListarLotesOC(OrdenCompra, PartidaERP, NumParte, contexto);
                        ListaLotes = new ArrayList<>();
                        ArrayLotes = new ArrayList<>();
                        mensaje = sa.getMensaje();
                        decision = sa.getDecision();
                        break;
                    case "ConsultaPallet":
                        sa.SOAPConsultaPalletAbiertoOC(IdRecepcion, PartidaERP, contexto);
                        mensaje = sa.getMensaje();
                        decision = sa.getDecision();
                        break;
                    case "RegistrarEmpaqueNuevo":

                     //   Thread.sleep(5000);

                        CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                        Cantidad = edtx_Cantidad.getText().toString();
                        EmpaquesPallet = edtx_EmpxPallet.getText().toString();
                        Log.i("SoapResponse",ArrayLotes.size() +" "+ ArrayLotes.isEmpty());

                        if(!(Integer.parseInt(edtx_EmpxPallet.getText().toString()) <=0))
                            {
                                if(!(Float.parseFloat(edtx_Cantidad.getText().toString()) <=0))
                                    {
                                        if (!ArrayLotes.isEmpty())
                                        {
                                            sa.SOAPRegistrarEmpaqueCompra(OrdenCompra, PartidaERP, CodigoEmpaque, ListaLotes.get(spnr_Lotes.getSelectedItemPosition()).getLoteAXC(), Cantidad, EmpaquesPallet, contexto);
                                            Log.d("SoapResponse", "doInBackground: " + OrdenCompra + " " + PartidaERP + " " + CodigoEmpaque + " " + ListaLotes.get(spnr_Lotes.getSelectedItemPosition()).getLoteAXC() + " " + Cantidad + " " + EmpaquesPallet);
                                            mensaje = sa.getMensaje();
                                            decision = sa.getDecision();
                                        } else
                                            {
                                                decision = "false";
                                                mensaje = "No hay un lote seleccionado";

                                            }
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
                        sa.SOAPCierraPalletCompra(txtv_Pallet.getText().toString(), contexto);
                        mensaje = sa.getMensaje();
                        decision = sa.getDecision();
                        break;
                    case "CierraPalletSinConsulta":
                        sa.SOAPCierraPalletCompra(txtv_Pallet.getText().toString(), contexto);
                        mensaje = sa.getMensaje();
                        decision = sa.getDecision();
                        break;

                }

               /* mensaje = sa.getMensaje();
                decision = sa.getDecision();*/

                if (decision.equals("true") && !tarea.equals("ConsultaPallet")) {
                    sacaDatos(tarea);
                }

            }catch (Exception e)
                {
                    e.printStackTrace();
                    mensaje = e.getMessage();
                    decision = "false";
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


                    if(decision.equals("true"))
                        {

                            SegundoPlano sp;
                            switch (tarea)
                                {
                                    case "RegistrarEmpaqueNuevo":


                                        txtv_EmpaquesRegistrados.setText(CantEmpaquesRegistrados);
                                        txtv_Pallet.setText(CodigoPallet);
                                        txtv_CantidadRegistrada.setText(CantidadRecibida);
                                        edtx_CodigoEmpaque.setText("");
                                        edtx_CodigoEmpaque.requestFocus();

                                        new esconderTeclado(Recepcion_Registro_PorEmpaque_Spinner.this);

                                        if (OrdenCerrada.equals("1"))
                                            {
                                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_completada), decision, true, true);

                                                Log.d(TAG, "onPostExecute() Orden cerrada: " + OrdenCerrada);


                                                Log.d(TAG, "onPostExecute() Orden cerrada - Pallet cerrado " + OrdenCerrada + "  " + PalletCerrado);

                                                sp = new SegundoPlano("RegistraPalletNuevo");
                                                sp.execute();
                                                //  reiniciaVariables();


                                                return;
                                            }
                                        else if (PartidaCerrada.equals("1"))
                                            {
                                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), decision, true, true);

                                                Log.d(TAG, "onPostExecute() Partida cerrada " + PartidaCerrada);


                                                Log.d(TAG, "onPostExecute() Partida cerrada - Pallet cerrado " + PartidaCerrada + "  " + PalletCerrado);

                                                sp = new SegundoPlano("RegistraPalletNuevo");
                                                sp.execute();


                                                // reiniciaVariables();
                                                return;
                                            }
                                        else if ((PalletCerrado.equals("1")))
                                            {

                                                //    new popUpGenerico(contexto, vista, getString(R.string.orden_compra_pallet_cerrado), "false", true, true);
                                                Log.d(TAG, "onPostExecute() Pallet cerrado " + PalletCerrado);
                                                sp = new SegundoPlano("RegistraPalletNuevo");
                                                sp.execute();
                                                // reiniciaVariables();
                                            }



                                        sp = new SegundoPlano("ListarOrdenesCompra");
                                        sp.execute();



                                        break;


                                    case "ConsultaPallet":
                                        String[] arrayData = mensaje.split("#");
                                        txtv_EmpaquesRegistrados.setText(arrayData[1]);
                                        txtv_Pallet.setText(arrayData[0]);

                                        break;

                                    case "RegistraPalletNuevo":
                                        new popUpGenerico(contexto, LastView,"Pallet"+"["+mensaje+"] Cerrado con éxito",decision, true, true);

                                        sp = new SegundoPlano("ListarOrdenesCompra");
                                        sp.execute();
                                        sp = new SegundoPlano("ConsultaPallet");
                                        sp.execute();
                                        break;
                                    case "CierraPalletSinConsulta":
                                        new popUpGenerico(contexto, LastView,"Pallet"+"["+mensaje+"] Cerrado con éxito",decision, true, true);
                                        sp = new SegundoPlano("ListarOrdenesCompra");
                                        sp.execute();
                                        reiniciaVariables();
                                        txtv_Pallet.setText("");
                                        break;

                                    case "ListarOrdenesCompra":
                                        registroAnteriorSpinner = spnr_Lotes.getSelectedItemPosition();
                                        spinnerAdapter= new ArrayAdapter<String>(
                                                Recepcion_Registro_PorEmpaque_Spinner.this,
                                                android.R.layout.simple_spinner_item,
                                                ArrayLotes);
                                        ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spnr_Lotes.setAdapter(spinnerAdapter);
                                        spnr_Lotes.setSelection(registroAnteriorSpinner);
                                        break;
                                }
                        }


                    if(decision.equals("false"))
                        {
                            reiniciaVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), mensaje,decision, true, true);
                        }
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);

                    OrdenCerrada="";
                    PartidaCerrada="";
                    PalletCerrado="";

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                }
                recargar = false;
        }

    }
    private void sacaDatos(String tarea)
    {
        try {
            SoapObject tabla = sa.parser();


            if(tabla!=null)
                for (int i = 0; i < tabla.getPropertyCount(); i++) {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    switch (tarea)
                        {
                            case "RegistrarEmpaqueNuevo":

                                Log.i(TAG, tabla1.toString());
                                Estado = tabla1.getPrimitivePropertyAsString("Estado");
                                Texto = tabla1.getPrimitivePropertyAsString("Texto");
                                CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                                PartidaCerrada = tabla1.getPrimitivePropertyAsString("PartidaCerrada");
                                OrdenCerrada = tabla1.getPrimitivePropertyAsString("OrdenCerrada");
                                PalletCerrado = tabla1.getPrimitivePropertyAsString("PalletCerrado");
                                CantEmpaquesRegistrados= tabla1.getPrimitivePropertyAsString("CantEmpaques");
                                //EmpaquesPallet = tabla1.getPrimitivePropertyAsString("EmpaquesPallet");
                                CantidadRecibida = tabla1.getPrimitivePropertyAsString("CantRecibida");


                                break;
                            case "ListarOrdenesCompra":

                                Log.i(TAG, "Listar    "+tabla1.toString());
                                ArrayLotes.add(tabla1.getPrimitivePropertyAsString("LoteProveedor"));

                                //ArrayFechaCaducidad.add(tabla1.getPrimitivePropertyAsString("FechaCaducidad"));
                                //Log.d("SoapResponse", tabla1.getPrimitivePropertyAsString("LoteProveedor"));

                                ListaLotes.add(
                                        new Lote(
                                        tabla1.getPrimitivePropertyAsString("LoteAXC"),
                                        tabla1.getPrimitivePropertyAsString("CantidadRegistrada"),
                                        tabla1.getPrimitivePropertyAsString("FechaCaducidad"),
                                        tabla1.getPrimitivePropertyAsString("CantidadRecibida"))
                                );
                                break;



                        }


                }
        }catch (Exception e)
            {
                e.printStackTrace();
                Log.d(TAG, "sacaDatos: ");
            }
    }
    @Override
    public void onBackPressed()
    {
        Recepcion_Registro_PorEmpaque_Spinner.this.finish();
        super.onBackPressed();
    }




}
