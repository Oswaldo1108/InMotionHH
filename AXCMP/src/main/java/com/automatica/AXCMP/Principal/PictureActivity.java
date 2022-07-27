package com.automatica.AXCMP.Principal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.automatica.AXCMP.R;


public class PictureActivity extends AppCompatActivity
{
    //VIEWS
    private ImageView imgv_;

    //PRIMITIVAS
    private static final int CAMERA_REQUEST_CODE = 1001;
    Integer PERMISSIONS_REQUEST_READ_CAMERA = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (ActivityCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                PERMISSIONS_REQUEST_READ_CAMERA);
                    }

            }
        imgv_ = (ImageView) findViewById(R.id.imageView);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(CameraIntent, CAMERA_REQUEST_CODE);
               // CameraIntent
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_READ_CAMERA
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.e("SoapResponse", "onRequestPermissionsResult: Jalo el permiso");
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==CAMERA_REQUEST_CODE)
            {
                if(resultCode == RESULT_OK)
                    {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        imgv_.setImageBitmap(bitmap);
                    }
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
