package com.automatica.axc_lib.Servicios;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.widget.ArrayAdapter;

import com.automatica.axc_lib.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class CreaDialogos
{
Context contexto;
    public CreaDialogos()
    {}

    public CreaDialogos(Context contexto) {
        this.contexto = contexto;
    }

    public  void dialogoDefault(String Titulo, String mensaje, DialogInterface.OnClickListener listenerSi,
                                DialogInterface.OnClickListener listenerNo){
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto,R.style.AlertDialog).setIcon(R.drawable.ic_warning_black_24dp)

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

    public  void dialogoDefault(String Titulo, Spanned mensaje, DialogInterface.OnClickListener listenerSi,
                                DialogInterface.OnClickListener listenerNo){
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto,R.style.AlertDialog).setIcon(R.drawable.ic_warning_black_24dp)

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
            builder.setIcon(R.drawable.ic_warning_black_24dp)
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
            builder.setIcon(R.drawable.ic_warning_black_24dp)
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
            builder.setIcon(R.drawable.ic_warning_black_24dp)
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

    public CreaDialogos(String Titulo, DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo, Context contexto)
    {
        try {
           Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto,R.style.AlertDialog).setIcon(R.drawable.ic_warning_black_24dp)

                    .setTitle(Titulo).setCancelable(false)
                    .setPositiveButton("Si", listenerSi)
                    .setNegativeButton("No", listenerNo)
                    .show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();

        }catch (Exception e)
        {
            e.printStackTrace();

        }
    }


    public void CerrarDia(String Titulo, DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo, Context contexto)
    {
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto,R.style.AlertDialog).setIcon(R.drawable.ic_warning_black_24dp)

                    .setTitle(Titulo).setCancelable(false)
                    .setPositiveButton("OK", listenerSi)
                    //.setNegativeButton("No", listenerNo)
                    .show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();

        }catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    public void Dialogo(String Titulo, DialogInterface.OnClickListener listenerSi, Context contexto, Context context)
    {
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto,R.style.AlertDialog).setIcon(R.drawable.ic_warning_black_24dp)

                    .setTitle(Titulo).setCancelable(false)
                    .setPositiveButton("OK", listenerSi)

                    .show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();

        }catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    public CreaDialogos(String Titulo,String Positivo ,String Negativo,DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo, Context contexto)
    {
        try {
            Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

                    .setTitle(Titulo).setCancelable(false)
                    .setPositiveButton(Positivo, listenerSi)
                    .setNegativeButton(Negativo, listenerNo)
                    .show();
            v.vibrate(150);
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();

        }catch (Exception e)
            {
                e.printStackTrace();

            }
    }




    public void CreaSeleccionador(String Titulo,final Activity activity, String[] Seleccionables,DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setIcon(R.mipmap.m_bco_amarillo);//inmotion_logo_mini
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
}


