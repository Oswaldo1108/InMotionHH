package com.automatica.axc_lib.views;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automatica.axc_lib.ClasesSerializables.Embarque;
import com.automatica.axc_lib.R;
import com.automatica.axc_lib.views.DataViewHolders.DataViewHolderPartidas;
import com.automatica.axc_lib.views.DataViewHolders.DataViewHolderPartidas_Transfer;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adaptador_RV_Partidas_Transfer extends RecyclerView.Adapter<DataViewHolderPartidas_Transfer>//, View.OnClickListener
{
    ArrayList<Embarque> ArrayDocumentos;
//    int contadorTarjetas = 0;

    private onClickRV onClickrv;//Interfaz

    private boolean mostrarBotonMasInf = true;

    private String MensajePrevio = null;

    private boolean MostrarEstatus = false;
    Context contexto = null;

    public void setMostrarBotonMasInf(boolean mostrarBotonMasInf)
    {
        this.mostrarBotonMasInf = mostrarBotonMasInf;
    }

    public ArrayList<Embarque> getArrayPartidas()
    {
        return ArrayDocumentos;
    }


    public void setMostrarEstatus(boolean MostrarEstatus)
    {
        this.MostrarEstatus = MostrarEstatus;
    }

    public Adaptador_RV_Partidas_Transfer(ArrayList<Embarque>Partidas)
    {
        ArrayDocumentos = Partidas;
        setHasStableIds(true);
    }



    public void ActualizaLote(int Partida, String LoteNuevo)
    {



        if(Partida == -1)
            {
                for(Embarque emb: ArrayDocumentos)
                    {
                        emb.setLote(LoteNuevo);
                    }

                this.notifyDataSetChanged();

            }else
            {
                ArrayDocumentos.get(Partida).setLote(LoteNuevo);
                this.notifyItemChanged(Partida);

            }

    }

    public void ActualizaEstacion(int Partida, String EstacionNueva)
    {
        if(Partida == -1)
            {
                for(Embarque emb: ArrayDocumentos)
                    {
                        emb.setEstacion(EstacionNueva);
                    }

                this.notifyDataSetChanged();

            }else
            {
                ArrayDocumentos.get(Partida).setEstacion(EstacionNueva);
                this.notifyItemChanged(Partida);

            }
    }

    public void ActualizaCantidad(int Partida, String CantidadNueva)
    {
        ArrayDocumentos.get(Partida).setCantidadPedida(CantidadNueva);
        this.notifyItemChanged(Partida);
    }

    public void AgregarPartida(Embarque emb)
    {
        ArrayDocumentos.add(emb);
        this.notifyDataSetChanged();
    }


    public void LimpiarPantalla()
    {
        ArrayDocumentos.clear();
        this.notifyDataSetChanged();
    }



    @NonNull
    @Override
    public DataViewHolderPartidas_Transfer onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i)
    {
        View vista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_partida_transfer, viewGroup, false);
        final DataViewHolderPartidas_Transfer dvh;
        dvh = new DataViewHolderPartidas_Transfer(vista);

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
                final String TipoDocumento = ArrayDocumentos.get(i).getOrdenProd();
                final String Documento = ArrayDocumentos.get(i).getPartida();
                final String TagDocumento = ArrayDocumentos.get(i).getNumParte();


//
                if (ArrayDocumentos.get(i) != null)
                    {

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
//                                        onClickrv.clickBotonMasInfo(arr);
                                        Log.i("ACTIVOMASINFO", "ACTIVO");
                                    }
                            }
                        });

                        dvh.edtx_Cantidad.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {

//                                Toast.makeText(contexto,"Cantidad" ,Toast.LENGTH_SHORT ).show();

                                onClickrv.EditarCantidad(i,ArrayDocumentos.get(i).getCantidadPedida());
                            }
                        });

                        dvh.edtx_Lote.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
//                                Toast.makeText(contexto,"Lote" ,Toast.LENGTH_SHORT ).show();

                                onClickrv.EditarLote(i,ArrayDocumentos.get(i).getNumParte());
                            }
                        });


//                        dvh.btn_MasInfo.setVisibility(View.VISIBLE);

                        dvh.btn_MasInfo.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                onClickrv.BorrarPartida(i,ArrayDocumentos.get(i).getOrdenProd(),ArrayDocumentos.get(i).getPartida());
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
    public void onBindViewHolder(@NonNull DataViewHolderPartidas_Transfer dataViewHolder, int i)
    {
        try
            {

                final String Partida = ArrayDocumentos.get(i).getOrdenProd();
                final String Documento = ArrayDocumentos.get(i).getDNumParte1();
                final String TagDocumento = ArrayDocumentos.get(i).getOrdenProd();




                dataViewHolder.Documento.setText(Documento);

                dataViewHolder.edtx_Cantidad.setText(ArrayDocumentos.get(i).getCantidadPedida());
                dataViewHolder.edtx_Lote.setText(ArrayDocumentos.get(i).getLote());
//                dataViewHolder.edtx_EstacionAsignada.setText(ArrayDocumentos.get(i).getEstacion());


                if (MensajePrevio == null)
                    {
                        dataViewHolder.TipoDocumento.setText("Partida " + ArrayDocumentos.get(i).getPartida() + " - " +  ArrayDocumentos.get(i).getNumParte());
                    } else
                    {
                        dataViewHolder.TipoDocumento.setText("REVISAR CONFIG MENSAJE PREVIO");
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
//                                            dataViewHolder.img_Status.setBackgroundResource(R.drawable.circleverde);
//                                            dataViewHolder.img_Status.setImageResource(R.drawable.ic_aceptar);
                                            break;

                                        case "En proceso":
                                            dataViewHolder.view_hor.setBackground(contexto.getDrawable(R.color.grisAXC));
                                            dataViewHolder.view_Izq.setBackground(contexto.getDrawable(R.color.grisAXC));
                                            dataViewHolder.view_der.setBackground(contexto.getDrawable(R.color.grisAXC));
                                            dataViewHolder.cl_Titulo.setBackground(contexto.getDrawable(R.color.grisAXC));


                                            break;
                                        case "Detenido":
                                            dataViewHolder.view_hor.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
                                            dataViewHolder.view_Izq.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
                                            dataViewHolder.view_der.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
                                            dataViewHolder.cl_Titulo.setBackground(contexto.getDrawable(R.color.RojoSemaforo));
//                                            dataViewHolder.img_Status.setBackgroundResource(R.drawable.circle);
//                                            dataViewHolder.img_Status.setImageResource(R.drawable.ic_cancel);

                                            break;
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

//    @Override
//    public void onClick(View v)
//    {


//        switch (v.getId())
//            {
//                case R.id.edtx_LoteAsignado:
//                    break;
//            }


//    }

    public interface onClickRV
    {
        void clickBotonMasInfo(String[] datos);
        String EditarLote(int PosicionPartida, String prmProducto);
        String EditarEstacion(int PosicionPartida, String prmEstacion);
        String BorrarPartida(int PosicionPartida, String prmDocumento,String prmPartida);
        String EditarCantidad(int PosicionPartida, String prmCantidad);

    }











}
