package com.automatica.AXCPT.c_Embarques.Surtido_Pedidos.Validacion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH.frgmnt_Seleccion_Producto;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adUtilidades;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos_Embarques;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

public class fragmento_validacion_transporte extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    ImageButton back;
    TextView tx_Titulo, tx_subtitulo, textView117, tx_param1,tipoC;

    TextView tv_codigo, tv_Producto, tv_empaque, tv_Estatus, tv_Numparte;

    EditText edtx_Codigo, edtx_Comentario;
    Button btn_Aceptar;
    ConstraintLayout fondo;
    fragValInterface valInterface;

    private String mParam1;
    private String mParam2;

    public fragmento_validacion_transporte()
    {
        // Required empty public constructor
    }

    public static fragmento_validacion_transporte newInstance(String param1, String param2)
    {
        fragmento_validacion_transporte fragment = new fragmento_validacion_transporte();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void AgregarListeners()
    {
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().popBackStack();
            }
        });
        edtx_Codigo.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (TextUtils.isEmpty(edtx_Codigo.getText()))
                {
                    new popUpGenerico(getContext(), getView(), "Llene el campo '" + mParam1 + "'", false, true, true);
                    return false;
                }
                new SegundoPlano("ConsultaPallet").execute();
                return false;
            }
        });
    }

    public void LimpiarCampos()
    {
        tv_codigo.setText("");
        tv_empaque.setText("");
        tv_Estatus.setText("");
        tv_Numparte.setText("");
        tv_Producto.setText("");
    }
    public void DeclararVariables(View view, String tipo)
    {
        tx_Titulo = view.findViewById(R.id.txtv_Titulo);
        tx_subtitulo = view.findViewById(R.id.txtv_Incidencia);
        textView117 = view.findViewById(R.id.textView117);
        edtx_Codigo = view.findViewById(R.id.edtx_Codigo);
        edtx_Comentario = view.findViewById(R.id.edtx_Comentario);
        btn_Aceptar = view.findViewById(R.id.btn_Aceptar);
        tx_param1 = view.findViewById(R.id.tx_param1);

        tv_codigo = view.findViewById(R.id.tv_codigo);
        tv_Producto = view.findViewById(R.id.tv_Producto);
        tv_empaque = view.findViewById(R.id.tv_empaque);
        tv_Estatus = view.findViewById(R.id.tv_Estatus);
        tv_Numparte = view.findViewById(R.id.tv_Numparte);
        tipoC=view.findViewById(R.id.tipoC);

        back = view.findViewById(R.id.imgb_AtrasRep);
        back.setVisibility(View.VISIBLE);

        fondo = view.findViewById(R.id.include2);
        fondo.setBackgroundResource(R.drawable.orilla_layout_partial);


        switch (tipo)
        {
            case "Pallet":
                tx_Titulo.setText("Rechazo");
                tx_subtitulo.setText("Por pallet");
                textView117.setText("Pallet");
                tx_param1.setText("Pallet");
                break;
            case "Empaque":
                tx_Titulo.setText("Rechazo");
                tx_subtitulo.setText("Por paquete");
                textView117.setText("Paquete");
                tx_param1.setText("Paquete");
                tipoC.setText("Pallet");

                break;
            default:
                tx_Titulo.setText("Validación");
                tx_subtitulo.setText("Tipo no seleccionado");
                textView117.setText("-");
                tx_param1.setText("-");
                edtx_Comentario.setEnabled(false);
                edtx_Comentario.setEnabled(false);
                btn_Aceptar.setEnabled(false);
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragmento_validacion_transporte, container, false);
        DeclararVariables(v, mParam1);
        AgregarListeners();
        valInterface.Inflate(true);
        return v;
    }

    @Override
    public void onDestroyView()
    {
        valInterface.Inflate(false);
        super.onDestroyView();
    }

    public interface fragValInterface
    {
        void Inflate(boolean estado);

        void Clicked();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof fragmento_validacion_transporte.fragValInterface)
        {
            valInterface = (fragValInterface) context;
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
        valInterface = null;
    }

    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {
        String Tarea;

        DataAccessObject dao;
        cAccesoADatos_Embarques ca = new cAccesoADatos_Embarques(getContext());

        public SegundoPlano(String tarea)
        {
            Tarea = tarea;
        }

        @Override
        protected Void doInBackground(String... strings)
        {
            try
            {
                switch (mParam1)
                {
//<==================================================Fragmento en pallets=============================================>
                    case "Pallet":
                        switch (Tarea)
                        {

                            case "ConsultaPallet":
                                dao = ca.c_ConsultarPalletPT(edtx_Codigo.getText().toString());
                                break;

                            case "Rechazar":
                                dao = new DataAccessObject();

//                                dao= ca.c_OSRegistrarPalletLineaRechazado(edtx_Comentario.getText().toString(),edtx_Codigo.getText().toString());
                                break;
                            default:
                                dao = new DataAccessObject();
                                break;
                        }
                        break;
//<=====================================================Fragmento en empaques==========================================>
                    case "Empaque":
                        switch (Tarea)
                        {

                            case "ConsultaPallet":
                                dao = ca.c_ConsultaEmpaque(edtx_Codigo.getText().toString());
                                break;
                            case "Rechazar":

//                                dao= ca.c_OSRegistrarEmpaqueAndenRechazado(edtx_Comentario.getText().toString(),edtx_Codigo.getText().toString());
                                break;
                            default:
                                dao = new DataAccessObject();
                                break;
                        }
                        break;
                    default:
                        new popUpGenerico(getContext(), getView(), "No hay un tipo seleccionado", false, true, true);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                new popUpGenerico(getContext(), getView(), e.getMessage(), false, true, true);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
            {

                super.onPostExecute(aVoid);
                if (dao.iscEstado())
                {
                switch (mParam1)
                {
    //<==================================================Fragmento en pallets=============================================>
                        case "Pallet":
                            switch (Tarea) {

                                case "ConsultaPallet":
                                    tv_codigo.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                    tv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                                    tv_Numparte.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                    tv_empaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("Empaques"));
                                    tv_Estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                                    break;
                                default:
                                    dao = new DataAccessObject();
                                    break;
                            }
                            break;
    //<=====================================================Fragmento en empaques==========================================>
                        case "Empaque":
                            switch (Tarea) {

                                case "ConsultaPallet":
                                    tv_codigo.setText(edtx_Codigo.getText().toString());
                                    tv_Numparte.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("NumParte"));
                                    tv_Producto.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescProd"));
                                    tv_empaque.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("CodigoPallet"));
                                    tv_Estatus.setText(dao.getSoapObject_parced().getPrimitivePropertyAsString("DescStatus"));
                                    break;
                                default:
                                    dao = new DataAccessObject();
                                    break;
                            }
                            break;
                    }
                    edtx_Codigo.setText("");
                }else
                    {
                    LimpiarCampos();
                    new popUpGenerico(getContext(),getView(),dao.getcMensaje(),false,true,true);
                }
             }
             catch (Exception e)
             {
                 e.printStackTrace();
                 new popUpGenerico(getContext(), getView(), e.getMessage(), false, true, true);
             }

        }
    }
}