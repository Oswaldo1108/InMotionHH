package com.automatica.AXCPT.Fragmentos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

public class frgmnt_SKU_Conteo extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String[] paramArr = null;
    private String str_CodigoEsperado;

    private OnFragmentInteractionListener mListener;

    private int conteoSku = 0 ;

    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle,txtv_conteo;
    private ImageButton btn_Back,imgb_undo;

    private Button btn_RegistrarCantidad;

    private EditText edtx_Producto;
        private                 Handler h = new Handler();

    public frgmnt_SKU_Conteo()
    {

    }

    public static Fragment newInstance(String[] param, String TipoConsulta,OnFragmentInteractionListener mListener)
    {

        frgmnt_SKU_Conteo fragment = new frgmnt_SKU_Conteo();
        fragment.mListener=mListener;
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,param);
        args.putString(ARG_PARAM2,TipoConsulta);

        fragment.setArguments(args);
        return fragment;
    }


    public static Fragment newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_SKU_Conteo fragment = new frgmnt_SKU_Conteo();
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
                str_CodigoEsperado= getArguments().getString(ARG_PARAM2);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sku_conteo, container, false);
        DeclaraVariables(view);



        return view;
    }

    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle= view.findViewById(R.id.txtv_Incidencia);


        txtv_conteo  = view.findViewById(R.id.txtv_conteo);


       // txtv_Detalle.setText("Conteo de producto");

        txtv_Detalle.setText("SKU: " + str_CodigoEsperado);

        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        btn_Back.setVisibility(View.VISIBLE);


        imgb_undo = view.findViewById(R.id.imgb_undo);

        btn_RegistrarCantidad = view.findViewById(R.id.btn_ElegirLote);

        edtx_Producto = view.findViewById(R.id.edtx_Producto);


        edtx_Producto.requestFocus();

        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        imgb_undo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                conteoSku = 0;

                txtv_conteo.setText(String.valueOf(conteoSku));

                edtx_Producto.setText("");

                edtx_Producto.requestFocus();
                btn_RegistrarCantidad.setEnabled(false);

                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        edtx_Producto.requestFocus();
                    }
                },100);
            }
        });

        edtx_Producto.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if (edtx_Producto.getText().toString().equals("")){
                        new popUpGenerico(getActivity(),edtx_Producto, "Ingrese la cantidad a reempacar", false, true, false);
                        edtx_Producto.requestFocus();
                        return false;
                    }
                    new esconderTeclado(getActivity());
                        txtv_conteo.setText(edtx_Producto.getText().toString());
                        edtx_Producto.setText("");
                        txtv_conteo.setEnabled(false);
                        btn_RegistrarCantidad.setEnabled(true);

                }
                return false;
            }
        });

     /*   edtx_Producto.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
*//*
                if(edtx_Producto.getText().toString().equals(""))
                {
                    new popUpGenerico(getActivity(),edtx_Producto, "Escaneé un código.", false, true, false);
                    return false;
                }

                if(!edtx_Producto.getText().toString().equals(str_CodigoEsperado))
                {
                    new popUpGenerico(getActivity(),edtx_Producto, "Código ingresado es diferente al esperado:[" + str_CodigoEsperado + "] .", false, true, false);
                    return false;
                }

                conteoSku++;*//*

                txtv_conteo.setText(String.valueOf(conteoSku));

                edtx_Producto.setText("");

                edtx_Producto.requestFocus();

                h.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        edtx_Producto.requestFocus();
                    }
                },100);


                new esconderTeclado(getActivity());
                return false;
            }
        });


*/
        btn_RegistrarCantidad.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {


                    new esconderTeclado(getActivity());

                    mListener.RegistrarCantidad(str_CodigoEsperado, txtv_conteo.getText().toString() );

                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                }
            });


    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            {
                mListener = (OnFragmentInteractionListener) context;
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

        boolean ActivaProgressBar(Boolean estado);
        void RegistrarCantidad(String Producto, String strCantidadEscaneada);

    }




}
