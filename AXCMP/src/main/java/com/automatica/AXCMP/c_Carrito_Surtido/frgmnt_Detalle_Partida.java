package com.automatica.AXCMP.c_Carrito_Surtido;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.TableAdapter_Partidas;

import org.ksoap2.serialization.SoapObject;

import androidx.fragment.app.Fragment;

public class frgmnt_Detalle_Partida extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta = "";
    private OnFragmentInteractionListener mListener;
    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";
    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle,txtv_Producto,txtv_Lote,txtv_CantidadActual,txtv_CantidadTotal,txtv_UM,txtv_Estatus,txtv_TipoDetalle,txtv_DescProd,txtv_Ubicacion;
    private ListView lv_Empaques;
    private ImageButton btn_Back;


    public frgmnt_Detalle_Partida()
    {
    }
    public static frgmnt_Detalle_Partida newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Detalle_Partida fragment = new frgmnt_Detalle_Partida();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,param);
        args.putString(ARG_PARAM2,TipoConsulta);
        fragment.setArguments(args);
        return fragment;
    }

    public String getDocumento()
    {
        return paramArr[0];//AQUI REGRESO EL DOCUMENTO PARA TENER REGISTRAR EL QUE ESTA EN LA PANTALLA
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                paramArr= getArguments().getStringArray(ARG_PARAM1);
                str_TipoConsulta= getArguments().getString(ARG_PARAM2);
                Log.i("ARGUMENTOSPALLETDET", "HOLA "+str_TipoConsulta);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detalle_pallet, container, false);
        DeclaraVariables(view);

        return view;
    }
    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle= view.findViewById(R.id.txtv_Incidencia);
        txtv_Producto= view.findViewById(R.id.txtv_Producto);
        txtv_Lote= view.findViewById(R.id.txtv_Lote);
       // txtv_CantidadActual = view.findViewById(R.id.txtv_CantAct);
        txtv_CantidadTotal= view.findViewById(R.id.txtv_CantTot);
        //txtv_UM = view.findViewById(R.id.txtv_UM);
        txtv_Estatus= view.findViewById(R.id.txtv_Estatus);
        lv_Empaques = view.findViewById(R.id.lv_EmpaquesPallet);
        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        //txtv_DescProd = view.findViewById(R.id.txtv_Producto2);
      //  txtv_Ubicacion = view.findViewById(R.id.txtv_UM2);


        txtv_TipoDetalle= view.findViewById(R.id.txtv_TipoDetalle);

        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Detalle_Partida.this).commit();
            }
        });
    }
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
            {
                mListener.onFragmentInteraction(uri);
            }
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            {
                mListener = (OnFragmentInteractionListener) context;
            } else
            {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
        SegundoPlano sp = new SegundoPlano("ConsultaDocumento");
        sp.execute();
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(getContext());
        DataAccessObject dao;
        String tarea;
        View view;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                mListener.ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    if(!this.isCancelled())
                        {


                            switch (tarea)
                                {

                                    case "ConsultaDocumento":

                                        if(paramArr[0].equals("INICIALIZACION"))
                                            {
                                                dao = new DataAccessObject(true,"",null);
                                                break;
                                            }

                                        dao = ca.C_DetallePedido(paramArr[0]);
                                        break;

                                    default:
                                     dao = new DataAccessObject(false,"Operación no soportada. ["+ tarea+ "]",null);
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
                            if(paramArr[0].equals("INICIALIZACION"))
                                {
                                    Log.i("SOAP","FRAGMENTO DE DETALLE INICIALIZADO.");
                                    return;
                                }

                            SoapObject so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                            switch (tarea)
                                {
                                    case "ConsultaDocumento":

                                        Log.i("SOAP",dao.getSoapObject().getProperty(1).toString());
                                        txtv_Detalle.setText("["+so_ResultSet.getPrimitivePropertyAsString("NumParte")+"] - " + so_ResultSet.getPrimitivePropertyAsString("DNumParte1"));
                                        txtv_Titulo.setText( "Pedido " + so_ResultSet.getPrimitivePropertyAsString("OrdenProd"));

                                        //                                        txtv_Detalle.setText("Consulta de Pallet");
//                                        txtv_Producto.setText(so_ResultSet.getPrimitivePropertyAsString("Producto"));
//                                        txtv_Lote.setText(so_ResultSet.getPrimitivePropertyAsString("Lote"));
//                                        txtv_CantidadActual.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_CantidadTotal.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadTotal"));
//                                        txtv_UM.setText(so_ResultSet.getPrimitivePropertyAsString("UnidadMedida"));
                                        txtv_Estatus.setText(so_ResultSet.getPrimitivePropertyAsString("Estatus"));
//                                        txtv_DescProd.setText(so_ResultSet.getPrimitivePropertyAsString("DescNumParte"));
//                                        txtv_Ubicacion.setText(so_ResultSet.getPrimitivePropertyAsString("CodigoPosicion"));
                                        dao.getcTablas().remove(0);
                                        lv_Empaques.setAdapter(new TableAdapter_Partidas(getContext(), R.layout.lv_partida_v2, dao.getcTablas()));
                                        break;
                                }
                        }else
                        {
                            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }
            mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }
    public void ConsultarOrden(String[] prmDocumento)
    {
        paramArr[0] = prmDocumento[1];
        new SegundoPlano("ConsultaDocumento").execute();
    }

}
