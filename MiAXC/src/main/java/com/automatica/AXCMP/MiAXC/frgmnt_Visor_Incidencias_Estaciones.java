package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.Principal.DateTimeSince;
import com.automatica.AXCMP.Principal.constructor_Documento;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;
import com.automatica.AXCMP.Constructor_Dato;

import java.util.ArrayList;

public class frgmnt_Visor_Incidencias_Estaciones extends Fragment implements Adaptador_RV_MenuPrincipal.onClickRV

{

    //VARIABLES
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    //OBJECTS
    private OnFragmentInteractionListener mListener;
    Adaptador_RV_MenuPrincipal adv;
    ArrayList<constructor_Documento> ArrayDocumentos;
    ArrayList<Constructor_Dato> arrayPartidas;
    RecyclerView rv;

    //VIEWS
    SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout cl_QuickQry;



    public frgmnt_Visor_Incidencias_Estaciones()
    {
        // Required empty public constructor
    }

    public static frgmnt_Visor_Incidencias_Estaciones newInstance(String param1, String param2)
    {
        frgmnt_Visor_Incidencias_Estaciones fragment = new frgmnt_Visor_Incidencias_Estaciones();
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

        view.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                Log.i("ONKEY","onKEYEVENT" + String.valueOf(keyCode) + " event" + event.getAction());

                if(keyCode == KeyEvent.KEYCODE_BACK)
                    {
                        onButtonPressed(Uri.parse("frgmnt_ConsultaDetalleIncidencia"));
                    }
                return false;
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                SegundoPlano sp = new SegundoPlano("ConsultaEstaciones");
                sp.execute();
//                onButtonPressed(Uri.parse("frgmnt_ConsultaDetalleIncidencia"));

            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_consulta_incidencias));


        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);


        cl_QuickQry = view.findViewById(R.id.cl_BotonesExtra);
        cl_QuickQry.setVisibility(View.GONE);

        return view;
    }

    public void onButtonPressed(Uri uri)
    {
        Log.i("ONBUTTONPRESSED","onbuttonpressed");
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

                     SegundoPlano sp = new SegundoPlano("ConsultaEstaciones");
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

    @Override
    public void clickBotonMasInfo(String[] datos)
    {

        Log.i("kMasInfFrgmntInc","Tarea "+ datos[0]+datos[1]);
        switch (datos[0])
                    {
                        case "Estacion":        //Se debe disparar este evento al dar click en el boton de ver más cuando se ve el listado de estaciones por incidencia
                            SegundoPlano sp = new SegundoPlano("ConsultaIncidenciasEst");
                            sp.execute(datos[2]);

                            break;
                        case "Incidencia":      //Se debe disparar este evento cuando ya se esta en el listados de incidencias de una estacion
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .add(R.id.fl_ContenedorFragments, frgmnt_ConsultaDetalleIncidencia.newInstance(datos[1], null),"FRGDETINC")
                                    .commit();
                            break;

                    }

    }



    public interface OnFragmentInteractionListener
    {

        void onFragmentInteraction(Uri uri);
        void onFragmentInteraction(String dato1,String dato2);
        boolean ActivaProgressBar(boolean estado);
        void onBackPressed();

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
            try
            {
                mListener.ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(String... Params)
        {

            try
                {
                if(!this.isCancelled())
                    {
                        switch (tarea)
                            {
                                case "ConsultaEstaciones":
                                    dao = ca.cad_ConsultaEstacionesIncidencia();
                                    break;

                                case "ConsultaIncidenciasEst":
                                    dao = ca.cad_ConsultaIncidenciasEst(Params[0]);
                                    break;
                                default:
                                    dao = new DataAccessObject(false,"Tarea no soportada " + tarea,null);
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
                    ArrayList<ArrayList<Constructor_Dato>> arrayTablas;
                if (dao.iscEstado())
                {
                    switch (tarea)
                        {
                        case "ConsultaEstaciones":

                            ArrayDocumentos = new ArrayList<>();
                           arrayTablas = dao.getcTablas();

                            for(ArrayList<Constructor_Dato> a:arrayTablas)
                                {
                                    String TituloDoc =a.get(0).getTitulo();
                                    String datoDoc = a.get(0).getDato();
                                    String tagDocumento = a.get(0).getTitulo();

                                    a.remove(0);
                                    ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,null));
                                }

                            adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);

                            adv.setOicl(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    mListener.onFragmentInteraction("Dato1", "Dato2");


                                }
                            });
                            rv.setHasFixedSize(false);
                            rv.setItemViewCacheSize(10);
                            rv.setAdapter(adv);

                            break;

                            case "ConsultaIncidenciasEst":

                                ArrayDocumentos = new ArrayList<>();
                                 arrayTablas = dao.getcTablas();

                                for(ArrayList<Constructor_Dato> a:arrayTablas)
                                    {
                                        String TituloDoc = a.get(0).getDato();//Incidencia
                                        String datoDoc = "Hace " + DateTimeSince.getDateTimeSince(a.get(1).getDato());//Fecha
                                        String tagDocumento = a.get(0).getTitulo();
                                        a.remove(0);//Con estos quito los campos que ya puse en el header
                                        a.remove(0);
                                       for(Constructor_Dato b:a)
                                            {
                                                if(b.getTitulo().equals("Fecha"))
                                                {
                                                    b.setDato(DateTimeSince.convertDate(b.getDato()));
                                                }
                                            }
                                        ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,a));
                                    }

                                adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);

                                adv.setOicl(new AdapterView.OnItemClickListener()
                                {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                    {
                                        //mListener.onFragmentInteraction("Dato1", "Dato2");
                                        SegundoPlano sp = new SegundoPlano("ConsultaIncidenciasEst");
                                        sp.execute();
                                    }
                                });

                                rv.setHasFixedSize(false);
                                rv.setItemViewCacheSize(10);
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
            mListener.ActivaProgressBar(false);

            swipeRefreshLayout.setRefreshing(false);

//            outAnimation = new AlphaAnimation(1f, 0f);
//            outAnimation.setDuration(200);
//            progressBarHolder.setAnimation(outAnimation);
//            progressBarHolder.setVisibility(View.GONE);
//            recargar = false;
        }

        @Override
        protected void onCancelled()
        {
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }




}
