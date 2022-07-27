package com.automatica.AXCPT.Servicios;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.automatica.AXCPT.Fragmentos.Fragmento_Menu;
import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.R;
import com.automatica.axc_lib.Servicios.popUpGenerico;
import static com.automatica.AXCPT.Fragmentos.Fragmento_Menu.getToolbarLogoIcon;

public class ActivityHelpers
{
    private frgmnt_taskbar_AXC taskbar_axc;
    public popUpGenerico pop;
    /**
     * ESTE METODO REALIZA LA CONFIGURACION DEL MENU PRINCIPAL Y DEL TASKBAR DE CONSULTAS DE MANERA SENCILLA
     * EN LAS ACTIVIDADES.
     *
     * @param actividad
     * Actividad actual.
     * @param Layout
     * Layout donde se deberá ingresar los objetos del menú principal.
     * */

    public void AgregarMenus(final AppCompatActivity actividad,final int Layout,boolean regresarMenu)
    {
        if(actividad == null)
        {
            throw new NullPointerException("La actividad no puede ser nula.");
        }
        if(Layout <= 0)
        {
            throw new IndexOutOfBoundsException("El layout tiene que ser un número valido.");
        }

        if(actividad.findViewById(Layout) == null)
        {
            throw new NullPointerException("El layout tiene que existir dentro de la actividad.");
        }

        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance(regresarMenu);
        actividad.getSupportFragmentManager().beginTransaction().add(Layout, taskbar_axc, "FragmentoTaskBar").commit();

        Toolbar toolbar = actividad.findViewById(R.id.toolbar);
        actividad.setSupportActionBar(toolbar);
        pop = new popUpGenerico(actividad.getBaseContext());
        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (actividad.getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null)
                {
                    actividad.getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(Layout, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else
                {
                    actividad.getSupportFragmentManager().popBackStack();
                }
            }
        });

                    /*
                    FrameLayout vistaMenu = new FrameLayout(actividad.getApplicationContext());
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    vistaMenu.setLayoutParams(lp);
                    ConstraintSet set = new ConstraintSet();
                    vistaMenu.setId(View.generateViewId());
                    ConstraintLayout padre = ((ConstraintLayout)actividad.findViewById(Layout));
                    vistaMenu.setElevation(100);
                    padre.addView(vistaMenu,0);
                    set.clone(padre);
                    set.connect(vistaMenu.getId(),ConstraintSet.TOP,padre.getId(),ConstraintSet.TOP);
                    set.applyTo(padre);
                    //vistaMenu.addView(new ProgressBar(actividad.getApplicationContext()));
                    */
    }

    public void AgregarMenu(final AppCompatActivity actividad,final int Layout)
    {
        if(actividad == null)
        {
            throw new NullPointerException("La actividad no puede ser nula.");
        }
        if(Layout <= 0)
        {
            throw new IndexOutOfBoundsException("El layout tiene que ser un número valido.");
        }

        if(actividad.findViewById(Layout) == null)
        {
            throw new NullPointerException("El layout tiene que existir dentro de la actividad.");
        }
        Toolbar toolbar = actividad.findViewById(R.id.toolbar);

        View logoView = getToolbarLogoIcon(toolbar);
        logoView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (actividad.getSupportFragmentManager().findFragmentByTag("FragmentoMenu") == null)
                {
                    actividad.getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, R.anim.slide_out_left, android.R.anim.slide_in_left, R.anim.slide_out_left)
                            .add(Layout, Fragmento_Menu.newInstance("", ""), "FragmentoMenu").addToBackStack("").commit();
                } else
                {
                    actividad.getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    public void AgregarTaskBar(final AppCompatActivity actividad,final int Layout,boolean regresarMenu)
    {
        if(actividad == null)
        {
            throw new NullPointerException("La actividad no puede ser nula.");
        }
        if(Layout <= 0)
        {
            throw new IndexOutOfBoundsException("El layout tiene que ser un número valido.");
        }

        if(actividad.findViewById(Layout) == null)
        {
            throw new NullPointerException("El layout tiene que existir dentro de la actividad.");
        }
        taskbar_axc = (frgmnt_taskbar_AXC) frgmnt_taskbar_AXC.newInstance(regresarMenu);
        actividad.getSupportFragmentManager().beginTransaction().add(Layout, taskbar_axc, "FragmentoTaskBar").commit();

    }
    public frgmnt_taskbar_AXC getTaskbar_axc()
    {
        return taskbar_axc;
    }
}
