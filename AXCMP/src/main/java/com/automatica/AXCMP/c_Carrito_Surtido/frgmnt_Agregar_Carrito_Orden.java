package com.automatica.AXCMP.c_Carrito_Surtido;

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

public class frgmnt_Agregar_Carrito_Orden extends Fragment
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

    public frgmnt_Agregar_Carrito_Orden()
    {
    }
    public static frgmnt_Agregar_Carrito_Orden newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Agregar_Carrito_Orden fragment = new frgmnt_Agregar_Carrito_Orden();
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
        View view = inflater.inflate(R.layout.fragment_agrecar_carrito_orden, container, false);

        DeclaraVariables(view);
        return view;
    }


    private void DeclaraVariables(View view)
    {

//        ((TextView)view.findViewById(R.id.txtv_Incidencia)).setText(paramArr[1]);
  //      ((TextView)view.findViewById(R.id.txtv_Titulo)).setText(getString(R.string.subtitulo_registro_contenedor));

        btn_ConfirmarContenedor = view.findViewById(R.id.btn_Registrar);
        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        edtx_Contenedor = view.findViewById(R.id.edtx_Contenedor);

        ((TextView)view.findViewById(R.id.txtv_Titulo)).setText("");
        ((TextView)view.findViewById(R.id.txtv_Incidencia)).setText("Registro de contenedor");

        btn_Back.setVisibility(View.VISIBLE);
        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finishFragment();
            }
        });
        btn_ConfirmarContenedor.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener.ConfirmaAgregarContenedor(edtx_Contenedor.getText().toString()))
                    {
                        finishFragment();
                    }
            }
        });
        edtx_Contenedor.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                    {
                        if(mListener.ConfirmaAgregarContenedor(edtx_Contenedor.getText().toString()))
                        {
                                finishFragment();
                        }
                    }


                    return false;
            }
        });

        edtx_Contenedor.requestFocus();
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
        boolean ConfirmaAgregarContenedor(String Contenedor);
    }


    private void finishFragment()
    {
        getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Agregar_Carrito_Orden.this).commit();
    }


}
