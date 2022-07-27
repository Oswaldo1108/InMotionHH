package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

public class frgmnt_Consulta_Documento_Partidas extends Fragment implements Adaptador_RV_MenuPrincipal.onClickRV
{
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR QUE CONSULTA DE DOCUMENTO SE HARA
    private static final String ARG_PARAM2 = "param2";


    private String str_Modulo;
    private String str_TipoConsulta;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    //VIEWS
    private RecyclerView rv_Detalle;
    private TextView txtv_Titulo,txtv_Detalle;


    public frgmnt_Consulta_Documento_Partidas()
    {
    }
    public static frgmnt_Consulta_Documento_Partidas newInstance(String param1, String param2)
    {
        frgmnt_Consulta_Documento_Partidas fragment = new frgmnt_Consulta_Documento_Partidas();
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
        View view = inflater.inflate(R.layout.frgmnt_consulta_doc_part, container, false);
        rv_Detalle= view.findViewById(R.id.rv_Partidas);
        txtv_Titulo = view.findViewById(R.id.txtv_Incidencia);
        txtv_Detalle = view.findViewById(R.id.txtv_Titulo);
        ImageButton imgb_AtrasRep = view.findViewById(R.id.imgb_AtrasRep);
        imgb_AtrasRep.setVisibility(View.GONE);
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
    }


    public void NewInfo(ArrayList<ArrayList<Constructor_Dato>> data, String str_Titulo, String str_Detalle)
    {
        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);

        ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:data)
            {
                String TituloDoc =a.get(1).getTitulo() + " " + a.get(1).getDato();
                String datoDoc = a.get(2).getDato();
                String tagDocumento = a.get(1).getDato();

                a.remove(0);
                a.remove(0);
                a.remove(0);

                ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,a));
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


    public void NewInfo_OC(ArrayList<ArrayList<Constructor_Dato>> data, String str_Titulo, String str_Detalle, String str_DetalleEntradas)
    {
        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);

        ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:data)
            {
                String TituloDoc =a.get(1).getTitulo() + " " + a.get(1).getDato();
                String datoDoc = a.get(0).getTitulo() + " " + a.get(0).getDato();
                String tagDocumento = a.get(1).getDato();

                a.remove(0);
                a.remove(0);

                ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,a));
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

    public void NewInfo_OC_Rec(ArrayList<ArrayList<Constructor_Dato>> data, String str_Titulo, String str_Detalle, String str_DetalleEntradas)
    {
        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);

        ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:data)
            {
                String TituloDoc =a.get(0).getTitulo() + " " + a.get(0).getDato();
                String datoDoc = a.get(2).getTitulo() + " " + a.get(2).getDato();
                String tagDocumento = a.get(1).getDato();

                a.remove(0);
                a.remove(0);
                a.remove(0);

                ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,a));
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



    public void NewInfo_Tras(ArrayList<ArrayList<Constructor_Dato>> data, String str_Titulo, String str_Detalle, String str_DetalleEntradas)
    {
        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);

        ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:data)
            {
                String TituloDoc =a.get(0).getDato() + " " + a.get(2).getTitulo();
                String datoDoc = a.get(2).getTitulo() + " " + a.get(2).getDato();
                String tagDocumento = a.get(1).getDato();

                a.remove(0);
                a.remove(0);
                a.remove(0);

                ArrayDocumentos.add(new constructor_Documento(tagDocumento,datoDoc, TituloDoc,a));
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




    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
    }




}
