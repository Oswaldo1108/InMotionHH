package com.automatica.AXCPT.objetos;

import android.util.Log;

public class ObjetoEtiquetaSKU {
    public String sku;
    public String numeroSerie;
    public String codigo;

    public String cadena;

    public ObjetoEtiquetaSKU(String codigo) {
        this.codigo = codigo;



        if(codigo.substring(0,3).equals("(P)")) {
            cadena = codigo.replace("(P)", "");
            if(codigo.contains("(S)")) {
                cadena = cadena.replace("(S)", "");
                String[] datos = cadena.split("\\|");
                sku = datos[0];
                numeroSerie = datos[1];
            }
            else{
                sku=cadena;
                numeroSerie="";
            }

        }

        else if(codigo.substring(0,3).equals("(S)")&& codigo.contains("(P)")){
            cadena = codigo.replace("(P)", "");
            cadena = cadena.replace("(S)","");
            String [] datos = cadena.split("\\|");
            sku = datos[1];
            numeroSerie = datos[0];
        }


        else if(codigo.charAt(0) == 'M' && codigo.charAt(7) == 'C' && codigo.length()==27){
            sku=codigo.substring(8,15);
            numeroSerie=codigo;
        }


        else{
            sku=codigo;
            numeroSerie="";
        }


    }

/*   public ObjetoEtiquetaSKU incrByTen() {
        ObjetoEtiquetaSKU split = new ObjetoEtiquetaSKU(this.codigo);
       Log.e("Mensaje", "Hola");
        return split;
    }*/

    }



