package com.automatica.AXCMP.c_Carrito_Surtido;

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
import android.widget.Spinner;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.fragment.app.Fragment;

public class frgmnt_Cierre_Contenedor extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta;
    private static final String frgtag_ImpEtiqueta= "FRGIMP";
    private static final String frgtag_CierrePallet= "FRGCP";

    private static final String frgtag_Ordenes_Activas = "FRGOA";

    private OnFragmentInteractionListener mListener;

    private Button btn_ImprimirEtiquetas;

    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";

    //VIEWS
    private EditText edtx_CantidadEmpaques,edtx_CantidadxEmpaque;
    private TextView txtv_Titulo,txtv_Detalle,txtv_Producto,txtv_DescProd;
    private ImageButton btn_Back;
    private Button btn_ImprimirEmpaques;
    private Spinner spnr_Impresoras;


    public frgmnt_Cierre_Contenedor()
    {

    }

    public static Fragment newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Cierre_Contenedor fragment = new frgmnt_Cierre_Contenedor();
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
        View view = inflater.inflate(R.layout.fragment_cierre_pallet, container, false);
        DeclaraVariables(view);
        return view;
    }

    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle= view.findViewById(R.id.txtv_Incidencia);

        txtv_Titulo.setText("Escaneé etiqueta");
        txtv_Detalle.setText("Cierre de contenedor");

   //     txtv_Producto= view.findViewById(R.id.txtv_ProductoDet);
   //     txtv_DescProd = view.findViewById(R.id.txtv_ProductoDet2);
        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        btn_Back.setVisibility(View.VISIBLE);
     //   btn_ImprimirEmpaques = view.findViewById(R.id.btn_ImprimirEtiquetas);

        edtx_CantidadEmpaques = view.findViewById(R.id.edtx_CantidadEmpaques);

        btn_ImprimirEtiquetas = view.findViewById(R.id.btn_ImprimirEtiquetas);

        edtx_CantidadEmpaques.requestFocus();
   //     edtx_CantidadxEmpaque= view.findViewById(R.id.edtx_CantidadPorEmpaque);

   //     spnr_Impresoras = view.findViewById(R.id.spinner);
     //   txtv_Producto.setText(paramArr[0]);

    //    txtv_DescProd.setText(paramArr[1]);

        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment f =  getActivity().getSupportFragmentManager().findFragmentByTag(frgtag_CierrePallet);
                getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();

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
                                new SegundoPlano("CierreContenedor").execute();
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
                        new SegundoPlano("CierreContenedor").execute();
                    }
            }
        });
//        btn_ImprimirEmpaques.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                SegundoPlano sp = new SegundoPlano("ImprimeEtiqueta");
//                sp.execute();
//            }
//        });

//        SegundoPlano sp = new SegundoPlano("ListaImpresoras");
//        sp.execute();
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
                                       // dao = ca.cad_ListaImpresoras();
                                        break;

                                    case "ImprimeEtiqueta":
                                        dao = ca.C_ImprimeEtiquetasMP(edtx_CantidadEmpaques.getText().toString(),"IMPRESORA");
                                        break;


                                    case "CierreContenedor":
                                        dao = ca.C_CierraPalletSurtido(edtx_CantidadEmpaques.getText().toString());

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
                         //  SoapObject so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                            switch (tarea)
                                {


                                    case "ListaImpresoras":

                                        spnr_Impresoras.setAdapter(new CustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item , dao.getcTablasSorteadas("Impresora","Impresora")));
                                        break;

                                    case "ImprimeEtiqueta":
                                        new popUpGenerico(getContext(),null, "Etiquetas impresas con éxito." ,true,true , true);

                                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                                        break;

                                    case "CierreContenedor":
                                        Fragment f =  getActivity().getSupportFragmentManager().findFragmentByTag(frgtag_CierrePallet);
                                        ((frgmnt_Surtiendo_Ordenes)getActivity().getSupportFragmentManager().findFragmentByTag(frgtag_Ordenes_Activas)).RecargarLista();
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();

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
