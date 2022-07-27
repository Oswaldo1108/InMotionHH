package com.automatica.AXCMP.SoapConection.prb;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

//import com.automatica.axc_lib.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.DataAccessObject;

import org.ksoap2.serialization.SoapObject;
//import com.automatica.axc_lib.SoapConection.prb.conexionWS;

public class cAccesoADatos extends conexionWS
{
    private String  NAMESPACE ="http://tempuri.org/";
    private String  Usuario,Estacion;
    private Context context;

    public cAccesoADatos(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Usuario = pref.getString("usuario", "NONE");
        Estacion = pref.getString("estacion", "NONE");
        this.context = context;
    }

    public DataAccessObject cad_ConsultaResumenMIAXC()
    {
        String METHOD_NAME = "WM_ConsultaResumenMIAXC";
        return IniciaAccionSOAP( new SoapObject(NAMESPACE,METHOD_NAME).addProperty("prmUsuario",Usuario).addProperty("prmEstacion",Estacion),METHOD_NAME,context,null);
    }

    public DataAccessObject cad_RegistraConfiguracionMiAXC()
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String METHOD_NAME = "WM_RegistraConfigMIAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prm1",pref.getInt("OC_Q1", 15));
        request.addProperty("prm2",pref.getInt("OC_Q2", 15));
        request.addProperty("prm3",pref.getInt("TRAS_Q1", 15));
        request.addProperty("prm4",pref.getInt("TRAS_Q2", 15));
        request.addProperty("prm5",pref.getInt("Pedido_Q1", 15));
        request.addProperty("prm6",pref.getInt("Pedido_Q2", 15));
        request.addProperty("prm7",pref.getInt("OSurtido_Q1", 15));
        request.addProperty("prm8",pref.getInt("OSurtido_Q2", 15));
        request.addProperty("prm9",pref.getInt("OP_Q1", 15));
        request.addProperty("prm10",pref.getInt("OP_Q2", 15));
        request.addProperty("prm11",pref.getInt("OEMB_Q1", 15));
        request.addProperty("prm12",pref.getInt("OEMB_Q2", 15));
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        Log.i("SoapRequest", request.toString());

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaEstacionesIncidencia()
    {
        String METHOD_NAME = "WM_ConsultaEstacionesIncidencia";
        return IniciaAccionSOAP( new SoapObject(NAMESPACE,METHOD_NAME).addProperty("prmUsuario",Usuario).addProperty("prmEstacion",Estacion),METHOD_NAME,context,null);
    }

    public DataAccessObject SOAPLogin(String usuario, String contraseña,String autorizacion)
    {
        String METHOD_NAME = "WM_Login";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

        request.addProperty("prmUsuario",usuario);
        request.addProperty("prmPassword",contraseña);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmAutorizacion", autorizacion);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject SOAPIncidencia(String prmArea,String prmDescripcion,byte[] imgBase64String)
    {
        String METHOD_NAME = "WM_PruebaFoto";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmArea",prmArea);
        request.addProperty("prmDesc",prmDescripcion);
        request.addProperty("prmFoto",imgBase64String);
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

    public DataAccessObject cad_ConsultaIncidenciaDet(String prmIdIncidencia)
    {
        String METHOD_NAME = "WM_ConsultaIncidenciaDet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmIdIncidencia",prmIdIncidencia);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaIncidenciasEst(String prmEstIncidencia)
    {
        String METHOD_NAME = "WM_ConsultaIncidenciasEst";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstIncidencia",prmEstIncidencia);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);

        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaPallet(String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPalletMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaPosicion(String prmCodigoPosicion)
    {
        String METHOD_NAME = "WM_ConsultaPosicionMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPosicion",prmCodigoPosicion);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
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

    public DataAccessObject cad_ConsultaExistencias(String prmProducto)
    {
        String METHOD_NAME = "WM_ConsultaExistenciasMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmProducto",prmProducto);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaEmpaque(String prmEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",prmEmpaque);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaPalletDet(String prmModulo, String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPalletDetMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmPallet);
        request.addProperty("prmModulo",prmModulo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaPalletReimpresionDet(String prmPallet)
    {
        String METHOD_NAME = "WM_ConsultaPalletReimpresionMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmPallet);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }


    public DataAccessObject cad_ConsultaEmpaqueDet(String prmModulo, String prmEmpaque)
    {
        String METHOD_NAME = "WM_ConsultaEmpaqueDetMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoEmpaque",prmEmpaque);
        request.addProperty("prmModulo",prmModulo);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaOrdenProd(String prmDocumento)
    {
        String METHOD_NAME = "WM_ConsultaOPMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaOrdenCompra(String prmDocumento)
    {
        String METHOD_NAME = "WM_ConsultaOCMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ConsultaTraspaso(String prmDocumento)
    {
        String METHOD_NAME = "WM_ConsultaTraspasoMiAXC";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmDocumento",prmDocumento);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME, context,null);
    }

    public DataAccessObject cad_ListaImpresorasMiAXC()
    {
        String METHOD_NAME = "WM_ListaImpresoras";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }


    public DataAccessObject c_ReimprimePalletMP(String prmCodigoPallet,String prmImpresora)
    {
        String METHOD_NAME = "WM_ReimprimePalletMP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }



    public DataAccessObject c_ReimprimePalletPT(String prmCodigoPallet,String prmImpresora)
    {
        String METHOD_NAME = "WM_ReimprimePalletPT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmCodigoPallet",prmCodigoPallet);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject c_ReimprimeEmpaqueMP(String prmEmpaque1, String prmEmpaque2, String prmImpresora)
    {
        String METHOD_NAME = "WM_ReimprimeEmpaqueMP";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmpaque1",prmEmpaque1);
        request.addProperty("prmEmpaque2",prmEmpaque2);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject c_ReimprimeEmpaquePT(String prmEmpaque1, String prmEmpaque2, String prmImpresora)
    {
        String METHOD_NAME = "WM_ReimprimeEmpaquePT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmEmpaque1",prmEmpaque1);
        request.addProperty("prmEmpaque2",prmEmpaque2);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }




    public DataAccessObject c_ImprimeEtiquetaEmpaquesPallet(String prmPallet,  String prmImpresora)
    {
        String METHOD_NAME = "WM_ImprimeEtiquetaEmpaquesPallet";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

    }

    public DataAccessObject c_ImprimeEtiquetaEmpaquesPalletPT(String prmPallet,  String prmImpresora)
    {
        String METHOD_NAME = "WM_ImprimeEtiquetaEmpaquesPalletPT";
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        request.addProperty("prmPallet",prmPallet);
        request.addProperty("prmImpresora",prmImpresora);
        request.addProperty("prmEstacion",Estacion);
        request.addProperty("prmUsuario",Usuario);
        return IniciaAccionSOAP(request,METHOD_NAME,context,null);

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


}


