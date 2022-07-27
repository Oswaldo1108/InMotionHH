package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.automatica.AXCMP.Servicios.EdtxLongClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
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


public class Consulta_Empaque_Pallet_Det extends Fragment
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
    //private static final String frgtag_ConsultaPalletsDET= "FRGCPLTDET";
    private static final String frgtag_ConsultaEmpaquePalletDET= "FRGCEPDET";


    private DataAccessObject dao;

    frgmnt_Consulta_Documento_Det DetPT = null,DetMP = null;
    private static final String sp_switch_consultaDc = "CONSULTADOCUMENTO";

    public Consulta_Empaque_Pallet_Det()
    {

    }
    public static Consulta_Empaque_Pallet_Det newInstance(String param1, String param2)
    {
        Consulta_Empaque_Pallet_Det fragment = new Consulta_Empaque_Pallet_Det();
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
                            .add(R.id.fl_ContenedorFragments, frgmt_Consulta_Camera_view.newInstance(frgtag_ConsultaEmpaquePalletDET,null), frgtag_ConsultaCamaraView)
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


        if(mParam1.equals("ConsultaEmpaque"))
            {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_consulta_empaque));

            }else
                {

                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_consulta_pallet));

                }



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

        DetMP = frgmnt_Consulta_Documento_Det.newInstance("ConsultaEmpaque", null);
        DetPT = frgmnt_Consulta_Documento_Det.newInstance("ConsultaEmpaque", null);

       // DetPalletMP = frgmnt_Consulta_Pallet_Det.newInstance("", "ConsultaPallet");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.search_toolbar, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();


        EdtxLongClickListener edtxLongClickListener = new EdtxLongClickListener(getActivity(), "Escaneé empaque o pallet.");


        searchView.setOnLongClickListener(edtxLongClickListener);


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
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
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
                                        dao = ca.cad_ConsultaPallet(params[0]);
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
                            Log.i("HOLA", "HOLA ON POST");

                            switch (tarea)
                                {
                                    case "ConsultaEmpaque":
                                    case "ConsultaPallet":
                                    case sp_switch_consultaDc:

                                     //  DetPT.NewInfo(dao.getcTablas().get(1),mParam1,"PT");

                                        if(dao.getcTablas().size()>0)
                                            {
                                                if (dao.getcTablas().get(0).get(0).getDato().equals("MP"))
                                                    {
                                                        DetMP.NewInfo(dao.getcTablas().get(0), mParam1, "[MP] " + dao.getcTablas().get(0).get(1).getDato());
                                                        tabLayout.getTabAt(0).setText("Coincidencias MP (1)");
                                                        DetPT.NewInfo(null, "Sin resultados", "[PT] " );
                                                        tabLayout.getTabAt(1).setText("Coincidencias PT (0)");
                                                        TabLayout.Tab tab = tabLayout.getTabAt(0);
                                                        tab.select();
                                                    }

                                                if (dao.getcTablas().get(0).get(0).getDato().equals("PT"))
                                                    {
                                                        DetPT.NewInfo(dao.getcTablas().get(0), mParam1, "[PT] " + dao.getcTablas().get(0).get(1).getDato());
                                                        tabLayout.getTabAt(1).setText("Coincidencias PT (1)");
                                                        DetMP.NewInfo(null, "Sin resultados", "[MP] " );
                                                        tabLayout.getTabAt(0).setText("Coincidencias MP (0)");
                                                        TabLayout.Tab tab = tabLayout.getTabAt(1);
                                                        tab.select();
                                                    }

                                                try
                                                    {
                                                        if (dao.getcTablas().get(1).get(0).getDato().equals("PT"))
                                                            {
                                                                DetPT.NewInfo(dao.getcTablas().get(1), mParam1, "[PT] " + dao.getcTablas().get(1).get(1).getDato());
                                                                tabLayout.getTabAt(1).setText("Coincidencias PT (1)");
                                                                TabLayout.Tab tab = tabLayout.getTabAt(1);
                                                                tab.select();

                                                            }
                                                    } catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }


                                            }else
                                            {

                                                DetPT.NewInfo(null, "Sin resultados", "[PT] " );
                                                tabLayout.getTabAt(1).setText("Coincidencias PT (0)");

                                                DetMP.NewInfo(null, "Sin resultados", "[MP] " );
                                                tabLayout.getTabAt(0).setText("Coincidencias MP (0)");
                                            }


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


