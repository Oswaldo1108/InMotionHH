package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
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
import com.automatica.AXCMP.Servicios.popUpGenerico;

import java.util.Arrays;
import java.util.List;

public class frgmnt_Registra_Configuracion extends Fragment
{

    List<String> valid = Arrays.asList("Maulec2020", "52637825A2","Coflex2021");

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_Config= "FRGCONF";

    //VIEWS

    private ImageButton btn_Back;
    private TextView txtv_Titulo,txtv_Detalle, txtv_TipoDetalle;
    private EditText edtx_DireccionIP,edtx_Estacion,edtx_CodigoEmpresa;
    private Button btn_Registrar;
    public frgmnt_Registra_Configuracion()
    {
    }
    public static frgmnt_Registra_Configuracion newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Registra_Configuracion fragment = new frgmnt_Registra_Configuracion();
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
        View view = inflater.inflate(R.layout.fragment_registra_datos, container, false);
        DeclaraVariables(view);

        return view;
    }

    private void DeclaraVariables(View view)
    {
        final  Handler h = new Handler();
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle = view.findViewById(R.id.txtv_Incidencia);
        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        edtx_CodigoEmpresa  = view.findViewById(R.id.edtx_CodigoEmpresa);
        edtx_DireccionIP= view.findViewById(R.id.edtx_DireccionIP);
        edtx_Estacion= view.findViewById(R.id.edtx_Estacion);
        btn_Registrar = view.findViewById(R.id.btn_Registrar);
        txtv_Titulo.setText(getString(R.string.DescIngresarDatos));
        txtv_Detalle.setText(getString(R.string.TitIngresarDatos));
        btn_Back.setVisibility(View.GONE);

//        btn_Back.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Registra_Configuracion.this).commit();
//
//            }
//        });


        edtx_CodigoEmpresa.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_Estacion.requestFocus();
                            }
                        });
                    }
                return false;
            }
        });


        edtx_Estacion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_DireccionIP.requestFocus();
                            }
                        });
                    }
                    return false;
            }
        });


        edtx_DireccionIP.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        h.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                btn_Registrar.requestFocus();
                            }
                        });
                    }
                return false;
            }
        });


        btn_Registrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                    {
                        if(edtx_DireccionIP.getText().toString().equals("")||edtx_CodigoEmpresa.getText().toString().equals("")||edtx_Estacion.getText().toString().equals(""))
                            {

                                new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Por favor, llena todos los campos.", false, true, true);
                                return;
                            }


//
                        if(!valid.contains(edtx_CodigoEmpresa.getText().toString()))
                            {

                                new popUpGenerico(getContext(),edtx_CodigoEmpresa, "Código incorrecto." + valid.contains(edtx_CodigoEmpresa.getText().toString()) , false, true, true);
                                return;
                            }

//                        if(!edtx_CodigoEmpresa.getText().toString().equals("52637825A2"))
//                            {
//
//                                new popUpGenerico(getContext(),edtx_CodigoEmpresa, "Código incorrecto.", false, true, true);
//                                return;
//                            }

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editor.putString("codigoEmpresa", edtx_CodigoEmpresa.getText().toString());
                        editor.putString("estacion", edtx_Estacion.getText().toString());
                        editor.putString("direccionWebService", edtx_DireccionIP.getText().toString());
                        editor.putBoolean("ConfiguracionInicial", true);
                        editor.putBoolean("MostrarConfiguracionPrimerInicio", false);
                        editor.apply();
                        getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Registra_Configuracion.this).commit();

                    }catch (Exception e)
                    {
                        e.printStackTrace();

                    }
            }
        });
    }

    public void onButtonPressed(Uri uri)
    {
//        if (mListener != null)
//            {
//                mListener.onFragmentInteraction(uri);
//            }
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
       // boolean ActivaProgressBar(Boolean estado);
    }


}
