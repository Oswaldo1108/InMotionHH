package com.automatica.AXCPT.Servicios;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.automatica.AXCPT.R;

public class preferencias extends PreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
