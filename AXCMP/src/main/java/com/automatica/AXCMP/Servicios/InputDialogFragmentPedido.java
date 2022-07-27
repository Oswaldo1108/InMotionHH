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
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.c_Surtido.Surtido_Pedidos.Surtido_Docificacion_Pedidos;

import org.ksoap2.serialization.SoapObject;

public class InputDialogFragmentPedido extends DialogFragment
{
    EditText edtx_Empaque;
    Button btn_ReimprimirEtiqueta;
    SoapAction sa = new SoapAction();

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        view =  inflater.inflate(R.layout.edtxfragment,container, false);

        edtx_Empaque = (EditText) view.findViewById(R.id.editText);
        btn_ReimprimirEtiqueta = (Button) view.findViewById(R.id.button2);




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


    public class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        AlphaAnimation inAnimation;
        AlphaAnimation outAnimation;
        String tarea;
        String decision,mensaje;
        FrameLayout progressBarHolder;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);


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
                                sa.SOAPReimprimirEtiquetaEPK(edtx_Empaque.getText().toString(),((Surtido_Docificacion_Pedidos)getActivity()).Area,getContext());
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

                                        SoapObject so = (SoapObject) sa.parser().getProperty(0);


                                        new popUpGenerico(getContext(), null, "Etiqueta"+" reimpresa ["+so.getPrimitivePropertyAsString("Empaque")+"].", "true", true, true);

                                        break;



                                }
                        }
                    if (decision.equals("false"))
                        {
                            new popUpGenerico(getContext(), null, mensaje, decision, true, true);
                            edtx_Empaque.requestFocus();
                            edtx_Empaque.setText("");
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(),"false" ,true,true);

                }
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);


        }
    }



}
