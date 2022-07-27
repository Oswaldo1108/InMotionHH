package com.automatica.AXCPT.ReabastecimientoPicking;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import com.automatica.axc_lib.views.CustomArrayAdapter;

public class frgmnt_Reab_Empaques extends Fragment
{
    private static final String frgtag_ControladorSurtido = "frgtag_ControladorSurtido";
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR QUE CONSULTA DE DOCUMENTO SE HARA
    private static final String ARG_PARAM2 = "param2";

    TextView txtv_Pallet_Producto,txtv_Pallet_Cantidad,edtx_Lote;
    private String documento;
    private String str_TipoConsulta;

    private EditText edtx_Empaque, edtx_Posicion;



    private ImageButton imgb_Buscar;

    private Button btn_Transferencia,btn_Liberar,btn_Cancelar;
    private Spinner sp_AlmacenOrigen,sp_AlmacenDestino;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle;


    public frgmnt_Reab_Empaques()
    {
    }
    public static frgmnt_Reab_Empaques newInstance(String param1, String param2)
    {
        frgmnt_Reab_Empaques fragment = new frgmnt_Reab_Empaques();
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
                documento = getArguments().getString(ARG_PARAM1);
                str_TipoConsulta = getArguments().getString(ARG_PARAM2);
                Log.i("ARGUmENTOS", str_TipoConsulta + " " + documento);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.reabastece_surtido_empaque, container, false);

        edtx_Empaque = (EditText) view.findViewById(R.id.edtx_Empaque);
        edtx_Posicion = (EditText) view.findViewById(R.id.edtx_Posicion);


        edtx_Lote= view.findViewById(R.id.edtx_Lote);
        txtv_Pallet_Producto = (TextView) view.findViewById(R.id.txtv_Pallet_Producto);
        txtv_Pallet_Cantidad = (TextView) view.findViewById(R.id.txtv_Pallet_Cantidad);

        edtx_Empaque.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(edtx_Empaque.getText().equals(""))
                    {
                        new popUpGenerico(getContext(),null, "Ingrese código de empaque.", false, true, true);
                        edtx_Empaque.requestFocus();
                        return false;
                    }

                new SegundoPlano("ConsultaMaterial").execute();

                return false;
            }
        });

        edtx_Posicion.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(edtx_Empaque.getText().equals(""))
                    {
                        new popUpGenerico(getContext(),null, "Ingrese código de empaque.", false, true, true);
                        edtx_Empaque.requestFocus();
                        return false;
                    }
                if(edtx_Posicion.getText().equals(""))
                    {
                        new popUpGenerico(getContext(),null, "Ingrese código de posición.", false, true, true);
                        edtx_Posicion.requestFocus();
                        return false;
                    }

                new SegundoPlano("RegistroReabastecimiento").execute();

                return false;
            }
        });
        return view;
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
        return edtx_Empaque.getText().toString();
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
                            edtx_Posicion.getText().toString(),
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


                                    case "ConsultaMaterial":

                                        dao = ca.c_ConsultaEmpaqueReabastece(documento,edtx_Empaque.getText().toString());

                                        break;

                                    case "RegistroReabastecimiento":

                                        dao = ca.c_ReabasteceEmpaque(documento,edtx_Empaque.getText().toString(),edtx_Posicion.getText().toString());

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
                                    case "ConsultaMaterial":
                                        txtv_Pallet_Cantidad .setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_Pallet_Producto .setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        edtx_Lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProv"));

                                        edtx_Posicion.requestFocus();

                                        break;

                                    case "RegistroReabastecimiento":


                                        ((frgmnt_ControlSurtidos) (getActivity().getSupportFragmentManager().findFragmentByTag(frgtag_ControladorSurtido))).ActualizarDocumento();



                                        new popUpGenerico(getContext(),null,"Reabastecimiento registrado con éxito.", dao.iscEstado(), true, true);

                                        edtx_Empaque.setText("");
                                        edtx_Posicion.setText("");

                                        txtv_Pallet_Cantidad.setText("");
                                        txtv_Pallet_Producto.setText("");

                                        break;

                                }
                        }else
                        {
                            new popUpGenerico(getContext(),null, dao.getcMensaje(), dao.iscEstado(), true, true);
                            edtx_Empaque.setText("");
                            edtx_Posicion.setText("");

                            txtv_Pallet_Cantidad.setText("");
                            txtv_Pallet_Producto.setText("");
                            edtx_Lote.setText("");
                            edtx_Empaque.requestFocus();

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
