package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.SurtidoTraspaso;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
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
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.Transferencia_Envio.Alm_Registro_Seleccion_Lote;
import com.automatica.AXCPT.c_Almacen.Almacen_Transferencia.c_Recepcion_Transferencias.Creacion_Seleccion_Almacen.Recepcion_Registro_Transferencia_NE_SelAlm;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import java.util.HashMap;

import de.codecrafters.tableview.SortableTableView;

public class Fragmento_Rechazo_Pallet_Trasp extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LINEA = "LINEA";

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

    private  Button btn_Rechazar;
    private  TextView txtv_Titulo,txtv_Descripcion;

    public Fragmento_Rechazo_Pallet_Trasp()
    {
        // Required empty public constructor
    }

    public static Fragmento_Rechazo_Pallet_Trasp newInstance(String param1, String param2, String Linea)
    {
        Fragmento_Rechazo_Pallet_Trasp fragment = new Fragmento_Rechazo_Pallet_Trasp();
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

        View view = inflater.inflate(R.layout.fragment_rechazo_pallet_trasp, container, false);
        ConstraintLayout fl_Oscuro = view.findViewById(R.id.fl_Oscuridad);

        txtv_Titulo =(TextView) view.findViewById(R .id.txtv_Incidencia);
        txtv_Descripcion =(TextView) view.findViewById(R.id.txtv_Titulo);

        txtv_Titulo.setText("Validación de pallet");
        txtv_Descripcion.setText("Documento: [" + Documento + "]");

        img_Back = view.findViewById(R.id.imgb_AtrasRep);
        edtx_Incidencia =(EditText) view.findViewById(R.id.edtx_Descripcion);
        edtx_CodigoPallet  =(EditText) view.findViewById(R.id.edtx_CodigoPallet);
        btn_EnviarRechazo = (Button) view.findViewById(R.id.btn_EnviarRechazo);
        btn_Rechazar = (Button) view.findViewById(R.id.btn_Rechazar);

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


                new CreaDialogos(getString(R.string.pregunta_rechazo_pallet), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        new SegundoPlano(RechazarSurtido).execute();
                        new esconderTeclado(getActivity());
                    }
                },null,getContext());

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

                btn_Rechazar.setEnabled(false);
            }
        });

        edtx_CodigoPallet.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if(!edtx_CodigoPallet.getText().toString().equals(""))
                    {
                        new SegundoPlano(consultaPallet).execute();
                    }
                }
                return false;
            }
        });





        edtx_CodigoPallet.requestFocus();

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
        cAccesoADatos_Transferencia cad = new cAccesoADatos_Transferencia(getContext());
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

                                case consultaPallet:
                                    dao = cad.cad_ConsultaRechazoPalletTrasp(Documento,edtx_CodigoPallet.getText().toString());
                                    break;

                                case RechazarSurtido:
                                    dao = cad.c_RechazoPalletTrasp(Documento, edtx_CodigoPallet.getText().toString(), edtx_Incidencia.getText().toString());
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

                                            HashMap<Integer,Integer> sizes = new HashMap<>();
                                            sizes.put(0,100);
                                            ConfigTabla.customRowsLength(sizes);
                                        }else
                                        {
                                            ConfigTabla.CargarDatosTabla(dao);
                                        }

                                        break;
                                    case RechazarSurtido:
                                        mListener.AceptarOrden();

                                        String sourceString = "<p>" + getString(R.string.Surtido_Rechazado1) + "</p> <b>" + getString(R.string.Surtido_Rechazado2) + "</b>";

                                        new popUpGenerico(getContext(), null, Html.fromHtml(sourceString), dao.iscEstado(), true, true);

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
