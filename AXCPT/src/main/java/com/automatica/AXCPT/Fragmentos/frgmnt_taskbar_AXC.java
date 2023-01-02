package com.automatica.AXCPT.Fragmentos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.automatica.AXCPT.Fragmentos.Elementos.AdaptadorItem;
import com.automatica.AXCPT.Fragmentos.Elementos.Item;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.databinding.AxcActionFootbarBinding;

import java.util.ArrayList;

public class frgmnt_taskbar_AXC extends Fragment
{
    //VARIABLES

    private boolean regresarMnuPrincipal;
    private static final String REGRESAR_MENU_PRINCIPAL = "REG";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static final String DEFAULT="DEFAULT";
    public static final String SELECCIONAR="SELECCIONAR";
    public static final String SIGUIENTE="SIGUIENTE";
    public static final String REGISTRAR= "REGISTRAR";
    public static final String CERRAR_TARIMA="CERRAR_TARIMA";
    public static final String CERRAR_REEMPAQUE="CERRAR_REEMPAQUE";
    public static final String CERRAR_CARRITO="CERRAR_CARRITO";
    public static final String REBASTECIMIENTO="REBASTECIMIENTO";
    public static final String TOMAR_INVENTARIO ="TOMAR_INVENTARIO";
    public static final String REGISTRO= "REGISTRO";
    public static final String TERMINAR= "TERMINAR";
    public static final String RECEPCION= "RECEPCION";
    public static final String IMPRIMIR= "IMPRIMIR";
    public static final String VALIDAR= "VALIDAR";



    private TextView txtv_Next,txtv_Last;
    private FrameLayout gridMenu;
    AxcActionFootbarBinding binding;
    ArrayList<Item> items;
    String[] armar;

    boolean bToggle= false;

    String resourcePantalla= DEFAULT;

    interfazTaskbar taskbar;
    //OBJECTS


    //VIEWS



    public frgmnt_taskbar_AXC()
    {
    }

    public static Fragment newInstance(String param1, String param2)
    {
        frgmnt_taskbar_AXC fragment = new frgmnt_taskbar_AXC();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static Fragment newInstance(boolean regresarMnuPrincipal)
    {
        frgmnt_taskbar_AXC fragment = new frgmnt_taskbar_AXC();
        Bundle args = new Bundle();
        args.putBoolean(REGRESAR_MENU_PRINCIPAL, regresarMnuPrincipal);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                regresarMnuPrincipal = getArguments().getBoolean(REGRESAR_MENU_PRINCIPAL);
            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState)
    {
        ArmarLista();
        binding = AxcActionFootbarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.gridMenu.setClickable(true);
        gridMenu = binding.frameGrid;
        cargarRecycler();
        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        binding.BotonDer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(FragmentoConsulta.TAG);
                if (fragment instanceof FragmentoImpresionesPorArticulo){
                    ((FragmentoImpresionesPorArticulo) fragment).ClicImprimir();
                    return;
                }
                if (fragment instanceof FragmentoImpresionEmpaque){

                    return;
                }
                taskbar.BotonDerecha();
            }
        });
        binding.BotonIzqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskbar.BotonIzquierda();
            }
        });

        requireActivity().registerForContextMenu(binding.BotonDer);
    }


    public boolean toggle() {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(300);
        transition.addTarget(binding.gridMenu);

        Transition transition1 = new Fade();
        transition1.setDuration(400);
        transition1.addTarget(binding.frameGrid);
        TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot(),transition);
        TransitionManager.beginDelayedTransition((ViewGroup)binding.getRoot(),transition1);

        if (gridMenu.getVisibility()==View.VISIBLE){
            gridMenu.setVisibility(View.GONE);
            binding.gridMenu.setVisibility(View.GONE);
            bToggle=false;
            cambiarResources(resourcePantalla);
            return false;
        }else {
            bToggle=true;
            gridMenu.setVisibility(View.VISIBLE);
            binding.gridMenu.setVisibility(View.VISIBLE);
            cambiarResources(resourcePantalla);
            return true;
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void ArmarLista(){
        items = new ArrayList<>();
        armar= new String[]{"", ""};
        items.add(new Item(getString(R.string.taskbar_consulta_pallet),getContext().getResources().getDrawable(R.drawable.ic__1cons_pallet),FragmentoConsulta.newInstance(null,FragmentoConsulta.TIPO_PALLET),FragmentoConsulta.TAG));
        items.add(new Item(getString(R.string.taskbar_consulta_empaque),getContext().getResources().getDrawable(R.drawable.ic__2_cons_empaque),FragmentoConsulta.newInstance(null,FragmentoConsulta.TIPO_EMPAQUE),FragmentoConsulta.TAG));
        items.add(new Item(getString(R.string.taskbar_consulta_existencias),getContext().getResources().getDrawable(R.drawable.ic__3_cons_exist),FragmentoConsulta.newInstance(null,FragmentoConsulta.TIPO_EXISTENCIA),FragmentoConsulta.TAG));
        items.add(new Item(getString(R.string.taskbar_consulta_posicion),getContext().getResources().getDrawable(R.drawable.ic__5_cons_posici_n),FragmentoConsulta.newInstance(null,FragmentoConsulta.TIPO_POSICION),FragmentoConsulta.TAG));
        //items.add(new Item(getString(R.string.taskbar_consulta_referencia),getContext().getResources().getDrawable(R.drawable.ic__4_cons_referencia),FragmentoConsulta.newInstance(null,FragmentoConsulta.TIPO_REFERENCIA),FragmentoConsulta.TAG));
        /*items.add(new Item(getString(R.string.taskbar_impresion),getContext().getResources().getDrawable(R.drawable.ic_impresion_producto), FragmentoImpresionesPorArticulo.newInstance(null, null, new FragmentoImpresionesPorArticulo.interfazImpresiones() {
            @Override
            public void taskbarGoneButton() {
                toggle();
                toggle();
            }
        }),FragmentoConsulta.TAG));
        items.add(new Item(getString(R.string.taskbar_impresion_empaque),getContext().getResources().getDrawable(R.drawable.ic_impresion_empaque), FragmentoImpresionEmpaque.newInstance("empaque", "", new FragmentoImpresionEmpaque.interfazImpresiones() {
            @Override
            public void taskbarGoneButton() {
                toggle();
                toggle();
            }
        }),FragmentoConsulta.TAG));
        items.add(new Item(getString(R.string.taskbar_impresion_contenedor),getContext().getResources().getDrawable(R.drawable.ic_impresion_contenedor), FragmentoImpresionEmpaque.newInstance("contenedor", "", new FragmentoImpresionEmpaque.interfazImpresiones() {
            @Override
            public void taskbarGoneButton() {
                toggle();
                toggle();
            }
        }),FragmentoConsulta.TAG));*/
    }


    public  void cargarRecycler(){
        binding.gridMenu.setLayoutManager(new GridLayoutManager(gridMenu.getContext(), 2, GridLayoutManager.VERTICAL, false));
        AdaptadorItem adaptadorItem = new AdaptadorItem(items, new AdaptadorItem.taskbarInterface() {
            @Override
            public void abrirFragmento(final Fragment fragment, String TAG) {
                FragmentManager transaction= getActivity().getSupportFragmentManager();
                /*
                if (transaction.findFragmentByTag(TAG)!=null){
                    transaction.beginTransaction().remove(transaction.findFragmentByTag(TAG)).commit();
                }
                */
                transaction.beginTransaction().replace(binding.vistaFragmento.getId(),fragment,TAG).runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment instanceof FragmentoImpresionesPorArticulo){
                            binding.BotonDer.setVisibility(View.VISIBLE);
                            binding.BotonDer.setText(getString(R.string.taskbar_axc_imprimir));
                        }else{
                            binding.BotonDer.setVisibility(View.GONE);
                        }
                    }
                })
                        .commit();

                toggle();
            }

            @Override
            public void fragmentoAbierto(boolean estado) {
            }


        });
        binding.gridMenu.setAdapter(adaptadorItem);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof frgmnt_taskbar_AXC.interfazTaskbar)
        {
            taskbar = (frgmnt_taskbar_AXC.interfazTaskbar) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        taskbar= null;
    }


    public void cambiarResources(String TIPO){
        resourcePantalla=TIPO;
        if (bToggle){
            binding.BotonDer.setVisibility(View.GONE);
        }else {
            switch (TIPO){
                case REGISTRAR:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_registrar));
                    break;
                case RECEPCION:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_recibir));
                    break;
                case SELECCIONAR:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_seleccionar));
                    break;
                case SIGUIENTE:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_siguiente));
                    break;
                case CERRAR_TARIMA:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_cerrar_tarima));
                    break;
                case CERRAR_REEMPAQUE:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_cerrar_reempaque));
                    break;
                case CERRAR_CARRITO:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_cerrar_carrito));
                    break;
                case REBASTECIMIENTO:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_rebastecimiento));
                    break;
                case TOMAR_INVENTARIO:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_tomar));
                    break;
                case REGISTRO:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_registro));
                    break;
                case TERMINAR:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_terminar));
                    break;
                case IMPRIMIR:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText(getString(R.string.taskbar_axc_imprimir));
                    break;
                case VALIDAR:
                    binding.BotonDer.setVisibility(View.VISIBLE);
                    binding.BotonDer.setText("VALIDAR");
                    break;
                default:
                    binding.BotonDer.setVisibility(View.GONE);
                    break;
            }
            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(FragmentoConsulta.TAG);
            if (fragment instanceof FragmentoImpresionesPorArticulo){
                binding.BotonDer.setVisibility(View.VISIBLE);
                binding.BotonDer.setText(getString(R.string.taskbar_axc_imprimir));
            }
        }
    }






    public void onBackPressed()
    {
        if (!toggle()){
            return;
        }else {
            toggle();
        }
        if (getActivity().getSupportFragmentManager().findFragmentByTag("fragmentoConsulta")!= null){
            cerrarFragmento();
            return;
        }

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount()>0){
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        getActivity().overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);

        if(regresarMnuPrincipal)
        {
            Intent intent = new Intent(getActivity(), Inicio_Menu_Dinamico.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else
        {
            getActivity().finish();
        }
        getActivity().overridePendingTransition(R.anim.slide_left_in_close,R.anim.slide_left_out_close);

    }


    public void abrirFragmentoDesdeActividad(final Fragment fragment, final String TAG){
        toggle();
        binding.gridMenu.setClickable(false);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager transaction= getActivity().getSupportFragmentManager();
                if (transaction.findFragmentByTag(TAG)!=null){
                    transaction.beginTransaction().remove(transaction.findFragmentByTag(TAG)).commit();
                }
                transaction.beginTransaction().replace(binding.vistaFragmento.getId(),fragment,TAG)
                        .commit();

                toggle();
                binding.gridMenu.setClickable(true);
            }

        }, 300);

    }

    public void cerrarFragmento() {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(FragmentoConsulta.TAG);
        if (fragment != null) {
            try {
                if (fragment instanceof FragmentoImpresionEmpaque){
                  ((FragmentoImpresionEmpaque) fragment).cerrarFragmento();
                }
                if (fragment instanceof FragmentoImpresionesPorArticulo){
                    ((FragmentoImpresionesPorArticulo) fragment).cerrarFragmento();
                }
                if (fragment instanceof FragmentoConsulta){
                    ((FragmentoConsulta) fragment).cerrarFragmento();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

          cambiarResources(resourcePantalla);
            //getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


    public interface interfazTaskbar{
        void BotonDerecha();
        void BotonIzquierda();
    }
}


/*
*






        if (getActivity().getSupportFragmentManager().findFragmentByTag("FragmentoMenu")!=null)
        {
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        if (!toggle())
        {
            return;
        } else
            {
            toggle();
        }
        if (getActivity().getSupportFragmentManager().findFragmentByTag("fragmentoConsulta") != null)
        {
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return;
        }

        if(regresarMnuPrincipal)
        {
            Intent intent = new Intent(getActivity(), Inicio_Menu_Dinamico.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_left_in_close, R.anim.slide_left_out_close);
        }else
        {
            getActivity().finish();
        }
*
* */