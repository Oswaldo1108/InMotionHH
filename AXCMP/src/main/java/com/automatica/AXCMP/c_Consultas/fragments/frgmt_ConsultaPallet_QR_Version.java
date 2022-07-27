package com.automatica.AXCMP.c_Consultas.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.esconderTeclado;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.SoapAction;
import com.automatica.AXCMP.adaptadorTabla;
import com.automatica.AXCMP.c_Consultas.ConstructorDatoTitulo;
import com.automatica.AXCMP.constructorTablaEntradaAlmacen;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class frgmt_ConsultaPallet_QR_Version extends DialogFragment
{
    EditText edtx_Empaque;
    Button btn_ReimprimirEtiqueta;
    SoapAction sa = new SoapAction();
    String Area;
    View view;
    TextView txtv_Status;
    constructorTablaEntradaAlmacen cd;
    ArrayList<constructorTablaEntradaAlmacen> lista = null;
    ArrayList<ConstructorDatoTitulo> ArrayData;
    ListView tablaPallet;
    adaptadorTabla adaptador;
    Handler h = new Handler();

    int vecesEscaneado = 0;
    Integer PERMISSIONS_REQUEST_READ_CAMERA = 0;
    SurfaceView surfaceView;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        try
            {
                view = inflater.inflate(R.layout.frgmt_consulta_pallet_qr, container, false);
                edtx_Empaque = (EditText) view.findViewById(R.id.editText);
                btn_ReimprimirEtiqueta = (Button) view.findViewById(R.id.button2);

                surfaceView = (SurfaceView) view.findViewById(R.id.sv_CameraQR);
                  txtv_Status = (TextView) view.findViewById(R.id.textView112);
                tablaPallet = (ListView) view.findViewById(R.id.lstv_Pallet);


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
                                vecesEscaneado++;
                                edtx_Empaque.setText(qrCodes.valueAt(0).displayValue);
                                //edtx_Empaque.setText("Veces escaneado: " + String.valueOf(vecesEscaneado));
                                h.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        txtv_Status.setText(String.valueOf(vecesEscaneado));

                                    }
                                });
                          //      Toast.makeText(getContext(), String.valueOf(vecesEscaneado), Toast.LENGTH_LONG).show();
//                                SegundoPlano sp = new SegundoPlano("ReimprimeEtiqueta");
//                                sp.execute();

                            }
                    }
                });


                edtx_Empaque.setOnKeyListener(new View.OnKeyListener()
                {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                            {
                                if (!edtx_Empaque.getText().toString().equals(""))
                                    {

                                        SegundoPlano sp = new SegundoPlano("ReimprimeEtiqueta");
                                        sp.execute();


                                    } else
                                    {
                                        new popUpGenerico(getContext(), null, getString(R.string.error_ingrese_pallet), "false", true, true);
//                                handler.post(new Runnable()
//                                {
//                                    @Override
//                                    public void run()
//                                    {
//
//                                        edtx_CodigoPallet.requestFocus();
//                                    }
//                                });
                                    }
                                new esconderTeclado(getActivity());
                            }

                        return false;
                    }
                });

            }catch (Exception e)
            {
                e.printStackTrace();

            }
        return view;
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

    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {

        String tarea;
        String decision,mensaje;


        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
        //    txtv_Status.setText("Consultando empaque");
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
                {
                    switch (tarea)
                        {
                            case "ReimprimeEtiqueta":
                               // Thread.sleep(5000);
                                sa.SOAPConsultaPalletReg(edtx_Empaque.getText().toString(), getContext());

                                break;

                        }
                    mensaje = sa.getMensaje();
                    decision = sa.getDecision();

                    if(decision.equals("true"))
                        {
                            sacaDatos();
                        }

                    Log.i("SoapResponse", "Do in backgound");
                } catch (Exception e)
                {
                    e.printStackTrace();
                    mensaje = e.getMessage();
                }

            return null;
        }

        @Override
        protected  void onPostExecute(Void aVoid)
        {
            try
                {

                    if (decision.equals("true"))
                        {
                            switch (tarea)
                                {
                                    case   "ReimprimeEtiqueta":
                                       // getDialog().dismiss();
                                        adaptador = new adaptadorTabla(getContext(), R.layout.list_item_2datos, lista);
                                        tablaPallet.setAdapter(adaptador);

                                        edtx_Empaque.setText("");
                                        edtx_Empaque.requestFocus();
                                        break;



                                }
                        }
                    if (decision.equals("false"))
                        {

                            new popUpGenerico(getContext(),null, mensaje,"false" ,true,true);

                            //  txtv_Status.setText(mensaje);
                            edtx_Empaque.requestFocus();
                            edtx_Empaque.setText("");
                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(getContext(),null, e.getMessage(),"false" ,true,true);

                }


        }
    }

    private void sacaDatos()
    {
        try
            {

                lista = new ArrayList<>();
                PropertyInfo pi;
                ArrayData = new ArrayList<>();
                SoapObject tabla = sa.parser();
                if(tabla!=null)
                    {
                        cd = new constructorTablaEntradaAlmacen("Código miaxc_consulta_pallet",edtx_Empaque.getText().toString());
                        lista.add(cd);
                        for (int i = 0; i < tabla.getPropertyCount(); i++)
                            {
                                SoapObject tabla11 = (SoapObject) tabla.getProperty(i);

                                for(int t=0;t<tabla11.getPropertyCount();t++)
                                    {
                                        pi = new PropertyInfo();
                                        tabla11.getPropertyInfo(t,pi);
                                        String name = pi.name;
                                        if(name.equals("FechaCrea"))
                                            {
                                                name = "Fecha Creación";
                                            }
                                        if(name.equals("FechaCad"))
                                            {
                                                name = "Fecha Caducidad";
                                            }
                                        if(name.equals("LoteProveedor"))
                                            {
                                                name = "Lote Proveedor";
                                            }
                                        if(name.equals("CantidadOriginal"))
                                            {
                                                name = "Cantidad Original";
                                            }
                                        if(name.equals("CantidadActual"))
                                            {
                                                name = "Cantidad Actual";
                                            }
                                        if(name.equals("CantidadOriginal"))
                                            {
                                                name = "CantidadOriginal";
                                            }
                                        if(name.equals("Estacion"))
                                            {
                                                name = "Estación";
                                            }
                                        if(name.equals("Revision"))
                                            {
                                                name = "Revisión";
                                            }
                                        cd = new constructorTablaEntradaAlmacen(name,tabla11.getPropertyAsString(t));
                                        lista.add(cd);
                                    }
                                // UbicacionSugerida = tabla11.getPrimitivePropertyAsString("UbicacionSugerida");
                                Log.d("SoapResponse", tabla11.toString());
                            }
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }



}
