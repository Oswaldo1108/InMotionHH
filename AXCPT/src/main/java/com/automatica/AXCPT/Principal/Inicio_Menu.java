package com.automatica.AXCPT.Principal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Menu;
import com.automatica.AXCPT.c_Consultas.Consultas_Menu;
import com.automatica.AXCPT.c_Embarques.Embarques_Menu;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_Menu;

import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Recepcion_Menu_Tipo_Recepcion;

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
        this.getSupportActionBar().setTitle(" "+getString(R.string.menu_principal));
//        toolbar.setLogo(R.mipmap.logo_axc);
        new cambiaColorStatusBar(contexto,R.color.grisAXC,Inicio_Menu.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen =  R.drawable.icono_mov_alm_v5_mov_almacen__desktop;
        int iconoConsultas = R.drawable.icono_consultas_v5consultas_desktop;
        int iconoEmbarques = R.drawable.icono_surt_v5_surtidodesktop;
        int iconoInventarios= R.drawable.icono_inv_v5_inventarios_desktop;
        constructorTablaMenuPrincipal RegistroProductoAlmacen= new constructorTablaMenuPrincipal(iconoInventarios,getString(R.string.menu_recepcion),"Descripción inventarios");
       constructorTablaMenuPrincipal Almacen = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.menu_almacen),"Descripción almacen");

       constructorTablaMenuPrincipal Consultas = new constructorTablaMenuPrincipal(iconoConsultas,getString(R.string.menu_consultas),"Descripción consultas");
       constructorTablaMenuPrincipal Embarques = new constructorTablaMenuPrincipal(iconoEmbarques,getString(R.string.menu_embarques),"Descripción embarques");
       constructorTablaMenuPrincipal Inventarios = new constructorTablaMenuPrincipal(iconoInventarios,getString(R.string.menu_inventarios),"Descripción inventarios");
       lista.add(RegistroProductoAlmacen);
       lista.add(Almacen);

       lista.add(Consultas);
       lista.add(Embarques);
       lista.add(Inventarios);

        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(Inicio_Menu.this, Recepcion_Menu_Tipo_Recepcion.class);
                        break;
                    case 1:
                        intent = new Intent(Inicio_Menu.this, Almacen_Menu.class);
                        break;
                    case 2:
                        intent = new Intent(Inicio_Menu.this, Consultas_Menu.class);
                        break;
                    case 3:
                        intent = new Intent(Inicio_Menu.this, Embarques_Menu.class);
                        break;
                    case 4:
                        intent = new Intent(Inicio_Menu.this, Inventarios_Menu.class);
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
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_black_24dp)

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
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
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
