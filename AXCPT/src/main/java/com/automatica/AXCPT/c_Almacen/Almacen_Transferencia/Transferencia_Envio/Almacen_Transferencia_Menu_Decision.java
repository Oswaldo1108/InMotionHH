package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio;

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
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.OLD.Almacen_Transferencia_SurtirEmpaque;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.OLD.Almacen_Transferencia_SurtirPalletNE;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.OLD.Almacen_Transferencia_Surtir_Pallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso.Surtido_Traspaso_Empaque;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso.Surtido_Traspaso_Empaque_NE;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso.Surtido_Traspaso_Pallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso.Surtido_Traspaso_Picking_NE;

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
            this.getSupportActionBar().setTitle(getString(R.string.Transferencia));

            new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Transferencia_Menu_Decision.this);

            ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


            int iconoAlmacen1 = R.drawable.icono_mov_alm_v5_envio_pallet;
            int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_envio_emp;

            constructorTablaMenuPrincipal Colocar = new constructorTablaMenuPrincipal(iconoAlmacen1, "Pallet", "Descripción Pallet");
            constructorTablaMenuPrincipal PalletNE = new constructorTablaMenuPrincipal(iconoAlmacen1, "Pallet Sin Etiquetas", "Descripción Pallet");
            constructorTablaMenuPrincipal Reubicar = new constructorTablaMenuPrincipal(iconoAlmacen2, "Empaque", "Descripción Pallet");
            constructorTablaMenuPrincipal Picking = new constructorTablaMenuPrincipal(iconoAlmacen2, "Picking", "Descripción Pallet");

            lista.add(Reubicar);
            lista.add(PalletNE);
            lista.add(Colocar);
            lista.add(Picking);

            final Bundle b = getIntent().getExtras();

            ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
            ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = null;
                    switch (position) {
                        case 0:
                            intent = new Intent(Almacen_Transferencia_Menu_Decision.this, Surtido_Traspaso_Empaque.class);
                            break;
                        case 1:
                            intent = new Intent(Almacen_Transferencia_Menu_Decision.this, Surtido_Traspaso_Empaque_NE.class);
                            break;
                        case 2:
                            intent = new Intent(Almacen_Transferencia_Menu_Decision.this, Surtido_Traspaso_Pallet.class);
                            break;
                        case 3:
                            intent = new Intent(Almacen_Transferencia_Menu_Decision.this, Surtido_Traspaso_Picking_NE.class);
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
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
