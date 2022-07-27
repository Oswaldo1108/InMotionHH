package com.automatica.AXCPT.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorRecyclerNotificaciones;
import com.automatica.AXCPT.Principal.Inicio_Menu_Dinamico;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.SQLite.dbNotificaciones;
import com.automatica.AXCPT.c_Almacen.Almacen_Menu;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.Alm_Registro_Seleccion_Lote;
import com.automatica.AXCPT.c_ArmadoPalletsRegProduccion.Almacen_Armado_Pallets.RegistroPT_Menu;
import com.automatica.AXCPT.c_Embarques.Embarques_Embarque;
import com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Surtido.Surtido_Seleccion_Partida;
import com.automatica.AXCPT.c_Inventarios.Menus.Inventarios_Menu;
import com.automatica.AXCPT.c_Recepcion.Rec_Registro_Seleccion_Partida;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.Objects;

public class FragNotificaciones extends Fragment{
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    RecyclerView recycler;
    TextView titulo,subtitulo;
    ConstraintLayout cl_header;
    dbNotificaciones dbNotificaciones;

    public static FragNotificaciones newInstance() {
        FragNotificaciones fragment = new FragNotificaciones();
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (deltaX < MIN_DISTANCE && deltaX!=0)
                        {
                            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                            if (getFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                            }
                        }
                        break;
                }
                return false;
            }
        });

        recycler= view.findViewById(R.id.recycler);

        cl_header= view.findViewById(R.id.cl_header);
        titulo= cl_header.findViewById(R.id.txtv_Incidencia);
        subtitulo= cl_header.findViewById(R.id.txtv_Titulo);
        titulo.setText("Notificaciones");
        subtitulo.setText("Buzón");
        try{
            dbNotificaciones = new dbNotificaciones(getContext());
            //recycler.setHasFixedSize(false);
            LinearLayoutManager manager= new LinearLayoutManager(getContext());
            recycler.setLayoutManager(manager);
            recycler.setAdapter(new AdaptadorRecyclerNotificaciones(new dbNotificaciones(getContext()).qryTablaNotificaciones(), new AdaptadorRecyclerNotificaciones.onItemClickListener() {
                @Override
                public void onItemClick(Constructor_Dato dato) {
                    onclickEvent(dato);
                }
            }));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragmento_notificaciones, container, false);
        }

        public void onclickEvent(Constructor_Dato dato){
            Context context= getContext();
            String IDWS= dato.getTag2();
            String Documento= dato.getTag3();

            switch (dato.getDato()){
                case "1":
                    Intent ToMenu = new Intent(context, Inicio_Menu_Dinamico.class);
                    ToMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dbNotificaciones.actualizarAviso(IDWS);
                    Objects.requireNonNull(context).startActivity(ToMenu);
                    if (getFragmentManager() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                    }
                    break;
                case "2":
                    Intent ToAlmacen = new Intent(context, Almacen_Menu.class);
                    ToAlmacen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dbNotificaciones.actualizarAviso(IDWS);
                    Objects.requireNonNull(context).startActivity(ToAlmacen);
                    if (getFragmentManager() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                    }

                    break;

                case "4":
                    Intent ToSurtido = new Intent(context, Surtido_Seleccion_Partida.class);
                    ToSurtido.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dbNotificaciones.actualizarAviso(IDWS);
                    ToSurtido.putExtra("Documento",Documento);
                    Objects.requireNonNull(context).startActivity(ToSurtido);
                    if (getFragmentManager() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                    }
                    break;
                case "7":
                    Intent ToEmbarques = new Intent(context, Embarques_Embarque.class);
                    ToEmbarques.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dbNotificaciones.actualizarAviso(IDWS);
                    Objects.requireNonNull(context).startActivity(ToEmbarques);
                    if (getFragmentManager() != null) {
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                    }
                    break;
                case "8":
                    Intent ToInventarios = new Intent(context, Inventarios_Menu.class);
                    ToInventarios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dbNotificaciones.actualizarAviso(IDWS);
                    Objects.requireNonNull(context).startActivity(ToInventarios);
                    if (getFragmentManager() != null) {
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                    }
                    break;
                case "9":
                    Intent ToEnvios = new Intent(context, Alm_Registro_Seleccion_Lote.class);
                    ToEnvios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ToEnvios.putExtra("Documento",Documento);
                    Log.i("Documento",Documento);
                    dbNotificaciones.actualizarAviso(IDWS);
                    Objects.requireNonNull(context).startActivity(ToEnvios);
                    if (getFragmentManager() != null) {
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                    }
                    break;
                case "10":
                    Intent ToRegistro = new Intent(context, RegistroPT_Menu.class);
                    ToRegistro.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dbNotificaciones.actualizarAviso(IDWS);
                    ToRegistro.putExtra("Documento",Documento);
                    Log.i("Documento",Documento);
                    Objects.requireNonNull(context).startActivity(ToRegistro);
                    break;
                case "11":
                    Intent ToRecepcion = new Intent(context, Rec_Registro_Seleccion_Partida.class);
                    ToRecepcion.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dbNotificaciones.actualizarAviso(IDWS);
                    ToRecepcion.putExtra("Documento",Documento);
                    Log.i("Documento",Documento);
                    Objects.requireNonNull(context).startActivity(ToRecepcion);
                    break;
                default:
                    Log.i("Error", String.valueOf(dato.getDato()));
                    if (getFragmentManager() != null) {
                        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().remove(Objects.requireNonNull(getFragmentManager().findFragmentByTag("FragmentoNoti")));
                    }                    break;
            }
            recycler.setAdapter(new AdaptadorRecyclerNotificaciones(new dbNotificaciones(getContext()).qryTablaNotificaciones(), new AdaptadorRecyclerNotificaciones.onItemClickListener() {
                @Override
                public void onItemClick(Constructor_Dato dato) {
                    onclickEvent(dato);
                }
            }));
        }

    @Override
    public void onResume() {
        super.onResume();
        recycler.setAdapter(new AdaptadorRecyclerNotificaciones(new dbNotificaciones(getContext()).qryTablaNotificaciones(), new AdaptadorRecyclerNotificaciones.onItemClickListener() {
            @Override
            public void onItemClick(Constructor_Dato dato) {
                onclickEvent(dato);
            }
        }));
    }

    public void MandarMensaje(int Cantidad) {
        if (subtitulo!=null){
            if (Cantidad>0){
                subtitulo.setText("Notificaciónes nuevas: "+Cantidad);
            }else {
                subtitulo.setText("Buzón");
            }
        }
    }

    public void resetearRecycler(){
        recycler.setAdapter(new AdaptadorRecyclerNotificaciones(new dbNotificaciones(getContext()).qryTablaNotificaciones(), new AdaptadorRecyclerNotificaciones.onItemClickListener() {
            @Override
            public void onItemClick(Constructor_Dato dato) {
                onclickEvent(dato);
            }
        }));
    }
}
