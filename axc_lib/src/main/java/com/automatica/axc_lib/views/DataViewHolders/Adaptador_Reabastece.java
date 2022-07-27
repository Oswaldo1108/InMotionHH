package com.automatica.axc_lib.views.DataViewHolders;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.axc_lib.R;
import com.automatica.axc_lib.views.adaptadorTabla;
import com.automatica.axc_lib.views.constructor_Documento;

import java.util.ArrayList;
import java.util.List;

public class Adaptador_Reabastece extends RecyclerView.Adapter<Adaptador_Reabastece.DataViewHolder>
{
    ArrayList<constructor_Documento> ArrayDocumentos;
    int contadorTarjetas = 0;
    public int LastSelectedCard;
    private onClickRV onClickrv;//Interfaz
    ListView.OnItemClickListener oicl;
    private boolean mostrarBotonMasInf = true;

    private String MensajePrevio = null;

    private boolean MostrarEstatus = false;

    Adaptador_Reabastece adaptador_rv_menuPrincipalInstance = this;

    public void setMensajePrevio(String mensajePrevio)
    {
        MensajePrevio = mensajePrevio;
    }

    public String getMensajePrevio()
    {
        return MensajePrevio;
    }

    public void setMostrarBotonMasInf(boolean mostrarBotonMasInf)
    {
        this.mostrarBotonMasInf = mostrarBotonMasInf;
    }

    public void setCardStatus(String Contenedor,int index)
    {

        ArrayDocumentos.get(index).setDocumento(Contenedor);
        Log.i("CARDSTATUS", "TipoDocumento " + ArrayDocumentos.get(index).getDocumento()+ " " + ArrayDocumentos.get(index).getTipoDocumento() + " " + ArrayDocumentos.get(index).getTagDocumento());

        //   Toast.makeText(contexto, Contenedor + " " + index, Toast.LENGTH_SHORT).show();
        this.notifyItemChanged(index);
    }

    public void setMostrarEstatus(boolean MostrarEstatus)
    {
        this.MostrarEstatus = MostrarEstatus;
    }

    public int getLastSelectedCard()
    {
        return LastSelectedCard;
    }

    Context contexto = null;
    public Adaptador_Reabastece(ArrayList<constructor_Documento>Documentos)
    {
        Log.i("CONSULTA_INfO", "ENTRE constructor Adaptador RV");
        //Toast.makeText(contexto, "PRUEBA 2 ADAPTER", Toast.LENGTH_SHORT).show();

        ArrayDocumentos = Documentos;
        setHasStableIds(true);
    }
    public Adaptador_Reabastece(List<constructor_Documento> Documentos)
    {
        ArrayDocumentos = new ArrayList<>();
        ArrayDocumentos.addAll( Documentos);
        this.notifyDataSetChanged();
        setHasStableIds(true);
    }
    public static class DataViewHolder extends RecyclerView.ViewHolder
    {
        CardView cv;
        TextView TipoDocumento, Documento;
        ListView lstv_DetalleDoc;
        ImageView btn_MasInfo;
        LinearLayout ll;
        ImageView imv_Thumbnail;
        View view_hor,view_Izq,view_der;
        ConstraintLayout cl_Titulo;
        DataViewHolder(View itemView)
        {
            super(itemView);
            try
            {
                cl_Titulo = (ConstraintLayout) itemView.findViewById(R.id.cl);
                //  cv = (CardView) itemView.findViewById(R.id.rv_MenuPrincipal);
                TipoDocumento = itemView.findViewById(R.id.txtv_TipoDocumento);
                Documento = itemView.findViewById(R.id.txtv_Documento);
                lstv_DetalleDoc = itemView.findViewById(R.id.lstv_DetalleDoc);
                ll = itemView.findViewById(R.id.ll_Lista);
                btn_MasInfo = itemView.findViewById(R.id.btn_MasInfo);
                //  imv_Thumbnail =  itemView.findViewById(R.id.imgv_Thumbnail_CardView);
                view_hor = itemView.findViewById(R.id.view);
                view_Izq = itemView.findViewById(R.id.view3);
                view_der = itemView.findViewById(R.id.view2);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i)
    {
        try
        {

            View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_documento_reabastece, viewGroup, false);
            final DataViewHolder dvh;
            dvh = new DataViewHolder(vista);
//        Toast.makeText(contexto, "PRUEBA 3 on crate ADAPTER", Toast.LENGTH_SHORT).show();
            Log.i("HOLAHOLAHOLA", "PRUEBA 2 3");


            Log.i("HOLAHOLAHOLA", "PRUEBA 2 1");

            contexto = viewGroup.getContext();

            if (mostrarBotonMasInf)
            {
                dvh.btn_MasInfo.setVisibility(View.VISIBLE);

                try
                {
                    onClickrv = (onClickRV) contexto;//Inicializar interfaz
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            } else
            {
                dvh.btn_MasInfo.setVisibility(View.GONE);
            }
            final String TipoDocumento = ArrayDocumentos.get(i).getTipoDocumento();
            final String Documento = ArrayDocumentos.get(i).getDocumento();
            final String TagDocumento = ArrayDocumentos.get(i).getTagDocumento();

            Log.i("TipoDoc",TipoDocumento);
            Log.i("Documento",Documento);
            Log.i("TagDocumento",TagDocumento);


            dvh.cl_Titulo.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    LastSelectedCard = dvh.getAdapterPosition();
                    String[] arr = {TipoDocumento, TagDocumento, Documento};
                    //Log.i("adpRV", "TipoDocumento " + TipoDocumento + " DOC " + Documento + " " + TagDocumento);

                    //Log.i("adpRV", "TipoDocumento " + arr[0] + " DOC " + arr[1] + " TAG " + arr[2]);
                    if(mostrarBotonMasInf)
                    {
                        onClickrv.clickBotonMasInfo(arr);
                        //  Log.i("ACTIVOMASINFO", "ACTIVO");
                    }
                }
            });

            if (ArrayDocumentos.get(i).getDato() != null)
            {
                dvh.lstv_DetalleDoc.setAdapter(new adaptadorTabla(contexto, R.layout.list_item_2_datos_fragment, ArrayDocumentos.get(i).getDato()));

                dvh.lstv_DetalleDoc.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        //   Toast.makeText(viewGroup.getContext(), "ListView Postion " + position, Toast.LENGTH_LONG).show();
                        Log.i("POSITION", "ListView Postion " + position);

                    }
                });
            } else
            {
                dvh.view_hor.setVisibility(View.GONE);
            }
            Log.i("HOLAHOLAHOLA", "PRUEBA 2 2");

//                Log.i("POSITION", String.valueOf(i) + " " + String.valueOf(i));
            return dvh;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;

        }
//        return dvh;
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
            //    contadorTarjetas++;
            //   contadorTarjetas = i;
            final String TipoDocumento = ArrayDocumentos.get(i).getTipoDocumento();
            final String Documento = ArrayDocumentos.get(i).getTagDocumento();
            final String TagDocumento = ArrayDocumentos.get(i).getDocumento();

            Log.i("HOLAHOLAHOLA", "PRUEBA 1");

            Log.i("HOLAHOLAHOLA", TipoDocumento + " " + Documento);
            dataViewHolder.Documento.setText(Documento);
            if (MensajePrevio == null)
            {
                dataViewHolder.TipoDocumento.setText(TipoDocumento.toUpperCase());
            } else
            {
                dataViewHolder.TipoDocumento.setText(//MensajePrevio+ " " +

                        TipoDocumento.toUpperCase());
            }

            if(MostrarEstatus)
            {
                if (Documento.equals("0 Pallets"))
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        dataViewHolder.view_hor.setBackground(contexto.getDrawable(R.color.grisAXC));
                        dataViewHolder.view_Izq.setBackground(contexto.getDrawable(R.color.grisAXC));
                        dataViewHolder.view_der.setBackground(contexto.getDrawable(R.color.grisAXC));
                        dataViewHolder.cl_Titulo.setBackground(contexto.getDrawable(R.color.grisAXC));
                    }
                }
            }

        }catch (Exception e)
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
