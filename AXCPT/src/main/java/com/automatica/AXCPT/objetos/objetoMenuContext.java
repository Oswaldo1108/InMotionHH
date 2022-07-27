package com.automatica.AXCPT.objetos;

import android.content.Intent;

import java.util.ArrayList;

public class objetoMenuContext{
    String Titulo;
    Intent intent;
    int Tipo;
    int imagen;
    ArrayList<objetoMenu> intents;


    public objetoMenuContext(String titulo, int imagen, Intent intent) {
        Titulo = titulo;
        this.intent = intent;
        Tipo = 1;
        this.imagen = imagen;
    }

    public objetoMenuContext(String titulo,int imagen, ArrayList<objetoMenu> intents) {
        Titulo = titulo;
        Tipo = 2;
        this.imagen = imagen;
        this.intents = intents;
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

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public ArrayList<objetoMenu> getIntents() {
        return intents;
    }

    public void setIntents(ArrayList<objetoMenu> intents) {
        this.intents = intents;
    }
}
