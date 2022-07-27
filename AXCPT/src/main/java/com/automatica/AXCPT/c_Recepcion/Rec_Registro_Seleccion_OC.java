package com.automatica.AXCPT.c_Recepcion;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

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

public class Rec_Registro_Seleccion_OC extends AppCompatActivity
{
    //region variables


    SortableTableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String OrdenCompra;
    TableColumnDpWidthModel columnModel;
    SimpleTableDataAdapter st ;
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();

    SimpleTableHeaderAdapter sthd;

    Handler h = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;

    boolean OrdenCompraSeleccionada = true, Recarga=true;
    private String[] HEADER;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity_registro_seleccion_oc);
        new cambiaColorStatusBar(contexto, R.color.VerdeStd, Rec_Registro_Seleccion_OC.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declaraVariables();
        ajustarTamañoColumnas();



        AgregaListeners();



    }

    @Override
    protected void onResume()
    {
       new SegundoPlano("Tabla").execute();
        super.onResume();
    }

    private void declaraVariables()
    {
        try
            {
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(getString(R.string.recepcion_OC));

                tabla = (SortableTableView) findViewById(R.id.tableView_OC);
                btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);
                progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() ,false ,true,true );

            }

    }
    private void AgregaListeners()
    {
        btnConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidacionFinal();
            }
        });
        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());
        tabla.addDataLongClickListener(new ListenerLongClickTabla());
    }
    private void ValidacionFinal()
    {

        if(OrdenCompraSeleccionada)
        {

            b.putString("FechaCaducidad", "FechaCaducidad");
            Intent intent = new Intent(Rec_Registro_Seleccion_OC.this, Rec_Registro_Seleccion_Partida.class);
            intent.putExtras(b);
            startActivity(intent);
        }else
        {
            new popUpGenerico(contexto,vista ,getString(R.string.error_seleccionar_registro) ,"false" ,true,true );
        }

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
        columnModel = new TableColumnDpWidthModel(contexto,10,150);
        columnModel.setColumnWidth(0,185);
        columnModel.setColumnWidth(1,150);
        tabla.getDataAdapter().notifyDataSetChanged();
        tabla.setColumnModel(columnModel);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(Recarga)
        {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar)
            {
                SegundoPlano sp = new SegundoPlano("Tabla");
                sp.execute();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                b.putString("Orden",clickedData[0]);
                b.putString("Tipo",clickedData[1]);
                btnConfirmar.setEnabled(true);
//                OrdenCompraSeleccionada = true;
            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                b = new Bundle();
                renglonAnterior=-1;
//                OrdenCompraSeleccionada = false;
            }
        }

    }
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
            String DataToShow="";
            int i =0;
            for(String data:clickedData)
            {
                DataToShow +=HEADER[i] +" - "+ data + "\n";
                i++;
            }
            new popUpGenerico(contexto, vista, DataToShow, "Información", true, false);

            return false;
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
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {

            Toast.makeText(contexto, HEADER[columnIndex], Toast.LENGTH_SHORT).show();
            Log.d("SoapResponse", HEADER[columnIndex]);
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
    private void reiniciarDatos()
    {


        tabla.getDataAdapter().clear();
        b.clear();

    }
    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(Rec_Registro_Seleccion_OC.this);

        String Tarea;
        DataAccessObject dao;
        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {
            Recarga = false;
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
                switch (Tarea)
                {
                    case"Tabla":
                      //  dao = ca.c_ListarOrdenesCompraLiberadas();
                        break;
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

                            if (dao.getcTablaUnica()!=null)
                                {
                                    tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(Rec_Registro_Seleccion_OC.this, dao.getcEncabezado()));
                                    tabla.setDataAdapter(st = new SimpleTableDataAdapter(Rec_Registro_Seleccion_OC.this, dao.getcTablaUnica()));
                                    tabla.getDataAdapter().notifyDataSetChanged();
                                    tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                    sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));


                                }
                            renglonAnterior = -1;
                            break;

                    }
                }else {
                    new popUpGenerico(contexto, vista, dao.getcMensaje(),dao.iscEstado(), true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            Recarga = true;
        }

        @Override
        protected void onCancelled()
        {

            Log.e("SP", "onCancelled: hilo terminado" );

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            return;

        }
    }
    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") != null){
            super.onBackPressed();
        }else {
            Intent intent = new Intent(contexto, Inicio_Menu_Dinamico.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
        }

    }
}
