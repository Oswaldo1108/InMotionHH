package com.automatica.axc_lib.AccesoDatos.MetodosConexion;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;
import org.ksoap2.serialization.SoapObject;
public class cAccesoADatos_Recepcion  extends conexionWS2{
    private String  NAMESPACE ="http://tempuri.org/";
    private String  Usuario,Estacion,Area;
    private Context context;

    public cAccesoADatos_Recepcion(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "null");
        Estacion = pref.getString("estacion", "null");
        Area = pref.getString("area", null);
        this.context = context;
    }

    public DataAccessObject c_ListarLotesOC(String prmOrdenCompra, String prmPartida, String prmNumParte)
    {
        String METHOD_NAME = "WM_ListarLotesOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ListaMercados() {
        String METHOD_NAME = "WM_ListaMercados";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject c_detalleReciboPartida(String prmOrdenCompra, String prmPartida){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_DetalleReciboPartida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return  c.IniciaAccionSOAP(request, METHOD_NAME,context,null);
    }

    public DataAccessObject c_ListarValidacionCalidad()
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaValidarCalidad";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ListaPalletSinColocar()
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPalletSinColocar";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ListarValidacionIngreso()
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaValidarIngreso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ListarEmpaquesCalidad(String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesCalidad";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaInfoPallet(String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaInfoPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ValidarPalletEntrada(String prmCodigoPallet)
    {
        String METHOD_NAME = "WM_ValidarPalletEntrada";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultarPegadoEtiqueta(String prmCodigoPallet, String prmPrimerEmpaque, String prmSegundoEmpaque)
    {
        String METHOD_NAME = "WM_ConsultarPegadoEtiqueta";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmPrimerEmpaque",prmPrimerEmpaque);
        request.addProperty("prmSegundoEmpaque",prmSegundoEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_ValidarPalletCalidad(String prmCodigoPallet)
    {
        String METHOD_NAME = "WM_ValidarPalletCalidad";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RechazarPalletCalidad(String prmCodigoPallet)
    {
        String METHOD_NAME = "WM_RechazarPalletCalidad";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RechazarPalletIngreso(String prmCodigoPallet)
    {
        String METHOD_NAME = "WM_RechazarPalletAlmacen";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_EnviarNotificacion(String prmDocumento)
    {
        String METHOD_NAME = "WM_CreaNotificacion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmArea", "Almacén" );
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ListarEmpaquesIngreso(String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesIngreso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_RegistrarPalletCompra_NE(String prmOrdenCompra , String  prmPartida, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques, String prmNumSerie)
    {
        String METHOD_NAME = "WM_OCRegistrarPalletCompra_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        request.addProperty("prmNumSerie",prmNumSerie);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistrarPalletCompra_NE_Mercado(String prmOrdenCompra , String  prmPartida, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques,
                                                               String prmMercado)
    {
        String METHOD_NAME = "WM_OCRegistrarPalletCompra_NE_Mercado";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistrarPalletCompra_NE_Mercado_SA(String prmOrdenCompra , String  prmPartida, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques,
                                                               String prmMercado)
    {
        String METHOD_NAME = "WM_OCRegistrarPalletCompra_NE_Mercado_SA";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistrarPalletGranel(String prmOrdenCompra , String  prmPartida, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques,String prmPosicion)
    {
        String METHOD_NAME = "WM_RegistrarPalletGranel";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cRegistroEmpaqueDaP(String prmOrdenCompra , String  prmPartida, String prmCodigoEmpaque, String prmLoteProveedor , String prmCantidad ,
                                                String prmEmpaques, String prmPosicionPiso  )
    {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueDaP";
        conexionWS c = new conexionWS();
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmPosicionPiso",prmPosicionPiso);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return  c.IniciaAccionSOAP(request, METHOD_NAME,context,null);
    }


    public DataAccessObject c_RegistrarRefaccionesSKU(String prmOrdenCompra,
                                                      String  prmPartida,
                                                      String  prmCantidad,
                                                      String  prmSKU)
    {
        String METHOD_NAME = "WM_RegistrarRefaccionesSKU";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmSKU",prmSKU);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistrarRefaccionesSKUPiezas(String prmOrdenCompra,
                                                      String  prmPartida,
                                                      String  prmCantidad,
                                                      String  prmSKU)
    {
        String METHOD_NAME = "WM_RegistrarRefaccionesSKUPiezas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmSKU",prmSKU);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_RegistrarPalletGranel_Mercado(String prmOrdenCompra , String  prmPartida, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques,String prmPosicion,
                                                            String prmMercado)
    {
        String METHOD_NAME = "WM_RegistrarPalletGranel_Mercado";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistrarPalletGranel_Mercado_SA(String prmOrdenCompra , String  prmPartida, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques,String prmPosicion,
                                                            String prmMercado)
    {
        String METHOD_NAME = "WM_RegistrarPalletGranel_Mercado_SA";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistrarEmpaqueCompra(String prmOrdenCompra  ,String  prmPartida ,String  prmCodigoEmpaque, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques, String prmNumSerie)
    {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueCompra";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmNumSerie",prmNumSerie);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }



    public DataAccessObject c_RegistrarEmpaqueDaP(String prmOrdenCompra  ,String  prmPartida ,String  prmCodigoEmpaque, String  prmLoteProveedor,String  prmCantidad ,String  prmEmpaques, String prmPosicionPiso )
    {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueDaP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_OCRegistrarEmpaqueCompra_Mercado(String prmOrdenCompra  ,String  prmPartida,
                                                               String  prmCodigoEmpaque, String  prmLoteProveedor,
                                                               String  prmCantidad ,String  prmEmpaques,
                                                               String prmMercado)
    {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueCompra_Mercado";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_OCRegistrarEmpaqueCompra_Mercado_SA(String prmOrdenCompra  ,String  prmPartida,
                                                               String  prmCodigoEmpaque, String  prmLoteProveedor,
                                                               String  prmCantidad ,String  prmEmpaques,
                                                               String prmMercado)
    {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueCompra_Mercado_SA";
        conexionWS c = new conexionWS();
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_CierraPalletCompra(String prmPallet)
    {
        String METHOD_NAME = "WM_OCCierraPalletMPCompra";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_OCCierraPalletCompraRefacciones(String prmPallet)
    {
        String METHOD_NAME = "WM_OCCierraPalletCompraRefacciones";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistraMPPrimerasYUltimas(String prmOrdenCompra,String prmPartida,String prmLoteProveedor,
                                                     String prmCantidad,String prmEmpaques,String prmPrimera,
                                                     String prmUltima,String prmCantidadEmpaques, String prmNumSerie)
    {
        String METHOD_NAME = "WM_OCCreaPalletMPPrimeraUltima";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmPrimera",prmPrimera);
        request.addProperty("prmUltima",prmUltima);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        request.addProperty("prmNumSerie",prmNumSerie);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistraMPPrimerasYUltimas_Mercado(String prmOrdenCompra,String prmPartida,String prmLoteProveedor,
                                                         String prmCantidad,String prmEmpaques,String prmPrimera,
                                                         String prmUltima,String prmCantidadEmpaques,String prmMercado)
    {
        String METHOD_NAME = "WM_OCCreaPalletMPPrimeraUltima_Mercado";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmPrimera",prmPrimera);
        request.addProperty("prmUltima",prmUltima);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistraMPPrimerasYUltimas_Mercado_SA(String prmOrdenCompra,String prmPartida,String prmLoteProveedor,
                                                                 String prmCantidad,String prmEmpaques,String prmPrimera,
                                                                 String prmUltima,String prmCantidadEmpaques,String prmMercado)
    {
        String METHOD_NAME = "WM_OCCreaPalletMPPrimeraUltima_Mercado_SA";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLoteProveedor",prmLoteProveedor);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmPrimera",prmPrimera);
        request.addProperty("prmUltima",prmUltima);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaPalletAbiertoOC(String prmOrdenCompra, String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultarPalletAbiertoOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaPalletArmadoOC(String prmOrdenCompra)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletArmadoOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaPosicionPisoOC(String prmOrdenCompra, String prmPartida,String prmPosicionPiso)
    {
        String METHOD_NAME = "WM_ConsultaPosicionPisoOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPosicionPiso",prmPosicionPiso);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaPosicionPisoPalletOC(String prmOrdenCompra, String prmPartida,String prmPosicionPiso)
    {
        String METHOD_NAME = "WM_ConsultaPosicionPisoPalletOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPosicionPiso",prmPosicionPiso);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ListarPartidasOCLiberadas(String prmOrdenCompra)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasOCLiberadas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra );
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }



    public DataAccessObject c_ListarPartidasOCEnProceso(String prmOrdenCompra)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasOCEnProceso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra );
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prsmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

/*    public DataAccessObject c_ConsultarStatusConteo(String prmOrdenCompra, String prmPartida) {
        String METHOD_NAME = "WM_ConsultarPalletPT";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmOrdenCompra);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }*/

    public DataAccessObject c_ConsultarStatusConteo(String prmDocumento, String prmPartida, int prmConteo) {
        String METHOD_NAME = "WM_ConsultarStatusConteo";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmConteo", prmConteo);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListarPartidasHojaConteo(String prmOrdenCompra){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasHC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra );
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prsmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistrarConteo(String prmOrdenCompra, String prmPartida, int prmCantidad, int prmConteo){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistrarConteoPartida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmOrdenCompra );
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmConteo", prmConteo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ListarOrdenesCompraLiberadas(String prmOrdenCompra)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarOCLiberadas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra", prmOrdenCompra);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ListarOrdenesCompraContadas(String prmOrdenCompra)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarOCContadas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra", prmOrdenCompra);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ListarOrdenesProduccionLiberadas()
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarOPLiberadas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_ListarPartidasHC(String documento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("Documento",documento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ImportarPartidasConteo(String documento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ImportarPartidasConteo";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",documento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_DetallesOrdenCompra(String prmOrden, String PartidaERP, String NumParte, String Lote)
    {
        String METHOD_NAME = "WM_DetallesOrdenCompra";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrden",prmOrden);
        request.addProperty("PartidaERP",PartidaERP);
        request.addProperty("NumParte",NumParte);
        request.addProperty("Lote",Lote);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_CerrarRecepcionSinEtiqueta(String prmPartida)
    {
        String METHOD_NAME = "WM_CerrarRecepcion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_CreaLoteRecepcionOC(String prmOrdenCompra,String prmPartida,String prmLote,String prmGenerarLote)
    {
        String METHOD_NAME = "WM_CreaLoteRecepcionOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmGenerarLote",prmGenerarLote);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_CerrarRecepcion(String prmOrdenCompra)
    {
        String METHOD_NAME = "WM_CerrarRecepcion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    /**
     *
     *
     * AQUI EMPIEZAN LOS METODOS DE CIESA
     *
     *
     * **/

    public DataAccessObject cConsultaContenedor(String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaContenedor";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cOCContenedorSKU(String prmOrdenCompra, String prmPartida, String prmCodigoEmpaque ,String prmLoteProveedor,String prmSKU,String prmPosicionPiso)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_OCContenedorSKU";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra );
        request.addProperty("prmPartida",prmPartida );
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque );
        request.addProperty("prmLoteProveedor",prmLoteProveedor );
        request.addProperty("prmSKU",prmSKU );
        request.addProperty("prmPosicionPiso",prmPosicionPiso );
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cOCContenedor(String prmOrdenCompra, String prmPartida, String prmCodigoEmpaque ,String prmLoteProveedor,String prmCantidad, String prmPosicionPiso)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_OCContenedor";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenCompra",prmOrdenCompra );
        request.addProperty("prmPartida",prmPartida );
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque );
        request.addProperty("prmLoteProveedor",prmLoteProveedor );
        request.addProperty("prmCantidad",prmCantidad );
        request.addProperty("prmPosicionPiso",prmPosicionPiso );
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject cListarEmpaquesOC(String prmDoc)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CosultaEmpaquesOC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDoc",prmDoc);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cDelEmpaqueOrdenRecepcion(String prmOrdenRecepcion,String prmEmpaque,String prmRazon)
    {
        String METHOD_NAME = "WM_DelEmpaqueOrdenRecepcion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenRecepcion",prmOrdenRecepcion);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmRazon",prmRazon);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

}