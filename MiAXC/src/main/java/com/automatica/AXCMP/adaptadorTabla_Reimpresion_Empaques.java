package com.automatica.AXCMP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.automatica.AXCMP.ImpresionEtiquetas.frgmnt_Consulta_Pallet_Reimpresion;

import java.util.ArrayList;

public class adaptadorTabla_Reimpresion_Empaques extends ArrayAdapter<Constructor_Dato>
{
    Context contexto;
    int recurso;
    ListenerBoton listenerInterface;
    public adaptadorTabla_Reimpresion_Empaques(Context context, int resource, ArrayList<Constructor_Dato> objects)
    {
        super(context, resource, objects);
        contexto = context;
        recurso =  resource;
    }

       @Override
    public View getView(int position,  View convertView,  ViewGroup parent)
       {


//              SI SE QUIERE QUE ESTRICTAMENTE HAYA CONEXION DEL BOTON CON LA PANTALLA DESCOMENTAR ESTO
//           if (contexto instanceof ListenerBoton)
//               {
//                   listenerInterface = (ListenerBoton) contexto;
//               } else
//               {
//                   throw new RuntimeException(contexto.toString()
//                           + " Debe implementarse la interfaz para registrar conexion con el boton del listview");
//               }
//


        String CodigoEmpaque = getItem(position).getDato();
        String Producto   = getItem(position).getTag1();
        String Lote  = getItem(position).getTag2();
        String Cantidad    = getItem(position).getTag3();


        LayoutInflater inflater = LayoutInflater.from(contexto);

        convertView = inflater.inflate(recurso,parent,false);


           final TextView txtv_CodigoEmpaque       = (TextView) convertView.findViewById(R.id.txtv_CodigoEmpaque);
           TextView txtv_Producto            = (TextView) convertView.findViewById(R.id.txtv_Producto);
           TextView txtv_Lote                = (TextView) convertView.findViewById(R.id.txtv_Lote);
           TextView txtv_Cantidad            = (TextView) convertView.findViewById(R.id.txtv_Cantidad);


           txtv_CodigoEmpaque.setText(CodigoEmpaque);
           txtv_Producto.setText(Producto);
           txtv_Lote.setText(Lote);
           txtv_Cantidad.setText(Cantidad);



           convertView.findViewById(R.id.btn_ReimprimirEmpaque).setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v)
               {
                   if(contexto!=null)
                       {

                           ((ListenerBoton)contexto).ClickBotonReimprimir(txtv_CodigoEmpaque.getText().toString());
                       }
               }
           });


        return  convertView;
    }
    public interface ListenerBoton
    {
        void ClickBotonReimprimir(String prmCodigoEmpaque);
    }
}
