package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.Principal.constructor_Documento;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Constructor_Dato;

import java.util.ArrayList;

public class frgmnt_Consulta_Documento_Det_Lista extends Fragment implements Adaptador_RV_MenuPrincipal.onClickRV
{
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR QUE CONSULTA DE DOCUMENTO SE HARA
    private static final String ARG_PARAM2 = "param2";


    private String str_Modulo;
    private String str_TipoConsulta;
    String CodigoPallet = null;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    private  static final String frgtag_ConsultaPalletDet = "FRGCPDT";

    //VIEWS
    private RecyclerView rv_Detalle;
    private TextView txtv_Titulo,txtv_Detalle,txtv_CantidadPal,txtv_CantidadEmp,txtv_CantNetaProd;
    private ConstraintLayout cl_DetLista;


    public frgmnt_Consulta_Documento_Det_Lista()
    {
    }
    public static frgmnt_Consulta_Documento_Det_Lista newInstance(String param1, String param2)
    {
        frgmnt_Consulta_Documento_Det_Lista fragment = new frgmnt_Consulta_Documento_Det_Lista();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frgmnt_consulta_doc_det_lista, container, false);
        rv_Detalle= view.findViewById(R.id.rv_Partidas);
        txtv_Titulo = view.findViewById(R.id.txtv_Incidencia);
        txtv_Detalle = view.findViewById(R.id.txtv_Titulo);

        txtv_CantidadPal = view.findViewById(R.id.txtv_CantPallets);
        txtv_CantidadEmp = view.findViewById(R.id.txtv_CantEmpaques);
        txtv_CantNetaProd = view.findViewById(R.id.txtv_CantNetaProd);
        cl_DetLista = view.findViewById(R.id.cl_DetLista);

        ImageButton imgb_AtrasRep = view.findViewById(R.id.imgb_AtrasRep);
        imgb_AtrasRep.setVisibility(View.GONE);
        cl_DetLista.setVisibility(View.GONE);
        return view;
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
        getActivity().getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(R.id.fl_ContenedorFragments, frgmnt_Consulta_Pallet_Det.newInstance(datos, str_Modulo), frgtag_ConsultaPalletDet)
            .commit();
    }


    public void NewInfo(ArrayList<ArrayList<Constructor_Dato>> data, final String str_Titulo, final String str_Detalle)
    {
        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);
        final ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:data)
            {
                String datoDoc =  a.get(1).getDato();
                //String TituloDoc = a.get(0).getTitulo() + " " +a.get(0).getDato();
                String TituloDoc = str_Detalle;//a.get(0).getTitulo() + " " +a.get(0).getDato();
                String tagDocumento = a.get(1).getTitulo();
                ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,null));

            }

        Adaptador_RV_MenuPrincipal adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);

        adv.setOicl(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            }
        });
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv_Detalle.setLayoutManager(lm);

        rv_Detalle.setHasFixedSize(false);
        rv_Detalle.setItemViewCacheSize(10);
        rv_Detalle.setAdapter(adv);

    }



    public void NewInfo(ArrayList<ArrayList<Constructor_Dato>> data, String str_Titulo, String str_Detalle, String str_DetalleEntradas)
    {
        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);

        ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:data)
            {
                String TituloDoc =a.get(1).getTitulo() + " " + a.get(1).getDato();
                String datoDoc = a.get(1).getDato();
                String tagDocumento = a.get(1).getDato();

                a.remove(0);
                a.remove(0);

                ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, str_DetalleEntradas,a));
            }

        Adaptador_RV_MenuPrincipal adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);

        adv.setOicl(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //   mListener.onFragmentInteraction("Dato1", "Dato2");


            }
        });

        adv.setMostrarBotonMasInf(false);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv_Detalle.setLayoutManager(lm);
        rv_Detalle.setHasFixedSize(false);
        rv_Detalle.setItemViewCacheSize(10);
        rv_Detalle.setAdapter(adv);
    }



    public void NewInfo_Det_Lista(ArrayList<ArrayList<Constructor_Dato>> data, String str_Titulo, String str_Detalle, String CantPal, String CantEmp, String CandidadNeta)
    {
        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);
        txtv_CantidadPal.setText(CantPal);
        txtv_CantidadEmp.setText(CantEmp);
        txtv_CantNetaProd.setText(CandidadNeta);
        cl_DetLista.setVisibility(View.VISIBLE);

        ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:data)
            {
                String TituloDoc =a.get(0).getDato();
                String datoDoc = a.get(2).getDato();
                String tagDocumento = "ConsultaPallet";

//                a.remove(0);
//                a.remove(0);

                ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,null));
            }

        Adaptador_RV_MenuPrincipal adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);

        adv.setOicl(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //   mListener.onFragmentInteraction("Dato1", "Dato2");


            }
        });

     //  adv.setMostrarBotonMasInf(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv_Detalle.setLayoutManager(lm);
        rv_Detalle.setHasFixedSize(false);
        rv_Detalle.setItemViewCacheSize(10);
        rv_Detalle.setAdapter(adv);
    }


    public interface OnFragmentInteractionListener
    {
        void clickBotonMasInfo(String[] arr);
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
    }




}
