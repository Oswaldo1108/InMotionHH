package com.automatica.AXCPT.Fragmentos.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.objetos.objetoMenu;

import java.util.ArrayList;

public class AdaptadorMenu extends RecyclerView.Adapter<AdaptadorMenu.HolderMenu> {
    ArrayList<objetoMenu> datos;
    Context context;

    public AdaptadorMenu(ArrayList<objetoMenu> datos, Context context) {
        this.datos = datos;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_menu, parent, false);
        return new HolderMenu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMenu holder, int position) {
        holder.txtv_dato.setText(datos.get(position).getTitulo());
        holder.bind(datos.get(position), context);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class HolderMenu extends RecyclerView.ViewHolder {
        TextView txtv_dato;

        public HolderMenu(@NonNull View itemView) {
            super(itemView);
            txtv_dato = itemView.findViewById(R.id.txtv_dato);
        }

        public void bind(final objetoMenu dato, final Context context) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = dato.getIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    Activity miActivity= getActivity(context);
                    if (miActivity!=null){
                        miActivity.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                    }
                }
            });
        }

    }

    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }
}
