package com.automatica.axc_lib.Servicios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.axc_lib.R;


public class creaStatusBar extends AppCompatActivity
{
    public creaStatusBar(Toolbar toolbar,String Titulo)
    {

        setSupportActionBar(toolbar);
        toolbar.setSubtitle(Titulo);
        toolbar.setLogo(R.drawable.logoaxc42);
    }

}
