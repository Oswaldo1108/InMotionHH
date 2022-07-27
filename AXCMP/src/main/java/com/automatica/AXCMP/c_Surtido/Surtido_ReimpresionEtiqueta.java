package com.automatica.AXCMP.c_Surtido;

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

public class Surtido_ReimpresionEtiqueta extends AppCompatActivity
{
    //region variables
    String TAG = Surtido_ReimpresionEtiqueta.class.getSimpleName();
    Button btn_enviar ;
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    Context contexto = this;
    EditText edtx_CodigoEmpaque ;
    View vista ;
    Handler handler = new Handler();
    TextView txtv_peso ;

    Spinner spnr_Areas;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String Area;
    Bundle b;

    ArrayList<String> ArrayAreas;
    ArrayList<String> ArrayIdAreas;
    Integer ItemSeleccionado = null;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimpresion_etiqueta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        try {
            //new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_ReimpresionEtiqueta.this);

            getSupportActionBar().setTitle(getString(R.string.reimpresion_etiqueta));
            SacaExtrasIntent();
            declaraVariables();
            agregaListeners();
            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Surtido_ReimpresionEtiqueta.this);

            if(!Area.equals("0"))
                {
                    new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_ReimpresionEtiqueta.this);

                }



            SegundoPlano sp = new SegundoPlano("ConsultaAreas");
            sp.execute();
            spnr_Areas.setSelection(Integer.parseInt(Area));

            Log.d("SoapResponse", "On create"  + Area);

        } catch (Exception e) {
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
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume()
    {

        super.onResume();
    }

    @Override
    protected void onPause()
    {

        edtx_CodigoEmpaque.setText("");

        super.onPause();
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
                    if ((id == R.id.borrar_datos))
                        {

                       edtx_CodigoEmpaque.setText("" );
                       edtx_CodigoEmpaque.requestFocus();
                        }

//

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
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            btn_enviar = (Button) findViewById(R.id.btn_ReimprimirEtiqueta);
            edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

            spnr_Areas = (Spinner) findViewById(R.id.spnr_OrdenesCompra);

            txtv_peso = (TextView) findViewById(R.id.txtv_peso);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }

    private void agregaListeners()
    {




        btn_enviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {

                    if(edtx_CodigoEmpaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_empaque),"false" ,true ,true );
                        edtx_CodigoEmpaque.setText("");
                        edtx_CodigoEmpaque.requestFocus();
                        return;
                    }

                        SegundoPlano sp = new SegundoPlano("ReimprimeEtiqueta");
                        sp.execute();

                     } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });





        edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    if(!edtx_CodigoEmpaque.getText().toString().equals(""))
                    {


                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"false",true,true);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoEmpaque.setText("");
                                edtx_CodigoEmpaque.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Surtido_ReimpresionEtiqueta.this);
                }
                return false;
            }
        });


    }

    private void SacaExtrasIntent()
    {
        try
        {

              b = getIntent().getExtras();

              Area= b.getString("Area");

              if(Area == null)
              {
                  Area = "1";
              }

              Log.e("SoapResponse", "SacaExtrasIntent:"+Area);



        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
            Area = "0";
        }

    }



    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        String decision,mensaje;
        SpinnerAdapter spinnerAdapter;

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
        //    ic_conexion.setImageResource(R.drawable.ic_conectado);
            Log.i("SoapResponse", "OnPreex");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Log.i("SoapResponse", "Antes switch Do in backgound");
            try
            {
                switch (tarea)
                {
                    case "ReimprimeEtiqueta":
                        // Thread.sleep(5000);
                        sa.SOAPReimprimirEtiquetaEPK(edtx_CodigoEmpaque.getText().toString(),ArrayIdAreas.get(spnr_Areas.getSelectedItemPosition()),contexto);
                        break;

                    case"ConsultaAreas":
                        ArrayAreas = new ArrayList<>();
                        ArrayIdAreas = new ArrayList<>();
                        sa.SOAPConsultaAreasPesaje(contexto);
                        break;


                }
                mensaje = sa.getMensaje();
                decision = sa.getDecision();
                if(decision.equals("true"))
                    {
                        sacaDatos(tarea);
                    }

                Log.i("SoapResponse", "Do in backgound");
            } catch (Exception e)
            {
                e.printStackTrace();
                mensaje = e.getMessage();
            }

            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {
            try
            {

                if (decision.equals("true"))
                {
                    switch (tarea)
                    {
                        case "ReimprimeEtiqueta":
                            new popUpGenerico(Surtido_ReimpresionEtiqueta.this,getCurrentFocus(), "Empaque reimpreso con exito ["+edtx_CodigoEmpaque.getText().toString() + "]",decision,true,true);
                            edtx_CodigoEmpaque.setText("");
                            break;

                        case "ConsultaAreas":
                            //   new popUpGenerico(contexto, vista, "Orden de compra cerrada con exito.", "false", true, true);
                            //   registroAnteriorSpinner = spnr_Areas.getSelectedItemPosition();
                            spinnerAdapter= new ArrayAdapter<String>(
                                    Surtido_ReimpresionEtiqueta.this,
                                    android.R.layout.simple_spinner_item,
                                    ArrayAreas);
                            ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_Areas.setAdapter(spinnerAdapter);
                            //spnr_Areas.setSelection(registroAnteriorSpinner);

                            if(Area != null)
                                {
                                    spnr_Areas.setSelection(Integer.parseInt(Area)-1);
                                }

                            break;



                    }
                }
                if (decision.equals("false"))
                {
                    new popUpGenerico(Surtido_ReimpresionEtiqueta.this,getCurrentFocus(), mensaje,"false" ,true,true);
                    edtx_CodigoEmpaque.setText("");
                    edtx_CodigoEmpaque.requestFocus();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(Surtido_ReimpresionEtiqueta.this,getCurrentFocus(), e.getMessage(),"false" ,true,true);

            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

        }
    }


    @Override
    protected void onDestroy()
    {

        super.onDestroy();
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

                            case "ConsultaAreas":
                                ArrayAreas.add( tabla1.getPrimitivePropertyAsString("Descripcion"));
                                ArrayIdAreas.add(tabla1.getPrimitivePropertyAsString("IdArea"));
                                break;

                        }

                }
        }catch (Exception e)
            {
                e.printStackTrace();
                Log.d("SoapResponse", "sacaDatos: ");
            }
    }


//
}
