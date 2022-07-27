package com.automatica.AXCPT.c_Almacen;

import android.content.Context;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Almacen_Colocar_v2 extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_CodigoPallet,edtx_CodigoUbicacion;


    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_Producto, txtv_Empaques,txtv_Cantidad;

    View vista;
    Context contexto = this;

    Bundle b ;

    String Producto,Empaques,Cantidad;
    String UbicacionSugerida;


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_colocar);
        declararVariables();
        agregaListeners();
    }

    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(" Colocación");
//        toolbar.setLogo(R.drawable.axc_logo_toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoUbicacion = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_CodigoUbicacion.setEnabled(false);


    }
    private void agregaListeners()
    {
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
                        new popUpGenerico(contexto,vista,"Debes capturar un codigo de pallet","Advertencia",true,true);
                    }

                }
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

                }
                return false;
            }
        });

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

            switch (tarea)
            {
                case "ConsultaPallet":
                    sa.SOAPConsultaPallet(edtx_CodigoPallet.getText().toString(),contexto);
                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();
                    break;

                case "ConsultaEmbalaje":
                    sa.SOAPConsultaPallet(edtx_CodigoPallet.getText().toString(),contexto);
                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();

                    break;
                case "ConsultaUbicacionSugerida":

                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();
                    break;

            }

            if(decision.equals("true"))
            {
                sacaDatos(tarea);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if (decision.equals("true"))
            {
                switch (tarea)
                {
                    case "ConsultaPallet":


                        break;
                    case "ConsultaUbicacionSugerida":

                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    private void sacaDatos(String Tarea)
    {
        SoapObject tabla = sa.parser();


        if(tabla!=null)
        {
            for(int i = 0; i<tabla.getPropertyCount();i++)

                try {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);

                    switch (Tarea)
                    {
                        case "ConsultaPallet":
                            Producto = tabla1.getPrimitivePropertyAsString("Producto");
                            Cantidad = tabla1.getPrimitivePropertyAsString("CantidadActual");
                            Empaques = tabla1.getPrimitivePropertyAsString("Empaques");
                            break;
                        case "ConsultaUbicacionSugerida":

                            break;
                    }
                    Log.d("SoapResponse",tabla1.toString());


                }catch (Exception e)
                {

                    e.printStackTrace();
                }
        }
        {
        }
    }
}
