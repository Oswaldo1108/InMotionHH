package com.automatica.axc_lib.Servicios;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.automatica.axc_lib.R;

import androidx.appcompat.app.AlertDialog;

class PopUp_CampoEditable
{
                    /**INCOMPLETO**/
    public static String  PopUp_CampoEditables(Context contexto, Activity act, String Titulo)
    {

        final AlertDialog dialogBuilder = new AlertDialog.Builder(contexto).create();
        LayoutInflater inflater = act.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edittext_popup_dialog, null);

        Button btn_Aceptar = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button btn_Cancelar = (Button) dialogView.findViewById(R.id.buttonCancel);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);

        TextView titulo = (TextView) dialogView.findViewById(R.id.textView);

        titulo.setText(Titulo);


                btn_Cancelar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialogBuilder.dismiss();

                    }
                });
                btn_Aceptar.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                    }
                });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


        return null;

    }


}
