package com.automatica.AXCPT.Servicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.automatica.AXCPT.Validacion.ValidarColocar;
import com.automatica.AXCPT.c_Almacen.Almacen.Colocar;
import com.automatica.AXCPT.c_Recepcion.Recepcion.RecepcionConteo;
import com.automatica.AXCPT.R;

public class creaNotificacion {
    Context contexto;

    BroadcastReceiver br= new BroadCastIntent();
    public creaNotificacion(Context contexto) {
        this.contexto = contexto;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public creaNotificacion(Context contexto, String titulo, String Contenido, int id, String TextoLargo , int intent, String Documento) {

        //IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        //filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        //contexto.registerReceiver(br,filter);

        Intent intentBorrar= new Intent(contexto,BroadCastIntent.class);
        Intent intentMontacarga = new Intent(contexto, ValidarColocar.class);
        intentBorrar.setAction(String.valueOf(intent));
        intentBorrar.putExtra("IDWS", String.valueOf(id));
        intentBorrar.putExtra("Documento",Documento);
        PendingIntent actionIntent = PendingIntent.getActivity(contexto,0,intentMontacarga, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri sonido= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(contexto,"Notificacion");
        builder.setSmallIcon(R.mipmap.m_bco_amarillo);
        builder.setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.mipmap.m_bco_amarillo));

        long [] patron={12,50,12};
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(TextoLargo));
        builder.setVibrate(patron);
        builder.setSound(sonido)
        .setContentIntent(actionIntent)
                .addAction(R.mipmap.m_bco_amarillo, "Pallet pendiente", actionIntent)//.setGroup("InMotionNotification")
        .setAutoCancel(true);
        builder.setContentTitle(titulo).setContentText(Contenido).setPriority(NotificationCompat.PRIORITY_MAX);
        //builder.build().flags|= Notification.GROUP_ALERT_ALL|NotificationCompat.GROUP_ALERT_ALL;
        builder.build().flags|=NotificationCompat.FLAG_ONLY_ALERT_ONCE;
        NotificationManager mNotificationManager;
        mNotificationManager =
                (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "CanalInMotion1";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Canal notificaciones InMotion",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.enableVibration(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(contexto);
        notificationManagerCompat.notify("Aviso",id,builder.build());

    }
    public creaNotificacion(Context contexto, String titulo, String Contenido, int id, String TextoLargo , int intent, String Documento, int second) {

        //IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        //filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        //contexto.registerReceiver(br,filter);
        Log.e("String", String.valueOf(intent));
        Log.e("Hola", "sdfdsfds");

        Intent intentBorrar= new Intent(contexto,BroadCastIntent.class);

        Intent intentMontacarga = new Intent(contexto, RecepcionConteo.class);


        intentBorrar.setAction(String.valueOf(intent));
        intentBorrar.putExtra("IDWS", String.valueOf(id));
        intentBorrar.putExtra("Documento",Documento);

        PendingIntent actionIntent = PendingIntent.getActivity(contexto,0,intentMontacarga, PendingIntent.FLAG_UPDATE_CURRENT);
        CharSequence contentTitle = "Alert Mobile";
        Log.i("Intent", String.valueOf(intent));
        NotificationCompat.Builder builder= new NotificationCompat.Builder(contexto,"Notificacion");
        builder.setSmallIcon(R.mipmap.m_bco_amarillo);
        builder.setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.mipmap.m_bco_amarillo));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(TextoLargo));
        builder
                .setContentIntent(actionIntent)
                .addAction(R.mipmap.m_bco_amarillo, contentTitle, actionIntent)//.setGroup("InMotionNotification")
                .setAutoCancel(true);
        builder.setContentTitle(titulo).setContentText(Contenido).setPriority(NotificationCompat.PRIORITY_MAX);
        //builder.build().flags|= Notification.GROUP_ALERT_ALL|NotificationCompat.GROUP_ALERT_ALL;
        builder.build().flags|=NotificationCompat.FLAG_ONLY_ALERT_ONCE;
        NotificationManager mNotificationManager;
        mNotificationManager =
                (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "CanalInMotion1";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Canal notificaciones InMotion",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.enableVibration(true);
            channel.setSound(null,null);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(contexto);
        notificationManagerCompat.notify("Aviso",id,builder.build());



    }
    public creaNotificacion(Context contexto, String titulo, String Contenido, String TextoLargo) {

        //IntentFilter filter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        //filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        //contexto.registerReceiver(br,filter);


        Uri sonido= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(contexto,"Notificacion");
        builder.setSmallIcon(R.mipmap.m_bco_amarillo);
        builder.setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.mipmap.m_bco_amarillo));
        long [] patron={12,50,12};
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(TextoLargo));
        builder.setVibrate(patron);
        builder.setSound(sonido)
                .setAutoCancel(true);
        builder.setContentTitle(titulo).setContentText(Contenido).setPriority(NotificationCompat.PRIORITY_MAX);
        //builder.build().flags|= Notification.GROUP_ALERT_ALL|NotificationCompat.GROUP_ALERT_ALL;
        builder.build().flags|=NotificationCompat.FLAG_ONLY_ALERT_ONCE;
        NotificationManager mNotificationManager;
        mNotificationManager =
                (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "CanalInMotion1";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Canal notificaciones InMotion",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            channel.enableVibration(true);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(contexto);
        notificationManagerCompat.notify("Aviso",1,builder.build());


    }
}
