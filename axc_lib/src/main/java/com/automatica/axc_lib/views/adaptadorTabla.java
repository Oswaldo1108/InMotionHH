package com.automatica.axc_lib.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.automatica.axc_lib.R;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.ArrayList;

public class adaptadorTabla extends ArrayAdapter<Constructor_Dato>
{
    Context contexto;
    int recurso;
    public adaptadorTabla(Context context, int resource, ArrayList<Constructor_Dato> objects)
    {
        super(context, resource, objects);
        contexto = context;
        recurso =  resource;
    }



    @Override
    public View getView(int position,  View convertView,  ViewGroup parent)
       {
        String Titulo = getItem(position).getTitulo();
        String Dato = getItem(position).getDato();

        LayoutInflater inflater = LayoutInflater.from(contexto);
        convertView = inflater.inflate(recurso,parent,false);

        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tv_Titulo);
        TextView tvDato = (TextView) convertView.findViewById(R.id.tv_Dato);
        tvTitulo.setText(Titulo);
        tvDato.setText(Dato);
        return  convertView;
    }
}
