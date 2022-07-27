package com.automatica.AXCPT.c_Consultas;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorFragConsultas;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class consultas_ConsultaPallet extends AppCompatActivity
{
    //region generados
    Context contexto = this;
    View vista;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    cAccesoADatos_Consultas ca;
    AlphaAnimation outAnimation;
    EditText edtx_pallet;
    boolean empaquesB=false;
    String pallet;
    SimpleTableDataAdapter st;
    SimpleTableHeaderAdapter sth;
    SortableTableView tblv_Referencia;
    HorizontalScrollView hsv_tabla_embarques;
    RecyclerView recycler;
    private Handler handlerRequestFocus = new Handler();
    Boolean datosEnTabla = false;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultas_activity_consulta_pallet);
        ca = new cAccesoADatos_Consultas(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        edtx_pallet = findViewById(R.id.edtx_CodigoPallet);
        edtx_pallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Consultas_Pallet));
        //toolbar.setSubtitle("  Consulta de Pallet");
//        toolbar.setLogo(R.mipmap.logo_axc);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
      //  this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        new cambiaColorStatusBar(contexto, R.color.AzulStd, consultas_ConsultaPallet.this);
        DeclararVariables();
        edtx_pallet.requestFocus();
        edtx_pallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    if (edtx_pallet.getText().toString().equals("")){
                        new popUpGenerico(consultas_ConsultaPallet.this,getCurrentFocus(),"Favor de llenar campo ''Pallet''","Advertencia",true,true);
                    }
                    else {
                        SegundoPlanoPallet tarea = new SegundoPlanoPallet("Pallet");
                        tarea.execute();
                        new esconderTeclado(consultas_ConsultaPallet.this);
                        return true;
                    }
                }
                return false;

            }

        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.menu_pallet_consulta, menu);
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
            hsv_tabla_embarques.setVisibility(View.GONE);
            recycler.setVisibility(View.GONE);
            edtx_pallet.setText("");
        }
        if ((id==R.id.empaquesMenu)){
            if (!TextUtils.isEmpty(edtx_pallet.getText())){
                if (empaquesB){
                    new SegundoPlanoPallet("Pallet").execute();
                    empaquesB=false;
                }else {
                    new SegundoPlanoPallet("PalletNE").execute();
                    empaquesB=true;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
    public void DeclararVariables(){
        recycler = findViewById(R.id.recycler);
        progressBarHolder = findViewById(R.id.progressBarHolder);
        tblv_Referencia = findViewById(R.id.tblv_Referencia);
        hsv_tabla_embarques = findViewById(R.id.hsv_tabla_embarques);

    }
    private class SegundoPlanoPallet extends AsyncTask<Void,Void,Void>
    {
        DataAccessObject dao= new DataAccessObject();
        String Tarea;
        cAccesoADatos_Consultas ca = new cAccesoADatos_Consultas(consultas_ConsultaPallet.this);

        public SegundoPlanoPallet(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                switch (Tarea) {
                    case "Pallet":
                        dao = ca.c_ConsultarPalletPT(edtx_pallet.getText().toString());
                        break;
                    case "PalletNE":
                        dao = ca.c_ConsultaEmpaquesPallet_NE(edtx_pallet.getText().toString());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                TableColumnDpWidthModel model;
                if (dao.iscEstado()) {
                    switch (Tarea) {
                        case "Pallet":
                            //if (dao.getSoapObject_parced().getPrimitivePropertyAsString("Revision").equals("1")) {
                                recycler.setVisibility(View.VISIBLE);
                                hsv_tabla_embarques.setVisibility(View.GONE);
                                recycler.setAdapter(new AdaptadorFragConsultas(CrearAdapter(dao)));
                            //}
                            /**
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("Revision").equals("0")) {
                                new SegundoPlanoPallet("PalletNE").execute();
                            }
                            /**else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("Producto").equals("Compartido")){
                             new SegundoPlano("PalletConsolidado").execute();
                             }
                             */
                            break;
                        case "PalletNE":
                                hsv_tabla_embarques.setVisibility(View.VISIBLE);
                                recycler.setVisibility(View.GONE);
                                model = new TableColumnDpWidthModel(consultas_ConsultaPallet.this, 16, 200);
                                tblv_Referencia.getLayoutParams().width = 3700;
                                CrearTabla(dao, model);
                            break;
                    }
                }else {
                    new  popUpGenerico(consultas_ConsultaPallet.this,getCurrentFocus(),dao.getcMensaje(),dao.iscEstado(),true,true);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), String.valueOf(dao.iscEstado()), true, true);

            }
            outAnimation= new AlphaAnimation(0f, 1f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    private ArrayList<Constructor_Dato> CrearAdapter(DataAccessObject dao) {
        ArrayList<Constructor_Dato> Datos = new ArrayList<>();
        PropertyInfo pisp;
        recycler.setLayoutManager(new LinearLayoutManager(consultas_ConsultaPallet.this));
        if (dao.getcTablaUnica() != null) {
            SoapObject objeto = dao.getSoapObject_parced();
            for (int i = 0; i < objeto.getPropertyCount(); i++) {
                pisp = new PropertyInfo();
                objeto.getPropertyInfo(i, pisp);
                String Etiqueta = pisp.name.replace("_x0020_", " ");
                Log.i("Etiqueta", Etiqueta);
                String Dato = objeto.getPropertyAsString(i);
                Log.i("Dato", Dato);
                if (Dato.equals("anyType{}")) {
                    Dato = "";
                }
                if (Etiqueta.equals("Revision")) {
                    Etiqueta = "Tipo de registro";
                    switch (Dato){
                        case "0":
                            Dato="Etiquetado";
                            break;
                        case "1":
                            Dato="No etiquetado";
                            break;
                    }
                }
                if (Etiqueta.contains("IdPallet")){
                    continue;
                }
                if (Etiqueta.contains("NumParte")){
                    Etiqueta= "Producto";
                }
                if (Etiqueta.contains("DescProd")){
                    Etiqueta= "Descripción";
                }
                if (Etiqueta.contains("LoteAXC")){
                    Etiqueta= "Lote AXC";
                }
                if (Etiqueta.contains("LoteProveedor")){
                    Etiqueta= "Lote Proveedor";
                }
                if (Etiqueta.contains("CantidadOriginal")){
                    Etiqueta= "Cantidad original";
                }
                if (Etiqueta.contains("CantidadActual")){
                    Etiqueta= "Cantidad actual";
                }
                if (Etiqueta.equals("Status")){
                    continue;
                }
                if (Etiqueta.contains("DescStatus")){
                    Etiqueta="Estatus";
                }
                if (Etiqueta.contains("OrdenProd")){
                    Etiqueta="Orden prod.";
                }
                if (Etiqueta.contains("Empaques")){
                    Etiqueta= "Cant. Empaques";
                }
                if (Etiqueta.contains("Estacion")){
                    Etiqueta="Estación";
                }
                if (Etiqueta.contains("FechaCrea")){
                    Etiqueta="Fecha reg.";
                }
                if (Etiqueta.contains("Ubicacion")){
                    Etiqueta="Ubicación";
                }

                Constructor_Dato dato = new Constructor_Dato(Etiqueta, Dato);
                Datos.add(dato);
            }
        }

        else {
            SoapObject objeto = dao.getSoapObject();
            for (int i = 0; i < objeto.getPropertyCount(); i++) {
                pisp = new PropertyInfo();
                objeto.getPropertyInfo(i, pisp);
                String Etiqueta = pisp.name.replace("_x0020_", " ");
                Log.i("Etiqueta", Etiqueta);
                String Dato = objeto.getPropertyAsString(i);
                Log.i("Dato", Dato);
                if (Dato.equals("anyType{}")) {
                    Dato = "";
                }
                Constructor_Dato dato = new Constructor_Dato(Etiqueta, Dato);
                Datos.add(dato);
            }
        }

        return Datos;
    }
    private void CrearTabla(DataAccessObject dao, TableColumnDpWidthModel model) {
        tblv_Referencia.setColumnModel(model);
        tblv_Referencia.setDataAdapter(st = new SimpleTableDataAdapter(consultas_ConsultaPallet.this, dao.getcTablaUnica()));
        tblv_Referencia.setHeaderAdapter(sth = new SimpleTableHeaderAdapter(consultas_ConsultaPallet.this, dao.getcEncabezado()));
        st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        sth.setTextColor(getResources().getColor(R.color.blancoLetraStd));
    }
}

