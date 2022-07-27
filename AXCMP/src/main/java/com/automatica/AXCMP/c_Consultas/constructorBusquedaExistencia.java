package com.automatica.AXCMP.c_Consultas;

public class constructorBusquedaExistencia
{
    String Ubicacion,Pallet,Producto,Lote,Empaques,CantidadActual,LoteAXC;

    public constructorBusquedaExistencia(String ubicacion, String pallet, String producto, String lote, String empaques, String cantidadActual,String LoteAXC)
    {
        Ubicacion = ubicacion;
        Pallet = pallet;
        Producto = producto;
        Lote = lote;
        Empaques = empaques;
        CantidadActual = cantidadActual;
        this.LoteAXC= LoteAXC;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public String getPallet() {
        return Pallet;
    }

    public String getProducto() {
        return Producto;
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

    public String getLoteAXC()
    {
        return LoteAXC;
    }
}
