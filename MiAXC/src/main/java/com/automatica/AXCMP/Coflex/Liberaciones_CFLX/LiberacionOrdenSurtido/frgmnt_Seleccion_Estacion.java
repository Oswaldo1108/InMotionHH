package com.automatica.AXCMP.Coflex.Liberaciones_CFLX.LiberacionOrdenSurtido;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.fragment.app.Fragment;

public class frgmnt_Seleccion_Estacion extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARTIDA = "partida";

    private int Partida = 0;
    private String[] paramArr = null;
    private String str_TipoConsulta;
    private static final String frgtag_ImpEtiqueta= "FRGIMP";


    private OnFragmentInteractionListener mListener;


    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";

    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle;
    private ImageButton btn_Back;
    private Spinner spnr_Lotes;

    private Button btn_ElegirLote;


    RadioGroup rdgrp_Modulos;
    public frgmnt_Seleccion_Estacion()
    {

    }

    public static Fragment newInstance(String[] param, String TipoConsulta,int Partida)
    {
        frgmnt_Seleccion_Estacion fragment = new frgmnt_Seleccion_Estacion();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,param);
        args.putString(ARG_PARAM2,TipoConsulta);
        args.putInt(ARG_PARTIDA,Partida);
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
                Partida = getArguments().getInt(ARG_PARTIDA);
                Log.i("ARGUMENTOSPALLETDET", "HOLA "+str_TipoConsulta);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_seleccion_estacion, container, false);
        DeclaraVariables(view);



        new SegundoPlano("ListaLotes").execute();

        return view;
    }

    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle= view.findViewById(R.id.txtv_Incidencia);

        txtv_Titulo.setText("Selección de estacion");
        txtv_Detalle.setText("Producto - " + str_TipoConsulta);

        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        btn_Back.setVisibility(View.VISIBLE);


        rdgrp_Modulos = view.findViewById(R.id.radioGroup);

//        Log.i("Mamarre", paramArr[0]);
//        edtx_CantidadEmpaques.setText(paramArr[0]);

        btn_ElegirLote = view.findViewById(R.id.btn_ElegirLote);

        spnr_Lotes = view.findViewById(R.id.spinner);


        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });



        btn_ElegirLote.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    mListener.EstacionElegida(Partida,spnr_Lotes.getSelectedItem().toString());
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                }
            });


        Log.i("HOLA", "OnATach");
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
        void EstacionElegida(int Partida, String prmEstacion);

    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(getContext());
        DataAccessObject dao;
        String tarea;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
            //    mListener.ActivaProgressBar(true);

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

                                    case "ListaLotes":
                                        dao = ca.c_ListaEstacionesMiAXC();
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
                            switch (tarea)
                                {
                                    case "ListaLotes":
                                        spnr_Lotes.setAdapter(new CustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item , dao.getcTablasSorteadas("Estacion","Estacion")));
                                        break;


                                    default:
                                           new popUpGenerico(getContext(),null, "Actividad no soportada. " + tarea,false ,true , true);
                                        break;
                                }
                        }else
                        {
                            new popUpGenerico(getContext(), null, dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                    mListener.ActivaProgressBar(false);

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(),dao.iscEstado(), true, true);
                }

            //swipeRefreshLayout.setRefreshing(false);

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
