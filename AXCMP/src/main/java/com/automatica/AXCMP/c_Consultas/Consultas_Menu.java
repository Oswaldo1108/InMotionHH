package com.automatica.AXCMP.c_Consultas;

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

public class Consultas_Menu extends AppCompatActivity {

    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.menu_consultas));
        new cambiaColorStatusBar(contexto, R.color.AzulStd,Consultas_Menu.this);

        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen1 = R.drawable.icono_consultas_v5consultas_posicion;
        int iconoAlmacen2 =  R.drawable.icono_consultas_v5consultas_pallet;
        int iconoAlmacen3 = R.drawable.icono_consultas_v5consultas_empaque;
        int iconoAlmacen4= R.drawable.icono_consultas_v5consultas_referencia;
        int iconoAlmacen5= R.drawable.icono_consultas_v5consultas_existencias;
        constructorTablaMenuPrincipal Posicion = new constructorTablaMenuPrincipal(iconoAlmacen1,getString(R.string.Consultas_Posicion),"Descripción Posición");
        constructorTablaMenuPrincipal Pallet = new constructorTablaMenuPrincipal(iconoAlmacen2,getString(R.string.Consultas_Pallet),"Descripción Pallet");
        constructorTablaMenuPrincipal Empaque = new constructorTablaMenuPrincipal(iconoAlmacen3,getString(R.string.Consultas_Empaque),"Descripción Empaque");
        constructorTablaMenuPrincipal Referencia = new constructorTablaMenuPrincipal(iconoAlmacen4,getString(R.string.Consultas_Referencia),"Descripción Referencia");
        constructorTablaMenuPrincipal Existencias = new constructorTablaMenuPrincipal(iconoAlmacen5,getString(R.string.Consultas_Existencias),"Descripción Existencias");
        lista.add(Posicion);
        lista.add(Pallet);
        lista.add(Empaque);
        lista.add(Referencia);
        lista.add(Existencias);
        ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(Consultas_Menu.this, consultas_ConsultaPosicion.class);
                        break;
                    case 1:
                        intent = new Intent(Consultas_Menu.this, consultas_ConsultaPallet.class);
                        break;
                    case 2:
                        intent = new Intent(Consultas_Menu.this, consultas_ConsultaEmpaque.class);
                        break;
                    case 3:
                        intent = new Intent(Consultas_Menu.this, consultas_ConsultaReferencia.class);
                        break;
                    case 4:
                        intent = new Intent(Consultas_Menu.this, consultas_BusquedaExistencia.class);
                        break;
                }
                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Consultas_Menu.this, R.layout.menu_item, lista);
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
