package com.automatica.axc_lib.Servicios;

import android.os.AsyncTask;

public class CancelarTarea implements Runnable
{
    private AsyncTask Tarea;

    public CancelarTarea(AsyncTask tarea)
    {
        Tarea = tarea;
    }
    @Override
    public void run()
    {
        if(Tarea.getStatus().equals(AsyncTask.Status.RUNNING))
            {
                Tarea.cancel(true);
            }
    }
}
