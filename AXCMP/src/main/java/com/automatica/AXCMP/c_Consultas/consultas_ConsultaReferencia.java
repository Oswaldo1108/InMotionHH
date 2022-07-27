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
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;


import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class consultas_ConsultaReferencia extends AppCompatActivity
{

    Toolbar toolbar;

    EditText Referencia;
    SortableTableView<String[]> tablaReferencia;

    String datoReferencia;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    Handler handlerRequestFocus = new Handler();
    boolean ReiniciarTabla;
    SoapAction sa = new SoapAction();
    Context contexto=this;
    View vista ;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;
    TableColumnDpWidthModel columnModel;
    constructorConsultaReferencia constrReferencia;
    ArrayList<constructorConsultaReferencia> ArrayReferencia;
    String[] HEADERS = {"Transacción","Usuario","Nombre","Estación","Almacén","dato","dato2","dato3","dato4","dato5","dato6","dato7","dato8","dato9","dato10","FechaCrea"};
    String[][] DATA_TO_SHOW;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_consulta_referencia);
        creaToolbar(toolbar,"Consulta por Referencia");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        declaraVariables();
    }
    public void creaToolbar(Toolbar toolbar, String Titulo)
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.Consultas_Referencia));

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

        tablaReferencia.getDataAdapter().clear();
        tablaReferencia.getDataAdapter().notifyDataSetChanged();
        Referencia.setText("");
        requestFocusTextFields();

    }
    public void declaraVariables()
    {
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        tablaReferencia = (SortableTableView) findViewById(R.id.tblv_Referencia);
        tablaReferencia.setHeaderAdapter(new SimpleTableHeaderAdapter(consultas_ConsultaReferencia.this, HEADERS));
        Referencia = findViewById(R.id.edtx_Referencia);
        Referencia.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        Referencia.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {


                        llenaTablaReferencias tr = new llenaTablaReferencias();
                        tr.execute();

                        new esconderTeclado(consultas_ConsultaReferencia.this);
                }
                return false;
            }
        });
        tablaReferencia.addDataClickListener(new ListenerClickTabla());
        tablaReferencia.addHeaderClickListener(new headerClickListener());
        tablaReferencia.addDataLongClickListener(new ListenerLongClickTabla());
    }
    public void ajustarTamañoColumnas()
    {
        columnModel = new TableColumnDpWidthModel(contexto, 16, 100);
        columnModel.setColumnWidth(0,400);//Transaccion
        columnModel.setColumnWidth(4,150);//ERPALmacen
        columnModel.setColumnWidth(5,160);//dato1
        columnModel.setColumnWidth(6,160);//dato1
        columnModel.setColumnWidth(7,160);//dato1
        columnModel.setColumnWidth(8,160);//dato1
        columnModel.setColumnWidth(9,160);//dato1
        columnModel.setColumnWidth(10,160);//dato1
        columnModel.setColumnWidth(11,160);//dato1
        columnModel.setColumnWidth(12,160);//dato1
        columnModel.setColumnWidth(13,160);//dato1
        columnModel.setColumnWidth(14,160);//dato1
        columnModel.setColumnWidth(15,400);//dato1
        columnModel.setColumnWidth(16,400);
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
                tablaReferencia.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tablaReferencia.getDataAdapter().notifyDataSetChanged();

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tablaReferencia.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tablaReferencia.getDataAdapter().notifyDataSetChanged();

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
            String DataToShow="";
            int i =0;
            for(String data:clickedData)
            {
                DataToShow +=HEADERS[i] +" - "+ data + "\n";
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


    public void requestFocusTextFields()
    {


        handlerRequestFocus.post(new Runnable()
        {
            @Override
            public void run()
            {
                Referencia.clearFocus();       //Aqui hago request focus a el textfield, para seguir escaneando
                Referencia.requestFocus();
                //Referencia.setText("");
            }
        });

    }
    class llenaTablaReferencias extends AsyncTask<Void,Void,Void>
    {
        String mensaje,decision;
        DataAccessObject dao = null;
        cAccesoADatos ca = new cAccesoADatos(contexto);
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

                if (datoReferencia.equals(""))
                {
                    decision = "false";
                    mensaje = getString(R.string.error_ingrese_referencia);


                } else
                    {

                        dao = ca.SOAPConsultaReferencia(datoReferencia);

                    }

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


                if (dao.iscEstado())
                {

                    ajustarTamañoColumnas();
                    tablaReferencia.setDataAdapter(st = new SimpleTableDataAdapter(consultas_ConsultaReferencia.this, dao.getcTablaUnica()));
                    tablaReferencia.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,dao.getcEncabezado()));
                    tablaReferencia.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                    tablaReferencia.getDataAdapter().notifyDataSetChanged();
                    tablaReferencia.setColumnModel(columnModel);


                } else
                {
                    requestFocusTextFields();
                    reiniciarPantalla();
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), false, true, true);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }

}
