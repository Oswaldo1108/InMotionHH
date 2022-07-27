package com.automatica.AXCPT.c_Almacen.Almacen;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.ReabastecimientoPicking.SinOrden.frgmnt_Reab_Pallet_SO;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.databinding.ActivityReabastecerPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;

public class ReabastecerPallet extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    ActivityReabastecerPalletBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Context contexto = this;
    private ProgressBarHelper p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReabastecerPalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configurarToolbar();
        configurarTaskbar();
        agregarListener();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {

    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reabastecimiento");
        getSupportActionBar().setSubtitle("Pallet");
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
    }

    private void configurarTaskbar() {
        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance("", "");
        getSupportFragmentManager().beginTransaction().add(R.id.Pantalla_principal, taskbar_axc, "FragmentoTaskBar").commit();
    }

    private void agregarListener(){

        binding.edtxEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if(binding.edtxEmpaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(ReabastecerPallet.this,null, "Ingrese el pallet.", false, true, true);
                        return false;
                    }

                    new ReabastecerPallet.SegundoPlano("ConsultaPallet");

                    new esconderTeclado(ReabastecerPallet.this);
                }
                return false;
            }
        });

        binding.edtxConfirmarEmpaque.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {


                    if(binding.edtxEmpaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(ReabastecerPallet.this,null, "Ingrese el pallet.", false, true, true);
                        return false;
                    }


                    if(binding.edtxConfirmarEmpaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(ReabastecerPallet.this,null, "Ingrese la posición.", false, true, true);

                        return false;
                    }

                    new ReabastecerPallet.SegundoPlano("RegistraPallet");

                    new esconderTeclado(ReabastecerPallet.this);
                }
                return false;
            }
        });

    }
    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {

        DataAccessObject dao;
        String tarea;
        View view;

        cAccesoADatos ca = new cAccesoADatos(contexto);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                p.ActivarProgressBar(tarea);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String...Params)
        {
            try
            {
                if(!this.isCancelled())
                {
                    switch (tarea)
                    {

                        case "ConsultaPallet":

                            dao = ca.c_ConsultaPallet(Params[0]);

                            break;

                        case "RegistraPallet":

                            dao = ca.c_ReabastecePalletSO(binding.edtxEmpaque.getText().toString(),binding.edtxConfirmarEmpaque.getText().toString());

                            break;

                        default:
                            dao = new DataAccessObject(false,"Operación no soportada",null);
                            break;
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                dao = new DataAccessObject(false,e.getMessage(),null);

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
                        case "ConsultaPallet":

                            binding.txtvPalletProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.txtvPalletCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.txtvPalletCantidadEmpaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.txtvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("UnidadMedida"));
                            binding.txtvPalletLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));
                            binding.edtxConfirmarEmpaque.requestFocus();
                            break;
                        case "RegistraPallet":

                            new popUpGenerico(contexto,null, "Pallet reabastecido con éxito en la posición [" + binding.edtxConfirmarEmpaque.getText().toString()+"]", dao.iscEstado(), true, true);
                            binding.edtxEmpaque.setText("");
                            binding.edtxConfirmarEmpaque.setText("");
                            binding.txtvPalletProducto.setText("");
                            binding.txtvPalletCantidad.setText("");
                            binding.txtvPalletCantidadEmpaques.setText("");
                            binding.txtvEstatus.setText("");
                            binding.txtvPalletLote.setText("");
                            binding.edtxEmpaque.requestFocus();
                            break;

                    }

                }else
                {
                    new popUpGenerico(contexto,null, dao.getcMensaje(), dao.iscEstado(), true, true);

                    binding.edtxEmpaque.setText("");
                    binding.edtxConfirmarEmpaque.setText("");
                    binding.txtvPalletProducto.setText("");
//                            txtv_Lote.setText("");
                    binding.txtvPalletCantidad.setText("");
                    binding.txtvPalletLote.setText("");
                    binding.txtvPalletCantidadEmpaques.setText("");
                    binding.txtvEstatus.setText("");
                }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(ReabastecerPallet.this,null, e.getMessage(),dao.iscEstado(), true, true);
            }

            p.DesactivarProgressBar();
        }


    }

}