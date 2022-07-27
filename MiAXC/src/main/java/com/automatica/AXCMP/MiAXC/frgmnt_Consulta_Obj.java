package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.Principal.constructor_Documento;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;
import com.automatica.AXCMP.Constructor_Dato;
import com.automatica.axc_lib.Servicios.esconderTeclado;

import java.util.ArrayList;

public class frgmnt_Consulta_Obj extends Fragment implements Adaptador_RV_MenuPrincipal.onClickRV
{
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR SI SE HARA UNA CONSULTA DE PALLET O TE EMPAQUE
    private static final String ARG_PARAM2 = "param2";


    private String str_Modulo;
    private String str_TipoConsulta;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    private  static final String frgtag_ConsultaPalletDet = "FRGCPDT";
    //VIEWS
    private RecyclerView rv=null,rv_2=null;
    private EditText edtx_Codigo;
    private ImageButton imgb_EscanearCodigo;



    public frgmnt_Consulta_Obj()
    {
        // Required empty public constructor
    }
    public static frgmnt_Consulta_Obj newInstance(String param1, String param2)
    {
        frgmnt_Consulta_Obj fragment = new frgmnt_Consulta_Obj();
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
                str_Modulo = getArguments().getString(ARG_PARAM1);
                str_TipoConsulta = getArguments().getString(ARG_PARAM2);
                Log.i("ARGUmENTOS", str_TipoConsulta + " " + str_Modulo);
            }
    }

    public void onQRCodeScanResponse(String Codigo)
    {
        //Toast.makeText(getContext(), Codigo, Toast.LENGTH_LONG).show();
        edtx_Codigo.setText(Codigo);
        SegundoPlano sp = new SegundoPlano(str_Modulo);
        sp.execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frgmnt__consulta__obj, container, false);
        rv = view.findViewById(R.id.rv_1);
        rv_2 = view.findViewById(R.id.rv_2);


        imgb_EscanearCodigo = view.findViewById(R.id.imgb_EscanearCodigo);
        edtx_Codigo = view.findViewById(R.id.edtx_Consulta);

        edtx_Codigo.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()== KeyEvent.ACTION_DOWN)
                    {
                        SegundoPlano sp = new SegundoPlano(str_Modulo);
                        sp.execute();
                        new esconderTeclado(getActivity());
                    }

                return false;
            }
        });

        imgb_EscanearCodigo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fl_ContenedorFragments, frgmt_Consulta_Camera_view.newInstance(frgtag_ConsultaPallet,null), frgtag_ConsultaCamaraView)
                        .commit();
            }
        });
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        LinearLayoutManager lm2 = new LinearLayoutManager(getContext());

        rv.setLayoutManager(lm);
        rv_2.setLayoutManager(lm2);

//        SegundoPlano sp = new SegundoPlano("ConsultaDocumentos");
//        sp.execute();
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

    @Override
    public void clickBotonMasInfo(String[] datos)
    {

 //       Log.i("TIPOCONSULTa",str_TipoConsulta);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fl_ContenedorFragments, frgmnt_Consulta_Pallet_Det.newInstance(datos, str_Modulo), frgtag_ConsultaPalletDet)
                .commit();
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
        View view;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                mListener.ActivaProgressBar(true);

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
                                    case "ConsultaPallet":
                                        if(str_TipoConsulta.equals("POSICION"))
                                            {
                                                dao = ca.cad_ConsultaPosicion(edtx_Codigo.getText().toString());

                                            }else
                                            {
                                                dao = ca.cad_ConsultaPallet(edtx_Codigo.getText().toString());

                                            }
                                        break;
                                    case "ConsultaEmpaque":
                                        dao = ca.cad_ConsultaEmpaque(edtx_Codigo.getText().toString());
                                        break;
                                }

                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    dao.setcMensaje(e.getMessage());
                    dao.setcEstado(false);
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
                            ArrayList<constructor_Documento> ArrayDocumentosMP = new ArrayList<>();
                            ArrayList<constructor_Documento> ArrayDocumentosPT = new ArrayList<>();
                            Adaptador_RV_MenuPrincipal adv_MP = null;
                            Adaptador_RV_MenuPrincipal adv_PT = null;
                            switch (tarea)
                                {
                                    case "ConsultaPallet":




                                                for (ArrayList<Constructor_Dato> a : dao.getcTablas())
                                                    {
                                                        //a.remove(0);
                                                        if(!(a.size()<=0))
                                                            {
                                                                if (a.get(0).getTitulo().equals("IdPalletMP"))
                                                                    {
                                                                        ArrayDocumentosMP.add(new constructor_Documento(a.get(1).getDato(), a.get(2).getDato(), null).setTagDocumento("MP"));
                                                                    }

                                                                if (a.get(0).getTitulo().equals("IdPalletPT"))
                                                                    {
                                                                        ArrayDocumentosPT.add(new constructor_Documento(a.get(1).getDato(), a.get(2).getDato(), null).setTagDocumento("PT"));
                                                                    }
                                                            }
                                                        else
                                                            {
                                                                Log.e("frgmnt_Consulta_Obj", "SE REGRESO UNA ENTRADA NULA AL SERIALIZAR LAS COINCIDENCIAS DE MP Y PT.");
                                                            }


                                                    }

                                        adv_MP = new Adaptador_RV_MenuPrincipal(ArrayDocumentosMP);
                                        adv_PT = new Adaptador_RV_MenuPrincipal(ArrayDocumentosPT);

                                        rv.setHasFixedSize(false);
                                        rv.setItemViewCacheSize(10);
                                        rv.setAdapter(adv_MP);

                                        rv_2.setHasFixedSize(false);
                                        rv_2.setItemViewCacheSize(10);
                                        rv_2.setAdapter(adv_PT);

                                        break;



                                    case "ConsultaEmpaque":

                                        for (ArrayList<Constructor_Dato> a : dao.getcTablas())
                                            {
                                                //a.remove(0);
                                                if(!(a.size()<=0))
                                                    {
                                                        if (a.get(0).getTitulo().equals("IdPalletMP"))
                                                            {
                                                                ArrayDocumentosMP.add(new constructor_Documento(a.get(1).getDato(), a.get(2).getDato(), null).setTagDocumento("MP"));
                                                            }

                                                        if (a.get(0).getTitulo().equals("IdPalletPT"))
                                                            {
                                                                ArrayDocumentosPT.add(new constructor_Documento(a.get(1).getDato(), a.get(2).getDato(), null).setTagDocumento("PT"));
                                                            }
                                                    }
                                                else
                                                    {
                                                        Log.e("frgmnt_Consulta_Obj", "SE REGRESO UNA ENTRADA NULA AL SERIALIZAR LAS COINCIDENCIAS DE MP Y PT.");
                                                    }


                                            }

                                        adv_MP = new Adaptador_RV_MenuPrincipal(ArrayDocumentosMP);
                                        adv_PT = new Adaptador_RV_MenuPrincipal(ArrayDocumentosPT);

                                        rv.setHasFixedSize(false);
                                        rv.setItemViewCacheSize(10);
                                        rv.setAdapter(adv_MP);

                                        rv_2.setHasFixedSize(false);
                                        rv_2.setItemViewCacheSize(10);
                                        rv_2.setAdapter(adv_PT);

                                        break;



                                }
                        }else
                        {
                            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }

                    edtx_Codigo.setText("");
                    edtx_Codigo.requestFocus();
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }

            //swipeRefreshLayout.setRefreshing(false);
            mListener.ActivaProgressBar(false);
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
