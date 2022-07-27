package com.automatica.AXCPT.c_Embarques;

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
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido.Surtido_Seleccion_Partida;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion.Validacion_Menu;

import java.util.ArrayList;

public class Embarques_Menu extends AppCompatActivity {

    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.menu_embarques));

        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen2 = R.drawable.icono_surt_v5_surtido;
        int iconoAlmacen3 = R.drawable.icono_surt_v5_validacion;
        int iconoAlmacen4= R.drawable.icono_surt_v5_embarque;

        constructorTablaMenuPrincipal Surtido = new constructorTablaMenuPrincipal(iconoAlmacen2,getString(R.string.Embarques_Surtido),"Descripción Surtido");
        constructorTablaMenuPrincipal Valida = new constructorTablaMenuPrincipal(iconoAlmacen3,getString(R.string.Embarques_Valida),"Descripción Valida");
        constructorTablaMenuPrincipal Embarque = new constructorTablaMenuPrincipal(iconoAlmacen4,getString(R.string.Embarques_Embarque),"Descripción Embarque");
        lista.add(Surtido);
        lista.add(Valida);

        ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                        intent = new Intent(Embarques_Menu.this, Surtido_Seleccion_Partida.class);
                        break;
                    case 1:
                        intent = new Intent(Embarques_Menu.this, Validacion_Menu.class);

                        break;
                    case 2:
                        intent = new Intent(Embarques_Menu.this, Embarques_Embarque.class);

                        break;
                }
                if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Embarques_Menu.this, R.layout.menu_item, lista);
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
