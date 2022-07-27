package com.automatica.AXCMP.Servicios;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


import com.automatica.AXCMP.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class popUpGenerico
{

    AlertDialog aDialog;


    public  popUpGenerico(Context contexto, final View view, String mensaje, String titulo, Boolean activarVibracion, Boolean activarSonido)
    {
        try {

            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            String TituloAVISO = "Aviso";
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, R.style.AlertDialog);

            String boton = "OK";
            String botonPositivo = "Sí";
            String botonNegativo = "No";
            ForegroundColorSpan foregroundColorSpanTitulo = null;
            ForegroundColorSpan foregroundColorSpanBoton = null;
            SpannableStringBuilder ssBuilderTitulo = new SpannableStringBuilder(TituloAVISO);
            SpannableStringBuilder ssBuilderBoton = new SpannableStringBuilder(boton);


            switch (titulo) {
                case "Error":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.RED);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.RED);
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "Advertencia":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.RED);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.RED);
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "false":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.RED);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.RED);
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "Acerca de este dispositivo":
                    ssBuilderTitulo = new SpannableStringBuilder("Acerca de este dispositivo");
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "Información":
                    ssBuilderTitulo = new SpannableStringBuilder("Información");
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "Registrado":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.GREEN);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.GREEN);
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "Verificación":
                    SpannableStringBuilder ssBuilderBotonPositivo = new SpannableStringBuilder(botonPositivo);
                    SpannableStringBuilder ssBuilderBotonNegativo = new SpannableStringBuilder(botonNegativo);
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.GREEN);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.GREEN);

                    builder.setNegativeButton(ssBuilderBotonNegativo, null);
                    builder.setPositiveButton(ssBuilderBotonPositivo, null);
                    break;
                case "true":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.GREEN);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.GREEN);
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
            }
            //ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, TituloAVISO.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderBoton.setSpan(foregroundColorSpanBoton, 0, boton.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (activarSonido) {
                MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
                mp.start();
            }
            if (activarVibracion) {

                v.vibrate(150);
            }
            builder.setTitle(ssBuilderTitulo);
            builder.setMessage(mensaje);







            aDialog = builder.create();
            aDialog.setIcon(R.mipmap.logo_x_150x150_);
            aDialog.setOnKeyListener(new Dialog.OnKeyListener()
            {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
                {
                    try {
                        if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            v.vibrate(100);

                        }
                        //aDialog.dismiss();
                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            aDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialog)
                {

                    if(view!=null)
                    {
                        try {
                            Log.d("SoapResponse", view.getClass().toString());
                            if (view.getClass().toString().equals("class android.support.v7.widget.AppCompatEditText"))
                            {
                                EditText tmp = (EditText) view;
                                tmp.setText("");
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });
            aDialog.setCancelable(false);
            aDialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
