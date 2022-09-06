package com.automatica.AXCPT.c_Almacen.Cuarentena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorMenuSeleccion;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_Ciesa.Ajustes_AjustesContenedor;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_AjustePalletSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_BajaEmpaqueSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_NuevoEmpaqueSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_AjustePallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaEmpaque;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaPallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_NuevoEmpaque;
import com.automatica.AXCPT.databinding.ActivityMenuNuevoBinding;
import com.automatica.AXCPT.objetos.objetoMenu;
import com.automatica.AXCPT.objetos.objetoMenuContext;

import java.util.ArrayList;

public class MenuNuevo extends AppCompatActivity  implements frgmnt_taskbar_AXC.interfazTaskbar {


    ListView ListaAlmacen;
    Context contexto = this;
    ActivityMenuNuevoBinding binding;
    private ActivityHelpers activityHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMenuNuevoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(MenuNuevo.this,R.id.Pantalla_principal);
        activityHelpers.AgregarTaskBar(MenuNuevo.this,R.id.FrameLayout,true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Cuarentena));

        ArrayList<objetoMenu> empaque= new ArrayList<>();
        empaque.add(new objetoMenu("Cuarentena",new Intent(contexto, CuarentenaEmpaque.class)));
        empaque.add(new objetoMenu("Recuperar",new Intent(contexto, RecuperarEmpaque.class)));


        ArrayList<objetoMenu> contenedor = new ArrayList<>();
        contenedor.add(new objetoMenu("Cuarentena",new Intent(contexto, CuarentenaContenedor.class)));
        contenedor.add(new objetoMenu("Recuperar",new Intent(contexto, RecuperarContenedor.class)));

        ArrayList<objetoMenu> pallet = new ArrayList<>();
        pallet.add(new objetoMenu("Cuarentena",new Intent(contexto, CuarentenaPallet.class)));
        pallet.add(new objetoMenu("Recuperar",new Intent(contexto, RecuperarPallet.class)));


        ArrayList<objetoMenuContext> objetos= new ArrayList<>();
        objetos.add(new objetoMenuContext("Empaque",R.drawable.ic_cuarentenaempaque, empaque));
        objetos.add(new objetoMenuContext("Contenedor",R.drawable.ic_cuarentenacontenedor, contenedor));
        objetos.add(new objetoMenuContext("Pallet",R.drawable.ic_cuarentenacontenedor, pallet));

        llenarRecycler(binding.recycler,objetos);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);
    }


    public void llenarRecycler(RecyclerView recyclerView, ArrayList<objetoMenuContext> menu) {
        recyclerView.setLayoutManager(new GridLayoutManager(contexto,2,GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdaptadorMenuSeleccion(menu, contexto));
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
            new sobreDispositivo(contexto,getCurrentFocus());
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        activityHelpers.getTaskbar_axc().onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}