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
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen.Recepcion_Registro_Transferencia_NE_SelAlm;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen.Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen.Recepcion_Registro_Transferencias_Por_Empaque_SelAlm;
import com.automatica.AXCPT.c_Recepcion.Recepcion_Menu_Registro;

import java.util.ArrayList;

public class Recepcion_Menu_Transferencia_Tipo_Recepcion extends AppCompatActivity
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

        getSupportActionBar().setTitle(getString(R.string.almacen_traspaso));
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Recepcion_Menu_Transferencia_Tipo_Recepcion.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


            int iconoConsultas =  R.drawable.mov_alm_porempaque;
            int iconoAlmacen = R.drawable.mov_alm_primerayultima;

            constructorTablaMenuPrincipal Empaque = new constructorTablaMenuPrincipal(iconoAlmacen,getString(R.string.recepcion_por_empaque),"Descripción Empaque");
            constructorTablaMenuPrincipal primult = new constructorTablaMenuPrincipal(iconoConsultas,getString(R.string.recepcion_primera_y_ultima),"Descripción Primera y Ultima");
            constructorTablaMenuPrincipal NE = new constructorTablaMenuPrincipal(iconoAlmacen,"No etiquetados","Descripción no etiquetados");

            lista.add(Empaque);
            lista.add(primult);
            lista.add(NE);



        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try {
                    Intent intent = null;
                    switch (position) {
                        case 0:
                            intent = new Intent(Recepcion_Menu_Transferencia_Tipo_Recepcion.this, Recepcion_Registro_Transferencias_Por_Empaque_SelAlm.class);
                            break;
                        case 1:
                            intent = new Intent(Recepcion_Menu_Transferencia_Tipo_Recepcion.this, Recepcion_Registro_Transferencia_PrimeraYUltima_SelAlm.class);
                            break;

                        case 2:
                            intent = new Intent(Recepcion_Menu_Transferencia_Tipo_Recepcion.this, Recepcion_Registro_Transferencia_NE_SelAlm.class);
                    }

                    if (intent != null)
                    {
                        intent.putExtras(getIntent().getExtras());
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

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Recepcion_Menu_Transferencia_Tipo_Recepcion.this, R.layout.menu_item, lista);
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
