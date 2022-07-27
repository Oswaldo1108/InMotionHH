package com.automatica.axc_lib.ClasesSerializables;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class Embarque  implements KvmSerializable
{
    //Xml variables
    private String OrdenProd;
    private String Partida;
    private String NumParte;
    private String CantidadPedida;
    private String CantidadSurtida;
    private String CantidadPendiente;
    private String DStatus;
    private String UnidadMedida;
    private String Tag1;
    private String Tag2;
    private String Tag3;
    private String FechaCrea;
    private String Lote;
    private String Estacion;


    private ArrayList<Embarque> LotesPartida;


    public ArrayList<Embarque> getLotesPartida()
    {

        if (LotesPartida == null)
            {
                LotesPartida = new ArrayList<>();
            }

        return LotesPartida;
    }
    public void AgregarPartida(Embarque e)
    {
        if (LotesPartida == null)
            {
                LotesPartida = new ArrayList<>();
            }

        LotesPartida.add(e);
    }

    public void AgregarPartidas(ArrayList<Embarque> e)
    {
//        if (LotesPartida == null)     Si se quiere agregar partidas descomentarizar esto
//            {
//                LotesPartida = new ArrayList<>();
//            }

//        LotesPartida.addAll(e);

        LotesPartida = e;
    }


    public String getDNumParte1()
    {
        return DNumParte1;
    }

    public void setDNumParte1(String DNumParte1)
    {
        this.DNumParte1 = DNumParte1;
    }

    private String DNumParte1;


    public String getLote()
    {
        return Lote;
    }

    public void setLote(String lote)
    {
        Lote = lote;
    }

    public String getEstacion()
    {
        return Estacion;
    }

    public void setEstacion(String estacion)
    {
        Estacion = estacion;
    }

    public Embarque()
    {
    }
    public Embarque(String prmPartida,String prmNumParte,String prmCantidadPedida)
    {
        OrdenProd = "";
        Partida = prmPartida;
        NumParte= prmNumParte;
        CantidadPedida= prmCantidadPedida;
        CantidadSurtida= "";
        CantidadPendiente= "";
        DStatus= "";
        UnidadMedida= "";
        Tag1= "";
        Tag2= "";
        Tag3= "";
        FechaCrea= "";
        Lote= "";
        Estacion= "";
    }

    public String getOrdenProd()
    {
        return OrdenProd;
    }

    public String getPartida()
    {
        return Partida;
    }

    public String getNumParte()
    {
        return NumParte;
    }

    public String getCantidadPedida()
    {
        return CantidadPedida;
    }

    public String getCantidadSurtida()
    {
        return CantidadSurtida;
    }

    public String getCantidadPendiente()
    {
        return CantidadPendiente;
    }

    public String getDStatus()
    {
        return DStatus;
    }

    public String getUnidadMedida()
    {
        return UnidadMedida;
    }

    public String getTag1()
    {
        return Tag1;
    }

    public String getTag2()
    {
        return Tag2;
    }

    public String getTag3()
    {
        return Tag3;
    }

    public String getFechaCrea()
    {
        return FechaCrea;
    }

    public void setOrdenProd(String ordenProd)
    {
        OrdenProd = ordenProd;
    }

    public void setPartida(String partida)
    {
        Partida = partida;
    }

    public void setNumParte(String NumParte)
    {
        this.NumParte = NumParte;
    }

    public void setCantidadPedida(String cantidadPedida)
    {
        CantidadPedida = cantidadPedida;
    }

    public void setCantidadSurtida(String cantidadSurtida)
    {
        CantidadSurtida = cantidadSurtida;
    }

    public void setCantidadPendiente(String cantidadPendiente)
    {
        CantidadPendiente = cantidadPendiente;
    }

    public void setDStatus(String DStatus)
    {
        this.DStatus = DStatus;
    }

    public void setUnidadMedida(String unidadMedida)
    {
        UnidadMedida = unidadMedida;
    }

    public void setTag1(String tag1)
    {
        Tag1 = tag1;
    }

    public void setTag2(String tag2)
    {
        Tag2 = tag2;
    }

    public void setTag3(String tag3)
    {
        Tag3 = tag3;
    }

    public void setFechaCrea(String fechaCrea)
    {
        FechaCrea = fechaCrea;
    }

    @Override
    public Object getProperty(int i)
    {
        switch (i)
            {
                case 0:
                    return this.OrdenProd;
                case 1:
                    return this.Partida;
                case 2:
                    return this.NumParte;
                case 3:
                    return this.CantidadPedida;
                case 4:
                    return this.CantidadSurtida;
                case 5:
                    return this.CantidadPendiente;
                case 6:
                    return this.DStatus;
                case 7:
                    return this.UnidadMedida;
                case 8:
                    return this.Tag1;
                case 9:
                    return this.Tag2;
                case 10:
                    return this.Tag3;
                case 11:
                    return this.FechaCrea;
                case 12:
                    return this.Lote;
                case 13:
                    return this.Estacion;
                default: return null;
            }
    }

    @Override
    public int getPropertyCount()
    {
        return 0;
    }

    @Override
    public void setProperty(int i, Object o)
    {

        switch(i){
            case 0:
                this.OrdenProd = o.toString();
                break;
            case 1:
                this.Partida = o.toString();
                break;
            case 2:
                this.NumParte = o.toString();
                break;
            case 3:
                this.CantidadPedida = o.toString();
                break;
            case 4:
                this.CantidadSurtida = o.toString();
                break;
            case 5:
                this.CantidadPendiente = o.toString();
                break;
            case 6:
                this.DStatus = o.toString();
                break;
            case 7:
                this.UnidadMedida = o.toString();
                break;
            case 8:
                this.Tag1 = o.toString();
                break;
            case 9:
                this.Tag2 = o.toString();
                break;
            case 10:
                this.Tag3 = o.toString();
                break;
            case 11:
                this.FechaCrea = o.toString();
                break;
            case 12:
                this.Lote = o.toString();
                break;
            case 13:
                this.Estacion = o.toString();
                break;

        }
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo)
    {
        switch(i){
            case 0:
                propertyInfo.name = "OrdenProd";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = "Partida";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 2:
                propertyInfo.name = "NumParte";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 3:
                propertyInfo.name = "CantidadPedida";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 4:
               propertyInfo.name = "CantidadSurtida";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 5:
                propertyInfo.name = "CantidadPendiente";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 6:
                propertyInfo.name = "DStatus";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 7:
                propertyInfo.name = "UnidadMedida";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 8:
                propertyInfo.name = "Tag1";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 9:
                propertyInfo.name = "Tag2";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 10:
                propertyInfo.name = "Tag3";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 11:
                propertyInfo.name = "FechaCrea";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 12:
                propertyInfo.name = "Lote";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 13:
                propertyInfo.name = "Estacion";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;

        }
    }


}
