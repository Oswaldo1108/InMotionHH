package com.automatica.AXCPT.c_Embarques.Reempaque;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.Fragmentos.FragmentoConsulta;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.ActivityHelpers;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.AXCPT.databinding.ActivityEmbarquesReempaqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.sobreDispositivo;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Embarques_Reempaque extends AppCompatActivity implements frgmnt_taskbar_AXC.interfazTaskbar{

    String Producto, datoConsulta;
    int Cant;
    Button btn_CerrarTarima;
    Context contexto = Embarques_Reempaque.this;

    SortableTableView tblv_pallets;

    EditText edtx_pall, edtx_cant, edtx_embarque;
    TextView tv_pallet;
    String registro;
    Spinner spnr_Productos;

    Toolbar toolbar;
    private ActivityHelpers activityHelpers;
    private ProgressBarHelper p;
    ActivityEmbarquesReempaqueBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmbarquesReempaqueBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        p = new ProgressBarHelper(this);
        activityHelpers = new ActivityHelpers();
        activityHelpers.AgregarMenu(Embarques_Reempaque.this,R.id.Pantalla_principal);
        activityHelpers.AgregarTaskBar(Embarques_Reempaque.this,R.id.FrameLayout,true);
        try {
            InicializarVariables();
            AgregarListeners();
            edtx_embarque.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        activityHelpers.getTaskbar_axc().cambiarResources(frgmnt_taskbar_AXC.CERRAR_TARIMA);
        super.onPostCreate(savedInstanceState);
    }
    public void InicializarVariables() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reempaque");
        getSupportActionBar().setSubtitle("Surtido");
        tblv_pallets = findViewById(R.id.customtableview).findViewById(R.id.tableView_OC);
        edtx_pall = findViewById(R.id.edtx_pallet);
        edtx_embarque = findViewById(R.id.edtx_embarque);
        tv_pallet = findViewById(R.id.tv_pallet);
        edtx_cant = findViewById(R.id.edtx_cantidad);
        spnr_Productos = findViewById(R.id.vw_spinnerProducto).findViewById(R.id.spinner);
        btn_CerrarTarima = findViewById(R.id.btn_CerrarTarima);

    }

    public void AgregarListeners() {
        btn_CerrarTarima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tblv_pallets.getChildCount() > 0) {
                    new CreaDialogos(contexto).dialogoDefault("AXC", "¿Cerrar pallet?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new SegundoPlano("Cerrar").execute();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                } else {
                    new popUpGenerico(contexto, getCurrentFocus(), "No hay pallet abierto", false, true, true);

                }
            }
        });
        edtx_embarque.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!TextUtils.isEmpty(edtx_embarque.getText())) {
                    new SegundoPlano("RegistraPalletCons").execute();
                } else {
                    new popUpGenerico(contexto, getCurrentFocus(), "Capture el embarque para el reempaque", false, true, true);
                }

                return false;
            }
        });
        edtx_pall.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!tv_pallet.equals("-")) {
                    if (!TextUtils.isEmpty(edtx_pall.getText())) {
                        edtx_pall.requestFocus();
                        new SegundoPlano("ConsultarATomar").execute();
                    } else {
                        new popUpGenerico(contexto, getCurrentFocus(), "Captura el pallet a consolidar", false, true, true);

                    }
                } else {
                    new popUpGenerico(contexto, getCurrentFocus(), "Captura el pallet nuevo", false, true, true);

                }

                return false;
            }
        });

        spnr_Productos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Producto = ((Constructor_Dato) spnr_Productos.getSelectedItem()).getTag1();
                    registro = ((Constructor_Dato) spnr_Productos.getSelectedItem()).getTag3();
                    Log.i("Dato", Producto);
                    Log.i("registro", registro);
                    if (registro.equals("E")){
                       // Log.i("registro", "Paso");
                        binding.switchSku.setEnabled(false);
                        binding.switchSku.setChecked(true);
                        binding.switchSku.setChecked(false);
                        binding.switchSku.setTextColor(contexto.getResources().getColor(R.color.negroTrans));
                    }else {
                     //   Log.i("registro", "No paso");
                        binding.switchSku.setChecked(false);
                        binding.switchSku.setEnabled(true);
                        binding.textView40.setText("Cantidad:");
                        binding.edtxCantidad.setHint("Ingrese Cantidad");
                        binding.edtxCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                        binding.switchSku.setTextColor(contexto.getResources().getColor(R.color.blancoLetraStd));
                    }
                    // new popUpGenerico(contexto,getCurrentFocus(),Producto,true,true,true);
                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtx_cant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(Producto)){
                    new popUpGenerico(contexto, getCurrentFocus(), "Seleccione un producto'", false, true, true);
                    return false;
                }
                if (TextUtils.isEmpty(edtx_cant.getText())){
                    new popUpGenerico(contexto, getCurrentFocus(), "Llene el campo 'Cantidad'", false, true, true);
                    edtx_cant.requestFocus();
                    return false;
                }
                if (TextUtils.isEmpty(edtx_pall.getText())){
                    new popUpGenerico(contexto, getCurrentFocus(), "Llene el campo 'Pallet a tomar'", false, true, true);
                    edtx_pall.requestFocus();
                    return false;
                }
                if (tv_pallet.getText().toString().length() < 6){
                    new popUpGenerico(contexto, getCurrentFocus(), "Se debe crear un pallet para poder continuar'", false, true, true);
                    return false;
                }
                if (binding.switchSku.isEnabled()){
                    if (binding.switchSku.isChecked()){

                        new SegundoPlano("RegistraEmpaqueConsSKU").execute();
                    }else {
                        new SegundoPlano("RegistraEmpaqueConsPiezas").execute();
                    }
                }else {
                    new SegundoPlano("RegistraEmpaqueCons").execute();
                }
                return false;
            }
        });
        tv_pallet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (tv_pallet.getText().toString().trim().equals("")||tv_pallet.getText().toString().trim().equals("-")){
                    new popUpGenerico(contexto, getCurrentFocus(), "Se debe crear un pallet para poder continuar'", false, true, true);
                    return false;
                }
                String[] datos ={tv_pallet.getText().toString()};
                activityHelpers.getTaskbar_axc().abrirFragmentoDesdeActividad(FragmentoConsulta.newInstance(datos,FragmentoConsulta.TIPO_PALLET),FragmentoConsulta.TAG);
                return true;
            }
        });


        binding.switchSku.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binding.switchSku.isEnabled()){
                    if (isChecked){
                        binding.textView40.setText("SKU: ");
                        binding.edtxCantidad.setHint("Ingrese SKU");

                        binding.edtxCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
                    }else {
                        binding.textView40.setText("Cantidad:");
                        binding.edtxCantidad.setHint("Ingrese Cantidad");
                        binding.edtxCantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                }else {
                    binding.textView40.setText("Código empaque");
                    binding.edtxCantidad.setHint("Ingrese código");
                    binding.edtxCantidad.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_cancelar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (p.ispBarActiva()) {
            int id = item.getItemId();
            switch (id) {
                case R.id.InformacionDispositivo:
                    new sobreDispositivo(Embarques_Reempaque.this, getCurrentFocus());
                    return true;
                case R.id.Cancelar:
                    new CreaDialogos(contexto).dialogoDefault("AXC", "¿Cancelar reempaque?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new SegundoPlano("Cancelar").execute();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    return true;
                case R.id.recargar:
                    if (!TextUtils.isEmpty(tv_pallet.getText())) {
                        new SegundoPlano("ConsultaPallet").execute();
                    }
                    return true;
                /*case R.id.Iniciar:
                    new CreaDialogos("AXC","¿Nueva consolidación?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (TextUtils.isEmpty(tv_pallet.getText())){
                                new SegundoPlano("Registra").execute();
                            }else {
                                new popUpGenerico(contexto,getCurrentFocus(),"Debes cerrar la consolidación actual para poder comenzar otra",false,true,true);
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    },contexto);
                    return true;*/
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void BotonDerecha() {
        btn_CerrarTarima.callOnClick();
    }

    @Override
    public void BotonIzquierda() {
        onBackPressed();

    }

    private class SegundoPlano extends AsyncTask<String, Void, Void> {
        String Tarea;
        DataAccessObject dao;
        cAccesoADatos ca = new cAccesoADatos(contexto);

        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            p.ActivarProgressBar(Tarea);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                switch (Tarea) {
                    case "Cancelar":
                        dao = ca.cCancelaReempaque(edtx_embarque.getText().toString(), tv_pallet.getText().toString());
                        break;
                        case "ConsultarATomar":
                        dao = ca.cConsultaPalletReempaque(edtx_embarque.getText().toString(), edtx_pall.getText().toString());
                        break;

                        case "RegistraEmpaqueCons":
                        dao = ca.cRegistraReempaqueCons(edtx_embarque.getText().toString(), tv_pallet.getText().toString(), edtx_pall.getText().toString(), Producto, edtx_cant.getText().toString());
                        break;
                    case "RegistraEmpaqueConsPiezas":
                        dao = ca.cRegistraReempaqueConsPiezas(edtx_embarque.getText().toString(), tv_pallet.getText().toString(), edtx_pall.getText().toString(), Producto, edtx_cant.getText().toString());
                        break;
                    case "RegistraEmpaqueConsSKU":
                        dao = ca.cRegistraReempaqueConsSKU(edtx_embarque.getText().toString(), tv_pallet.getText().toString(), edtx_pall.getText().toString(), Producto, edtx_cant.getText().toString());
                        break;

                        case "Cerrar":
                        dao = ca.cCerrarReempaque(edtx_embarque.getText().toString(), tv_pallet.getText().toString());
                        break;
                    case "RegistraPalletCons":
                        dao = ca.cCreaPalletReempaque(edtx_embarque.getText().toString());
                        break;
                    case "ConsultaPallet":
                        if (tv_pallet.equals("-")) {

                            new popUpGenerico(contexto, getCurrentFocus(), "No hay pallet seleccionado", false, true, true);

                        } else {
                            dao = ca.cConsultaPalletCons(tv_pallet.getText().toString());
                        }
                        break;
                    default:
                        dao = new DataAccessObject();
                }

            } catch (Exception e) {
                dao = new DataAccessObject(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (dao.iscEstado()) {
                    switch (Tarea) {

                        case "Cancelar":
                            if (tblv_pallets.getDataAdapter() != null) {
                                tblv_pallets.getDataAdapter().clear();
                            }
                            tv_pallet.setText("-");
                            edtx_cant.setText("");
                            edtx_pall.setText("");
                            edtx_embarque.setText("");
                            edtx_embarque.requestFocus();
                            spnr_Productos.setAdapter(null);
                            new popUpGenerico(contexto, getCurrentFocus(), "Pallet cancelado con exito", true, true, true);

                            break;
                        case "ConsultarATomar":
                            if (dao.getcTablaUnica() == null) {
                                edtx_pall.setText("");
                            } else {
                                spnr_Productos.setAdapter(new CustomArrayAdapter(contexto, android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("cItem", "cValue", "cValue","TipoReg")));

                                edtx_cant.requestFocus();
                                new esconderTeclado(Embarques_Reempaque .this);
                            }

                            break;

                        case "RegistraEmpaqueConsPiezas":
                        case "RegistraEmpaqueConsSKU":
                        case "RegistraEmpaqueCons":

                            String[] mensajeca = dao.getcMensaje().split("#");
                            datoConsulta = mensajeca[0];
                            tv_pallet.setText(datoConsulta);
                            if (!datoConsulta.equals("")) {
                                new SegundoPlano("ConsultaPallet").execute();
                                edtx_pall.requestFocus();
                            }
                            edtx_cant.setText("");
                            if (!edtx_pall.equals("")) {
                                new SegundoPlano("ConsultarATomar").execute();
                            }

                            edtx_cant.setText("");
                            break;
                        case "Cerrar":
                            if (tblv_pallets.getDataAdapter() != null) {
                                tblv_pallets.getDataAdapter().clear();
                            }
                            tv_pallet.setText("-");
                            edtx_cant.setText("");
                            new popUpGenerico(contexto, getCurrentFocus(), "Pallet cerrado con éxito", true, true, true);
                            break;
                        case "RegistraPalletCons":
                            String[] mensajea = dao.getcMensaje().split("#");
                            datoConsulta = mensajea[0].trim();
                            tv_pallet.setText(datoConsulta);
                            if (!datoConsulta.equals("")) {
                                new SegundoPlano("ConsultaPallet").execute();
                                edtx_pall.requestFocus();

                            }
                            break;
                        case "ConsultaPallet":
                            try {
                                TableViewDataConfigurator.newInstance(tblv_pallets, dao, Embarques_Reempaque.this, new TableViewDataConfigurator.TableClickInterface() {
                                    @Override
                                    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

                                    }

                                    @Override
                                    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {

                                    }

                                    @Override
                                    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

                                    }
                                });

                                edtx_pall.requestFocus();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                } else {
                    new popUpGenerico(contexto, getCurrentFocus(), dao.getcMensaje(), false, true, true);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            p.DesactivarProgressBar(Tarea);
        }
    }

    @Override
    public void onBackPressed() {
        activityHelpers.getTaskbar_axc().onBackPressed();
    }
}