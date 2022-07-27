package com.automatica.AXCPT.Fragmentos.Elementos;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.Fragmentos.Adaptadores.AdaptadorRecyclerNotificaciones;
import com.automatica.AXCPT.R;

import java.util.ArrayList;

public class AdaptadorItem extends RecyclerView.Adapter<AdaptadorItem.HolderItem> {
    ArrayList<Item> items;
    private final taskbarInterface anInterface;
    boolean estado=false;

    public AdaptadorItem(ArrayList<Item> items,taskbarInterface anInterface) {
        this.items = items;
        this.anInterface= anInterface;

        Log.i("Cantidad", String.valueOf(items.size()));

    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null, false);
        return new HolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        holder.txtv_dato.setText(items.get(position).getEtiqueta());
        holder.icono.setImageDrawable(items.get(position).getIcono());
        holder.bind(items.get(position),anInterface);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        TextView  txtv_dato;
        ImageView icono;

        public HolderItem(@NonNull View itemView) {
            super(itemView);
            txtv_dato = itemView.findViewById(R.id.nombre);
            icono = itemView.findViewById(R.id.imagen);
        }

        public void bind(final Item item, final taskbarInterface taskbarInterface) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //activity.getSupportFragmentManager().beginTransaction().add(item.getFragmento(),"FragmentoAgregado").commit();
                    try {
                        if (item!=null){
                            anInterface.abrirFragmento(item.getFragmento(),item.getTAG());
                            anInterface.fragmentoAbierto(true);
                            //taskbarInterface.toast();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }



    public interface taskbarInterface{
        void abrirFragmento(Fragment fragment,String TAG);
        void fragmentoAbierto(boolean estado);
    }
}
