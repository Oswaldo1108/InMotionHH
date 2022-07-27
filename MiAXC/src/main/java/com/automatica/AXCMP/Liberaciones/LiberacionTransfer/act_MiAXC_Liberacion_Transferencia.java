package com.automatica.AXCMP.Liberaciones.LiberacionTransfer;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.automatica.AXCMP.MiAXC.ConsultaTransacciones.frgmnt_ConsultaTransacciones;
import com.automatica.AXCMP.Principal.intro_fragment;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.DatePickerFragment;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.axc_lib.ClasesSerializables.Embarque;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_Partidas_Transfer;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class act_MiAXC_Liberacion_Transferencia extends AppCompatActivity
        implements frgmnt_ConsultaTransacciones.OnFragmentInteractionListener, Adaptador_RV_Partidas_Transfer.onClickRV,intro_fragment.OnFragmentInteractionListener,frgmnt_Controles_Transferencias.OnFragmentInteractionListener
        , frgmnt_Agregar_Articulos.OnFragmentInteractionListener, frgmnt_Seleccion_Almacenes.OnFragmentInteractionListener

{
    //region variables


     Context contexto = this;

    String str_Orden,str_Linea;



    //Views
//    CheckBox cb_FiltroEstacion,cb_Lote;
    Toolbar toolbar;

    EditText edtx_Documento, edtx_FiltroEstacion;

    ImageButton imgb_Buscar;
    ImageView imgv_BusquedaEstacion;
    Button btn_LiberarEmbarque;

    Spinner sp_AlmacenOrigen,sp_AlmacenDestino;

    FrameLayout progressBarHolder;
    DatePickerFragment newFragment;


    View vista;



    private static final String frgtag_Indicadores_Por_Turno = "frgtag_Indicadores_Por_Turno";
    private static final String frgtag_Almacenes = "frgtag_Almacenes";
    private static final String frgtag_Articulos = "frgtag_Articulos";

    private static final String frgtag_Controles = "frgtag_Controles";

    boolean recargar;

    //endregion

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_miaxc_liberacion_transferencia);
        declararVariables();
//        agregaListeners();

        toolbar.setSubtitle("Liberación de transferencias");

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.constraintLayout, frgmnt_ConsultaTransacciones.newInstance(null, str_Orden),frgtag_Indicadores_Por_Turno)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.controles_liberacion_transferencia, frgmnt_Controles_Transferencias.newInstance(null, str_Orden),frgtag_Controles)
                .commit();

        new esconderTeclado(act_MiAXC_Liberacion_Transferencia.this);



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void declararVariables()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(null);

//        edtx_Documento  = (EditText) findViewById(R.id.edtx_Embarque);
//        edtx_FiltroEstacion  = (EditText) findViewById(R.id.edtx_FiltroUser);
//
//        sp_AlmacenOrigen = findViewById(R.id.vw_Spinner_Almacen).findViewById(R.id.spinner);
//        sp_AlmacenDestino = findViewById(R.id.vw_Spinner_Almacen2).findViewById(R.id.spinner);
//
//
////        cb_FiltroEstacion= (CheckBox) findViewById(R.id.cb_FiltroUser);
////        cb_Lote= (CheckBox) findViewById(R.id.cb_Lote);
//        edtx_FiltroEstacion  = (EditText) findViewById(R.id.edtx_FiltroUser);
//
//        imgv_BusquedaEstacion = (ImageView) findViewById(R.id.imageView9 );
//
//
//        edtx_FiltroEstacion.setEnabled(false);
//
//        sp_AlmacenOrigen.setEnabled(false);
//        sp_AlmacenDestino.setEnabled(false);
//
//        imgv_BusquedaEstacion.setEnabled(false);
//
//        imgb_Buscar = (ImageButton) findViewById(R.id.imgb_Buscar);



        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);



    }

    private void agregaListeners()
    {


        imgb_Buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {


                        if(edtx_Documento.getText().toString().equals(""))
                            {
                                new popUpGenerico(contexto, getCurrentFocus(),"Ingrese documento de embarque.", false, true, true);
                            }


                        lanzarConsulta();


                    } catch (Exception e)
                    {
                        e.printStackTrace();
//                        lanzarConsulta();

                        new popUpGenerico(contexto, getCurrentFocus(),e.getMessage(), false, true, true);
                    }
            }
        });






        edtx_FiltroEstacion.setOnKeyListener(new View.OnKeyListener()
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




//        cb_FiltroEstacion.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(cb_FiltroEstacion.isChecked())
//                    {
//                        new SegundoPlano("ListaEstaciones").execute(null,null);
//                    }
//                else
//                    {
//                        edtx_FiltroEstacion.setText("");
//                        sp_Estacion.setAdapter(null);
//                    }
//                edtx_FiltroEstacion.setEnabled(cb_FiltroEstacion.isChecked());
//                imgv_BusquedaEstacion.setEnabled(cb_FiltroEstacion.isChecked());
//                sp_Estacion.setEnabled(cb_FiltroEstacion.isChecked());
////                Toast.makeText(contexto, "filtrouser", Toast.LENGTH_SHORT).show();
//            }
//        });








    }


    private void lanzarConsulta()
    {
            try
                {


//                    String AlmacenOrigen = null, AlmacenDestino= null, TipoAjuste = null;
//
//
//                    if (sp_AlmacenOrigen.getAdapter() != null)
//                        {
//                            AlmacenOrigen = ((CustomArrayAdapter) sp_AlmacenOrigen.getAdapter()).getSelectedExtra(sp_AlmacenOrigen.getSelectedItemPosition());
//                        }
//
//
//                    if (sp_AlmacenDestino.getAdapter() != null)
//                        {
//                            AlmacenDestino = ((CustomArrayAdapter) sp_AlmacenDestino.getAdapter()).getSelectedExtra(sp_AlmacenDestino.getSelectedItemPosition());
//                        }

                    String Documento = ((frgmnt_Controles_Transferencias)(getSupportFragmentManager().findFragmentByTag(frgtag_Controles))).getDocumentData()[0];


                    ((frgmnt_Controles_Transferencias)(getSupportFragmentManager().findFragmentByTag(frgtag_Controles))).setDocumentoArticulos(Documento);
//                    String NumParte = "@",prmLote = "@",prmTipoMovimiento = "@",TipoAjuste = "@", Desdec = "@", Hastac = "@";

                    new SegundoPlano("ConsultaDocumentos").execute(
                            Documento,
                            "@",
                            "@",
                            "@");


                }catch (Exception e)
                {
                    e.printStackTrace();
                }
    }



    @Override
    public void CrearPartida()
    {
        try
            {

                String ARTICULOS;


//                ARTICULOS = ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).getArticulosDePartidas();

                String[] Documentos = ((frgmnt_Controles_Transferencias)(getSupportFragmentManager().findFragmentByTag(frgtag_Controles))).getDocumentData();

                String[] Articulos = ((frgmnt_Controles_Transferencias)(getSupportFragmentManager().findFragmentByTag(frgtag_Controles))).getArticuloData();





                new SegundoPlano("AgregaPartida").execute(
                        Documentos[0],  //DOC
                        Documentos[1],  //ORIGEN
                        Documentos[2],  //DESTINO
//                        ARTICULOS = " ",      //ARTICULOS
                                 "art",      //ARTICULOS
                        Articulos[0],   //NumParte
                        Articulos[1],   //Lote
                        "1",            //Revision
                        Articulos[2]   //Cantidad
                );


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
                ReiniciarVariables();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {
        return false;
    }

    @Override
    public void BuscarTransfer()
    {
        lanzarConsulta();
    }

    @Override
    public void LimpiarPantalla()
    {

        try
            {
                ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_LiberacionTransferencia_Limpiar();

            }catch (Exception e)
            {
                new popUpGenerico(contexto,getCurrentFocus(), e.getMessage(), false, true, true);

                e.printStackTrace();
            }

    }

    @Override
    public String ReturnData(String[] data)
    {
        return null;
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
    public String EditarLote(int PosicionPartida, String prmProducto)
    {
        return null;
    }

    @Override
    public String EditarEstacion(int PosicionPartida, String prmEstacion)
    {
        return null;
    }

    @Override
    public String BorrarPartida(int PosicionPartida, String prmDocumento,String prmPartida)
    {

        new SegundoPlano("borrarPartida").execute(prmDocumento,prmPartida);


        return null;
    }

    @Override
    public String EditarCantidad(int PosicionPartida, String prmCantidad)
    {
        return null;
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

        cAccesoADatos ca = new cAccesoADatos(act_MiAXC_Liberacion_Transferencia.this);

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
                                        String Orden = "@",Status = "@", Desdec = "@", Hastac = "@";

                                        if(Params[0]!=null)
                                            {
                                                Orden = Params[0];
                                            }
                                        if(Params[1]!=null)
                                            {
                                                Status = Params[1];
                                                if(Status.equals(""))
                                                    Status = "@";
                                            }


                                        if(Params[2]!=null)
                                            {
                                                Desdec = Params[2];
                                            }
                                        if(Params[3]!=null)
                                            {
                                                Hastac = Params[3];
                                            }

                                        dao = ca.c_ListaOrdenesTransferenciaDetMiAXC(Orden,Status,Desdec,Hastac);

                                        break;

                                    case "AgregaPartida":

                                        dao = ca.c_AgregarLineaInterorgSalidaPT(
                                                Params[0],   //DOC
                                                Params[1],   //ORIGEN
                                                Params[2],   //DESTINO
                                                Params[3],   //ARTICULOS
                                                Params[4],   //NumParte
                                                Params[5],   //Lote
                                                Params[6],   //Revision
                                                Params[7]);  //Cantidad
                                        break;

                                    case    "borrarPartida":

                                        dao = ca.c_BajaPartidaTransferenciaMiAXC(
                                                Params[0],   //DOC
                                                Params[1]);//PARTIDA
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


                                    case    "borrarPartida":

                                        new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);



                                        lanzarConsulta();
                                        break;

                                    case "ConsultaDocumentos":


//                                        Embarque emb = new Embarque();
//
//                                        SoapObject sa = dao.getSoapObject();
//                                        sa = (SoapObject) sa.getProperty(1);
//


                                        ArrayList<Embarque> ArrayDocumentos = new ArrayList<>();

                                        for(ArrayList<Constructor_Dato> a:dao.getcTablas())
                                            {


                                                Embarque embarque = new Embarque();
                                                for(Constructor_Dato b: a)
                                                    {

                                                        switch(b.getTitulo())
                                                            {
                                                                case "Documento":
                                                                    embarque.setOrdenProd(b.getDato());
                                                                    break;
                                                                case "Partida":
                                                                    embarque.setPartida(b.getDato());
                                                                    break;
                                                                case "NumParte":
                                                                    embarque.setNumParte(b.getDato());
                                                                    break;
                                                                case "DNumParte1":
                                                                    embarque.setDNumParte1(b.getDato());
                                                                    break;
                                                                case "CantidadPedida":
                                                                    embarque.setCantidadPedida(b.getDato());
                                                                    break;
                                                                case "CantidadSurtida":
                                                                    embarque.setCantidadSurtida(b.getDato());
                                                                    break;
                                                                case "CantidadPendiente":
                                                                    embarque.setCantidadPendiente(b.getDato());
                                                                    break;
                                                                case "CantidadActual":
//                                   embarque.setCantidadActual("1");
                                                                    break;
                                                                case "DStatus":
                                                                    embarque.setDStatus(b.getDato());
                                                                    break;
                                                                case "Lote":
                                                                    embarque.setLote(b.getDato());
                                                                    break;
                                                                case "Estacion":
                                                                    embarque.setEstacion(b.getDato());
                                                                    break;
                                                                case "UnidadMedida":
                                                                    embarque.setUnidadMedida(b.getDato());
                                                                    break;
                                                                case "Tag1":
                                                                    embarque.setTag1(b.getDato());
                                                                    break;
                                                                case "Tag2":
                                                                    embarque.setTag2(b.getDato());
                                                                    break;
                                                                case "Tag3":
                                                                    embarque.setTag3(b.getDato());
                                                                    break;
                                                            }


                                                    }

                                                ArrayDocumentos.add(embarque);


                                            }


//                                        emb.setPartida( sa.getPrimitivePropertyAsString("Partida"));
//                                        emb.setNumParte( sa.getPrimitivePropertyAsString("NumParte"));
//                                        emb.setDNumParte1( sa.getPrimitivePropertyAsString("DNumParte1"));
//                                        emb.setCantidadPedida( sa.getPrimitivePropertyAsString("CantidadPedida"));
//                                        emb.setLote( sa.getPrimitivePropertyAsString("Lote"));
//                                        emb.setEstacion( sa.getPrimitivePropertyAsString("Estacion"));


                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_LiberacionTransfer(ArrayDocumentos,true);


                                        //                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_LiberacionTransfer(dao.getcTablas());
                                        break;
                                    case "AgregaPartida":


//                                        Embarque emb = new Embarque();
//
//                                        SoapObject sa = dao.getSoapObject();
//                                        sa = (SoapObject) sa.getProperty(1);
//


                                        ArrayList<Embarque> ArrayDocumentos2 = new ArrayList<>();

                                        for(ArrayList<Constructor_Dato> a:dao.getcTablas())
                                            {


                                                Embarque embarque = new Embarque();
                                                for(Constructor_Dato b: a)
                                                    {

                                                        switch(b.getTitulo())
                                                            {
                                                                case "Documento":
                                                                    embarque.setOrdenProd(b.getDato());
                                                                    break;
                                                                case "Partida":
                                                                    embarque.setPartida(b.getDato());
                                                                    break;
                                                                case "NumParte":
                                                                    embarque.setNumParte(b.getDato());
                                                                    break;
                                                                case "DNumParte1":
                                                                    embarque.setDNumParte1(b.getDato());
                                                                    break;
                                                                case "CantidadPedida":
                                                                    embarque.setCantidadPedida(b.getDato());
                                                                    break;
                                                                case "CantidadSurtida":
                                                                    embarque.setCantidadSurtida(b.getDato());
                                                                    break;
                                                                case "CantidadPendiente":
                                                                    embarque.setCantidadPendiente(b.getDato());
                                                                    break;
                                                                case "CantidadActual":
//                                   embarque.setCantidadActual("1");
                                                                    break;
                                                                case "DStatus":
                                                                    embarque.setDStatus(b.getDato());
                                                                    break;
                                                                case "Lote":
                                                                    embarque.setLote(b.getDato());
                                                                    break;
                                                                case "Estacion":
                                                                    embarque.setEstacion(b.getDato());
                                                                    break;
                                                                case "UnidadMedida":
                                                                    embarque.setUnidadMedida(b.getDato());
                                                                    break;
                                                                case "Tag1":
                                                                    embarque.setTag1(b.getDato());
                                                                    break;
                                                                case "Tag2":
                                                                    embarque.setTag2(b.getDato());
                                                                    break;
                                                                case "Tag3":
                                                                    embarque.setTag3(b.getDato());
                                                                    break;
                                                            }


                                                    }

                                                ArrayDocumentos2.add(embarque);


                                            }


//                                        emb.setPartida( sa.getPrimitivePropertyAsString("Partida"));
//                                        emb.setNumParte( sa.getPrimitivePropertyAsString("NumParte"));
//                                        emb.setDNumParte1( sa.getPrimitivePropertyAsString("DNumParte1"));
//                                        emb.setCantidadPedida( sa.getPrimitivePropertyAsString("CantidadPedida"));
//                                        emb.setLote( sa.getPrimitivePropertyAsString("Lote"));
//                                        emb.setEstacion( sa.getPrimitivePropertyAsString("Estacion"));


                                        ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_LiberacionTransfer(ArrayDocumentos2,false);
//                                        Log.i("TAG",sa.toString());


                                        break;


                                    case "ListaAlmacenes":

                                        sp_AlmacenOrigen.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_Transferencia.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Estacion","Estacion")));

                                        sp_AlmacenDestino.setAdapter(new CustomArrayAdapter(act_MiAXC_Liberacion_Transferencia.this,
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Estacion","Estacion")));



                                        break;



                                }


//                            XmlSerializer






                        }else
                        {
                            new popUpGenerico(contexto,getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);

                            if(dao.getcMensaje().contains("E-NP1")){
                                ((frgmnt_ConsultaTransacciones)(getSupportFragmentManager().findFragmentByTag(frgtag_Indicadores_Por_Turno))).UpdateData_LiberacionTransferencia_Limpiar();

                            }

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








    private void SerializarXML()
    {
        XmlSerializer serializer = Xml.newSerializer();



    }




}
