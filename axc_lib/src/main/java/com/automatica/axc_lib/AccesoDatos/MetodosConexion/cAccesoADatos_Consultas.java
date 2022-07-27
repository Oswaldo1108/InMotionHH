package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

public class cAccesoADatos_Consultas extends conexionWS2{
    private String  NAMESPACE ="http://tempuri.org/";
    private String  Usuario,Estacion;
    private Context context;

    public cAccesoADatos_Consultas(Context context)
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

    public DataAccessObject c_ConsultaEmpaque(String prmCodigoEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueMP";
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
}



