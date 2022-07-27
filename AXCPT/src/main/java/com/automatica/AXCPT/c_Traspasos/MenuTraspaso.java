package com.automatica.AXCPT.c_Traspasos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorMenuSeleccion;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Traspasos.Envio.SeleccionarOrdenesTraspasoEnvio;
import com.automatica.AXCPT.c_Traspasos.Recibe.SeleccionOrdenTraspasoRecepcion;
import com.automatica.AXCPT.databinding.ActivityMenuTraspasoBinding;
import com.automatica.AXCPT.objetos.objetoMenuContext;

import java.util.ArrayList;

public class MenuTraspaso extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    Context contexto = this;
    ActivityMenuTraspasoBinding binding;
    private ActivityHelpers activityHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuTraspasoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(MenuTraspaso.this,R.id.Pantalla_principal);
        activityHelpers.AgregarTaskBar(MenuTraspaso.this,R.id.FrameLayout,true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(getString(R.string.recepcion_recepcion_por_empaque_traspasos));

        ArrayList<objetoMenuContext> objetos= new ArrayList<>();
        objetos.add(new objetoMenuContext("Recibo", R.drawable.ic_recepcion_se, new Intent(contexto, SeleccionOrdenTraspasoRecepcion.class)));
        objetos.add(new objetoMenuContext("Envio", R.drawable.ic_recepcion, new Intent(contexto, SeleccionarOrdenesTraspasoEnvio.class)));

        llenarRecycler(binding.recycler,objetos);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
        super.onPostCreate(savedInstanceState);
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


    public void llenarRecycler(RecyclerView recyclerView, ArrayList<objetoMenuContext> menu) {
        recyclerView.setLayoutManager(new GridLayoutManager(contexto,2,GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdaptadorMenuSeleccion(menu, contexto));
    }

}