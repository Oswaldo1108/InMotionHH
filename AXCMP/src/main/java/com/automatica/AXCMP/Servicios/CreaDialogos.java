package com.automatica.AXCMP.Servicios;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Vibrator;
import androidx.appcompat.app.AlertDialog;

import com.automatica.AXCMP.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class CreaDialogos
{
    public CreaDialogos(String Titulo, DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo, Context contexto)
    {
        try {
           Vibrator v = (Vibrator) contexto.getSystemService(VIBRATOR_SERVICE);
            new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)

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
}
