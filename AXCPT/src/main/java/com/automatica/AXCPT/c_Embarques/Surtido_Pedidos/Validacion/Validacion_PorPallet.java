package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido.Surtido_Seleccion_Partida;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdPiezas;
import com.automatica.AXCPT.databinding.ActivityValidacionPorPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Validacion_PorPallet extends AppCompatActivity implements frgmnt_SKU_Conteo.OnFragmentInteractionListener, TableViewDataConfigurator.TableClickInterface,frgmnt_taskbar_AXC.interfazTaskbar
{
    private EditText edtx_OrdenCompra,edtx_CodigoPallet, edtx_Guia;
    private SortableTableView tabla;
    private TableViewDataConfigurator ConfigTabla_Totales = null;
    private ProgressBarHelper progressBarHelper;
    private Context contexto = this;
    private Handler h = new Handler();
    private static String strIdTabla = "strIdTablaTotales";
    private CreaDialogos creaDialogos;
    private ActivityHelpers activityHelpers;
    private ActivityValidacionPorPalletBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityValidacionPorPalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Validacion_PorPallet.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declaraVariables();
        AgregaListeners();

        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenus(Validacion_PorPallet.this,R.id.cl,false);



        String Documento = "";


        Documento = getIntent().getExtras().getString("Documento");

        if(!Documento.equals(""))
        {
            binding.tvPedido.setText(Documento);
            new SegundoPlano("Tabla").execute();
        }else
        {
            binding.edtxCodigoPallet.requestFocus();
        }

        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);

    }
    @Override
    public void BotonDerecha() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
           switch (id){
               case R.id.recargar:
                   new SegundoPlano("Tabla").execute();
                   break;
               case R.id.borrar_datos:
                   break;
               case R.id.InformacionDispositivo:
                   new sobreDispositivo(contexto,getCurrentFocus());
                   break;
           }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {
        try
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Validación");
            getSupportActionBar().setSubtitle("Por pallet");

           // edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Pedido);
        /*    edtx_CodigoPallet = findViewById(R.id.edtx_CodigoPallet);
            edtx_Guia = findViewById(R.id.edtx_Anden);

            edtx_OrdenCompra.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Guia.setFilters(new InputFilter[]{new InputFilter.AllCaps()});*/


            tabla = (SortableTableView) findViewById(R.id.tableView_OC);
            progressBarHelper = new ProgressBarHelper(this);
            creaDialogos = new CreaDialogos(contexto);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,binding.edtxCodigoPallet ,e.getMessage() ,false , true, true);

        }
    }
    private void AgregaListeners()
    {
        try
        {
            binding.edtxCodigoPallet.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER)){
                        if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            if (binding.edtxCodigoPallet.getText().toString().equals("")){
                                new popUpGenerico(contexto, binding.edtxEmpaque, "Ingrese un carrito o pallet", false, true, true);
                                return false;
                            }
                            else{

                                if (binding.edtxCodigoPallet.getText().toString().substring(0,2).matches("99"))
                                    new SegundoPlano("ConsultaCarrito").execute();

                                else{
                                    new CreaDialogos(contexto).dialogoDefault("Validar Pallet", "El Código: [" + binding.edtxCodigoPallet.getText().toString() +"] es un pallet ¿Desea Validarlo?", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new  Validacion_PorPallet.SegundoPlano("ValidaPallet").execute();
                                        }
                                    }, new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {

                                        }
                                    });

                                }


                            }
                        }
                        new com.automatica.AXCPT.Servicios.esconderTeclado(Validacion_PorPallet.this);
                        return  true;
                    }
                    return false;
                }
            });

            binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if (binding.edtxEmpaque.getText().toString().equals("")){
                            new popUpGenerico(contexto, binding.edtxEmpaque, "Ingrese un código de empaque o SKU", false, true, true);
                            return false;
                        }

                        if (binding.edtxCodigoPallet.getText().toString().equals("")){
                            new popUpGenerico(contexto, binding.edtxEmpaque, "Ingrese un carrito o pallet", false, true, true);
                            return false;
                        }

                        new SegundoPlano("ConsultaTipo").execute();
                        new com.automatica.AXCPT.Servicios.esconderTeclado(Validacion_PorPallet.this);
                        return  true;
                    }

                    return false;
                }
            });

            binding.edtxConfirmarEmpaque.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if (binding.edtxEmpaque.getText().toString().equals("")){
                            new popUpGenerico(contexto, binding.edtxEmpaque, "Ingrese un código de empaque o SKU", false, true, true);
                            return false;
                        }

                        if (binding.edtxCodigoPallet.getText().toString().equals("")){
                            new popUpGenerico(contexto, binding.edtxEmpaque, "Ingrese un carrito o pallet", false, true, true);
                            return false;
                        }
                        if (binding.edtxConfirmarEmpaque.getText().toString().equals("")){
                            new popUpGenerico(contexto, binding.edtxConfirmarEmpaque, "Ingrese la cantidad a validar", false, true, true);
                            return false;
                        }

                        new SegundoPlano("ValidaPzas").execute();
                        new com.automatica.AXCPT.Servicios.esconderTeclado(Validacion_PorPallet.this);
                        return  true;
                    }

                    return false;
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,null ,e.getMessage() ,false , true, true);

        }
    }

    private void reiniciarDatos()
    {

        binding.edtxCodigoPallet.requestFocus();
//        tabla.getDataAdapter().clear();

    }

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
/*    @Override
    public boolean AceptarOrden()
    {
        new SegundoPlano("Tabla").execute();
        return false;
    }
    @Override
    public void EstadoCarga(Boolean Estado)
    {
        if(Estado)
        {
            progressBarHelper.ActivarProgressBar();
        }
        else
        {
            progressBarHelper.DesactivarProgressBar();
        }
    }*/

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada) {

    }

    class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String Tarea;
        String PalletSeleccionado;
        DataAccessObject dao;
        cAccesoADatos_Embarques ca= new cAccesoADatos_Embarques(contexto);
        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {
            progressBarHelper.ActivarProgressBar();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {


                switch (Tarea)
                {
                    case"Tabla":
                        dao = ca.c_ConsultaEmbarqueValidarPallets(binding.tvPedido.getText().toString());
                        break;

                    case "ConsultaCarrito":
                        dao = ca.c_ConsultaCarritoValida(binding.edtxCodigoPallet.getText().toString(),binding.tvPedido.getText().toString());
                        break;

                    case "ConsultaTipo":
                        dao = ca.c_ConsultaTipoRegValida(binding.edtxEmpaque.getText().toString(), binding.tvPedido.getText().toString(), binding.edtxCodigoPallet.getText().toString());
                        break;

                    case "ValidaEmpaque":
                        dao = ca.c_ValidaEmbEmpaque(binding.tvPedido.getText().toString(),binding.edtxEmpaque.getText().toString());
                        break;

                    case "ValidaSKU":
                        dao = ca.c_ValidaEmbSKUPzas(binding.tvPedido.getText().toString(),binding.edtxEmpaque.getText().toString(), "1");
                        break;

                    case "ValidaPzas":
                        dao = ca.c_ValidaEmbSKUCantidad(binding.tvPedido.getText().toString(),binding.edtxEmpaque.getText().toString(), binding.edtxConfirmarEmpaque.getText().toString());
                        break;

                    case "ValidaPallet":
                        dao = ca.c_ValidaEmbPallets(binding.edtxCodigoPallet.getText().toString(),binding.tvPedido.getText().toString());
                        break;

                    case "Embarca":

                        dao = ca.c_RegistrarEmbMaterial(edtx_OrdenCompra.getText().toString(),edtx_Guia.getText().toString(),"1");
                        break;

                    default:
                        dao = new DataAccessObject();
                }
            }catch (Exception e)
            {
                dao = new DataAccessObject(e);
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
                    switch (Tarea)
                    {
                        case "Tabla":
                            if(ConfigTabla_Totales == null)
                            {
                                ConfigTabla_Totales = new TableViewDataConfigurator(strIdTabla,3, "VALIDADA", "SURTIDA", "4", tabla, dao, Validacion_PorPallet.this);
                            }else
                            {
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }

                            break;

                        case "ConsultaTipo":
                            Log.e("Tipo", dao.getcMensaje());
                            if (dao.getcMensaje().equals("E")){
                                new SegundoPlano("ValidaEmpaque").execute();
                            }
                            else{
                                binding.edtxConfirmarEmpaque.setEnabled(true);
                                binding.switchSku.setEnabled(true);
                            }
                            break;
                        case "ValidaEmpaque":
                            binding.edtxEmpaque.setText("");
                            binding.edtxEmpaque.requestFocus();

                            new esconderTeclado(Validacion_PorPallet.this);
                           new SegundoPlano("Tabla").execute();
                            break;

                        case "ValidaPzas":
                            binding.edtxEmpaque.setText("");
                            binding.edtxEmpaque.requestFocus();
                            binding.edtxConfirmarEmpaque.setText("");
                            binding.edtxConfirmarEmpaque.setEnabled(false);
                            new esconderTeclado(Validacion_PorPallet.this);
                            new SegundoPlano("Tabla").execute();
                            break;

                        case "Embarca":

                            new popUpGenerico(contexto, getCurrentFocus(), "Documento embarcado con éxito." ,dao.iscEstado(), true, true);
                            break;
                    }
                }
                else
                {

                    switch (Tarea)
                    {

                        case "ValidaEmpaque":
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);

                            binding.edtxCodigoPallet.setText("");
                            break;

                        case "ValidaPzas":
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                            binding.edtxConfirmarEmpaque.setText("");
                            binding.edtxConfirmarEmpaque.setEnabled(false);
                            binding.edtxCodigoPallet.setText("");
                            break;



                        default:
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                            reiniciarDatos();
                            break;
                    }


                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);
            }
            progressBarHelper.DesactivarProgressBar();
        }
        @Override
        protected void onCancelled()
        {
            progressBarHelper.DesactivarProgressBar();
            super.onCancelled();
        }

    }

    @Override
    public void BotonIzquierda() {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }
    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

}