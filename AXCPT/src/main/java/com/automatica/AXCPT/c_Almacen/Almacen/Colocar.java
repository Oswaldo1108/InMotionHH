package com.automatica.AXCPT.c_Almacen.Almacen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.ReabastecimientoPicking.ReabPK_Seleccion_Material;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.Validacion.ValidarColocar;
import com.automatica.AXCPT.databinding.ActivityColocarBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Colocar extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    popUpGenerico pop = new popUpGenerico(Colocar.this);
    ActivityColocarBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    ProgressBarHelper progressBarHelper;
    EditText edtx_CodigoPallet,edtx_CodigoUbicacion;
    private ProgressBarHelper p;
    TextView txtv_Producto,txtv_DescProd, txtv_Empaques,txtv_Cantidad,txtv_Caducidad,txtv_Estatus,txtv_UbicacionSugerida;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    String posicion = "";
    Bundle b = new Bundle();
    boolean recargar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            binding = ActivityColocarBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Colocar");
            getSupportActionBar().setSubtitle("Pallet");
            View logoView = getToolbarLogoIcon(toolbar);
            logoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                                .add(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });
            taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
            try {
                getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();
            }catch (Exception e){
                e.printStackTrace();
            }


            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Colocar.this);
            declararVariables();
            sacarDatosIntent();
            agregaListeners();
        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }



    private void declararVariables()
    {
        p = new ProgressBarHelper(this);

        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        edtx_CodigoUbicacion = (EditText) findViewById(R.id.edtx_Ubicacion);
        edtx_CodigoUbicacion .setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtv_Producto= (TextView) findViewById(R.id.txtv_Producto);
        txtv_DescProd = (TextView) findViewById(R.id.txtv_DescProd);
        txtv_Cantidad = (TextView) findViewById(R.id.txtv_Cantidad);
        txtv_Empaques = (TextView) findViewById(R.id.txtv_Empaques);
        txtv_UbicacionSugerida = (TextView) findViewById(R.id.txtv_UbicacionSugerida);
        txtv_Caducidad = (TextView) findViewById(R.id.txtv_FechaCaducidad);
        txtv_Estatus = (TextView) findViewById(R.id.txtv_Estatus);
        edtx_CodigoPallet.requestFocus();
    }

    private void sacarDatosIntent() {
        try {
            b = getIntent().getExtras();
            if (!b.getString("Documento").equals("")){
                edtx_CodigoPallet.setText(b.getString("Documento"));
                new Colocar.SegundoPlano("ConsultaPallet").execute();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void agregaListeners()
    {
        edtx_CodigoPallet.setCustomSelectionActionModeCallback(new ActionMode.Callback2() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        edtx_CodigoPallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
          });
        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new SegundoPlano("ConsultaPallet").execute();

                    }else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoPallet.requestFocus();
                                edtx_CodigoPallet.setText("");
                            }
                        });
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),"false",true,true);
                    }
                    new esconderTeclado(Colocar.this);
                }

                return false;
            }
        });

        txtv_Producto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(txtv_Producto.getText())||TextUtils.equals(txtv_Producto.getText(),"COMPARTIDO")||TextUtils.equals(txtv_Producto.getText(),"-")){
                    new popUpGenerico(contexto,getCurrentFocus(),"Consulta un pallet con un producto valido para continuar",false,true,true);
                    return false;
                }
                String[] datos={txtv_Producto.getText().toString()};
                taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_EXISTENCIA),FragmentoConsulta.TAG);
                return true;
            }
        });
        edtx_CodigoUbicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoUbicacion.getText().toString().equals(""))
                    {

                        if(edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_pallet),"false",true,true);
                            return false;
                        }
                        posicion = edtx_CodigoUbicacion.getText().toString();
                         new SegundoPlano("ColocacionPallet").execute();

                    }else
                    {

                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_CodigoUbicacion.requestFocus();
                                edtx_CodigoUbicacion.setText("");
                            }
                        });
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_ubicacion),"false",true,true);
                    }
                    new esconderTeclado(Colocar.this);
                }

                return false;
            }
        });

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

    private void ReiniciarVariables()
    {
        edtx_CodigoPallet.setText("");
        edtx_CodigoUbicacion.setText("");

        txtv_Empaques.setText("");
        txtv_Producto.setText("");
        txtv_DescProd.setText("");
        txtv_Cantidad.setText("");
        txtv_Estatus.setText("");
        txtv_Caducidad.setText("");
        txtv_UbicacionSugerida.setText("");
        edtx_CodigoPallet.requestFocus();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (p.ispBarActiva())
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                ReiniciarVariables();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Colocar.this);
        cAccesoADatos_Consultas caCons = new cAccesoADatos_Consultas(Colocar.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override

        protected void onPreExecute()
        {
            p.ActivarProgressBar(tarea);

            recargar = false;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
            {


                switch (tarea) {
                    case "ConsultaPallet":

                        dao = caCons.c_ConsultarPalletPT(edtx_CodigoPallet.getText().toString());
                        break;
                    case "ConsultaUbicacionSugerida":


                        dao = ca.c_SugierePosicion(edtx_CodigoPallet.getText().toString());

                        break;
                    case "ColocacionPallet":
                        dao= ca.c_colocaPallet(edtx_CodigoPallet.getText().toString(),edtx_CodigoUbicacion.getText().toString(),txtv_UbicacionSugerida.getText().toString());
                        break;


                }
            }catch (Exception e)
            {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case "ConsultaPallet":
                            txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            txtv_Empaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
//                            txtv_DescProd.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                            txtv_DescProd.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                            txtv_Caducidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                            txtv_Estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));


                            handler.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_CodigoUbicacion.requestFocus();
                                }
                            },10);
                            new SegundoPlano("ConsultaUbicacionSugerida").execute();
                            break;
                        case "ConsultaUbicacionSugerida":
                            txtv_UbicacionSugerida.setText(dao.getcMensaje());
                            break;

                        case "ColocacionPallet":

                            ReiniciarVariables();
                            new popUpGenerico(contexto, edtx_CodigoPallet, getString(R.string.pallet_colocado) + "en la ubicación [" +posicion + "]", dao.iscEstado(), true, true);
                            break;

                    }

                }else
                {
                    ReiniciarVariables();
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), String.valueOf(dao.iscEstado()), true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), String.valueOf(dao.iscEstado()), true, true);
            }
            p.DesactivarProgressBar(tarea);
        }
    }


    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {

        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!=null){
            if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
                taskbar_axc.cerrarFragmento();
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        Intent intent = new Intent(Colocar.this, ValidarColocar.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}