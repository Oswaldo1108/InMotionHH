package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.automatica.AXCPT.SoapConection.SoapAction;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_Ciesa.Ajustes_AjustesContenedor;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_AjustePalletSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_BajaEmpaqueSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_NuevoEmpaqueSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_AjustePallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaEmpaque;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaPallet;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_NuevoEmpaque;
import com.automatica.AXCPT.databinding.ActivityMenuBinding;
import com.automatica.AXCPT.objetos.objetoMenu;
import com.automatica.AXCPT.objetos.objetoMenuContext;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Validacion_Menu extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{
    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    ActivityMenuBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        registerForContextMenu(binding.recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.Ajustes));
        toolbar.setSubtitle("Tipos de ajuste");
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Validacion_Menu.this);
        ArrayList<objetoMenu> intentsEtiquetados= new ArrayList<>();
        intentsEtiquetados.add(new objetoMenu("Nuevo pallet",new Intent(contexto, Almacen_Ajustes_AjustePallet.class)));
        intentsEtiquetados.add(new objetoMenu("Nuevo empaque",new Intent(contexto, Almacen_Ajustes_NuevoEmpaque.class)));
        intentsEtiquetados.add(new objetoMenu("baja empaque",new Intent(contexto, Almacen_Ajustes_BajaEmpaque.class)));

        ArrayList<objetoMenu> intentNE = new ArrayList<>();
        intentNE.add(new objetoMenu("Nuevo pallet NE",new Intent(contexto, Almacen_Ajustes_AjustePalletSCH.class)));
        intentNE.add(new objetoMenu("Nuevo empaque NE",new Intent(contexto, Almacen_Ajustes_NuevoEmpaqueSCH.class)));
        intentNE.add(new objetoMenu("baja empaque NE",new Intent(contexto, Almacen_Ajustes_BajaEmpaqueSCH.class)));

        ArrayList<objetoMenuContext> objetos= new ArrayList<>();
        //objetos.add(new objetoMenuContext("Etiquetados",R.drawable.ic_ajuste_etiqueado, intentsEtiquetados));
        //objetos.add(new objetoMenuContext("No etiquetados",R.drawable.ic_ajuste_noetiqueado, intentNE));
        objetos.add(new objetoMenuContext("Validación por pallet",R.drawable.ic_pallet_etiquetado,new Intent(contexto, Validacion_PorPallet.class)));
        objetos.add(new objetoMenuContext("Validación por empaque",R.drawable.ic_pallet_no_etiquetado,new Intent(contexto, Validacion_PorEmpaque.class)));

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

    }

    @Override
    protected void onResume()
    {

        super.onResume();
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
        Intent intent = new Intent(Validacion_Menu.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
