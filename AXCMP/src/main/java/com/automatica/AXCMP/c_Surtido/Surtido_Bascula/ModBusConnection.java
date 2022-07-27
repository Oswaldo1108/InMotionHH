package com.automatica.AXCMP.c_Surtido.Surtido_Bascula;

import android.util.Log;
import android.view.View;

import com.zgkxzx.modbus4And.requset.ModbusParam;
import com.zgkxzx.modbus4And.requset.ModbusReq;
import com.zgkxzx.modbus4And.requset.OnRequestBack;

import java.util.Arrays;

public class ModBusConnection
{
    String TAG = ModBusConnection.class.getSimpleName();
    String HOST;
    int PORT;
    int SLAVEID = 2;

    //"192.168.1.102"   8899
    public ModBusConnection(String HOST,int PORT)
    {
        this.HOST = HOST;
        this.PORT = PORT;
    }

    public ModBusConnection()
    {

    }


    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public void IniciarConexion()
    {
        try {
            ModbusReq.getInstance().setParam(new ModbusParam()
                    .setHost(HOST)
                    .setPort(PORT)
                    .setEncapsulated(false)
                    .setKeepAlive(true)
                    .setTimeout(4000)
                    .setRetries(2))
                    .init(new OnRequestBack<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Log.d(TAG, "onSuccess " + s);
                        }

                        @Override
                        public void onFailed(String msg) {
                            Log.d(TAG, "onFailed " + msg);
                        }
                    });
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void writeRegisterClickEvent(View view, final int offset, final int value)
    {
        try
        {
            ModbusReq.getInstance().writeRegister(new OnRequestBack<String>()
            {
                @Override
                public void onSuccess(String s)
                {
                    Log.e(TAG, "writeRegister onSuccess    " + s + "OFFSET   " + offset + " VALUE" + value);
                }

                @Override
                public void onFailed(String msg)
                {
                    Log.e(TAG, "writeRegister onFailed " + msg + " SLAVEID" + SLAVEID + " offset " + offset + " value" + value);
                }
            }, SLAVEID, offset, value);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void readHoldingRegistersClickEvent(View view)
    {

        //readHoldingRegisters
        ModbusReq.getInstance().readHoldingRegisters(new OnRequestBack<short[]>()
        {
            @Override
            public void onSuccess(short[] data)
            {
                Log.d(TAG, "readHoldingRegisters onSuccess " + Arrays.toString(data));
            }

            @Override
            public void onFailed(String msg)
            {
                Log.e(TAG, "readHoldingRegisters onFailed " + msg);
            }
        }, SLAVEID, 2, 8);


    }


    public ModbusReq getInstance()
    {
        return ModbusReq.getInstance();
    }



    public void destroyInstance()
    {
        try
        {
            if(ModbusReq.getInstance()!=null)
            {

                ModbusReq.getInstance().destory();
                Log.e(TAG, "ModbusReq Instance destroyed (ModBusConnection class)") ;

            }else
            {
                Log.e(TAG, "ModbusReq Instance not destroyed (ModBusConnection class null)") ;

            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
