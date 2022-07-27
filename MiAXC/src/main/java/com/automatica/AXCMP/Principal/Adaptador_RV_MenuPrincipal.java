package com.automatica.AXCMP.Principal;

import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
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

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.adaptadorTabla;

import java.util.ArrayList;

public class Adaptador_RV_MenuPrincipal extends RecyclerView.Adapter<Adaptador_RV_MenuPrincipal.DataViewHolder>
{
    ArrayList<constructor_Documento> ArrayDocumentos;
//    int contadorTarjetas = 0;
    private onClickRV onClickrv;//Interfaz
    ListView.OnItemClickListener oicl;
    private boolean mostrarBotonMasInf = true;

    public void setOicl(ListView.OnItemClickListener oicl)
    {
        this.oicl = oicl;
    }


    public void setMostrarBotonMasInf(boolean mostrarBotonMasInf)
    {
        this.mostrarBotonMasInf = mostrarBotonMasInf;
    }

    Context contexto = null;
    public Adaptador_RV_MenuPrincipal(ArrayList<constructor_Documento>Documentos)
    {
        Log.i("CARDSTATUS", "TipoDocumento " );

        ArrayDocumentos = Documentos;
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
                    cv = (CardView) itemView.findViewById(R.id.rv_MenuPrincipal);
                    TipoDocumento = itemView.findViewById(R.id.txtv_TipoDocumento);
                    Documento = itemView.findViewById(R.id.txtv_Documento);
                    lstv_DetalleDoc = itemView.findViewById(R.id.lstv_DetalleDoc);
                    ll = itemView.findViewById(R.id.ll_Lista);
                    btn_MasInfo = itemView.findViewById(R.id.btn_MasInfo);
                    imv_Thumbnail =  itemView.findViewById(R.id.imgv_Thumbnail_CardView);
                    view_hor = itemView.findViewById(R.id.view);
                    view_Izq = itemView.findViewById(R.id.view21);
                    view_der = itemView.findViewById(R.id.view3);

                }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
            }

    }

    @NonNull
    @Override
    public Adaptador_RV_MenuPrincipal.DataViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i)
    {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_documento_inicio, viewGroup, false);
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
                if(mostrarBotonMasInf)
                    {
                        dvh.btn_MasInfo.setVisibility(View.VISIBLE);
                    }else
                    {
                        dvh.btn_MasInfo.setVisibility(View.GONE);
                    }
                final String TipoDocumento = ArrayDocumentos.get(i).getTipoDocumento();
                final String Documento = ArrayDocumentos.get(i).getDocumento();
                final String TagDocumento = ArrayDocumentos.get(i).getTagDocumento();
                dvh.Documento.setText(Documento);
                dvh.TipoDocumento.setText(TipoDocumento.toUpperCase());

                final DataViewHolder dvh2 = dvh;

                dvh.cl_Titulo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String[] arr = {TagDocumento, TipoDocumento, Documento};
                        Log.i("adpRV", "TipoDocumento " + TipoDocumento + " DOC " + Documento + " " + TagDocumento);
                        Log.i("adpRV", "TipoDocumento " + arr[0] + " DOC " + arr[1] + " TAG " + arr[2]);
                        if(mostrarBotonMasInf)
                        {
                            onClickrv.clickBotonMasInfo(arr);
                            Log.i("ACTIVOMASINFO", "ACTIVO");
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
                Log.i("POSITION", String.valueOf(i) + " " + String.valueOf(i));
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
                final String TagDocumento = ArrayDocumentos.get(i).getTagDocumento();



                dataViewHolder.Documento.setText(Documento);
//                if (MensajePrevio == null)
//                    {
//                        dataViewHolder.TipoDocumento.setText(TipoDocumento.toUpperCase());
//                    } else
//                    {
//                        dataViewHolder.TipoDocumento.setText(MensajePrevio+ " " + TipoDocumento.toUpperCase());
//                    }

                if(true)
                    {
                        if (TipoDocumento.equals("0 Pallets"))
                            {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                    {
                                        dataViewHolder.view_hor.setBackground(contexto.getDrawable(com.automatica.axc_lib.R.color.grisAXC));
                                        dataViewHolder.view_Izq.setBackground(contexto.getDrawable(com.automatica.axc_lib.R.color.grisAXC));
                                        dataViewHolder.view_der.setBackground(contexto.getDrawable(com.automatica.axc_lib.R.color.grisAXC));
                                        dataViewHolder.cl_Titulo.setBackground(contexto.getDrawable(com.automatica.axc_lib.R.color.grisAXC));
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
