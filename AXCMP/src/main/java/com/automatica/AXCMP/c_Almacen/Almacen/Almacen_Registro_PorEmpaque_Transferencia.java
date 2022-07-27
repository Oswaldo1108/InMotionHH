package com.automatica.AXCMP.c_Almacen.Almacen;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;
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

public class Almacen_Registro_PorEmpaque_Transferencia extends AppCompatActivity
{

    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_OrdenCompra, edtx_EmpxPallet, edtx_Cantidad,edtx_CodigoEmpaque;
    String  Cantidad,CodigoEmpaque;
    Button btnCerrarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    TextView txtv_EmpaquesRegistrados, txtv_Caducidad,txtv_Partida,txtv_UM,txtv_CantidadOriginal,txtv_CantidadRegistrada,txtv_Producto,txtv_Pallet;
    Bundle b;

    ArrayList<String> ArrayLotes= new ArrayList<>();
    ArrayList<String> ArrayFechaCaducidad = new ArrayList<>();
    View vista;
    Context contexto = this;
    String OrdenCompra, FechaCaducidad,ModificaCant,PartidaERP,IdRecepcion,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet;
    String Estado,Texto,CodigoPallet,PartidaCerrada,OrdenCerrada,PalletCerrado;
    Spinner spnr_Ordenescompra;
    TextView txtv_OrdenCompra;

    String TipoRecepcion;
    Handler h = new Handler();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_oc_porproveedor);
            new cambiaColorStatusBar(contexto, R.color.VerdeStd, Almacen_Registro_PorEmpaque_Transferencia.this);
            SacaExtrasIntent();
            declararVariables();
            AgregaListeners();
            SegundoPlano sp = new SegundoPlano("ConsultaPallet");
            sp.execute();
            sp = new SegundoPlano("ListarOrdenesCompra");
            sp.execute();
            edtx_Cantidad.requestFocus();

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista,e.getMessage()+" "+e.getCause() ,"false" ,true,true);
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
        if((id == R.id.borrar_datos))
        {
            reiniciaVariables();
        }

        return super.onOptionsItemSelected(item);
    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(TipoRecepcion.equals("Proveedor"))
        {
            getSupportActionBar().setTitle(getString(R.string.recepcion_recepcion_por_empaque_proveedor));
        }else if(TipoRecepcion.equals("Maquila"))
        {
            getSupportActionBar().setTitle(getString(R.string.recepcion_recepcion_por_empaque_maquila));
        }else if(TipoRecepcion.equals("Traspaso"))
        {
            getSupportActionBar().setTitle(getString(R.string.recepcion_recepcion_por_empaque_traspasos));
        }
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        //  edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Factura);
        edtx_EmpxPallet = (EditText) findViewById(R.id.edtx_EmpxPallet);
        edtx_EmpxPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_Cantidad.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
        edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);

        txtv_Caducidad = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Partida= (TextView) findViewById(R.id.txtv_Partida);
        txtv_UM= (TextView) findViewById(R.id.txtv_Caducidad);
        txtv_Producto= (TextView) findViewById(R.id.txtv_Prod);
        txtv_CantidadOriginal= (TextView) findViewById(R.id.txtv_CantidadTotal);
        txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_Lote);
        txtv_Pallet= (TextView) findViewById(R.id.txtv_Pallet);
        txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpReg);
        txtv_OrdenCompra = (TextView) findViewById(R.id.txtv_OC);



        btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btnCerrarTarima.setEnabled(false);




        txtv_Caducidad.setText(FechaCaducidad);
        txtv_Partida.setText(PartidaERP);
        txtv_UM.setText(UM);
        txtv_Producto.setText(NumParte);
        txtv_CantidadOriginal.setText(CantidadTotal);
        txtv_CantidadRegistrada.setText(CantidadRecibida);
        txtv_OrdenCompra.setText(OrdenCompra);

        spnr_Ordenescompra = (Spinner) findViewById(R.id.spnr_OrdenesCompra);
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
    private void AgregaListeners()
    {

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
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_CodigoEmpaque.requestFocus();
                                }
                            });

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
                    if (!edtx_CodigoEmpaque.getText().toString().equals(""))
                    {

                        SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
                        sp.execute();
                        btnCerrarTarima.setEnabled(true);
                       /* edtx_CodigoEmpaque.setText("");
                        edtx_CodigoEmpaque.requestFocus();*/

                    }else {

                        new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_empaque) , "false", true, true);
                        h.post(new Runnable() {
                            @Override
                            public void run() {

                                edtx_CodigoEmpaque.setText("");
                                edtx_CodigoEmpaque.requestFocus();
                            }
                        });
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
                    new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

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

        spnr_Ordenescompra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
               txtv_Caducidad.setText(ArrayFechaCaducidad.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                txtv_Caducidad.setText("-");
            }
        });
    }
    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();

            OrdenCompra= b.getString("Orden");
            FechaCaducidad = b.getString("FechaCaducidad");
            ModificaCant= b.getString("ModificaCant");
            PartidaERP= b.getString("PartidaERP");
            IdRecepcion= b.getString("IdRecepcion");
            NumParte= b.getString("NumParte");
            UM= b.getString("UM");
            CantidadTotal= b.getString("CantidadTotal");
            CantidadRecibida= b.getString("CantidadRecibida");
            CantidadEmpaques= b.getString("CantidadEmpaques");
            EmpaquesPallet= b.getString("EmpaquesPallet");

            //Esta variable se usa durante el AsyncTask
            TipoRecepcion = b.getString("Tipo");


        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras, revisar variables " + e.getMessage());
        }

    }
    private void reiniciaVariables()
    {

        edtx_EmpxPallet .setText("");
        edtx_Cantidad .setText("");

        edtx_Cantidad.setText("");
        edtx_CodigoEmpaque .setText("");
        btnCerrarTarima.setEnabled(false);
        edtx_Cantidad.requestFocus();
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
            try {
                switch (tarea) {
                    case "ListarOrdenesCompra":
                        if (TipoRecepcion.equals("Proveedor")) {
                            sa.SOAPListarLotesOC(OrdenCompra, PartidaERP, NumParte, contexto);
                        } else if (TipoRecepcion.equals("Maquila")) {

                        }
                        break;

                    case "ConsultaPallet":
                        if (TipoRecepcion.equals("Proveedor")) {
                            sa.SOAPConsultaPalletAbiertoOC(IdRecepcion, PartidaERP, contexto);
                        } else if (TipoRecepcion.equals("Maquila")) {
                            //Aqui podria ser el mismo
                        }

                        break;
                    case "RegistrarEmpaqueNuevo":

                        CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                        Cantidad = edtx_Cantidad.getText().toString();
                        EmpaquesPallet = edtx_EmpxPallet.getText().toString();
                        //En la variable de lote se esta mandando el numero de factura ingresado por el usuario


                        if (TipoRecepcion.equals("Proveedor")) {
                            sa.SOAPRegistrarEmpaqueCompra(OrdenCompra, PartidaERP, CodigoEmpaque,/*Aqui va lote*/ spnr_Ordenescompra.getSelectedItem().toString(), Cantidad, EmpaquesPallet, contexto);
                        } else if (TipoRecepcion.equals("Maquila")) {

                        }
                        break;
                    case "RegistraPalletNuevo":

                        if (TipoRecepcion.equals("Proveedor")) {
                            sa.SOAPCierraPalletCompra(CodigoPallet, contexto);
                        } else if (TipoRecepcion.equals("Maquila")) {

                        }
                        break;

                    case "CompraPalletUnicoMP":
                        if (TipoRecepcion.equals("Proveedor")) {
                            sa.SOAPCompraPalletMPCompraUnico(OrdenCompra, PartidaERP, FechaCaducidad, Cantidad, EmpaquesPallet, contexto);
                        } else if (TipoRecepcion.equals("Maquila")) {

                        }
                        break;
                }

                mensaje = sa.getMensaje();
                decision = sa.getDecision();

                if (decision.equals("true") && !tarea.equals("ConsultaPallet")) {
                    sacaDatos(tarea);
                }
            }catch (Exception e)
            {
                mensaje = e.getMessage();
                decision= "false";
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

                    //    new popUpGenerico(contexto, vista,getString(R.string.empaque_baja_cuarentena_exito) ,decision, true, true);
                        txtv_EmpaquesRegistrados.setText(CantidadEmpaques);
                        txtv_Pallet.setText(CodigoPallet);
                        txtv_CantidadRegistrada.setText(CantidadRecibida);
                        edtx_CodigoEmpaque.setText("");
                        edtx_CodigoEmpaque.requestFocus();
                        new esconderTeclado(Almacen_Registro_PorEmpaque_Transferencia.this);
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
                        new popUpGenerico(contexto, vista,"Pallet"+"["+mensaje+"] Cerrado con éxito",decision, true, true);
                        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                        sp.execute();
                        break;
                    case "ListarOrdenesCompra":
                        spinnerAdapter= new ArrayAdapter<String>(
                                Almacen_Registro_PorEmpaque_Transferencia.this,
                                android.R.layout.simple_spinner_item,
                                ArrayLotes);
                        ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnr_Ordenescompra.setAdapter(spinnerAdapter);
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
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
        }

        }
    }
    public void sacaDatos(String tarea)
    {
        try {
            SoapObject tabla = sa.parser();

            for (int i = 0; i < tabla.getPropertyCount(); i++) {
                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
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
                        ArrayFechaCaducidad.add(tabla1.getPrimitivePropertyAsString("FechaCaducidad"));
                        //Log.d("SoapResponse", tabla1.getPrimitivePropertyAsString("LoteProveedor"));
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


            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        Almacen_Registro_PorEmpaque_Transferencia.this.finish();
        super.onBackPressed();
    }
}
