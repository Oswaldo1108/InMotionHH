package com.automatica.AXCMP.c_Surtido.Surtido_Pedidos;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Surtido_Menu_Registro extends AppCompatActivity
{
    String PartidaERP;

    ListView listaMenu;
    View vista;
    Context contexto = this;
    FrameLayout progressBarHolder;
    SoapAction sa = new SoapAction();
    boolean Recargar;
    String OrdenRecepcion;
    Bundle b = new Bundle();
    String TipoRecepcion;
    Intent tntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        getSupportActionBar().setTitle(getString(R.string.Embarques_Surtido_tipo_surtido));
        new cambiaColorStatusBar(contexto,R.color.MoradoStd, Surtido_Menu_Registro.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen =  R.drawable.icono_surt_v5_entregaaprod;
        int iconoConsultas = R.drawable.icono_surt_v5_entregaaprod;

        constructorTablaMenuPrincipal Proveedor = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.empaque),"");
        constructorTablaMenuPrincipal Maquila = new constructorTablaMenuPrincipal(iconoConsultas,getString(R.string.pallet),"");
        constructorTablaMenuPrincipal Dosificacion = new constructorTablaMenuPrincipal(iconoConsultas,getString(R.string.pedido_docificacion),"");


        lista.add(Proveedor);
        lista.add(Maquila);
        lista.add(Dosificacion);






        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                          intent = new Intent(Surtido_Menu_Registro.this, Surtido_Surtido_Empaque.class);

                        break;
                    case 1:
                          intent = new Intent(Surtido_Menu_Registro.this, Surtido_Surtido_Pallet.class);

                          break;

                    case 2:
                        //intent = new Intent(Surtido_Menu_Docificacion.this, Bascula_Pesaje.class);
                        intent = new Intent(Surtido_Menu_Registro.this, Surtido_Docificacion_Pedidos.class);


                        break;
                }



                if(intent!=null)
                {
                    intent.putExtras(getIntent().getExtras());
                    startActivity(intent);
                }
//                 tntent = intent;
//                SegundoPlano sp = new SegundoPlano("ConsultaOrdenCompra");
//                sp.execute();
            }

        });


        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Surtido_Menu_Registro.this, R.layout.menu_item, lista);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto,vista);
        }


        return super.onOptionsItemSelected(item);
    }

    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String decision,mensaje,Tarea;


        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }
        @Override
        protected void onPreExecute()
        {
            Recargar = false;

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
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            Log.d("SoapResponse", "On post");

            try {
                if (decision.equals("true")) {
                    switch (Tarea) {
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
                    b.putString("Linea",tabla1.getPrimitivePropertyAsString("CantidadEmpaque"));
                    b.putString("EmpaquesPallet",tabla1.getPrimitivePropertyAsString("EmpaquesPallet"));


                    Log.d("SoapResponse", "Paso if");
                 }



              }catch (Exception e)
            {

                e.printStackTrace();

                //new popUpGenerico(contexto,vista,e.getMessage(),decision,true,true);
            }
        }
    }


}
