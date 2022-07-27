package com.automatica.AXCPT.objetos;

import android.graphics.Bitmap;

public class ObjetoConstructor {
    String Titulo;
    String Subtitulo;
    String Tag1;
    String Tag2;
    Bitmap icono;

    public ObjetoConstructor(String titulo, String subtitulo, Bitmap icono) {
        Titulo = titulo;
        Subtitulo = subtitulo;
        this.icono = icono;
    }

    public ObjetoConstructor(String titulo, String subtitulo, String tag1, String tag2, Bitmap icono) {
        Titulo = titulo;
        Subtitulo = subtitulo;
        Tag1 = tag1;
        Tag2 = tag2;
        this.icono = icono;
    }

    public ObjetoConstructor(String titulo, String subtitulo, String tag1, String tag2) {
        Titulo = titulo;
        Subtitulo = subtitulo;
        Tag1 = tag1;
        Tag2 = tag2;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getSubtitulo() {
        return Subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        Subtitulo = subtitulo;
    }

    public String getTag1() {
        return Tag1;
    }

    public void setTag1(String tag1) {
        Tag1 = tag1;
    }

    public String getTag2() {
        return Tag2;
    }

    public void setTag2(String tag2) {
        Tag2 = tag2;
    }

    public Bitmap getIcono() {
        return icono;
    }

    public void setIcono(Bitmap icono) {
        this.icono = icono;
    }
}
