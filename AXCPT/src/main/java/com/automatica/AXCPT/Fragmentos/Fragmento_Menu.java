package com.automatica.AXCPT.Fragmentos;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.ReabastecimientoPicking.ReabPK_Seleccion_Material;
import com.automatica.AXCPT.Validacion.ValidacionCalidad;
import com.automatica.AXCPT.Validacion.ValidacionIngreso;
import com.automatica.AXCPT.Validacion.ValidarColocar;
import com.automatica.AXCPT.c_Almacen.Almacen.Colocar;
import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorMenu;
import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorMenuInventarios;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.c_Almacen.Almacen.Reubicar_Menu;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Almacen_Ajustes_Menu;
import com.automatica.AXCPT.c_Almacen.Cuarentena.MenuNuevo;
import com.automatica.AXCPT.c_Almacen.Devolucion.DevolucionEmpaque;
import com.automatica.AXCPT.c_Almacen.Devolucion.SeleccionOrdenDevolucion;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Almacen_Armado_Pallets_Liquidos;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Almacen_Armado_Pallets_Liquidos_CantidadModificable;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Almacen_Armado_Pallets_Liquidos_PyU;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.Almacen_Armado_Pallets_Ne;
import com.automatica.AXCPT.c_Embarques.CancelacionEmbarque;
import com.automatica.AXCPT.c_Embarques.Reempaque.Reempaque_Ciesa.Reempaque_Seleccion_Orden;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido_Armado_Pallets.Surtido_Seleccion_Orden;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion.Validacion_Seleccion_Orden;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_PantallaPrincipal;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_PorPosicion;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_PorProductoMulti;
import com.automatica.AXCPT.c_Produccion.Produccion.Almacen_Armado_Pallets;
import com.automatica.AXCPT.c_Produccion.Produccion.Almacen_Armado_Pallets_NE;
import com.automatica.AXCPT.c_Produccion.Produccion.Almacen_Armado_Pallets_PyU;
import com.automatica.AXCPT.c_Produccion.Produccion.SeleccionarOrdenProd;
import com.automatica.AXCPT.c_Produccion.Surtido.RechazarSurtido;
import com.automatica.AXCPT.c_Produccion.Surtido.ReempaqueProd;
import com.automatica.AXCPT.c_Produccion.Surtido.SurtidoProdSeleccionOrden;
import com.automatica.AXCPT.c_Produccion.Surtido.ValidarOrdenSurtido;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionPegadoEtiqueta;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionar;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionSeleccionarHC;
import com.automatica.AXCPT.c_Traspasos.MenuTraspaso;
import com.automatica.AXCPT.databinding.FragmentFragmentoMenuBinding;
import com.automatica.AXCPT.objetos.objetoMenu;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class Fragmento_Menu extends Fragment{

    FragmentFragmentoMenuBinding binding;
    View vista;
    View pantallaNegra;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragmento_Menu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragmento_Menu.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragmento_Menu newInstance(String param1, String param2) {
        Fragmento_Menu fragment = new Fragmento_Menu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    CardView CardRecepcion, CardConsultas, CardAlmacen, CardSurtido, CardInventarios,CardProduccion;
    View toolbarIcon;
    RecyclerView recyclerRecepcion;
    Toolbar toolbar;
    LinearLayout pantallaMenu;
    ScrollView scrollView2;

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
        binding = FragmentFragmentoMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vista = view;
        pantallaNegra = view.findViewById(R.id.pantallaNegra);
        //toggle();
        getViews(view);
        setListeners(view);

    }

    private void toggle() {
        Transition transition = new Fade();
        /*
        if (pantallaNegra.getVisibility()==View.GONE){
             transition = new Slide(Gravity.START);
        }else {
             transition = new Slide(Gravity.END);
        }
        */
        transition.setDuration(200);
        transition.addTarget(R.id.pantallaNegra);

        TransitionManager.beginDelayedTransition((ViewGroup) vista.getRootView(),transition);
        if (pantallaNegra.getVisibility()==View.VISIBLE){
            pantallaNegra.setVisibility(View.GONE);
        }else {
            pantallaNegra.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                toggle();
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return anim;

    }

    public void removeMenu(){
        if (getParentFragmentManager().findFragmentByTag("FragmentoMenu")!=null){
            Fragment fragment = getParentFragmentManager().findFragmentByTag("FragmentoMenu");
            getParentFragmentManager().popBackStack();
            //getParentFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
    public void setListeners(final View view) {

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMenu();
            }
        });


        //--------------------------------------------MENÚS EN USO INMOTION--------------------------------------------------------------------
        CardRecepcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerRecepcion = view.findViewById(R.id.recyclerRecepcion);
                if (recyclerRecepcion.getVisibility() == View.GONE){
                    ArrayList<objetoMenu> datos = new ArrayList<>();
                    //datos.add(new objetoMenu(getString(R.string.menu_conteo), new Intent(getContext(), RecepcionSeleccionarHC.class)));
                    datos.add(new objetoMenu(getString(R.string.menu_recepcion), new Intent(getContext(), RecepcionSeleccionar.class)));
                    datos.add(new objetoMenu(getString(R.string.menu_pegado_etiquetas), new Intent(getContext(), RecepcionPegadoEtiqueta.class)));
                    //datos.add(new objetoMenu(getString(R.string.menu_validacion_calidad), new Intent(getContext(), ValidacionCalidad.class)));
                 //   datos.add(new objetoMenu(getString(R.string.almacen_armado_tarimas_PyU), new Intent(getContext(), ValidacionCalidad.class)));
                    llenarRecycler(recyclerRecepcion, datos);
                    recyclerRecepcion.setVisibility(View.VISIBLE);
                }else{
                    recyclerRecepcion.setVisibility(View.GONE);
                }
            }
        });

        CardAlmacen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerAlmacen);
                if (recyclerView.getVisibility() == View.GONE) {
                    ArrayList<objetoMenu> datos = new ArrayList<>();
                    datos.add(new objetoMenu(getString(R.string.menu_colocacion),new Intent(getContext(), ValidarColocar.class)));
                    datos.add(new objetoMenu(getString(R.string.menu_reubicacion),new Intent(getContext(), Reubicar_Menu.class)));
                    datos.add(new objetoMenu(getString(R.string.menu_mov_misc),new Intent(getContext(), Almacen_Ajustes_Menu.class)));
                    //datos.add(new objetoMenu("Devolución",new Intent(getContext(), Seleccion_DevolucionPT.class)));
                    //datos.add(new objetoMenu("Validación",new Intent(getContext(), ValidacionIngreso.class)));
                    //datos.add(new objetoMenu( getString(R.string.menu_reabastimiento),new Intent(getContext(), ReabPK_Seleccion_Material.class)));
                    //datos.add(new objetoMenu(getString(R.string.menu_cuarentena),new Intent(getContext(), MenuNuevo.class)));
                    //datos.add(new objetoMenu(getString(R.string.recepcion_recepcion_por_empaque_traspasos), new Intent(getContext(), MenuTraspaso.class)));
                    //datos.add(new objetoMenu("Devolución", new Intent(getContext(), SeleccionOrdenDevolucion.class)));
                    llenarRecycler(recyclerView, datos);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        CardSurtido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerSurtido);
                if (recyclerView.getVisibility() == View.GONE) {
                    ArrayList<objetoMenu> datos = new ArrayList<>();
                    datos.add(new objetoMenu(getString(R.string.menu_embarques),new Intent(getContext(), Surtido_Seleccion_Orden.class)));
                    datos.add(new objetoMenu("Validación",new Intent(getContext(), Validacion_Seleccion_Orden.class)));
                    datos.add(new objetoMenu(getString(R.string.menu_reempaque),new Intent(getContext(), Reempaque_Seleccion_Orden.class)));
                    //   datos.add(new objetoMenu(getString(R.string.menu_validacion),new Intent(getContext(), Validacion_Seleccion_Orden.class)));
                    // datos.add(new objetoMenu(getString(R.string.menu_cancelacion),new Intent(getContext(), CancelacionEmbarque.class)));    //AQUI SE METE CANCELACIÓN
                    llenarRecycler(recyclerView, datos);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        CardInventarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerInventarios);
                if (recyclerView.getVisibility() == View.GONE) {
                    ArrayList<objetoMenu> datos = new ArrayList<>();
                    datos.add(new objetoMenu(getString(R.string.menu_inventario_posicion),new Intent(getContext(), Inventarios_PorPosicion.class),1));
                    datos.add(new objetoMenu(getString(R.string.menu_inventario_articulo),new Intent(getContext(), Inventarios_PorProductoMulti.class),2));
                    //datos.add(new objetoMenu(getString(R.string.menu_inventario_físico),new Intent(getContext(), Inventarios_PantallaPrincipal.class),3));
                    Log.i("Elementos", String.valueOf(datos.size()));
                    llenarRecyclerInventarios(recyclerView, datos);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        //-------------------------------------------- FIN DE MENÚS EN USO INMOTION--------------------------------------------------------------------

        //--------------------------------------------MENÚS OCULTOS INMOTION--------------------------------------------------------------------
        CardProduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = view.findViewById(R.id.recyclerProduccion);
                if (recyclerView.getVisibility() == View.GONE){
                    ArrayList<objetoMenu> datos = new ArrayList<>();
                    datos.add(new objetoMenu(getString(R.string.almacen_armado_tarimas), new Intent(getContext(), SeleccionarOrdenProd.class)));
                    datos.add(new objetoMenu(getString(R.string.menu_embarques),new Intent(getContext(), SurtidoProdSeleccionOrden.class)));
                    datos.add(new objetoMenu("Validación",new Intent(getContext(), ValidarOrdenSurtido.class)));
                    datos.add(new objetoMenu("Devolución",new Intent(getContext(), RechazarSurtido.class)));
                   // datos.add(new objetoMenu(getString(R.string.almacen_armado_tarimas), new Intent(getContext(), Almacen_Armado_Pallets_Liquidos.class)));
                    llenarRecycler(recyclerView, datos);
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });

        CardConsultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerConsultas = view.findViewById(R.id.recyclerConsultas);
                if (recyclerConsultas.getVisibility() == View.GONE) {
                    ArrayList<objetoMenu> datos = new ArrayList<>();
                    datos.add(new objetoMenu(getString(R.string.menu_reabastimiento),new Intent(getContext(), ReabPK_Seleccion_Material.class)));
                    llenarRecycler(recyclerConsultas, datos);
                    recyclerConsultas.setVisibility(View.VISIBLE);
                } else {
                    recyclerConsultas.setVisibility(View.GONE);
                }
            }
        });
        //--------------------------------------------FIN DE MENÚS OCULTOS INMOTION--------------------------------------------------------------------

        binding.pantallaNegra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMenu();
            }
        });
    }

    public void getViews(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Menú");
        pantallaMenu = view.findViewById(R.id.pantallaMenu);
        toolbarIcon = getToolbarLogoIcon(toolbar);
        CardRecepcion = view.findViewById(R.id.CardRecepcion);
        CardConsultas = view.findViewById(R.id.CardConsultas);
        CardAlmacen = view.findViewById(R.id.CardAlmacen);
        CardSurtido = view.findViewById(R.id.CardSurtido);
        CardInventarios = view.findViewById(R.id.CardInventarios);
        CardProduccion= view.findViewById(R.id.CardProduccion);
        scrollView2= view.findViewById(R.id.scrollView2);
    }

    public static View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }

    public void llenarRecycler(RecyclerView recyclerView, ArrayList<objetoMenu> menu) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdaptadorMenu(menu, getContext()));
        ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        int sizeStack = am.getRunningTasks(5).size();
        for (int i = 0; i < sizeStack; i++) {
            ComponentName cn = am.getRunningTasks(10).get(i).topActivity;
            Log.d("Act" + i, cn.getClassName());
        }
    }
    public void llenarRecyclerInventarios(RecyclerView recyclerView, ArrayList<objetoMenu> menu) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdaptadorMenuInventarios(menu, getContext()));
        ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        int sizeStack = am.getRunningTasks(5).size();
        for (int i = 0; i < sizeStack; i++) {
            ComponentName cn = am.getRunningTasks(10).get(i).topActivity;
            Log.d("Act" + i, cn.getClassName());
        }
    }




    @Override
    public void onDestroyView() {
        vista.findViewById(R.id.pantallaNegra).setVisibility(View.GONE);
        super.onDestroyView();
    }


}