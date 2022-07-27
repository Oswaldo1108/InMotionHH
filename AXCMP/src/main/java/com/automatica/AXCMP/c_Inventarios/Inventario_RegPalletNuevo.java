package com.automatica.AXCMP.c_Inventarios;

import android.app.DatePickerDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.DatePickerFragment;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;


public class Inventario_RegPalletNuevo extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_Pallet, edtx_Producto,edtx_Cantidad,edtx_Lote,edtx_CodigoEmpaque,edtx_FechaCad;
    String Pallet,Producto, Cantidad,Lote,CodigoEmpaque,EmpaquesRegistrados;
    Button btnCerrarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView txtv_EmpaquesRegistrados;
    Bundle b;
    String UbicacionIntent,IdInventario;
    DatePickerFragment newFragment;
    View vista;
    Context contexto = this;

    int ContadorEmpaquesRegistrados =0;
    //endregion
    Handler handler =  new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario_activity_reg_pallet_nuevo);
        declararVariables();
        AgregaListeners();
        SacaExtrasIntent();
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
        edtx_Pallet.setText("");
        edtx_Producto .setText("");
        edtx_Cantidad .setText("");
        edtx_Lote .setText("");
        edtx_Cantidad.setText("");
        edtx_CodigoEmpaque .setText("");
        btnCerrarTarima.setEnabled(false);
        edtx_Pallet.requestFocus();
    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Inventarios_PalletNuevo));
        //toolbar.setSubtitle("  Registro Pallet Nuevo");
        toolbar.setLogo(R.mipmap.logo_axc);//  toolbar.setLogo(R.drawable.axc_logo_toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Pallet = (EditText) findViewById(R.id.txtv_Pallet);
        edtx_Producto = (EditText) findViewById(R.id.txtv_Producto);
        edtx_Cantidad = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_Lote = (EditText) findViewById(R.id.edtx_Lote_Empaque);
        edtx_FechaCad = (EditText) findViewById(R.id.edtx_FechaCaducidad);
        edtx_Cantidad= (EditText) findViewById(R.id.edtx_Empaque);
        edtx_CodigoEmpaque = (EditText) findViewById(R.id.edtx_CodigoPallet);

        edtx_Pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Producto.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Cantidad.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Lote.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        btnCerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima) ;

        txtv_EmpaquesRegistrados = (TextView) findViewById(R.id.txtv_EmpaquesReg);

        btnCerrarTarima = findViewById(R.id.btn_CerrarTarima);
        btnCerrarTarima.setEnabled(true);

    }
    private void SacaExtrasIntent()
    {

        b = getIntent().getExtras();
        UbicacionIntent = b.getString("UbicacionIntent");
        IdInventario = b.getString("IdInventario");
    }
    private void AgregaListeners()
    {
        try {
            edtx_Pallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        if (!edtx_Pallet.getText().toString().equals("")) {

                            //   edtx_Producto.requestFocus();
                            handler.post(
                                    new Runnable() {
                                        public void run() {

                                            edtx_Producto.requestFocus();
                                        }
                                    }
                            );

                        } else {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet), "false", true, true);

                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_Pallet.setText("");
                                            edtx_Pallet.requestFocus();
                                        }
                                    }
                            );

                        }
                    }
                    return false;
                }
            });
            edtx_Producto.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (!edtx_Producto.getText().toString().equals("")/*||edtx_Producto.getText().toString().length()<5*/) {
                            SegundoPlano sp = new SegundoPlano("ConsultaProducto");
                            sp.execute();

                        } else {

                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_producto), "false", true, true);

                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_Producto.setText("");
                                            edtx_Producto.requestFocus();
                                        }
                                    }
                            );
                        }
                    }
                    return false;
                }
            });
            edtx_Cantidad.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (!edtx_Cantidad.getText().toString().equals("")) {
                            //Toast.makeText(Inventario_RegPalletNuevo.this,  "Ingrese una cantida correcta.", Toast.LENGTH_SHORT).show();

                            handler.post(
                                    new Runnable() {
                                        public void run() {

                                            edtx_Lote.requestFocus();
                                        }
                                    }
                            );
                        } else {
                            new popUpGenerico(contexto, vista, getString(R.string.ingrese_cantidad), "false", true, true);

                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_Cantidad.setText("");
                                            edtx_Cantidad.requestFocus();
                                        }
                                    }
                            );
                        }
                    }
                    return false;
                }
            });
            edtx_CodigoEmpaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (!edtx_CodigoEmpaque.getText().toString().equals("")/*||edtx_CodigoEmpaque.getText().toString().length()<5*/) {
                            //Toast.makeText(Inventario_RegPalletNuevo.this,  "Ingrese un codigo de empaque correcto.", Toast.LENGTH_SHORT).show();

                            SegundoPlano sp = new SegundoPlano("RegistrarEmpaqueNuevo");
                            sp.execute();

                        } else {
                            new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_empaque), "Advertencia", true, true);
                            edtx_CodigoEmpaque.setText("");
                            edtx_CodigoEmpaque.requestFocus();
                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_CodigoEmpaque.setText("");
                                            edtx_CodigoEmpaque.requestFocus();
                                        }
                                    }
                            );
                        }
                    }
                    return false;
                }
            });

            edtx_Lote.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (!edtx_Lote.getText().toString().equals("")) {

                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_CodigoEmpaque.requestFocus();
                                        }
                                    }
                            );

                        } else {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_lote), "false", true, true);

                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            edtx_Lote.setText("");
                                            edtx_Lote.requestFocus();
                                        }
                                    }
                            );
                        }
                    }
                    return false;
                }
            });


            edtx_FechaCad.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {

                    try {

                        if (event.getAction() == MotionEvent.ACTION_UP)
                            {
                                newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        final String selectedDate = day + "-" + (month + 1) + "-" + year;
                                        edtx_FechaCad.setText(selectedDate);





                                    }
                                });

                                newFragment.show(getSupportFragmentManager(), "datePicker");
                            }

                    } catch (Exception e) {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                    return false;
                }
            });


            btnCerrarTarima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (txtv_EmpaquesRegistrados.getText().toString().equals("-"))
                    {

                        handler.post(
                                new Runnable() {
                                    public void run() {
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_cerrar_pallet_sin_empaques), "false", true, true);
                                        edtx_Pallet.requestFocus();
                                    }
                                });
                        return;
                    }

                    SegundoPlano sp = new SegundoPlano("RegistraPalletNuevo");
                    sp.execute();
                }
            });
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }
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
            try {


          /*
            Cantidad = edtx_Cantidad.getText().toString();
            Lote =*/
                switch (tarea) {
                    case "ConsultaProducto":

                        Producto = edtx_Producto.getText().toString();
                        sa.SOAPConsultaExisteNumParte(Producto, contexto);
                        break;
                    case "RegistrarEmpaqueNuevo":
                        Pallet = edtx_Pallet.getText().toString();
                        CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                        Producto = edtx_Producto.getText().toString();
                        String Revision = "";
                        Cantidad = edtx_Cantidad.getText().toString();
                        sa.SOAPRegistrarEmpaqueNuevoPalletInventarioFechaCadLote(IdInventario,Pallet,CodigoEmpaque,Producto,Revision
                                ,Cantidad,UbicacionIntent,edtx_Lote.getText().toString(),edtx_FechaCad.getText().toString(),contexto);
                       // sa.SOAPRegistrarEmpaqueNuevoPalletInventario(IdInventario, Pallet, CodigoEmpaque, Producto, Revision, Cantidad, UbicacionIntent, contexto);
                        break;
                    case "RegistraPalletNuevo":

                        sa.SOAPRegistrarPalletNuevo(IdInventario, Pallet, contexto);
                        break;
                }

                mensaje = sa.getMensaje();
                decision = sa.getDecision();
            }catch (Exception e)
            {
                mensaje = e.getMessage();
                decision = "false";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if(decision.equals("true"))
                    {
                        switch (tarea)
                            {
                                case "ConsultaProducto":

                                    break;

                                case "RegistrarEmpaqueNuevo":

                                    ContadorEmpaquesRegistrados++;
                                    txtv_EmpaquesRegistrados.setText(String.valueOf(ContadorEmpaquesRegistrados));
                                    if(ContadorEmpaquesRegistrados>0)
                                        {
                                            btnCerrarTarima.setEnabled(true);
                                        }
                                    edtx_CodigoEmpaque.setText("");
                                    edtx_CodigoEmpaque.requestFocus();
                                    break;

                                case "RegistraPalletNuevo":
                                    edtx_Pallet.setText("");
                                    edtx_Producto.setText("");
                                    edtx_Cantidad.setText("");
                                    edtx_Lote.setText("");
                                    edtx_CodigoEmpaque.setText("");
                                    edtx_FechaCad.setText("");
                                    txtv_EmpaquesRegistrados.setText("");
                                    edtx_Pallet.requestFocus();
                                    new popUpGenerico(contexto, getCurrentFocus(),"Pallet [" + mensaje + "] cerrado con exito.",decision, true, true);
                                    break;
                            }


                    }
                if (decision.equals("false"))
                {
                    switch (tarea)
                        {
                            case "ConsultaProducto":
                                new popUpGenerico(contexto, getCurrentFocus(), mensaje,decision, true, true);

                                edtx_Producto.requestFocus();
                                edtx_Producto.setText("" );
                                break;

                            case "RegistrarEmpaqueNuevo":

                                new popUpGenerico(contexto, getCurrentFocus(), mensaje,decision, true, true);

                                edtx_CodigoEmpaque.setText("" );
                                edtx_CodigoEmpaque.requestFocus();
                                break;

                            case "RegistraPalletNuevo":

                                new popUpGenerico(contexto, getCurrentFocus(),mensaje,decision, true, true);
                                break;
                        }
                }



            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() ,"false" ,true,true );

            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}
