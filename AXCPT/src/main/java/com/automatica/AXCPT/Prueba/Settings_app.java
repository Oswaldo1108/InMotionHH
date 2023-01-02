package com.automatica.AXCPT.Prueba;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.automatica.AXCPT.Fragmentos.frgmnt_taskbar_AXC;
import com.automatica.AXCPT.Servicios.ProgressBarHelper;
import com.automatica.AXCPT.databinding.ActivityPruebaBinding;
import com.automatica.axc_lib.Servicios.popUpGenerico;

public class Settings_app extends AppCompatActivity {

    private ProgressBarHelper p;
    View vista;
    Context contexto = this;
    Bundle bundle = new Bundle();
    popUpGenerico pop = new popUpGenerico(Settings_app.this);
    ActivityPruebaBinding binding;
    frgmnt_taskbar_AXC taskbar_axc;
    Intent intent;
    Handler h = new Handler();

    // Ciclo de vida

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPruebaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        taskbar_axc.cambiarResources(frgmnt_taskbar_AXC.DEFAULT);
    }
}