package com.automatica.AXCPT.Servicios.TableHelpers;

import java.util.Comparator;

class ComparadorStatus implements Comparator<String[]>
{
    int Columna;
    public ComparadorStatus(int Columna)
    {
        this.Columna = Columna;
    }
    @Override
    public int compare(String[] statusa,String statusb[])
    {
        return  statusa[Columna].compareTo(statusb[Columna]);
    }
}