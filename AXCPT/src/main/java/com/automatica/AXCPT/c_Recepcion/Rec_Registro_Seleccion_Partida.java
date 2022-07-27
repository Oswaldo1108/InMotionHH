package com.automatica.AXCPT.c_Recepcion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;


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

public class Rec_Registro_Seleccion_Partida extends AppCompatActivity
{
    //region variables
    EditText edtx_OrdenCompra;
    SortableTableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    popUpGenerico pop = new popUpGenerico(Rec_Registro_Seleccion_Partida.this);
    AlphaAnimation outAnimation;
    String ModificaCant = "",IdRecepcion = "";
    TableColumnDpWidthModel columnModel;
    SimpleTableDataAdapter st ;
    SimpleTableHeaderAdapter sthd;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Partida","Producto","Desc","Cantidad Total","Cantidad Pendiente","Cantidad Recibida"};
    String OrdenCompra;


    SegundoPlano sp;
    Bundle b = new Bundle();

    Handler h = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;
    boolean Seleccionado,Recargar;
    String Tipo;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //ca= new cAccesoADatos_Recepcion(getApplicationContext());
        setContentView(R.layout.recepcion_activity_registrooc);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SacaDatosIntent();
        declaraVariables();
        AgregaListeners();
        String Doc= null;
        try {
            Doc = getIntent().getStringExtra("Documento");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (Doc!=null){
            edtx_OrdenCompra.setText(Doc);
        }
        ajustarTamañoColumnas();
        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);

    }
    @Override
    protected void onResume()
    {
//         new SegundoPlano("Tabla").execute();
        if(!edtx_OrdenCompra.getText().toString().equals(""))
            {
                new SegundoPlano("Tabla").execute();
            }
        super.onResume();
    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.seleccionar_orden_compra));
        edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Cantidad);
        edtx_OrdenCompra.setText(OrdenCompra);
        tabla = (SortableTableView) findViewById(R.id.tableView_OC);
        tabla.setHeaderAdapter( sthd = new SimpleTableHeaderAdapter(contexto,HEADER));
        btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_OrdenCompra.setText(OrdenCompra);
    }
    private void AgregaListeners()
    {
        edtx_OrdenCompra.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_OrdenCompra.getText().toString().equals(""))
                    {
                       new SegundoPlano("Tabla").execute();

                    }else
                    {
                        new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_orden_compra) ,"false" , true, true);
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_OrdenCompra.requestFocus();
                                edtx_OrdenCompra.setText("");
                            }
                        });
                    }
                    new esconderTeclado(Rec_Registro_Seleccion_Partida.this);
                }

                return false;
            }
        });
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



        if(edtx_OrdenCompra.getText().toString().equals(""))
        {
            new popUpGenerico(contexto,vista,getString(R.string.error_seleccionar_registro),"Advertencia",true,false);
            reiniciarDatos();
            return;
        }
        if(!Seleccionado)
        {
            new popUpGenerico(contexto,vista ,getString(R.string.error_seleccionar_registro) , false, true,true );
            return;
        }

        b.putString("Orden", edtx_OrdenCompra.getText().toString());
        b.putString("FechaCaducidad", "-");

        Intent intent = new Intent(Rec_Registro_Seleccion_Partida.this, Recepcion_Menu_Registro.class);
        intent.putExtras(b);
        startActivity(intent);
    }
    private void SacaDatosIntent()
    {
        try
        {
            b  = getIntent().getExtras();
            OrdenCompra = b.getString("Orden");
            Log.e("Error", OrdenCompra);
            Tipo = b.getString("Tipo");
        }catch (Exception e)
        {
            Log.e("Error", "SacaDatosIntent: Hubo un error sacando datos de el Bundle -" + e.getStackTrace() );
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.cerrar_oc_toolbar, menu);
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
        columnModel = new TableColumnDpWidthModel(contexto,6,180);
        columnModel.setColumnWidth(0,110);
        columnModel.setColumnWidth(1,200);
        columnModel.setColumnWidth(2,280);

        tabla.getDataAdapter().notifyDataSetChanged();
        tabla.setColumnModel(columnModel);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (Recargar == true)
        {


            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.CerrarOC && edtx_OrdenCompra.getText().toString() != "")//////////////////////////////////////
            {
                if (!edtx_OrdenCompra.getText().toString().equals("")) {

                    new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)

                            .setTitle("¿Cerrar Orden de Compra en el ERP?").setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   new SegundoPlano("CierreOC").execute();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else if (edtx_OrdenCompra.getText().toString().equals("")) {
                    new popUpGenerico(contexto, vista, "No hay una orden de compra seleccionada", "Advertencia", true, true);
                }
            }

            if ((id == R.id.borrar_datos)) {
                reiniciarDatos();
            }
            if ((id == R.id.recargar) && edtx_OrdenCompra.getText().toString() != null) {
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
            try
                {
                    if (renglonAnterior != rowIndex)
                        {
                            renglonAnterior = rowIndex;
                            renglonSeleccionado = rowIndex;
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                            tabla.getDataAdapter().notifyDataSetChanged();

//                            b.putString("ModificaCant", ModificaCant);
//                            b.putString("CantidadPendiente",clickedData[4]);

                            b.putString("IdRecepcion", IdRecepcion);
                            b.putString("PartidaERP", clickedData[0]);
                            b.putString("NumParte", clickedData[2]);

                            b.putString("CantidadTotal", clickedData[3]);
                            b.putString("CantidadRecibida", clickedData[5]);
                            b.putString("UM", clickedData[7]);
                            b.putString("CantidadEmpaques", clickedData[8]);
                            b.putString("EmpaquesPallet", clickedData[9]);
                            Seleccionado = true;
                            btnConfirmar.setEnabled(true);

                        } else if (renglonAnterior == rowIndex)
                        {
                            renglonSeleccionado = -1;
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                            tabla.getDataAdapter().notifyDataSetChanged();
                            renglonAnterior = -1;
                            Seleccionado = false;
                        }
                }catch(Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,null, e.getMessage(), false, true, false);

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
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {
            String notifyText = HEADER[columnIndex];
            Toast toast = new Toast(contexto);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0,80);
            toast.makeText(contexto, notifyText, Toast.LENGTH_SHORT).show();

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
    private void reiniciarDatos()
    {
        //edtx_OrdenCompra.setText("");

        tabla.getDataAdapter().clear();

        //edtx_OrdenCompra.requestFocus();
    }
    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(contexto);

        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {
            Recargar = false;
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
                        //sa.SOAPListarPartidasOCLiberadas(Partida,contexto);
                        dao = ca.c_ListarPartidasOCLiberadas(edtx_OrdenCompra.getText().toString());
                        break;
                    case"CierreOC":
                        //sa.SOAPCerrarRecepcion(Partida,contexto);
                        dao = ca.c_CerrarRecepcion(edtx_OrdenCompra.getText().toString());
                        break;
                }
            }catch (Exception e)
            {
                Log.e("RegistroOC", "doInBackground: " + e.getMessage() );
                dao = new DataAccessObject(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (dao.iscEstado())
                {
                    switch (Tarea)
                        {
                        case "Tabla":

                            if (dao.getcTablaUnica()!=null)
                                {
                                    tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(Rec_Registro_Seleccion_Partida.this, dao.getcEncabezado()));
                                    tabla.setDataAdapter(st = new SimpleTableDataAdapter(Rec_Registro_Seleccion_Partida.this, dao.getcTablaUnica()));
                                    tabla.getDataAdapter().notifyDataSetChanged();
                                    tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                    sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));


                                }
                            Seleccionado = false;

                            renglonAnterior = -1;



                            break;
                        case "CierreOC":
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            break;
                    }
                }else{
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), dao.iscEstado(), true, true);
                e.printStackTrace();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            Recargar = true;
        }
        @Override
        protected void onCancelled()
        {
            Log.e("SP", "onCancelled: hilo terminado" );
            super.onCancelled();
        }

    }
    @Override
    public void onBackPressed()
    {
        if(sp!=null)
        {
            sp.cancel(true);
        }
        super.onBackPressed();
    }
}
