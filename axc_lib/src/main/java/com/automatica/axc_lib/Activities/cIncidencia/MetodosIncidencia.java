package com.automatica.axc_lib.Activities.cIncidencia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.loader.content.CursorLoader;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.automatica.axc_lib.Servicios.popUpGenerico;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetodosIncidencia extends Activity
{
    //OBJETOS
    private Activity activity = null;
    private Context contexto;

    //PRIMITIVAS
    private String currentPhotoPath;
    private String ESTACION = "E499";
    public  static final int CAMERA_REQUEST_CODE = 1001;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final Integer PERMISSIONS_REQUEST_READ_CAMERA = 0123;


    public String getCurrentPhotoPath()
    {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath)
    {
        this.currentPhotoPath = currentPhotoPath;
    }

    public MetodosIncidencia(Activity activity, Context context)
    {
        this.activity = activity;
        this.contexto = context;

        Log.i("ACTIVITY",activity.toString() + " HOLA ");
    }

    public String TomaFotografia()
    {
        try
            {
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (CameraIntent.resolveActivity(activity.getPackageManager()) != null)
                    {
                        File foto = null;
                        foto = createImageFile();

                        if (foto != null)
                            {
                                Uri fotoURI = FileProvider.getUriForFile(activity, "com.automatica.miaxc.fileprovider", foto);
                                CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                                activity.startActivityForResult(CameraIntent, CAMERA_REQUEST_CODE);
                                galleryAddPic();
                            }
                    } else
                    {
                        new popUpGenerico(activity, activity.getCurrentFocus(), "No hay ninguna aplicación instalada que pueda manejar la acción de toma de fotografias", false, true, true);
                    }
            }catch (Exception e)
            {
                e.printStackTrace();

                new popUpGenerico(contexto, activity.getCurrentFocus(), e.getMessage(),false,true ,true);
            }
        return "";
    }


    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = ESTACION + "_JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        activity.sendBroadcast(mediaScanIntent);
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
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        return os.toByteArray();
    }


    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(activity, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    public static Bitmap rotateImage(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }



}
