package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido;

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

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

public class Surtido_Surtido_Pallet  extends AppCompatActivity {
    Toolbar toolbar;
    EditText edtx_Pallet, edtx_ConfirmarPallet;
    TextView txtv_Pedido, txtv_Producto, txtv_CantPend, txtv_CantReg, txtv_SugPallet, txtv_SugLote, txtv_SugPosicion,txtv_TipoPallet, txtv_ConsProd, txtv_ConsLote, txtv_ConsCant, txtv_ConsEstatus, txtv_SugEmpaque,txtv_ConsUM;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    Context contexto = this;
    String Pedido, Partida, NumParte, UM, CantidadTotal, CantidadPendiente, CantidadSurtida, Linea;
    Handler handler = new Handler();
    Bundle b;
    boolean recargar;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surtido_surtido_pallet);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, this);
        SacaExtrasIntent();
        declararVariables();
        agregaListeners();

        edtx_Pallet.requestFocus();

    }
    @Override
    protected void onResume() {
        Recarga();

        super.onResume();
    }
    private void Recarga()
    {
        if(!txtv_Pedido.getText().toString().equals("-"))
        {
            new SegundoPlano("ConsultaPedidoDet").execute();
        }
    }
    private void declararVariables()
    {
        try {


            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido));
            getSupportActionBar().setSubtitle("Por pallet");
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            edtx_Pallet = (EditText) findViewById(R.id.edtx_Pallet);
            edtx_ConfirmarPallet = (EditText) findViewById(R.id.edtx_ConfirmarPallet);

            edtx_Pallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_ConfirmarPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            txtv_Pedido = (TextView) findViewById(R.id.txtv_Pedido);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_CantPend = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_CantReg = (TextView) findViewById(R.id.txtv_CantidadReg);
            txtv_SugPallet = (TextView) findViewById(R.id.txtv_Pallet);
            txtv_SugLote = (TextView) findViewById(R.id.txtv_Lote);
            txtv_SugPosicion = (TextView) findViewById(R.id.txtv_Posicion);
            txtv_TipoPallet = (TextView) findViewById(R.id.txtv_TipoPallet);

            txtv_ConsProd = (TextView) findViewById(R.id.txtv_Empaque_Producto);
            txtv_ConsLote = (TextView) findViewById(R.id.txtv_Empaque_Lote);
            txtv_ConsCant = (TextView) findViewById(R.id.txtv_Empaque_Cantidad);
            txtv_ConsEstatus = (TextView) findViewById(R.id.txtv_Estatus);
            txtv_ConsUM = (TextView) findViewById(R.id.txtv_Empaque_UM);

            txtv_SugEmpaque = (TextView) findViewById(R.id.txtv_SugEmpaque);

            txtv_Pedido.setText(Pedido);
            txtv_Producto.setText(NumParte);
            txtv_CantPend.setText(CantidadPendiente);
            txtv_CantReg.setText(CantidadSurtida);

        }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
    }
    private void SacaExtrasIntent()
    {
        try
            {

                b = getIntent().getExtras();
                Pedido= b.getString("Pedido");
                Partida= b.getString("Partida");
                NumParte= b.getString("NumParte");
                UM= b.getString("UM");
                CantidadTotal= b.getString("CantidadTotal");
                CantidadPendiente= b.getString("CantidadPendiente");
                CantidadSurtida= b.getString("CantidadSurtida");
                Linea= b.getString("Linea");

                Log.e("SoapResponse", "SacaExtrasIntent: ");



            }catch (Exception e)
            {
                Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
            }

    }
    private void agregaListeners()
    {
        edtx_ConfirmarPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {

                        if(edtx_Pallet.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Pallet.setText("");
                                    edtx_Pallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet) ,"false" ,true , true);
                            return false;
                        }
                        if(edtx_ConfirmarPallet.getText().toString().equals(""))
                            {
                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_ConfirmarPallet.setText("");
                                        edtx_ConfirmarPallet.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet_confirmacion) ,"false" ,true , true);
                            }

                        if(!edtx_Pallet.getText().toString().equals(edtx_ConfirmarPallet.getText().toString()))
                            {
                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Pallet.setText("");
                                        edtx_Pallet.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.pallets_no_coinciden) ,false ,true , true);
                                return false;
                            }

                        new SegundoPlano("RegistrarPallet").execute();


                        new esconderTeclado(Surtido_Surtido_Pallet.this);
                        return true;
                    }
                return false;
            }
        });




        edtx_Pallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_Pallet.getText().toString().equals(""))
                            {
                                new SegundoPlano("ConsultaPallet").execute();
                            }else
                            {
                                new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_ingrese_pallet) ,"false" ,true , true);

                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Pallet.setText("");
                                        edtx_Pallet.requestFocus();

                                    }
                                });
                            }
                        new esconderTeclado(Surtido_Surtido_Pallet.this);
                        return  true;
                    }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
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
        try {


            int id = item.getItemId();

            if (!recargar) {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, null);
                    }
                if ((id == R.id.recargar))
                    {

                        Recarga();
                    }

            }
        }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
        return super.onOptionsItemSelected(item);
    }
    private void ReiniciarVariables(String tarea)
    {
        switch (tarea)
            {
                case "ConsultaOrdenProduccion":
                    edtx_ConfirmarPallet.setText("");
                    edtx_ConfirmarPallet.requestFocus();

                    break;
                case "ConsultarTarima":
                    edtx_Pallet.setText("");
                    edtx_Pallet.requestFocus();
                    break;
                case "ConsultaPallet":
                    edtx_Pallet.setText("");
                    txtv_ConsProd.setText("");
                    txtv_ConsCant.setText("");
                    txtv_ConsEstatus.setText("");
                    txtv_ConsUM.setText("");
                    txtv_ConsLote.setText("");
                    edtx_Pallet.requestFocus();
                    break;
                case "RegistrarPallet":
                    txtv_ConsProd.setText("");
                    txtv_ConsEstatus.setText("");
                    txtv_ConsCant.setText("");
                    txtv_ConsLote.setText("");
                    txtv_ConsUM.setText("");
                    edtx_Pallet.setText("");
                    edtx_ConfirmarPallet.setText("");
                    edtx_Pallet.requestFocus();
                    break;
                case "Reiniciar":
                    edtx_Pallet.setText("");
                    edtx_ConfirmarPallet.setText("");
                    edtx_Pallet.requestFocus();
                    break;
            }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        View LastView;

        DataAccessObject dao;
        cAccesoADatos_Embarques cadEmb = new cAccesoADatos_Embarques(Surtido_Surtido_Pallet.this);
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
            try
                {
                    LastView = getCurrentFocus();

                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressBarHolder.requestFocus();
                            recargar = true;
                        }
                    },1);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }


        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {

                    switch (tarea)
                        {
                            case "ConsultaPedidoDet":
                                dao = cadEmb.cad_ConsultaSurtidoDetPartida(Pedido,Partida);
                                break;

                            case "SugierePallet":
                                dao = cadEmb.cad_ConsultaPalletSugerido(Pedido,Partida);
                                break;

                            case "ConsultaPallet":
                                dao = cadEmb.cad_ConsultaPalletSurtido(Pedido,Partida,edtx_Pallet.getText().toString());
                                break;

                            case "RegistrarPallet":
                                dao = cadEmb.cad_RegistroPalletSurtido(Pedido,Partida,edtx_ConfirmarPallet.getText().toString());
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

                    if(LastView!=null)
                        {
                            LastView.requestFocus();
                        }

                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {

                                    case "ConsultaPedidoDet":
                                        txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));
                                        txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                                        new SegundoPlano("SugierePallet").execute();
                                        break;

                                    case "SugierePallet":

                                        txtv_SugPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_SugLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                        txtv_SugPosicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                                        txtv_SugEmpaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                        txtv_TipoPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("TipoReg"));

                                        break;

                                    case "ConsultaPallet":

                                        txtv_ConsProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        txtv_ConsLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                        txtv_ConsCant.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_ConsEstatus.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                        txtv_ConsUM.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UM"));

                                        handler.postDelayed(new Runnable()
                                        {
                                            @Override
                                            public void run() {
                                                edtx_ConfirmarPallet.requestFocus();

                                            }
                                        },1);
                                        break;
                                    case "RegistrarPallet":
//                                        txtv_CantPend.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
//                                        txtv_CantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadSurtida"));
                                        ReiniciarVariables(tarea);
                                        new SegundoPlano("ConsultaPedidoDet").execute();
                                        break;
                                }
                        }

                    else
                        {

                            ReiniciarVariables(tarea);
                            if(dao.getcMensaje().contains("partida no válido"))
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), "Partida completada con exíto.", "true", true, true);
                                }
                            else
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), "false", true, true);

                                }
                        }
                }catch (Exception e)
                {
                    new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), "false", true, true);
                    e.printStackTrace();
                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = false;
        }
    }
}
