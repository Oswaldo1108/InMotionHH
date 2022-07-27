package com.automatica.AXCPT.Fragmentos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorFragConsultas;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;
import com.automatica.AXCPT.databinding.FragmentFragmentoConsultaBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adConsultasFragmento;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoConsulta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoConsulta extends Fragment{
    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    public static final int TIPO_PALLET = 1;
    public  static final int TIPO_EMPAQUE= 2;
    public  static final int TIPO_POSICION= 3;
    public   static final int TIPO_REFERENCIA= 4;
    public  static final int TIPO_EXISTENCIA= 5;
    public static final int TIPO_PALLETNE = 6;
    public static final int TIPO_BLOQUEADOS = 7;
    public static final String TAG= "fragmentoConsulta";
    boolean block = true;

    FrameLayout progressBarHolder;



    TableViewDataConfigurator ConfigTabla = null;
    popUpGenerico pop;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    FragmentFragmentoConsultaBinding binding;

    private String[] mParam1;
    private int mParam2;

    public FragmentoConsulta() {
        // Required empty public constructor
    }

    RecyclerView recycler;
    TextView titulo, subtitulo;
    ConstraintLayout cl_header;
    SimpleTableDataAdapter st;
    SimpleTableHeaderAdapter sth;
    SortableTableView tblv_Referencia;
    HorizontalScrollView hsv_tabla_embarques;


    /**
     * param1[]
     * [0] A consultar
     * [1] campo
     * [2] campo
     */

    public static FragmentoConsulta newInstance(String[] Datos, int Tipo) {
        FragmentoConsulta fragment = new FragmentoConsulta();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, Datos);
        args.putInt(ARG_PARAM2, Tipo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArray(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }


    public void cerrarGesto(View view){
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (deltaX < MIN_DISTANCE && deltaX != 0) {

                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {

                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toggle();
        //cerrarGesto(view);
        pop= new popUpGenerico(getContext());
        recycler = view.findViewById(R.id.recycler);
        cl_header = view.findViewById(R.id.cl_header);
        titulo = cl_header.findViewById(R.id.txtv_Incidencia);
        subtitulo = cl_header.findViewById(R.id.txtv_Titulo);
        hsv_tabla_embarques = view.findViewById(R.id.hsv_tabla_embarques);
        tblv_Referencia = hsv_tabla_embarques.findViewById(R.id.tableView_OC);
        binding.edtxCodigo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        progressBarHolder = binding.progressBarHolder;
        binding.edtxCodigo.requestFocus();
        binding.clHeader.botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.setAdapter(null);
                tblv_Referencia.getDataAdapter().clear();
                tblv_Referencia.getDataAdapter().notifyDataSetChanged();
                binding.edtxCodigo.setText("");
            }
        });
        binding.clHeader.botonEmpaques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (recycler.getVisibility() == View.VISIBLE)
                    {
                        recycler.setVisibility(View.GONE);
                        hsv_tabla_embarques.setVisibility(View.VISIBLE);
                        new SegundoPlano(TIPO_PALLETNE).execute();
                    } else
                    {
                        recycler.setVisibility(View.VISIBLE);
                        hsv_tabla_embarques.setVisibility(View.GONE);
                        new SegundoPlano(TIPO_PALLET).execute();
                    }
            }
        });

        binding.clHeader.botonBloqueados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (block){
                    block=false;
                    binding.clHeader.txtvTitulo.setText("Bloqueados");
                    binding.subtitulo.setText("Bloqueados: ");
                    binding.clHeader.botonBloqueados.setImageResource(R.drawable.ic_round_production_quantity_limits_24);
                    binding.hsvTablaEmbarques.tableViewOC.getDataAdapter().clear();
                    new SegundoPlano(TIPO_BLOQUEADOS).execute();
                }else{
                    block=true;
                    binding.clHeader.txtvTitulo.setText("Existencias");
                    binding.subtitulo.setText("Existencias: ");
                    binding.clHeader.botonBloqueados.setImageResource(R.drawable.ic_baseline_block_24);
                    binding.hsvTablaEmbarques.tableViewOC.getDataAdapter().clear();
                    if (!TextUtils.isEmpty(binding.edtxCodigo.getText().toString())){
                        new SegundoPlano(TIPO_EXISTENCIA).execute();
                    }
                }
            }
        });

        binding.edtxCodigo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxCodigo.getText())){
                    pop.popUpGenericoDefault(getView(),getString(R.string.msg_llene_campo),false);
                    binding.edtxCodigo.requestFocus();
                    new esconderTeclado(getActivity());
                    return false;
                }
                new SegundoPlano(mParam2).execute();
                return false;
            }
        });
        if (mParam2==TIPO_EXISTENCIA){
            binding.edtxCodigo.setHint(R.string.msg_presionado);
            binding.edtxCodigo.setCustomSelectionActionModeCallback(new ActionMode.Callback2() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
            binding.edtxCodigo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Vibrator vibratorService = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
                    vibratorService.vibrate(150);
                    String[] armar = {binding.edtxCodigo.getText().toString()};
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(binding.PantallaPrincipal.getId(), frgmnt_Seleccion_Producto.newInstance(armar, "", new frgmnt_Seleccion_Producto.OnFragmentInteractionListener() {
                                @Override
                                public boolean ActivaProgressBar(Boolean estado) {
                                    
                                    return false;
                                }

                                @Override
                                public void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto) {
                                    binding.edtxCodigo.setText(prmProducto);
                                    new SegundoPlano(mParam2).execute();
                                }
                            }), "ElegirProducto").commit();
                    return true;
                }
            });
        }else {
            binding.edtxCodigo.setHint("");
        }
        titulo.setText(getString(R.string.fragmento_consulta));
        String Subtitulo;
        switch (mParam2){
            case TIPO_PALLET:
                Subtitulo=getString(R.string.consulta_pallet);
                binding.clHeader.botonEmpaques.setVisibility(View.VISIBLE);
                binding.clHeader.botonBloqueados.setVisibility(View.GONE);
                break;
            case TIPO_EMPAQUE:
                Subtitulo=getString(R.string.consulta_empaque);
                binding.clHeader.botonEmpaques.setVisibility(View.GONE);
                binding.clHeader.botonBloqueados.setVisibility(View.GONE);
                break;
            case TIPO_EXISTENCIA:
                Subtitulo=getString(R.string.consulta_existencia);
                binding.clHeader.botonEmpaques.setVisibility(View.GONE);
                binding.clHeader.botonBloqueados.setVisibility(View.VISIBLE);

                break;
            case TIPO_POSICION:
                Subtitulo=getString(R.string.consulta_ubicacion);
                binding.clHeader.botonEmpaques.setVisibility(View.GONE);
                binding.clHeader.botonBloqueados.setVisibility(View.GONE);
                break;
            case TIPO_REFERENCIA:
                Subtitulo=getString(R.string.consulta_referencia);
                binding.clHeader.botonEmpaques.setVisibility(View.GONE);
                binding.clHeader.botonBloqueados.setVisibility(View.GONE);
                break;
            case TIPO_PALLETNE:
                Subtitulo= "Pallet";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mParam2);
        }
        binding.subtitulo.setText(Subtitulo+": ");
        binding.clHeader.txtvTitulo.setText(Subtitulo);
        if (mParam1!=null){
            binding.edtxCodigo.setText(mParam1[0]);
            new SegundoPlano(mParam2).execute();
        }


        binding.edtxCodigo.requestFocus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentFragmentoConsultaBinding.inflate(inflater,null,false);
        return binding.getRoot();
    }


    private class SegundoPlano extends AsyncTask<String, Void, Void> {
        int Tarea;
        DataAccessObject dao = new DataAccessObject();
        adConsultasFragmento ca = new adConsultasFragmento(getContext());

        public SegundoPlano(int tarea) {
            Tarea = tarea;
        }

        @Override
        protected void onPreExecute() {
            progressBarHolder.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                switch (Tarea) {


                    case TIPO_PALLET:

                        if(!binding.edtxCodigo.getText().toString().equals(""))
                        {
                            dao = ca.c_ConsultarPalletPT(binding.edtxCodigo.getText().toString());
                        }else
                        {
                            dao = ca.c_ConsultarPalletPT(binding.clHeader.txtvTitulo.getText().toString());
                        }
                        break;
                    case TIPO_EMPAQUE:
                        dao = ca.c_ConsultaEmpaqueMP(binding.edtxCodigo.getText().toString());
                        break;
                    case TIPO_EXISTENCIA:
                        dao = ca.c_ConsultaPalletArticulo(binding.edtxCodigo.getText().toString(),"");
                        break;
                    case TIPO_POSICION:
                        dao = ca.c_BuscarUbicacion(binding.edtxCodigo.getText().toString());
                        break;
                    case TIPO_REFERENCIA:
                        dao= ca.c_ConsultaReferencia(binding.edtxCodigo.getText().toString());
                        break;
                    case TIPO_PALLETNE:
                        if(!binding.edtxCodigo.getText().toString().equals(""))
                        {
                            dao= ca.c_ConsultaEmpaquesPallet_NE(binding.edtxCodigo.getText().toString());
                        }else
                        {
                            dao= ca.c_ConsultaEmpaquesPallet_NE(binding.clHeader.txtvTitulo.getText().toString());
                        }

                        break;
                    case TIPO_BLOQUEADOS:
                        if (TextUtils.isEmpty(binding.edtxCodigo.getText())){
                            dao = ca.c_ListaBloqueados("");
                        }else {
                            dao = ca.c_ListaBloqueados(binding.edtxCodigo.getText().toString());
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                new esconderTeclado(getActivity());
                if (dao.iscEstado()) {
                    switch (Tarea) {
                        case TIPO_PALLET:
                            if(!binding.edtxCodigo.getText().toString().equals(""))
                            {
                                binding.clHeader.txtvTitulo.setText(binding.edtxCodigo.getText().toString());
                            }
                            if (dao.getSoapObject_parced().getPrimitivePropertyAsString("Revision").equals("1")) {
                                recycler.setVisibility(View.VISIBLE);
                                recycler.setAdapter(new AdaptadorFragConsultas(CrearAdapterPallet(dao)));
                            }else {
                                recycler.setVisibility(View.VISIBLE);
                                recycler.setAdapter(new AdaptadorFragConsultas(CrearAdapterPallet(dao)));
                            }
                            break;
                        case TIPO_EMPAQUE:
                            Log.i("get", String.valueOf(dao.getSoapObject_parced().getPropertyCount()));
                            recycler.setVisibility(View.VISIBLE);
                            recycler.setAdapter(new AdaptadorFragConsultas(CrearAdapterEmpaque(dao)));
                            break;
                        case TIPO_POSICION:
                            hsv_tabla_embarques.setVisibility(View.VISIBLE);
                            binding.clHeader.txtvTitulo.setText(binding.edtxCodigo.getText() +  " (" + dao.getcMensaje() + ")");
                            if (dao.getcTablas()!=null){
                                ConfigTabla= TableViewDataConfigurator.newInstance(tblv_Referencia, dao, getActivity(), new TableViewDataConfigurator.TableClickInterface() {
                                    @Override
                                    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

                                    }

                                    @Override
                                    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {
                                        Toast.makeText(getContext(),MensajeCompleto,Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

                                    }
                                });
                            }else{
                                tblv_Referencia.getDataAdapter().clear();
                            }
                            break;
                        case TIPO_REFERENCIA:
                            hsv_tabla_embarques.setVisibility(View.VISIBLE);
                            binding.clHeader.txtvTitulo.setText("Referencia (" +binding.edtxCodigo.getText() + ")");
                            if (dao.getcTablas()!=null){
                                ConfigTabla= TableViewDataConfigurator.newInstance(tblv_Referencia, dao, getActivity(), new TableViewDataConfigurator.TableClickInterface() {
                                    @Override
                                    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

                                    }

                                    @Override
                                    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {
                                        Toast.makeText(getContext(),MensajeCompleto,Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

                                    }
                                });
                            }else{
                                tblv_Referencia.getDataAdapter().clear();
                            }
                            break;
                        case TIPO_EXISTENCIA:
                        case TIPO_PALLETNE:
                        case TIPO_BLOQUEADOS:
                            hsv_tabla_embarques.setVisibility(View.VISIBLE);
                            if (dao.getcTablas()!=null){
                                ConfigTabla= TableViewDataConfigurator.newInstance(tblv_Referencia, dao, getActivity(), new TableViewDataConfigurator.TableClickInterface() {
                                    @Override
                                    public void onTableHeaderClick(int columnIndex, String Header, String IdentificadorTabla) {

                                    }

                                    @Override
                                    public void onTableLongClick(int rowIndex, String[] clickedData, String MensajeCompleto, String IdentificadorTabla) {
                                        Toast.makeText(getContext(),MensajeCompleto,Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTableClick(int rowIndex, String[] clickedData, boolean Seleccionado, String IdentificadorTabla) {

                                    }
                                });
                            }else{
                                tblv_Referencia.getDataAdapter().clear();
                            }
                            break;
                    }
                    binding.edtxCodigo.requestFocus();
                }else {
                    pop.popUpGenericoDefault(getView(),dao.getcMensaje(),dao.iscEstado());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBarHolder.setVisibility(View.GONE);


        }
    }


    private ArrayList<Constructor_Dato> CrearAdapterPallet(DataAccessObject dao) {
        ArrayList<Constructor_Dato> Datos = new ArrayList<>();
        PropertyInfo pisp;
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //if (dao.getcTablaUnica() != null) {
        SoapObject objeto = dao.getSoapObject_parced();


        for (int i = 0; i < objeto.getPropertyCount(); i++) {
            pisp = new PropertyInfo();
            objeto.getPropertyInfo(i, pisp);
            String Etiqueta = pisp.name.replace("_x0020_", " ");
            Log.i("Etiqueta", Etiqueta);
            String Dato = objeto.getPropertyAsString(i);
            Log.i("Dato", Dato);
            if (Dato.equals("anyType{}")) {
                Dato = "";
            }
            if (Etiqueta.equals("IdPallet")){
                continue;
            }
            if (Etiqueta.equals("Status")){
                continue;
            }
            if (Etiqueta.equals("Revision")) {
                Etiqueta = "Tipo de reg.";
                if (Dato.equals("PROD")){
                    Dato= "Producción";
                }else if(Dato.equals("U")) {
                    Dato="Empaque único";
                }else if (Dato.equals("DEV"))
                    Dato = "Devolución";
                else if (Dato.equals("AE"))
                    Dato = "Ajuste por inventario";
                else if (Dato.equals("NE"))
                    Dato = "No Etiquetado";
                else
                    Dato = "AnyType()";
            }
            if (Etiqueta.equals("Ubicacion")) {
                Etiqueta = "Posición";
            }
            if (Etiqueta.equals("DescStatus")){
                Etiqueta= "Estatus";
            }

            Constructor_Dato dato = new Constructor_Dato(Etiqueta, Dato);
            Datos.add(dato);
        }
        return Datos;
    }
    private ArrayList<Constructor_Dato> CrearAdapterEmpaque(DataAccessObject dao) {
        ArrayList<Constructor_Dato> Datos = new ArrayList<>();
        PropertyInfo pisp;
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Datos.add(new Constructor_Dato("Código", binding.edtxCodigo.getText().toString()));

        SoapObject objeto = dao.getSoapObject_parced();
        for (int i = 0; i < objeto.getPropertyCount(); i++) {
            pisp = new PropertyInfo();
            objeto.getPropertyInfo(i, pisp);
            String Etiqueta = pisp.name.replace("_x0020_", " ");
            Log.i("Etiqueta", Etiqueta);
            String Dato = objeto.getPropertyAsString(i);
            Log.i("Dato", Dato);
            if (Dato.equals("anyType{}")) {
                Dato = "";
            }
            if (Etiqueta.equals("IdEmpaquePT")){
                continue;
            }
            if (Etiqueta.equals("Status")){
                continue;
            }
            if (Etiqueta.equals("Revision")) {
                continue;
            }
            if (Etiqueta.equals("Ubicacion")) {
                Etiqueta = "Posición";
            }
            if (Etiqueta.equals("DescStatus")){
                Etiqueta= "Estatus";
            }
            if (Etiqueta.equals("Descripcion")){
                continue;
            }
            Constructor_Dato dato = new Constructor_Dato(Etiqueta, Dato);
            Datos.add(dato);
        }
        return Datos;
    }
    private ArrayList<Constructor_Dato> CrearAdapter(DataAccessObject dao) {
        ArrayList<Constructor_Dato> Datos = new ArrayList<>();
        PropertyInfo pisp;
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //if (dao.getcTablaUnica() != null) {
            SoapObject objeto = dao.getSoapObject_parced();
            for (int i = 0; i < objeto.getPropertyCount(); i++) {
                pisp = new PropertyInfo();
                objeto.getPropertyInfo(i, pisp);
                String Etiqueta = pisp.name.replace("_x0020_", " ");
                Log.i("Etiqueta", Etiqueta);
                String Dato = objeto.getPropertyAsString(i);
                Log.i("Dato", Dato);
                if (Dato.equals("anyType{}")) {
                    Dato = "";
                }
                if (Etiqueta.equals("Revision")) {
                    Etiqueta = "Revisión";
                }
                Constructor_Dato dato = new Constructor_Dato(Etiqueta, Dato);
                Datos.add(dato);
            }
       // } else {
        /*
            SoapObject objeto = dao.getSoapObject();
            for (int i = 0; i < objeto.getPropertyCount(); i++) {
                pisp = new PropertyInfo();
                objeto.getPropertyInfo(i, pisp);
                String Etiqueta = pisp.name.replace("_x0020_", " ");
                Log.i("Etiqueta", Etiqueta);
                String Dato = objeto.getPropertyAsString(i);
                Log.i("Dato", Dato);
                if (Dato.equals("anyType{}")) {
                    Dato = "";
                }
                Constructor_Dato dato = new Constructor_Dato(Etiqueta, Dato);
                Datos.add(dato);
            }

         */
      //  }

        return Datos;
    }

    private void CrearTabla(DataAccessObject dao, TableColumnDpWidthModel model) {
        tblv_Referencia.setColumnModel(model);
        tblv_Referencia.setDataAdapter(st = new SimpleTableDataAdapter(getContext(), dao.getcTablaUnica()));
        tblv_Referencia.setHeaderAdapter(sth = new SimpleTableHeaderAdapter(getContext(), dao.getcEncabezado()));
        st.setTextColor(getResources().getColor(R.color.blancoLetraStd));
        sth.setTextColor(getResources().getColor(R.color.blancoLetraStd));

        binding.edtxCodigo.setTextIsSelectable(false);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.edtxCodigo.setText("");
        toggle();
    }

    public void cerrarFragmento(){
        Fragment fragmento;
        fragmento= getActivity().getSupportFragmentManager().findFragmentByTag("ElegirProducto");
        if (fragmento!=null){
            Fragment fragmento1= getActivity().getSupportFragmentManager().findFragmentByTag("vistaAumentada");
            if (fragmento1!=null){
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmento1).commit();
                return;
            }
            getActivity().getSupportFragmentManager().beginTransaction().remove(fragmento).commit();
            return;
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

}