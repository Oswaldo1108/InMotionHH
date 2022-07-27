package com.automatica.AXCPT.Servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificacionesReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent Servicio = new Intent(context,HiloNotificaciones.class);
        context.startService(Servicio);
    }
}
