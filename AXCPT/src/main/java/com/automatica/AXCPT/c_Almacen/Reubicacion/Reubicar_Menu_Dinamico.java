package com.automatica.AXCPT.c_Almacen.Reubicacion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.automatica.AXCPT.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCPT.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCPT.R;
import com.automatica.axc_lib.Servicios.cambiaColorStatusBar;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


/*
* Para agregar opciones nuevas al menu dinamico, se debe agregar una entrada al arrayList de Intents llamado "ActionsList"
* Y una  entrada a el Array list de constructor Tabla menu principal
* Se debe respetar el orden en que se agrega, procurar agregarlo al final para no revolver las entradas
*
* */


public class Reubicar_Menu_Dinamico extends AppCompatActivity
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
        getSupportActionBar().setTitle("Reubicación");

        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Reubicar_Menu_Dinamico.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();

        ArrayList<String> controlMenu = new ArrayList<>();
        ArrayList<constructorTablaMenuPrincipal> ct = new ArrayList<>();


        ArrayList<Intent> actionList = new ArrayList<>();

        actionList.add(new Intent(Reubicar_Menu_Dinamico.this, Almacen_Reubicar.class));
        actionList.add(new Intent(Reubicar_Menu_Dinamico.this, Almacen_Reubicacion_Por_Cantidad.class));
//        actionList.add(new Intent(Reubicar_Menu_Dinamico.this, Almacen_Reubicacion_Contenedor.class));




        final ArrayList<Intent> actionListtoExecute = new ArrayList<>();


        int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_consolidacion_tarimas;
        int iconoAlmacen21 = R.drawable.icono_mov_alm_v5_reubicacion;


        constructorTablaMenuPrincipal Reubicacion_NE = new constructorTablaMenuPrincipal(iconoAlmacen2,"Reubicación de tarimas","Descripción Ajustes");
        constructorTablaMenuPrincipal Reubicacion_Activo= new constructorTablaMenuPrincipal(iconoAlmacen2,"Reubicación a granel","Descripción Ajustes");
//        constructorTablaMenuPrincipal Reubicacion_Contenedor= new constructorTablaMenuPrincipal(iconoAlmacen2,getString(R.string.reubicacion_contenedor),"Descripción Ajustes");

        ct.add(Reubicacion_NE);
        ct.add(Reubicacion_Activo);
//        ct.add(Reubicacion_Contenedor);


        String Apertura = getAperturaSharedPreferences(contexto); // b.getString("Apertura");
        String[] SeparadoApertura = Apertura.split(","); //

        for(int i = 0;i <SeparadoApertura[1].length();i++)
        {
   //         Toast.makeText( this, SeparadoApertura[1], Toast.LENGTH_SHORT).show();
            controlMenu.add(Character.toString(SeparadoApertura[1].charAt(i)));
            Log.i(TAG, "onCreate: Creando Menu Principal, el código recibido fue:" + SeparadoApertura[1].charAt(i));

            if(controlMenu.get(i).equals("1")&&controlMenu.size()<=ct.size())
            {
                lista.add(ct.get(i));
                actionListtoExecute.add(actionList.get(i));
            }else
            {
                Log.e(TAG, "onCreate: Error Creando Menu Principal, el código recibido fue:" + SeparadoApertura[0] );
            }
        }


        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                intent = actionListtoExecute.get(position);
               if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Reubicar_Menu_Dinamico.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
    }
    public String getAperturaSharedPreferences(Context contexto)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        String aperturaPref = pref.getString("apertura", "00000");
        return aperturaPref;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
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



}
