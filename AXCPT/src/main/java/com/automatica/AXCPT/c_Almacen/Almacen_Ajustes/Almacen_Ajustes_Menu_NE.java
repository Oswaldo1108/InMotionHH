package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_AjustePalletSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_BajaEmpaqueSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_NuevoEmpaqueSCH;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Almacen_Ajustes_Menu_NE extends AppCompatActivity
{
    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_old);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Ajustes));
        toolbar.setSubtitle("Ajuste No Etiquetado");


        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_Menu_NE.this);

        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen1 = R.drawable.icono_mov_alm_v5_agregar_pallet;
        int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_agregar_empaque;
        int iconoAlmacen3 = R.drawable.icono_mov_alm_v5_baja_empaque;

        constructorTablaMenuPrincipal NuevoPalletNE = new constructorTablaMenuPrincipal(iconoAlmacen1,"Nuevo pallet NE", "Nuevo pallet sin etiqueta");
        constructorTablaMenuPrincipal NuevoEmpaqueNE = new constructorTablaMenuPrincipal(iconoAlmacen2,"Nuevo empaque NE", "Nuevo Empaque sin etiqueta");
        constructorTablaMenuPrincipal BajaEmpaqueNE = new constructorTablaMenuPrincipal(iconoAlmacen3,"Baja empaque NE", "Baja Empaque sin etiqueta");


        lista.add(NuevoPalletNE);
        lista.add(NuevoEmpaqueNE);
        lista.add(BajaEmpaqueNE);


        ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {

                    case 0:
                        intent = new Intent(Almacen_Ajustes_Menu_NE.this, Almacen_Ajustes_AjustePalletSCH.class);
                        break;

                    case 1:
                        intent = new Intent(Almacen_Ajustes_Menu_NE.this, Almacen_Ajustes_NuevoEmpaqueSCH.class);
                        break;

                    case 2:
                        intent = new Intent(Almacen_Ajustes_Menu_NE.this, Almacen_Ajustes_BajaEmpaqueSCH.class);
                        break;

                }
                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Almacen_Ajustes_Menu_NE.this, R.layout.menu_item, lista);
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

