package com.automatica.AXCPT.ReabastecimientoPicking.SinOrden;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;


public class frgmnt_ControlSurtidos_SO extends Fragment
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

   // private static final String frgtag_ConsultaDocumentos= "FRGCD";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";

    private static final String frgtag_Almacenes = "frgtag_Almacenes";
    private static final String frgtag_Articulos = "frgtag_Articulos";

    private DataAccessObject dao;

//    frgmnt_Consulta_Documento_Det det = null;

    frgmnt_Reab_Empaques_SO emp = null;
    frgmnt_Reab_Pallet_SO pal = null;
    frgmnt_Reab_Piezas_SO pz = null;

    private static final String sp_switch_consultaDc = "CONSULTADOCUMENTO";
    private static final String sp_switch_consultaSugerencia = "CONSULTADOSUGERENCIA";

    public frgmnt_ControlSurtidos_SO()
    {

    }
    public static frgmnt_ControlSurtidos_SO newInstance(String param1, String param2)
    {
        frgmnt_ControlSurtidos_SO fragment = new frgmnt_ControlSurtidos_SO();
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
        View view = inflater.inflate(R.layout.reabastece_frgmnt_surtido_so, container, false);

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

        emp = frgmnt_Reab_Empaques_SO.newInstance("ConsultaEmpaque", null);
        pal = frgmnt_Reab_Pallet_SO.newInstance("ConsultaEmpaque", null);
        pz = frgmnt_Reab_Piezas_SO.newInstance("ConsultaEmpaque", null);

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

    public String[] getDocumentData()
    {
        return emp.getDatos();
    }

    public String[] getArticuloData()
    {
        return pal.getDatos();
    }

    public void setDocumentoArticulos(String prmDocumento)
    {
        pal.setDocumento(prmDocumento);
    }
}
