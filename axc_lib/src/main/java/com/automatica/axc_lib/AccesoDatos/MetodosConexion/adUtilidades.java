package com.automatica.axc_lib.AccesoDatos.MetodosConexion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.DataAccessObject;

import org.ksoap2.serialization.SoapObject;

public class adUtilidades extends conexionWS2 {

    private String  NAMESPACE ="http://tempuri.org/";
    private String  Usuario,Estacion, Area;
    private Context context;

    public adUtilidades(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "null");
        Estacion = pref.getString("estacion", "null");
        Area = pref.getString("area", "Almacén");
        this.context = context;
    }

    public DataAccessObject cConsultaBascula(String prmBascula){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultaBascula";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmBascula",prmBascula);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return  c.IniciaAccionSOAP(request, METHOD_NAME,context,null);
    }
    public DataAccessObject cDesconectarBascula(String prmBascula){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_DesconectaBascula";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmBascula",prmBascula);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return  c.IniciaAccionSOAP(request, METHOD_NAME,context,null);
    }

    public DataAccessObject C_ListaProducto(String prmProducto)
    {
        String METHOD_NAME = "WM_ConsultaCoincidenciaArticulo";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmArticulo",prmProducto);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject cConsultarUltimaNotificacion (String prmId){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarUltimaNotificacion";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmId",prmId);
        request.addProperty("prmEstacion",Area);
        //request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context, null);
    }

    public DataAccessObject cConsultarNotificaciones (String prmId){
        conexionWS c = new conexionWS();
        String METHOD_NAME = "WM_ConsultarNotificaciones";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmId",prmId);
        request.addProperty("prmEstacion",Area);
        //request.addProperty("prmUsuario",Usuario);
        return c.IniciaAccionSOAP(request,METHOD_NAME, context, null);
    }

}
