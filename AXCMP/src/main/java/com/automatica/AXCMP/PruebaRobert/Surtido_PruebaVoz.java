package com.automatica.AXCMP.PruebaRobert;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.SoapAction;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Surtido_PruebaVoz extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_Empaque, edtx_OrdenPron,edtx_Ubicacion;
    SortableTableView tabla_OrdenProd,tabla_Empaques;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    View vista;
    Context contexto = this;
    Handler handler = new Handler();

    int renglonSeleccionado;
    int renglonAnterior = -1;
    SimpleTableDataAdapter st;
    Bundle b;
    Boolean seleccionado, ReiniciarTabla = false;

    boolean recargar;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_surtido_empaque);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_PruebaVoz.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //  SacaExtrasIntent();
        declararVariables();
        agregaListeners();
    }

    @Override
    protected void onResume()
    {
        Recarga();

        super.onResume();
    }

    private void Recarga()
    {
//        if (!txtv_Pedido.getText().toString().equals("-")) //Si hay un pedido en esta variable, esta verfica que parte de la pantalla esta visible, y actualiza el modulo presente.
//            {
//                if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)//
//                    {
//                        SegundoPlano sp = new SegundoPlano("ConsultaPedidoDet");
//                        sp.execute();
//                    } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
//                    {
//                        SegundoPlano sp = new SegundoPlano("ConsultaPalletAbierto");
//                        sp.execute();
//                    }
//            }
    }


    private void declararVariables()        //Aqui se asignan las Vistas del xml de pantalla a las variables de Java
    {
        try
            {


                toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido));
                getSupportActionBar().setSubtitle("Empaque");
                progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

                edtx_OrdenPron = (EditText) findViewById(R.id.edtx_OrdenProd);
                edtx_Empaque = (EditText) findViewById(R.id.edtx_Empaque);
                edtx_Ubicacion = (EditText) findViewById(R.id.edtx_Ubicacion);

                tabla_OrdenProd = (SortableTableView) findViewById(R.id.tblv_OrdenProd);
                tabla_Empaques= (SortableTableView) findViewById(R.id.tblv_Empaques);

                edtx_Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});                  //Mayusculas en EditText
                edtx_OrdenPron.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                edtx_Ubicacion.setFilters(new InputFilter[]{new InputFilter.AllCaps()});


            //    tabla_Salidas.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, HEADER));

             //   btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);


//
//            txtv_Pedido.setText(Pedido);
//            txtv_Producto.setText(NumParte);
//            txtv_CantPend.setText(CantidadPendiente);
//            txtv_CantReg.setText(CantidadSurtida);

            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
    }

//    private void SacaExtrasIntent()//Aqui se sacan las variables traidas de la pantalla anterior
//    {
//        try
//            {
//                b = getIntent().getExtras();
//                Pedido = b.getString("Pedido");
//                Partida = b.getString("Partida");
//                NumParte = b.getString("NumParte");
//                UM = b.getString("UM");
//                CantidadTotal = b.getString("CantidadTotal");
//                CantidadPendiente = b.getString("CantidadPendiente");
//                CantidadSurtida = b.getString("CantidadSurtida");
//                Linea = b.getString("Linea");
//
//                Log.e("SoapResponse", "SacaExtrasIntent: ");
//            } catch (Exception e)
//            {
//                Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
//            }
//
//    }

    private void agregaListeners()
    {
        edtx_OrdenPron.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!edtx_OrdenPron.getText().toString().equals(""))
                            {
//                                if (edtx_Empaque.getText().toString().equals(""))         EJEMPLO VALIDACION
//                                    {
//                                        handler.post(new Runnable()
//                                        {
//                                            @Override
//                                            public void run()
//                                            {
//                                                edtx_Empaque.setText("");
//                                                edtx_Empaque.requestFocus();
//                                            }
//                                        });
//                                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
//                                        return false;
//                                    }

                                SegundoPlano sp = new SegundoPlano("RegistrarEmpaque");
                                sp.execute();

                            } else
                            {
                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_OrdenPron.setText("");
                                        edtx_OrdenPron.requestFocus();
                                    }
                                });
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_orden_produccion), "false", true, true);
                            }
                        new esconderTeclado(Surtido_PruebaVoz.this);
                        return true;
                    }
                return false;
            }
        });

        edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (edtx_Empaque.getText().toString().equals(""))
                                {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);

                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();

                                    }
                                });
                                return false;

                            }
                        SegundoPlano sp = new SegundoPlano("ConsultaEmpaque");
                        sp.execute();
                        new esconderTeclado(Surtido_PruebaVoz.this);
                        return true;
                    }
                return false;
            }
        });


        edtx_Ubicacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (edtx_Ubicacion.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_ubicacion), "false", true, true);

                                handler.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        edtx_Ubicacion.setText("");
                                        edtx_Ubicacion.requestFocus();

                                    }
                                });
                                return false;

                            }
                        SegundoPlano sp = new SegundoPlano("ConsultaEmpaque");
                        sp.execute();
                        new esconderTeclado(Surtido_PruebaVoz.this);
                        return true;
                    }
                return false;
            }
        });


//        btn_CerrarTarima.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (arrayDatosTabla != null)
//                    if (!(arrayDatosTabla.size() <= 0))
//                        {
//                            new CreaDialogos(getString(R.string.pregunta_cierre_pallet),
//                                    new DialogInterface.OnClickListener()
//                                    {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which)
//                                        {
//                                            SegundoPlano sp = new SegundoPlano("CerrarTarima");
//                                            sp.execute();
//                                        }
//                                    }, null, contexto);
//
//                        } else
//                        {
//                            //     new popUpGenerico(contexto,vista ,getString(R.string.error_cerrar_pallet_sin_empaques) , "false",true , true);
//                        }
//            }
//        });
        tabla_Empaques.addDataClickListener(new ListenerClickTabla());
        tabla_Empaques.addDataLongClickListener(new ListenerLongClickTabla());
        tabla_Empaques.addHeaderClickListener(new headerClickListener());

        tabla_OrdenProd.addDataClickListener(new ListenerClickTabla());
        tabla_OrdenProd.addDataLongClickListener(new ListenerLongClickTabla());
        tabla_OrdenProd.addHeaderClickListener(new headerClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
            {
                getMenuInflater().inflate(R.menu.change_view_toolbar, menu);
                return true;
            } catch (Exception ex)
            {
                Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
            }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)         //Manejo de los botones del toolbar
    {
        try
            {
                int id = item.getItemId();

                if (!recargar)
                    {
                        if ((id == R.id.InformacionDispositivo))
                            {
                                new sobreDispositivo(contexto, vista);
                            }
                        if ((id == R.id.recargar))
                            {
                                Recarga();
                            }
                        if ((id == R.id.CambiarVista))
                            {
//                                if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
//                                    {
//
//                                        cl_EmpaqueRegistro.setVisibility(View.GONE);
//                                        cl_TablaRegistro.setVisibility(View.VISIBLE);
//                                        item.setIcon(R.drawable.ic_add_box);
//                                        item.setChecked(true);
//                                        SegundoPlano sp = new SegundoPlano("ConsultaPalletAbierto");
//                                        sp.execute();
//
//                                    } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
//                                    {
//                                        cl_TablaRegistro.setVisibility(View.GONE);
//                                        cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
//                                        item.setIcon(R.drawable.ic_change_view);
//
//                                        SegundoPlano sp = new SegundoPlano("SugiereEmpaque");
//                                        sp.execute();
//
//                                    }

                            }
                    }
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }
        return super.onOptionsItemSelected(item);
    }

    private void ReiniciarVariables(String tarea)       //Limpiar pantalla
    {
        switch (tarea)
            {
                case "ConsultaOrdenProduccion":
                    edtx_OrdenPron.setText("");
//                    tabla_Salidas.getDataAdapter().clear();
//                    edtx_ConfirmarEmpaque.requestFocus();

                    break;
                case "ConsultarTarima":
//                    tabla_Salidas.getDataAdapter().clear();
                    edtx_Empaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "ConsultaEmpaque":
                    edtx_Empaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "RegistrarEmpaque":
                    edtx_Empaque.setText("");
//                    edtx_ConfirmarEmpaque.setText("");
                    edtx_Empaque.requestFocus();
                    break;
                case "Reiniciar":
//                    edtx_Empaque.setText("");
//                    edtx_ConfirmarEmpaque.setText("");
//                    tabla_Salidas.getDataAdapter().clear();
                    edtx_Empaque.requestFocus();
                    break;
            }
    }

    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if (renglonAnterior != rowIndex)
                {

                    renglonAnterior = rowIndex;
                    renglonSeleccionado = rowIndex;
//                    tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
//                    tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                } else if (renglonAnterior == rowIndex)
                {
                    renglonSeleccionado = -1;
//                    tabla_Salidas.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
//                    tabla_Salidas.getDataAdapter().notifyDataSetChanged();
                    renglonAnterior = -1;
                }
            ReiniciarTabla = false;
        }

    }

    private class cambiaColorTablaSeleccionada implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;

        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {

            if (rowIndex == renglonSeleccionado)
                {
                    Color = R.color.RengSelStd;
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                    seleccionado = true;
                } else
                {
                    Color = R.color.Transparente;
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                }

            return new ColorDrawable(getResources().getColor(Color));
        }
    }

    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
            String DataToShow = "";
            int i = 0;
            for (String data : clickedData)
                {
                  //  DataToShow += HEADER[i] + " - " + data + "\n";
                    i++;
                }
            new popUpGenerico(contexto, vista, DataToShow, "Información", true, false);

            return false;
        }
    }

    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {

//            Toast.makeText(contexto, HEADER[columnIndex], Toast.LENGTH_SHORT).show();
//            Log.d("SoapResponse", HEADER[columnIndex]);
        }
    }

    private class cambiaColorTablaClear implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;

        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {
            Color = R.color.Transparente;
            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            return new ColorDrawable(getResources().getColor(Color));
        }
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        String tarea;
        View LastView;

        DataAccessObject dao = null;
        cAccesoADatos c = new cAccesoADatos(contexto);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            //palletRegistradosVar=0;

            try
                {
                    LastView = getCurrentFocus();

                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressBarHolder.requestFocus();
                            recargar = true;
                        }
                    }, 1);
                } catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }


        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    switch (tarea)
                        {
                            case "ConsultaPedidoDet":
                              //  dao = c.SOAPConsultaPedidoDetPartida(edtx_OrdenProd.getText().toString(), Partida);
                                break;

                            case "CancelaEmpaque":
                              //  dao = c.SOAPCancelaEmpaque(Pedido, Partida, edtx_Empaque.getText().toString());
                                break;
                            case "RegistrarEmpaque":
                            //    dao = c.SOAPRegistraEmpaqueSurtido(Pedido, Partida, edtx_Empaque.getText().toString());
                                break;

                            case "ConsultaPalletAbierto":
                             //   c.SOAPConsultaTarimaConsolidada(Pedido);
                                break;

                            case "ConsultaPosicion":
                                //dao = c.SOAPConsultaPosicion(, , );           Aqui agrega los parametros que necesites, para ir a defeinicion ctrl + b
                                break;
                        }

                } catch (Exception e)
                {
                    dao = new DataAccessObject(false, e.getMessage(), null);
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
                {
                    SegundoPlano sp;
                    if (LastView != null)
                        {
                            LastView.requestFocus();
                        }

                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {

                                    case "ConsultaPedidoDet":
                                        tabla_OrdenProd.getDataAdapter().notifyDataSetChanged();
                                        tabla_OrdenProd.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_PruebaVoz.this, dao.getcTablaUnica()));
                                        tabla_OrdenProd.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, dao.getcEncabezado()));
                                        ReiniciarTabla = true;
                                        tabla_OrdenProd.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                                        break;


                                    case "CancelaEmpaque":

                                        //Evento de empaque cancelado
                                        break;

                                    case "ConsultaPalletAbierto":
                                        tabla_OrdenProd.getDataAdapter().notifyDataSetChanged();
                                        tabla_OrdenProd.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_PruebaVoz.this, dao.getcTablaUnica()));
                                        tabla_OrdenProd.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, dao.getcEncabezado()));
                                        ReiniciarTabla = true;
                                        tabla_OrdenProd.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                                        break;


                                    case "RegistrarEmpaque":
                                        new popUpGenerico(contexto, getCurrentFocus(), "Empaque Registrado.", dao.iscEstado(), true, true);

                                        sp = new SegundoPlano("ConsultaPalletAbierto");
                                        sp.execute();
                                        break;

                                    case "CerrarTarima":
                                    //    new popUpGenerico(contexto, getCurrentFocus(), "Tarima cerrada [" + RegEmpPalletAbierto + "] con exito.", dao.iscEstado(), true, true);
                                        break;

                                    case "ConsultaPosicion":
                                        //Aqui deberias hacer el evento de sonido
                                        break;

                                }
                        }

                    else
                        {

                            ReiniciarVariables(tarea);
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }

                } catch (Exception e)
                {
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);
                    e.printStackTrace();
                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            recargar = false;
        }
    }
}

