package com.automatica.AXCMP.c_Consultas.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.adaptadorTabla;
import com.automatica.AXCMP.c_Consultas.ConstructorDatoTitulo;
import com.automatica.AXCMP.constructorTablaEntradaAlmacen;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class frgmt_ConsultaPallet extends DialogFragment
{
    EditText edtx_Empaque;
    Button btn_ReimprimirEtiqueta;
    SoapAction sa = new SoapAction();
    String Area;
    View view;
    TextView txtv_Status;
    constructorTablaEntradaAlmacen cd;
    ArrayList<constructorTablaEntradaAlmacen> lista = null;
    ArrayList<ConstructorDatoTitulo> ArrayData;
    ListView tablaPallet;
    adaptadorTabla adaptador;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        try
            {


                view = inflater.inflate(R.layout.frgmt_consulta_pallet, container, false);
                edtx_Empaque = (EditText) view.findViewById(R.id.editText);
                btn_ReimprimirEtiqueta = (Button) view.findViewById(R.id.button2);
                txtv_Status = (TextView) view.findViewById(R.id.txtv_Status);
                tablaPallet = (ListView) view.findViewById(R.id.lstv_Pallet);

                edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                            {
                                if (!edtx_Empaque.getText().toString().equals(""))
                                    {

                                        SegundoPlano sp = new SegundoPlano("ReimprimeEtiqueta");
                                        sp.execute();


                                    } else
                                    {
                                        new popUpGenerico(getContext(), null, getString(R.string.error_ingrese_pallet), "false", true, true);
//                                handler.post(new Runnable()
//                                {
//                                    @Override
//                                    public void run()
//                                    {
//
//                                        edtx_CodigoPallet.requestFocus();
//                                    }
//                                });
                                    }
                                new esconderTeclado(getActivity());
                            }

                        return false;
                    }
                });

            }catch (Exception e)
            {
                e.printStackTrace();

            }
        return view;
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
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
        //    txtv_Status.setText("Consultando empaque");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
                {
                    switch (tarea)
                        {
                            case "ReimprimeEtiqueta":
                               // Thread.sleep(5000);
                                sa.SOAPConsultaPalletReg(edtx_Empaque.getText().toString(), getContext());

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
                                    case   "ReimprimeEtiqueta":
                                       // getDialog().dismiss();
                                        adaptador = new adaptadorTabla(getContext(), R.layout.list_item_2datos, lista);
                                        tablaPallet.setAdapter(adaptador);

                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                        break;



                                }
                        }
                    if (decision.equals("false"))
                        {

                            txtv_Status.setText(mensaje);
                            edtx_Empaque.requestFocus();
                            edtx_Empaque.setText("");
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(),"false" ,true,true);

                }


        }
    }

    private void sacaDatos()
    {
        try
            {

                lista = new ArrayList<>();
                PropertyInfo pi;
                ArrayData = new ArrayList<>();
                SoapObject tabla = sa.parser();
                if(tabla!=null)
                    {
                        cd = new constructorTablaEntradaAlmacen("Código miaxc_consulta_pallet",edtx_Empaque.getText().toString());
                        lista.add(cd);
                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                            {
                                SoapObject tabla11 = (SoapObject) tabla.getProperty(i);

                                for(int t=0;t<tabla11.getPropertyCount();t++)
                                    {
                                        pi = new PropertyInfo();
                                        tabla11.getPropertyInfo(t,pi);
                                        String name = pi.name;
                                        if(name.equals("FechaCrea"))
                                            {
                                                name = "Fecha Creación";
                                            }
                                        if(name.equals("FechaCad"))
                                            {
                                                name = "Fecha Caducidad";
                                            }
                                        if(name.equals("LoteProveedor"))
                                            {
                                                name = "Lote Proveedor";
                                            }
                                        if(name.equals("CantidadOriginal"))
                                            {
                                                name = "Cantidad Original";
                                            }
                                        if(name.equals("CantidadActual"))
                                            {
                                                name = "Cantidad Actual";
                                            }
                                        if(name.equals("CantidadOriginal"))
                                            {
                                                name = "CantidadOriginal";
                                            }
                                        if(name.equals("Estacion"))
                                            {
                                                name = "Estación";
                                            }
                                        if(name.equals("Revision"))
                                            {
                                                name = "Revisión";
                                            }
                                        cd = new constructorTablaEntradaAlmacen(name,tabla11.getPropertyAsString(t));
                                        lista.add(cd);
                                    }
                                // UbicacionSugerida = tabla11.getPrimitivePropertyAsString("UbicacionSugerida");
                                Log.d("SoapResponse", tabla11.toString());
                            }
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }



}
