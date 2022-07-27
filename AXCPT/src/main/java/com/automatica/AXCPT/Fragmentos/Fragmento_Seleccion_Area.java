package com.automatica.AXCPT.Fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.automatica.AXCPT.Principal.frgmnt_Registra_Configuracion;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.databinding.FragmentFragmentoAumentarVistaBinding;
import com.automatica.AXCPT.databinding.FragmentFragmentoSeleccionAreaBinding;
import com.automatica.AXCPT.objetos.ObjetoConstructor;
import com.automatica.AXCPT.objetos.ObjetoParserImagenes;
import com.automatica.AXCPT.objetos.spinnerCustomAdapter;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adConsultasFragmento;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragmento_Seleccion_Area#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragmento_Seleccion_Area extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentFragmentoSeleccionAreaBinding binding;

    Spinner spinner;
    InterfazSeleccionArea mListener;
    String Area, Area2;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragmento_Seleccion_Area() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragmento_Seleccion_Area.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragmento_Seleccion_Area newInstance(String param1, String param2) {
        Fragmento_Seleccion_Area fragment = new Fragmento_Seleccion_Area();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentFragmentoSeleccionAreaBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.spinner);
        view.findViewById(R.id.imgb_AtrasRep).setVisibility(View.VISIBLE);
        ((TextView)view.findViewById(R.id.txtv_Incidencia)).setText("Selección de área");
        view.findViewById(R.id.txtv_Titulo).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.imgb_AtrasRep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
        new SegundoPlano("Consultar").execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Area = ((ObjetoConstructor)spinner.getSelectedItem()).getTitulo();
                Area2 = ((ObjetoConstructor)spinner.getSelectedItem()).getSubtitulo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.buttonSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getAdapter()!=null){
                    new SegundoPlano("Actualizar").execute();
                }
            }
        });

    }

    private class SegundoPlano extends AsyncTask<String, Void, Void> {
        String Tarea;
        DataAccessObject dao = new DataAccessObject();
        adConsultasFragmento ca = new adConsultasFragmento(getContext());


        protected void onPreExecute() {
            mListener.ActivaProgressBar(true);
            super.onPreExecute();
        }
        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {

                switch (Tarea){
                    case "Consultar":
                        dao = ca.c_ConsultaAreas();
                        break;
                    case "Actualizar":
                        dao = ca.c_ActualizaArea(Area);
                        break;

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (dao.iscEstado()){
                    switch (Tarea){
                        case "Consultar":
                            if (dao!=null){

                                try {
                                    ArrayList<Constructor_Dato> cDatos = dao.getcTablasSorteadas("Tag1","Impresora","NumParte");
                                    ArrayList<ObjetoConstructor> datos= new ArrayList<>();
                                    for (Constructor_Dato dato:
                                            cDatos) {
                                        //  Drawable drawable = getResources().getDrawable(R.drawable.ic_pallet_no_etiquetado);

                                        Bitmap bitmap = ObjetoParserImagenes.getBitmapFromVectorDrawable(getContext(),R.drawable.ic_inventory);
                                        datos.add(new ObjetoConstructor(dato.getDato(),dato.getTag1(),bitmap));
                                    }
                                    Log.i("datos",datos.toString());
                                    spinner.setAdapter(new spinnerCustomAdapter(getContext(),datos));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                            break;
                        case "Actualizar":
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString("area", Area2);
                            editor.putBoolean("MostrarSeleccionArea", false);
                            editor.apply();
                            new popUpGenerico(getContext(),getActivity().getCurrentFocus(),"Área actualizada con éxito",dao.iscEstado(),true,true);
                            getActivity().getSupportFragmentManager().beginTransaction().remove(Fragmento_Seleccion_Area.this).commit();
                            break;
                    }
                }else {
                    new popUpGenerico(getContext(),getActivity().getCurrentFocus(),dao.getcMensaje(),dao.iscEstado(),false,false);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            mListener.ActivaProgressBar(false);
        }
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof Fragmento_Seleccion_Area.InterfazSeleccionArea)
        {
            mListener = (InterfazSeleccionArea) context;
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface InterfazSeleccionArea
    {

        boolean ActivaProgressBar(Boolean estado);

    }


}