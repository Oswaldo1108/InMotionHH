package com.automatica.AXCMP.c_Surtido.Surtido_Bascula;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Comparator;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Surtido_Seleccion_Registro_Partida extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_OrdenCompra;
    SortableTableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    Spinner spnr_Areas;
    TableColumnDpWidthModel columnModel;
    SimpleTableDataAdapter st ;
    SimpleTableHeaderAdapter sthd;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Partida","Producto","UM","Cantidad Total","Cantidad Pendiente","Cantidad Surtida","Status"};
    String[][] DATA_TO_SHOW;
    String OrdenCompra;
    ArrayList<posiciones> arrayListPosiciones;
    ArrayList<String> ArrayAreas;
    ArrayList<String> ArrayIdAreas;
    SegundoPlano sp;
    Bundle b = new Bundle();

    Handler h = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;
    class posiciones
    {
        String PartidaERP, NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida, Linea,EmpaquesPallet,Muestreo,Estatus;


        public posiciones(String partidaERP, String numParte, String UM, String cantidadTotal, String cantidadPendiente, String cantidadRecibida, String Linea) {
            PartidaERP = partidaERP;
            NumParte = numParte;
            this.UM = UM;
            CantidadTotal = cantidadTotal;
            CantidadPendiente = cantidadPendiente;
            CantidadRecibida = cantidadRecibida;
            this.Linea = Linea;

            this.EmpaquesPallet =EmpaquesPallet;
        }

        public String getEmpaquesPallet() {
            return EmpaquesPallet;
        }

        public String getUM() {
            return UM;
        }

        public String getCantidadTotal() {
            return CantidadTotal;
        }

        public String getCantidadPendiente() {
            return CantidadPendiente;
        }

        public String getCantidadRecibida() {
            return CantidadRecibida;
        }

        public String getLinea() {
            return Linea;
        }

        public String getMuestreo() {
            return Muestreo;
        }

        public String getEstatus() {
            return Estatus;
        }

        public String getPartidaERP()
        {
            return PartidaERP;
        }

        public String getNumParte()
        {
            return NumParte;
        }
    }
    boolean Seleccionado,Recargar;
    String Tipo;
    int registroAnteriorSpinner=0;
    Integer ItemSeleccionado = null;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_seleccion_partida_docificacion);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Seleccion_Registro_Partida.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declaraVariables();
        AgregaListeners();
        ajustarTamañoColumnas();
    }
    @Override
    protected void onResume()
    {
        if(!edtx_OrdenCompra.getText().toString().equals(""))
        {
            sp = new SegundoPlano("Tabla");
            sp.execute();
        }


        super.onResume();
    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.seleccionar_orden_compra));

        edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Pedido);
        tabla = (SortableTableView) findViewById(R.id.tableView_OC);
        tabla.setHeaderAdapter( sthd = new SimpleTableHeaderAdapter(contexto,HEADER));
        btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        spnr_Areas = (Spinner) findViewById(R.id.spnr_OrdenesCompra);
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
                    SegundoPlano sp = new SegundoPlano("Tabla");
                    sp.execute();

                }else
                {
                    new popUpGenerico(contexto,vista ,getString(R.string.error_ingrese_pedido) ,"false" , true, true);
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
                new esconderTeclado(Surtido_Seleccion_Registro_Partida.this);

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
        try {

/*
        Intent intent = new Intent(Surtido_Seleccion_Registro_Partida.this, Surtido_Menu_Docificacion.class);
        //intent.putExtras(b);
        startActivity(intent);
*/

            if (edtx_OrdenCompra.getText().toString().equals(""))
            {
                new popUpGenerico(contexto, vista, getString(R.string.error_ingrese_orden_produccion), "false", true, false);
                reiniciarDatos();
                return;
            }
            if (Seleccionado) {
                Intent intent = new Intent(Surtido_Seleccion_Registro_Partida.this, Surtido_Menu_Docificacion.class);

          // b.putString("Linea", clickedData[7]);
                    b.putString("Area",ArrayIdAreas.get(spnr_Areas.getSelectedItemPosition()));      //         AQUI SE SELECCIONABA EL ITEM, SE CAMBIO AL BOTON
                    b.putString("AreaDesc",spnr_Areas.getSelectedItem().toString());
                ItemSeleccionado = spnr_Areas.getSelectedItemPosition();
                intent.putExtras(b);
                startActivity(intent);
            } else {
                new popUpGenerico(contexto, vista, getString(R.string.error_seleccionar_registro), "false", true, true);
            }


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
    private void ajustarTamañoColumnas()
    {
        columnModel = new TableColumnDpWidthModel(contexto,10,150);
        columnModel.setColumnWidth(0,185);
        columnModel.setColumnWidth(1,150);

        tabla.getDataAdapter().notifyDataSetChanged();
        tabla.setColumnModel(columnModel);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

                        .setTitle("¿Cerrar Orden de Compra?").setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SegundoPlano sp = new SegundoPlano("CierreOC");
                                sp.execute();
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
            try {
                if (renglonAnterior != rowIndex)
                {
                    renglonAnterior = rowIndex;

                    renglonSeleccionado = rowIndex;
                    tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                    tabla.getDataAdapter().notifyDataSetChanged();

                    b.putString("Pedido", edtx_OrdenCompra.getText().toString());
                    b.putString("Partida", clickedData[0]);
                    b.putString("NumParte", clickedData[1]);
                    b.putString("UM", clickedData[2]);
                    b.putString("CantidadTotal", clickedData[3]);
                    b.putString("CantidadPendiente", clickedData[4]);
                    b.putString("CantidadSurtida", clickedData[5]);
                    //b.putString("Linea", clickedData[7]);
//                    b.putString("Area",ArrayIdAreas.get(spnr_Areas.getSelectedItemPosition()));               AQUI SE SELECCIONABA EL ITEM, SE CAMBIO AL BOTON
//                    b.putString("AreaDesc",spnr_Areas.getSelectedItem().toString());
                 //   ItemSeleccionado = spnr_Areas.getSelectedItemPosition();
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
//            Color = R.color.Transparente;
//            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            if(rowData[6].equals(String.valueOf(5)))
                {
                    Color = R.color.VerdeRenglon;
                }
            else if(rowData[6].equals(String.valueOf(2)))
                {
                    Color = R.color.AmarilloRenglon;
                }
            else
                {

                    Color = R.color.Transparente;
                }
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
//         Color = R.color.Transparente;
//         st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            if(rowData[6].equals(String.valueOf(5)))
                {
                    Color = R.color.VerdeRenglon;
                }
            else if(rowData[6].equals(String.valueOf(2)))
                {
                    Color = R.color.AmarilloRenglon;
                }
            else
                {

                    Color = R.color.Transparente;
                }
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
       // tabla.sort(columnIndex, false);

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

    private class ComparadorStatus implements Comparator<String[]>
    {
        @Override
        public int compare(String[] statusa,String statusb[])
        {
            return  statusa[6].compareTo(statusb[6]);
        }
    }

    private void reiniciarDatos()
    {
        edtx_OrdenCompra.setText("");

        tabla.getDataAdapter().clear();

        //edtx_OrdenCompra.requestFocus();
    }
    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String decision,mensaje,Tarea;

        SpinnerAdapter spinnerAdapter;
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
                        sa.SOAPListarPartidasOrdenesSurtido( edtx_OrdenCompra.getText().toString(),contexto);
                        break;
                    case"ConsultaAreas":
                        ArrayAreas = new ArrayList<>();
                        ArrayIdAreas = new ArrayList<>();
                        sa.SOAPConsultaAreasPesaje(contexto);
                        break;


                }

                decision = sa.getDecision();
                mensaje = sa.getMensaje();
                if(decision.equals("true"))
                {
                    sacaDatos(Tarea);
                    if(decision.equals("true")&&arrayListPosiciones!=null)
                    {
                        int i = 0;
                        DATA_TO_SHOW = new String[arrayListPosiciones.size()][7];
                        for(posiciones a: arrayListPosiciones)
                        {
                            DATA_TO_SHOW[i][0] = a.getPartidaERP();
                            DATA_TO_SHOW[i][1] = a.getNumParte();
                            DATA_TO_SHOW[i][2] = a.getUM();
                            DATA_TO_SHOW[i][3] = a.getCantidadTotal();
                            DATA_TO_SHOW[i][4] = a.getCantidadPendiente();
                            DATA_TO_SHOW[i][5] = a.getCantidadRecibida();
                            DATA_TO_SHOW[i][6] = a.getLinea();


                            i++;
                        }
                    }
                }
            }catch (Exception e)
            {
             //   Log.e("RegistroOC", "doInBackground: " + e.getMessage() );
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (decision.equals("true"))
                {
                    switch (Tarea)
                        {
                        case "Tabla":
                            columnModel = new TableColumnDpWidthModel(contexto, 7, 150);
                            columnModel.setColumnWidth(0,100);
                            columnModel.setColumnWidth(6,0);
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_Seleccion_Registro_Partida.this, DATA_TO_SHOW));
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                            tabla.setColumnModel(columnModel);
                            tabla.getDataAdapter().notifyDataSetChanged();
                            tabla.setColumnComparator(6, new ComparadorStatus());
                            tabla.sort(6,true );
                            renglonSeleccionado = -1;
                            renglonAnterior = -1;
                            Seleccionado = false;
                            arrayListPosiciones.clear();
                            SegundoPlano sp = new SegundoPlano("ConsultaAreas");
                            sp.execute();
                            break;
                        case "ConsultaAreas":
                            spinnerAdapter= new ArrayAdapter<String>(
                                   Surtido_Seleccion_Registro_Partida.this,
                                    android.R.layout.simple_spinner_item,
                                    ArrayAreas);
                            ((ArrayAdapter) spinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnr_Areas.setAdapter(spinnerAdapter);
                            if(ItemSeleccionado != null)
                            {
                                spnr_Areas.setSelection(ItemSeleccionado);
                            }

                            break;
                    }
                }

                if (decision.equals("false"))
                {
                    new popUpGenerico(contexto, vista, mensaje, decision, true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), decision, true, true);
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
//    public void sacaDatos()
//    {
//        SoapObject tabla = sa.parser();
//        String PartidaERP,NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,Muestreo,Estatus;
//        arrayListPosiciones = new ArrayList<>();
//
//        if(tabla!=null)
//        {
//            for (int i = 0; i < tabla.getPropertyCount(); i++) {
//                try {
//
//                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
//                    Log.d("SoapResponse", tabla1.toString());
//
//                    arrayListPosiciones.add(
//                            new posiciones(tabla1.getPrimitivePropertyAsString("Partida"),
//                                    tabla1.getPrimitivePropertyAsString("NumParte"),
//                                    tabla1.getPrimitivePropertyAsString("UM"),
//                                    tabla1.getPrimitivePropertyAsString("CantidadTotal"),
//                                    tabla1.getPrimitivePropertyAsString("CantidadPend"),
//                                    tabla1.getPrimitivePropertyAsString("CantidadSurtida"),
//                                    tabla1.getPrimitivePropertyAsString("Linea")));
//
//
//                } catch (Exception e) {
//
//                    e.printStackTrace();
//
//                    //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
//                }
//            }
//        }
//    }

    private void sacaDatos(String tarea)
    {
        try {
            SoapObject tabla = sa.parser();

            arrayListPosiciones = new ArrayList<>();

            if(tabla!=null)
                for (int i = 0; i < tabla.getPropertyCount(); i++) {
                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    switch (tarea)
                    {
                        case "Tabla":

                            arrayListPosiciones.add(
                                    new posiciones(tabla1.getPrimitivePropertyAsString("Partida"),
                                            tabla1.getPrimitivePropertyAsString("NumParte"),
                                            tabla1.getPrimitivePropertyAsString("UM"),
                                            tabla1.getPrimitivePropertyAsString("CantidadTotal"),
                                            tabla1.getPrimitivePropertyAsString("CantidadPend"),
                                            tabla1.getPrimitivePropertyAsString("CantidadSurtida"),
                                            tabla1.getPrimitivePropertyAsString("Status")));


                            break;
                        case "ConsultaAreas":
                            ArrayAreas.add( tabla1.getPrimitivePropertyAsString("Descripcion"));
                            ArrayIdAreas.add(tabla1.getPrimitivePropertyAsString("IdArea"));
                            break;



                    }


                }
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d("SoapResponse", "sacaDatos: ");
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
