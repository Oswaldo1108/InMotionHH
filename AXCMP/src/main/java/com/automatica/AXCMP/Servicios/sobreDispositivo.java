package com.automatica.AXCMP.Servicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.View;

public class sobreDispositivo
{
    public sobreDispositivo(Context contexto, View vista)
    {
        WifiManager wm = (WifiManager) contexto.getSystemService(contexto.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        new popUpGenerico(contexto,vista,
                "Dirección IP: " + ip +"\n" +
                        "Estación: " + pref.getString("estacion", "ER")+ "\n"+
                        "Usuario: "  + pref.getString("usuario" ,  "--") + "\n"
                ,
                "Acerca de este dispositivo",false,false);
    }
}
