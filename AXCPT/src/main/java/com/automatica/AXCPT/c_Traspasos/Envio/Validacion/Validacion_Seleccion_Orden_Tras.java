package com.automatica.AXCPT.c_Traspasos.Envio.Validacion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion.Validacion_PorPallet;
import com.automatica.AXCPT.c_Traspasos.Envio.SeleccionarOrdenesTraspasoEnvio;
import com.automatica.AXCPT.c_Traspasos.MenuTraspaso;
import com.automatica.AXCPT.databinding.ActivityEmbarquesSeleccionOrdenBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class Validacion_Seleccion_Orden_Tras extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    private Toolbar toolbar;
    private ActivityHelpers activityHelpers;
    private ProgressBarHelper p;
    private ActivityEmbarquesSeleccionOrdenBinding binding;
    private Context contexto = Validacion_Seleccion_Orden_Tras.this;
    private SortableTableView tabla;
    private TableViewDataConfigurator tableViewDataConfigurator;
    frgmnt_taskbar_AXC taskbar_axc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEmbarquesSeleccionOrdenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        InicializarVariables();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new SegundoPlano("ConsultaOrdenes").execute();
    }

    private void InicializarVariables(){
        p = new ProgressBarHelper(this);
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenus(Validacion_Seleccion_Orden_Tras.this,R.id.Pantalla_principal,true);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Validación");
        getSupportActionBar().setSubtitle("Selección Documento");
        tabla = binding.customtableview.tableViewOC;


        tableViewDataConfigurator =  TableViewDataConfigurator.newInstance(tabla, null, Validacion_Seleccion_Orden_Tras.this, new TableViewDataConfigurator.TableClickInterface() {
            @Override
            public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

            }

            @Override
            public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

            }

            @Override
            public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (p.ispBarActiva()) {
            int id = item.getItemId();
            switch (id) {
                case R.id.InformacionDispositivo:
                    new sobreDispositivo(contexto, getCurrentFocus());
                    return true;

                case R.id.recargar:
                    new SegundoPlano("ConsultaOrdenes").execute();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.SELECCIONAR);
        super.onPostCreate(savedInstanceState);
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Validacion_Seleccion_Orden_Tras.this, MenuTraspaso.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    @Override
    public void BotonDerecha()
    {
//        if (!tableViewDataConfigurator.renglonEstaSelec())
//        {
//            new popUpGenerico(contexto,getCurrentFocus(),"Seleccione una orden para continuar.",false,true,true);
//            return;
//        }

        Bundle b = new Bundle();

      if(tableViewDataConfigurator.renglonEstaSelec())
      {
          Constructor_Dato cd = Constructor_Dato.getValue(tableViewDataConfigurator.getRenglonSeleccionado(), "Orden");

          if(cd!=null)
          {
              String tmpDocumento = cd.getDato();

              if(tmpDocumento == null)
              {
                  tmpDocumento = "";
              }
              b.putString("Documento",tmpDocumento);
          }
      }else{
          b.putString("Documento","");
      }

        startActivity(new Intent(contexto, Validacion_PorPallet_Tras.class).putExtras(b));
        overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {
        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia ca = new cAccesoADatos_Transferencia(contexto);

        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            p.ActivarProgressBar(Tarea);
        }

        @Override
        protected Void doInBackground(String... strings)
        {
            try
            {
                switch (Tarea)
                {
                    case "ConsultaOrdenes":
                            dao = ca.c_ConsultaOrdenSurtidoTras("@","SURTIDA");
                        break;

                    default:
                        dao = new DataAccessObject();
                }

            } catch (Exception e)
            {
                dao = new DataAccessObject(e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            try {
                if (dao.iscEstado())
                {
                    switch (Tarea)
                    {
                        case "ConsultaOrdenes":
                            try
                            {
                                if(tableViewDataConfigurator!=null)
                                {
                                    tableViewDataConfigurator.CargarDatosTabla(dao);
                                }

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            break;
                    }
                } else {
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), false, true, true);
                    tabla.getDataAdapter().clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.DesactivarProgressBar(Tarea);
        }
    }
}
