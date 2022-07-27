package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

public class cAccesoADatos_RegistroPT extends conexionWS2{

        private String  NAMESPACE ="http://tempuri.org/";
        private String  Usuario,Estacion;
        private Context context;

        public cAccesoADatos_RegistroPT(Context context)
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            Usuario = pref.getString("usuario", "null");
            Estacion = pref.getString("estacion", "null");
            this.context = context;
        }


    //region Registro Pallets PT

    public DataAccessObject c_ConsultaOrdenProduccion(String prmDocumento)
    {

        String METHOD_NAME = "WM_ConsultaOrdenProduccion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_CerrarOrdenProduccion(String prmDocumento)
    {

        String METHOD_NAME = "WM_CerrarOrdenProduccion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_CerrarOrdenProduccion_v2(String prmDocumento,String prmFechaCaducidad,String prmTipo)
    {

        String METHOD_NAME = "WM_CerrarOrdenProduccion_v2";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmDocumento);
        request.addProperty("prmFechaCad", prmFechaCaducidad);
        // request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_RegistraFechaCadOrdenProd(String prmOrdenProduccion,String prmFechaCad)
    {

        String METHOD_NAME = "WM_RegistraFechaCadOrdenProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenProduccion);
        request.addProperty("prmFechaCad", prmFechaCad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }


    //endregion

    //region Armado de pallets

    public DataAccessObject c_ConsultaEmpaquesArmadoProd(String prmOrdenProd)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesArmadoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd", prmOrdenProd);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_ConsultaEmpaquesArmadoProdUnico(String prmOrdenProd)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesArmadoProdUnico";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd", prmOrdenProd);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_ConsultaEmpaquesArmadoProd_NE(String prmOrdenProd)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesArmadoProd_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd", prmOrdenProd);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }
    public DataAccessObject c_RegistraEmpaqueEnPallet(String prmEmpaque,String prmOrdenPoduccion,String prmTipo,String prmCantidad, String prmNumSerie)
    {

        String METHOD_NAME = "WM_RegistraEmpaqueEnPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden", prmOrdenPoduccion);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        request.addProperty("prmNumSerie", prmNumSerie);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_RegistraEmpaqueEnPalletUnico(String prmEmpaque,String prmOrdenPoduccion,String prmTipo,String prmCantidad, String prmNumSerie)
    {

        String METHOD_NAME = "WM_RegistraEmpaqueEnPalletUnico";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden", prmOrdenPoduccion);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        request.addProperty("prmNumSerie", prmNumSerie);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_RegistraEmpaqueEnPalletPrimeraYUltima(String prmPrimerEmpaque,String prmUltEmpaque,String prmOrdenPoduccion,String prmTipo,String prmCantidad)
    {

        String METHOD_NAME = "WM_RegistraEmpaqueEnPalletPrimeraYUltima";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden", prmOrdenPoduccion);
        request.addProperty("prmPrimerEmpaque", prmPrimerEmpaque);
        request.addProperty("prmUltimoEmpaque", prmUltEmpaque);
        //  request.addProperty("prmCantidadTarimas", prmCantidadTarimas);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmTipo", prmTipo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }

    public DataAccessObject c_CerrarRegistroPallet(String prmOrdenPoduccion,String prmFechaCaducidad)
    {

        String METHOD_NAME = "WM_CerrarRegistroPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenPoduccion);
        request.addProperty("prmFechaCaducidad", prmFechaCaducidad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }
    public DataAccessObject c_CancelarRegistroPallet(String prmOrdenPoduccion)
    {
        String METHOD_NAME = "WM_CancelarRegistroPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProduccion", prmOrdenPoduccion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }
    public DataAccessObject c_BajaEmpaqueArmadoTarimas(String prmOrdenPoduccion,String prmEmpaque)
    {

        String METHOD_NAME = "WM_BajaEmpaqueArmadoTarimas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden", prmOrdenPoduccion);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);

    }
   public DataAccessObject c_CreaEmpaqueNE(String prmOrdenProd, String prmCantidad, String prmTipo, String prmNumSerie)
    {
        String METHOD_NAME ="WM_CreaEmpaqueNE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd",prmOrdenProd);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        request.addProperty("prmTipo",prmTipo);
        request.addProperty("prmNumSerie",prmNumSerie);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);


    }

    public DataAccessObject cCreaPalletReempaqueProd () {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CreaPalletReempaqueProd";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cRegistraReempaqueConsProdSKU (String prmPallet,String prmPalletAConsolidar, String prmProducto, String prmCantidadEmpaques) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistraReempaqueProdSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmPalletAConsolidar", prmPalletAConsolidar);
        request.addProperty("prmProducto", prmProducto);
        request.addProperty("prmSKU", prmCantidadEmpaques);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cConsultaPalletConsProd (String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletCons";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    // Surtido

    public DataAccessObject cad_ListarPartidasProdSpinner(String prmPedido)
    {
        String METHOD_NAME = "WM_ConsultaPartidasProdSpinner";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaSurtidoProdDetPartida(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaSurtidoProdDetPartida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaEmpaqueProdSugerido(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSugeridoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaEmpaqueSurtidoProd(String prmPedido,String prmPartida,String prmEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistroEmpaqueSurtidoProd(String prmPedido,String prmPartida,String prmEmpaque,String prmArmadoPallet)
    {
        String METHOD_NAME = "WM_RegistroEmpaqueSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmArmadoPallet",prmArmadoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaTarimaConsolidadaProd(String prmPedido,String prmPalletCons)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTarimaConsolidadaProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPalletCons",prmPalletCons);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_CierraPalletSurtidoProd(String prmPallet)
    {
        String METHOD_NAME = "WM_CierraPalletSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaEmpaqueSugeridoProd_NE(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueProdSugerido_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaPalletSurtidoProd_NE(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPalletSurtidoProd_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistroEmpaqueSurtidoProd_NE(String prmPedido,String prmPartida,String prmPallet,String prmCantidadEmpaques,String prmArmadoPallet)
    {
        String METHOD_NAME = "WM_RegistroEmpaqueSurtidoProd_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmArmadoPallet",prmArmadoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaContenedorSugeridoProd(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaContenedorSugeridoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ConsultaContenedorSurtidoProd(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaContenedorSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_OSSurtirPiezasProd(String prmPedido,String prmPartida,String prmContenedor,String prmCantidadPiezas,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSSurtirPiezasProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmCantidadPiezas",prmCantidadPiezas);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_OSSurtirPiezasSKUProd(String prmPedido,String prmPartida,String prmContenedor,String prmSKU,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSSurtirPiezasSKUProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmSKU",prmSKU);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_OSCancelarSurtPiezasProd(String prmPedido,String prmPartida,String prmContenedor,String prmPiezas,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSCancelarSurtPiezasProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmPiezas",prmPiezas);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaPalletSugeridoProd(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaPalletSugeridoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaPalletSurtidoProd(String prmPedido,String prmPartida,String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistroPalletSurtidoProd(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistroPalletSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistroPalletSurtidoProdMultPart(String prmPedido,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistroPalletSurtidoProdMultPart";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaPedidoSurtidoProd(String prmPedido)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPedidoSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }


    public DataAccessObject cad_ConsultaCarritoSurtidoProd(String prmPedido,String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaCarritoSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ConsultaOrdenSurtidoProd(String prmOrdenProd, String prmStatus) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaOrdenSurtidoProd";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenProd", prmOrdenProd);
        request.addProperty("prmStatus", prmStatus);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }



    //endregion




}
