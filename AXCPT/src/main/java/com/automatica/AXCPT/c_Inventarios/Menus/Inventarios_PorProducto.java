package com.automatica.AXCPT.c_Inventarios.Menus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Inventarios.Granel.Inventarios_ConfirmarEmpaqueGranel;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Inventarios_PorProducto extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface,frgmnt_taskbar_AXC.interfazTaskbar
{
    //region variables
    Button btnIniciarInventario;
    SortableTableView tabla;
    TableViewDataConfigurator ConfigTabla = null;
    ProgressBarHelper progressBarHelper;
    Bundle b;
    String UbicacionIntent, IdInventario;
    Context contexto = this;
    frgmnt_taskbar_AXC taskbar_axc;


    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarios_activity_por_producto);
        getExtrasIntent();
        declararVariables();
        AgregaListeners();

        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(Inventarios_PorProducto.this));
        View logoView = getToolbarLogoIcon((Toolbar) findViewById(R.id.toolbar));
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .replace(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(findViewById(R.id.FrameLayout).getId(),taskbar_axc,"FragmentoTaskBar").commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new SegundoPlano("llenarTabla").execute();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.TOMAR_INVENTARIO);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ((id == R.id.InformacionDispositivo)) {
            new sobreDispositivo(contexto, null);
        }
        if ((id == R.id.recargar)) {
            //tabla.getDataAdapter().clear();
            SegundoPlano sp = new SegundoPlano("llenarTabla");
            sp.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {
            if(Seleccionado)
            {
                UbicacionIntent = clickedData[2];
            }else
            {
                UbicacionIntent = "";
            }
    }

    private void declararVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.Inventarios_CiclicoPorProducto));
        tabla  = (SortableTableView) findViewById(R.id.tableView_OC);
        progressBarHelper = new ProgressBarHelper(this);
        btnIniciarInventario = (Button) findViewById(R.id.btn_TomarInventario);
    }

    private void getExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            IdInventario = b.getString("IdInv");
        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    private void AgregaListeners()
    {

        btnIniciarInventario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent = new Intent(Inventarios_PorProducto.this, Inventarios_PantallaPrincipal.class);
                    if(ConfigTabla.renglonEstaSelec())
                    {

                        if (Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(), "Tipo").getDato().equals("PICKING"))
                        {
                            intent = new Intent(Inventarios_PorProducto.this, Inventarios_ConfirmarEmpaqueGranel.class);
                        }else
                        {
                            intent = new Intent(Inventarios_PorProducto.this, Inventarios_PantallaPrincipal.class);
                        }

                        intent.putExtra("UbicacionIntent", Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(), "Posición").getDato());

                    }
                    intent.putExtras(b);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);


                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }

            }
        });
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        String tarea;
        DataAccessObject dao;
        adInventarios ca = new adInventarios(contexto);

        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            progressBarHelper.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case "llenarTabla":
                        dao = ca.c_ConsultaInventarioNumParte(b.getString("IdInv"));
                        break;
                    default:
                        dao = new DataAccessObject();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
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
                    switch (tarea)
                    {
                        case "llenarTabla":
                        if(!dao.getcMensaje().equals("")){


                                if(ConfigTabla == null)
                                {
                                    ConfigTabla =  new TableViewDataConfigurator( 1,"REGISTRADO","SIN REGISTRO","RECIBIENDO",tabla, dao, Inventarios_PorProducto.this);
                                }else
                                {
                                    ConfigTabla.CargarDatosTabla(dao);
                                }

                        }else{
                            new popUpGenerico(contexto, getCurrentFocus(),"Sin registros", false, true, true);
                        }
                            break;


                    }
                } else {
                    new popUpGenerico(contexto, null, dao.getcMensaje(), dao.iscEstado(), true, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
            progressBarHelper.DesactivarProgressBar();

        }
    }
    @Override
    public void BotonDerecha() {
        btnIniciarInventario.callOnClick();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
            taskbar_axc.cerrarFragmento();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        Intent intent = new Intent(Inventarios_PorProducto.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}
