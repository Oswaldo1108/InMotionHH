package com.automatica.AXCMP;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.views.Adaptador_RV_TimeLine;
import com.automatica.axc_lib.views.constructor_Documento;


import java.util.ArrayList;

public class frgmnt_Timeline_Pentagono extends Fragment
{
    //VARIABLES
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String frgtag_ConsultaDocumentos= "FRGCD";
    private static final String frgtag_ConsultaDocumentosOC= "FRGCDOC";
    private static final String frgtag_ConsultaDocumentosTras= "FRGCDTRAS";


    //OBJECTS
    private OnFragmentInteractionListener mListener;
    private Adaptador_RV_TimeLine adv;
    private ArrayList<constructor_Documento> ArrayDocumentos;
    private RecyclerView rv;


    //VIEWS
    SwipeRefreshLayout swipeRefreshLayout;


    public frgmnt_Timeline_Pentagono()
    {
    }

    public static frgmnt_Timeline_Pentagono newInstance(String param1, String param2)
    {
        frgmnt_Timeline_Pentagono fragment = new frgmnt_Timeline_Pentagono();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_frgmnt__menu__inicio, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_MenuPrincipal);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");



        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                SegundoPlano sp = new SegundoPlano("ConsultaDocumentos");
                sp.execute();
            }
        });
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);



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

                SegundoPlano sp = new SegundoPlano("ConsultaDocumentos");
                sp.execute();

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

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {

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
//                    if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("DebugMode", false))
//                        {
//                            dao = new DataAccessObject(true,"",null);
//                            return null;
//                        }

                if(!this.isCancelled())
                    {
                        switch (tarea)
                            {
                                case "RegistraConfigMiAXC":
                                    //dao = ca.C_IniciaSurtidoCM();//, , , , , , , , , , , );
                                    break;
                                case "ConsultaDocumentos":
                                    dao = new DataAccessObject(true,"",null);//ca.cad_ConsultaResumenMIAXC();
                                    break;
                                default:
                                    dao = new DataAccessObject(false,"Operación no soportada",null);
                                    break;
                            }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    dao.setcMensaje(e.getMessage());
                    dao.setcEstado(false);
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
                        { case "RegistraConfigMiAXC":
                         new SegundoPlano("ConsultaDocumentos").execute();
                            break;
                        case "ConsultaDocumentos":
                            ArrayDocumentos = new ArrayList<>();

//                            if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("DebugMode", false))
//                                {
                                    ArrayDocumentos.add(new constructor_Documento("Terminado    ","ORDEN 121212",null).InicializarDatosPrueba());
                                   // ArrayDocumentos.add(new constructor_Documento("0982C1","Azul 2",null).InicializarDatosPrueba());
                                    ArrayDocumentos.add(new constructor_Documento("En proceso","ORDEN 131313",null).InicializarDatosPrueba());
                                    ArrayDocumentos.add(new constructor_Documento("Detenido","ORDEN 141414",null).InicializarDatosPrueba());
//                                    ArrayDocumentos.add(new constructor_Documento("0077C8","Azul 1",null));
//                                   // ArrayDocumentos.add(new constructor_Documento("0982C1","Azul 2",null));
//                                    ArrayDocumentos.add(new constructor_Documento("0142A0","Azul AXCBASE",null));
//                                    ArrayDocumentos.add(new constructor_Documento("0982C1","Azul MIAXC",null));
//                                }
//                                else
//                                {
//                                    ArrayList<ArrayList<constructorTablaEntradaAlmacen>> arrayTablas = dao.getcTablas();
//                                    for (ArrayList<constructorTablaEntradaAlmacen> a : arrayTablas)
//                                        {
//                                            String TituloDoc = a.get(0).getDato();
//
//                                            switch (TituloDoc)
//                                                {
//                                                    case "OrdenesSurtido":
//                                                        TituloDoc = "Ordenes de Surtido";
//                                                        break;
//
//                                                    case "OrdenesProduccion":
//                                                        TituloDoc = "Ordenes de Producción";
//
//                                                        break;
//
//                                                    case "OrdenesEmbarque":
//                                                        TituloDoc = "Ordenes de Embarque";
//
//                                                        break;
//                                                    case "OrdenesCompra":
//                                                        TituloDoc = "Ordenes de Compra";
//
//                                                        break;
//
//                                                    case "Produccion":
//                                                        TituloDoc = "Producción";
//
//                                                        break;
//
//
//                                                    case "ProductoTerminado":
//                                                        TituloDoc = "Producto Terminado";
//
//                                                        break;
//                                                }

//
//                                            String datoDoc = a.get(1).getDato();
//                                            a.remove(1);
//                                            a.remove(0);
//                                            ArrayDocumentos.add(new constructor_Documento(datoDoc,TituloDoc, a));
//                                        }
                            //    }

                            adv = new Adaptador_RV_TimeLine(ArrayDocumentos);
                            adv.setMostrarBotonMasInf(true);
                            adv.setMostrarEstatus(true);
                            adv.setHasStableIds(true);
                            rv.setHasFixedSize(false);
                            rv.setItemViewCacheSize(20);

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
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }

            swipeRefreshLayout.setRefreshing(false);
            try
                {
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




}
