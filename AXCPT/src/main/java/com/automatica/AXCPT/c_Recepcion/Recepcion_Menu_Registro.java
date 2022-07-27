package com.automatica.AXCPT.c_Recepcion;

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
import com.automatica.AXCPT.c_Recepcion.Base.Recepcion_Registro_PalletSinEtiqueta;
import com.automatica.AXCPT.c_Recepcion.Base.Recepcion_Registro_PorEmpaque;
import com.automatica.AXCPT.c_Recepcion.Base.Recepcion_Registro_PrimerasYUltimas;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro_Granel_NE_LoteModificable;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro_Pallet_NE_LoteModificable;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro_PrimerasYUltimas_LoteModificable;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro__PorEmpaque_LoteModificable;

import java.util.ArrayList;

public class Recepcion_Menu_Registro extends AppCompatActivity
{

    ListView listaMenu;
    View vista;
    Context contexto = this;
    boolean Recargar;
    Intent tntent;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);
        b = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.recepcion_tipo_recepcion));
        new cambiaColorStatusBar(contexto, R.color.VerdeStd, Recepcion_Menu_Registro.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen = R.drawable.icono_rec_v5_registro_empaque;
        int iconoConsultas = R.drawable.icono_rec_v5_registro_prim_ult;
        int iconoNE = R.drawable.icono_rec_v5_recepcionmaterial;

        constructorTablaMenuPrincipal Proveedor = new constructorTablaMenuPrincipal(iconoAlmacen, getString(R.string.recepcion_por_empaque), "Descripción Empaque");
        constructorTablaMenuPrincipal Maquila = new constructorTablaMenuPrincipal(iconoConsultas, getString(R.string.recepcion_primera_y_ultima), "Descripción Primera y Ultima");
        constructorTablaMenuPrincipal sinEtiqueta = new constructorTablaMenuPrincipal(iconoNE, "Pallet sin etiqueta", "Descripción Pallet sin etiqueta");
        constructorTablaMenuPrincipal Granel = new constructorTablaMenuPrincipal(iconoAlmacen, "Material a Granel", "Descripción Pallet sin etiqueta");
        lista.add(Proveedor);
        lista.add(Maquila);
        lista.add(sinEtiqueta);
        lista.add(Granel);
        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                    {
                        case 0:
                            intent = new Intent(Recepcion_Menu_Registro.this, Recepcion_Registro__PorEmpaque_LoteModificable.class);


                            //  b.putString("Tipo", "Proveedor");
                            break;
                        case 1:
                            intent = new Intent(Recepcion_Menu_Registro.this, Recepcion_Registro_PrimerasYUltimas_LoteModificable.class);
                            //b.putString("Tipo", "Maquila");
                            break;
                        case 2:
                            intent = new Intent(Recepcion_Menu_Registro.this, Recepcion_Registro_Pallet_NE_LoteModificable.class);
                            break;
                        case 3:
                            intent = new Intent(Recepcion_Menu_Registro.this, Recepcion_Registro_Granel_NE_LoteModificable.class);
                            //b.putString("Tipo", "Maquila");
                            break;
                    }
                intent.putExtras(b);
                startActivity(intent);


                tntent = intent;
            }

        });


        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Recepcion_Menu_Registro.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
            {
                getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);
                return true;
            } catch (Exception ex)
            {
                Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
            }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (Recargar)
            {
                if ((id == R.id.InformacionDispositivo))
                    {
                        new sobreDispositivo(contexto, vista);
                    }
            }

        return super.onOptionsItemSelected(item);
    }
}



