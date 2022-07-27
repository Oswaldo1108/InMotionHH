package com.automatica.AXCPT.c_Embarques;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.AXCPT.c_Consultas.constructorConsultasPosicion;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Embarques_Embarque extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();

    EditText OrdenSalida,Anden,Pallet;
    TableView tabla;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    public ArrayList<constructorConsultasPosicion> arrayAdaptador;
    constructorConsultasPosicion constrPosicion;
    View vista;
    Context contexto = this;
    String[] HEADERS = {"Orden de Surtido","Codigo de Pallet","Producto","Lote","Empaque","Cantidad"};
    String[][] DATA_TO_SHOW;
    SegundoPlanoEmbarques SPE;
    String pallet;
    class Datos
    {
        String OrdenSurtido,CodigoPallet,Producto,Lote,Empaques,CantidadActual;

        public Datos(String ordenSurtido, String codigoPallet, String producto, String lote, String empaques, String cantidadActual) {
            OrdenSurtido = ordenSurtido;
            CodigoPallet = codigoPallet;
            Producto = producto;
            Lote = lote;
            Empaques = empaques;
            CantidadActual = cantidadActual;
        }

        public String getOrdenSurtido() {
            return OrdenSurtido;
        }

        public String getCodigoPallet() {
            return CodigoPallet;
        }

        public String getProducto() {
            return Producto;
        }

        public String getLote() {
            return Lote;
        }

        public String getEmpaques() {
            return Empaques;
        }

        public String getCantidadActual() {
            return CantidadActual;
        }
    }
    ArrayList<Datos> arrayToDisplay;
    TableColumnDpWidthModel columnModel;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;

    Handler handler = new Handler();
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.embarques_activity_embarque);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Embarques_Embarque.this);
        declaraVariables();
        agregaListeners();
        creaTableView();
        ajustarTamañoColumnas();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        try {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                ReiniciaCampos();
            }
            if ((id == R.id.recargar))
            {
                if(OrdenSalida.getText().toString().equals(""))
                {
                    new popUpGenerico(contexto,getCurrentFocus(),"Ingrese orden de embarque.","false",true,true);
                    return false;
                }
                SegundoPlanoEmbarques sp = new SegundoPlanoEmbarques("EntregarPallets");
                sp.execute();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public void creaTableView()
    {
//        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADERS));
        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADERS);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);

    }
    private void declaraVariables()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" " + getString(R.string.Embarques_Embarque));
            //  toolbar.setSubtitle("  Embarque");
//            toolbar.setLogo(R.mipmap.logo_axc);// toolbar.setLogo(R.drawable.axc_logo_toolbar);
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            OrdenSalida = (EditText) findViewById(R.id.edtx_OrdenSalida);
            OrdenSalida.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            Anden = (EditText) findViewById(R.id.edtx_Anden);
            Anden.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            Pallet = (EditText) findViewById(R.id.txtv_Pallet);
            Pallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            tabla = (TableView) findViewById(R.id.tabla);
            tabla.addDataClickListener(new ListenerClickTabla());
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }

    }
    private void agregaListeners()
    {
        try {
            OrdenSalida.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    try
                    {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!OrdenSalida.getText().toString().equals("")) {
                            SPE = new SegundoPlanoEmbarques("EntregarPallets");
                            SPE.execute();
                        } else {
                            Handler handler = new Handler();
                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            OrdenSalida.setText("");
                                            OrdenSalida.requestFocus();
                                        }
                                    }
                            );
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese una orden de surtido.", "false", true, true);
                        }
                        new esconderTeclado(Embarques_Embarque.this);
                    }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                    }
                    return false;
                }
            });
            Anden.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    try
                    {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (!Anden.getText().toString().equals("")) {

                            new esconderTeclado(Embarques_Embarque.this);
                        } else {
                            Handler handler = new Handler();
                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            Anden.setText("");
                                            Anden.requestFocus();
                                        }
                                    }
                            );
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese un código de línea.", "Advertencia", true, true);
                        }
                    }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                    }
                    return false;
                }
            });
            Pallet.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    try
                    {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {


                        if(OrdenSalida.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese orden de surtido.", "false", true, true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Anden.setText("");
                                    Pallet.setText("");
                                    Anden.requestFocus();
                                }
                            });

                            return false;
                        }
                        if(Anden.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese anden.", "false", true, true);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Anden.setText("");
                                    Pallet.setText("");
                                    Anden.requestFocus();
                                }
                            });

                        return false;

                        }



                        if (!Pallet.getText().toString().equals("")) {

                            SPE = new SegundoPlanoEmbarques("RegistrarPalletEntregado");
                            SPE.execute();
                        } else {
                            handler.post(
                                    new Runnable() {
                                        public void run() {
                                            Pallet.setText("");
                                            Pallet.requestFocus();
                                        }
                                    }
                            );
                            new popUpGenerico(contexto, getCurrentFocus(), "Confirme el código de pallet.", "false", true, true);
                        }
                        new esconderTeclado(Embarques_Embarque.this);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
                }

                    return false;

                }});
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }
    public void ajustarTamañoColumnas()
    {
        try
        {
        columnModel = new TableColumnDpWidthModel(contexto,6,150);
        columnModel.setColumnWidth(0,200);
        columnModel.setColumnWidth(1,200);
        columnModel.setColumnWidth(2,200);

        columnModel.setColumnWidth(4,150);
        columnModel.setColumnWidth(5,150);
        tabla.getDataAdapter().notifyDataSetChanged();
        tabla.setColumnModel(columnModel);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }

    private void ReiniciaCampos()
    {
        try
        {
            OrdenSalida.setText("");

            tabla.getDataAdapter().clear();
            tabla.getDataAdapter().notifyDataSetChanged();
            Anden.setText("");
            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
            Pallet.setText("");

            OrdenSalida.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }
    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {

        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            try
            {
                String dato = clickedData[0];
                renglonSeleccionado =rowIndex;


                Log.d("SoapResponse", "onDataClicked: " +clickedData[0]);

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

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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

  class SegundoPlanoEmbarques extends AsyncTask<Void,Void,Void>
  { String decision, mensaje,actividad;

      public SegundoPlanoEmbarques(String Actividad)
      {
          actividad = Actividad;
      }

      @Override
      protected void onPreExecute()
      {
          inAnimation = new AlphaAnimation(0f, 1f);
          inAnimation.setDuration(200);
          progressBarHolder.setAnimation(inAnimation);
          progressBarHolder.setVisibility(View.VISIBLE);
      }

      @Override
      protected Void doInBackground(Void... voids)
      {
          try {
              String StrOrdenSalida = OrdenSalida.getText().toString();

              switch (actividad) {
                  case "EntregarPallets":

                      sa.SOAPListarPalletsAEntregar(StrOrdenSalida, contexto);

                      break;
                  case "RegistrarPalletEntregado":
                      String Linea = Anden.getText().toString();
                      sa.SOAPRegistrarPalletEntregado(StrOrdenSalida, Pallet.getText().toString(), Linea, "", contexto);
                      break;

              }
              decision = sa.getDecision();
              mensaje = sa.getMensaje();
              if (decision.equals("true") && actividad.equals("EntregarPallets")) {
                  sacaDatos();
                  if (arrayToDisplay != null) {
                      DATA_TO_SHOW = new String[arrayToDisplay.size()][7];
                      int i = 0;
                      for (Datos a : arrayToDisplay) {

                          //DATA_TO_SHOW[i][0] = a.getCodigoPallet();
                          DATA_TO_SHOW[i][0] = a.getOrdenSurtido();
                          DATA_TO_SHOW[i][1] = a.getCodigoPallet();
                          DATA_TO_SHOW[i][2] = a.getProducto();
                          DATA_TO_SHOW[i][3] = a.getLote();
                          DATA_TO_SHOW[i][4] = a.getEmpaques();
                          DATA_TO_SHOW[i][5] = a.getCantidadActual();

                          i++;
                          //   Log.i("SoapResponse", "sacaDatos: " + i);

                      }
                  }
              }
          }catch (Exception e)
          {
              mensaje = e.getMessage();
              decision = "false";
          }
          return null;

      }

      @Override
      protected void onPostExecute(Void aVoid)
      {
          try
              {
              if (decision.equals("true"))
              {

                  switch (actividad)
                      {
                      case "EntregarPallets":
                          Pallet.setText("");

                          tabla.setDataAdapter(st = new SimpleTableDataAdapter(Embarques_Embarque.this, DATA_TO_SHOW));
                          tabla.getDataAdapter().notifyDataSetChanged();
                          tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                          arrayToDisplay.clear();
                          break;

                      case "RegistrarPalletEntregado":
                          SegundoPlanoEmbarques sp = new SegundoPlanoEmbarques("EntregarPallets");
                          sp.execute();
//                          new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                          break;
                  }
              }
              if(decision.equals("false"))
              {
                  new popUpGenerico(contexto,getCurrentFocus(),mensaje,decision,true,true);
                  ReiniciaCampos();

              }

          }catch (Exception e)
          {
              e.printStackTrace();
              new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),decision,true,true);

          }

          outAnimation = new AlphaAnimation(1f, 0f);
          outAnimation.setDuration(200);
          progressBarHolder.setAnimation(outAnimation);
          progressBarHolder.setVisibility(View.GONE);
      }
  }
  public void sacaDatos()
  {

      SoapObject tabla = sa.parser();
  if (tabla != null)
      {
      String OrdenSurtido,CodigoPallet,Producto,Lote,Empaques,CantidadActual;
      Datos datos ;
      arrayToDisplay = new ArrayList<>();


          for(int i = 0; i<tabla.getPropertyCount();i++)
          {
              try {

                  SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                  Log.d("SoapResponse",tabla1.toString());


                  OrdenSurtido =  tabla1.getPrimitivePropertyAsString("OrdenSurtido");
                  CodigoPallet = tabla1.getPrimitivePropertyAsString("CodigoPallet");
                  pallet = CodigoPallet;
                  Producto =  tabla1.getPrimitivePropertyAsString("Producto");
                  Lote= tabla1.getPrimitivePropertyAsString("Lote");
                  Empaques= tabla1.getPrimitivePropertyAsString("Empaques");
                  CantidadActual= tabla1.getPrimitivePropertyAsString("CantidadActual");

                  datos = new Datos(OrdenSurtido,CodigoPallet,Producto,Lote,Empaques,CantidadActual);
                  arrayToDisplay.add(datos);

              }catch (Exception e)
              {

                  e.printStackTrace();
              }
          }
      }
  }
}
