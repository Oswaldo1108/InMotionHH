package com.automatica.AXCMP.c_Almacen.Almacen;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class Almacen_Registro_PrimerasYUltimas_Transferencia extends AppCompatActivity
{

    //region variables
    SoapAction sa = new SoapAction();
    Handler handler = new Handler();
    EditText edtx_EmpxPallet, edtx_Cantidad, edtx_PrimerEmpaque,edtx_UltimoEmpaque,edtx_CantidadEmpaques;
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
    Spinner spnr_OrdenesCompra;

    TextView txtv_Caducidad;
    ArrayList<String> ArrayLotes= new ArrayList<>();
    ArrayList<String> ArrayCaducidades = new ArrayList<>();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity_registro_oc_primerasyultimas);

        new cambiaColorStatusBar(contexto,R.color.VerdeStd, Almacen_Registro_PrimerasYUltimas_Transferencia.this);
        SacaExtrasIntent();
        declararVariables();
        AgregaListeners();


        SegundoPlano sp = new SegundoPlano("ListarOrdenesCompra");
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
        //edtx_Factura.setText("");
        edtx_EmpxPallet .setText("");
        edtx_Cantidad .setText("");

        edtx_Cantidad.setText("");
        edtx_PrimerEmpaque.setText("");
      //  btnCerrarTarima.setEnabled(false);
     //   edtx_Factura.requestFocus();
    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.recepcion_primera_y_ultima));
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


    //    edtx_Factura = (EditText) findViewById(R.id.edtx_Factura);
        edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
        edtx_EmpxPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_Cantidad.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        edtx_PrimerEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
        edtx_PrimerEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CantidadEmpaques = (EditText) findViewById(R.id.edtx_CantidadEmpaques);
        edtx_CantidadEmpaques.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_UltimoEmpaque = (EditText) findViewById(R.id.edtx_UltimoEmpaque);
        edtx_UltimoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

     //   btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

  //      txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
        txtv_Partida= (TextView) findViewById(R.id.txtv_Partida);
        txtv_UM= (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
        txtv_CantidadOriginal= (TextView) findViewById(R.id.txtv_CantidadTotal);
        txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
        txtv_Pallet= (TextView) findViewById(R.id.txtv_Pallet);
        txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
        txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);
        txtv_Caducidad = (TextView) findViewById(R.id.txtv_Producto);
     //   edtx_Factura.setText(Factura);
        edtx_Cantidad.setText(CantidadEmpaques);
        edtx_EmpxPallet.setText(EmpaquesPallet);

        spnr_OrdenesCompra = (Spinner) findViewById(R.id.spnr_OrdenesCompra);


     /*   btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btnCerrarTarima.setEnabled(false);
*/




    //    txtv_OrdenCompra.setText(OrdenCompra);
        txtv_Partida.setText(PartidaERP);
        txtv_UM.setText(UM);
        txtv_Producto.setText(NumParte);
        txtv_CantidadOriginal.setText(CantidadTotal);
        txtv_CantidadRegistrada.setText(CantidadRecibida);
        txtv_OrdenCompra.setText(OrdenCompra);

        if(ModificaCant.equals("1"))
        {
            edtx_Cantidad.setEnabled(true);
            edtx_EmpxPallet.setEnabled(true);
        }else if(ModificaCant.equals("0"))
        {
            edtx_Cantidad.setEnabled(false);
            edtx_EmpxPallet.setEnabled(false);
        }

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
            e.printStackTrace();
        }

    }
    private void AgregaListeners()
    {

        edtx_EmpxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if (!edtx_EmpxPallet.getText().toString().equals(""))
                    {

                        if(Integer.parseInt(edtx_EmpxPallet.getText().toString())>999999)
                        {
                            new popUpGenerico(contexto, vista,getString(R.string.error_cantidad_mayor_999999),"false", true, true);
                            edtx_EmpxPallet.setText("");
                            edtx_EmpxPallet.requestFocus();
                        }else
                        {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_PrimerEmpaque.requestFocus();
                                }
                            });
                        }
                    }else
                    {
                        new popUpGenerico(contexto, vista,getString(R.string.error_ingrese_cantidad),"false", true, true);


                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_EmpxPallet.requestFocus();
                                edtx_EmpxPallet.setText("");
                            }
                        });

                    }
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
                    if (!edtx_Cantidad.getText().toString().equals(""))
                    {

                        if(Integer.parseInt(edtx_Cantidad.getText().toString())>999999)
                        {
                            new popUpGenerico(contexto, vista,getString(R.string.error_cantidad_mayor_999999),"Advertencia", true, true);

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Cantidad.setText("");
                                    edtx_Cantidad.requestFocus();
                                }
                            });

                        }else
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_EmpxPallet.requestFocus();
                                }
                            });

                        }
                    }else
                    {
                        new popUpGenerico(contexto, vista,"Ingrese la cantidad correcta","Advertencia", true, true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Cantidad.setText("");
                                edtx_Cantidad.requestFocus();
                            }
                        });

                    }

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
                    if (!edtx_PrimerEmpaque.getText().toString().equals(""))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_UltimoEmpaque.requestFocus();

                            }
                        });



                    }else
                        {

                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque), "false", true, true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_PrimerEmpaque.setText("");
                                edtx_PrimerEmpaque.requestFocus();
                            }
                        });
                    }
                }
                return false;
            }
        });


        edtx_UltimoEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if (!edtx_UltimoEmpaque.getText().toString().equals(""))
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CantidadEmpaques.requestFocus();

                            }
                        });



                    }else
                    {

                        new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque), "false", true, true);

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_UltimoEmpaque.setText("");
                                edtx_UltimoEmpaque.requestFocus();
                            }
                        });
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
                 validacionFinal();
                }
                return false;
            }
        });

        spnr_OrdenesCompra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                    txtv_Caducidad.setText(ArrayCaducidades.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }


    private void validacionFinal()
    {
        if(edtx_Cantidad.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad)+" - Cantidad", "false", true, true);
            return;
        }
        if(edtx_EmpxPallet.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad) +" - Empaquespor Pallet", "false", true, true);
            return;
        }
        if(edtx_PrimerEmpaque.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque) +" - Primer empaque", "false", true, true);
            return;
        }
        if(edtx_UltimoEmpaque.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque) +" - Ultimo empaque", "false", true, true);
            return;
        }
        if(edtx_CantidadEmpaques.getText().toString().equals(""))
        {
            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_cantidad) +" - Validación" +  " cantidad empaques", "false", true, true);
            return;
        }
        SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
        sp.execute();
    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        SpinnerAdapter spinnerAdapter;
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
            Lote =*/
                switch (tarea)
                {

                    case "ListarOrdenesCompra":

                            sa.SOAPListarLotesOC(OrdenCompra, PartidaERP, NumParte, contexto);

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

                if(decision.equals("true"))
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

            if(decision.equals("true"))
            {
                switch (tarea)
                {
                    case "RegistrarEmpaqueNuevo":

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
                        new esconderTeclado(Almacen_Registro_PrimerasYUltimas_Transferencia.this);
                        break;
                    case "CompraPalletUnicoMP":

                        new popUpGenerico(contexto, vista,getString(R.string.pallet_empaque_unico_exito) ,decision, true, true);
                        txtv_EmpaquesRegistrados.setText(CantidadEmpaques);
                        txtv_Pallet.setText(CodigoPallet);
                        txtv_CantidadRegistrada.setText(CantidadRecibida);
                        edtx_Cantidad.requestFocus();
                        break;
                    case "ConsultaPallet":
                        String[] arrayData = mensaje.split("#");
                        txtv_EmpaquesRegistrados.setText(arrayData[1]);
                        txtv_Pallet.setText(arrayData[0]);
                        break;
                    case "RegistraPalletNuevo":
                        new popUpGenerico(contexto, vista,"Pallet"+" ["+mensaje+"] Cerrado con éxito",decision, true, true);
                        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                        sp.execute();
                        break;
                    case "ListarOrdenesCompra":
                        spinnerAdapter= new ArrayAdapter<String>(
                                Almacen_Registro_PrimerasYUltimas_Transferencia.this,
                                android.R.layout.simple_spinner_item,
                                ArrayLotes);
                        ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_OrdenesCompra.setAdapter(spinnerAdapter);
                        txtv_CantidadOriginal.setText(CantidadTotal);
                        txtv_CantidadRegistrada.setText(CantidadRecibida);
                        break;
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




        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
            e.printStackTrace();
            e.getCause();
        }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

    }


    public void sacaDatos(String tarea)
    {

        SoapObject tabla = sa.parser();


       // arrayListPosiciones = new ArrayList<>();
//        Rec_RegistroOC.posiciones p;
    if(tabla!=null)
        for(int i = 0; i<tabla.getPropertyCount();i++)
        {
            try {

                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse",tabla1.toString());

                switch (tarea)
                {
                    case "RegistrarEmpaqueNuevo":

                        Log.d("SoapResponse", tabla1.toString());
                        Estado = tabla1.getPrimitivePropertyAsString("Estado");
                        Texto = tabla1.getPrimitivePropertyAsString("Texto");
                        CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                        PartidaCerrada = tabla1.getPrimitivePropertyAsString("PartidaCerrada");
                        OrdenCerrada = tabla1.getPrimitivePropertyAsString("OrdenCerrada");
                        CantidadEmpaques = tabla1.getPrimitivePropertyAsString("CantEmpaques");
                        CantidadRecibida = tabla1.getPrimitivePropertyAsString("CantRecibida");
                        PalletCerrado = tabla1.getPrimitivePropertyAsString("PalletCerrado");

                        break;

                    case "ListarOrdenesCompra":
                        Log.d("SoapResponse", "Listar    "+tabla1.toString());
                        ArrayLotes.add(tabla1.getPrimitivePropertyAsString("LoteProveedor"));
                        ArrayCaducidades.add(tabla1.getPrimitivePropertyAsString("FechaCaducidad"));

                        Log.d("SoapResponse", tabla1.getPrimitivePropertyAsString("LoteProveedor"));
                        break;
                    case "CompraPalletUnicoMP":
                        CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                        PartidaCerrada = tabla1.getPrimitivePropertyAsString("PartidaCerrada");
                        OrdenCerrada = tabla1.getPrimitivePropertyAsString("OrdenCerrada");
                        CantidadEmpaques = tabla1.getPrimitivePropertyAsString("CantEmpaques");
                        CantidadRecibida = tabla1.getPrimitivePropertyAsString("CantRecibida");
                        PalletCerrado = tabla1.getPrimitivePropertyAsString("PalletCerrado");
                        break;
                }

            }catch (Exception e)
            {

                e.printStackTrace();


            }
        }
    }

}
