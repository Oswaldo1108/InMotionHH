package com.automatica.AXCPT.c_Traspasos.Recibe;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionPalletNe;
import com.automatica.AXCPT.databinding.ActivityRecepcionPalletNeBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class RecepcionTraspasoPalletNE extends AppCompatActivity implements  frgmnt_taskbar_AXC.interfazTaskbar{

    frgmnt_taskbar_AXC taskbar_axc;
    popUpGenerico pop = new popUpGenerico(RecepcionTraspasoPalletNE.this);
    ActivityRecepcionPalletNeBinding binding;
    CheckBox checkNumSerie;
    String  Cantidad,CodigoEmpaque, CantidadRecibida;
    private ProgressBarHelper p;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private Spinner sp_Partidas;
    String NumSerie;
    private Spinner sp_NumSerie;
    Handler handler = new Handler();
    Bundle b;
    View vista;
    Context contexto = this;
    boolean recargar;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            binding = ActivityRecepcionPalletNeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            configuracionToolbar();
            configuracionTaskbar();
            h.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {

                }
            },150);

        } catch (Exception e) {
            e.printStackTrace();
            pop.popUpGenericoDefault(vista, e.getMessage(), false);
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (p.ispBarActiva()) {
            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos)) {

            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void configuracionToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recepción traspaso");
        getSupportActionBar().setSubtitle("Pallet NE");
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
    }

    private void configuracionTaskbar() {
        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();
    }

    private void validacionFinal(){
        try {
            if (!binding.txtvEmpReg.getText().toString().equals("")) {
                if (Integer.parseInt(binding.txtvEmpReg.getText().toString()) > 0) {


                    new CreaDialogos("¿Cerrar tarima?",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new RecepcionTraspasoPalletNE.SegundoPlano("RegistraPalletNuevo").execute();
                                }
                            }, null, contexto);


                } else {
                    new popUpGenerico(contexto, null, getString(R.string.error_empaque_no_seleccionado), "false", true, true);
                }
            } else {
                new popUpGenerico(contexto, null, getString(R.string.error_empaque_no_seleccionado), "false", true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {

    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(RecepcionTraspasoPalletNE.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        View LastView;
        @Override
        protected void onPreExecute()
        {

            p.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                switch (tarea)
                {

                    case"Tabla":
                        dao = ca.c_ListarPartidasOCEnProceso("OrdenCompra");
                        break;

                    case"DetallePartida":
                        dao = ca.c_detalleReciboPartida("OrdenCompra",binding.txtvPartida.getText().toString());
                        break;

                    case "ConsultaPallet":

                        dao= ca.c_ConsultaPalletAbiertoOC("OrdenCompra", "PartidaERP");
                        break;
                    case "RegistrarEmpaqueNuevo":
                        if(!binding.toggleNumSerie.isChecked()){
                            NumSerie = binding.edtxNumSerie.getText().toString();
                        }
                        else{
                            NumSerie = ((Constructor_Dato)sp_NumSerie.getSelectedItem()).getDato();
                        }


                        CodigoEmpaque = binding.edtxPrimerEmpaque.getText().toString();
                        Cantidad = binding.edtxEmpaque.getText().toString();


                        dao = ca.c_RegistrarPalletCompra_NE("OrdenCompra",
                                binding.txtvPartida.getText().toString(),
                                "",
                                binding.txtvCantidadTotal.getText().toString(),
                                binding.edtxPrimerEmpaque.getText().toString(), NumSerie);

                        break;
                    case "RegistraPalletNuevo":

                        dao = ca.c_CierraPalletCompra(binding.txtvPallet.getText().toString());
                        break;

                    case "CerrarRecepcion":

                      //  dao = ca.c_CerrarRecepcion(OrdenCompra);

                        break;
                    default:

                        dao =new DataAccessObject();

                        break;
                }

            }catch (Exception e)
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
                if(LastView!=null)
                {
                    LastView.requestFocus();
                }
                if(dao.iscEstado())
                {

                    switch (tarea)
                    {

                        case"Tabla":
                            if(dao.getcTablas() != null)
                            {
                                CustomArrayAdapter c;
                                sp_Partidas.setAdapter(c = new CustomArrayAdapter(RecepcionTraspasoPalletNE.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));

                                sp_NumSerie.setAdapter(c = new CustomArrayAdapter(RecepcionTraspasoPalletNE.this,
                                        android.R.layout.simple_spinner_item,
                                        dao.getcTablasSorteadas("SKU","Partida","Artículo","Cant. Total")));
                            }else
                            {
                                sp_Partidas.setAdapter(null);
                            }


                            int SKUSel = -2;
                            //  SKUSel  = CustomArrayAdapter.getIndex(sp_Partidas,SKU);

                            switch(SKUSel)
                            {
                                case -2:
                             //       new popUpGenerico(contexto,edtx_SKU,"Error interno." , false, true, true);
                                    new esconderTeclado(RecepcionTraspasoPalletNE.this);
                                 //   edtx_SKU.setText("");
                                 //   edtx_SKU.requestFocus();
                                    return;
                                case -1:
                                    int UPCSel=-2;

                                    //new popUpGenerico(contexto,"edtx_SKU","No se encontro el SKU dentro del listado de partidas, verifique que sea correcto. [" + SKU +"]" , false, true, true);
                                    new esconderTeclado(RecepcionTraspasoPalletNE.this);
                                    return;
                                case -3:
                                    sp_Partidas.setSelection(0);
                                    return;
                                default:
                                    sp_Partidas.setSelection(SKUSel);
                            }

                            new esconderTeclado(RecepcionTraspasoPalletNE.this);
                           // SKU = "DEFAULT";
                            break;

                        case"DetallePartida":
                            binding.txtvCantidadTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            binding.txtvProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProducto"));

                            new esconderTeclado(RecepcionTraspasoPalletNE.this);


                            break;

                        case "RegistrarEmpaqueNuevo":
                            binding.txtvCantidadTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantEmpaques"));
                            binding.txtvCantidadTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida"));
                            CantidadRecibida = dao.getSoapObject_parced().getPrimitivePropertyAsString("CantRecibida");
                            binding.txtvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));

                            new esconderTeclado(RecepcionTraspasoPalletNE.this);
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);
//                                                new SegundoPlano("CerrarRecepcion").execute();
                                new RecepcionTraspasoPalletNE.SegundoPlano("RegistraPalletNuevo").execute();
                                //  reiniciaVariables();
                                break;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(contexto, LastView, getString(R.string.orden_compra_partida_completa), dao.iscEstado(), true, true);
                                new RecepcionTraspasoPalletNE.SegundoPlano("Tabla").execute();

                                new RecepcionTraspasoPalletNE.SegundoPlano("RegistraPalletNuevo").execute();

                                break;
                            }
                            else if ((dao.getSoapObject_parced().getPrimitivePropertyAsString("PalletCerrado").equals("1")))
                            {

                                new RecepcionTraspasoPalletNE.SegundoPlano("RegistraPalletNuevo").execute();

                            }

                            binding.edtxEmpaque.setText("");
                            binding.edtxPrimerEmpaque.setText("");
                            binding.edtxEmpaque.requestFocus();

                            break;
                        case "ConsultaPallet":

                            Log.i("DAO", dao.getSoapObject_parced().toString());
                            binding.txtvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            binding.txtvEmpReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("EmpaquesActuales"));
                            break;
                        case "RegistraPalletNuevo":
                            new popUpGenerico(contexto, LastView,"Pallet "+"["+dao.getcMensaje()+"] Cerrado con éxito",dao.iscEstado(), true, true);
                            new RecepcionTraspasoPalletNE.SegundoPlano("ConsultaPallet").execute();
                            break;


                        case "CerrarRecepcion":

                            new popUpGenerico(contexto, LastView,dao.getcMensaje(),dao.iscEstado(), true, true);

                            break;
                    }
                }
                else
                {
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                }

                p.DesactivarProgressBar(tarea);
            }catch (Exception e)
            {
                switch (tarea)
                {
                    case "RegistrarEmpaqueNuevo":
                        if (dao.getcMensaje().contains("Error SAP"))
                        {
                            CreaDialogos cd = new CreaDialogos(contexto);


                            String sourceString = "<p>Se ha presentado un error al registrar en sap.</p> <p>" + dao.getcMensaje() +"</p>  <p><b>¿Registrar de todas maneras en AXC?</b></p>";

                            cd.dialogoDefault("VALIDE LA INFORMACIÓN", Html.fromHtml(sourceString),
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            // new SegundoPlano("RegistrarEmpaqueNuevoSA").execute();
                                        }
                                    }, null);
                            break;
                        }

                    default:
                        new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(),String.valueOf(dao.iscEstado()), true, true);
                }
            }
            recargar = false;
        }
    }
}