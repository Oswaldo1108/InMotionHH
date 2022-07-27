package com.automatica.AXCPT.Servicios;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler
{

    private Activity _app;
    private Thread.UncaughtExceptionHandler _defaultEH;

    public CustomExceptionHandler(Activity ac){

        _defaultEH = Thread.getDefaultUncaughtExceptionHandler();
        _app = ac;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {

        Toast.makeText(_app, "Delivering log...", Toast.LENGTH_LONG).show();
        // obtain the Exception info as a String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            new creaNotificacion(_app.getApplicationContext(),"Contacte a AXC","La información ingresada ha generado una excepción: " + ex.getMessage(),1,"",1,"");
        }
        _defaultEH.uncaughtException(thread, ex);
    }

}