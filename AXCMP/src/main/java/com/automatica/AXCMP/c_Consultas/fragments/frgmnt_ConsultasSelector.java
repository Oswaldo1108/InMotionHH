package com.automatica.AXCMP.c_Consultas.fragments;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import com.automatica.AXCMP.R;
//
//
//
//public class frgmnt_ConsultasSelector extends Fragment implements  frgmnt_ConsultaEmpaque.OnFragmentInteractionListener,
//                                                                    frgmnt_ConsultaPallet.OnFragmentInteractionListener,
//                                                                    frgmnt_ConsultaPosicion.OnFragmentInteractionListener
//{
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private int FRAGMENTO_ACTIVO = -1;
//
//
//
//    //FRAGMENTOS
//    private frgmnt_ConsultaEmpaque fc;
//    private frgmnt_ConsultaPallet fe;
//    private frgmnt_ConsultaPosicion fg;
//    private frgmnt_ConsultaReferencia fh;
//    private frgmnt_ConsultaBusquedaNumParte fi;
//    //ACCIONES FRAGMENT
//    private FragmentManager fragmentManager;
//    private FragmentTransaction fragmentTransaction;
//
//    //VIEWS
//    FloatingActionButton fab,fab2,fab3,fab4,fab5,fab6;
//    FrameLayout fl_FAB = null;
//
//    // VARIABLES PRIMITIVAS
//    private String mParam1;
//    private String mParam2;
//
//    private boolean isFABOpen = false;
//
//    private OnFragmentInteractionListener mListener;
//
//    public frgmnt_ConsultasSelector()
//    {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment frgmnt_ConsultasSelector.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static frgmnt_ConsultasSelector newInstance(String param1, String param2)
//    {
//        frgmnt_ConsultasSelector fragment = new frgmnt_ConsultasSelector();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null)
//            {
//                mParam1 = getArguments().getString(ARG_PARAM1);
//                mParam2 = getArguments().getString(ARG_PARAM2);
//            }
//    }
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState)
//    {
//        View view=null;
//        try
//            {
//               view = inflater.inflate(R.layout.fragment_frgmnt__consultas_selector, container, false);
//
//                fragmentManager = getActivity().getSupportFragmentManager();
//
//                declararVariables(view);
//                agregarListeners();
//
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        // Inflate the layout for this fragment
//        return view;
//    }
//
//    private void declararVariables(View view)
//    {
//
//        try
//            {
//                fab = (FloatingActionButton) view.findViewById(R.id.fab);
//                fab2 = (FloatingActionButton) view.findViewById(R.id.fab1);
//                fab3 = (FloatingActionButton) view.findViewById(R.id.fab2);
//                fab4 = (FloatingActionButton) view.findViewById(R.id.fab3);
//                fab5 = (FloatingActionButton) view.findViewById(R.id.fab4);
//                fab6 = (FloatingActionButton) view.findViewById(R.id.fab5);
//                fl_FAB = (FrameLayout) view.findViewById(R.id.fl_FAB);
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//    }
//
//    private void agregarListeners()
//    {
//        try
//            {
//                fab.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        //   Toast.makeText(contexto, "HOLA", Toast.LENGTH_LONG).show();
//                        if (!isFABOpen)
//                            {
//                                showFABMenu();
//                            } else
//                            {
//                                closeFABMenu();
//                            }
//                    }
//                });
//
//                fab2.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        logicaMenu(1);
//                    }
//                });
//
//
//                fab3.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//
//                logicaMenu(2);
//
//                    }
//                });
//
//
//                fab4.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        logicaMenu(3);
////                frgmt_ConsultaPallet_QR_Version frm = new frgmt_ConsultaPallet_QR_Version();
////                frm.show(getActivity().getSupportFragmentManager(),"ConsultaPalletQR");
//                    }
//                });
//
//
//
//                fab5.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        logicaMenu(4);
////                frgmt_ConsultaPallet_QR_Version frm = new frgmt_ConsultaPallet_QR_Version();
////                frm.show(getActivity().getSupportFragmentManager(),"ConsultaPalletQR");
//                    }
//                });
//                fab6.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        logicaMenu(5);
////                frgmt_ConsultaPallet_QR_Version frm = new frgmt_ConsultaPallet_QR_Version();
////                frm.show(getActivity().getSupportFragmentManager(),"ConsultaPalletQR");
//                    }
//                });
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//    }
//
//
//
//
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri)
//    {
//        if (mListener != null)
//            {
//                mListener.onFragmentInteraction(uri);
//            }
//    }
//
//    private void showFABMenu()
//    {
//        isFABOpen = true;
//        fab2.animate().translationX(-getResources().getDimension(R.dimen.standard_55));
//        fab3.animate().translationX(-getResources().getDimension(R.dimen.standard_105));
//        fab4.animate().translationX(-getResources().getDimension(R.dimen.standard_155));
//        fab5.animate().translationX(-getResources().getDimension(R.dimen.standard_205));
//        fab6.animate().translationX(-getResources().getDimension(R.dimen.standard_255));
//
//        fl_FAB.setBackgroundColor(getResources().getColor(R.color.negroTrans));
//    }
//
//    private void closeFABMenu()
//    {
//
//        isFABOpen=false;
//        fab2.animate().translationX(0);
//        fab3.animate().translationX(0);
//        fab4.animate().translationX(0);
//        fab5.animate().translationX(0);
//        fab6.animate().translationX(0);
//
//        logicaMenu(0);
//        fl_FAB.setBackgroundColor(getResources().getColor(R.color.Transparente));
//    }
//
//
//    @Override
//    public void onAttach(Context context)
//    {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener)
//            {
//                mListener = (OnFragmentInteractionListener) context;
//            } else
//            {
//                throw new RuntimeException(context.toString()
//                        + " must implement OnFragmentInteractionListener");
//            }
//    }
//
//    @Override
//    public void onDetach()
//    {
//        super.onDetach();
//        mListener = null;
//    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri)
//    {
//
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener
//    {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//
//    private boolean logicaMenu(int fragmentLlamado)
//    {
//
//        try
//            {
//                fragmentTransaction = fragmentManager.beginTransaction();
//
//                if (fragmentLlamado == FRAGMENTO_ACTIVO)
//                    {
//
//                        return true;
//                    }
//
//                switch (fragmentLlamado)
//                    {
//                        case 0:
//
//                            break;
//
//                        case 1:
//                            fc = new frgmnt_ConsultaEmpaque();
//                            fragmentTransaction.add(R.id.fl_FAB, fc);
//                            break;
//
//                        case 2:
//
//                            fe = new frgmnt_ConsultaPallet();
//                            fragmentTransaction.add(R.id.fl_FAB, fe);
//
//                            break;
//
//                        case 3:
//                            fg = new frgmnt_ConsultaPosicion();
//                            fragmentTransaction.add(R.id.fl_FAB, fg);
//
//                            break;
//
//                        case 4:
//                            fh= new  frgmnt_ConsultaReferencia();
//                            fragmentTransaction.add(R.id.fl_FAB, fh);
//
//                            break;
//                        case 5:
//                            fi= new  frgmnt_ConsultaBusquedaNumParte();
//                            fragmentTransaction.add(R.id.fl_FAB, fi);
//
//                            break;
//                    }
//
//
//                switch (FRAGMENTO_ACTIVO)
//                    {
//                        case 0:
//
//                            break;
//
//                        case 1:
//                            fragmentTransaction.remove(fc);
//                            break;
//
//                        case 2:
//                            fragmentTransaction.remove(fe);
//                            break;
//
//                        case 3:
//                            fragmentTransaction.remove(fg);
//                            break;
//
//                        case 4:
//                            fragmentTransaction.remove(fh);
//                            break;
//                        case 5:
//                            fragmentTransaction.remove(fi);
//                            break;
//                    }
//                FRAGMENTO_ACTIVO = fragmentLlamado;
//
//                fragmentTransaction.commit();
//
//                return true;
//
//            }catch (Exception e)
//            {
//                e.printStackTrace();
//                return false;
//            }
//    }
//}
