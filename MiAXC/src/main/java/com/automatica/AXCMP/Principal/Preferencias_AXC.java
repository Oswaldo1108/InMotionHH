package com.automatica.AXCMP.Principal;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import androidx.annotation.RequiresApi;
import android.view.View;
import android.widget.Toast;

import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.AXCMP.SoapConection.DataAccessObject;
import com.automatica.AXCMP.SoapConection.prb.cAccesoADatos;

public class Preferencias_AXC  extends PreferenceActivity
{
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferencias_miaxc2);


        findPreference("registrar").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                SegundoPlano sp = new SegundoPlano("RegistraConfigMiAXC");
                sp.execute();

                return false;
            }
        });
    }
    private class SegundoPlano extends AsyncTask<Void,Void,Void>
    {
        cAccesoADatos ca = new cAccesoADatos(Preferencias_AXC.this);
        DataAccessObject dao;
        String tarea;
        View view;

        public SegundoPlano(String tarea)
        {
            this.tarea = tarea;
        }
        @Override
        protected void onPreExecute()
        {
            try {
            //    mListener.ActivaProgressBar(true);

            }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(Preferencias_AXC.this, getCurrentFocus(), e.getMessage(), false, true, true);
                }
        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            try
                {
//                    if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("DebugMode", false))
//                        {
//                            dao = new DataAccessObject(true,"",null);
//                            return null;
//                        }

                    if(!this.isCancelled())
                        {
                            switch (tarea)
                                {
                                    case "RegistraConfigMiAXC":
                                        dao = ca.cad_RegistraConfiguracionMiAXC();//, , , , , , , , , , , );
                                        break;

                                    default:
                                        dao = new DataAccessObject(false,"Operación no soportada",null);
                                        break;
                                }

                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                dao = new DataAccessObject(false,e.getMessage(),null);
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            try
                {
                    if (dao.iscEstado())
                        {
                            switch (tarea)
                                {
                                    case "RegistraConfigMiAXC":
                                        Toast.makeText(Preferencias_AXC.this, "Configuración registrada correctamente", Toast.LENGTH_LONG).show();
                                        break;
                                }
                        }else
                        {
                            new popUpGenerico(Preferencias_AXC.this, getCurrentFocus(), dao.getcMensaje(), false, true, true);

                        }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    new popUpGenerico(Preferencias_AXC.this, getCurrentFocus(), e.getMessage(), false, true, true);

                }



        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }
    }



}
