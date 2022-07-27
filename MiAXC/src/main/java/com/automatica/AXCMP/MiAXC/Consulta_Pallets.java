package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;
import com.automatica.AXCMP.Constructor_Dato;

import java.util.ArrayList;


public class Consulta_Pallets extends Fragment
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

    //
    private static final String frgtag_ConsultaDocumentos= "FRGCD";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    private static final String frgtag_ConsultaDocDet= "FRGCDD";
    private static final String frgtag_ConsultaDocPart = "FRGCDP";
    private static final String frgtag_ConsultaDocPal= "FRGCDPL";
    private static final String frgtag_ConsultaPalletsDET= "FRGCPLTDET";


    private DataAccessObject dao;

    frgmnt_Consulta_Documento_Pallets DetPT = null,DetMP = null;
    private static final String sp_switch_consultaDc = "CONSULTADOCUMENTO";

    public Consulta_Pallets()
    {

    }
    public static Consulta_Pallets newInstance(String param1, String param2)
    {
        Consulta_Pallets fragment = new Consulta_Pallets();
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
                            .add(R.id.fl_ContenedorFragments, frgmt_Consulta_Camera_view.newInstance(frgtag_ConsultaPalletsDET,null), frgtag_ConsultaCamaraView)
                            .commit();
                    break;
            }
        return super.onOptionsItemSelected(item);
    }
    public void onQRCodeScanResponse(String Codigo)
    {
        //Toast.makeText(getContext(), Codigo, Toast.LENGTH_LONG).show();
        SegundoPlano sp = new SegundoPlano(mParam1);
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_consulta_ubicacion));

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

        DetMP = frgmnt_Consulta_Documento_Pallets.newInstance("ConsultaEmpaque", null);
        DetPT = frgmnt_Consulta_Documento_Pallets.newInstance("ConsultaEmpaque", null);

       // DetPalletMP = frgmnt_Consulta_Pallet_Det.newInstance("", "ConsultaPallet");
        mListener.ActivarConsultaPalletBotonMasInfo(frgtag_ConsultaPalletsDET, true);
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
                SegundoPlano sp = new SegundoPlano(mParam1);
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
        mListener.ActivarConsultaPalletBotonMasInfo(frgtag_ConsultaPalletsDET, false);
        super.onDestroyView();
        appBarLayout.removeView(tabLayout);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        String[] titulos = {"Coincidencias MP","Coincidencias PT"};
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
                        if(DetMP !=null)
                            {
                                return DetMP;
                            }

                    case 1:
                        if(DetPT !=null)
                            {
                                return DetPT;
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
        mListener.ActivarConsultaPalletBotonMasInfo(frgtag_ConsultaPalletsDET, false);
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
        boolean ActivarConsultaPalletBotonMasInfo(String frgtag,boolean Dec);
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
                                    case "ConsultaPallet":
                                        if(mParam2.equals("POSICION"))
                                            {
                                                dao = ca.cad_ConsultaPosicion(params[0]);

                                            }else
                                            {
                                                dao = ca.cad_ConsultaPallet(params[0]);

                                            }
                                        break;
                                    case "ConsultaEmpaque":
                                        dao = ca.cad_ConsultaEmpaque(params[0]);
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
                                    case "ConsultaEmpaque":

                                    case "ConsultaPallet":
                                    case sp_switch_consultaDc:

                                      ArrayList<ArrayList<Constructor_Dato>> newData_PalletsMP = new ArrayList<>();
                                      ArrayList<ArrayList<Constructor_Dato>> newData_PalletsPT= new ArrayList<>();

                                       for(ArrayList<Constructor_Dato> a: dao.getcTablas())
                                           {
                                               if(!(a.size()<=0))
                                                   {
                                                       if (a.get(2).getTitulo().equals("IdPalletMP"))
                                                           {
                                                               newData_PalletsMP.add(a);
                                                           }
                                                       if (a.get(2).getTitulo().equals("IdPalletPT"))
                                                           {
                                                               newData_PalletsPT.add(a);
                                                           }
                                                   }
                                           }
                                       DetMP.NewInfo(newData_PalletsMP,mParam1,"MP");
                                       DetPT.NewInfo(newData_PalletsPT,mParam1,"PT");

                                       tabLayout.getTabAt(0).setText("MP (" + newData_PalletsMP.size() + ")");
                                       tabLayout.getTabAt(1).setText("PT (" + newData_PalletsPT.size() + ")");

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

//
//    public void myUpdatePageTitle(int pagePosition, int numItems) {
//        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab);
//        TabLayout.Tab tab = tabLayout.getTabAt(pagePosition);
//        if (tab != null) {
//            tab.setText(myCalcTabTitle(pagePosition, numItems));
//        }
//    }



}


