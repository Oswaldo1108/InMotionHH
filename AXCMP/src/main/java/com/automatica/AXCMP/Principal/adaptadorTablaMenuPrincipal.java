package com.automatica.AXCMP.Principal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;

import java.util.ArrayList;

public class adaptadorTablaMenuPrincipal extends ArrayAdapter<constructorTablaMenuPrincipal>
{
    Context contexto;
    int recurso;


    public adaptadorTablaMenuPrincipal(Context context, int resource, ArrayList<constructorTablaMenuPrincipal> objects) {
        super(context, resource, objects);
        contexto = context;
        recurso = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        try {
            String Titulo = getItem(position).getTitulo();
            String Dato = getItem(position).getDescripcion();
            int iconoi = getItem(position).getIconoi();

            LayoutInflater inflater = LayoutInflater.from(contexto);
            convertView = inflater.inflate(recurso, parent, false);

            TextView tvTitulo = (TextView) convertView.findViewById(R.id.txtv_TituloActv);
            TextView tvDescripcion = (TextView) convertView.findViewById(R.id.txtv_desc);
            ImageView ivIcono = (ImageView) convertView.findViewById(R.id.imgv_iconoAct);

            tvTitulo.setText(Titulo);
            tvDescripcion.setText(Dato);

            ivIcono.setTag(position);
            ivIcono.setImageResource(iconoi);

        }catch (Exception e)
        {
            e.printStackTrace();
            new popUpGenerico(contexto,null,e.getMessage(),"false",true,true);
        }

        return convertView;
    }
}