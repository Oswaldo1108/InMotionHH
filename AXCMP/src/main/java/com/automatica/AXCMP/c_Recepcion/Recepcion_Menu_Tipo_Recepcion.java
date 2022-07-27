package com.automatica.AXCMP.c_Recepcion;

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
import com.automatica.AXCMP.c_Recepcion.c_Recepcion_Transferencias_Piasa.Rec_Registro_Seleccion_Lote;

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


        int iconoAlmacen =  R.drawable.icono_rec_v5_registro_empaque;
        int iconoConsultas = R.drawable.icono_rec_v5_registro_prim_ult;

        constructorTablaMenuPrincipal RecepcionOC = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.recepcion_OC),"Descripción Empaque");
        constructorTablaMenuPrincipal RecepcionTransfer = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.recepcion_transferenia),"Descripción Empaque");


        lista.add(RecepcionOC);
        lista.add(RecepcionTransfer);



        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                switch (position)
                {
                    case 0:
                          intent = new Intent(Recepcion_Menu_Tipo_Recepcion.this, Rec_Registro_Seleccion_OC.class);
                        break;
                    case 1:
                        intent = new Intent(Recepcion_Menu_Tipo_Recepcion.this, Rec_Registro_Seleccion_Lote.class);
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
