package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

public class adInventarios extends conexionWS2
{
    private String NAMESPACE = "http://tempuri.org/";
    private String Usuario, Estacion;
    private Context context;

    public adInventarios(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "null");
        Estacion = pref.getString("estacion", "null");
        this.context = context;
    }

    public DataAccessObject c_ListaImpresoras()
    {
        String METHOD_NAME = "WM_ListaImpresoras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaPalletAbiertoInv(String prmInventario)
    {
        String METHOD_NAME = "WM_ConsultarPalletAbiertoInv";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmInventario", prmInventario);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject c_ConsultaPalletAbiertoInv_NE(String prmInventario)
    {
        String METHOD_NAME = "WM_ConsultarPalletAbiertoInv_NE";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmInventario", prmInventario);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject c_ListaMercados()
    {
        String METHOD_NAME = "WM_ListaMercados";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaAlmacen(String prmPlanta,String prmAlmacen)
    {
        String METHOD_NAME = "WM_ConsultaAlmacen";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPlanta",prmPlanta);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaInvCiclicoPosicion(String prmIdAlmacen)
    {
        String METHOD_NAME = "WM_ConsultaInvCiclicoPosicion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdAlmacen",prmIdAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaInvCiclicoNumParte(String prmIdAlmacen)
    {
        String METHOD_NAME = "WM_ConsultaInvCiclicoNumParte";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdAlmacen",prmIdAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaInvFisico(String prmIdAlmacen)
    {
        String METHOD_NAME = "WM_ConsultaInvFisico";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdAlmacen",prmIdAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaInvCiclicoPorLote(String prmIdAlmacen)
    {
        String METHOD_NAME = "WM_ConsultaInvCiclicoPorLote";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdAlmacen",prmIdAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaInventarioNumParte(String pIdInventario)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_ConsultaInventarioNumParte";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("pIdInventario",pIdInventario);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_ConsultaInventariosNumParteAbiertos()
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_ConsultaInventariosNumParteAbiertos";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_INVConsultaUbicacion(String prmIdInventario,String prmUbicacion)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_INVConsultaUbicacion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_BajaPalletInventario(String prmIdInventario,String prmPallet)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_BajaPalletInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaPalletInventario(String prmIdInventario,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPalletInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistraPalletInvSinCambio(String prmIdInventario,String prmPallet,String prmUbicacion)
    {
        String METHOD_NAME = "WM_RegistraPalletInvSinCambio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_EditaRegistroPalletInventario(String prmIdInventario,String prmPallet,String prmUbicacion,
                                                            String prmLote,String prmCantidadActual,String prmEmpaques)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_EditaRegistroPalletInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmCantidadActual",prmCantidadActual);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaCoincidenciaArticulo(String prmArticulo)
    {
        String METHOD_NAME = "WM_ConsultaCoincidenciaArticulo";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmArticulo",prmArticulo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistraEmpaqueNuevoPalletInventario(String prmInventario,String prmEmpaque,
                                                                   String prmNumParte,String prmCantidad,
                                                                   String prmPosicion, String prmLote, String prmPedimento,String prmClavePedimento,String prmFactura,
                                                                   String prmFechaPedimento, String prmFechaRecepcion)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueNuevoPalletInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmInventario",prmInventario);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmPedimento",prmPedimento);
        request.addProperty("prmClavePedimento",prmClavePedimento);
        request.addProperty("prmFactura",prmFactura);
        request.addProperty("prmFechaPedimento",prmFechaPedimento);
        request.addProperty("prmFechaRecepcion",prmFechaRecepcion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistraEmpaqueNuevoPalletInventario_NE(String prmInventario,String prmEmpaques,
                                                                      String prmNumParte,String prmCantidad,
                                                                      String prmPosicion, String prmLote,
                                                                      String prmPedimento,
                                                                      String prmClavePedimento,String prmFactura,
                                                                      String prmFechaPedimento, String prmFechaRecepcion)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueNuevoPalletInventario_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmInventario",prmInventario);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmPedimento",prmPedimento);
        request.addProperty("prmClavePedimento",prmClavePedimento);
        request.addProperty("prmFactura",prmFactura);
        request.addProperty("prmFechaPedimento",prmFechaPedimento);
        request.addProperty("prmFechaRecepcion",prmFechaRecepcion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_CierraPalletInventario(String prmIdInventario,String prmPallet,String prmImpresora)
    {
        String METHOD_NAME = "WM_CierraPalletInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaEmpaquesPorPalletInventario(String prmIdInventario,String prmPallet)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesPorPalletInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaEmpaquesPosicionPickingInv(String prmIdInventario,String prmPosicion)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesPosicionPickingInv";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPosicion",prmPosicion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaEmpaqueInventario(String prmIdInventario,String prmEmpaque)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaqueInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistraNuevoEmpaquePalletInventario(String prmIdInventario,String prmPallet, String prmEmpaque, String prmCantidad,
                                                                   String prmUbicacion, String prmPedimento, String prmClavePedimento, String prmFechaPedimento, String FechaRecepcion)
    {
        String METHOD_NAME = "WM_RegistraNuevoEmpaquePalletInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmPedimento",prmPedimento);
        request.addProperty("prmClavePedimento",prmClavePedimento);
        request.addProperty("prmFechaPedimento",prmFechaPedimento);
        request.addProperty("FechaRecepcion",FechaRecepcion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistraEmpaqueInventario(String prmIdInventario,String prmEmpaque, String prmPallet, String prmLoteNuevo,
                                                        String prmUbicacion)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmLoteNuevo",prmLoteNuevo);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistraEmpaqueInventarioPK(String prmIdInventario,String prmEmpaque, String prmPallet, String prmLoteNuevo,
                                                        String prmUbicacion)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueInventarioPK";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmLoteNuevo",prmLoteNuevo);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_EditaRegistroEmpaqueInventario(String prmIdInventario, String prmPallet,String prmEmpaque,
                                                             String prmUbicacion, String prmCantidad)

    {
        String METHOD_NAME = "WM_EditaRegistroEmpaqueInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_EditaRegistroEmpaqueInventarioPK(String prmIdInventario, String prmPallet,String prmEmpaque,
                                                             String prmUbicacion, String prmCantidad)

    {
        String METHOD_NAME = "WM_EditaRegistroEmpaqueInventarioPK";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_BajaEmpaqueInventario(String prmIdInventario,String prmEmpaque)
    {
        String METHOD_NAME = "WM_BajaEmpaqueInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaEmpaquesPorPalletInventario_NE(String prmIdInventario,String prmPallet)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesPorPalletInventario_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_RegistraEmpaqueInventario_NE(String prmIdInventario,String prmPallet,String prmCantidadEmpaque,
                                                           String prmCantidadEmpaques, String prmProducto,String prmLote,
                                                           String prmUbicacion)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueInventario_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmCantidadEmpaque",prmCantidadEmpaque);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_NuevoRegistroEmpaqueInventario_NE(String prmIdInventario,
                                                                String prmPallet,
                                                                String prmProducto,
                                                                String prmUbicacion,
                                                                String prmCantXEmp,
                                                                String prmEmpaques,
                                                                String prmLote)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueInventario_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmCantXEmp",prmCantXEmp);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_BajaEmpaquesInventario_NE(String prmIdInventario,String prmCodigoPallet,String prmProducto,
                                                        String prmLote, String prmCantidadEmpaques)
    {
        String METHOD_NAME = "WM_BajaEmpaquesInventario_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultaInventarioPosicion(String pIdInventario)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_ConsultaInventarioPosicion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("pIdInventario",pIdInventario);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaEmpaquesPisoInv(String prmIdInventario,String prmUbicacion)
    {
        conexionWS c= new conexionWS();
        String METHOD_NAME = "WM_EmpaquesPorPisoInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_RegistraEmpaquePisoInv(String prmIdInventario,String prmPallet,String prmCantidadEmpaque,
                                                     String prmCantidadEmpaques, String prmProducto,String prmLote,
                                                     String prmUbicacion)
    {
        String METHOD_NAME = "WM_RegistraEmpaquePisoInv";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmCantidadEmpaque",prmCantidadEmpaque);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_BajaEmpaquesPisoInv(String prmIdInventario,String prmCodigoPallet,String prmProducto,
                                                  String prmLote, String prmCantidadEmpaques)
    {
        String METHOD_NAME = "WM_BajaEmpaquesPisoInv";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_NuevoRegistroEmpaquePisoInv(String prmIdInventario,
                                                          String prmProducto,
                                                          String prmUbicacion,
                                                          String prmCantXEmp,
                                                          String prmEmpaques,
                                                          String prmLote)
    {
        String METHOD_NAME = "WM_NuevoRegistroEmpaquePisoInv";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmCantXEmp",prmCantXEmp);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmLote",prmLote);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cEscanearEmpaquesInv( String prmInventario, String prmProducto,String prmPiso){

        String METHOD_NAME = "WM_EscanearEmpaqueInv";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmInventario",prmInventario);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmPiso",prmPiso);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cBajaEmpaqueInv( String prmInventario,String prmPiso, String prmProducto){

        String METHOD_NAME = "WM_BajaEmpaqueInv";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmInventario",prmInventario);
        request.addProperty("prmPiso",prmPiso);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cEditarEmpaqueInv( String prmInventario,String prmContenedor,String prmSKU, String prmProducto,String prmPiso){

        String METHOD_NAME = "WM_EditarEmpaqueInv";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmInventario",prmInventario);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmSKU",prmSKU);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmPiso",prmPiso);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_RegistraNuevoEmpaquePalletInventarioPicking(String prmIdInventario,String prmPallet, String prmEmpaque, String prmCantidad,
                                                                             String prmUbicacion, String prmPedimento, String prmClavePedimento, String prmFactura,
                                                                                String prmFechaPedimento, String prmFechaRecepcion, String prmNumParte)
    {
        String METHOD_NAME = "WM_RegistraNuevoEmpaquePalletInventarioPicking";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmPedimento",prmPedimento);
        request.addProperty("prmClavePedimento",prmClavePedimento);
        request.addProperty("prmFactura",prmFactura);
        request.addProperty("prmFechaPedimento",prmFechaPedimento);
        request.addProperty("prmFechaRecepcion",prmFechaRecepcion);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_EditaRegistroContenedorInventario(String prmIdInventario, String prmPallet,String prmEmpaque,String prmUbicacion,
                                                                String prmCantidad,String prmNumParte, String prmPedimento)

    {
        String METHOD_NAME = "WM_EditaRegistroContenedorInventario";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdInventario",prmIdInventario);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmUbicacion",prmUbicacion);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmPedimento",prmPedimento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultarPedimentos(String prmContenedor)

    {
        String METHOD_NAME = "WM_ListarPedimentos";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_DetallePedimento(String prmContenedor, String prmPedimento)

    {
        String METHOD_NAME = "WM_DetallePedimentos";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmContenedor",prmContenedor);
        request.addProperty("prmPedimento",prmPedimento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
}