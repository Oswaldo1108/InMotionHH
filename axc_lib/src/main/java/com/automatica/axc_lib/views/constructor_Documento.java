package com.automatica.axc_lib.views;

//import com.automatica.AXCMP.c_Consultas.ConstructorDatoTitulo;

import com.automatica.axc_lib.ClasesSerializables.Embarque;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.ArrayList;

public class constructor_Documento
{
    private String tagDocumento = null;
    private String Documento = null;
    private String TipoDocumento = null;
    private ArrayList<Constructor_Dato> Dato =  new ArrayList<>();
    private Embarque Partida;


    public  constructor_Documento InicializarDatosPrueba()
    {
        Dato = new ArrayList<>();
//        Documento = "00000168341";
//        TipoDocumento = "Orden Producción";
        Dato.add(new Constructor_Dato("1", "Desc 1"));
        Dato.add(new Constructor_Dato("2", "Desc 2"));
        Dato.add(new Constructor_Dato("3", "Desc 3"));

        return constructor_Documento.this;
    }


    public String getTagDocumento()
    {
        return tagDocumento;
    }

    public constructor_Documento setTagDocumento(String tagDocumento)
    {
        this.tagDocumento = tagDocumento;
        return  constructor_Documento.this;
    }

    public constructor_Documento(String documento, String tipoDocumento, ArrayList<Constructor_Dato> dato)
    {
        Documento = documento;
        TipoDocumento = tipoDocumento;
        Dato = dato;
//        Log.i("CONSULTA_INfO",Documento);
//        Log.i("CONSULTA_INfO",TipoDocumento);

    }

    public constructor_Documento(String tagDocumento, String documento, String tipoDocumento, ArrayList<Constructor_Dato> dato)
    {
        this.tagDocumento = tagDocumento;
        Documento = documento;
        TipoDocumento = tipoDocumento;
        Dato = dato;
    }

    public String getDocumento()
    {
        return Documento;
    }

    public void setDocumento(String documento)
    {
        Documento = documento;
    }

    public String getTipoDocumento()
    {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento)
    {
        TipoDocumento = tipoDocumento;
    }

    public ArrayList<Constructor_Dato> getDato()
    {
        return Dato;
    }

    public void setDato(ArrayList<Constructor_Dato> dato)
    {
        Dato = dato;
    }

    @Override
    public String toString()
    {
        return "Documento - "+ Documento +"\n"+
               "Tipo Documento - "+ TipoDocumento + "\n"+
               "Tamaño Array - " + String.valueOf(Dato.size());

    }





    //SE USA PARA MANTENER EL REGISTRO DE LA PARTIDA QUE REPRESENTA CADA TARJETA
    public Embarque getPartida()
    {
        return Partida;
    }

    public void setPartida(Embarque partida)
    {
        Partida = partida;
    }
}
