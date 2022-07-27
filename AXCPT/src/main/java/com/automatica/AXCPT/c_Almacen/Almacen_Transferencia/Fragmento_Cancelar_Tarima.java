package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.TableHelpers.TableViewDataConfigurator;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import de.codecrafters.tableview.SortableTableView;

public class Fragmento_Cancelar_Tarima extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LINEA = "LINEA";

    private static final String consultaPallets = "consultaPallets";
    private static final String CancelarTarima= "RechazarSurtido";

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
    private ImageButton img_Back;

    private  Button btn_CancelarTarima;
    private  TextView txtv_Titulo,txtv_Descripcion;

    public Fragmento_Cancelar_Tarima()
    {
    }

    public static Fragmento_Cancelar_Tarima newInstance(String param1, String param2, String Linea)
    {
        Fragmento_Cancelar_Tarima fragment = new Fragmento_Cancelar_Tarima();
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

        View view = inflater.inflate(R.layout.fragment_cancelar_tarima, container, false);
        ConstraintLayout fl_Oscuro = view.findViewById(R.id.fl_Oscuridad);

        txtv_Titulo =(TextView) view.findViewById(R .id.txtv_Incidencia);
        txtv_Descripcion =(TextView) view.findViewById(R.id.txtv_Titulo);

        txtv_Titulo.setText("Validación de pallet");
        txtv_Descripcion.setText("Documento: [" + Documento + "]");

        img_Back = view.findViewById(R.id.imgb_AtrasRep);
        edtx_Incidencia =(EditText) view.findViewById(R.id.edtx_Descripcion);
        edtx_CodigoPallet  =(EditText) view.findViewById(R.id.edtx_CodigoPallet);
        btn_CancelarTarima = (Button) view.findViewById(R.id.btn_CancelarTarima);

        edtx_Incidencia.setVisibility(View.GONE);
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
        btn_CancelarTarima.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null,"Ingrese el pallet a rechazar.",false,true,true);
                        return;
                    }

                if(edtx_Incidencia.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null,"Ingrese la razón del rechazo.",false,true,true);
                        return;
                    }


                new CreaDialogos("¿Cancelar Tarima? [" + edtx_CodigoPallet.getText().toString() + "]",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {     new SegundoPlano(CancelarTarima).execute();
                            }
                        }, null,getActivity());



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

        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    if(edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null,"Ingrese el código a rechazar.",false,true,true);
                        return false;
                    }
                    edtx_Incidencia.setVisibility(View.VISIBLE);
                    btn_CancelarTarima.setVisibility(View.VISIBLE);
                    edtx_Incidencia.requestFocus();
                }
                return false;
            }
        });
        edtx_CodigoPallet.requestFocus();
        new SegundoPlano(consultaPallets).execute();

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
        cAccesoADatos_Transferencia ca = new cAccesoADatos_Transferencia(getContext());
        DataAccessObject dao = null;
        String Tarea;

        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute()
        {

            mListener.EstadoCarga(true);
        }

        @Override
        protected Void doInBackground(String... params)
        {
            if (!this.isCancelled())
                try
                    {
                        switch (Tarea)
                            {

                                case consultaPallets:
                                    dao = ca.cad_ListarTarimasRecibidasTraspaso(Documento);
                                    break;
                                case CancelarTarima:
                                    dao = ca.c_CancelarPalletTraspaso(Documento,edtx_CodigoPallet.getText().toString(),edtx_Incidencia.getText().toString());
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
                                    case consultaPallets:
                                        if(ConfigTabla == null)
                                        {
                                            ConfigTabla = new TableViewDataConfigurator(strIdTabla, tabla, dao,getActivity());
                                        }else
                                        {
                                            ConfigTabla.CargarDatosTabla(dao);
                                        }
                                        break;

                                    case CancelarTarima:
                                        new popUpGenerico(getContext(), null, "Tarima cancelada con éxito.", dao.iscEstado(), true, true);
                                        new SegundoPlano(consultaPallets).execute();
                                        edtx_Incidencia.setText("");
                                        edtx_CodigoPallet.setText("");
                                        edtx_CodigoPallet.requestFocus();
                                        mListener.AceptarOrden();
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
            mListener.EstadoCarga(false);
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            mListener.EstadoCarga(false);
        }
    }
}
