package com.automatica.AXCPT.objetos;

import android.content.Intent;

public class objetoMenu {
    String Titulo;
    Intent intent;
    int Tipo;
    int imagen;

    public objetoMenu(String titulo, Intent intent) {
        Titulo = titulo;
        this.intent = intent;
    }


    public objetoMenu(String titulo, Intent intent, int tipo) {
        Titulo = titulo;
        Tipo = tipo;
        this.intent= intent;
    }
    public objetoMenu(String titulo, Intent intent, int tipo,int imagen) {
        Titulo = titulo;
        Tipo = tipo;
        this.intent= intent;
        this.imagen= imagen;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
