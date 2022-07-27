package com.automatica.AXCMP.Servicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.format.Formatter;

public class getPreferencias
{


    SharedPreferences pref;

    WifiManager wm;
    public getPreferencias(Context contexto)
    {

        pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        wm = (WifiManager) contexto.getSystemService(contexto.WIFI_SERVICE);
    }




    public String getUsuario()
    {
        return pref.getString("usuario" ,  "Er");
    }

    public  String getEstacion()
    {
        return pref.getString("estacion", "ER");
    }

    public String getIP()
    {
        return  Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

}
