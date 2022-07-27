package com.automatica.AXCMP.Servicios;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.annotation.RequiresApi;

import com.automatica.AXCMP.R;


public class preferencias extends PreferenceActivity
{
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

    }
}
