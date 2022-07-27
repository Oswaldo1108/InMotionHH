package com.automatica.AXCPT.Fragmentos.Adaptadores;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.ArrayList;

public class AdaptadorRecyclerNotificaciones extends RecyclerView.Adapter<AdaptadorRecyclerNotificaciones.HolderNotificaciones> {
    ArrayList<Constructor_Dato> datos;
    private final onItemClickListener listener;

    public AdaptadorRecyclerNotificaciones(ArrayList<Constructor_Dato> datos, onItemClickListener listener) {
        this.datos = datos;
        this.listener= listener;
    }

    public class HolderNotificaciones extends RecyclerView.ViewHolder {
        TextView txtv_contenido,txtv_tipo,txtv_fecha;
        ImageView img_estatus;
        public HolderNotificaciones(@NonNull View itemView) {
            super(itemView);
            txtv_contenido= itemView.findViewById(R.id.txtv_contenido);
            txtv_tipo= itemView.findViewById(R.id.txtv_tipo);
            txtv_fecha= itemView.findViewById(R.id.txtv_fecha);
            img_estatus= itemView.findViewById(R.id.img_estatus);
        }
        public void bind(final Constructor_Dato dato, final onItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(dato);
                }
            });
        }
    }

    @NonNull
    @Override
    public HolderNotificaciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_notificacion,null,false);
        return new HolderNotificaciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNotificaciones holder, int position) {
        String Tipo = null;
        switch (datos.get(position).getDato()){
            case "1":
                Tipo="Aviso";
                break;
            case "2":
                Tipo="Almacén";
                break;
            case "3":
                Tipo="Precarga";
                break;
            case "4":
                Tipo="Surtido";
                break;
            case "5":
                Tipo="Reempaque sin Etiqueta";
                break;
            case "6":
                Tipo="Valida";
                break;
            case "7":
                Tipo="Embarque";
                break;
            case "8":
                Tipo="Inventarios";
                break;
            case "9":
                Tipo="Envios";
                break;
            case "10":
                Tipo="Registro de Prod.";
                break;
            case "11":
                Tipo="Recepción";
                break;
            default:
                Tipo="Otro";
                break;
        }
        if (datos.get(position).getTag1().equals("1")){
            Log.i("TAG",datos.get(position).getTag1());

            holder.img_estatus.setImageResource(R.drawable.notificacion_status_1);
        }
        if (datos.get(position).getTag1().equals("2")){
            Log.i("TAG",datos.get(position).getTag1());
            holder.img_estatus.setImageResource(R.drawable.notificacion_status_2);
        }
        try {
            holder.txtv_tipo.setText(Tipo);
            holder.txtv_contenido.setText(datos.get(position).getTitulo());
            holder.txtv_fecha.setText(datos.get(position).getTag4());
            holder.bind(datos.get(position), listener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public interface onItemClickListener{
        void onItemClick(Constructor_Dato dato);
    }

}
