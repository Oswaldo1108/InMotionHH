package com.automatica.AXCMP.c_Inventarios;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

public class Inventarios_CiclosPosicionReg extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    EditText edtx_Pallet,edtx_Lote,edtx_ConfirmarPallet;
    EditText edtx_Producto, edtx_Desc;
    EditText edtx_Unidades,edtx_Empaques;
    TextView txtv_Producto,txtv_Desc,txtv_Unidades,txtv_Empaques;
    Button btnEmpaques,btnConfirmar;
    CheckBox chk_Editar;

    String Pallet,Producto, Descripcion,Unidades,Empaques,Lote,CodigoEmpaque,EmpaquesRegistrados;
    String UbicacionIntent,IdInventario;
    Bundle b;
    String TipoAccion = "ConfirmarPallet";
    String Revision = "";

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    Handler handler = new Handler();

    int ContadorClicksCheckBox =0;
    View vista;
    Context contexto = this;
//endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarios_activity_ciclos_posicion_reg);
        new cambiaColorStatusBar(contexto, R.color.RojoStd,Inventarios_CiclosPosicionReg.this);
        DeclararVariables();
        SacaExtrasIntent();
        AgregaListeners();
        if(Pallet!=null&&!Pallet.equals(""))
        {
            edtx_Pallet.setText(Pallet);
            SegundoPlano se = new SegundoPlano("ConsultaPallet");
            se.execute();

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

            edtx_Pallet.requestFocus();
            reiniciaVariables();
        }



        return super.onOptionsItemSelected(item);
    }

    private void reiniciaVariables()
    {
        edtx_Pallet.setText("");
        edtx_ConfirmarPallet.setText("");
        edtx_Pallet.setText("");
        edtx_Producto.setText("");
        edtx_Desc.setText("");
        edtx_Lote.setText("" );
        edtx_Unidades.setText("");
        edtx_Empaques.setText("");
        edtx_ConfirmarPallet.setText("");
        btnConfirmar.setEnabled(false);
        btnEmpaques.setEnabled(false);

    }
    private void DeclararVariables()
    {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.confirma_pallet));
        //toolbar.setSubtitle("  Confirmar Pallet");
        toolbar.setLogo(R.mipmap.logo_axc);//   toolbar.setLogo(R.drawable.axc_logo_toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_Pallet = (EditText) findViewById(R.id.txtv_Pallet);
        edtx_Lote = (EditText) findViewById(R.id.edtx_Lote_Empaque);
        edtx_ConfirmarPallet = (EditText) findViewById(R.id.edtx_ConfirmarPallet);
        edtx_Lote.setEnabled(false);

        edtx_Producto = (EditText) findViewById(R.id.edtx_Producto);
        edtx_Desc = (EditText) findViewById(R.id.edtx_Desc);
        edtx_Unidades= (EditText) findViewById(R.id.edtx_Unidades);
        edtx_Empaques = (EditText) findViewById(R.id.edtx_Empaques);

        edtx_Producto.setEnabled(false);
        edtx_Desc.setEnabled(false);
        edtx_Unidades.setEnabled(false);
        edtx_Empaques.setEnabled(false);


        edtx_ConfirmarPallet.setEnabled(false);
        edtx_Pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Lote.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_ConfirmarPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Desc.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
/*

        txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
        txtv_Desc = (TextView) findViewById(R.id.txtv_Desc);
        txtv_Unidades = (TextView) findViewById(R.id.txtv_Unidades);

        txtv_Empaques= (TextView) findViewById(R.id.txtv_Empaques);
*/

        btnEmpaques = (Button) findViewById(R.id.btn_Empaques);
        btnConfirmar= (Button) findViewById(R.id.btn_Confirmar);

        chk_Editar = (CheckBox) findViewById(R.id.chkbx_Editar);

        btnEmpaques.setEnabled(false);
        btnConfirmar.setEnabled(false);
    }

    private void SacaExtrasIntent()
    {

        b = getIntent().getExtras();
        UbicacionIntent = b.getString("UbicacionIntent");
        IdInventario = b.getString("IdInventario");
        Pallet = b.getString("CodigoPallet");
    }
    private void AgregaListeners()
    {

        edtx_Pallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Pallet.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                        sp.execute();
                    }else
                    {

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                            edtx_Pallet.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),"false",true,true);
                    }

                    new esconderTeclado(Inventarios_CiclosPosicionReg.this);
                }
                return false;
            }
        });
        edtx_Desc.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Desc.getText().toString().equals(""))
                    {

                        if(ContadorClicksCheckBox==2)
                        {
                            btnConfirmar.setEnabled(true);
                            btnEmpaques.setEnabled(true);
                        }else
                        {
                            edtx_ConfirmarPallet.requestFocus();
                        }
                    }else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Desc.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_lote),"Advertencia",true,true);

                    }
                    new esconderTeclado(Inventarios_CiclosPosicionReg.this);
                }
                return false;
            }
        });
        edtx_ConfirmarPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_Pallet.getText().toString().equals(""))
                    {
                        if(edtx_Pallet.getText().toString().equals(edtx_ConfirmarPallet.getText().toString()))
                        {
                            SegundoPlano sp = new SegundoPlano(TipoAccion);
                            sp.execute();
                        }else {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run() {
                                    edtx_ConfirmarPallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto,vista,getString(R.string.pallets_no_coinciden),"Advertencia",true,true);
                        }
                    }else {

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run() {
                                edtx_ConfirmarPallet.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),"Advertencia",true,true);
                    }

                    new esconderTeclado(Inventarios_CiclosPosicionReg.this);
                }
                return false;
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                edtx_ConfirmarPallet.setEnabled(true);
                edtx_ConfirmarPallet.requestFocus();
                btnEmpaques.setEnabled(false);
            }
        });
        btnEmpaques.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Inventarios_CiclosPosicionReg.this,Inventarios_ConfirmarEmpaque.class);
              //  b.putString("");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        chk_Editar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if(isChecked)
                {
                    TipoAccion = "PalletEditado";
                    edtx_Desc.setEnabled(true);
                    edtx_Desc.requestFocus();
                    edtx_ConfirmarPallet.setEnabled(true);
                    btnConfirmar.setEnabled(false);
                    btnEmpaques.setEnabled(false);
                    ContadorClicksCheckBox++;

                }
                if(!isChecked)
                {
                    TipoAccion = "ConfirmarPallet";
                    edtx_Lote.setEnabled(false);
                    edtx_ConfirmarPallet.setEnabled(false);
                    btnConfirmar.setEnabled(true);
                    btnEmpaques.setEnabled(true);
                    ContadorClicksCheckBox++;
                    edtx_ConfirmarPallet.requestFocus();
                }
                if(ContadorClicksCheckBox ==2)
                {

                    edtx_Lote.setEnabled(true);
                    edtx_ConfirmarPallet.setEnabled(true);
                    btnConfirmar.setEnabled(false);
                    btnEmpaques.setEnabled(false);
                    chk_Editar.setEnabled(true);
                    edtx_Pallet.requestFocus();
                    edtx_Pallet.setText("");
                    edtx_Producto.setText("");
                    edtx_Desc.setText("");
                    edtx_Lote.setText("" );
                    edtx_Unidades.setText("");
                    edtx_Empaques.setText("");
                    edtx_ConfirmarPallet.setText("");
                    ContadorClicksCheckBox++;
                }
                if(ContadorClicksCheckBox ==4)
                {

                    edtx_Lote.setEnabled(false);
                    edtx_ConfirmarPallet.setEnabled(false);
                    btnConfirmar.setEnabled(true);
                    btnEmpaques.setEnabled(true);
                    chk_Editar.setEnabled(true);
                    edtx_Pallet.requestFocus();
                    edtx_Pallet.setText(Pallet);
                    edtx_Producto.setText(Producto);
                    edtx_Desc.setText(Descripcion);
                    edtx_Lote.setText(Lote);
                    edtx_Unidades.setText(Unidades);
                    edtx_Empaques.setText(Empaques);
                    edtx_ConfirmarPallet.setText("");
                    ContadorClicksCheckBox=0;
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
            Pallet = edtx_Pallet.getText().toString();
          /*
            Cantidad = edtx_Cantidad.getText().toString();
            Lote =*/
            switch (tarea)
            {
                case "ConsultaPallet":
                    try {
                        sa.SOAPBuscaInfoPallet(IdInventario, Pallet, contexto);
                        decision = sa.getDecision();
                        mensaje = sa.getMensaje();
                    }catch (Exception e )
                    {
                        decision = "false";
                        mensaje = e.getMessage();
                    }
                    break;
                case "ConfirmarPallet":
                    if(edtx_Pallet.getText().toString().equals(edtx_ConfirmarPallet.getText().toString()))
                    {
                        try {
                        sa.SOAPRegistraPalletNormal(IdInventario, Pallet, UbicacionIntent, contexto);
                            decision = sa.getDecision();
                            mensaje = sa.getMensaje();
                             }catch (Exception e )
                                {
                                    decision = "false";
                                    mensaje = e.getMessage();

                                }
                    }else
                        {
                            decision = "false";
                            mensaje = "Los codigos de Pallet no coinciden";
                         }

                    break;
                case "PalletEditado":
                    if(edtx_Pallet.getText().toString().equals(edtx_ConfirmarPallet.getText().toString()))
                    {
                        try{
                        sa.SOAPEditarPalletRegistroInventario(IdInventario, Pallet, UbicacionIntent, edtx_Desc.getText().toString(), contexto);
                            decision = sa.getDecision();
                            mensaje = sa.getMensaje();
                        }catch (Exception e )
                        {
                            decision = "false";
                            mensaje = e.getMessage();
                        }
                    }else
                    {
                        decision = "false";
                        mensaje = "Los codigos de Pallet no coinciden";
                    }
                    break;
            }



            if(decision.equals("true")&&tarea.equals("ConsultaPallet"))
            {
                sacaDatos();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            if(decision.equals("true")&&tarea.equals("ConsultaPallet"))
            {
                edtx_Producto.setText(Producto);
                edtx_Desc.setText(Descripcion);
                edtx_Unidades.setText(Unidades);
                edtx_Empaques.setText(Empaques);
                edtx_Lote.setText(Lote);

            btnEmpaques.setEnabled(true);
            btnConfirmar.setEnabled(true);
            }
            if(decision.equals("true")&&(tarea.equals("ConfirmarPallet")||tarea.equals("PalletEditado")))
            {
                new popUpGenerico(contexto, vista,getString(R.string.registro_exito),"Registrado", true, true);
                reiniciaVariables();
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
                if(mensaje.equals("Los codigos de Pallet no coinciden"))
                {
                    edtx_ConfirmarPallet.setText("");
                }
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }


    private void sacaDatos() {
        SoapObject tabla = sa.parser();



        for (int i = 0; i < tabla.getPropertyCount(); i++) {
            try {

                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse", tabla1.toString());

                Producto = tabla1.getPrimitivePropertyAsString("NumeroParte");
                Descripcion= tabla1.getPrimitivePropertyAsString("Revision");
                Unidades= tabla1.getPrimitivePropertyAsString("Unidades");
                Empaques = tabla1.getPrimitivePropertyAsString("Empaques");
                Lote= tabla1.getPrimitivePropertyAsString("Lote");
                if(Lote.equals("anyType{}"))Lote ="";
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}
