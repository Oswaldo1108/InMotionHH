package com.automatica.AXCMP.c_Almacen.devolucion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCMP.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCMP.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.sobreDispositivo;

import java.util.ArrayList;

public class Almacen_Devolucion_Proveedor_Menu extends AppCompatActivity
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

        getSupportActionBar().setTitle(getString(R.string.devolucion_proveedor));

        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Devolucion_Proveedor_Menu.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen =  R.drawable.icono_mov_alm_v5_baja_pallet;
        int iconoConsultas = R.drawable.icono_mov_alm_v5_baja_empaque;

       constructorTablaMenuPrincipal Almacen = new constructorTablaMenuPrincipal(iconoAlmacen,"Pallet","Descripción Almacen");
       constructorTablaMenuPrincipal Consultas = new constructorTablaMenuPrincipal(iconoConsultas,"Empaque","Descripción Consultas");

       lista.add(Almacen);
       lista.add(Consultas);
        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(Almacen_Devolucion_Proveedor_Menu.this, Almacen_Devolucion_Proveedor_Pallet.class);
                        break;
                    case 1:
                        intent = new Intent(Almacen_Devolucion_Proveedor_Menu.this, Almacen_Devolucion_Proveedor_Empaque.class);
                        break;

                }
               if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Almacen_Devolucion_Proveedor_Menu.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
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
          new sobreDispositivo(contexto,vista);
        }


        return super.onOptionsItemSelected(item);
    }


}
