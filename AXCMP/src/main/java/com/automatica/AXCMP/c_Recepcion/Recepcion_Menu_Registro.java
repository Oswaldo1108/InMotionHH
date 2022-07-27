package com.automatica.AXCMP.c_Recepcion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCMP.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCMP.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class Recepcion_Menu_Registro extends AppCompatActivity
{
    String PartidaERP,NumParte,UM,CantidadTotal,CantidadPendiente,CantidadRecibida,CantidadEmpaques,EmpaquesPallet,Muestreo,Estatus;

    ListView listaMenu;
    View vista;
    Context contexto = this;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    SoapAction sa = new SoapAction();
    boolean Recargar;
    String Partida;
    String OrdenRecepcion;
    String IdRecepcion, ModificaCant;
    Bundle b = new Bundle();

    String TipoRecepcion;
    Intent tntent;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        getSupportActionBar().setTitle(getString(R.string.recepcion_tipo_recepcion));
        new cambiaColorStatusBar(contexto,R.color.VerdeStd,Recepcion_Menu_Registro.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen =  R.drawable.icono_rec_v5_registro_empaque;
        int iconoConsultas = R.drawable.icono_rec_v5_registro_prim_ult;

        constructorTablaMenuPrincipal Proveedor = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.recepcion_por_empaque),"Descripción Empaque");
        constructorTablaMenuPrincipal Maquila = new constructorTablaMenuPrincipal(iconoConsultas,getString(R.string.recepcion_primera_y_ultima),"Descripción Primera y Ultima");

        lista.add(Proveedor);
        lista.add(Maquila);

        b = getIntent().getExtras();

        OrdenRecepcion = b.getString("Orden");
        PartidaERP = b.getString("PartidaERP");
        TipoRecepcion = b.getString("Tipo");

        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                          intent = new Intent(Recepcion_Menu_Registro.this, Recepcion_Registro_PorEmpaque_Spinner.class);


                        //  b.putString("Tipo", "Proveedor");
                        break;
                    case 1:
                          intent = new Intent(Recepcion_Menu_Registro.this, Recepcion_Registro_PrimerasYUltimas_Spinner.class);
                          //b.putString("Tipo", "Maquila");
                          break;
                }


               tntent = intent;
                SegundoPlano sp = new SegundoPlano("ConsultaOrdenCompra");
                sp.execute();
            }

        });


        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Recepcion_Menu_Registro.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
    }


    @Override
    protected void onResume()
    {

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(Recargar)
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String decision,mensaje,Tarea;
        View  LastView;

        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {

            try {


                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LastView = getCurrentFocus();
                        progressBarHolder.requestFocus();
                        Recargar = false;
                    }
                }, 10);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
            }


        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {

                switch (Tarea)
                {
                    case"ConsultaOrdenCompra":
                        sa.SOAPListarPartidasOCLiberadas(OrdenRecepcion,contexto);
                        break;



                }

                decision = sa.getDecision();
                mensaje = sa.getMensaje();
                if(decision.equals("true")&&Tarea.equals("ConsultaOrdenCompra"))
                {
                    sacaDatos();


                }
            }catch (Exception e)
            {
                Log.e("RegistroOC", "doInBackground: " + e.getMessage() );
                decision = "false";
                mensaje = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            Log.d("SoapResponse", "On post");

            try {
                if (decision.equals("true"))
                {
                    switch (Tarea)
                    {
                        case "ConsultaOrdenCompra":
                            Log.d("SoapResponse", "On post switch");

                            if(tntent!=null) {
                                b.putString("Tipo", TipoRecepcion);

                                tntent.putExtras(b);
                                startActivity(tntent);
                            }


                            break;
                    }
                }

                if (decision.equals("false"))
                {
                    new popUpGenerico(contexto, vista, mensaje, "false", true, true);

                }
            }catch (Exception e)
            {
                new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
                Log.e("RegistroOC", "doInBackground: " + e.getMessage() );
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            Recargar = true;
        }
        @Override
        protected void onCancelled()
        {
            Log.e("SP", "onCancelled: hilo terminado" );
            super.onCancelled();
        }

    }
    public void sacaDatos()
    {
        SoapObject tabla = sa.parser();


        for(int i = 0; i<tabla.getPropertyCount();i++)
        {
            try {

                SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                Log.d("SoapResponse", tabla1.toString());
                String Partida = tabla1.getPrimitivePropertyAsString("PartidaERP");

                Log.d("SoapResponse", PartidaERP + " " + Partida);
                if(PartidaERP.equals(Partida))
                {
                    b.putString("IdRecepcion",tabla1.getPrimitivePropertyAsString("IdRecepcion"));
                    b.putString("PartidaERP",tabla1.getPrimitivePropertyAsString("PartidaERP"));
                    b.putString("NumParte",tabla1.getPrimitivePropertyAsString("NumParte"));
                    b.putString("UM",tabla1.getPrimitivePropertyAsString("UM"));
                    b.putString("CantidadTotal",tabla1.getPrimitivePropertyAsString("CantidadPendienteTotal"));
                    b.putString("CantidadRecibida",tabla1.getPrimitivePropertyAsString("CantidadRecibida"));
                    b.putString("CantidadEmpaques",tabla1.getPrimitivePropertyAsString("CantidadEmpaque"));
                    b.putString("EmpaquesPallet",tabla1.getPrimitivePropertyAsString("EmpaquesPallet"));


                    Log.d("SoapResponse", "Paso if");

                                       /*

                   //     CantidadPendiente = tabla1.getPrimitivePropertyAsString("CantidadPendiente");
                    Muestreo = tabla1.getPrimitivePropertyAsString("Muestreo");
                    Estatus = tabla1.getPrimitivePropertyAsString("Estatus");
                    ModificaCant = tabla1.getPrimitivePropertyAsString("ModificaCant");


                     b.putString("ModificaCant",ModificaCant);
              // b.putString("PartidaERP",clickedData[0]);
              //  b.putString("IdRecepcion",IdRecepcion);
               // b.putString("NumParte",clickedData[1]);
            //    b.putString("UM",clickedData[2]);
              //  b.putString("CantidadTotal",clickedData[3]);

              //  b.putString("CantidadRecibida",clickedData[5]);
              //  b.putString("CantidadEmpaques",clickedData[6]);
                //b.putString("EmpaquesPallet",clickedData[7]);

                */

                }



              }catch (Exception e)
            {

                e.printStackTrace();

                //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
            }
        }
    }


}
