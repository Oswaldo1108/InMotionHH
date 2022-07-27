package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class conexionWS2 //extends cAccesoADatos
{

    List<String> valid = Arrays.asList("Http","hTTP", "HTTP","http");
    //VARIABLES
    private String  NAMESPACE ="http://tempuri.org/" , URL, SOAP_ACTION;
    private String  decision;
    private String  mensaje;
    private String  tabla;
    private String  Usuario,Estacion;
    boolean debug = true;


    private static final String IGNORAR_DATO = "E_XNE";

    //OBJETOS
    DataAccessObject dao;

    conexionWS2() {}

    DataAccessObject IniciaAccionSOAP(SoapObject objeto, String METHOD_NAME, Context contexto, String URL)
    {
        try
            {
        ConnectivityManager connManager = (ConnectivityManager) contexto.getSystemService(contexto.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // debug = true;
        if(debug)
            {
                if (!wifi.isConnected())
                    {
                        mensaje = "Verifique conexión a red WiFi";
                        decision = "false";
                        return new DataAccessObject(false,"Verifique conexión a red WIFI.",null);
                    }
            }
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        if(METHOD_NAME.equals("WM_PruebaFoto"))//Se debe de activar esa codificacion para que se puedan enviar Arreglos de bytes
            {
                new MarshalBase64().register(soapEnvelope);
            }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);

        if(URL!=null)
            {
                this.URL = URL;
            }else
            {
                URL = pref.getString("direccionWebService", "http://svr-wm/MiAXC/wsAXCMP.asmx");

                //CONCATENACIÓN IP
    //                if (!URL.startsWith("http")||!URL.startsWith("Http")||!URL.startsWith("HTTP"))

                String CodigoValidacion = PreferenceManager.getDefaultSharedPreferences(contexto).getString("codigoEmpresa", "SINCODIGO");

                switch (CodigoValidacion)
                    {
                        case "52637825A2":
                            URL = pref.getString("direccionWebService", "http://svr-wm/MiAXC/wsAXCMP.asmx");
                            URL = "http://" + URL + "/MiAXC/wsaxcmp.asmx";

                            break;

                        case "Maulec2020":
                            URL = pref.getString("direccionWebService", "http://svr-wm/MiAXC/wsAXCMP.asmx");
                            URL = "http://" + URL + "/MiAXCMaulec/wsaxcmp.asmx";

                            break;


                        case "Coflex2021":
                            URL = pref.getString("direccionWebService", "http://svr-wm/MiAXCCoflex/wsAXCMP.asmx");
                            URL = "http://" + URL + "/MiAXCCoflex/wsaxcmp.asmx";

                            break;

                        default:
                            URL = "http://" + URL + "/" + pref.getString("Ruta","wsAXCCiesa") +"/wsaxcmp.asmx";
                            break;
                    }
            }

             SOAP_ACTION = NAMESPACE + METHOD_NAME;
                Log.i("SOAP", " >>>------- "+ URL + " URL ------>>>" +"\n");
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(objeto);
                HttpTransportSE transporte = new HttpTransportSE(URL);
                transporte.debug= true;

                PropertyInfo pi;



                Log.i("SOAP", " >>>------- "+ METHOD_NAME+ " SOAP REQUEST------>>>" +"\n");
                for(int i = 0; i<=objeto.getPropertyCount()-1;i++)
                    {
                        pi = new PropertyInfo();
                        objeto.getPropertyInfo(i, pi);
                        if(objeto.getProperty(i)!=null)

                            {
                                Log.i("SOAP", "PROPIEDAD " + String.valueOf(i) + " >>>>>>  " + pi.name + " : " + objeto.getProperty(i));
                            }else
                            {
                                Log.i("SOAP", "PROPIEDAD " + String.valueOf(i) + " >>>>>>  " + pi.name + " : " + " null");
                            }
                    }




                 transporte.call(SOAP_ACTION, soapEnvelope);

                    SoapObject sp =(SoapObject) soapEnvelope.getResponse();
                    String requestDump = transporte.requestDump;
                    String responseDump = transporte.responseDump;



//                Log.i("SOAP", " REQUEST DUMP >>>>>> " + requestDump);


                Log.i("SOAP", " >>>-------/"+ METHOD_NAME+ " SOAP REQUEST/------>>>" + "\n");


                    //                Log.i("SOAP", " <<<<<<" + responseDump);

                    decision = sp.getPrimitivePropertyAsString("Estado");
                    mensaje = sp.getPrimitivePropertyAsString("Texto");

                    Log.i("SOAP", " >>>------- "+ URL + " URL ------>>>" +"\n");
                    Log.i("SOAP", " <<<------- "+ METHOD_NAME+ " SOAP RESPONSE------<<<" +  "\n");
                    Log.i("SOAP", " <<<<<<DECISION:" + decision);
                    Log.i("SOAP", " <<<<<<MENSAJE: " + mensaje);
//                    Log.i("SOAP", " <<<<<<TABLA : Tamaño -("+String.valueOf(sp.getPropertyCount())+") "+  sp.toString());


                Object IMGARR = null;

                    SoapObject object = null;


                try
                        {
                            object = (SoapObject) sp.getProperty(2);
//                            Log.i("SOAP", " <<<<<<OBJETO 1: Tamaño -(" + String.valueOf(object.getPropertyCount()) + ") " + object.toString());
                            object = (SoapObject) object.getProperty(1);
//                            Log.i("SOAP", " <<<<<<OBJETO 2: Tamaño -(" + String.valueOf(object.getPropertyCount()) + ") " + object.toString());
                            object = (SoapObject) object.getProperty(0);
//                            Log.i("SOAP", " <<<<<<OBJETO 3: Tamaño -(" + String.valueOf(object.getPropertyCount()) + ") " + object.toString());

                            if(METHOD_NAME.equals("WM_ConsultaIncidenciaDet"))
                                {
                                    IMGARR = (Object) sp.getProperty("Objeto");
                                    Log.i("SOAP", " <<<<<<OBJETO IMAGEN " + IMGARR.toString().length() + ">>>>>>");
                                }

                        }catch (Exception e)
                        {
//                            e.printStackTrace();
                            Log.e("ConexionWSExption","Se regreso tabla");
                            return dao = new DataAccessObject(Boolean.valueOf(sp.getProperty(0).toString()), sp.getProperty(1).toString(), null, sp);

                        }


                if(object==null)
                    {
                        return dao = new DataAccessObject(false,"SoapObject returned null.",sp);
                    }
                            PropertyInfo pisp = null;


                            String[] ENCABEZADO = null;
                            String[][] RENGLON = null;
                            ArrayList<String[][]> aAgregar = new ArrayList<>();
                            ArrayList<ArrayList<Constructor_Dato>> arrayTablas = new ArrayList<>();
                            ArrayList<Constructor_Dato> Dato;//=  new ArrayList<>();


                            //if(tabla!=null)
                            for (int IteradorDataSet = 0; IteradorDataSet <= object.getPropertyCount() - 1; IteradorDataSet++)
                                {
                                    Dato = new ArrayList<>();
                                    ENCABEZADO = null;
                                    RENGLON = null;

                                    SoapObject renglon = (SoapObject) object.getProperty(IteradorDataSet);

                                    RENGLON = new String[object.getPropertyCount()][renglon.getPropertyCount()];

                                    String Encabezados = "";
                                    String Str_Renglon = "";

                                    //     Log.i("SOAP","Renglon - " + renglon.toString() + " " + String.valueOf( renglon.getPropertyCount()));

                                    //for (int b = 0; b <= renglon.getPropertyCount() - 2; b++)
                                    //{
                                    //  SoapObject tabla  = (SoapObject) renglon.getProperty(b);

                                    if (true)
                                        {
                                            ENCABEZADO = new String[renglon.getPropertyCount()];
                                            Encabezados = "";
                                            for (int f = 0; f <= renglon.getPropertyCount() - 1; f++)
                                                {
                                                    pisp = new PropertyInfo();
                                                    renglon.getPropertyInfo(f, pisp);
                                                    Encabezados = Encabezados + "   |   " + pisp.name;
                                                    ENCABEZADO[f] = pisp.name;
                                                }

                                            Log.i("SOAP", Encabezados + " |");
                                        }

                                    for (int i = 0; i <= renglon.getPropertyCount() - 1; i++)
                                        {
                                            //   Log.i("SOAP", String.valueOf(i));
                                            pisp = new PropertyInfo();
                                            renglon.getPropertyInfo(i, pisp);

                                            Str_Renglon = Str_Renglon + "   |   " + renglon.getPropertyAsString(i);

                                            String datoNuevo = renglon.getPropertyAsString(i);
                                            if (datoNuevo.equals("anyType{}"))
                                                {
                                                    datoNuevo = "";
                                                }
                                            RENGLON[IteradorDataSet][i] = datoNuevo;


                                            if(!datoNuevo.equals(IGNORAR_DATO))
                                                {
                                                    Dato.add(new Constructor_Dato(pisp.name.replace("_x0020_", " "), datoNuevo.replace("_x0020_", " ")));
                                                }
                                        }
                                    Log.i("SOAP", Str_Renglon + " |");
                                    Str_Renglon = "";


                                    aAgregar.add(RENGLON);
                                    //if (IteradorDataSet != 0)
                                      if(Dato.size()<=0||!Dato.get(0).getTitulo().equals("Estado"))
                                        {
                                            arrayTablas.add(Dato);
                                        }
                                    // }
                                }
                if(IMGARR==null)//Si la variable donde se guarda la imagen se queda nula, quiere decir quecir que no se regreso una imagen
                    {
                        dao = new DataAccessObject(Boolean.valueOf(sp.getProperty(0).toString()), sp.getProperty(1).toString(), arrayTablas, object);
                    }else
                    {
                        //dao = new DataAccessObject(Boolean.valueOf(sp.getProperty(0).toString()), sp.getProperty(1).toString(), arrayTablas, object,IMGARR);

                    }

                    Log.i("SOAP", " <<<-------/"+ METHOD_NAME+ " SOAP RESPONSE/------<<<" +"\n");




            }catch (ConnectException ex)
            {
                dao = new DataAccessObject(false,ex.getMessage(),objeto);

                ex.printStackTrace();
            }
            catch  (Exception ex)
            {
                dao = new DataAccessObject(false,ex.getMessage(),objeto);

                ex.printStackTrace();
            }
        return dao;
    }

}
