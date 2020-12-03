package com.example.blackboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.blackboard.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;
    private ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            splashImage = findViewById(R.id.splash_image);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoom_in);
            splashImage.startAnimation(animation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }, SPLASH_SCREEN);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}