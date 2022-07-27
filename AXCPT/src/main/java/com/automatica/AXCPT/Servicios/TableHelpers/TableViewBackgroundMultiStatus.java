package com.automatica.AXCPT.Servicios.TableHelpers;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.automatica.AXCPT.R;

import java.util.HashMap;

import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;

class TableViewBackgroundMultiStatus implements TableDataRowBackgroundProvider<String[]>
{
    int renglonSeleccionado, ColumnaStatus = -1;
    HashMap<String,Integer> ColoresStatus;
    StandarDataAdapter st;
    Context context;
     /**
     * ESTE CONSTRUCTOR SE USA PARA GENERAR UN ADAPTADOR DE BACKGROUND DE LAS TABLAS CON TRES COLORES.
     * <p>
     * int ColumnaStatus,                       COLUMNA A SACAR EL ESTATUS
     * int renglonSeleccionado,                 RENGLON QUE ESTA SELECCIONADO ACTUALMENTE
     * HashMap<String,Integer> ValorVerde,      VALORES A PINTAR EN LA TABLA, EL STRING ES EL ESTATUS Y EL INTEGER EL COLOR
     * SimpleTableDataAdapter st,               ADAPTADOR DE DATOS DEL TABLEVIEW
     * Context context                          CONTEXTO DE LA ACTIVIDAD
     **/
    public TableViewBackgroundMultiStatus(
            int ColumnaStatus,
            int renglonSeleccionado,
            HashMap<String,Integer> ColoresStatus,
            StandarDataAdapter st,
            Context context
    ) {
        this.renglonSeleccionado = renglonSeleccionado;
        this.st = st;
        this.context = context;
        this.ColumnaStatus = ColumnaStatus;
        this.ColoresStatus = ColoresStatus;
        if (ColumnaStatus < 0)
        {
            throw new IllegalArgumentException("La columna de la que se sacara el estatus debe existir: " + String.valueOf(ColumnaStatus));
        }

    }

    @Override
    public Drawable getRowBackground(int rowIndex, String[] rowData)
    {
        Drawable draw = context.getDrawable(R.drawable.renglon_seleccionado);
        st.setTextColor(context.getResources().getColor(R.color.blancoLetraStd));

        if (rowIndex == renglonSeleccionado)
        {
            draw.setTint(context.getResources().getColor(R.color.RengSelStd));
            st.setTextColor(context.getResources().getColor(R.color.blancoLetraStd));
        } else
        {
            if (ColumnaStatus != -1)
            {
                if (ColoresStatus.containsKey(rowData[ColumnaStatus]))
                {
                    draw.setTint(context.getResources().getColor(ColoresStatus.get(rowData[ColumnaStatus])));
                    switch (ColoresStatus.get(rowData[ColumnaStatus]))
                    {
                        case R.color.AmarilloRenglon:
                        case R.color.VerdeRenglon:
                            st.setTextColor(context.getResources().getColor(R.color.negroLetrastd));
                            break;
                        default:
                            st.setTextColor(context.getResources().getColor(R.color.blancoLetraStd));
                    }
                }
                else
                {
                    draw.setTint(context.getResources().getColor(R.color.Transparente));
                    st.setTextColor(context.getResources().getColor(R.color.blancoLetraStd));
                }
            } else
            {
                draw.setTint(context.getResources().getColor(R.color.Transparente));
                st.setTextColor(context.getResources().getColor(R.color.blancoLetraStd));
            }
        }
        return draw;
    }
}