package com.automatica.AXCMP.c_Surtido.Surtido_Bascula;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class EnviarData
{
    public void dataSender(String data ,Socket s) throws IOException
    {


        ObjectOutputStream oos = null;
        OutputStreamWriter osw;
        try
        {
            s.setSoTimeout(1000);
            osw =new OutputStreamWriter(s.getOutputStream(), "UTF-8");

            osw.write(data,0 ,data.length());
            oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeUTF(data);


        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

        finally
        {
            if(oos != null) oos.close();
            if(s != null) s.close();
            Log.i("SoapResponse","EnviarData - finally");
        }

    }
}
