package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Ajustes_Registro extends AppCompatActivity
{


    //region variables
    cAccesoADatos_Almacen ca;
    TableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    TableColumnDpWidthModel columnModel;
    View vista;
    Context contexto = this;
    String IdAjustesSeleccionado, Producto, Cantidad,CantReg;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;
    SimpleTableHeaderAdapter sthd;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_ajustes__registro);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,this);
        declaraVariables();
        ajustarTamañoColumnas();

        AgregaListeners();
       SegundoPlano sp = new SegundoPlano();
        sp.execute();

    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Ajustes));
        //toolbar.setSubtitle(" Registro Ajuste");
//        toolbar.setLogo(R.mipmap.logo_axc);//   toolbar.setLogo(R.drawable.axc_logo_toolbar);
        tabla = (TableView) findViewById(R.id.tableView_ajustes);

//        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);

        btnConfirmar = (Button) findViewById(R.id.btn_Confirmar);
        btnConfirmar.setEnabled(false);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
    }
    private void AgregaListeners()
    {
        btnConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Bundle b = new Bundle();
                b.putString("IdAjustesSeleccionado",IdAjustesSeleccionado);
                b.putString("Producto", Producto);
                b.putString("Cantidad",Cantidad);
                b.putString("CantReg",CantReg);
                Intent intent = new Intent(Almacen_Ajustes_Registro.this,Almacen_Ajustes_AjustePallet.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        tabla.addDataClickListener(new ListenerClickTabla());
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
        columnModel = new TableColumnDpWidthModel(contexto,7,150);
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
          /*  tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
            tabla.getDataAdapter().notifyDataSetChanged();
            btnConfirmar.setEnabled(true);*/



            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                IdAjustesSeleccionado = clickedData[0];
                Producto = clickedData[1];
                Cantidad = clickedData[2];
                CantReg = clickedData[3];

                btnConfirmar.setEnabled(true);
                tabla.getDataAdapter().notifyDataSetChanged();


            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                IdAjustesSeleccionado = null;
                Producto = null;
                Cantidad = null;
                CantReg = null;

                btnConfirmar.setEnabled(false);
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

    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        DataAccessObject dao;

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
            //sa.SOAPAjustesPositivos(contexto);
            dao= ca.c_AjustesPositivos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if(dao.iscEstado())
            {
                tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Ajustes_Registro.this, dao.getcTablaUnica()));
                tabla.setHeaderAdapter(sthd= new SimpleTableHeaderAdapter(Almacen_Ajustes_Registro.this, dao.getcEncabezado()));
                st.setTextColor(R.color.blancoLetraStd);
                sthd.setTextColor(R.color.blancoLetraStd);
                tabla.getDataAdapter().notifyDataSetChanged();
            }

            else
            {
                new popUpGenerico(contexto,vista,dao.getcMensaje(),"Advertencia",true,true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}
