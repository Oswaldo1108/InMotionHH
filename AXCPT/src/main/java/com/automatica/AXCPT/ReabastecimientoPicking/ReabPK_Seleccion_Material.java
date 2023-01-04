package com.automatica.AXCPT.ReabastecimientoPicking;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.ReabastecimientoPicking.SinOrden.frgmnt_ControlSurtidos_SO;
import com.automatica.AXCPT.ReabastecimientoPicking.SinOrden.frgmnt_Reab_Empaques_SO;
import com.automatica.AXCPT.ReabastecimientoPicking.SinOrden.frgmnt_Reab_Pallet_SO;
import com.automatica.AXCPT.ReabastecimientoPicking.SinOrden.frgmnt_Reab_Piezas_SO;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.Adaptador_RV_MenuPrincipal;
import com.automatica.axc_lib.views.DataViewHolders.Adaptador_Reabastece;

import java.util.Arrays;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class ReabPK_Seleccion_Material extends AppCompatActivity implements  frgmnt_ListaDocumentos.OnFragmentInteractionListener,
                                                                                Adaptador_Reabastece.onClickRV,
                                                                                frgmnt_ControlSurtidos.OnFragmentInteractionListener,
                                                                                frgmnt_ControlSurtidos_SO.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Empaques.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Pallet.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Piezas.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Empaques_SO.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Pallet_SO.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Piezas_SO.OnFragmentInteractionListener,
                                                                                frgmnt_taskbar_AXC.interfazTaskbar {
    //region variables
    private static final String frgtag_Indicadores_Por_Turno = "frgtag_Indicadores_Por_Turno";
    private static final String frgtag_ControladorSurtido = "frgtag_ControladorSurtido";
    frgmnt_taskbar_AXC taskbar_axc;
    private ProgressBarHelper p;


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.reabastece_actividad);
                new cambiaColorStatusBar(ReabPK_Seleccion_Material.this, R.color.camposAzul, ReabPK_Seleccion_Material.this);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                declaraVariables();
                AgregaListeners();

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.area_trabajo, frgmnt_ListaDocumentos.newInstance(null, null), frgtag_Indicadores_Por_Turno)//.addToBackStack(frgtag_Indicadores_Por_Turno)
                        .commit();

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
                lanzaRebasteceSinOrden();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.REBASTECIMIENTO);
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
                Log.i("FM count", String.valueOf(fm.getBackStackEntryCount()));
                if (fm.getBackStackEntryCount()>0){
                    taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
                }else {
                    taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.REBASTECIMIENTO);

                }
            }

            @Override
            public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f)
            {
                super.onFragmentDestroyed(fm, f);
                try
                {
                    Log.i("FM count", String.valueOf(fm.getBackStackEntryCount()));
                    if (fm.getBackStackEntryCount() > 0)
                    {
                        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
                    } else
                    {
                        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.REBASTECIMIENTO);

                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        },true);
    }

    @Override
    protected void onResume()
    {
        //new SegundoPlano("ConsultarReabastecimientos").execute();
        super.onResume();
    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.reabastecimiento));


        p = new ProgressBarHelper(this);
    }
    private void AgregaListeners()
    {

    }
    private void ValidacionFinal()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
        switch (item.getItemId()){
            case R.id.InformacionDispositivo:
               new sobreDispositivo(ReabPK_Seleccion_Material.this, getCurrentFocus());
                break;

            case R.id.recargar:
                //new SegundoPlano("ConsultarReabastecimientos").execute();
                break;
        }
//        if (Recargar == true)
//        {
//
//
//        int id = item.getItemId();
//
//        if ((id == R.id.InformacionDispositivo)) {
//            new sobreDispositivo(contexto, vista);
//        }
//        if (id == R.id.CerrarOC && edtx_OrdenCompra.getText().toString() != "")//////////////////////////////////////
//        {
//            if (!edtx_OrdenCompra.getText().toString().equals("")) {
//
//                new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
//
//                        .setTitle("¿Cerrar Orden de Compra?").setCancelable(false)
//                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                SegundoPlano sp = new SegundoPlano("CierreOC");
//                                sp.execute();
//                            }
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
//            } else if (edtx_OrdenCompra.getText().toString().equals("")) {
//                new popUpGenerico(contexto, vista, "No hay una orden de compra seleccionada", "Advertencia", true, true);
//            }
//        }
//
//        if ((id == R.id.borrar_datos)) {
//            reiniciarDatos();
//        }
//        if ((id == R.id.recargar) && edtx_OrdenCompra.getText().toString() != null) {
//            SegundoPlano sp = new SegundoPlano("Tabla");
//            sp.execute();
//        }
//
//    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void destruir() {
        Log.i("SE lanzo","ok");
        try {
            taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.REBASTECIMIENTO);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        try
        {
            p = new ProgressBarHelper(this);
            if (estado)
                {
                    //ENTRADA
                    p.ActivarProgressBar();
                } else
                {
                    //SALIDA
                    p.DesactivarProgressBar();
                }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void ActualizarDocumento()
    {
        ((frgmnt_ControlSurtidos) (getSupportFragmentManager().findFragmentByTag(frgtag_ControladorSurtido))).ActualizarDocumento();

    }

    @Override
    public String ReturnData(String[] data)
    {
        return null;
    }

    @Override
    public void longclick(String dato, int tipo) {

        String [] datos = {dato};
        taskbar_axc.abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos,tipo),"fragmentoConsulta");
    }

    @Override
    public boolean ActivaProgressBar(boolean estado)
    {
        return false;
    }

    @Override
    public void refresh()
    {
        //new SegundoPlano("ConsultarReabastecimientos").execute();
    }

    @Override
    public void setParametros(String Inicial, String Entradas, String Salidas, String Existencia)
    {

    }

    @Override
    public void lanzaRebasteceSinOrden()
    {
        Fragment f = getSupportFragmentManager().findFragmentByTag(frgtag_ControladorSurtido);

        if(f == null)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                        .add(R.id.area_trabajo, frgmnt_ControlSurtidos_SO.newInstance(null, null), frgtag_ControladorSurtido).addToBackStack(frgtag_ControladorSurtido)
                        .commit();
                //taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
            }else
            {

            }
    }

    @Override
    public void clickBotonMasInfo(String[] datos)
    {

        Fragment f = getSupportFragmentManager().findFragmentByTag(frgtag_ControladorSurtido);

        if(f == null)
            {
                Log.i("Datos", Arrays.toString(datos));
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_right_in_enter, R.anim.slide_out_left, R.anim.slide_right_in_enter, R.anim.slide_out_left)
                        .add(R.id.area_trabajo, frgmnt_ControlSurtidos.newInstance(datos[0], datos[1]), frgtag_ControladorSurtido).addToBackStack(frgtag_ControladorSurtido)
                        .commit();
            }else
            {

            }

    }

    @Override
    public void BotonDerecha() {
        lanzaRebasteceSinOrden();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }


    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        cAccesoADatos ca = new cAccesoADatos(ReabPK_Seleccion_Material.this);
        DataAccessObject dao;
        String tarea;
        View view;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try
                {
                p.ActivarProgressBar(tarea);

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.axc_lib.Servicios.popUpGenerico(ReabPK_Seleccion_Material.this, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    if(!this.isCancelled())
                        {


                            switch (tarea)
                                {

                                    case "ConsultarReabastecimientos":
                                        dao = ca.C_ConsultaOrdenesReabastecimiento();
                                        break;


                                    case "AgregarPedido":
//                                        dao = ca.C_AgregaPedidoCM(AgregarAOrdenar);
                                        break;



                                    default:
                                        dao = new DataAccessObject(false,"Operación no soportada. ["+ tarea+ "]",null);
                                        break;
                                }
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    dao = new DataAccessObject(false,e.getMessage(),null);
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

                                    case "ConsultarReabastecimientos":
                                        if (dao.getcTablas()!=null){
                                            ((frgmnt_ListaDocumentos) (getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Reab(dao.getcTablas());

                                        }else {
                                            ((frgmnt_ListaDocumentos) (getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Reab(null);

                                        }

                                        break;

                                    case "AgregarPedido":

//                                        frgmnt_Menu_Inicio fmi = (frgmnt_Menu_Inicio) getSupportFragmentManager().findFragmentByTag(frgtag_Menu_Inicio);
//                                        fmi.RecargarDatos();

                                        break;

                                }
                        }else
                        {
                            new com.automatica.axc_lib.Servicios.popUpGenerico(ReabPK_Seleccion_Material.this, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.axc_lib.Servicios.popUpGenerico(ReabPK_Seleccion_Material.this, getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }
            p.DesactivarProgressBar(tarea);
            //      mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            // mListener.ActivaProgressBar(false);
            super.onCancelled();
        }
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

        /*if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }*/
        Intent intent = new Intent(ReabPK_Seleccion_Material.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}
