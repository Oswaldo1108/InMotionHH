package com.automatica.AXCMP.MiAXC;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.automatica.AXCMP.Principal.DateTimeSince;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.Constructor_Dato;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;


public class frgmnt_ConsultaDetalleIncidencia extends Fragment
{
    private static final String ARG_Documento = "Documento";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //PRIMITIVAS
    boolean recarga;
    String Documento;

    //VISTAS
    private TextView txtv_Fecha,txtv_Titulo,txtv_Area,txtv_Descripcion,txtv_Estacion,txtv_Usuario,txtv_Incidencia;
    private ImageButton imgb_Cerrar;
    private ImageView imgv_Preview;

    //OBJETOS
    ArrayList<Constructor_Dato> lista = null;


    public frgmnt_ConsultaDetalleIncidencia()
    {

    }

    public static frgmnt_ConsultaDetalleIncidencia newInstance(String Documento, String param2)
    {
        frgmnt_ConsultaDetalleIncidencia fragment = new frgmnt_ConsultaDetalleIncidencia();
        Bundle args = new Bundle();
        Log.i("DOCUMENTO", Documento);
        args.putString(ARG_Documento, Documento);
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
                mParam1 = getArguments().getString(ARG_Documento);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        mListener.onDataPass("HOLA ESTUVE EN ON CREATE" +
                "");

        mListener.onFragmentInteraction(Uri.parse(this.getClass().getSimpleName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = null;
        try
            {


                // Inflate the layout for this fragment
                view   = inflater.inflate(R.layout.fragment_incidencia_det, container, false);
                declararVariables(view);
                agregarListeners(view);

                Documento = (String) this.getArguments().get(ARG_Documento);

                SegundoPlano sp = new SegundoPlano("ConsultaIncidenciaDet");
                sp.execute();

            }catch (Exception e)
            {
                e.printStackTrace();

            }
        return view;
    }


    private void declararVariables(View view)
    {
        try
            {
                imgb_Cerrar = view.findViewById(R.id.imgb_AtrasRep);
                txtv_Area = view.findViewById(R.id.txtv_Pallet);
                txtv_Descripcion= view.findViewById(R.id.txtv_DescIncidencia);
                txtv_Titulo= view.findViewById(R.id.txtv_Titulo);
                txtv_Fecha= view.findViewById(R.id.txtv_Fecha);
                txtv_Estacion = view.findViewById(R.id.txtv_Estacion);
                txtv_Usuario = view.findViewById(R.id.txtv_Usuario);
                txtv_Incidencia = view.findViewById(R.id.txtv_Incidencia);


                imgv_Preview= view.findViewById(R.id.img_Preview);

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);


            }
    }

    private void agregarListeners(View view)
    {
        try
            {
                imgb_Cerrar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_ConsultaDetalleIncidencia.this).commit();
                    }
                });
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);


            }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            {
                mListener = (OnFragmentInteractionListener) context;
            //    onDataPass = (OnDataPass) context;
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

    public void passData(String data)
    {
        mListener.onDataPass(data);
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
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
            try
            {
                mListener.ActivaProgressBar(true);
            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), getActivity().getCurrentFocus(), e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
                {
                    if(!this.isCancelled())
                        {
                            switch (tarea)
                                {
                                    case "ConsultaIncidenciaDet":
                                        dao =   ca.cad_ConsultaIncidenciaDet(Documento);
                                        break;
                                    default:
                                        dao = new DataAccessObject(false,"Tarea no soportada " + tarea,null);
                                        break;


                                }

                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    dao.setcMensaje(e.getMessage());
                    dao.setcEstado(false);
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
                                    case "ConsultaIncidenciaDet":
                                        SoapObject sa = (SoapObject) dao.getSoapObject().getProperty(0);

                                        txtv_Incidencia.setText("INCIDENCIA " + sa.getPrimitivePropertyAsString("IdIncidencia"));
                                        txtv_Titulo.setText("Hace " + DateTimeSince.getDateTimeSince(sa.getPrimitivePropertyAsString("FechaSince")));
                                        txtv_Fecha.setText(DateTimeSince.convertDate(sa.getPrimitivePropertyAsString("FechaSince")));
                                        txtv_Area.setText(sa.getPrimitivePropertyAsString("Modulo"));
                                        txtv_Descripcion.setText(sa.getPrimitivePropertyAsString("Descripcion"));
                                        txtv_Estacion.setText(sa.getPrimitivePropertyAsString("Estacion"));
                                        txtv_Usuario.setText(sa.getPrimitivePropertyAsString("Usuario"));
                                        imgv_Preview.setImageBitmap(dao.getImagen());
                                        break;
                                    case "ConsultaIncidenciasEst":
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
            mListener.ActivaProgressBar(false);
        }
        @Override
        protected void onCancelled()
        {
            new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }
    public interface OnDataPass
    {
        void onDataPass(String data);

    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onDataPass(String data);
        boolean ActivaProgressBar(boolean estado);

    }





}
