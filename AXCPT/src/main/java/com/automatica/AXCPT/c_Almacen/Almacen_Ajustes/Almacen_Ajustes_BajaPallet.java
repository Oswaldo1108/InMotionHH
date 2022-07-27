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
import com.automatica.AXCPT.databinding.AlmacenActivityAjustesBajaPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Almacen_Ajustes_BajaPallet extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar
{


    //region variables

    EditText edtx_CodigoPallet,edtx_CodigoPalletConfirmacion;
    TextView txtv_Empaques,txtv_Producto,txtv_Cantidad;
    Spinner spnr_Ajuste;
    frgmnt_taskbar_AXC taskbar_axc;
    private ProgressBarHelper p;

    AlmacenActivityAjustesBajaPalletBinding binding;
    View vista;
    Context contexto = this;
    Handler h = new Handler();

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding= AlmacenActivityAjustesBajaPalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_BajaPallet.this);
        declaraVariables();
        agregaListeners();
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
        try
        {
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
        try {

            if (p.ispBarActiva()){
                int id = item.getItemId();

                if ((id == R.id.InformacionDispositivo)) {
                    new sobreDispositivo(contexto, vista);
                }
                if ((id == R.id.borrar_datos)) {
                    reiniciaCampos();
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" " + getString(R.string.Ajustes_Baja_Pallet));
//            toolbar.setLogo(R.mipmap.logo_axc);

            p = new ProgressBarHelper(this);
            edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_CodigoPallet);
            edtx_CodigoPalletConfirmacion = (EditText) findViewById(R.id.edtx_ConfirmacionPallet);
            edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoPalletConfirmacion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Cantidad = (TextView) findViewById(R.id.txtv_CantPend);
            txtv_Empaques = (TextView) findViewById(R.id.txtv_Caducidad);


            spnr_Ajuste = (Spinner) findViewById(R.id.spnr_Ajuste);


            // Spinner spinner = new Spinner(this);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }

    }
    private void agregaListeners()
    {
        try
        {
        edtx_CodigoPalletConfirmacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPalletConfirmacion.getText().toString().equals(""))
                    {
                        if(edtx_CodigoPalletConfirmacion.getText().toString().equals(edtx_CodigoPallet.getText().toString()))
                        {
                            SegundoPlano sp = new SegundoPlano("AjusteBajaPallet");
                            sp.execute();

                        }else
                        {
                            new popUpGenerico(contexto,vista,"Los códigos de pallet no coinciden.","Advertencia",true,true);
                           h.post(new Runnable() {
                               @Override
                               public void run() {
                                   edtx_CodigoPalletConfirmacion.setText("");
                                   edtx_CodigoPalletConfirmacion.requestFocus();
                               }
                           });

                        }
                    }else
                    {
                        new popUpGenerico(contexto,vista,"Debes capturar un código de pallet.","Advertencia",true,true);
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoPalletConfirmacion.setText("");
                                edtx_CodigoPalletConfirmacion.requestFocus();    }
                        });

                    }

                    new esconderTeclado(Almacen_Ajustes_BajaPallet.this);
                }
                return false;
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
                        SegundoPlano sp = new SegundoPlano("ConsultaPallet");
                        sp.execute();

                    }else
                    {
                        new popUpGenerico(contexto,vista,"Debes capturar una cantidad valida","Advertencia",true,true);
                        edtx_CodigoPallet.setText("");
                        edtx_CodigoPallet.requestFocus();
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                edtx_CodigoPallet.setText("");
                                edtx_CodigoPallet.requestFocus();    }
                        });
                    }

                    new esconderTeclado(Almacen_Ajustes_BajaPallet.this);
                }
                return false;
            }
        });
            binding.txtvProducto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (binding.txtvProducto.getText().toString().equals("-")|| TextUtils.isEmpty(binding.txtvProducto.getText())){
                        new popUpGenerico(contexto,getCurrentFocus(),"Consulta un pallet para continuar",false,true,true);
                        return false;
                    }
                    String[] datos={txtv_Producto.getText().toString()};
                    taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos, FragmentoConsulta.TIPO_EXISTENCIA),FragmentoConsulta.TAG);
                    return true;
                }
            });


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }
    }
    private void reiniciaCampos()
    {
        try
        {
        edtx_CodigoPallet.setText("");
        edtx_CodigoPalletConfirmacion.setText("");
        edtx_CodigoPallet.requestFocus();
        txtv_Cantidad.setText("");
        txtv_Empaques.setText("");
        txtv_Producto.setText("");
        binding.txtvEstatus.setText("");
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

        }
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
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Ajustes_BajaPallet.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                p.ActivarProgressBar(tarea);

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);

            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
        try{
            String CodigoPallet = edtx_CodigoPallet.getText().toString();

            switch (tarea)
            {

                case "ConsultaPallet":
                    //sa.SOAPConsultaPallet(CodigoPallet,contexto);
                    dao= ca.c_ConsultarPalletPT(CodigoPallet);
                    break;

                case "ListarAjustes"://Se usa para llenar el Spinner
                    //sa.SOAPListarConceptosAjuste("2",contexto);
                    dao= ca.c_ListarConceptosAjuste("2");

                    break;
                case "AjusteBajaPallet" :
                    String TipoEvento = spnr_Ajuste.getSelectedItem().toString();
                    //sa.SOAPAjusteBajaPallet(CodigoPallet,TipoEvento,contexto);
                    dao = ca.c_AjusteBajaPallet(CodigoPallet,TipoEvento);
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
                if (dao.iscEstado()) {
                    switch (tarea) {


                        case "ConsultaPallet":
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            //txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                            txtv_Cantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            txtv_Empaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.txtvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));

                            break;
                        case "ListarAjustes":
                            spnr_Ajuste.setAdapter(new CustomArrayAdapter(Almacen_Ajustes_BajaPallet.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","Tag1","")));
                            break;
                        case "AjusteBajaPallet":
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallet_baja), String.valueOf(dao.iscEstado()), true, true);
                            reiniciaCampos();
                            break;
                    }
                }else{
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), String.valueOf(dao.iscEstado()), true, true);
                    reiniciaCampos();
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
