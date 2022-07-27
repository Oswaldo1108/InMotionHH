package com.automatica.AXCMP.Liberaciones.LiberacionTransfer;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import androidx.fragment.app.Fragment;

public class frgmnt_Agregar_Articulos extends Fragment implements Adaptador_RV_MenuPrincipal.onClickRV
{
    private static final String ARG_PARAM1 = "param1";//ESTE ARGUMENTOS SE TOMA PARA DECIDIR QUE CONSULTA DE DOCUMENTO SE HARA
    private static final String ARG_PARAM2 = "param2";


    private String str_Modulo;
    private String str_TipoConsulta;

    Spinner sp_Articulo, spnr_Lotes;

    private Button btn_AgregarPartida;
    private OnFragmentInteractionListener mListener;


    private static final String frgtag_ConsultaPallet= "FRGCP";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    //VIEWS

    private EditText edtx_Transferencia, edtx_Cantidad;



    public frgmnt_Agregar_Articulos()
    {
    }
    public static frgmnt_Agregar_Articulos newInstance(String param1, String param2)
    {
        frgmnt_Agregar_Articulos fragment = new frgmnt_Agregar_Articulos();
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
        View view = inflater.inflate(R.layout.frgmnt_seleccion_articulos, container, false);
//        rv_Detalle= view.findViewById(R.id.rv_Partidas);
//        txtv_Titulo = view.findViewById(R.id.txtv_Incidencia);
//        txtv_Detalle = view.findViewById(R.id.txtv_Titulo);


//
//        ImageButton imgb_AtrasRep = view.findViewById(R.id.imgb_AtrasRep);
//        imgb_AtrasRep.setVisibility(View.GONE);

        spnr_Lotes= view.findViewById(R.id.vw_Spinner_Lote).findViewById(R.id.spinner);
        sp_Articulo = view.findViewById(R.id.vw_Spinner_Producto).findViewById(R.id.spinner);

        edtx_Transferencia = view.findViewById(R.id.edtx_Embarque);
        edtx_Cantidad = view.findViewById(R.id.edtx_Embarque3);


        new SegundoPlano("ListaProductos").execute();

        btn_AgregarPartida = view.findViewById(R.id.btn_AgregarPartida);



        btn_AgregarPartida.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edtx_Cantidad.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null, "Ingrese una cantidad.", false, true, true);
                        return;
                    }
                    mListener.CrearPartida();

            }
        });


        sp_Articulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {



                new SegundoPlano("ListaLotes").execute(((CustomArrayAdapter)sp_Articulo.getAdapter()).getSelectedExtra(sp_Articulo.getSelectedItemPosition()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

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

    @Override
    public void clickBotonMasInfo(String[] datos)
    {
    }


    private class SegundoPlano extends AsyncTask<String,Void,Void>
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




                                        case "ListaProductos":

                                        String Producto = "@";

//                                        if(Params!=null)
//                                            {
//                                                Producto = Params[0];
//                                            }

                                        dao = ca.C_ListaProducto(Producto);

                                        break;

                                    case "ListaLotes":



                                        String ProductoLote = "@";

                                        if(Params[0]!=null)
                                            {
                                                ProductoLote = Params[0];
                                            }

                                        dao = ca.cad_ListaLotesEmbarquesMiAXC(ProductoLote);
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
                                    case "ListaProductos":
                                        sp_Articulo.setAdapter(new CustomArrayAdapter(getActivity(),
                                                android.R.layout.simple_spinner_item,
                                                dao.getcTablasSorteadas("ItemSpinner","Producto")));

                                        break;
                                    case "ListaLotes":
                                        spnr_Lotes.setAdapter(new CustomArrayAdapter(getContext(),
                                                android.R.layout.simple_spinner_item ,
                                                dao.getcTablasSorteadas("LoteAXC","LoteAXC")));
                                        break;



                                }


//                            XmlSerializer






                        }else
                        {
                            new popUpGenerico(getContext(),null, dao.getcMensaje(), dao.iscEstado(), true, true);
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



    public void  setDocumento(String prmDoc)
    {
        edtx_Transferencia.setText(prmDoc);
    }



    public String getArticulo()
    {
        return ((CustomArrayAdapter)sp_Articulo.getAdapter()).getSelectedExtra(sp_Articulo.getSelectedItemPosition());
    }


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

                                ((CustomArrayAdapter)sp_Articulo.getAdapter()).getSelectedExtra(sp_Articulo.getSelectedItemPosition()),
                                ((CustomArrayAdapter)spnr_Lotes.getAdapter()).getSelectedExtra(spnr_Lotes.getSelectedItemPosition()),
                                edtx_Cantidad.getText().toString()
                        };
    }

    /**
     *
     *CAMBIAR DOCUMENTO FRAGMENTO AGREGAR PARTIDA
     * */
    public void SetDocumento(String prmDocumento)
    {
        edtx_Transferencia.setText(prmDocumento);
    }




    public String getLote()
    {
        return ((CustomArrayAdapter)spnr_Lotes.getAdapter()).getSelectedExtra(spnr_Lotes.getSelectedItemPosition());
    }



    public String getCantidad()
    {
        return edtx_Cantidad.getText().toString();
    }




    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
        void CrearPartida();
    }



    //endregion






}
