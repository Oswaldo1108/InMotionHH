package com.automatica.AXCMP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adaptadorTabla extends ArrayAdapter<constructorTablaEntradaAlmacen>
{
    Context contexto;
    int recurso;
    public adaptadorTabla(Context context, int resource, ArrayList<constructorTablaEntradaAlmacen> objects)
    {
        super(context, resource, objects);
        contexto = context;
        recurso =  resource;
    }



    /* public adaptadorTabla(Almacen_Colocar_v1 context, int list_item_2datos, ArrayList<com.automatica.AXCMP.c_Almacen.constructorTablaEntradaAlmacen> lista) {
         super();
     }
 */
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        String Titulo = getItem(position).getTitulo();
        String Dato = getItem(position).getDato();

        constructorTablaEntradaAlmacen entrada = new constructorTablaEntradaAlmacen(Titulo, Dato);


        LayoutInflater inflater = LayoutInflater.from(contexto);
        convertView = inflater.inflate(recurso,parent,false);

        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tv_Titulo);
        TextView tvDato = (TextView) convertView.findViewById(R.id.tv_Dato);
        tvTitulo.setText(Titulo);
        tvDato.setText(Dato);
        return  convertView;
    }
}
