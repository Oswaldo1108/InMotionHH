package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;
import com.automatica.AXCMP.adaptadorTabla;

import org.ksoap2.serialization.SoapObject;

public class frgmnt_Consulta_Pallet_Det extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta;


    private OnFragmentInteractionListener mListener;


    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";

    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle,txtv_Producto,txtv_Lote,txtv_CantidadActual,txtv_CantidadTotal,txtv_UM,txtv_Estatus,txtv_TipoDetalle,txtv_DescProd,txtv_Ubicacion;
    private ListView lv_Empaques;
    private ImageButton btn_Back;


    public frgmnt_Consulta_Pallet_Det()
    {
    }
    public static frgmnt_Consulta_Pallet_Det newInstance(String[] param,String TipoConsulta)
    {
        frgmnt_Consulta_Pallet_Det fragment = new frgmnt_Consulta_Pallet_Det();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,param);
        args.putString(ARG_PARAM2,TipoConsulta);
        fragment.setArguments(args);
        return fragment;
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
        txtv_CantidadActual = view.findViewById(R.id.txtv_CantAct);
        txtv_CantidadTotal= view.findViewById(R.id.txtv_CantTot);
        txtv_UM = view.findViewById(R.id.txtv_UM);
        txtv_Estatus= view.findViewById(R.id.txtv_Estatus);
        lv_Empaques = view.findViewById(R.id.lv_EmpaquesPallet);
        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        txtv_DescProd = view.findViewById(R.id.txtv_Producto2);
        txtv_Ubicacion = view.findViewById(R.id.txtv_UM2);


        txtv_TipoDetalle= view.findViewById(R.id.txtv_TipoDetalle);

        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Consulta_Pallet_Det.this).commit();

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
        SegundoPlano sp = new SegundoPlano(str_TipoConsulta);
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


                            switch (str_TipoConsulta)
                                {
                                    case "Pallet":
                                    case "ConsultaPallets":
                                        dao = ca.cad_ConsultaPalletDet(paramArr[0],paramArr[1]);
                                        break;
                                    case "CodigoPallet":

                                    case "ConsultaPallet":
                                        dao = ca.cad_ConsultaPalletDet(paramArr[1],paramArr[2]);
                                        break;

                                    case "ConsultaEmpaque":
                                        dao = ca.cad_ConsultaEmpaqueDet(paramArr[0],paramArr[1]);
                                        break;

                                         default:
                                             dao = new DataAccessObject(false,"Operacion no soportada.",null);

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
                            SoapObject so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                            switch (str_TipoConsulta)
                                {
                                    case "CodigoPallet":
                                    case "Pallet":
                                    case "ConsultaPallets":

                                    case "ConsultaPallet":
                                    //    SoapObject so_Tabla = (SoapObject) so_ResultSet.getProperty(1);

                                        Log.i("SOAP",dao.getSoapObject().getProperty(1).toString());
                                      //  Log.i("SOAP",so_Tabla.toString());

                                        txtv_Titulo.setText("["+so_ResultSet.getPrimitivePropertyAsString("Modulo")+"] " + so_ResultSet.getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_Detalle.setText("Consulta de Pallet");
                                        txtv_Producto.setText(so_ResultSet.getPrimitivePropertyAsString("Producto"));
                                        txtv_Lote.setText(so_ResultSet.getPrimitivePropertyAsString("Lote"));
                                        txtv_CantidadActual.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_CantidadTotal.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadOriginal"));
                                        txtv_UM.setText(so_ResultSet.getPrimitivePropertyAsString("UnidadMedida"));
                                        txtv_Estatus.setText(so_ResultSet.getPrimitivePropertyAsString("Status"));
                                        txtv_DescProd.setText(so_ResultSet.getPrimitivePropertyAsString("DescNumParte"));
                                        txtv_Ubicacion.setText(so_ResultSet.getPrimitivePropertyAsString("CodigoPosicion"));

                                        txtv_TipoDetalle.setText("Empaques");

                                        lv_Empaques.setAdapter(new adaptadorTabla(getContext(), R.layout.list_item_2_datos_fragment, dao.getcTablasSorteadas("CodigoEmpaque", "Modulo")));

                                        break;

                                    case "ConsultaEmpaque":


                                       so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                                        //    SoapObject so_Tabla = (SoapObject) so_ResultSet.getProperty(1);

                                        Log.i("SOAP",dao.getSoapObject().getProperty(1).toString());
                                        //  Log.i("SOAP",so_Tabla.toString());

                                        txtv_Titulo.setText("["+so_ResultSet.getPrimitivePropertyAsString("Modulo")+"] " + so_ResultSet.getPrimitivePropertyAsString("CodigoEmpaque"));
                                        txtv_Detalle.setText("Consulta de Empaque");
                                        txtv_Producto.setText(so_ResultSet.getPrimitivePropertyAsString("Producto"));
                                        txtv_Lote.setText(so_ResultSet.getPrimitivePropertyAsString("Lote"));
                                        txtv_CantidadActual.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_CantidadTotal.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadOriginal"));
                                        txtv_UM.setText(so_ResultSet.getPrimitivePropertyAsString("UnidadMedida"));
                                        txtv_Estatus.setText(so_ResultSet.getPrimitivePropertyAsString("Status"));
                                        txtv_TipoDetalle.setText("Pallet");


                                        lv_Empaques.setAdapter(new adaptadorTabla(getContext(), R.layout.list_item_2_datos_fragment, dao.getcTablasSorteadas("CodigoPallet", "Modulo")));

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

            //swipeRefreshLayout.setRefreshing(false);
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




}
