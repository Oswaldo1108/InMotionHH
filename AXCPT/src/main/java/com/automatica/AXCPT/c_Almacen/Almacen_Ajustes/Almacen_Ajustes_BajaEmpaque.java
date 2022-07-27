package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityAlmacenAjustesBajaEmpaqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Almacen_Ajustes_BajaEmpaque extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar
{


    //region variables
    cAccesoADatos_Almacen ca;
    EditText edtx_Cantidad,edtx_CodigoEmpaque,edtx_CodigoEmpaqueConfirmacion;
    TextView txtv_Producto,txtv_Cantidad;
        frgmnt_taskbar_AXC taskbar_axc;

    Spinner spnr_Ajuste;

    private ProgressBarHelper p;


    View vista;
    Context contexto = this;
    String[] TipoAjuste;

    Handler h = new Handler();
    ArrayAdapter<String> spinnerArrayAdapter;
    ActivityAlmacenAjustesBajaEmpaqueBinding binding;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenAjustesBajaEmpaqueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        declaraVariables();
        agregaListeners();
        ca = new cAccesoADatos_Almacen(Almacen_Ajustes_BajaEmpaque.this);
       SegundoPlano sp = new SegundoPlano("ListarAjustes");
        sp.execute();
        View logoView = getToolbarLogoIcon((Toolbar) findViewById(R.id.toolbar));
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
        getSupportFragmentManager().beginTransaction().add(findViewById(R.id.FrameLayout).getId(),taskbar_axc,"FragmentoTaskBar").commit();

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
        if (p.ispBarActiva()){
            int id = item.getItemId();

            if((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto,vista);
            }
            if((id == R.id.borrar_datos))
            {
                reiniciaCampos();


            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Ajustes_Baja_Empaque));


        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_BajaEmpaque.this);
        p= new ProgressBarHelper(this);
        edtx_CodigoEmpaque= (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoEmpaqueConfirmacion = (EditText) findViewById(R.id.edtx_ConfirmacionEmpaque);

        edtx_CodigoEmpaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoEmpaqueConfirmacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        txtv_Producto= (TextView) findViewById(R.id.txtv_Producto);
        txtv_Cantidad= (TextView) findViewById(R.id.txtv_CantPend);

        spnr_Ajuste= (Spinner) findViewById(R.id.spnr_Ajuste);

    }
    private void agregaListeners()
    {
        edtx_CodigoEmpaqueConfirmacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoEmpaqueConfirmacion.getText().toString().equals(""))
                    {
                        if(edtx_CodigoEmpaqueConfirmacion.getText().toString().equals(edtx_CodigoEmpaque.getText().toString()))
                        {
                            SegundoPlano sp = new SegundoPlano("AjusteBajaEmpaques");
                            sp.execute();

                        }else
                        {
                            new popUpGenerico(contexto,vista,getString(R.string.empaques_no_coinciden),"Advertencia",true,true);
                           h.post(new Runnable() {
                               @Override
                               public void run() {

                                   edtx_CodigoEmpaqueConfirmacion.setText("");
                                   edtx_CodigoEmpaqueConfirmacion.requestFocus();
                               }
                           });
                        }
                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"Advertencia",true,true);
                        edtx_CodigoEmpaqueConfirmacion.setText("");
                        edtx_CodigoEmpaqueConfirmacion.requestFocus();
                        h.post(new Runnable() {
                            @Override
                            public void run() {

                                edtx_CodigoEmpaqueConfirmacion.setText("");
                                edtx_CodigoEmpaqueConfirmacion.requestFocus();
                            }
                        });
                    }

                    new esconderTeclado(Almacen_Ajustes_BajaEmpaque.this);
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
                    if(!edtx_CodigoEmpaque.getText().toString().equals(""))
                    {
                        SegundoPlano sp = new SegundoPlano("ConsultaEmpaque");
                        sp.execute();

                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"Advertencia",true,true);

                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoEmpaque.setText("");
                                edtx_CodigoEmpaque.requestFocus();
                            }
                        });
                    }

                    new esconderTeclado(Almacen_Ajustes_BajaEmpaque.this);
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
    private void reiniciaCampos()
    {

        edtx_CodigoEmpaque.setText("");
        edtx_CodigoEmpaqueConfirmacion.setText("");
        edtx_CodigoEmpaque.requestFocus();
        txtv_Producto.setText("");
        txtv_Cantidad.setText("");
        binding.txtvEstatus.setText("");
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
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

            String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();

            switch (tarea)
            {

                case "ConsultaEmpaque":
                    dao = ca.c_ConsultarEmpaque(CodigoEmpaque);
                    break;

                case "ListarAjustes"://Se usa para llenar el Spinner
                    dao= ca.c_ListarConceptosAjuste("2");
                    break;
                case "AjusteBajaEmpaques" :
                    String TipoEvento = spnr_Ajuste.getSelectedItem().toString();
                    dao = ca.c_AjusteBajaEmpaque(CodigoEmpaque,TipoEvento);

                    break;

               /* case "AjusteNuevoEmpaquePalletExistente":
                    String Cantidad = edtx_Cantidad.getText().toString();
                    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                    String TipoEvento = spnr_Ajuste.getSelectedItem().toString();
                    String Revision ="";
                    sa.SOAPAjusteNuevoEmpaquePalletExistente(CodigoEmpaque,CodigoPallet,Producto,Cantidad,Revision,TipoEvento,contexto);

                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();
                    break;*/


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


                                    case "ConsultaEmpaque":
                                        txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        //txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                                        txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        binding.txtvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                                        break;
                                    case "ListarAjustes":
                                        spnr_Ajuste.setAdapter(new CustomArrayAdapter(Almacen_Ajustes_BajaEmpaque.this, android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion", "Tag1", "")));
                                        break;
                                    case "AjusteBajaEmpaques":
                                        new popUpGenerico(contexto, vista, "Empaque dado de baja con éxito.", dao.iscEstado(), true, true);
                                        reiniciaCampos();
                                        break;
                                }
                        } else
                        {
                            new popUpGenerico(contexto, vista, dao.getcMensaje(), dao.iscEstado(), true, true);
                            reiniciaCampos();
                        }

                } catch (Exception e)
                {
                    new popUpGenerico(contexto, vista, e.getMessage(), false, true, true);

                }
            p.DesactivarProgressBar(tarea);
        }
    }
    @Override
    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
            taskbar_axc.cerrarFragmento();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}
