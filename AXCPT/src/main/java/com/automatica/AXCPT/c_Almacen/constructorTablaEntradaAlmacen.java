package com.automatica.AXCPT.c_Almacen;

public class constructorTablaEntradaAlmacen
{
    String titulo;
    String dato;

    String Tag1,Tag2,Tag3;


    public constructorTablaEntradaAlmacen(String titulo, String dato)
    {
        this.titulo = titulo;
        this.dato = dato;
    }

    public constructorTablaEntradaAlmacen(String titulo, String dato, String Tag1, String Tag2, String Tag3)
    {
        this.titulo = titulo;
        this.dato = dato;
        this.Tag1 = Tag1;
        this.Tag2 = Tag2;
        this.Tag3 = Tag3;
    }



    public String getTitulo()
    {
        return titulo;
    }

    public String getDato()
    {
        return dato;
    }


    public void setTag1(String tag1)
    {
        Tag1 = tag1;
    }

    public void setTag2(String tag2)
    {
        Tag2 = tag2;
    }

    public void setTag3(String tag3)
    {
        Tag3 = tag3;
    }

    public String getTag1()
    {
        return Tag1;
    }

    public String getTag2()
    {
        return Tag2;
    }

    public String getTag3()
    {
        return Tag3;
    }


}
