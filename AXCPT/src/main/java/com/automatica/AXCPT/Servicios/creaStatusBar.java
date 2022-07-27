package com.automatica.AXCPT.Servicios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.automatica.AXCPT.R;

public class creaStatusBar extends AppCompatActivity
{
    public creaStatusBar(Toolbar toolbar,String Titulo)
    {

        setSupportActionBar(toolbar);
        toolbar.setSubtitle(Titulo);
//        toolbar.setLogo(R.drawable.axc_logo_toolbar);
    }

}
