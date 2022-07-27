package com.automatica.AXCPT.c_Recepcion.Recepcion;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.databinding.ActivityRecepcionConteoBinding;

import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.Servicios.sobreDispositivo;

import de.codecrafters.tableview.SortableTableView;

public class RecepcionConteo extends AppCompatActivity implements TableViewDataConfigurator.TableClickInterface, frgmnt_taskbar_AXC.interfazTaskbar{

    private ProgressBarHelper p;
    TableViewDataConfigurator ConfigTabla_Totales = null;
    SortableTableView tabla;
    private static String strIdTabla = "strIdTablaTotales";
    View vista;
    Context contexto = this;
    Bundle b = new Bundle();
    popUpGenerico pop = new popUpGenerico(RecepcionConteo.this);
    boolean OrdenCompraSeleccionada, Recarga = true;
    ActivityRecepcionConteoBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    int conteo;
    String estado = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecepcionConteoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SacaDatosIntent();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tabla = findViewById(R.id.tableView_OC);
        p = new ProgressBarHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hoja de conteo");
        AgregarListeners();
        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("", "");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal, taskbar_axc, "FragmentoTaskBar").commit();
    }

    // MÉTODOS DEL CICLO DE VIDA DE ACTIVITY
    @Override
    protected void onResume() {

        new RecepcionConteo.SegundoPlano("ConsultaConteo").execute();
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.SELECCIONAR);
    }

    // MÉTODOS PARA LA TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {
            getMenuInflater().inflate(R.menu.ciclicos_recarga_toolbar, menu);
            return true;

        } catch (Exception e) {
            Toast.makeText(contexto, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (p.ispBarActiva())
        {

            int id = item.getItemId();
            if ((id == R.id.InformacionDispositivo)) {

                new sobreDispositivo(contexto, vista);
            }
            if (id == R.id.recargar) {

                new RecepcionConteo.SegundoPlano("ConsultaConteo").execute();
            }
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

    @Override
    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

    }

    @Override
    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

    }

    @Override
    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

    }

    // CLASE PARA LLAMAR A LOS WEBSERVICES
    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        cAccesoADatos_Recepcion ca = new cAccesoADatos_Recepcion(RecepcionConteo.this);
        String Tarea;
        DataAccessObject dao;

        private SegundoPlano(String Tarea) {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute() {

            p.ActivarProgressBar(Tarea);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                switch (Tarea) {

                    case "ConsultaConteo":
                        dao = ca.c_ConsultarStatusConteo(binding.tvHojaConteo.getText().toString(), binding.edtxDocumento.getText().toString(), conteo);
                        break;

                    case "RegistrarConteo":
                        dao = ca.c_RegistrarConteo(binding.tvHojaConteo.getText().toString(), binding.edtxDocumento.getText().toString(), Integer.parseInt(binding.edtxConteo.getText().toString()), conteo);
                        break;
                }
            } catch (Exception e) {

                dao = new DataAccessObject(e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            try {

                if (dao.iscEstado()) {

                    switch (Tarea) {

                        case "ConsultaConteo":
                            binding.tvNoConteo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Conteos"));
                            binding.tvContDisponible.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Disponibles"));
                            estado = dao.getSoapObject_parced().getPrimitivePropertyAsString("Estado");

                            TextView tv;
                            tv = findViewById(R.id.tvEstadoCont);
                            binding.tvEstadoCont.setText(estado);
                            if (estado=="Sin conteos")
                                tv.setTextColor(getResources().getColor(R.color.RojoSemaforo));
                            else if(estado=="Incorrecto")
                                tv.setTextColor(getResources().getColor(R.color.naranjaAXCMid));
                            else if(estado=="Correcto")
                                tv.setTextColor(getResources().getColor(R.color.VerdeRenglon));


                            break;

                        case "RegistrarConteo":
                            new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), "Conteo relizado", String.valueOf(dao.iscEstado()), true, true);
                            conteo++;
                            new RecepcionConteo.SegundoPlano("ConsultaConteo").execute();
                            binding.tvEstadoCont.setText(estado);
                            if (estado=="Sin conteos")
                                binding.tvEstadoCont.setTextColor(getResources().getColor(R.color.RojoSemaforo));
                            else if(estado=="Incorrecto")
                                binding.tvEstadoCont.setTextColor(getResources().getColor(R.color.naranjaAXCMid));
                            else if(estado=="Correcto")
                                binding.tvEstadoCont.setTextColor(getResources().getColor(R.color.verdeAXCMid));

                            break;
                    }
                } else {

                    pop.popUpGenericoDefault(vista, dao.getcMensaje(), false);

                }

            } catch (Exception e) {
                e.printStackTrace();
                pop.popUpGenericoDefault(vista, e.getMessage(), false);

            }
            p.DesactivarProgressBar(Tarea);
        }
    }

    // MÉTODOS ADICIONALES


    private void SacaDatosIntent(){
        try
        {
            b  = getIntent().getExtras();
            binding.edtxDocumento.setText(b.getString("Partida"));
            binding.tvHojaConteo.setText(b.getString("Documento").toString());
            binding.tvNumParte.setText(b.getString("NumParte").toString());
            binding.tvDescripcion.setText(b.getString("Descripcion").toString());
            binding.tvUM.setText(b.getString("UM").toString());
            conteo = b.getInt("Conteo");

            /*int conteo = b.getInt("Conteo");
            binding.tvNoConteo.setText((conteo)+"");
            binding.tvContDisponible.setText((3-conteo)+"");

            if (conteo==0){
                binding.tvEstadoCont.setText("Sin conteos");
            }*/

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void AgregarListeners() {

        binding.edtxConteo.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(binding.tvEstadoCont.getText().equals("Correcto"))
                        pop.popUpGenericoDefault(vista, "Conteo correcto", false);
                    else
                        new RecepcionConteo.SegundoPlano("RegistrarConteo").execute();


                }
                return false;
            }


        });

    }

}