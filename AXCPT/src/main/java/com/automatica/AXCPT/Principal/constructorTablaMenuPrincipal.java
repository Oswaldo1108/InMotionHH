package com.automatica.AXCPT.Principal;

public class constructorTablaMenuPrincipal
{
    String titulo;
    String Descripcion;

    int iconoi;


    public constructorTablaMenuPrincipal(int iconoi,   String titulo, String descripcion) {
        this.titulo = titulo;
        Descripcion = descripcion;
        this.iconoi = iconoi;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public int getIconoi() {
        return iconoi;
    }
}