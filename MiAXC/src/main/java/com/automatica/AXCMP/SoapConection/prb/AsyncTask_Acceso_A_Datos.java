package com.automatica.AXCMP.SoapConection.prb;

import android.content.Context;
import android.os.AsyncTask;
import android.text.PrecomputedText;

import java.util.Objects;

public class AsyncTask_Acceso_A_Datos extends AsyncTask<String[],Void,Void>
{

    public cAccesoADatos accesoADatos;

    @Override
    protected Void doInBackground(String[]... strings)
    {
        return null;
    }

    public AsyncTask_Acceso_A_Datos(Context context)
    {
        accesoADatos = new cAccesoADatos(context);

    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Void aVoid)
    {
        super.onCancelled(aVoid);
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();
    }



}
