package com.automatica.AXCMP.Principal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.loader.content.CursorLoader;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;
import com.automatica.AXCMP.views.CustomArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Incidencia extends AppCompatActivity
{
    //VIEWS
    private ImageView imgv_;
    private Button btn_TomarFoto, btn_Buscar;
    private Spinner sp_Modulos;
    private EditText edtx_DescripcionIncidencia;

    //PRIMITIVAS
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int GALLERY_REQUEST_CODE = 1002;
    private Integer PERMISSIONS_REQUEST_READ_CAMERA = 0123;


    private String currentPhotoPath;
    private String ESTACION = "E499";

    //OBJETOS
    private Bitmap bit_foto;
    private FileInputStream is = null;
    private   CustomArrayAdapter cr = null;

    @Override
    protected void onResume()
    {
        SegundoPlano sp = new SegundoPlano("ConsultaModulos");
        sp.execute();

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgv_ = (ImageView) findViewById(R.id.imageView);
        sp_Modulos = (Spinner) findViewById(R.id.spnr_Modulos);
        edtx_DescripcionIncidencia =(EditText) findViewById(R.id.edtx_Descripcion);

        btn_Buscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                try
                    {
                        Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (CameraIntent.resolveActivity(getPackageManager()) != null)
                            {

                                File foto = null;
                                foto = createImageFile();

                                if (foto != null)
                                    {
                                        Uri fotoURI = FileProvider.getUriForFile(Incidencia.this, "com.automatica.miaxc.fileprovider", foto);
                                        CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                                        startActivityForResult(CameraIntent, CAMERA_REQUEST_CODE);
                                        galleryAddPic();
                                    }


                            } else
                            {
                                new popUpGenerico(Incidencia.this, getCurrentFocus(), "No hay ninguna aplicación instalada que pueda manejar la acción de toma de fotografias", false, true, true);
                            }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        new popUpGenerico(Incidencia.this, getCurrentFocus(), e.getMessage(), false, true, true);
                    }
            }
        });


        btn_TomarFoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });


        // if (ActivityCompat.checkSelfPermission(PictureActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_READ_CAMERA);
                }

        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                SegundoPlano sp = new SegundoPlano("EnviarIncidencia");
                sp.execute();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == PERMISSIONS_REQUEST_READ_CAMERA
                && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Log.e("SoapResponse", "onRequestPermissionsResult: Jalo el permiso");
            }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (requestCode == CAMERA_REQUEST_CODE)
            {
                if (resultCode == RESULT_OK)
                    {
                        // Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                        bit_foto = BitmapFactory.decodeFile(currentPhotoPath);
                        imgv_.setImageBitmap(bit_foto);
                    }
            }


        if (requestCode == GALLERY_REQUEST_CODE)
            {
                if (resultCode == RESULT_OK)
                    {
                        Uri img = data.getData();
                        imgv_.setImageURI(img);
                        Log.i("PATH", img.getPath());
                        currentPhotoPath = getRealPathFromURI(data.getData());

                    }
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = ESTACION + "_JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("FOTOS", Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {
        cAccesoADatos ca = new cAccesoADatos(Incidencia.this);
        DataAccessObject dao = null;
        String Tarea = null;
        View LastView;

        public SegundoPlano(String Tarea)
        {
            this.Tarea = Tarea;
        }

        @Override
        protected void onPreExecute()
        {
//            recarga = true;
//            inAnimation = new AlphaAnimation(0f, 1f);
//            inAnimation.setDuration(200);
//            progressBarHolder.setAnimation(inAnimation);
//            progressBarHolder.setVisibility(View.VISIBLE);
//
//            h.postDelayed(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    LastView = getCurrentFocus();
//                    progressBarHolder.requestFocus();
//
//                }
//            },10);


        }

        @Override
        protected Void doInBackground(String... params)
        {

            if (!this.isCancelled())
                {
                    try
                        {

                            switch (Tarea)
                                {

                                    case "ConsultaModulos":

                                        dao = ca.SOAPConsultaModulosIncidencia();
                                        break;

                                    case "EnviarIncidencia":
                                        is = new FileInputStream(currentPhotoPath);
                                        if (currentPhotoPath != null)
                                            {
                                                Log.i("PATH","HOLA "+cr.getSelectedExtra(sp_Modulos.getSelectedItemPosition()));
                                                dao = ca.SOAPIncidencia(cr.getSelectedExtra(sp_Modulos.getSelectedItemPosition()), edtx_DescripcionIncidencia.getText().toString(), streamToBytes(is));
                                            }
                                        is.close();
                                        break;

                                     default:
                                         dao = new DataAccessObject(false,"Tarea no soportada (AsyncTask)",null);
                                }




                        } catch (Exception e)
                        {
                            if (dao == null)
                                 { dao = new DataAccessObject(false,e.getMessage(),null);}

                            e.printStackTrace();
                        }

                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
//            outAnimation = new AlphaAnimation(1f, 0f);
//            outAnimation.setDuration(200);
//            progressBarHolder.setAnimation(outAnimation);
//            progressBarHolder.setVisibility(View.GONE);

            try
                {
                    Handler h = new Handler();
                    h.postAtFrontOfQueue(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //   LastView.requestFocus();
                            //   recarga = true;
                        }
                    });

//                if(dao.iscEstado())
//                    {
//                        new popUpGenerico(PictureActivity.this,getCurrentFocus(),dao.getcMensaje(),dao.iscEstado(),true,true);
//
//                    }
//                else

                    if(dao.iscEstado())
                    {
                        switch (Tarea)
                            {

                                case "ConsultaModulos":
                                    cr = new CustomArrayAdapter(Incidencia.this,android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","Tag1"));
                                    sp_Modulos.setAdapter(cr);
                                    break;

                                case "EnviarIncidencia":
                                    if(currentPhotoPath==null)
                                        {
                                            new popUpGenerico(Incidencia.this, getCurrentFocus(), "Debe seleccionar una imagen.", dao.iscEstado(), true, true);
                                            return;
                                        }

//                                    File f = new File(currentPhotoPath);
//                                    boolean hola = f.delete();
//                                    if(hola)
//                                        {
//                                            new popUpGenerico(PictureActivity.this, getCurrentFocus(), dao.getcMensaje(), false, true, true);
//                                        }

                                    break;

                                default:
                                    new popUpGenerico(Incidencia.this, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);
                            }
                    }else
                        {
                            new popUpGenerico(Incidencia.this, getCurrentFocus(), dao.getcMensaje(), dao.iscEstado(), true, true);

                        }


                } catch (Exception e)
                {
                    new popUpGenerico(Incidencia.this, getCurrentFocus(), e.getMessage(), false, true, true);
                    e.printStackTrace();
                }
            // recarga = false;
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }
    }

    public static byte[] streamToBytes(InputStream is)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try
            {
                while ((len = is.read(buffer)) >= 0)
                    {
                        os.write(buffer, 0, len);
                    }
            } catch (java.io.IOException e)
            {
            }
        return os.toByteArray();
    }


    private String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(Incidencia.this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}

