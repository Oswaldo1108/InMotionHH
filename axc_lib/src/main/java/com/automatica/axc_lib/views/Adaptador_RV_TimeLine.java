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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.automatica.axc_lib.R;

import java.util.ArrayList;

public class Adaptador_RV_TimeLine extends RecyclerView.Adapter<Adaptador_RV_TimeLine.DataViewHolder>
{
    ArrayList<constructor_Documento> ArrayDocumentos;
    int contadorTarjetas = 0;
    public int LastSelectedCard;
    private onClickRV onClickrv;//Interfaz

    private boolean mostrarBotonMasInf = true;

    private String MensajePrevio = null;

    private boolean MostrarEstatus = false;
    Context contexto = null;

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

    public Adaptador_RV_TimeLine(ArrayList<constructor_Documento>Documentos)
    {
        ArrayDocumentos = Documentos;
        setHasStableIds(true);
    }





    public static class DataViewHolder extends RecyclerView.ViewHolder
    {
            TextView TipoDocumento, Documento;
            ListView lstv_DetalleDoc;
            ImageView btn_MasInfo;
            LinearLayout ll;
            View Tarjeta,view_hor,view_Izq,view_der,LineaArriba,LineaAbajo;
            ConstraintLayout cl_Titulo;
            ImageView img_Status;

            DataViewHolder(View itemView)
            {
                super(itemView);
                try
                {
                    img_Status = (ImageView) itemView.findViewById(R.id.icn_Status);
                    Tarjeta =itemView.findViewById(R.id.Tarjeta);


                    //if(Tarjeta==null)
                    cl_Titulo = (ConstraintLayout) itemView.findViewById(R.id.cl);

                    LineaArriba      = itemView.findViewById(R.id.view6);

                    LineaAbajo = itemView.findViewById(R.id.view5);

                    TipoDocumento = itemView.findViewById(R.id.txtv_TipoDocumento);
                    Documento = itemView.findViewById(R.id.txtv_Documento);
                    lstv_DetalleDoc = itemView.findViewById(R.id.lstv_DetalleDoc);
                    ll = itemView.findViewById(R.id.ll_Lista);
                    btn_MasInfo = itemView.findViewById(R.id.btn_MasInfo);
                    view_hor = itemView.findViewById(R.id.view);
                    view_Izq = itemView.findViewById(R.id.view2);
                    view_der = itemView.findViewById(R.id.view3);

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
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.timeline_container, viewGroup, false);
        final DataViewHolder dvh;
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
                if (mostrarBotonMasInf)
                    {
                        dvh.btn_MasInfo.setVisibility(View.VISIBLE);
                    } else
                    {
                        dvh.btn_MasInfo.setVisibility(View.GONE);
                    }
                final String TipoDocumento = ArrayDocumentos.get(contadorTarjetas).getTipoDocumento();
                final String Documento = ArrayDocumentos.get(contadorTarjetas).getDocumento();
                final String TagDocumento = ArrayDocumentos.get(contadorTarjetas).getTagDocumento();

                dvh.cl_Titulo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        LastSelectedCard = dvh.getAdapterPosition();
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
//
                if (ArrayDocumentos.get(contadorTarjetas).getDato() != null)
                    {
                        dvh.lstv_DetalleDoc.setAdapter(new adaptadorTabla(contexto, R.layout.list_item_2_datos_fragment, ArrayDocumentos.get(contadorTarjetas).getDato()));

                        dvh.lstv_DetalleDoc.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                             Toast.makeText(viewGroup.getContext(), "ListView Postion " + position, Toast.LENGTH_LONG).show();
                            Log.i("POSITION", "ListView Postion " + position);
                            }
                        });
                    } else
                    {
                        dvh.view_hor.setVisibility(View.GONE);
                    }
//                Log.i("POSITION", String.valueOf(i) + " " + String.valueOf(contadorTarjetas));
//                return dvh;
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
                contadorTarjetas = i;
                final String TipoDocumento = ArrayDocumentos.get(i).getTipoDocumento();
                final String Documento = ArrayDocumentos.get(i).getDocumento();
                final String TagDocumento = ArrayDocumentos.get(i).getTagDocumento();



                dataViewHolder.Documento.setText(Documento);
                if (MensajePrevio == null)
                    {
                        dataViewHolder.TipoDocumento.setText(TipoDocumento.toUpperCase());
                    } else
                    {
                        dataViewHolder.TipoDocumento.setText(MensajePrevio+ " " + TipoDocumento.toUpperCase());
                    }

                if(MostrarEstatus)
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

                            {
                                switch (Documento)
                                    {
                                        case "Terminado":
                                            dataViewHolder.view_hor.setBackground(contexto.getDrawable(R.color.VerdeRenglon));
                                            dataViewHolder.view_Izq.setBackground(contexto.getDrawable(R.color.VerdeRenglon));
                                            dataViewHolder.view_der.setBackground(contexto.getDrawable(R.color.VerdeRenglon));
                                            dataViewHolder.cl_Titulo.setBackground(contexto.getDrawable(R.color.VerdeRenglon));
                                            dataViewHolder.img_Status.setBackgroundResource(R.drawable.circleverde);
                                            dataViewHolder.img_Status.setImageResource(R.drawable.ic_aceptar);
                                            break;

                                        case "En proceso":
                                            dataViewHolder.view_hor.setBackground(contexto.getDrawable(R.color.grisAXC));
                                            dataViewHolder.view_Izq.setBackground(contexto.getDrawable(R.color.grisAXC));
                                            dataViewHolder.view_der.setBackground(contexto.getDrawable(R.color.grisAXC));
                                            dataViewHolder.cl_Titulo.setBackground(contexto.getDrawable(R.color.grisAXC));
                                            dataViewHolder.img_Status.setBackgroundResource(R.drawable.circleamarillo);
                                            dataViewHolder.img_Status.setImageResource(R.drawable.ic_right);

                                            break;
                                        case "Detenido":
                                            dataViewHolder.view_hor.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
                                            dataViewHolder.view_Izq.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
                                            dataViewHolder.view_der.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
                                            dataViewHolder.cl_Titulo.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
                                            dataViewHolder.img_Status.setBackgroundResource(R.drawable.circle);
                                            dataViewHolder.img_Status.setImageResource(R.drawable.ic_cancel);

                                            break;
                                    }
                            }

                        if(i==0)
                            {
                                dataViewHolder.LineaArriba.setVisibility(View.GONE);
                            }

                        Log.i("PRBALINEAS", String.valueOf(i) + " " + String.valueOf(getItemCount()));
                        if(i>=getItemCount()-1)
                            {
                                dataViewHolder.LineaAbajo.setVisibility(View.GONE);
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
