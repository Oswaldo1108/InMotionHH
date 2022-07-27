package com.automatica.AXCMP.c_Consultas;

public class ConstructorDatoTitulo
{
    String Titulo,Dato;

    public ConstructorDatoTitulo(String titulo, String dato)
    {
        Titulo = titulo;
        Dato = dato;
    }

    public String getTitulo() {
        return Titulo;
    }

    public String getDato() {
        return Dato;
    }
}
