package com.automatica.AXCPT.c_Recepcion.Recepcion;

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
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.ReabastecimientoPicking.ReabPK_Seleccion_Material;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityPrimerayUltimaBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class PrimerayUltima extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener
{

    popUpGenerico pop = new popUpGenerico(PrimerayUltima.this);
    ActivityPrimerayUltimaBinding binding;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    frgmnt_taskbar_AXC taskbar_axc;
    String OrdenCompra,Factura,ModificaCant,PartidaERP,IdRecepcion,NumParte,UM,CantidadTotal,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,SKU;
    Bundle b;
    String NumSerie;
    private Spinner sp_NumSerie;
    private ProgressBarHelper p;
    int registroAnteriorSpinner=0;
    private EditText edtx_SKU;
    private Spinner sp_Partidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrimerayUltimaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recepción");
        getSupportActionBar().setSubtitle("Primera y Última");
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
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();
        SacaExtraIntent();
        DeclararVariables();
        AgregaListeneres();

        new SegundoPlano("ConsultaPallet").execute();
        new SegundoPlano("Tabla").execute();


        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                edtx_SKU.requestFocus();
            }
        },150);
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
        try {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {
                ReiniciarVariables();
            }
        }catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
    }

    private void SacaExtraIntent()
    {
        try
        {
            b = getIntent().getExtras();
            OrdenCompra= b.getString("Orden");
            PartidaERP= b.getString("PartidaERP");
            NumParte= b.getString("NumParte");
            UM= b.getString("UM");
            SKU= b.getString("SKU");
            CantidadTotal= b.getString("CantidadTotal");
            CantidadRecibida= b.getString("CantidadRecibida");
            CantidadEmpaques= b.getString("CantidadEmpaques");
            EmpaquesPallet= b.getString("EmpaquesPallet");
        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }

    private void DeclararVariables(){
        p = new ProgressBarHelper(this);
        binding.txtvPartida.setText(PartidaERP);
        binding.txtvCaducidad.setText(UM);
        binding.txtvProd.setText(NumParte);
        binding.txtvCantidadTotal.setText(CantidadTotal);
        binding.txtvLote.setText(CantidadRecibida);
        binding.txtvOC.setText(OrdenCompra);
        sp_NumSerie = binding.vwSpinner2.findViewById(R.id.spinner);
        sp_Partidas = binding.vwSpinner.findViewById(R.id.spinner);
        edtx_SKU = binding.edtxSKU;


    }

    private void AgregaListeneres(){

        try{

            binding.toggleNumSerie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!binding.toggleNumSerie.isChecked()){
                        binding.edtxNumSerie.setVisibility(View.VISIBLE);
                        binding.vwSpinner2.setVisibility(View.GONE);
                    }else{
                        binding.edtxNumSerie.setVisibility(View.GONE);
                        binding.vwSpinner2.setVisibility(View.VISIBLE);
                    }
                }
            });


            binding.edtxEmpaque.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {

                    if(edtx_SKU.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,edtx_SKU,"Antes de hacer el conteo, ingrese el SKU o UPC.",false,true,true);
                        return false;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, edtx_SKU.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();


                    return true;
                }
            });
            binding.edtxEmpaque.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
            {
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


            edtx_SKU.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if(edtx_SKU.getText().toString().equals(""))
                            {

                                edtx_SKU.setText("");
                                edtx_SKU.requestFocus();

                                new popUpGenerico(contexto,edtx_SKU,"Ingrese un SKU." , false, true, true);
                                return false;
                            }

                            edtx_SKU.setText(edtx_SKU.getText().toString().replace(" ","").replace("\t","").replace("\n",""));

                            int SKUSel = -2;
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,edtx_SKU.getText().toString());

                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                    new esconderTeclado(PrimerayUltima.this);
                                    return false;
                                case -1:
                                    int UPCsel= -2;
                                    UPCsel = CustomArrayAdapter.getIndex(sp_Partidas,edtx_SKU.getText().toString(),CustomArrayAdapter.TAG2);
                                    switch (UPCsel){
                                        case -2:
                                            new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                            new esconderTeclado(PrimerayUltima.this);

                                            break;
                                        case -1:
                                            new popUpGenerico(contexto,edtx_SKU,"No se encontró el SKU dentro del listado de partidas, verifique que sea correcto. [" + edtx_SKU.getText().toString() +"]" , false, true, true);
                                            new esconderTeclado(PrimerayUltima.this);

                                            break;
                                        default:
                                            sp_Partidas.setSelection(UPCsel);
                                            break;
                                    }
                                    break;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            //new esconderTeclado(PrimerayUltima.this);
                           //binding.can.requestFocus();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                        }
                    }
                    return false;
                }
            });


            sp_Partidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                {

                    binding.txtvPartida.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag1());
                    //txtv_Producto.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag2());
                    binding.txtvCantidadTotal.setText(((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag3());


                    Log.i("Tag1",((Constructor_Dato) sp_Partidas.getSelectedItem()).getTag1());
//                if(!edtx_SKU.getText().toString().equals(((Constructor_Dato) sp_Partidas.getSelectedItem()).getDato()))
//                {
                    new SegundoPlano("DetallePartida").execute();
//                }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView)
                {

                }
            });



            binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if (!binding.edtxEmpaque.getText().toString().equals("")) {
                                try {
                                    if (Float.parseFloat(binding.edtxEmpaque.getText().toString()) > 999999) {


                                        binding.edtxEmpaque.setText("");
                                        binding.edtxEmpaque.requestFocus();


                                        pop.popUpGenericoDefault(vista,getString(R.string.error_cantidad_mayor_999999),false);
                                    } else {


                                    }
                                } catch (NumberFormatException ex) {

                                    binding.edtxEmpaque.setText("");
                                    binding.edtxEmpaque.requestFocus();

                                    pop.popUpGenericoDefault(vista,getString(R.string.error_cantidad_valida),false);
                                }
                            }else
                            {

                                binding.edtxEmpaque.setText("");
                                binding.edtxEmpaque.requestFocus();

                                pop.popUpGenericoDefault(vista,getString(R.string.error_cantidad_valida),false);

                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            pop.popUpGenericoDefault(vista,e.getMessage(),false);
                        }
                    }
                    return false;
                }
            });


            binding.edtxEmpxPallet.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if (!binding.edtxEmpxPallet.getText().toString().equals(""))
                            {
                                try {
                                    if (Integer.parseInt(binding.edtxEmpxPallet.getText().toString()) > 999999)
                                    {
                                        binding.edtxEmpxPallet.setText("");
                                        binding.edtxEmpxPallet.requestFocus();
                                        pop.popUpGenericoDefault(vista,getString(R.string.error_cantidad_mayor_999999),false);
                                    } else {




                                    }
                                }catch (NumberFormatException ex)
                                {
                                    pop.popUpGenericoDefault(vista,getString(R.string.error_cantidad_valida),false);

                                            binding.edtxEmpxPallet.setText("");
                                            binding.edtxEmpxPallet.requestFocus();

                                }
                            }else
                            {
                                pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_cantidad),false);

                                        binding.edtxEmpxPallet.requestFocus();
                                        binding.edtxEmpxPallet.setText("");

                            }
                            //edtx_EmpxPallet.requestFocus();
                            // new esconderTeclado(Recepcion_Registro_PrimerasYUltimas_Spinner.this);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                           pop.popUpGenericoDefault(vista,e.getMessage(),false);
                        }
                    }
                    return false;
                }
            });



            binding.edtxPrimerEmpaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if (!binding.edtxPrimerEmpaque.getText().toString().equals(""))
                            {
                            }else
                            {

                                        binding.edtxPrimerEmpaque.setText("");
                                        binding.edtxPrimerEmpaque.requestFocus();

                               pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_empaque),false);
                            }
                            new esconderTeclado(PrimerayUltima.this);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                           pop.popUpGenericoDefault(vista,e.getMessage(),false);
                        }
                    }
                    return false;
                }
            });
            binding.edtxUltimoEmpaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if (!binding.edtxUltimoEmpaque.getText().toString().equals(""))
                            {




                            }else
                            {

                                        binding.edtxUltimoEmpaque.setText("");
                                        binding.edtxUltimoEmpaque.requestFocus();
                             pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_empaque),false);
                            }
                            new esconderTeclado(PrimerayUltima.this);
                        }catch (Exception e) {
                            e.printStackTrace();
                            pop.popUpGenericoDefault(vista,e.getMessage(),false);
                        }
                    }
                    return false;
                }
            });
            binding.edtxCantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                        {
                            if(binding.edtxEmpaque.getText().toString().equals(""))
                            {
                                pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_cantidad) + " - Cantidad",false);
                                return true;
                            }
                            if(binding.edtxEmpxPallet.getText().toString().equals(""))
                            {
                                pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_cantidad) + " - Empaques por Pallet",false);
                                return true;
                            }
                            if(binding.edtxPrimerEmpaque.getText().toString().equals(""))
                            {
                                pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_empaque) +" - Primer empaque", false);
                                return true;
                            }
                            if(binding.edtxUltimoEmpaque.getText().toString().equals(""))
                            {
                                pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_empaque) +" - Ultimo empaque", false);
                                return true;
                            }
                            if(binding.edtxCantidadEmpaques.getText().toString().equals(""))
                            {
                                pop.popUpGenericoDefault(vista,getString(R.string.error_ingrese_cantidad)+" - Validación " +  " cantidad empaques",false);
                                return true;
                            }


                            if(Integer.parseInt(binding.edtxEmpxPallet.getText().toString()) <=0)
                            {
                                pop.popUpGenericoDefault(vista,"La cantidad ingresada no puede ser menor o igual a cero.",false);
                                return false;
                            }
                            if(Float.parseFloat(binding.edtxEmpaque.getText().toString()) <=0)
                            {
                                pop.popUpGenericoDefault(vista,"La cantidad de empaques no puede ser menor o igual a cero.",false);
                                return false;
                            }
                            //validacionFinal();
                            new SegundoPlano("RegistrarEmpaqueNuevo").execute();
                            new esconderTeclado(PrimerayUltima.this);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                           pop.popUpGenericoDefault(vista,e.getMessage(),false);
                        }
                    }
                    return false;
                }
            });

            binding.txtvPallet.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(binding.txtvPallet.getText().toString().trim().equals("") || binding.txtvPallet.getText().toString().trim().equals("-")){

                        new popUpGenerico(contexto,getCurrentFocus(),"Se debe crear un pallet para poder continuar",false,true,true);
                    }else{

                        String[] datos = { binding.txtvPallet.getText().toString() };
                        taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance( datos, FragmentoConsulta.TIPO_PALLET), FragmentoConsulta.TAG);
                    }

                    return false;
                }
            });


        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }
    }

    private void ReiniciarVariables()
    {
        try {
            binding.edtxPrimerEmpaque.setText("");
            binding.edtxUltimoEmpaque.setText("");
            binding.edtxCantidadEmpaques.setText("");
            binding.edtxEmpaque.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
           pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }
    }

    @Override
    public void BotonDerecha() {
        try
        {
            if(!binding.txtvEmpReg.getText().toString().equals(""))
            {
                if (Integer.parseInt(binding.txtvEmpReg.getText().toString())>0)
                {
                    new CreaDialogos("¿Cerrar tarima?",
                            new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    new SegundoPlano("RegistraPalletNuevo").execute();
                                }
                            },null,contexto);
                }
                else
                {
                   pop.popUpGenericoDefault(vista,getString(R.string.error_pallet_no_seleccionado),false);
                }
            }else
            {
                pop.popUpGenericoDefault(vista,getString(R.string.error_pallet_no_seleccionado),false);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada)
    {
        binding.edtxEmpaque.setText(strCantidadEscaneada);
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                binding.edtxPrimerEmpaque.requestFocus();
            }
        },100);
    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>{

        DataAccessObject dao;
        cAccesoADatos_Recepcion ca  = new cAccesoADatos_Recepcion(PrimerayUltima.this);
        String tarea;
        View LastView;
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
                pop.popUpGenericoDefault(vista,e.getMessage(),false);
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try{

                switch (tarea)
                {

                    case"Tabla":
                        dao = ca.c_ListarPartidasOCEnProceso(OrdenCompra);
                        break;

                    case"DetallePartida":
                        dao = ca.c_detalleReciboPartida(OrdenCompra,binding.txtvPartida.getText().toString());
                        break;


                    case "ConsultaPallet":

                        dao= ca.c_ConsultaPalletAbiertoOC(OrdenCompra, PartidaERP);
                        break;

                    case "RegistrarEmpaqueNuevo":
                        if(!binding.toggleNumSerie.isChecked()){
                            NumSerie = binding.edtxNumSerie.getText().toString();
                        }
                        else{
                            NumSerie = ((Constructor_Dato)sp_NumSerie.getSelectedItem()).getDato();
                        }

                            dao = ca.c_RegistraMPPrimerasYUltimas(OrdenCompra,
                                    binding.txtvPartida.getText().toString(),
                                    "",
                                    binding.edtxEmpaque.getText().toString(),
                                    binding.edtxEmpxPallet.getText().toString(),
                                    binding.edtxPrimerEmpaque.getText().toString(),
                                    binding.edtxUltimoEmpaque.getText().toString(),
                                    binding.edtxCantidadEmpaques.getText().toString(),
                                    NumSerie)
                                   ;

                        break;

                    case "RegistraPalletNuevo":

                        dao = ca.c_CierraPalletCompra(binding.txtvPallet.getText().toString());
                        break;


                    case "CerrarRecepcion":

                        dao = ca.c_CerrarRecepcion(OrdenCompra);

                        break;
                    default:

                        dao =new DataAccessObject();

                        break;

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
            try
            {

                if(dao.iscEstado())
                {
                    switch (tarea)
                    {

                        case"Tabla":
                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(PrimerayUltima.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }




                            int SKUSel = -2;
                            SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU);

                            switch(SKUSel)
                            {
                                case -2:
                                    new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                    new esconderTeclado(PrimerayUltima.this);
                                    edtx_SKU.setText("");
                                    edtx_SKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    new popUpGenerico(contexto,edtx_SKU,"No se encontró el SKU dentro del listado de partidas, verifique que sea correcto. [" + SKU +"]" , false, true, true);
                                    new esconderTeclado(PrimerayUltima.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            new esconderTeclado(PrimerayUltima.this);
                            SKU = "DEFAULT";
                            break;

                        case"DetallePartida":
                            binding.txtvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            binding.txtvProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProducto"));


                            break;


                        case "RegistrarEmpaqueNuevo":
                            binding.txtvPallet.setText(CantidadRecibida);
                            binding.edtxPrimerEmpaque.setText("");
                            binding.edtxUltimoEmpaque.setText("");
                            binding.edtxCantidadEmpaques.setText("");


                            binding.txtvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            binding.txtvEmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));

                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");

                            new esconderTeclado(PrimerayUltima.this);

                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                                //   new SegundoPlano("CerrarRecepcion").execute();
                               new SegundoPlano("RegistraPalletNuevo").execute();
                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new SegundoPlano("Tabla").execute();

                                new SegundoPlano("RegistraPalletNuevo").execute();
                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1") )
                            {
                                new SegundoPlano("RegistraPalletNuevo").execute();
                            }


                            break;


                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, getCurrentFocus(),"Pallet ["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);

                            new SegundoPlano("ConsultaPallet").execute();

                            ReiniciarVariables();
                            break;


                        case "CerrarRecepcion":

                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);

                            break;
                        case "ConsultaPallet":

                            binding.txtvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            binding.txtvEmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));
                            binding.edtxEmpaque.requestFocus();
                            break;
                    }
                }
                else
                {
                    switch (tarea)
                    {
                        case "RegistrarEmpaqueNuevo":
                            if (dao.getcMensaje().contains("Error SAP"))
                            {
                                CreaDialogos cd = new CreaDialogos(contexto);


                                String sourceString = "<p>Se ha presentado un error al registrar en sap.</p> <p>" + dao.getcMensaje() +"</p>  <p><b>¿Registrar de todas maneras en AXC?</b></p>";

                                cd.dialogoDefault("VALIDE LA INFORMACIÓN", Html.fromHtml(sourceString),
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {

                                            }
                                        }, null);
                                break;
                            }

                        default:
                            ReiniciarVariables();
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            }
           p.DesactivarProgressBar(tarea);
        }

    }

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