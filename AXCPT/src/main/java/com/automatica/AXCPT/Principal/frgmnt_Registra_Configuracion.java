package com.automatica.AXCPT.Principal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class frgmnt_Registra_Configuracion extends Fragment {

    List<String> valid = Arrays.asList("Maulec2020", "52637825A2", "Coflex2021");

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta;


    private OnFragmentInteractionListener mListener;


    private static final String frgtag_Config = "FRGCONF";

    //VIEWS

    private ImageButton btn_Back;
    private TextView txtv_Titulo, txtv_Detalle, txtv_Licencia;
    private EditText edtx_DireccionIP, edtx_Estacion, edtx_Ruta;
    private Button btn_Registrar;
    private Switch switchNotificaciones;

    public frgmnt_Registra_Configuracion() {
    }

    public static frgmnt_Registra_Configuracion newInstance(String[] param, String TipoConsulta) {
        frgmnt_Registra_Configuracion fragment = new frgmnt_Registra_Configuracion();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1, param);
        args.putString(ARG_PARAM2, TipoConsulta);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramArr = getArguments().getStringArray(ARG_PARAM1);
            str_TipoConsulta = getArguments().getString(ARG_PARAM2);
            Log.i("ARGUMENTOSPALLETDET", "HOLA " + paramArr[0]);
        }
    }


    public void generarQR(View view) {
        if (paramArr[0]!=null){
            QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(paramArr[0], BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                ((ImageView) view.findViewById(R.id.qr_code)).setImageBitmap(bmp);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registra_datos, container, false);
        DeclaraVariables(view);

        return view;
    }

    private void DeclaraVariables(View view) {
        final Handler h = new Handler();
        txtv_Titulo = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle = view.findViewById(R.id.txtv_Incidencia);

        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        edtx_DireccionIP = view.findViewById(R.id.edtx_DireccionIP);
        edtx_Estacion = view.findViewById(R.id.edtx_Estacion);
        btn_Registrar = view.findViewById(R.id.btn_Registrar);
        txtv_Titulo.setText(getString(R.string.DescIngresarDatos));
        txtv_Detalle.setText(getString(R.string.TitIngresarDatos));
        btn_Back.setVisibility(View.GONE);
        edtx_Ruta = view.findViewById(R.id.edtx_Ruta);
        txtv_Licencia = view.findViewById(R.id.txtv_Licencia);
        switchNotificaciones= view.findViewById(R.id.switchNotificaciones);

//        btn_Back.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Registra_Configuracion.this).commit();
//
//            }
//        });


        edtx_Estacion.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            edtx_DireccionIP.requestFocus();
                        }
                    });
                }
                return false;
            }
        });


        edtx_DireccionIP.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            btn_Registrar.requestFocus();
                        }
                    });
                }
                return false;
            }
        });


        btn_Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edtx_DireccionIP.getText().toString().equals("") || edtx_Estacion.getText().toString().equals("") || edtx_Ruta.getText().toString().equals("")) {

                        new popUpGenerico(getContext(), getActivity().getCurrentFocus(), "Por favor, llena todos los campos.", false, true, true);
                        return;
                    }


//                        if(!edtx_CodigoEmpresa.getText().toString().equals("52637825A2"))
//                            {
//
//                                new popUpGenerico(getContext(),edtx_CodigoEmpresa, "Código incorrecto.", false, true, true);
//                                return;
//                            }

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                    editor.putString("estacion", edtx_Estacion.getText().toString());
                    editor.putString("direccionWebService", edtx_DireccionIP.getText().toString());
                    editor.putString("Ruta", edtx_Ruta.getText().toString());
                    editor.putBoolean("ConfiguracionInicial", true);
                    editor.putBoolean("MostrarConfiguracionPrimerInicio", false);

                    editor.putString("area", null);
                    editor.apply();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Registra_Configuracion.this).commit();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        switchNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                editor.putBoolean("booleanNotificaciones", isChecked);
                editor.apply();
            }
        });
    }

    public void onButtonPressed(Uri uri) {
//        if (mListener != null)
//            {
//                mListener.onFragmentInteraction(uri);
//            }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            generarQR(view);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        txtv_Licencia.setText(paramArr[0]);
            Log.i("boolean", String.valueOf(pref.getBoolean("booleanNotificaciones",false)));
            switchNotificaciones.setChecked(pref.getBoolean("booleanNotificaciones",false));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        // boolean ActivaProgressBar(Boolean estado);
    }


}
