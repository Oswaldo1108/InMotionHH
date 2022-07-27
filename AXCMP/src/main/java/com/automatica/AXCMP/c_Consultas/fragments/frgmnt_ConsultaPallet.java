package com.automatica.AXCMP.c_Consultas.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.adaptadorTabla;
import com.automatica.AXCMP.constructorTablaEntradaAlmacen;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class frgmnt_ConsultaPallet extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //PRIMITIVAS
    ArrayList<constructorTablaEntradaAlmacen> lista = null;
    boolean recarga;


    //VISTAS
    private EditText edtx_CodigoPallet;
    private ListView lv_TablaEmpaque;
    private TextView txtv_Titulo;

    //OBJETOS
    private SoapAction sa = new SoapAction();
    private    adaptadorTabla adaptador = null;


    public frgmnt_ConsultaPallet()
    {
        // Required empty public constructor
    }


    public static frgmnt_ConsultaPallet newInstance(String param1, String param2)
    {
        frgmnt_ConsultaPallet fragment = new frgmnt_ConsultaPallet();
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
        View view = null;
        try
            {


                // Inflate the layout for this fragment
             view   = inflater.inflate(R.layout.fragment_frgmnt__consulta_empaque, container, false);
            declararVariables(view);
            agregarListeners(view);

            }catch (Exception e)
            {
                e.printStackTrace();

            }
        return view;
    }


    private void declararVariables(View view)
    {
        try
            {
                edtx_CodigoPallet = (EditText) view.findViewById(R.id.edtx_CodigoEmpaque);
                edtx_CodigoPallet.requestFocus();
                edtx_CodigoPallet.setHint(getString(R.string.escanear_Pallet));
                txtv_Titulo = (TextView) view.findViewById(R.id.txtv_empaque);
                txtv_Titulo.setText("Consulta de miaxc_consulta_pallet");
                lv_TablaEmpaque = (ListView) view.findViewById(R.id.lstv_Empaque);
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);


            }
    }




    private void agregarListeners(View view)
    {
        try
            {
                edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                            {
                                try {

                                    if (edtx_CodigoPallet.getText().toString().equals(""))
                                        {

                                            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                                            edtx_CodigoPallet.requestFocus();
                                            return true;
                                        }
                                    SegundoPlano tarea = new SegundoPlano();
                                    tarea.execute();

                                    new esconderTeclado(getActivity());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);

                                }
                                return true;
                            }


                        return false;
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {


        String mensaje, estado;

        View LastView;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
//            recarga=true;
//            inAnimation = new AlphaAnimation(0f, 1f);
//            inAnimation.setDuration(200);
//            progressBarHolder.setAnimation(inAnimation);
//
//            progressBarHolder.setVisibility(View.VISIBLE);
//            LastView = getCurrentFocus();
//            progressBarHolder.requestFocus();

        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {

                sa.SOAPConsultaPalletReg(edtx_CodigoPallet.getText().toString(), getContext());

                mensaje = sa.getMensaje();
                estado = sa.getDecision();

                DataAccessObject dao = sa.getDao();

                if (estado.equals("true"))
                    {
                        lista = new ArrayList<>();
                        for(int i = 0; i<= dao.getcEncabezado().length-1;i++)
                            {
                                lista.add(new constructorTablaEntradaAlmacen(dao.getcEncabezado()[i], dao.getcTabla()[0][i]));
                            }
                    }

            } catch (Exception e)
                {
                    mensaje = e.getMessage();
                    estado = "false";
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try {
                recarga=false;
                //       LastView.requestFocus();
                if (estado.equals("true"))
                    {

                        adaptador = new adaptadorTabla(getActivity(), R.layout.list_item_2_datos_fragment, lista);
                        lv_TablaEmpaque.setAdapter(adaptador);

                    } else if (estado.equals("false"))
                    {

                        // reiniciaCampos();
                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), mensaje, "false", true, true);
                    }

                edtx_CodigoPallet.setText("");
                edtx_CodigoPallet.requestFocus();
            } catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);
                }
//            outAnimation = new AlphaAnimation(1f, 0f);
//            outAnimation.setDuration(200);
//            progressBarHolder.setAnimation(outAnimation);
//            progressBarHolder.setVisibility(View.GONE);
        }
    }




}
