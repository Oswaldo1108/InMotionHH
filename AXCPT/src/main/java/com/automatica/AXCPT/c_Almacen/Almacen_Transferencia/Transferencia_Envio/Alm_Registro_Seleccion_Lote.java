package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Fragmento_Cancelar_Tarima;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso.Fragmento_Rechazo_Pallet_Trasp;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Rec_Registro_Seleccion_Lote;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;

import de.codecrafters.tableview.SortableTableView;

public class Alm_Registro_Seleccion_Lote extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface,Fragmento_Rechazo_Pallet_Trasp.OnFragmentInteractionListener
{
    //region variables
    SortableTableView tabla;
    TableViewDataConfigurator ConfigTabla = null;
    ProgressBarHelper p;
    Button btnConfirmar;
    EditText edtx_Transferencia;
    Context contexto = this;
    Bundle b = new Bundle();
    Handler h = new Handler();

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recepcion_activity_registro_transferer_seleccion_orden);
            declaraVariables();
            AgregaListeners();

            String Doc= null;
            Doc = getIntent().getStringExtra("Documento");
            if (Doc!=null)
            {
                edtx_Transferencia.setText(Doc);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume()
    {
        if(!edtx_Transferencia.getText().toString().equals(""))
        {
         new SegundoPlano("Tabla").execute();
        }
        super.onResume();
    }

    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.almacen_traspaso));
        getSupportActionBar().setSubtitle("Envío");

        tabla = (SortableTableView) findViewById(R.id.tableView_OC);

        edtx_Transferencia  = (EditText) findViewById(R.id.edtx_Transferencia);
        btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);

        edtx_Transferencia.requestFocus();

        p = new ProgressBarHelper(this);
    }
    private void AgregaListeners()
    {

        edtx_Transferencia.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_Transferencia.getText().toString().equals(""))
                    {
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Transferencia.requestFocus();
                                edtx_Transferencia.setText("");
                            }
                        });
                        new popUpGenerico(contexto,getCurrentFocus(), getString(R.string.error_ingrese_orden_transferencia),"false", true, true);
                    }

                    new SegundoPlano("Tabla").execute();
                    new esconderTeclado(Alm_Registro_Seleccion_Lote.this);
                    return true;
                }
                return false;
            }

        });
        btnConfirmar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!ConfigTabla.renglonEstaSelec())
                {
                    new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_seleccionar_registro) ,"false" ,true,true );
                    return;
                }

                b.putString("Transferencia",edtx_Transferencia.getText().toString());
                Intent intent = new Intent(Alm_Registro_Seleccion_Lote.this, Almacen_Transferencia_Menu_Decision.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.cerrar_oc_toolbar_cancelar, menu);
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

    if(p.ispBarActiva())
        {
            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, null);
            }
            if (id == R.id.recargar)
            {
                if(!edtx_Transferencia.getText().toString().equals(""))
                {
                  new SegundoPlano("Tabla").execute();
                }
            }
            if (id == R.id.CerrarOC)
            {
                if(!edtx_Transferencia.getText().toString().equals(""))
                {
                    new CreaDialogos("¿Cerrar documento parcial en AXC y SAP?",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                    new esconderTeclado(Alm_Registro_Seleccion_Lote.this);
//                                    new popUpGenerico(contexto,getCurrentFocus() ,"Cierre parcial de envio de traspaso desactivado." ,false ,true,true );

                                    new SegundoPlano("CerrarTransfer").execute();
                                }
                            },null,contexto);
                }
            }

            if (id == R.id.cancelar_pallets)
            {
                if(!edtx_Transferencia.getText().toString().equals(""))
                {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.cl, Fragmento_Rechazo_Pallet_Trasp.newInstance(edtx_Transferencia.getText().toString(), null,""), "FRAGTAG111").addToBackStack("FRAGTAG111")
                            .commit();
                }
            }
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
        new popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {
        b.putString("Partida",clickedData[0]);
        b.putString("Origen",clickedData[1]);
        b.putString("Producto",clickedData[2]);
        b.putString("Lote",clickedData[3]);
        b.putString("CantSolicitada",clickedData[4]);
        b.putString("CantSurtida",clickedData[5]);
        b.putString("CantPendiente",clickedData[6]);
        b.putString("FechaProd",clickedData[7]);
        b.putString("FechaCad",clickedData[8]);
    }

    private void reiniciarDatos()
    {
        tabla.getDataAdapter().clear();
        b.clear();
    }

    @Override
    public boolean AceptarOrden()
    {
        if(!edtx_Transferencia.getText().toString().equals(""))
        {
            new SegundoPlano("Tabla").execute();
        }
        return false;
    }

    @Override
    public void EstadoCarga(Boolean Estado)
    {
        if(Estado)
        {
            p.ActivarProgressBar();
        }
        else
        {
            p.DesactivarProgressBar();
        }
    }

    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Alm_Registro_Seleccion_Lote.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
          p.ActivarProgressBar();
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case"Tabla":
                        dao = cad.cad_ListarPartidasRecepcionTraspaso(edtx_Transferencia.getText().toString());
                        break;

                    case"CerrarTransfer":
                        dao = cad.c_CierreParcialEnvioTraspaso(edtx_Transferencia.getText().toString());
                        break;
                }
            }catch (Exception e)
            {
                dao = new DataAccessObject(e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
         {
            try {
                if (dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case "Tabla":
                            if(ConfigTabla == null)
                            {
                                ConfigTabla =  new TableViewDataConfigurator( 9,"CERRADOPARCIAL","LIBERADO","RECIBIENDO",tabla, dao, Alm_Registro_Seleccion_Lote.this);
                            }else
                            {
                                ConfigTabla.CargarDatosTabla(dao);
                            }
                            break;

                        case"CerrarTransfer":
                            new popUpGenerico(contexto, null,dao.getcMensaje(),dao.iscEstado(), true, true);
                            break;
                    }
                }
            else
                {
                    new popUpGenerico(contexto, getCurrentFocus(),dao.getcMensaje(),dao.iscEstado(), true, true);
                    reiniciarDatos();
                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                e.printStackTrace();
            }
            p.DesactivarProgressBar();
        }

        @Override
        protected void onCancelled()
        {
            p.DesactivarProgressBar();
        }
    }
}
