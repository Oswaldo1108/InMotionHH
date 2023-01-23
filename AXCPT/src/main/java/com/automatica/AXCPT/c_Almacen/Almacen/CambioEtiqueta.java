package com.automatica.AXCPT.c_Almacen.Almacen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.Almacen_Ajustes_CambioMercado;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.DialogFragmentConfirmarMercado;
import com.automatica.AXCPT.databinding.ActivityCambioEtiquetaBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CambioEtiqueta extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{
    private ProgressBarHelper p;
    popUpGenerico pop = new popUpGenerico(CambioEtiqueta.this);
    ActivityCambioEtiquetaBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    ProgressBarHelper progressBarHelper;
    Handler handler = new Handler();
    View vista;
    Context contexto = this;
    String posicion = "";
    Bundle b = new Bundle();
    boolean recargar;
    String mercado = "", tipoPallet = "";
    String impresora = "";
    private String ID_MOVIMIENTO="1";


    // ****************************************************** CICLO DE VIDA *********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            binding = ActivityCambioEtiquetaBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            configurarToolbar();
            configurarTaskbar();
            declararVariables();
            agregaListeners();
        }catch (Exception e){
            e.printStackTrace();
            pop.popUpGenericoDefault(vista,e.getMessage(),false);
        }
    }


    @Override
    protected void onResume() {
        /*new DevolucionEmpaqueUnico.SegundoPlano("ConsultarPartida").execute();
        ejecutarTarea("ConsultarPartida");*/

        super.onResume();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }




    // ****************************************************** Toolbar & Taskbar *********************************************
    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CambioEtiqueta");
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



    @Override
    public void BotonDerecha() {

    }

    @Override
    public void BotonIzquierda() {

        onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.toolbar_borrar_datos, menu);
            return true;
        } catch (Exception e) {
            Toast.makeText(contexto, "error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (p.ispBarActiva())
        {
            if ((id == R.id.InformacionDispositivo))
            {
                new sobreDispositivo(contexto, vista);
            }
            if ((id == R.id.borrar_datos))
            {
                ReiniciarVariables();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    // ******************************************************** MÉTODOS NECESARIOS *********************************************************
    private void declararVariables()
    {
        p = new ProgressBarHelper(this);

    }


    private void agregaListeners()
    {

        binding.edtxPallet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER)) {
                    if (TextUtils.isEmpty(binding.edtxPallet.getText())){
                        pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Pallet'",false);
                        if (mercado.equals("MND")){
                            binding.edtxPrimera.requestFocus();
                        }else{
                            binding.edtxPallet.requestFocus();
                        }

                        return false;
                    }
                    ejecutarTarea("Consulta");

                }

                return false;

            }
        });

        binding.edtxEmpaque.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxEmpaque.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Paquete'",false);
                    //binding.edtxPallet.requestFocus();
                    return false;
                }
                binding.edtxEmpaque.requestFocus();
                ejecutarTarea("RegistrarEmpaque");
                return false;
            }
        });



        binding.btnCerrarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!binding.tvPallet.getText().equals("-")){
                    new CreaDialogos(getString(R.string.pregunta_cancelar_pallet), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id) {

                            ejecutarTarea("CerrarTarima");
                            new esconderTeclado(CambioEtiqueta.this);
                        }
                    },null,contexto);

                }
            }
        });



      /*  ((Spinner)binding.vwSpinner.findViewById(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mercado = ((Constructor_Dato)parent.getSelectedItem()).getDato();
                if (mercado.equals("MND")){
                    binding.lPallet.setVisibility(View.GONE);
                    binding.lPrimera.setVisibility(View.VISIBLE);
                    binding.lUltima.setVisibility(View.VISIBLE);
                    binding.lCantidad.setVisibility(View.VISIBLE);
                }else{
                    binding.lPallet.setVisibility(View.VISIBLE);
                    binding.lPrimera.setVisibility(View.GONE);
                    binding.lUltima.setVisibility(View.GONE);
                    binding.lCantidad.setVisibility(View.GONE);
                }
                MostrarCampos(tipoPallet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

       /*
        ((Spinner)binding.vwSpinner2.findViewById(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                impresora = ((Constructor_Dato)parent.getSelectedItem()).getDato();
                Log.i("Impresora",impresora);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        binding.edtxConfirmaPallet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxPallet.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Pallet'",false);
                    binding.edtxPallet.requestFocus();
                    return false;
                }
                if (TextUtils.isEmpty(binding.edtxConfirmaPallet.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Confirmar pallet'",false);
                    binding.edtxConfirmaPallet.requestFocus();
                    return false;
                }
                if (!TextUtils.equals(binding.edtxConfirmaPallet.getText(),binding.edtxPallet.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Los códigos no coinciden",false);
                    binding.edtxPallet.requestFocus();
                    binding.edtxPallet.setText("");
                    binding.edtxConfirmaPallet.setText("");
                    return false;
                }
                new DialogFragmentConfirmarMercado(CambioEtiqueta.this,ID_MOVIMIENTO).show(getSupportFragmentManager(),"cambioMercado");
                return false;
            }
        });

        binding.edtxCantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxPallet.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Pallet'",false);
                    binding.edtxPallet.requestFocus();
                    return false;
                }
                if (TextUtils.isEmpty(binding.edtxPrimera.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Inserte el código del primer empaque",false);
                    binding.edtxPrimera.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(binding.edtxUltima.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Inserte el código del último empaque",false);
                    binding.edtxUltima.requestFocus();
                    return false;
                }

                if (TextUtils.isEmpty(binding.edtxCantidad.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Ingrese la cantidad de empaques",false);
                    binding.edtxCantidad.requestFocus();
                    return false;
                }

                ejecutarTarea("CambiarPalletNE");
                return false;
            }
        });



        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (binding.rbEmpaque.isChecked()){
                    binding.lCerrarPallet.setVisibility(View.VISIBLE);
                    binding.lPalletCompleto.setVisibility(View.GONE);
                }
                else if(binding.rbPallet.isChecked()){
                    binding.lCerrarPallet.setVisibility(View.GONE);
                    binding.lPalletCompleto.setVisibility(View.VISIBLE);
                }
            }
        });

       binding.edtxMenudeoPallet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.equals(binding.edtxMenudeoPallet.getText(),binding.edtxPallet.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Los códigos no coinciden",false);
                    binding.edtxPallet.requestFocus();
                    binding.edtxPallet.setText("");
                    binding.edtxConfirmaPallet.setText("");
                    return false;
                }
                ejecutarTarea("ConfirmarEtiquetado");
                return false;
            }
        });
    }



    private void ReiniciarVariables()
    {
        binding.edtxPallet.requestFocus();
        binding.edtxPallet.setText("");
        binding.txtvProducto.setText("");
        binding.txtvDescProd.setText("");
        binding.txtvEmpaques.setText("");
        binding.txtvUnidadMedida.setText("");
        binding.txtvEstatus.setText("");
        binding.txtvMercado.setText("");
        binding.txtvCantidad.setText("");
        binding.edtxEmpaque.setText("");
        binding.tvPallet.setText("");
        binding.tvRegistrados.setText("");
        binding.edtxPallet.setText("");
        binding.edtxPrimera.setText("");
        binding.edtxUltima.setText("");
        binding.edtxCantidad.setText("");
    }

    @Override
    public void onBackPressed() {
        if (!taskbar_axc.toggle()){
            return;
        }else {
            taskbar_axc.toggle();
        }
        if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!=null){
            if (getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
                taskbar_axc.cerrarFragmento();
                return;
            }
        }
        if (getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            return;
        }
        Intent intent = new Intent(CambioEtiqueta.this, Inicio_Menu_Dinamico.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);
    }


    // ******************************************************** MÉTODOS EXTRA *********************************************************

    public void MostrarCampos(@NonNull String mercadoDePallet){

        if (mercadoDePallet.equals("NE")) {
            binding.edtxPrimera.requestFocus();
            binding.lPallet.setVisibility(View.GONE);
            binding.lPyU.setVisibility(View.VISIBLE);
            binding.lPE.setVisibility(View.GONE);
        }


        else if(mercadoDePallet.equals("E")) {


            binding.lPallet.setVisibility(View.GONE);
            binding.lPyU.setVisibility(View.GONE);
            binding.lPE.setVisibility(View.VISIBLE);


            if (binding.rbEmpaque.isChecked()){
                binding.edtxEmpaque.requestFocus();
                binding.lCerrarPallet.setVisibility(View.VISIBLE);
                binding.lPalletCompleto.setVisibility(View.GONE);
            }
            else if(binding.rbPallet.isChecked()){
                binding.edtxMenudeoPallet.requestFocus();
                binding.lCerrarPallet.setVisibility(View.GONE);
                binding.lPalletCompleto.setVisibility(View.VISIBLE);
            }


        }

        else{
            binding.lPallet.setVisibility(View.GONE);
            binding.lPyU.setVisibility(View.GONE);
            binding.lPE.setVisibility(View.GONE);
        }
    }




    // ******************************************************** EXECUTOR ****************************************************************
    void ejecutarTarea(String tarea){
        ExecutorService service = Executors.newSingleThreadExecutor();
        final DataAccessObject[] dao = new DataAccessObject[1];
        final cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(contexto);

        service.execute(new Runnable() {
            @Override
            public void run() {

                // ******************************************************** PRE EXECUTE ****************************************
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        p.ActivarProgressBar(tarea);
                    }
                });
                // ********************************************************* IN BACKGROUND ************************************
                try{
                    switch (tarea) {
                        case "Consulta":
                            dao[0]= ca.c_ConsultarPalletPT(binding.edtxPallet.getText().toString());
                            break;

                        case "RegistrarEmpaque":
                            dao[0] = ca.c_registraEmpaqueCambioEtiqueta(binding.edtxPallet.getText().toString(), binding.edtxEmpaque.getText().toString());
                            break;


                        case "ConfirmarEtiquetado":
                            dao [0]= ca.c_CambiarPalletEtiquetado(binding.edtxPallet.getText().toString());
                            break;


                        case "CambiarPalletNE":
                            dao[0] = ca.c_CambiarPalletNE(binding.edtxPallet.getText().toString(), binding.edtxPrimera.getText().toString(), binding.edtxUltima.getText().toString(), binding.edtxCantidad.getText().toString());
                            break;


                        case "CerrarTarima":
                            dao[0] = ca.c_CierraPalletCambio(binding.tvPallet.getText().toString());
                            break;


                       /* case "ListaImpresoras":
                            dao[0]= ca.c_ListaImpresoras();
                            break;*/

                      /*  case "Mercados":
                            dao[0]= ca.c_ListaMercados();
                            break; */

                        /*case "Confirmar":
                            dao[0] = ca.c_CambiaMercado(binding.edtxPallet.getText().toString(),mercado);
                            break;
*/



                        case "Imprimir":
                            dao[0]= ca.c_ReimprimirEtiquetasMercado(binding.edtxPallet.getText().toString(),impresora);
                            break;

                            case "ImprimirMenudeo":
                            dao[0] = ca.c_ReimprimirEtiquetasCambio(binding.tvPallet.getText().toString(),impresora);
                            break;


                    }

                }
                catch (Exception e){
                    dao[0] = new DataAccessObject(e);
                    e.printStackTrace();
                }

                // ******************************************************* POST EXECUTE ***************************************
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (dao[0].iscEstado()) {
                                switch (tarea) {

                                    case "Consulta":
                                        binding.txtvProducto.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        binding.txtvEmpaques.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                        binding.txtvCantidad.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        binding.txtvEstatus.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                                        binding.txtvMercado.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("Tipo"));
                                        binding.txtvDescProd.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                                        binding.txtvUnidadMedida.setText(" " + dao[0].getSoapObject_parced().getPrimitivePropertyAsString("UnidadMedida"));
                                        binding.edtxConfirmaPallet.requestFocus();

                                        tipoPallet = binding.txtvMercado.getText().toString();
                                        MostrarCampos(tipoPallet);
                                        break;


                                    case "RegistrarEmpaque":
                                        binding.edtxEmpaque.setText("");
                                        binding.edtxEmpaque.requestFocus();
                                        binding.tvRegistrados.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                        binding.tvPallet.setText(dao[0].getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        break;


                                    case "ConfirmarEtiquetado":
                                        pop.popUpGenericoDefault(getCurrentFocus(),"Pallet cambiado con éxito",true);
                                        //ejecutarTarea("ImprimirMenudeo");
                                        ReiniciarVariables();

                                    case "CambiarPalletNE":
                                        pop.popUpGenericoDefault(getCurrentFocus(),"PCambio realizado con éxito",true);
                                        //ejecutarTarea("ImprimirMenudeo");
                                        ReiniciarVariables();
                                        break;

                                    case "ImprimirMenudeo":
                                        binding.tvRegistrados.setText("-");
                                        binding.tvPallet.setText("-");
                                        //ReiniciarVariables();
                                        break;

                                    case "CerrarTarima":
                                        pop.popUpGenericoDefault(getCurrentFocus(),"Pallet cerrado con éxito",true);
                                        //ejecutarTarea("ImprimirMenudeo");
                                        ReiniciarVariables();
                                        break;
                                }

                            } else {
                                pop.popUpGenericoDefault(vista, dao[0].getcMensaje(), false);
                                //ReiniciarVariables();
                                binding.edtxEmpaque.setText("");
                                //binding.edtxEmpaque.requestFocus();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            pop.popUpGenericoDefault(vista, e.getMessage(), false);
                            ReiniciarVariables();
                            binding.edtxPallet.setText("");
                            binding.edtxPallet.requestFocus();
                        }
                        p.DesactivarProgressBar(tarea);
                    }
                });
            }
        });
    }

}