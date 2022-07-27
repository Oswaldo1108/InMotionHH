package com.automatica.AXCMP.Coflex.Liberaciones_CFLX.LiberacionOC;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.ClasesSerializables.Embarque;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.Adaptador_RV_Lotes_OC;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class frgmnt_Seleccion_Lotes_CFLX extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARTIDA_REAL = "partidareal";
    private static final String ARG_PARTIDA = "partida";//  POSICION PARTIDA EN RV
    private static final String ARG_cant = "cant";

    private int PosicionPartida = 0,CantidadTotal = 0;
    private String[] paramArr = null;
    private String Producto,Partida;
    private static final String frgtag_ImpEtiqueta= "FRGIMP";


    private OnFragmentInteractionListener mListener;

    private ArrayList<Embarque> LotesPartida;
    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";

    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle;
    private ImageButton btn_Back;

    private RecyclerView rv;

//    private EditText edtx_LoteNvo, edtx_CantidadLote;

    private Button btn_AgregarLote,btn_GuardarCambios;

    private Adaptador_RV_Lotes_OC adv_Lotes_OC;


    public frgmnt_Seleccion_Lotes_CFLX()
    {

    }

    public static Fragment newInstance(String Partida,String Producto,int PosicionPartida,int CantidadTotal)
    {
        frgmnt_Seleccion_Lotes_CFLX fragment = new frgmnt_Seleccion_Lotes_CFLX();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2,Producto);
        args.putString(ARG_PARTIDA_REAL,Partida);
        args.putInt(ARG_PARTIDA,PosicionPartida);
        args.putInt(ARG_cant,CantidadTotal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                Producto= getArguments().getString(ARG_PARAM2);
                Partida = getArguments().getString(ARG_PARTIDA_REAL);
                PosicionPartida = getArguments().getInt(ARG_PARTIDA);
                CantidadTotal = getArguments().getInt(ARG_cant);



                Log.i("ARGUMENTOSPALLETDET", "HOLA "+Producto);
            }
    }

    public void AgregarLotes(ArrayList<Embarque> newData, @Nullable boolean ForceUpdate)
    {
        try
            {
//        if (newData.size() > 0)
//            {
//                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
//            } else
//            {
//                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
//            }


                if (adv_Lotes_OC == null || ForceUpdate)
                    {

                        adv_Lotes_OC = new Adaptador_RV_Lotes_OC(newData);
                        adv_Lotes_OC.setMostrarBotonMasInf(false);
                        //        adv.setMostrarEstatus(true);
                        adv_Lotes_OC.setHasStableIds(true);

                        adv_Lotes_OC.setMostrarBotonMasInf(true);
                        //  adv.setVisibilityStatus(false);
                        rv.setHasFixedSize(false);
                        rv.setItemViewCacheSize(20);
                        rv.setAdapter(adv_Lotes_OC);

                    } else
                    {
                        adv_Lotes_OC.AgregarPartida(newData.get(0));
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frgmnt_agregar_lotes_partida_oc, container, false);
        DeclaraVariables(view);





        return view;
    }

    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle = view.findViewById(R.id.txtv_Incidencia);

        txtv_Titulo.setText("Al terminar de llenar cada campo, confirme con la tecla de ENTER");
        txtv_Detalle.setText("Producto - " + Producto);

        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        btn_Back.setVisibility(View.VISIBLE);


//        edtx_LoteNvo = view.findViewById(R.id.edtx_Embarque13);
//        edtx_CantidadLote = view.findViewById(R.id.edtx_Embarque18);



        btn_AgregarLote= view.findViewById(R.id.button5);
        btn_GuardarCambios= view.findViewById(R.id.button6);


        rv = view.findViewById(R.id.rv_Partidas);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));


        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });



        btn_AgregarLote.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    ArrayList<Embarque> newData = new ArrayList<>();

                    newData.add(new Embarque(String.valueOf(Partida),Producto,""));

                    AgregarLotes(newData, false);

                }
            });

        btn_GuardarCambios.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(adv_Lotes_OC.getArrayPartidas() != null)
                    {
                        int cantLotes = adv_Lotes_OC.getCantidadTotalLotes();

                        if(cantLotes == -1)
                            {
                                new popUpGenerico(getContext(),null,"Revise que todos los campos de lote y cantidades esten llenos, para confirmarlos ingrese 'ENTER' ",false, true, true);
                                return;
                            }


                            if(CantidadTotal == cantLotes)

                            {
                                mListener.GuardarLotesPartida(adv_Lotes_OC.getArrayPartidas(), PosicionPartida);
                                getActivity().getSupportFragmentManager().popBackStack();
                            }else
                            {
                                new popUpGenerico(getContext(),null,"La sumatoria de cantidades de los lotes es diferente a la cantidad ingresada para la partida. [ Total: " +CantidadTotal + " / Sumatoria: " + cantLotes+ "]",false, true, true);

                            }
                    }
                Log.i("HOLA", "Boton guardar");

            }
        });

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
        void GuardarLotesPartida(ArrayList<Embarque> LotesPartida,int PosicionPartida);


    }

}
