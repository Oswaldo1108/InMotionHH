package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.sobreDispositivo;
import com.automatica.AXCPT.databinding.ActivityAlmacenAjustesCambioMercadoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Almacen_Ajustes_CambioMercado extends AppCompatActivity implements DialogFragmentConfirmarMercado.confirmaMercado{

    private ActivityAlmacenAjustesCambioMercadoBinding binding;
    popUpGenerico pop= new popUpGenerico(Almacen_Ajustes_CambioMercado.this);
    String mercado;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    private String ID_MOVIMIENTO="1";
    boolean ischecked;
    String impresora = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlmacenAjustesCambioMercadoBinding.inflate(getLayoutInflater());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setContentView(binding.getRoot());
        try {
            setListeners();
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Ajustes");
            getSupportActionBar().setSubtitle("Cambiar mercado");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new SegundoPlano("Mercados").execute();
        //new SegundoPlano("ListaImpresoras").execute();
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
    public void setListeners(){
        binding.edtxPallet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxPallet.getText())){
                    pop.popUpGenericoDefault(getCurrentFocus(),"Llene el campo 'Pallet'",false);
                    binding.edtxPallet.requestFocus();
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
                Log.i("Mercado",mercado);
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
                case "Imprimir":
                    dao= ca.c_ReimprimirEtiquetasMercado(binding.edtxPallet.getText().toString(),impresora);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (dao.iscEstado()){
                    switch (Tarea){
                        case "ListaImpresoras":
                            ((Spinner)binding.vwSpinner2.findViewById(R.id.spinner)).setAdapter(new CustomArrayAdapter(Almacen_Ajustes_CambioMercado.this, android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("Impresora","Impresora")));
                            break;
                        case "Consulta":
                            binding.txtvProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            binding.txtvEmpaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                            binding.txtvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                            binding.txtvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                            binding.txtvMercado.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Tipo"));
                            binding.txtvDescProd.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                            binding.txtvUnidadMedida.setText(" "+dao.getSoapObject_parced().getPrimitivePropertyAsString("UnidadMedida"));
                            binding.edtxConfirmaPallet.requestFocus();
                            break;
                        case "Mercados":
                            ((Spinner)binding.vwSpinner.findViewById(R.id.spinner)).setAdapter(new CustomArrayAdapter(Almacen_Ajustes_CambioMercado.this, android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("Mercado","DMercado")));
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
                        case "Imprimir":
                            LimpiarCampos();
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