package com.automatica.AXCMP.MejorasAXC.ReabastecimientoPicking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.automatica.AXCMP.R;
import com.automatica.axc_lib.ClasesSerializables.Embarque;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.views.Adaptador_RV_MenuPrincipal;
import com.automatica.axc_lib.views.Adaptador_RV_Partidas;
import com.automatica.axc_lib.views.Adaptador_RV_Partidas_Orden_Compra;
import com.automatica.axc_lib.views.Adaptador_RV_Partidas_Transfer;
import com.automatica.axc_lib.views.constructor_Documento;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class frgmnt_ListaDocumentos extends Fragment
{
    //VARIABLES
    private String mParam1;
    private String str_Orden;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btn_SinOrden;

    //OBJECTS
    private OnFragmentInteractionListener mListener;
    private Adaptador_RV_MenuPrincipal adv;
    private ArrayList<constructor_Documento> ArrayDocumentos;
    private RecyclerView rv;
    private Adaptador_RV_Partidas adv_Partidas_Embarque;
    private Adaptador_RV_Partidas_Orden_Compra adv_Partidas_OrdenCompra;
    private Adaptador_RV_Partidas_Transfer adv_Partidas_Transfer;

    //VIEWS
    SwipeRefreshLayout swipeRefreshLayout;


    public frgmnt_ListaDocumentos()
    {
    }

    public static frgmnt_ListaDocumentos newInstance(String param1, String param2)
    {
        frgmnt_ListaDocumentos fragment = new frgmnt_ListaDocumentos();
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
                str_Orden = getArguments().getString(ARG_PARAM2);
            }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.reabastece_frgmnt__rv_ordenes_, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv_MenuPrincipal);

        btn_SinOrden = view.findViewById(R.id.btn_SinOrden);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                mListener.refresh();
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getContext()));


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


    public void UpdateData(ArrayList<ArrayList<Constructor_Dato>> newData)
    {

        try
            {
                ArrayDocumentos = new ArrayList<>();

//        txtv_Titulo.setText(str_Titulo);
//        txtv_Detalle.setText(str_Detalle);

                ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

                for (ArrayList<Constructor_Dato> a : newData)
                    {
                        String TituloDoc = a.get(0).getTitulo() + " " + a.get(0).getDato();
                        String datoDoc = "prb";// a.get(1).getTitulo() + " " + a.get(1).getDato();
//                String tagDocumento = a.get(1).getDato();

//                a.remove(4);

//                Toast.makeText(getContext(), TituloDoc, Toast.LENGTH_SHORT).show();
                        if (!TituloDoc.contains("Inicial"))
                            {
//                    a.remove(0);
//                    a.remove(0);
                                ArrayDocumentos.add(new constructor_Documento(datoDoc, TituloDoc, a));
                            } else
                            {
                                mListener.setParametros(a.get(0).getDato(), a.get(1).getDato(), a.get(2).getDato(), a.get(3).getDato());
                            }
                    }


                if (ArrayDocumentos.size() > 0)
                    {
                        getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
                    } else
                    {
                        getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
                    }

                Adaptador_RV_MenuPrincipal adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);
                adv.setMostrarBotonMasInf(false);
//        adv.setMostrarEstatus(true);
                adv.setHasStableIds(true);
                //  adv.setVisibilityStatus(false);
                rv.setHasFixedSize(false);
                rv.setItemViewCacheSize(20);
                rv.setAdapter(adv);

                setSwipeRefreshStatus(false);
            }catch (Exception e)
            {
                e.printStackTrace();

            }
    }

    public void UpdateData_Reab(ArrayList<ArrayList<Constructor_Dato>> newData)
    {

        try
            {

                btn_SinOrden.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mListener.lanzaRebasteceSinOrden();
                    }
                });

//                lanzaRebasteceSinOrden();


                ArrayDocumentos = new ArrayList<>();

//        txtv_Titulo.setText(str_Titulo);
//        txtv_Detalle.setText(str_Detalle);

                ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();

                for (ArrayList<Constructor_Dato> a : newData)
                    {
                        String TituloDoc = a.get(0).getTitulo() + " " + a.get(0).getDato();
                        String datoDoc =  a.get(1).getTitulo() + " " + a.get(1).getDato();


                                ArrayDocumentos.add(new constructor_Documento(a.get(2).getDato(),datoDoc, TituloDoc,null));

                    }


                if (ArrayDocumentos.size() > 0)
                    {
                        getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
                    } else
                    {
                        getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
                    }

                Adaptador_RV_MenuPrincipal adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);
                adv.setMostrarBotonMasInf(true);
//        adv.setMostrarEstatus(true);
                adv.setHasStableIds(true);
                //  adv.setVisibilityStatus(false);
                rv.setHasFixedSize(false);
                rv.setItemViewCacheSize(20);
                rv.setAdapter(adv);

                setSwipeRefreshStatus(false);
            }catch (Exception e)
            {
                e.printStackTrace();

            }
    }




    //region Referencia
    public void UpdateData_Referencia(ArrayList<ArrayList<Constructor_Dato>> newData)
    {
            try
                {
                    ArrayDocumentos = new ArrayList<>();

            //        txtv_Titulo.setText(str_Titulo);
            //        txtv_Detalle.setText(str_Detalle);

            //        ArrayList<constructor_Documento> ArrayDocumentos = new ArrayList<>();
            //        Toast.makeText(getContext(), "PRB", Toast.LENGTH_SHORT).show();

                    for (ArrayList<Constructor_Dato> a : newData)
                        {
                            String TituloDoc = a.get(0).getDato();
                            String datoDoc = a.get(1).getDato();
                            a.remove(0);
                            a.remove(0);
//                            for(Constructor_Dato b :a)
//                                {
//                                    if(b.getDato().equals(""))
//                                        {
//                                            a.remove(b);
//                                        }
//                                }
                            ArrayDocumentos.add(new constructor_Documento( TituloDoc,  datoDoc,a));
                        }


                    if (ArrayDocumentos.size() > 0)
                        {
                            getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
                        } else
                        {
                            getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
                        }

                   adv = new Adaptador_RV_MenuPrincipal(ArrayDocumentos);
                    adv.setMostrarBotonMasInf(false);
            //        adv.setMostrarEstatus(true);
                    adv.setHasStableIds(true);
                    adv.setMostrarBotonMasInf(false);
//                      adv.setVisibilityStatus(false);
                    rv.setHasFixedSize(false);
                    rv.setItemViewCacheSize(20);
                    rv.setAdapter(adv);

                    setSwipeRefreshStatus(false);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
    }

    public void UpdateData_Referencia_Limpiar()
    {

        getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);


        if (adv_Partidas_Embarque == null)
            {
                ArrayList<constructor_Documento> newData = new ArrayList();
                adv = new Adaptador_RV_MenuPrincipal(newData);
                adv.setMostrarBotonMasInf(false);
                adv.setHasStableIds(true);
                rv.setHasFixedSize(false);
                rv.setItemViewCacheSize(20);
                rv.setAdapter(adv);
                setSwipeRefreshStatus(false);

            }else
            {
                adv_Partidas_Transfer.LimpiarPantalla();
            }

    }
    //endregion

//region LIBERACION ORDEN PROD CFX

public void UpdateData_Liberacion_Orden_Prod(ArrayList<ArrayList<Constructor_Dato>> newData)
{
    ArrayList<Embarque> ArrayDocumentos = new ArrayList<>();

    for(ArrayList<Constructor_Dato> a:newData)
        {


            Embarque embarque = new Embarque();
            for(Constructor_Dato b: a)
                {

                    switch(b.getTitulo())
                        {
                            case "OrdenProd":
                                embarque.setOrdenProd(b.getDato());
                                break;
                            case "Partida":
                                embarque.setPartida(b.getDato());
                                break;
                            case "NumParte":
                                embarque.setNumParte(b.getDato());
                                break;
                            case "DNumParte1":
                                embarque.setDNumParte1(b.getDato());
                                break;
                            case "CantidadPedida":
                                embarque.setCantidadPedida(b.getDato());
                                break;
                            case "CantidadSurtida":
                                embarque.setCantidadSurtida(b.getDato());
                                break;
                            case "CantidadPendiente":
                                embarque.setCantidadPendiente(b.getDato());
                                break;
                            case "CantidadActual":
//                                   embarque.setCantidadActual("1");
                                break;
                            case "DStatus":
                                embarque.setDStatus(b.getDato());
                                break;
                            case "Revision":
                                embarque.setLote(b.getDato());
                                break;
                            case "Estacion":
                                embarque.setEstacion(b.getDato());
                                break;
                            case "UnidadMedida":
                                embarque.setUnidadMedida(b.getDato());
                                break;
                            case "Tag1":
                                embarque.setTag1(b.getDato());
                                break;
                            case "Tag2":
                                embarque.setTag2(b.getDato());
                                break;
                            case "Tag3":
                                embarque.setTag3(b.getDato());
                                break;
                        }


                }

            ArrayDocumentos.add(embarque);


        }



    if(ArrayDocumentos.size()>0)
        {
            getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
        }
    else
        {
            getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
        }

    adv_Partidas_Embarque = new Adaptador_RV_Partidas(ArrayDocumentos);
    adv_Partidas_Embarque.setMostrarBotonMasInf(false);
    adv_Partidas_Embarque.CambiarLoteARevision(true);
//        adv.setMostrarEstatus(true);
    adv_Partidas_Embarque.setHasStableIds(true);
    //  adv.setVisibilityStatus(false);
    rv.setHasFixedSize(false);
    rv.setItemViewCacheSize(20);
    rv.setAdapter(adv_Partidas_Embarque);

    setSwipeRefreshStatus(false);
}

    public void UpdateData_Liberacion_Orden_Prod_Limpiar()
    {

        getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);


        if (adv_Partidas_Embarque == null)
            {
                ArrayList<Embarque> newData = new ArrayList();
                adv_Partidas_Embarque = new Adaptador_RV_Partidas(newData);
                adv_Partidas_Embarque.setMostrarBotonMasInf(false);
                adv_Partidas_Embarque.setHasStableIds(true);
                rv.setHasFixedSize(false);
                rv.setItemViewCacheSize(20);
                rv.setAdapter(adv_Partidas_Embarque);
                setSwipeRefreshStatus(false);

            }else
            {
                adv_Partidas_Embarque.LimpiarPantalla();
            }

    }
// endregion


//region LIBERACION EMBARQUE

    public void UpdateData_LiberacionEmbarque(ArrayList<ArrayList<Constructor_Dato>> newData)
    {
        ArrayList<Embarque> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:newData)
            {


                Embarque embarque = new Embarque();
                for(Constructor_Dato b: a)
                    {

                       switch(b.getTitulo())
                           {
                               case "OrdenEmbarque":
                                   embarque.setOrdenProd(b.getDato());
                                   break;
                               case "Partida":
                                   embarque.setPartida(b.getDato());
                                   break;
                               case "NumParte":
                                   embarque.setNumParte(b.getDato());
                                   break;
                               case "DNumParte1":
                                   embarque.setDNumParte1(b.getDato());
                                   break;
                               case "CantidadPedida":
                                   embarque.setCantidadPedida(b.getDato());
                                   break;
                               case "CantidadSurtida":
                                   embarque.setCantidadSurtida(b.getDato());
                                   break;
                               case "CantidadPendiente":
                                   embarque.setCantidadPendiente(b.getDato());
                                   break;
                               case "CantidadActual":
//                                   embarque.setCantidadActual("1");
                                   break;
                               case "DStatus":
                                   embarque.setDStatus(b.getDato());
                                   break;
                               case "Lote":
                                   embarque.setLote(b.getDato());
                                   break;
                               case "Estacion":
                                   embarque.setEstacion(b.getDato());
                                   break;
                               case "UnidadMedida":
                                   embarque.setUnidadMedida(b.getDato());
                                   break;
                               case "Tag1":
                                   embarque.setTag1(b.getDato());
                                   break;
                               case "Tag2":
                                   embarque.setTag2(b.getDato());
                                   break;
                               case "Tag3":
                                   embarque.setTag3(b.getDato());
                                   break;
                           }


                    }

                ArrayDocumentos.add(embarque);


            }



        if(ArrayDocumentos.size()>0)
            {
                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
            }
        else
            {
                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
            }

        adv_Partidas_Embarque = new Adaptador_RV_Partidas(ArrayDocumentos);
        adv_Partidas_Embarque.setMostrarBotonMasInf(false);
//        adv.setMostrarEstatus(true);
        adv_Partidas_Embarque.setHasStableIds(true);
        //  adv.setVisibilityStatus(false);
        rv.setHasFixedSize(false);
        rv.setItemViewCacheSize(20);
        rv.setAdapter(adv_Partidas_Embarque);

        setSwipeRefreshStatus(false);
    }


    public void UpdateData_LiberacionTransfer(ArrayList<Embarque> newData, @Nullable boolean ForceUpdate)
    {


        if (newData.size() > 0)
            {
                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
            } else
            {
                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
            }


        if (adv_Partidas_Transfer == null||ForceUpdate)
        {

            adv_Partidas_Transfer = new Adaptador_RV_Partidas_Transfer(newData);
            adv_Partidas_Transfer.setMostrarBotonMasInf(false);
    //        adv.setMostrarEstatus(true);
            adv_Partidas_Transfer.setHasStableIds(true);

            adv_Partidas_Transfer.setMostrarBotonMasInf(true);
            //  adv.setVisibilityStatus(false);
            rv.setHasFixedSize(false);
            rv.setItemViewCacheSize(20);
            rv.setAdapter(adv_Partidas_Transfer);
            setSwipeRefreshStatus(false);

        }else
            {
                adv_Partidas_Transfer.AgregarPartida(newData.get(0));
            }

    }
    public void UpdateData_LiberacionTransferencia_Limpiar()
    {

        getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);


        if (adv_Partidas_Embarque == null)
            {
                ArrayList<Embarque> newData = new ArrayList();
                adv_Partidas_Transfer = new Adaptador_RV_Partidas_Transfer(newData);
                adv_Partidas_Transfer.setMostrarBotonMasInf(false);
                adv_Partidas_Transfer.setHasStableIds(true);
                rv.setHasFixedSize(false);
                rv.setItemViewCacheSize(20);
                rv.setAdapter(adv_Partidas_Transfer);
                setSwipeRefreshStatus(false);

            }else
            {
                adv_Partidas_Transfer.LimpiarPantalla();
            }

    }





    public void ActualizaLote(int Partida, String LoteNuevo)
    {
        if(adv_Partidas_Embarque!=null)
            {
                adv_Partidas_Embarque.ActualizaLote(Partida,LoteNuevo);
            }
    }

    public void ActualizaEstacion(int Partida, String Estacion)
    {
        if(adv_Partidas_Embarque!=null)
            {
                adv_Partidas_Embarque.ActualizaEstacion(Partida,Estacion);
            }
    }


    public ArrayList<Embarque> getArrayPartidasEmbarque()
    {
        return adv_Partidas_Embarque.getArrayPartidas();
    }

        /**
         *
         * Metodo generada un String con todos los numeros de parte de la orden
         *
         * */

    public String getArticulosDePartidas()
    {
        ArrayList<Embarque> Embarques = adv_Partidas_Embarque.getArrayPartidas();

        String Articulos = "";

        int control = 0 ;
        for(Embarque embarque: Embarques)
            {
                        Articulos = Articulos + embarque.getNumParte() + ",";


                        if(control > 0 && control < Embarques.size())
                            {
                                Articulos = Articulos + ",";
                            }


                        control++;

            }
        return Articulos;
    }



    /**
     *
     * Agregar Nueva partida al array
     *
     * */
    public void AgregarPartida(Embarque emb)
    {
        adv_Partidas_Embarque.AgregarPartida(emb);
    }


//endregion



    //region LIBERACION OC



    public void UpdateData_Liberacion_Orden_Compra(ArrayList<ArrayList<Constructor_Dato>> newData)
    {
        ArrayList<Embarque> ArrayDocumentos = new ArrayList<>();

        for(ArrayList<Constructor_Dato> a:newData)
            {


                Embarque embarque = new Embarque();
                for(Constructor_Dato b: a)
                    {

                        Log.i("ADAPTER 1",b.getTitulo() + " " + b.getDato());
                        switch(b.getTitulo())
                            {
                                case "OrdenCompra":
                                    embarque.setOrdenProd(b.getDato());
                                    break;
                                case "Partida":
                                    embarque.setPartida(b.getDato());
                                    break;
                                case "NumParte":
                                    embarque.setNumParte(b.getDato());
                                    break;
                                case "DNumParte1":
                                    embarque.setDNumParte1(b.getDato());
                                    break;
                                case "CantidadOrdenada":
                                    embarque.setCantidadPedida(b.getDato());
                                    break;
                                case "CantidadRecibida":
                                    embarque.setCantidadSurtida(b.getDato());
                                    break;
                                case "CantidadARecibir":
                                    embarque.setCantidadPendiente(b.getDato());
                                    break;
                                case "CantidadActual":
//                                   embarque.setCantidadActual("1");
                                    break;
                                case "DStatus":
                                    embarque.setDStatus(b.getDato());
                                    break;
                                case "Lote":
                                    embarque.setLote(b.getDato());
                                    break;
                                case "Estacion":
                                    embarque.setEstacion(b.getDato());
                                    break;
                                case "UnidadMedida":
                                    embarque.setUnidadMedida(b.getDato());
                                    break;
                                case "Tag1":
                                    embarque.setTag1(b.getDato());
                                    break;
                                case "Tag2":
                                    embarque.setTag2(b.getDato());
                                    break;
                                case "Tag3":
                                    embarque.setTag3(b.getDato());
                                    break;
                            }


                    }

                ArrayDocumentos.add(embarque);

                Log.i("ADAPTER SIZE",ArrayDocumentos.size()+ " ");

            }



        if(ArrayDocumentos.size()>0)
            {
                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.GONE);
            }
        else
            {
                getActivity().findViewById(R.id.txtv_MensajeNoInfo).setVisibility(View.VISIBLE);
            }

        adv_Partidas_OrdenCompra = new Adaptador_RV_Partidas_Orden_Compra(ArrayDocumentos);
        adv_Partidas_OrdenCompra.setMostrarBotonMasInf(false);
//        adv.setMostrarEstatus(true);
        adv_Partidas_OrdenCompra.setHasStableIds(true);
        //  adv.setVisibilityStatus(false);
        rv.setHasFixedSize(false);
        rv.setItemViewCacheSize(20);
        rv.setAdapter(adv_Partidas_OrdenCompra);

        setSwipeRefreshStatus(false);
    }





    //endregion

    public interface OnFragmentInteractionListener
    {

        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(boolean estado);
        void refresh();
        void setParametros(String Inicial, String Entradas, String Salidas, String Existencia);
        void lanzaRebasteceSinOrden();

        //Object UpdateTimeline(String[] Resources);
    }


    public void setSwipeRefreshStatus(Boolean Status)
    {
        swipeRefreshLayout.setRefreshing(Status);
    }

}
