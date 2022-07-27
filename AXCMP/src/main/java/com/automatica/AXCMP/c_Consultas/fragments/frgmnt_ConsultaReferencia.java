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

import androidx.fragment.app.Fragment;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class frgmnt_ConsultaReferencia extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //PRIMITIVAS
    private boolean recarga;



    //VISTAS
    private EditText edtx_CodigoUbicacion;
    private ListView lv_TablaEmpaque;
    private TextView txtv_Consulta;
    private SortableTableView tblv_Posicion;


    //OBJETOS
    private  SoapAction sa = new SoapAction();
    private adaptadorTabla adaptador = null;


    private OnFragmentInteractionListener mListener;

    public frgmnt_ConsultaReferencia()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frgmnt_ConsultaEmpaque.
     */
    // TODO: Rename and change types and number of parameters
    public static frgmnt_ConsultaReferencia newInstance(String param1, String param2)
    {
        frgmnt_ConsultaReferencia fragment = new frgmnt_ConsultaReferencia();
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


    private void declararVariables(View view)
    {
        try
            {
                edtx_CodigoUbicacion= (EditText) view.findViewById(R.id.edtx_CodigoEmpaque);
                edtx_CodigoUbicacion.requestFocus();

                tblv_Posicion = (SortableTableView) view.findViewById(R.id.tblv_);
                txtv_Consulta = (TextView) view.findViewById(R.id.txtv_empaque);
              //  txtv_Consulta.setText("Consulta de posición");

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
                edtx_CodigoUbicacion.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                            {
                                try {

                                    if (edtx_CodigoUbicacion.getText().toString().equals(""))
                                    {

                                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), getString(R.string.error_ingrese_empaque), "false", true, true);
                                        edtx_CodigoUbicacion.requestFocus();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = null;
        try
            {
                view = inflater.inflate(R.layout.fragment_frgmnt__consulta_referencia, container, false);

                declararVariables(view);
                agregarListeners(view);
                return view;

                // Inflate the layout for this fragment
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);
            }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

                sa.SOAPConsultaReferencia(edtx_CodigoUbicacion.getText().toString(), getContext());

                mensaje = sa.getMensaje();
                estado = sa.getDecision();

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

                        DataAccessObject dao = sa.getDao();
                        TableColumnWeightModel t = new TableColumnWeightModel(dao.getcEncabezado().length);
                        t.setColumnWeight(0, 4);
                        t.setColumnWeight(1, 1);
                        tblv_Posicion.setColumnCount(dao.getcEncabezado().length);
                        tblv_Posicion.setColumnModel(t);
                        tblv_Posicion.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(),dao.getcEncabezado()));
                        tblv_Posicion.setDataAdapter(new SimpleTableDataAdapter(getContext(), dao.getcTabla()));
                    } else if (estado.equals("false"))
                    {

                        // reiniciaCampos();
                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), mensaje, "false", true, true);
                    }

                edtx_CodigoUbicacion.setText("");
                edtx_CodigoUbicacion.requestFocus();
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
