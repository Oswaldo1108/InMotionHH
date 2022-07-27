package com.automatica.axc_lib.views.DataViewHolders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.axc_lib.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;



public  class DataViewHolderPartidas extends RecyclerView.ViewHolder
{
  public TextView TipoDocumento, Documento, txtv_lblLote;
  public ImageView btn_MasInfo;

  public View Tarjeta,view_hor,view_Izq,view_der,LineaArriba,LineaAbajo;
  public ConstraintLayout cl_Titulo;
  public ImageView img_Status;
  public EditText edtx_Cantidad, edtx_Lote, edtx_EstacionAsignada;

    public DataViewHolderPartidas(View itemView)
    {
        super(itemView);
        try
            {
                img_Status = (ImageView) itemView.findViewById(R.id.icn_Status);
                Tarjeta =itemView.findViewById(R.id.Tarjeta);
                cl_Titulo = (ConstraintLayout) itemView.findViewById(R.id.cl);
                LineaArriba      = itemView.findViewById(R.id.view6);
                LineaAbajo = itemView.findViewById(R.id.view5);
                TipoDocumento = itemView.findViewById(R.id.txtv_TipoDocumento);
                Documento = itemView.findViewById(R.id.txtv_Documento);
                txtv_lblLote = itemView.findViewById(R.id.textView8);
                btn_MasInfo = itemView.findViewById(R.id.btn_MasInfo);
                view_hor = itemView.findViewById(R.id.view);
                view_Izq = itemView.findViewById(R.id.view2);
                view_der = itemView.findViewById(R.id.view3);

                edtx_Cantidad = itemView.findViewById(R.id.edtx_Cantidad);
                edtx_Lote = itemView.findViewById(R.id.edtx_LoteAsignado);
                edtx_EstacionAsignada = itemView.findViewById(R.id.edtx_EstacionAsignada);


            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }


}