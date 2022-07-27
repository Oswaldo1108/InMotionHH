package com.automatica.AXCMP.c_Surtido;

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
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.c_Surtido.Surtido_Bascula.Surtido_Seleccion_Registro_Partida;
import com.automatica.AXCMP.c_Surtido.Surtido_Pedidos.Surtido_Seleccion_Partida;
import java.util.ArrayList;


/*
* Para agregar opciones nuevas al menu dinamico, se debe agregar una entrada al arrayList de Intents llamado "ActionsList"
* Y una  entrada a el Array list de constructor Tabla menu principal
* Se debe respetar el orden en que se agrega, procurar agregarlo al final para no revolver las entradas
*/


public class Surtido_Menu_Dinamico extends AppCompatActivity
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
            getSupportActionBar().setTitle(getString(R.string.menu_embarques));


            new cambiaColorStatusBar(contexto, R.color.MoradoStd, Surtido_Menu_Dinamico.this);
            ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


            Bundle b = getIntent().getExtras();


            ArrayList<String> controlMenu = new ArrayList<>();
            ArrayList<constructorTablaMenuPrincipal> ct = new ArrayList<>();


            ArrayList<Intent> actionList = new ArrayList<>();
            actionList.add(new Intent(Surtido_Menu_Dinamico.this, Surtido_Seleccion_Partida.class));
            actionList.add(new Intent(Surtido_Menu_Dinamico.this, Surtido_Entrega.class));
            actionList.add(new Intent(Surtido_Menu_Dinamico.this, Surtido_Valida.class));
            actionList.add(new Intent(Surtido_Menu_Dinamico.this, Surtido_Seleccion_Registro_Partida.class));
           // actionList.add(new Intent(Surtido_Menu_Dinamico.this, Bascula_Pesaje.class));

            final ArrayList<Intent> actionListtoExecute = new ArrayList<>();

            int icono1 = R.drawable.icono_surt_v5_surtidodesktop;
            int icono2 = R.drawable.icono_surt_v5_entregaaprod;
            int icono4 = R.drawable.icono_surt_v5_validacion;

            int icono3 = R.drawable.iconobascula;


            constructorTablaMenuPrincipal e1= new constructorTablaMenuPrincipal(icono1, getString(R.string.Embarques_Surtido_pedido) , "Descripción Colocar");
            constructorTablaMenuPrincipal e3= new constructorTablaMenuPrincipal(icono2, getString(R.string.Embarques_Surtido_Entrega), "Descripción Reubicar");
            constructorTablaMenuPrincipal e4= new constructorTablaMenuPrincipal(icono4, getString(R.string.Embarques_Surtido_valida), "Descripción Reubicar");
            constructorTablaMenuPrincipal e2= new constructorTablaMenuPrincipal(icono3, getString(R.string.Embarques_Surtido_docificacion), "Descripción Reubicar");
          //  constructorTablaMenuPrincipal e5= new constructorTablaMenuPrincipal(icono3,"Bascula pesaje", "Descripción Reubicar");

            ct.add(e1);
            ct.add(e3);
            ct.add(e4);
            ct.add(e2);
           // ct.add(e5);

            String Apertura = getAperturaSharedPreferences(contexto); // b.getString("Apertura");
            String[] SeparadoApertura = Apertura.split(","); //

            for (int i = 0; i < SeparadoApertura[1].length(); i++) {
                //         Toast.makeText( this, SeparadoApertura[1], Toast.LENGTH_SHORT).show();
                controlMenu.add(Character.toString(SeparadoApertura[1].charAt(i)));
                Log.i(TAG, "onCreate: Creando Menu Principal, el código recibido fue:" + SeparadoApertura[1].charAt(i));

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

            adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Surtido_Menu_Dinamico.this, R.layout.menu_item, lista);
            listaMenu.setAdapter(adaptador);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
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
