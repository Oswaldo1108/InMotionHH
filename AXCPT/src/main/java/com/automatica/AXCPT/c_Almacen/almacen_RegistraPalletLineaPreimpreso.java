package com.automatica.AXCPT.c_Almacen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;

public class almacen_RegistraPalletLineaPreimpreso extends AppCompatActivity
{

    EditText OrdenProduccion, PrimerEmpaque, UltimoEmpaque, CantidadEmpaques;

    String  prmOrden,prmCierre,prmLinea,prmPrimerEmpaque,
            prmUltimoEmpaque,prmCantidadEmpaques,prmEstacion,prmUsuario;
    Context contexto = this;
    View vista;
    Switch Conforme, Evento;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_registra_pallet_linea_preimpreso);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Registro de Pallet");
//        toolbar.setLogo(R.mipmap.logo_axc);//toolbar.setLogo(R.drawable.axc_logo_toolbar);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, almacen_RegistraPalletLineaPreimpreso.this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);


      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        Conforme = (Switch) findViewById(R.id.swt_Conforme);
        Conforme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(Conforme.isChecked()==true)
                {
                    Evento.setChecked(false);
                }

            }
        });
        Evento = (Switch) findViewById(R.id.swt_Evento);
        Evento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(Evento.isChecked()==true)
                {
                    Conforme.setChecked(false);
                }
            }
        });

        OrdenProduccion = (EditText) findViewById(R.id.edtx_Orden);
        OrdenProduccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        PrimerEmpaque = (EditText) findViewById(R.id.edtx_PrimerEmpaque);
        PrimerEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        UltimoEmpaque = (EditText) findViewById(R.id.edtx_UltimoEmpaque);
        UltimoEmpaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    CantidadEmpaques.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                return false;
            }
        });
        UltimoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        CantidadEmpaques = (EditText) findViewById(R.id.edtx_CantidadEmpaques);
        CantidadEmpaques.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        CantidadEmpaques.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    escuchaSwitch();
                    SegundoPlanoSOAP s = new SegundoPlanoSOAP();
                    s.execute();
                }
                return false;
            }
        });

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ((id == R.id.borrar_datos)) {

            reiniciaCampos();
          //  Toast.makeText( this, "Campos reiniciados.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }
        return super.onOptionsItemSelected(item);
    }

    public void reiniciaCampos()
    {

        OrdenProduccion.setText("");
        PrimerEmpaque.setText("");
        UltimoEmpaque.setText("");
        CantidadEmpaques.setText("");
        OrdenProduccion.requestFocus();
    }



    public void escuchaSwitch()
    {


    }
    private class SegundoPlanoSOAP extends AsyncTask<Void,Void,Void> {
    //  String URL = "http://192.168.1.181/AXCMaulec/wsAXCMP.asmx";
        SoapAction sa = new SoapAction();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(almacen_RegistraPalletLineaPreimpreso.this);
        String estado, mensaje;
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            prmOrden = OrdenProduccion.getText().toString();
            prmPrimerEmpaque = PrimerEmpaque.getText().toString();
            prmUltimoEmpaque = UltimoEmpaque.getText().toString();
            prmCantidadEmpaques = CantidadEmpaques.getText().toString();
            prmCierre = "0";
            prmLinea = "LINEA1";
            prmEstacion = pref.getString("estacion","E102");
            prmUsuario = pref.getString("usuario","1937");
            sa.SOAPregistraPalletLineaImpreso(prmOrden,prmCierre,prmLinea,prmPrimerEmpaque,
                    prmUltimoEmpaque,prmCantidadEmpaques,contexto);
            estado = sa.getDecision();
            mensaje = sa.getMensaje();
            return null;
        }

        protected void onPostExecute(Void aVoid) {

            popUpGenerico pg = new popUpGenerico(contexto,vista,mensaje,estado ,true,true);

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}
