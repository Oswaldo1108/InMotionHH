package com.automatica.AXCMP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        Constructor_Dato entrada = new Constructor_Dato(Titulo, Dato);


        LayoutInflater inflater = LayoutInflater.from(contexto);
        convertView = inflater.inflate(recurso,parent,false);

        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tv_Titulo);
        TextView tvDato = (TextView) convertView.findViewById(R.id.tv_Dato);
        tvTitulo.setText(Titulo);
        tvDato.setText(Dato);
        return  convertView;
    }
}
