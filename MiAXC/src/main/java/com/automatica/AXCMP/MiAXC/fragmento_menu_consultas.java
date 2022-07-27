package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.automatica.AXCMP.R;


public class fragmento_menu_consultas extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaDocumentos= "FRGCD";
    private static final String frgtag_ConsultaDocumentosOC= "FRGCDOC";
    private static final String frgtag_ConsultaDocumentosTras= "FRGCDTRAS";
    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaPalletsDET= "FRGCPLTDET";
    private static final String frgtag_ConsultaEmpaquePalletDET= "FRGCEPDET";
    private static final String frgtag_ConsultaPalletsSinColocar = "FRGCPSC";
    private static final String frgtag_ConsultaExistenciasDet = "FRGCEDT";


    //Views
    private CardView cv_Empaque,cv_Pallet,cv_Posicion,cv_OC,cv_OP,cv_Tras,cv_PalletsSC,cv_Existencia;

    public fragmento_menu_consultas()
    {
    }

    public static fragmento_menu_consultas newInstance(String param1, String param2)
    {
        fragmento_menu_consultas fragment = new fragmento_menu_consultas();
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
        View view = inflater.inflate(R.layout.fragmento_menu_consultas, container, false);
        cv_Empaque = view.findViewById(R.id.cv_empaque);
        cv_Pallet = view.findViewById(R.id.cv_Pallet);
        cv_Posicion = view.findViewById(R.id.cv_Posicion);
        cv_OC= view.findViewById(R.id.cv_OC);
        cv_OP= view.findViewById(R.id.cv_OP);
        cv_Tras = view.findViewById(R.id.cv_Traspaso);

        cv_PalletsSC= view.findViewById(R.id.cv_PalletsSC);
        cv_Existencia = view.findViewById(R.id.cv_Existencia);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.miaxc_menu_consultas));


        cv_Empaque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Empaque_Pallet_Det.newInstance("ConsultaEmpaque", "ConsultaEmpaque"),frgtag_ConsultaEmpaquePalletDET)
                        .commit();
            }
        });

        cv_Pallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Empaque_Pallet_Det.newInstance("ConsultaPallet", "ConsultaPallet"),frgtag_ConsultaEmpaquePalletDET)
                        .commit();
            }
        });

        cv_Posicion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Toast.makeText(getContext(), "POSICIon", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Pallets.newInstance("ConsultaPallet", "POSICION"),frgtag_ConsultaPalletsDET)
                        .commit();
            }
        });
        cv_OC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Documentos_OC.newInstance("ConsultaPallet", null),frgtag_ConsultaDocumentosOC)
                        .commit();
            }
        });
        cv_OP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Documentos.newInstance("ConsultaPallet", null),frgtag_ConsultaDocumentos)
                        .commit();
            }
        });
        cv_Tras.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Documentos_Tras.newInstance("ConsultaPallet", "ConsultaPallet"),frgtag_ConsultaDocumentosTras)
                        .commit();
            }
        });

        cv_Existencia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Existencias.newInstance("ConsultaExistencias", "Consulta de existencias"),frgtag_ConsultaExistenciasDet)
                        .commit();
            }
        });


        cv_PalletsSC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, Consulta_Pallets_Sin_Colocar.newInstance("ConsultaPalletSC", "Consulta de pallets sin colocar"),frgtag_ConsultaPalletsSinColocar)
                        .commit();
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
    }
}
