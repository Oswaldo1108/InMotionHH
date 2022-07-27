package com.automatica.AXCMP.Principal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;

import java.util.ArrayList;

public class Inicio_Menu extends AppCompatActivity
{
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
        toolbar.setSubtitle("  Menu Principal");


        new cambiaColorStatusBar(contexto,R.color.grisAXC,Inicio_Menu.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen =  R.drawable.icono_mov_alm_v5_mov_almacen__desktop;
        int iconoConsultas = R.drawable.icono_consultas_v5consultas_desktop;
        int iconoEmbarques = R.drawable.icono_surt_v5_surtidodesktop;
        int iconoInventarios= R.drawable.icono_inv_v5_inventarios_desktop;
        int iconoRecepcion= R.drawable.icono_rec_v5_recepciondesktop;
       constructorTablaMenuPrincipal Almacen = new constructorTablaMenuPrincipal(iconoAlmacen,"Almacen","Descripción Almacen");
       constructorTablaMenuPrincipal Consultas = new constructorTablaMenuPrincipal(iconoConsultas,"Consultas","Descripción Consultas");
       constructorTablaMenuPrincipal Embarques = new constructorTablaMenuPrincipal(iconoEmbarques,"Embarques","Descripción Embarques");
       constructorTablaMenuPrincipal Inventarios = new constructorTablaMenuPrincipal(iconoInventarios,"Inventarios","Descripción Inventarios");
        constructorTablaMenuPrincipal Recepcion = new constructorTablaMenuPrincipal(iconoRecepcion,"Recepción","Descripción Recepción");
       lista.add(Almacen);
       lista.add(Consultas);
       lista.add(Embarques);
       lista.add(Inventarios);
       lista.add(Recepcion);
        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                    //    intent = new Intent(Inicio_Menu.this, Almacen_Menu.class);
                        break;
                    case 1:
                      //  intent = new Intent(Inicio_Menu.this, Consultas_Menu.class);
                        break;
                    case 2:
                       // intent = new Intent(Inicio_Menu.this, Embarques_Menu.class);
                        break;
                    case 3:
                      //  intent = new Intent(Inicio_Menu.this, Inventarios_Menu.class);
                        break;
                }
               if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Inicio_Menu.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("¿Desea cerrar la sesión?").setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
                        SharedPreferences.Editor edit = pref.edit();

                       edit.putString("usuario","");
                        edit.apply();

                       // String usuarioPref = pref.getString("usuario", "null");
                        Inicio_Menu.super.onBackPressed();
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
            Intent i = new Intent(Inicio_Menu.this,PictureActivity.class);
            startActivity(i);
        //  new sobreDispositivo(contexto,vista);
        }


        return super.onOptionsItemSelected(item);
    }


}
