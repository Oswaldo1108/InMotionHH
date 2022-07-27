package com.automatica.AXCPT.objetos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.ArrayList;

public class spinnerCustomAdapter extends BaseAdapter {
    Context contexto;
    ArrayList<ObjetoConstructor> datos;
    LayoutInflater inflater;

    public spinnerCustomAdapter(Context contexto, ArrayList<ObjetoConstructor> datos) {
        this.contexto = contexto;
        this.datos = datos;
        inflater = LayoutInflater.from(contexto);
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        convertView = inflater.inflate(R.layout.spinner_item_custom, null);
        if (convertView==null){
            view = LayoutInflater.from(contexto).inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
        }else {
            view = convertView;
        }
        ImageView icono = (ImageView) convertView.findViewById(R.id.spinner_icono);
        TextView titulo = (TextView) convertView.findViewById(R.id.spinner_Titulo);
        TextView subtitulo = (TextView) convertView.findViewById(R.id.spinner_Subtitulo);
        if (datos.get(position).getIcono()!=null){
            icono.setImageBitmap(datos.get(position).getIcono());
        }
        if (datos.get(position).getSubtitulo()!=null){
            subtitulo.setText(datos.get(position).getSubtitulo());
        }
        titulo.setText(datos.get(position).getTitulo());
        return view;
    }
}
