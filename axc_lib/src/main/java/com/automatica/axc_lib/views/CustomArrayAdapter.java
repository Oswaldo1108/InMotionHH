package com.automatica.axc_lib.views;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.automatica.axc_lib.AccesoDatos.ObjetosConexion.Constructor_Dato;

import java.util.ArrayList;

public class CustomArrayAdapter extends ArrayAdapter<Constructor_Dato>
{
    private Context context;
    private ArrayList<Constructor_Dato> arrayData;
    static final  public int TITULO=1;
    static final public  int DATO=2;
    static final public int TAG1=3;
    static final public int TAG2=4;




    /***TOMAR EL INDICE DE UN VALOR TEXTO EN UN SPINNER*/
    public static int getIndex(Spinner spinner, String myString)
    {
        if(myString.equals("DEFAULT"))
        {
            return -3;// se usara el default
        }

        for (int i=0;i<spinner.getCount();i++){
            Log.i("PRB","PRB 1 ");

            if (spinner.getItemAtPosition(i).toString().equals(myString))
            {
                return i;
            }
        }
        return -1;
    }

    public static int getIndex(Spinner spinner, String myString,int TIPO)
    {
        for (int i=0;i<spinner.getCount();i++){
            Log.i("PRB","PRB spinner");

            switch (TIPO){
                case TITULO:
                    if (((Constructor_Dato)(spinner.getAdapter().getItem(i))).getTitulo().equals(myString))
                    {
                        return i;
                    }
                    break;
                case DATO:
                    if (((Constructor_Dato)(spinner.getAdapter().getItem(i))).getDato().equals(myString))
                    {
                        return i;
                    }
                    break;
                case TAG1:
                    if (((Constructor_Dato)(spinner.getAdapter().getItem(i))).getTag1().equals(myString))
                    {
                        return i;
                    }
                    break;
                case TAG2:
                    if (((Constructor_Dato)(spinner.getAdapter().getItem(i))).getTag2().equals(myString))
                    {
                        Log.i("PRB","PRB ACTIVA tag2");
                        Log.i("PRB",((Constructor_Dato)(spinner.getAdapter().getItem(i))).getTag2());
                        return i;
                    }
                    break;
                default:
                    Log.i("Error","El int ingresado no es parte de las variables estaticas declaradas");
                    return -1;
            }

        }
        return -1;
    }


    public CustomArrayAdapter(Context context, int textViewResourceId,
                       ArrayList<Constructor_Dato> arrayData)
    {
        super(context, textViewResourceId, arrayData);
        this.context = context;
        this.arrayData= arrayData;
    }

    @Override
    public int getCount(){
        return arrayData.size();
    }

    @Override
    public Constructor_Dato getItem(int position)
    {
        return arrayData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(arrayData.get(position).getDato());

        return label;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent)
    {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(arrayData.get(position).getDato());

        return label;
    }

    public String getSelectedExtra(int position)
    {
        Log.i("GETSELECTEDEXTRA",position + arrayData.get(position).getTag1());
        return arrayData.get(position).getTag1();
    }

}
