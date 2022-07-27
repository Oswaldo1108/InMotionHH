package com.automatica.AXCPT.c_Inventarios.Menus;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCPT.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import java.util.ArrayList;

public class Inventarios_Menu extends AppCompatActivity {
    ListView ListaAlmacen;
    Context contexto = this;
    View vista;
    String Almacen;
    Spinner spinner;
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_inventarios__menu);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //toolbar.setSubtitle("  Inventarios");

            getSupportActionBar().setTitle("Inventarios");
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            spinner = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Almacen = ((Constructor_Dato) spinner.getSelectedItem()).getTag2();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();

            new cambiaColorStatusBar(contexto, R.color.RojoStd, Inventarios_Menu.this);

            int iconoAlmacen1 = R.drawable.icono_inv_v5_inventarios_ciclico_parte;
            int iconoAlmacen2 = R.drawable.icono_inv_v5_inventarios_ciclico_pos;
            int iconoAlmacen3 = R.drawable.icono_inv_v5_inventarios_fisico_total;
            int iconoAlmacen4 = R.drawable.icono_inv_v5_inventarios_desktop;


            constructorTablaMenuPrincipal cicNumeroParte = new constructorTablaMenuPrincipal(iconoAlmacen1, "Cíclico por" + "\n" + "Producto", "Descripción Ciclicos por" + " \n" + "Producto");
            constructorTablaMenuPrincipal cicLote = new constructorTablaMenuPrincipal(iconoAlmacen2, "Cíclico por Posición", "Descripción Ciclicos por Lote");
            constructorTablaMenuPrincipal fisicoTotal = new constructorTablaMenuPrincipal(iconoAlmacen3, "Físico Total", "Descripción Físico Total");
            constructorTablaMenuPrincipal porLote = new constructorTablaMenuPrincipal(iconoAlmacen4, "Ciclico por\n lote", "Descripción Ciclico por\n lote");

            lista.add(cicNumeroParte);
            lista.add(cicLote);
            lista.add(fisicoTotal);
            lista.add(porLote);


            ListaAlmacen = (ListView) findViewById(R.id.lstv_MenuPrincipal);
            ListaAlmacen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = null;
                    String PorEjecutar = null;
                    switch (position) {
                        case 0:
                            intent = new Intent(Inventarios_Menu.this, Inventarios_PorProducto.class);
                            PorEjecutar = "CiclicoPorProducto";
                            break;
                        case 1:
                            intent = new Intent(Inventarios_Menu.this, Inventarios_PorPosicion.class);
                            PorEjecutar = "CiclicoPorPosicion";
                            break;
                        case 2:
                            intent = new Intent(Inventarios_Menu.this, Inventarios_PantallaPrincipal.class);
                            PorEjecutar = "Fisico Total";
                            break;
                        case 3:
                            intent = new Intent(contexto, Inventarios_PorPosicion.class);
                            PorEjecutar = "PorLote";
                            break;

                    }
                    SegundoPlano sp = new SegundoPlano(PorEjecutar, intent);
                    if (intent != null && PorEjecutar != null) sp.execute();
                    //startActivity(intent);
                }
            });

            adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Inventarios_Menu.this, R.layout.menu_item, lista);
            ListaAlmacen.setAdapter(adaptador);
        } catch (Exception e) {
            new popUpGenerico(contexto, vista, e.getMessage(), "Advertencia", true, true);
            e.printStackTrace();

        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new SegundoPlano("Almacenes", null).execute();
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


    class SegundoPlano extends AsyncTask<Void, Void, Void> {
        DataAccessObject dao;
        adInventarios ca = new adInventarios(contexto);
        Bundle b = new Bundle();
        String Actividad;
        Intent intent;

        public SegundoPlano(String Actividad, Intent intent) {
            this.Actividad = Actividad;
            this.intent = intent;
        }

        @Override
        protected void onPreExecute() {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            switch (Actividad) {
                case "CiclicoPorPosicion":

                    dao = ca.c_ConsultaInvCiclicoPosicion(Almacen);
                    b.putString("Actividad", "Posición");
                    break;
                case "CiclicoPorProducto":

                    dao = ca.c_ConsultaInvCiclicoNumParte(Almacen);
                    b.putString("Actividad", "Producto");
                    break;
                case "Fisico Total":

                    dao = ca.c_ConsultaInvFisico(Almacen);
                    b.putString("Actividad", "Fisico Total");
                    b.putString("Ubicacion", "nula");
                    break;
                case "PorLote":
                    dao = ca.c_ConsultaInvCiclicoPorLote(Almacen);
                    b.putString("Actividad", "Lote");
                    break;
                case "Almacenes":
                    dao = ca.c_ConsultaAlmacen("@", "@");
                    break;

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (dao.iscEstado()) {
                switch (Actividad) {
                    case "Almacenes":
                        spinner.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Almacen", "ERPAlmacen", "IdAlmacen")));
                        break;
                    default:
                        b.putString("IdInventario", dao.getcMensaje());
                        b.putString("Almacen", Almacen);
                        intent.putExtras(b);
                        if (intent != null) startActivity(intent);
                        break;
                }
            } else {
                new popUpGenerico(contexto, vista, dao.getcMensaje(), "Advertencia", true, true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }


}
