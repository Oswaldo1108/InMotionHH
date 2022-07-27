package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_BajaEmpaque;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class Almacen_Ajustes_BajaEmpaqueSCH extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar
{

    //region variables
    cAccesoADatos_Almacen ca;
    TextView txtv_EmpaquesRegistrados,txtv_Pallet;
    String[] HEADER;
    EditText edtx_CodigoPallet,edtx_CodigoPalletConfirmacion,edtx_CantEmpaque,edtx_numparte;
    Spinner spnr_Ajuste;
    SortableTableView tabla;
    SimpleTableDataAdapter st;
    String productoT, loteT;
    SimpleTableHeaderAdapter sthd;
    int renglonSeleccionado;
    int renglonAnterior=-1;
    boolean Seleccionado,Recargar;
    private ProgressBarHelper p;

    frgmnt_taskbar_AXC taskbar_axc;
    View vista;
    Context contexto = this;
    String[] TipoAjuste;

    Handler h = new Handler();
    ArrayAdapter<String> spinnerArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_almacen__ajustes__baja_empaque_s_c_h);

        declaraVariables();
        agregaListeners();
        ca = new cAccesoADatos_Almacen(Almacen_Ajustes_BajaEmpaqueSCH.this);
        SegundoPlano sp = new SegundoPlano("ListarAjustes");
        sp.execute();
        View logoView = getToolbarLogoIcon((Toolbar) findViewById(R.id.toolbar));
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
        getSupportFragmentManager().beginTransaction().add(findViewById(R.id.FrameLayout).getId(),taskbar_axc,"FragmentoTaskBar").commit();


    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
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
        if (p.ispBarActiva()){
            int id = item.getItemId();

            if((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto,vista);
            }
            if((id == R.id.borrar_datos))
            {
                reiniciaCampos();


            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void declaraVariables()
    {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(" "+getString(R.string.Ajustes_Baja_Empaque));

        tabla = findViewById(R.id.tableView_OC);
        new cambiaColorStatusBar(contexto,R.color.doradoLetrastd, Almacen_Ajustes_BajaEmpaqueSCH.this);


        edtx_CodigoPallet= (EditText) findViewById(R.id.edtx_CodigoPallet);
        edtx_CodigoPalletConfirmacion = (EditText) findViewById(R.id.edtx_ConfirmacionPallet);
        edtx_CantEmpaque = findViewById(R.id.edtx_CantEmpaque);
        edtx_CodigoPallet.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_CodigoPalletConfirmacion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_numparte=findViewById(R.id.edtx_numparte);
        spnr_Ajuste= (Spinner) findViewById(R.id.spnr_Ajuste);

        p= new ProgressBarHelper(this);

        // Spinner spinner = new Spinner(this);



    }
    private void agregaListeners()
    {
        try {
            tabla.addDataClickListener(new ListenerClickTabla());
            tabla.addHeaderClickListener(new headerClickListener());
            tabla.addDataLongClickListener(new ListenerLongClickTabla());
            edtx_CodigoPalletConfirmacion.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_CodigoPalletConfirmacion.getText().toString().equals(""))
                        {
                            if(edtx_CodigoPalletConfirmacion.getText().toString().equals(edtx_CodigoPallet.getText().toString()))
                            {
                                SegundoPlano sp = new SegundoPlano("AjusteBajaEmpaques");
                                sp.execute();

                            }else
                            {
                                new popUpGenerico(contexto,vista,getString(R.string.empaques_no_coinciden),"Advertencia",true,true);
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        edtx_CodigoPalletConfirmacion.setText("");
                                        edtx_CodigoPalletConfirmacion.requestFocus();
                                    }
                                });
                            }
                        }else
                        {
                            new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"Advertencia",true,true);
                            edtx_CodigoPalletConfirmacion.setText("");
                            edtx_CodigoPalletConfirmacion.requestFocus();
                            h.post(new Runnable() {
                                @Override
                                public void run() {

                                    edtx_CodigoPalletConfirmacion.setText("");
                                    edtx_CodigoPalletConfirmacion.requestFocus();
                                }
                            });
                        }

                        new esconderTeclado(Almacen_Ajustes_BajaEmpaqueSCH.this);
                    }
                    return false;
                }
            });



            edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(!edtx_CodigoPallet.getText().toString().equals(""))
                        {
                            SegundoPlano sp = new SegundoPlano("ConsultaEmpaque");
                            sp.execute();

                        }else
                        {
                            new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_empaque),"Advertencia",true,true);

                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    edtx_CodigoPallet.setText("");
                                    edtx_CodigoPallet.requestFocus();
                                }
                            });
                        }

                        new esconderTeclado(Almacen_Ajustes_BajaEmpaqueSCH.this);
                    }
                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void reiniciaCampos()
    {

        edtx_CodigoPallet.setText("");
        edtx_CantEmpaque.setText("");
        edtx_numparte.setText("");
        edtx_CodigoPalletConfirmacion.setText("");
        edtx_CodigoPallet.requestFocus();
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();
    }

    private class ListenerClickTabla implements TableDataClickListener<String[]>
    {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData)
        {
            if(renglonAnterior != rowIndex)
            {
                Seleccionado = true;
                renglonAnterior = rowIndex;
                renglonSeleccionado = rowIndex;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                productoT = clickedData[0];
                loteT= clickedData[5];
                edtx_numparte.setText(productoT);
                tabla.getDataAdapter().notifyDataSetChanged();

            }else if(renglonAnterior == rowIndex)
            {
                renglonSeleccionado = -1;
                tabla.setDataRowBackgroundProvider(new cambiaColorTablaSeleccionada());
                tabla.getDataAdapter().notifyDataSetChanged();
                renglonAnterior=-1;
                Seleccionado = false;
            }
        }

    }

    private class cambiaColorTablaSeleccionada implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {
            if(rowIndex == renglonSeleccionado)
            {
                Color = R.color.RengSelStd;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            }
            else
            {
                Color = R.color.Transparente;
                st.setTextColor(getResources().getColor(R.color.blancoLetraStd));

            }
            return new ColorDrawable(getResources().getColor(Color));
        }
    }
    private class cambiaColorTablaClear implements TableDataRowBackgroundProvider<String[]>
    {

        int Color;
        @Override
        public Drawable getRowBackground(int rowIndex, String[] rowData)
        {
            Color = R.color.Transparente;
            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
            return new ColorDrawable(getResources().getColor(Color));
        }
    }
    private class headerClickListener implements TableHeaderClickListener
    {
        @Override
        public void onHeaderClicked(int columnIndex)
        {
            String notifyText = HEADER[columnIndex];
            Toast toast = new Toast(contexto);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0,80);
            toast.makeText(contexto, notifyText, Toast.LENGTH_SHORT).show();

        }
    }
    private class ListenerLongClickTabla implements TableDataLongClickListener<String[]>
    {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData)
        {
            String DataToShow="";
            int i =0;
            for(String data:clickedData)
            {
                DataToShow +=HEADER[i] +" - "+ data + "\n";
                i++;
            }
            new popUpGenerico(contexto, vista, DataToShow, "Información", true, false);

            return false;
        }
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        DataAccessObject dao;
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Ajustes_BajaEmpaqueSCH.this);
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            p.ActivarProgressBar(tarea);

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            String CodigoEmpaque = edtx_CodigoPallet.getText().toString();

            switch (tarea)
            {

                case "ConsultaEmpaque":
                    dao = ca.c_ConsultaEmpaquesPallet_NE(CodigoEmpaque);
                    break;

                case "ListarAjustes"://Se usa para llenar el Spinner
                    dao= ca.c_ListarConceptosAjuste("2");
                    break;
                case "AjusteBajaEmpaques" :
                    String TipoEvento = ((Constructor_Dato)spnr_Ajuste.getSelectedItem()).getTag1();
                    dao = ca.c_AjusteBajaEmpaque_SelProd_NE(edtx_CodigoPallet.getText().toString(),edtx_numparte.getText().toString(),edtx_CantEmpaque.getText().toString(),TipoEvento);
                    break;

               /* case "AjusteNuevoEmpaquePalletExistente":
                    String Cantidad = edtx_Cantidad.getText().toString();
                    String CodigoEmpaque = edtx_CodigoEmpaque.getText().toString();
                    String TipoEvento = spnr_Ajuste.getSelectedItem().toString();
                    String Revision ="";
                    sa.SOAPAjusteNuevoEmpaquePalletExistente(CodigoEmpaque,CodigoPallet,Producto,Cantidad,Revision,TipoEvento,contexto);

                    decision =   sa.getDecision();
                    mensaje = sa.getMensaje();
                    break;*/


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                if (dao.iscEstado())
                {
                    switch (tarea)
                    {


                        case "ConsultaEmpaque":
                            HEADER = dao.getcEncabezado();
                            tabla.setDataAdapter(st = new SimpleTableDataAdapter(Almacen_Ajustes_BajaEmpaqueSCH.this,dao.getcTablaUnica()));
                            tabla.setHeaderAdapter(sthd= new SimpleTableHeaderAdapter(Almacen_Ajustes_BajaEmpaqueSCH.this, dao.getcEncabezado()));
                            tabla.getDataAdapter().notifyDataSetChanged();
                            st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            sthd.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            break;
                        case "ListarAjustes":
                            spnr_Ajuste.setAdapter(new CustomArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("Descripcion","IdAjuste","")));
                            break;
                        case "AjusteBajaEmpaques":
                            new popUpGenerico(contexto, vista, "Empaque dado de baja con éxito.","Registrado", true, true);
                            new SegundoPlano("ConsultaEmpaque").execute();
                            reiniciaCampos();
                            break;
                    }
                } else
                {
                    new popUpGenerico(contexto, vista, dao.getcMensaje(),"Advertencia", true, true);
                    reiniciaCampos();
                }
            }catch (Exception e){
                e.printStackTrace();
                //Log.e("Error", dao.getcMensaje());
            }

            p.DesactivarProgressBar(tarea);
        }
    }
    @Override
    public void onBackPressed() {
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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }
}
