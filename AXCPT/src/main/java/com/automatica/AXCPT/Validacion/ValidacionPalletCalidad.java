package com.automatica.AXCPT.Validacion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.FragmentoValidarEmpaques;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.databinding.ActivityValidacionPalletCalidadBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class ValidacionPalletCalidad extends AppCompatActivity  implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar {

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    private static String strIdTabla = "strIdTablaTotales";
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(ValidacionPalletCalidad.this);
    boolean OrdenCompraSeleccionada, Recarga = true;
    ActivityValidacionPalletCalidadBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    String articulo;
    String cantidad;
    String lote;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValidacionPalletCalidadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        agregarListener();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Validación");
        getSupportActionBar().setSubtitle("(Calidad)");
        binding.edtxPallet.requestFocus();
        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("", "");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal, taskbar_axc, "FragmentoTaskBar").commit();
    }

    private void agregarListener() {

        binding.edtxPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if(binding.edtxPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto,null, "Ingrese el pallet.", false, true, true);
                        return false;
                    }
                    else{
                        new ValidacionPalletCalidad.SegundoPlano("LlenarTabla").execute();
                    }


                }
                return false;
            }
        });

    }

    // MÉTODOS DEL CICLO DE VIDA DE ACTIVITY
    @Override
    protected void onResume() {
       // new ValidiacionCalidad.SegundoPlano("LlenarTabla").execute();
        if(binding.edtxPallet.getText().toString().isEmpty()){
            tabla.getDataAdapter().clear();
        }

        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.VALIDAR);
    }

    // MÉTODOS PARA LA TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.cerrar_op_toolbar_cancelar, menu);
            return true;

        } catch (Exception e) {
            Toast.makeText(contexto, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (p.ispBarActiva())
        {

            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo)) {

                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar) {

                new ValidacionPalletCalidad.SegundoPlano("LlenarTabla").execute();
            }

            if ((id == R.id.cancelar_pallets))
            {
                RechazarPallet();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // MÉTODOS PARA EL MENÚ INFERIOR
    @Override
    public void BotonDerecha() {
        ValidacionFinal();


    }

    @Override
    public void BotonIzquierda() {onBackPressed();  }

    // EVENTOS PARA LA TABLA
    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {
        articulo = clickedData[1];
        cantidad = clickedData[3];
        lote = clickedData[4];
        OrdenCompraSeleccionada = true;
    }

    // CLASE PARA LLAMAR A LOS WEBSERVICES
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(ValidacionPalletCalidad.this);
        String Tarea;
        DataAccessObject dao;

        private SegundoPlano(String Tarea) {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute() {

            p.ActivarProgressBar(Tarea);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                switch (Tarea) {

                    case "LlenarTabla":
                        dao = ca.c_ListarEmpaquesCalidad(binding.edtxPallet.getText().toString());
                        break;

                    case "ConsultaPallet":
                        dao = ca.c_ConsultaInfoPallet(binding.edtxPallet.getText().toString());
                        break;

                    case"Validar":
                        dao = ca.c_ValidarPalletCalidad(binding.edtxPallet.getText().toString());
                        break;
                    case "Rechazar":
                       dao = ca.c_RechazarPalletCalidad(binding.edtxPallet.getText().toString());
                        break;



                }
            } catch (Exception e) {

                dao = new DataAccessObject(e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {

                if (dao.iscEstado()) {

                    switch (Tarea) {

                        case "LlenarTabla":

                            binding.tvPzas.setText(dao.getcMensaje());
                            new ValidacionPalletCalidad.SegundoPlano("ConsultaPallet").execute();
                            new esconderTeclado(ValidacionPalletCalidad.this);
                            if (ConfigTabla_Totales == null) {

                                ConfigTabla_Totales = new TableViewDataConfigurator(tabla, dao,ValidacionPalletCalidad.this);
                            } else{
                                ConfigTabla_Totales.CargarDatosTabla(dao);
                            }
                            break;

                        case "ConsultaPallet":
                            binding.tvDocumento.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Documento"));
                            binding.tvArticulo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.tvFecha.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Fecha"));
                            binding.tvPiezas.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.tvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            break;

                        case "Validar":
                            pop.popUpGenericoDefault(vista, "Pallet validado", true);
                            ReiniciarVariables();
                            break;
                        case "Rechazar":
                            pop.popUpGenericoDefault(vista, "Pallet cancelado", true);
                            ReiniciarVariables();
                            break;
                    }
                } else {

                    pop.popUpGenericoDefault(vista, dao.getcMensaje(), false);
                    ReiniciarVariables();

                }

            } catch (Exception e) {
                e.printStackTrace();
                pop.popUpGenericoDefault(vista, e.getMessage(), false);

            }
            p.DesactivarProgressBar(Tarea);
        }
    }

    // MÉTODOS ADICIONALES
    private void ValidacionFinal() {

        new CreaDialogos(getString(R.string.pregunta_validar_pallet), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {

                new ValidacionPalletCalidad.SegundoPlano("Validar").execute();
                new esconderTeclado(ValidacionPalletCalidad.this);
            }
        },null,contexto);

//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
//                .add(R.id.Pantalla_principal, FragmentoValidarEmpaques.newInstance(binding.edtxPallet.getText().toString(), articulo,  cantidad, lote), "FragmentoValidar").addToBackStack("FragmentoValidar").commit();
//

//        if (!OrdenCompraSeleccionada) {
//            pop.popUpGenericoDefault(vista, "Seleccione un documento", false);
//        } else {
//            new ValidacionPalletCalidad.SegundoPlano("ImportarPartidas").execute();
//            Intent intent = new Intent(ValidacionPalletCalidad.this, RecepcionSeleccionarPartidaHC.class);
//            intent.putExtras(b);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
//        }

    }

    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!=null){
            if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
                taskbar_axc.cerrarFragmento();
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        Intent intent = new Intent(ValidacionPalletCalidad.this, ValidacionCalidad.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    public  void  RechazarPallet(){
                new CreaDialogos(getString(R.string.pregunta_rechazo_pallet), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {
                        new ValidacionPalletCalidad.SegundoPlano("Rechazar").execute();
                        new esconderTeclado(ValidacionPalletCalidad.this);
                    }
                },null,contexto);

    }

    public void ReiniciarVariables(){
        binding.edtxPallet.setText("");
        binding.tvDocumento.setText("-");
        binding.tvArticulo.setText("-");
        binding.tvFecha.setText("-");
        binding.tvPiezas.setText("-");
        binding.tvCantidad.setText("-");
        tabla.getDataAdapter().clear();
        binding.edtxPallet.requestFocus();
        new esconderTeclado(ValidacionPalletCalidad.this);
    }
}