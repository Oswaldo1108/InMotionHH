package com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro_PrimerasYUltimas_LoteModificable;
import com.automatica.AXCPT.databinding.ActivityDevolucionPYUBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Devolucion_PYU extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{
    TextView tv_devolucion,tv_Producto,tv_Partida,tv_cantOrig,tv_cantReg2, tv_ProductoDet, tv_cantReg;
    int registroAnteriorSpinner=0;
    EditText edtx_PrimerEmpaque,edtx_CantidadEmpaques,edtx_CantidadPiezas,edtx_UltimoEmpaque,edtx_ConfEmpaques,edtx_LoteNuevo;
    FrameLayout progressBarHolder;
    String strEmpaques,strPallet;
    Bundle b;
    Handler h = new Handler();

    Context contexto = this;
    String OrdenDevolucion, Partida, NumParte;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    frgmnt_taskbar_AXC taskbar_axc;

    ActivityDevolucionPYUBinding binding;
    Spinner spnr_lote;
    boolean recargar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDevolucionPYUBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);
        InicializarVariables();
        AgregarListeners();
        new SegundoPlano("ConsultarPallet").execute();
        new SegundoPlano("ConsultarPartida").execute();

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
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    public void InicializarVariables(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devolución");
        getSupportActionBar().setSubtitle("Primera y ultima");
        tv_devolucion=findViewById(R.id.tv_devolucion);
        tv_Producto = findViewById(R.id.tv_Producto);
        tv_Partida= findViewById(R.id.tv_Partida);
        tv_cantOrig= findViewById(R.id.tv_cantOrig);
        tv_ProductoDet= findViewById(R.id.tv_ProductoDet);
        tv_cantReg2= findViewById(R.id.tv_cantReg2);
        tv_cantReg = findViewById(R.id.tv_cantReg);
        edtx_LoteNuevo = (EditText) findViewById(R.id.edtx_LoteNuevo);
        edtx_LoteNuevo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


        progressBarHolder = findViewById(R.id.progressBarHolder);
        b = getIntent().getExtras();
        OrdenDevolucion = b.getString("OrdenDevolucion");
        Partida = b.getString("Partida");
        NumParte= b.getString("NumParte");

        edtx_PrimerEmpaque= findViewById(R.id.edtx_PrimerEmpaque);
        edtx_CantidadEmpaques= findViewById(R.id.edtx_CantidadEmpaques);
        edtx_CantidadPiezas = findViewById(R.id.edtx_CantidadPiezas);
        edtx_UltimoEmpaque= findViewById(R.id.edtx_UltimoEmpaque);
        edtx_ConfEmpaques= findViewById(R.id.edtx_ConfEmpaques);

        spnr_lote = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);
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
                        new sobreDispositivo(Devolucion_PYU.this,null);
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
    public void AgregarListeners()
    {

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
                                                new SegundoPlano("CreaLoteRecepcionDev").execute();
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


        edtx_ConfEmpaques.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
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
                                edtx_CantidadEmpaques.setText("");

                                new popUpGenerico(Devolucion_PYU.this,getCurrentFocus(),getString(R.string.error_ingrese_lote_desc) , "false", true, true);

                            }
                        }, Devolucion_PYU.this);
                    }else{

                    new SegundoPlano("RegistrarEmpaques").execute();
                }

                return false;
            }
        });

        spnr_lote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    tv_cantReg2.setText( ((Constructor_Dato)spnr_lote.getSelectedItem()).getTag2());

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(Devolucion_PYU.this, getCurrentFocus(), e.getMessage(), "false", true, true);
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
    }
    public void LimpiarCampos()
    {
//        edtx_PrimerEmpaque.setText("");
//        edtx_UltimoEmpaque.setText("");
//        edtx_ConfEmpaques.setText("");
        edtx_PrimerEmpaque.setText("");
        edtx_UltimoEmpaque.setText("");
        edtx_CantidadPiezas.setText("");
        edtx_PrimerEmpaque.requestFocus();
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String Tarea;
        DataAccessObject dao= new DataAccessObject();
        cAccesoADatos_Almacen ca= new cAccesoADatos_Almacen(Devolucion_PYU.this);
        public SegundoPlano(String tarea)
        {
            Tarea = tarea;
            recargar = true;

        }

        @Override
        protected void onPreExecute() {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try{
                ca = new cAccesoADatos_Almacen(Devolucion_PYU.this);
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

                                dao = ca.c_PrimeraUltimaEmpaqueDev(OrdenDevolucion,Partida,"",
                                        edtx_CantidadPiezas.getText().toString(),edtx_CantidadEmpaques.getText().toString(),edtx_PrimerEmpaque.getText().toString(),
                                        edtx_UltimoEmpaque.getText().toString(),edtx_ConfEmpaques.getText().toString());
                            }else{

                            dao = ca.c_PrimeraUltimaEmpaqueDev(OrdenDevolucion,Partida,((Constructor_Dato)spnr_lote.getSelectedItem()).getDato(),
                                    edtx_CantidadPiezas.getText().toString(),edtx_CantidadEmpaques.getText().toString(),edtx_PrimerEmpaque.getText().toString(),
                                    edtx_UltimoEmpaque.getText().toString(),edtx_ConfEmpaques.getText().toString());
                        }


                        break;

                    case "CreaLoteRecepcionDev":

                        dao = ca.c_CreaLoteRecepcionDev(OrdenDevolucion,
                                Partida,
                                edtx_LoteNuevo.getText().toString(),
                                "1");
                        break;
                    case "CerrarTarima":
                        dao = ca.c_CierraPalletDevolucion(strPallet);
                        break;
                    default:
                        dao = new DataAccessObject();
                }
            }catch (Exception e){
                dao = new DataAccessObject(e);

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try
                {
                recargar = false;

                if (dao.iscEstado()){
                    switch (Tarea){
                        case "ConsultarPallet":
//                            ,

                            String CodigoPallet, Empaques;
                            String[] mensaje= dao.getcMensaje().split("#");
                            CodigoPallet = mensaje[0];
                            Empaques = mensaje[1];
                            if(!CodigoPallet.equals(""))
                            {
                                strPallet = CodigoPallet;
                                strEmpaques = Empaques;
                            }
                            break;
                        case "ListarLotes":
                            if(dao.getcTablas() != null)
                            {
                                registroAnteriorSpinner = spnr_lote.getSelectedItemPosition();
                                spnr_lote.setAdapter(new CustomArrayAdapter(Devolucion_PYU.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("LoteAXC","CantidadRecibida","CantidadRegistrada")));
                                spnr_lote.setSelection(registroAnteriorSpinner);
                            }else
                            {
                                spnr_lote.setAdapter(null);
                            }

                            break;
                        case "ConsultarPartida":
                            tv_devolucion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Devolucion"));
                            tv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            tv_Partida.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaERP"));
                            tv_cantOrig.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                            tv_cantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            tv_ProductoDet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));
                            new SegundoPlano("ListarLotes").execute();
                      break;

                        case "RegistrarEmpaques":
                            strPallet = dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet");

                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                                {
                                    new com.automatica.AXCPT.Servicios.popUpGenerico(Devolucion_PYU.this, getCurrentFocus(), getString(R.string.orden_compra_completada), String.valueOf(dao.iscEstado()), true, true);
                                    new SegundoPlano("CerrarTarima").execute();
                                    return;
                                }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                                {
                                    new popUpGenerico(Devolucion_PYU.this, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);

                                    new SegundoPlano("CerrarTarima").execute();
                                    return;
                                }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1") )
                                {
                                    new SegundoPlano("CerrarTarima").execute();
                                }
                            new SegundoPlano("ConsultarPartida").execute();
                            break;
                        case "CerrarTarima":
                            new popUpGenerico(Devolucion_PYU.this,getCurrentFocus(),getString(R.string.orden_compra_pallet_cerrado) + " [" +dao.getcMensaje() + "]"  ,true,true,true);
                            new SegundoPlano("ConsultarPallet").execute();

                            break;
                        case "CreaLoteRecepcionDev":
                            new SegundoPlano("ListarLotes").execute();

                            break;
                    }

                    LimpiarCampos();

                }else {
                    new popUpGenerico(Devolucion_PYU.this,getCurrentFocus(),dao.getcMensaje(),false,true,true);
                }
            }catch (Exception e){
                e.printStackTrace();
                new popUpGenerico(Devolucion_PYU.this,getCurrentFocus(),e.getMessage(),false,true,true);

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