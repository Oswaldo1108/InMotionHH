package com.automatica.AXCMP.c_Almacen.Almacen;

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
import android.text.InputType;
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
import com.automatica.AXCMP.c_Recepcion.Recepcion_Menu_Registro;

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

public class Almacen_Reg_Transferencia_Seleccion_Partida extends AppCompatActivity
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
    String[] HEADER = {"Partida","Producto","UM","Cantidad Total","Cantidad Pendiente","Cantidad Recibida","Cantidad de Empaques","EmpaquesPallet", "Muestreo","Estatus"};
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
        String PartidaERP, NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,Muestreo,Estatus;


        public posiciones(String partidaERP, String numParte, String UM, String cantidadTotal, String cantidadPendiente, String cantidadRecibida, String cantidadEmpaques,String EmpaquesPallet, String muestreo, String estatus) {
            PartidaERP = partidaERP;
            NumParte = numParte;
            this.UM = UM;
            CantidadTotal = cantidadTotal;
            CantidadPendiente = cantidadPendiente;
            CantidadRecibida = cantidadRecibida;
            CantidadEmpaques = cantidadEmpaques;
            Muestreo = muestreo;
            Estatus = estatus;
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

        public String getCantidadEmpaques() {
            return CantidadEmpaques;
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
    boolean primeraConsulta;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_transfer_registrooc);
            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Reg_Transferencia_Seleccion_Partida.this);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            SacaDatosIntent();
            declaraVariables();
            AgregaListeners();
            ajustarTamañoColumnas();

       /* SegundoPlano sp = new SegundoPlano("Tabla");
        sp.execute();*/
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista ,e.getMessage() ,"false" ,true,true);
        }
    }
    @Override
    protected void onResume()
    {
        if (primeraConsulta)
        {
            sp = new SegundoPlano("Tabla");
            sp.execute();
        }
        else
        {
            edtx_OrdenCompra.setFocusable(true);
            edtx_OrdenCompra.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }
        super.onResume();
    }


    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.recepcion_recepcion_por_empaque_traspasos);


        edtx_OrdenCompra = (EditText) findViewById(R.id.edtx_Cantidad);
        edtx_OrdenCompra.setText(OrdenCompra);
        tabla = (SortableTableView) findViewById(R.id.tableView_OC);
        tabla.setHeaderAdapter( sthd = new SimpleTableHeaderAdapter(contexto,HEADER));
        btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


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

            }
            new esconderTeclado(Almacen_Reg_Transferencia_Seleccion_Partida.this);
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
            b.putString("Orden", edtx_OrdenCompra.getText().toString());
            b.putString("FechaCaducidad", "FechaCaducidad");

            Intent intent = new Intent(Almacen_Reg_Transferencia_Seleccion_Partida.this, Recepcion_Menu_Registro.class);
            intent.putExtras(b);
            startActivity(intent);
        }else
            {
                new popUpGenerico(contexto,vista ,getString(R.string.error_seleccionar_registro) , "false", true,true );
            }

    }
    private void SacaDatosIntent()
    {
try
{
 /*   b  = getIntent().getExtras();
    OrdenCompra = b.getString("Orden");
    Log.e("Error", OrdenCompra);
    Tipo = b.getString("Tipo");*/
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
            if(renglonAnterior != rowIndex)
            {
                renglonAnterior = rowIndex;

                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();

                b.putString("ModificaCant",ModificaCant);
                b.putString("PartidaERP",clickedData[0]);
                b.putString("IdRecepcion",IdRecepcion);
                b.putString("NumParte",clickedData[1]);
                b.putString("UM",clickedData[2]);
                b.putString("CantidadTotal",clickedData[3]);
                // b.putString("CantidadPendiente",clickedData[4]);
                b.putString("CantidadRecibida",clickedData[5]);
                b.putString("CantidadEmpaques",clickedData[6]);
                b.putString("EmpaquesPallet",clickedData[7]);
                Seleccionado = true;
                btnConfirmar.setEnabled(true);

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                renglonAnterior=-1;
                Seleccionado = false;
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
        edtx_OrdenCompra.setText("");

        tabla.getDataAdapter().clear();
        b.clear();
        edtx_OrdenCompra.requestFocus();
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
                String Partida = edtx_OrdenCompra.getText().toString();
                switch (Tarea)
                {
                    case"Tabla":
                        sa.SOAPListarPartidasOCLiberadas(Partida,contexto);
                        break;
                    case"CierreOC":
                        sa.SOAPCerrarRecepcion(Partida,contexto);
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
                        DATA_TO_SHOW = new String[arrayListPosiciones.size()][10];
                        for(posiciones a: arrayListPosiciones)
                        {
                            DATA_TO_SHOW[i][0] = a.getPartidaERP();
                            DATA_TO_SHOW[i][1] = a.getNumParte();
                            DATA_TO_SHOW[i][2] = a.getUM();
                            DATA_TO_SHOW[i][3] = a.getCantidadTotal();
                            DATA_TO_SHOW[i][4] = a.getCantidadPendiente();
                            DATA_TO_SHOW[i][5] = a.getCantidadRecibida();
                            DATA_TO_SHOW[i][6] = a.getCantidadEmpaques();
                            DATA_TO_SHOW[i][7] = a.getEmpaquesPallet();
                            DATA_TO_SHOW[i][8] = a.getMuestreo();
                            DATA_TO_SHOW[i][9] = a.getEstatus();

                            i++;
                        }
                    }
                }
            }catch (Exception e)
            {
                Log.e("RegistroOC", "doInBackground: " + e.getMessage() );
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


                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Reg_Transferencia_Seleccion_Partida.this, DATA_TO_SHOW));
                            tabla.setDataRowBackgroundProvider(new cambiaColorTablaClear());
                            tabla.getDataAdapter().notifyDataSetChanged();
                            renglonSeleccionado = -1;
                            Seleccionado = false;
                            arrayListPosiciones.clear();
                            primeraConsulta = true;
                            break;
                        case "CierreOC":
                            new popUpGenerico(contexto, vista, "Orden de compra cerrada con éxito.", "Registrado", true, true);
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
        posiciones p;

        for(int i = 0; i<tabla.getPropertyCount();i++)
        {
            try {

                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse",tabla1.toString());
                this.IdRecepcion = tabla1.getPrimitivePropertyAsString("IdRecepcion");
                PartidaERP=  tabla1.getPrimitivePropertyAsString("PartidaERP");
                NumParte = tabla1.getPrimitivePropertyAsString("NumParte");
                UM= tabla1.getPrimitivePropertyAsString("UM");
                CantidadTotal = tabla1.getPrimitivePropertyAsString("CantidadPendienteTotal");
                CantidadPendiente= tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                CantidadRecibida= tabla1.getPrimitivePropertyAsString("CantidadRecibida");
                CantidadEmpaques = tabla1.getPrimitivePropertyAsString("CantidadEmpaque");
                EmpaquesPallet = tabla1.getPrimitivePropertyAsString("EmpaquesPallet");
                Muestreo= tabla1.getPrimitivePropertyAsString("Muestreo");
                Estatus = tabla1.getPrimitivePropertyAsString("Estatus");
                ModificaCant= tabla1.getPrimitivePropertyAsString("ModificaCant");

                p= new posiciones(PartidaERP,NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,Muestreo,Estatus);

                arrayListPosiciones.add(p);




            }catch (Exception e)
            {

                e.printStackTrace();

                //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
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
