package com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro__PorEmpaque_LoteModificable;
import com.automatica.AXCPT.databinding.ActivityDevolucionEmpaquesBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Devolucion_Empaques extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    TextView tv_devolucion,tv_Producto,tv_Partida,tv_cantOrig,tv_cantReg2,
            tv_EmpaquesRegistrados,tv_pallet,tv_ProductoDet, tv_cantReg;
    int registroAnteriorSpinner=0;
    EditText edtx_CodigoEmpaque,edtx_CantidadEmpaques,edtx_CantidadPiezas,edtx_LoteNuevo;
    Handler h = new Handler();

    Context contexto = this;
    FrameLayout progressBarHolder;

    Bundle b;

    String OrdenDevolucion, Partida, NumParte;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    frgmnt_taskbar_AXC taskbar_axc;

    Spinner spnr_lote;
    ActivityDevolucionEmpaquesBinding binding;
    boolean recargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDevolucionEmpaquesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        InicializarVariables();
        AgregarListeners();
        new SegundoPlano("ConsultarPallet").execute();
        new SegundoPlano("ConsultarPartida").execute();
        new SegundoPlano("ListarLotes").execute();
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
        getSupportFragmentManager().beginTransaction().add(binding.FrameLayout.getId(),taskbar_axc,"FragmentoTaskBar").commit();


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
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

        if(!recargar)
            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(Devolucion_Empaques.this,null);
                    }
                if ((id == R.id.recargar))
                    {

                        if (!tv_devolucion.getText().toString().equals(""))
                            {
                                new SegundoPlano("ConsultaOrdenProduccion").execute();
                            }
                    }

            }
        return super.onOptionsItemSelected(item);
    }
    public void InicializarVariables(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devolución");
        getSupportActionBar().setSubtitle("Empaque");
        tv_devolucion=findViewById(R.id.tv_devolucion);
        tv_Producto = findViewById(R.id.tv_Producto);
        tv_Partida= findViewById(R.id.tv_Partida);
        tv_cantOrig= findViewById(R.id.tv_cantOrig);
        tv_ProductoDet= findViewById(R.id.tv_ProductoDet);
        tv_cantReg2= findViewById(R.id.tv_cantReg2);
        tv_EmpaquesRegistrados = findViewById(R.id.tv_EmpaquesRegistrados);
        tv_pallet= findViewById(R.id.tv_pallet);
        tv_cantReg = findViewById(R.id.tv_cantReg);
        edtx_LoteNuevo = (EditText) findViewById(R.id.edtx_LoteNuevo);
        edtx_LoteNuevo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        progressBarHolder = findViewById(R.id.progressBarHolder);

        b = getIntent().getExtras();
        OrdenDevolucion = b.getString("OrdenDevolucion");
        Partida = b.getString("Partida");
        NumParte= b.getString("NumParte");

        edtx_CodigoEmpaque= findViewById(R.id.edtx_CodigoEmpaque);
        edtx_CantidadEmpaques= findViewById(R.id.edtx_CantidadEmpaques);
        edtx_CantidadPiezas = findViewById(R.id.edtx_CantidadPiezas);

        spnr_lote = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);
    }

    public void LimpiarCampos(){
        tv_devolucion.setText("");
        tv_Producto.setText("");
        tv_Partida.setText("");
        tv_cantOrig.setText("");
        tv_cantReg.setText("");
        tv_ProductoDet.setText("");
        edtx_CantidadEmpaques.setText("");
        edtx_CantidadPiezas.setText("");
    }

    public void AgregarListeners(){

        edtx_LoteNuevo.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                    {
                        try
                            {
                                edtx_LoteNuevo.setText("");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                            }
                    }
            }
        });

        edtx_LoteNuevo.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                            {
                                if(edtx_LoteNuevo.getText().toString().equals(""))
                                    {
                                        h.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                edtx_LoteNuevo.setText("");
                                                edtx_LoteNuevo.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto,getCurrentFocus(),"Ingrese un lote." , false, true, true);
                                        return false;
                                    }

                                new CreaDialogos("¿Crear lote? [" + edtx_LoteNuevo.getText().toString() + "]",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                new  SegundoPlano("CreaLoteRecepcionDev").execute();
                                            }
                                        },null,contexto);



                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }
                    }
                return false;
            }
        });


        spnr_lote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                   tv_cantReg2.setText(((Constructor_Dato)spnr_lote.getSelectedItem()).getTag2());

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.AXCPT.Servicios.popUpGenerico(Devolucion_Empaques.this, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                    tv_cantReg2.setText("-");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        edtx_CodigoEmpaque.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (edtx_CantidadPiezas.getText().toString().equals(""))
                 {
                    new popUpGenerico(Devolucion_Empaques.this,edtx_CantidadPiezas,getString(R.string.error_ingrese_cantidad),false,true,true);
                    return false;
                 }
                if (edtx_CantidadEmpaques.getText().toString().equals(""))
                {
                    new popUpGenerico(Devolucion_Empaques.this,edtx_CantidadEmpaques,getString(R.string.error_ingrese_cantidad_empaques),false,true,true);
                    return false;
                }

                if (edtx_CodigoEmpaque.getText().toString().equals(""))
                {
                    new popUpGenerico(Devolucion_Empaques.this,edtx_CodigoEmpaque,"Código de empaque vacio",false,true,true);
                    return false;
                }



                if(spnr_lote.getAdapter() == null)
                    {
                        new CreaDialogos("Se generará lote nuevo.",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        new SegundoPlano("RegistrarEmpaques").execute();
                                    }
                                }, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                edtx_CodigoEmpaque.setText("");

                                new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_lote_desc) , "false", true, true);

                            }
                        }, contexto);
                    }else{

                    new SegundoPlano("RegistrarEmpaques").execute();

                }



                return false;
            }
        });
    }

    @Override
    public void BotonDerecha() {
        new SegundoPlano("CerrarTarima").execute();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
    String Tarea;
    DataAccessObject dao= new DataAccessObject();
    cAccesoADatos_Almacen ca= new cAccesoADatos_Almacen(Devolucion_Empaques.this);
        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            recargar = true;

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                ca = new cAccesoADatos_Almacen(Devolucion_Empaques.this);
                switch (Tarea){
                    case "ConsultarPallet":

                        dao= ca.c_ConsultaPalletAbiertoDev(OrdenDevolucion,Partida);
                        break;
                    case "ListarLotes":

                        dao= ca.c_ListarLotesDev(OrdenDevolucion,Partida,NumParte);
                        break;
                    case "ConsultarPartida":

                        dao= ca.c_ConsultaPartidaDevolucion(OrdenDevolucion,Partida);
                        break;
                    case "RegistrarEmpaques":

                        if(spnr_lote.getAdapter() == null)
                            {
                                    dao = ca.c_OCRegistrarEmpaqueDevolucion(OrdenDevolucion,
                                                                            Partida,
                                                                            edtx_CodigoEmpaque.getText().toString(),
                                                                            "", "",
                                                                            edtx_CantidadPiezas.getText().toString(),
                                                                            edtx_CantidadEmpaques.getText().toString());
                            }else
                                {
                                    dao = ca.c_OCRegistrarEmpaqueDevolucion(OrdenDevolucion,
                                                                            Partida,
                                                                            edtx_CodigoEmpaque.getText().toString(),
                                                                            ((Constructor_Dato)spnr_lote.getSelectedItem()).getDato(),"",
                                                                            edtx_CantidadPiezas.getText().toString(),
                                                                            edtx_CantidadEmpaques.getText().toString());
                                }

                        break;
                    case "CerrarTarima":

                        dao = ca.c_CierraPalletDevolucion(tv_pallet.getText().toString());
                        break;

                    case "CreaLoteRecepcionDev":

                        dao = ca.c_CreaLoteRecepcionDev(OrdenDevolucion,
                                Partida,
                                edtx_LoteNuevo.getText().toString(),
                                "1");
                        break;

                    default:
                        dao = new DataAccessObject();
                }
            }catch (Exception e){
                e.printStackTrace();
                dao = new DataAccessObject(e);

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                recargar = false;
                if (dao.iscEstado())
                {
                    switch (Tarea)
                        {
                        case "ConsultarPallet":
                            String CodigoPallet, Empaques;
                            String[] mensaje= dao.getcMensaje().split("#");
                            CodigoPallet = mensaje[0];
                            Empaques = mensaje[1];
                            if(!CodigoPallet.equals(""))
                            {
                                tv_pallet.setText(CodigoPallet);
                                tv_EmpaquesRegistrados.setText(Empaques);
                            }else {
                                tv_pallet.setText("-");
                                tv_EmpaquesRegistrados.setText("-");
                            }
                            break;
                        case "ListarLotes":
                            if(dao.getcTablas() != null)
                            {
                                registroAnteriorSpinner = spnr_lote.getSelectedItemPosition();
                                spnr_lote.setAdapter(new CustomArrayAdapter(Devolucion_Empaques.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("LoteAXC","CantidadRecibida","CantidadRegistrada")));
                                spnr_lote.setSelection(registroAnteriorSpinner);
                            }else
                            {
                                spnr_lote.setAdapter(null);
                            }
                            break;
                            case "CreaLoteRecepcionDev":
                                new SegundoPlano("ListarLotes").execute();

                                break;
                        case "ConsultarPartida":
                            tv_devolucion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Devolucion"));
                            tv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            tv_Partida.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaERP"));
                            tv_cantOrig.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                            tv_cantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            tv_ProductoDet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));
                            new SegundoPlano("ListarLotes").execute();
                            new SegundoPlano("ConsultarPallet").execute();

                            break;

                        case "RegistrarEmpaques":

                            tv_pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            edtx_CodigoEmpaque.setText("");

                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                                {
                                    new popUpGenerico(Devolucion_Empaques.this, getCurrentFocus(), getString(R.string.orden_compra_completada),dao.iscEstado(), true, true);
                                    new SegundoPlano("CerrarTarima").execute();
                                    return;
                                }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                                {
                                    new popUpGenerico(Devolucion_Empaques.this, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);

                                    new SegundoPlano("CerrarTarima").execute();
                                    return;
                                }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1") )
                                {
                                    new SegundoPlano("CerrarTarima").execute();
                                }
                            new SegundoPlano("ConsultarPartida").execute();
                            edtx_CodigoEmpaque.requestFocus();

                            break;
                        case "CerrarTarima":
                            new popUpGenerico(Devolucion_Empaques.this,getCurrentFocus(),getString(R.string.orden_compra_pallet_cerrado) + " [" +dao.getcMensaje() + "]"  ,true,true,true);
                            new SegundoPlano("ConsultarPallet").execute();

                            break;
                    }
                }else
                    {
                        new popUpGenerico(Devolucion_Empaques.this,getCurrentFocus(),dao.getcMensaje(),false,true,true);
                        edtx_CodigoEmpaque.setText("");
                    }
            }catch (Exception e){
                e.printStackTrace();
                new popUpGenerico(Devolucion_Empaques.this,getCurrentFocus(),e.getMessage(),false,true,true);

            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu")!=null){
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("FragmentoNoti")!=null||getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!=null){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}