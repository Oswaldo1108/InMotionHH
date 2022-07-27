package com.automatica.AXCPT.Servicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.View;

import com.automatica.AXCPT.R;

public class sobreDispositivo
{
    public sobreDispositivo(Context contexto, View vista)
    {
        try {
            WifiManager wm = (WifiManager) contexto.getSystemService(contexto.WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);

            new popUpGenerico(contexto, vista,
                    "Dirección IP: " + ip + "\n" +
                            "Estación: " + pref.getString("estacion", "ER") + "\n" +
                            "Usuario: " + pref.getString("usuario", "ER") + "\n" +
                            "Área de trabajo: " + pref.getString("area", "ER") + "\n" +
                            "Versión de la aplicación: "  + contexto.getString(R.string.app_version) + "\n"
                    ,
                    "Acerca de este dispositivo", false, false);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
