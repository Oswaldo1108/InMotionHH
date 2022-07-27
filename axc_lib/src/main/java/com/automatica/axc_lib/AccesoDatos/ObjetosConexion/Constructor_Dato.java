package com.automatica.axc_lib.AccesoDatos.ObjetosConexion;

import android.util.Log;
import android.widget.Spinner;

import java.util.ArrayList;

public class Constructor_Dato
{
    String titulo;
    String dato;

    String Tag1,Tag2,Tag3,Tag4,Tag5;




    /***TOMAR EL INDICE DE UN VALOR TEXTO EN UN SPINNER*/
    public static Constructor_Dato getValue(ArrayList<Constructor_Dato> data, String busqueda)
    {
        for(Constructor_Dato c: data)
        {
            if(c.getTitulo().equals(busqueda))
            {
                Log.i(c.titulo,c.dato);
                return c;
            }
        }
        return null;
    }

    public Constructor_Dato(String titulo, String dato)
    {
        this.titulo = titulo;
        this.dato = dato;
    }

    public Constructor_Dato(String titulo, String dato, String Tag1, String Tag2, String Tag3)
    {
        this.titulo = titulo;
        this.dato = dato;
        this.Tag1 = Tag1;
        this.Tag2 = Tag2;
        this.Tag3 = Tag3;
    }
    public Constructor_Dato(String titulo, String dato, String Tag1, String Tag2, String Tag3,String Tag4){
        this.titulo = titulo;
        this.dato = dato;
        this.Tag1 = Tag1;
        this.Tag2 = Tag2;
        this.Tag3 = Tag3;
        this.Tag4= Tag4;
    }
    public Constructor_Dato(String titulo, String dato, String Tag1, String Tag2, String Tag3,String Tag4, String Tag5){
        this.titulo = titulo;
        this.dato = dato;
        this.Tag1 = Tag1;
        this.Tag2 = Tag2;
        this.Tag3 = Tag3;
        this.Tag4= Tag4;
        this.Tag5= Tag5;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public void setDato(String dato)
    {
        this.dato = dato;
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

    public String getTitulo()
    {
        return titulo;
    }

    public String getDato()
    {
        return dato;
    }

    public String getTag4() {
        return Tag4;
    }

    public void setTag4(String tag4) {
        Tag4 = tag4;
    }

    public String getTag5() {
        return Tag5;
    }
    public void setTag5(String tag5) {
        Tag5 = tag5;
    }

    @Override
    public String toString()
    {
        return dato;
    }
}
