package com.automatica.AXCMP.Principal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Constructor_Dato;

import java.util.ArrayList;

public class Inicio_Menu_RecyclerView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
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
                setContentView(R.layout.principal_activity_menu_card_view);

                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                RecyclerView rv = (RecyclerView) findViewById(R.id.rv_MenuPrincipal);

                LinearLayoutManager lm = new LinearLayoutManager(Inicio_Menu_RecyclerView.this);
                rv.setLayoutManager(lm);

                ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

                ArrayList<Constructor_Dato> arrayPartidas = new ArrayList<>();
                arrayPartidas.add(new Constructor_Dato("Ordenes completadas", "100"));
                arrayPartidas.add(new Constructor_Dato("Ordenes Validadas", "10"));
                arrayPartidas.add(new Constructor_Dato("Ordednes canceladas", "2"));


                ArrayDocumentos.add(new constructor_Documento("0000011111", "Orden de producciòn", arrayPartidas));
                ArrayDocumentos.add(new constructor_Documento("0000022222", "Orden de surtido", arrayPartidas));
                ArrayDocumentos.add(new constructor_Documento("0000033333", "Orden de embarque", arrayPartidas));

                Adaptador_RV_MenuPrincipal adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);
                rv.setHasFixedSize(false);
                rv.setAdapter(adv);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);



            }catch (Exception e)
            {
                e.printStackTrace();
            }

    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)

                .setTitle("¿Desea cerrar la sesión?").setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
                        SharedPreferences.Editor edit = pref.edit();

                       edit.putString("usuario","");
                        edit.apply();

                       // String usuarioPref = pref.getString("usuario", "null");
                        Inicio_Menu_RecyclerView.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        //   super.onBackPressed();
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
            Intent i = new Intent(Inicio_Menu_RecyclerView.this, Incidencia.class);
            startActivity(i);
        //  new sobreDispositivo(contexto,vista);
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }
}
