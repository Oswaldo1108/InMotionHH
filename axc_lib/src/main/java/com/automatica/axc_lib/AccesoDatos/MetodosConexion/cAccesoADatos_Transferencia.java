package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

public class cAccesoADatos_Transferencia extends conexionWS2{

    private String  NAMESPACE ="http://tempuri.org/";
    private String  Usuario,Estacion;
    private Context contexto;

    public cAccesoADatos_Transferencia(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "null");
        Estacion = pref.getString("estacion", "null");
        this.contexto = context;
    }

    public DataAccessObject c_ConsultaAlmacen(String prmPlanta,String prmAlmacen)
    {
        String METHOD_NAME = "WM_ConsultaAlmacen";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPlanta",prmPlanta);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject cad_ListarTraspasos()
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarTransfersLiberados";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento","");
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_ListaOrdenesTraspaso(String prmDocumento) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarOrdenesTraspasoRecibo";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject cad_ListarPartidasRecepcionTraspaso(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasRecepcionTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject c_ListarPalletsRecepcionTraspaso(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListaPalletSurtRecTra";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }



    public DataAccessObject cad_DetallePartidaTraspaso(String prmDocumento,String prmPartida)
    {
        String METHOD_NAME = "WM_DetallePartidaTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_ListaMercados() {
        String METHOD_NAME = "WM_ListaMercados";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    //region ENVIO

    public DataAccessObject cad_ConsultaPalletSugeridoTraspasoEnvio(String prmDocumento,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaPalletSugeridoTraspasoEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaPalletSugeridoTraspasoEnvio_NE(String prmDocumento,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaPalletSugeridoTraspasoEnvio_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaEmpaqueSugeridoTraspasoEnvio(String prmDocumento,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSugeridoTraspasoEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_ConsultarPalletTraspaso_NE(String prmDocumento,String prmPartida,String prmCodigoPallet)
    {
        String METHOD_NAME = "WM_ConsultarPalletTraspaso_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }


    public DataAccessObject cad_ConsultaPalletSurtido(String prmDocumento,String prmPartida,String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RegistroTraspasoPalletEnvio(String prmDocumento,String prmPartida,String prmCodigoPallet)
    {
        String METHOD_NAME = "WM_RegistroTraspasoPalletEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return  IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RegistroPalletTraspasoMultPart(String prmDocumento,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistroPalletTraspasoMultPart";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_RegistroTraspasoEmpaqueEnvio(String prmDocumento,String prmPartida,String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_RegistroTraspasoEmpaqueEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_RegistroTraspasoEmpaqueEnvio_NE(String prmDocumento,String prmPartida,String prmCodigoPallet,String prmCantidadEmpaques)
    {
        String METHOD_NAME = "WM_RegistroTraspasoEmpaqueEnvio_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_CierrePalletTraspasoEnvio(String prmDocumento)
    {
        String METHOD_NAME = "WM_CierrePalletTraspasoEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaTarimaArmadoTraspaso(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTarimaArmadoTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ConsultaTarimaArmadoTraspaso_NE(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTarimaArmadoTraspaso_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    //picking
    public DataAccessObject c_ConsultarPisoTraspaso(String prmDocumento,String prmPartida,String prmPiso)
    {
        String METHOD_NAME = "WM_ConsultarPisoTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPiso",prmPiso);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }
    public DataAccessObject cad_ConsultaPisoSugeridoTraspaso(String prmDocumento,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaPisoSugeridoTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_RegistroTraspasoPisoEnvio(String prmDocumento,String prmPartida,String prmPiso,String prmCantidadEmpaques)
    {
        String METHOD_NAME = "WM_RegistroTraspasoPisoEnvio";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPiso",prmPiso);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return  IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ListarPartidasTrasSpinner(String prmPedido)
    {
        String METHOD_NAME = "WM_ConsultaPartidasTrasSpinner";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ListarPartidasTrasSpinnerRec(String prmPedido)
    {
        String METHOD_NAME = "WM_ConsultaPartidasTrasSpinnerRec";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_ConsultarPalletPT(String prmPedido, String prmPartida, String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarPalletRecepcion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmbalaje",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject cad_ConsultaRechazoPalletTrasp(String prmDocumento,String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaRechazoPalletTrasp";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject c_RechazoPalletTrasp(String prmDocumento,String prmPallet, String prmRazonRechazo)
    {
        String METHOD_NAME = "WM_RechazoPalletTrasp";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmRazonRechazo",prmRazonRechazo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    //endregion

    //region RECIBO


    public DataAccessObject ad_CreaEmpaqueTraspasoNE(String prmDocumento,
                                                     String prmPartida,
                                                     String prmCantidad,
                                                     String prmEmpaques)
    {

        String METHOD_NAME = "WM_CreaEmpaqueTraspasoNE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_PrimeraUltimaTraspaso(String prmDocumento,
                                                      String prmPartida,
                                                      String prmCantidad,
                                                      String prmEmpaques,
                                                      String prmPrimerEmpaque,
                                                      String prmUltimoEmpaque,
                                                      String prmCantidadEmpaques)
    {

        String METHOD_NAME = "WM_PrimeraUltimaTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmPrimera",prmPrimerEmpaque);
        request.addProperty("prmUltima",prmUltimoEmpaque);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);

    }


    public DataAccessObject cad_OCCierraPalletTraspaso(String prmPallet)
    {
        String METHOD_NAME = "WM_OCCierraPalletTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }



    public DataAccessObject cad_RegistroTraspasoPalletConEtiquetas(String prmDocumento,String prmPartida,String prmCodigoPallet)
    {
        String METHOD_NAME = "WM_RegistroTraspasoPalletConEtiquetas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject c_ListarPartidasTransEnProceso(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasTransEnProceso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento );
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prsmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }


    public DataAccessObject cad_ConsultaPalletAbiertoTraspaso(String prmDocumento, String prmPartida )
    {
        String METHOD_NAME = "WM_ConsultaPalletAbiertoTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);

    }




    public DataAccessObject cad_RegistraEmpaqueTraspaso(String prmDocumento,String prmPartida,String prmCodigoEmpaque,
                                                        String prmCantidad,String prmEmpaques, String prmNumSerie)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        request.addProperty("prmNumSerie",prmNumSerie);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_CierreParcialTraspaso(String prmDocumento)
    {
        String METHOD_NAME = "WM_CierreParcialTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject c_CierreParcialEnvioTraspaso(String prmDocumento)
    {
        String METHOD_NAME = "WM_CierreParcialEnvioTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cad_ListarTarimasRecibidasTraspaso(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarTarimasRecibidasTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }



    public DataAccessObject c_CancelarPalletTraspaso(String prmDocumento,String prmCodigoPallet,String prmRazon)
    {
        String METHOD_NAME = "WM_CancelarPalletTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmRazon",prmRazon);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject ad_CreaEmpaqueTraspasoNESelAlm(String prmDocumento,
                                                           String prmPartida,
                                                           String prmCantidad,
                                                           String prmEmpaques,
                                                           String prmAlmacen)
    {

        String METHOD_NAME = "WM_CreaEmpaqueTraspasoNESelAlm";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject ad_CreaEmpaqueTraspasoNESelAlm_SA(String prmDocumento,
                                                              String prmPartida,
                                                              String prmCantidad,
                                                              String prmEmpaques,
                                                              String prmAlmacen)
    {

        String METHOD_NAME = "WM_CreaEmpaqueTraspasoNESelAlm_SA";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_PrimeraUltimaTraspasoSelAlm(String prmDocumento,
                                                            String prmPartida,
                                                            String prmCantidad,
                                                            String prmEmpaques,
                                                            String prmPrimerEmpaque,
                                                            String prmUltimoEmpaque,
                                                            String prmCantidadEmpaques,
                                                            String prmAlmacen)
    {

        String METHOD_NAME = "WM_PrimeraUltimaTraspasoSelAlm";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmPrimera",prmPrimerEmpaque);
        request.addProperty("prmUltima",prmUltimoEmpaque);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);

    }
    public DataAccessObject cad_PrimeraUltimaTraspasoSelAlm_SA(String prmDocumento,
                                                               String prmPartida,
                                                               String prmCantidad,
                                                               String prmEmpaques,
                                                               String prmPrimerEmpaque,
                                                               String prmUltimoEmpaque,
                                                               String prmCantidadEmpaques,
                                                               String prmAlmacen)
    {

        String METHOD_NAME = "WM_PrimeraUltimaTraspasoSelAlm_SA";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmPrimera",prmPrimerEmpaque);
        request.addProperty("prmUltima",prmUltimoEmpaque);
        request.addProperty("prmCantidadEmpaques",prmCantidadEmpaques);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);

    }
    public DataAccessObject cad_RegistraEmpaqueTraspasoSelAlm(String prmDocumento,String prmPartida,String prmCodigoEmpaque,
                                                              String prmCantidad,String prmEmpaques,String prmAlmacen)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueTraspasoSelAlm";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
    public DataAccessObject cad_RegistraEmpaqueTraspasoSelAlm_SA(String prmDocumento,String prmPartida,String prmCodigoEmpaque,
                                                                 String prmCantidad,String prmEmpaques,String prmAlmacen)
    {
        String METHOD_NAME = "WM_RegistraEmpaqueTraspasoSelAlm_SA";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEmpaques",prmEmpaques);
        request.addProperty("prmAlmacen",prmAlmacen);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_ConsultaTextosTraspaso(String prmDocumento)
    {
        String METHOD_NAME = "WM_ConsultaTextosTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject c_RegistroTextosTraspaso(String prmDocumento,String prmNotaEntrega,String prmCartaPorte,String prmTextoCabecera)
    {
        String METHOD_NAME = "WM_RegistroTextosTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmNotaEntrega",prmNotaEntrega);
        request.addProperty("prmCartaPorte",prmCartaPorte);
        request.addProperty("prmTextoCabecera",prmTextoCabecera);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }
////// VALIDACION TRASPASOS
    public DataAccessObject c_ConsultaOrdenSurtidoTras(String prmOrdenEmbarque, String prmStatus) {
        conexionWS c = new conexionWS();
            String METHOD_NAME = "WM_ConsultaOrdenSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenEmbarque", prmOrdenEmbarque);
        request.addProperty("prmStatus", prmStatus);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject c_ConsultaEmbarqueValidarPalletsTras(String prmOrdenEmbarque)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmbarqueValidarPalletsTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmOrdenEmbarque",prmOrdenEmbarque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ConsultaCarritoValidaTras(String prmCarrito,String prmOrdenEmbarque)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaCarritoValidaTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEmbarque",prmOrdenEmbarque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }
    public DataAccessObject c_ConsultaTipoRegValidaTras(String prmEmpaque,String prmDocumento, String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTipoRegValidaTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    public DataAccessObject c_ValidaEmbEmpaqueTras(String prmDocumento,String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_ValidaEmbEmpaqueTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmGuia", "");
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }


    /*public DataAccessObject c_ValidaEmbSKUPzasTras(String prmDocumento,String prmNumParte, String prmCantidad)
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
    }*/

    public DataAccessObject c_ValidaEmbSKUCantidadTras(String prmDocumento,String prmNumParte, String prmCantidad)
    {
        String METHOD_NAME = "WM_ValidaEmbSkuCantidadTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCodigoEmpaque",prmNumParte);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmGuia", "");
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }


    //Reempaque

    public DataAccessObject c_RegistrarEmbMaterialTras(String prmDocumento,String prmLinea,String prmValida)
    {
        String METHOD_NAME = "WM_RegistrarEmbMaterialTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmLinea",prmLinea);
        request.addProperty("prmValida",prmValida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, contexto,null);
    }

    /*public DataAccessObject cConsultaListaConsolidadosReempaqueTras(String prmDocumento) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaListaConsolidadosReempaqueTras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }
*/
    public DataAccessObject cad_ConsultaPedidoSurtidoReempaqueTras(String prmPedido)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPedidoSurtidoReempaqueTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }


    public DataAccessObject cad_ConsultaCarritoEmpaquesTras(String prmDocumento,String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesCarritoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,contexto,null);
    }

    public DataAccessObject cCreaPalletReempaqueTras(String prmEmbarque) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CreaPalletReempaqueTras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbarque", prmEmbarque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    public DataAccessObject cConsultaPalletConsTras(String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletConsTras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }
    public DataAccessObject cRegistraReempaqueConsSKUTras(String prmDocumento, String prmPallet,String prmPalletAConsolidar, String prmProducto, String prmCantidadEmpaques) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistraReempaqueSKUTras";
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


    public DataAccessObject cRegistraReempaqueConsTras(String prmDocumento, String prmPallet,String prmPalletAConsolidar, String prmProducto, String prmCantidadEmpaques) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistraReempaqueTras";
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
    public DataAccessObject cCerrarReempaqueTras (String prmEmbarque, String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_CerrarReempaqueTras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmTraspaso", prmEmbarque);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }


    public DataAccessObject cConsultaEmpaqueoSKUTras (String prmEmpaqueOSKU) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaqueOSKUTras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaqueOSKU", prmEmpaqueOSKU);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, contexto, null);
    }

    //endregion
}
