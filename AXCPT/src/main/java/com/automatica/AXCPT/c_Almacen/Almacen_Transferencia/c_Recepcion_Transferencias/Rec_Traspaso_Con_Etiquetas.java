package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias;

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
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

public class Rec_Traspaso_Con_Etiquetas extends AppCompatActivity
{
    //region variables
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    Handler h = new Handler();
    View vista;
    Context contexto = this;
    EditText edtx_Pallet,edtx_ConfirmaPallet;
    TextView txtv_Producto,txtv_Empaques,txtv_Contenido,txtv_Lote,txtv_Estatus;
    String Transferencia,Partida;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_transferencia_surtir_pallet_con_etiquetas_v2);
        try
        {
            declaraVariables();
            agregaListeners();
            Bundle b = getIntent().getExtras();
            Transferencia = b.getString("Transferencia");
            Partida =  b.getString("Partida");
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
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
        try
        {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }

        if((id == R.id.recargar))
        {
            if(!edtx_Pallet.getText().toString().equals(""))
            {
                new SegundoPlano("ConsultaPallet").execute();
            }
        }
        if((id == R.id.borrar_datos))
        {
            reiniciarCampos();
        }
    }catch (Exception e)
    {
        new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
    }

        return super.onOptionsItemSelected(item);
    }

    private void declaraVariables()
    {
        try
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(getString(R.string.Transferencia_Recibo_Pallet));
            this.getSupportActionBar().setSubtitle("Etiquetado");
             new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Rec_Traspaso_Con_Etiquetas.this);
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            edtx_Pallet= (EditText)findViewById(R.id.edtx_CodigoPallet);
            edtx_Pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);
            txtv_Contenido = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_Lote = (TextView)findViewById(R.id.txtv_FechaCaducidad);
            txtv_Estatus = (TextView) findViewById(R.id.txtv_Estatus);
            edtx_ConfirmaPallet = (EditText)findViewById(R.id.edtx_ConfirmaPallet);
            edtx_ConfirmaPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }

    }
    private void agregaListeners()
    {
        try
        {
            edtx_Pallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_Pallet.getText().toString().equals(""))
                        {
                            SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                            sp.execute();
                            new esconderTeclado(Rec_Traspaso_Con_Etiquetas.this);
                        }
                        else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet),"false", true, true);
                            new esconderTeclado(Rec_Traspaso_Con_Etiquetas.this);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    reiniciarCampos();
                                    edtx_Pallet.requestFocus();
                                }
                            });
                        }

                        return true;
                    }
                    return false;

                }

            });
            edtx_ConfirmaPallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_Pallet.getText().toString().equals(""))
                        {
                            if (edtx_Pallet.getText().toString().equals(edtx_ConfirmaPallet.getText().toString()))
                            {
                                new SegundoPlano("ConfirmaPallet").execute();
                                new esconderTeclado(Rec_Traspaso_Con_Etiquetas.this);
                            }
                            else
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallets_no_coinciden),"false", true, true);
                                new esconderTeclado(Rec_Traspaso_Con_Etiquetas.this);
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        reiniciarCampos();
                                        edtx_Pallet.requestFocus();
                                    }
                                });
                            }
                        }
                        else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet),"false", true, true);
                            new esconderTeclado(Rec_Traspaso_Con_Etiquetas.this);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    reiniciarCampos();
                                    edtx_Pallet.requestFocus();
                                }
                            });
                        }

                        return true;
                    }
                    return false;

                }

            });

        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }


    }

    private void reiniciarCampos()
    {
        try
        {
            edtx_ConfirmaPallet.setText("");
            txtv_Producto.setText("");
            txtv_Contenido.setText("");
            txtv_Empaques.setText("");
            txtv_Estatus.setText("");
            txtv_Lote.setText("");

            edtx_Pallet.setText("");
            edtx_Pallet.requestFocus();
        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }

    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Rec_Traspaso_Con_Etiquetas.this);
        cAccesoADatos_Consultas cadcons = new cAccesoADatos_Consultas(Rec_Traspaso_Con_Etiquetas.this);

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

            //    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
            try
            {
                switch (tarea)
                {

                    case "ConsultaPallet":

//                        sa.SOAPConsultaPallet(edtx_Pallet.getText().toString(),contexto);
                        dao = cadcons.c_ConsultarPalletPT(edtx_Pallet.getText().toString());

                        break;

                    case "ConfirmaPallet" :

//                        sa.SOAPReciboPalletTraspasoConEtiquetas(Transferencia,Partida,edtx_ConfirmaPallet.getText().toString(),contexto);
                        dao = cad.cad_RegistroTraspasoPalletConEtiquetas(Transferencia,Partida,edtx_ConfirmaPallet.getText().toString());

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
                        txtv_Empaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                        txtv_Contenido.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                        txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                        txtv_Estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                        edtx_ConfirmaPallet.requestFocus();
                        break;
                    case "ConfirmaPallet":
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.envio_pallet_registro_exito),dao.iscEstado(), true, true);
                        reiniciarCampos();
                        break;

                }
            }
            else
            {
                new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                reiniciarCampos();
                edtx_Pallet.requestFocus();
            }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(),false, true, true);
                reiniciarCampos();
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}
