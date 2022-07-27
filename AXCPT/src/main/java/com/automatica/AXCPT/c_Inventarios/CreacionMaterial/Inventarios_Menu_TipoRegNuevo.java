package com.automatica.AXCPT.c_Inventarios.CreacionMaterial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorMenuSeleccion;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCPT.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu;
import com.automatica.AXCPT.databinding.ActivityMenuBinding;
import com.automatica.AXCPT.objetos.objetoMenuContext;

import java.util.ArrayList;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Inventarios_Menu_TipoRegNuevo extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar {
    String TAG = "Inventarios_Menu2";
    ListView listaMenu;
    View vista;
    Context contexto = this;
    ActivityMenuBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            binding = ActivityMenuBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Inventarios");
            getSupportActionBar().setSubtitle("Tipo de nuevo pallet");

            new cambiaColorStatusBar(contexto, R.color.grisAXC, Inventarios_Menu_TipoRegNuevo.this);
            Bundle b = getIntent().getExtras();

            ArrayList<objetoMenuContext> objetos= new ArrayList<>();
            objetos.add(new objetoMenuContext("Pallet etiquetado",R.drawable.cons_pallet, new Intent(Inventarios_Menu_TipoRegNuevo.this,Inventario_RegPalletNuevo.class).putExtras(b)));
            objetos.add(new objetoMenuContext("Pallet no etiquetado",R.drawable.cons_pallet, new Intent(Inventarios_Menu_TipoRegNuevo.this,Inventarios_RegPalletNuevoNE.class).putExtras(b)));

            llenarRecycler(binding.recycler,objetos);
            View logoView = getToolbarLogoIcon((Toolbar) findViewById(R.id.toolbar));
            logoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                                .replace(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                    } else {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });

            taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
            getSupportFragmentManager().beginTransaction().add(findViewById(R.id.FrameLayout).getId(),taskbar_axc,"FragmentoTaskBar").commit();




        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, vista, e.getMessage(), "false", true, true);
        }
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    public void llenarRecycler(RecyclerView recyclerView, ArrayList<objetoMenuContext> menu) {
        recyclerView.setLayoutManager(new GridLayoutManager(contexto,2,GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdaptadorMenuSeleccion(menu, contexto));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu_principal_toolbar, menu);
            return true;
        } catch (Exception ex) {
            Toast.makeText(this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ((id == R.id.InformacionDispositivo)) {
            new sobreDispositivo(contexto, vista);
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
            taskbar_axc.cerrarFragmento();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }
}