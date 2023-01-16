package com.automatica.AXCPT.Principal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.automatica.AXCPT.R;
import com.automatica.AXCPT.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {
    ActivitySplashBinding binding;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());



        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.dezplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.dezplazamiento_abajo);


        //TextView txtDe = findViewById(R.id.txtDe);
        //TextView txtAuto = findViewById(R.id.txtAuto);
        ImageView imgName = findViewById(R.id.imgv_INMOTION);

        //txtDe.setAnimation(animacion2);
        //txtAuto.setAnimation(animacion2);
        imgName.setAnimation(animacion1);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(Splash.this, Login.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(imgName, "logoTrans");
                //pairs[1] = new Pair<View, String>(txtAuto, "txtTrans");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash.this, pairs);
                startActivity(intent, options.toBundle());

                    //startActivity(intent);
                    //finish();


            }
        },1000);
    }
}