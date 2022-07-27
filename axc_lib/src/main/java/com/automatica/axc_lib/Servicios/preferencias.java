package com.automatica.axc_lib.Servicios;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.automatica.axc_lib.R;


public class preferencias extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
