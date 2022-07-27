package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion;

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


import com.automatica.AXCPT.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCPT.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.RegistroPT_Menu;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Recepcion_Menu_Tipo_Recepcion_Tipo_Registro_Polvos;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Registro_Pallets_Produccion.Almacen_Registro_Pallets_Produccion_v2;

import java.util.ArrayList;

public class Recepcion_Menu_Tipo_Recepcion extends AppCompatActivity
{
    ListView listaMenu;
    View vista;
    Context contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.recepcion_tipo_recepcion));
        new cambiaColorStatusBar(contexto,R.color.VerdeStd, Recepcion_Menu_Tipo_Recepcion.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoPolvos =  R.drawable.armado_tarimas_polvos;
        int iconoPyU = R.drawable.icono_rec_v5_registro_prim_ult;
        int iconoLiq=  R.drawable.armado_tarimas_liquidos;
        int iconoRegProd = R.drawable.registro_produccion;

        constructorTablaMenuPrincipal RegistroProd= new constructorTablaMenuPrincipal(iconoRegProd,getString(R.string.Registro_Pallets_Produccion),"Descripción Primera y Ultima");
        constructorTablaMenuPrincipal ArmadoPolv = new constructorTablaMenuPrincipal(iconoPolvos,getString(R.string.almacen_armado_tarimas)+" "+getString(R.string.almacen_armado_tarimas_polvos),"Descripción Empaque");
       // constructorTablaMenuPrincipal ArmadoPolvPyU = new constructorTablaMenuPrincipal(iconoPyU,getString(R.string.almacen_armado_tarimas_PyU)+" "+getString(R.string.almacen_armado_tarimas_polvos),"Descripción Empaque");
        constructorTablaMenuPrincipal ArmadoLiq = new constructorTablaMenuPrincipal(iconoLiq,getString(R.string.almacen_armado_tarimas)+" "+getString(R.string.almacen_armado_tarimas_liquidos),"Descripción Primera y Ultima");
      //  constructorTablaMenuPrincipal ArmadoLiqPyU = new constructorTablaMenuPrincipal(iconoLiq,getString(R.string.almacen_armado_tarimas_PyU)+" "+getString(R.string.almacen_armado_tarimas_liquidos),"Descripción Primera y Ultima");

        lista.add(RegistroProd);
        lista.add(ArmadoPolv);
     //   lista.add(ArmadoPolvPyU);
        lista.add(ArmadoLiq);
     //   lista.add(ArmadoLiqPyU);

        // lista.add(RegistroProdold);
        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                          intent = new Intent(Recepcion_Menu_Tipo_Recepcion.this, Almacen_Registro_Pallets_Produccion_v2.class);
                        break;
                    case 1:
                          intent = new Intent(Recepcion_Menu_Tipo_Recepcion.this, Recepcion_Menu_Tipo_Recepcion_Tipo_Registro_Polvos.class);
                          break;
                    case 2:
                          intent = new Intent(Recepcion_Menu_Tipo_Recepcion.this, RegistroPT_Menu.class);
                        break;

                }

                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Recepcion_Menu_Tipo_Recepcion.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

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


}
