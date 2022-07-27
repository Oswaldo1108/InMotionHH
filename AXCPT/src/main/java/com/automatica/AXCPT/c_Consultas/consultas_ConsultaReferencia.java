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
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class consultas_ConsultaReferencia extends AppCompatActivity
{
    Toolbar toolbar;

    EditText Referencia;
    SortableTableView tabla;
    SimpleTableDataAdapter st ;
    SimpleTableHeaderAdapter sthd;
    String datoReferencia;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    Handler handlerRequestFocus = new Handler();
    Context contexto=this;
    View vista ;
    TableColumnDpWidthModel columnModel;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_consulta_referencia);
        try
        {
            creaToolbar(toolbar,"Consulta por Referencia");
           // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            declaraVariables();
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }

    }
    public void creaToolbar(Toolbar toolbar, String Titulo)
    {
        try
        {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" "+getString(R.string.Consultas_Referencia));
            //toolbar.setSubtitle(Titulo);
//            toolbar.setLogo(R.mipmap.logo_axc);//    toolbar.setLogo(R.drawable.axc_logo_toolbar);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }

        if((id == R.id.borrar_datos))
        {
            reiniciarPantalla();
        }

        return super.onOptionsItemSelected(item);
    }

    public void reiniciarPantalla()
    {
        try
        {
            tabla.getDataAdapter().clear();
            tabla.getDataAdapter().notifyDataSetChanged();
            Referencia.setText("");
            requestFocusTextFields();
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }


    }
    public void declaraVariables()
    {
        try
        {
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            tabla = findViewById(R.id.tblv_Referencia);

//            tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(consultas_ConsultaReferencia.this, HEADERS));

            tabla.addDataClickListener(new ListenerClickTabla());
            //tabla.addDataLongClickListener(new ListenerLongClickTabla());
            Referencia = findViewById(R.id.edtx_Referencia);
            Referencia.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            Referencia.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if (Referencia.getText().toString().equals("")){
                            new popUpGenerico(consultas_ConsultaReferencia.this,getCurrentFocus(),"Favor de llenar campo ''Referencia''","Advertencia",true,true);
                        }else{
                            llenaTablaReferencias tr = new llenaTablaReferencias();
                            tr.execute();
                            new esconderTeclado(consultas_ConsultaReferencia.this);
                        }
                    }
                    return false;
                }
            });
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }


    }

    public void ajustarTamañoColumnas()//metodo normal
    {
        try
          {
            columnModel = new TableColumnDpWidthModel(contexto, 16, 100);
            columnModel.setColumnWidth(0,350);//Transaccion
              columnModel.setColumnWidth(1,120);//Transaccion
              columnModel.setColumnWidth(2,120);//Transaccion
              columnModel.setColumnWidth(3,120);//Transaccion
            columnModel.setColumnWidth(4,120);//ERPALmacen
            columnModel.setColumnWidth(5,120);//dato1
            columnModel.setColumnWidth(6,150);//dato1
            columnModel.setColumnWidth(7,150);//dato1
            columnModel.setColumnWidth(8,150);//dato1
            columnModel.setColumnWidth(9,120);//dato1
            columnModel.setColumnWidth(10,120);//dato1
            columnModel.setColumnWidth(11,120);//dato1
            columnModel.setColumnWidth(12,120);//dato1
            columnModel.setColumnWidth(13,120);//dato1
            columnModel.setColumnWidth(14,120);//dato1
            columnModel.setColumnWidth(15,400);//dato1
            }catch (Exception e)
            {
                new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
            }

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
    /*
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
            String DataToShow="";
            int i =0;
            for(String data:clickedData)
            {
                DataToShow +=HEADERS[i] +" - "+ data + "\n";
                i++;
            }
            new popUpGenerico(contexto, getCurrentFocus(), DataToShow, "Información", true, false);

            return false;
        }
    }

     */
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

    public void requestFocusTextFields()
    {

        try {
            handlerRequestFocus.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Referencia.clearFocus();       //Aqui hago request focus a el textfield, para seguir escaneando
                    Referencia.requestFocus();
                    Referencia.setText("");
                }
            });
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }


    }
    class llenaTablaReferencias extends AsyncTask<Void,Void,Void>
    {
        DataAccessObject dao;
        cAccesoADatos_Consultas ca = new cAccesoADatos_Consultas(consultas_ConsultaReferencia.this);
        @Override
        protected void onPreExecute()
        {

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try {
                datoReferencia = Referencia.getText().toString();
                dao= ca.c_ConsultaReferencia(datoReferencia);
                //dao = ca1.SOAPConsultaReferencia(datoReferencia);
            }catch (Exception e)
            {
              e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if(dao.iscEstado())
                {
                    tabla.setDataAdapter(st = new SimpleTableDataAdapter(consultas_ConsultaReferencia.this, dao.getcTablaUnica()));
                    tabla.setHeaderAdapter(sthd= new SimpleTableHeaderAdapter(consultas_ConsultaReferencia.this,dao.getcEncabezado()));
                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                    sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                    ajustarTamañoColumnas();
                    tabla.setColumnModel(columnModel);
                    tabla.getDataAdapter().notifyDataSetChanged();
                    //HEADERS = dao.getcEncabezado();
                }
                else
                {
                    requestFocusTextFields();
                    reiniciarPantalla();
                    new popUpGenerico(contexto,getCurrentFocus(),dao.getcMensaje()+"1","Advertencia",true,true);
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto,getCurrentFocus(),dao.getcMensaje(),"Advertencia",true,true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }



    //region Soap
//endregion

}
