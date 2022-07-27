package com.automatica.AXCMP.c_Inventarios;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.automatica.AXCMP.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCMP.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.c_Inventarios.CiclicosPorProducto.Inventarios_PorProducto;

import java.util.ArrayList;

public class Inventarios_Menu extends AppCompatActivity
{

    SoapAction sa = new SoapAction();

    ListView ListaAlmacen;
    Context contexto = this;
    View vista;

    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String decision,mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.principal_activity_menu);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //toolbar.setSubtitle("  Inventarios");

            getSupportActionBar().setTitle(getString(R.string.menu_inventarios));

            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

            ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();

            new cambiaColorStatusBar(contexto, R.color.RojoStd, Inventarios_Menu.this);

            int iconoAlmacen1 = R.drawable.icono_inv_v5_inventarios_ciclico_parte;
            int iconoAlmacen2 = R.drawable.icono_inv_v5_inventarios_ciclico_pos;
            int iconoAlmacen3 = R.drawable.icono_inv_v5_inventarios_fisico_total;


            constructorTablaMenuPrincipal cicNumeroParte = new constructorTablaMenuPrincipal(iconoAlmacen1, "Cíclico por" + "\n" + "Producto", "Descripción Ciclicos por" + " \n" + "Producto");
            constructorTablaMenuPrincipal cicLote = new constructorTablaMenuPrincipal(iconoAlmacen2, "Cíclico por posición", "Descripción Ciclicos por Lote");
            constructorTablaMenuPrincipal fisicoTotal = new constructorTablaMenuPrincipal(iconoAlmacen3, "Físico Total", "Descripción Físico Total");

            lista.add(cicNumeroParte);
            lista.add(cicLote);
            lista.add(fisicoTotal);


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
                            intent = new Intent(Inventarios_Menu.this, Inventarios_CiclicoPorLote.class);
                            PorEjecutar = "CiclicoPorPosicion";
                            break;
                        case 2:
                            intent = new Intent(Inventarios_Menu.this, Inventarios_InventiarioCiclicoPorPosicionDet.class);
                            PorEjecutar = "Fisico Total";
                            break;

                    }
                    SegundoPlano sp = new SegundoPlano(PorEjecutar, intent);
                    if (intent != null && PorEjecutar != null) sp.execute();
                }
            });

            adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Inventarios_Menu.this, R.layout.menu_item, lista);
            ListaAlmacen.setAdapter(adaptador);
        }catch (Exception e)
        {
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
            e.printStackTrace();

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
            new sobreDispositivo(contexto,vista);
        }


        return super.onOptionsItemSelected(item);
    }



    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        Bundle b = new Bundle();
        String Actividad;
        Intent intent;
        public SegundoPlano(String Actividad, Intent intent)
        {
            this.Actividad = Actividad;
            this.intent=intent;
        }
        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            switch (Actividad)
            {
                case"CiclicoPorPosicion":
                sa.SOAPConsultaInventarioCiclicoPosicion(contexto);

                break;
                case"CiclicoPorProducto":
                sa.SOAPConsultaInventarioCiclicoNumeroParte(contexto);
                break;
                case"Fisico Total":
                sa.SOAPConsultaInventarioFisico(contexto);
                b.putString("Actividad","Fisico Total");
                b.putString("Ubicacion","nula");
                    break;

            }
            decision = sa.getDecision();
            mensaje = sa.getMensaje();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            if(decision.equals("true"))
            {


                Log.i("SoapResponse", "ID INVENTARIO" + String.valueOf(mensaje));
                b.putString("IdInventario",mensaje);
                intent.putExtras(b);
                if(intent!=null)startActivity(intent);
            }
            if (decision.equals("false"))
            {
                new popUpGenerico(contexto,vista,mensaje,"Advertencia",true,true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }
    }


}
