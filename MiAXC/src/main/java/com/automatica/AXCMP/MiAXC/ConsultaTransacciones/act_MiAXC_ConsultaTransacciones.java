package com.automatica.AXCMP.MiAXC.ConsultaTransacciones;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.Principal.intro_fragment;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.DatePickerFragment;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.sobreDispositivo;

import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class act_MiAXC_ConsultaTransacciones extends AppCompatActivity
        implements frgmnt_ConsultaTransacciones.OnFragmentInteractionListener, Adaptador_RV_MenuPrincipal.onClickRV,intro_fragment.OnFragmentInteractionListener
{
    //region variables


     Context contexto = this;

    String str_Orden,str_Linea;



    //Views
    CheckBox cb_FiltroFecha,cb_FiltroUsuario,cb_FiltroTransaccion;
    Toolbar toolbar;
    EditText edtx_Desde,edtx_Hasta,edtx_FiltroUser,edtx_FiltroTransaccion;
    ImageButton imgb_Buscar;

    ImageView imgv_BusquedaUser,imgv_BusquedaTrans;
    Spinner sp_Usuario,sp_Transaccion;

    FrameLayout progressBarHolder;
    DatePickerFragment newFragment;


    View vista;



    private static final String frgtag_Indicadores_Por_Turno = "frgtag_Indicadores_Por_Turno";

    boolean recargar;

    //endregion

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_miaxc_transacciones);
        declararVariables();
        agregaListeners();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.constraintLayout, frgmnt_ConsultaTransacciones.newInstance(null, str_Orden),frgtag_Indicadores_Por_Turno)
                .commit();

        toolbar.setSubtitle("Consulta de transacciones");


        new esconderTeclado(act_MiAXC_ConsultaTransacciones.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(null);

        edtx_Desde  = (EditText) findViewById(R.id.edtx_Desde);
        edtx_Hasta  = (EditText) findViewById(R.id.edtx_Hasta);
        edtx_FiltroUser  = (EditText) findViewById(R.id.edtx_FiltroUser);
        edtx_FiltroTransaccion  = (EditText) findViewById(R.id.edtx_FiltroTransaccion);

        edtx_FiltroUser.setEnabled(false);
        edtx_FiltroTransaccion.setEnabled(false);


        cb_FiltroFecha= (CheckBox) findViewById(R.id.cb_FiltroFecha);
        cb_FiltroUsuario= (CheckBox) findViewById(R.id.cb_FiltroUser);
        cb_FiltroTransaccion= (CheckBox) findViewById(R.id.cb_FiltroTransaccion);


        sp_Usuario  = findViewById(R.id.vw_Spinner_user).findViewById(R.id.spinner);
        sp_Transaccion  = findViewById(R.id.vw_Spinner_transaccion).findViewById(R.id.spinner);

        imgb_Buscar = (ImageButton) findViewById(R.id.imgb_Buscar);

        imgv_BusquedaUser = (ImageView) findViewById(R.id.imgv_BusquedaUser);
        imgv_BusquedaTrans = (ImageView) findViewById(R.id.imgv_BusquedaTrans);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

    }

    private void agregaListeners()
    {

        edtx_Desde.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                        try
                            {

                                newFragment = DatePickerFragment.newInstance(
                                        new DatePickerDialog.OnDateSetListener()
                                        {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                            {
                                                final String selectedDate = day + "/" + (month + 1) + "/" + year;
                                                edtx_Desde.setText(selectedDate);
                                            }
                                        });

                                newFragment.show(getSupportFragmentManager(), "datePicker");


                        } catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }

                return;
            }});

        edtx_Hasta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {

                                newFragment = DatePickerFragment.newInstance(
                                        new DatePickerDialog.OnDateSetListener()
                                        {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                            {
                                                final String selectedDate = day + "/" + (month + 1) + "/" + year;
                                                edtx_Hasta.setText(selectedDate);
                                            }
                                        });

                                newFragment.show(getSupportFragmentManager(), "datePicker");

                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                    }

                return;
            }
        });


        imgb_Buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {

                        Date FechaDesde = null, FechaHasta = null, FechaActual = null;

                        String strFechaActual = "";
                        Calendar calendar = Calendar.getInstance();
                        FechaActual =  calendar.getTime();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                            {
//                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                                LocalDateTime now = LocalDateTime.now();
//                                strFechaActual = dtf.format(now);
//
//                                FechaActual  = new SimpleDateFormat("dd/MM/yyyy").parse(strFechaActual);
//
//                            }
//                        else
//                            {
//
//
//                            }


                        if(!edtx_Desde.getText().toString().equals("--/--/----"))
                            {
                                FechaDesde   = new SimpleDateFormat("dd/MM/yyyy").parse(edtx_Desde.getText().toString());

                            }

                        if(!edtx_Hasta.getText().toString().equals("--/--/----"))
                            {
                                FechaHasta = new SimpleDateFormat("dd/MM/yyyy").parse(edtx_Hasta.getText().toString());
                            }



                        if(FechaDesde.compareTo(FechaActual) > 0)
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"La fecha del campo [Desde] no puede ser mayor a la fecha actual. ", false, true, true);
                                return;
                            }

                        if(FechaHasta.compareTo(FechaActual) > 0)
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"La fecha del campo [Hasta] no puede ser mayor a la fecha actual. ", false, true, true);
                                return;
                            }

                        if(FechaDesde.compareTo(FechaHasta) > 0)
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"La fecha del campo [Desde] no puede ser mayor a la fecha del campo [Hasta]. ", false, true, true);
                                return;
                            }

                        lanzarConsulta();


                    } catch (Exception e)
                    {
                        e.printStackTrace();
//                        lanzarConsulta();

                        new popUpGenerico(contexto, getCurrentFocus(),"Ingrese fechas de consulta.", false, true, true);
                    }
            }
        });






        edtx_FiltroTransaccion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                }

                return false;
            }
        });




        cb_FiltroUsuario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_FiltroUsuario.isChecked())
                    {
                        new SegundoPlano("ListaUsuarios").execute(null,null);
                    }
                else
                    {
                        edtx_FiltroUser.setText("");
                        sp_Usuario.setAdapter(null);
                    }
                edtx_FiltroUser.setEnabled(cb_FiltroUsuario.isChecked());
//                Toast.makeText(contexto, "filtrouser", Toast.LENGTH_SHORT).show();
            }
        });



        cb_FiltroTransaccion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_FiltroTransaccion.isChecked())
                    {
                        new SegundoPlano("ListaTransaccion").execute(null,null);
                    }
                else
                    {
                        edtx_FiltroTransaccion.setText("");

                        sp_Transaccion.setAdapter(null);
                    }
                edtx_FiltroTransaccion.setEnabled(cb_FiltroTransaccion.isChecked());
//                Toast.makeText(contexto, "filtrotrans", Toast.LENGTH_SHORT).show();

            }
        });



        imgv_BusquedaUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_FiltroUsuario.isChecked())
                    {
                        new SegundoPlano("ListaUsuarios").execute(edtx_FiltroUser.getText().toString(),null);
                    }
            }
        });

        imgv_BusquedaTrans.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_FiltroTransaccion.isChecked())
                    {
                        new SegundoPlano("ListaTransaccion").execute(edtx_FiltroTransaccion.getText().toString());
                    }
            }
        });

    }


    private void lanzarConsulta()
    {
            try
                {


                    String user = null, Trans = null;

                    if (sp_Transaccion.getAdapter() != null)
                        {
                            Trans = ((CustomArrayAdapter) sp_Transaccion.getAdapter()).getSelectedExtra(sp_Transaccion.getSelectedItemPosition());
                        }


                    if (sp_Usuario.getAdapter() != null)
                        {
                            user = ((CustomArrayAdapter) sp_Usuario.getAdapter()).getSelectedExtra(sp_Usuario.getSelectedItemPosition());
                        }


                    new SegundoPlano("ConsultaDocumentos").execute(
                            edtx_Desde.getText().toString(),
                            edtx_Hasta.getText().toString(),
                            Trans,
                            user);


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.main_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void ReiniciarVariables()
    {
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(!recargar)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.recargar))
            {
                refresh();
            }
            if ((id == R.id.borrar_datos))
                {
                    edtx_Desde.setText("--/--/----");
                    edtx_Hasta.setText("--/--/----");
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(boolean estado)
    {
        return webmPlayer(estado);
    }

    private static final String frgtag_Loading= "FRGLOAD";

    private boolean webmPlayer(final boolean estado)
    {

        if(estado)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.constraintLayout7, intro_fragment.newInstance(null, null),frgtag_Loading)
                        .commit();
            }else
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(frgtag_Loading);
                if (fragment != null)
                    {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }

            }
        return true;
    }


    @Override
    public void refresh()
    {
        lanzarConsulta();

    }

    @Override
    public void setParametros(String Inicial, String Entradas, String Salidas, String Existencia)
    {

    }

    @Override
    public void clickBotonMasInfo(String[] datos)
    {
//        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .replace(R.id.constraintLayout7, frgmnt_Timeline_Orden_Individual.newInstance(null, null),frgtag_Timeline_Orden)
//                .commit();
    }
    @Override
    protected void onResume()
    {

    //    new SegundoPlano("ConsultaDocumentos").execute();
        super.onResume();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {

        DataAccessObject dao;
        String tarea;
        View view;

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_ConsultaTransacciones.this);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
               ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), false, true, true);
                }
        }

        @Override
        protected Void doInBackground(String...Params)
        {
            try
                {
                    if(!this.isCancelled())
                        {
                            switch (tarea)
                                {


                                    case "ConsultaDocumentos":
                                        String Usuarioc = "@", transaccionc = "@", Desdec = "@", Hastac = "@";

                                        if(Params[0]!=null)
                                            {
                                                Usuarioc = Params[0];
                                            }
                                        if(Params[1]!=null)
                                            {
                                                transaccionc = Params[1];
                                            }
                                        if(Params[2]!=null)
                                            {
                                                Desdec = Params[2];
                                            }
                                        if(Params[3]!=null)
                                            {
                                                Hastac = Params[3];
                                            }

                                        dao = ca.C_ConsultaTransacciones(Usuarioc,transaccionc,Desdec,Hastac);

                                        break;

                                    case "ListaUsuarios":

                                        String Usuario = "@", grupo = "@";

                                        if(Params[0]!=null)
                                            {
                                                Usuario = Params[0];
                                            }
                                        if(Params[1]!=null)
                                            {
                                                grupo = Params[1];
                                            }

                                        dao = ca.C_ListaUsuarios(Usuario,grupo);

                                        break;

                                    case "ListaTransaccion":

                                        String Transaccion = "@";

                                        if(Params[0]!=null)
                                            {
                                                Transaccion  = Params[0];
                                            }


                                        dao = ca.C_ListaTransaccion(Transaccion);

                                        break;

                                    default:
                                        dao = new DataAccessObject(false,"Operación no soportada",null);
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




                                    case "ConsultaDocumentos":

                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData(dao.getcTablas());

                                        break;

                                    case "ListaUsuarios":


                                        sp_Usuario.setAdapter(new CustomArrayAdapter(act_MiAXC_ConsultaTransacciones.this,
                                                                    android.R.layout.simple_spinner_item,
                                                                                            dao.getcTablasSorteadas("ItemSpinner","Usuario")));

                                        break;


                                    case "ListaTransaccion":


                                        sp_Transaccion.setAdapter(new CustomArrayAdapter(act_MiAXC_ConsultaTransacciones.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Transaccion","Transaccion")));

                                        break;



                                }
                        }else
                        {
                            new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }

                    ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            ActivaProgressBar(false);
            new popUpGenerico(contexto,getCurrentFocus(), "Cancelado", false, true, true);
            super.onCancelled();
        }
    }


}
