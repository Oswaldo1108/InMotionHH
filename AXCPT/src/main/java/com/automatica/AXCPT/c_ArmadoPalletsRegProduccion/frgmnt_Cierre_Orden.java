package com.automatica.AXCPT.c_ArmadoPalletsRegProduccion;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.Servicios.CreaDialogos;
import com.automatica.AXCPT.Servicios.CustomArrayAdapter;
import com.automatica.AXCPT.Servicios.DatePickerFragment;
import com.automatica.AXCPT.Servicios.esconderTeclado;
import com.automatica.AXCPT.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;

import androidx.fragment.app.Fragment;

public class frgmnt_Cierre_Orden extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String[] paramArr = null;
    private String str_TipoConsulta = "";
    private OnFragmentInteractionListener mListener;
    private  static final String frgtag_ConsultaPalletDet= "FRGCPDT";
    //VIEWS
    private TextView txtv_Titulo,txtv_Detalle;
    private EditText edtx_Maquina,edtx_FechaAct,edtx_FechaTer,edtx_HoraTer,edtx_FechaInicio,edtx_HoraInicio;


    private Spinner sp_Maquinas;
    private ImageButton btn_Back;
    private Button btn_Registrar;

    DatePickerFragment newFragment;

    cAccesoADatos cad = null;

    public frgmnt_Cierre_Orden()
    {
    }
    public static frgmnt_Cierre_Orden newInstance(String[] param, String TipoConsulta)
    {
        frgmnt_Cierre_Orden fragment = new frgmnt_Cierre_Orden();
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
        View view = inflater.inflate(R.layout.fragment_cierre_orden, container, false);
        DeclaraVariables(view);

        cad = new cAccesoADatos(getContext());

      //  new SegundoPlano("consultaMaquinas").execute();

        edtx_Maquina.requestFocus();
        return view;
    }
    private void DeclaraVariables(View view)
    {
        txtv_Titulo  = view.findViewById(R.id.txtv_Titulo);
        txtv_Detalle = view.findViewById(R.id.txtv_Incidencia);

        txtv_Detalle.setText("Cierre de orden");
        txtv_Titulo.setText("Toque los campos para llenarlos.");


        final int mYear,mMonth,mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        edtx_Maquina= view.findViewById(R.id.edtx_Maquina);

        edtx_Maquina.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        edtx_FechaAct= view.findViewById(R.id.edtx_FechaActual);
        edtx_FechaTer= view.findViewById(R.id.edtx_FechaTermina);
        edtx_HoraTer= view.findViewById(R.id.edtx_HoraTermina);
        edtx_FechaInicio= view.findViewById(R.id.edtx_FechaInicio);
        edtx_HoraInicio= view.findViewById(R.id.edtx_HoraInicio);


        btn_Back = view.findViewById(R.id.imgb_AtrasRep);
        btn_Registrar= view.findViewById(R.id.btn_RegistreCierre);
        btn_Back.setVisibility(View.VISIBLE);





        btn_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            //    getActivity().getSupportFragmentManager().popBackStackImmediate();//.beginTransaction().remove(frgmnt_Cierre_Orden.this).commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(frgmnt_Cierre_Orden.this).commit();

            }
        });




        edtx_Maquina .setOnKeyListener(new View.OnKeyListener()
        {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {

                    if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            new esconderTeclado(getActivity());

                        }
                    return false;

                }

         });

        edtx_FechaAct.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                try
                    {

                        //      if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getAction() == MotionEvent.ACTION_UP)
                                {
                                    newFragment = DatePickerFragment.newInstance(
                                            new DatePickerDialog.OnDateSetListener()
                                            {
                                                @Override
                                                public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                                {

                                                     String Mes = String.valueOf(month + 1), Dia = String.valueOf(day);
                                                    if(month + 1<10)
                                                        {
                                                           Mes = "0" + String.valueOf(month + 1);
                                                        }
                                                    if(day<10)
                                                        {
                                                          Dia = "0" + String.valueOf(day);
                                                        }

                                                    edtx_FechaAct.setText(String.valueOf(year) + Mes + Dia);


                                                }
                                            });

                                    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                                    //    }

                                }
                            } catch(Exception e)
                        {
                            e.printStackTrace();
                            new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);
                        }

                return false;
            }
        });
        edtx_FechaTer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {

                    //      if (event.getAction() == MotionEvent.ACTION_UP) {
                    newFragment = DatePickerFragment.newInstance(
                            new DatePickerDialog.OnDateSetListener()
                            {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                {
                             //       final String selectedDate = day + "-" + (month + 1) + "-" + year;
                               //     final String selectedDate = year+ "" + (month + 1) + ""+ day;

                                    String Mes = String.valueOf(month + 1), Dia = String.valueOf(day);
                                    if(month + 1<10)
                                        {
                                            Mes = "0" + String.valueOf(month + 1);
                                        }
                                    if(day<10)
                                        {
                                            Dia = "0" + String.valueOf(day);
                                        }

                                    edtx_FechaTer.setText(String.valueOf(year) + Mes + Dia);

                                 //   edtx_FechaTer.setText(selectedDate);


                                }
                            });

                    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                    //    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);
                }
            }
        });
        edtx_HoraTer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int mHour,mMinute;
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {


                                String hora = String.valueOf(hourOfDay), minutos = String.valueOf(minute);


                                if(hourOfDay <10)
                                    {
                                        hora = "0" + String.valueOf(hourOfDay);
                                    }

                                if(minute <10)
                                    {
                                        minutos = "0" + String.valueOf(minute);
                                    }

                                edtx_HoraTer.setText(hora + ":" + minutos + ":00");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        edtx_FechaInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {

                    //      if (event.getAction() == MotionEvent.ACTION_UP) {
                    newFragment = DatePickerFragment.newInstance(
                            new DatePickerDialog.OnDateSetListener()
                            {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day)
                                {
                                  //  final String selectedDate = day + "-" + (month + 1) + "-" + year;
//
//                                    final String selectedDate = year+ "" + (month + 1) + ""+ day;
//
//
//                                    edtx_FechaInicio.setText(selectedDate);



                                    String Mes = String.valueOf(month + 1), Dia = String.valueOf(day);
                                    if(month + 1<10)
                                        {
                                            Mes = "0" + String.valueOf(month + 1);
                                        }
                                    if(day <10)
                                        {
                                            Dia = "0" + String.valueOf(day);
                                        }

                                    edtx_FechaInicio.setText(String.valueOf(year) + Mes + Dia);

                                }
                            });

                    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                    //    }

                } catch (Exception e) {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);
                }
            }
        });
        edtx_HoraInicio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {

                int mHour,mMinute,mSeconds;
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                mSeconds = c.get(Calendar.SECOND);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String hora = String.valueOf(hourOfDay), minutos = String.valueOf(minute);


                                if(hourOfDay <10)
                                    {
                                        hora = "0" + String.valueOf(hourOfDay);
                                    }

                                if(minute <10)
                                    {
                                        minutos = "0" + String.valueOf(minute);
                                    }

                                edtx_HoraInicio.setText(hora + ":" + minutos + ":00");
                          //      edtx_HoraInicio.setText(hourOfDay + ":" + minute + ":00");
                            }
                        }, mHour, mMinute,false);
                timePickerDialog.show();


            }
        });
        btn_Registrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                        if(edtx_Maquina.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null,"Ingrese una máquina.",false ,true , true);
                                return;
                            }

                        if(edtx_FechaAct.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null,"Ingrese la fecha actual.",false ,true , true);
                                return;
                            }

                        if(edtx_FechaTer.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null,"Ingrese la fecha de terminación.",false ,true , true);
                                return;
                            }

                        if(edtx_HoraTer.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null,"Ingrese la hora de terminación.",false ,true , true);
                                return;
                            }

                        if(edtx_FechaInicio.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null,"Ingrese la fecha de inicio.",false ,true , true);
                                return;
                            }

                        if(edtx_HoraInicio.getText().toString().equals(""))
                            {
                                new popUpGenerico(getContext(),null,"Ingrese la hora de inicio.",false ,true , true);
                                return;
                            }

                new SegundoPlano("RegistraCierre").execute();
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
//        SegundoPlano sp = new SegundoPlano("ConsultaDocumento");
//        sp.execute();
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
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {

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
                    new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);
                }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
                {
                    if(!this.isCancelled())
                        {
                      //      Thread.sleep(5000);


                            switch (tarea)
                                {

                                    case "consultaMaquinas":
                                        dao = cad.c_ConsultaMaquinas();
                                        break;

                                    case "RegistraCierre":

                                        Log.i("RegistraCierre",paramArr[0] + " "+paramArr[1]);

                                        dao = cad.c_CerrarOrdenProduccion_SAP(paramArr[0],paramArr[1],edtx_Maquina.getText().toString(),edtx_FechaAct.getText().toString(),edtx_FechaTer.getText().toString(),edtx_HoraTer.getText().toString(),
                                                                                                            edtx_FechaInicio.getText().toString(),edtx_HoraInicio.getText().toString());
                                        break;

                                    default:
                                     dao = new DataAccessObject(false,"Operación no soportada. ["+ tarea+ "]",null);
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
                  //  SoapObject so_ResultSet = (SoapObject) dao.getSoapObject().getProperty(1);
                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {
                                    case "consultaMaquinas":

                               //         sp_Maquinas.setAdapter(new CustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Maquina", "IdMaquina")));

                                        break;

                                    case "RegistraCierre":

                                        new popUpGenerico(getContext(), null, "cierre registrado correctamente.", dao.iscEstado(), true, true);
                                        getActivity().getSupportFragmentManager().popBackStackImmediate();//.beginTransaction().remove(frgmnt_Cierre_Orden.this).commit();
                                        break;
                                }
                        }else
                        {
                            new popUpGenerico(getContext(),null, dao.getcMensaje(), dao.iscEstado(), true, true);
                            edtx_Maquina.setText("");
                            edtx_FechaAct.setText("");
                            edtx_FechaTer.setText("");
                            edtx_HoraTer.setText("");
                            edtx_FechaInicio.setText("");
                            edtx_HoraInicio.setText("");
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(), null, e.getMessage(),dao.iscEstado(), true, true);
                }
            mListener.ActivaProgressBar(false);
        }

        @Override
        protected void onCancelled()
        {
            mListener.ActivaProgressBar(false);
            new popUpGenerico(getContext(), null, "Cancelado", "false", true, true);
            super.onCancelled();
        }
    }
    public void ConsultarOrden(String[] prmDocumento)
    {
        paramArr[0] = prmDocumento[1];
        new SegundoPlano("ConsultaDocumento").execute();
    }




}
