package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.ts_app.config.authdata;
import com.example.ts_app.kasir.activity_dashboard_kasir;
import com.example.ts_app.pelanggan.activity_profil;

public class activity_profil_kasir extends AppCompatActivity {

    CardView cv_profil, cv_logout, cv_status_outlet;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_kasir);

        cv_profil = (CardView) findViewById(R.id.card_profil);
        cv_logout = (CardView) findViewById(R.id.card_log_out);
        cv_status_outlet = (CardView) findViewById(R.id.card_status_outlet);

        btn_back = (ImageView) findViewById(R.id.img_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_profil_kasir.this, activity_dashboard_kasir.class);
                activity_profil_kasir.this.startActivity(intent);
            }
        });

        cv_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(activity_profil_kasir.this, activity_edit_profil_kasir.class);
                myIntent.putExtra("kd_user", authdata.getInstance(activity_profil_kasir.this).getKd_user()); //Optional parameters
                activity_profil_kasir.this.startActivity(myIntent);
            }
        });

        cv_status_outlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity_profil_kasir.this, MapsActivity.class);
                activity_profil_kasir.this.startActivity(i);
            }
        });

        cv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authdata.getInstance(activity_profil_kasir.this).logout();
                Intent i = new Intent(activity_profil_kasir.this, activity_login.class);
                activity_profil_kasir.this.startActivity(i);
            }
        });
    }
}
