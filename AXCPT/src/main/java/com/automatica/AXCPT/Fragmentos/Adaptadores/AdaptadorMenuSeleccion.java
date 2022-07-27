package com.automatica.AXCPT.Fragmentos.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.Fragmentos.PopUpMenuAXC;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.c_Recepcion.Recepcion.PrimerayUltima;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionContenedor;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionDAP;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionEmpaques;
import com.automatica.AXCPT.objetos.objetoMenu;
import com.automatica.AXCPT.objetos.objetoMenuContext;

import java.util.ArrayList;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class AdaptadorMenuSeleccion extends RecyclerView.Adapter<AdaptadorMenuSeleccion.HolderMenu>{
    ArrayList<objetoMenuContext> datos;
    Context context;

    public AdaptadorMenuSeleccion(ArrayList<objetoMenuContext> datos, Context context) {
        this.datos = datos;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_menu, parent, false);
        return new HolderMenu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMenu holder, int position) {
        holder.texto.setText(datos.get(position).getTitulo());
        holder.imagen.setImageResource(datos.get(position).getImagen());
        holder.bind(datos.get(position), context);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class HolderMenu extends RecyclerView.ViewHolder{
        ImageView imagen;
        TextView texto;
        public HolderMenu(@NonNull View itemView) {
            super(itemView);
            imagen =itemView.findViewById(R.id.imagen);
            texto= itemView.findViewById(R.id.texto);
            getActivity(context).registerForContextMenu(itemView);
        }

        public void bind(final objetoMenuContext dato, final Context context){
            final Activity actividad = getActivity(context);
            switch (dato.getTipo()){
                case 1:
                    itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        intent = dato.getIntent();
                        context.startActivity(intent);
                        Activity miActivity= getActivity(context);
                        if (miActivity!=null){
                            miActivity.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                        }
                    }
                });
                    break;
                case 2:
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Context wrapper = new ContextThemeWrapper(context, R.style.popupMenuStyle);
                            PopupMenu mypopupmenu = new PopupMenu(wrapper, v);
                            //setForceShowIcon(mypopupmenu);
                            Log.i("Cantidad intents", String.valueOf(dato.getIntents().size()));
                            for (int i= 0; i< dato.getIntents().size();i++){
                                int idForMenu;
                                switch (i){
                                    case 0:
                                        idForMenu = R.id.etiquetado;
                                        break;
                                    case 1:
                                        idForMenu=R.id.PyU;
                                        break;
                                    case 2:
                                        idForMenu=R.id.DAP;
                                        break;
                                    case 3:
                                        idForMenu= R.id.contenedor;
                                        break;
                                    default:
                                        idForMenu=R.id.grid_menu;
                                        break;
                                }
                                mypopupmenu.getMenu().add(1,idForMenu,1,dato.getIntents().get(i).getTitulo());
                            }
                            mypopupmenu.show();
                            mypopupmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()){
                                        case R.id.etiquetado:
                                            actividad.startActivity(dato.getIntents().get(0).getIntent());
                                            actividad.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                            break;
                                        case R.id.PyU:
                                            actividad.startActivity(dato.getIntents().get(1).getIntent());
                                            actividad.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                            break;
                                        case R.id.DAP:
                                            actividad.startActivity(dato.getIntents().get(2).getIntent());
                                            actividad.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                            break;
                                        case R.id.contenedor:
                                            actividad.startActivity(dato.getIntents().get(3).getIntent());
                                            actividad.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                            break;
                                        default:
                                            actividad.startActivity(dato.getIntents().get(4).getIntent());
                                            actividad.overridePendingTransition(R.anim.slide_right_in_enter,R.anim.slide_right_out_enter);
                                            break;
                                    }
                                    return false;
                                }
                            });
                        }
                    });
                    break;
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
