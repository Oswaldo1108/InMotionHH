package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

public class adConsultasFragmento extends conexionWS2 {
    private String NAMESPACE ="http://tempuri.org/";
    private String Usuario,Estacion;
    private Context context;

    public adConsultasFragmento(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "null");
        Estacion = pref.getString("estacion", "null");
        this.context = context;
    }
    public DataAccessObject c_ConsultaReferencia(String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaReferencia";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmTexto",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_BuscarUbicacion(String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_BuscarUbicacion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmUbicacion",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }
    public DataAccessObject c_ConsultarPalletPT(String prmPallet)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarPalletPT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmbalaje",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
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

    public DataAccessObject c_ConsultaEmpaqueMP(String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
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

    public DataAccessObject c_ListarValidacionCalidad()
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaValidarCalidad";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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


    public DataAccessObject c_ConsultaEmpaque(String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaque";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",prmCodigoEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
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
    public DataAccessObject c_ConsultaPalletArticulo(String prmNumParte,String prmRevision)
    {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaPalletArticulo";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmNumParte",prmNumParte);
        request.addProperty("prmRevision",prmRevision);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ConsultaMaquinas(String prmMaquina)
    {
        String METHOD_NAME = "WM_ConsultaMaquinas";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmMaquina",prmMaquina);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject c_ImprimeEtiquetaEmpaque(String prmimpresora,String prmNumEtiquetas) {
        String METHOD_NAME = "WM_ImprimeEtiquetaEmpaque";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmLote", prmimpresora);
        request.addProperty("prmNumEtiquetas", prmNumEtiquetas);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }

    public DataAccessObject c_ImpContenedor(String prmNumEtiquetas,String prmImpresora, String prmOpcion) {
        String METHOD_NAME = "WM_ImpContenedor";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmNumEtiquetas", prmNumEtiquetas);
        request.addProperty("prmImpresora", prmImpresora);
        request.addProperty("prmOpcion", prmOpcion);
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

    public DataAccessObject c_ImprimeSKU(String prmNumParte,String prmUPC, String prmDescripcion,String prmNumEtiquetas, String prmImpresora) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ImprimeSKU";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmUPC", prmUPC);
        request.addProperty("prmDescripcion", prmDescripcion);
        request.addProperty("prmNumEtiquetas", prmNumEtiquetas);
        request.addProperty("prmImpresora", prmImpresora);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject c_ListaArticulos(String prmNumParte,String prmTipo) {
        conexionWS c = new conexionWS();
            String METHOD_NAME = "WM_ListaArticulos";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmTipo", prmTipo);

        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject c_ListaBloqueados(String prmNumParte) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ListaBloqueados";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmNumParte", prmNumParte);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }


    public DataAccessObject c_ConsultaAreas() {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaAreas";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
    public DataAccessObject c_ActualizaArea(String prmArea) {
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ActualizaArea";
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("prmArea", prmArea);
        request.addProperty("prmEstacion", Estacion);
        request.addProperty("prmUsuario", Usuario);
        return c.IniciaAccionSOAP(request, METHOD_NAME, context, null);
    }
}
