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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido.Surtido_Seleccion_Partida;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Validacion_PorPallet extends AppCompatActivity implements Fragmento_Validacion.OnFragmentInteractionListener, TableViewDataConfigurator.TableClickInterface,frgmnt_taskbar_AXC.interfazTaskbar
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacion__por_pallet);
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
            edtx_OrdenCompra.setText(Documento);
            new SegundoPlano("Tabla").execute();
        }else
        {
            edtx_OrdenCompra.requestFocus();
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

                   if (TextUtils.isEmpty(edtx_OrdenCompra.getText()))
                   {
                       new popUpGenerico(contexto, getCurrentFocus(), "Ingrese un documento.",false, true, true);
                        return false;
                   }
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

            edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Pedido);
            edtx_CodigoPallet = findViewById(R.id.edtx_CodigoPallet);
            edtx_Guia = findViewById(R.id.edtx_Anden);

            edtx_OrdenCompra.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_CodigoPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Guia.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


            tabla = (SortableTableView) findViewById(R.id.tableView_OC);
            progressBarHelper = new ProgressBarHelper(this);
            creaDialogos = new CreaDialogos(contexto);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,edtx_OrdenCompra ,e.getMessage() ,false , true, true);

        }
    }
    private void AgregaListeners()
    {
        try
        {

        edtx_Guia.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                if(b)
                {
                    edtx_Guia.setText("");
                }
            }
        });

        edtx_OrdenCompra.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_OrdenCompra.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,edtx_OrdenCompra ,getString(R.string.error_ingrese_pedido) ,"false" , true, true);
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_OrdenCompra.requestFocus();
                                edtx_OrdenCompra.setText("");
                            }
                        });
                        return false;
                    }

                    new SegundoPlano("Tabla").execute();

                    new esconderTeclado(Validacion_PorPallet.this);

                }
                return false;
            }
        });
        edtx_CodigoPallet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (TextUtils.isEmpty(edtx_OrdenCompra.getText()))
                {
                    new popUpGenerico(contexto,edtx_OrdenCompra, "Ingrese un documento.",false, true, true);
                    return false;
                }
                if (TextUtils.isEmpty(edtx_Guia.getText()))
                {
                    new popUpGenerico(contexto,edtx_Guia, "Ingrese una guia.",false, true, true);
                    return false;
                }

                if (TextUtils.isEmpty(edtx_CodigoPallet.getText())){
                    new popUpGenerico(contexto,edtx_CodigoPallet, "Ingrese un código.",false, true, true);
                    return false;
                }
                new esconderTeclado(Validacion_PorPallet.this);

                new SegundoPlano("Valida").execute(edtx_CodigoPallet.getText().toString());
                return false;
            }
        });

            edtx_Guia.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(edtx_OrdenCompra.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto,null ,getString(R.string.error_ingrese_pedido) ,false , true, true);
                            h.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_OrdenCompra.requestFocus();
                                    edtx_OrdenCompra.setText("");
                                }
                            });
                        }

                        if (TextUtils.isEmpty(edtx_Guia.getText()))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese una guía.",false, true, true);
                            return false;
                        }

                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CodigoPallet.requestFocus();
                            }
                        },100);

                        new esconderTeclado(Validacion_PorPallet.this);

                    }
                    return false;
                }
            });

        edtx_OrdenCompra.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,null ,e.getMessage() ,false , true, true);

        }
    }

    private void reiniciarDatos()
    {
        edtx_CodigoPallet.setText("");
        edtx_CodigoPallet.requestFocus();
//        tabla.getDataAdapter().clear();

    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {
        if(edtx_OrdenCompra.getText().toString().equals(""))
        {
            new popUpGenerico(contexto,edtx_OrdenCompra, "Ingrese un documento.",false, true, true);
            return;
        }

        Constructor_Dato cd = Constructor_Dato.getValue(ConfigTabla_Totales.getRenglonSeleccionado(), "Estatus");

        String tmpDSts="";
        if(cd!=null)
        {
            tmpDSts = cd.getDato();

            if(tmpDSts == null)
            {
                tmpDSts = "";
            }

        }

        if(tmpDSts.equals("VALIDADO"))
        {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.contenedor,Fragmento_Validacion
                            .newInstance(edtx_OrdenCompra.getText().toString(),clickedData[0],"")).addToBackStack("").commit();
            return;
        }

        if(edtx_Guia.getText().toString().equals(""))
        {
            new popUpGenerico(contexto,edtx_Guia, "Ingrese una guía.",false, true, true);
            return;
        }
        new SegundoPlano("Valida").execute(clickedData[0]);

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {

    }
    @Override
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
                        dao = ca.c_ConsultaEmbarqueValidarPallets(edtx_OrdenCompra.getText().toString());
                        break;
                    case "Valida":
                        PalletSeleccionado = params[0];

                        dao = ca.c_ValidaEmbPallet(edtx_OrdenCompra.getText().toString(),PalletSeleccionado,edtx_Guia.getText().toString(),"1");
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
                                ConfigTabla_Totales = new TableViewDataConfigurator(strIdTabla,6, "VALIDADO", "SURTIDO", "4", tabla, dao, Validacion_PorPallet.this);
                            }else
                            {
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }

                            if(dao.getcMensaje().equals("2"))
                            {
                                creaDialogos.dialogoDefault("Cerrar Embarque","Validación del embarque completa. ¿Registrar la salida?",
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            new SegundoPlano("Embarca").execute();
                                            new esconderTeclado(Validacion_PorPallet.this);
                                        }
                                    }
                                ,null);
                            }

                            h.postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Guia.requestFocus();
                                }
                            },100);

                            break;
                        case "Valida":
                            edtx_CodigoPallet.setText("");
                            edtx_CodigoPallet.requestFocus();

                            new esconderTeclado(Validacion_PorPallet.this);

                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                        .replace(R.id.contenedor,Fragmento_Validacion
                                                .newInstance(edtx_OrdenCompra.getText().toString(),PalletSeleccionado,edtx_Guia.getText().toString())).addToBackStack("").commit();
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

                        case "Valida":
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                            edtx_Guia.setText("");
                            edtx_CodigoPallet.setText("");
                            edtx_Guia.requestFocus();
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