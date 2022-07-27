package com.automatica.AXCMP.MiAXC.Kardex;

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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.automatica.AXCMP.MiAXC.ConsultaTransacciones.frgmnt_ConsultaTransacciones;
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

public class act_MiAXC_Kardex extends AppCompatActivity
        implements frgmnt_ConsultaTransacciones.OnFragmentInteractionListener, Adaptador_RV_MenuPrincipal.onClickRV,intro_fragment.OnFragmentInteractionListener
{
    //region variables


     Context contexto = this;

    String str_Orden,str_Linea;



    //Views
    CheckBox cb_FiltroFecha,cb_FiltroUsuario, cb_TipoConsulta,cb_Lote,cb_FiltroAjuste;
    Switch swtch_TipoConsulta;
    Toolbar toolbar;

    EditText edtx_Desde, edtx_Hasta, edtx_FiltroArticulo,edtx_TipoMovimiento,edtx_Lote;

    ImageButton imgb_Buscar;

    ImageView imgv_BusquedaUser;

    Spinner sp_Articulo, sp_TipoAjuste;

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
        setContentView(R.layout.act_miaxc_kardex);
        declararVariables();
        agregaListeners();

        toolbar.setSubtitle("Kardex");

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.constraintLayout, frgmnt_ConsultaTransacciones.newInstance(null, str_Orden),frgtag_Indicadores_Por_Turno)
                .commit();


        new esconderTeclado(act_MiAXC_Kardex.this);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(null);

        edtx_Desde  = (EditText) findViewById(R.id.edtx_Desde);
        edtx_Hasta  = (EditText) findViewById(R.id.edtx_Hasta);
        edtx_FiltroArticulo = (EditText) findViewById(R.id.edtx_FiltroUser);
        edtx_TipoMovimiento = (EditText) findViewById(R.id.edtx_TipoConsulta);
//        edtx_TipoAjuste = (EditText) findViewById(R.id.edtx_TipoConsulta);
        edtx_Lote  = (EditText) findViewById(R.id.edtx_Lote);

        edtx_Lote.setEnabled(false);
        edtx_FiltroArticulo.setEnabled(false);
        edtx_TipoMovimiento.setEnabled(false);


        cb_FiltroFecha= (CheckBox) findViewById(R.id.cb_FiltroFecha);
        cb_FiltroUsuario= (CheckBox) findViewById(R.id.cb_FiltroUser);
        cb_TipoConsulta = (CheckBox) findViewById(R.id.cb_FiltroTransaccion);
        cb_Lote = (CheckBox) findViewById(R.id.cb_Lote);
        cb_FiltroAjuste = (CheckBox) findViewById(R.id.cb_FiltroAjuste);
        sp_Articulo = findViewById(R.id.vw_Spinner_user).findViewById(R.id.spinner);
        sp_TipoAjuste = findViewById(R.id.vw_Spinner_TipoAjuste).findViewById(R.id.spinner);
        imgb_Buscar = (ImageButton) findViewById(R.id.imgb_Buscar);



        imgv_BusquedaUser = (ImageView) findViewById(R.id.imgv_BusquedaUser);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);


        swtch_TipoConsulta = (Switch) findViewById(R.id.swtch_TipoMovimientos);
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


                        Calendar calendar = Calendar.getInstance();
                        FechaActual =  calendar.getTime();

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






        edtx_TipoMovimiento.setOnKeyListener(new View.OnKeyListener()
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
                        new SegundoPlano("ListaProductos").execute(null,null);
                    }
                else
                    {
                        edtx_FiltroArticulo.setText("");
                        sp_Articulo.setAdapter(null);
                    }
                edtx_FiltroArticulo.setEnabled(cb_FiltroUsuario.isChecked());
//                Toast.makeText(contexto, "filtrouser", Toast.LENGTH_SHORT).show();
            }
        });

        cb_FiltroAjuste.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_FiltroAjuste.isChecked())
                    {
                        new SegundoPlano("ListaAjustes").execute(null,null);
                    }
                else
                    {
//                        edtx_FiltroArticulo.setText("");
                        sp_TipoAjuste.setAdapter(null);
                    }
//                edtx_FiltroArticulo.setEnabled(cb_FiltroUsuario.isChecked());
//                Toast.makeText(contexto, "filtrouser", Toast.LENGTH_SHORT).show();
            }
        });


        cb_TipoConsulta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_TipoConsulta.isChecked())
                    {

                        edtx_TipoMovimiento.setText("");
                        swtch_TipoConsulta.setEnabled(true);
                        swtch_TipoConsulta.setChecked(false);
                        edtx_TipoMovimiento.setText("ENTRADA");
                    }
                else
                    {
                        swtch_TipoConsulta.setEnabled(false);
                        edtx_TipoMovimiento.setText("TODOS");
                    }
                edtx_TipoMovimiento.setEnabled(false);
//                Toast.makeText(contexto, "filtrotrans", Toast.LENGTH_SHORT).show();

            }
        });


        cb_Lote.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_TipoConsulta.isChecked())
                    {
                        edtx_Lote.setEnabled(true);
                        edtx_Lote.setText("");

                    }
                else
                    {
                        edtx_Lote.setEnabled(false);
                        edtx_Lote.setText("");
                    }
//                edtx_TipoMovimiento.setEnabled(false);
//                Toast.makeText(contexto, "filtro lote", Toast.LENGTH_SHORT).show();

            }
        });

        swtch_TipoConsulta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                    {
                        edtx_TipoMovimiento.setText("SALIDA");

                    }
                else
                    {
                        edtx_TipoMovimiento.setText("ENTRADA");

                    }
            }
        });

        imgv_BusquedaUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cb_FiltroUsuario.isChecked())
                    {
                        new SegundoPlano("ListaProductos").execute(edtx_FiltroArticulo.getText().toString(),null);
                    }
            }
        });


    }


    private void lanzarConsulta()
    {
            try
                {


                    String numParte= null, TipoAjuste = null;


                    if (sp_Articulo.getAdapter() != null)
                        {
                            numParte = ((CustomArrayAdapter) sp_Articulo.getAdapter()).getSelectedExtra(sp_Articulo.getSelectedItemPosition());
                        }

                    if (sp_TipoAjuste.getAdapter() != null)
                        {
                            TipoAjuste = ((CustomArrayAdapter) sp_TipoAjuste.getAdapter()).getSelectedExtra(sp_TipoAjuste.getSelectedItemPosition());
                        }


//                    String NumParte = "@",prmLote = "@",prmTipoMovimiento = "@",TipoAjuste = "@", Desdec = "@", Hastac = "@";

                    new SegundoPlano("ConsultaDocumentos").execute(
                            numParte,
                            edtx_Lote.getText().toString(),
                            edtx_TipoMovimiento.getText().toString(),
                            TipoAjuste,
                            edtx_Desde.getText().toString(),
                            edtx_Hasta.getText().toString());


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
    public void setParametros(String Existencia, String Inicial, String Salidas, String Entradas)
    {
        ((EditText)findViewById(R.id.edtx_Existencia)).setText(Existencia);
        ((EditText)findViewById(R.id.edtx_Inicial)).setText(Inicial);
        ((EditText)findViewById(R.id.edtx_Salidas)).setText(Salidas);
        ((EditText)findViewById(R.id.edtx_Entradas)).setText(Entradas);

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

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_Kardex.this);

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
                                        String NumParte = "@",prmLote = "@",prmTipoMovimiento = "@",TipoAjuste = "@", Desdec = "@", Hastac = "@";

                                        if(Params[0]!=null)
                                            {
                                                NumParte = Params[0];
                                            }
                                        if(Params[1]!=null)
                                            {
                                                prmLote = Params[1];
                                                if(prmLote.equals(""))
                                                    prmLote = "@";
                                            }
                                        if(Params[2]!=null)
                                            {
                                                prmTipoMovimiento = Params[2];
                                                if(prmTipoMovimiento.equals("TODOS"))
                                                    prmTipoMovimiento = "@";
                                                if(prmTipoMovimiento.equals("ENTRADA"))
                                                    prmTipoMovimiento = "E";
                                                if(prmTipoMovimiento.equals("SALIDA"))
                                                    prmTipoMovimiento = "S";
                                            }
                                        if(Params[3]!=null)
                                            {
                                                TipoAjuste = Params[3];
                                            }
                                        if(Params[4]!=null)
                                            {
                                                Desdec = Params[4];
                                            }
                                        if(Params[5]!=null)
                                            {
                                                Hastac = Params[5];
                                            }

                                        dao = ca.C_ConsultaKardex(NumParte,prmLote,prmTipoMovimiento,TipoAjuste,Desdec,Hastac);

                                        break;

                                    case "ListaProductos":

                                        String Producto = "@";

                                        if(Params[0]!=null)
                                            {
                                                Producto = Params[0];
                                            }

                                        dao = ca.C_ListaProducto(Producto);

                                        break;

                                    case "ListaAjustes":

                                        String Ajuste = "@",prmTipo = "@";

                                        if(Params[0]!=null)
                                            {
                                                Ajuste  = Params[0];
                                            }

                                        if(Params[1]!=null)
                                            {
                                                prmTipo  = Params[1];
                                            }

                                        dao = ca.C_ListaAjustes(Ajuste,prmTipo);

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

                                    case "ListaProductos":


                                        sp_Articulo.setAdapter(new CustomArrayAdapter(act_MiAXC_Kardex.this,
                                                                    android.R.layout.simple_spinner_item,
                                                                                            dao.getcTablasSorteadas("ItemSpinner","Producto")));

                                        break;


                                    case "ListaAjustes":

                                        sp_TipoAjuste.setAdapter(new CustomArrayAdapter(act_MiAXC_Kardex.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Tipo","Tipo")));

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
