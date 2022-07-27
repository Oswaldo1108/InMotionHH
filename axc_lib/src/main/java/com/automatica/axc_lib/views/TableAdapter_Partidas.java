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

public class TableAdapter_Partidas extends ArrayAdapter<ArrayList<Constructor_Dato>>
{
    Context contexto;
    int recurso;

    public TableAdapter_Partidas(Context context, int resource, ArrayList<ArrayList<Constructor_Dato>> objects)
    {
        super(context, resource, objects);
        contexto = context;
        recurso =  resource;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent)
       {
        LayoutInflater inflater = LayoutInflater.from(contexto);

        convertView = inflater.inflate(recurso,parent,false);

        ArrayList<Constructor_Dato> arr = getItem(position);
        for(Constructor_Dato dato: arr )
            {
                switch (dato.getTitulo())
                    {
                        case "Partida":
                          //  ((TextView) convertView.findViewById(R.id.tv_Dato)).setText(dato.getDato());
                            break;
                        case "Producto":
                            ((TextView) convertView.findViewById(R.id.tv_Producto)).setText(dato.getDato());
                            break;

                        case "Descripción":
                            ((TextView) convertView.findViewById(R.id.tv_Producto)).append(" - " +dato.getDato());
                            break;
                        case "CantidadPedida":
                            ((TextView) convertView.findViewById(R.id.tv_CantTot)).setText(dato.getDato());
                            break;
                        case "CantidadSurtida":
                            //((TextView) convertView.findViewById(R.id.tv_CantSurt)).setText(dato.getDato());
                            break;
                        case "CantidadPendiente":
                            //((TextView) convertView.findViewById(R.id.tv_CantPend)).setText(dato.getDato());
                            break;
                        case "UnidadMedida":
                            //((TextView) convertView.findViewById(R.id.tv_UM)).setText(dato.getDato());
                            break;
                        case "Estatus":
                            //((TextView) convertView.findViewById(R.id.tv_Estatus)).setText(dato.getDato());
                            break;
                    }
            }
        return  convertView;
    }
}
