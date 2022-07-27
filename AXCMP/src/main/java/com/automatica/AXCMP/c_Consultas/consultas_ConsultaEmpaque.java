package com.automatica.AXCMP.c_Consultas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
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


import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class consultas_ConsultaEmpaque extends AppCompatActivity
{
   //region Variables
    Context contexto= this;
    View vista;
    EditText Empaque;
    ListView lista;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    SoapAction sa = new SoapAction();
    ListView datosEmpaque;
    AdaptadorTablaNuevo adaptador;
    private Handler handlerRequestFocus = new Handler();
    boolean recarga;
    ArrayList<ConstructorDatoTitulo> DataToShow;
    //endregion

    //region generado
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_consulta_empaque);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Consultas_Empaque));
      //  toolbar.setSubtitle("  Consulta de Empaque");
       // toolbar.setLogo(R.drawable.logo_axc_mediano3);
        datosEmpaque = (ListView) findViewById(R.id.lstv_Empaque);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new cambiaColorStatusBar(contexto, R.color.AzulStd, consultas_ConsultaEmpaque.this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
    //    lista = (ListView) findViewById(R.id.lstv_posicion);

        Empaque= (EditText) findViewById(R.id.edtx_CodigoEmpaque);
        Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        Empaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    HiloRecibeDatos hrd = new HiloRecibeDatos();
                    hrd.execute();
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    new esconderTeclado(consultas_ConsultaEmpaque.this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(!recarga)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                //   Toast.makeText( this, "Campos Reiniciados", Toast.LENGTH_SHORT).show();
                Empaque.setText("");
                if (adaptador != null) adaptador.clear();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        return super.onOptionsItemSelected(item);
    }


    //endregion

    //region AsyncTask
    class HiloRecibeDatos extends AsyncTask<Void,Void,Void>
    {
        ArrayList<ConstructorDatoTitulo> arrayAdaptador = new ArrayList<>();

        String datoConsulta, decision,mensaje,tabla;
        @Override
        protected void onPreExecute()
        {
            recarga = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try {


                datoConsulta = Empaque.getText().toString();

                if (!datoConsulta.equals("")) {
                    sa.SOAPConsultaEmpaque(datoConsulta, contexto);
                    decision = sa.getDecision();
                    mensaje = sa.getMensaje();

                    if (decision.equals("true")) {
                        tabla = sa.getTabla();
                    }
                } else
                {
                    decision = "false";
                    mensaje = "Debe capturar un código de empaque";
                }
                if (decision.equals("true")) {
                    sacaDatos();


                    if (DataToShow != null) {
                        for (ConstructorDatoTitulo a : DataToShow)
                        {
                            arrayAdaptador.add(a);
                        }
                    }

                }
             }catch (Exception e)
        {
            decision = "false";
            mensaje = e.getMessage();
        }
        return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {

            try {
                recarga = false;
                if (decision.equals("true")) {
                    adaptador = new AdaptadorTablaNuevo(consultas_ConsultaEmpaque.this, R.layout.list_item_2datos, DataToShow);
                    datosEmpaque.setAdapter(adaptador);
                } else if (decision.equals("false")) {

                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                    Empaque.setText("");
                }
                Empaque.setText("");
                Empaque.requestFocus();
            }catch (Exception e)
            {
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), decision, true, true);
                e.printStackTrace();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    //endregion

    //region Soap
    public void sacaDatos() {
        try {
            SoapObject tabla = sa.parser();
            SoapObject tabla1 = (SoapObject) tabla.getProperty(0);

            if(tabla!=null) {
                String dato, dato2, dato3, dato4, dato5, dato6, dato7, dato8, dato9, dato10, dato11;
                DataToShow = new ArrayList<>();

                dato = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                dato2 = tabla1.getPrimitivePropertyAsString("NumParte");
                dato3 = tabla1.getPrimitivePropertyAsString("OrdenCompra");
                dato4 = tabla1.getPrimitivePropertyAsString("LoteProveedor");
                dato5 = tabla1.getPrimitivePropertyAsString("CantidadOriginal");
                dato6 = tabla1.getPrimitivePropertyAsString("CantidadActual");
                dato7 = tabla1.getPrimitivePropertyAsString("DescStatus");//
                dato8 = tabla1.getPrimitivePropertyAsString("FechaCrea");//
                dato9 = tabla1.getPrimitivePropertyAsString("Ubicacion");//
                dato10 = tabla1.getPrimitivePropertyAsString("LoteAXC");//
                dato11 = tabla1.getPrimitivePropertyAsString("Revision");

                ConstructorDatoTitulo e12 = new ConstructorDatoTitulo("Código empaque", Empaque.getText().toString());

                ConstructorDatoTitulo e = new ConstructorDatoTitulo("Código de Pallet", dato);
                ConstructorDatoTitulo e1 = new ConstructorDatoTitulo("Producto", dato2);
                ConstructorDatoTitulo e2 = new ConstructorDatoTitulo("Orden de Compra", dato3);
                ConstructorDatoTitulo e9 = new ConstructorDatoTitulo("Lote", dato10);
                ConstructorDatoTitulo e3 = new ConstructorDatoTitulo("Lote proveedor", dato4);
                ConstructorDatoTitulo e4 = new ConstructorDatoTitulo("Cantidad Original", dato5);
                ConstructorDatoTitulo e5 = new ConstructorDatoTitulo("Cantidad Actual", dato6);
                ConstructorDatoTitulo e6 = new ConstructorDatoTitulo("Estatus", dato7);
                ConstructorDatoTitulo e7 = new ConstructorDatoTitulo("Fecha de Creación", dato8);
                ConstructorDatoTitulo e8 = new ConstructorDatoTitulo("Ubicación", dato9);
                ConstructorDatoTitulo e10 = new ConstructorDatoTitulo("Revision", dato11);

                DataToShow.add(e12);
                DataToShow.add(e);
                DataToShow.add(e1);
                DataToShow.add(e2);
                DataToShow.add(e3);
                DataToShow.add(e9);
                DataToShow.add(e4);
                DataToShow.add(e5);
                DataToShow.add(e6);
                DataToShow.add(e7);
                DataToShow.add(e8);
                DataToShow.add(e10);


           /* for (int i = 0; i <= tabla1.getPropertyCount()-1; i++) {

                PropertyInfo pi = new PropertyInfo();
                tabla1.getPropertyInfo(i, pi);

                String data = tabla1.getProperty(i).toString();
                String titulo = pi.name;


                ConstructorDatoTitulo e = new ConstructorDatoTitulo(titulo, data);
                DataToShow.add(e);

                Log.i("SoapResponse", " Tabla 1" + titulo + data);
            }*/
            }

        }catch (Exception e)
        {
         e.printStackTrace();
        }
    }
//endregion
}
