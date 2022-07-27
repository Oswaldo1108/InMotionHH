package com.automatica.AXCPT.Servicios;

import android.content.Context;
import android.net.wifi.WifiManager;

public class verConexionRed
{
    public boolean verificarConexion(Context contexto)
    {
        WifiManager wm = (WifiManager) contexto.getSystemService(contexto.WIFI_SERVICE);
        if(wm.isWifiEnabled())
        {
            //if(wm.)
        }
        return false;
    }
}
