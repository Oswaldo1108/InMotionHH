package com.automatica.AXCPT.Servicios;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.automatica.AXCPT.R;

public class cambiaColorStatusBar
{
    public  cambiaColorStatusBar(Context contexto, int color, Activity actividad)
    {
        Window window =  actividad.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(contexto,R.color.camposAzul));
    }

}
