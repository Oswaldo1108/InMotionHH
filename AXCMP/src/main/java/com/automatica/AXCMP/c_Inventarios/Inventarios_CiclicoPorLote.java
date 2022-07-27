package com.automatica.AXCMP.c_Inventarios;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Inventarios_CiclicoPorLote extends AppCompatActivity
{

    //region variables
    SoapAction sa = new SoapAction();

    EditText TipoDeInventario;
    TableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    TableColumnDpWidthModel columnModel;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Codigo de Posición","Estado"};
    String[][] DATA_TO_SHOW;
    String IdInventario,Actividad="CiclicoPorLote";
    ArrayList<posiciones> arrayListPosiciones;
    int renglonSeleccionado;
    String IdUbicacionSeleccionado = "";
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;
    class posiciones
    {
        String CodigoPosicion,Estado;

        public posiciones(String codigoPosicion, String estado)
        {
            CodigoPosicion = codigoPosicion;
            Estado = estado;

        }

        public String getCodigoPosicion()
        {
            return CodigoPosicion;
        }

        public String getEstado()
        {
            return Estado;
        }
    }
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarios_activity_ciclico_por_lote);
        new cambiaColorStatusBar(contexto, R.color.RojoStd,Inventarios_CiclicoPorLote.this);
        declaraVariables();
        ajustarTamañoColumnas();
        SacaDatosIntent();
        AgregaListeners();
        SegundoPlano sp = new SegundoPlano();
        sp.execute();

    }
    private void declaraVariables()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" " + getString(R.string.Inventarios_CiclicoPorLote));
            tabla = (TableView) findViewById(R.id.tabla);
            tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto, HEADER));
            btnConfirmar = (Button) findViewById(R.id.btn_Confirmar);
            btnConfirmar.setEnabled(true);
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }
    private void AgregaListeners()
    {
        btnConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Bundle b = new Bundle();
                b.putString("UbicacionIntent",IdUbicacionSeleccionado);
                b.putString("IdInventario",IdInventario);
                b.putString("Actividad",Actividad);
                Intent intent = new Intent(Inventarios_CiclicoPorLote.this,Inventarios_InventiarioCiclicoPorPosicionDet.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        tabla.addDataClickListener(new ListenerClickTabla());
    }
    private void SacaDatosIntent()
    {

        Bundle b  = getIntent().getExtras();
        IdInventario = b.getString("IdInventario");
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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void ajustarTamañoColumnas()
    {
        columnModel = new TableColumnDpWidthModel(contexto,2,150);
        columnModel.setColumnWidth(0,185);
        columnModel.setColumnWidth(1,150);

        tabla.getDataAdapter().notifyDataSetChanged();
        tabla.setColumnModel(columnModel);
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
            SegundoPlano sp = new SegundoPlano();
            sp.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {

            renglonSeleccionado = rowIndex;
            tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
            tabla.getDataAdapter().notifyDataSetChanged();
            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tabla.getDataAdapter().notifyDataSetChanged();
                btnConfirmar.setEnabled(true);
                IdUbicacionSeleccionado = clickedData[0];



            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();

                renglonAnterior=-1;

                btnConfirmar.setEnabled(true);
                IdUbicacionSeleccionado = null;

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

    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String decision,mensaje;

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
            sa.SOAPConsultaInventarioPosicion(IdInventario,contexto);
            decision = sa.getDecision();
            mensaje = sa.getMensaje();
            if(decision.equals("true"))
            {
                sacaDatos();
                if(decision.equals("true")&&arrayListPosiciones!=null)
                {
                    int i = 0;
                    DATA_TO_SHOW = new String[arrayListPosiciones.size()][2];
                    for(posiciones a: arrayListPosiciones)
                    {
                        DATA_TO_SHOW[i][0] = a.getCodigoPosicion();
                        DATA_TO_SHOW[i][1] = a.getEstado();
                        i++;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(decision.equals("true"))
            {
                tabla.getDataAdapter().notifyDataSetChanged();
                tabla.setDataAdapter(st = new SimpleTableDataAdapter(Inventarios_CiclicoPorLote.this, DATA_TO_SHOW));
                tabla.getDataAdapter().notifyDataSetChanged();
                arrayListPosiciones.clear();
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto,vista,mensaje,"Advertencia",true,true);
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
        String CodigoPos,Estado;

        arrayListPosiciones = new ArrayList<>();
        posiciones p;

        if(tabla!=null) {
            for (int i = 0; i < tabla.getPropertyCount(); i++)
            {
                try {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    Log.d("SoapResponse", tabla1.toString());
                    CodigoPos = tabla1.getPrimitivePropertyAsString("CodigoPos");
                    Estado = tabla1.getPrimitivePropertyAsString("Estado");

                    p = new posiciones(CodigoPos, Estado);
                    arrayListPosiciones.add(p);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

}
