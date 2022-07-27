package com.automatica.AXCMP.SoapConection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.automatica.AXCMP.Constructor_Dato;

import org.ksoap2.serialization.SoapObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class DataAccessObject
{
    private boolean cEstado = false;
    private String cMensaje;

    private String[] cEncabezado;

    private ArrayList<String[][]> cTabla;
    private ArrayList<String[][]> cEncabezados;
    private ArrayList< ArrayList<Constructor_Dato> > cTablas;

    private SoapObject soapObject;

    private Object Imagen;

    public Bitmap getImagen()
    {
        InputStream inputStream = null;
        try
            {
                Log.i("IMAGEN",Imagen.toString());
                inputStream  = new ByteArrayInputStream(Base64.decode(Imagen.toString(), Base64.NO_WRAP));
                inputStream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        return BitmapFactory.decodeStream(inputStream);
    }

    public void setImagen(Object imagen)
    {
        Imagen = imagen;
    }

    public DataAccessObject()
    {
    }

    public DataAccessObject newInstance(boolean cEstado, String cMensaje, @Nullable String[] cEncabezado, @Nullable ArrayList<String[][]> cTabla,@NonNull SoapObject soapObject)
    {

        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezado = cEncabezado;
        this.cTabla = cTabla;
        this.soapObject = soapObject;
        return new DataAccessObject(cEstado, cMensaje, cEncabezado, cTabla, soapObject);
    }

    public DataAccessObject(Boolean cEstado, String cMensaje, String[] cEncabezado, ArrayList<String[][]>cTabla, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezado = cEncabezado;
        this.cTabla = cTabla;
        this.soapObject = soapObject;
    }


    public DataAccessObject(Boolean cEstado, String cMensaje, ArrayList< ArrayList<Constructor_Dato> >cTablas, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cTablas = cTablas;
        this.soapObject = soapObject;
    }

    public DataAccessObject(Boolean cEstado, String cMensaje, ArrayList< ArrayList<Constructor_Dato> >cTablas, SoapObject soapObject, Object Imagen)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cTablas = cTablas;
        this.soapObject = soapObject;
        this.Imagen = Imagen;
    }


    public DataAccessObject(Boolean cEstado, String cMensaje,  ArrayList<String[][]> cEncabezados, ArrayList<String[][]>cTabla, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezados = cEncabezados;
        this.cTabla = cTabla;
        this.soapObject = soapObject;
    }

    public DataAccessObject(Boolean cEstado, String cMensaje,SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.soapObject = soapObject;
    }




    public boolean iscEstado()
    {
        return cEstado;
    }

    public String getcMensaje()
    {
        return cMensaje;
    }

    public String[] getcEncabezado()
    {
        return cEncabezado;
    }

    public String[][] getcTabla()
    {
        return cTabla.get(0);
    }

    public ArrayList<String[][]> getcArrayTabla()
    {
        return cTabla;
    }



    public SoapObject getSoapObject()
    {
        return soapObject;
    }

    public void setcEstado(boolean cEstado)
    {
        this.cEstado = cEstado;
    }

    public void setcMensaje(String cMensaje)
    {
        this.cMensaje = cMensaje;
    }

    public void setcEncabezado(String[] cEncabezado)
    {
        this.cEncabezado = cEncabezado;
    }

    public void setcTabla(ArrayList<String[][]> cTabla)
    {
        this.cTabla = cTabla;
    }

    public void setSoapObject(SoapObject soapObject)
    {
        this.soapObject = soapObject;
    }

    public String toString()
    {
        return "Estado = " + cEstado +"\n"+
               "Mensaje = "+ cMensaje + "\n"+
               "Encabezados = " + Arrays.toString(cEncabezado) + "\n"+
               "Tabla vacia = " + Boolean.toString(cTabla.isEmpty()) + "\n";
    }

    public ArrayList<ArrayList<Constructor_Dato>> getcTablas()
    {
        return cTablas;
    }


    public ArrayList<Constructor_Dato> getcTablasSorteadas(String Titulo, String Detalle)//Recorre todos los arraylists, y crea uno solo con un solo tipo de dato y un detalle a elegir
    {

        Log.i("TABLAS SORTEADAS",Titulo + " " + Detalle);
        ArrayList<Constructor_Dato> arr = new ArrayList<>();
        Constructor_Dato add = null;
        for(ArrayList<Constructor_Dato> c:cTablas)
            {
                String agregarDatoExtra = null;
                add = null;
                for(int r = 0; r<=c.size()-1;r++)
                    {

//                        Log.i("SPINNER","DATO " + c.get(r).getDato()+ " TITULO - " + c.get(r).getTitulo()+ " DATOCOMPARATIVO " + Detalle);
                        if(c.get(r).getTitulo().equals(Detalle))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                            {
                                agregarDatoExtra = c.get(r).getDato();
//                                Log.i("SPINNER","ARRAY " + c.get(r).getDato()+" AGREGAR " + agregarDatoExtra);
                            }
                        if(c.get(r).getTitulo().equals(Titulo))  //revisa la descripcion con la que se quiere agregar
                            {
//                                Log.i("SPINNER","ENTRO - DATO1 " + c.get(r).getDato()+" DATO2 - " + agregarDatoExtra);
                                c.get(r).setTag1(agregarDatoExtra);
                                add = c.get(r);
                            }
                    }
                if(add!=null)
                    {
                        add.setTag1(agregarDatoExtra);  //Agrega Detalle al Tag1 para ser tomado en pantalla por Spinner
                        arr.add(add);

                    }
               // Log.i("SPINNER","i" + i);

            }
        return arr;
    }
    //Esta funcion es usada para llenar spinners

    public ArrayList<Constructor_Dato> getcTablasSorteadas(String Titulo,String Detalle,String Extra,String Extra2)//Recorre todos los arraylists, y crea uno solo con un solo tipo de dato y un detalle a elegir
    {
        ArrayList<Constructor_Dato> arr = new ArrayList<>();
        Constructor_Dato add;
        for(ArrayList<Constructor_Dato> c:cTablas)
            {
                String agregarDatoExtra = null;
                String agregarDatoExtraTag2 = null;
                String agregarDatoExtraTag3 = null;

                add = null;
                for (int r = 0; r <= c.size() - 1; r++)
                    {
                        if (c.get(r).getTitulo().equals(Detalle))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                            {
                                agregarDatoExtra = c.get(r).getDato();
                            }

                        if (c.get(r).getTitulo().equals(Extra))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                            {
                                agregarDatoExtraTag2 = c.get(r).getDato();
                            }

                        if (c.get(r).getTitulo().equals(Extra2))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato3
                            {
                                agregarDatoExtraTag3 = c.get(r).getDato();
                            }



                        if (c.get(r).getTitulo().equals(Titulo))
                            {

                                c.get(r).setTag1(agregarDatoExtra);
                                c.get(r).setTag2(agregarDatoExtraTag2);
                                c.get(r).setTag2(agregarDatoExtraTag3);

                                add = c.get(r);
                            }

                    }
                if (add != null)
                    {
                        add.setTag1(agregarDatoExtra);  //Agrega Detalle al Tag1 para ser tomado en pantalla por Spinner
                        add.setTag2(agregarDatoExtraTag2);
                        add.setTag2(agregarDatoExtraTag3);
                        arr.add(add);
                    }
            }
        return arr;
    }
}
