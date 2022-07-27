package com.automatica.AXCPT.Principal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.c_Almacen.constructorTablaEntradaAlmacen;

import java.util.ArrayList;

public class adaptadorTablaMenuPrincipal extends ArrayAdapter<constructorTablaMenuPrincipal>
{
    Context contexto;
    int recurso;
    int icono;

    public adaptadorTablaMenuPrincipal(Context context, int resource, ArrayList<constructorTablaMenuPrincipal> objects) {
        super(context, resource, objects);
        contexto = context;
        recurso = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String Titulo = getItem(position).getTitulo();
        String Dato = getItem(position).getDescripcion();
      //  String icono = getItem(position).getIcono();
        int iconoi = getItem(position).getIconoi();
        constructorTablaEntradaAlmacen entrada = new constructorTablaEntradaAlmacen(Titulo, Dato);


        LayoutInflater inflater = LayoutInflater.from(contexto);
        convertView = inflater.inflate(recurso, parent, false);

        TextView tvTitulo = (TextView) convertView.findViewById(R.id.txtv_TituloActv);
        TextView tvDescripcion = (TextView) convertView.findViewById(R.id.txtv_desc);


        ImageView ivIcono = (ImageView) convertView.findViewById(R.id.imgv_iconoAct);

     //   String url = String.valueOf(getItem(recurso));
        tvTitulo.setText(Titulo);
        tvDescripcion.setText(Dato);
     //   Picasso.get().load(recurso).into(ivIcono);

        ivIcono.setTag(position);
        ivIcono.setImageResource(iconoi);


        return convertView;
    }
}