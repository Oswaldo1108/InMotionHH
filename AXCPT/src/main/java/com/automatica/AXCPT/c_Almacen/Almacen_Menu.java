package com.automatica.AXCPT.c_Almacen;

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
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Almacen_Transferencias_Menu;
import com.automatica.AXCPT.c_Almacen.Cuarentena.Almacen_Cuarentena_Menu;
import com.automatica.AXCPT.c_Almacen.Reubicacion.Almacen_Reubicar;

import java.util.ArrayList;

public class Almacen_Menu extends AppCompatActivity
{

    ListView  ListaAlmacen;
    Context contexto = this;
    View vista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.menu_almacen));
        //  toolbar.setSubtitle("  Almacén");
//        toolbar.setLogo(R.mipmap.logo_axc);//   toolbar.setLogo(R.drawable.axc_logo_toolbar);



        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Menu.this);

        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen1 = R.drawable.icono_mov_alm_v5_colocacion;
        int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_reubicacion;
        int iconoAlmacen3 = R.drawable.icono_mov_alm_v5_ajustes_inventario;
        int iconoAlmacen4 = R.drawable.icono_mov_alm_v5_devolucion_linea;
        int iconoAlmacen5 = R.drawable.icono_mov_alm_v5_consolidacion_tarimas;
        int iconoAlmacen6= R.drawable.icono_mov_alm_v5_transfer_plantas;
        constructorTablaMenuPrincipal Colocar = new constructorTablaMenuPrincipal(iconoAlmacen1,getString(R.string.Colocar),"Descripción Colocar");
        constructorTablaMenuPrincipal Reubicar = new constructorTablaMenuPrincipal(iconoAlmacen2,getString(R.string.Reubicar),"Descripción Reubicar");
        constructorTablaMenuPrincipal Ajustes = new constructorTablaMenuPrincipal(iconoAlmacen3,getString(R.string.Ajustes),"Descripción Ajustes");
        constructorTablaMenuPrincipal Cuarentena = new constructorTablaMenuPrincipal(iconoAlmacen4,getString(R.string.Cuarentena),"Descripción Cuarentena");
        constructorTablaMenuPrincipal Consolida = new constructorTablaMenuPrincipal(iconoAlmacen5,getString(R.string.Consolidacion),"Descripción Consolidación");
        constructorTablaMenuPrincipal Transfer = new constructorTablaMenuPrincipal(iconoAlmacen6,getString(R.string.Transferencia),"Descripción Transfenrencia");

        lista.add(Colocar);
        lista.add(Reubicar);
        lista.add(Ajustes);
        lista.add(Consolida);
        lista.add(Cuarentena);
        lista.add(Transfer);

        ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;

                switch (position)
                {
                    case 0:
                        intent = new Intent(Almacen_Menu.this, Almacen_Colocar.class);
                        break;
                    case 1:
                        intent = new Intent(Almacen_Menu.this, Almacen_Reubicar.class);
                        break;
                    case 2:
                        intent = new Intent(Almacen_Menu.this, Almacen_Ajustes_Menu.class);
                        break;
                    case 3:
                        intent = new Intent(Almacen_Menu.this, Almacen_Consolidacion.class);
                        break;
                    case 4:
                        intent = new Intent(Almacen_Menu.this, Almacen_Cuarentena_Menu.class);
                        break;
                    case 5:
                        intent = new Intent(Almacen_Menu.this, Almacen_Transferencias_Menu.class);
                        break;

                }
                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Almacen_Menu.this, R.layout.menu_item, lista);
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
