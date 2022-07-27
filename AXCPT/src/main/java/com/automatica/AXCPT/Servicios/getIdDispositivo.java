package com.automatica.AXCPT.Servicios;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.automatica.AXCPT.R;

public class getIdDispositivo extends AppCompatActivity {

    Button boton;
    TextView tv;
    Integer PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicios_getiddispositivo);

        boton =(Button) findViewById(R.id.botonhm);
        tv = (TextView) findViewById(R.id.textView7);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPermisoImei();
            }
        });

    }
        @TargetApi(Build.VERSION_CODES.M)
        public void getPermisoImei()
        {
            TelephonyManager mtelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);


            mtelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                getImei();
            }



        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getImei();
        }
    }
    private void getImei() {

       TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String deviceid = mTelephonyManager.getDeviceId();
        Log.d("msg", "DeviceImei " + deviceid);
        tv.setText(deviceid);
    }
}
