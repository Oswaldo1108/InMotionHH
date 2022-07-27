package com.automatica.axc_lib.Servicios;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.automatica.axc_lib.R;

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


    public  void popUpListener(final View view, String mensaje, boolean estado, DialogInterface.OnClickListener listener){
        try {
            String TituloAVISO = "Aviso";
            if (estado){
                TituloAVISO = Correcto[new Random().nextInt(Correcto.length)];
            }else {
                TituloAVISO = Incorrecto[new Random().nextInt(Incorrecto.length)];
            }
            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);

            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, R.style.AlertDialog);

            String boton = "OK";
            if(estado)
            {
                builder.setPositiveButton("OK",null);
                MediaPlayer mp = MediaPlayer.create(contexto, R.raw.tilin);
                mp.start();
            }
            else
            {
                builder.setNegativeButton("OK", null);
                MediaPlayer mp = MediaPlayer.create(contexto,R.raw.beep);
                mp.start();

            }
            v.vibrate(150);
            builder.setTitle(TituloAVISO);
            builder.setPositiveButton("Ok",listener);
            builder.setMessage(mensaje);
            aDialog = builder.create();
            aDialog.setIcon(R.mipmap.logo_x_150x150_);
            aDialog.setCancelable(false);
            aDialog.show();
        }catch (Exception e)
        {
            e.printStackTrace();
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

            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, R.style.AlertDialog);

            String boton = "OK";
            if(estado)
            {
                builder.setPositiveButton("OK",null);
                MediaPlayer mp = MediaPlayer.create(contexto, R.raw.tilin);
                mp.start();
            }
            else
            {
                builder.setNegativeButton("OK", null);
                MediaPlayer mp = MediaPlayer.create(contexto,R.raw.beep);
                mp.start();

            }
            v.vibrate(150);
            builder.setTitle(TituloAVISO);
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

    public
    popUpGenerico(Context contexto, final View view, String mensaje, String titulo, Boolean activarVibracion, Boolean activarSonido)
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
                case "Acerca de este dispositivo" :
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



    public  popUpGenerico(Context contexto, final View view, String mensaje, boolean estado, Boolean activarVibracion, Boolean activarSonido)
    {
        try {

            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            String TituloAVISO = "Aviso";
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, R.style.AlertDialog);

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

                    foregroundColorSpanTitulo = new ForegroundColorSpan(Color.RED);
                    foregroundColorSpanBoton = new ForegroundColorSpan(Color.RED);
                    builder.setPositiveButton(ssBuilderBoton, null);
                }


            //ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, TituloAVISO.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderBoton.setSpan(foregroundColorSpanBoton, 0, boton.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (activarSonido)
            {
                MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
                mp.start();
            }
            if (activarVibracion)
            {

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

    public  popUpGenerico(Context contexto, final View view, Spanned mensaje, boolean estado, Boolean activarVibracion, Boolean activarSonido)
    {
        try {

            final  Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            String TituloAVISO = "Aviso";
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto, R.style.AlertDialog);

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

                foregroundColorSpanTitulo = new ForegroundColorSpan(Color.RED);
                foregroundColorSpanBoton = new ForegroundColorSpan(Color.RED);
                builder.setPositiveButton(ssBuilderBoton, null);
            }


            //ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, titulo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderTitulo.setSpan(foregroundColorSpanTitulo, 0, TituloAVISO.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilderBoton.setSpan(foregroundColorSpanBoton, 0, boton.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (activarSonido)
            {
                MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
                mp.start();
            }
            if (activarVibracion)
            {

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

    public  void dialogoDefault(String Titulo, String mensaje, DialogInterface.OnClickListener listenerSi,
                                DialogInterface.OnClickListener listenerNo){
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto,R.style.AlertDialog).setIcon(R.mipmap.logo_x_150x150_)

                    .setTitle(Titulo).setCancelable(false)
                    .setPositiveButton("Si", listenerSi)
                    .setNegativeButton("No", listenerNo)
                    .setMessage(mensaje)
                    .show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();

        }catch (Exception e)
        {
            e.printStackTrace();

        }
    }
    public void dialogoConIcono(String Titulo,String Mensaje ,DialogInterface.OnClickListener listenerSi,DialogInterface.OnClickListener
            listenerNo,int icono){
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto,R.style.AlertDialog);
            builder.setIcon(R.mipmap.logo_x_150x150_)
                    .setMessage(Mensaje)
                    .setTitle(Titulo).setCancelable(false)
                    .setNegativeButton("No", listenerNo)
                    .setPositiveButton("Si", listenerSi);
            if (icono!=0){
                builder.setIcon(icono);
            }
            builder.show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void dialogoTextoColor(String Titulo,String Link ,DialogInterface.OnClickListener listenerSi,
                                  DialogInterface.OnClickListener listenerNo,int icono,int color){
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            final SpannableString s =new SpannableString(Link);
            Linkify.addLinks(s,Linkify.ALL);
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto,R.style.AlertDialog);
            s.setSpan(new ForegroundColorSpan(contexto.getResources().getColor(color)),0,Link.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setIcon(R.mipmap.logo_x_150x150_)
                    .setMessage(s)
                    .setTitle(Titulo).setCancelable(false)
                    .setNegativeButton("No", listenerNo)
                    .setPositiveButton("Si", listenerSi);

            if (icono!=0){
                builder.setIcon(icono);
            }
            builder.show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void dialogoActualizacion(String Titulo,String Link ,DialogInterface.OnClickListener listenerSi,
                                     DialogInterface.OnClickListener listenerNo,int icono,int color){
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            final SpannableString s =new SpannableString(Link);
            Linkify.addLinks(s,Linkify.ALL);
            AlertDialog.Builder builder = new AlertDialog.Builder(contexto,R.style.AlertDialog);
            s.setSpan(new ForegroundColorSpan(contexto.getResources().getColor(color)),0,Link.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setIcon(R.mipmap.logo_x_150x150_)
                    .setMessage(s)
                    .setTitle(Titulo).setCancelable(false)
                    .setNegativeButton("No", listenerNo)
                    .setPositiveButton("Si", listenerSi);

            if (icono!=0){
                builder.setIcon(icono);
            }
            builder.show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void CreaSeleccionador(String Titulo, final Activity activity, String[] Seleccionables, DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setIcon(R.mipmap.logo_x_150x150_);
        builderSingle.setTitle(Titulo);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_singlechoice);

        arrayAdapter.addAll(Seleccionables);

        builderSingle.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, listener);
        builderSingle.show();
    }

    public void popUpEditable(String Titulo,String Mensaje,DialogInterface.OnClickListener si){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(contexto,R.style.AlertDialog);
        alertDialog.setTitle(Titulo);
        alertDialog.setMessage(Mensaje);

        final EditText input = new EditText(contexto);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.mipmap.logo_x_150x150_);
        alertDialog.setPositiveButton("Aceptar",si);
        alertDialog.setNegativeButton("Cancelar",null);
        alertDialog.show();

    }

}
