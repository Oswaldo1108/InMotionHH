package com.automatica.AXCPT.Servicios;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import com.automatica.AXCPT.BuildConfig;

import com.automatica.AXCPT.Principal.Login;
import com.automatica.axc_lib.Servicios.popUpGenerico;

import java.io.File;

public class HiloDescargas extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    String Version;
    long downloadID;
    popUpGenerico pop;
    Interfaz interfaz;
    SharedPreferences pref;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String Vversion = pref.getString("Vversion","1.0.0.0");

            Version="INMOTION"+Vversion;
            //http://192.168.1.181/AXCSchPT/APKS/AXCSchPT_1.0.0.1.apk
            //http://192.168.1.181/AXCSchPT/APKS/AXCSchPT_1.0.0.1.apk

            String url = "http://"+pref.getString("direccionWebService","192.168.1.181")+"/"+pref.getString("Ruta","wsINMOTION")+"/APKS/"+Version+".apk";
            Log.i("URL",url);
            final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("Descargando...");
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle(Version);
            request.allowScanningByMediaScanner();
            request.setMimeType("application/vnd.android.package-archive");
            request.setAllowedOverRoaming(false);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,Version+".apk");
            Log.i("Direccion",Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOWNLOADS
                    + "/"+Version+".apk");
            File file= new File(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOWNLOADS
                    + "/"+Version+".apk");
            if (file.exists()){
                file.delete();
            }
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            downloadID =manager.enqueue(request);
            //File write logic here
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            try {
                try {
                    pop= new popUpGenerico(getApplicationContext());
                }catch (Exception e){
                    e.printStackTrace();
                }

                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (downloadID == id) {
                    Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_SHORT).show();
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    File file= new File(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOWNLOADS
                            + "/"+Version+".apk");
                    if (file.exists()){
                        Intent action = new Intent(Intent.ACTION_VIEW);
                        action.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Log.i("Uri", String.valueOf(Uri.fromFile(file)));
                        Uri data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".provider",file);
                        action.setDataAndType(data, "application/vnd.android.package-archive");
                        action.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        action.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        action.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
                            Intent intent1= Intent.createChooser(action,"Descargando...");
                            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            try {
                                startActivity(intent1);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {*/
                            startActivity(action);

                    }
                    DetenerServicio();
                    interfaz.matarAplicacion();
                }
            }catch (Exception e){
                e.printStackTrace();
                pop.popUpGenericoDefault(null,e.getMessage(),false);
            }
            DetenerServicio();
        }
    };
    public void DetenerServicio(){
        this.stopSelf();
    }

   public interface Interfaz {
        void matarAplicacion();
   }

}
