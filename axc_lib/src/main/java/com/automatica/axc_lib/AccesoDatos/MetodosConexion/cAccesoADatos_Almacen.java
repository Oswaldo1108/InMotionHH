package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

public class cAccesoADatos_Almacen extends conexionWS2 {

    private String NAMESPACE = "http://tempuri.org/";
    private String Usuario, Estacion;
    private Context context;

    public cAccesoADatos_Almacen(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "null");
        Estacion = pref.getString("estacion", "null");
        this.context = context;
    }

    //Ajustes
    public DataAccessObject c_ConsultarPalletPT(String prmPallet) {
        String METHOD_NAME = "WM_ConsultarPalletPT";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbalaje", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject cMoverPalletACuarentena (String prmPallet,String prmUbicacionDisponible){

        String METHOD_NAME = "WM_MoverPalletACuarentena";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmUbicacionDisponible",prmUbicacionDisponible);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject cMoverPalletADisponible(String prmPallet, String prmUbicacionDisponible){

        String METHOD_NAME = "WM_MoverPalletADisponible";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmUbicacionDisponible",prmUbicacionDisponible);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject c_SugierePosicion(String prmCodigoPallet) {
        String METHOD_NAME = "WM_SugierePosicion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_SugierePosicionCont(String prmCodigoPallet) {
        String METHOD_NAME = "WM_SugierePosicionContenedor";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_colocaPallet(String prmCodigoPallet, String prmUbicacion, String prmUbicacionSug) {
        String METHOD_NAME = "WM_ColocaPallet";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        request.addProperty("prmPosicionSug", prmUbicacionSug);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaCoincidenciaArticulo(String prmArticulo) {
        String METHOD_NAME = "WM_ConsultaCoincidenciaArticulo";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmArticulo", prmArticulo);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_FotoNumParte(String prmNumParte) {
        String METHOD_NAME = "WM_FotoNumParte";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_RegistraEmpaqueNvoPalletv2(String Empaque, String NumParte, String Cantidad, String Ajuste,
                                                         String prmFechaCad, String prmLote, String prmMercado) {
        String METHOD_NAME = "WM_RegistraEmpaqueNvoPalletv2";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoEmpaque", Empaque);
        request.addProperty("prmNumParte", NumParte);
        request.addProperty("prmCantidad", Cantidad);
        request.addProperty("prmAjuste", Ajuste);
        request.addProperty("prmFechaCad", prmFechaCad);
        request.addProperty("prmLote", prmLote);
        request.addProperty("prmMercado", prmMercado);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_OCCreaPalletMPAjusteUnico(String NumParte, String Cantidad, String Ajuste, String prmMercado) {
        String METHOD_NAME = "WM_OCCreaPalletMPAjusteUnico";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmNumParte", NumParte);
        request.addProperty("prmCantidad", Cantidad);
        request.addProperty("prmAjuste", Ajuste);
        request.addProperty("prmMercado", prmMercado);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ReubicarEmbalaje(String prmCodigoPallet, String prmUbicacion) {
        String METHOD_NAME = "WM_ReubicarEmbalaje";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CierraPalletAjuste(String CodigoEmpaque) {
        String METHOD_NAME = "WM_CierraPalletAjuste";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", CodigoEmpaque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultarEmpaque(String prmEmpaque) {
        String METHOD_NAME = "WM_ConsultaEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoEmpaque", prmEmpaque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaContenedorAjustes(String prmEmpaque) {
        String METHOD_NAME = "WM_ConsultaContenedorAjustes";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoEmpaque", prmEmpaque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListarConceptosAjuste(String prmTipoAjuste) {
        String METHOD_NAME = "WM_ListarConceptosAjuste";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmTipoAjuste", prmTipoAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteContenedorNegativo(String prmContenedor, String prmCantidad,
                                                       String prmUbicacion, String prmTipoAjuste) {
        String METHOD_NAME = "WM_AjusteContenedorNegativo";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmContenedor", prmContenedor);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmTipoAjuste", prmTipoAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteContenedorPositivo(String prmContenedor, String prmCantidad,
                                                       String prmUbicacion, String prmTipoAjuste) {
        String METHOD_NAME = "WM_AjusteContenedorPositivo";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmContenedor", prmContenedor);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmTipoAjuste", prmTipoAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteContenedorPositivoSKU(String prmContenedor, String prmSKU,
                                                          String prmUbicacion, String prmTipoAjuste,
                                                          String prmNumParte) {
        String METHOD_NAME = "WM_AjusteContenedorPositivoSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmContenedor", prmContenedor);
        request.addProperty("prmSKU", prmSKU);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmTipoAjuste", prmTipoAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteContenedorPositivoNumParte(String prmContenedor, String prmCantidad,
                                                               String prmUbicacion, String prmTipoAjuste,
                                                               String prmNumParte) {
        String METHOD_NAME = "WM_AjusteContenedorPositivoNumParte";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmContenedor", prmContenedor);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmTipoAjuste", prmTipoAjuste);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListaMercados() {
        String METHOD_NAME = "WM_ListaMercados";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CambiaMercado(String prmCodigoPallet, String prmMercado) {
        String METHOD_NAME = "WM_CambiaMercado";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmMercado", prmMercado);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CambiaMercadoPalletMenudeo(String prmCodigoPallet,String prmMercado) {
        String METHOD_NAME = "WM_CambiaMercadoPalletMenudeo";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CambiarPalletEtiquetado(String prmCodigoPallet) {
        String METHOD_NAME = "WM_CambiarPalletEtiquetado";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CambiaMercadoMenudeo(String prmCodigoPallet,String prmMercado, String prmPrimera, String prmUltima, String prmCantidad) {
        String METHOD_NAME = "WM_CambiaMercadoMenudeo";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmPrimerEmpaque", prmPrimera);
        request.addProperty("prmUltimoEmpaque", prmUltima);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CambiarPalletNE(String prmCodigoPallet, String prmPrimera, String prmUltima, String prmCantidad) {
        String METHOD_NAME = "WM_CambiarPalletNE";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmPrimerEmpaque", prmPrimera);
        request.addProperty("prmUltimoEmpaque", prmUltima);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_registraEmpaqueCambioMercado(String prmCodigoPallet,String prmCodigoEmpaque, String prmMercado) {
        String METHOD_NAME = "WM_CambiaMercadoEmpaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmMercado",prmMercado);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_registraEmpaqueCambioEtiqueta(String prmCodigoPallet,String prmCodigoEmpaque) {
        String METHOD_NAME = "WM_CambiaEtiquetaEmpaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_ReimprimirEtiquetasMercado(String prmPallet, String prmImpresora) {
        String METHOD_NAME = "WM_ReimprimirEtiquetasMercado";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmPallet);
        request.addProperty("prmImpresora", prmImpresora);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ReimprimirEtiquetasCambio(String prmPallet, String prmImpresora) {
        String METHOD_NAME = "WM_ReimprimirEtiquetasCambio";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmPallet);
        request.addProperty("prmImpresora", prmImpresora);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CierraPalletCambio(String prmPallet) {
        String METHOD_NAME = "WM_CierraPalletCambio";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteBajaEmpaque(String prmEmpaque, String prmAjuste) {
        String METHOD_NAME = "WM_AjusteBajaEmpaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteBajaPallet(String prmPallet, String prmAjuste) {
        String METHOD_NAME = "WM_AjusteBajaPallet";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteNuevoEmpaquePalletExistente(String prmEmpaque, String prmPallet,
                                                                String prmNumParte, String prmCantidad,
                                                                String prmRevision, String prmAjuste,
                                                                String prmNumSerie) {
        String METHOD_NAME = "WM_AjusteNuevoEmpaquePalletExistente";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmRevision", prmRevision);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmNumSerie", prmNumSerie);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteNuevoEmpaquePalletExistentePedimento(String prmEmpaque,
                                                                         String prmPallet,
                                                                         String prmNumParte,
                                                                         String prmCantidad,
                                                                         String prmPedimento,
                                                                         String prmClave,
                                                                         String prmFactura,
                                                                         String prmFechaPedimento,
                                                                         String prmFechaRecibo,
                                                                         String prmAjuste) {
        String METHOD_NAME = "WM_AjusteNuevoEmpaquePalletExistentePedimento";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmPedimento", prmPedimento);
        request.addProperty("prmClave", prmClave);
        request.addProperty("prmFactura", prmFactura);
        request.addProperty("prmFechaPedimento", prmFechaPedimento);
        request.addProperty("prmFechaRecibo", prmFechaRecibo);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_AjusteNuevoEmpaque_NE(String prmCantEmpaques,
                                                    String prmPallet,
                                                    String prmNumParte,
                                                    String prmCantidad,
                                                    String prmTipoAjuste,
                                                    String prmFechaCad) {
        String METHOD_NAME = "WM_AjusteNuevoEmpaque_NE";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCantEmpaques", prmCantEmpaques);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmTipoAjuste", prmTipoAjuste);
        request.addProperty("prmFechaCad", prmFechaCad);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjustesPositivos() {
        String METHOD_NAME = "WM_AjustesPositivos";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    //Ajustes Pallet
    public DataAccessObject c_RegistraEmpaqueUnico(String prmNumParte, String prmCantidad, String prmAjuste, String prmMercado) {
        String METHOD_NAME = "WM_OCCreaPalletMPAjusteUnico";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmMercado", prmMercado);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaPalletAbiertoSinAfecta() {
        String METHOD_NAME = "WM_ConsultarPalletAbiertoSinAfecta";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_RegistrarAjusteNuevoPallet(String prmCodigoEmpaque, String prmCantidad, String prmAjuste) {
        String METHOD_NAME = "WM_RegistraAjusteNvoPallet";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoEmpaque", prmCodigoEmpaque);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    //AjustePalletSCH
    public DataAccessObject c_RegistraEmpaqueNvoPallet_NE(String prmCantidadEmpaques, String prmNumParte, String prmCantidad,
                                                          String prmAjuste, String prmFechaCad, String prmLote, String prmMercado) {
        String METHOD_NAME = "WM_RegistraEmpaqueNvoPallet_NE";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCantidadEmpaques", prmCantidadEmpaques);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmFechaCad", prmFechaCad);
        request.addProperty("prmLote", prmLote);
        request.addProperty("prmMercado", prmMercado);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultarPalletPTTabla(String prmPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarPalletPT";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmbalaje", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaEmpaquesPallet_NE(String prmCodigoPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesPallet_NE";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaEmpaquesPallet_Impresion(String prmCodigoPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaquesPallet_Impresion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_AjusteBajaEmpaque_SelProd_NE(String prmPallet, String prmProducto,
                                                           String prmCantidadEmpaques, String prmAjuste) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_AjusteBajaEmpaque_SelProd_NE";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmProducto", prmProducto);
        request.addProperty("prmCantidadEmpaques", prmCantidadEmpaques);
        request.addProperty("prmAjuste", prmAjuste);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    //Traspaso
    public DataAccessObject c_ListaOrdenesTraspaso(String prmDocumento) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarOrdenesTraspaso";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListarPartidasEnvioTraspaso(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasEnvioTraspaso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ConsultaCarritoSurtidoTras(String prmPedido,String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaCarritoSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    // *******************************************************************************************NUEVOS METODOS *******************************************************************//

    public DataAccessObject cad_ListarPartidasTrasSpinner(String prmPedido)
    {
        String METHOD_NAME = "WM_ConsultaPartidasTrasSpinner";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }



    public DataAccessObject cad_ConsultaSurtidoTrasDetPartida(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaSurtidoTrasDetPartida";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaEmpaqueTrasSugerido(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSugeridoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaEmpaqueSurtidoTras(String prmPedido,String prmPartida,String prmEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistroEmpaqueSurtidoTras(String prmPedido,String prmPartida,String prmEmpaque,String prmArmadoPallet)
    {
        String METHOD_NAME = "WM_RegistroEmpaqueSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEmpaque",prmEmpaque);
        request.addProperty("prmArmadoPallet",prmArmadoPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaTarimaConsolidadaTras(String prmPedido,String prmPalletCons)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaTarimaConsolidadaTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPalletCons",prmPalletCons);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_CierraPalletSurtidoTras(String prmPallet)
    {
        String METHOD_NAME = "WM_CierraPalletSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }


    // **************************************************************************************No Etiquetado ***********************************************************/

    public DataAccessObject cad_ConsultaEmpaqueSugeridoTras_NE(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueTrasSugerido_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaPalletSurtidoTras_NE(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPalletSurtidoTras_NE";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistroEmpaqueSurtidoTras_NE(String prmPedido,String prmPartida,String prmPallet,String prmCantidadEmpaques,String prmArmadoPallet)
    {
        String METHOD_NAME = "WM_RegistroEmpaqueSurtidoTras_NE";
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

    //******************************************************************************************* PIEZAS *********************************************//

    public DataAccessObject cad_ConsultaContenedorSugeridoTras(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaContenedorSugeridoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ConsultaContenedorSurtidoTras(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaContenedorSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_OSSurtirPiezasTras(String prmPedido,String prmPartida,String prmContenedor,String prmCantidadPiezas,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSSurtirPiezasTras";
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

    public DataAccessObject c_OSSurtirPiezasSKUTras(String prmPedido,String prmPartida,String prmContenedor,String prmSKU,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSSurtirPiezasSKUTras";
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

    public DataAccessObject c_OSCancelarSurtPiezasTras(String prmPedido,String prmPartida,String prmContenedor,String prmPiezas,String prmCarrito)
    {
        String METHOD_NAME = "WM_OSCancelarSurtPiezasTras";
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

    //*******************************************************************************************PALLET**********************///////////////////////////

    public DataAccessObject cad_ConsultaPalletSugeridoTras(String prmPedido,String prmPartida)
    {
        String METHOD_NAME = "WM_ConsultaPalletSugeridoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_ConsultaPalletSurtidoTras(String prmPedido,String prmPartida,String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistroPalletSurtidoTras(String prmPedido,String prmPartida,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistroPalletSurtidoTras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPartida",prmPartida);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public DataAccessObject cad_RegistroPalletSurtidoTrasMultPart(String prmPedido,String prmPallet)
    {
        String METHOD_NAME = "WM_RegistroPalletSurtidoTrasMultPart";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }

    public DataAccessObject c_ConsultaCarritoSurtido(String prmPedido,String prmCarrito)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaCarritoSurtido";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPedido",prmPedido);
        request.addProperty("prmCarrito",prmCarrito);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME,context,null);
    }



    public DataAccessObject c_ListarPartidasRecepcionTraspaso(String prmDocumento) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasRecepcionTraspaso";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaPalletSugeridoTraspasoEnvio(String prmDocumento, String prmPartida) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletSugeridoTraspasoEnvio";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_RegistroTraspasoPalletEnvio(String prmDocumento, String prmPartida, String prmCodigoPallet) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_RegistroTraspasoPalletEnvio";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDocumento", prmDocumento);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    //Devoluciones
    public DataAccessObject c_ListaOrdenesDevolucion() {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarOrdenesDevolucion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListaDevoluciones(String prmDevolucion) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasDevolucion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDevolucion", prmDevolucion);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListarPartidasDevEnProceso(String prmDocumento)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListarPartidasDevEnProceso";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento );
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prsmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaPartidaDevolucion(String prmDevolucion, String prmPartida) {
        String METHOD_NAME = "WM_DevolucionDet";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmDevolucion", prmDevolucion);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CreaEmpaqueDevSE(String prmOrdenDevolucion, String prmPartida, String prmLoteProveedor,
                                               String prmCantidad, String prmEmpaques) {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueSEDevolucion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenDevolucion", prmOrdenDevolucion);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmLoteProveedor", prmLoteProveedor);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEmpaques", prmEmpaques);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_CierraPalletDevolucion(String prmPallet) {
        String METHOD_NAME = "WM_OCCierraPalletDevolucion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPallet", prmPallet);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListaImpresoras() {
        String METHOD_NAME = "WM_ListaImpresoras";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ListarLotesDev(String prmOrdenCompra, String prmPartida, String prmNumParte) {
        String METHOD_NAME = "WM_ListarLotesDev";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenCompra", prmOrdenCompra);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    /*    public DataAccessObject c_ConsultarPalletAbiertoDev()
    }
    <-------------------------------------------------------AQUI VA METODO CONSULTA PALLET ABIERTO ------------------------------------------->
     */
    public DataAccessObject c_ConsultaEmpaqueDev(String prmEmpaque) {
        String METHOD_NAME = "WM_ConsultarEmpaqueDev";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaPalletAbiertoDev(String prmIdDevolucion, String prmPartida) {
        String METHOD_NAME = "WM_ConsultarPalletAbiertoDev";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmIdDevolucion", prmIdDevolucion);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_OCRegistrarEmpaqueDevolucion(String prmOrdenDevolucion, String prmPartida,
                                                           String prmCodigoEmpaque, String prmLoteProveedor,
                                                           String prmNumSerie, String prmCantidad,
                                                           String prmEmpaques) {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueDevolucion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenDevolucion", prmOrdenDevolucion);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmCodigoEmpaque", prmCodigoEmpaque);
        request.addProperty("prmLoteProveedor", prmLoteProveedor);
        request.addProperty("prmNumSerie", prmNumSerie);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEmpaques", prmEmpaques);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_OCRegistrarEmpaqueUnicoDevolucion(String prmOrdenDevolucion, String prmPartida,
                                                                String prmCodigoEmpaque, String prmLoteProveedor, String prmNumSerie) {
        String METHOD_NAME = "WM_OCRegistrarEmpaqueUnicoDevolucion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenDevolucion", prmOrdenDevolucion);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmCodigoEmpaque", prmCodigoEmpaque);
        request.addProperty("prmLoteProveedor", prmLoteProveedor);
        request.addProperty("prmNumSerie", prmNumSerie);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_PrimeraUltimaEmpaqueDev(String prmOrdenDevolucion, String prmPartida,
                                                      String prmLoteProveedor, String prmCantidad,
                                                      String prmEmpaques, String prmPrimerEmpaque,
                                                      String prmUltimoEmpaque, String prmCantidadEmpaques) {
        String METHOD_NAME = "WM_PrimeraUltimaEmpaqueDev";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenDevolucion", prmOrdenDevolucion);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmLoteProveedor", prmLoteProveedor);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEmpaques", prmEmpaques);
        request.addProperty("prmPrimerEmpaque", prmPrimerEmpaque);
        request.addProperty("prmUltimoEmpaque", prmUltimoEmpaque);
        request.addProperty("prmCantidadEmpaques", prmCantidadEmpaques);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_CreaLoteRecepcionDev(String prmOrdenDev, String prmPartida, String prmLote, String prmGenerarLote) {
        String METHOD_NAME = "WM_CreaLoteRecepcionDev";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmOrdenDev", prmOrdenDev);
        request.addProperty("prmPartida", prmPartida);
        request.addProperty("prmLote", prmLote);
        request.addProperty("prmGenerarLote", prmGenerarLote);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    //TERMINA DEVOLUCION


    public DataAccessObject c_ConsultaPosicion(String prmPosicion) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPosicion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmPosicion", prmPosicion);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);

        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject cad_ContenedorSugeridoReubicacion(String prmProducto) {
        String METHOD_NAME = "WM_ContenedorSugeridoReubicacion";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmProducto", prmProducto);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaEmpaqueMultipleSKU(String prmCodigo) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaEmpaqueMultipleSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigo", prmCodigo);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);

        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_ReubicarProductoPorCantidades(String prmUbicacion, String prmProducto,
                                                            String prmLote, String prmCantidad, String prmCantidadXEmpaque, String prmUbicacionNueva) {

        String METHOD_NAME = "WM_ReubicarProductoPorCantidades";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmProducto", prmProducto);
        request.addProperty("prmLote", prmLote);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmCantidadXEmpaque", prmCantidadXEmpaque);
        request.addProperty("prmUbicacionNueva", prmUbicacionNueva);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);

    }


    public DataAccessObject c_ReubicarProductoPorSKU(String prmCodigo, String prmUbicacion, String prmNvoContenedor, String prmSKU, String prmCantidad) {

        String METHOD_NAME = "WM_ReubicarProductoPorSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigo", prmCodigo);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmNvoContenedor", prmNvoContenedor);
        request.addProperty("prmSKU", prmSKU);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);

    }


    public DataAccessObject c_ReubicarProductoPorSKUPiezas(String prmCodigo, String prmUbicacion, String prmNvoContenedor, String prmSKU, String prmCantidad) {

        String METHOD_NAME = "WM_ReubicarProductoPorSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigo", prmCodigo);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmNvoContenedor", prmNvoContenedor);
        request.addProperty("prmSKU", prmSKU);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);

    }


    /*
            Metodos primera y ultima
     */


    public DataAccessObject c_ReubicarEmpaque(String prmCodigoPallet, String prmUbicacion) {
        String METHOD_NAME = "WM_ReubicarEmpaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_ReubicarCont(String prmCodigoPallet, String prmUbicacion) {
        String METHOD_NAME = "WM_ReubicarContenedor";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoPallet", prmCodigoPallet);
        request.addProperty("prmUbicacion", prmUbicacion);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cConsultaContenedor(String prmCodigoEmpaque) {
        String METHOD_NAME = "WM_ConsultaContenedor";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmCodigoEmpaque", prmCodigoEmpaque);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cMoverEmpaqueACuarentena(String prmEmpaque, String prmUbicacionDisponible) {

        String METHOD_NAME = "WM_MoverEmpaqueACuarentena";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmUbicacionDisponible", prmUbicacionDisponible);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cMoverContenedorACuarentena(String prmEmpaque, String prmUbicacionDisponible) {

        String METHOD_NAME = "WM_MoverContenedorACuarentena";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmUbicacionDisponible", prmUbicacionDisponible);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cMoverEmpaqueADisponible(String prmEmpaque, String prmUbicacionDisponible) {

        String METHOD_NAME = "WM_MoverEmpaqueADisponible";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmUbicacionDisponible", prmUbicacionDisponible);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject cMoverContenedorADisponible(String prmEmpaque, String prmUbicacionDisponible) {

        String METHOD_NAME = "WM_MoverContenedorADisponible";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEmpaque", prmEmpaque);
        request.addProperty("prmUbicacionDisponible", prmUbicacionDisponible);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ConsultaPalletArmadoAjuste() {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletArmadoAjuste";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_RegistrarAjusteRefaccionesSKU(String prmCantidad,
                                                            String prmSKU) {
        String METHOD_NAME = "WM_RegistrarAjusteRefaccionesSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmSKU", prmSKU);
        request.addProperty("prmCantidad", prmCantidad);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_RegistrarAjusteRefaccionesSKUPiezas(String  prmCantidad,
                                                                  String  prmSKU)
    {
        String METHOD_NAME = "WM_RegistrarAjusteRefaccionesSKUPiezas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmSKU",prmSKU);
        request.addProperty("prmCantidad",prmCantidad);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_OCCierraPalletAjusteRefacciones(String prmPallet)
    {
        String METHOD_NAME = "WM_OCCierraPalletAjusteRefacciones";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
}