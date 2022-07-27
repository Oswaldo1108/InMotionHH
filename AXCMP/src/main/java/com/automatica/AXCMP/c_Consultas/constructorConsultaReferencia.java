package com.automatica.AXCMP.c_Consultas;

public class constructorConsultaReferencia
{
    String Transaccion,Usuario, Nombre, Estacion, ERPAlmacen,Dato1,Dato2,
            dato3,dato4,dato5,dato6,dato7,dato8,dato9,dato10,
            FechaCrea;


    public constructorConsultaReferencia(String transaccion,String usuario, String nombre, String estacion,
                                         String ERPAlmacen, String dato1, String dato2, String dato3,
                                         String dato4, String dato5, String dato6, String dato7, String dato8,
                                         String dato9, String dato10, String fechaCrea)
    {
        Transaccion = transaccion;
        Usuario = usuario;
        Nombre = nombre;
        Estacion = estacion;
        this.ERPAlmacen = ERPAlmacen;
        Dato1 = dato1;
        Dato2 = dato2;
        this.dato3 = dato3;
        this.dato4 = dato4;
        this.dato5 = dato5;
        this.dato6 = dato6;
        this.dato7 = dato7;
        this.dato8 = dato8;
        this.dato9 = dato9;
        this.dato10 = dato10;
        FechaCrea = fechaCrea;
    }

    public String getTransaccion() {
        return Transaccion;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getEstacion() {
        return Estacion;
    }

    public String getERPAlmacen() {
        return ERPAlmacen;
    }

    public String getDato1() {
        return Dato1;
    }

    public String getDato2() {
        return Dato2;
    }

    public String getDato3() {
        return dato3;
    }

    public String getDato4() {
        return dato4;
    }

    public String getDato5() {
        return dato5;
    }

    public String getDato6() {
        return dato6;
    }

    public String getDato7() {
        return dato7;
    }

    public String getDato8() {
        return dato8;
    }

    public String getDato9() {
        return dato9;
    }

    public String getDato10() {
        return dato10;
    }

    public String getFechaCrea() {
        return FechaCrea;
    }
}
