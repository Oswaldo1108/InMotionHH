package com.automatica.AXCPT.c_Almacen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Almacen_Impresion_Etiquetas extends AppCompatActivity {
    EditText edtx_pallet;
    TextView txtv_Pallet,txtv_Status,txtv_CantidadEmpaques;
    SortableTableView tabla;
    Button btn_ImprimirEtiquetas;
    FrameLayout progressBarHolder;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    SimpleTableDataAdapter st ;
    View vista;
    private String[] HEADER;
    SimpleTableHeaderAdapter sthd;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    boolean Seleccionada,Recarga=true;
    Context contexto = Almacen_Impresion_Etiquetas.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen__impresion__etiquetas);
        InicializarVariables();
        DeclararListeners();

    }

    public void InicializarVariables(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Álmacen");
        getSupportActionBar().setSubtitle("Impresión de etiquetas");
        edtx_pallet= findViewById(R.id.edtx_pallet);
        edtx_pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        txtv_Pallet= findViewById(R.id.txtv_Pallet);
        txtv_Status= findViewById(R.id.txtv_Status);
        txtv_CantidadEmpaques= findViewById(R.id.txtv_CantidadEmpaques);
        btn_ImprimirEtiquetas= findViewById(R.id.btn_ImprimirEtiquetas);
        tabla= findViewById(R.id.tableView_OC);
        progressBarHolder = findViewById(R.id.progressBarHolder);
    }

    public void DeclararListeners(){
        edtx_pallet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(edtx_pallet.getText())){
                    new popUpGenerico(contexto,getCurrentFocus(),"Llene el campo 'pallet'",false,true,true);
                    return false;
                }
                new SegundoPlano("Consulta").execute();
                return false;
            }
        });
        btn_ImprimirEtiquetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *
                 */
            }
        });
        tabla.addDataClickListener(new ListenerClickTabla());
        tabla.addHeaderClickListener(new headerClickListener());
        tabla.addDataLongClickListener(new ListenerLongClickTabla());
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
            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                renglonAnterior=-1;
            }
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

            Toast.makeText(contexto, HEADER[columnIndex], Toast.LENGTH_SHORT).show();
            Log.d("SoapResponse", HEADER[columnIndex]);
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
    private void reiniciarDatos()
    {
        tabla.getDataAdapter().clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
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
        if(Recarga)
        {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar)
            {
                new SegundoPlano("Tabla").execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private class SegundoPlano extends AsyncTask<String, Void,Void> {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca;
        public SegundoPlano(String Tarea) {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute() {
            Recarga = false;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                ca= new cAccesoADatos_Almacen(contexto);
                switch (Tarea){
                    case "Consulta":
                        dao = ca.c_ConsultarPalletPT(edtx_pallet.getText().toString());
                        break;
                    case "Tabla":
                        //sa.SOAPConsultaPallet(CodigoPallet,contexto);
                        dao = ca.c_ConsultaEmpaquesPallet_Impresion(edtx_pallet.getText().toString());
                        break;
                }
            }catch (Exception e){
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (dao.iscEstado()){
                    switch (Tarea){
                        case "Tabla":
                            try {
                                tabla.getDataAdapter().clear();
                                tabla.getDataAdapter().notifyDataSetChanged();
                            }catch (Exception e){
                                e.getStackTrace();
                            }
                            if (dao.getcTablaUnica()!=null){
                                try{
                                    tabla.setHeaderAdapter(sthd = new SimpleTableHeaderAdapter(contexto,dao.getcEncabezado()));
                                    tabla.setDataAdapter(st= new SimpleTableDataAdapter(contexto,dao.getcTablaUnica()));
                                    st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                    sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                    tabla.getDataAdapter().notifyDataSetChanged();
                                }catch (Exception e){
                                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
                                }
                            }
                            break;
                        case "Consulta":
                            txtv_Pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            txtv_CantidadEmpaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            txtv_Status.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                            new SegundoPlano("Tabla").execute();
                            break;
                    }
                }else {
                    new popUpGenerico(contexto,getCurrentFocus(),dao.getcMensaje(),false,true,true);
                }
            }catch (Exception e){
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
                e.getStackTrace();
            }
            Recarga = true;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}