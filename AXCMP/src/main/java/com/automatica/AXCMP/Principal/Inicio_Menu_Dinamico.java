package com.automatica.AXCMP.Principal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking.ReabPK_Seleccion_Material;
import com.automatica.AXCMP.Prueba_PENTAGONO;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.c_Carrito_Surtido.act_Surt_Carrito;
import com.automatica.axc_lib.Activities.cIncidencia.Incidencia;
import com.automatica.AXCMP.c_Almacen.Almacen_Menu_Dinamico;
import com.automatica.AXCMP.c_Consultas.Consultas_Menu_Dinamico;
import com.automatica.AXCMP.c_Inventarios.Inventarios_Menu;
import com.automatica.AXCMP.c_Recepcion.Recepcion_Menu_Tipo_Recepcion;
import com.automatica.AXCMP.c_Surtido.Surtido_Menu_Dinamico;
import com.automatica.axc_lib.Activities.cIncidencia.fragmentoIncidencia;

import java.util.ArrayList;


/*
* Para agregar opciones nuevas al menu dinamico, se debe agregar una entrada al arrayList de Intents llamado "ActionsList"
* Y una entrada a el Array list de constructor Tabla menu principal
* Se debe respetar el orden en que se agrega, procurar agregarlo al final para no revolver las entradas
*
* */



public class Inicio_Menu_Dinamico extends AppCompatActivity implements fragmentoIncidencia.OnFragmentInteractionListener
{
    String TAG = "InicioMenu";
    ListView listaMenu;
    View vista;
    Context contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_principal));

        new cambiaColorStatusBar(contexto, R.color.grisAXC, Inicio_Menu_Dinamico.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        Bundle b = getIntent().getExtras();


        ArrayList<String> controlMenu = new ArrayList<>();
        ArrayList<constructorTablaMenuPrincipal> ct = new ArrayList<>();


        ArrayList<Intent> actionList = new ArrayList<>();
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, Recepcion_Menu_Tipo_Recepcion.class));//Recepcion
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, Almacen_Menu_Dinamico.class));//Almacen
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, Consultas_Menu_Dinamico.class));//Consultas
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, Surtido_Menu_Dinamico.class));
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, act_Surt_Carrito.class));
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, Inventarios_Menu.class));//Inventarios
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, Incidencia.class));//Inventarios
        actionList.add(new Intent(Inicio_Menu_Dinamico.this, ReabPK_Seleccion_Material.class));//Inventarios


        final ArrayList<Intent> actionListtoExecute = new ArrayList<>();
        int iconoAlmacen =  R.drawable.icono_mov_alm_v5_mov_almacen__desktop;
        int iconoConsultas = R.drawable.icono_consultas_v5consultas_desktop;
        int iconoSurtido= R.drawable.icono_surt_v5_surtidodesktop;
        int iconoInventarios= R.drawable.icono_inv_v5_inventarios_desktop;
        int iconoRecepcion= R.drawable.icono_rec_v5_recepciondesktop;


        constructorTablaMenuPrincipal Recepcion = new constructorTablaMenuPrincipal(iconoRecepcion,getString(R.string.menu_recepcion),"Descripción Recepción");
        constructorTablaMenuPrincipal Almacen = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.menu_almacen),"Descripción Almacen");
        constructorTablaMenuPrincipal Consultas = new constructorTablaMenuPrincipal(iconoConsultas,getString(R.string.menu_consultas),"Descripción Consultas");
        constructorTablaMenuPrincipal Surtido = new constructorTablaMenuPrincipal(iconoSurtido,getString(R.string.menu_embarques),"Descripción Embarques");
        constructorTablaMenuPrincipal Surtido_AXC_Karts = new constructorTablaMenuPrincipal(iconoSurtido,"Cart Moving","Descripción Embarques");
         constructorTablaMenuPrincipal Inventarios = new constructorTablaMenuPrincipal(iconoInventarios,getString(R.string.menu_inventarios),"Descripción Inventarios");
        constructorTablaMenuPrincipal Incidencia = new constructorTablaMenuPrincipal(iconoInventarios,getString(R.string.menu_inventarios),"Descripción Inventarios");
        constructorTablaMenuPrincipal prbPent= new constructorTablaMenuPrincipal(iconoInventarios,"Reabastecimiento","Descripción Inventarios");

        ct.add(Recepcion);
        ct.add(Almacen);
        ct.add(Consultas);
        ct.add(Surtido);
        ct.add(Surtido_AXC_Karts);
        ct.add(Inventarios);
        ct.add(Incidencia);
        ct.add(prbPent);


        String Apertura = getAperturaSharedPreferences(contexto); // b.getString("Apertura");
        String[] SeparadoApertura = Apertura.split(","); //

        for(int i = 0;i <SeparadoApertura[0].length();i++)// la posicion en SeparadoApertura es de donde se saca el permiso de cada modulo, aqui se saca del menu principal
        {
        //    Toast.makeText( this, SeparadoApertura[0], Toast.LENGTH_SHORT).show();
            controlMenu.add(Character.toString(SeparadoApertura[0].charAt(i)));
            Log.e(TAG, "onCreate: Error Creando Menu Principal, el código recibido fue:" + SeparadoApertura[0].charAt(i));
            if(controlMenu.get(i).equals("1")&&controlMenu.size()<=ct.size())
            {
            lista.add(ct.get(i));
            actionListtoExecute.add(actionList.get(i));
            }else
            {
               // Log.e(TAG, "onCreate: Error Creando Menu Principal, el código recibido fue:" + SeparadoApertura[i] );
            }
        }


        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

           //    if(position==6)
                   {
//                       getSupportFragmentManager().beginTransaction()
//                               .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                               .add(R.id.constraintLayout, fragmentoIncidencia.newInstance("",""))
//                               .commit();

                   }
//                else
//                    {
                        Intent intent = null;

                        intent = actionListtoExecute.get(position);

                        if(intent!=null)startActivity(intent);
//
//                    }
            }
        });
        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Inicio_Menu_Dinamico.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
    }
    public String getAperturaSharedPreferences(Context contexto)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        String aperturaPref = pref.getString("apertura", "00000");
        return aperturaPref;
    }
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_black_24dp)

                .setTitle("¿Desea cerrar la sesión?").setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
                        SharedPreferences.Editor edit = pref.edit();

                       edit.putString("usuario","--");
                        edit.apply();

                       // String usuarioPref = pref.getString("usuario", "null");
                        Inicio_Menu_Dinamico.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        //   super.onBackPressed();
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

        if((id == R.id.InformacionDispositivo))
        {
            Intent i = new Intent(Inicio_Menu_Dinamico.this,PictureActivity.class);
            startActivity(i);
            //     new sobreDispositivo(contexto,vista);
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
        return false;
    }
}
