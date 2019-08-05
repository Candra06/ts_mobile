package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash_screen extends AppCompatActivity {
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        textView = (TextView) findViewById(R.id.txt_hint_splash);
        imageView = (ImageView) findViewById(R.id.img_splash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        textView.startAnimation(animation);
        imageView.startAnimation(animation);
        final Intent in = new Intent(this, activity_login.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(in);
                    finish();
                }
            }

        };
        timer.start();
    }
}
