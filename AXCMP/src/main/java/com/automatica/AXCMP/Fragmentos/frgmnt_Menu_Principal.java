package com.automatica.AXCMP.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.automatica.AXCMP.R;

import androidx.fragment.app.Fragment;

public class frgmnt_Menu_Principal extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta;


    private OnFragmentInteractionListener mListener;


    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";

    //VIEWS
    private ImageButton btn_Back;

    private EditText edtx_Contenedor;
    private Button btn_ConfirmarContenedor;

    public frgmnt_Menu_Principal()
    {
    }
    public static frgmnt_Menu_Principal newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Menu_Principal fragment = new frgmnt_Menu_Principal();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,param);
        args.putString(ARG_PARAM2,TipoConsulta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                paramArr= getArguments().getStringArray(ARG_PARAM1);
                str_TipoConsulta= getArguments().getString(ARG_PARAM2);
                Log.i("ARGUMENTOSPALLETDET", "HOLA "+str_TipoConsulta);
            }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_menu_principal, container, false);

        DeclaraVariables(view);
        return view;
    }


    private void DeclaraVariables(View view)
    {

//        ((TextView)view.findViewById(R.id.txtv_Incidencia)).setText(paramArr[1]);
  //      ((TextView)view.findViewById(R.id.txtv_Titulo)).setText(getString(R.string.subtitulo_registro_contenedor));


        view.findViewById(R.id.btn_Registrar).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left,R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .add(R.id.constraintLayout7, frgmnt_Ej_Consulta.newInstance(null, null), "frgtag_MenuPrin2").addToBackStack("frgtag_MenuPrin2")
                        .commit();
            }
        });
        view.findViewById(R.id.btn_Registrar2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left,R.anim.enter_left_to_right, R.anim.exit_right_to_left)

                        .add(R.id.constraintLayout7, frgmnt_Ej_Consulta.newInstance(null, null), "frgtag_MenuPrin2").addToBackStack("frgtag_MenuPrin2")
                        .commit();
            }
        });
        view.findViewById(R.id.btn_Registrar3).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left,R.anim.enter_left_to_right, R.anim.exit_right_to_left)

                        .add(R.id.constraintLayout7, frgmnt_Ej_Consulta.newInstance(null, null), "frgtag_MenuPrin2").addToBackStack("frgtag_MenuPrin2")
                        .commit();
            }
        });
        view.findViewById(R.id.btn_Registrar4).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                        .add(R.id.constraintLayout7, frgmnt_Ej_Consulta.newInstance(null, null), "frgtag_MenuPrin2").addToBackStack("frgtag_MenuPrin2")
                        .commit();
            }
        });










        view.findViewById(R.id.btn_Registrar5).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finishFragment();
            }
        });
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
        boolean ActivaProgressBar(boolean estado);

    }


    private void finishFragment()
    {
//        getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Menu_Principal.this).commit();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }


}
