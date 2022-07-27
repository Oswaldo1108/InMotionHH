package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets;

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

import java.util.ArrayList;

public class RegistroPT_Menu extends AppCompatActivity
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

        getSupportActionBar().setTitle(getString(R.string.registro_produccion));
        new cambiaColorStatusBar(contexto,R.color.VerdeStd, RegistroPT_Menu.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();

        int iconoLiq=  R.drawable.armado_tarimas_liquidos;


            int iconoPolvos =  R.drawable.armado_tarimas_polvos;
            int iconoPyU = R.drawable.icono_rec_v5_registro_prim_ult;



            constructorTablaMenuPrincipal ArmadoLiq = new constructorTablaMenuPrincipal(iconoPolvos,getString(R.string.almacen_armado_tarimas),"Descripción Primera y Ultima");
        constructorTablaMenuPrincipal ArmadoLiqMod = new constructorTablaMenuPrincipal(iconoLiq,"Armado tarimas cant configurable","Descripción Primera y Ultima");
            constructorTablaMenuPrincipal ArmadoLiqPyU = new constructorTablaMenuPrincipal(iconoPyU,getString(R.string.almacen_armado_tarimas_PyU),"Descripción Primera y Ultima");
            constructorTablaMenuPrincipal ArmadoNE = new constructorTablaMenuPrincipal(iconoPyU,getString(R.string.almacen_armado_tarimas_NE),"Descripción Primera y Ultima");

        lista.add(ArmadoLiq);
        lista.add(ArmadoLiqMod);
        lista.add(ArmadoLiqPyU);
        lista.add(ArmadoNE);

        // lista.add(RegistroProdold);
        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                String Doc= null;
                try {
                    Doc = getIntent().getStringExtra("Documento");
                }catch (Exception e){
                    e.printStackTrace();
                }
                switch (position)
                {

                    case 0:
                        intent = new Intent(RegistroPT_Menu.this, Almacen_Armado_Pallets_Liquidos.class);
                        if (Doc!=null){
                            intent.putExtra("Documento",Doc);
                        }
                        break;
                    case 1:
                        intent = new Intent(RegistroPT_Menu.this, Almacen_Armado_Pallets_Liquidos_CantidadModificable.class);
                        if (Doc!=null){
                            intent.putExtra("Documento",Doc);
                        }
                        break;
                    case 2:
                        intent = new Intent(RegistroPT_Menu.this, Almacen_Armado_Pallets_Liquidos_PyU.class);
                        if (Doc!=null){
                            intent.putExtra("Documento",Doc);
                        }
                        break;
                    case 3:
                        intent = new Intent(RegistroPT_Menu.this, Almacen_Armado_Pallets_Ne.class);
                        if (Doc!=null){
                            intent.putExtra("Documento",Doc);
                        }
                        break;
                }

                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(RegistroPT_Menu.this, R.layout.menu_item, lista);
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
