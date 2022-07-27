package com.automatica.AXCMP.ImpresionEtiquetas;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.automatica.AXCMP.Principal.Login;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.CreaDialogos;
import com.automatica.AXCMP.Servicios.EdtxLongClickListener;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;
import com.automatica.AXCMP.adaptadorTabla;
import com.automatica.AXCMP.adaptadorTabla_Reimpresion_Empaques;
import com.automatica.AXCMP.views.CustomArrayAdapter;

import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Text;

import androidx.fragment.app.Fragment;

public class frgmnt_Consulta_Pallet_Reimpresion extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta;

    private String ModuloPalletActual = "MP";
    private OnFragmentInteractionListener mListener;


    private  static final String frgtag_ConsultaPalletDet = "FRGCPDT";

    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle,txtv_Producto,txtv_Lote,txtv_CantidadActual,txtv_CantidadTotal,txtv_UM,txtv_Estatus,txtv_TipoDetalle,txtv_DescProd,txtv_Ubicacion,txtv_Pallet;
    private EditText edtx_CodigoPallet;
    private Button btn_ImprimirEtiquetaPallet,btn_ImprimirTodasEtiquetasEmpaque;
    private ListView lv_Empaques;
    private ImageButton btn_Back;

    private Spinner spnr_Impresoras;



    public frgmnt_Consulta_Pallet_Reimpresion()
    {
    }
    public static frgmnt_Consulta_Pallet_Reimpresion newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Consulta_Pallet_Reimpresion fragment = new frgmnt_Consulta_Pallet_Reimpresion();
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
        View view = inflater.inflate(R.layout.fragment_reimpresion_pallet, container, false);
        DeclaraVariables(view);



        new SegundoPlano("ListaImpresoras").execute();


        return view;
    }



    public void ImprimirEmpaque(final String prmCodigoEmpaque)
    {

        new CreaDialogos("¿Reimprimir etiqueta de empaque? [" + prmCodigoEmpaque + "]", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {



                if(ModuloPalletActual.equals("MP"))
                    {
                        new SegundoPlano("ReimprimirEmpaqueMP").execute(prmCodigoEmpaque);
                    }else
                    {
                        new SegundoPlano("ReimprimirEmpaquePT").execute(prmCodigoEmpaque);
                    }

            }
        },null,"Imprimir",getContext());
    }



    public void ConsultarEscaneo(final String prmCodigo)
    {

        edtx_CodigoPallet.setText(prmCodigo);

        new SegundoPlano("ConsultaPallet").execute();
    }


    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle= view.findViewById(R.id.txtv_Incidencia);
        txtv_Producto= view.findViewById(R.id.txtv_Producto);
        txtv_Lote= view.findViewById(R.id.txtv_Lote);
        txtv_CantidadActual = view.findViewById(R.id.txtv_CantAct);
        txtv_CantidadTotal= view.findViewById(R.id.txtv_CantTot);
        txtv_UM = view.findViewById(R.id.txtv_UM);
        txtv_Estatus= view.findViewById(R.id.txtv_Estatus);
        lv_Empaques = view.findViewById(R.id.lv_EmpaquesPallet);
        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        txtv_DescProd = view.findViewById(R.id.txtv_Producto2);
        txtv_Ubicacion = view.findViewById(R.id.txtv_UM2);
        spnr_Impresoras = view.findViewById(R.id.spinner);

        txtv_Pallet = view.findViewById(R.id.txtv_Pallet);

        edtx_CodigoPallet = view.findViewById(R.id.edtx_Codigo);
        btn_ImprimirEtiquetaPallet = view.findViewById(R.id.button4);
        btn_ImprimirTodasEtiquetasEmpaque = view.findViewById(R.id.button3);


        txtv_TipoDetalle= view.findViewById(R.id.txtv_TipoDetalle);







        EdtxLongClickListener edtxLongClickListener = new EdtxLongClickListener(getActivity(), "Escaneé empaque o pallet.");

        edtx_CodigoPallet.setOnLongClickListener(edtxLongClickListener );



        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                    {

                        if(edtx_CodigoPallet.getText().toString().equals(""))
                            {
                                return false;
                            }
                                 new SegundoPlano("CodigoPallet").execute();

                    }
                return false;
            }
        });


        btn_ImprimirEtiquetaPallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new CreaDialogos("¿Reimprimir etiqueta de pallet?", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {

                        if(ModuloPalletActual.equals("MP"))
                            {
                                new SegundoPlano("ReimprimirPalletMP").execute();

                            }else
                            {
                                new SegundoPlano("ReimprimirPalletPT").execute();

                            }
                    }
                },null,"Imprimir",getContext());
            }
        });


        btn_ImprimirTodasEtiquetasEmpaque.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new CreaDialogos("¿Reimprimir todas las etiquetas del pallet?", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id) {

                        if(ModuloPalletActual.equals("MP"))
                            {
                                new SegundoPlano("ReimprimirTodosEmpaqueMP").execute();

                            }else
                            {
                                new SegundoPlano("ReimprimirTodosEmpaquePT").execute();

                            }


                    }
                },null,"Imprimir",getContext());

            }
        });


        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Consulta_Pallet_Reimpresion.this).commit();

            }
        });
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
            {
                mListener.onFragmentInteraction(uri);
            }
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

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(Boolean estado);
    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(getContext());
        DataAccessObject dao;
        String tarea;
        View view;

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
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(String... arr)
        {
            try
                {
                    if(!this.isCancelled())
                        {


                            switch (tarea)
                                {

                                    case "ReimprimirPalletMP":

                                        dao = ca.c_ReimprimePalletMP(txtv_Pallet.getText().toString(),spnr_Impresoras.getSelectedItem().toString());

                                        break;
                                    case "ReimprimirPalletPT":

                                        dao = ca.c_ReimprimePalletPT(txtv_Pallet.getText().toString(),spnr_Impresoras.getSelectedItem().toString());

                                        break;
                                    case "ReimprimirTodosEmpaqueMP":
                                        dao = ca.c_ImprimeEtiquetaEmpaquesPallet(txtv_Pallet.getText().toString(),spnr_Impresoras.getSelectedItem().toString());

                                        break;


                                    case "ReimprimirTodosEmpaquePT":
                                        dao = ca.c_ImprimeEtiquetaEmpaquesPalletPT(txtv_Pallet.getText().toString(),spnr_Impresoras.getSelectedItem().toString());

                                        break;

                                    case "ReimprimirEmpaqueMP":

                                        dao = ca.c_ReimprimeEmpaqueMP(arr[0],"",spnr_Impresoras.getSelectedItem().toString());

                                        break;
                                    case "ReimprimirEmpaquePT":

                                        dao = ca.c_ReimprimeEmpaquePT(arr[0],"",spnr_Impresoras.getSelectedItem().toString());

                                        break;
                                    case "ListaImpresoras":
                                        dao = ca.cad_ListaImpresorasMiAXC();
                                        break;

                                    case "CodigoPallet":
                                    case "Pallet":
                                    case "ConsultaPallet":
                                        dao = ca.cad_ConsultaPalletReimpresionDet(edtx_CodigoPallet.getText().toString());
                                        break;

                                    case "ConsultaEmpaque":
                                        dao = ca.cad_ConsultaEmpaqueDet(paramArr[0],paramArr[1]);
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
                            SoapObject so_ResultSet;
                            switch (tarea)
                                {



                                    case "ReimprimirPallet":
                                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Pallet reimpreso con éxito",dao.iscEstado(), true, true);

                                        break;

                                    case "ReimprimirEmpaqueMP":
                                    case "ReimprimirEmpaquePT":

                                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Empaque reimpreso con éxito",dao.iscEstado(), true, true);

                                        break;

                                    case "ReimprimirTodosEmpaqueMP":
                                    case "ReimprimirTodosEmpaquePT":
                                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), dao.getcMensaje(),dao.iscEstado(), true, true);
                                        break;
                                    case "ListaImpresoras":
                                        spnr_Impresoras.setAdapter(new CustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item , dao.getcTablasSorteadas("Impresora","Impresora")));
                                        break;

                                    case "CodigoPallet":
                                    case "Pallet":
                                    case "ConsultaPallet":
                                        so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                                    //    SoapObject so_Tabla = (SoapObject) so_ResultSet.getProperty(1);

                                        Log.i("SOAP",dao.getSoapObject().getProperty(1).toString());
                                      //  Log.i("SOAP",so_Tabla.toString());
                                         ModuloPalletActual = so_ResultSet.getPrimitivePropertyAsString("Modulo");
                                        txtv_Titulo.setText("["+ModuloPalletActual+"] " + so_ResultSet.getPrimitivePropertyAsString("CodigoPallet"));
                                        txtv_Detalle.setText("Reimpresión");
                                        txtv_Producto.setText(so_ResultSet.getPrimitivePropertyAsString("Producto"));
                                        txtv_Lote.setText(so_ResultSet.getPrimitivePropertyAsString("Lote"));
                                        txtv_CantidadActual.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_CantidadTotal.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadOriginal"));
                                        txtv_UM.setText(so_ResultSet.getPrimitivePropertyAsString("UnidadMedida"));
                                        txtv_Estatus.setText(so_ResultSet.getPrimitivePropertyAsString("Status"));
                                        txtv_DescProd.setText(so_ResultSet.getPrimitivePropertyAsString("DescNumParte"));
                                        txtv_Ubicacion.setText(so_ResultSet.getPrimitivePropertyAsString("CodigoPosicion"));

                                        txtv_Pallet.setText(so_ResultSet.getPrimitivePropertyAsString("CodigoPallet"));

                                        txtv_TipoDetalle.setText("Empaques");

                                        lv_Empaques.setAdapter(new adaptadorTabla_Reimpresion_Empaques(getContext(), R.layout.list_item_2_datos_reimp, dao.getcTablasSorteadas("CodigoEmpaque", "Producto","Lote","CantidadActual")));

                                        break;

                                    case "ConsultaEmpaque":
                                        so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);

                                       so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                                        //    SoapObject so_Tabla = (SoapObject) so_ResultSet.getProperty(1);

                                        Log.i("SOAP",dao.getSoapObject().getProperty(1).toString());
                                        //  Log.i("SOAP",so_Tabla.toString());

                                        txtv_Titulo.setText("["+so_ResultSet.getPrimitivePropertyAsString("Modulo")+"] " + so_ResultSet.getPrimitivePropertyAsString("CodigoEmpaque"));
                                        txtv_Detalle.setText("Consulta de Empaque");
                                        txtv_Producto.setText(so_ResultSet.getPrimitivePropertyAsString("Producto"));
                                        txtv_Lote.setText(so_ResultSet.getPrimitivePropertyAsString("Lote"));
                                        txtv_CantidadActual.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadActual"));
                                        txtv_CantidadTotal.setText(so_ResultSet.getPrimitivePropertyAsString("CantidadOriginal"));
                                        txtv_UM.setText(so_ResultSet.getPrimitivePropertyAsString("UnidadMedida"));
                                        txtv_Estatus.setText(so_ResultSet.getPrimitivePropertyAsString("Status"));
                                        txtv_TipoDetalle.setText("Pallet");


                                        lv_Empaques.setAdapter(new adaptadorTabla_Reimpresion_Empaques(getContext(), R.layout.list_item_2_datos_fragment, dao.getcTablasSorteadas("CodigoPallet", "Modulo")));

                                        break;


                                }
                        }else
                        {
                            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(),dao.iscEstado(), true, true);
                }

            //swipeRefreshLayout.setRefreshing(false);
            mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }




}
