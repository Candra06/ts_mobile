package com.example.ts_app.pelanggan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.ts_app.R;
import com.example.ts_app.home.fragment_home;
import com.example.ts_app.menu.fragment_menu;
import com.example.ts_app.outlet.fragment_outlet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class activity_tab_dashboard extends AppCompatActivity  {
    private TextView mTextMessage;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    mTextMessage.setText(R.string.mn_home);
                    return true;
                case R.id.nav_menu:
                    mTextMessage.setText(R.string.mn_menu);
                    return true;
                case R.id.nav_outlet:
                    mTextMessage.setText(R.string.mn_outlet);
                    return true;
                case R.id.nav_akun:
                    mTextMessage.setText(R.string.mn_akun);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_dashboard);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new fragment_home()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new fragment_home();
                            break;
                        case R.id.nav_menu:
                            selectedFragment = new fragment_menu();
                            break;
                        case R.id.nav_outlet:
                            selectedFragment = new fragment_outlet();
                            break;
                        case R.id.nav_akun:
                            selectedFragment = new fragment_akun();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                    return true;
                }
            };



}
