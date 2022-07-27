package com.automatica.AXCPT.ReabastecimientoPicking.SinOrden;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.ReabastecimientoPicking.frgmnt_ControlSurtidos;
import com.automatica.AXCPT.ReabastecimientoPicking.frgmnt_Reab_Piezas;
import com.automatica.AXCPT.databinding.ReabasteceSurtidoPiezasBinding;
import com.automatica.AXCPT.databinding.ReabasteceSurtidoPiezasSoBinding;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import com.automatica.axc_lib.views.CustomArrayAdapter;

public class frgmnt_Reab_Piezas_SO extends Fragment
{
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR QUE CONSULTA DE DOCUMENTO SE HARA
    private static final String ARG_PARAM2 = "param2";


    private String str_Modulo;
    private String str_TipoConsulta;
    ReabasteceSurtidoPiezasSoBinding binding;

    private EditText edtx_Transferencia, edtx_Referencia;



    private ImageButton imgb_Buscar;

    private Button btn_Transferencia,btn_Liberar,btn_Cancelar;
    private Spinner sp_AlmacenOrigen,sp_AlmacenDestino;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle;


    public frgmnt_Reab_Piezas_SO()
    {
    }
    public static frgmnt_Reab_Piezas_SO newInstance(String param1, String param2)
    {
        frgmnt_Reab_Piezas_SO fragment = new frgmnt_Reab_Piezas_SO();
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
                str_Modulo = getArguments().getString(ARG_PARAM1);
                str_TipoConsulta = getArguments().getString(ARG_PARAM2);
                Log.i("ARGUmENTOS", str_TipoConsulta + " " + str_Modulo);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.reabastece_surtido_piezas_so, container, false);
//        txtv_Titulo = view.findViewById(R.id.txtv_Incidencia);
//        txtv_Detalle = view.findViewById(R.id.txtv_Titulo);



//
//        edtx_Referencia.setOnKeyListener(new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event)
//            {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
//                    {
//                        new esconderTeclado(getActivity());
//                    }
//                return false;
//            }
//        });
//
//
//        edtx_Transferencia.setOnKeyListener(new View.OnKeyListener()
//        {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event)
//            {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
//                    {
//                        new esconderTeclado(getActivity());
//                    }
//                    return false;
//            }
//        });
//
//
//
//
//        imgb_Buscar.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                if(edtx_Transferencia.getText().toString().equals(""))
//                    {
//                        new popUpGenerico(getContext(),null, "Ingrese una transferencia.", false, true, true);
//                        return;
//                    }
//
//                mListener.BuscarTransfer();
//            }
//        });
//
//
//
//        btn_Transferencia.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//
//                if(edtx_Referencia.getText().toString().equals(""))
//                    {
//                        new popUpGenerico(getContext(),null, "Ingrese una referencia.", false, true, true);
//                        return;
//                    }
//
//                String Origen = ((CustomArrayAdapter) sp_AlmacenOrigen.getAdapter()).getSelectedExtra(sp_AlmacenOrigen.getSelectedItemPosition());
//                String Destino = ((CustomArrayAdapter) sp_AlmacenDestino.getAdapter()).getSelectedExtra(sp_AlmacenDestino.getSelectedItemPosition());
//
//                if(Origen.equals(Destino))
//                    {
//                        new popUpGenerico(getContext(),null, "No puede liberar un documento de transferencia de un almacén a si mismo.", false, true, true);
//                        return;
//                    }
//                new SegundoPlano("LiberarTransfer").execute(
//                        edtx_Transferencia.getText().toString(),
//                        edtx_Referencia.getText().toString());
//
//
//            }
//        });
//
//
//
//        btn_Liberar.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//
//                if(edtx_Referencia.getText().toString().equals(""))
//                    {
//                        new popUpGenerico(getContext(),null, "Ingrese una referencia.", false, true, true);
//                        return;
//                    }
//
//                String Origen = ((CustomArrayAdapter) sp_AlmacenOrigen.getAdapter()).getSelectedExtra(sp_AlmacenOrigen.getSelectedItemPosition());
//                String Destino = ((CustomArrayAdapter) sp_AlmacenDestino.getAdapter()).getSelectedExtra(sp_AlmacenDestino.getSelectedItemPosition());
//
//                if(Origen.equals(Destino))
//                    {
//                        new popUpGenerico(getContext(),null, "No puede liberar un documento de transferencia de un almacén a si mismo.", false, true, true);
//                        return;
//                    }
//                new SegundoPlano("LiberarTransferNuevo").execute(
//                        edtx_Transferencia.getText().toString(),
//                        edtx_Referencia.getText().toString());
//
//
//            }
//        });
//
//
//
//        btn_Cancelar.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//
//                edtx_Transferencia.setText("");
//                edtx_Referencia.setText("");
//                mListener.LimpiarPantalla();
//
//            }
//        });
//
//
//
//
//
//
//
//        new SegundoPlano("ListaAlmacenes").execute();
        binding= ReabasteceSurtidoPiezasSoBinding.inflate(inflater,null,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.edtxEmpaque.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxEmpaque.getText())){
                    new popUpGenerico(getContext(),getView(),"Llene el campo empaque",false,true,true);
                    binding.edtxEmpaque.requestFocus();
                    return false;
                }
                new SegundoPlano("ConsultarEmpaque").execute();
                return false;
            }
        });
        binding.edtxContenedor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(binding.edtxEmpaque.getText())){
                    new popUpGenerico(getContext(),getView(),"Llene el campo empaque",false,true,true);
                    binding.edtxEmpaque.requestFocus();
                    return false;
                }
                if (TextUtils.isEmpty(binding.edtxConfirmarEmpaque.getText())){
                    new popUpGenerico(getContext(),getView(),"Llene el campo confirmar empaque",false,true,true);
                    binding.edtxConfirmarEmpaque.requestFocus();
                    return false;
                }
                if (TextUtils.isEmpty(binding.edtxEmpaque2.getText())|| Integer.parseInt(binding.edtxEmpaque2.getText().toString())<0){
                    new popUpGenerico(getContext(),getView(),"Ingrese una cantidad valida",false,true,true);
                    binding.edtxEmpaque2.requestFocus();
                    return false;
                }
                if (TextUtils.isEmpty(binding.edtxContenedor.getText())){
                    new popUpGenerico(getContext(),getView(),"Llene el campo 'Contenedor'",false,true,true);
                    binding.edtxContenedor.requestFocus();
                    return false;
                }
                new SegundoPlano("ReabastecerPiezas").execute();
                return false;
            }
        });
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




    public String getDocumento()
    {
        return edtx_Transferencia.getText().toString();
    }

    /**
     *
     * REGRESA ALMACEN ORIGEN
     *
     * */
    public String getAlmacenOrigen()
    {
        return ((CustomArrayAdapter)sp_AlmacenOrigen.getAdapter()).getSelectedExtra(sp_AlmacenOrigen.getSelectedItemPosition());
    }






    /**
     *
     * 0 - Documento
     * 1 - Almacen Origen
     * 2 - Almacen Destino
     *
     * */
    public String[] getDatos()
    {
        return

                new String[]
                        {
                            edtx_Transferencia.getText().toString(),
                            ((CustomArrayAdapter)sp_AlmacenOrigen.getAdapter()).getSelectedExtra(sp_AlmacenOrigen.getSelectedItemPosition()),
                            ((CustomArrayAdapter)sp_AlmacenDestino.getAdapter()).getSelectedExtra(sp_AlmacenDestino.getSelectedItemPosition())
                        };
    }

    /**
     *
     *
     * Regresa Almacen Destino
     *
     * */
    public String getAlmacenDestino()
    {
        return ((CustomArrayAdapter)sp_AlmacenDestino.getAdapter()).getSelectedExtra(sp_AlmacenDestino.getSelectedItemPosition());
    }



    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {

        DataAccessObject dao;
        String tarea;
        View view;

        cAccesoADatos ca = new cAccesoADatos(getContext());

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
                mListener.ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(), false, true, true);
                }
        }

        @Override
        protected Void doInBackground(String...Params)
        {
            try
                {
                    if(!this.isCancelled())
                        {
                            switch (tarea)
                                {

                                    case "ConsultarEmpaque":
                                        dao = ca.c_ConsultaEmpaque(binding.edtxEmpaque.getText().toString());
                                        break;

                                    case "ReabastecerPiezas":
                                        dao= ca.c_ReabastecePiezasSO(binding.edtxEmpaque.getText().toString(),binding.edtxEmpaque2.getText().toString(),binding.edtxConfirmarEmpaque.getText().toString(),binding.edtxContenedor.getText().toString());
                                        break;
                                    case "LiberarTransfer":
                                        String Transfer = "0",Referencia = "SIN REFERENCIA";




                                        if(!Params[0].equals(""))
                                            {
                                                Transfer = Params[0];
                                            }
                                        if(Params[1]!=null&&Params[1]!="")
                                            {
                                                Referencia = Params[1];
                                            }

                                        dao = ca.c_CrearInterorgSalidaPT(Transfer,Referencia);

                                        break;

                                    case "ListaProductos":

                                        break;

                                    case "ListaAlmacenes":

                                        dao = ca.C_ListaAlmacenesMiAXC();

                                        break;
                                    case "LiberarTransferNuevo":
                                        dao = new DataAccessObject(true,"Transferencia Liberada para surtido con éxito.",null);
                                        break;
                                    default:
                                        dao = new DataAccessObject(false,"Operación no soportada",null);
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


                                    case "ReabastecerPiezas":
                                        new popUpGenerico(getContext(),null,"Reabastecimiento registrado con éxito.", dao.iscEstado(), true, true);

                                        binding.edtxEmpaque.setText("");
                                        binding.edtxConfirmarEmpaque.setText("");
                                        binding.edtxEmpaque2.setText("");
                                        binding.txtvPalletCantidad.setText("");
                                        binding.txtvEstatus.setText("");
                                        binding.txtvPalletProducto.setText("");
                                        binding.edtxContenedor.setText("");
                                        binding.txtvLote.setText("");

                                        break;

                                    case "ConsultarEmpaque":
                                        binding.txtvPalletProducto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                                        binding.txtvEstatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                                        binding.txtvPalletCantidad.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        binding.txtvLote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));

                                        binding.edtxEmpaque2.requestFocus();
                                        break;




                                    case "LiberarTransfer":

                                        edtx_Transferencia.setText(dao.getcMensaje());

                                        new popUpGenerico(getContext(),null,"Transferencia creada con éxito [" + dao.getcMensaje() + "]", dao.iscEstado(), true, true);


                                        break;

                                    case "LiberarTransferNuevo":

                                        edtx_Transferencia.setText("");
                                        edtx_Referencia.setText("");

//                                        mListener.LimpiarPantalla();
                                        new popUpGenerico(getContext(),null, dao.getcMensaje(), dao.iscEstado(), true, true);

                                        break;


                                    case "ListaAlmacenes":

                                        sp_AlmacenOrigen.setAdapter(new CustomArrayAdapter(getContext(),
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Almacen","Almacen")));

                                        sp_AlmacenDestino.setAdapter(new CustomArrayAdapter(getContext(),
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("Almacen","Almacen")));



                                        break;



                                }


//                            XmlSerializer






                        }else
                        {
                            new popUpGenerico(getContext(),null, dao.getcMensaje(), dao.iscEstado(), true, true);
                            binding.edtxEmpaque.setText("");
                            binding.edtxConfirmarEmpaque.setText("");
                            binding.edtxEmpaque2.setText("");
                            binding.txtvPalletCantidad.setText("");
                            binding.txtvEstatus.setText("");
                            binding.txtvPalletProducto.setText("");
                            binding.edtxContenedor.setText("");
                            binding.txtvLote.setText("");
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(),dao.iscEstado(), true, true);
                }

            mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(),null, "Cancelado", false, true, true);
            super.onCancelled();
        }
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);

    }




}
