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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class consultas_BusquedaExistencia extends AppCompatActivity {

    EditText Producto;
    Spinner Resultados;
    Button Buscar;
    Toolbar toolbar;
    Context contexto = this;
    String NumParte;
    View vista;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    cAccesoADatos_Consultas ca;
    TableColumnDpWidthModel columnModel;
    SortableTableView tabla;
    Handler handlerRequestFocus = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;
    SimpleTableDataAdapter st ;
    SimpleTableHeaderAdapter sthd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_busqueda_existencia);
        ca = new cAccesoADatos_Consultas(getApplicationContext());
        try
        {
            creaToolbar(toolbar," Existencias");
         //   this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            new cambiaColorStatusBar(contexto,R.color.AzulStd,consultas_BusquedaExistencia.this);
            declaraVariables();
            Producto.requestFocus();
        }catch (Exception e)
        {

            new popUpGenerico(contexto,vista,e.getMessage(),"Advertencia",true,true);
        }



    }
    public void declaraVariables()
    {
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            Producto = (EditText) findViewById(R.id.txtv_Producto);
            Producto.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
            Resultados = (Spinner) findViewById(R.id.spn_Resultados);
            Resultados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    NumParte=((Constructor_Dato)Resultados.getSelectedItem()).getTitulo();
                    //txtv_CantRegLote.setText( ((Constructor_Dato)Resultados.getSelectedItem()).getDato());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Buscar = (Button) findViewById(R.id.btn_ConsultaProducto);
            Buscar.setEnabled(false);
            tabla =findViewById(R.id.tblv_ResultadosProducto);
//            tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(consultas_BusquedaExistencia.this, ));
            tabla.addDataClickListener(new ListenerClickTabla());
            Producto.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if (Producto.getText().toString().equals("")){
                            new popUpGenerico(consultas_BusquedaExistencia.this,getCurrentFocus(),"Por favor, Llenar campo Producto","Advertencia",true,true);
                        }else{
                            SegundoPlanoMuestraTabla spb = new SegundoPlanoMuestraTabla("ConsultaProducto");
                            spb.execute();
                            new esconderTeclado(consultas_BusquedaExistencia.this);
                        }
                    }
                    return false;
                }
            });

            Buscar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SegundoPlanoMuestraTabla spb = new SegundoPlanoMuestraTabla("MuestraTabla");
                    spb.execute();
                    new esconderTeclado(consultas_BusquedaExistencia.this);
                }
            });
    }

    public void creaToolbar(Toolbar toolbar, String Titulo)
    {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" "+getString(R.string.Consultas_Existencias));
            //toolbar.setSubtitle("  "+Titulo);
//            toolbar.setLogo(R.mipmap.logo_axc);//    toolbar.setLogo(R.drawable.axc_logo_toolbar);


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

            if((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto,vista);
            }

            if((id == R.id.borrar_datos))
            {

                reiniciarVariables();
            }
        return super.onOptionsItemSelected(item);
    }

    public void reiniciarVariables()
    {
            tabla.getDataAdapter().clear();
            tabla.getDataAdapter().notifyDataSetChanged();
            requestFocusTextFields();
            Resultados.setAdapter(null);
            Buscar.setEnabled(false);
    }
    public void ajustarTamañoColumnas()
    {
            columnModel = new TableColumnDpWidthModel(contexto,6,120);
            columnModel.setColumnWidth(1,200);
            columnModel.setColumnWidth(3,250);
            columnModel.setColumnWidth(5,200);
    }
    public void requestFocusTextFields()
    {

            handlerRequestFocus.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Producto.clearFocus();       //Aqui hago request focus a el textfield, para seguir escaneando
                    Producto.requestFocus();
                    Producto.setText("");

                }
            });
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
    class SegundoPlanoMuestraTabla extends  AsyncTask<Void,Void,Void>

    {
        String Revision = "",Tarea;
        DataAccessObject dao;

        public SegundoPlanoMuestraTabla(String Tarea) {
            this.Tarea= Tarea;
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

                switch(Tarea)
                    {
                        case    "MuestraTabla":
                            dao = ca.c_ConsultaPalletArticulo( Resultados.getSelectedItem().toString(),Revision);
                            break;

                        case    "ConsultaProducto":

                            dao = ca.c_ConsultaCoincidenciaArticulo( Producto.getText().toString());
                            break;
                        default:
                            dao = new DataAccessObject();
                    }
            }catch (Exception e)
            {
                e.getMessage();
                dao = new DataAccessObject(e);
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

                    switch(Tarea)
                        {
                            case    "MuestraTabla":
                                ajustarTamañoColumnas();
                                tabla.setDataAdapter(st = new SimpleTableDataAdapter(consultas_BusquedaExistencia.this,dao.getcTablaUnica()));
                                tabla.setHeaderAdapter(sthd= new SimpleTableHeaderAdapter(consultas_BusquedaExistencia.this, dao.getcEncabezado()));
                                tabla.getDataAdapter().notifyDataSetChanged();
                                st.setTextColor(R.color.blancoLetraStd);
                                sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());

                                tabla.setColumnModel(columnModel);
                                break;

                            case    "ConsultaProducto":
                                Resultados.setAdapter(new CustomArrayAdapter(consultas_BusquedaExistencia.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("NumParte","NumParte","NumParte")));
                                Buscar.setEnabled(true);
                                break;
                        }
                }
                else
                {
                    new popUpGenerico(contexto,vista,dao.getcMensaje(),"Error",true,true);
                    requestFocusTextFields();
                    reiniciarVariables();
                }
            }catch (Exception e)
            {
                e.getMessage();
                new popUpGenerico(contexto,vista,dao.getcMensaje(),"Advertencia",true,true);
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}
