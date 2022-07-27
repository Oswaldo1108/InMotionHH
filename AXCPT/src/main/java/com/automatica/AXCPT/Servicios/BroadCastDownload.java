package com.automatica.AXCPT.Servicios;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadCastDownload extends BroadcastReceiver {
    private static final String TAG ="BroadCastDownload";
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context= context;
        int downloadID  = Integer.parseInt(intent.getAction());
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        //Checking if the received broadcast is for our enqueued download by matching download id
        if (downloadID == id) {
            Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
        }
    }
}
