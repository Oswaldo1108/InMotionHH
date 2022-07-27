package com.automatica.AXCPT.Fragmentos.Elementos;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

public class Item {
    CharSequence etiqueta;//paquete
    Drawable icono; //icono
    Fragment fragmento;
    String TAG;

    public Item(CharSequence etiqueta, Drawable icono, Fragment fragmento,String TAG) {
        this.etiqueta = etiqueta;
        this.icono = icono;
        this.fragmento = fragmento;
        this.TAG= TAG;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public Fragment getFragmento() {
        return fragmento;
    }

    public void setFragmento(Fragment fragmento) {
        this.fragmento = fragmento;
    }

    public CharSequence getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(CharSequence etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Drawable getIcono() {
        return icono;
    }

    public void setIcono(Drawable icono) {
        this.icono = icono;
    }
}
