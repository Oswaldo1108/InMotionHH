package com.automatica.AXCMP.c_Inventarios.CiclicosPorProducto;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.c_Inventarios.Inventarios_InventiarioCiclicoPorPosicionDet;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Inventarios_PorProducto extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();


    String Producto, CodigoPos,Estado;
    String ProductoSeleccionado;
    Button btnIniciarInventario;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TableView tabla;
    Bundle b;
    String UbicacionIntent ="",IdInventario;
    View vista;
    Context contexto = this;

    String[] HEADER = {"Producto","Código de Posición ","Estado"};
    String[][] DATA_TO_SHOW;
    String actividad = "Producto";//Aqui le digo a la suguiente pantalla que continue el proceso dedpendiendo de que inventario se hara
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;


    private class datosTabla
    {
        String Producto, CodigoPos,Estado;

        public datosTabla(String producto,  String codigoPos, String estado)
        {
            Producto = producto;
            CodigoPos = codigoPos;
            Estado = estado;
        }

        public String getProducto() {
            return Producto;
        }


        public String getCodigoPos() {
            return CodigoPos;
        }

        public String getEstado() {
            return Estado;
        }
    }
    ArrayList<datosTabla> DataToFill;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarios_activity_por_producto);
        new cambiaColorStatusBar(contexto, R.color.RojoStd,Inventarios_PorProducto.this);
        declararVariables();
        AgregaListeners();
        getExtrasIntent();
        SegundoPlano sp = new SegundoPlano("llenarTabla");
        sp.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }
        if((id == R.id.recargar))
        {
            //tabla.getDataAdapter().clear();
          SegundoPlano sp = new SegundoPlano("llenarTabla");
            sp.execute();
        }

        return super.onOptionsItemSelected(item);
    }
    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {

//            renglonSeleccionado = rowIndex;
//            tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
//            tabla.getDataAdapter().notifyDataSetChanged();
//            UbicacionIntent = clickedData[2];
//            Log.i("SoapResponse",UbicacionIntent+clickedData[2]);
//            btnIniciarInventario.setEnabled(true);



            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tabla.getDataAdapter().notifyDataSetChanged();
                UbicacionIntent = clickedData[1];
                Log.i("SoapResponse",UbicacionIntent+clickedData[2]);
                btnIniciarInventario.setEnabled(true);


            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();

                UbicacionIntent =null;
                Log.i("SoapResponse",UbicacionIntent+clickedData[2]);
                btnIniciarInventario.setEnabled(false);



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
                Color = R.color.AzulStd;
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
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Inventarios_CiclicoPorProducto));
        // toolbar.setSubtitle("  Inventario Por Producto");
        toolbar.setLogo(R.mipmap.logo_axc);//    toolbar.setLogo(R.drawable.axc_logo_toolbar);

        tabla = (TableView) findViewById(R.id.tableView_TomarInventario);

        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        btnIniciarInventario = (Button) findViewById(R.id.btn_TomarInventario) ;




    }
    private void getExtrasIntent()
    {
        b = getIntent().getExtras();
        IdInventario = b.getString("IdInventario");
    }
    private void AgregaListeners()
    {

        btnIniciarInventario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Inventarios_PorProducto.this, Inventarios_InventiarioCiclicoPorPosicionDet.class);


                b.putString("UbicacionIntent",UbicacionIntent);

                b.putString("Actividad",actividad);

                intent.putExtras(b);
                startActivity(intent);
            }
        });
        tabla.addDataClickListener(new ListenerClickTabla());

    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
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
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
            {

                switch (tarea)
                {
                    case "llenarTabla":
                        sa.SOAPConsultaInventarioNumeroParte(IdInventario, contexto);
                        break;

                }

                mensaje = sa.getMensaje();
                decision = sa.getDecision();

                if (decision.equals("true"))
                {
                    sacaDatos();
                    int i = 0;
                    DATA_TO_SHOW = new String[DataToFill.size()][6];
                    for (datosTabla a : DataToFill) {
                        DATA_TO_SHOW[i][0] = a.getProducto();
                        DATA_TO_SHOW[i][1] = a.getCodigoPos();
                        DATA_TO_SHOW[i][2] = a.getEstado();


                        i++;
                        Log.i("SoapResponse", " SOAPDATOS" + DATA_TO_SHOW);
                    }
//                if( sa.getTablaSoap()== null)
////                {
////                    decision = "";
////                }

                }

            }catch (Exception e)
            {
                e.printStackTrace();
                decision = "false";
                mensaje = e.getMessage();
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
                    tabla.getDataAdapter().notifyDataSetChanged();
                    tabla.setDataAdapter(st = new SimpleTableDataAdapter(contexto, DATA_TO_SHOW));
                    tabla.getDataAdapter().notifyDataSetChanged();
                    DataToFill.clear();
                }

                if (decision.equals("false"))
                {
                    new popUpGenerico(contexto, getCurrentFocus(), mensaje, decision, true, true);
                }



          /*  if(decision.equals("true")&&tarea.equals(" "))
            {
                new popUpGenerico(contexto, vista,mensaje,decision, true, true);

            }*/
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
        DataToFill = new ArrayList<>();
        if (tabla != null) {
            for (int i = 0; i < tabla.getPropertyCount(); i++)
            {

                try
                {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    Log.d("SoapResponse", tabla.toString());
                    Producto = tabla1.getPrimitivePropertyAsString("NumParte");
                    CodigoPos = tabla1.getPrimitivePropertyAsString("CodigoPos");
                    Estado = tabla1.getPrimitivePropertyAsString("Estado");
                    Log.i("SoapResponse", "sacaDatos: " + tabla1);

                    datosTabla d = new datosTabla(Producto, CodigoPos, Estado);
                    DataToFill.add(d);
                  //  Log.i("SoapResponse", "sacaDatos: " + tabla1.getPrimitivePropertyAsString("NumParte"));

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
