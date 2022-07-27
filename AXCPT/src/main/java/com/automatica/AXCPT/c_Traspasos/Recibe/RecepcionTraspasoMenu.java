package com.automatica.AXCPT.c_Traspasos.Recibe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorMenuSeleccion;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityRecepcionTraspasoMenuBinding;
import com.automatica.AXCPT.objetos.objetoMenuContext;

import java.util.ArrayList;

public class RecepcionTraspasoMenu extends AppCompatActivity  implements frgmnt_taskbar_AXC.interfazTaskbar{

    Context contexto = this;
    ActivityRecepcionTraspasoMenuBinding binding;
    private ActivityHelpers activityHelpers;
    Bundle b = new Bundle();
    String documento, partida, articulo;
    Intent intent, intentPyU, intentNE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecepcionTraspasoMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sacarDatosIntent();
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(RecepcionTraspasoMenu.this,R.id.Pantalla_principal);
        activityHelpers.AgregarTaskBar(RecepcionTraspasoMenu.this,R.id.FrameLayout,true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Menú");
        this.getSupportActionBar().setSubtitle("Recepción de traspasos");

        intent = new Intent(contexto, RecepcionTraspasoEmpaque.class);
        intentPyU = new Intent(contexto, RecepcionTraspasoPyU.class);
        intentNE = new Intent(contexto, RecepcionTraspasoPalletNE.class);
        if (!documento.isEmpty()){
            intent.putExtras(b);
            intentPyU.putExtras(b);
            intentNE.putExtras(b);
        }
        ArrayList<objetoMenuContext> objetos= new ArrayList<>();
        objetos.add(new objetoMenuContext("Empaque",R.drawable.ic_empaque,intent) );
        objetos.add(new objetoMenuContext("Primera y última",R.drawable.ic_primera_ultima, intentPyU));
        objetos.add(new objetoMenuContext("Pallet NE",R.drawable.ic_pallet_no_etiquetado, intentNE));
        llenarRecycler(binding.recycler,objetos);
    }

    private void sacarDatosIntent() {
        try {
            b = getIntent().getExtras();
            documento = b.getString("Orden");
            partida = b.getString("Partida");
            articulo = b.getString("Producto");
            Log.e("Partida", partida);
            b.putString("Orden", documento);
            b.putString("Partida", partida);
            b.putString("Producto",articulo);



        } catch (Exception e) {
            e.printStackTrace();

        }

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



    public void llenarRecycler(RecyclerView recyclerView, ArrayList<objetoMenuContext> menu) {
        recyclerView.setLayoutManager(new GridLayoutManager(contexto,1,GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdaptadorMenuSeleccion(menu, contexto));
    }

    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}