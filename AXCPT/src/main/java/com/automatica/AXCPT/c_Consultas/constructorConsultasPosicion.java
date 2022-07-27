package com.automatica.AXCPT.c_Consultas;

public class constructorConsultasPosicion
{
    String CodigoPallet;
    String Producto;
    String Desc;
    String Lote;
    String Empaques;
    String CantidadActual;
    String CantidadPallets;

    public constructorConsultasPosicion(String codigoPallet, String producto, String desc, String lote, String empaques, String cantidadActual, String cantidadPallets) {
        CodigoPallet = codigoPallet;
        Producto = producto;
        Desc = desc;
        Lote = lote;
        Empaques = empaques;
        CantidadActual = cantidadActual;
        CantidadPallets = cantidadPallets;
    }

    public String getCodigoPallet() {
        return CodigoPallet;
    }

    public String getProducto() {
        return Producto;
    }

    public String getDesc() {
        return Desc;
    }

    public String getLote() {
        return Lote;
    }

    public String getEmpaques() {
        return Empaques;
    }

    public String getCantidadActual() {
        return CantidadActual;
    }

    public String getCantidadPallets() {
        return CantidadPallets;
    }
}
