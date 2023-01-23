package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH;

import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.cambiaColorStatusBar;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.c_Almacen.Almacen.Reubicar;
import com.automatica.AXCPT.databinding.ActivityAlmacenAjustesCambioMercadoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Almacen_Ajustes_CambioMercado extends AppCompatActivity implements DialogFragmentConfirmarMercado.confirmaMercado, frgmnt_taskbar_AXC.interfazTaskbar{

    private ActivityAlmacenAjustesCambioMercadoBinding binding;
    popUpGenerico pop= new popUpGenerico(Almacen_Ajustes_CambioMercado.this);
    String mercado = "", mercadoPallet = "";
    Context contexto = this;
    cAccesoADatos_Almacen ca;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private String ID_MOVIMIENTO="1";
    boolean ischecked;
    String impresora = "";
    String tipo = "", pallet = "";
    frgmnt_taskbar_AXC taskbar_axc;
    private ProgressBarHelper p;
    Handler handler = new Handler();
    View vista;
    boolean recargar = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenAjustesCambioMercadoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setContentView(R.layout.almacen_activity_colocar);
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
        getSupportFragmentManager().beginTransaction().add(R.id.FrameLayout,taskbar_axc,"FragmentoTaskBar").commit();


        ca= new cAccesoADatos_Almacen(Almacen_Ajustes_CambioMercado.this);

        new cambiaColorStatusBar(contexto, R.color.doradoLetrastd, Almacen_Ajustes_CambioMercado.this);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new SegundoPlano("Mercados").execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try
        {
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
        try {

            int id = item.getItemId();

            if ((id == R.id.InformacionDispositivo)) {
                new sobreDispositivo(Almacen_Ajustes_CambioMercado.this, getCurrentFocus());
            }
            if ((id == R.id.borrar_datos)) {
                LimpiarCampos();
                binding.edtxPallet.setText("");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
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


    public void setListeners(){
        binding.edtxPallet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxPallet.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Pallet'",false);
                    if (mercado.equals("MND")){
                        binding.edtxPrimera.requestFocus();
                    }else{
                        binding.edtxPallet.requestFocus();
                    }

                    return false;
                }
                new SegundoPlano("Consulta").execute();

                return false;
            }
        });

        ((Spinner)binding.vwSpinner.findViewById(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mercado = ((Constructor_Dato)parent.getSelectedItem()).getDato();
               /* if (mercado.equals("MND")){
                    binding.lPallet.setVisibility(View.GONE);
                    binding.lPrimera.setVisibility(View.VISIBLE);
                    binding.lUltima.setVisibility(View.VISIBLE);
                    binding.lCantidad.setVisibility(View.VISIBLE);
                }else{
                    binding.lPallet.setVisibility(View.VISIBLE);
                    binding.lPrimera.setVisibility(View.GONE);
                    binding.lUltima.setVisibility(View.GONE);
                    binding.lCantidad.setVisibility(View.GONE);
                }*/
                MostrarCampos(mercadoPallet);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((Spinner)binding.vwSpinner2.findViewById(R.id.spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                impresora = ((Constructor_Dato)parent.getSelectedItem()).getDato();
                Log.i("Impresora",impresora);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                new DialogFragmentConfirmarMercado(Almacen_Ajustes_CambioMercado.this,ID_MOVIMIENTO).show(getSupportFragmentManager(),"cambioMercado");
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

                new SegundoPlano("Cambiar").execute();
                return false;
            }
        });

        binding.edtxEmpaque.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxEmpaque.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Paquete'",false);
                    binding.edtxPallet.requestFocus();
                    return false;
                }
                new SegundoPlano("RegistrarEmpaque").execute();
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

                            new SegundoPlano("CerrarTarima").execute();
                            new esconderTeclado(Almacen_Ajustes_CambioMercado.this);
                        }
                    },null,contexto);

                }
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
                new SegundoPlano("ConfirmarMenudeo").execute();
                return false;
            }
        });

    }


    public void LimpiarCampos(){
        binding.txtvProducto.setText("");
        binding.txtvMercado.setText("");
        binding.txtvEstatus.setText("");
        binding.txtvCantidad.setText("");
        binding.txtvEmpaques.setText("");
        binding.edtxConfirmaPallet.setText("");
        binding.edtxPallet.setText("");
        binding.edtxPallet.requestFocus();
        binding.txtvDescProd.setText("");
        binding.txtvUnidadMedida.setText("");
        binding.edtxMenudeoPallet.setText("");
        binding.edtxPrimera.setText("");
        binding.edtxUltima.setText("");
        binding.edtxEmpaque.setText("");
        binding.edtxCantidad.setText("");
    }

    public void MostrarCampos(@NonNull String mercadoDePallet){

        if (mercadoDePallet.equals("MAYOREO") && mercado.equals("ATS") || mercadoDePallet.equals("AUTOSERVICIO") && mercado.equals("MYO") ){
            binding.edtxConfirmaPallet.requestFocus();
            binding.lPallet.setVisibility(View.VISIBLE);
            binding.lPyU.setVisibility(View.GONE);
            binding.lPE.setVisibility(View.GONE);

        }else if(mercadoDePallet.equals("MENUDEO") && mercado.equals("ATS") || mercadoDePallet.equals("MENUDEO") && mercado.equals("MYO")){


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


        }else if(mercadoDePallet.equals("MAYOREO") && mercado.equals("MND") || mercadoDePallet.equals("AUTOSERVICIO") && mercado.equals("MND")){
            binding.edtxPrimera.requestFocus();
            binding.lPallet.setVisibility(View.GONE);
            binding.lPyU.setVisibility(View.VISIBLE);
            binding.lPE.setVisibility(View.GONE);
        }
        else{
            binding.lPallet.setVisibility(View.GONE);
            binding.lPyU.setVisibility(View.GONE);
            binding.lPE.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean confirmar(boolean mercado, String id) {
        new SegundoPlano("Confirmar").execute();
        ischecked= mercado;
        return false;
    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>{

        String Tarea;
        DataAccessObject dao= new DataAccessObject();
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(Almacen_Ajustes_CambioMercado.this);
        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                binding.progressBarHolder.setAnimation(inAnimation);
                binding.progressBarHolder.setVisibility(View.VISIBLE);

            }catch (Exception e)
            {
                e.printStackTrace();
                pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            switch (Tarea){
                case "Consulta":
                    dao= ca.c_ConsultarPalletPT(binding.edtxPallet.getText().toString());
                    break;
                case "ListaImpresoras":
                    dao= ca.c_ListaImpresoras();
                    break;
                case "Mercados":
                    dao= ca.c_ListaMercados();
                    break;
                case "Confirmar":
                    dao = ca.c_CambiaMercado(binding.edtxPallet.getText().toString(),mercado);
                    break;

                case "ConfirmarMenudeo":
                    dao = ca.c_CambiaMercadoPalletMenudeo(binding.edtxPallet.getText().toString(),mercado);
                    break;

                case "Cambiar":
                    dao = ca.c_CambiaMercadoMenudeo(binding.edtxPallet.getText().toString(),mercado, binding.edtxPrimera.getText().toString(), binding.edtxUltima.getText().toString(), binding.edtxCantidad.getText().toString());
                    break;
                case "Imprimir":
                    dao= ca.c_ReimprimirEtiquetasMercado(binding.edtxPallet.getText().toString(),impresora);
                    break;
                case "RegistrarEmpaque":
                    dao = ca.c_registraEmpaqueCambioMercado(binding.edtxPallet.getText().toString(), binding.edtxEmpaque.getText().toString(), mercado);
                    break;

                case "ImprimirMenudeo":
                    dao= ca.c_ReimprimirEtiquetasMercado(strings[0],impresora);
                    break;

                case "CerrarTarima":
                    dao= ca.c_CierraPalletCambio(binding.tvPallet.getText().toString());
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (dao.iscEstado()){
                    switch (Tarea) {

                        case "ListaImpresoras":
                            ((Spinner) binding.vwSpinner2.findViewById(R.id.spinner)).setAdapter(new CustomArrayAdapter(Almacen_Ajustes_CambioMercado.this, android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Impresora", "Impresora")));
                            break;

                        case "Consulta":
                            binding.txtvProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.txtvEmpaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.txtvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.txtvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                            binding.txtvMercado.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Tipo"));
                            binding.txtvDescProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                            binding.txtvUnidadMedida.setText(" " + dao.getSoapObject_parced().getPrimitivePropertyAsString("UnidadMedida"));
                            binding.edtxConfirmaPallet.requestFocus();

                            mercadoPallet = binding.txtvMercado.getText().toString();
                            MostrarCampos(mercadoPallet);

                            break;


                        case "Mercados":
                            ((Spinner)binding.vwSpinner.findViewById(R.id.spinner)).setAdapter(new CustomArrayAdapter(Almacen_Ajustes_CambioMercado.this, android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("Mercado","DMercado")));
                            mercado = ((Constructor_Dato)  ((Spinner)binding.vwSpinner.findViewById(R.id.spinner)).getSelectedItem()).getDato();
                            break;

                        case "Confirmar":
                            pop.popUpGenericoDefault(getCurrentFocus(),"Mercado cambiado con éxito",true);
                            if (ischecked){
                                outAnimation = new AlphaAnimation(1f, 0f);
                                outAnimation.setDuration(200);
                                binding.progressBarHolder.setAnimation(outAnimation);
                                binding.progressBarHolder.setVisibility(View.GONE);
                                new SegundoPlano("Imprimir").execute();
                            }else {
                                LimpiarCampos();
                            }
                            break;

                        case "ConfirmarMenudeo":
                            pop.popUpGenericoDefault(getCurrentFocus(),"Mercado cambiado con éxito",true);
                            new SegundoPlano("Imprimir").execute();
                            break;

                        case "Imprimir":
                            LimpiarCampos();
                            break;
                        case "Cambiar":
                            binding.edtxPrimera.setText("");
                            binding.edtxUltima.setText("");
                            binding.edtxCantidad.setText("");
                            pop.popUpGenericoDefault(getCurrentFocus(),"Mercado cambiado con éxito",true);
                            new SegundoPlano("ImprimirMenudeo").execute(dao.getcMensaje());
                            break;
                        case "RegistrarEmpaque":
                            binding.edtxEmpaque.setText("");
                            binding.edtxEmpaque.requestFocus();
                            binding.tvRegistrados.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.tvPallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            break;

                        case "CerrarTarima":
                            pop.popUpGenericoDefault(getCurrentFocus(),"Pallet cerrado con éxito",true);
                            new SegundoPlano("ImprimirMenudeo").execute(binding.tvPallet.getText().toString());
                            binding.tvRegistrados.setText("-");
                            binding.tvPallet.setText("-");
                            break;
                    }
                }else {
                    pop.popUpGenericoDefault(getCurrentFocus(),dao.getcMensaje(),false);
                    LimpiarCampos();
                    binding.edtxPallet.setText("");
                }
            }catch (Exception e){
                pop.popUpGenericoDefault(getCurrentFocus(),e.getMessage(),false);
                LimpiarCampos();
                binding.edtxPallet.setText("");
                binding.edtxPallet.requestFocus();
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            binding.progressBarHolder.setAnimation(outAnimation);
            binding.progressBarHolder.setVisibility(View.GONE);
        }
    }
}