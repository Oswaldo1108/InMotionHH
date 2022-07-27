package com.automatica.AXCPT.Fragmentos.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.automatica.AXCPT.R;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class AdaptadorFragConsultas extends RecyclerView.Adapter<AdaptadorFragConsultas.HolderConsulta> {
    ArrayList<Constructor_Dato> datos;

    public AdaptadorFragConsultas(ArrayList<Constructor_Dato> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public HolderConsulta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_consulta, null, false);
        return new HolderConsulta(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderConsulta holder, int position) {

        holder.txtv_dato.setText(datos.get(position).getDato());
        holder.txtv_nombre.setText(datos.get(position).getTitulo());
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class HolderConsulta extends RecyclerView.ViewHolder {
        TextView txtv_nombre, txtv_dato;

        public HolderConsulta(@NonNull View itemView) {
            super(itemView);
            txtv_dato = itemView.findViewById(R.id.txtv_dato);
            txtv_nombre = itemView.findViewById(R.id.txtv_nombre);
        }
    }


//Creacion adaptador de pallets
    private ArrayList<Constructor_Dato> CrearAdapterPallet(DataAccessObject dao, Context context, RecyclerView recyclerView) {
        ArrayList<Constructor_Dato> Datos = new ArrayList<>();
        PropertyInfo pisp;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //if (dao.getcTablaUnica() != null) {
        SoapObject objeto = dao.getSoapObject_parced();
        for (int i = 0; i < objeto.getPropertyCount(); i++) {
            pisp = new PropertyInfo();
            objeto.getPropertyInfo(i, pisp);
            String Etiqueta = pisp.name.replace("_x0020_", " ");
            Log.i("Etiqueta", Etiqueta);
            String Dato = objeto.getPropertyAsString(i);
            Log.i("Dato", Dato);
            if (Dato.equals("anyType{}")) {
                Dato = "";
            }
            if (Etiqueta.equals("IdPallet")){
                continue;
            }
            if (Etiqueta.equals("Status")){
                continue;
            }
            if (Etiqueta.equals("Revision")) {
                Etiqueta = "Tipo de reg.";
                if (Dato.equals("1")){
                    Dato= "No etiquetado";
                }else if(Dato.equals("0")) {
                    Dato="Etiquetado";
                }
            }
            if (Etiqueta.equals("Ubicacion")) {
                Etiqueta = "Posición";
            }
            if (Etiqueta.equals("DescStatus")){
                Etiqueta= "Estatus";
            }

            Constructor_Dato dato = new Constructor_Dato(Etiqueta, Dato);
            Datos.add(dato);
        }
        return Datos;
    }
}
