package com.automatica.AXCMP.ImpresionEtiquetas;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.automatica.AXCMP.Constructor_Dato;
import com.automatica.AXCMP.MiAXC.frgmnt_Consulta_Documento_Det;
import com.automatica.AXCMP.MiAXC.frgmnt_Consulta_Documento_Pallets;
import com.automatica.AXCMP.MiAXC.frgmnt_Consulta_Documento_Partidas;
import com.automatica.AXCMP.MiAXC.frgmt_Consulta_Camera_view;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class Impresion_Etiquetas extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //OBJETOS
    private OnFragmentInteractionListener mListener;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;


    //VIEWS
    private ViewPager viewPager;
    Toolbar actionBarToolBar;

    //
    private static final String frgtag_ConsultaDocumentos= "FRGCD";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    private static final String frgtag_ConsultaDocDet= "FRGCDD";
    private static final String frgtag_ConsultaDocPart = "FRGCDP";
    private static final String frgtag_ConsultaDocPal= "FRGCDPL";


    private DataAccessObject dao;


    frgmnt_Consulta_Documento_Det det = null;
    frgmnt_Consulta_Documento_Pallets pal = null;
    frgmnt_Consulta_Documento_Partidas part = null;
    private static final String sp_switch_consultaDc = "CONSULTADOCUMENTO";

    public Impresion_Etiquetas()
    {

    }
    public static Impresion_Etiquetas newInstance(String param1, String param2)
    {
        Impresion_Etiquetas fragment = new Impresion_Etiquetas();
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();


        switch (id)
            {
                case R.id.action_qr_scan:
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.fl_ContenedorFragments, frgmt_Consulta_Camera_view.newInstance(frgtag_ConsultaDocumentos,null), frgtag_ConsultaCamaraView)
                            .commit();
                    break;
            }
        return super.onOptionsItemSelected(item);
    }
    public void onQRCodeScanResponse(String Codigo)
    {
        //Toast.makeText(getContext(), Codigo, Toast.LENGTH_LONG).show();
        SegundoPlano sp = new SegundoPlano(sp_switch_consultaDc);
        sp.execute(Codigo);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_consulta__documentos, container, false);

        View contenedor = (View) container.getParent();
        appBarLayout = (AppBarLayout) contenedor.findViewById(R.id.appBar);
        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_consulta_doc_op));

        tabLayout = new TabLayout(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                tabLayout.setTabTextColors(getActivity().getColor(R.color.grisAXC), getActivity().getColor(R.color.blancoLetraStd));
            }
        appBarLayout.addView(tabLayout);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);

        det = frgmnt_Consulta_Documento_Det.newInstance("ConsultaEmpaque", null);
        part = frgmnt_Consulta_Documento_Partidas.newInstance("ConsultaEmpaque", null);
        pal = frgmnt_Consulta_Documento_Pallets.newInstance("ConsultaEmpaque", null);
        mListener.ActivarConsultaPalletBotonMasInfo(frgtag_ConsultaDocumentos, true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.search_toolbar, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //Toast.makeText(getContext(), "SUBMIT", Toast.LENGTH_LONG).show();
                SegundoPlano sp = new SegundoPlano(sp_switch_consultaDc);
                sp.execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //Toast.makeText(getContext(), "TEXT CHANGED", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        appBarLayout.removeView(tabLayout);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        String[] titulos = {"Documento","Partidas","Pallets"};
        public ViewPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position)
        {
            switch (position)
                {
                    case 0:
                        if(det!=null)
                            {
                                return det;
                            }
                    case 1:
                        if(part!=null)
                            {
                                return part;
                            }

                    case 2:
                        if(pal!=null)
                            {
                                return pal;
                            }
                }
            return null;
        }

        @Override
        public int getCount()
        {
            return titulos.length;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            return titulos[position];
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
        mListener.ActivarConsultaPalletBotonMasInfo(frgtag_ConsultaDocumentos, false);
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
        boolean ActivarConsultaPalletBotonMasInfo(String frgtag, boolean Dec);

        public String ReturnData(String[] data);

    }
    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(getContext());
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
                                    case sp_switch_consultaDc:

                                            dao = ca.cad_ConsultaOrdenProd(params[0]);

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
                                {
                                    case sp_switch_consultaDc:

                                      String TituloTarjetas = dao.getcTablas().get(0).get(0).getTitulo() + " - " + dao.getcTablas().get(0).get(0).getDato();
                                      String DetalleTarjetas =dao.getcTablas().get(0).get(1).getDato() + " - " + dao.getcTablas().get(0).get(2).getDato();

                                      det.NewInfo(dao.getcTablas().get(0),TituloTarjetas,DetalleTarjetas);

                                      ArrayList<ArrayList<Constructor_Dato>> newData_Partida = new ArrayList<>();
                                      ArrayList<ArrayList<Constructor_Dato>> newData_Pallets= new ArrayList<>();

                                       for(ArrayList<Constructor_Dato> a: dao.getcTablas())
                                           {
                                               if(a.get(0).getTitulo().equals("Partidas"))
                                                   {
                                                       newData_Partida.add(a);
                                                   }
                                               if(a.get(1).getTitulo().equals("CodigoPallet"))
                                                   {
                                                       newData_Pallets.add(a);
                                                   }
                                           }
                                       part.NewInfo(newData_Partida,TituloTarjetas,DetalleTarjetas);
                                       pal.NewInfo_OP(newData_Pallets,TituloTarjetas,DetalleTarjetas,"");

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
