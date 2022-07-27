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
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityReubicarEmpaqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class ReubicarEmpaque extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    ActivityReubicarEmpaqueBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    cAccesoADatos_Almacen ca;
    TextView txtv_Producto, txtv_Empaques, txtv_Cantidad, txtv_Ubicacion,txtv_DescProd,txtv_Estatus;
    EditText edtx_CodigoPallet,edtx_NuevaUbicacion;
    private ProgressBarHelper p;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    boolean recargar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReubicarEmpaqueBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setContentView(R.layout.almacen_activity_colocar);
        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .replace(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout,taskbar_axc,"FragmentoTaskBar").commit();

        declararVariables();
        ca= new cAccesoADatos_Almacen(ReubicarEmpaque.this);
        agregaListeners();
        edtx_CodigoPallet.requestFocus();
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, ReubicarEmpaque.this);

    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
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
        if (p.ispBarActiva())
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                borrarDatos();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void borrarDatos()
    {
        try
        {


            edtx_NuevaUbicacion.setText("");
            edtx_CodigoPallet.setText("");
            txtv_Ubicacion.setText("");

            txtv_Producto.setText("");
            txtv_Cantidad.setText("");
            edtx_CodigoPallet.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
        }
    }
    private void declararVariables()
    {
        try
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle("Reubicar Empaque");

//        toolbar.setLogo(R.mipmap.logo_axc);//      toolbar.setLogo(R.drawable.axc_logo_toolbar);

            p = new ProgressBarHelper(this);
            edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
            edtx_NuevaUbicacion = (EditText) findViewById(R.id.edtx_NuevaUbicacion);
            edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            edtx_NuevaUbicacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_DescProd = (TextView) findViewById(R.id.txtv_DescProd);
            txtv_Cantidad = (TextView) findViewById(R.id.txtv_CantPend);

            txtv_Estatus = (TextView) findViewById(R.id.txtv_Estatus);
            txtv_Ubicacion = (TextView) findViewById(R.id.txtv_Ubicacion);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista ,e.getMessage() , "false", true, true);
        }

    }
    private void agregaListeners()
    {
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

                    }
                    else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CodigoPallet.setText("");
                                edtx_CodigoPallet.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.error_ingrese_empaque),"false", true, true);
                    }
                    new esconderTeclado(ReubicarEmpaque.this);
                }
                return false;
            }
        });
        edtx_NuevaUbicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_empaque),"false", true, true);
                        return false;
                    }


                    if(!edtx_NuevaUbicacion.getText().toString().equals(""))
                    {
                        new SegundoPlano("RegistroNuevaUbicacion").execute();

                    }
                    else
                    {
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_NuevaUbicacion.setText("");
                                edtx_NuevaUbicacion.requestFocus();
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_ubicacion),"false", true, true);
                    }

                    new esconderTeclado(ReubicarEmpaque.this);
                }
                return false;
            }
        });
        binding.txtvProducto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (binding.txtvProducto.getText().toString().equals("-")|| TextUtils.isEmpty(binding.txtvProducto.getText())){
                    new popUpGenerico(contexto,getCurrentFocus(),"Consulta un empaque para continuar",false,true,true);
                    return false;
                }
                String[] datos={txtv_Producto.getText().toString()};
                taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_EXISTENCIA),FragmentoConsulta.TAG);
                return true;
            }
        });
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            p.ActivarProgressBar(tarea);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case "ConsultaPallet":
                        //sa.SOAPConsultaPallet(CodigoPallet, contexto);
                        dao= ca.c_ConsultarEmpaque(edtx_CodigoPallet.getText().toString());
                        break;
                    case "RegistroNuevaUbicacion":
                        //sa.SOAPReubicarEmbalaje(CodigoPallet, NuevaUbicacion, contexto);
                        dao = ca.c_ReubicarEmpaque(edtx_CodigoPallet.getText().toString(), edtx_NuevaUbicacion.getText().toString());
                        break;
                    default:
                        dao = new DataAccessObject();
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
                if (dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case "ConsultaPallet":
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            txtv_DescProd.setText( dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));

                            txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            txtv_Ubicacion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Ubicacion"));
                            txtv_Estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));

                            break;
                        case "RegistroNuevaUbicacion":
                            borrarDatos();
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.empaque_reubicado_exito), String.valueOf(dao.iscEstado()), true, true);
                            break;

                    }
                }else
                {
                    borrarDatos();
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), "Advertencia", true, true);

                }

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico( contexto,getCurrentFocus(),e.getMessage() ,"false" , true, true);
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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}