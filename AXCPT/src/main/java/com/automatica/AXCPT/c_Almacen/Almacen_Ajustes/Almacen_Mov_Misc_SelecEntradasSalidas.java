package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_Ciesa.Ajustes_AjustesContenedor;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_AjustePalletSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_BajaEmpaqueSCH;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_NuevoEmpaqueSCH;
import com.automatica.AXCPT.databinding.ActivityMovMiscEntradasSalidasBinding;
import com.automatica.AXCPT.objetos.objetoMenu;
import com.automatica.AXCPT.objetos.objetoMenuContext;

import java.util.ArrayList;

public class Almacen_Mov_Misc_SelecEntradasSalidas extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar
{
    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    ActivityMovMiscEntradasSalidasBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMovMiscEntradasSalidasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //registerForContextMenu(binding.recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.menu_mov_misc));
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Mov_Misc_SelecEntradasSalidas.this);

        addListeners();
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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
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
        Intent intent = new Intent(Almacen_Mov_Misc_SelecEntradasSalidas.this, Inicio_Menu_Dinamico.class);
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

    private void addListeners() {
        binding.textoEntradas.setText(getString(R.string.misc_entradas));
        binding.textoSalidas.setText(getString(R.string.misc_salidas));

        binding.linearMiscEntradas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(contexto, Almacen_Mov_Misc_Menu_Entradas.class);
                startActivity(intent);
            }
        });

        binding.linearMiscSalidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               intent = new Intent(contexto, Almacen_Ajustes_Menu.class);
               startActivity(intent);
            }
        });

    }
}

