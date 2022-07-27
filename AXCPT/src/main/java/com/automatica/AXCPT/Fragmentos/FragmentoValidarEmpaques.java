package com.automatica.AXCPT.Fragmentos;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Validacion.ValidacionCalidad;
import com.automatica.AXCPT.Validacion.ValidacionPalletCalidad;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.databinding.FragmentFragmentoValidarEmpaquesBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adConsultasFragmento;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Recepcion;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoValidarEmpaques#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoValidarEmpaques extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;

    popUpGenerico pop;
    FragmentFragmentoValidarEmpaquesBinding binding;

    public FragmentoValidarEmpaques() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoValidarEmpaques.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoValidarEmpaques newInstance(String param1, String param2, String param3, String param4) {
        FragmentoValidarEmpaques fragment = new FragmentoValidarEmpaques();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFragmentoValidarEmpaquesBinding.inflate(inflater,container,false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvEmpaque.setText(mParam1);
       // binding.tvArticulo.setText(mParam2);
       // binding.tvCantidad.setText(mParam3);
      //  binding.tvLote.setText(mParam4);
        Funcionalidades();
        new SegundoPlano("Consultar").execute();
       // new SegundoPlano("ConsultaEmpaque").execute();
    }



    private void Funcionalidades(){

        binding.btnValidar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                new SegundoPlano("Validar").execute();
                getFragmentManager().beginTransaction().remove(FragmentoValidarEmpaques.this).commit();
            }
        });

        binding.btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.constraintBotones.setVisibility(View.INVISIBLE);
                binding.constraitMotivo.setVisibility(View.VISIBLE);

            }
        });

        binding.bkgroundNegro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }




     private class SegundoPlano extends AsyncTask<Void, Void, Void>{

        String tarea;
        DataAccessObject dao;
        adConsultasFragmento ca = new adConsultasFragmento(getContext());

         public SegundoPlano(String tarea) {
             this.tarea = tarea;
         }
         View LastView;

         @Override
        protected void onPreExecute() {
            binding.progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                switch (tarea) {
                    case"Validar":
                        dao = ca.c_ValidarPalletCalidad(binding.tvEmpaque.getText().toString());
                        break;
                    case "Consultar":
                        dao = ca.c_ListarValidacionCalidad();
                        break;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (dao.iscEstado()) {
                    switch (tarea) {
                        case "Consultar":
                            binding.tvArticulo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Artículo"));
                            binding.tvCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Cantidad"));
                            binding.tvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Lote"));
                            break;
                        case "Validar":
                            new popUpGenerico(getContext(),getActivity().getCurrentFocus(),"Pallet Validado con éxito",dao.iscEstado(),false,false);
                            break;
                    }
                } else {
                    pop.popUpGenericoDefault(getView(), dao.getcMensaje(), false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                pop.popUpGenericoDefault(getView(), e.getMessage(), false);
            }
            binding.progressBarHolder.setVisibility(View.GONE);
        }
    }

}