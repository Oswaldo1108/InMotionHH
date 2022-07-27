package com.automatica.AXCMP.Servicios;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.SoapConection.SoapAction;

public class InputDialogFragment extends DialogFragment
{
    EditText edtx_Empaque;
    Button btn_ReimprimirEtiqueta;
    SoapAction sa = new SoapAction();
    String Area;
    View view;
    TextView txtv_Status;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        view =  inflater.inflate(R.layout.edtxfragment,container, false);
        edtx_Empaque = (EditText) view.findViewById(R.id.editText);
        btn_ReimprimirEtiqueta = (Button) view.findViewById(R.id.button2);
        txtv_Status = (TextView)  view.findViewById(R.id.txtv_Status);

        Area = getArguments().getString("Area");

        btn_ReimprimirEtiqueta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                if(edtx_Empaque.getText().toString().equals(""))
                    {
                        new popUpGenerico(getContext(),null, getString(R.string.error_ingrese_empaque),"false" ,true,true);
                        return;
                    }
                SegundoPlano sp = new SegundoPlano("ReimprimeEtiqueta");
                sp.execute();
            }

        });



        return view;
    }


    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {

        String tarea;
        String decision,mensaje;


        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            txtv_Status.setText("Consultando empaque");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
                {
                    switch (tarea)
                        {
                            case "ReimprimeEtiqueta":
                               // Thread.sleep(5000);
                                sa.SOAPReimprimirEtiquetaEPK(edtx_Empaque.getText().toString(),Area,getContext());
                                break;

                        }
                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();


                    Log.i("SoapResponse", "Do in backgound");
                } catch (Exception e)
                {
                    e.printStackTrace();
                    mensaje = e.getMessage();
                }

            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {
            try
                {

                    if (decision.equals("true"))
                        {
                            switch (tarea)
                                {
                                    case   "ReimprimeEtiqueta":
                                        getDialog().dismiss();

                                        break;



                                }
                        }
                    if (decision.equals("false"))
                        {

                            txtv_Status.setText(mensaje);
                            edtx_Empaque.requestFocus();
                            edtx_Empaque.setText("");
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(),"false" ,true,true);

                }


        }
    }



}
