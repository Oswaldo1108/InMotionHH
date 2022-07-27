package com.automatica.AXCMP.c_Consultas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCMP.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCMP.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.sobreDispositivo;

import java.util.ArrayList;


/*
* Para agregar opciones nuevas al menu dinamico, se debe agregar una entrada al arrayList de Intents llamado "ActionsList"
* Y una su entrada a el Array list de constructor Tabla menu principal
* Se debe respetar el orden en que se agrega, procurar agregarlo al final para no revolver las entradas
*
* */


public class Consultas_Menu_Dinamico extends AppCompatActivity
{
    String TAG = "InicioMenu";
    ListView listaMenu;
    View vista;
    Context contexto = this;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.principal_activity_menu);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getString(R.string.menu_consultas));


            new cambiaColorStatusBar(contexto, R.color.AzulStd, Consultas_Menu_Dinamico.this);
            ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


            Bundle b = getIntent().getExtras();


            ArrayList<String> controlMenu = new ArrayList<>();
            ArrayList<constructorTablaMenuPrincipal> ct = new ArrayList<>();


            ArrayList<Intent> actionList = new ArrayList<>();
            actionList.add(new Intent(Consultas_Menu_Dinamico.this, consultas_ConsultaPosicion.class));
            actionList.add(new Intent(Consultas_Menu_Dinamico.this, Consultas_PalletV2.class));
            actionList.add(new Intent(Consultas_Menu_Dinamico.this, consultas_ConsultaEmpaque.class));
            actionList.add(new Intent(Consultas_Menu_Dinamico.this, consultas_ConsultaReferencia.class));
            actionList.add(new Intent(Consultas_Menu_Dinamico.this, consultas_BusquedaExistencia.class));

            final ArrayList<Intent> actionListtoExecute = new ArrayList<>();

            int iconoAlmacen1 = R.drawable.icono_consultas_v5consultas_posicion;
            int iconoAlmacen2 = R.drawable.icono_consultas_v5consultas_pallet;
            int iconoAlmacen3 = R.drawable.icono_consultas_v5consultas_empaque;
            int iconoAlmacen4 = R.drawable.icono_consultas_v5consultas_referencia;
            int iconoAlmacen5 = R.drawable.icono_consultas_v5consultas_existencias;
            constructorTablaMenuPrincipal Posicion = new constructorTablaMenuPrincipal(iconoAlmacen1, "Posición ", "Descripción Posición");
            constructorTablaMenuPrincipal Pallet = new constructorTablaMenuPrincipal(iconoAlmacen2, "Pallet", "Descripción Pallet");
            constructorTablaMenuPrincipal Empaque = new constructorTablaMenuPrincipal(iconoAlmacen3, "Empaque", "Descripción Empaque");
            constructorTablaMenuPrincipal Referencia = new constructorTablaMenuPrincipal(iconoAlmacen4, "Referencia", "Descripción Referencia");
            constructorTablaMenuPrincipal Existencias = new constructorTablaMenuPrincipal(iconoAlmacen5, "Existencias    ", "Descripción Existencias");
            ct.add(Posicion);
            ct.add(Pallet);
            ct.add(Empaque);
            ct.add(Referencia);
            ct.add(Existencias);


            String Apertura = getAperturaSharedPreferences(contexto); // b.getString("Apertura");
            String[] SeparadoApertura = Apertura.split(","); //

            for (int i = 0; i < SeparadoApertura[1].length(); i++) //La posicion dos es de donde se sacan los permisos de pantallas
            {
                //         Toast.makeText( this, SeparadoApertura[1], Toast.LENGTH_SHORT).show();
                controlMenu.add(Character.toString(SeparadoApertura[1].charAt(i)));
                Log.e(TAG, "onCreate: Error Creando Menu Principal, el código recibido fue:" + SeparadoApertura[1].charAt(i));
                if (controlMenu.get(i).equals("1") && controlMenu.size() <= ct.size()) {
                    lista.add(ct.get(i));
                    actionListtoExecute.add(actionList.get(i));
                } else {
                    Log.e(TAG, "onCreate: Error Creando Menu Principal, el código recibido fue:" + SeparadoApertura[0]);
                }
            }


            listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
            listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = null;
                    intent = actionListtoExecute.get(position);

                    if (intent != null) startActivity(intent);
                }
            });

            adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Consultas_Menu_Dinamico.this, R.layout.menu_item, lista);
            listaMenu.setAdapter(adaptador);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public String getAperturaSharedPreferences(Context contexto)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        String aperturaPref = pref.getString("apertura", "00000");
        return aperturaPref;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if((id == R.id.InformacionDispositivo))
        {
          new sobreDispositivo(contexto,vista);
        }


        return super.onOptionsItemSelected(item);
    }



}
