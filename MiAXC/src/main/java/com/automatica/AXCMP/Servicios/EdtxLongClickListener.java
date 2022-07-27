package com.automatica.AXCMP.Servicios;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

public class EdtxLongClickListener implements View.OnLongClickListener
{
    Context context;
    String Prompt = "Escaneé código";
    int RequestSoliticer;

    DataTransfer dataTransfer;
    public EdtxLongClickListener(Context context, String Prompt )
    {
        this.context  = context;
//        dataTransfer = (EdtxLongClickListener.DataTransfer) context;

        if(Prompt!=null)
        {
            this.Prompt = Prompt;
        }
    }
        @Override
        public boolean onLongClick(View v)
                {
                    RequestSoliticer = v.getId();
                    IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity(context));
                    intentIntegrator.setCaptureActivity(CaptureClass.class);
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    intentIntegrator.setPrompt(Prompt);
                    intentIntegrator.initiateScan();
//                    dataTransfer.ValidateLaunchedView(v.getId());
                    return false;
                }




    public Activity getActivity(Context context)
    {
        if (context == null)
            {
                return null;
            }
        else if (context instanceof ContextWrapper)
            {
                if (context instanceof Activity)
                    {
                        return (Activity) context;
                    }
                else
                    {
                        return getActivity(((ContextWrapper) context).getBaseContext());
                    }
            }
        return null;
    }

    public int getLastId()
    {
        return RequestSoliticer;
    }

    public interface DataTransfer
    {
        void ValidateLaunchedView(int ViewId);
    }

 }
