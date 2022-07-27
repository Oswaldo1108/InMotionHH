package com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking.SinOrden.frgmnt_ControlSurtidos_SO;
import com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking.SinOrden.frgmnt_Reab_Empaques_SO;
import com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking.SinOrden.frgmnt_Reab_Pallet_SO;
import com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking.SinOrden.frgmnt_Reab_Piezas_SO;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_MenuPrincipal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class ReabPK_Seleccion_Material extends AppCompatActivity implements  frgmnt_ListaDocumentos.OnFragmentInteractionListener,
                                                                                Adaptador_RV_MenuPrincipal.onClickRV,
                                                                                frgmnt_ControlSurtidos.OnFragmentInteractionListener,
                                                                                frgmnt_ControlSurtidos_SO.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Empaques.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Pallet.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Piezas.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Empaques_SO.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Pallet_SO.OnFragmentInteractionListener,
                                                                                frgmnt_Reab_Piezas_SO.OnFragmentInteractionListener


{
    //region variables
    private static final String frgtag_Indicadores_Por_Turno = "frgtag_Indicadores_Por_Turno";
    private static final String frgtag_ControladorSurtido = "frgtag_ControladorSurtido";
    FrameLayout progressBarHolder ;
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



            }catch (Exception e)
            {
                e.printStackTrace();
            }

    }

    @Override
    protected void onResume()
    {

        new SegundoPlano("ConsultarReabastecimientos").execute();

        super.onResume();
    }
    private void declaraVariables()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.reabastecimiento));

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

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
    public void onBackPressed()
    {

        super.onBackPressed();
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
            AlphaAnimation inAnimation;
            if (progressBarHolder == null)
                {
                    progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
                }
            AlphaAnimation outAnimation;
            if (estado)
                {
                    //ENTRADA
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    progressBarHolder.setAnimation(inAnimation);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    progressBarHolder.requestFocus();
                } else
                {
                    //SALIDA
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);
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
    public boolean ActivaProgressBar(boolean estado)
    {
        return false;
    }

    @Override
    public void refresh()
    {


        new SegundoPlano("ConsultarReabastecimientos").execute();

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
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .add(R.id.area_trabajo, frgmnt_ControlSurtidos_SO.newInstance(null, null), frgtag_ControladorSurtido).addToBackStack(frgtag_ControladorSurtido)
                        .commit();
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
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left, R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .add(R.id.area_trabajo, frgmnt_ControlSurtidos.newInstance(datos[0], datos[1]), frgtag_ControladorSurtido).addToBackStack(frgtag_ControladorSurtido)
                        .commit();
            }else
            {

            }

    }



    private class SegundoPlano extends AsyncTask<Void,Void,Void>
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
//                mListener.ActivaProgressBar(true);

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.axc_lib.Servicios.popUpGenerico(ReabPK_Seleccion_Material.this, getCurrentFocus(), e.getMessage(), false, true, true);
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
                                        ((frgmnt_ListaDocumentos) (getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_Reab(dao.getcTablas());

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
            //      mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            // mListener.ActivaProgressBar(false);
            super.onCancelled();
        }
    }













}
