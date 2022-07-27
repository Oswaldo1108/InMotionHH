package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.OLD;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Almacen_Transferencia_SurtirPalletNE extends AppCompatActivity
{


    //region variables
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    Handler h = new Handler();
    EditText edtx_CodigoPallet,edtx_CantidadEmpaques;
    TextView txtv_Producto,txtv_DescProd,txtv_Cantidad,txtv_Lote,txtv_PalletSug,txtv_LoteSug,txtv_PosicionSug,txtv_EmpaquesDisp;
    Button btn_CerrarPallet;
    String Transferencia,Partida,Pallet;
    boolean Recarga;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_transferencia_surtir_pallet_ne);
        declaraVariables();
        agregaListeners();
        Bundle b = getIntent().getExtras();
        Transferencia = b.getString("Transferencia");
        Partida= b.getString("Partida");
    }

    @Override
    protected void onResume()
    {
        if(Transferencia!=null)
        {
            SegundoPlano sp = new SegundoPlano("SugerenciaPallet");
            sp.execute();
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
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)



    {
        int id = item.getItemId();

        if(!Recarga)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, getCurrentFocus());
            }

            if ((id == R.id.recargar)) {
                if (!edtx_CantidadEmpaques.getText().toString().equals("")) {
                    SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                    sp.execute();
                }
            }
            if ((id == R.id.borrar_datos)) {
                reiniciarCampos();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void declaraVariables()
    {
        try
        {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.Transferencia_Envio_Pallet_NE));
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Transferencia_SurtirPalletNE.this);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_CodigoPallet= (EditText) findViewById(R.id.edtx_CodigoEmpaque);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CantidadEmpaques= (EditText)findViewById(R.id.edtx_ConfirmaEmpaque);
        edtx_CantidadEmpaques.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_DescProd = (TextView) findViewById(R.id.txtv_Producto3);
        txtv_Cantidad= (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_EmpaquesDisp= (TextView) findViewById(R.id.txtv_EmpaquesDisp);
        txtv_Lote = (TextView) findViewById(R.id.txtv_FechaCaducidad);
        txtv_PalletSug = (TextView) findViewById(R.id.txtv_Pallet);
        txtv_PosicionSug= (TextView) findViewById(R.id.txtv_Posicion);
        txtv_LoteSug= (TextView) findViewById(R.id.txtv_Lote);
        btn_CerrarPallet = (Button) findViewById(R.id.btn_CerrarPallet);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }


    }
    private void agregaListeners()
    {
        try {
            edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            new SegundoPlano("ConsultaPallet").execute();
                            new esconderTeclado(Almacen_Transferencia_SurtirPalletNE.this);

                            edtx_CantidadEmpaques.requestFocus();
                        } else {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), false, true, true);
                        }

                        return true;
                    }
                    return false;

                }

            });
            edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {

                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {

                        if (edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), false, true, true);
                            new esconderTeclado(Almacen_Transferencia_SurtirPalletNE.this);
                            reiniciarCampos();
                            return false;
                        }

                        if (edtx_CantidadEmpaques.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), false, true, true);
                            new esconderTeclado(Almacen_Transferencia_SurtirPalletNE.this);
                            reiniciarCampos();
                            return false;
                        }
                        new SegundoPlano("ConfirmaPallet").execute();
                        new esconderTeclado(Almacen_Transferencia_SurtirPalletNE.this);
                        return true;
                    }
                    return false;

                }

            });

            btn_CerrarPallet.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    new CreaDialogos("¿Cerrar pallet?", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            new SegundoPlano("CerrarPallet").execute();
                        }
                    },null,contexto);
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }
    }

    private void reiniciarCampos()
    {
        try
        {
        edtx_CantidadEmpaques.setText("");
        txtv_Producto.setText("");
        txtv_DescProd.setText("");
        txtv_Cantidad.setText("");
        txtv_EmpaquesDisp.setText("");
        txtv_Lote.setText("");
        edtx_CodigoPallet.setText("");
        edtx_CodigoPallet.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }
    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Almacen_Transferencia_SurtirPalletNE.this);
        cAccesoADatos_Consultas cadcons = new cAccesoADatos_Consultas(Almacen_Transferencia_SurtirPalletNE.this);

        View LastView;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            Recarga = true;
            if(getCurrentFocus()!=null)
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        LastView = getCurrentFocus();
                        progressBarHolder.requestFocus();
                    }
                },1);
            }
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
//                        sa.SOAPConsultaEmpaqueRegular(edtx_CodigoPallet.getText().toString(), contexto);
                        dao = cad.c_ConsultarPalletTraspaso_NE(Transferencia,Partida,edtx_CodigoPallet.getText().toString());

                        break;

                    case "ConfirmaPallet":
//                        sa.SOAPRegistrarEmpaqueTraspasoEnvio(Transferencia, Partida, edtx_CantidadEmpaques.getText().toString(), contexto);
                        dao = cad.cad_RegistroTraspasoEmpaqueEnvio_NE(Transferencia,Partida,edtx_CodigoPallet.getText().toString(),edtx_CantidadEmpaques.getText().toString());

                        break;
                    case "SugerenciaPallet":
//                        sa.SOAPSugiereEmpaqueTraspasoEnvio(Transferencia,Partida,contexto);
                        dao = cad.cad_ConsultaPalletSugeridoTraspasoEnvio_NE(Transferencia,Partida);

                        break;
                    case "CerrarPallet":
//                        sa.SOAPCierraPalletTraspasoEnvio(Transferencia, contexto);
                        dao = cad.cad_CierrePalletTraspasoEnvio(Transferencia);

                        break;


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
                    if (LastView != null)
                        {
                            h.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    LastView.requestFocus();
                                }
                            }, 1);
                        }
                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {
                                    case "ConsultaPallet":

                                        txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Producto"));
                                        txtv_DescProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                                        txtv_EmpaquesDisp.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteAXC"));

                                        edtx_CantidadEmpaques.requestFocus();
                                        break;
                                    case "ConfirmaPallet":

                                        if (Transferencia != null)
                                            {
                                                new SegundoPlano("SugerenciaPallet").execute();
                                            }

                                        reiniciarCampos();
                                        edtx_CodigoPallet.requestFocus();
                                        break;
                                    case "SugerenciaPallet":
                                        txtv_PalletSug.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_LoteSug.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteAXC"));
                                        txtv_PosicionSug.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPosicion"));
                                    break;

                                    case "CerrarPallet":
                                        new popUpGenerico(contexto, getCurrentFocus(), "Pallet [" + Pallet + "] cerrado con éxito.", dao.iscEstado(), true, true);
                                        reiniciarCampos();
                                        edtx_CodigoPallet.requestFocus();
                                        break;

                                }
                        } else
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            reiniciarCampos();

                            if (tarea.equals("SugerenciaPallet"))
                                {
                                    txtv_LoteSug.setText("");
                                    txtv_PalletSug.setText("");
                                    txtv_PosicionSug.setText("");
                                }
                        }
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);
                    Recarga = false;
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);

                }
        }
    }
}
