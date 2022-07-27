package com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones.DevolucionesCIESA.Devolucion_DAP;
import com.automatica.AXCPT.databinding.ActivitySeleccionDevolucionBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;

import de.codecrafters.tableview.SortableTableView;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Seleccion_DevolucionPT extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar,PopUpMenuAXC.ContextMenuListener
{
    SortableTableView tabla;
    FrameLayout progressBarHolder;
    EditText edtx_devolucion;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    TableViewDataConfigurator ConfigTabla = null;
    Bundle b;
    View vista;
    Context contexto = this;
    frgmnt_taskbar_AXC taskbar_axc;
    boolean Seleccionado;
    Intent intent;
    boolean OrdenCompraSeleccionada,Recarga = true;
    ActivitySeleccionDevolucionBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding=ActivitySeleccionDevolucionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Devolución");
        b = new Bundle();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        declaraVariables();
        agregarListeners();

        View logoView = getToolbarLogoIcon(toolbar);
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
        getSupportFragmentManager().beginTransaction().add(binding.FrameLayout.getId(),taskbar_axc,"FragmentoTaskBar").commit();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!edtx_devolucion.getText().toString().equals(""))
        {
            new SegundoPlano("Tabla").execute();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.SELECCIONAR);
       // registerForContextMenu(taskbar_axc.getView().findViewById(R.id.BotonDer));
    }





    public  void  declaraVariables(){
            try
            {
                tabla = (SortableTableView) findViewById(R.id.tableView_OC);
                progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
                edtx_devolucion = findViewById(R.id.edtx_devolucion);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() ,false ,true,true );
            }
    }
    public void agregarListeners()
    {
        edtx_devolucion.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                new SegundoPlano("Tabla").execute();
                new esconderTeclado(Seleccion_DevolucionPT.this);
                return false;
            }
        });

    }

    @Override
    public void onTableHeaderClick(int columnIndex, String Header,String IdConfigurador)
    {
        Toast.makeText(contexto, Header, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto,String IdConfigurador)
    {
        new popUpGenerico(contexto, null, MensajeCompleto, "Información", true, false);

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado,String IdConfigurador)
    {
        if(this.Seleccionado = Seleccionado)
            {
                b.putString("Partida", clickedData[0]);
                b.putString("NumParte", clickedData[1]);
                b.putString("CantidadTotal", clickedData[3]);
                b.putString("CantidadRecibida", clickedData[4]);
                b.putString("UM", clickedData[6]);
                b.putString("CantidadEmpaques", clickedData[7]);
                b.putString("EmpaquesPallet", clickedData[8]);
                //btn_Siguiente.setEnabled(true);
                OrdenCompraSeleccionada = true;
            }else
            {
                OrdenCompraSeleccionada = false;

            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(Recarga)
        {
            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar)
            {
                new SegundoPlano("Tabla").execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BotonDerecha() {
        if (OrdenCompraSeleccionada){

            if(edtx_devolucion.getText().toString().equals(""))
            {
                new popUpGenerico(Seleccion_DevolucionPT.this,getCurrentFocus(),"Ingrese un documento de devolución.", false,true,true);
                return;
            }

            new PopUpMenuAXC(taskbar_axc.getView().findViewById(R.id.BotonDer),this,R.menu.popup_etiquetados);
            //showPopup(taskbar_axc.getView().findViewById(R.id.BotonDer));
            //taskbar_axc.getView().findViewById(R.id.BotonDer).showContextMenu();

        }else {
            new popUpGenerico(Seleccion_DevolucionPT.this,getCurrentFocus(),"Seleccione un campo", false,true,true);
        }

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    @Override
    public void listenerItem(MenuItem item) {
        switch (item.getItemId()){
            case R.id.etiquetado:
                b.putString("OrdenDevolucion",edtx_devolucion.getText().toString());
                intent = new Intent(Seleccion_DevolucionPT.this, Devolucion_Empaques.class);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                break;
            case R.id.PyU:
                b.putString("OrdenDevolucion",edtx_devolucion.getText().toString());
                intent = new Intent(Seleccion_DevolucionPT.this,Devolucion_PYU.class);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                break;
            case R.id.DAP:
                b.putString("OrdenDevolucion",edtx_devolucion.getText().toString());
                intent = new Intent(Seleccion_DevolucionPT.this, Devolucion_DAP.class);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                break;
            case R.id.contenedor:
                b.putString("OrdenDevolucion",edtx_devolucion.getText().toString());
                intent = new Intent(Seleccion_DevolucionPT.this,Devolucion_NE.class);
                intent.putExtras(b);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);

                break;
            default:
                intent=null;
                break;
        }
    }


    private class SegundoPlano extends AsyncTask<String, Void,Void>
    {

        String Tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca;
        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute()
        {
            Recarga = false;
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                ca= new cAccesoADatos_Almacen(Seleccion_DevolucionPT.this);
                switch (Tarea){
                    case "Tabla":
                        dao = ca.c_ListaDevoluciones(edtx_devolucion.getText().toString());
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (dao.iscEstado())
                {
                    switch (Tarea)
                        {
                            case "Tabla":
                                if(ConfigTabla == null)
                                    {
                                        ConfigTabla = new TableViewDataConfigurator(9, "RECIBIDA", "LIBERADA", "10", tabla, dao, Seleccion_DevolucionPT.this);
                                    }else{
                                    ConfigTabla.CargarDatosTabla(dao);
                                    }
                                OrdenCompraSeleccionada = false;
                                break;
                        }
                }else
                    {
                        new popUpGenerico(Seleccion_DevolucionPT.this,null,dao.getcMensaje(),false,true,true);
                        edtx_devolucion.setText("");
                        edtx_devolucion.requestFocus();
                    }

                }catch (Exception e){
                    new popUpGenerico(Seleccion_DevolucionPT.this,getCurrentFocus(),e.getMessage(),false,true,true);
                    e.printStackTrace();
                }
                Recarga = true;
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu")!=null){
            getSupportFragmentManager().popBackStack();
            return;
        }
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("FragmentoNoti")!=null||getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!=null){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            startActivity(new Intent(contexto,Inicio_Menu_Dinamico.class));
        }
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}