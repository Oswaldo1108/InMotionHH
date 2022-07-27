package com.automatica.AXCMP.ImpresionEtiquetas;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.fragment.app.Fragment;

public class frgmnt_Imprime_Etiquetas extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta;
    private static final String frgtag_ImpEtiqueta= "FRGIMP";


    private OnFragmentInteractionListener mListener;


    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";

    //VIEWS
    private EditText edtx_CantidadEmpaques,edtx_CantidadxEmpaque;
    private TextView txtv_Titulo,txtv_Detalle,txtv_Producto,txtv_DescProd;
    private ImageButton btn_Back;
    private Button btn_ImprimirEmpaques;
    private Spinner spnr_Impresoras;

    private Button btn_ImprimirEtiquetas;


    RadioGroup rdgrp_Modulos;
    public frgmnt_Imprime_Etiquetas()
    {

    }

    public static Fragment newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Imprime_Etiquetas fragment = new frgmnt_Imprime_Etiquetas();
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
        View view = inflater.inflate(R.layout.fragment_detalle_pallet2, container, false);
        DeclaraVariables(view);



        new SegundoPlano("ListaImpresoras").execute();

        return view;
    }

    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle= view.findViewById(R.id.txtv_Incidencia);

        txtv_Titulo.setText("Etiquetas de empaque");
        txtv_Detalle.setText("MiAXC - Impresión");

        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        btn_Back.setVisibility(View.VISIBLE);

        edtx_CantidadEmpaques = view.findViewById(R.id.edtx_CantidadEmpaques);
        rdgrp_Modulos = view.findViewById(R.id.radioGroup);

//        Log.i("Mamarre", paramArr[0]);
//        edtx_CantidadEmpaques.setText(paramArr[0]);

        btn_ImprimirEtiquetas = view.findViewById(R.id.btn_ImprimirEtiquetas);

        spnr_Impresoras = view.findViewById(R.id.spinner);


        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        edtx_CantidadEmpaques.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {



                        if(!edtx_CantidadEmpaques.getText().toString().equals(""))
                            {
                                switch(rdgrp_Modulos.getCheckedRadioButtonId())
                                    {
                                        case R.id.rdb_MP:
                                            new SegundoPlano("ImprimeEtiquetaMP").execute();
                                            break;
                                        case R.id.rdb_PT:
                                            new SegundoPlano("ImprimeEtiquetaPT").execute();
                                            break;
                                        default:
                                            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Modulo seleccionado no valido.", false, true, true);
                                            break;
                                    }
                            }
                    }
                return false;
            }
        });

            btn_ImprimirEtiquetas.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if(!edtx_CantidadEmpaques.getText().toString().equals(""))

                        {

                            switch(rdgrp_Modulos.getCheckedRadioButtonId())
                                {
                                    case R.id.rdb_MP:
                                            new SegundoPlano("ImprimeEtiquetaMP").execute();
                                        break;
                                    case R.id.rdb_PT:
                                            new SegundoPlano("ImprimeEtiquetaPT").execute();
                                        break;
                                    default:
                                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Modulo seleccionado no valido.", false, true, true);
                                        break;
                                }




                        }
                }
            });

        edtx_CantidadEmpaques.requestFocus();
        Log.i("HOLA", "OnATach");
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
        boolean ActivaProgressBar(Boolean estado);
    }

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(getContext());
        DataAccessObject dao;
        String tarea;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
            //    mListener.ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    if(!this.isCancelled())
                        {
                            switch (tarea)
                                {

                                    case "ListaImpresoras":
                                        dao = ca.cad_ListaImpresorasMiAXC();
                                        break;

                                    case "ImprimeEtiquetaMP":
                                        dao = ca.C_ImprimeEtiquetasMP(edtx_CantidadEmpaques.getText().toString(),spnr_Impresoras.getSelectedItem().toString());
                                        break;

                                    case "ImprimeEtiquetaPT":
                                        dao = ca.C_ImprimeEtiquetasPT("",edtx_CantidadEmpaques.getText().toString(),spnr_Impresoras.getSelectedItem().toString());
                                        break;

                                     default:
                                         dao = new DataAccessObject(false,"Operacion no soportada.",null);
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
                            switch (tarea)
                                {
                                    case "ListaImpresoras":
                                          spnr_Impresoras.setAdapter(new CustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item , dao.getcTablasSorteadas("Impresora","Impresora")));
                                        break;

                                    case "ImprimeEtiquetaMP":
                                    case "ImprimeEtiquetaPT":
                                           new popUpGenerico(getContext(),null, "Etiquetas impresas con éxito." ,true,true , true);

                                          getActivity().getSupportFragmentManager().popBackStackImmediate();
                                        break;

                                    default:
                                           new popUpGenerico(getContext(),null, "Actividad no soportada. " + tarea,false ,true , true);
                                        break;
                                }
                        }else
                        {
                            new popUpGenerico(getContext(), null, dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                    mListener.ActivaProgressBar(false);

                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(),dao.iscEstado(), true, true);
                }

            //swipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }





}
