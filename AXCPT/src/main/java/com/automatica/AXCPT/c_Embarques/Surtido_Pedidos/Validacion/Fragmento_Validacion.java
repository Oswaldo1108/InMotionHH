package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import de.codecrafters.tableview.SortableTableView;

public class Fragmento_Validacion extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LINEA = "LINEA";

    private static final String AceptarSurtido= "AceptarSurtido";
    private static final String RechazarSurtido= "RechazarSurtido";
    private static final String consultaPallet = "ConsultaPallet";

    private static final String strIdTabla = "strIdTabla";

    private String Documento;
    private String CodigoPallet;
    private String Linea;


    //PRIMITIVAS


    //Objetos
    private OnFragmentInteractionListener mListener;

    //VIEWS
    SortableTableView tabla;
    TableViewDataConfigurator ConfigTabla = null;

    private EditText edtx_CodigoPallet, edtx_Incidencia;
    private Button btn_EnviarRechazo;
    private ImageButton img_Back;

    private  Button btn_Rechazar,btn_Aceptar;
    private  TextView txtv_Titulo,txtv_Descripcion;

    public Fragmento_Validacion()
    {
        // Required empty public constructor
    }

    public static Fragmento_Validacion newInstance(String param1, String param2, String Linea)
    {
        Fragmento_Validacion fragment = new Fragmento_Validacion();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_LINEA, Linea);
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
                CodigoPallet = getArguments().getString(ARG_PARAM2);
                Linea = getArguments().getString(ARG_LINEA);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_validacion, container, false);
        ConstraintLayout fl_Oscuro = view.findViewById(R.id.fl_Oscuridad);

        txtv_Titulo =(TextView) view.findViewById(R .id.txtv_Incidencia);
        txtv_Descripcion =(TextView) view.findViewById(R.id.txtv_Titulo);

        txtv_Titulo.setText("Documento: [" + Documento + "]");
        txtv_Descripcion.setText("Guía: " + Linea);

        img_Back = view.findViewById(R.id.imgb_AtrasRep);
        edtx_Incidencia =(EditText) view.findViewById(R.id.edtx_Descripcion);
        edtx_CodigoPallet  =(EditText) view.findViewById(R.id.edtx_CodigoPallet);
        btn_EnviarRechazo = (Button) view.findViewById(R.id.btn_EnviarRechazo);
        btn_Rechazar = (Button) view.findViewById(R.id.btn_Rechazar);
        btn_Aceptar = (Button) view.findViewById(R.id.btn_Aceptar);

        edtx_Incidencia.setVisibility(View.GONE);
        btn_EnviarRechazo.setVisibility(View.GONE);
        img_Back.setVisibility(View.VISIBLE);

        tabla = (SortableTableView) view.findViewById(R.id.tableView_OC);

        img_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        btn_EnviarRechazo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null,"Ingrese el código a rechazar.",false,true,true);
                        return;
                    }

                if(edtx_Incidencia.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null,"Ingrese la razón del rechazo.",false,true,true);
                        return;
                    }

                        new SegundoPlano(RechazarSurtido).execute();

            }
        });

        fl_Oscuro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                return;
            }
        });

        btn_Rechazar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                edtx_Incidencia.setVisibility(View.VISIBLE);
                btn_EnviarRechazo.setVisibility(View.VISIBLE);

                btn_Aceptar.setEnabled(false);
                btn_Rechazar.setEnabled(false);
            }
        });

        btn_Aceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null,"Ingrese el código de pallet.",false,true,true);
                        return;
                    }
                new SegundoPlano(AceptarSurtido).execute();
            }
        });

        edtx_CodigoPallet.setText(CodigoPallet);

        if(!edtx_CodigoPallet.getText().toString().equals(""))
        {
            new SegundoPlano(consultaPallet).execute();
        }

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


    public interface OnFragmentInteractionListener
    {
        boolean AceptarOrden();
        void EstadoCarga(Boolean Estado);
    }

    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {
        cAccesoADatos_Embarques ca = new cAccesoADatos_Embarques(getContext());
        DataAccessObject dao = null;
        String Tarea;

        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute()
        {
            if(mListener!=null)
            {
                mListener.EstadoCarga(true);
            }
        }

        @Override
        protected Void doInBackground(String... params)
        {
            if (!this.isCancelled())
                try
                    {
                        switch (Tarea)
                            {

                                case consultaPallet:
                                    dao = ca.cad_ConsultaValidacionPallet(Documento,edtx_CodigoPallet.getText().toString());
                                    break;
                                case AceptarSurtido:
                                    dao = ca.c_ValidaEmbPallet(Documento,edtx_CodigoPallet.getText().toString(),Linea,"0");

                                    break;

                                case RechazarSurtido:
                                    dao = ca.c_OSRegistrarPalletLineaRechazado(Documento, edtx_CodigoPallet.getText().toString(),Linea, edtx_Incidencia.getText().toString());
                                    break;

                                default:
                                    dao = new DataAccessObject();
                                    break;
                            }
                    } catch (Exception e)
                    {
                        if (dao == null)
                            {
                                dao = new DataAccessObject(e);
                            }

                        e.printStackTrace();
                    }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {

            try
                {

                    if(dao.iscEstado())
                        {
                            switch (Tarea)
                                {
                                    case "ConsultaPallet":
                                        if(ConfigTabla == null)
                                        {
                                            ConfigTabla = new TableViewDataConfigurator(strIdTabla, tabla, dao,getActivity());
                                        }else
                                        {
                                            ConfigTabla.CargarDatosTabla(dao);
                                        }

                                        if(!dao.getcMensaje().equals("")&&!dao.getcMensaje().contains("AnyType"))
                                        {
                                            txtv_Descripcion.setText("Guía:" + dao.getcMensaje());

                                        }

                                        break;
                                    case RechazarSurtido:
                                    case AceptarSurtido:
                                        mListener.AceptarOrden();
                                        getActivity().getSupportFragmentManager().popBackStack();
                                        break;


                                    default:
                                        new popUpGenerico(getContext(), null, dao.getcMensaje(), dao.iscEstado(), true, true);
                                }
                        }else
                        {
                            new popUpGenerico(getContext(), null, dao.getcMensaje(), dao.iscEstado(), true, true);

                        }


                } catch (Exception e)
                {
                    new popUpGenerico(getContext(), null, e.getMessage(), false, true, true);
                    e.printStackTrace();
                }
            if(mListener!=null)
            {
                mListener.EstadoCarga(false);
            }
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            if(mListener!=null)
            {
                mListener.EstadoCarga(false);
            }
        }
    }


}
