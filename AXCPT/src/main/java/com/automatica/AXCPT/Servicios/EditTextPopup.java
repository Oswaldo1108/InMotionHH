package com.automatica.AXCPT.Servicios;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Vibrator;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.widget.EditText;

import com.automatica.AXCPT.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class EditTextPopup
{
    private Context context;
    public EditTextPopup(Context context)
    {
        this.context= context;
    }



    public String CreaDialogos(String Titulo, DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo, Context contexto)
    {
        try {

            new AlertDialog.Builder(contexto).setIcon(R.drawable.ic_warning_black_24dp)
                    .setTitle(Titulo).setCancelable(false)
                    .setPositiveButton("Si", listenerSi)
                    .setNegativeButton("No", listenerNo)
                    .show();
            MediaPlayer mp = MediaPlayer.create(contexto, R.raw.beep);
            mp.start();

        }catch (Exception e)
            {
                e.printStackTrace();

            }
        return "FIN";
    }
}
