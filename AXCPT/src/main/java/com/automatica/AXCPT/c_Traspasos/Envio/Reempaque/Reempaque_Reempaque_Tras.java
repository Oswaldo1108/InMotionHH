package com.automatica.AXCPT.c_Traspasos.Envio.Reempaque;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;

import com.automatica.AXCPT.c_Embarques.Reempaque.Reempaque_Ciesa.Reempaque_Reempaque;
import com.automatica.AXCPT.c_Traspasos.Envio.Validacion.Validacion_PorPallet_Tras;
import com.automatica.AXCPT.databinding.ActivityReempaqueReempaqueTrasBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Reempaque_Reempaque_Tras extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_Seleccion_Producto.OnFragmentInteractionListener, frgmnt_SKU_Conteo.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private ActivityHelpers activityHelpers;
    private ProgressBarHelper p;
    private ActivityReempaqueReempaqueTrasBinding binding;
    private Context contexto = Reempaque_Reempaque_Tras.this;
    private Bundle b;
    private String orden;
    private CreaDialogos creaDialogos;
//  private Spinner spinnerDocumentos;
    private TableViewDataConfigurator tblcnf_Partidas;
    private TableViewDataConfigurator tblcnf_Carrito;
    private TableViewDataConfigurator tblcnf_ReempaqueAbierto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            binding = ActivityReempaqueReempaqueTrasBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            InicializarVariables();
            DeclararListeners();
          //  binding.edtxSKU.setEnabled(false);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),"Llene el campo carrito",false,true,true);

        }

    }


    private void InicializarVariables()
    {
        try
        {
            p = new ProgressBarHelper(this);
            activityHelpers = new ActivityHelpers();
            activityHelpers.AgregarMenus(Reempaque_Reempaque_Tras.this, R.id.Pantalla_principal, false);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Reempaque");
            getSupportActionBar().setSubtitle("Surtido");
            b = getIntent().getExtras();
            orden = b.getString("Orden");

            //spinnerDocumentos = binding.vwSpinner.findViewById(R.id.spinner);
            creaDialogos = new CreaDialogos(contexto);


            if(!orden.equals(""))
            {
                binding.txtvDocumento.setText(orden);
                new SegundoPlano("ConsultaTabla").execute();
            }else
            {
                binding.edtxCarrito.requestFocus();
            }

            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));

            tblcnf_Partidas = TableViewDataConfigurator.newInstance(binding.customtableview.tableViewOC, null, Reempaque_Reempaque_Tras.this, new TableViewDataConfigurator.TableClickInterface()
            {
                @Override
                public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
                {

                }

                @Override
                public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
                {

                }

                @Override
                public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
                {

                }
            });

            tblcnf_Carrito = TableViewDataConfigurator.newInstance(binding.customtableview2.tableViewOC, null, Reempaque_Reempaque_Tras.this, new TableViewDataConfigurator.TableClickInterface()
            {
                @Override
                public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
                {

                }

                @Override
                public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
                {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, clickedData[4]), "Fragmentosku").addToBackStack("Fragmentosku").commit();
                }

                @Override
                public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
                {
                    /*binding.edtxSKU.setEnabled(Seleccionado);
                    if (Seleccionado)
                    {
                        Constructor_Dato cd = Constructor_Dato.getValue(tblcnf_Carrito.getRenglonSeleccionado(), "TipoReg");
                        if (cd != null)
                        {
                            String tmpTipoReg = cd.getDato();
                            if (tmpTipoReg == null)
                            {
                                tmpTipoReg = "";
                            }
                            if (tmpTipoReg.equals("NE") || tmpTipoReg.equals("C"))
                            {
                                binding.textView168.setText("SKU");
                                binding.edtxSKU.setHint("Capture SKU");

                            } else
                            {

                                binding.textView168.setText("Código");
                                binding.edtxSKU.setHint("Capture código de empaque");
                            }
                        }
                    }*/
                }
            });


            tblcnf_ReempaqueAbierto = TableViewDataConfigurator.newInstance(binding.customtableview3.tableViewOC, null, Reempaque_Reempaque_Tras.this, new TableViewDataConfigurator.TableClickInterface()
            {
                @Override
                public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
                {

                }

                @Override
                public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
                {

                }

                @Override
                public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
                {

                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
        }
    }

    private void DeclararListeners()
    {
        try
        {

           /* spinnerDocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    new SegundoPlano("ConsultaTabla").execute();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });*/
            binding.edtxCarrito.setOnEditorActionListener(new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {
                    if (TextUtils.isEmpty(binding.edtxCarrito.getText()))
                    {
                        new popUpGenerico(contexto, getCurrentFocus(), "Llene el campo carrito", false, true, true);
                        binding.edtxCarrito.requestFocus();
                        return false;
                    }
                   /* if (TextUtils.isEmpty(spinnerDocumentos.getSelectedItem().toString()))
                    {
                        new popUpGenerico(contexto, getCurrentFocus(), "Seleccione un documento", false, true, true);
                        binding.edtxCarrito.requestFocus();
                        return false;
                    }*/
                    new SegundoPlano("ConsultaCarrito").execute();
                    binding.edtxSKU.requestFocus();
                    return true;
                }
            });

            binding.edtxSKU.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v) {
                    final Vibrator vibratorService = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
                    //vibratorService.vibrate(150);

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.cl, frgmnt_Seleccion_Producto.newInstance(null,""), "ElegirPallet").addToBackStack("ElegirPallet")
                            .commit();
                    return true;
                }
            });

            binding.edtxSKU.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
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

            binding.edtxSKU.setOnEditorActionListener(new TextView.OnEditorActionListener()
            {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
                {

                    try
                    {

                       /* if (!tblcnf_Carrito.renglonEstaSelec())
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), "Seleccione un producto.", false, true, true);
                            binding.txtvConsolidado.requestFocus();
                            return false;
                        }*/

                        if (TextUtils.isEmpty(binding.txtvDocumento.getText()))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), "Seleccione un documento", false, true, true);
                            binding.txtvDocumento.requestFocus();
                            return false;
                        }
                        if (TextUtils.isEmpty(binding.edtxCarrito.getText()))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), "Llene el campo carrito", false, true, true);
                            binding.edtxCarrito.requestFocus();
                            return false;
                        }

                        new SegundoPlano("ConsultaEmpaqueoSKU").execute();

                        /*** SE COMENTO PARA MANDAR UNA CONSULTA PARA DESPUES REALIZAR EL REGISTRO ***/
                       /* Constructor_Dato cd = Constructor_Dato.getValue(tblcnf_Carrito.getRenglonSeleccionado(), "TipoReg");

                        if (cd != null)
                        {
                            String tmpTipoReg = cd.getDato();

                            if (tmpTipoReg == null)
                            {
                                tmpTipoReg = "";
                            }

                            String tmpSKU = "";
                            String tmpUPC = "";

                            cd = Constructor_Dato.getValue(tblcnf_Carrito.getRenglonSeleccionado(), "SKU");
                            if (cd != null)
                            {
                                tmpSKU = cd.getDato();
                                if (tmpSKU == null)
                                {
                                    tmpSKU = "";
                                }
                            }
                            cd = Constructor_Dato.getValue(tblcnf_Carrito.getRenglonSeleccionado(), "UPC");
                            if (cd != null)
                            {
                                tmpUPC = cd.getDato();
                                if (tmpUPC == null)
                                {
                                    tmpUPC = "";
                                }
                            }

                            if (tmpTipoReg.equals("NE") || tmpTipoReg.equals("C"))
                            {
                                if (TextUtils.isEmpty(binding.edtxSKU.getText()))
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), "Llene el campo SKU", false, true, true);
                                    binding.edtxSKU.requestFocus();
                                    return false;
                                }

                                if (!binding.edtxSKU.getText().toString().equals(tmpSKU) && !binding.edtxSKU.getText().toString().equals(tmpUPC))
                                {
                                    new popUpGenerico(contexto,binding.edtxSKU, (Exception) Html.fromHtml("<p>El código ingresado no coincide con los datos del producto.</p> <p>Datos producto seleccionado: </p>  <p><b>SKU: " + tmpSKU + "</b></p> <p><b>" + "UPC: " + tmpUPC + "</b></p>  <p><b>" + "Ingresado: " + binding.edtxSKU.getText() + "</b></p>"), false,true,true);
                                    binding.edtxSKU.requestFocus();
                                    return false;
                                }

                                new SegundoPlano("RegistraEmpaqueConsSKU").execute(tmpSKU);
                            } else
                            {
                                if (TextUtils.isEmpty(binding.edtxSKU.getText()))
                                {
                                    new popUpGenerico(contexto, getCurrentFocus(), "Llene el campo código", false, true, true);
                                    binding.edtxSKU.requestFocus();
                                    return false;
                                }
                                new SegundoPlano("RegistraEmpaqueCons").execute(tmpSKU);
                            }
                        }*/
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e, false, true, true);

                    }

                    return false;
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.reempaque_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        if (p.ispBarActiva())
        {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.InformacionDispositivo:
                    new sobreDispositivo(contexto, getCurrentFocus());
                    return true;
                case R.id.CambiarVista:
                    if (binding.clEmpaqueRegistro.getVisibility() == View.VISIBLE)
                    {
                        binding.clEmpaqueRegistro.setVisibility(View.GONE);
                        binding.clTablaRegistro.setVisibility(View.VISIBLE);
                        item.setIcon(R.drawable.ic_add_box);
                        item.setChecked(true);
                        new SegundoPlano("ConsultaPalletAbierto").execute();

                    } else if (binding.clTablaRegistro.getVisibility() == View.VISIBLE)
                    {
                        binding.clTablaRegistro.setVisibility(View.GONE);
                        binding.clEmpaqueRegistro.setVisibility(View.VISIBLE);
                        item.setIcon(R.drawable.ic_change_view);

                        if (!TextUtils.isEmpty(binding.edtxCarrito.getText())){
                            new SegundoPlano("ConsultaCarrito").execute();
                        }
                    }
                    break;
                case R.id.recargar:
                    //new SegundoPlano("ConsultarDocumento").execute();
                    new SegundoPlano("ConsultaTabla").execute();
                    if (!TextUtils.isEmpty(binding.txtvPallet.getText())){
                        new SegundoPlano("ConsultaPalletAbierto").execute();
                    }
                    if (!TextUtils.isEmpty(binding.edtxCarrito.getText())){
                        new SegundoPlano("ConsultaCarrito").execute();
                    }
                    break;
                case R.id.CerrarPicking:
                    creaDialogos.dialogoDefault("Cerrar Traspaso","Validación del traspaso completa. ¿Registrar la salida?",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    new Reempaque_Reempaque_Tras.SegundoPlano("Embarca").execute();
                                    new com.automatica.axc_lib.Servicios.esconderTeclado(Reempaque_Reempaque_Tras.this);
                                }
                            }
                            ,null);
                    break;
                case R.id.borrar_datos:
                    binding.edtxCarrito.setText("");
                    binding.edtxSKU.setText("");
                    binding.customtableview.tableViewOC.getDataAdapter().clear();
                    binding.customtableview3.tableViewOC.getDataAdapter().clear();
                    break;

            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.CERRAR_REEMPAQUE);
        super.onPostCreate(savedInstanceState);
    }
    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

    @Override
    public void BotonDerecha()
    {
        if (binding.customtableview3.tableViewOC.getChildCount() > 0)
        {
            new CreaDialogos(contexto).dialogoDefault("AXC", "¿Cerrar Reempaque? Código: [" + binding.txtvPallet.getText() +"]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new SegundoPlano("Cerrar").execute();
                }
            }, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });

        } else {
            new popUpGenerico(contexto, getCurrentFocus(), "No hay pallet abierto", false, true, true);

        }
    }

    @Override
    public void BotonIzquierda()
    {
        onBackPressed();
    }


    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        //new SegundoPlano("ConsultarDocumento").execute();
        new SegundoPlano("ConsultaPalletAbierto").execute();
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada) {
        Log.e("Producto", Producto);
        Log.e("Cantidad", strCantidadEscaneada);
        new Reempaque_Reempaque_Tras.SegundoPlano("RegistraEmpaqueCant").execute(Producto,strCantidadEscaneada);
    }


    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {
        try
        {
            Handler h = new Handler();
            binding.edtxSKU.setText(prmProducto);

            if(binding.edtxSKU.getText().toString().equals(""))
            {
                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        binding.edtxSKU.requestFocus();

                    }
                },100);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {
        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia ca = new cAccesoADatos_Transferencia(contexto);

        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            p.ActivarProgressBar(Tarea);
        }

        @Override
        protected Void doInBackground(String... strings)
        {
            try
            {
                switch (Tarea)
                {
                  /*  case "ConsultarDocumento":
                        dao = ca.cConsultaListaConsolidadosReempaqueTras(binding.txtvConsolidado.getText().toString());
                        break;*/
                    case "ConsultaTabla":
                        dao = ca.cad_ConsultaPedidoSurtidoReempaqueTras(binding.txtvDocumento.getText().toString());
                        break;
                    case "ConsultaCarrito":
                        dao = ca.cad_ConsultaCarritoEmpaquesTras(binding.txtvDocumento.getText().toString(),binding.edtxCarrito.getText().toString());
                        break;
                    case "ConsultaPalletAbierto":
                        dao = ca.cCreaPalletReempaqueTras(binding.txtvDocumento.getText().toString());
                        break;
                    case "ConsultaEmpaquesPalletCons":
                        dao = ca.cConsultaPalletConsTras(binding.txtvPallet.getText().toString());
                        break;
                    case "RegistraEmpaqueNEOC":
                        dao = ca.cRegistraReempaqueConsSKUTras(binding.txtvDocumento.getText().toString(),binding.txtvPallet.getText().toString(),
                                binding.edtxCarrito.getText().toString(),strings[0],strings[0]);
                        break;

                    case "RegistraEmpaqueCant":
                        dao = ca.cRegistraReempaqueConsCantidadTras(binding.txtvDocumento.getText().toString(),binding.txtvPallet.getText().toString(),
                                binding.edtxCarrito.getText().toString(),strings[0], strings[1]);
                        break;

                    case "RegistraEmpaqueConsSKU":
                        dao = ca.cRegistraReempaqueConsSKUTras(binding.txtvDocumento.getText().toString(),binding.txtvPallet.getText().toString(),
                                                            binding.edtxCarrito.getText().toString(),binding.edtxSKU.getText().toString(),binding.edtxSKU.getText().toString());
                        break;
                    case "RegistraEmpaqueCons":
                        dao = ca.cRegistraReempaqueConsTras(binding.txtvDocumento.getText().toString(),binding.txtvPallet.getText().toString(),
                                                        binding.edtxCarrito.getText().toString(),strings[0],binding.edtxSKU.getText().toString());
                        break;
                    case "Cerrar":
                        dao = ca.cCerrarReempaqueTras(binding.txtvDocumento.getText().toString(), binding.txtvPallet.getText().toString());
                        break;
                    case "Embarca":
                        dao = ca.c_RegistrarEmbMaterialTras(binding.txtvDocumento.getText().toString(),"","2");
                        break;
                    case "ConsultaEmpaqueoSKU":
                        dao = ca.cConsultaEmpaqueoSKUTras(binding.edtxSKU.getText().toString());
                        break;
                    default:
                        dao = new DataAccessObject();break;

                }

            } catch (Exception e)
            {
                e.printStackTrace();
                dao = new DataAccessObject(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            try
            {
                if (dao.iscEstado())
                {
                    switch (Tarea)
                    {
                        /*case "ConsultarDocumento":
                            //spinnerDocumentos.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("OrdenEmbarque","OrdenEmbarque")));
                            break;*/

                        case "ConsultaTabla":
                            if(tblcnf_Partidas!=null)
                            {
                                tblcnf_Partidas.CargarDatosTabla(dao);
                            }

                            if(!binding.edtxCarrito.getText().toString().equals(""))
                            {
                                new SegundoPlano("ConsultaCarrito").execute();
                            }
                            //binding.edtxCarrito.requestFocus();

                            break;
                        case "Embarca":
                            new popUpGenerico(contexto, getCurrentFocus(), "Documento cerrado con éxito", true, true, true);
                            break;
                        case "ConsultaCarrito":

                            if(tblcnf_Carrito!=null)
                            {
                                tblcnf_Carrito.CargarDatosTabla(dao);
                            }

                            binding.edtxSKU.requestFocus();

                            new esconderTeclado(Reempaque_Reempaque_Tras.this);

                            break;
                        case "ConsultaPalletAbierto":
                            binding.txtvPallet.setText(dao.getcMensaje());
                            new SegundoPlano("ConsultaEmpaquesPalletCons").execute();
                            break;
                        case "ConsultaEmpaquesPalletCons":

                            if(tblcnf_ReempaqueAbierto!=null)
                            {
                                tblcnf_ReempaqueAbierto.CargarDatosTabla(dao);
                            }
                            new esconderTeclado(Reempaque_Reempaque_Tras.this);
                            break;
                        case "RegistraEmpaqueConsSKU":
                        case "RegistraEmpaqueNEOC":
                        case "RegistraEmpaqueCons":
                        case "RegistraEmpaqueCant":

                            new SegundoPlano("ConsultaTabla").execute();

                            binding.edtxSKU.setText("");
                            binding.edtxSKU.requestFocus();
                            new esconderTeclado(Reempaque_Reempaque_Tras.this);
                            break;
                        case "Cerrar":
                            if (binding.customtableview3.tableViewOC.getDataAdapter() != null) {
                                binding.customtableview3.tableViewOC.getDataAdapter().clear();
                            }
                            binding.txtvPallet.setText("-");
                            binding.edtxSKU.setText("");
                            new popUpGenerico(contexto, getCurrentFocus(), "Reempaque cerrado con éxito", true, true, true);
                            break;
                        case "ConsultaEmpaqueoSKU":

                            switch (dao.getSoapObject_parced().getPrimitivePropertyAsString("TipoReg")){

                                case  "NE":
                                case   "C":
                                   // binding.edtxSKU.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("SKU"));
                                    new SegundoPlano("RegistraEmpaqueNEOC").execute(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                    break;
                                case "SKU":
                                    new SegundoPlano("RegistraEmpaqueConsSKU").execute(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                    break;
                                default:
                                    new SegundoPlano("RegistraEmpaqueCons").execute(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                    break;
                            }


                            break;
                    }
                } else
                    {
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), false, true, true);

                    switch (Tarea)
                    {
                        case "ConsultaTabla":
                            tblcnf_Partidas.CargarDatosTabla(null);
                            break;
                        case "ConsultaCarrito":
                            tblcnf_Carrito.CargarDatosTabla(null);
                            break;
                        case "ConsultaPalletAbierto":
                            tblcnf_ReempaqueAbierto.CargarDatosTabla(null);
                            break;
                        case "Embarca":
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), true, true, true);
                            break;

                        case "RegistraEmpaqueConsSKU":
                        case "RegistraEmpaqueNEOC":
                        case "RegistraEmpaqueCons":

                            binding.edtxSKU.setText("");
                            binding.edtxSKU.requestFocus();
                            break;

                    }

                }
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, null, e.getMessage(), false, true, true);
            }
            p.DesactivarProgressBar(Tarea);
        }
    }
}