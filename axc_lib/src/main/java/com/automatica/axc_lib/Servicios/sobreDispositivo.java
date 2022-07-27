package com.automatica.axc_lib.Servicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.View;

import com.automatica.axc_lib.R;

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
                        "Usuario: "  + pref.getString("usuario" ,  "--") + "\n"+
                        "Versión de la aplicación: "  + contexto.getString(R.string.app_version) + "\n"
                ,
                "Acerca de este dispositivo",false,false);
    }
}
