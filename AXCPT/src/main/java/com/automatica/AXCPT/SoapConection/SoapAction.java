package com.automatica.AXCPT.SoapConection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SoapAction
{


//region variablesGlobales
    private String  NAMESPACE ="http://tempuri.org/" , URL, SOAP_ACTION;
    public String  decision;
    public String  mensaje;
    public String  tabla;
    private  int tipoObjeto;

    private   SoapObject TablaSOAP ;

    public ArrayList<String> arrayListCaso3;
//endregion


//region getPreferencias
    public String getUsuarioSharedPreferences(Context contexto)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        String usuarioPref = pref.getString("usuario", "null");
        return usuarioPref;
    }
    public String getEstacionSharedPreferences(Context contexto)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        String estacionPref = pref.getString("estacion", "null");
        return estacionPref;
    }
//endregion


//region funciones

public void SOAPLogin(String usuario, String contraseña, String estacion, String autorizacion, Context contexto)
    {

        tipoObjeto = 0;
        String METHOD_NAME = "WM_Login";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmUsuario",usuario);
        request.addProperty("prmPassword",contraseña);
        request.addProperty("prmEstacion",estacion);
        request.addProperty("prmAutorizacion", autorizacion);

        IniciaAccionSOAP(request,METHOD_NAME, contexto, tipoObjeto,null);


    }

public void SOAPentradaAlmacen(String empaque,Context contexto)
    {
        int tipoObjeto = 1;
        String METHOD_NAME = "WM_ConsultaEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


        request.addProperty("prmCodigoEmpaque", empaque);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);


    }
public void SOAPregistraPalletLineaImpreso(String prmOrden,String prmCierre,String prmLinea,String prmPrimerEmpaque,
                                           String prmUltimoEmpaque,String prmCantidadEmpaques,Context contexto)
{
    int tipoObjeto = 0;

    String METHOD_NAME = "WM_RegistraPalletLineaPreimpreso";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrden",prmOrden);
    request.addProperty("prmCierre",prmCierre);
    request.addProperty("prmLinea",prmLinea);
    request.addProperty("prmPrimerEmpaque",prmPrimerEmpaque);
    request.addProperty("prmUltimoEmpaque",prmUltimoEmpaque);
    request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,"http://192.168.1.181/AXCMaulec/wsAXCMP.asmx");
}
public void SOAPcolocaPallet(String prmCodigoPallet,String prmUbicacion, Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ColocaPallet";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


    request.addProperty("prmCodigoPallet", prmCodigoPallet);
    request.addProperty("prmUbicacion", prmUbicacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);


}



    //region Traspasos PIASA

    public void SOAPListarPartidasTraspaso(String prmDocumento,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ListarPartidasRecepcionTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPRegistrarEmpaqueTraspaso(String prmDocumento,String prmPartida,String prmCodigoEmpaque,
                                             String prmCantidad,String prmEmpaques,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraEmpaqueTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPRegistraPrimeraYUltimaTraspaso(String prmDocumento,String prmPartida,String prmCantidad, String prmEmpaques,String prmPrimerEmpaque, String prmUltimoEmpaque,String prmCantidadEmpaques, Context contexto)
    {


        int tipoObjeto = 2;
        String METHOD_NAME = "WM_PrimeraUltimaTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCantidad",prmCantidad);
        //request.addProperty("prmEmpaques", prmEmpaques);
        request.addProperty("prmPrimera",prmPrimerEmpaque);
        request.addProperty("prmUltima",prmUltimoEmpaque);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCierraPalletTraspaso(String prmCodigoPallet,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_OCCierraPalletTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPConsultaPalletAbiertoTraspasoMP(String prmDocumento, String prmPartida, Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaPalletAbiertoTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    public void SOAPSugierePalletTraspasoEnvio(String prmDocumento,String prmPartida,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaPalletSugeridoTraspasoEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPSugiereEmpaqueTraspasoEnvio(String prmDocumento,String prmPartida,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaEmpaqueSugeridoTraspasoEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    public void SOAPRegistrarPalletTraspasoEnvio(String prmDocumento,String prmPartida,String prmCodigoPallet,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistroTraspasoPalletEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }




    public void SOAPRegistrarEmpaqueTraspasoEnvio(String prmDocumento,String prmPartida,String prmCodigoEmpaque,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistroTraspasoEmpaqueEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCierraPalletTraspasoEnvio(String prmDocumento,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CierrePalletTraspasoEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    public void SOAPReciboPalletTraspasoConEtiquetas(String prmDocumento,String prmPartida,String prmCodigoPallet,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistroTraspasoPalletConEtiquetas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    //endregion


    //region Recepcion
    public void SOAPListarPartidasOCLiberadas(String Partida, Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ListarPartidasOCLiberadas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",Partida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        // request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCerrarRecepcion(String OrdenCompra,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CerrarRecepcion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",OrdenCompra);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPRegistrarEmpaqueCompra(String OrdenCompra,String Partida,String CodigoEmpaque,String LoteProveedor,
                                           String Cantidad,String Empaques,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_OCRegistrarEmpaqueCompra";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",OrdenCompra);
        request.addProperty("prmPartida",Partida);
        request.addProperty("prmCodigoEmpaque",CodigoEmpaque);
        request.addProperty("prmLoteProveedor",LoteProveedor);//En realidad es factura
        request.addProperty("prmCantidad",Cantidad);
        request.addProperty("prmEmpaques",Empaques);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPConsultaPalletAbiertoOC(String Recepcion, String Partida, Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultarPalletAbiertoOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmIdRecepcion",Recepcion);
        request.addProperty("prmPartida",Partida);

        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPCreaPalletMPCompraUnico(String OrdenCompra,String Partida,String LoteProveedor, String Cantidad, String Empaques, Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultarPalletAbiertoOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",OrdenCompra);
        request.addProperty("prmPartida",Partida);
        request.addProperty("prmloteProveedor",LoteProveedor);
        request.addProperty("prmCantidad",Cantidad);
        request.addProperty("prmEmpaques",Empaques);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCierraPalletCompra(String Pallet, Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_OCCierraPalletMPCompra";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmPallet",Pallet);

        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    public void SOAPRegistraMPPrimerasYUltimas(String OrdenCompra,String Partida,String Proveedor, String Cantidad,String Empaques,String Primera, String Ultima,String CantidadEmpaques, Context contexto)
    {


        int tipoObjeto = 2;
        String METHOD_NAME = "WM_OCCreaPalletMPPrimeraUltima";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",OrdenCompra);
        request.addProperty("prmPartida",Partida);
        request.addProperty("prmLoteProveedor",Proveedor);
        request.addProperty("prmCantidad", Cantidad);
        request.addProperty("prmEmpaques",Empaques);
        request.addProperty("prmPrimera",Primera);
        request.addProperty("prmUltima",Ultima);
        request.addProperty("prmCantidadEmpaques",CantidadEmpaques);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCompraPalletMPCompraUnico(String OrdenCompra,String Partida, String Proveedor, String Cantidad,String Empaques, Context contexto)
    {



        int tipoObjeto = 2;
        String METHOD_NAME = "WM_OCCreaPalletMPCompraUnico";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",OrdenCompra);
        request.addProperty("prmPartida",Partida);
        request.addProperty("prmLoteProveedor",Proveedor);
        request.addProperty("prmCantidad", Cantidad);
        request.addProperty("prmEmpaques",Empaques);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);

    }

    public void SOAPListarLotesOC(String prmOrdenCompra,String prmPartida,String prmNumParte, Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ListarLotesOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPListarLotesMaquilaOC(String prmOrdenCompra,String prmPartida,String prmNumParte, Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ListarLotesOCMaquila";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void     SOAPListarOrdenesCompraLiberadas(Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ListarOCLiberadas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);


    }

    //endregion

    //region EnvioMaquila
    public void SOAPListarPartidasEnvioMaquila(String prmDocumento, Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ListarPartidasEnvioMaquila";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);


    }
    public void SOAPConsultarPartidaEnvioMaquila(String prmDocumento,String prmPartida,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultarPartidaEnvioMaquila";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPSugierePalletEnvioMaquila(String prmDocumento,String prmPartida,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_SugierePalletEnvioMaquila";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPEnviaPalletMaquila(String prmDocumento,String prmPartida,String prmPallet,String prmUbicacion,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_EnviaPalletMaquila";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


        request.addProperty("prmOrdenCompra", prmDocumento);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPEnviaEmpaqueMaquila(String prmDocumento,String prmPartida,String prmEmpaque,String prmUbicacion,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_EnviaEmpaqueMaquila";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


        request.addProperty("prmOrdenCompra", prmDocumento);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    //endregion

//region Movimientos Almacen
public void SOAPConsultaPalletAColocar(String prmCodigoPallet,Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaPalletAColocar";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoPallet", prmCodigoPallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

public void SOAPSugierePosicion(String prmCodigoPallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_SugierePosicion";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoPallet", prmCodigoPallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPReubicarEmbalaje(String prmCodigoPallet,String prmCodigoUbicacion,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ReubicarEmbalaje";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoPallet", prmCodigoPallet);
    request.addProperty("prmUbicacion", prmCodigoUbicacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPAjustesPositivos(Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ListarAjustes";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPConsultaPalletAbiertoSinAfecta(Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultarPalletAbiertoSinAfecta";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

public void SOAPRegistraEmpaqueUnico (String NumParte, String Cantidad,String Ajuste,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OCCreaPalletMPAjusteUnico";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmNumParte",NumParte);
    request.addProperty("prmCantidad",Cantidad);
    request.addProperty("prmAjuste",Ajuste);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPRegistrarAjusteNuevoPallet(String prmCodigoEmpaque,String prmCantidad,String prmAjuste, Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_RegistraAjusteNvoPallet";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
    request.addProperty("prmCantidad",prmCantidad);
    request.addProperty("prmAjuste",prmAjuste);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));


    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}


public void SOAPBuscarArticulos(String NumParte, Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaCoincidenciaArticulo";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmArticulo",NumParte);


    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPRegistraEmpaqueNuevoPallet(String Empaque,String NumParte, String Cantidad,String Ajuste,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_RegistraEmpaqueNvoPallet";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoEmpaque",Empaque);
    request.addProperty("prmNumParte",NumParte);
    request.addProperty("prmCantidad",Cantidad);
    request.addProperty("prmAjuste",Ajuste);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

    public void SOAPRegistraEmpaqueNuevoPallet(String Empaque,String NumParte, String Cantidad,String Ajuste,String prmFechaCad,String prmLote,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraEmpaqueNvoPalletv2";

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",Empaque);
        request.addProperty("prmNumParte",NumParte);
        request.addProperty("prmCantidad",Cantidad);
        request.addProperty("prmAjuste",Ajuste);
        request.addProperty("prmFechaCad",prmFechaCad);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
    }

    public void SOAPCierraPalletAjuste(String CodigoEmpaque,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CierraPalletAjuste";

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",CodigoEmpaque);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
    }

public void SOAPCierraPalletAjuste(String prmCodigoPallet,String prmTipo, Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_CierraPalletAjuste";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoPallet",prmCodigoPallet);
    request.addProperty("prmTipo",prmTipo);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPListarConceptosAjuste(String TipoAjuste,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ListarConceptosAjuste";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmTipoAjuste",TipoAjuste);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPAjusteNuevoEmpaquePalletExistente(String prmEmpaque, String prmPallet, String prmNumParte, String prmCantidad, String prmRevision, String prmAjuste, Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_AjusteNuevoEmpaquePalletExistente";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmEmpaque",prmEmpaque);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmNumParte",prmNumParte);
    request.addProperty("prmCantidad",prmCantidad);
    request.addProperty("prmRevision",prmRevision);
    request.addProperty("prmAjuste",prmAjuste);

    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPConsultarEmpaque(String prmEmpaque,Context contexto)
{
    int tipoObjeto = 3;
    String METHOD_NAME = "WM_ConsultarEmpaque";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmEmpaque",prmEmpaque);
   /* request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));*/
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPAjusteBajaEmpaque(String prmEmpaque,String prmAjuste,Context contexto)
{

    int tipoObjeto = 3;
    String METHOD_NAME = "WM_AjusteBajaEmpaque";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmEmpaque",prmEmpaque);
    request.addProperty("prmAjuste",prmAjuste);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPAjusteBajaPallet(String prmPallet,String prmAjuste,Context contexto)
{

        int tipoObjeto = 3;
        String METHOD_NAME = "WM_AjusteBajaPallet";

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmAjuste",prmAjuste);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
        IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
    }

public void SOAPListarPartidasOrdenesSurtido(String prmOrdenSurtido,Context contexto)
{

        int tipoObjeto = 3;
        String METHOD_NAME = "WM_OSListarPartidasOrdenesSurtido";

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmOrdenSurtido);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
        IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
    }

public void SOAPConsultaPalletConsAbierto( Context contexto)
{
    int tipoObjeto = 3;
    String METHOD_NAME = "WM_OSConsultaPalletConsAbierto";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPTransfersLiberados(Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ListarTransfersLiberados";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPConsultaPalletSalidaLiberado( String prmInterOrg,Context contexto)
{
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ListarPartidasInterorgSalidaLiberado";

        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmInterorg",prmInterOrg);
        //request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
        IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPConsultaEmpaqueRegular(String CodigoEmpaque, Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaEmpaque";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoEmpaque",CodigoEmpaque);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}


public void SOAPRegistraEmpaqueInterorgSalida(String Interorg, String prmEmpaque,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_RegistraEmpaqueInterorgSalida";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmInterorg",Interorg);
    request.addProperty("prmPallet",prmEmpaque);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPListarTransferenciasARecibir(Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ListarTransfersARecibir";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);

}


    //region Registro Pallets PT
    public void SOAPConsultaOrdenProduccion_V2(String prmDocumento,String prmTipo,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaOrdenProduccion_v2";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
    public void SOAPConsultaOrdenProduccion(String prmDocumento,Context contexto)
    {
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaOrdenProduccion";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmDocumento", prmDocumento);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCerrarOrdenProduccion(String prmDocumento,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CerrarOrdenProduccion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmDocumento);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCerrarOrdenProduccion_v2(String prmDocumento,String prmFechaCaducidad,String prmTipo,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CerrarOrdenProduccion_v2";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmDocumento);
        request.addProperty("prmFechaCad", prmFechaCaducidad);
       // request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    public void SOAPRegistrarCantidadOrdenProduccion(String prmOrdenProduccion,String prmCantidad,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraCantidadOrdenProduccion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenProduccion);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPRegistrarSobranteOrdenProduccion(String prmOrdenProduccion,String prmSobrante,String prmEmpaque,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraSobranteOrdenProduccion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenProduccion);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmCantidadSobrante", prmSobrante);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPRegistraFechaCadOrdenProd(String prmOrdenProduccion,String prmFechaCad,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraFechaCadOrdenProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenProduccion);
        request.addProperty("prmFechaCad", prmFechaCad);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    //endregion

    //region Armado de pallets

    public void SOAPConsultaEmpaquesRegistroProduccion(String prmOrdenProd, Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaEmpaquesArmadoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd", prmOrdenProd);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPRegistraEmpaqueEnPallet(String prmEmpaque,String prmOrdenPoduccion,String prmTipo,String prmCantidad,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraEmpaqueEnPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden", prmOrdenPoduccion);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPRegistraEmpaquesEnPalletPrimeraYUltima(String prmPrimerEmpaque,String prmUltEmpaque,/*String prmCantidadTarimas,*/String prmOrdenPoduccion,String prmTipo,String prmCantidad,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraEmpaqueEnPalletPrimeraYUltima";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden", prmOrdenPoduccion);
        request.addProperty("prmPrimerEmpaque", prmPrimerEmpaque);
        request.addProperty("prmUltimoEmpaque", prmUltEmpaque);
      //  request.addProperty("prmCantidadTarimas", prmCantidadTarimas);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCerrarRegistroPallet(String prmOrdenPoduccion,String prmFechaCaducidad,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CerrarRegistroPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenPoduccion);
        request.addProperty("prmFechaCaducidad", prmFechaCaducidad);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPCancelarRegistroPallet(String prmOrdenPoduccion,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CancelarRegistroPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenPoduccion);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPBajaEmpaqueArmadoTarimas(String prmOrdenPoduccion,String prmEmpaque,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_BajaEmpaqueArmadoTarimas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden", prmOrdenPoduccion);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    //endregion



//endregion


//region consulta
public void SOAPconsultaUbicacion(String consultaUbicacion, Context contexto)
{
    int tipoObjeto = 1;
    String METHOD_NAME = "WM_BuscarUbicacion";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmUbicacion",consultaUbicacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);

}


public void SOAPConsultaPallet(String ConsultaPallet,Context contexto)
{
    int tipoObjeto = 3;
    String METHOD_NAME = "WM_ConsultarPalletPT";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmEmbalaje",ConsultaPallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPConsultaEmpaque(String ConsultaEmpaque,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaEmpaqueMP";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoEmpaque",ConsultaEmpaque);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPConsultaProducto(String Producto,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME ="WM_ConsultaCoincidenciaArticulo";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmArticulo",Producto);

    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPConsultaPalletArticulo(String NumParte,String Revision,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaPalletArticulo";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmNumParte",NumParte);
    request.addProperty("prmRevision",Revision);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    Log.i("SoapResponse",request.toString());
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPConsultaReferencia(String datoConsulta,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaReferencia";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmTexto",datoConsulta);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPEmbarquesPrecarga(String prmOrdenSurtido,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME ="WM_OSListarPartidasOSPrecarga";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido", prmOrdenSurtido);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPRegistraPalletInterSalida(String InterOrg,String Pallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME ="WM_RegistraPalletInterorgSalida";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmInterorg", InterOrg);
    request.addProperty("prmPallet", Pallet);

    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);

}

//endregion

//region Embarques
public void SOAPSugerirPalletPrecarga(String OrdenSurtido,String Partida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSugerirPalletPrecarga";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",OrdenSurtido);
    request.addProperty("prmPartida",Partida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPConsultaPalletASurtir(String CodigoPallet,String OrdenSurtido,String Partida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaPalletASurtir";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmCodigoPallet",CodigoPallet);
    request.addProperty("prmOrdenSurtido",OrdenSurtido);
    request.addProperty("prmPartida",Partida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPSurtirPalletPrecarga(String OrdenSurtido,String Partida,
                                     String CodigoPallet,String ZonaPreparacion,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSurtirPalletPrecarga";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmOrdenSurtido",OrdenSurtido);
    request.addProperty("prmPartida",Partida);
    request.addProperty("prmCodigoPallet",CodigoPallet);
    request.addProperty("prmZonaPrepara",ZonaPreparacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPListarPartidasOrdenSurtido(String OrdenSurtido,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSListarPartidasOrdenesSurtido";

    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",OrdenSurtido);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPConsultarPartidaOrdenesSurtido(String prmOrdenSurtido,String prmPartida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSConsultaPartidaOrdenesSurtido";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPSugierePallet(String prmOrdenSurtido,String prmPartida, Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSugerirPallet";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPSurtirPallet(String prmOrdenSurtido,String prmPartida,String prmCodigoPallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSurtirPallet";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmCodigoPallet",prmCodigoPallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);

}
public void SOAPConsultaEmpaqueASurtir(String prmCodigoEmpaque,String prmOrdenSurtido,String prmPartida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaEmpaqueASurtir";//este no es
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPSugerirEmpaqueUnidad(String prmOrdenSurtido,String prmPartida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSugerirEmpaqueUnidad";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPSurtirPiezas(String prmOrdenSurtido,String prmPartida,String prmCodigoEmpaque,String prmCantidadPiezas,Context contexto){
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSurtirPiezas";//este no es
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
    request.addProperty("prmCantidadPiezas",prmCantidadPiezas);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPSugiereEmpaque(String prmOrdenSurtido,String prmPartida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSugerirEmpaque";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPSurtirEmpaque(String prmPartida,String prmOrdenSurtido,String prmCodigoEmpaque,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSSurtirEmpaque";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOrdenSurtido",prmOrdenSurtido);
    request.addProperty("prmPartida",prmPartida);
    request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPListarPartidasAValidar(String prmOrdenSalida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSListarPartidasAValidar";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdOS",prmOrdenSalida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPConsultarPalletAValidar(String pallet,Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSConsultarPalletAValidar";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmPallet",pallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPRegistrarPalletLineaValidado(String OrdenSalida,String Pallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSRegistrarPalletLineaValidado";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOS",OrdenSalida);
    request.addProperty("prmPallet",Pallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPRegistrarPalletLineaRechazado(String OrdenSalida,String Pallet,Context contexto)
{
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_OSRegistrarPalletLineaRechazado";
        Log.d("SoapResponse",METHOD_NAME);
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOS",OrdenSalida);
        request.addProperty("prmPallet",Pallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
        IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
    }
public void SOAPListarPalletsAEntregar(String OrdenSalida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSListarPalletsAEntregar";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdOS",OrdenSalida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPRegistrarPalletEntregado(String OrdenSalida,String Pallet, String Linea,String UsuarioEntrega, Context contexto)
{
    int tipoObjeto = 3;
    String METHOD_NAME = "WM_OSRegistrarPalletEntregado";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOS",OrdenSalida);
    request.addProperty("prmPallet",Pallet);
    request.addProperty("prmLinea",Linea);
    request.addProperty("prmUsuarioEntrega",UsuarioEntrega);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPCierraEntregaParcial(String OrdenSalida,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_CierraEntregaParcial";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmOS",OrdenSalida);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}

public void SOAPConsultaPalletPKAbierto(Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSConsultaPalletPKAbierto";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
public void SOAPImprimirEtiquetas(String prmPallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_OSCerrarPalletPK";
    Log.d("SoapResponse",METHOD_NAME);
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));
    IniciaAccionSOAP(request,METHOD_NAME,contexto,tipoObjeto,null);
}
//endregion

//region Inventarios

        //region ciclicos posicion
public void SOAPConsultaInventarioCiclicoPosicion(Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaInvCiclicoPosicion";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);

}
public void SOAPConsultaInventarioPosicion(String IdInventiario,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaInventarioPosicion";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("pIdInventario",IdInventiario);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPConsultaUbicacion(String IdInventario,String Ubicacion,Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_INVConsultaUbicacion";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",IdInventario);
    request.addProperty("prmUbicacion",Ubicacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPBajaPalletInventario(String IdInventario,String Pallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_BajaPalletInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",IdInventario);
    request.addProperty("prmPallet",Pallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

public void SOAPRegistrarPalletNuevo(String IdInventario, String Pallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_CierraPalletInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",IdInventario);
    request.addProperty("prmPallet",Pallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPRegistrarEmpaqueNuevoPalletInventario(String prmIdInventario,String  prmPallet,String  prmEmpaque,String  prmNumParte,
                                                      String  prmRevision,String  prmCantidad,String  prmPosicion,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_RegistraEmpaqueNuevoPalletInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmEmpaque",prmEmpaque);
    request.addProperty("prmNumParte",prmNumParte);
    request.addProperty("prmRevision",prmRevision);
    request.addProperty("prmCantidad",prmCantidad);
    request.addProperty("prmPosicion",prmPosicion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}


    public void SOAPRegistrarEmpaqueNuevoPalletInventarioFechaCadLote(String prmIdInventario,String  prmPallet,String  prmEmpaque,String  prmNumParte,
                                                          String  prmRevision,String  prmCantidad,String prmPosicion,String prmLote,String prmFechaCad,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraEmpaqueNuevoPalletInventarioFechaCadLote";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmRevision",prmRevision);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmFechaCad",prmFechaCad);

        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


public void SOAPConsultaExisteNumParte(String NumParte,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaExisteNumParte";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmNumParte",NumParte);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPBuscaInfoPallet(String IdInventario,String prmPallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaPalletInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",IdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPRegistraPalletNormal(String prmIdInventario,String  prmPallet,String  prmUbicacion,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_RegistraPalletInvSinCambio";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmUbicacion",prmUbicacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPEditarPalletRegistroInventario(String prmIdInventario,String prmPallet,String prmUbicacion,String prmRevision,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_EditaRegistroPalletInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmUbicacion",prmUbicacion);
    request.addProperty("prmRevision",prmRevision);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}
public void SOAPBajaEmpaqueInventario(String prmIdInventario,String Empaque,Context contexto)
{

    int tipoObjeto = 2;
    String METHOD_NAME = "WM_BajaEmpaqueInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmEmpaque",Empaque);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

public void SOAPEditaRegistroEmpaqueInventario(String prmIdInventario, String prmPallet, String prmEmpaque, String prmUbicacion, String prmCantidad,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_EditaRegistroEmpaqueInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmEmpaque",prmEmpaque);
    request.addProperty("prmUbicacion",prmUbicacion);
    request.addProperty("prmCantidad",prmCantidad);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

    public void SOAPEditaRegistroEmpaqueInventarioConLote(String prmIdInventario, String prmPallet, String prmEmpaque, String prmUbicacion, String prmCantidad,String prmLote,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_EditaRegistroEmpaqueInventarioConLote";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }



public void SOAPConsultaEmpaqueInventario(String prmIdInventario,String prmEmpaque,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaEmpaqueInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmEmpaque",prmEmpaque);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}


public void SOAPConsultaEmpaquePorPalletInventario(String prmIdInventario,String prmPallet,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaEmpaquesPorPalletInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

public void SOAPRegistraNuevoEmpaqueInventario(String prmIdInventario,String  prmPallet,String prmEmpaque,String prmCantidad,String prmUbicacion,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_RegistraNuevoEmpaquePalletInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmEmpaque",prmEmpaque);
    request.addProperty("prmCantidad",prmCantidad);
    request.addProperty("prmUbicacion",prmUbicacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

    public void SOAPRegistraNuevoEmpaqueInventarioConLote(String prmIdInventario,String  prmPallet,String prmProducto,String prmEmpaque,String prmCantidad,String prmLote,String prmUbicacion,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistraNuevoEmpaquePalletInventarioConLote";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

public void SOAPRegistraEmpaqueInventario(String prmIdInventario,String prmPallet, String prmEmpaque,String prmUbicacion,Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_RegistraEmpaqueInventario";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
    request.addProperty("prmIdInventario",prmIdInventario);
    request.addProperty("prmPallet",prmPallet);
    request.addProperty("prmEmpaque",prmEmpaque);
    request.addProperty("prmUbicacion",prmUbicacion);
    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
}

    //endregion

        //region ciclicos por articulo
    public void SOAPConsultaInventarioCiclicoNumeroParte(Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaInvCiclicoNumParte";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    public void SOAPConsultaInventarioNumeroParte(String IdInventario,Context contexto)
    {
        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaInventarioNumParte";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("pIdInventario",IdInventario);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }
    //endregion

        //region ciclico total
public void SOAPConsultaInventarioFisico(Context contexto)
{
    int tipoObjeto = 2;
    String METHOD_NAME = "WM_ConsultaInvFisico";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


    request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
    request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

    IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);

}
    //endregion

//endregion

    //region Embarques PIASA

    public void SOAPConsultaPedidoSurtido(String prmPedidoSurtido,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaPedidoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedidoSurtido);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPConsultaPedidoDetPartida(String prmPedido,String prmPartida,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaSurtidoDetPartida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPConsultaEmpaqueSurtido(String prmPedido,String prmPartida,String prmEmpaque,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaEmpaqueSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPConsultaPalletSurtido(String prmPedido,String prmPartida,String prmPallet,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaPalletSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPConsultaTarimaConsolidada(String prmPallet,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaTarimaConsolidada";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }



    public void SOAPRegistraEmpaqueSurtido(String prmPedido,String prmPartida,String prmEmpaque,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistroEmpaqueSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPRegistraPalletSurtido(String prmPedido,String prmPartida,String prmPallet,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistroPalletSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);//Aqui se envia pallet
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPCierraPalletSurtido(String prmPallet,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_CierraPalletSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPSugierePalletSurtido(String prmPedido,String prmPartida,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaPalletSugerido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPSugiereEmpaqueSurtido(String prmPedido,String prmPartida,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaEmpaqueSugerido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPConsultaPalletEntregaSurtido(String prmCodigoEmpaque,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_ConsultaEmpaqueEntregaSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }

    public void SOAPEntragaPalletSurtido(String prmCodigoPallet,String prmUbicacion,Context contexto)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_EntregaSurtidoLinea";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",getEstacionSharedPreferences(contexto));
        request.addProperty("prmUsuario",getUsuarioSharedPreferences(contexto));

        IniciaAccionSOAP(request,METHOD_NAME,contexto, tipoObjeto,null);
    }


    //endregion

//endregion


// region ConexionWebService

    private void IniciaAccionSOAP(SoapObject objeto,String METHOD_NAME, Context contexto, int tipoObjeto,String URL)
    {

        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);

        if(URL!=null)
        {
            this.URL = URL;
        }else
            {
            URL = pref.getString("direccionWebService", "http://192.168.1.181/AXCPT/wsAXCMP.asmx");
            }
        SOAP_ACTION = NAMESPACE + METHOD_NAME;
        try
        {
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(objeto);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            transporte.debug= true;


            try {
                transporte.call(SOAP_ACTION, soapEnvelope);
                SoapObject sp =(SoapObject) soapEnvelope.getResponse();
                String requestDump = transporte.requestDump;
                String responseDump = transporte.responseDump;


                switch (tipoObjeto)
                {
                    case 0://cuando necesitas mensaje y decision
                        SoapPrimitive s1 = (SoapPrimitive)sp.getProperty(0);
                        SoapPrimitive s2 = (SoapPrimitive)sp.getProperty(1);
                        decision = s1.toString();
                        mensaje =  s2.toString();
                        break;

                    case 1: //cuando necesitas regresar decision, tabla y estado

                        decision = sp.getProperty(0).toString();
                        mensaje =  sp.getProperty(1).toString();

                        TablaSOAP = (SoapObject) sp.getProperty(2);
                        break;

                    case 2://cuando solo necesitas regresar la decision

                        SoapPrimitive s = (SoapPrimitive)sp.getProperty(0);
                        decision = s.toString();
                        mensaje = sp.getProperty(1).toString();

                        if(sp.getPropertyCount()>2)
                        {
                            TablaSOAP = (SoapObject) sp.getProperty(2);

                        }
                        break;
                    case 3: //toma los Datos que recibe como RowSet, y los convierte en un SoapObject para entregar a la pantalla
                        arrayListCaso3 = new ArrayList<>();
                        int cantidadDatos = sp.getPropertyCount();
                        decision = sp.getPrimitivePropertyAsString("Estado");
                        mensaje =  sp.getPrimitivePropertyAsString("Texto");
                        TablaSOAP = sp;

                        for(int i =2;i<=cantidadDatos-1;i++) //inicia en 2 para evitar tomar decision y texto && -2 por la misma razón
                        {
                            String dato= sp.getProperty(i).toString();

                            arrayListCaso3.add(dato);


                        }
                        break;

                }

                Log.i("SoapResponse", " >>>------- "+ METHOD_NAME +" ------>>> \n");
                Log.i("SoapResponse", " >>>>>>" + requestDump);
                Log.i("SoapResponse", " >>>------- "+ METHOD_NAME +" ------>>> \n");

                Log.i("SoapResponse", " <<<------- "+ METHOD_NAME +" ------<<< \n");
                Log.i("SoapResponse", " <<<<<<" + responseDump);
                Log.i("SoapResponse", " <<<------- "+ METHOD_NAME +" ------<<< \n");
            } catch (Exception e)
            {
                if (mensaje==null&&decision==null)
                {
                    mensaje = e.getMessage();
                    decision = "false";
                }
                if ((mensaje.equals("anyType{}")||mensaje==null)&&decision.equals("true"))
                {
                    mensaje = e.getMessage();
                    decision = "false";
                }
                e.printStackTrace();

            }

        }catch  (Exception ex)
        {
                decision = "false";
                mensaje = ex.getMessage();

                if(mensaje.contains("BufferedInputStream"))
                    {
                        mensaje = "Error de conexión.";
                    }

            ex.printStackTrace();
        }

    }
//endregion


//region getters
    public String getMensaje() {
        return mensaje;
    }

    public String getDecision() {
        return decision;
    }

    public String getTabla() { return tabla;}

    public SoapObject getTablaSoap()
    {
        return TablaSOAP;
    }

    public ArrayList<String> getArrayListCaso3() {
        return arrayListCaso3;
    }

    public void setTablaSOAP(SoapObject tablaSOAP) {
        TablaSOAP = tablaSOAP;
    }


    public void setDecision(String decision)
    {
        this.decision = decision;
    }

    public void setMensaje(String mensaje)
    {
        this.mensaje = mensaje;
    }

    //endregion

//region parser



    public SoapObject parser()
    {
        SoapObject tabla1 = null;
        try {

            SoapObject tabla = (SoapObject) TablaSOAP.getProperty(1);
            Log.d("SoapResponse", tabla.toString());
            if(!tabla.toString().equals("anyType{}"))
            {
                tabla1 = (SoapObject) tabla.getProperty(0);
                Log.d("SoapResponse", tabla.toString());
                setTablaSOAP(null);
                /*mensaje = "Error: Tabla regresa Datos nulos (Parseo)";
                decision="false";*/
            }else
                {
                return null;
                }
        } catch (Exception e)
            {
            e.printStackTrace();
            mensaje = "Soap Table = null";
            return null;
        }
        return tabla1;
    }



//endregion


}

