package com.automatica.AXCPT.c_Almacen.Cuarentena;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaEmpaque;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaPallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_NuevoEmpaque;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Registro;

import java.util.ArrayList;

public class Almacen_Cuarentena_Menu extends AppCompatActivity
{

    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Cuarentena));
        //toolbar.setSubtitle("  Cuarentena");
//        toolbar.setLogo(R.mipmap.logo_axc);//     toolbar.setLogo(R.drawable.axc_logo_toolbar);


        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Cuarentena_Menu.this);

        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen1 = R.drawable.icono_mov_alm_v5_agregar_pallet;
        int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_agregar_empaque;
        int iconoAlmacen3 = R.drawable.icono_mov_alm_v5_baja_empaque;
        int iconoAlmacen4 = R.drawable.icono_mov_alm_v5_baja_pallet;

        constructorTablaMenuPrincipal Colocar = new constructorTablaMenuPrincipal(iconoAlmacen1,"Marcar Pallet","Descripción Marcar Pallet");
        constructorTablaMenuPrincipal Reubicar = new constructorTablaMenuPrincipal(iconoAlmacen2,"Marcar Empaque","Descripción Marcar Empaque");
        constructorTablaMenuPrincipal Ajustes = new constructorTablaMenuPrincipal(iconoAlmacen3,"Recuperar","Descripción Recuperar");
        constructorTablaMenuPrincipal Cuarentena = new constructorTablaMenuPrincipal(iconoAlmacen4,"Baja","Descripción Baja");

        lista.add(Colocar);
        lista.add(Reubicar);
        lista.add(Ajustes);
        lista.add(Cuarentena);

        ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(Almacen_Cuarentena_Menu.this, Almacen_Ajustes_Registro.class);
                        break;
                    case 1:
                        intent = new Intent(Almacen_Cuarentena_Menu.this, Almacen_Ajustes_NuevoEmpaque.class);
                        break;
                    case 2:
                        intent = new Intent(Almacen_Cuarentena_Menu.this, Almacen_Ajustes_BajaEmpaque.class);
                        break;
                    case 3:
                        intent = new Intent(Almacen_Cuarentena_Menu.this, Almacen_Ajustes_BajaPallet.class);
                        break;
                }
                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Almacen_Cuarentena_Menu.this, R.layout.menu_item, lista);
        ListaAlmacen.setAdapter(adaptador);

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
