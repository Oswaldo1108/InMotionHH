package com.automatica.AXCMP.MiAXC;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import com.automatica.AXCMP.ImpresionEtiquetas.act_MiAXC_Impresion_Etiquetas;
import com.automatica.AXCMP.Liberaciones.LiberacionEmbarque.act_MiAXC_Liberacion_Embarque;
import com.automatica.AXCMP.Liberaciones.LiberacionOrdenCompra.act_MiAXC_Liberacion_OrdenCompra;
import com.automatica.AXCMP.Liberaciones.LiberacionTransfer.act_MiAXC_Liberacion_Transferencia;
import com.automatica.AXCMP.Maulec.MainActivity_MLC;
import com.automatica.AXCMP.MiAXC.ConsultaPosiciones.act_Mapa_Almacen;
import com.automatica.AXCMP.MiAXC.ConsultaTransacciones.act_MiAXC_ConsultaTransacciones;
import com.automatica.AXCMP.MiAXC.Kardex.act_MiAXC_Kardex;
import com.automatica.AXCMP.Principal.fragment_VLC_Cargando;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.automatica.AXCMP.Principal.Adaptador_RV_MenuPrincipal;
import com.automatica.AXCMP.Principal.Preferencias_AXC;
import com.automatica.AXCMP.Principal.intro_fragment;
import com.automatica.AXCMP.R;
import com.automatica.AXCMP.Servicios.popUpGenerico;
import com.automatica.axc_lib.Activities.cIncidencia.fragmentoIncidencia;

public class MainActivity<intro> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   frgmnt_Menu_Inicio.OnFragmentInteractionListener,
        frgmnt_Visor_Incidencias_Estaciones.OnFragmentInteractionListener,
        frgmnt_ConsultaDetalleIncidencia.OnFragmentInteractionListener,
        fragmentoIncidencia.OnFragmentInteractionListener,
        Adaptador_RV_MenuPrincipal.onClickRV,
        frgmt_Consulta_Camera_view.dataTransfer,
        fragmento_menu_consultas.OnFragmentInteractionListener,
        frgmnt_Consulta_Obj.OnFragmentInteractionListener,
        frgmnt_Consulta_Pallet_Det.OnFragmentInteractionListener,
        Consulta_Documentos.OnFragmentInteractionListener,
        frgmnt_Consulta_Documento_Det.OnFragmentInteractionListener,
        frgmnt_Consulta_Documento_Partidas.OnFragmentInteractionListener,
        frgmnt_Consulta_Documento_Pallets.OnFragmentInteractionListener,
        Consulta_Pallets.OnFragmentInteractionListener,
        Consulta_Empaque_Pallet_Det.OnFragmentInteractionListener,
        Consulta_Documentos_OC.OnFragmentInteractionListener,
        Consulta_Documentos_Tras.OnFragmentInteractionListener,
        intro_fragment.OnFragmentInteractionListener,
        Consulta_Pallets_Sin_Colocar.OnFragmentInteractionListener,
        Consulta_Existencias.OnFragmentInteractionListener,
        frgmnt_Consulta_Documento_Det_Lista.OnFragmentInteractionListener,
        fragment_VLC_Cargando.OnFragmentInteractionListener



{

    //OBJECTS
    Context contexto = this;

    //FRAGMENTS

    //VIEWS
    FloatingActionButton fab;
    TextView txtv_Usuario_Menu_Header, txtv_Detalle_Menu_Header;
    FrameLayout progressBarHolder,progressBarHolder_webm;
    Toolbar toolbar;
    VideoView vv_Loading;
    View conteinerWebm;


    //FRAGMENT TAGS
    private static final String frgtag_ListaIncidencias ="LISTAINCIDENCIAS";
    private static final String frgtag_IncidenciaDet ="FRGDETINC";
    private static final String frgtag_ReportaIncidencia ="FRGREPINS";
    private static final String frgtag_ConsultaCamaraView = "FRGCMR";
    private static final String frgtag_Menu_Inicio = "FRGMI";
    private static final String frgtag_Menu_Consultas= "FRGMC";
    private static final String frgtag_ConsultaPallet = "FRGCP";
    private static final String frgtag_ConsultaPalletDet = "FRGCPDT";
    private static final String frgtag_ConsultaDocumentos = "FRGCD";
    private static final String frgtag_ConsultaPalletsDET = "FRGCPLTDET";
    private static final String frgtag_ConsultaEmpaquePalletDET = "FRGCEPDET";
    private static final String frgtag_ConsultaDocumentosOC = "FRGCDOC";
    private static final String frgtag_ConsultaDocumentosTras = "FRGCDTRAS";
    private static final String frgtag_Loading= "FRGLOAD";
    private static final String frgtag_ConsultaPalletsSinColocar = "FRGCPSC";
    private static final String frgtag_ConsultaExistenciasDet = "FRGCEDT";

    private String fragment_Menu_Actual;
    boolean permitirConsultaPallet = false;
    Uri video;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                declararVariables();
                agregarListeners();

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, frgmnt_Menu_Inicio.newInstance(null, null),frgtag_Menu_Inicio)
                        .commit();
                fragment_Menu_Actual = frgtag_Menu_Inicio;

            }catch (Exception e)
            {
                e.printStackTrace();
                new popUpGenerico(MainActivity.this, getCurrentFocus(), e.getMessage(), false, true,true);
            }

    }

    private  void declararVariables()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_camera);
        this.onOptionsItemSelected(navigationView.getCheckedItem());
        View v = navigationView.getHeaderView(0);

        txtv_Usuario_Menu_Header = (TextView) v.findViewById(R.id.txtv_NavHeadUsername);
        txtv_Detalle_Menu_Header = (TextView) v.findViewById(R.id.txtv_NavHeadUsr);

        fab = (FloatingActionButton) findViewById(R.id.fab);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);
        txtv_Usuario_Menu_Header.setText(pref.getString("usuarioNombre", "NULO"));
//        txtv_Detalle_Menu_Header.setText(pref.getString("usuario", "NULO"));
        txtv_Detalle_Menu_Header.setText(getString(R.string.app_name) + " - " + getString(R.string.app_version));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //conteinerWebm = findViewById(R.id.layout_webm);
        //conteinerWebm.setVisibility(View.VISIBLE);

        progressBarHolder_webm = (FrameLayout) findViewById(R.id.progressBarHolder_auto);
        vv_Loading = findViewById(R.id.videoView);

        video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loading_automatica);


    }

    private void agregarListeners()
    {
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fl_ContenedorFragments, fragmentoIncidencia.newInstance(null, null),frgtag_ReportaIncidencia)
                        .commit();


//                startActivity(new Intent(MainActivity.this, act_MiAXC_ConsultaTransacciones.class));
//                startActivity(new Intent(MainActivity.this, act_MiAXC_Kardex.class));

            }
        });
    }



    boolean salir = false;
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);
                return;
            }
//      switch ("hola")
//          {
//              case frgtag_ConsultaCamaraView:
//                  break;
//          }

        Fragment fragment = (fragmentoIncidencia) getSupportFragmentManager().findFragmentByTag(frgtag_ReportaIncidencia);
        if (fragment != null)
            {
                MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                return;
            }
        fragment = (frgmt_Consulta_Camera_view) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaCamaraView);
        if (fragment != null)
            {
                MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                return;
            }



        fragment = (frgmnt_Consulta_Pallet_Det) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaPalletDet);
        if (fragment != null)
            {
                MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                return;
            }

//        fragment = (Consulta_Pallets) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaPallet);
//        if (fragment != null)
//            {
//                MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
//                        .commit();
//                return;
//            }

        fragment = (Consulta_Pallets) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaPalletsDET);
        if (fragment != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
                        .commit();
           //     MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                return;
            }


        fragment = (Consulta_Empaque_Pallet_Det) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaEmpaquePalletDET);
        if (fragment != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
                        .commit();
                //     MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                return;
            }

        fragment = (Consulta_Documentos) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaDocumentos);
        if (fragment != null)
            {
                if(fragment_Menu_Actual == frgtag_Menu_Inicio)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, frgmnt_Menu_Inicio.newInstance(null, null))
                                .commit();
                    }
                else if(fragment_Menu_Actual == frgtag_Menu_Consultas)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
                                .commit();
                    }
//                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
//                        .commit();

                return;
            }
        fragment = (Consulta_Documentos_OC) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaDocumentosOC);
        if (fragment != null)
            {

                if(fragment_Menu_Actual == frgtag_Menu_Inicio)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, frgmnt_Menu_Inicio.newInstance(null, null))
                                .commit();
                    }
                else if(fragment_Menu_Actual == frgtag_Menu_Consultas)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
                                .commit();
                    }

//                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
//                        .commit();

                return;
            }



        fragment = (Consulta_Pallets_Sin_Colocar) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaPalletsSinColocar);
        if (fragment != null)
            {

                if(fragment_Menu_Actual == frgtag_Menu_Inicio)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, frgmnt_Menu_Inicio.newInstance(null, null))
                                .commit();
                    }
                else if(fragment_Menu_Actual == frgtag_Menu_Consultas)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
                                .commit();
                    }

//                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
//                        .commit();

                return;
            }

        fragment = (Consulta_Existencias) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaExistenciasDet);
        if (fragment != null)
            {

                if(fragment_Menu_Actual == frgtag_Menu_Inicio)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, frgmnt_Menu_Inicio.newInstance(null, null))
                                .commit();
                    }
                else if(fragment_Menu_Actual == frgtag_Menu_Consultas)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
                                .commit();
                    }

//                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
//                        .commit();

                return;
            }


        fragment = (Consulta_Documentos_Tras) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaDocumentosTras);
        if (fragment != null)
            {

                if(fragment_Menu_Actual == frgtag_Menu_Inicio)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, frgmnt_Menu_Inicio.newInstance(null, null))
                                .commit();
                    }
                else if(fragment_Menu_Actual == frgtag_Menu_Consultas)
                    {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
                                .commit();
                    }
//                getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null, null))
//                        .commit();

                return;
            }

        fragment = (frgmnt_ConsultaDetalleIncidencia) getSupportFragmentManager().findFragmentByTag(frgtag_IncidenciaDet);
        if (fragment != null)
            {
                MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                return;
            }

        fragment = (frgmnt_Visor_Incidencias_Estaciones) getSupportFragmentManager().findFragmentByTag(frgtag_ListaIncidencias);
        if (fragment != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fl_ContenedorFragments, frgmnt_Visor_Incidencias_Estaciones.newInstance(null,null),frgtag_ListaIncidencias)
                        .commit();
                if(!salir)
                    {
                        Toast.makeText(this, "Para salir de la aplicación, vuelva a tocar atras.", Toast.LENGTH_SHORT).show();
                        salir = true;
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                salir = false;
                            }
                        },3000);
                    }
                else
                    {
//                        finish();
//                        System.exit(0);


                         startActivity(new Intent(Intent.ACTION_MAIN).addCategory( Intent.CATEGORY_HOME ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                            {
//                                finishAndRemoveTask();
//                            }else
//                            {
//                                this.finishAffinity();
//                            }
//




                    }


                return;
            }

        if(!salir)
            {
                Toast.makeText(this, "Para salir de la aplicación, vuelva a tocar atras.", Toast.LENGTH_SHORT).show();
                salir = true;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        salir = false;
                    }
                },3000);
            }
        else
            {
//                finish();
//                System.exit(0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        finishAndRemoveTask();
                    }else
                    {
                        this.finishAffinity();
                    }
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            {
                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        Log.i("ITEM_ID", "MAIN ACTIVITY" + String.valueOf(id));

        switch(id)
            {
                case R.id.nav_camera:
                    new Handler().post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .replace(R.id.fl_ContenedorFragments,frgmnt_Menu_Inicio.newInstance(null,null),frgtag_Menu_Inicio)
                                    .commit();
                        }
                    });
                    fragment_Menu_Actual = frgtag_Menu_Inicio;
                    break;

                case R.id.nav_gallery:
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fl_ContenedorFragments, fragmento_menu_consultas.newInstance(null,null),frgtag_Menu_Consultas)
                            .commit();
                    //fab.setVisibility(View.GONE);
                    fragment_Menu_Actual = frgtag_Menu_Consultas;
                    break;

                case R.id.nav_slideshow:
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fl_ContenedorFragments, frgmnt_Visor_Incidencias_Estaciones.newInstance(null,null),frgtag_ListaIncidencias)
                            .commit();
                    break;

                case R.id.Menu_Reimpresion_Etiquetas:
                    startActivity( new Intent(MainActivity.this, act_MiAXC_Impresion_Etiquetas.class));
                break;

                case R.id.Kardex:
                    startActivity(new Intent(MainActivity.this, act_MiAXC_ConsultaTransacciones.class));
                    break;

                case R.id.Referencia:
                    startActivity(new Intent(MainActivity.this, act_MiAXC_Kardex.class));

                    break;

                case R.id.MapaAlmacen:
                    startActivity( new Intent(MainActivity.this, act_Mapa_Almacen.class));

                    break;

                case R.id.lib_Embarque:
                    startActivity(new Intent(MainActivity.this, act_MiAXC_Liberacion_Embarque.class));

                    break;


                case R.id.lib_Transfer:
                    startActivity(new Intent(MainActivity.this, act_MiAXC_Liberacion_Transferencia.class));

                    break;

                case R.id.nav_share:
                    Intent intent = new Intent(MainActivity.this, Preferencias_AXC.class);
                    startActivity(intent);
                    break;
                case R.id.nav_send:
                    finish();
                    break;

                case R.id.Cons_Eb:
                    startActivity(new Intent(MainActivity.this, act_MiAXC_Liberacion_OrdenCompra.class));

                    break;

                default:
                    new popUpGenerico(this,getCurrentFocus() , "Opción no valida [DEF_VAL_EX]", false, true, true);


            }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        Log.i("URI", String.valueOf(uri));


        switch (String.valueOf(uri))
            {
                case "frgmnt_ConsultaDetalleIncidencia":
                    try
                        {

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    break;
               // onBackPressed();
            }
    }

    @Override
    public boolean ActivaProgressBar(Boolean estado)
    {

        return webmPlayer(estado);
    }

    @Override
    public boolean ActivarConsultaPalletBotonMasInfo(String frgtag, boolean Dec)
    {
        Log.i("frTAG", frgtag + " " + String.valueOf(Dec));
        if(frgtag.equals(frgtag_ConsultaPalletsDET))
            {
                permitirConsultaPallet = Dec;
            }
        if(frgtag.equals(frgtag_ConsultaDocumentos))
            {
                permitirConsultaPallet = Dec;
            }
        if(frgtag.equals(frgtag_ConsultaPalletsSinColocar))
            {
                permitirConsultaPallet = Dec;
            }
        if(frgtag.equals(frgtag_ConsultaExistenciasDet))
            {
                permitirConsultaPallet = Dec;
            }
        return false;
    }

    @Override
    public void onFragmentInteraction(String dato1, String dato2)
    {
        try
            {
                Toast.makeText(contexto, "Antes "+frgtag_IncidenciaDet , Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fl_ContenedorFragments, frgmnt_ConsultaDetalleIncidencia.newInstance(dato1, dato2), frgtag_IncidenciaDet)
                        //     .addToBackStack("add")
                        .commit();
                getSupportFragmentManager().executePendingTransactions();

             //   Thread.sleep(5000);
                Toast.makeText(contexto, "Despues "+frgtag_IncidenciaDet , Toast.LENGTH_SHORT).show();
                Fragment fragment = (frgmnt_ConsultaDetalleIncidencia) getSupportFragmentManager().findFragmentByTag(frgtag_IncidenciaDet);
                if (fragment != null)
                    {
                        MainActivity.this.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        return;
                    }
            }catch (Exception e)
            {
                new popUpGenerico(this,getCurrentFocus() , e.getMessage(), false, true, true);
            }
    }

    @Override
    public void onDataPass(String data)
    {
        Log.d("LOG","hello " + data);
    }

    @Override
    public boolean ActivaProgressBar(boolean estado)
    {
        return webmPlayer(estado);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        Log.i("onActivityResult", " ACTIVITY RQ " + requestCode + " RS " + resultCode);
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(frgtag_ReportaIncidencia);
        if (fragment != null)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void clickBotonMasInfo(String[] datos)
    {
        try
            {
                Fragment fragment = null;
                try
                    {
                      fragment = (frgmnt_Visor_Incidencias_Estaciones) getSupportFragmentManager().findFragmentByTag(frgtag_ListaIncidencias);
                        if (fragment != null)
                            {
                                ((frgmnt_Visor_Incidencias_Estaciones) fragment).clickBotonMasInfo(datos);
                            }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                try
                    {
                    fragment = (frgmnt_Consulta_Obj) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaPallet);
                    if (fragment != null)
                        {
                            ((frgmnt_Consulta_Obj) fragment).clickBotonMasInfo(datos);
                        }


                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                try
                    {
                    fragment = (frgmnt_Consulta_Obj) getSupportFragmentManager().findFragmentByTag(frgtag_ConsultaPallet);
                    if (fragment != null)
                        {
                            ((frgmnt_Consulta_Obj) fragment).clickBotonMasInfo(datos);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                if(permitirConsultaPallet)
                    {
                      //  if (fragment == null)
                            {
                                Log.i("CLICKBOTONMASINFO", datos[0] + " -   " + datos[1] + "   -   " + datos[2]);
                                getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                        .add(R.id.drawer_layout, frgmnt_Consulta_Pallet_Det.newInstance(datos, datos[0]), frgtag_ConsultaPalletDet)
                                        .commit();
                            }
                    }


            }catch (Exception e)
            {
                e.printStackTrace();
            }

    }

    @Override
    public String ReturnData(String[] dataToReturn) //EL FRAGMENTO QUE TENGA QUE USAR ESTE EVENTO DEL LECTOR DE CODIGOS DEBE AGREGAR EL TAG DEL FRAGMENTO EN LA POSICION 0 DE ARRAY
    {
            Fragment fragment = null;
            switch (dataToReturn[0])
            {
                case frgtag_ConsultaPallet:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((frgmnt_Consulta_Obj)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
                case frgtag_ConsultaDocumentos:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((Consulta_Documentos)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
                case frgtag_ConsultaPalletsDET:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((Consulta_Pallets)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
                    case frgtag_ConsultaEmpaquePalletDET:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((Consulta_Empaque_Pallet_Det)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
                case frgtag_ConsultaDocumentosOC:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((Consulta_Documentos_OC)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
                case frgtag_ConsultaDocumentosTras:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((Consulta_Documentos_Tras)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
                case frgtag_ConsultaPalletsSinColocar:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((Consulta_Pallets_Sin_Colocar)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
                case frgtag_ConsultaExistenciasDet:
                    fragment = getSupportFragmentManager().findFragmentByTag(dataToReturn[0]);
                    ((Consulta_Existencias)fragment).onQRCodeScanResponse(dataToReturn[1]);
                    break;
            }

        return null;
    }

    public boolean CambiarEstadocarga(boolean Estado)
    {
        try
            {
                AlphaAnimation inAnimation;
                if (progressBarHolder == null)
                    {
                        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
                    }
                AlphaAnimation outAnimation;
                if (Estado)
                    {
                        //ENTRADA
                        inAnimation = new AlphaAnimation(0f, 1f);
                        inAnimation.setDuration(200);
                        progressBarHolder.setAnimation(inAnimation);
                        progressBarHolder.setVisibility(View.VISIBLE);
                        progressBarHolder.requestFocus();
                    } else
                    {
                        //SALIDA
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        progressBarHolder.setAnimation(outAnimation);
                        progressBarHolder.setVisibility(View.GONE);
                    }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        return true;
    }

    private boolean webmPlayer(final boolean estado)
    {

        if(estado)
            {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fl_ContenedorFragments, intro_fragment.newInstance(null, null),frgtag_Loading)
                        .commit();
            }else
                {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(frgtag_Loading);
                    if (fragment != null)
                        {
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }

                }
        return true;
    }
}

//FragmentManager fm = getSupportFragmentManager();
//        Log.i("FM", "Fragment count" +getSupportFragmentManager().getBackStackEntryCount()
//                +getSupportFragmentManager().getFragments().toString());
//
//        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++)
//            {
//
//                Log.i("FM", "Found fragment: " + getSupportFragmentManager().getBackStackEntryAt(i).getId());
//            }

