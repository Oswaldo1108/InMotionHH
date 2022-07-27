package com.automatica.AXCPT.c_Consultas;

import android.annotation.SuppressLint;
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
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;


import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class consultas_ConsultaPosicion extends AppCompatActivity
{
//region variables
    Context contexto = this;
    View vista;
    TextView empaque;
    cAccesoADatos_Consultas ca;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    HorizontalScrollView horizontalScrollView;
    final Handler handlerRequestFocus = new Handler();
    HiloRecibeDatos hrd = new HiloRecibeDatos();
    SortableTableView tabla;
    SimpleTableHeaderAdapter sthd;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;
    //String[] TABLE_HEADERS = { "Código pallet", "Producto", "Descripción", "Lote","Empaques", "Cantidad Actual", "Cantidad Pallets" };
    //endregion
//region generado
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_consulta_posicion);
        ca = new cAccesoADatos_Consultas(getApplicationContext());
        try
        {
            empaque = (EditText) findViewById(R.id.edtx_posicion);
            empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_tabla_embarques);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" "+getString(R.string.Consultas_Posicion));
            //  toolbar.setSubtitle("  Consulta de Posición");
//            toolbar.setLogo(R.mipmap.logo_axc);//    toolbar.setLogo(R.drawable.axc_logo_toolbar);


            tabla = findViewById(R.id.tableView_posicion);
//            tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(consultas_ConsultaPosicion.this, TABLE_HEADERS));

            tabla.addDataClickListener(new ListenerClickTabla());


          //  this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                        if (empaque.getText().toString().equals("")){
                            new popUpGenerico(consultas_ConsultaPosicion.this,getCurrentFocus(),"Favor de llenar campo ''Posición''","Advertencia",true,true);
                    }else{
                        hrd.cancel(true);
                        hrd = new HiloRecibeDatos();
                        hrd.execute();
                        requestFocusTextFields();
                        new esconderTeclado(consultas_ConsultaPosicion.this);
                    }
                    }
                    return false;
                }
            });


            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            new cambiaColorStatusBar(contexto, R.color.AzulStd, consultas_ConsultaPosicion.this);



        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }
    }

    public void requestFocusTextFields()
    {

        try
        {
            handlerRequestFocus.post(new Runnable()
            {
                @Override
                public void run()
                {
                    empaque.clearFocus();       //Aqui hago request focus a el textfield, para seguir escaneando
                    empaque.requestFocus();
                    //empaque.setText("");
                }
            });
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        try
        {
            if((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto,vista);
            }

            if((id == R.id.borrar_datos))
            {

                empaque.setText("");
                tabla.getDataAdapter().clear();
                tabla.getDataAdapter().notifyDataSetChanged();
            }
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }


        return super.onOptionsItemSelected(item);
    }

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


    //endregion


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
        String datoConsulta;
        DataAccessObject dao;
        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            consultas_ConsultaPosicion.this.tabla.getDataAdapter().clear();
            consultas_ConsultaPosicion.this.tabla.getDataAdapter().notifyDataSetChanged();

        }
        @Override
        protected Void doInBackground(Void... voids)
        {

          //  Log.i("SoapResponse", " background");
           try
           {
               datoConsulta = empaque.getText().toString();    //Lanzo consulta, enviando los Datos a SoapAction
                      dao = ca.c_BuscarUbicacion(datoConsulta);
           }catch (Exception e)
           {
               e.printStackTrace();
           }




            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {

          //  Log.i("SoapResponse", " On postexecute");
            try
            {
                if(dao.iscEstado())
                {

                    if(dao.getcTablaUnica()!=null)
                        {
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(consultas_ConsultaPosicion.this, dao.getcTablaUnica()));
                            tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(consultas_ConsultaPosicion.this, dao.getcEncabezado()));
                            consultas_ConsultaPosicion.this.tabla.getDataAdapter().notifyDataSetChanged();
                            st.setTextColor(R.color.blancoLetraStd);
                            sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                        }

                }
                else
                {

                    requestFocusTextFields();
                    new popUpGenerico(contexto,getCurrentFocus(),dao.getcMensaje(),"Advertencia",true,true);
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"Advertencia",true,true);
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    //endregion

}
