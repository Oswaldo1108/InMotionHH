package com.automatica.AXCMP.SoapConection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.automatica.AXCMP.constructorTablaEntradaAlmacen;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DataAccessObject
{
    private boolean cEstado = false;
    private String cMensaje;

    private String[] cEncabezado;
    private String[][] cTabla;
    private ArrayList< ArrayList<constructorTablaEntradaAlmacen> > cTablas;


    private SoapObject soapObject;

    public DataAccessObject()
    {
    }

    public DataAccessObject(Boolean cEstado, String cMensaje,SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.soapObject = soapObject;
    }

    public void newInstance(boolean cEstado, String cMensaje, @Nullable String[] cEncabezado, @Nullable String[][] cTabla,@NonNull SoapObject soapObject)
    {

        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezado = cEncabezado;
        this.cTabla = cTabla;
        this.soapObject = soapObject;
    }

    public DataAccessObject(Boolean cEstado, String cMensaje, String[] cEncabezado, String[][] cTabla, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezado = cEncabezado;
        this.cTabla = cTabla;
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

    public void setcTabla(String[][] cTabla)
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
               "Tabla = " + Arrays.toString(cTabla) + "\n";
    }

    public ArrayList<ArrayList<constructorTablaEntradaAlmacen>> getcTablas()
    {
        return cTablas;
    }


    public ArrayList<constructorTablaEntradaAlmacen> getcTablasSorteadas(String Titulo,String Detalle)//Recorre todos los arraylists, y crea uno solo con un solo tipo de dato y un detalle a elegir
    {

        Log.i("TABLAS SORTEADAS",Titulo + " " + Detalle);
        ArrayList<constructorTablaEntradaAlmacen> arr = new ArrayList<>();
        constructorTablaEntradaAlmacen add = null;
        for(ArrayList<constructorTablaEntradaAlmacen> c:cTablas)
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
}
