package com.automatica.AXCMP.c_Surtido.Surtido_Pedidos;

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

public class Surtido_Seleccion_Partida extends AppCompatActivity
{
    //region variables
    SoapAction sa = new SoapAction();

    EditText edtx_OrdenCompra;
    SortableTableView tabla;
    Button btnConfirmar;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String ModificaCant = "",IdRecepcion = "";

    TableColumnDpWidthModel columnModel;
    SimpleTableDataAdapter st ;
    SimpleTableHeaderAdapter sthd;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Partida","Producto","UM","Cantidad Total","Cantidad Pendiente","Cantidad Surtidad","Área","Status"};
    String[][] DATA_TO_SHOW;
    String OrdenCompra;
    ArrayList<posiciones> arrayListPosiciones;

    SegundoPlano sp;
    Bundle b = new Bundle();

    Handler h = new Handler();
    int renglonSeleccionado;
    int renglonAnterior=-1;
    class posiciones
    {
        String PartidaERP, NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida, Linea,EmpaquesPallet,Status,Estatus,IdArea;


        public posiciones(String partidaERP, String numParte, String UM, String cantidadTotal, String cantidadPendiente, String cantidadRecibida, String Linea,String IdArea,String Status)
        {
            PartidaERP = partidaERP;
            NumParte = numParte;
            this.UM = UM;
            CantidadTotal = cantidadTotal;
            CantidadPendiente = cantidadPendiente;
            CantidadRecibida = cantidadRecibida;
            this.Linea = Linea;
            this.IdArea = IdArea;
           this.Status = Status;
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

        public String getIdArea() {
            return IdArea;
        }

        public String getStatus()
        {
            return Status;
        }
    }
    boolean Seleccionado,Recargar=true;
    String Tipo;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surtido_seleccion_partida);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Seleccion_Partida.this);
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
                new esconderTeclado(Surtido_Seleccion_Partida.this);

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
        if(Seleccionado)
        {


            Intent intent = new Intent(Surtido_Seleccion_Partida.this, Surtido_Menu_Registro.class);
            intent.putExtras(b);
            startActivity(intent);
        }else
            {
                new popUpGenerico(contexto,vista ,getString(R.string.error_seleccionar_registro) , "false", true,true );
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
                    b.putString("AreaDesc", clickedData[6]);
                    b.putString("Area", clickedData[7]);

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

        Log.i("SoapResponse", "getRowBackground: "+ rowData[1]);
        if(rowIndex == renglonSeleccionado)
        {
            Color = R.color.RengSelStd;
            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

        }
        else
        {
//            Color = R.color.Transparente;
//            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            if(rowData[8].equals(String.valueOf(3)))
                {
                    Color = R.color.VerdeRenglon;
                }
            else //if(rowData[6].equals(String.valueOf(2)))
                {
                    Color = R.color.AmarilloRenglon;
                }
//            else
//                {
//
//                    Color = R.color.Transparente;
//                }
            st.setTextColor(getResources().getColor(R.color.negroLetrastd));

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
//         st.setTextColor(getResources().getColor(R.color.negroLetrastd));
            if(rowData[8].equals(String.valueOf(3)))
                {
                    Color = R.color.VerdeRenglon;
                }
            else //if(rowData[6].equals(String.valueOf(2)))
                {
                    Color = R.color.AmarilloRenglon;
                }
//            else
//                {
//
//                    Color = R.color.Transparente;
//                }
            st.setTextColor(getResources().getColor(R.color.negroLetrastd));
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

        tabla.sort(columnIndex, false);
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


//            tabla.getDataAdapter().notifyDataSetChanged();

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
                        sa.SOAPConsultaPedidoSurtido( edtx_OrdenCompra.getText().toString(),contexto);
                        break;


                }

                decision = sa.getDecision();
                mensaje = sa.getMensaje();
                if(decision.equals("true")&&Tarea.equals("Tabla"))
                {
                    sacaDatos();
                    if(decision.equals("true")&&arrayListPosiciones!=null)
                    {
                        int i = 0;
                        DATA_TO_SHOW = new String[arrayListPosiciones.size()][9];
                        for(posiciones a: arrayListPosiciones)
                        {
                            DATA_TO_SHOW[i][0] = a.getPartidaERP();
                            DATA_TO_SHOW[i][1] = a.getNumParte();
                            DATA_TO_SHOW[i][2] = a.getUM();
                            DATA_TO_SHOW[i][3] = a.getCantidadTotal();
                            DATA_TO_SHOW[i][4] = a.getCantidadPendiente();
                            DATA_TO_SHOW[i][5] = a.getCantidadRecibida();
                            DATA_TO_SHOW[i][6] = a.getLinea();
                            DATA_TO_SHOW[i][7] = a.getIdArea();
                            DATA_TO_SHOW[i][8] = a.getStatus();


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
                if (decision.equals("true")) {
                    switch (Tarea) {
                        case "Tabla":

                            columnModel = new TableColumnDpWidthModel(contexto, 7, 150);
                            columnModel.setColumnWidth(0,100);
                            columnModel.setColumnWidth(2,100);
                            columnModel.setColumnWidth(7,200);

                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(Surtido_Seleccion_Partida.this, DATA_TO_SHOW));
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                            tabla.setColumnComparator(8, new ComparadorStatus());
                            tabla.sort(8,true );
                            tabla.setColumnModel(columnModel);
                            tabla.getDataAdapter().notifyDataSetChanged();
                            renglonSeleccionado = -1;
                            renglonAnterior = -1;
                            Seleccionado = false;
                            arrayListPosiciones.clear();
                            break;

                    }
                }

                if (decision.equals("false")) {
                    new popUpGenerico(contexto, vista, mensaje, "Advertencia", true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), "Advertencia", true, true);
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
    public void sacaDatos()
    {
        SoapObject tabla = sa.parser();
        String PartidaERP,NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,Muestreo,Estatus;
        arrayListPosiciones = new ArrayList<>();

        if(tabla!=null)
        {
            for (int i = 0; i < tabla.getPropertyCount(); i++) {
                try {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    Log.d("SoapResponse", tabla1.toString());

                    arrayListPosiciones.add(
                            new posiciones(tabla1.getPrimitivePropertyAsString("Partida"),
                                    tabla1.getPrimitivePropertyAsString("NumParte"),
                                    tabla1.getPrimitivePropertyAsString("UM"),
                                    tabla1.getPrimitivePropertyAsString("CantidadTotal"),
                                    tabla1.getPrimitivePropertyAsString("CantidadPendiente"),
                                    tabla1.getPrimitivePropertyAsString("CantidadSurtida"),
                                    tabla1.getPrimitivePropertyAsString("Linea"),
                                    tabla1.getPrimitivePropertyAsString("IdArea"),
                                    tabla1.getPrimitivePropertyAsString("Status")));


                } catch (Exception e) {

                    e.printStackTrace();

                    //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
                }
            }
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
