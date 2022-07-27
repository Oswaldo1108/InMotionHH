package com.automatica.AXCPT.Servicios;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.automatica.AXCPT.R;

import java.lang.annotation.Target;

public class ProgressBarHelper
{
    Activity activity;
    FrameLayout progressBarHolder;
    String Tarea;// TAREA QUE INICIO EL PROGRESSBAR
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    Toast toast;
    boolean pBarActiva=true;
    public ProgressBarHelper(final Activity activity)
    {
        toast = Toast.makeText(activity,"Cargando información, espere un momento.",Toast.LENGTH_SHORT);
        this.activity = activity;
        progressBarHolder = activity.findViewById(R.id.progressBarHolder);
        /**Se comentarizo esto por que estaba causando que la pantalla se detuviera.*/
        //        inAnimation = new AlphaAnimation(0f, 1f);
        //        outAnimation = new AlphaAnimation(1f, 0f);
        //        inAnimation.setDuration(50);
        //        outAnimation.setDuration(50);
        //        progressBarHolder.setAnimation(inAnimation);
        //        progressBarHolder.setAnimation(outAnimation);

        progressBarHolder.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                {
                    toast.show();
                }
                    return false;
            }
        });

        progressBarHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toast.show();
            }
        });
    }

    public void ActivarProgressBar()
    {
        pBarActiva = false;
        progressBarHolder.setVisibility(View.VISIBLE);
        progressBarHolder.requestFocus();
    }

    public void DesactivarProgressBar()
    {
        pBarActiva = true;
        progressBarHolder.setVisibility(View.GONE);
        toast.cancel();
    }

    public void ActivarProgressBar(String Tarea)
    {
        this.Tarea = Tarea;
        pBarActiva = false;
        progressBarHolder.setVisibility(View.VISIBLE);
        progressBarHolder.requestFocus();
    }

    public void DesactivarProgressBar(String Tarea)
    {
        if(this.Tarea.equals(Tarea))
        {
            pBarActiva = true;
            progressBarHolder.setVisibility(View.GONE);
            toast.cancel();
        }
    }
    /**
     * False si la progressbar esta activa en la actividad
     * True  si no esta activa
     * */
    public boolean ispBarActiva()
    {
        return pBarActiva;
    }
}
