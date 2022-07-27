package com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking;

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
import android.widget.TextView;


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


public class frgmnt_ControlSurtidos extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String Documento;
    private String Producto;


    private TextView txtv_Doc,txtv_Producto,txtv_CantidadActual,txtv_Minimo,txtv_Maximo,txtv_PalletSug,txtv_Lote,txtv_Posicion;

    //OBJETOS
    private OnFragmentInteractionListener mListener;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;


    //VIEWS
    private ViewPager viewPager;
    Toolbar actionBarToolBar;

    //
   // private static final String frgtag_ConsultaDocumentos= "FRGCD";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";

    private static final String frgtag_Almacenes = "frgtag_Almacenes";
    private static final String frgtag_Articulos = "frgtag_Articulos";


    private DataAccessObject dao;


//    frgmnt_Consulta_Documento_Det det = null;




    frgmnt_Reab_Empaques emp = null;
    frgmnt_Reab_Pallet pal = null;
    frgmnt_Reab_Piezas pz = null;

    private static final String sp_switch_consultaDc = "CONSULTADOCUMENTO";
    private static final String sp_switch_consultaSugerencia = "CONSULTADOSUGERENCIA";

    public frgmnt_ControlSurtidos()
    {

    }
    public static frgmnt_ControlSurtidos newInstance(String param1, String param2)
    {
        frgmnt_ControlSurtidos fragment = new frgmnt_ControlSurtidos();
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
                Documento = getArguments().getString(ARG_PARAM1);
                Producto = getArguments().getString(ARG_PARAM2);
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.reabastece_frgmnt_surtido, container, false);

        View contenedor = (View) container.getParent();
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appBar);
        setHasOptionsMenu(false);
        appBarLayout.setLiftable(false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_consulta_doc_OC));


        tabLayout = new TabLayout(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                tabLayout.setTabTextColors(getActivity().getColor(R.color.ControlStd), getActivity().getColor(R.color.blancoLetraStd));
            }
        appBarLayout.addView(tabLayout);


        viewPager = (ViewPager) view.findViewById(R.id.pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.setOffscreenPageLimit(2);


        txtv_Doc                =(TextView) view.findViewById(R.id.txtv_Pedido);
        txtv_Producto           =(TextView) view.findViewById(R.id.txtv_Producto);
        txtv_CantidadActual     =(TextView) view.findViewById(R.id.txtv_Cantidad);
        txtv_Minimo             =(TextView) view.findViewById(R.id.txtv_CantidadReg);
        txtv_Maximo             =(TextView) view.findViewById(R.id.txtv_CantidadReg2);
        txtv_PalletSug          =(TextView) view.findViewById(R.id.txtv_Pallet);
        txtv_Lote               =(TextView) view.findViewById(R.id.txtv_Lote);
        txtv_Posicion           =(TextView) view.findViewById(R.id.txtv_Posicion);







        emp = frgmnt_Reab_Empaques.newInstance(Documento, null);


        pal = frgmnt_Reab_Pallet.newInstance(Documento, null);

        pz = frgmnt_Reab_Piezas.newInstance(Documento, null);


        new SegundoPlano(sp_switch_consultaDc).execute(Documento, Producto);



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
//        inflater.inflate(R.menu.search_toolbar, menu);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//            @Override
//            public boolean onQueryTextSubmit(String query)
//            {
//              //  Toast.makeText(getContext(), "SUBMIT", Toast.LENGTH_LONG).show();
////                SegundoPlano sp = new SegundoPlano(sp_switch_consultaDc);
////                sp.execute(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText)
//            {
//                //Toast.makeText(getContext(), "TEXT CHANGED", Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
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
        String[] titulos = {"Pallet","Empaque","Piezas"};
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
                        if(pal!=null)
                            {
                                return pal;
                            }
                    case 1:
                        if(emp!=null)
                            {
                                return emp;
                            }
                    case 2:
                        if(pz!=null)
                            {
                                return pz;
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
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), false, true, true);
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
                                        dao = ca.C_ConsultaOrdenesReabastecimientoDet(params[0]);
                                        break;

                                    case sp_switch_consultaSugerencia:
                                        dao = ca.c_ConsultaUbicacionPK(params[0],params[1]);
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
//
                                        txtv_Doc.setText(Documento);
                                        txtv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("prod."));
                                        txtv_CantidadActual.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_Minimo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadMinima"));
                                        txtv_Maximo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CapacidadMaxima"));
//

                                        new SegundoPlano(sp_switch_consultaSugerencia).execute(dao.getSoapObject_parced().getPrimitivePropertyAsString("Ubicacion")
                                                                                     ,dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        break;
                                    case sp_switch_consultaSugerencia:


                                        txtv_PalletSug.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                                        txtv_Posicion.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Posicion"));


                                        break;
                                }
                        }else
                        {
                            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), dao.getcMensaje(), false, true, true);
                        }

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(),false, true, true);
                }

            //swipeRefreshLayout.setRefreshing(false);
            mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", false, true, true);
            super.onCancelled();
        }
    }



    public String[] getDocumentData()
    {
        return emp.getDatos();
    }



    public void  ActualizarDocumento()
    {        new SegundoPlano(sp_switch_consultaDc).execute(Documento, Producto);
    }

//    public void setDocumentoArticulos(String prmDocumento)
//    {
//        pal.setDocumento(prmDocumento);
//    }
}
