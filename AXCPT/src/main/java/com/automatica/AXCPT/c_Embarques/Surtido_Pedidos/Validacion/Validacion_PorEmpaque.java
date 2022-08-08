package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
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

import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import java.util.Comparator;
import de.codecrafters.tableview.SortableTableView;

public class Validacion_PorEmpaque extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, fragmento_validacion_transporte.fragValInterface, frgmnt_taskbar_AXC.interfazTaskbar
{
    private EditText edtx_OrdenCompra,edtx_CodigoEmpaque, edtx_Anden;
    private SortableTableView tabla_Totales,tabla_empaques;
    private ProgressBarHelper progressBarHelper;
    private Context contexto = this;
    private String strProducto = "@";
    private Handler h = new Handler();
    private boolean  inflate=false,Recargar;
    private static String strIdTablaTotales = "strIdTablaTotales";
    private static String strIdTablaEmpaques = "strIdTablaEmpaques";
    private TableViewDataConfigurator ConfigTabla_Totales = null, ConfigTabla_Empaques = null;
    private CreaDialogos creaDialogos;
    private ActivityHelpers activityHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacion__por_empaque);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Validacion_PorEmpaque.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declaraVariables();
        AgregaListeners();
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(this,R.id.cl);
        activityHelpers.AgregarTaskBar(this,R.id.cl,false);
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
    public void BotonIzquierda() {
        onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.fragm_rechazar_menu, menu);
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
        switch (id)
        {

            case R.id.CambiarVista:
                if (!inflate)
                {
                    if (TextUtils.isEmpty(edtx_OrdenCompra.getText()))
                    {
                        new popUpGenerico(contexto, getCurrentFocus(), "Ingrese un documento de embarque.", false, true, true);
                        return false;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.contenedor, fragmento_validacion_transporte.newInstance("Empaque", "")).addToBackStack("")
                            .commit();
                }
                break;
            case R.id.recargar:
                if (!edtx_OrdenCompra.getText().equals(""))
                {
                    new SegundoPlano("TablaTotales").execute();
                }
                break;
            case R.id.borrar_datos:
                reiniciarDatos();
                break;
            case R.id.InformacionDispositivo:
                new sobreDispositivo(contexto, getCurrentFocus());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Validación");
        getSupportActionBar().setSubtitle("Por paquete");
//        hsv_tabla_embarques tableView_OC
        tabla_Totales = (SortableTableView) findViewById(R.id.customtableview_totales).findViewById(R.id.tableView_OC);
        tabla_empaques = (SortableTableView) findViewById(R.id.customtableview_empaques).findViewById(R.id.tableView_OC);

        edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Pedido);
        edtx_CodigoEmpaque = findViewById(R.id.edtx_CodigoEmpaque);
        edtx_Anden = findViewById(R.id.edtx_Anden);

        edtx_OrdenCompra.requestFocus();

        progressBarHelper = new ProgressBarHelper(this);
        creaDialogos = new CreaDialogos(contexto);

    }
    private void AgregaListeners()
    {
        edtx_OrdenCompra.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_OrdenCompra.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,null ,getString(R.string.error_ingrese_pedido) ,"false" , true, true);
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
                        new SegundoPlano("TablaTotales").execute();
                    new esconderTeclado(Validacion_PorEmpaque.this);

                }
                return false;
            }
        });
        edtx_Anden.setOnKeyListener(new View.OnKeyListener()
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

                    if (TextUtils.isEmpty(edtx_Anden.getText()))
                    {
                        new popUpGenerico(contexto, getCurrentFocus(), "Ingrese una guía.",false, true, true);
                        return false;
                    }

                    edtx_CodigoEmpaque.requestFocus();
                    new esconderTeclado(Validacion_PorEmpaque.this);

                }
                return false;
            }
        });

        edtx_CodigoEmpaque.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (tabla_Totales.getDataAdapter().getCount()<1||tabla_Totales.getDataAdapter() == null)
                {
                    new popUpGenerico(contexto, edtx_OrdenCompra, "Consulta una orden de surtido",false, true, true);
                    return false;
                }
                if (TextUtils.isEmpty(edtx_Anden.getText()))
                {
                    new popUpGenerico(contexto,edtx_Anden, "Ingrese un guía.",false, true, true);
                    return false;
                }
                if (TextUtils.isEmpty(edtx_CodigoEmpaque.getText()))
                {
                    new popUpGenerico(contexto,edtx_CodigoEmpaque, "Ingrese un paquete.",false, true, true);
                    return false;
                }
                new SegundoPlano("Valida").execute();
                return false;
            }
        });
    }

    @Override
    public void Inflate(boolean estado) {
        inflate=estado;
    }

    @Override
    public void Clicked() {

    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header,String IdConfigurador) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto,String IdConfigurador) {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado,String IdConfigurador)
    {
        if(Seleccionado)
        {
            if(IdConfigurador.equals(strIdTablaTotales))
            {
                if (!edtx_CodigoEmpaque.getText().equals(strIdTablaTotales))
                {
                    strProducto = clickedData[4];
                    new SegundoPlano("TablaEmpaques").execute(clickedData[4]);
                } else
                {
                    tabla_empaques.getDataAdapter().clear();
                    tabla_empaques.getDataAdapter().notifyDataSetChanged();
                }
            }
        }
    }

    private void reiniciarDatos()
    {
        edtx_CodigoEmpaque.setText("");
//        tabla_Totales.getDataAdapter().clear();
    }
    class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Embarques ca= new cAccesoADatos_Embarques(contexto);
        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {
            Recargar = false;
            progressBarHelper.ActivarProgressBar();
        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
            {
                switch (Tarea)
                {
                    case"TablaTotales":
                        dao= ca.c_ConsultaEmbarqueValidarTotales(edtx_OrdenCompra.getText().toString());
                        break;
                    case"TablaEmpaques":
                        dao= ca.c_ConsultaEmbarqueValidarEmpaques(edtx_OrdenCompra.getText().toString(),params[0]);
                        break;
                    case "Valida":
                       // dao = ca.c_ValidaEmbEmpaque(edtx_OrdenCompra.getText().toString(),edtx_CodigoEmpaque.getText().toString(),edtx_Anden.getText().toString());
                        break;
                    case "Embarca":

                        dao = ca.c_RegistrarEmbMaterial(edtx_OrdenCompra.getText().toString(),edtx_Anden.getText().toString(),"1");
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
                        case "TablaTotales":
                            if(ConfigTabla_Totales == null)
                            {
                                ConfigTabla_Totales = new TableViewDataConfigurator(strIdTablaTotales,3, "VALIDADO", "SURTIDO", "4", tabla_Totales, dao, Validacion_PorEmpaque.this);
                            }else
                            {
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                                strProducto = "@";
                            }

                            if(dao.getcMensaje().equals("2"))
                            {
                                creaDialogos.dialogoDefault("Cerrar Embarque","Validación del material completa. ¿Registrar el embarque?",
                                        new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                new SegundoPlano("Embarca").execute();
                                                new esconderTeclado(Validacion_PorEmpaque.this);
                                            }
                                        }
                                        ,null);
                            }

                            edtx_Anden.requestFocus();
                            break;
                        case "TablaEmpaques":
                            if(ConfigTabla_Empaques == null)
                            {
                                ConfigTabla_Empaques = new TableViewDataConfigurator(strIdTablaEmpaques,4, "VALIDADO", "SURTIDO", "4", tabla_empaques, dao, Validacion_PorEmpaque.this);
                            }else
                            {
                                ConfigTabla_Empaques.CargarDatosTabla(dao);
                            }
                            break;
                        case "Valida":
                            new SegundoPlano("TablaTotales").execute();
                            new SegundoPlano("TablaEmpaques").execute(strProducto);

                            edtx_CodigoEmpaque.setText("");
                            edtx_Anden.setText("");
                            edtx_Anden.requestFocus();
                            break;
                        case "Embarca":

                            new popUpGenerico(contexto, getCurrentFocus(), "Documento embarcado con éxito." ,dao.iscEstado(), true, true);

                            break;
                    }
                }
                else
                {
                    new popUpGenerico(contexto, null, dao.getcMensaje(),dao.iscEstado(), true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, null, e.getMessage(), dao.iscEstado(), true, true);
            }
            progressBarHelper.DesactivarProgressBar();

            Recargar = true;
        }
        @Override
        protected void onCancelled()
        {
            Log.e("SP", "onCancelled: hilo terminado" );
            super.onCancelled();
        }

    }

    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }
}