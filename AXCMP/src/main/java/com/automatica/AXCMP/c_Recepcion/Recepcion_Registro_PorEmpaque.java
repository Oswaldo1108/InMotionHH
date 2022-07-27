package com.automatica.AXCMP.c_Recepcion;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Recepcion_Registro_PorEmpaque extends AppCompatActivity
{

    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_Factura, edtx_EmpxPallet, edtx_Cantidad,edtx_CodigoEmpaque;
    String Pallet,Producto, Cantidad,Lote,CodigoEmpaque,EmpaquesRegistrados;
    Button btnCerrarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_EmpaquesRegistrados, txtv_OrdenCompra,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet;
    Bundle b;
    String UbicacionIntent,IdInventario;
    View vista;
    Context contexto = this;
    String OrdenCompra,Factura,ModificaCant,PartidaERP,IdRecepcion,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    String Estado,Texto,CodigoPallet,PartidaCerrada,OrdenCerrada,PalletCerrado;
    int palletsRegistrados;
    Handler h = new Handler();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity_registro_oc);
        SacaExtrasIntent();
        declararVariables();
        AgregaListeners();
        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
        sp.execute();
        edtx_Cantidad.requestFocus();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);
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
        if((id == R.id.borrar_datos))
        {

            reiniciaVariables();
        }

        return super.onOptionsItemSelected(item);
    }
    private void reiniciaVariables()
    {
        edtx_Factura.setText("");
        edtx_EmpxPallet .setText("");
        edtx_Cantidad .setText("");

        edtx_Cantidad.setText("");
        edtx_CodigoEmpaque .setText("");
        btnCerrarTarima.setEnabled(false);
        edtx_Factura.requestFocus();
    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Factura = (EditText) findViewById(R.id.edtx_Factura);
        edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);


        btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

        txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Partida= (TextView) findViewById(R.id.txtv_Partida);
        txtv_UM= (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
        txtv_CantidadOriginal= (TextView) findViewById(R.id.txtv_CantidadTotal);
        txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
        txtv_Pallet= (TextView) findViewById(R.id.txtv_Pallet);
        txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);




        btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btnCerrarTarima.setEnabled(false);



        edtx_Factura.setText(Factura);
        txtv_OrdenCompra.setText(OrdenCompra);
        txtv_Partida.setText(PartidaERP);
        txtv_UM.setText(UM);
        txtv_Producto.setText(NumParte);
        txtv_CantidadOriginal.setText(CantidadTotal);
        txtv_CantidadRegistrada.setText(CantidadRecibida);

        if(ModificaCant.equals("1"))
        {
            edtx_Cantidad.setEnabled(true);
            edtx_EmpxPallet.setEnabled(true);
        }else if(ModificaCant.equals("0"))
        {
            edtx_Cantidad.setEnabled(false);
            edtx_EmpxPallet.setEnabled(false);
        }

       /* if(Cantidad.equals("0"))
        {
            txtv_EmpaquesRegistrados.setText("");

        }*/
    }
    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();

            OrdenCompra= b.getString("Orden");
            Factura= b.getString("FechaCaducidad");
            ModificaCant= b.getString("ModificaCant");
            PartidaERP= b.getString("PartidaERP");
            IdRecepcion= b.getString("IdRecepcion");
            NumParte= b.getString("NumParte");
            UM= b.getString("UM");
            CantidadTotal= b.getString("CantidadTotal");
            CantidadRecibida= b.getString("CantidadRecibida");
            CantidadEmpaques= b.getString("CantidadEmpaques");
            EmpaquesPallet= b.getString("EmpaquesPallet");


        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras, revisar variables " + e.getMessage());
        }

    }
    private void AgregaListeners()
    {
        edtx_Factura.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER)) {
                    if (!edtx_Factura.getText().toString().equals(""))
                    {
                        edtx_Cantidad.requestFocus();
                    } else
                        {
                        Toast.makeText(Recepcion_Registro_PorEmpaque.this, getString(R.string.error_ingrese_factura), Toast.LENGTH_SHORT).show();

                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Factura.setText("");
                                edtx_Factura.requestFocus();
                            }
                        });

                    }
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
                    if (!edtx_Cantidad.getText().toString().equals(""))
                    {

                        if(!(Integer.parseInt(edtx_Cantidad.getText().toString())>999999))
                        {
                            edtx_EmpxPallet.requestFocus();
                        }else
                        {
                            new popUpGenerico(contexto, vista,getString(R.string.error_cantidad_mayor_999999),"Advertencia", true, true);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();
                                }
                            });

                        }
                    }else
                    {
                        new popUpGenerico(contexto, vista,getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);

                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {  edtx_Cantidad.setText("");
                                edtx_Cantidad.requestFocus();

                            }
                        });
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
                    if (!edtx_EmpxPallet.getText().toString().equals(""))
                    {

                        if(!(Integer.parseInt(edtx_EmpxPallet.getText().toString())>999999))
                        {
                           edtx_CodigoEmpaque.requestFocus();
                        }else
                        {
                            new popUpGenerico(contexto, vista,getString(R.string.error_cantidad_mayor_999999),"Advertencia", true, true);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_EmpxPallet.setText("");
                                    edtx_EmpxPallet.requestFocus();
                                }
                            });
                        }
                    }else
                    {
                        new popUpGenerico(contexto, vista,getString(R.string.error_ingrese_cantidad),"Advertencia", true, true);
                        edtx_EmpxPallet.setText("");
                        edtx_EmpxPallet.requestFocus();
                    }
                    edtx_EmpxPallet.requestFocus();
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
                    if (edtx_CodigoEmpaque.getText().toString().equals("")|| edtx_Factura.getText().toString().length()<10)
                    {

                        SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
                        sp.execute();
                        btnCerrarTarima.setEnabled(true);
                       /* edtx_CodigoEmpaque.setText("");
                        edtx_CodigoEmpaque.requestFocus();*/

                    }else {
                        Toast.makeText(Recepcion_Registro_PorEmpaque.this,  "Ingrese un codigo de empaque correcto.", Toast.LENGTH_SHORT).show();
                        edtx_CodigoEmpaque.setText("");
                        edtx_CodigoEmpaque.requestFocus();
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


                if(txtv_EmpaquesRegistrados.getText().toString().equals("1"))
                {
                    new AlertDialog.Builder(contexto).setIcon(android.R.drawable.ic_dialog_alert)

                            .setTitle("¿Crear miaxc_consulta_pallet con un solo empaque?").setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    SegundoPlano sp = null;
                                    sp = new SegundoPlano("CompraPalletUnicoMP");
                                    sp.execute();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }else
                {

                    SegundoPlano sp = null;
                    sp = new SegundoPlano("RegistraPalletNuevo");
                    sp.execute();

                }


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
          /*
            Cantidad = edtx_Cantidad.getText().toString();
            LoteCantidadRegistrada =*/
            switch (tarea)
            {
                case "ConsultaPallet":
                    sa.SOAPConsultaPalletAbiertoOC(IdRecepcion,PartidaERP,contexto);
                    break;
                case "RegistrarEmpaqueNuevo":

                    CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                    Cantidad = edtx_Cantidad.getText().toString();
                    EmpaquesPallet = edtx_EmpxPallet.getText().toString();
                    sa.SOAPRegistrarEmpaqueCompra(OrdenCompra,PartidaERP,CodigoEmpaque,Factura,Cantidad,EmpaquesPallet, contexto);
                    break;
                case "RegistraPalletNuevo":

                    sa.SOAPCierraPalletCompra(CodigoPallet,contexto);
                    break;

                case "CompraPalletUnicoMP":
                    sa.SOAPCompraPalletMPCompraUnico(OrdenCompra,PartidaERP,Factura,Cantidad,EmpaquesPallet,contexto);
                    break;
            }

            mensaje = sa.getMensaje();
            decision = sa.getDecision();

            if(decision.equals("true")&&tarea.equals("RegistrarEmpaqueNuevo"))
            {
                sacaDatos();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

        try
        {
            if(decision.equals("true")&&(tarea.equals("RegistrarEmpaqueNuevo")||tarea.equals("CompraPalletUnicoMP")))
            {
                new popUpGenerico(contexto, vista,mensaje ,decision, true, true);
                txtv_EmpaquesRegistrados.setText(CantidadEmpaques);
                txtv_Pallet.setText(CodigoPallet);
                txtv_CantidadRegistrada.setText(CantidadRecibida);
                edtx_Cantidad.requestFocus();
            }
            if(decision.equals("true")&&tarea.equals("ConsultaPallet"))
            {
                try
                {
                    String[] arrayData = mensaje.split("#");
                    txtv_EmpaquesRegistrados.setText(arrayData[1]);
                    txtv_Pallet.setText(arrayData[0]);
                }catch (Exception e)
                {
                    new popUpGenerico(contexto, vista,"AsyncTask: Error en la deserialización del mensaje","Advertencia", true, true);
                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
            }

            if(decision.equals("true")&&tarea.equals("RegistraPalletNuevo"))
            {
                new popUpGenerico(contexto, vista,"Pallet"+"["+mensaje+"] Cerrado con éxito","Verificado", true, true);

            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
        }

        }
    }


    public void sacaDatos()
    {
        SoapObject tabla = sa.parser();


       // arrayListPosiciones = new ArrayList<>();
//        Rec_RegistroOC.posiciones p;

        for(int i = 0; i<tabla.getPropertyCount();i++)
        {
            try {

                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse",tabla1.toString());
                //String Estado,Texto,CodigoPallet,PartidaCerrada,OrdenCerrada,PalletCerrado;
                Estado=  tabla1.getPrimitivePropertyAsString("Estado");
                Texto = tabla1.getPrimitivePropertyAsString("Texto");
                CodigoPallet= tabla1.getPrimitivePropertyAsString("CodigoPallet");
                PartidaCerrada= tabla1.getPrimitivePropertyAsString("PartidaCerrada");
                OrdenCerrada= tabla1.getPrimitivePropertyAsString("OrdenCerrada");
                CantidadEmpaques= tabla1.getPrimitivePropertyAsString("CantEmpaques");

                CantidadRecibida= tabla1.getPrimitivePropertyAsString("CantRecibida");

                PalletCerrado= tabla1.getPrimitivePropertyAsString("PalletCerrado");
              //  p= new Rec_RegistroOC.posiciones(PartidaERP,NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,Muestreo,Estatus);

            //    arrayListPosiciones.add(p);




            }catch (Exception e)
            {

                e.printStackTrace();

                //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
            }
        }
    }

}
