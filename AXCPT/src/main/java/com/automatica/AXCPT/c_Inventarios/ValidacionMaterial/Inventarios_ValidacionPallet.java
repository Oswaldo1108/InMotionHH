package com.automatica.AXCPT.c_Inventarios.ValidacionMaterial;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_SKU_Conteo;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Inventarios.CreacionMaterial.Inventarios_Menu_TipoRegNuevo;
import com.automatica.AXCPT.databinding.ActivityMenuBinding;
import com.automatica.AXCPT.databinding.InvActValidaPalletBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

public class Inventarios_ValidacionPallet extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar, frgmnt_SKU_Conteo.OnFragmentInteractionListener
{
    //region variables
    private final static String ConsultaPallet  = "ConsultaPallet";
    private final static String ConfirmarPallet = "ConfirmarPallet";
    private final static String PalletEditado   = "PalletEditado";

    private EditText edtx_Pallet, edtx_Lote, edtx_ConfirmarPallet;
    private TextView txtv_Inventario, txtv_Posicion;
    private TextView txtv_Producto, txtv_Desc, txtv_TipoReg,tvCantidadTotal;
    private EditText edtx_Unidades, edtx_Empaques;
    private Button btn_Empaques, btn_Confirmar;
    private CheckBox chk_Editar;
    private String str_Pallet;
    private String UbicacionIntent, IdInventario;
    private Bundle b;
    private Handler handler = new Handler();
    private Context contexto = this;
    private ProgressBarHelper progressBarHelper;


    frgmnt_taskbar_AXC taskbar_axc;
    InvActValidaPalletBinding binding;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            binding = InvActValidaPalletBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            this.getSupportActionBar().setTitle(getString(R.string.Inventarios_ValidaPallet));

            new cambiaColorStatusBar(contexto, R.color.grisAXC, Inventarios_ValidacionPallet.this);
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

            DeclararVariables();
            SacaExtrasIntent();
            AgregaListeners();
            if (str_Pallet != null && !str_Pallet.equals(""))
            {
                edtx_Pallet.setText(str_Pallet);

                new SegundoPlano("ConsultaPallet").execute();
            }


            txtv_Inventario.setText(IdInventario);
            txtv_Posicion.setText(UbicacionIntent);
        } catch (Exception e) {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

        }
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }

    private void DeclararVariables()
    {
        try
        {

            progressBarHelper = new ProgressBarHelper(this);

            edtx_Pallet = (EditText) findViewById(R.id.txtv_Pallet);
            edtx_Lote = (EditText) findViewById(R.id.edtx_Lote_Empaque);
            edtx_ConfirmarPallet = (EditText) findViewById(R.id.edtx_ConfirmarPallet);
            edtx_Unidades = (EditText) findViewById(R.id.edtx_Unidades);
            edtx_Empaques = (EditText) findViewById(R.id.edtx_Empaques);
            tvCantidadTotal = findViewById(R.id.tvCanttotal);
            edtx_ConfirmarPallet.setEnabled(false);

            edtx_Pallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_Lote.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            edtx_ConfirmarPallet.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            edtx_Lote.setEnabled(false);




            txtv_Inventario = (TextView) findViewById(R.id.txtv_Inventario);
            txtv_Posicion = (TextView) findViewById(R.id.txtv_Posicion);

            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_Desc = (TextView) findViewById(R.id.txtv_Desc);
            txtv_TipoReg = (TextView) findViewById(R.id.txtv_TipoReg);

            edtx_Unidades.setEnabled(false);
            edtx_Empaques.setEnabled(false);

            btn_Empaques = (Button) findViewById(R.id.btn_Empaques);
            btn_Confirmar = (Button) findViewById(R.id.btn_Confirmar);

            chk_Editar = (CheckBox) findViewById(R.id.chkbx_Editar);
            chk_Editar.setEnabled(false);

            btn_Confirmar.setEnabled(false);
            btn_Empaques.setEnabled(false);
        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

        }
    }

    private void SacaExtrasIntent()
    {
        try
        {
            b = getIntent().getExtras();
            UbicacionIntent = b.getString("UbicacionIntent");
            IdInventario = b.getString("IdInv");
            str_Pallet = b.getString("CodigoPallet");



        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "Advertencia", true, true);
        }
    }

    private void AgregaListeners()
    {
        try
        {

            edtx_Unidades.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(R.id.Pantalla_principal, frgmnt_SKU_Conteo.newInstance(null, txtv_Producto.getText().toString()), "Fragmentosku").addToBackStack("Fragmentosku").commit();
                    return true;
                }
            });
            edtx_Unidades.setCustomSelectionActionModeCallback(new ActionMode.Callback2()
            {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });

            chk_Editar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {


                        edtx_Lote.setEnabled(b);
                        edtx_Unidades.setEnabled(b);
                        edtx_Empaques.setEnabled(b);


                }
            });


            edtx_Pallet.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (edtx_Pallet.getText().toString().equals(""))
                        {

                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Pallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet), "false", true, true);
                            return false;
                        }

                        new SegundoPlano("ConsultaPallet").execute();
                        new esconderTeclado(Inventarios_ValidacionPallet.this);
                    }

                    return false;
                }
            });
            edtx_Lote.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (edtx_Lote.getText().toString().equals(""))
                        {
                            new popUpGenerico(contexto, getCurrentFocus(), "Ingrese un lote.", "Advertencia", true, true);
                            return false;
                        }
                    }
                    return false;
                }
            });
            edtx_ConfirmarPallet.setOnKeyListener(new View.OnKeyListener()
            {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        if (edtx_Pallet.getText().toString().equals(""))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_Pallet.requestFocus();
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_ingrese_pallet), false, true, true);
                            return false;
                        }

                        if (!edtx_Pallet.getText().toString().equals(edtx_ConfirmarPallet.getText().toString()))
                        {
                            handler.post(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    edtx_ConfirmarPallet.requestFocus();
                                    edtx_ConfirmarPallet.setText("");
                                }
                            });
                            new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.pallets_no_coinciden), false, true, true);
                            return false;
                        }

                        if(chk_Editar.isChecked())
                        {
                            if (txtv_TipoReg.getText().toString().equals("Etiquetado"))
                            {
                                new popUpGenerico(contexto,edtx_ConfirmarPallet, "Solo puede modificar pallets NO ETIQUETADOS de esta manera.", false, true, true);
                                return false;
                            }

                            if (txtv_Producto.getText().toString().equals("COMPARTIDO"))
                            {
                                new popUpGenerico(contexto,edtx_ConfirmarPallet, "Solo puede modificar pallets NO ETIQUETADOS de UN SOLO PRODUCTO de esta manera.", false, true, true);
                                return false;
                            }

                            if (Double.parseDouble(edtx_Unidades.getText().toString())<=0){
                                new popUpGenerico(contexto,edtx_ConfirmarPallet, getResources().getString(R.string.error_cantidad_valida), false, true, true);
                                return false;
                            }
                            new SegundoPlano(PalletEditado).execute();
                        }
                        else
                        {
                            new SegundoPlano(ConfirmarPallet).execute();
                        }

                        new esconderTeclado(Inventarios_ValidacionPallet.this);
                    }
                    return false;
                }
            });

            btn_Confirmar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    edtx_ConfirmarPallet.setEnabled(true);
                    edtx_ConfirmarPallet.requestFocus();

                }
            });
            btn_Empaques.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent;

                    if (edtx_Pallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(contexto, getCurrentFocus(), getString(R.string.error_pallet_no_seleccionado), false, true, true);
                        return;
                    }

                        intent  = new Intent(Inventarios_ValidacionPallet.this, Inventarios_ConfirmarEmpaque.class);

                    intent.putExtras(getIntent().getExtras());
                    intent.putExtra("CodigoPallet", edtx_Pallet.getText().toString());
                    intent.putExtra("UbicacionIntent",UbicacionIntent);
                    intent.putExtra("IdInv",IdInventario);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                }
            });

        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
            return true;
        } catch (Exception ex)
        {
            Toast.makeText(this, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if ((id == R.id.InformacionDispositivo))
        {
            new sobreDispositivo(contexto, getCurrentFocus());
        }
        if ((id == R.id.borrar_datos))
        {

            edtx_Pallet.requestFocus();
            reiniciaVariables();
        }


        return super.onOptionsItemSelected(item);
    }

    private void reiniciaVariables()
    {
        try
        {
            edtx_Pallet.setText("");
            edtx_ConfirmarPallet.setText("");
            edtx_Pallet.setText("");
            txtv_Producto.setText("");
            txtv_Desc.setText("");
            tvCantidadTotal.setText("");
            txtv_TipoReg.setText("");
            edtx_Lote.setText("");
            edtx_Unidades.setText("");
            edtx_Empaques.setText("");
            edtx_ConfirmarPallet.setText("");
            btn_Empaques.setEnabled(false);
            btn_Confirmar.setEnabled(true);
        } catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "Advertencia", true, true);

        }
    }

    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();

    }

    @Override
    public boolean ActivaProgressBar(Boolean estado) {
        return false;
    }

    @Override
    public void RegistrarCantidad(String Producto, String strCantidadEscaneada) {
        edtx_Unidades.setText(strCantidadEscaneada);
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void>
    {
        String tarea;
        DataAccessObject dao;
        adInventarios ca = new adInventarios(contexto);

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }

        @Override
        protected void onPreExecute()
        {
            try
            {
                progressBarHelper.ActivarProgressBar(tarea);
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                switch (tarea)
                {
                    case ConsultaPallet:
                            dao = ca.c_ConsultaPalletInventario(IdInventario, edtx_Pallet.getText().toString());
                        break;
                    case ConfirmarPallet:
                            dao = ca.c_RegistraPalletInvSinCambio(IdInventario, edtx_ConfirmarPallet.getText().toString(), UbicacionIntent);
                        break;
                    case PalletEditado:
                            dao = ca.c_EditaRegistroPalletInventario(IdInventario,
                                                                    edtx_Pallet.getText().toString(),
                                                                    UbicacionIntent,
                                                                    edtx_Lote.getText().toString(),
                                                                    edtx_Unidades.getText().toString(),
                                                                    edtx_Empaques.getText().toString());
                        break;
                    default:
                        dao = new DataAccessObject();
                        break;
                }
            } catch (Exception e)
            {
                dao = new DataAccessObject(e);
                e.printStackTrace();
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
                        case ConsultaPallet:
                            txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumeroParte"));
                            txtv_Desc.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Desc"));
                            tvCantidadTotal.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Unidades"));
                            edtx_Empaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            edtx_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));

                            if(dao.getSoapObject_parced().getPrimitivePropertyAsString("TipoReg").equals("1"))
                            {
                                txtv_TipoReg.setText("No etiquetado");
                                chk_Editar.setEnabled(true);
                                btn_Empaques.setEnabled(false);
                            }
                            else
                            {
                                txtv_TipoReg.setText("Etiquetado");

                                    chk_Editar.setEnabled(false);
                                    btn_Empaques.setEnabled(true);

                            }

                            btn_Confirmar.setEnabled(true);
                            edtx_ConfirmarPallet.setEnabled(false);
                            break;
                        case PalletEditado:
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.registro_exito),  dao.iscEstado(), true, true);
                            reiniciaVariables();
                            break;
                        case ConfirmarPallet:
                            new popUpGenerico(contexto, getCurrentFocus(),getString(R.string.registro_exito),  dao.iscEstado(), true, true);
                            reiniciaVariables();
                            break;
                    }
                } else
                {

                    switch (tarea)
                    {
                        case ConsultaPallet:
                            txtv_Producto.setText("");
                            txtv_Desc.setText("");
                            edtx_Unidades.setText("");
                            edtx_Empaques.setText("");
                            edtx_Lote.setText("");
                            txtv_TipoReg.setText("");
                            btn_Empaques.setEnabled(false);
                            btn_Confirmar.setEnabled(false);
                            break;
                        case PalletEditado:
                        case ConfirmarPallet:
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            edtx_ConfirmarPallet.setText("");
                            edtx_ConfirmarPallet.requestFocus();
                            break;
                        default:
                            new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            break;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(),  dao.iscEstado(), true, true);

            }
            progressBarHelper.DesactivarProgressBar(tarea);
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
