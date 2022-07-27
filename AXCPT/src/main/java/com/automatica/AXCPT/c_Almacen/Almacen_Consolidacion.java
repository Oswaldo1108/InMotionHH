package com.automatica.AXCPT.c_Almacen;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Consolidacion extends AppCompatActivity
{


    //region variables
    SoapAction sa = new SoapAction();
    Toolbar toolbar;
    EditText edtx_CodigoPallet, edtx_Empaque;
    TableView tabla;
    Button btn_Terminar;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TextView PalletsRegistrados;

    TableColumnDpWidthModel columnModel;
    View vista;
    Context contexto = this;


    String PalletSeleccionadoTabla;
    String Actividad;
    String[] HEADER = {"Tipo","Código de Pallet","Producto","Lote","Cantidad Actual","Empaques"};
    String[][] DATA_TO_SHOW;


    int renglonSeleccionado;
    ArrayList<datosTabla> arrayDatosTabla;

    class datosTabla
    {
        String Tipo,IdStatus,CodigoPallet,Producto,Lote,CantidadActual,Empaques;

        public datosTabla(String tipo, String idStatus, String codigoPallet, String producto, String lote, String cantidadActual, String empaques)
        {
            Tipo = tipo;
            IdStatus = idStatus;
            CodigoPallet = codigoPallet;
            Producto = producto;
            Lote = lote;
            CantidadActual = cantidadActual;
            Empaques = empaques;
        }

        public String getTipo()
        {
            return Tipo;
        }

        public String getIdStatus()
        {
            return IdStatus;
        }

        public String getCodigoPallet()
        {
            return CodigoPallet;
        }

        public String getProducto()
        {
            return Producto;
        }

        public String getLote()
        {
            return Lote;
        }

        public String getCantidadActual() {
            return CantidadActual;
        }

        public String getEmpaques() {
            return Empaques;
        }
    }
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almacen_activity_consolidacion);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd,Almacen_Consolidacion.this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        declararVariables();
        agregaListeners();





    }

    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Consolidacion));

        //  toolbar.setSubtitle(" Consolidación");
//        toolbar.setLogo(R.mipmap.logo_axc);//toolbar.setLogo(R.drawable.axc_logo_toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        edtx_CodigoPallet = (EditText) findViewById(R.id.edtx_Empaque);
        edtx_Empaque =(EditText) findViewById(R.id.edtx_Empaque);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_Empaque.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        tabla = (TableView) findViewById(R.id.tableView_Pallet);
        tabla.setHeaderAdapter(new SimpleTableHeaderAdapter(contexto,HEADER));

        SimpleTableHeaderAdapter sthd = new SimpleTableHeaderAdapter(contexto,HEADER);
        sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        tabla.setHeaderAdapter(sthd);

        btn_Terminar = findViewById(R.id.btn_Terminar);
        btn_Terminar.setEnabled(false);

    }
    private void agregaListeners()
    {
        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                   SegundoPlano sp = new SegundoPlano("llenarTabla");
                    sp.execute();


                }
                return false;
            }
        });


        btn_Terminar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });



        tabla.addDataClickListener(new ListenerClickTabla());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.toolbar_cancelar, menu);
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
        if((id == R.id.recargar))
        {

            if(!edtx_CodigoPallet.getText().toString().equals(""))
            {
                tabla.getDataAdapter().clear();
                SegundoPlano sp = new  SegundoPlano("llenarTabla");
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

            renglonSeleccionado = rowIndex;
            tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
            tabla.getDataAdapter().notifyDataSetChanged();
            PalletSeleccionadoTabla = clickedData[1];

            btn_Terminar.setEnabled(true);
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

            }
            else
            {
                Color = R.color.blancoLetraStd;
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

            switch (tarea) {
                case "llenarTabla":

                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();
                    break;
                case "ConsultaPalletConsAbierto":

                    break;

            }


            if(decision.equals("true")&&tarea.equals("llenarTabla"))
            {
                sacaDatos();
                int i = 0;
                DATA_TO_SHOW = new String[arrayDatosTabla.size()][6];
                for(datosTabla a:arrayDatosTabla)
                {

                    DATA_TO_SHOW[i][0] = a.getTipo();
                    DATA_TO_SHOW[i][1] = a.getCodigoPallet();
                    DATA_TO_SHOW[i][2] = a.getProducto();
                    DATA_TO_SHOW[i][3] = a.getLote();
                    DATA_TO_SHOW[i][4] = a.getCantidadActual();
                    DATA_TO_SHOW[i][5] = a.getEmpaques();
                    i++;
                    Log.i("SoapResponse", " SOAPDATOS" + DATA_TO_SHOW.toString());
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            if (decision.equals("true"))
            {
                switch (tarea) {
                    case "llenarTabla":
                        tabla.getDataAdapter().notifyDataSetChanged();
                        tabla.setDataAdapter(new SimpleTableDataAdapter(Almacen_Consolidacion.this, DATA_TO_SHOW));
                        tabla.getDataAdapter().notifyDataSetChanged();
                        arrayDatosTabla.clear();


                        break;
                    case "ConsultaPalletConsAbierto":

                        break;



                }
            }

            if(decision.equals("false"))
            {
                new popUpGenerico(contexto, vista, mensaje,"Advertencia", true, true);
            }

            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }



    private void sacaDatos()
    {
        SoapObject tabla = sa.parser();
        String Tipo,IdStatus,CodigoPallet,Producto,Lote,CantidadActual,Empaques;

        arrayDatosTabla = null;
        arrayDatosTabla = new ArrayList<>();
        if(tabla!=null)
            for(int i = 0; i<tabla.getPropertyCount();i++)
            {
                try {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    Log.d("SoapResponse",tabla1.toString());

                    Tipo =  tabla1.getPrimitivePropertyAsString("Tipo");
                    IdStatus= tabla1.getPrimitivePropertyAsString("IdStatus");
                    CodigoPallet= tabla1.getPrimitivePropertyAsString("CodigoPallet");
                    Producto = tabla1.getPrimitivePropertyAsString("Producto");
                    Lote= tabla1.getPrimitivePropertyAsString("Lote");
                    CantidadActual = tabla1.getPrimitivePropertyAsString("CantidadActual");
                    Empaques = tabla1.getPrimitivePropertyAsString("Empaques");

                  datosTabla d = new datosTabla(Tipo,IdStatus,CodigoPallet,Producto,Lote,CantidadActual,Empaques);
                    arrayDatosTabla.add(d);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
    }
}
