package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import org.ksoap2.serialization.SoapObject;

public class cAccesoADatos_Embarques extends conexionWS2{

        private String  NAMESPACE ="http://tempuri.org/";
        private String  Usuario,Estacion;
        private Context contexto;

        public cAccesoADatos_Embarques(Context context)
        {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            Usuario = pref.getString("usuario", "null");
            Estacion = pref.getString("estacion", "null");
            this.contexto = context;
        }

    public DataAccessObject cad_ConsultaPedidoSurtido(String prmPedido)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPedidoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaPedidoSurtidoAgrupado(String prmPedido)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPedidoSurtidoAgrupado";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject c_ConsultaOrdenSurtido(String prmOrdenEmbarque, String prmStatus) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaOrdenSurtido";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenEmbarque", prmOrdenEmbarque);
        request.addProperty("prmStatus", prmStatus);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }


    public DataAccessObject cad_ConsultaPedidoSurtidoReempaque(String prmPedido)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPedidoSurtidoReempaque";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaCarritoSurtido(String prmPedido,String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaCarritoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaCarrito(String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaCarritoValidar";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ValidarCarrito(String prmCarrito, String prmArea)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ValidarCarritoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmArea", prmArea);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RechazarCarrito(String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RechazarCarritoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaCarritoEmpaques(String prmDocumento,String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesCarrito";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaSurtidoDetPartida(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaSurtidoDetPartida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_ListarPartidasSpinner(String prmPedido)
    {
        String METHOD_NAME = "WM_ConsultaPartidasSpinner";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaEmpaqueSugerido(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSugerido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaEmpaqueSugerido_NE(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSugerido_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaContenedorSugerido(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaContenedorSugerido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaPalletSugerido(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaPalletSugerido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaEmpaqueSurtido(String prmPedido,String prmPartida,String prmEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_ConsultaPalletSurtido_NE(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPalletSurtido_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_ConsultaContenedorSurtido(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaContenedorSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaValidacionPallet(String prmPedido,String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaValidacionPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaPalletSurtido(String prmPedido,String prmPartida,String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RegistroPalletSurtido(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistroPalletSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RegistroPalletSurtidoMultPart(String prmPedido,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistroPalletSurtidoMultPart";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RegistroEmpaqueSurtido(String prmPedido,String prmPartida,String prmEmpaque,String prmArmadoPallet)
    {
        String METHOD_NAME = "WM_RegistroEmpaqueSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmArmadoPallet",prmArmadoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_RegistroEmpaqueSurtido_NE(String prmPedido,String prmPartida,String prmPallet,String prmCantidadEmpaques,String prmArmadoPallet)
    {
        String METHOD_NAME = "WM_RegistroEmpaqueSurtido_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmArmadoPallet",prmArmadoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_OSSurtirPiezas(String prmPedido,String prmPartida,String prmContenedor,String prmCantidadPiezas,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSSurtirPiezas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmCantidadPiezas",prmCantidadPiezas);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_OSSurtirPiezasSKU(String prmPedido,String prmPartida,String prmContenedor,String prmSKU,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSSurtirPiezasSKU";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmSKU",prmSKU);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_OSCancelarSurtPiezas(String prmPedido,String prmPartida,String prmContenedor,String prmPiezas,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSCancelarSurtPiezas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmPiezas",prmPiezas);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaTarimaConsolidada(String prmPedido)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTarimaConsolidada";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_ConsultaTarimaConsolidada(String prmPedido,String prmPalletCons)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTarimaConsolidada";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPalletCons",prmPalletCons);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_ConsultaTarimaConsolidada_NE(String prmPedido,String prmPalletCons)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTarimaConsolidada_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPalletCons",prmPalletCons);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_CierraPalletSurtido(String prmPallet)
    {
        String METHOD_NAME = "WM_CierraPalletSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_ConsultarPalletPT(String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarPalletPT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmbalaje",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ConsultaEmpaque(String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ConsultaEmbarqueValidarTotales(String prmOrdenEmbarque)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmbarqueValidarTotales";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenEmbarque",prmOrdenEmbarque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ConsultaEmbarqueValidarEmpaques(String prmOrdenEmbarque,String prmProducto)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmbarqueValidarEmpaques";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenEmbarque",prmOrdenEmbarque);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }
    public DataAccessObject c_ConsultaEmbarqueValidarPallets(String prmOrdenEmbarque)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmbarqueValidarPallets";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenEmbarque",prmOrdenEmbarque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }
    public DataAccessObject c_ValidaEmbEmpaque(String prmDocumento,String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_ValidaEmbEmpaque";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmGuia", "");
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ValidaEmbSKUPzas(String prmDocumento,String prmNumParte, String prmCantidad)
    {
        String METHOD_NAME = "WM_ValidaEmbSkuCantidad";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCodigoEmpaque",prmNumParte);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmGuia", "");
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ValidaEmbSKUCantidad(String prmDocumento,String prmNumParte, String prmCantidad)
    {
        String METHOD_NAME = "WM_ValidaEmbSkuCantidad";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCodigoEmpaque",prmNumParte);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmGuia", "");
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }



    public DataAccessObject c_ValidaEmbPallet(String prmOS,String prmPallet,String prmGuia,String prmValida)
    {
        String METHOD_NAME = "WM_ValidaEmbPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOS",prmOS);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmGuia",prmGuia);
        request.addProperty("prmValida",prmValida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }
    public DataAccessObject c_OSRegistrarPalletLineaRechazado(String prmOS,String prmPallet,String prmLinea,String prmRazonRechazo)
    {
        String METHOD_NAME = "WM_OSRegistrarPalletLineaRechazado";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOS",prmOS);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmLinea",prmLinea);
        request.addProperty("prmRazonRechazo",prmRazonRechazo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }



    public DataAccessObject cad_ConsultaPisoSugerido(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaPisoSugerido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaPisoSurtido(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPisoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RegistroPisoSurtido(String prmPedido,String prmPartida,String prmPallet,String prmCantidadEmpaques,String prmArmadoPallet)
    {
        String METHOD_NAME = "WM_RegistroPisoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmArmadoPallet",prmArmadoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_RegistrarEmbMaterial(String prmDocumento,String prmLinea,String prmValida)
    {
        String METHOD_NAME = "WM_RegistrarEmbMaterial";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmLinea",prmLinea);
        request.addProperty("prmValida",prmValida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_PalletsArmadoPedido(String prmPedido)
    {
        String METHOD_NAME = "WM_PalletsArmadoPedido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cConsultaListaConsolidadosReempaque(String prmDocumento) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaListaConsolidadosReempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject cCreaPalletReempaque (String prmEmbarque) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CreaPalletReempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbarque", prmEmbarque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject cConsultaPalletCons (String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletCons";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
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
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
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
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }
    public DataAccessObject cCerrarReempaque (String prmEmbarque, String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CerrarReempaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbarque", prmEmbarque);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject cConsultaEmpaqueoSKU (String prmEmpaqueOSKU) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaqueOSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaqueOSKU", prmEmpaqueOSKU);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject cCancelaEmbarqueValidado (String prmOrden, String prmMotivo) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CancelaEmbarqueValidado";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrden", prmOrden);
        request.addProperty("prmMotivo", prmMotivo);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }
    public DataAccessObject cListaEmbarqueValidadasDet (String prmOrdenEmbarquea) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListaEmbarqueValidadasDet";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenEmbarquea", prmOrdenEmbarquea);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject c_ConsultaCarritoValida(String prmCarrito,String prmOrdenEmbarque)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaCarritoValida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEmbarque",prmOrdenEmbarque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ConsultaTipoRegValida(String prmEmpaque,String prmDocumento, String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTipoRegValida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ConsultaEmpaquesOP(String prmOrdenEmbarque)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesOP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd",prmOrdenEmbarque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_RechazarMaterialProd(String prmOrdenProd, String prmNumParte, String prmCantidad, String prmPartida, String prmNuevoEmpaque, String prmBandera)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RechazharMaterialOP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd",prmOrdenProd);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmNuevoEmpaque",prmNuevoEmpaque);
        request.addProperty("prmBandera", prmBandera);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }
    public DataAccessObject c_ConsultaMaquinas(String prmMaquina)
    {
        String METHOD_NAME = "WM_ConsultaMaquinas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmMaquina",prmMaquina);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_RechazaSurtidoProduccion(String prmOrdenProd,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistrarRechazoSurtidoOP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenProd",prmOrdenProd);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }
}
