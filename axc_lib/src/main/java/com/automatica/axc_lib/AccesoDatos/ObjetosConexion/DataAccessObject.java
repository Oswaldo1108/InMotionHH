package com.automatica.axc_lib.AccesoDatos.ObjetosConexion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DataAccessObject
{
    private boolean cEstado = false;
    private String cMensaje;
    private String cEmpaques;
    private String cCantidad;

    private String[] cEncabezado;

    private String[][] cTablaUnica;
    private ArrayList<String[][]> cTabla;
    private ArrayList<String[][]> cEncabezados;
    private ArrayList<ArrayList<Constructor_Dato>> cTablas;

    private SoapObject soapObject;

    public DataAccessObject()
    {
        cMensaje = "Objeto no inicializado correctamente";
        cEstado =false;
    }

    public DataAccessObject(Exception e)
    {
        cMensaje = "Error en la recepción de datos: " + e.getMessage() ;
        cEstado =false;
    }

    public DataAccessObject(Exception e,SoapObject so)
    {
        cMensaje = "Error en la recepción de datos: " + e.getMessage() ;
        cEstado =false;
        soapObject = so;
    }


    public DataAccessObject newInstance(boolean cEstado, String cMensaje, @Nullable String[] cEncabezado, @Nullable ArrayList<String[][]> cTabla, @NonNull SoapObject soapObject)
    {

        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezado = cEncabezado;
        this.cTabla = cTabla;
        this.soapObject = soapObject;
        return new DataAccessObject(cEstado, cMensaje, cEncabezado, cTabla, soapObject);
    }

    public DataAccessObject(Boolean cEstado, String cMensaje, String[] cEncabezado, ArrayList<String[][]> cTabla, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezado = cEncabezado;
        this.cTabla = cTabla;
        this.soapObject = soapObject;
    }


    public DataAccessObject(Boolean cEstado, String cMensaje, ArrayList<ArrayList<Constructor_Dato>> cTablas, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cTablas = cTablas;
        this.soapObject = soapObject;
    }


    public DataAccessObject(Boolean cEstado, String cMensaje, ArrayList<String[][]> cEncabezados, ArrayList<String[][]> cTabla, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cEncabezados = cEncabezados;
        this.cTabla = cTabla;
        this.soapObject = soapObject;
    }

    public DataAccessObject(Boolean cEstado, String cMensaje, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.soapObject = soapObject;
    }


    public String[][] getcTablaUnica()
    {
        return cTablaUnica;
    }

    public DataAccessObject(Boolean cEstado, String cMensaje, ArrayList<ArrayList<Constructor_Dato>> cTablas, String[][] ArrayParaTablas, String[] cEncabezado, SoapObject soapObject)
    {
        this.cEstado = cEstado;
        this.cMensaje = cMensaje;
        this.cTablas = cTablas;
        this.soapObject = soapObject;
        this.cTablaUnica = ArrayParaTablas;
        this.cEncabezado = cEncabezado;
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
        Log.i("CTABLA", cTabla.size() + " " + cTabla.toString());


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


    /***
     *Esta funcion regresa el primero renglon de la tabla soap que se obtuvo. Esto se utiliza al hacer consultas unitarias, en las que no es
     * necesario traer multiples rows.
     */

    public SoapObject getSoapObject_parced()
    {
        return (SoapObject) soapObject.getProperty(0);
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

    @Override
    public String toString()
    {
        return "Estado = " + cEstado + "\n" +
                "Mensaje = " + cMensaje + "\n" +
                "Encabezados = " + Arrays.toString(cEncabezado) + "\n" +
                "Tabla vacia = " + Boolean.toString(cTabla.isEmpty()) + "\n";
    }

    public ArrayList<ArrayList<Constructor_Dato>> getcTablas()
    {
        return cTablas;
    }




    //Esta funcion es usada para llenar spinners

    public ArrayList<Constructor_Dato> getcTablasSorteadas(String Titulo, String Detalle)//Recorre todos los arraylists, y crea uno solo con un solo tipo de dato y un detalle a elegir
    {
        ArrayList<Constructor_Dato> arr = new ArrayList<>();
        Constructor_Dato add;
        for(ArrayList<Constructor_Dato> c:cTablas)
            {
                String agregarDatoExtra = null;
                add = null;
                for(int r = 0; r<=c.size()-1;r++)
                    {
                        if(c.get(r).getTitulo().equals(Detalle))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                            {
                                agregarDatoExtra = c.get(r).getDato();
                            }
                        if(c.get(r).getTitulo().equals(Titulo))  //revisa la descripcion con la que se quiere agregar
                            {
                                c.get(r).setTag1(agregarDatoExtra);
                                add = c.get(r);
                            }
                    }
                if(add!=null)
                    {
                        add.setTag1(agregarDatoExtra);  //Agrega Detalle al Tag1 para ser tomado en pantalla por Spinner
                        arr.add(add);
                    }
            }
        return arr;
    }








    //Esta funcion es usada para llenar spinners

    public ArrayList<Constructor_Dato> getcTablasSorteadas(String Titulo,String Detalle,String Extra)//Recorre todos los arraylists, y crea uno solo con un solo tipo de dato y un detalle a elegir
    {
        ArrayList<Constructor_Dato> arr = new ArrayList<>();
        Constructor_Dato add;
        for(ArrayList<Constructor_Dato> c:cTablas)
            {
                String agregarDatoExtra = null;
                String agregarDatoExtraTag2 = null;

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



                        if (c.get(r).getTitulo().equals(Titulo))  //revisa la descripcion con la que se quiere agregar
                            {

                                c.get(r).setTag1(agregarDatoExtra);
                                c.get(r).setTag2(agregarDatoExtraTag2);

                                add = c.get(r);
                            }

                    }
                if (add != null)
                    {
                        add.setTag1(agregarDatoExtra);  //Agrega Detalle al Tag1 para ser tomado en pantalla por Spinner
                        add.setTag2(agregarDatoExtraTag2);
                        arr.add(add);
                    }
            }
        return arr;
    }


    public ArrayList<Constructor_Dato> getcTablasSorteadas(String Titulo,String Detalle,String Extra,String Extra2)//Recorre todos los arraylists, y crea uno solo con un solo tipo de dato y un detalle a elegir
    {
        ArrayList<Constructor_Dato> arr = new ArrayList<>();
        Constructor_Dato add;
        for(ArrayList<Constructor_Dato> c:cTablas)
            {
                String agregarDatoExtra = null;
                String agregarDatoExtraTag2 = null;
                String agregarDatoExtra2 = null;

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
                        if (c.get(r).getTitulo().equals(Extra2))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                            {
                                agregarDatoExtra2 = c.get(r).getDato();
                            }


                        if (c.get(r).getTitulo().equals(Titulo))  //revisa la descripcion con la que se quiere agregar
                            {

                                c.get(r).setTag1(agregarDatoExtra);
                                c.get(r).setTag2(agregarDatoExtraTag2);
                                c.get(r).setTag3(agregarDatoExtra2);

                                add = c.get(r);
                            }

                    }
                if (add != null)
                    {
                        add.setTag1(agregarDatoExtra);  //Agrega Detalle al Tag1 para ser tomado en pantalla por Spinner
                        add.setTag2(agregarDatoExtraTag2);
                        add.setTag3(agregarDatoExtra2);

                        arr.add(add);
                    }
            }
        return arr;
    }


    public ArrayList<Constructor_Dato> getcTablasSorteadas(String Titulo,String Detalle,String Extra,String Extra2,String Extra3,String Extra4)//Recorre todos los arraylists, y crea uno solo con un solo tipo de dato y un detalle a elegir
    {
        ArrayList<Constructor_Dato> arr = new ArrayList<>();
        Constructor_Dato add;
        for(ArrayList<Constructor_Dato> c:cTablas)
        {
            String agregarDatoExtra = null;
            String agregarDatoExtraTag2 = null;
            String agregarDatoExtra2 = null;
            String agregarDatoExtra3 = null;
            String agregarDatoExtra4 = null;

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
                if (c.get(r).getTitulo().equals(Extra2))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                {
                    agregarDatoExtra2 = c.get(r).getDato();
                }
                if (c.get(r).getTitulo().equals(Extra3))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                {
                    agregarDatoExtra3 = c.get(r).getDato();
                }
                if (c.get(r).getTitulo().equals(Extra4))  //revisa la descripcion de la variable que se quiere tomar para agregar al nuevo array contra la del dato2
                {
                    agregarDatoExtra4 = c.get(r).getDato();
                }


                if (c.get(r).getTitulo().equals(Titulo))  //revisa la descripcion con la que se quiere agregar
                {

                    c.get(r).setTag1(agregarDatoExtra);
                    c.get(r).setTag2(agregarDatoExtraTag2);
                    c.get(r).setTag3(agregarDatoExtra2);
                    c.get(r).setTag4(agregarDatoExtra3);
                    c.get(r).setTag5(agregarDatoExtra4);

                    add = c.get(r);
                }

            }
            if (add != null)
            {
                add.setTag1(agregarDatoExtra);  //Agrega Detalle al Tag1 para ser tomado en pantalla por Spinner
                add.setTag2(agregarDatoExtraTag2);
                add.setTag3(agregarDatoExtra2);
                add.setTag4(agregarDatoExtra3);
                add.setTag5(agregarDatoExtra4);

                arr.add(add);
            }
        }
        return arr;
    }



}
