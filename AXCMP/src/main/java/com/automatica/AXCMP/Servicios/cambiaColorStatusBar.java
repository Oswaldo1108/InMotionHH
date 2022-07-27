package com.automatica.AXCMP.Servicios;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

public class cambiaColorStatusBar
{
    public  cambiaColorStatusBar(Context contexto, int color, Activity actividad)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
        Window window =  actividad.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                window.setStatusBarColor(ContextCompat.getColor(contexto, color));
            }
    }

}
