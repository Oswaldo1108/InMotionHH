package com.automatica.axc_lib.views;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.axc_lib.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class Adaptador_RV_Posiciones extends RecyclerView.Adapter<Adaptador_RV_Posiciones.DataViewHolder>
{
    ArrayList<constructor_Documento> ArrayDocumentos;
    int contadorTarjetas = 0;
    public int LastSelectedCard;
    private onClickRV onClickrv;//Interfaz
    ListView.OnItemClickListener oicl;
    private boolean mostrarBotonMasInf = true;

    private String MensajePrevio = null;

    private boolean MostrarEstatus = false;

    Adaptador_RV_Posiciones adaptador_rv_menuPrincipalInstance = this;

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
    public Adaptador_RV_Posiciones(ArrayList<constructor_Documento>Documentos)
    {                                    //Log.i("CONSULTA_INfO", "ENTRE constructor Adaptador RV");

        ArrayDocumentos = Documentos;
        setHasStableIds(true);
    }
    public Adaptador_RV_Posiciones(List<constructor_Documento> Documentos)
    {                                    //Log.i("CONSULTA_INfO", "ENTRE constructor Adaptador RV");
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
                    TipoDocumento = itemView.findViewById(R.id.txtv_TipoDocumento);
                    Documento = itemView.findViewById(R.id.txtv_Documento);
                    lstv_DetalleDoc = itemView.findViewById(R.id.lstv_DetalleDoc);
                    ll = itemView.findViewById(R.id.ll_Lista);
                    btn_MasInfo = itemView.findViewById(R.id.btn_MasInfo);
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
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_posicion, viewGroup, false);
        final DataViewHolder dvh;
        dvh = new DataViewHolder(vista);

        try
            {
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



                dvh.cl_Titulo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        LastSelectedCard = dvh.getAdapterPosition();
                        String[] arr = {TagDocumento, TipoDocumento, Documento};

                        if(mostrarBotonMasInf)
                        {
                            onClickrv.clickBotonMasInfo(arr);

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

                final String TipoDocumento = ArrayDocumentos.get(i).getTipoDocumento();
                final String Documento = ArrayDocumentos.get(i).getDocumento();

                dataViewHolder.Documento.setText(Documento);

                        dataViewHolder.TipoDocumento.setText(TipoDocumento.toUpperCase());

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
