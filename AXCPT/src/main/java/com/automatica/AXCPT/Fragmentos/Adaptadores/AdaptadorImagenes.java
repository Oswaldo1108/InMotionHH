package com.automatica.AXCPT.Fragmentos.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.Fragmentos.FragmentoAumentarVista;
import com.automatica.AXCPT.R;
import com.automatica.AXCPT.objetos.objetoMenu;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.ArrayList;

public class AdaptadorImagenes extends RecyclerView.Adapter<AdaptadorImagenes.holderImagenes> {
    ArrayList<Constructor_Dato> datos;

    AppCompatActivity activity;

    public AdaptadorImagenes(ArrayList<Constructor_Dato> datos, AppCompatActivity activity) {
        this.datos = datos;
        this.activity = activity;
    }

    @NonNull
    @Override
    public holderImagenes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagen, parent, false);
        return new holderImagenes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holderImagenes holder, int position) {
        byte[] decodedString = Base64.decode(datos.get(position).getTag1(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imagen.setImageBitmap(decodedByte);

        holder.bind(datos.get(position), activity);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class holderImagenes extends RecyclerView.ViewHolder {
        ImageView imagen;

        public holderImagenes(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen);
        }

        public void bind(final Constructor_Dato dato, final AppCompatActivity activity) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    byte[] decodedString = Base64.decode(dato.getTag1(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    final Drawable vista = new BitmapDrawable(activity.getResources(),decodedByte);
                    activity.getSupportFragmentManager().beginTransaction().add(R.id.fl_Oscuridad, FragmentoAumentarVista.newInstance("", "", vista), "vistaAumentada").commit();
                }
            });
        }
    }
}
