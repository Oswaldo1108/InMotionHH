package com.automatica.AXCMP.c_Consultas;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.constructorTablaEntradaAlmacen;


import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class consultas_ConsultaPosicion extends AppCompatActivity
{
//region variables
SoapAction sa = new SoapAction();

    Context contexto = this;
    View vista;
    ArrayList<constructorTablaEntradaAlmacen> lista = null;

    TextView empaque;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    constructorConsultasPosicion constrPosicion;
   // SoapAction sa = new SoapAction();
    HorizontalScrollView horizontalScrollView;
    final Handler handlerRequestFocus = new Handler();
    HiloRecibeDatos hrd = new HiloRecibeDatos();
    SortableTableView<String[]> tabla;

    int renglonSeleccionado;
    int renglonAnterior=-1;

    public String[] HEADERS = { "Código miaxc_consulta_pallet", "Producto","Lote","UM","Empaques", "Cantidad Actual", "Cantidad Pallets" };
    public String[][] DATA_TO_SHOW;
    public ArrayList<constructorConsultasPosicion> arrayAdaptador;
    SimpleTableDataAdapter st;

    constructorTablaEntradaAlmacen cd;

    boolean recarga;


    //endregion
//region generado
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_consulta_posicion);

        empaque = (EditText) findViewById(R.id.edtx_posicion);
        empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_tabla_embarques);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Consultas_Posicion));


        tabla = (SortableTableView<String[]>) findViewById(R.id.tableView_posicion);
        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(consultas_ConsultaPosicion.this, HEADERS));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new cambiaColorStatusBar(contexto, R.color.AzulStd, consultas_ConsultaPosicion.this);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);



        empaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    //sa= new SoapAction();
                    hrd.cancel(true);
                    hrd = new HiloRecibeDatos();
                    hrd.execute();
                    requestFocusTextFields();

                    new esconderTeclado(consultas_ConsultaPosicion.this);
                }
                return false;
            }
        });


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new cambiaColorStatusBar(contexto, R.color.AzulStd, consultas_ConsultaPosicion.this);


        tabla.addDataLongClickListener(new ListenerLongClickTabla());
        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());


    }

    public void requestFocusTextFields()
    {

        Log.i("SoapResponse", "  texto " +empaque.getText().toString());
        handlerRequestFocus.post(new Runnable()
        {
            @Override
            public void run()
            {
                empaque.clearFocus();
                empaque.requestFocus();

            }
        });

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
        int id = item.getItemId();

        if(!recarga) {


            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }

            if ((id == R.id.borrar_datos)) {

                empaque.setText("");
                tabla.getDataAdapter().clear();
                tabla.getDataAdapter().notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion


    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tabla.getDataAdapter().notifyDataSetChanged();

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();

                renglonAnterior=-1;
            }

        }

    }
    private class cambiaColorTablaSeleccionada implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {



            if(rowIndex == renglonSeleccionado)
            {
                Color = R.color.RengSelStd;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            }
            else
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

          SegundoPlanoPallet sp = new SegundoPlanoPallet(clickedData[0]);
          sp.execute();

            return false;
        }
    }
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {

            Toast.makeText(contexto, HEADERS[columnIndex], Toast.LENGTH_SHORT).show();
            Log.d("SoapResponse", HEADERS[columnIndex]);
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

//region AsyncTask
    class HiloRecibeDatos extends AsyncTask<Void,Void,Void>
    {

        constructorConsultasPosicion array;
        String datoConsulta, decision,mensaje,tabla;
        View LastView;
        @Override
        protected void onPreExecute()
        {
            recarga = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            consultas_ConsultaPosicion.this.tabla.getDataAdapter().clear();
            consultas_ConsultaPosicion.this.tabla.getDataAdapter().notifyDataSetChanged();
          handlerRequestFocus.postDelayed(new Runnable()
          {
              @Override
              public void run() {

                  LastView=getCurrentFocus();
                  progressBarHolder.requestFocus();

              }
          },10);
        }
        @Override
        protected Void doInBackground(Void... voids)
        {


           try
           {

               datoConsulta = empaque.getText().toString();
               if(datoConsulta.equals("")||(datoConsulta.length()<1))
               {
                   decision = "false";
                   mensaje = getString(R.string.error_ingrese_ubicacion);

               }else
                   {
                       sa.SOAPconsultaUbicacion(datoConsulta,contexto);

                       decision =  sa.getDecision();
                       mensaje = sa.getMensaje();
                       if(decision.equals("true"))
                       {
                          arrayAdaptador= sacaDatos(sa);

                           DATA_TO_SHOW = new String[arrayAdaptador.size()][7];
                           int i = 0;
                           for(constructorConsultasPosicion a:arrayAdaptador)
                           {

                               DATA_TO_SHOW[i][0] = a.getCodigoPallet();
                               DATA_TO_SHOW[i][1] = a.getProducto();
                               DATA_TO_SHOW[i][2] = a.getDesc();
                               DATA_TO_SHOW[i][3] = a.getLote();
                               DATA_TO_SHOW[i][4] = a.getEmpaques();
                               DATA_TO_SHOW[i][5] = a.getCantidadActual();
                               DATA_TO_SHOW[i][6] = a.getCantidadPallets();

                               i++;

                           }
                       }
                   }

           }catch (Exception e)
           {
               e.printStackTrace();
           }




            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {

            try {
                //  Log.i("SoapResponse", " On postexecute");
                handlerRequestFocus.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {

                        recarga=false;
                        LastView.requestFocus();

                    }
                },10);

                if (decision.equals("true"))
                {
                    TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(contexto, 16, 200);
                    columnModel.setColumnWidth(0,250);


                    consultas_ConsultaPosicion.this.tabla.setDataAdapter(st = new SimpleTableDataAdapter(consultas_ConsultaPosicion.this, DATA_TO_SHOW));
                    consultas_ConsultaPosicion.this.tabla.getDataAdapter().notifyDataSetChanged();
                    consultas_ConsultaPosicion.this.tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                    consultas_ConsultaPosicion.this.tabla.setColumnModel(columnModel);
                    arrayAdaptador.clear();
                }
                if (decision.equals("false"))
                {

                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);

                }

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), decision, true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }


    private class SegundoPlanoPallet extends AsyncTask<Void,Void,Void>
    {
       String Pallet;

        String mensaje, estado;

        View LastView;
        public SegundoPlanoPallet(String Pallet)
        {
            this.Pallet = Pallet;
        }

        @Override
        protected void onPreExecute()
        {
            recarga=true;
            super.onPreExecute();
            LastView = getCurrentFocus();
            progressBarHolder.requestFocus();

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {

                sa.SOAPConsultaPalletReg(Pallet, contexto);

                mensaje = sa.getMensaje();
                estado = sa.getDecision();

                if (estado.equals("true"))
                {
                    sacaDatos();
                }

            } catch (Exception e) {
                mensaje = e.getMessage();
                estado = "false";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                recarga=false;
                LastView.requestFocus();
                if (estado.equals("true"))
                {

                    String DataToShow="CONSULTA PALLET\n["+Pallet+"]\n\n";
                    int i =0;
                    for(constructorTablaEntradaAlmacen data:lista)
                    {
                        DataToShow +=data.getTitulo()+" - "+ data.getDato() + "\n";
                        i++;
                    }
                    new popUpGenerico(contexto, getCurrentFocus(), DataToShow, "Información", true, false);
                } else if (estado.equals("false"))
                {
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, "Advertencia", true, true);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "Advertencia", true, true);
            }

        }
    }


    //endregion
//region Soap
    public ArrayList<constructorConsultasPosicion> sacaDatos(SoapAction sa)
    {

        SoapObject tabla = sa.parser();

        arrayAdaptador = new ArrayList<>();
        if(tabla!=null) {
            for (int i = 0; i < tabla.getPropertyCount(); i++) {

                try {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);

                    constrPosicion = new constructorConsultasPosicion(tabla1.getPrimitivePropertyAsString("CodigoPallet"),
                                                                      tabla1.getPrimitivePropertyAsString("NumParte"),
                                                                      tabla1.getPrimitivePropertyAsString("Lote"),
                                                                      tabla1.getPrimitivePropertyAsString("UM"),
                                                                      tabla1.getPrimitivePropertyAsString("CantidadActual"),
                                                                      tabla1.getPrimitivePropertyAsString("Empaques"),
                                                                      tabla1.getPrimitivePropertyAsString("CantidadPallets"));
                    arrayAdaptador.add(constrPosicion);
                    // Log.i("SoapResponse", "sacaDatos: " + constrPosicion.getCodigoPallet());


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        return arrayAdaptador;
    }


    private void sacaDatos()
    {
        try
        {

            lista = new ArrayList<>();
            PropertyInfo pi;

            SoapObject tabla = sa.parser();
            if(tabla!=null)
            {
                for (int i = 0; i < tabla.getPropertyCount(); i++)
                {
                    SoapObject tabla11 = (SoapObject) tabla.getProperty(i);

                    for(int t=0;t<tabla11.getPropertyCount();t++)
                    {
                        pi = new PropertyInfo();
                        tabla11.getPropertyInfo(t,pi);
                        String name = pi.name;
                        if(name.equals("FechaCrea"))
                        {
                            name = "Fecha Creación";
                        }
                        if(name.equals("FechaCad"))
                        {
                            name = "Fecha Caducidad";
                        }
                        if(name.equals("LoteProveedor"))
                        {
                            name = "Lote Proveedor";
                        }
                        if(name.equals("CantidadOriginal"))
                        {
                            name = "Cantidad Original";
                        }
                        if(name.equals("CantidadActual"))
                        {
                            name = "Cantidad Actual";
                        }
                        if(name.equals("CantidadOriginal"))
                        {
                            name = "CantidadOriginal";
                        }
                        if(name.equals("Estacion"))
                        {
                            name = "Estación";
                        }
                        if(name.equals("Revision"))
                        {
                            name = "Revisión";
                        }
                        cd = new constructorTablaEntradaAlmacen(name,tabla11.getPropertyAsString(t));
                        lista.add(cd);
                    }
                    // UbicacionSugerida = tabla11.getPrimitivePropertyAsString("UbicacionSugerida");
                    Log.d("SoapResponse", tabla11.toString());
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



//endregion

}
