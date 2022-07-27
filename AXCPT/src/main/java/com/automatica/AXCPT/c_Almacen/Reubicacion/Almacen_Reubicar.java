package com.automatica.AXCPT.c_Almacen.Reubicacion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

public class Almacen_Reubicar extends AppCompatActivity
{
    cAccesoADatos_Almacen ca;
    TextView txtv_Producto, txtv_Empaques, txtv_Cantidad, txtv_Ubicacion,txtv_DescProd,txtv_Estatus;
    EditText edtx_CodigoPallet,edtx_NuevaUbicacion;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    boolean recargar = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_reubicar);
        declararVariables();
        ca= new cAccesoADatos_Almacen(Almacen_Reubicar.this);
        agregaListeners();
        edtx_CodigoPallet.requestFocus();
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Reubicar.this);
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
        if(!recargar)
            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, vista);
                    }
                if ((id == R.id.borrar_datos))
                    {
                        borrarDatos();
                    }
            }
        return super.onOptionsItemSelected(item);
    }
    private void borrarDatos()
    {
    try
        {


            edtx_NuevaUbicacion.setText("");
            edtx_CodigoPallet.setText("");
            txtv_Ubicacion.setText("");
            txtv_Empaques.setText("");
            txtv_Producto.setText("");
            txtv_Cantidad.setText("");
            edtx_CodigoPallet.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
        }
    }
    private void declararVariables()
    {
        try
            {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Reubicar));
        //toolbar.setSubtitle(" Reubicar Pallet");
//        toolbar.setLogo(R.mipmap.logo_axc);//      toolbar.setLogo(R.drawable.axc_logo_toolbar);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_NuevaUbicacion = (EditText) findViewById(R.id.edtx_NuevaUbicacion);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_NuevaUbicacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_DescProd = (TextView) findViewById(R.id.txtv_DescProd);
        txtv_Cantidad = (TextView) findViewById(R.id.txtv_CantPend);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);
        txtv_Estatus = (TextView) findViewById(R.id.txtv_Estatus);
        txtv_Ubicacion = (TextView) findViewById(R.id.txtv_Ubicacion);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
            }

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
                }
                else
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            edtx_CodigoPallet.setText("");
                            edtx_CodigoPallet.requestFocus();
                        }
                    });
                    new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_pallet),"false", true, true);
                }
                new esconderTeclado(Almacen_Reubicar.this);
            }
            return false;
        }
    });
        edtx_NuevaUbicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_pallet),"false", true, true);
                        return false;
                    }


                    if(!edtx_NuevaUbicacion.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("RegistroNuevaUbicacion");
                        sp.execute();

                    }
                    else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_NuevaUbicacion.setText("");
                                edtx_NuevaUbicacion.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_ubicacion),"false", true, true);
                    }

                    new esconderTeclado(Almacen_Reubicar.this);
                }
                return false;
            }
        });
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            recargar = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    switch (tarea)
                        {
                            case "ConsultaPallet":
                                //sa.SOAPConsultaPallet(CodigoPallet, contexto);
                                dao= ca.c_ConsultarPalletPT(edtx_CodigoPallet.getText().toString());
                                break;
                            case "RegistroNuevaUbicacion":
                                //sa.SOAPReubicarEmbalaje(CodigoPallet, NuevaUbicacion, contexto);
                                dao = ca.c_ReubicarEmbalaje(edtx_CodigoPallet.getText().toString(), edtx_NuevaUbicacion.getText().toString());
                                break;
                            default:
                                dao = new DataAccessObject();
                        }
                }catch (Exception e)
                {
                    dao = new DataAccessObject(e);
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
                {
                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {
                                    case "ConsultaPallet":
                                        txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        txtv_DescProd.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                                        txtv_Empaques.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                        txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_Ubicacion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Ubicacion"));
                                        txtv_Estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));

                                        break;
                                    case "RegistroNuevaUbicacion":
                                        borrarDatos();
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_reubicado_exito), String.valueOf(dao.iscEstado()), true, true);
                                        break;

                                }
                        }else
                        {
                            borrarDatos();
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), "Advertencia", true, true);

                        }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico( contexto,getCurrentFocus(),e.getMessage() ,"false" , true, true);
                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = false;
        }
    }

}
