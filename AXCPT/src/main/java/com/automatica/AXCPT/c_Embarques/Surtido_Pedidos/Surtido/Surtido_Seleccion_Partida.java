package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen.Recepcion_Registro_Transferencia_NE_SelAlm;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Surtido_Empaque_Armado;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Surtido_Empaque_NE_Armado;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Surtido_Pallet_MultiplesProd;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Surtido_Picking_NE_Armado;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Surtido_Piezas_Armado;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_PantallaPrincipal;
import com.automatica.AXCPT.databinding.SurtidoSeleccionPartidaBinding;
import com.automatica.AXCPT.objetos.ObjetoEtiquetaSKU;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import de.codecrafters.tableview.SortableTableView;

public class Surtido_Seleccion_Partida extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar
{
    //region variables

    private EditText edtx_Documento,edtx_Carrito;
    private TextView txtv_EstatusCarr,txtv_DocAct,txtv_CantPaq;
    private SortableTableView tabla;
    private SortableTableView tabla2;
    private ProgressBarHelper p;
    private Context contexto = this;
    private Bundle b = new Bundle();
    private Handler h = new Handler();
    private TableViewDataConfigurator ConfigTabla = null;
    private TableViewDataConfigurator ConfigTabla2 = null;
    private ActivityHelpers activityHelpers;
    private SurtidoSeleccionPartidaBinding binding;
    private String strCarritoActual = "";
    private String tipoSurtido;
    private int vista = 1;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       try
           {
               binding = SurtidoSeleccionPartidaBinding.inflate(getLayoutInflater());
                setContentView(R.layout.surtido_seleccion_partida);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                declaraVariables();
                AgregaListeners();
                edtx_Documento.requestFocus();
               Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,null ,e.getMessage() ,false , true, true);

            }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.SIGUIENTE);
        super.onPostCreate(savedInstanceState);
    }
    @Override
    protected void onResume()
    {
        if(!edtx_Documento.getText().toString().equals(""))
        {
          new SegundoPlano("Tabla").execute();
        }
        if(!edtx_Carrito.getText().toString().equals("")){
            new SegundoPlano("ConsultaCarrito").execute();
        }
        super.onResume();


    }

    private void declaraVariables()
    {
        try
            {
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("Orden de Surtido");

                edtx_Documento = (EditText) findViewById(R.id.edtx_Pedido);
                edtx_Carrito = (EditText) findViewById(R.id.edtx_Carrito);

                txtv_EstatusCarr = (TextView) findViewById(R.id.txtv_EstatusCarr);
                txtv_DocAct = (TextView) findViewById(R.id.txtv_DocAct);
                txtv_CantPaq = (TextView) findViewById(R.id.txtv_CantPaq);

                tabla = (SortableTableView) findViewById(R.id.tableView_OC);
                tabla2 = (SortableTableView) findViewById(R.id.tableView_OC);
                p = new ProgressBarHelper(this);
                edtx_Documento.setText("");

                activityHelpers = new ActivityHelpers();
                activityHelpers.AgregarMenus(this,R.id.cl,false);

                String Documento = "";


                Documento = getIntent().getExtras().getString("Documento");

                if(!Documento.equals(""))
                {
                    edtx_Documento.setText(Documento);
                    new SegundoPlano("Tabla").execute();
                }else
                {
                    edtx_Documento.requestFocus();
                }



            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,null ,e.getMessage() ,false , true, true);
            }
    }
    private void AgregaListeners()
    {
        try
            {
                edtx_Documento.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            if(edtx_Documento.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto,edtx_Documento ,getString(R.string.error_ingrese_pedido) ,"false" , true, true);
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Documento.requestFocus();
                                        edtx_Documento.setText("");
                                    }
                                });

                                return false;
                            }


                            new SegundoPlano("Tabla").execute();

                            new esconderTeclado(Surtido_Seleccion_Partida.this);

                        }
                            return false;
                        }
                    });



                edtx_Carrito.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View view)
                    {
                        edtx_Carrito.setText("");
                        txtv_CantPaq.setText("");
                        txtv_DocAct.setText("");
                        txtv_EstatusCarr.setText("");
                        return false;
                    }
                });

                edtx_Carrito.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            if(edtx_Carrito.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto,edtx_Carrito ,getString(R.string.error_ingrese_ubicacion) ,"false" , true, true);
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Carrito.requestFocus();
                                        edtx_Carrito.setText("");
                                    }
                                });

                                return false;
                            }


                            new SegundoPlano("ConsultaCarrito").execute();

                            new esconderTeclado(Surtido_Seleccion_Partida.this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.change_view_toolbar, menu);
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
        if (p.ispBarActiva())
        {
            switch (item.getItemId())
            {
                case  R.id.InformacionDispositivo:
                    new sobreDispositivo(contexto, null);
                    break;

                case R.id.CambiarVista:
                    if (vista==1){
                        vista=2;
                        new SegundoPlano("Tabla2").execute();
                    }else{
                        vista =1;
                        new SegundoPlano("Tabla").execute();
                    }
                    break;

                case  R.id.CerrarOC:
                    if (edtx_Documento.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto, null, "No hay una orden de compra seleccionada", false, true, true);
                        break;
                    }
                        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("¿Cerrar Orden de Compra?").setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                     new SegundoPlano("CierreOC").execute();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    break;
                case  R.id.borrar_datos:
                    reiniciarDatos();
                    break;
                case  R.id.recargar:
                    new SegundoPlano("Tabla").execute();
                    break;
            }
    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header,String IdConfigurador)
    {
        Toast.makeText(contexto, Header, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData,String MensajeCompleto,String IdConfigurador)
    {
        new popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);
    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData,boolean Seleccionado,String IdConfigurador)
    {
//        if(Seleccionado)
//            {
                b.putString("Pedido", edtx_Documento.getText().toString());
                b.putString("Partida", clickedData[0]);
                b.putString("NumParte", clickedData[1]);
                b.putString("UM", clickedData[5]);
                b.putString("CantidadTotal", clickedData[2]);
                b.putString("CantidadPendiente", clickedData[3]);
                b.putString("CantidadSurtida", clickedData[4]);
                b.putString("Linea", clickedData[6]);
//            }
    }

    private void reiniciarDatos()
    {
        edtx_Documento.setText("");
        edtx_Carrito.setText("");
        edtx_Documento.requestFocus();
        tabla.getDataAdapter().clear();

    }

    @Override
    public void BotonDerecha()
    {

        if(edtx_Documento.getText().toString().equals(""))
        {
            new popUpGenerico(contexto,edtx_Documento,getString(R.string.error_seleccionar_registro),false,true,false);
            reiniciarDatos();
            return;
        }
        if(ConfigTabla==null)
        {
            new popUpGenerico(contexto,null ,getString(R.string.error_seleccionar_registro) , false, true,true );
            return;
        }

        if(!ConfigTabla.renglonEstaSelec())
        {
            new popUpGenerico(contexto,null ,getString(R.string.error_seleccionar_registro) , false, true,true );
            return;
        }



        new PopUpMenuAXC().newInstance(activityHelpers.getTaskbar_axc().getView().findViewById(R.id.BotonDer),
                                        contexto, R.menu.popup_surtido,
                                        new PopUpMenuAXC.ContextMenuListener()
            {
            @Override
            public void listenerItem(MenuItem item)
            {

                String strCarritoSel = edtx_Carrito.getText().toString();



                if (edtx_Carrito.getText().toString().equals("")){
                    new popUpGenerico(contexto,edtx_Carrito ,"Para surtir debe de ingresar un código de carrito." , false, true,true );
                    return;
                }


                Intent intent;

                switch (item.getItemId())
                {
                    case R.id.Empaque:
                       if (tipoSurtido.equals("PICKING") || tipoSurtido.equals("TODO"))
                            intent = new Intent(contexto, Surtido_Surtido_Empaque_Armado.class);
                       else{
                           new popUpGenerico(contexto,edtx_Carrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                           return;
                       }

                        break;
                    case R.id.EmpaqueNE:
                        if (tipoSurtido.equals("PICKING") || tipoSurtido.equals("TODO"))
                            intent = new Intent(contexto, Surtido_Surtido_Empaque_NE_Armado.class);
                        else{
                            new popUpGenerico(contexto,edtx_Carrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                            return;
                        }
                        break;
                    case R.id.Piezas:
                        if (tipoSurtido.equals("PICKING") || tipoSurtido.equals("TODO"))
                            intent = new Intent(contexto, Surtido_Surtido_Piezas_Armado.class);
                        else{
                            new popUpGenerico(contexto,edtx_Carrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                            return;
                        }
                        break;
                    case R.id.PalletCompleto:
                        if (tipoSurtido.equals("PALLET") || tipoSurtido.equals("TODO"))
                            intent = new Intent(contexto, Surtido_Surtido_Pallet_MultiplesProd.class);
                        else{
                            new popUpGenerico(contexto,edtx_Carrito ,"Opción de surtido no válida, intente surtir por ["+tipoSurtido+"]" , false, true,true );
                            return;
                        }
                        break;
                    default:
                        intent = new Intent();
                }
                b.putString("Carrito", strCarritoSel);
                intent.putExtras(b);
                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                startActivity(intent);
            }
        });
    }

    @Override
    public void BotonIzquierda()
    {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }

    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Embarques adEmb = new cAccesoADatos_Embarques(Surtido_Seleccion_Partida.this);

        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {
            p.ActivarProgressBar(Tarea);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (Tarea)
                {
                    case"Tabla":
                        dao = adEmb.cad_ConsultaPedidoSurtido(edtx_Documento.getText().toString());
                        break;

                    case"Tabla2":
                        dao = adEmb.cad_ConsultaPedidoSurtidoAgrupado(edtx_Documento.getText().toString());
                        break;

                    case"ConsultaCarrito":
                        dao = adEmb.cad_ConsultaCarritoSurtido(edtx_Documento.getText().toString(),edtx_Carrito.getText().toString());
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
            try
            {
                if (dao.iscEstado())
                {
                    switch (Tarea)
                    {
                        case "Tabla":
                            tabla.getDataAdapter().clear();
                            if(ConfigTabla == null)
                            {
                                ConfigTabla =  new TableViewDataConfigurator( 7, "SURTIDA","LIBERADA TOTAL","10",tabla, dao,Surtido_Seleccion_Partida.this);

                            }else
                            {
                                ConfigTabla.CargarDatosTabla(dao);
                            }


                            edtx_Carrito.requestFocus();
                            tipoSurtido = dao.getcMensaje();
                            Log.e("tipo", dao.getcMensaje());
                        break;

                        case "Tabla2":
                            tabla.getDataAdapter().clear();
                            if(ConfigTabla == null)
                            {
                                ConfigTabla =  new TableViewDataConfigurator( 5, "SURTIDA","LIBERADA TOTAL","10",tabla, dao,Surtido_Seleccion_Partida.this);

                            }else
                            {
                                ConfigTabla.CargarDatosTabla(dao);
                            }


                            edtx_Carrito.requestFocus();
                            tipoSurtido = dao.getcMensaje();
                            Log.e("tipo", dao.getcMensaje());
                            break;

                        case"ConsultaCarrito":
                            strCarritoActual = dao.getcMensaje();
                            txtv_CantPaq.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Paquetes"));
                            txtv_EstatusCarr.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Estatus"));
                            txtv_DocAct.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Documento"));
                            break;
                    }
                }
                else
                {

                    switch (Tarea)
                    {
                        case "Tabla":
                            new popUpGenerico(contexto,edtx_Documento,dao.getcMensaje(),false,true,true);
                            if(ConfigTabla != null)
                            {
                                ConfigTabla.CargarDatosTabla(dao);
                            }

                            edtx_Documento.setText("");
                            edtx_Documento.requestFocus();

                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);

                            break;

                        case"ConsultaCarrito":
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            strCarritoActual = "";
                            txtv_CantPaq.setText("");
                            txtv_EstatusCarr.setText("");
                            txtv_DocAct.setText("");
                            break;
                    }
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
            p.DesactivarProgressBar(Tarea);
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
