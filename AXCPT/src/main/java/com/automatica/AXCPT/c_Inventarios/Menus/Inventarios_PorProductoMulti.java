package com.automatica.AXCPT.c_Inventarios.Menus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.CustomExceptionHandler;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Embarques.Reempaque.Reempaque_Ciesa.Reempaque_Reempaque;
import com.automatica.AXCPT.c_Inventarios.Granel.Inventarios_ConfirmarEmpaqueGranel;
import com.automatica.AXCPT.objetos.ObjetoConstructor;
import com.automatica.AXCPT.objetos.spinnerCustomAdapter;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Inventarios_PorProductoMulti extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface,frgmnt_taskbar_AXC.interfazTaskbar
{
    //region variables
    Button btnIniciarInventario;
    SortableTableView tabla;
    boolean seleccion=false;
    private ActivityHelpers activityHelpers;
    TableViewDataConfigurator ConfigTabla = null;
    ProgressBarHelper progressBarHelper;
    Bundle b;
    View vista;
    String UbicacionIntent, IdInventario;
    Context contexto = this;
    Spinner spinnerCustom;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    com.automatica.axc_lib.Servicios.popUpGenerico pop = new com.automatica.axc_lib.Servicios.popUpGenerico(Inventarios_PorProductoMulti.this);


    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventarios_activity_por_producto_multi);
        getExtrasIntent();
        declararVariables();
        AgregaListeners();
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(Inventarios_PorProductoMulti.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // new SegundoPlano("llenarTabla").execute();
        new SegundoPlano("llenarSpinner").execute();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.TOMAR_INVENTARIO);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
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
            new sobreDispositivo(contexto, null);
        }
        if ((id == R.id.recargar)) {
            //tabla.getDataAdapter().clear();
            //SegundoPlano sp = new SegundoPlano("llenarTabla");
            new SegundoPlano("llenarSpinner").execute();
            //sp.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla)
    {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla)
    {
        if (ConfigTabla!=null){
            if(Seleccionado)
            {
                seleccion = true;
                UbicacionIntent = clickedData[0];
            }else
            {
                seleccion = false;
                UbicacionIntent = "";
            }
        }else {
            seleccion = false;
            UbicacionIntent = "";
        }
    }

    private void declararVariables()
    {
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenus(Inventarios_PorProductoMulti.this, R.id.Pantalla_principal, true);
        this.getSupportActionBar().setTitle(getString(R.string.Inventarios_CiclicoPorProducto));
        tabla  = (SortableTableView) findViewById(R.id.tableView_OC);
        progressBarHelper = new ProgressBarHelper(this);
        btnIniciarInventario = (Button) findViewById(R.id.btn_TomarInventario);
        spinnerCustom = findViewById(R.id.spinner);
        taskbar_axc= (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("","");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal,taskbar_axc,"FragmentoTaskBar").commit();
    }

    private void getExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            IdInventario = b.getString("IdInv");
        } catch (Exception e)
        {
            e.printStackTrace();
            new com.automatica.axc_lib.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
        }

    }

    private void AgregaListeners()
    {

        btnIniciarInventario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent = new Intent(Inventarios_PorProductoMulti.this, Inventarios_PantallaPrincipal.class);
                    boolean bandera = true;
                    if(seleccion)
                    {

                        if (Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(), "Tipo").getDato().equals("PICKING"))
                        {
                            intent = new Intent(Inventarios_PorProductoMulti.this, Inventarios_ConfirmarEmpaqueGranel.class);
                        }
                        else if (Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(), "Tipo").getDato().equals("CUARENTENA")){
                            validacionFinal();
                            bandera = false;
                        }
                        else
                        {
                            intent = new Intent(Inventarios_PorProductoMulti.this, Inventarios_PantallaPrincipal.class);
                        }

                        if (ConfigTabla!=null){
                            try {
                                if (seleccion){
                                    intent.putExtra("UbicacionIntent", Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(), "Posición").getDato());
                                    b.putString("UbicacionIntent", Constructor_Dato.getValue(ConfigTabla.getRenglonSeleccionado(), "Posición").getDato());
                                }else {
                                    intent.putExtra("UbicacionIntent", UbicacionIntent);
                                    b.putString("UbicacionIntent", "");

                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {

                        }


                    }else {

                    }
                    if(bandera) {
                        intent.putExtras(b);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_in_enter, R.anim.slide_right_out_enter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new com.automatica.axc_lib.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }

            }
        });
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                b.putString("IdInv",((ObjetoConstructor)spinnerCustom.getSelectedItem()).getTitulo());
                seleccion = false;
                new SegundoPlano("llenarTabla").execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        String tarea;
        DataAccessObject dao;
        adInventarios ca = new adInventarios(contexto);

        public SegundoPlano(String tarea) {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            progressBarHelper.ActivarProgressBar(tarea);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case "llenarTabla":
                        dao = ca.c_ConsultaInventarioNumParte(b.getString("IdInv"));
                        break;
                    case "llenarSpinner":
                        dao = ca.c_ConsultaInventariosNumParteAbiertos();
                        break;
                    default:
                        dao = new DataAccessObject();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                dao = new DataAccessObject(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {

                if (dao.iscEstado())
                {
                    switch (tarea)
                    {
                        case "llenarTabla":
                            if(!dao.getcMensaje().equals("") && dao.getcTablaUnica()!=null) {


                                if(ConfigTabla == null )
                                {
                                    ConfigTabla =  new TableViewDataConfigurator( 1,"REGISTRADO","SIN REGISTRO","RECIBIENDO",tabla, dao, Inventarios_PorProductoMulti.this);
                                }else
                                {
                                    ConfigTabla.CargarDatosTabla(dao);
                                }

                            }else{
                                tabla.getDataAdapter().clear();
                                tabla.getDataAdapter().notifyDataSetChanged();
                                new com.automatica.axc_lib.Servicios.popUpGenerico(contexto, getCurrentFocus(),"Sin registros", false, true, true);
                            }
                            break;

                        case "llenarSpinner":
                            if (dao!=null){

                                try {
                                    ArrayList<Constructor_Dato> cDatos = dao.getcTablasSorteadas("IdEjercicio","Comentario","NumParte");
                                    ArrayList<ObjetoConstructor> datos= new ArrayList<>();
                                    for (Constructor_Dato dato:
                                            cDatos) {
                                        //  Drawable drawable = getResources().getDrawable(R.drawable.ic_pallet_no_etiquetado);

                                        Bitmap bitmap = getBitmapFromVectorDrawable(contexto,R.drawable.ic_inventory);
                                        datos.add(new ObjetoConstructor(dato.getDato(),dato.getTag1(),bitmap));
                                    }
                                    Log.i("datos",datos.toString());
                                    spinnerCustom.setAdapter(new spinnerCustomAdapter(contexto,datos));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                            break;


                    }
                } else {
                    new com.automatica.axc_lib.Servicios.popUpGenerico(contexto, null, dao.getcMensaje(), dao.iscEstado(), true, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
            progressBarHelper.DesactivarProgressBar();

        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    @Override
    public void BotonDerecha() {

        btnIniciarInventario.callOnClick();

    }

    private void validacionFinal() {
        try{


            new PopUpMenuAXC().newInstance(taskbar_axc.getView().findViewById(R.id.BotonDer), Inventarios_PorProductoMulti.this, R.menu.popup_inventario, new PopUpMenuAXC.ContextMenuListener() {
                @Override
                public void listenerItem(MenuItem item) {
                    try{
                        switch (item.getItemId()){
                            case R.id.pallet:
                                intent= new Intent(contexto, Inventarios_PantallaPrincipal.class);

                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;
                            case R.id.picking:
                                intent= new Intent(contexto, Inventarios_ConfirmarEmpaqueGranel.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                break;

                            default:
                                pop.popUpGenericoDefault(vista,"Seleccione una opción",false);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        pop.popUpGenericoDefault(vista,e.getMessage(),false);
                    }
                }
            });
            //new PopUpMenuAXC(taskbar_axc.getView().findViewById(R.id.BotonDer),RecepcionSeleccionarPartidas.this,R.menu.popup_etiquetados);

        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {

        activityHelpers.getTaskbar_axc().onBackPressed();
        /*
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
        Intent intent = new Intent(Inventarios_PorProductoMulti.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);

         */
    }
}