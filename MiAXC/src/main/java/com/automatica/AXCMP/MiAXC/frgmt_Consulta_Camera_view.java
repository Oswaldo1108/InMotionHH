package com.automatica.AXCMP.MiAXC;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.adaptadorTabla;
import com.automatica.AXCMP.Constructor_Dato;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import static android.content.Context.VIBRATOR_SERVICE;

public class frgmt_Consulta_Camera_view extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1,mParam2;
    TextView txtv_Titulo,txtv_detalle;
    Button btn_ReimprimirEtiqueta;
    SoapAction sa = new SoapAction();
    String Area;
    View view;
    TextView txtv_Status;
    Constructor_Dato cd;
//    ArrayList<constructorTablaEntradaAlmacen> lista = null;
//    ArrayList<ConstructorDatoTitulo> ArrayData;
    ListView tablaPallet;
    adaptadorTabla adaptador;
    Handler h = new Handler();

    int vecesEscaneado = 0;
    Integer PERMISSIONS_REQUEST_READ_CAMERA = 0;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    dataTransfer dataTransfer;

    String DatoAnterior = "";



    String frgtag_ConsultaCamaraView = "FRGCMR";


    public static frgmt_Consulta_Camera_view newInstance(String param1, String param2)
    {
        frgmt_Consulta_Camera_view fragment = new frgmt_Consulta_Camera_view ();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        try
            {
                view = inflater.inflate(R.layout.frgmt_consulta_pallet_qr, container, false);

                txtv_Titulo = view.findViewById(R.id.txtv_Incidencia);
                txtv_detalle = view.findViewById(R.id.txtv_Titulo);

                txtv_Titulo.setText(getString(R.string.frgmnt_titulo_qr));
                txtv_detalle.setText(getString(R.string.frgmnt_detalle_qr));

                surfaceView = (SurfaceView) view.findViewById(R.id.sv_CameraQR);
                txtv_Status = (TextView) view.findViewById(R.id.textView117);
                tablaPallet = (ListView) view.findViewById(R.id.lstv_Pallet);

                ImageButton imgb_Atras = view.findViewById(R.id.imgb_AtrasRep);

                imgb_Atras.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                       getActivity().getSupportFragmentManager().beginTransaction().remove(frgmt_Consulta_Camera_view.this).commit();

                    }
                });

                barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.CODE_128|Barcode.QR_CODE|Barcode.ALL_FORMATS|Barcode.ITF|Barcode.DATA_MATRIX|Barcode.CODE_39).build();
                cameraSource = new CameraSource.Builder(getContext(), barcodeDetector).setRequestedPreviewSize(2048, 2048).setAutoFocusEnabled(true).build();


                surfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
                {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder)
                    {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                            {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        PERMISSIONS_REQUEST_READ_CAMERA);
                                return;
                            }
                        try
                            {
                                cameraSource.start(holder);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                                new popUpGenerico(getContext(), null, e.getMessage(), "false", true, true);
                            }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
                    {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder)
                    {
                        cameraSource.stop();
                    }
                });


                barcodeDetector.setProcessor(new Detector.Processor<Barcode>()
                {
                    @Override
                    public void release()
                    {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections)
                    {
                        final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                        if(qrCodes.size()!=0)
                            {

                                final String DatoNuevo = qrCodes.valueAt(0).displayValue;

                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                        if(!DatoNuevo.equals(DatoAnterior))
                                            {
                                                 DatoAnterior=DatoNuevo;
                                                 vecesEscaneado = 0;
                                                 return;
                                            }

                                        if(DatoNuevo.equals(DatoAnterior))
                                            {
                                                vecesEscaneado++;
                                            }

                                        if(vecesEscaneado==5)
                                            {
                                                Log.i("MPARAM",mParam1);
                                                String[] arr = {mParam1,qrCodes.valueAt(0).displayValue};
                                                txtv_Status.setText(qrCodes.valueAt(0).displayValue);
                                                dataTransfer.ReturnData(arr);
                                                barcodeDetector.release();
                                                cameraSource.release();

                                                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.correct);
                                                mp.start();

                                                Vibrator v = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
                                                v.vibrate(100);


                                                getActivity().getSupportFragmentManager().beginTransaction().remove(frgmt_Consulta_Camera_view.this).commit();

                                            }

                                    }
                                });
                            }
                    }
                });

            }catch (Exception e)
            {
                e.printStackTrace();

            }
        return view;
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
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof frgmt_Consulta_Camera_view.dataTransfer)
            {
                 dataTransfer = (frgmt_Consulta_Camera_view.dataTransfer) context;

            } else
            {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
    }

    public interface dataTransfer
    {
        String ReturnData(String[] dataToReturn);
    }


}
