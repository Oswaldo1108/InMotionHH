package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia;

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
import com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Envio.Almacen_Transferencia_Envio;
import com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Recibo.Almacen_Transferencia_SeleccionEntrada;

import java.util.ArrayList;

public class Almacen_Transferencias_Menu extends AppCompatActivity
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
        this.getSupportActionBar().setTitle(" "+getString(R.string.Transferencia));


        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Transferencias_Menu.this);

        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen1 = R.drawable.icono_mov_alm_v5_envio_pallet;
        int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_recibo_pallet;

        constructorTablaMenuPrincipal Colocar = new constructorTablaMenuPrincipal(iconoAlmacen1,"Envío","Descripción Envio");
        constructorTablaMenuPrincipal Reubicar = new constructorTablaMenuPrincipal(iconoAlmacen2,"Recibo","Descripción Recibo");

        lista.add(Colocar);
        lista.add(Reubicar);

        ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(Almacen_Transferencias_Menu.this, Almacen_Transferencia_Envio.class);
                        break;
                    case 1:
                        intent = new Intent(Almacen_Transferencias_Menu.this, Almacen_Transferencia_SeleccionEntrada.class);
                        break;

                }
                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Almacen_Transferencias_Menu.this, R.layout.menu_item, lista);
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
