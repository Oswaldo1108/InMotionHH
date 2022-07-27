package com.automatica.AXCPT.c_Almacen.Reubicacion;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Seleccion_Orden;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionarPartidas;
import com.automatica.AXCPT.c_Recepcion.Recepcion.Recepcion_Refacciones;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Reubicacion_Por_Cantidad extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface,frgmnt_taskbar_AXC.interfazTaskbar
{
    //region variables

   //LISTA DE SPS QUE EJECUTA ESTA PANTALLA

    //spUpdReubicaCantidad
    private ActivityHelpers activityHelpers;

    private Toolbar toolbar;
    private EditText edtx_Codigo,edtx_Posicion, edtx_SKU, edtx_NuevoContenedor,edtx_Piezas;
    private SortableTableView table;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    private ProgressBarHelper p;
    TextView txtv_ProdCont,txtv_ContSel, txtv_CantPaq,txtv_SugPosReub,txtv_SugCont;

    private Handler handler = new Handler();
    private Context contexto = this;

    private static final String ConsultaCodigo    = "ConsultaCodigo";

    private Switch sw_SKU;
    private static final String ReubicarProducto = "ReubicarProducto";
    private static final String ReubicarProductoSKU = "ReubicarProductoSKU";
    private static final String ConsultaContenedor         = "ConsultaContenedor";
    private static final String SugReubi         = "SugReubi";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                super.onCreate(savedInstanceState);
                setContentView(R.layout.act_reubicacion_por_cantidad);
                new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Reubicacion_Por_Cantidad.this);
                declararVariables();
                agregaListeners();

                edtx_Codigo.requestFocus();

                sw_SKU.setChecked(true);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);

        edtx_Codigo.requestFocus();
        super.onPostCreate(savedInstanceState);
    }
    private void declararVariables()
    {
        try
            {
                    activityHelpers = new ActivityHelpers();
                    activityHelpers.AgregarMenus(this,R.id.Pantalla_principal,false);
                    toolbar = findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);

                    getSupportActionBar().setTitle("Reubicación");
                    getSupportActionBar().setSubtitle("Piezas a Cont.");

                    p = new ProgressBarHelper(this);

                    table = (SortableTableView) findViewById(R.id.tableView_OC);

                    edtx_Codigo = (EditText) findViewById(R.id.edtx_Codigo);
                    edtx_Posicion = (EditText) findViewById(R.id.edtx_Posicion);
                    edtx_SKU = (EditText) findViewById(R.id.edtx_SKU);
                    edtx_Piezas = (EditText) findViewById(R.id.edtx_Piezas);
                    edtx_NuevoContenedor = (EditText) findViewById(R.id.edtx_NuevoContenedor);

                    sw_SKU = (Switch) findViewById(R.id.sw_SKU);

                    txtv_ProdCont = findViewById(R.id. txtv_ProdCont);
                    txtv_ContSel = findViewById(R.id. txtv_ContSel);
                    txtv_CantPaq = findViewById(R.id. txtv_CantPaq);


                    txtv_SugPosReub = findViewById(R.id. txtv_SugPosReub);
                    txtv_SugCont = findViewById(R.id. txtv_SugCont);

                    edtx_Posicion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    edtx_Codigo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    edtx_SKU.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    edtx_NuevoContenedor.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(Almacen_Reubicacion_Por_Cantidad.this, getCurrentFocus(), e.getMessage(), false, true, true);

            }
    }
    private void agregaListeners()
    {

        try
            {

                sw_SKU.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {
                        edtx_SKU.setText("");
                        edtx_Piezas.setText("");
                        edtx_SKU.requestFocus();

                        edtx_Piezas.setEnabled(!b);

                        if(!b)
                        {
                            edtx_Piezas.setHint("Capturar piezas");
                        }else
                        {
                            edtx_Piezas.setHint("");
                        }
                    }
                });

                edtx_Codigo.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                    @Override
                    public void onFocusChange(View view, boolean b)
                    {
                        if(b)
                        {
                            edtx_Codigo.setText("");

                            if (ConfigTabla_Totales != null)
                            {
                                ConfigTabla_Totales.CargarDatosTabla(null);
                            }
                        }
                    }
                });

                edtx_Posicion.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                    @Override
                    public void onFocusChange(View view, boolean b)
                    {
                        if(b)
                        {
                            edtx_Posicion.setText("");

                        }
                    }
                });


                edtx_NuevoContenedor.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                    @Override
                    public void onFocusChange(View view, boolean b)
                    {
                        if(b)
                        {
                            edtx_NuevoContenedor.setText("");
                            txtv_CantPaq.setText("");
                            txtv_ContSel.setText("");
                            txtv_ProdCont.setText("");

                        }
                    }
                });

                edtx_Codigo.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                        {

                            if (edtx_Codigo.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto,edtx_Codigo,"ingrese un código con material.", false, true, true);

                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Codigo.requestFocus();
                                    }
                                });
                                return false;
                            }
                           new SegundoPlano(ConsultaCodigo).execute();

                            new esconderTeclado(Almacen_Reubicacion_Por_Cantidad.this);
                            return false;

                        }

                        return false;
                    }
                });

                edtx_NuevoContenedor.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                        {

                            if (edtx_NuevoContenedor.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto,edtx_NuevoContenedor, getString(R.string.error_ingrese_contenedor), false, true, true);

                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_NuevoContenedor.requestFocus();
                                    }
                                });
                                return false;
                            }
                            new SegundoPlano(ConsultaContenedor).execute();

                            new esconderTeclado(Almacen_Reubicacion_Por_Cantidad.this);
                            return false;

                        }

                        return false;
                    }
                });


                edtx_Posicion.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                            {

                                if (edtx_Posicion.getText().toString().equals(""))
                                    {
                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_ubicacion), false, true, true);

                                        handler.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                edtx_Posicion.requestFocus();
                                            }
                                        });
                                        return false;
                                    }

                                handler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        edtx_NuevoContenedor.requestFocus();
                                    }
                                },100);
                                new esconderTeclado(Almacen_Reubicacion_Por_Cantidad.this);
                                return false;

                            }

                        return false;
                    }
                });



                edtx_SKU.setOnFocusChangeListener(new View.OnFocusChangeListener()
                {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus)
                    {
                        if(hasFocus&&!edtx_SKU.getText().toString().equals(""))
                        {
                            try
                            {
                                edtx_SKU.setText("");
                                edtx_Piezas.setText("");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, edtx_SKU, e.getMessage(), "false", true, true);
                            }
                        }

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
                                if(edtx_Codigo.getText().toString().equals(""))
                                {

                                    edtx_Codigo.setText("");
                                    edtx_Codigo.requestFocus();

                                    new popUpGenerico(contexto,edtx_Codigo,"Ingrese un código con articulos." , false, true, true);
                                    return false;
                                }
                                if(edtx_Posicion.getText().toString().equals(""))
                                {

                                    edtx_Posicion.setText("");
                                    edtx_Posicion.requestFocus();

                                    new popUpGenerico(contexto,edtx_Posicion,getString(R.string.error_ingrese_nueva_posicion) , false, true, true);
                                    return false;
                                }

                                if(edtx_SKU.getText().toString().equals(""))
                                {

                                    edtx_SKU.setText("");
                                    edtx_SKU.requestFocus();

                                    new popUpGenerico(contexto,edtx_SKU,"Ingrese un SKU." , false, true, true);
                                    return false;
                                }

                                if(edtx_NuevoContenedor.getText().toString().equals(""))
                                {

                                    edtx_NuevoContenedor.setText("");
                                    edtx_NuevoContenedor.requestFocus();

                                    new popUpGenerico(contexto,edtx_NuevoContenedor,getString(R.string.error_ingrese_contenedor) , false, true, true);
                                    return false;
                                }


                                if(sw_SKU.isChecked())
                                {
                                    new SegundoPlano(ReubicarProductoSKU).execute();
                                }else
                                {
                                    handler.postDelayed(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            handler.post(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    edtx_Piezas.requestFocus();
                                                    edtx_Piezas.setText("");
                                                }
                                            });
                                        }
                                    },100);

                                }
                                new esconderTeclado(Almacen_Reubicacion_Por_Cantidad.this);

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                            }
                        }
                        return false;
                    }
                });





                edtx_Piezas.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            try
                            {
                                if(edtx_Codigo.getText().toString().equals(""))
                                {

                                    edtx_Codigo.setText("");
                                    edtx_Codigo.requestFocus();

                                    new popUpGenerico(contexto,edtx_Codigo,"Ingrese un código con articulos." , false, true, true);
                                    return false;
                                }
                                if(edtx_Posicion.getText().toString().equals(""))
                                {

                                    edtx_Posicion.setText("");
                                    edtx_Posicion.requestFocus();

                                    new popUpGenerico(contexto,edtx_Posicion,getString(R.string.error_ingrese_nueva_posicion) , false, true, true);
                                    return false;
                                }

                                if(edtx_SKU.getText().toString().equals(""))
                                {

                                    edtx_SKU.setText("");
                                    edtx_SKU.requestFocus();

                                    new popUpGenerico(contexto,edtx_SKU,"Ingrese un SKU." , false, true, true);
                                    return false;
                                }

                                if(edtx_NuevoContenedor.getText().toString().equals(""))
                                {

                                    edtx_NuevoContenedor.setText("");
                                    edtx_NuevoContenedor.requestFocus();

                                    new popUpGenerico(contexto,edtx_NuevoContenedor,getString(R.string.error_ingrese_contenedor) , false, true, true);
                                    return false;
                                }

                                if(sw_SKU.isChecked())
                                {
                                    new SegundoPlano(ReubicarProductoSKU).execute();


                                }else
                                {
                                    try
                                    {
                                        if (Float.parseFloat(edtx_Piezas.getText().toString()) > 999999)
                                        {
                                            handler.post(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    handler.post(new Runnable()
                                                    {
                                                        @Override
                                                        public void run()
                                                        {
                                                            edtx_Piezas.requestFocus();
                                                            edtx_Piezas.setText("");
                                                        }
                                                    });
                                                }
                                            });
                                            new popUpGenerico(contexto, edtx_Piezas, getString(R.string.error_cantidad_mayor_999999), "false", true, true);
                                            return false;
                                        }
                                    }catch (NumberFormatException ex)
                                    {
                                        handler.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {  edtx_Piezas.setText("");
                                                edtx_Piezas.requestFocus();

                                            }
                                        });
                                        new popUpGenerico(contexto,edtx_Piezas,getString(R.string.error_cantidad_valida),"false",true,true);
                                    }

                                    new SegundoPlano(ReubicarProducto).execute();


                                }

                                new com.automatica.AXCPT.Servicios.esconderTeclado(Almacen_Reubicacion_Por_Cantidad.this);

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                            }
                        }
                        return false;
                    }
                });




            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(Almacen_Reubicacion_Por_Cantidad.this, getCurrentFocus(), e.getMessage(), false, true, true);

            }
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
            if(Seleccionado)
            {
                new SegundoPlano(SugReubi).execute(clickedData[1]);
            }else
            {
                txtv_SugCont.setText("");
                txtv_SugPosReub.setText("");
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(p.ispBarActiva())
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, getCurrentFocus());
            }
            if ((id == R.id.borrar_datos))
            {
//
//            edtx_Cantidad.setText("");
//                edtx_Lote.setText("");
//            edtx_Posicion.setText("");
//            edtx_NuevaPosicion.setText("");
//            edtx_Producto.setText("");
//            edtx_CantxEmp.setText("");
//            edtx_Posicion.requestFocus();
                //ReiniciarVariables();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BotonDerecha()
    {

    }

    @Override
    public void BotonIzquierda()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();

    }

    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {

        String tarea;
        cAccesoADatos_Almacen cad = new cAccesoADatos_Almacen(Almacen_Reubicacion_Por_Cantidad.this);
        DataAccessObject dao = null;


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
        protected Void doInBackground(String... params)
        {
            try
                {
                    switch (tarea)
                        {



                            case SugReubi://spWSQryConsultaEmpaquesPallet
                                dao = cad.cad_ContenedorSugeridoReubicacion(params[0]);
                                break;

                            case ConsultaCodigo://spWSQryConsultaEmpaquesPallet
                                dao = cad.c_ConsultaEmpaqueMultipleSKU(edtx_Codigo.getText().toString());
                                break;

                            case ConsultaContenedor:
                                dao = cad.c_ConsultaContenedorAjustes(edtx_NuevoContenedor.getText().toString());
                                break;

                            case ReubicarProducto:
                                dao = cad.c_ReubicarProductoPorSKUPiezas(edtx_Codigo.getText().toString(),
                                        edtx_Posicion.getText().toString(),
                                        edtx_NuevoContenedor.getText().toString(),
                                        edtx_SKU.getText().toString(),
                                        edtx_Piezas.getText().toString());
                                break;

                            case ReubicarProductoSKU:
                                dao = cad.c_ReubicarProductoPorSKU(edtx_Codigo.getText().toString(),
                                        edtx_Posicion.getText().toString(),
                                        edtx_NuevoContenedor.getText().toString(),
                                        edtx_SKU.getText().toString(),
                                        "1");

                                break;
                            default:
                                dao = new DataAccessObject(false, "Operación no soportada.", null);
                                break;
                        }
                } catch (Exception e)
                {
                    dao = new DataAccessObject(false, e.getMessage(), null);
                    e.printStackTrace();
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

                                    case SugReubi://spWSQryConsultaEmpaquesPallet
                                        txtv_SugCont.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Contenedor"));
                                        txtv_SugPosReub.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Posicion"));

                                        break;
                                    case ConsultaCodigo:

                                        if (dao.getcTablaUnica()!=null)
                                        {
                                            if(ConfigTabla_Totales == null)
                                            {
                                                ConfigTabla_Totales = new TableViewDataConfigurator(table, dao, Almacen_Reubicacion_Por_Cantidad.this);
                                            }else
                                            {
                                                ConfigTabla_Totales.CargarDatosTabla(dao);
                                            }
                                        }


                                        if(!edtx_NuevoContenedor.getText().toString().equals(""))
                                        {
                                            new SegundoPlano(ConsultaContenedor).execute();
                                        }

                                        if(edtx_Posicion.getText().toString().equals(""))
                                        {
                                            handler.postDelayed(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    edtx_Posicion.requestFocus();

                                                }
                                            },100);
                                            break;
                                        }
                                        if(edtx_NuevoContenedor.getText().toString().equals(""))
                                        {
                                            handler.postDelayed(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    edtx_NuevoContenedor.requestFocus();
                                                }
                                            },100);
                                            break;

                                        }
                                        if(edtx_SKU.getText().toString().equals(""))
                                        {
                                            handler.postDelayed(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    edtx_SKU.requestFocus();
                                                }
                                            },100);
                                            break;
                                        }



                                        break;

                                    case ConsultaContenedor:

                                        txtv_ProdCont.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        txtv_ContSel.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_CantPaq.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));



                                            handler.postDelayed(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    edtx_SKU.requestFocus();
                                                }
                                            },100);
                                        break;


                                    case ReubicarProductoSKU:
                                    case ReubicarProducto:
                                        new SegundoPlano(ConsultaCodigo).execute();

                                        edtx_SKU.setText("");
                                        edtx_Piezas.setText("");
                                        edtx_SKU.requestFocus();
                                        break;

                                    default:
                                        new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                                        break;
                                }
                        } else
                        {


                            switch (tarea)
                            {


                                case ConsultaCodigo:
                                    new popUpGenerico(contexto,edtx_Codigo, dao.getcMensaje(), dao.iscEstado(), true, true);
                                    break;

                                case ConsultaContenedor:

                                    new popUpGenerico(contexto,edtx_NuevoContenedor, dao.getcMensaje(), dao.iscEstado(), true, true);

                                    break;


                                case ReubicarProductoSKU:
                                case ReubicarProducto:
                                    new popUpGenerico(contexto, edtx_SKU, dao.getcMensaje(), dao.iscEstado(), true, true);

                                    edtx_SKU.setText("");
                                    edtx_Piezas.setText("");
                                    edtx_SKU.requestFocus();
                                    break;

                                default:
                                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                                    break;
                            }
                        }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);
                }
            p.DesactivarProgressBar(tarea);
        }
    }


    @Override
    public void onBackPressed()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }
}
