package com.automatica.AXCPT.c_Almacen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.automatica.AXCPT.Principal.adaptadorTablaMenuPrincipal;
import com.automatica.AXCPT.Principal.constructorTablaMenuPrincipal;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu;
import com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.Seleccion_DevolucionPT;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Almacen_Transferencias_Menu;
import com.automatica.AXCPT.c_Almacen.Reubicacion.Almacen_Reubicar;
import com.automatica.AXCPT.c_Almacen.Reubicacion.Reubicar_Menu_Dinamico;

import java.util.ArrayList;


/*
* Para agregar opciones nuevas al menu dinamico, se debe agregar una entrada al arrayList de Intents llamado "ActionsList"
* Y una  entrada a el Array list de constructor Tabla menu principal
* Se debe respetar el orden en que se agrega, procurar agregarlo al final para no revolver las entradas
*
* */


public class Almacen_Menu_Dinamico extends AppCompatActivity  implements frgmnt_Seleccion_Producto.OnFragmentInteractionListener
{
    String TAG = "InicioMenu";
    ListView listaMenu;
    View vista;
    Context contexto = this;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.menu_almacen));



        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Menu_Dinamico.this);
        ArrayList<constructorTablaMenuPrincipal> lista = new ArrayList<>();


        Bundle b = getIntent().getExtras();


        ArrayList<String> controlMenu = new ArrayList<>();
        ArrayList<constructorTablaMenuPrincipal> ct = new ArrayList<>();


        ArrayList<Intent> actionList = new ArrayList<>();

        actionList.add(new Intent(Almacen_Menu_Dinamico.this, Almacen_Colocar.class));
        actionList.add(new Intent(Almacen_Menu_Dinamico.this, Reubicar_Menu_Dinamico.class));
        actionList.add(new Intent(Almacen_Menu_Dinamico.this, Almacen_Transferencias_Menu.class));
        actionList.add(new Intent(Almacen_Menu_Dinamico.this, Almacen_Ajustes_Menu.class));
        actionList.add(new Intent(Almacen_Menu_Dinamico.this, Seleccion_DevolucionPT.class));
        actionList.add(new Intent(Almacen_Menu_Dinamico.this,Almacen_Impresion_Etiquetas.class));

   
        final ArrayList<Intent> actionListtoExecute = new ArrayList<>();

        int iconoAlmacen1 = R.drawable.icono_mov_alm_v5_colocacion;
        int iconoAlmacen2 = R.drawable.icono_mov_alm_v5_reubicacion;
        int iconoAlmacen3 = R.drawable.icono_mov_alm_v5_baja_pallet;
        int iconoAlmacen4 = R.drawable.icono_mov_alm_v5_devolucion_linea;
        int iconoAlmacen5 = R.drawable.icono_mov_alm_v5_consolidacion_tarimas;
        int iconoAlmacen6= R.drawable.icono_mov_alm_v5_ajustes_inventario;
        int iconoAlmacen7 = R.drawable.icono_mov_alm_v5_transfer_plantas;
        int iconoAlmacen8= R.drawable.armado_tarimas_liquidos;
        int iconoAlmacen9= R.drawable.armado_tarimas_polvos;
        int iconoAlmacen10= R.drawable.registro_de_produccion;
        int iconoDevolucion = R.drawable.devolucion;
        int iconoEtiquetas= R.drawable.icono_mov_alm_v5_ajustes_inventario;

        constructorTablaMenuPrincipal Colocar = new constructorTablaMenuPrincipal(iconoAlmacen1,getString(R.string.Colocar),"Descripción Colocar");
        constructorTablaMenuPrincipal Reubicar = new constructorTablaMenuPrincipal(iconoAlmacen2,getString(R.string.Reubicar),"Descripción Reubicar");
        constructorTablaMenuPrincipal TraspasoRec= new constructorTablaMenuPrincipal(iconoAlmacen7,getString(R.string.almacen_traspaso),"Descripción Reubicar");
        constructorTablaMenuPrincipal Ajustes = new constructorTablaMenuPrincipal(iconoAlmacen6,getString(R.string.Ajustes),"Descripción Ajustes");
        constructorTablaMenuPrincipal Devolucion= new constructorTablaMenuPrincipal(iconoDevolucion,"Devolución","Descripción devolución");
        constructorTablaMenuPrincipal Impresion= new constructorTablaMenuPrincipal(iconoEtiquetas,"Impresión Etiquetas","");

        ct.add(Colocar);
        ct.add(Reubicar);
        ct.add(TraspasoRec);
       ct.add(Ajustes);
       ct.add(Devolucion);
       ct.add(Impresion);

        String Apertura = getAperturaSharedPreferences(contexto); // b.getString("Apertura");
        String[] SeparadoApertura = Apertura.split(","); //

        for(int i = 0;i <SeparadoApertura[1].length();i++)
        {

            controlMenu.add(Character.toString(SeparadoApertura[1].charAt(i)));
            Log.i(TAG, "onCreate: Creando Menu Principal, el código recibido fue:" + SeparadoApertura[1].charAt(i));

            if(controlMenu.get(i).equals("1")&&controlMenu.size()<=ct.size())
            {
                lista.add(ct.get(i));
                actionListtoExecute.add(actionList.get(i));
            }else
            {
                Log.e(TAG, "onCreate: Error Creando Menu Principal, el código recibido fue:" + SeparadoApertura[0] );
            }
        }


        listaMenu = (ListView) findViewById(R.id.lstv_MenuPrincipal);
        listaMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = null;
                intent = actionListtoExecute.get(position);
               if(intent!=null)startActivity(intent);
            }
        });

        adaptadorTablaMenuPrincipal adaptador = new adaptadorTablaMenuPrincipal(Almacen_Menu_Dinamico.this, R.layout.menu_item, lista);
        listaMenu.setAdapter(adaptador);
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

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }
    

    @Override
    public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {

    }
}
