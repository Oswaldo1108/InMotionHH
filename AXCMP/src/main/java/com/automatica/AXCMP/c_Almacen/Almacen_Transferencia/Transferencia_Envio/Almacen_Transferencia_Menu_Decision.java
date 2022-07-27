package com.automatica.AXCMP.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

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
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;

import java.util.ArrayList;

public class Almacen_Transferencia_Menu_Decision extends AppCompatActivity
{
    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.principal_activity_menu);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(" " + getString(R.string.Transferencia));

            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Transferencia_Menu_Decision.this);

            ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


            int iconoAlmacen1 = R.drawable.icono_mov_alm_v5_envio_pallet;
            int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_envio_emp;
            int iconoAlmacen3= R.drawable.icono_mov_alm_v5_envio_emp;

            constructorTablaMenuPrincipal Colocar = new constructorTablaMenuPrincipal(iconoAlmacen1, "Pallet", "Descripción Pallet");
            constructorTablaMenuPrincipal Reubicar = new constructorTablaMenuPrincipal(iconoAlmacen2, "Empaque", "Descripción Pallet");
            constructorTablaMenuPrincipal AjusteDocificado= new constructorTablaMenuPrincipal(iconoAlmacen3, "Unidad", "Descripción Pallet");

            lista.add(Colocar);
            lista.add(Reubicar);
            lista.add(AjusteDocificado);
            final Bundle b = getIntent().getExtras();

            String Transferencia = b.getString("Transferencia");

            ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
            ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = null;
                    switch (position) {
                        case 0:
                            intent = new Intent(Almacen_Transferencia_Menu_Decision.this, Almacen_Transferencia_Surtir_Pallet.class);
                            break;
                        case 1:
                            intent = new Intent(Almacen_Transferencia_Menu_Decision.this, Almacen_Transferencia_SurtirEmpaque.class);
                            break;
                        case 2:
                            intent = new Intent(Almacen_Transferencia_Menu_Decision.this, Almacen_Transferencia_SurtirUnidad.class);
                            break;
                    }

                    if (intent != null)
                    {
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            });

            adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Almacen_Transferencia_Menu_Decision.this, R.layout.menu_item, lista);
            ListaAlmacen.setAdapter(adaptador);


        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),false,true,true);
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
