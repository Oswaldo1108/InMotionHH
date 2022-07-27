package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_AjustePallet;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.frgmnt_Cierre_Orden;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_RegistroPT;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.fragment.app.Fragment;

public class Almacen_Armado_Pallets_Ne extends AppCompatActivity implements frgmnt_Cierre_Orden.OnFragmentInteractionListener
{

    Spinner spnr_Maquinas;
    String str_Maquina = "@";
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_OrdenProduccion;
    Button btn_CerrarTarima, btn_CancelarTarima;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    DatePickerFragment newFragment;
    String EmpaqueBaja;
    Handler handler = new Handler();
    int palletRegistradosVar = 0;
    TextView txtv_Producto, txtv_Cantidad, txtv_Lote, txtv_CantidadEmpaques, txtv_CantidadRegistrada,txtv_PalletAbierto;
    boolean recargar;
    private static final String frgmnt_tag = "tag";
    int cantidadEmpaques = 0;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        try {


            if (estado) {
                recargar = true;
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);
            } else {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
                recargar = false;
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
        }
        return false;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        try
            {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_almacen__armado__pallets__ne);
            new cambiaColorStatusBar(contexto, R.color.VerdeStd, Almacen_Armado_Pallets_Ne.this);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            declararVariables();
            agregaListeners();
                String Doc= null;
                try {
                    Doc = getIntent().getStringExtra("Documento");
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (Doc!=null){
                    edtx_OrdenProduccion.setText(Doc);
                }
            new SegundoPlano("ConsultaMaquinas").execute();

        }catch(Exception e){
            e.printStackTrace();
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (!TextUtils.isEmpty(edtx_OrdenProduccion.getText())){
            new SegundoPlano("ConsultaOrdenProduccion").execute();
        }
    }


    private void declararVariables()
    {
        try
            {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setTitle(getString(R.string.almacen_armado_tarimas));
            // getSupportActionBar().setSubtitle(getString(R.string.almacen_armado_tarimas_liquidos));

            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            edtx_Empaque = (EditText) findViewById(R.id.edtx_Empaque);
            edtx_OrdenProduccion = (EditText) findViewById(R.id.edtx_OrdenProduccion);
            spnr_Maquinas = (Spinner) findViewById(R.id.spnr_Maquinas).findViewById(R.id.spinner);



            txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_Lote = (TextView) findViewById(R.id.txtv_Lote);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_CantidadEmpaques = (TextView) findViewById(R.id.txtv_CantidadEmpaques);
            txtv_CantidadRegistrada = (TextView) findViewById(R.id.txtv_CantidadReg);
            txtv_PalletAbierto = (TextView) findViewById(R.id.txtv_PalletAbierto);

            edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_OrdenProduccion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            btn_CerrarTarima = (Button) findViewById(R.id.btn_CerrarTarima);
            btn_CancelarTarima = (Button) findViewById(R.id.btn_CancelarTarima);
            btn_CerrarTarima.setEnabled(true);

            edtx_OrdenProduccion.requestFocus();
        }catch(Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
         }
    }

    private void agregaListeners() {

        edtx_OrdenProduccion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (!edtx_OrdenProduccion.getText().toString().equals("")) {

                            new SegundoPlano("ConsultaOrdenProduccion").execute();
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_OrdenProduccion.setText("");
                                    edtx_OrdenProduccion.requestFocus();
                                }
                            });

                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                        }

                        new esconderTeclado(Almacen_Armado_Pallets_Ne.this);
                        return true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
                }
                return false;
            }
        });

        edtx_Empaque.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {


                        if (edtx_OrdenProduccion.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                    }
                                });
                            }

                        if (edtx_Empaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_Empaque.setText("");
                                    edtx_Empaque.requestFocus();
                                }
                            });
                        }

                        new SegundoPlano("RegistrarEmpaque").execute();
                        //txtv_CantidadEmpaques.setText("");

                        new esconderTeclado(Almacen_Armado_Pallets_Ne.this);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
                }
                return false;
            }
        });

        btn_CerrarTarima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (edtx_OrdenProduccion.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                    return;
                }


                if (txtv_CantidadEmpaques.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto,vista,getString(R.string.error_cerrar_pallet_sin_empaques), "false", true, true);
                    return;
                }
                new CreaDialogos(getString(R.string.pregunta_cierre_pallet), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SegundoPlano("CerrarTarima").execute();
                        new esconderTeclado(Almacen_Armado_Pallets_Ne.this);
                    }
                },null,Almacen_Armado_Pallets_Ne.this);


            }
        });

        btn_CancelarTarima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtx_OrdenProduccion.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                        return;
                    }


                if (txtv_CantidadEmpaques.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_cerrar_pallet_sin_empaques), "false", true, true);
                        return;
                    }

                    new CreaDialogos(getString(R.string.pregunta_cancelar_pallet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            new SegundoPlano("CancelarTarima").execute();
                            new esconderTeclado(Almacen_Armado_Pallets_Ne.this);
                        }
                    }, null,contexto);
                }
        });
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.cerrar_oc_toolbar, menu);
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
        try
            {
        int id = item.getItemId();
        if (!recargar)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            // if ((id == R.id.recargar)) {
                //               if (!edtx_OrdenProduccion.getText().toString().equals(""))
                //               {
                //                   tabla.getDataAdapter().clear();
                //                   new SegundoPlano("ConsultaOrdenProduccion").execute();
                //              }

           // }
            if ((id == R.id.CerrarOC)) {

                if (!edtx_OrdenProduccion.getText().toString().equals("")) {
                    new CreaDialogos(getString(R.string.pregunta_cierre_orden), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            try {

                                return;

//                                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in,
//                                        android.R.anim.fade_out).replace(R.id.clayout, frgmnt_Cierre_Orden.newInstance(
//                                        new String[]{edtx_OrdenProduccion.getText().toString(), edtx_FechaCaducidad.getText().toString()},
//                                        null), frgmnt_tag).commit();
//                                new esconderTeclado(Almacen_Armado_Pallets_Ne.this);

                            }catch(Exception e){
                                e.printStackTrace();
                                new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
                            }
                        }
                    }, null, contexto);
                }
            }
        }
        }catch(Exception e){
            e.printStackTrace();
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }
        return super.onOptionsItemSelected(item);
    }

    private void ReiniciarVariables(String tarea)
    {
        try {
            switch (tarea) {
                case "ConsultaOrdenProduccion":
                    edtx_OrdenProduccion.setText("");
                    txtv_Producto.setText("");
                    txtv_Lote.setText("");
                    txtv_Cantidad.setText("");
//                    edtx_FechaCaducidad.setText("");
                    //           tabla.getDataAdapter().clear();
                    txtv_CantidadRegistrada.setText("");
                    edtx_OrdenProduccion.requestFocus();
                    break;

                case "ConsultaTarima":

                    edtx_Empaque.setText("");
                    txtv_CantidadEmpaques.setText("-");
                    edtx_Empaque.requestFocus();
                    break;

                case "RegistrarEmpaque":

                    edtx_Empaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;

                case "Reiniciar":
                    edtx_OrdenProduccion.setText("");
                    txtv_Producto.setText("");
                    txtv_Lote.setText("");
                    txtv_Cantidad.setText("");
                    txtv_CantidadRegistrada.setText("");
                    txtv_CantidadEmpaques.setText("-");
//                    edtx_FechaCaducidad.setText("");
                    edtx_OrdenProduccion.requestFocus();
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
        }
    }


    private class SegundoPlano extends AsyncTask<Void, Void, Void> {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_RegistroPT cad = new cAccesoADatos_RegistroPT(Almacen_Armado_Pallets_Ne.this);
        cAccesoADatos_Consultas consAd = new cAccesoADatos_Consultas(Almacen_Armado_Pallets_Ne.this);

            public SegundoPlano(String tarea)
            {
                this.tarea = tarea;
            }

            @Override
            protected void onPreExecute() {
                try {
                    recargar = true;
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    progressBarHolder.setAnimation(inAnimation);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    palletRegistradosVar = 0;
                }catch(Exception e){
                    e.printStackTrace();
                    new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
                }
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                try
                {
                    switch (tarea) {
                        case "ConsultaOrdenProduccion":
                            dao = cad.c_ConsultaOrdenProduccion(edtx_OrdenProduccion.getText().toString());
                            break;

                        case "ConsultaMaquinas":
                            dao = consAd.c_ConsultaMaquinas(str_Maquina);
                            break;

                        case "ConsultaTarima":
                            dao = cad.c_ConsultaEmpaquesArmadoProd_NE(edtx_OrdenProduccion.getText().toString());
                            break;

                     /*   case "RegistrarEmpaque":
                            dao = cad.c_CreaEmpaqueNE(edtx_OrdenProduccion.getText().toString(),edtx_Empaque.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato());
                            break;*/

                        case "CerrarTarima":
                            dao = cad.c_CerrarRegistroPallet(edtx_OrdenProduccion.getText().toString(),((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato());
                            break;

                        case "CancelarTarima":
                            dao = cad.c_CancelarRegistroPallet(edtx_OrdenProduccion.getText().toString());
                            break;

                        default:
                            dao = new DataAccessObject();
                    }

                } catch (Exception e)
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
                            case "ConsultaOrdenProduccion":
                                txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadTotal"));
                                txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Producto"));
                                txtv_CantidadRegistrada.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRegistrada"));

                                if(!dao.getcMensaje().equals("@"))
                                    {
                                        str_Maquina = dao.getcMensaje();
                                    }

                                new SegundoPlano("ConsultaMaquinas").execute();
                                new SegundoPlano("ConsultaTarima").execute();

                                break;


                            case "ConsultaMaquinas":

                                spnr_Maquinas.setAdapter(new CustomArrayAdapter(Almacen_Armado_Pallets_Ne.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("DLinea","Linea")));

                                break;

                            case "RegistrarEmpaque":
                                new SegundoPlano("ConsultaOrdenProduccion").execute();
                                str_Maquina = ((Constructor_Dato)spnr_Maquinas.getSelectedItem()).getDato();// agarro la maquina del ultimo registro correcto
                                txtv_CantidadEmpaques.setText(dao.getcMensaje());
                                txtv_PalletAbierto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                                edtx_Empaque.setText("");

                                break;

                            case "ConsultaTarima":
                                txtv_PalletAbierto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                txtv_CantidadEmpaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                break;
                            case "CerrarTarima":
                                new popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + dao.getcMensaje() + "] con éxito.", dao.iscEstado(), true, true);
                                new SegundoPlano("ConsultaOrdenProduccion").execute();
                                edtx_Empaque.setText("");
                                txtv_CantidadEmpaques.setText("");
                                txtv_PalletAbierto.setText("");

                                break;

                            case "RegistrarCaducidad":
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.fecha_cad_reg_exito), dao.iscEstado(), true, true);

                                new SegundoPlano("ConsultaOrdenProduccion").execute();
                                break;

                            case "CancelarTarima":
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.cancelar_tarima), dao.iscEstado(), true, true);
                                new SegundoPlano("ConsultaOrdenProduccion").execute();
                                edtx_Empaque.setText("");
                                txtv_CantidadEmpaques.setText("");
                                txtv_PalletAbierto.setText("");
                                break;

                            case "CerrarOrden":

                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_prod_cerrar_exito), dao.iscEstado(), true, true);

                                break;

                            case "BajaEmpaque":
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_baja), dao.iscEstado(), true, true);
                                new SegundoPlano("ConsultaOrdenProduccion").execute();
//                                new SegundoPlano("ConsultaTarima").execute();
                                EmpaqueBaja = null;
                                break;

                        }
                    } else
                    {
                        ReiniciarVariables(tarea);
                        new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                    }
                } catch (Exception e) {
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);
                    e.printStackTrace();
                }
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
                recargar = false;
            }
        }

    @Override
    public void onBackPressed()
    {
//        try{
//            Fragment f = getSupportFragmentManager().findFragmentByTag(frgmnt_tag);
//            if (f != null) {
//                getSupportFragmentManager().beginTransaction().remove(f).commit();
//                return;
//            }
         //   super.onBackPressed();
//        }catch(Exception e){
//            e.printStackTrace();
//            new popUpGenerico(contexto,vista,e.getMessage(),"false",true,true);
//        }

        Intent intent = new Intent(Almacen_Armado_Pallets_Ne.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }



}




