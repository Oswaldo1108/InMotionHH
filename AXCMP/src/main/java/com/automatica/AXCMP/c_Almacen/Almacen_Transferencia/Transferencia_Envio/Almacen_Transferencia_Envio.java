package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

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
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.c_Consultas.constructorConsultasPosicion;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Transferencia_Envio extends AppCompatActivity
{

    //region variables
    TableView tableView;
    EditText so;
    Button BotonSurtirPartida;
    Spinner spnr_Transfer;


    SoapAction sa = new SoapAction();

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    public ArrayList<constructorConsultasPosicion> arrayAdaptador;
    constructorConsultasPosicion constrPosicion;
    View vista;
    Context contexto = this;
    String[] HEADER = {"Producto","Lote","Cantidad Solicitada", "Cantidad Surtida", "Cantidad Pendiente"};
    String [][] DATA_TO_SHOW;
    String orden,pallet,producto,lote,contenido, ubicacion;
    TableColumnDpWidthModel columnModel;
    String[] TipoAjuste;
    String Transferencia;
    ArrayAdapter<String> spinnerArrayAdapter;
    int renglonSeleccionado;
    SimpleTableDataAdapter st ;

    int renglonAnterior=-1;

    Boolean primerSeleccion = false;
    Boolean seleccionado;


class datosTabla
{
    String Producto,Lote,CantidadSolicitada,CantidadSurtida,CantidadPendiente;

    public datosTabla(String producto, String lote, String cantidadSolicitada, String cantidadSurtida, String cantidadPendiente)
    {
        Producto = producto;
        Lote = lote;
        CantidadSolicitada = cantidadSolicitada;
        CantidadSurtida = cantidadSurtida;
        CantidadPendiente = cantidadPendiente;
    }

    public String getProducto() {
        return Producto;
    }

    public String getLote() {
        return Lote;
    }

    public String getCantidadSolicitada() {
        return CantidadSolicitada;
    }

    public String getCantidadSurtida() {
        return CantidadSurtida;
    }

    public String getCantidadPendiente() {
        return CantidadPendiente;
    }
}
ArrayList<datosTabla> arrayDatosTabla;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. almacen_activity_transferencia__envio);
        declararVariables();
        agregaListeners();
        ajustarTamañoColumnas();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Transferencia_Envio.this);

        //BotonSurtirPartida.setEnabled(false);

        SegundoPlano sp = new SegundoPlano("llenaDropdown");
        sp.execute();
    }
    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Transferencia_Envio));
        //toolbar.setSubtitle("  Envío");
        toolbar.setLogo(R.mipmap.logo_axc);//    toolbar.setLogo(R.drawable.axc_logo_toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        tableView = (TableView) findViewById(R.id.tableView_Envio);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));
        tableView.addDataClickListener(new ListenerClickTabla());
        BotonSurtirPartida = (Button) findViewById(R.id.btn_surtir);
      //  BotonSurtirPartida.setEnabled(false);
        spnr_Transfer = (Spinner) findViewById(R.id.spnr_Transferencia);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
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


        return super.onOptionsItemSelected(item);
    }

    public void ajustarTamañoColumnas()
    {
        columnModel = new TableColumnDpWidthModel(contexto,6,150);
        columnModel.setColumnWidth(0,100);
        columnModel.setColumnWidth(1,150);
        columnModel.setColumnWidth(2,200);

        columnModel.setColumnWidth(4,200);
        columnModel.setColumnWidth(5,200);
        tableView.getDataAdapter().notifyDataSetChanged();
        tableView.setColumnModel(columnModel);
    }

    private void agregaListeners()
    {

        BotonSurtirPartida.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                lanzaTransferenciaPor();
            }
        });
        spnr_Transfer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText( Almacen_Transferencia_Envio.this,String.valueOf(position), Toast.LENGTH_SHORT).show();

                SegundoPlano sp = new SegundoPlano("ConsultaTransfer");
                sp.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {

        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {

/*

            tableView.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
            tableView.getDataAdapter().notifyDataSetChanged();
*/

            if(renglonAnterior != rowIndex)
            {

                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tableView.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());


                tableView.getDataAdapter().notifyDataSetChanged();

                renglonSeleccionado = rowIndex;

              //  BotonSurtirPartida.setEnabled(true);


            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tableView.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tableView.getDataAdapter().notifyDataSetChanged();

                renglonAnterior=-1;


             //   BotonSurtirPartida.setEnabled(false);


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
                Color = R.color.blancoLetraStd;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            }
            return new ColorDrawable(getResources().getColor(Color));
        }
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea,decision,mensaje;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
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

        //    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();

            switch (tarea)
            {

                case "ConsultaTransfer":
                    String Interorg = spnr_Transfer.getSelectedItem().toString();
                    Transferencia = Interorg;
                    sa.SOAPConsultaPalletSalidaLiberado(Interorg,contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();
                    break;

                case "llenaDropdown" ://Se usa para llenar el Spinner
                    sa.SOAPTransfersLiberados(contexto);
                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();

                    break;


            }

            if(decision.equals("true"))
            {
                sacaDatos(tarea);

                if (arrayDatosTabla!= null)
                {
                    DATA_TO_SHOW = new String[arrayDatosTabla.size()][5];
                    int i = 0;
                    for (datosTabla a : arrayDatosTabla)
                    {

                        //DATA_TO_SHOW[i][0] = a.getCodigoPallet();
                        DATA_TO_SHOW[i][0] = a.getProducto();
                        DATA_TO_SHOW[i][1] = a.getLote();
                        DATA_TO_SHOW[i][2] = a.getCantidadSolicitada();
                        DATA_TO_SHOW[i][3] = a.getCantidadSurtida();
                        DATA_TO_SHOW[i][4] = a.getCantidadPendiente();


                        i++;
                        //   Log.i("SoapResponse", "sacaDatos: " + i);

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if (decision.equals("true"))
            {
                switch (tarea)
                {


                    case "ConsultaTransfer":

                        tableView.setDataAdapter(st =new SimpleTableDataAdapter(Almacen_Transferencia_Envio.this, DATA_TO_SHOW));
                        tableView.getDataAdapter().notifyDataSetChanged();
                        arrayDatosTabla.clear();
                        //BotonSurtirPartida.setEnabled(false);


                        break;
                    case "llenaDropdown":
                        spinnerArrayAdapter = new ArrayAdapter<String>
                                (Almacen_Transferencia_Envio.this, android.R.layout.simple_spinner_item,
                                        TipoAjuste); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spnr_Transfer.setAdapter(spinnerArrayAdapter);
                        break;

                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
             //   reiniciaCampos();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }


    public void sacaDatos(String Tarea) {
        SoapObject tabla = sa.parser();

        if (tabla == null) tabla = sa.getTablaSoap();

        if (Tarea.equals("llenaDropdown")) TipoAjuste = new String[tabla.getPropertyCount()];


        if (tabla != null)
        {
            arrayDatosTabla = new ArrayList<>();
            switch (Tarea) {
                case "llenaDropdown":
                    for (int i = 0; i < tabla.getPropertyCount(); i++) {
                        try {


                            SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                            Log.d("SoapResponse", tabla1.toString());
                            TipoAjuste[i] = tabla1.getPrimitivePropertyAsString("IdTraspaso");
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                    break;

                case "ConsultaTransfer":
                    for (int i = 0; i < tabla.getPropertyCount(); i++)
                    {
                        SoapObject tabla1 = (SoapObject) tabla.getProperty(i);

                            try {
                                String Producto, Lote, CantidadSolicitada, CantidadSurtida, CantidadPendiente;
                                Producto = tabla1.getPrimitivePropertyAsString("Producto");
                                Lote = tabla1.getPrimitivePropertyAsString("Lote");
                                CantidadSolicitada = tabla1.getPrimitivePropertyAsString("CantidadSolicitada");
                                CantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                                CantidadSurtida = tabla1.getPrimitivePropertyAsString("CantidadSurtida");
                                datosTabla dt = new datosTabla(Producto, Lote, CantidadSolicitada, CantidadSurtida, CantidadPendiente);
                                arrayDatosTabla.add(dt);
                                Log.d("SoapResponse", tabla1.toString());
                            } catch (Exception e)
                            {

                                e.printStackTrace();
                            }

                    }
                    break;
            }

        }
    }

    private void lanzaTransferenciaPor()
    {
        Bundle b = new Bundle();
        b.putString("Transferencia",Transferencia);
        Intent intent = new Intent(Almacen_Transferencia_Envio.this, Almacen_Transferencia_Menu_Decision.class);
        intent.putExtras(b);
        startActivity(intent);
    }

}
