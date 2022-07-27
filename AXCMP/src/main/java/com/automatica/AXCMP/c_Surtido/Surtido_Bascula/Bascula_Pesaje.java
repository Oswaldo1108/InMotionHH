package com.automatica.AXCMP.c_Surtido.Surtido_Bascula;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.cambiaColorStatusBar;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.Servicios.sobreDispositivo;
import com.automatica.AXCMP.SoapConection.SoapAction;

import org.ksoap2.serialization.SoapObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Bascula_Pesaje extends AppCompatActivity
{
    //region variables
    String TAG = Bascula_Pesaje.class.getSimpleName();
    Button btn_enviar;
    SoapAction sa = new SoapAction();
    FrameLayout progressBarHolder;
    Context contexto = this;
    EditText edtx_Pesaje,edtx_Bascula;
    View vista ;
    Handler h;
    HiloDatosTCP hrd;
    ImageView ic_conexion;
    Handler handler = new Handler();
    TextView txtv_peso,txtv_lbl_Peso,txtv_lbl_Medida;
    RadioGroup rdgrp_pesaje;
    ConstraintLayout cl_semaforo;
    TextView txtv_Pedido,txtv_Producto,txtv_CantPend,txtv_CantReg,txtv_SugEmpaque,txtv_SugLote;

    String Pedido, Partida, NumParte, UM, CantidadTotal, CantidadPendiente, CantidadSurtida, Linea, CodigoPallet,Lote;

    String DireccionBascula, PuertoBascula,DireccionTorreta,PuertoTorreta;
    String Modelo;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    BufferedReader bufferedReader;
    boolean recargar;
    ConstraintLayout cl_TablaRegistro, cl_EmpaqueRegistro;
    Socket  s = null;
    Bundle b;

    double Varianza = .05;
    double PesoPrueba = 5;
    double limite;
    ModBusConnection modBusConnection;
    int modBusROJO = 11;
    int modBusVERDE = 10;
    int modBusAPAGAR = 0;
    int modBusPRENDERCORTO = 1;
    int modBusPRENDERLARGO = 2;
    int modBusColorAnterior = 0;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bascula);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new cambiaColorStatusBar(contexto, R.color.MoradoStd, Bascula_Pesaje.this);


        try {
            getSupportActionBar().setTitle(getString(R.string.recepcion_bascula));
            SacaExtrasIntent();
            declaraVariables();
            agregaListeners();

            Log.d("SoapResponse", "On create" );

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        try {
            getMenuInflater().inflate(R.menu.change_view_toolbar, menu);
            return true;
        }
        catch (Exception ex)
        {
            Toast.makeText( this, "Error al llenar toolbar", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private void declaraVariables()
    {
        try {
            progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
            btn_enviar = (Button) findViewById(R.id.btn_CerrarPesaje);
            // btn_RegSobrantes = (Button) findViewById(R.id.btn_RegistrarSobrantes);
            edtx_Pesaje = (EditText) findViewById(R.id.edtx_Peso);
            edtx_Bascula = (EditText) findViewById(R.id.edtx_Bascula);
            ic_conexion = (ImageView) findViewById(R.id.img_conectado);
            txtv_peso = (TextView) findViewById(R.id.txtv_peso);
            rdgrp_pesaje = (RadioGroup) findViewById(R.id.radioGroup_pesaje);
            cl_semaforo = (ConstraintLayout) findViewById(R.id.ly_semaforo);

            txtv_Pedido = (TextView) findViewById(R.id.txtv_Pedido);
            txtv_Producto = (TextView) findViewById(R.id.txtv_Producto);
            txtv_CantPend = (TextView) findViewById(R.id.txtv_Cantidad);
            txtv_CantReg = (TextView) findViewById(R.id.txtv_CantidadReg);


            txtv_SugEmpaque= (TextView) findViewById(R.id.txtv_SugEmpaque);
            txtv_SugLote = (TextView) findViewById(R.id.txtv_Lote);


            txtv_lbl_Medida = (TextView) findViewById(R.id.txtv_lbl_Medida);
            txtv_lbl_Peso = (TextView) findViewById(R.id.txtv_lbl_Peso);

            cl_EmpaqueRegistro = (ConstraintLayout) findViewById(R.id.cl_LayouPesaje);
            cl_TablaRegistro = (ConstraintLayout) findViewById(R.id.cl_TablaRegistro);

            cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
            cl_TablaRegistro.setVisibility(View.GONE);

            rdgrp_pesaje.check(R.id.rdbtn_pesaje_automatico);



            txtv_Pedido.setText(Pedido);
            txtv_Producto.setText(NumParte);
            txtv_CantPend.setText(CantidadPendiente);
            txtv_CantReg.setText(CantidadSurtida);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
    }

    private void agregaListeners()
    {
        btn_enviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    new popUpGenerico(contexto,vista ,"PESAJE CANCELADO","false" ,true ,true );

                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                           terminarPesaje();

                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtx_Bascula.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {

                    if(!edtx_Bascula.getText().toString().equals(""))
                    {



                        if(hrd!=null)
                            {
                                terminarPesaje();
                                //edtx_Bascula.setText("");
                            }

                        SegundoPlano sp = new SegundoPlano("ConsultarBascula");
                        sp.execute();


                    }else
                    {
                        new popUpGenerico(contexto,vista,getString(R.string.error_ingrese_pallet),"false",true,true);
                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                edtx_Bascula.requestFocus();
                            }
                        });
                    }
                    new esconderTeclado(Bascula_Pesaje.this);
                }
                    return false;
            }
        });

            rdgrp_pesaje.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {

                    Log.e("SoapResponse", "onCheckedChanged: "+checkedId);
                    RadioButton rdb = (RadioButton) rdgrp_pesaje.findViewById(checkedId);
                    int index = rdgrp_pesaje.indexOfChild(rdb);

                    switch (index)
                    {
                        case 0:
                            cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_amarillo);
                            cl_semaforo.setVisibility(View.VISIBLE);
                            edtx_Pesaje.setVisibility(View.GONE);
                            txtv_peso.setVisibility(View.VISIBLE);
                            edtx_Pesaje.setEnabled(false);
                            txtv_lbl_Medida.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            txtv_lbl_Peso.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                            break;

                        case 1:
                            cl_semaforo.setBackgroundResource(R.drawable.orilla_layout_input_group);
                            cl_semaforo.setVisibility(View.VISIBLE);
                            txtv_peso.setVisibility(View.GONE);
                            edtx_Pesaje.setVisibility(View.VISIBLE);
                            edtx_Pesaje.setEnabled(true);
                            txtv_lbl_Medida.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            txtv_lbl_Peso.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                            terminarPesaje();
                            break;

                            default:
                                cl_semaforo.setVisibility(View.VISIBLE);
                                txtv_peso.setVisibility(View.VISIBLE);
                                edtx_Pesaje.setVisibility(View.GONE);
                                txtv_lbl_Medida.setTextColor(getResources().getColor(R.color.blancoLetraStd));
                                txtv_lbl_Peso.setTextColor(getResources().getColor(R.color.blancoLetraStd));

                            break;
                    }
                }

            });

    }

    private void SacaExtrasIntent()
    {
        try
        {

            b = getIntent().getExtras();
            Pedido= b.getString("Pedido");
            Partida= b.getString("Partida");
            NumParte= b.getString("NumParte");
            UM= b.getString("UM");
            CantidadTotal= b.getString("CantidadTotal");
            CantidadPendiente= b.getString("CantidadPendiente");
            CantidadSurtida= b.getString("CantidadSurtida");
            Linea= b.getString("lINEA");

            Log.e("SoapResponse", "SacaExtrasIntent: ");



        }catch (Exception e)
        {
            Log.i("PorEmpaque", "SacaExtrasIntent: Error sacando del Intent Extras " + e.getMessage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {


            int id = item.getItemId();

            if (!recargar) {
                if ((id == R.id.InformacionDispositivo))
                {
                    new sobreDispositivo(contexto, vista);
                }
                if ((id == R.id.recargar))
                {

                    Recarga();
                }
                if ((id == R.id.CambiarVista))
                {
                    if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
                    {
//                        if(RegEmpPalletAbierto==null)
//                        {
//                            return false;
//                        }
                        cl_EmpaqueRegistro.setVisibility(View.GONE);
                        cl_TablaRegistro.setVisibility(View.VISIBLE);
                        item.setIcon(R.drawable.ic_add_box);
                        item.setChecked(true);
                        SegundoPlano sp = new SegundoPlano("ConsultaPalletAbierto");
                        sp.execute();

                    } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE)
                    {
                        cl_TablaRegistro.setVisibility(View.GONE);
                        cl_EmpaqueRegistro.setVisibility(View.VISIBLE);
                        item.setIcon(R.drawable.ic_change_view);

                        SegundoPlano sp = new SegundoPlano("SugiereEmpaque");
                        sp.execute();

                    }

//                    if (!edtx_ConfirmarEmpaque.getText().toString().equals(""))
//                    {
//                        tabla_Salidas.getDataAdapter().clear();
//                        SegundoPlano sp = new SegundoPlano("ConsultaOrdenProduccion");
//                        sp.execute();
//                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,getCurrentFocus(),e.getMessage(),"false",true,true);
        }
        return super.onOptionsItemSelected(item);
    }

    class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        String tarea;
        String decision,mensaje;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
           ic_conexion.setImageResource(R.drawable.ic_conectado);
            Log.i("SoapResponse", "OnPreex");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            Log.i("SoapResponse", "Antes switch Do in backgound");

                try
                {
                    switch (tarea)
                    {
                        case "ConsultarBascula":

                            sa.SOAPConsultaBascula(edtx_Bascula.getText().toString(),contexto);

                            break;
                        case "CerrarPesaje":

                            break;
                    }

                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();

                    if(decision.equals("true"))
                    {
                        sacaDatos();
                    }

                    Log.i("SoapResponse", "Do in backgound");

                } catch (Exception e)
                {
                    e.printStackTrace();
                    mensaje = e.getMessage();
                }

            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {
            try
                {
                    if (decision.equals("true"))
                        {
                            switch (tarea)
                                {
                                    case "ConsultarBascula":
                                        hrd = new HiloDatosTCP(DireccionBascula, PuertoBascula, DireccionTorreta, PuertoTorreta);
                                        hrd.execute();
                                        break;
                                }
                        }
                    if (decision.equals("false"))
                        {
                            new popUpGenerico(contexto, vista, mensaje, decision, true, true);
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(contexto,vista , e.getMessage(),"false" ,true,true);
                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);

        }
    }

    class HiloDatosTCP extends AsyncTask<Void,Void,Void>
    {

        String DireccionBascula,DireccionTorreta;
        public HiloDatosTCP(String DireccionBascula,String PuertoBascula,String DireccionTorreta,String PuertoTorreta)
        {
            this.DireccionBascula = DireccionBascula;
            this.DireccionTorreta = DireccionTorreta;
        }
        String mensaje;
        ObjectInputStream ois = null;
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPreExecute()
        { Log.d("SoapResponse", "HRD on preexecute ");
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                limite = PesoPrueba*Varianza;
                 Log.e("SoapResponse", "Varianza calculada");

//                String oldName = Thread.currentThread().getName();
//                Thread.currentThread().setName("Enviar");
//
                modBusConnection = new ModBusConnection();
                modBusConnection.setHOST(DireccionTorreta);
                modBusConnection.setPORT(8899);
                s =   new Socket(DireccionBascula, 8899);
                if(s.isConnected())
                {
                    Log.e("SoapResponse", "Is conected " + s.getInetAddress() + " " + s.getPort());
                }
                else
                {
                    Log.e("SoapResponse", "Not conected");
                }
               OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream(), "UTF-8");
                //String data = "P\r";
                String data = "P";
              //  osw.write(data,0 ,data.length());
                modBusConnection.IniciarConexion();
                Torreta(modBusROJO);

                while(!isCancelled())
                {
                    try
                    {
                        osw =new OutputStreamWriter(s.getOutputStream(), "ISO-8859-1");
                        bufferedReader= new BufferedReader(new InputStreamReader(s.getInputStream()));      //Envio de petición del peso
//                        osw.write(data,0 ,data.length());
//                        osw.flush();
                    //   Thread.sleep(80);

                        final String dato= bufferedReader.readLine();           //Lectura del dato de peso


                        handler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    String pesoParseado = dato.replaceAll("[-||KG|M|O|P|?|kg|N|E]", "");
                                    float pesoExtraido = 0;
                                    int colorActual=11;
                                    if (!pesoParseado.equals(""))
                                    {
                                        pesoExtraido = Float.parseFloat(pesoParseado);

                                    txtv_peso.setText(String.valueOf(pesoExtraido));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                    {
                                        if (pesoExtraido <= 0)
                                        {
                                            cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_rojo);
                                            colorActual = modBusROJO;
                                        }else
                                            {
                                            cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_amarillo);
                                            colorActual = modBusROJO;
                                            }

                                        if (pesoExtraido < PesoPrueba+limite && pesoExtraido >PesoPrueba-limite)
                                        {
                                            cl_semaforo.setBackgroundResource(R.drawable.orilla_bascula_verde);
                                            colorActual = modBusVERDE;

                                        }
                                    } else
                                    {
                                        Toast.makeText(contexto, "else", Toast.LENGTH_SHORT).show();
                                    }
                                        Torreta(colorActual);
                                        Log.d("SoapResponse", "doInBackground: "+ colorActual);
                                    }

                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                    apagarTorreta();
                                    Log.e("SoapResponse", "doInBackground: CATCH"  + e.getMessage());
                                }
                            }
                        });


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        s.close();
                    }


                }

                if(isCancelled())
                {

                    s.close();
                  //  ois.close();
                }

//                Thread.currentThread().setName(oldName);
            }catch (Exception e)
            {
                e.printStackTrace();
                mensaje = e.getMessage();
            }
            finally
            {

                try {
                    if (ois != null) ois.close();
                    if (s != null) s.close();
                    Log.e("SoapResponse", "DataReceiver: conexion cerrada por servidor ");
                    System.out.println("");
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {
            new popUpGenerico(contexto,vista ,mensaje + "ON post" ,"false" ,true ,true );
            txtv_peso.setText("----");
            apagarTorreta();
        }

        @Override
        protected void onCancelled()
        {
            Log.e("SoapResponse", "onCancelled: Cancelado" );
            try {
                txtv_peso.setText("----");
             terminarPesaje();

            }catch (Exception e)
            {

                e.printStackTrace();
                new popUpGenerico(contexto,vista ,e.getMessage() ,"false" ,true ,true );
            }
            super.onCancelled();

        }
    }
    private boolean terminarPesaje()
    {
        try
        {

            modBusColorAnterior = 0;
            if(!hrd.isCancelled())
            {
                hrd.cancel(true);
            }

            if(s!=null)s.close();

            apagarTorreta();
            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,vista ,e.getMessage() ,"false" , true,true );
        return false;
        }

    }

    private void Recarga()
    {
            if (cl_EmpaqueRegistro.getVisibility() == View.VISIBLE)
            {
              SegundoPlano sp = new SegundoPlano("SugiereEmpaque");
                sp.execute();

            } else if (cl_TablaRegistro.getVisibility() == View.VISIBLE) {
              SegundoPlano sp = new SegundoPlano("ConsultaPalletAbierto");
                sp.execute();

            }

    }

    private void Torreta(int Color)
    {
        if(Color!=modBusColorAnterior)
        {
            if(Color == 10)
            {
                modBusConnection.writeRegisterClickEvent(vista,Color, modBusPRENDERLARGO);
                Log.i(TAG, "verde prendido");
            }
            if(Color == 11)
            {
                modBusConnection.writeRegisterClickEvent(vista, Color, modBusPRENDERCORTO);
                Log.i(TAG, "rojo prendido");
            }
            modBusConnection.writeRegisterClickEvent(vista, modBusColorAnterior, modBusAPAGAR);
        }
        modBusColorAnterior = Color;

    }

    private void apagarTorreta()
    {
        if(modBusConnection!=null)
        {
            modBusConnection.writeRegisterClickEvent(vista, modBusROJO, modBusAPAGAR);
            modBusConnection.writeRegisterClickEvent(vista, modBusVERDE, modBusAPAGAR);
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning_black_24dp)

                    .setTitle("¿Terminar pesaje en curso?").setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                          terminarPesaje();
                            Bascula_Pesaje.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            //   super.onBackPressed();
        }catch (Exception e)
        {
            new popUpGenerico(contexto,vista ,e.getMessage() ,"false" ,true , true);
        }
    }

    @Override
    protected void onDestroy()
    {
        terminarPesaje();
        super.onDestroy();
    }


    public void sacaDatos()
    {
        SoapObject tabla = sa.parser();


        for(int i = 0; i<tabla.getPropertyCount();i++)
            {
                try {

                    SoapObject tabla1 = (SoapObject) tabla.getProperty(i);
                    Log.d("SoapResponse",tabla1.toString());
                    DireccionBascula=  tabla1.getPrimitivePropertyAsString("DireccionIP");
                    DireccionTorreta=  tabla1.getPrimitivePropertyAsString("DireccionIPTorreta");
                    PuertoBascula = tabla1.getPrimitivePropertyAsString("Puerto");
                    PuertoTorreta= tabla1.getPrimitivePropertyAsString("PuertoTorreta");
                    Modelo= tabla1.getPrimitivePropertyAsString("Modelo");

                    //FechaCaducidad = tabla1.getPrimitivePropertyAsString("FechaCaducidad");



                }catch (Exception e)
                    {

                        e.printStackTrace();

                        //new popUpGenerico(contexto,vista,e.getMessage(),"AXC",true,true);
                    }
            }
    }
}
