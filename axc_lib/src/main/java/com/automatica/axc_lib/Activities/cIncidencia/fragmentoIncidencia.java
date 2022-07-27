package com.automatica.axc_lib.Activities.cIncidencia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.automatica.axc_lib.R;
import com.automatica.axc_lib.Servicios.CreaDialogos;
import com.automatica.axc_lib.Servicios.esconderTeclado;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.MetodosConexion.cAccesoADatos;
import com.automatica.axc_lib.views.CustomArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;
import static com.automatica.axc_lib.Activities.cIncidencia.MetodosIncidencia.GALLERY_REQUEST_CODE;
import static com.automatica.axc_lib.Activities.cIncidencia.MetodosIncidencia.streamToBytes;


public class fragmentoIncidencia extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    //PRIMITIVAS
    private  byte[] byteArr;


    //Objetos
    private OnFragmentInteractionListener mListener;
    private MetodosIncidencia mi =null;
    private InputStream is = null;
    private CustomArrayAdapter cr = null;


    //VIEWS
    private ImageView img_Preview;
    private Spinner sp_Modulos;
    private EditText edtx_Incidencia;
    private Button btn_EnviarIncidencia;
    private ImageButton img_Back;
    public fragmentoIncidencia()
    {
        // Required empty public constructor
    }

    public static fragmentoIncidencia newInstance( String param1, String param2)
    {
        fragmentoIncidencia fragment = new fragmentoIncidencia();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragmento_incidencia, container, false);

        FrameLayout fl_Oscuro = view.findViewById(R.id.fl_Oscuridad);
        img_Preview = view.findViewById(R.id.img_Preview);
        img_Back = view.findViewById(R.id.imgb_AtrasRep);
        sp_Modulos = (Spinner) view.findViewById(R.id.spnr_Modulos);
        edtx_Incidencia=(EditText) view.findViewById(R.id.edtx_Descripcion);
        btn_EnviarIncidencia = (Button) view.findViewById(R.id.btn_EnviarIncidencia);

        mi = new MetodosIncidencia(getActivity(),getContext());
        img_Preview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreaDialogos cd = new CreaDialogos();
                cd.CreaSeleccionador("Seleccione la fuente de la imagen:",getActivity(), getResources().getStringArray(R.array.StrArr_TiposFotoIncidencia),
               new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                       switch (which)
                           {
                               case 0: //Camara
                                   //Toast.makeText(getContext(), "Camara", Toast.LENGTH_LONG).show();
                                   mi.TomaFotografia();
                                   break;


                               case 1:  //Galeria
                                   Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                           MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                   galleryIntent.setType("image/*");
                                   getActivity().startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);

                                   break;

                           }
                    }
                });
            }
        });
        img_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentoIncidencia.this).commit();

            }
        });
        btn_EnviarIncidencia.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new SegundoPlano("EnviarIncidencia").execute();
            }
        });

        fl_Oscuro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                return;
            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
            {
                mListener.onFragmentInteraction(uri);
            }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
            {
                mListener = (OnFragmentInteractionListener) context;

                SegundoPlano sp = new SegundoPlano("ConsultaModulos");
                sp.execute();

            } else
            {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        boolean ActivaProgressBar(boolean estado);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == MetodosIncidencia.PERMISSIONS_REQUEST_READ_CAMERA
                && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Log.e("SoapResponse", "onRequestPermissionsResult: Jalo el permiso");
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)//Aqui tuve que dar override al onActivityResult de la actividad padre para poder recibir los resultados
    {
        try
            {
                Log.i("onActivityResult", "RQ " + requestCode + " RS " + resultCode);

                if (requestCode == MetodosIncidencia.CAMERA_REQUEST_CODE)
                    {
                        if (resultCode == RESULT_OK)
                            {
                                ExifInterface ei = new ExifInterface(mi.getCurrentPhotoPath());
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);


                                ByteArrayOutputStream st = new ByteArrayOutputStream();

                                switch (orientation)
                                    {

                                        case ExifInterface.ORIENTATION_ROTATE_90:
                                            img_Preview.setImageBitmap(mi.rotateImage(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()), 90));
                                            mi.rotateImage(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()), 90).compress(Bitmap.CompressFormat.JPEG, 80, st);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_180:
                                            img_Preview.setImageBitmap(mi.rotateImage(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()), 180));
                                            mi.rotateImage(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()), 90).compress(Bitmap.CompressFormat.JPEG, 80, st);

                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_270:
                                            img_Preview.setImageBitmap(mi.rotateImage(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()), 270));
                                            mi.rotateImage(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()), 90).compress(Bitmap.CompressFormat.JPEG, 80, st);

                                            break;

                                        case ExifInterface.ORIENTATION_NORMAL:
                                        default:
                                            img_Preview.setImageBitmap(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()));
//                                            byteArr = null;
                                            mi.rotateImage(BitmapFactory.decodeFile(mi.getCurrentPhotoPath()), 0).compress(Bitmap.CompressFormat.JPEG, 80, st);
                                    }
                                            byteArr = st.toByteArray();
                            }
                    }


                if (requestCode == GALLERY_REQUEST_CODE)
                    {
                        if (resultCode == RESULT_OK)
                            {
                                Uri img = data.getData();
                                img_Preview.setImageURI(img);
                                Log.i("PATH", img.getPath());
                                mi.setCurrentPhotoPath(mi.getRealPathFromURI(data.getData()));

                            }
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(getContext(),null,e.getMessage(),false,true,true);
            }

    }


    private class SegundoPlano extends AsyncTask<String, Void, Void>
    {
        cAccesoADatos ca = new cAccesoADatos(getContext());
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


            mListener.ActivaProgressBar(true);
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

                                        if(byteArr!=null)
                                            {
                                                dao = ca.SOAPIncidencia(sp_Modulos.getSelectedItem().toString(), edtx_Incidencia.getText().toString(), byteArr);

                                            }else if (mi.getCurrentPhotoPath()!= null)
                                            {
                                                is = new FileInputStream(mi.getCurrentPhotoPath());

                                                dao = ca.SOAPIncidencia(sp_Modulos.getSelectedItem().toString(), edtx_Incidencia.getText().toString(), streamToBytes(is));
                                                is.close();

                                            }else
                                            {
                                                dao = new DataAccessObject(false,"Path no valido o nulo.",null);

                                            }
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
                                        cr = new CustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item, dao.getcTablasSorteadas("Descripcion","Tag1"));
                                        sp_Modulos.setAdapter(cr);
                                        break;

                                    case "EnviarIncidencia":
                                        new popUpGenerico(getContext(), null, "Incidencia registrada correctamente.", dao.iscEstado(), true, true);
                                        new esconderTeclado(getActivity());
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentoIncidencia.this).commit();
                                        break;

                                    default:
                                        new popUpGenerico(getContext(), null, dao.getcMensaje(), dao.iscEstado(), true, true);
                                }
                        }else
                        {
                            new popUpGenerico(getContext(), null, dao.getcMensaje(), dao.iscEstado(), true, true);

                        }


                } catch (Exception e)
                {
                    new popUpGenerico(getContext(), null, e.getMessage(), false, true, true);
                    e.printStackTrace();
                }
            // recarga = false;
            mListener.ActivaProgressBar(false);

        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }
    }


}
