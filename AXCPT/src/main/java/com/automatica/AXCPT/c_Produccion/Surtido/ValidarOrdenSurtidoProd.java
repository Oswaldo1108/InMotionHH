package com.automatica.AXCPT.c_Produccion.Surtido;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.automatica.AXCPT.databinding.ActivityValidarOrdenSurtidoProdBinding;

public class ValidarOrdenSurtidoProd extends AppCompatActivity {

    ActivityValidarOrdenSurtidoProdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityValidarOrdenSurtidoProdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}