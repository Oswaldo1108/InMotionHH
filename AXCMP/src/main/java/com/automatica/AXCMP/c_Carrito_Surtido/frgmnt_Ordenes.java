package com.automatica.AXCMP.c_Carrito_Surtido;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.Adaptador_RV_MenuPrincipal;
import com.automatica.axc_lib.views.constructor_Documento;

import java.util.ArrayList;

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


    //OBJECTS
    private OnFragmentInteractionListener mListener;
    private Adaptador_RV_MenuPrincipal adv;
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
                SegundoPlano sp = new SegundoPlano("ConsultaDocumentos");
                sp.execute();
            }
        });

        rv.setLayoutManager(new GridLayoutManager(getContext(), 3));

//        imgb_OC = view.findViewById(R.id.imageButton3);
//        imgb_OP = view.findViewById(R.id.imageButton);
//        imgb_Tras = view.findViewById(R.id.imageButton2);

        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_menu_inicio));


//        cl_QuickQry = view.findViewById(R.id.cl_BotonesExtra);
//        cl_QuickQry.setVisibility(View.VISIBLE);
//
//        imgb_OC.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, Consulta_Documentos_OC.newInstance("ConsultaPallet", null),frgtag_ConsultaDocumentosOC)
//                        .commit();
//            }
//        });
//
//        imgb_OP.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, Consulta_Documentos.newInstance("ConsultaPallet", null),frgtag_ConsultaDocumentos)
//                        .commit();
//            }
//        });
//
//        imgb_Tras.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, Consulta_Documentos_Tras.newInstance("ConsultaPallet", "ConsultaPallet"),frgtag_ConsultaDocumentosTras)
//                        .commit();
//            }
//        });



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
                                //    dao = ca.cad_RegistraConfiguracionMiAXC();//, , , , , , , , , , , );
                                    break;
                                case "ConsultaDocumentos":
                                    dao = ca.C_OrdenesPorEstacionCM();
                                    //dao = new DataAccessObject(true,"Operación no soportada",null);

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
                          SegundoPlano sp = new SegundoPlano("ConsultaDocumentos");
                          sp.execute();
                            break;
                        case "ConsultaDocumentos":
                            ArrayDocumentos = new ArrayList<>();

//                            if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("DebugMode", false))
//                                {
//
//                            ArrayDocumentos.add(new constructor_Documento("SIN CONTENEDOR","Surtido 1",null).InicializarDatosPrueba());
//                            ArrayDocumentos.add(new constructor_Documento("SIN CONTENEDOR","Surtido 2",null).InicializarDatosPrueba());
//                            ArrayDocumentos.add(new constructor_Documento("SIN CONTENEDOR","Surtido 3",null).InicializarDatosPrueba());
//                            ArrayDocumentos.add(new constructor_Documento("SIN CONTENEDOR","Surtido 4",null).InicializarDatosPrueba());
//                            ArrayDocumentos.add(new constructor_Documento("SIN CONTENEDOR","Surtido 5",null).InicializarDatosPrueba());
//                            ArrayDocumentos.add(new constructor_Documento("SIN CONTENEDOR","Surtido 6",null).InicializarDatosPrueba());

//                                }
//                                else
//                                {
                                    ArrayList<ArrayList<Constructor_Dato>> arrayTablas = dao.getcTablas();



                                    for (ArrayList<Constructor_Dato> a : arrayTablas)
                                        {


                                            if(a.get(1).getDato().equals(null)||a.get(1).getDato().equals("AnyType{}"))
                                                {
                                                    permitir = false;
                                                }else
                                                {
                                                    permitir = true;

                                                }


                                            String TituloDoc = a.get(0).getDato();
                                            String datoDoc = a.get(1).getDato();
                                            a.remove(1);
                                            a.remove(0);
                                            a.remove(0);
                                            a.remove(0);

                                            ArrayDocumentos.add(new constructor_Documento(datoDoc,TituloDoc, a));
                                        }
                            //    }

                            adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);
                            adv.setMostrarBotonMasInf(true);
                            adv.setMensajePrevio("Pedido ");
                            adv.setMostrarEstatus(true);
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

    public void setCardStatus(String Contenedor,int card)
    {
      //  Toast.makeText(getContext(), Contenedor + " " + card, Toast.LENGTH_SHORT).show();
        adv.setCardStatus(Contenedor, adv.getLastSelectedCard());
    }


}
