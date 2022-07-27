package com.automatica.AXCPT.c_Consultas;

import android.content.Context;
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
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Consultas;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class consultas_ConsultaEmpaque extends AppCompatActivity
{
   //region Variables
    Context contexto= this;
    View vista;
    EditText Empaque;
    cAccesoADatos_Consultas ca;
    FrameLayout progressBarHolder;
    TextView tx_Codigo_empaque,tx_Codigo_pallet,tx_Num_Parte,tx_Orden1,tx_Lote,tx_cant_Orig,tx_cant_Act,tx_revision,tx_estatus,tx_fecha,tx_Desc,tx_ubicacion;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private Handler handlerRequestFocus = new Handler();


    //endregion

    //region generado
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.consultas_activity_consulta_empaque);
            ca = new cAccesoADatos_Consultas(getApplicationContext());
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" " + getString(R.string.Consultas_Empaque));
            //     this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            new cambiaColorStatusBar(contexto, R.color.AzulStd, consultas_ConsultaEmpaque.this);
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            DeclararVariables();
            Empaque.requestFocus();
            Empaque.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (Empaque.getText().toString().equals(""))
                        {
                            new popUpGenerico(consultas_ConsultaEmpaque.this, getCurrentFocus(), "Ingrese el código del paquete.", false, true, true);
                        } else
                        {
                            HiloRecibeDatos hrd = new HiloRecibeDatos();
                            hrd.execute();
                            new esconderTeclado(consultas_ConsultaEmpaque.this);
                        }
                    }
                    return false;
                }
            });
        }catch ( Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try
        {
            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {

                reiniciarVariables();

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(Empaque.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }catch ( Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }
        return super.onOptionsItemSelected(item);
    }

    public  void  DeclararVariables()
    {
        try
        {

            Empaque = (EditText) findViewById(R.id.edtx_CodigoEmpaque);
            Empaque.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            tx_Codigo_empaque = findViewById(R.id.tx_Codigo_empaque);
            tx_Codigo_pallet = findViewById(R.id.tx_Codigo_pallet);
            tx_Num_Parte = findViewById(R.id.tx_Num_Parte);
            tx_Orden1 = findViewById(R.id.tx_Orden1);
            tx_Lote = findViewById(R.id.tx_Lote);
            tx_cant_Orig = findViewById(R.id.tx_cant_Orig);
            tx_cant_Act = findViewById(R.id.tx_cant_Act);
            tx_revision = findViewById(R.id.tx_revision);
            tx_estatus = findViewById(R.id.tx_estatus);
            tx_fecha = findViewById(R.id.tx_fecha);
            tx_Desc = findViewById(R.id.tx_Desc);
            tx_ubicacion = findViewById(R.id.tx_ubicacion);
            Empaque.requestFocus();
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
        }
    }

    //endregion

    public  void  reiniciarVariables(){
        Empaque.setText("");
        tx_Codigo_empaque.setText("");
        tx_Codigo_pallet.setText("");
        tx_Num_Parte.setText("");
        tx_Orden1.setText("");
        tx_Lote.setText("");
        tx_cant_Orig.setText("");
        tx_cant_Act.setText("");
        tx_revision.setText("");
        tx_estatus.setText("");
        tx_fecha.setText("");
        tx_Desc.setText("");
        tx_ubicacion.setText("");
    }
    //region AsyncTask
    class HiloRecibeDatos extends AsyncTask<Void,Void,Void>
    {

        String datoConsulta;
        DataAccessObject dao;
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
                datoConsulta = Empaque.getText().toString();
                dao = ca.c_ConsultaEmpaque(datoConsulta);
            }catch(Exception e)
            {
                e.printStackTrace();
                dao = new DataAccessObject(e);
            }
        return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {

            try
            {
                if (dao.iscEstado())
                {
                    String revision;
                    tx_Codigo_empaque.setText(Empaque.getText().toString());
                    tx_Codigo_pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                    tx_Num_Parte.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                    tx_Orden1.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenProd"));
                    tx_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteAXC"));
                    tx_cant_Orig.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadOriginal"));
                    tx_cant_Act.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                    tx_revision.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Revision"));
                    tx_estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                    tx_fecha.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("FechaCrea"));
                    tx_Desc.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Descripcion"));
                    tx_ubicacion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Ubicacion"));
                    revision = (dao.getSoapObject_parced().getPrimitivePropertyAsString("Revision"));
                    switch (revision){
                        case "1":
                            tx_revision.setText("Etiquetado");
                            break;
                        case "0":
                            tx_revision.setText("No etiquetado");
                            break;
                    }
                    Empaque.setText("");
                    Empaque.requestFocus();
                } else {
                    reiniciarVariables();
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), "Advertencia", true, true);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),String.valueOf(dao.iscEstado()),true,true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }
    //endregion
//endregion
}
