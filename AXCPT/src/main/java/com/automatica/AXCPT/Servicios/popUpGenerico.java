package com.automatica.AXCPT.Servicios;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.automatica.AXCPT.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.VIBRATOR_SERVICE;

public class popUpGenerico
{

    AlertDialog aDialog;
    Context contexto;

    public popUpGenerico(Context contexto) {
        this.contexto = contexto;
    }
    String Correcto[]={"¡Bien hecho!","¡Buen trabajo!","Trabajo completado con éxito","Tarea exitosa",
            "¡Muy bien!"};
    String Incorrecto[]={"¡Algo salió mal!","Intenta nuevamente","¡Upss! Algo salió mal","Verifica los datos","Comprueba la información","¡Upss!,Vuelve a intentar"};


    public  popUpGenerico(Context contexto, final View view, String mensaje, String titulo, Boolean activarVibracion, Boolean activarSonido)
    {
        try {

            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            String TituloAVISO = "Aviso";
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, com.automatica.axc_lib.R.style.AlertDialog);//estilo de los popups

            String boton = "OK";
            String botonPositivo = "Sí";
            String botonNegativo = "No";
            ForegroundColorSpan foregroundColorSpanTitulo = null;
            ForegroundColorSpan foregroundColorSpanBoton = null;
            SpannableStringBuilder ssBuilderTitulo = new SpannableStringBuilder(TituloAVISO);
            SpannableStringBuilder ssBuilderBoton = new SpannableStringBuilder(boton);


            switch (titulo) {
                case "Error":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.YELLOW);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.WHITE);
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "Advertencia":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.YELLOW);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.WHITE);
                    builder.setPositiveButton(ssBuilderBoton, null);
                    break;
                case "false":
                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.YELLOW);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.WHITE);
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
                MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.beep);
                mp.start();
            }
            if (activarVibracion) {

                v.vibrate(150);
            }
            builder.setTitle(ssBuilderTitulo);
            Date date = new Date();
            SimpleDateFormat dateFormatWithZone = new SimpleDateFormat("yyyy/MM/dd' 'HH:mm:ss", Locale.getDefault());
            builder.setMessage( dateFormatWithZone.format(date) + "\n" + mensaje);

            aDialog = builder.create();
            aDialog.setIcon(R.mipmap.m_bco_amarillo);
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
                            if (view.getClass().toString().contains("EditText"))
                            {
                                EditText tmp = (EditText) view;
                                //tmp.setText("");
                                //tmp.requestFocus();

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



    public  popUpGenerico(Context contexto, final View view, String mensaje, boolean estado, Boolean activarVibracion, Boolean activarSonido)
    {
        try {

            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            String TituloAVISO = "Aviso";
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, com.automatica.axc_lib.R.style.AlertDialog);

            String boton = "OK";

            ForegroundColorSpan foregroundColorSpanTitulo = null;
            ForegroundColorSpan foregroundColorSpanBoton = null;
            SpannableStringBuilder ssBuilderTitulo = new SpannableStringBuilder(TituloAVISO);
            SpannableStringBuilder ssBuilderBoton = new SpannableStringBuilder(boton);


            if(estado)
            {
                foregroundColorSpanTitulo = new ForegroundColorSpan(Color.GREEN);
                foregroundColorSpanBoton = new ForegroundColorSpan(Color.GREEN);
                builder.setPositiveButton(ssBuilderBoton, null);
            }
            else
            {

                foregroundColorSpanTitulo = new ForegroundColorSpan(Color.YELLOW);
                foregroundColorSpanBoton = new ForegroundColorSpan(Color.WHITE);
                builder.setPositiveButton(ssBuilderBoton, null);
            }


            //ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, TituloAVISO.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderBoton.setSpan(foregroundColorSpanBoton, 0, boton.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (activarSonido)
            {
                MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.beep);
                mp.start();
            }
            if (activarVibracion)
            {

                v.vibrate(150);
            }
            builder.setTitle(ssBuilderTitulo);
            Date date = new Date();
            SimpleDateFormat dateFormatWithZone = new SimpleDateFormat("yyyy/MM/dd' 'HH:mm:ss", Locale.getDefault());
            builder.setMessage( dateFormatWithZone.format(date) + "\n" + mensaje);








            aDialog = builder.create();
            aDialog.setIcon(R.mipmap.m_bco_amarillo);
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
                            if (view.getClass().toString().contains("EditText"))
                            {
                                EditText tmp = (EditText) view;
                                //tmp.setText("");
                                //tmp.requestFocus();
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


    public  popUpGenerico(Context contexto, final View view, Exception e, boolean estado, Boolean activarVibracion, Boolean activarSonido)
    {
        try {

            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            String TituloAVISO = "Aviso";
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, com.automatica.axc_lib.R.style.AlertDialog);

            String boton = "OK";

            ForegroundColorSpan foregroundColorSpanTitulo = null;
            ForegroundColorSpan foregroundColorSpanBoton = null;
            SpannableStringBuilder ssBuilderTitulo = new SpannableStringBuilder(TituloAVISO);
            SpannableStringBuilder ssBuilderBoton = new SpannableStringBuilder(boton);


            if(estado)
            {
                foregroundColorSpanTitulo = new ForegroundColorSpan(Color.GREEN);
                foregroundColorSpanBoton = new ForegroundColorSpan(Color.GREEN);
                builder.setPositiveButton(ssBuilderBoton, null);
            }
            else
            {

                foregroundColorSpanTitulo = new ForegroundColorSpan(Color.YELLOW);
                foregroundColorSpanBoton = new ForegroundColorSpan(Color.WHITE);
                builder.setPositiveButton(ssBuilderBoton, null);
            }


            //ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, TituloAVISO.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderBoton.setSpan(foregroundColorSpanBoton, 0, boton.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (activarSonido)
            {
                MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.beep);
                mp.start();
            }
            if (activarVibracion)
            {

                v.vibrate(150);
            }
            builder.setTitle(ssBuilderTitulo);


            Date date = new Date();
            SimpleDateFormat dateFormatWithZone = new SimpleDateFormat("yyyy/MM/dd' 'HH:mm:ss", Locale.getDefault());
            builder.setMessage( dateFormatWithZone.format(date) + "\n" + e.getMessage());

            aDialog = builder.create();
            aDialog.setIcon(R.mipmap.m_bco_amarillo);
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
                            if (view.getClass().toString().contains("EditText"))
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
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void popUpGenericoDefault(final View view, String mensaje, boolean estado){
        try {
            String TituloAVISO = "Aviso";
            if (estado){
                TituloAVISO = Correcto[new Random().nextInt(Correcto.length)];
            }else {
                TituloAVISO = Incorrecto[new Random().nextInt(Incorrecto.length)];
            }
            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);

            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, com.automatica.axc_lib.R.style.AlertDialog);

            String boton = "OK";
            if(estado)
            {
                builder.setPositiveButton("OK",null);
                MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.tilin);
                mp.start();
            }
            else
            {
                builder.setNegativeButton("OK", null);
                MediaPlayer mp = MediaPlayer.create(contexto, com.automatica.axc_lib.R.raw.beep);
                mp.start();

            }
            v.vibrate(150);
            builder.setTitle(TituloAVISO);
            builder.setMessage(mensaje);
            aDialog = builder.create();
            aDialog.setIcon(R.mipmap.m_bco_amarillo);
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
                            if (view.getClass().toString().contains("EditText"))
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