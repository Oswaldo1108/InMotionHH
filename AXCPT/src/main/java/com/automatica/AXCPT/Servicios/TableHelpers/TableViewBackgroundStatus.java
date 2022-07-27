package com.automatica.AXCPT.Servicios.TableHelpers;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.automatica.AXCPT.R;

import de.codecrafters.tableview.providers.TableDataRowBackgroundProvider;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;

class TableViewBackgroundStatus implements TableDataRowBackgroundProvider<String[]> {
    int renglonSeleccionado, ColumnaStatus = -1;
    String ValorVerde, ValorAmarillo, ValorRojo;
    StandarDataAdapter st;
    Context context;


    /**
     * ESTE CONSTRUCTOR SE USA PARA GENERAR UN ADAPTADOR DE BACKGROUND DE LAS TABLAS CON TRES COLORES.
     * <p>
     * int ColumnaStatus,          COLUMNA A SACAR EL ESTATUS
     * int renglonSeleccionado,    RENGLON QUE ESTA SELECCIONADO ACTUALMENTE
     * String ValorVerde,          VALOR QUE SERA REPRESENTADO CON EL COLOR VERDE
     * String ValorAmarillo,       VALOR QUE SERA REPRESENTADO CON EL COLOR AMARILLO
     * String ValorRojo,           VALOR QUE SERA REPRESENTADO CON EL COLOR ROJO
     * SimpleTableDataAdapter st,  ADAPTADOR DE DATOS DEL TABLEVIEW
     * Context context             CONTEXTO DE LA ACTIVIDAD
     **/
    public TableViewBackgroundStatus(
            int ColumnaStatus,
            int renglonSeleccionado,
            String ValorVerde,
            String ValorAmarillo,
            String ValorRojo,
            StandarDataAdapter st,
            Context context
    ) {
        this.renglonSeleccionado = renglonSeleccionado;
        this.st = st;
        this.context = context;
        this.ColumnaStatus = ColumnaStatus;
        this.ValorVerde = ValorVerde;
        this.ValorAmarillo = ValorAmarillo;
        this.ValorRojo = ValorRojo;
        if (ColumnaStatus < 0) {
            throw new IllegalArgumentException("La columna de la que se sacara el estatus debe existir: " + String.valueOf(ColumnaStatus));
        }

    }


    /**
     * ESTE CONSTRUCTOR SE USA PARA GENERAR UN ADAPTADOR DE BACKGROUND DE LAS TABLAS SIN COLORES.
     * <p>
     * int renglonSeleccionado,    RENGLON QUE ESTA SELECCIONADO ACTUALMENTE
     * SimpleTableDataAdapter st,  ADAPTADOR DE DATOS DEL TABLEVIEW
     * Context context             CONTEXTO DE LA ACTIVIDAD
     **/
    public TableViewBackgroundStatus(int renglonSeleccionado, StandarDataAdapter st, Context context) {
        this.renglonSeleccionado = renglonSeleccionado;
        this.st = st;
        this.context = context;
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
                if (rowData[ColumnaStatus].equals(ValorVerde))
                {
                    draw.setTint(context.getResources().getColor(R.color.VerdeRenglon));
                    st.setTextColor(context.getResources().getColor(R.color.negroLetrastd));
                } else if (rowData[ColumnaStatus].equals(ValorAmarillo))
                {
                    draw.setTint(context.getResources().getColor(R.color.AmarilloRenglon));
                    st.setTextColor(context.getResources().getColor(R.color.negroLetrastd));
                } else if (rowData[ColumnaStatus].equals(ValorRojo))
                {
                    draw.setTint(context.getResources().getColor(R.color.RojoRenglon));
                    st.setTextColor(context.getResources().getColor(R.color.negroLetrastd));
                } else
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