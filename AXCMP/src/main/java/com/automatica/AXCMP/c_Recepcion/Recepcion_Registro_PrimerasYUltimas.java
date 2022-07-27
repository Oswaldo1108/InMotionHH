package com.automatica.AXCMP.c_Recepcion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Recepcion_Registro_PrimerasYUltimas extends AppCompatActivity
{

    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_Factura, edtx_EmpxPallet, edtx_Cantidad, edtx_PrimerEmpaque,edtx_UltimoEmpaque,edtx_CantidadEmpaques;
    String Pallet,Producto, Cantidad,Lote,CodigoEmpaque,EmpaquesRegistrados;

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
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity_registro_oc_primerasyultimas);
        SacaExtrasIntent();
        declararVariables();
        AgregaListeners();
        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
        sp.execute();
        edtx_PrimerEmpaque.requestFocus();
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
            Toast.makeText( this, "Campos Reiniciados", Toast.LENGTH_SHORT).show();
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
        edtx_PrimerEmpaque.setText("");
      //  btnCerrarTarima.setEnabled(false);
        edtx_Factura.requestFocus();
    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.recepcion_OC));
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Factura = (EditText) findViewById(R.id.edtx_Factura);
        edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_PrimerEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
        edtx_CantidadEmpaques = (EditText) findViewById(R.id.edtx_CantidadEmpaques);
        edtx_UltimoEmpaque = (EditText) findViewById(R.id.edtx_UltimoEmpaque);

     //   btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

        txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Partida= (TextView) findViewById(R.id.txtv_Partida);
        txtv_UM= (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
        txtv_CantidadOriginal= (TextView) findViewById(R.id.txtv_CantidadTotal);
        txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
        txtv_Pallet= (TextView) findViewById(R.id.txtv_Pallet);
        txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
        edtx_Factura.setText(Factura);
        edtx_Cantidad.setText(CantidadEmpaques);
        edtx_EmpxPallet.setText(EmpaquesPallet);




     /*   btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btnCerrarTarima.setEnabled(false);
*/




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

            OrdenCompra= b.getString("edtx_OrdenCompra");
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
                    if (edtx_Factura.getText().toString().equals("") || edtx_Factura.getText().toString().length() < 5) {
                        Toast.makeText(Recepcion_Registro_PrimerasYUltimas.this, "Ingrese un codigo de miaxc_consulta_pallet correcto.", Toast.LENGTH_SHORT).show();
                        edtx_Factura.setText("");
                        edtx_Factura.requestFocus();
                    } else {


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
                {/*
                    if (!edtx_EmpxPallet.getText().toString().equals(""))
                    {

                        if(Integer.parseInt(edtx_EmpxPallet.getText().toString())>999999)
                        {
                            new popUpGenerico(contexto, vista,"La cantidad ingresada excede el valor mayor permitido. [999999]","Advertencia", true, true);
                            edtx_EmpxPallet.setText("");
                            edtx_EmpxPallet.requestFocus();
                        }else
                        {

                        }
                    }else
                    {
                        new popUpGenerico(contexto, vista,"Ingrese la cantidad correcta","Advertencia", true, true);
                        edtx_EmpxPallet.setText("");
                        edtx_EmpxPallet.requestFocus();
                    }*/
                    edtx_EmpxPallet.requestFocus();
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
                    /*if (!edtx_Cantidad.getText().toString().equals(""))
                    {

                        if(Integer.parseInt(edtx_Cantidad.getText().toString())>999999)
                        {
                            new popUpGenerico(contexto, vista,"La cantidad ingresada excede el valor mayor permitido. [999999]","Advertencia", true, true);
                            edtx_Cantidad.setText("");
                            edtx_Cantidad.requestFocus();
                        }else
                        {
                           edtx_EmpxPallet.requestFocus();
                        }
                    }else
                    {
                        new popUpGenerico(contexto, vista,"Ingrese la cantidad correcta","Advertencia", true, true);
                        edtx_Cantidad.setText("");
                        edtx_Cantidad.requestFocus();
                    }*/
                    edtx_EmpxPallet.requestFocus();
                }
                return false;
            }
        });
        edtx_PrimerEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if (edtx_PrimerEmpaque.getText().toString().equals("")|| edtx_Factura.getText().toString().length()<10)
                    {

                       /* SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
                        sp.execute();*/
                      //  btnCerrarTarima.setEnabled(true);
                       /* edtx_PrimerEmpaque.setText("");
                        edtx_PrimerEmpaque.requestFocus();*/

                    }else {
                        Toast.makeText(Recepcion_Registro_PrimerasYUltimas.this,  "Ingrese un codigo de empaque correcto.", Toast.LENGTH_SHORT).show();
                        edtx_PrimerEmpaque.setText("");
                        edtx_PrimerEmpaque.requestFocus();
                    }
                }
                return false;
            }
        });

        edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
                    sp.execute();
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
            try
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

                    //    CodigoEmpaque = edtx_PrimerEmpaque.getText().toString();
                        Cantidad = edtx_Cantidad.getText().toString();
                        EmpaquesPallet = edtx_EmpxPallet.getText().toString();
                        String PrimerEmpaque = edtx_PrimerEmpaque.getText().toString();
                        String UltimoEmpaque= edtx_UltimoEmpaque.getText().toString();
                        String CantidadEmpaques = edtx_CantidadEmpaques.getText().toString();
                        String LoteProveedor="";//En la variable de lote se esta mandando el numero de factura ingresado por el usuario

                        sa.SOAPRegistraMPPrimerasYUltimas(OrdenCompra,PartidaERP,Factura,Cantidad,EmpaquesPallet,PrimerEmpaque,UltimoEmpaque,CantidadEmpaques,contexto);
                        break;
                    case "RegistraPalletNuevo":

                        sa.SOAPCierraPalletCompra(CodigoPallet,contexto);
                        break;
                }

                mensaje = sa.getMensaje();
                decision = sa.getDecision();

                if(decision.equals("true")&&tarea.equals("RegistrarEmpaqueNuevo"))
                {
                    sacaDatos(tarea);
                }
            }catch (Exception e)
            {
                decision = "false";
                mensaje = e.getMessage();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

        try
        {
            if(decision.equals("true")&&tarea.equals("RegistrarEmpaqueNuevo"))
            {
                new popUpGenerico(contexto, vista,mensaje,decision, true, true);
//                txtv_EmpaquesRegistrados.setText(CantidadEmpaques);
//                txtv_Pallet.setText(CodigoPallet);
                if(PalletCerrado.equals("1"))
                {
                    SegundoPlano sp = new SegundoPlano("RegistraPalletNuevo");
                    sp.execute();
                }
                txtv_CantidadRegistrada.setText(CantidadRecibida);
                edtx_PrimerEmpaque.setText("");
                edtx_UltimoEmpaque.setText("");
                edtx_CantidadEmpaques.setText("");
                edtx_PrimerEmpaque.requestFocus();
            }
            if(decision.equals("true")&&tarea.equals("ConsultaPallet"))
            {
                try
                {
                   /* String[] arrayData = mensaje.split("#");
                    txtv_EmpaquesRegistrados.setText(arrayData[1]);
                    txtv_Pallet.setText(arrayData[0]);*/

                }catch (Exception e)
                {
                    new popUpGenerico(contexto, vista,"AsyncTask: Error en la deserialización del mensaje","Advertencia", true, true);
                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
                edtx_PrimerEmpaque.setText("");
                edtx_UltimoEmpaque.setText("");
                edtx_CantidadEmpaques.setText("");
                edtx_PrimerEmpaque.requestFocus();
            }

            if(decision.equals("true")&&tarea.equals("RegistraPalletNuevo"))
            {
                new popUpGenerico(contexto, vista,"Pallet"+"["+mensaje+"] Cerrado con exito","Verificado", true, true);

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


    public void sacaDatos(String tarea)
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



                switch (tarea)
                {
                    case "ConsultaPallet":
                        Estado = tabla1.getPrimitivePropertyAsString("Estado");
                        Texto =  tabla1.getPrimitivePropertyAsString("Texto");
                        break;
                }

            }catch (Exception e)
            {

                e.printStackTrace();


            }
        }
    }

}
