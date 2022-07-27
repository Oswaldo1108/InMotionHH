package com.automatica.AXCMP.MiAXC.ConsultaPosiciones;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_Posiciones;
import com.automatica.axc_lib.views.constructor_Documento;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class frgmnt_Ordenes extends Fragment
{
    //VARIABLES
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String frgtag_ConsultaDocumentos= "FRGCD";
    private static final String frgtag_ConsultaDocumentosOC= "FRGCDOC";
    private static final String frgtag_ConsultaDocumentosTras= "FRGCDTRAS";
    private String prmRack;
    private String prmLado;

    //OBJECTS
    private OnFragmentInteractionListener mListener;
    private Adaptador_RV_Posiciones adv;
    private ArrayList<constructor_Documento> ArrayDocumentos;
    private RecyclerView rv;

    private boolean permitir;
 //   private int LastSelectedCard = 0;

    //VIEWS
    SwipeRefreshLayout swipeRefreshLayout;


    public int getCantidadDocs()
    {
        return ArrayDocumentos.size();
    }

    public frgmnt_Ordenes()
    {
    }

    public static Fragment newInstance(String param1, String param2)
    {
        frgmnt_Ordenes fragment = new frgmnt_Ordenes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }




    }

    public boolean PermitePasar()
    {
        return permitir;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_frgmnt__menu__inicio, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_MenuPrincipal);

       // ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");



        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
             ActualizarPosiciones(prmRack,prmLado);

            }
        });


        return view;
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
//
//                SegundoPlano sp = new SegundoPlano("ConsultaDocumentos");
//                sp.execute();

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
        boolean ActivaProgressBar(boolean estado);

    }


    public void ActualizarPosiciones(String prmRack,String prmLado)
    {
        try
            {
//                new popUpGenerico(getContext(), null,prmRack, false, true, true);
                    this.prmRack = prmRack;
                    this.prmLado = prmLado;
                new SegundoPlano("ConsultaDocumentos").execute(prmRack,prmLado);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), false, true, true);

            }

    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
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
        protected Void doInBackground(String... params)
        {
            try
                {

                if(!this.isCancelled())
                    {
                        switch (tarea)
                            {
                                case "ConsultaDocumentos":
                                    dao = ca.C_ConsultaPosiciones(params[0],params[1]);
                                    break;
                                default:
                                    dao = new DataAccessObject(false,"Operación no soportada",null);
                                    break;
                            }

                        if (dao.iscEstado())
                            {
                                switch (tarea)
                                    {
                                        case "ConsultaDocumentos":
//                                    Log.i("CONSULTA_INfO", "ENTRE");
                                            ArrayDocumentos = new ArrayList<>();
                                            ArrayList<ArrayList<Constructor_Dato>> arrayTablas = dao.getcTablas();

                                            for (ArrayList<Constructor_Dato> a : arrayTablas)
                                                {
//                                            Log.i("CONSULTA_INfO", "ENTRE");
                                                    String TituloDoc = a.get(0).getDato();
                                                    String datoDoc = a.get(1).getDato() + " Pallets";


                                                    ArrayDocumentos.add(new constructor_Documento("ConsultaRacks", datoDoc, TituloDoc,null));


                                                }

                                            adv = new Adaptador_RV_Posiciones(ArrayDocumentos);
                                            adv.setMostrarBotonMasInf(true);
                                            adv.setMostrarEstatus(true);

                                            break;
                                    }
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
                            case "ConsultaDocumentos":
//                                Log.i("oli",dao.getcMensaje());
                                rv.setLayoutManager(new GridLayoutManager(getContext(), Integer.valueOf(dao.getcMensaje()),GridLayoutManager.HORIZONTAL,false));
                                rv.setHasFixedSize(false);
                                rv.setAdapter(adv);
                            break;
                        }
                }else
                    {
                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                    }
            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage() + dao.getcMensaje(),dao.iscEstado(), true, true);
                }
            try
                {
                    swipeRefreshLayout.setRefreshing(false);
                    mListener.ActivaProgressBar(false);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }

    public void setCardStatus(String Contenedor,int card)
    {
      //  Toast.makeText(getContext(), Contenedor + " " + card, Toast.LENGTH_SHORT).show();
        adv.setCardStatus(Contenedor, adv.getLastSelectedCard());
    }


}
