package com.automatica.AXCPT.Fragmentos.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.objetos.objetoMenu;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.adInventarios;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import java.util.ArrayList;

public class AdaptadorMenuInventarios extends RecyclerView.Adapter<AdaptadorMenuInventarios.HolderMenu> {
    ArrayList<objetoMenu> datos;
    Context context;

    public AdaptadorMenuInventarios(ArrayList<objetoMenu> datos, Context context) {
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

    /**
     * Catalogo Intent Inventarios
     * 1 : Ciclicos por articulo
     * 2: Fisico Total
     * 3:Anual
     * 4:Costos
     */

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
                    new SegundoPlano(dato.getTipo(),dato.getIntent()).execute();
                }
            });
        }

    }

    private class SegundoPlano extends AsyncTask<String,Void,Void>{
        int Tarea;
        DataAccessObject dao;
        Intent intent;
        adInventarios ca = new adInventarios(context);
        popUpGenerico pop = new popUpGenerico(context);
        public SegundoPlano(int tarea, Intent intent) {
            Tarea = tarea;
            this.intent= intent;
        }
        @Override
        protected Void doInBackground(String... strings) {
            switch (Tarea){
                case 1:
                    dao = ca.c_ConsultaInvCiclicoPosicion("1");

                    break;
                case 2:
                    dao = ca.c_ConsultaInvCiclicoNumParte("1");

                    break;
                case 3:
                    dao = ca.c_ConsultaInvFisico("1");
                    break;
                default:
                    dao = new DataAccessObject();
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dao!=null){
                if (dao.iscEstado()){
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle b = new Bundle();
                    b.putInt("Tipo",Tarea);
                    b.putString("IdInv",dao.getcMensaje());
                    intent.putExtras(b);
                    context.startActivity(intent);
                    Activity miActivity= getActivity(context);

                    if (miActivity!=null){
                        miActivity.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                    }
                }else {
                    pop.popUpGenericoDefault(null,dao.getcMensaje(),false);
                }
                super.onPostExecute(aVoid);
            }
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
