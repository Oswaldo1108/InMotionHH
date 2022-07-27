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
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;

public class frgmnt_Reab_Pallet extends Fragment
{
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR QUE CONSULTA DE DOCUMENTO SE HARA
    private static final String ARG_PARAM2 = "param2";


    private String Documento;
    private String str_TipoConsulta;


    private Button btn_AgregarPartida;
    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    //VIEWS

    private EditText edtx_Pallet, edtx_Posicion;

    private TextView txtv_Prod, txtv_Cant, txtv_Empaques,txtv_lote;


    public frgmnt_Reab_Pallet()
    {
    }
    public static frgmnt_Reab_Pallet newInstance(String param1, String param2)
    {
        frgmnt_Reab_Pallet fragment = new frgmnt_Reab_Pallet();
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
                Documento = getArguments().getString(ARG_PARAM1);
                str_TipoConsulta = getArguments().getString(ARG_PARAM2);
                Log.i("ARGUmENTOS", str_TipoConsulta + " " + Documento);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.reabastece_surtido_pallet, container, false);

        edtx_Pallet= view.findViewById(R.id.edtx_Empaque);
        edtx_Posicion= view.findViewById(R.id.edtx_Posicion);

        txtv_lote= view.findViewById(R.id.txtv_lote);
        txtv_Prod= view.findViewById(R.id.txtv_Pallet_Producto);
        txtv_Cant= view.findViewById(R.id.txtv_Pallet_Cantidad);
        txtv_Empaques= view.findViewById(R.id.txtv_Pallet_Cantidad_Empaques);

        edtx_Pallet.setOnKeyListener(new View.OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_Pallet.getText().toString().equals(""))
                        {
                            new popUpGenerico(getContext(),null, "Ingrese el pallet.", false, true, true);
                            return false;
                        }

                    new SegundoPlano("ConsultaPallet").execute(Documento,edtx_Pallet.getText().toString());

                    new esconderTeclado(getActivity());
                }
            return false;
        }
    });

        edtx_Posicion.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {


                        if(edtx_Pallet.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null, "Ingrese el pallet.", false, true, true);
                                return false;
                            }


                        if(edtx_Posicion.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null, "Ingrese la posición.", false, true, true);

                                return false;
                            }

                        new SegundoPlano("RegistraPallet").execute(Documento,edtx_Pallet.getText().toString(),edtx_Posicion.getText().toString());

                        new esconderTeclado(getActivity());
                    }
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

                                        case "ConsultaPallet":

                                        dao = ca.c_ConsultarPalletReabastece(Params[0],Params[1]);

                                        break;

                                    case "RegistraPallet":

                                        dao = ca.c_ReabastecePallet(Params[0],Params[1],Params[2]);

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
                                    case "ConsultaPallet":

                                        txtv_Prod.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                        txtv_Cant.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_Empaques.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                        txtv_lote.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("LoteProveedor"));

                                        edtx_Posicion.requestFocus();
                                        edtx_Posicion.requestFocus();


                                        break;
                                    case "RegistraPallet":

                                        new popUpGenerico(getContext(),null, "Pallet reabastecido con éxito en la posición [" + edtx_Posicion.getText().toString()+"]", dao.iscEstado(), true, true);
                                        edtx_Pallet.setText("");
                                        edtx_Posicion.setText("");
                                        txtv_Prod.setText("");
                                        txtv_Cant.setText("");
                                        txtv_Empaques.setText("");
                                        edtx_Pallet.requestFocus();
                                        txtv_lote.setText("");


                                        mListener.ActualizarDocumento();

                                        break;



                                }


//                            XmlSerializer






                        }else
                        {
                            new popUpGenerico(getContext(),null, dao.getcMensaje(), dao.iscEstado(), true, true);

                            edtx_Pallet.setText("");
                            edtx_Posicion.setText("");
                            txtv_Prod.setText("");
//                            txtv_Lote.setText("");
                            txtv_Cant.setText("");
                            txtv_Empaques.setText("");
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





    //region COMUNICACION ACT



//    public void  setDocumento(String prmDoc)
//    {
//        edtx_Transferencia.setText(prmDoc);
//    }



//    public String getArticulo()
//    {
//        return ((CustomArrayAdapter)sp_Articulo.getAdapter()).getSelectedExtra(sp_Articulo.getSelectedItemPosition());
//    }


    /**
     *
     * 0 - Articulo
     * 1 - Lote
     * 2 - Cantidad
     *
     * */
    public String[] getDatos()
    {
        return

                new String[]
                        {

                              ""
                        };
    }



    //endregion




    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
        void ActualizarDocumento();

    }



}
