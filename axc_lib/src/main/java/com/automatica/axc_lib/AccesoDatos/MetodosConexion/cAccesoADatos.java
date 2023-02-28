package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.automatica.axc_lib.ClasesSerializables.Embarque;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class cAccesoADatos extends conexionWS2
{
    private String  NAMESPACE ="http://tempuri.org/";
    private String  Usuario,Estacion;
    private Context context;

        public cAccesoADatos(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "null");
        Estacion = pref.getString("estacion", "null");
        this.context = context;
    }



    public DataAccessObject cConsultaOrdenSurtido_Reempaque(String prmOrdenEmbarque, String prmStatus) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaOrdenSurtido_Reempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenEmbarque", prmOrdenEmbarque);
        request.addProperty("prmStatus", prmStatus);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cConsultaPalletCons (String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletCons";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cCancelaConsolidacion (String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CancelaConsolidacion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cRegistraEmpaqueCons (String prmPallet, String prmEmpaque) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistraEmpaqueCons";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cCerrarrConsolidacion (String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CerrarConsolidacion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cConsultaPalletReempaque (String prmEmbarque, String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletReempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbarque", prmEmbarque);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cCreaPalletReempaque (String prmEmbarque) {
    conexionWS c = new conexionWS();
    String METHOD_NAME = "WM_CreaPalletReempaque";
    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    request.addProperty("prmEmbarque", prmEmbarque);
    request.addProperty("prmEstacion", Estacion);
    request.addProperty("prmUsuario", Usuario);
    return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
}

    public DataAccessObject c_ConsultaArticulo(String prmArticulo)
    {
        String METHOD_NAME = "WM_ConsultaProdDet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmProducto",prmArticulo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaMaquinas()
    {
        String METHOD_NAME = "WM_ConsultaProdDet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_CerrarOrdenProduccion(String prmDocumento,String prmFechaCaducidad,String prmMaquina,String prmFechaActual,
                                                        String prmFechaTerminacion,String prmHoraTerminacion,String prmFechaInicio,String prmHoraInicio)
    {
        String METHOD_NAME = "WM_CerrarOrdenProduccion_v2";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmDocumento);
        request.addProperty("prmFechaCad", prmFechaCaducidad);
        request.addProperty("prmMaquina", prmMaquina);
        request.addProperty("prmFechaActual", prmFechaActual);
        request.addProperty("prmFechaTerminacion", prmFechaTerminacion);
        request.addProperty("prmHoraTerminacion", prmHoraTerminacion);
        request.addProperty("prmFechaInicio", prmFechaInicio);
        request.addProperty("prmHoraInicio", prmHoraInicio);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_CerrarOrdenProduccion_SAP(String prmDocumento,String prmFechaCaducidad,String prmMaquina,String prmFechaActual,
                                                            String prmFechaTerminacion,String prmHoraTerminacion,String prmFechaInicio,String prmHoraInicio)
    {
        String METHOD_NAME = "WM_CerrarOrdenProduccion_SAP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmDocumento);
        request.addProperty("prmFechaCad", prmFechaCaducidad);
        request.addProperty("prmMaquina", prmMaquina);
        request.addProperty("prmFechaActual", prmFechaActual);
        request.addProperty("prmFechaTerminacion", prmFechaTerminacion);
        request.addProperty("prmHoraTerminacion", prmHoraTerminacion);
        request.addProperty("prmFechaInicio", prmFechaInicio);
        request.addProperty("prmHoraInicio", prmHoraInicio);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }



    public DataAccessObject SOAPLogin()
    {
        String METHOD_NAME = "WM_ConsultaResumenMIAXC";
        return IniciaAccionSOAP( new SoapObject(NAMESPACE,METHOD_NAME).addProperty("prmUsuario",Usuario).addProperty("prmEstacion",Estacion),METHOD_NAME,context,null);
    }


    public DataAccessObject SOAPLogin(String usuario, String contraseña, String autorizacion)
    {
        String METHOD_NAME = "WM_Login";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmUsuario",usuario);
        request.addProperty("prmPassword",contraseña);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmAutorizacion", autorizacion);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject cConsultaversion (String prmVersion){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaVersion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmUsuario",Usuario);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmVersion",prmVersion);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context, null);
    }



    public DataAccessObject SOAPIncidencia(String prmArea, String prmDescripcion, byte[] imgBase64String)//,byte[] thumbNail)
    {
        String METHOD_NAME = "WM_RegistraIncidencia";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmArea",prmArea);
        request.addProperty("prmDesc",prmDescripcion);
        request.addProperty("prmFoto",imgBase64String);
      //  request.addProperty("prmThumbNail",thumbNail);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject SOAPConsultaModulosIncidencia()
    {
        String METHOD_NAME = "WM_ConsultaModulosIncidencia";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject SOAPConsultaReferencia(String datoConsulta)
    {
        String METHOD_NAME = "WM_ConsultaReferencia";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmTexto",datoConsulta);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    //Region PRUEBA VOZ SURTIDO

    public DataAccessObject SOAPConsultaPedidoDetPartida(String prmPedido, String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaSurtidoDetPartida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

       return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

//    public DataAccessObject SOAPSugiereEmpaqueSurtido(String prmPedido,String prmPartida)
//    {
//
//        int tipoObjeto = 2;
//        String METHOD_NAME = "WM_ConsultaEmpaqueSugerido";
//        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
//        request.addProperty("prmPedido",prmPedido);
//        request.addProperty("prmPartida",prmPartida);
//        request.addProperty("prmEstacion",Estacion);
//        request.addProperty("prmUsuario",Usuario);
//
//        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
//    }

    public DataAccessObject SOAPCancelaEmpaque(String prmPedido, String prmPartida, String prmEmpaque)
    {

        String METHOD_NAME = "WM_CancelaEmpaqueSugerido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject SOAPRegistraEmpaqueSurtido(String prmPedido, String prmPartida, String prmEmpaque)
    {

        int tipoObjeto = 2;
        String METHOD_NAME = "WM_RegistroEmpaqueSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject SOAPConsultaPosicion(String prmPedido, String prmPartida, String prmEmpaque)
    {


        String METHOD_NAME = "WM_RegistroEmpaqueSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject SOAPConsultaTarimaConsolidada(String prmPallet)
    {


        String METHOD_NAME = "WM_ConsultaTarimaConsolidada";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    //end region



    //CARRITO SURTIDO


    public DataAccessObject C_ListaPedidosCM(String prmDocumento)
    {
        String METHOD_NAME = "WM_ListaPedidosCM";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject C_ListaPedidoDetalleCM(String prmDocumento)
    {
        String METHOD_NAME = "wm_ListaPedidoDetalleCM";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_OrdenesPorEstacionCM()
    {
        String METHOD_NAME = "WM_OrdenesPorEstacionCM";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_DetallePedido(String prmPedido)
    {
        String METHOD_NAME = "WM_ListaPedidoDetalleCM";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_PedidosPorEstacion()
    {
        String METHOD_NAME = "WM_PedidosPorEstacion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject C_AgregaPedidoCM(String prmDocumento)
    {
        String METHOD_NAME = "WM_AgregaPedidoCM";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject C_RegContenedorOrdenProd(String prmDocumento,String prmContenedor)
    {
        String METHOD_NAME = "WM_RegContenedorOrdenProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



    public DataAccessObject C_PedidosActivos()
    {
        String METHOD_NAME = "WM_PedidosActivos";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject C_SurtirEmpaque(String prmContenedor,String prmEmpaque)
    {
        String METHOD_NAME = "WM_SurtirEmpaque";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject C_SurtirPzas(String prmContenedor,String prmEmpaque,String prmCantPza)
    {
        String METHOD_NAME = "WM_SurtirPzas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmCantPza",prmCantPza);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_RegistrarPosicionVacia(String prmPosicion)
    {
        String METHOD_NAME = "WM_RegistrarPosicionAgotada";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject cad_ListaImpresoras()
    {
        String METHOD_NAME = "WM_ListaImpresorasCM";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



    public DataAccessObject cad_ListaImpresorasMiAXC()
    {
        String METHOD_NAME = "WM_ListaImpresoras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }
    public DataAccessObject C_ImprimeEtiquetas(String prmCantEtiquetas,String prmImpresora)
    {
        String METHOD_NAME = "WM_ImpEtiquetaEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmNumEtiquetas",prmCantEtiquetas);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_ImprimeEtiquetasMP(String prmCantEtiquetas,String prmImpresora)
    {
        String METHOD_NAME = "WM_ImpEtiquetaEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmNumEtiquetas",prmCantEtiquetas);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_ImprimeEtiquetasPT(String prmLote,String prmCantEtiquetas,String prmImpresora)
    {
        String METHOD_NAME = "WM_ImpEtiquetaEmpaquePT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmNumEtiquetas",prmCantEtiquetas);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_CierraPalletSurtido(String prmPallet)
    {
        String METHOD_NAME = "WM_CierraPalletSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_IniciaSurtidoCM()
    {
        String METHOD_NAME = "WM_IniciaSurtidoCM";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



//Region Reabastecimiento

    public DataAccessObject C_ConsultaOrdenesReabastecimiento()
    {
        String METHOD_NAME = "WM_ConsultaOrdenesReabastecimiento";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject C_ConsultaOrdenesReabastecimientoDet(String prmDocumento)
    {
        String METHOD_NAME = "WM_ConsultaOrdenesReabastecimientoDet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ConsultaUbicacionPK(String prmUbicacion, String prmNumParte)
    {
        String METHOD_NAME = "WM_ConsultaUbicacionPK";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ConsultaPallet(String ConsultaPallet)
    {

        String METHOD_NAME = "WM_ConsultarPalletPT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmbalaje",ConsultaPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject c_ConsultarPalletReabastece(String prmDocumento,String ConsultaPallet)
    {

        String METHOD_NAME = "WM_ConsultarPalletReabastece";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEmbalaje",ConsultaPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }


    public DataAccessObject c_ReabastecePallet(String prmOrden,String prmPallet,String prmUbicacion)
    {

        String METHOD_NAME = "WM_ReabastecePallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden",prmOrden);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject c_ReabastecePalletSO(String prmPallet,String prmUbicacion)
    {

        String METHOD_NAME = "WM_ReabastecePalletSO";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }




// endregion

    //MIAXC

    public DataAccessObject C_ConsultaPosiciones(String prmRack,String prmLado)
    {
        String METHOD_NAME = "WM_ConsultaPosiciones";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmRack",prmRack);
        request.addProperty("prmLado",prmLado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



    public DataAccessObject C_ListaUsuarios(String prmUsuario,String prmGrupo)
    {
        String METHOD_NAME = "WM_ListaUsuarios";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmConsulta",prmUsuario);
        request.addProperty("prmGrupo",prmGrupo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }




    public DataAccessObject C_ListaTransaccion(String prmTransaccion)
    {
        String METHOD_NAME = "WM_ListaTransaccion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmTransaccion",prmTransaccion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }






    public DataAccessObject C_ConsultaTransacciones(String prmFechaDesde,String prmFechaHasta,String prmTransaccion,String prmUsuarioConsulta)
    {
        String METHOD_NAME = "WM_ConsultaTransacciones";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmUsuarioConsulta",prmUsuarioConsulta);
        request.addProperty("prmTransaccion",prmTransaccion);
        request.addProperty("prmFechaDesde",prmFechaDesde);
        request.addProperty("prmFechaHasta",prmFechaHasta);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    //REFERENCIA


    public DataAccessObject C_ConsultaReferencia(String prmReferencia)
    {
        String METHOD_NAME = "WM_ConsultaReferencia";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmReferencia",prmReferencia);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



    //KARDEX



    public DataAccessObject C_ListaProducto(String prmProducto)
    {
        String METHOD_NAME = "WM_ListaProducto";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_ListaAjustes(String prmAjuste,String prmTipo)
    {
        String METHOD_NAME = "WM_ListaAjustes";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmAjuste",prmAjuste);
        request.addProperty("prmTipo",prmTipo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_ListaPlantasMiAXC()
    {
        String METHOD_NAME = "WM_ListaPlantasMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject C_ListaAlmacenesMiAXC()
    {
        String METHOD_NAME = "WM_ListaAlmacenesMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject c_ListaAlmacenesFiltroMiAXC(String prmPlanta)
    {
        String METHOD_NAME = "WM_ListaAlmacenesFiltroMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPlanta",prmPlanta);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject C_ListaRacksAlmacen(String prmAlmacen)
    {
        String METHOD_NAME = "WM_ListaRacksAlmacenMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject C_ConsultaKardex(String prmNumParte, String prmLote, String prmTipoMovimiento, String prmTipoAjuste, String prmFechaDesde,String prmFechaHasta)
    {
        String METHOD_NAME = "WM_ConsultaKardex";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmTipoMovimiento",prmTipoMovimiento);
        request.addProperty("prmTipoAjuste",prmTipoAjuste);
        request.addProperty("prmFechaDesde",prmFechaDesde);
        request.addProperty("prmFechaHasta",prmFechaHasta);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject cad_ConsultaPalletsSinColocar(String prmFiltroProducto)
    {
        String METHOD_NAME = "WM_ConsultaPalletsSinColocarMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmFiltroProducto",prmFiltroProducto);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaPalletsPosicionMiAXC(String prmPosicion)
    {
        String METHOD_NAME = "WM_ConsultaPalletsPosicionMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }



    public DataAccessObject c_ListaEstacionesMiAXC()
    {
        String METHOD_NAME = "WM_ListaEstacionesMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmStatus","-1");
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject c_ListaOrdenesEmbarqueDetMiAXC(String prmOrden, String prmStatus, String prmFechaInicio, String prmFechaFin)
    {

        String METHOD_NAME = "WM_ListaOrdenesEmbarqueDetMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrden",prmOrden);
        request.addProperty("prmStatus",prmStatus);
        request.addProperty("prmFechaInicio",prmFechaInicio);
        request.addProperty("prmFechaFin",prmFechaFin);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }




    public DataAccessObject cad_ListaLotesEmbarquesMiAXC(String prmNumParte)
    {
        String METHOD_NAME = "WM_ListaLotesEmbarquesMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



    public DataAccessObject cad_ListaRevisionOrdenProdMiAXC(String prmNumParte)
    {
        String METHOD_NAME = "WM_ListaRevisionesEmbarquesMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }





    public DataAccessObject c_LiberaOrdenEmbarqueMiAXC(String prmValidacion, String prmOrdenEmbarque , ArrayList<Embarque> prmPartidas)
    {

        SoapObject Embarque = new SoapObject(NAMESPACE,"NewDataSet");


        for (Embarque e:prmPartidas)
            {
                Embarque.addSoapObject(
                                    new SoapObject(NAMESPACE,"EmbarqueOBJ")

                                                .addProperty(  "OrdenProd"          , e.getProperty(0))
                                                .addProperty( "Partida"             , e.getProperty(1))
                                                .addProperty( "NumParte"            , e.getProperty(2))
                                                .addProperty( "CantidadPedida"      , e.getProperty(3))
                                                .addProperty( "CantidadSurtida"     , e.getProperty(4))
                                                .addProperty( "CantidadPendiente"   , e.getProperty(5))
                                                .addProperty( "DStatus"             , e.getProperty(6))
                                                .addProperty( "UnidadMedida"        , e.getProperty(7))
                                                .addProperty( "Tag1"                , e.getProperty(8))
                                                .addProperty( "Tag2"                , e.getProperty(9))
                                                .addProperty( "Tag3"                , e.getProperty(10))
                                                .addProperty( "FechaCrea"           , e.getProperty(11))
                                                .addProperty( "Lote"                , e.getProperty(12))
                                                .addProperty( "Estacion"            , e.getProperty(13))
                                            );


                Log.i("EMBARQUE", e.getProperty(0).toString());
            }

        String METHOD_NAME = "WM_LiberaOrdenEmbarqueMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmValidacion",prmValidacion);
        request.addProperty("prmOrdenEmbarque",prmOrdenEmbarque);
        request.addProperty("prmPartidasEmbarque",Embarque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }






    public DataAccessObject c_ListaOrdenesTransferenciaDetMiAXC(String prmOrden, String prmStatus, String prmFechaInicio, String prmFechaFin)
    {

        String METHOD_NAME = "WM_ListaOrdenesTransferenciaDetMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrden",prmOrden);
        request.addProperty("prmStatus",prmStatus);
        request.addProperty("prmFechaInicio",prmFechaInicio);
        request.addProperty("prmFechaFin",prmFechaFin);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }






    public DataAccessObject c_AgregarLineaInterorgSalidaPT(String prmInterorg ,
                                                           String prmAlmacenOrigen,
                                                           String prmAlmacenDestino,
                                                           String prmArticulos,
                                                           String prmArticulo ,
                                                           String prmTonoCalibre,
                                                           String prmRevision,
                                                           String prmCantidad)
    {


        String METHOD_NAME = "WM_AgregarLineaInterorgSalidaPT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmInterorg",        prmInterorg              );
        request.addProperty("prmAlmacenOrigen",   prmAlmacenOrigen                  );
        request.addProperty("prmAlmacenDestino",  prmAlmacenDestino                   );
        request.addProperty("prmArticulos",       prmArticulos              );
        request.addProperty("prmArticulo ",       prmArticulo               );
        request.addProperty("prmTonoCalibre",     prmTonoCalibre                );
        request.addProperty("prmRevision",        prmRevision             );
        request.addProperty("prmCantidad",        prmCantidad             );

        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject c_CrearInterorgSalidaPT(String prmTransfer,
                                                    String prmReferencia)
    {
        String METHOD_NAME = "WM_CrearInterorgSalidaPT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmTransfer",   prmTransfer);
        request.addProperty("prmReferencia", prmReferencia);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject c_BajaPartidaTransferenciaMiAXC(String prmOrden, String prmPartida)
    {


        String METHOD_NAME = "WM_BajaPartidaTransferenciaMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrden",prmOrden);
        request.addProperty("prmPartida",prmPartida   );


        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }







    public DataAccessObject c_ListarOrdenesRecepcionMiAXC(String prmOrden)
    {

        String METHOD_NAME = "WM_ListarOrdenesRecepcionMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrden",prmOrden);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



    public DataAccessObject c_ActualizaOrdenCompraMiAXC(String prmOrden)
    {

        String METHOD_NAME = "WM_ActualizaOrdenCompraMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra",prmOrden);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject c_LiberaOrdenCompraMiAXC(
                                                        String prmOrdenCompra,
                                                        String prmFactura,
                                                        String prmSubtotal,
                                                        String prmTipoCambio,
                                                        String prmReferencia,
                                                        String prmMoneda,
                                                        String prmFechaRecibe,
                                                        String prmClavePedimento,
                                                        String prmNumeroAgenteAduanal,
                                                        String prmNumeroPedimento,
                                                        String prmFechaPedimento,
                                                        ArrayList<Embarque> prmPartidas,
                                                        String prmCostosCarga
                                                        )
    {

        SoapObject Partidas = new SoapObject(NAMESPACE,"NewDataSet");


        for (Embarque e:prmPartidas)
            {
                SoapObject Lotes = new SoapObject(NAMESPACE,"NewDataSet");


                for(Embarque lote:e.getLotesPartida())
                    {
                        Lotes.addSoapObject(new SoapObject(NAMESPACE,"EmbarqueOBJ")
                                .addProperty(  "OrdenProd"          , "a")
                                .addProperty(  "Partida"          , lote.getPartida())
                                .addProperty(  "NumParte"          ,lote.getNumParte())
                                .addProperty(  "Lote"               ,lote.getLote())
                                .addProperty(  "CantidadPedida"          , lote.getCantidadPedida())
                                .addProperty( "CantidadSurtida"       , "a")
                                .addProperty( "CantidadPendiente"    , "a")
                                .addProperty( "Revision"                , "a")
                                .addProperty( "DStatus"             , "a")
                                .addProperty( "Tag1"                , "a")
                                .addProperty( "Tag2"                , "a")
                                .addProperty( "Tag3"                , "a")
                                .addProperty( "FechaCrea"           , "a")
                                .addProperty( "UnidadMedida"        , "a")
                                .addProperty( "IdProveedor"         , "a")



                        );
                    }


                Partidas.addSoapObject(
                        new SoapObject(NAMESPACE,"EmbarqueOBJ")

                                .addProperty(  "OrdenProd"          , e.getProperty(0))
                                .addProperty( "Partida"             , e.getProperty(1))
                                .addProperty( "NumParte"            , e.getProperty(2))
                                .addProperty( "CantidadPedida"         , e.getProperty(3))
                                .addProperty( "CantidadSurtida"       , e.getProperty(5))
                                .addProperty( "CantidadPendiente"    , e.getProperty(4))
                                .addProperty( "Lote"                , e.getLote())
                                .addProperty( "DStatus"             , e.getProperty(6))
                                .addProperty( "Tag1"                , e.getProperty(8))
                                .addProperty( "Tag2"                , e.getProperty(9))
                                .addProperty( "Tag3"                , e.getProperty(10))
                                .addProperty( "FechaCrea"           , e.getProperty(11))
                                .addProperty( "UnidadMedida"        , e.getProperty(7))
                                .addProperty( "IdProveedor"         , e.getProperty(10))
                                .addProperty( "Lotes"               , Lotes)
                );


                Log.i("EMBARQUE", e.getProperty(0).toString());
            }

        String METHOD_NAME = "WM_LiberaOrdenRecepcionMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra"                    ,prmOrdenCompra);
        request.addProperty("prmFactura"                        ,prmFactura);
        request.addProperty("prmSubtotal"                       ,prmSubtotal);
        request.addProperty("prmTipoCambio"                     ,prmTipoCambio);
        request.addProperty("prmReferencia"                     ,prmReferencia);
        request.addProperty("prmMoneda"                         ,prmMoneda);
        request.addProperty("prmFechaRecibe"                    ,prmFechaRecibe);
        request.addProperty("prmClavePedimento"                 ,prmClavePedimento);
        request.addProperty("prmNumeroAgenteAduanal"            ,prmNumeroAgenteAduanal);
        request.addProperty("prmNumeroPedimento"                ,prmNumeroPedimento);
        request.addProperty("prmFechaPedimento"                 ,prmFechaPedimento);
        request.addProperty("prmDetalle"                        ,Partidas);
        request.addProperty("prmCostosCarga"                    ,prmCostosCarga);
        request.addProperty("prmEstacion"                       ,Estacion);
        request.addProperty("prmUsuario"                        ,Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }




    //region OC

    public DataAccessObject c_LiberaOrdenCompraMiAXC_SIN_SAP(
            String prmOrdenCompra,
            String prmFactura,
            String prmSubtotal,
            String prmTipoCambio,
            String prmReferencia,
            String prmMoneda,
            String prmFechaRecibe,
            String prmClavePedimento,
            String prmNumeroAgenteAduanal,
            String prmNumeroPedimento,
            String prmFechaPedimento,
            ArrayList<Embarque> prmPartidas,
            String prmCostosCarga,
            String prmValida,
            String prmSAP
    )
    {

        SoapObject Partidas = new SoapObject(NAMESPACE,"NewDataSet");


        for (Embarque e:prmPartidas)
            {
                SoapObject Lotes = new SoapObject(NAMESPACE,"NewDataSet");


                for(Embarque lote:e.getLotesPartida())
                    {
                        Lotes.addSoapObject(new SoapObject(NAMESPACE,"EmbarqueOBJ")
                                .addProperty(  "OrdenProd"          , "a")
                                .addProperty(  "Partida"          , lote.getPartida())
                                .addProperty(  "NumParte"          ,lote.getNumParte())
                                .addProperty(  "Lote"               ,lote.getLote())
                                .addProperty(  "CantidadPedida"          , lote.getCantidadPedida())
                                .addProperty( "CantidadSurtida"       , "a")
                                .addProperty( "CantidadPendiente"    , "a")
                                .addProperty( "Revision"                , "a")
                                .addProperty( "DStatus"             , "a")
                                .addProperty( "Tag1"                , "a")
                                .addProperty( "Tag2"                , "a")
                                .addProperty( "Tag3"                , "a")
                                .addProperty( "FechaCrea"           , "a")
                                .addProperty( "UnidadMedida"        , "a")
                                .addProperty( "IdProveedor"         , "a")



                        );
                    }


                Partidas.addSoapObject(
                        new SoapObject(NAMESPACE,"EmbarqueOBJ")

                                .addProperty(  "OrdenProd"          , e.getProperty(0))
                                .addProperty( "Partida"             , e.getProperty(1))
                                .addProperty( "NumParte"            , e.getProperty(2))
                                .addProperty( "CantidadPedida"         , e.getProperty(3))
                                .addProperty( "CantidadSurtida"       , e.getProperty(5))
                                .addProperty( "CantidadPendiente"    , e.getProperty(4))
                                .addProperty( "Lote"                , e.getLote())
                                .addProperty( "DStatus"             , e.getProperty(6))
                                .addProperty( "Tag1"                , e.getProperty(8))
                                .addProperty( "Tag2"                , e.getProperty(9))
                                .addProperty( "Tag3"                , e.getProperty(10))
                                .addProperty( "FechaCrea"           , e.getProperty(11))
                                .addProperty( "UnidadMedida"        , e.getProperty(7))
                                .addProperty( "IdProveedor"         , e.getProperty(10))
                                .addProperty( "Lotes"               , Lotes)
                );


                Log.i("EMBARQUE", e.getProperty(0).toString());
            }

        String METHOD_NAME = "WM_LiberaOrdenRecepcionMiAXC_SinSAP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra"                    ,prmOrdenCompra);
        request.addProperty("prmFactura"                        ,prmFactura);
        request.addProperty("prmSubtotal"                       ,prmSubtotal);
        request.addProperty("prmTipoCambio"                     ,prmTipoCambio);
        request.addProperty("prmReferencia"                     ,prmReferencia);
        request.addProperty("prmMoneda"                         ,prmMoneda);
        request.addProperty("prmFechaRecibe"                    ,prmFechaRecibe);
        request.addProperty("prmClavePedimento"                 ,prmClavePedimento);
        request.addProperty("prmNumeroAgenteAduanal"            ,prmNumeroAgenteAduanal);
        request.addProperty("prmNumeroPedimento"                ,prmNumeroPedimento);
        request.addProperty("prmFechaPedimento"                 ,prmFechaPedimento);
        request.addProperty("prmDetalle"                        ,Partidas);
        request.addProperty("prmCostosCarga"                    ,prmCostosCarga);
        request.addProperty("prmEstacion"                       ,Estacion);
        request.addProperty("prmUsuario"                        ,Usuario);
        request.addProperty("prmValida"                         ,prmValida);
        request.addProperty("prmSAP"                            ,prmSAP);



        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String Liga = pref.getString("ligaMP", "http://svr-wm/AXCPiasaMP/wsAXCMP.asmx");


        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject c_LiberaOrdenCompraMiAXC_CON_SAP(
            String prmOrdenCompra,
            String prmFactura,
            String prmSubtotal,
            String prmTipoCambio,
            String prmReferencia,
            String prmMoneda,
            String prmFechaRecibe,
            String prmClavePedimento,
            String prmNumeroAgenteAduanal,
            String prmNumeroPedimento,
            String prmFechaPedimento,
            ArrayList<Embarque> prmPartidas,
            String prmCostosCarga,
            String prmValida,
            String prmSAP
    )
    {

        SoapObject Partidas = new SoapObject(NAMESPACE,"NewDataSet");


        for (Embarque e:prmPartidas)
            {
                SoapObject Lotes = new SoapObject(NAMESPACE,"NewDataSet");


                for(Embarque lote:e.getLotesPartida())
                    {
                        Lotes.addSoapObject(new SoapObject(NAMESPACE,"EmbarqueOBJ")
                                .addProperty(  "OrdenProd"          , "a")
                                .addProperty(  "Partida"          , lote.getPartida())
                                .addProperty(  "NumParte"          ,lote.getNumParte())
                                .addProperty(  "Lote"               ,lote.getLote())
                                .addProperty(  "CantidadPedida"          , lote.getCantidadPedida())
                                .addProperty( "CantidadSurtida"       , "a")
                                .addProperty( "CantidadPendiente"    , "a")
                                .addProperty( "Revision"                , "a")
                                .addProperty( "DStatus"             , "a")
                                .addProperty( "Tag1"                , "a")
                                .addProperty( "Tag2"                , "a")
                                .addProperty( "Tag3"                , "a")
                                .addProperty( "FechaCrea"           , "a")
                                .addProperty( "UnidadMedida"        , "a")
                                .addProperty( "IdProveedor"         , "a")



                        );
                    }


                Partidas.addSoapObject(
                        new SoapObject(NAMESPACE,"EmbarqueOBJ")

                                .addProperty(  "OrdenProd"          , e.getProperty(0))
                                .addProperty( "Partida"             , e.getProperty(1))
                                .addProperty( "NumParte"            , e.getProperty(2))
                                .addProperty( "CantidadPedida"         , e.getProperty(3))
                                .addProperty( "CantidadSurtida"       , e.getProperty(5))
                                .addProperty( "CantidadPendiente"    , e.getProperty(4))
                                .addProperty( "Lote"                , e.getLote())
                                .addProperty( "DStatus"             , e.getProperty(6))
                                .addProperty( "Tag1"                , e.getProperty(8))
                                .addProperty( "Tag2"                , e.getProperty(9))
                                .addProperty( "Tag3"                , e.getProperty(10))
                                .addProperty( "FechaCrea"           , e.getProperty(11))
                                .addProperty( "UnidadMedida"        , e.getProperty(7))
                                .addProperty( "IdProveedor"         , e.getProperty(10))
                                .addProperty( "Lotes"               , Lotes)
                );


                Log.i("EMBARQUE", e.getProperty(0).toString());
            }

        String METHOD_NAME = "WM_LiberaOrdenRecepcionMiAXC_ConSAP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenCompra"                    ,prmOrdenCompra);
        request.addProperty("prmFactura"                        ,prmFactura);
        request.addProperty("prmSubtotal"                       ,prmSubtotal);
        request.addProperty("prmTipoCambio"                     ,prmTipoCambio);
        request.addProperty("prmReferencia"                     ,prmReferencia);
        request.addProperty("prmMoneda"                         ,prmMoneda);
        request.addProperty("prmFechaRecibe"                    ,prmFechaRecibe);
        request.addProperty("prmClavePedimento"                 ,prmClavePedimento);
        request.addProperty("prmNumeroAgenteAduanal"            ,prmNumeroAgenteAduanal);
        request.addProperty("prmNumeroPedimento"                ,prmNumeroPedimento);
        request.addProperty("prmFechaPedimento"                 ,prmFechaPedimento);
        request.addProperty("prmDetalle"                        ,Partidas);
        request.addProperty("prmCostosCarga"                    ,prmCostosCarga);
        request.addProperty("prmEstacion"                       ,Estacion);
        request.addProperty("prmUsuario"                        ,Usuario);
        request.addProperty("prmValida"                         ,prmValida);
        request.addProperty("prmSAP"                            ,prmSAP);





        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String Liga = pref.getString("ligaMP", "http://svr-wm/AXCPiasaMP/wsAXCMP.asmx");

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }






//endregion




    //Region LIBERA ORDEN PRODUCCION
public DataAccessObject c_LiberaLiberaOrdenProdMiAXC(String prmOrdenProd, String prmNumParte, String prmCantidad, ArrayList<Embarque> prmDetalle)
{

    SoapObject Embarque = new SoapObject(NAMESPACE,"NewDataSet");


    for (Embarque e:prmDetalle)
        {
            Embarque.addSoapObject(
                    new SoapObject(NAMESPACE,"EmbarqueOBJ")

                            .addProperty(  "OrdenProd"          , e.getProperty(0))
                            .addProperty( "Partida"             , e.getProperty(1))
                            .addProperty( "NumParte"            , e.getProperty(2))
                            .addProperty( "CantidadPedida"      , e.getProperty(3))
                            .addProperty( "CantidadSurtida"     , e.getProperty(4))
                            .addProperty( "CantidadPendiente"   , e.getProperty(5))
                            .addProperty( "DStatus"             , e.getProperty(6))
                            .addProperty( "UnidadMedida"        , e.getProperty(7))
                            .addProperty( "Tag1"                , e.getProperty(8))
                            .addProperty( "Tag2"                , e.getProperty(9))
                            .addProperty( "Tag3"                , e.getProperty(10))
                            .addProperty( "FechaCrea"           , e.getProperty(11))
                            .addProperty( "Lote"                , e.getProperty(12))
                            .addProperty( "Estacion"            , e.getProperty(13))
            );


            Log.i("EMBARQUE", e.getProperty(0).toString());
        }

    String METHOD_NAME = "WM_LiberaLiberaOrdenProdMiAXC";
    SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

    request.addProperty("prmOrdenProd",prmOrdenProd);
    request.addProperty("prmNumPartePT",prmNumParte);
    request.addProperty("prmCantidad",prmCantidad);
    request.addProperty("prmDetalle",Embarque);
    request.addProperty("prmEstacion",Estacion);
    request.addProperty("prmUsuario",Usuario);

    return IniciaAccionSOAP(request,METHOD_NAME,context,null);

}



    public DataAccessObject c_ListaOrdenesProduccion()
    {

        String METHOD_NAME = "WM_ListaOrdenesProduccion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);


//        request.addProperty("prmEstacion",Estacion);
//        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }
        public DataAccessObject c_ListaOrdenesProdDetMiAXC(String prmOrdenProd)
    {

        String METHOD_NAME = "WM_ListaOrdenesProdDetMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmOrdenProd",prmOrdenProd);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject c_ConsultaEmpaque(String prmCodigoEmpaque)
    {

        String METHOD_NAME = "WM_ConsultaEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoEmpaque", prmCodigoEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject c_ConsultaEmpaqueReabastece(String prmDocumento,String prmCodigoEmpaque)
    {

        String METHOD_NAME = "WM_ConsultaEmpaqueReabastece";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmCodigoEmpaque", prmCodigoEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ReabasteceEmpaque(String prmOrden,String prmEmpaque,String prmUbicacion)
    {

        String METHOD_NAME = "WM_ReabasteceEmpaque";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden",prmOrden);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject c_ReabasteceEmpaqueSO(String prmEmpaque,String prmUbicacion)
    {

        String METHOD_NAME = "WM_ReabasteceEmpaqueSO";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject c_ReabastecePiezas(String prmOrden,String prmEmpaque,String prmCantidad,String prmUbicacion,String prmContenedor)
    {

        String METHOD_NAME = "WM_ReabastecePiezas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden",prmOrden);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }
    public DataAccessObject c_ReabastecePiezasSO(String prmEmpaque,String prmCantidad,String prmUbicacion,String prmContenedor)
    {

        String METHOD_NAME = "WM_ReabastecePiezasSO";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cRegistraReempaqueCons (String prmDocumento, String prmPallet,String prmPalletAConsolidar, String prmProducto, String prmCantidadEmpaques) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistraReempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmPalletAConsolidar", prmPalletAConsolidar);
        request.addProperty("prmProducto", prmProducto);
        request.addProperty("prmCantidadEmpaques", prmCantidadEmpaques);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cRegistraReempaqueConsSKU (String prmDocumento, String prmPallet,String prmPalletAConsolidar, String prmProducto, String prmCantidadEmpaques) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistraReempaqueSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmPalletAConsolidar", prmPalletAConsolidar);
        request.addProperty("prmProducto", prmProducto);
        request.addProperty("prmSKU", prmCantidadEmpaques);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cRegistraReempaqueConsPiezas (String prmDocumento, String prmPallet,String prmPalletAConsolidar, String prmProducto, String prmCantidadEmpaques) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistraReempaquePiezas";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmPalletAConsolidar", prmPalletAConsolidar);
        request.addProperty("prmProducto", prmProducto);
        request.addProperty("prmCantidadEmpaques", prmCantidadEmpaques);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cCancelaReempaque (String prmEmbarque, String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CancelaReempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbarque", prmEmbarque);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cCerrarReempaque (String prmEmbarque, String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CerrarReempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbarque", prmEmbarque);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }





    public DataAccessObject cConsultaOrdenesParaValidar(String prmOrden) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarPalletsOrdenes";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrden", prmOrden);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cConsultaOrdenesParaValidarSpinner(String prmOrden) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarPalletsOrdenes";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrden", prmOrden);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cListaEmpaquesXOrdenRecepcion(String prmOrden, String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListaEmpaquesXOrdenRecepcion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrden", prmOrden);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
}

