package com.automatica.AXCPT.c_Almacen.Almacen_Transferencia;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Transferencia;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import de.codecrafters.tableview.SortableTableView;

public class Fragmento_Ingresar_Textos extends Fragment
{
    //PRIMITIVAS

    private static final String ARG_DOC = "DOC";

    private static final String consultaDocumento = "consultaDocumento";
    private static final String registrarTextos= "registrarTextos";

    private static final String strIdTabla = "strIdTabla";

    private String Documento;

    //Objetos
    private OnFragmentInteractionListener mListener;
    Handler h =new Handler();

    //VIEWS

    private EditText edtx_NotaEntrega, edtx_CartaPorte,edtx_TextoCabecera;
    private ImageButton img_Back;

    private  Button btn_GuardarTextos;
    private  TextView txtv_Titulo,txtv_Descripcion;

    public Fragmento_Ingresar_Textos()
    {
    }

    public static Fragmento_Ingresar_Textos newInstance(String documento)
    {
        Fragmento_Ingresar_Textos fragment = new Fragmento_Ingresar_Textos();
        Bundle args = new Bundle();
        args.putString(ARG_DOC, documento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                Documento = getArguments().getString(ARG_DOC);

            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_ingresar_textos, container, false);
        ConstraintLayout fl_Oscuro = view.findViewById(R.id.fl_Oscuridad);

        txtv_Titulo =(TextView) view.findViewById(R .id.txtv_Incidencia);
        txtv_Descripcion =(TextView) view.findViewById(R.id.txtv_Titulo);

        txtv_Titulo.setText("Ingreso de textos ERP");
        txtv_Descripcion.setText("Documento: [" + Documento + "]");

        img_Back = view.findViewById(R.id.imgb_AtrasRep);

        edtx_TextoCabecera =(EditText) view.findViewById(R.id.edtx_TextoCabecera);
        edtx_CartaPorte  =(EditText) view.findViewById(R.id.edtx_CartaPorte);
        edtx_NotaEntrega = (EditText) view.findViewById(R.id.edtx_NotaEntrega);

        btn_GuardarTextos = (Button) view.findViewById(R.id.btn_GuardarTextos);

        img_Back.setVisibility(View.VISIBLE);

        img_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
        btn_GuardarTextos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(edtx_CartaPorte.getText().toString().equals(""))
                {
                    new popUpGenerico(getContext(),null,"Ingrese la carta porte.",false,true,true);
                    return;
                }
                if(edtx_NotaEntrega.getText().toString().equals(""))
                {
                    new popUpGenerico(getContext(),null,"Ingrese la nota de entrega.",false,true,true);
                    return;
                }
                if(edtx_TextoCabecera.getText().toString().equals(""))
                {
                    new popUpGenerico(getContext(),null,"Ingrese el texto de cabecera.",false,true,true);
                    return;
                }

                new CreaDialogos("¿Guardar textos?",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {     new SegundoPlano(registrarTextos).execute();
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





        edtx_NotaEntrega.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(edtx_NotaEntrega.getText().toString().equals(""))
                        {
                            return false;
                        }


                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_CartaPorte.requestFocus();
                            }
                        },10);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(getContext(), null, e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });

        edtx_CartaPorte.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(edtx_CartaPorte.getText().toString().equals(""))
                        {
                            return false;
                        }
                        h.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                edtx_TextoCabecera.requestFocus();
                            }
                        },10);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(getContext(), null, e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });


        edtx_TextoCabecera.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        if(edtx_TextoCabecera.getText().toString().equals(""))
                        {
                            return false;
                        }
                        new CreaDialogos("¿Guardar textos?",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {     new SegundoPlano(registrarTextos).execute();
                                    }
                                }, null,getActivity());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(getContext(), null, e.getMessage(), false, true, true);
                    }
                }
                return false;
            }
        });







        new SegundoPlano(consultaDocumento).execute();

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
        boolean RegistrarTextos();
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

                                case consultaDocumento:
                                    dao = ca.c_ConsultaTextosTraspaso(Documento);
                                    break;
                                case registrarTextos:
                                    dao = ca.c_RegistroTextosTraspaso(Documento,edtx_NotaEntrega.getText().toString(),edtx_CartaPorte.getText().toString(),edtx_TextoCabecera.getText().toString());
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
                                    case consultaDocumento:

                                    edtx_NotaEntrega.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NotaEntrega"));
                                    edtx_CartaPorte.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CartaPorte"));
                                    edtx_TextoCabecera.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("TextoCabecera"));
                                    if(edtx_NotaEntrega.getText().toString().equals(""))
                                    {
                                        edtx_NotaEntrega.requestFocus();
                                    }else if(edtx_CartaPorte.getText().toString().equals(""))
                                    {
                                        edtx_CartaPorte.requestFocus();

                                    }else if(edtx_TextoCabecera.getText().toString().equals(""))
                                    {
                                        edtx_TextoCabecera.requestFocus();
                                    }

                                        break;

                                    case registrarTextos:
                                    mListener.RegistrarTextos();
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
