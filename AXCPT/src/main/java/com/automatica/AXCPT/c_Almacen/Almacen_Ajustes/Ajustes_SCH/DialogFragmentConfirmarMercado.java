package com.automatica.AXCPT.c_Almacen.Almacen_Ajustes.Ajustes_SCH;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.databinding.ConfirmaMercadoBinding;

public class DialogFragmentConfirmarMercado extends DialogFragment {

    Context contexto;
    confirmaMercado mercado;
    String id;
    private ConfirmaMercadoBinding binding;

    public DialogFragmentConfirmarMercado(Context contexto,String id) {
        this.contexto = contexto;
        this.id = id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return fragmentoConfirmaMercado();
    }

    public AlertDialog fragmentoConfirmaMercado(){
        AlertDialog.Builder builder= new AlertDialog.Builder(contexto);
        binding = ConfirmaMercadoBinding.inflate(getLayoutInflater(),null,false);
        View view= binding.getRoot();
        builder.setView(view);
        binding.imageView2.requestFocus();
        binding.btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mercado.confirmar(binding.checkBox.isChecked(),id);
                dismiss();
            }
        });
        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }

    public  interface confirmaMercado{
        boolean confirmar(boolean mercado,String id);
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof confirmaMercado)
        {
            mercado = (confirmaMercado) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mercado = null;
    }
}
