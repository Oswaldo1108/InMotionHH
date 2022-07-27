package com.automatica.AXCMP.c_Almacen.Almacen_Ajustes;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Almacen_Ajustes_NuevoEmpaque extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_Cantidad,edtx_CodigoEmpaque,edtx_CodigoPallet;
    TextView txtv_Empaques,txtv_Producto,txtv_Cantidad,txtv_Lote;
    TextView txtv_EmpaquesRegistrados,txtv_Pallet;


    Spinner spnr_Ajuste;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    View vista;
    Context contexto = this;
    Bundle b;
    String IdAjusteBundle;
    String IdAjuste,Producto,Cantidad,CantReg,Lote,Estatus,FechaCrea;
    String Descripcion;
    String CantidadAConsultar;
    String[] TipoAjuste; //= {"ALTA POR INVENTARIO","ALTA POR DEVOLUCIÓN","PRUEBA"};
    String CodigoPallet, PalletRegistrados;
    Handler handler = new Handler();
    ArrayAdapter<String> spinnerArrayAdapter;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_ajustes__nuevo_empaque);
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_NuevoEmpaque.this);
        declaraVariables();
        agregaListeners();
        SegundoPlano sp = new SegundoPlano("ListarAjustes");
        sp.execute();


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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }
        if((id == R.id.borrar_datos))
        {
            reiniciaCampos();
            edtx_CodigoPallet.setText("");
            edtx_CodigoPallet.requestFocus();

        }

        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setTitle(" "+getString(R.string.Ajustes_nuevo_empaque));
       // toolbar.setSubtitle("  Agregar Empaque");
        toolbar.setLogo(R.mipmap.logo_axc);//  toolbar.setLogo(R.drawable.axc_logo_toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_CodigoPallet= (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_CodigoEmpaque);
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Cantidad.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtv_Producto= (TextView) findViewById(R.id.txtv_Producto);
        txtv_Lote= (TextView) findViewById(R.id.txtv_Lote);
        txtv_Cantidad= (TextView) findViewById(R.id.txtv_CantPend);
        txtv_Empaques= (TextView) findViewById(R.id.txtv_Empaque);


        spnr_Ajuste= (Spinner) findViewById(R.id.spnr_Ajuste);


        // Spinner spinner = new Spinner(this);



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
                    if(edtx_CodigoPallet.getText().toString().length()<10)
                    {
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet), "false", true, true);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoPallet.requestFocus();
                                edtx_CodigoPallet.setText("");
                            }
                        });
                        return false;
                    }

                    if(!edtx_CodigoPallet.getText().toString().equals(""))
                    {
                       SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                       sp.execute();

                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),"Advertencia",true,true);
                        edtx_CodigoPallet.setText("");
                        edtx_CodigoPallet.requestFocus();
                    }

                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);

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
                    if(!edtx_CodigoEmpaque.getText().toString().equals(""))
                    {

                /*        if(!edtx_CodigoEmpaque.getText().toString().equals(""))
                        {


                        }else
                        {
                            new popUpGenerico(contexto,vista,"Debes capturar un código de empaque valido","Advertencia",true,true);
                            edtx_Cantidad.setText("");
                            edtx_CodigoEmpaque.setText("");
                            edtx_Cantidad.requestFocus();
                        }
                */    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"Advertencia",true,true);
                        edtx_Cantidad.setText("");
                        edtx_CodigoEmpaque.setText("");
                        edtx_Cantidad.requestFocus();
                    }
                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);


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
                    if(!edtx_Cantidad.getText().toString().equals(""))
                    {

                        SegundoPlano sp = new SegundoPlano("AjusteNuevoEmpaquePalletExistente");
                        sp.execute();
/*
                        if(!edtx_Cantidad.getText().toString().equals(""))
                        {

                        }else
                        {
                            new popUpGenerico(contexto,vista,"Debes capturar una cantidad valida","Advertencia",true,true);
                            edtx_Cantidad.setText("");
                            edtx_CodigoEmpaque.setText("");
                            edtx_Cantidad.requestFocus();
                        }
*/
                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_cantidad),"Advertencia",true,true);
                        edtx_Cantidad.setText("");
                        edtx_CodigoEmpaque.setText("");
                        edtx_Cantidad.requestFocus();
                    }

                    new esconderTeclado(Almacen_Ajustes_NuevoEmpaque.this);
                }
                return false;
            }
        });

    }
    private void reiniciaCampos()
    {
        edtx_Cantidad.setText("");
        edtx_CodigoEmpaque.setText("");
        edtx_CodigoPallet.setText("");
        edtx_CodigoEmpaque.requestFocus();
        txtv_Empaques.setText("");
        txtv_Lote.setText("");
        txtv_Cantidad.setText("");
        txtv_Empaques.setText("");
        txtv_Producto.setText("");
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

            String CodigoPallet = edtx_CodigoPallet.getText().toString();

            switch (tarea)
            {

                case "ConsultaPallet":
                    sa.SOAPConsultaPallet(CodigoPallet,contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();
                    break;

                case "ListarAjustes"://Se usa para llenar el Spinner
                    sa.SOAPListarConceptosAjuste("1",contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();

                    break;
                case "AjusteNuevoEmpaquePalletExistente":
                    String Cantidad = edtx_Cantidad.getText().toString();
                    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                    String TipoEvento = spnr_Ajuste.getSelectedItem().toString();
                    String Revision ="";
                    sa.SOAPAjusteNuevoEmpaquePalletExistente(CodigoEmpaque,CodigoPallet,Producto,Cantidad,Revision,TipoEvento,contexto);

                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();
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

                        txtv_Producto.setText(Producto);
                        txtv_Lote.setText(Lote);
                        txtv_Cantidad.setText(Cantidad);
                        txtv_Empaques.setText(CantReg);

                        break;
                    case "ListarAjustes":
                        spinnerArrayAdapter = new ArrayAdapter<String>
                                (Almacen_Ajustes_NuevoEmpaque.this, android.R.layout.simple_spinner_item,
                                        TipoAjuste);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spnr_Ajuste.setAdapter(spinnerArrayAdapter);
                        break;
                    case "AjusteNuevoEmpaquePalletExistente":
                        new popUpGenerico(contexto, vista, "Empaque registrado con exito.","Registrado", true, true);
                        reiniciaCampos();
                        break;
                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
                reiniciaCampos();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    public void sacaDatos(String Tarea)
    {
        SoapObject tabla = sa.parser();
        if (tabla == null) tabla = sa.getTablaSoap();

        if (Tarea.equals("ListarAjustes")) TipoAjuste = new String[tabla.getPropertyCount()];


        if (tabla != null)


            switch (Tarea)
            {
                case "ListarAjustes":
                    for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        try
                        {


                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());
                            TipoAjuste[i] = tabla1.getPrimitivePropertyAsString("Descripcion");
                        } catch (Exception e)
                        {

                            e.printStackTrace();
                        }
                    }
                    break;

                case "ConsultaPallet":
                    Producto = tabla.getPrimitivePropertyAsString("NumParte");
                    Lote = tabla.getPrimitivePropertyAsString("LoteAXC");
                    Cantidad = tabla.getPrimitivePropertyAsString("CantidadActual");
                    CantReg = tabla.getPrimitivePropertyAsString("Empaques");

                    break;
            }


    }
}