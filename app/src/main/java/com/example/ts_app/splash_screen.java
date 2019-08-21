package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ts_app.config.authdata;
import com.example.ts_app.kasir.activity_dashboard_kasir;
import com.example.ts_app.pelanggan.activity_tab_dashboard;

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
        onLogin();
    }

    private void onLogin() {
        if (authdata.getInstance(this).isLoggedIn()) {
            if (authdata.getInstance(getBaseContext()).getLevel().equals("2")){
                Thread timer = new Thread(){
                    public void run(){
                        try{
                            sleep(2000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        finally {
                            startActivity(new Intent(getBaseContext(), activity_dashboard_kasir.class));
                            finish();
                        }
                    }
                };
                timer.start();
            }else if(authdata.getInstance(getBaseContext()).getLevel().equals("3")){
                Thread timer = new Thread(){
                    public void run(){
                        try{
                            sleep(2000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        finally {
                            startActivity(new Intent(getBaseContext(), activity_tab_dashboard.class));
                            finish();
                        }
                    }
                };
                timer.start();
            }
            splash_screen.this.finish();
        }else{
            final Intent in = new Intent(this, activity_login.class);
            Thread timer = new Thread(){
                public void run(){
                    try{
                        sleep(2000);
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
}
