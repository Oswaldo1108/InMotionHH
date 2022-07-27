package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Fragmento_Cancelar_Tarima;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;

import de.codecrafters.tableview.SortableTableView;

public class Rec_Registro_Seleccion_Lote extends AppCompatActivity implements Fragmento_Cancelar_Tarima.OnFragmentInteractionListener, TableViewDataConfigurator.TableClickInterface
{
    //region variables
    SortableTableView tabla;
    TableViewDataConfigurator ConfigTabla = null;
    Button btnConfirmar;
    ProgressBarHelper progressBarHelper;
    EditText edtx_Transferencia;
    Context contexto = this;
    Bundle b = new Bundle();
    Handler h = new Handler();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recepcion_activity_registro_transferer_seleccion_orden);
        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Rec_Registro_Seleccion_Lote.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declaraVariables();
        AgregaListeners();
        edtx_Transferencia.requestFocus();
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

        tabla = (SortableTableView) findViewById(R.id.tableView_OC);

        edtx_Transferencia  = (EditText) findViewById(R.id.edtx_Transferencia);
        btnConfirmar = (Button) findViewById(R.id.btn_RegistrarPallets);
        progressBarHelper = new ProgressBarHelper(this);
    }
    private void AgregaListeners()
    {

        edtx_Transferencia.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_Transferencia.getText().toString().equals(""))
                            {

                                SegundoPlano sp = new SegundoPlano("Tabla");
                                sp.execute();
                                new esconderTeclado(Rec_Registro_Seleccion_Lote.this);

                            }
                        else
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
                        new esconderTeclado(Rec_Registro_Seleccion_Lote.this);
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
                ValidacionFinal();
            }
        });
    }

    private void ValidacionFinal()
    {

        if(ConfigTabla.renglonEstaSelec())
        {
            Intent intent = new Intent(Rec_Registro_Seleccion_Lote.this, Recepcion_Menu_Transferencia_Etiquetas.class);
            intent.putExtras(b);
            startActivity(intent);
        }else
        {
            new popUpGenerico(contexto,getCurrentFocus() ,getString(R.string.error_seleccionar_registro) ,"false" ,true,true );
        }

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
        if(progressBarHelper.ispBarActiva())
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

                                        new SegundoPlano("CerrarTransfer").execute();
                                        new esconderTeclado(Rec_Registro_Seleccion_Lote.this);
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
                                .replace(R.id.cl, Fragmento_Cancelar_Tarima.newInstance(edtx_Transferencia.getText().toString(), null,""), "FRAGTAG111").addToBackStack("FRAGTAG111")
                                .commit();
                    }
                }
            }

        return super.onOptionsItemSelected(item);
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

    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {
        Toast.makeText(contexto, Header, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {
        new popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {
        b.putString("Transferencia",edtx_Transferencia.getText().toString());
        b.putString("Partida",clickedData[0]);
        b.putString("Producto",clickedData[1]);
        b.putString("Detalle",clickedData[2]);
        b.putString("Lote",clickedData[3]);
        b.putString("CantSolicitada",clickedData[4]);
        b.putString("CantSurtida",clickedData[5]);
        b.putString("CantPendiente",clickedData[6]);
        b.putString("CantRecibida",clickedData[7]);
        b.putString("Origen",clickedData[8]);
        b.putString("FechaProd",clickedData[9]);
        b.putString("FechaCad",clickedData[9]);
    }

    private void reiniciarDatos()
    {


        tabla.getDataAdapter().clear();
        b.clear();

    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(Rec_Registro_Seleccion_Lote.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            progressBarHelper.ActivarProgressBar();
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
                                dao = cad.c_CierreParcialTraspaso(edtx_Transferencia.getText().toString());
                                break;
                        }
                }catch (Exception e)
                {
                    dao = new DataAccessObject(e);
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
                                      ConfigTabla =  new TableViewDataConfigurator( 9,"CERRADO[Transito]","LIBERADO","RECIBIENDO",tabla, dao, Rec_Registro_Seleccion_Lote.this);
//                                        ConfigTabla =  new TableViewDataConfigurator(tabla, dao, Rec_Registro_Seleccion_Lote.this);
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
                        new popUpGenerico(contexto, null,dao.getcMensaje(),dao.iscEstado(), true, true);
                        reiniciarDatos();
                    }
            }catch (Exception e)
                {
                    new popUpGenerico(contexto, null, e.getMessage(), false, true, true);
                }
            progressBarHelper.DesactivarProgressBar();
        }

        @Override
        protected void onCancelled()
        {

            Log.e("SP", "onCancelled: hilo terminado" );
            progressBarHelper.ActivarProgressBar();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
