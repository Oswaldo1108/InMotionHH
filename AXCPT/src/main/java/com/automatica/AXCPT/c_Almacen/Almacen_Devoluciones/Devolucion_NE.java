package com.automatica.AXCPT.c_Almacen.Almacen_Devoluciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.c_Recepcion.Lote_Modificable.Recepcion_Registro_Pallet_NE_LoteModificable;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class Devolucion_NE extends AppCompatActivity {

    Spinner spnr_Lotes;
    int registroAnteriorSpinner=0;
    Button button_cerrar_tarima;
    DatePickerFragment newFragment;
    TextView tv_devolucion,tv_Producto,tv_Partida,tv_cantOrig,tv_cantReg2,
            tv_EmpaquesRegistrados,tv_pallet,tv_ProductoDet, tv_cantReg;
    EditText edtx_CantidadPiezas,edtx_CantidadEmpaques,edtx_LoteNuevo;
    Handler h = new Handler();
    FrameLayout progressBarHolder;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    String OrdenDevolucion,Partida,NumParte;
    Bundle b;
    Context contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion__ne);
        try {
            b= getIntent().getExtras();
            OrdenDevolucion = b.getString("OrdenDevolucion");
            Partida= b.getString("Partida");
            NumParte= b.getString("NumParte");
            DeclararVariables();
            AgregarListener();
            Log.i("bundle1",b.getString("OrdenDevolucion"));
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            new SegundoPlano("ConsultarOrden").execute();
            new SegundoPlano("ListarLotes").execute();
            new SegundoPlano("ConsultarPallet").execute();
        }catch (Exception e){
            e.printStackTrace();
            new popUpGenerico(Devolucion_NE.this,getCurrentFocus(),e.getMessage(),false,true,true);
        }
    }


    public void DeclararVariables(){
        try{
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Devolución");
            getSupportActionBar().setSubtitle("No etiquetados");
            spnr_Lotes = findViewById(R.id.vw_spinner).findViewById(R.id.spinner);
            tv_devolucion=findViewById(R.id.tv_devolucion);
            tv_Producto = findViewById(R.id.tv_Producto);
            tv_Partida= findViewById(R.id.tv_Partida);
            tv_cantOrig= findViewById(R.id.tv_cantOrig);
            tv_ProductoDet= findViewById(R.id.tv_ProductoDet);
            tv_cantReg2= findViewById(R.id.tv_cantReg2);
            tv_EmpaquesRegistrados = findViewById(R.id.tv_EmpaquesRegistrados);
            tv_pallet= findViewById(R.id.tv_pallet);
            tv_cantReg = findViewById(R.id.tv_cantReg);
            progressBarHolder= findViewById(R.id.progressBarHolder);
            edtx_CantidadEmpaques = findViewById(R.id.edtx_CantidadEmpaques);
            edtx_CantidadPiezas = findViewById(R.id.edtx_CantidadPiezas);
            edtx_LoteNuevo = (EditText) findViewById(R.id.edtx_LoteNuevo);
            edtx_LoteNuevo.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            button_cerrar_tarima= findViewById(R.id.button_cerrar_tarima);
            b = getIntent().getExtras();
            OrdenDevolucion = b.getString("OrdenDevolucion");
            Partida = b.getString("Partida");
            NumParte= b.getString("NumParte");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void AgregarListener(){
        /**
         * Agregar Listeners
         */
        edtx_LoteNuevo.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                    {
                        try
                            {
                                edtx_LoteNuevo.setText("");
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new com.automatica.AXCPT.Servicios.popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), false, true, true);
                            }
                    }
            }
        });

        edtx_LoteNuevo.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        try
                            {
                                if(edtx_LoteNuevo.getText().toString().equals(""))
                                    {
                                        h.post(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                edtx_LoteNuevo.setText("");
                                                edtx_LoteNuevo.requestFocus();
                                            }
                                        });
                                        new popUpGenerico(contexto,getCurrentFocus(),"Ingrese un lote." , false, true, true);
                                        return false;
                                    }

                                new CreaDialogos("¿Crear lote? [" + edtx_LoteNuevo.getText().toString() + "]",
                                        new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                new SegundoPlano("CreaLoteRecepcionDev").execute();
                                            }
                                        },null,contexto);



                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(contexto, getCurrentFocus(), e.getMessage(), "false", true, true);
                            }
                    }
                return false;
            }
        });

        edtx_CantidadEmpaques.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (!edtx_CantidadEmpaques.equals("")&& !edtx_CantidadPiezas.equals("")){

                    if(spnr_Lotes.getAdapter() == null)
                        {
                            new CreaDialogos("Se generará lote nuevo.",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            new SegundoPlano("RegistrarEmpaques").execute();
                                        }
                                    }, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                   new popUpGenerico(contexto,getCurrentFocus(),getString(R.string.error_ingrese_lote_desc) , "false", true, true);

                                }
                            }, contexto);
                        }else{

                             new SegundoPlano("RegistrarEmpaques").execute();
                        }




                }else {
                 new popUpGenerico(Devolucion_NE.this,getCurrentFocus(),"Llene todos los campos",false,true,true);
                }
                return false;
            }
        });
        spnr_Lotes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    tv_cantReg2.setText(((Constructor_Dato)spnr_Lotes.getSelectedItem()).getTag2());

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new com.automatica.AXCPT.Servicios.popUpGenerico(Devolucion_NE.this, getCurrentFocus(), e.getMessage(), "false", true, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                   tv_cantReg2.setText("-");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        button_cerrar_tarima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SegundoPlano("CerrarTarima").execute();
            }
        });
    }

    public void LimpiarCampos(){
        tv_devolucion.setText("");
        tv_Producto.setText("");
        tv_Partida.setText("");
        tv_cantOrig.setText("");
        tv_cantReg.setText("");
        tv_ProductoDet.setText("");
        edtx_CantidadEmpaques.setText("");
        edtx_CantidadPiezas.setText("");
    }

    private class  SegundoPlano extends AsyncTask<String, Void,Void>
    {
        String Tarea;
        DataAccessObject dao= new DataAccessObject();
        cAccesoADatos_Almacen ca= new cAccesoADatos_Almacen(Devolucion_NE.this);


        public SegundoPlano(String Tarea) {
            this.Tarea=Tarea;
        }

        @Override
        protected void onPreExecute() {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                ca= new cAccesoADatos_Almacen(Devolucion_NE.this);
                switch (Tarea){
                    case "ConsultarPallet":
                        /**
                         * dao pallet
                         *
                         */
                        dao = ca.c_ConsultaPalletAbiertoDev(OrdenDevolucion,Partida);
                        break;
                    case "ListarLotes":
                        /**
                         * dao listar Lotes
                         */
                        dao= ca.c_ListarLotesDev(OrdenDevolucion,Partida,NumParte);
                        break;
                    case "ConsultarOrden":
                        /**
                         * dao consultarOrden
                         */
                        dao= ca.c_ConsultaPartidaDevolucion(OrdenDevolucion,Partida);
                        break;
                    case "RegistrarEmpaques":
                        /**
                         * dao registrar Empaques
                         */


                        if(spnr_Lotes.getAdapter() == null)
                            {


                                dao= ca.c_CreaEmpaqueDevSE(OrdenDevolucion,Partida,"",edtx_CantidadPiezas.getText().toString(),edtx_CantidadEmpaques.getText().toString());


                            }else{

                            dao= ca.c_CreaEmpaqueDevSE(OrdenDevolucion,Partida,((Constructor_Dato)spnr_Lotes.getSelectedItem()).getDato(),edtx_CantidadPiezas.getText().toString(),edtx_CantidadEmpaques.getText().toString());


                        }


                        break;

                    case "CreaLoteRecepcionDev":

                        dao = ca.c_CreaLoteRecepcionDev(OrdenDevolucion,
                                Partida,
                                edtx_LoteNuevo.getText().toString(),
                                "1");
                        break;
                    case "CerrarTarima":
                        /**
                         * dao cerrar tarima
                         */
                        dao = ca.c_CierraPalletDevolucion(tv_pallet.getText().toString());
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (dao.iscEstado()){
                    switch (Tarea){
                        case "ConsultarPallet":
                            String CodigoPallet, Empaques;
                            String[] mensaje= dao.getcMensaje().split("#");
                            CodigoPallet = mensaje[0];
                            Empaques = mensaje[1];
                            if(!CodigoPallet.equals(""))
                            {
                                tv_pallet.setText(CodigoPallet);
                                tv_EmpaquesRegistrados.setText(Empaques);
                            }else {
                                tv_pallet.setText("-");
                                tv_EmpaquesRegistrados.setText("-");
                            }
                            break;
                        case "ListarLotes":
                            /**
                             * dao listar Lotes
                             */
                            if(dao.getcTablas() != null)
                            {
                                registroAnteriorSpinner = spnr_Lotes.getSelectedItemPosition();
                                spnr_Lotes.setAdapter(new CustomArrayAdapter(Devolucion_NE.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("LoteAXC","CantidadRecibida","CantidadRegistrada")));
                                spnr_Lotes.setSelection(registroAnteriorSpinner);
                            }else
                            {
                                spnr_Lotes.setAdapter(null);
                            }
                            break;
                        case "CreaLoteRecepcionDev":
                            new SegundoPlano("ListarLotes").execute();

                            break;
                        case "ConsultarOrden":
                            /**
                             * dao consultarOrden
                             */
                            tv_devolucion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Devolucion"));
                            tv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                            tv_Partida.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaERP"));
                            tv_cantOrig.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadPendiente"));
                            tv_cantReg.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadRecibida"));
                            tv_ProductoDet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DNumParte1"));

                            break;
                        case "RegistrarEmpaques":
                            /**
                             * dao registrar Empaques
                             */
                            tv_pallet.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("OrdenCerrada").equals("1"))
                            {
                                new popUpGenerico(Devolucion_NE.this,  getCurrentFocus(), getString(R.string.orden_compra_completada), String.valueOf(dao.iscEstado()), true, true);
                                //new SegundoPlano("ConsultarPallet").execute();



                                new SegundoPlano("CerrarTarima").execute();
                                LimpiarCampos();
                                //  reiniciaVariables();
                                return;
                            }
                            else if (dao.getSoapObject_parced().getPrimitivePropertyAsString("PartidaCerrada").equals("1"))
                            {
                                new popUpGenerico(Devolucion_NE.this, getCurrentFocus(), getString(R.string.orden_compra_partida_completa), String.valueOf(dao.iscEstado()), true, true);
                                //new SegundoPlano("ConsultarPallet").execute();
                                new SegundoPlano("CerrarTarima").execute();
                                LimpiarCampos();
                                return;
                            }else{
                                new SegundoPlano("ConsultarPallet").execute();
                                new SegundoPlano("ConsultarOrden").execute();
                            }
                            //new SegundoPlano("ListarOrdenesCompra").execute();
                            break;
                        case "CerrarTarima":
                            /**
                             * dao cerrar tarima
                             */
                            new popUpGenerico(Devolucion_NE.this,getCurrentFocus(),"Pallet Cerrado",true,true,true);
                            new SegundoPlano("ConsultarPallet").execute();
                            new SegundoPlano("ConsultarOrden").execute();
                            break;
                    }
                }else{
                    new popUpGenerico(Devolucion_NE.this,getCurrentFocus(),dao.getcMensaje(),dao.iscEstado(),true,true);
                }
            }catch (Exception e){
                e.printStackTrace();
                new popUpGenerico(Devolucion_NE.this,getCurrentFocus(),e.getMessage(),false,true,true);
            }
            outAnimation = new AlphaAnimation(1f, 0f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }
}