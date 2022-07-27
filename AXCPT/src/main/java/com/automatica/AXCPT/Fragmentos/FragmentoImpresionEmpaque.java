package com.automatica.AXCPT.Fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorFragConsultas;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.databinding.FragmentFragmentoImpresionEmpaqueBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adConsultasFragmento;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoImpresionEmpaque#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoImpresionEmpaque extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ListarImpresoras= "ListaImpresoras";
    private static final String EtiquetasEmpaque = "EtiquetasEmpaque";
    private static final String Imprimir="Imprimir";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static interfazImpresiones impresiones;

    TableViewDataConfigurator ConfigTabla = null;

    public FragmentoImpresionEmpaque() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoImpresionEmpaque.
     */
    // TODO: Rename and change types and number of parameters


    FragmentFragmentoImpresionEmpaqueBinding binding;
    Spinner spinnerImpresoras;
    FrameLayout progressBarHolder;
    public static FragmentoImpresionEmpaque newInstance(String param1, String param2,interfazImpresiones interfaz) {
        FragmentoImpresionEmpaque fragment = new FragmentoImpresionEmpaque();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        impresiones= interfaz;
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
        binding= FragmentFragmentoImpresionEmpaqueBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggle();
        spinnerImpresoras = binding.vwSpinner.findViewById(R.id.spinner);
        progressBarHolder = binding.progressBarHolder;


        binding.edtxBusqueda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    binding.edtxBusqueda.setText("");
                }            }
        });
        binding.clHeader.txtvIncidencia.setText("Impresión de etiquetas");
        binding.clHeader.txtvTitulo.setText("Etiquetas de "+mParam1);
        new SegundoPlano(ListarImpresoras).execute();
        binding.clHeader.botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.edtxBusqueda.setText("");
            }
        });
        binding.edtxBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxBusqueda.getText())){
                    new popUpGenerico(getContext(),getActivity().getCurrentFocus(),"Llene el campo ''busqueda''",false,true,true);
                    binding.edtxBusqueda.requestFocus();
                    return false;
                }
                new SegundoPlano(EtiquetasEmpaque).execute();
                return false;
            }
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.edtxBusqueda.setText("");
        toggle();
    }
    public void cerrarFragmento(){
        Fragment fragmento;
        fragmento= getActivity().getSupportFragmentManager().findFragmentByTag("ElegirProducto");
        if (fragmento!=null){
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragmento).commit();
            return;
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).runOnCommit(new Runnable() {
            @Override
            public void run() {
                impresiones.taskbarGoneButton();
            }
        }).commit();
    }
    private void toggle() {
        Transition transition = new Slide(Gravity.BOTTOM);
        /*
        if (pantallaNegra.getVisibility()==View.GONE){
             transition = new Slide(Gravity.START);
        }else {
             transition = new Slide(Gravity.END);
        }
        */
        transition.setDuration(300);
        transition.addTarget(binding.PantallaPrincipal);

        TransitionManager.beginDelayedTransition((ViewGroup) binding.PantallaPrincipal.getRootView(),transition);
        if (binding.PantallaPrincipal.getVisibility()==View.VISIBLE){
            binding.PantallaPrincipal.setVisibility(View.GONE);
        }else {
            binding.PantallaPrincipal.setVisibility(View.VISIBLE);
        }
    }
    interface interfazImpresiones{
        void taskbarGoneButton();
    }

    private class SegundoPlano extends AsyncTask<String, Void, Void> {
        String Tarea;
        DataAccessObject dao = new DataAccessObject();
        adConsultasFragmento ca = new adConsultasFragmento(getContext());


        protected void onPreExecute() {
            progressBarHolder.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        public SegundoPlano(String tarea) {
            Tarea = tarea;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {

                    switch (Tarea){
                        case ListarImpresoras:
                            dao = ca.c_ListaImpresoras();
                            break;
                        case EtiquetasEmpaque:
                            if (mParam1.equals("empaque")){
                                dao = ca.c_ImprimeEtiquetaEmpaque(((Constructor_Dato)spinnerImpresoras.getSelectedItem()).getDato(),binding.edtxBusqueda.getText().toString());
                            }else{
                                dao = ca.c_ImpContenedor(binding.edtxBusqueda.getText().toString(),((Constructor_Dato)spinnerImpresoras.getSelectedItem()).getDato(),"1");
                            }
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
                        case ListarImpresoras:
                            spinnerImpresoras.setAdapter(new CustomArrayAdapter(getContext(), android.R.layout.simple_spinner_item,dao.getcTablasSorteadas("Impresora","Impresora")));
                            break;
                        case EtiquetasEmpaque:
                            binding.edtxBusqueda.setText("");
                           // new popUpGenerico(getContext(),getActivity().getCurrentFocus(),"Imprimiendo",dao.iscEstado(),true,true);
                            break;
                    }
                }else {
                    new popUpGenerico(getContext(),getActivity().getCurrentFocus(),dao.getcMensaje(),dao.iscEstado(),false,false);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            progressBarHolder.setVisibility(View.GONE);
        }
    }
}