package com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCPT.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaPallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu_Etiquetado;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu_NE;

import java.util.ArrayList;

public class Devoluciones_Menu extends AppCompatActivity {

    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);
        try {
            b = getIntent().getExtras();
            Log.i("Bundle",b.getString("OrdenDevolucion"));
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Devolución");
        }catch (Exception e){
            e.printStackTrace();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Devoluciones");
        toolbar.setSubtitle("Tipos de devolución");


        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Devoluciones_Menu.this);

        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        int iconoAlmacen1 = R.drawable.mov_alm_primerayultima;
        int iconoAlmacen2 = R.drawable.mov_alm_porempaque;
        int iconoAlmacen4 = R.drawable.traspaso_rec_ne;

        constructorTablaMenuPrincipal Colocar = new constructorTablaMenuPrincipal(iconoAlmacen2, "Primera y ultima", "Descripción Primera y ultima");
        constructorTablaMenuPrincipal NuevoPalletNE = new constructorTablaMenuPrincipal(iconoAlmacen1, "Por paquete", "Descripción Por empaque");
        constructorTablaMenuPrincipal Cuarentena = new constructorTablaMenuPrincipal(iconoAlmacen4, "Pallet no etiquetado", "Descripción Empaques sin etiqueta");

        lista.add(Colocar);
        lista.add(NuevoPalletNE);
        lista.add(Cuarentena);

        ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(Devoluciones_Menu.this, Devolucion_PYU.class);
                        break;
                    case 1:
                        intent = new Intent(Devoluciones_Menu.this, Devolucion_Empaques.class);
                        break;
                    case 2:
                        intent = new Intent(Devoluciones_Menu.this, Devolucion_NE.class);
                        break;
                }
                try {
                    intent.putExtras(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (intent != null) startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Devoluciones_Menu.this, R.layout.menu_item, lista);
        ListaAlmacen.setAdapter(adaptador);
    }

}

