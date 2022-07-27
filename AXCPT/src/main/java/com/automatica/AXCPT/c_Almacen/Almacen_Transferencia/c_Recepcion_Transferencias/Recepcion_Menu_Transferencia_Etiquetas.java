package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;

import java.util.ArrayList;

public class Recepcion_Menu_Transferencia_Etiquetas extends AppCompatActivity
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
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Recepcion_Menu_Transferencia_Etiquetas.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


            int iconoAlmacen =  R.drawable.icono_mov_alm_v5_agregar_empaque;
            int iconoConsultas = R.drawable.icono_mov_alm_v5_agregar_empaque;

            constructorTablaMenuPrincipal Proveedor = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.rec_con_etiquetas),"Descripción Empaque");
            constructorTablaMenuPrincipal Maquila = new constructorTablaMenuPrincipal(iconoConsultas,getString(R.string.rec_sin_etiquetas),"Descripción Primera y Ultima");

            lista.add(Proveedor);
            lista.add(Maquila);



        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try {
                    Intent intent = null;
                    switch (position) {
                        case 0:
                            intent = new Intent(Recepcion_Menu_Transferencia_Etiquetas.this, Rec_Traspaso_Con_Etiquetas.class);
                            break;
                        case 1:
                            intent = new Intent(Recepcion_Menu_Transferencia_Etiquetas.this, Recepcion_Menu_Transferencia_Tipo_Recepcion.class);
                            break;
                    }



                    if (intent != null)
                    {
                        intent.putExtras(getIntent().getExtras()); //Toma el intent recibido de la actividad anterior y lo entrega a la siguiente
                        Log.i("PorEmpaque1", "SacaExtrasIntent: Error sacando del Intent Extras, " +getIntent().getExtras().getString("Producto"));
                        startActivity(intent);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false", true,true);
                }
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Recepcion_Menu_Transferencia_Etiquetas.this, R.layout.menu_item, lista);
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
