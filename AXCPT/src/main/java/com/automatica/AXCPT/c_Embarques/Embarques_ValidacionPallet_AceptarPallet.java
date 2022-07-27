package com.automatica.AXCPT.c_Embarques;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.AXCPT.adaptadorTabla;
import com.automatica.AXCPT.c_Almacen.constructorTablaEntradaAlmacen;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;



public class Embarques_ValidacionPallet_AceptarPallet extends AppCompatActivity
{
    Context contexto = this;
    View vista;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    SoapAction sa = new SoapAction();

    EditText edtx_Pallet;
    ListView lista;
    Button Rechazar,Aceptar;

    String dato, dato2, dato3, dato4, dato5, dato6;
    String OrdenSalida,CodigoPallet;
    adaptadorTabla adaptador=null;
    ArrayList<datos> DATOS;
    ArrayList<constructorTablaEntradaAlmacen> constructorTablaEntradaAlmacens;

    class datos
    {
           String Titulo,valor;

        public datos(String titulo, String valor)
        {
            Titulo = titulo;
            this.valor = valor;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.embarques_activity_validacion_pallet_det);



        try
            {

                new cambiaColorStatusBar(contexto, R.color.MoradoStd, Embarques_ValidacionPallet_AceptarPallet.this);

                getIntentExtras();
                declaraVariables();
                agregaListeners();

                if(!CodigoPallet.equals(""))
                    {
                        edtx_Pallet.setText(CodigoPallet);
                        SegundoPlanoValidaPallet sp = new SegundoPlanoValidaPallet();
                        sp.execute();
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Embarques_Valida));
        //toolbar.setSubtitle("  Valida");
//        toolbar.setLogo(R.mipmap.logo_axc);// toolbar.setLogo(R.drawable.axc_logo_toolbar);
        lista = (ListView) findViewById(R.id.lstv_validaPallet);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Pallet= (EditText) findViewById(R.id.txtv_Pallet);
        edtx_Pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


      //  tableView.addDataClickListener(new Embarques_ValidacionPallet.ListenerClickTabla());
        Rechazar= (Button) findViewById(R.id.btn_rechazar);
        Aceptar = (Button) findViewById(R.id.btn_AceptarPallet);
//        Rechazar.setEnabled(false);
//        Aceptar.setEnabled(false);
    }
    private void agregaListeners()
    {
        edtx_Pallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                try
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if(edtx_Pallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet), false, true, false);
                            return false;
                        }
                        SegundoPlanoValidaPallet spv = new SegundoPlanoValidaPallet();
                        spv.execute();
                        new esconderTeclado(Embarques_ValidacionPallet_AceptarPallet.this);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, false);
                    reiniciaVariables();

                }
                return false;
            }
        });
        Aceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {

                if(edtx_Pallet.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet), false, true, false);
                    return;
                }
                new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                        .setTitle("¿Aceptar Pallet?").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SegundoPlanoDecisionPallet sp = new SegundoPlanoDecisionPallet("Aceptar");
                                sp.execute();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, false);
                    reiniciaVariables();

                }

            }
        });
        Rechazar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try
                {
                if(edtx_Pallet.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet), false, true, false);
                    return;
                }

                new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                        .setTitle("¿Rechazar Pallet?").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SegundoPlanoDecisionPallet sp = new SegundoPlanoDecisionPallet("Rechazar");
                                sp.execute();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, false);
                    reiniciaVariables();

                }
            }
        });
    }
    private void getIntentExtras()
    {
        try {
            Bundle b;
            b = getIntent().getExtras();
            OrdenSalida = b.getString("OrdenSalida");
            CodigoPallet = b.getString("CodigoPallet");
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
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

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }
        if((id ==R.id.borrar_datos))
        {
            reiniciaVariables();
        }
        return super.onOptionsItemSelected(item);
    }
    public void  reiniciaVariables()
    {
        edtx_Pallet.setText("");
        if (adaptador!=null) adaptador.clear();
        adaptador=null;
        Rechazar.setEnabled(false);
        Aceptar.setEnabled(false);
    }

    class SegundoPlanoValidaPallet extends AsyncTask<Void,Void,Void>
    {
        String mensaje ,decision;
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
            String Pallet = edtx_Pallet.getText().toString();
            sa.SOAPConsultarPalletAValidar(Pallet,contexto);
            mensaje = sa.getMensaje();
            decision = sa.getDecision();

            if(decision.equals("true"))
            {
                constructorTablaEntradaAlmacens = new ArrayList<>();
            sacaDatos("s");
            for(datos a:DATOS)
            {

                constructorTablaEntradaAlmacen c = new constructorTablaEntradaAlmacen(a.Titulo,a.valor);
                constructorTablaEntradaAlmacens.add(c);
            }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(decision.equals("true"))
            {
                adaptador = new adaptadorTabla(Embarques_ValidacionPallet_AceptarPallet.this, R.layout.list_item_2datos,constructorTablaEntradaAlmacens);
                lista.setAdapter(adaptador);
                Rechazar.setEnabled(true);
                Aceptar.setEnabled(true);
            }
            if(decision.equals("false"))
            {
                new popUpGenerico(contexto,vista,mensaje,"Advertencia",true,true);
                Rechazar.setEnabled(false);
                Aceptar.setEnabled(false);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    class SegundoPlanoDecisionPallet extends AsyncTask<Void,Void,Void>
    {
        String BotonPresionado, mensaje,decision;
        public SegundoPlanoDecisionPallet(String BotonPrecionado)
        {
            BotonPresionado = BotonPrecionado;
        }
        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
          //  new popUpGenerico(contexto,vista,"¿"+BotonPresionado+" pallet?","Verificación",true,false);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            String Pallet = edtx_Pallet.getText().toString();
            switch(BotonPresionado)
            {
                case "Aceptar":
                    Log.d("SoapResponse",OrdenSalida+Pallet);
                    sa.SOAPRegistrarPalletLineaValidado(OrdenSalida,Pallet,contexto);
                    break;

                case "Rechazar":
                    sa.SOAPRegistrarPalletLineaRechazado(OrdenSalida,Pallet,contexto);
                    break;

            }
            decision =sa.getDecision();
            mensaje = sa.getMensaje();
            if(decision.equals("true"))
            {
                mensaje = "Pallet registrado exitosamente.";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {


                if (decision.equals("true")) {
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, false);
                    reiniciaVariables();
                }
                if (decision.equals("false")) {
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, false);
                    reiniciaVariables();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), decision, true, false);
                reiniciaVariables();

            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    private void sacaDatos()
    {
        SoapObject tabla = sa.parser();
        if (sa.getDecision().equals("true"))
        {
            Log.i("SoapResponse", "sacaDatos: " + tabla.toString());

           DATOS = new ArrayList<>();
        //    for (int i = 0; i < tabla.getPropertyCount(); i++) {
                try {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(1);
                    Log.d("SoapResponse", tabla.toString());
                    dato = tabla1.getPrimitivePropertyAsString("Estado");
                    dato2 = tabla1.getPrimitivePropertyAsString("TipoPallet");
                    dato3 = tabla1.getPrimitivePropertyAsString("Producto");
                    dato4 = tabla1.getPrimitivePropertyAsString("Desc");
                    dato5 = tabla1.getPrimitivePropertyAsString("Cantidad");
                    dato6 = tabla1.getPrimitivePropertyAsString("Lote");
                    Log.i("SoapResponse", "sacaDatos: " + tabla1.toString());

                    datos d = new datos("Estado",dato);
                    datos d2 = new datos("Tipo de pallet",dato2);
                    datos d3 = new datos("Producto",dato3);
                    datos d4 = new datos("Descripción",dato4);
                    datos d5 = new datos("Cantidad",dato5);
                    datos d6 = new datos("Lote",dato6);
                    DATOS.add(d);
                    DATOS.add(d2);
                    DATOS.add(d3);
                    DATOS.add(d4);
                    DATOS.add(d5);
                    DATOS.add(d6);

                } catch (Exception e) {

                    e.printStackTrace();
                }
           // }
        }
    }

    private void sacaDatos(String tarea)
    {
        try {
            SoapObject tabla = sa.parser();
            String Empaque;

            PropertyInfo pi;


            DATOS = new ArrayList<>();
            if (tabla != null)
            {
                for (int i = 0; i < tabla.getPropertyCount(); i++) {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);


                    for (int t = 0; t < tabla1.getPropertyCount(); t++) {
                        pi = new PropertyInfo();
                        tabla1.getPropertyInfo(t, pi);
                        String name = pi.name;
                        if (name.equals("FechaCrea")) {
                            name = "Fecha Creación";
                        }
                        if (name.equals("FechaCad")) {
                            name = "Fecha Caducidad";
                        }
                        if (name.equals("LoteProveedor")) {
                            name = "Lote Proveedor";
                        }
                        if (name.equals("CantidadOriginal")) {
                            name = "Cantidad Original";
                        }
                        if (name.equals("CantidadActual")) {
                            name = "Cantidad Actual";
                        }
                        if (name.equals("CantidadOriginal")) {
                            name = "CantidadOriginal";
                        }
                        if (name.equals("Estacion")) {
                            name = "Estación";
                        }
                        if (name.equals("Revision")) {
                            name = "Revisión";
                        }


                        DATOS.add(new datos(name,tabla1.getPropertyAsString(t)));

                    }
                }
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



}
