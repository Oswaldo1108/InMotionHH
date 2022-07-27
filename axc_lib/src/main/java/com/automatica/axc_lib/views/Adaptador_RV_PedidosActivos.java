package com.automatica.axc_lib.views;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.axc_lib.R;

import java.util.ArrayList;

public class Adaptador_RV_PedidosActivos extends RecyclerView.Adapter<Adaptador_RV_PedidosActivos.DataViewHolder>
{
    ArrayList<constructor_Documento> ArrayDocumentos;
    int contadorTarjetas = 0;
    private onClickRV onClickrv;//Interfaz
    ListView.OnItemClickListener oicl;
    private boolean mostrarBotonMasInf = true;

    private String MensajePrevio = null;

    private boolean MostrarEstatus = false;

    Adaptador_RV_PedidosActivos adaptador_rv_menuPrincipalInstance = this;



    Context contexto = null;
    public Adaptador_RV_PedidosActivos(ArrayList<constructor_Documento>Documentos)
    {
        ArrayDocumentos = Documentos;
        setHasStableIds(true);
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder
    {
            TextView txtv_divisor, txtv_Documento,txtv_Dividendo,txtv_Contenedor;
            ConstraintLayout cl_Background;
            DataViewHolder(View itemView)
            {
                super(itemView);
                try
                {
                    cl_Background= (ConstraintLayout) itemView.findViewById(R.id.cl);
                    txtv_Dividendo= itemView.findViewById(R.id.txtv_Dividendo);
                    txtv_divisor = itemView.findViewById(R.id.txtv_Divisor);
                    txtv_Documento = itemView.findViewById(R.id.txtv_Documento);
                    txtv_Contenedor = itemView.findViewById(R.id.txtv_Contenedor);


//                    cl_Background.setOnClickListener(new View.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(View v)
//                        {
//
//                        }
//                    });

                }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i)
    {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_surtido_carrito_orden, viewGroup, false);
        DataViewHolder dvh;
        dvh = new DataViewHolder(vista);
        try
            {
                contexto = viewGroup.getContext();
                try
                    {
                        onClickrv = (onClickRV) contexto;//Inicializar interfaz
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                if(MostrarEstatus)
                    {
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                    {
                                        dvh.cl_Background.setBackground(contexto.getDrawable(R.color.VerdeSemaforo));
                                    }
                            }
                    }
                final DataViewHolder dvh2 = dvh;

                dvh.cl_Background.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String[] arr = {ArrayDocumentos.get(i).getDocumento(),dvh2.txtv_Documento.getText().toString()};

                        if(mostrarBotonMasInf)
                        {
                            onClickrv.clickBotonMasInfo(arr);
                            Log.i("ACTIVOMASINFO", "ACTIVO");
                        }
                    }
                });
                return dvh;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        return dvh;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int i)
    {
        try
            {
                contadorTarjetas++;

                dataViewHolder.txtv_Contenedor.setText(ArrayDocumentos.get(i).getTipoDocumento());
                dataViewHolder.txtv_Documento.setText(ArrayDocumentos.get(i).getDato().get(9).getDato());
                dataViewHolder.txtv_divisor.setText(ArrayDocumentos.get(i).getDato().get(1).getDato());
                dataViewHolder.txtv_Dividendo.setText(ArrayDocumentos.get(i).getDato().get(3).getDato());


            } catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    @Override
    public int getItemCount()
    {
        return ArrayDocumentos.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
    {

            super.onAttachedToRecyclerView(recyclerView);
    }

    public interface onClickRV
    {
        void clickBotonMasInfo(String[] datos);

    }

}
