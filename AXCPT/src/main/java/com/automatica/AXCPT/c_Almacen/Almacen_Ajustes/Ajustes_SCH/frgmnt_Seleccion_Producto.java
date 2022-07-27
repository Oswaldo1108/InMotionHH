package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorImagenes;
import com.automatica.AXCPT.Fragmentos.FragmentoAumentarVista;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.databinding.FragmentFragmentoAumentarVistaBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adUtilidades;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Almacen;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.views.CustomArrayAdapter;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class frgmnt_Seleccion_Producto extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String[] paramArr = null;
    private String str_TipoConsulta;
    private static final String frgtag_ImpEtiqueta= "FRGIMP";
    String RutaImagen;
    String Producto;

    private OnFragmentInteractionListener mListener;

    RecyclerView imagen;

    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";

    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle;
    private ImageButton btn_Back;
    private Spinner spnr_Productos;

    ConstraintLayout pantalla;
    private Button btn_ElegirLote;

    private EditText edtx_Producto;

    RadioGroup rdgrp_Modulos;
    public frgmnt_Seleccion_Producto()
    {

    }

    public static Fragment newInstance(String[] param, String TipoConsulta,OnFragmentInteractionListener mListener)
    {

        frgmnt_Seleccion_Producto fragment = new frgmnt_Seleccion_Producto();
        fragment.mListener=mListener;
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,param);
        args.putString(ARG_PARAM2,TipoConsulta);

        fragment.setArguments(args);
        return fragment;
    }


    public static Fragment newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Seleccion_Producto fragment = new frgmnt_Seleccion_Producto();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,param);
        args.putString(ARG_PARAM2,TipoConsulta);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                paramArr= getArguments().getStringArray(ARG_PARAM1);
                str_TipoConsulta= getArguments().getString(ARG_PARAM2);
                Log.i("ARGUMENTOSPALLETDET", "HOLA "+str_TipoConsulta);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_seleccion_producto, container, false);
        DeclaraVariables(view);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (paramArr!=null){
            if (!TextUtils.isEmpty(paramArr[0])){
                edtx_Producto.setText(paramArr[0]);
                new SegundoPlano("ConsultaProductos").execute(edtx_Producto.getText().toString().trim());
            }
        }

    }

    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle= view.findViewById(R.id.txtv_Incidencia);

        txtv_Detalle.setText(getString(R.string.seleccion_producto));
        if (str_TipoConsulta.equals("")){
            txtv_Titulo.setText("");
        }else{
            txtv_Titulo.setText(getString(R.string.partida) + str_TipoConsulta);
        }
        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        btn_Back.setVisibility(View.VISIBLE);
        imagen = view.findViewById(R.id.Imagen);
        rdgrp_Modulos = view.findViewById(R.id.radioGroup);
        btn_ElegirLote = view.findViewById(R.id.btn_ElegirLote);

        pantalla = view.findViewById(R.id.fl_Oscuridad);
        edtx_Producto = view.findViewById(R.id.edtx_Producto);

        spnr_Productos = view.findViewById(R.id.spinner);


        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Fragment fragment=getActivity().getSupportFragmentManager().findFragmentByTag("ElegirProducto");
               getActivity().getSupportFragmentManager().popBackStackImmediate();
               if (fragment!=null){
                   getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            }
        });



        edtx_Producto.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {

                if(edtx_Producto.getText().toString().equals(""))
                    {
                        new popUpGenerico(getActivity(),null, getString(R.string.msg_ingresa_producto), false, true, false);
                        return false;
                    }

                new SegundoPlano("ConsultaProductos").execute(edtx_Producto.getText().toString().trim());
                new esconderTeclado(getActivity());

                return false;
            }
        });



        btn_ElegirLote.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if(spnr_Productos.getAdapter() == null)
                        {
                            return;
                        }
                    new esconderTeclado(getActivity());

                    mListener.ProductoElegido(str_TipoConsulta,spnr_Productos.getSelectedItem().toString(),((Constructor_Dato)spnr_Productos.getSelectedItem()).getDato());

                    Fragment fragment=getActivity().getSupportFragmentManager().findFragmentByTag("ElegirProducto");
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                    if (fragment!=null){
                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }

                }
            });

        spnr_Productos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RutaImagen = ((Constructor_Dato)parent.getSelectedItem()).getTag2();
                Producto = ((Constructor_Dato)parent.getSelectedItem()).getDato();
                if (!TextUtils.isEmpty(Producto)){
                    try {
                        new SegundoPlano("CargarFotos").execute();
                    } catch (Exception e) {
                        Log.i("Error",e.toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.i("HOLA", "OnATach");
        edtx_Producto.requestFocus();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            {
                mListener = (OnFragmentInteractionListener) context;
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

        boolean ActivaProgressBar(Boolean estado);
        void ProductoElegido(String Partida, String prmProductoITEM, String prmProducto);

    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        cAccesoADatos_Almacen ca = new cAccesoADatos_Almacen(getContext());
        DataAccessObject dao;
        String tarea;
        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
            //    mListener.ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            try
                {
                    if(!this.isCancelled())
                        {
                            switch (tarea)
                                {

                                    case "ConsultaProductos":
                                        String prod = "@";

                                        if(params!=null)
                                            {
                                                prod = params[0];
                                            }
                                        dao = ca.c_ConsultaCoincidenciaArticulo(prod);
                                        break;
                                    case "CargarFotos":
                                      //  dao = ca.c_FotoNumParte(Producto);
                                        break;
                                     default:
                                         dao = new DataAccessObject(false,"Operacion no soportada.",null);
                                     break;

                                }
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                  dao = new DataAccessObject(false,e.getMessage(),null);
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
                {
                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {
                                    case "ConsultaProductos":
                                        spnr_Productos.setAdapter(new CustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item , dao.getcTablasSorteadas("NumParte","NumParte","RutaImagen")));

                                        break;
                                    case "CargarFotos":
/*
                                        try {
                                            //imagen.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                                            if (dao.getcTablas()!=null){
                                                imagen.setLayoutManager(new LinearLayoutManager(getContext()));
                                                imagen.setAdapter(new AdaptadorImagenes(dao.getcTablasSorteadas("imagen","imagen"), (AppCompatActivity) getActivity()));
                                            }else {
                                                imagen.setAdapter(null);
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }*/
                                        break;

                                    default:
                                           new popUpGenerico(getContext(),null, "Actividad no soportada. " + tarea,false ,true , true);
                                        break;
                                }
                        }else
                        {
                            new popUpGenerico(getContext(), null, dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                    mListener.ActivaProgressBar(false);

                }catch (Exception e)
                {
                    e.printStackTrace();
               //     new popUpGenerico(getContext(),null, e.getMessage(),dao.iscEstado(), true, true);
                }

            //swipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }
    /*
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {

            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                URL url = new URL(urldisplay);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String input;
                StringBuffer stringBuffer = new StringBuffer();
                while ((input = in.readLine()) != null)
                {
                    if (input.contains("A HREF")){
                        stringBuffer.append(input);
                        stringBuffer.append("\n");
                    }

                }
                in.close();

                String htmlData = stringBuffer.toString();

                Html.fromHtml(htmlData);
                Log.i("HTML", htmlData);

                InputStream in2 = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in2);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
     */
}
