package com.automatica.AXCMP.ImpresionEtiquetas;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.automatica.AXCMP.Constructor_Dato;
import com.automatica.AXCMP.MiAXC.frgmnt_Consulta_Documento_Det;
import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.adaptadorTabla;
import com.automatica.axc_lib.views.ExpandableListView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class frgmnt__Menu_Impresion_Empaques_Nuevos extends Fragment implements Adaptador_RV_MenuPrincipal.onClickRV
{
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR QUE CONSULTA DE DOCUMENTO SE HARA
    private static final String ARG_PARAM2 = "param2";


    private String str_Modulo;
    private String str_TipoConsulta;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    private ArrayList<Constructor_Dato> data;

    //VIEWS
    private  ExpandableListView elv_DocDet;
    private TextView txtv_Titulo,txtv_Detalle;

    public frgmnt__Menu_Impresion_Empaques_Nuevos()
    {
        // Required empty public constructor
    }

    public static frgmnt__Menu_Impresion_Empaques_Nuevos newInstance(String param1, String param2)
    {
        frgmnt__Menu_Impresion_Empaques_Nuevos fragment = new frgmnt__Menu_Impresion_Empaques_Nuevos();
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
        View view = inflater.inflate(R.layout.frgmnt_consulta_doc_det, container, false);
        elv_DocDet = view.findViewById(R.id.elv_DocDet);
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


    public void NewInfo(ArrayList<Constructor_Dato> data, String str_Titulo, String str_Detalle)
    {

        txtv_Titulo.setText(str_Titulo);
        txtv_Detalle.setText(str_Detalle);
        if(data!=null)
            {
                data.remove(0);
                data.remove(0);
                elv_DocDet.setAdapter(new adaptadorTabla(getContext(), R.layout.list_item_2_datos_fragment, data));
                this.data = data;

            }else
            {
                elv_DocDet.setAdapter(new adaptadorTabla(getContext(), R.layout.list_item_2_datos_fragment, new ArrayList<Constructor_Dato>()));

            }
        Log.i("HOLA","NEW INFO DOCUMENTO DET" + str_Titulo + " " + str_Detalle);
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
    }




}