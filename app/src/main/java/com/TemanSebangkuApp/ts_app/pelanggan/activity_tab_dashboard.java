package com.TemanSebangkuApp.ts_app.pelanggan;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.TemanSebangkuApp.ts_app.R;
import com.TemanSebangkuApp.ts_app.home.fragment_home;
import com.TemanSebangkuApp.ts_app.menu.fragment_menu;
import com.TemanSebangkuApp.ts_app.outlet.fragment_outlet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class activity_tab_dashboard extends AppCompatActivity implements LocationListener {
    private TextView mTextMessage;

    String lat ="", longi="";
    LocationManager locationManager;

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
        requestMultiplePermissions();
        getLocation();
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
                            Bundle bundle = new Bundle();

                            bundle.putString("latitude", "" + lat);
                            bundle.putString("longitude", "" + longi);
                            selectedFragment = new fragment_outlet();
                            selectedFragment.setArguments(bundle);
                            break;
                        case R.id.nav_akun:
                            selectedFragment = new fragment_akun();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                    return true;
                }
            };


    @Override
    public void onLocationChanged(Location location) {
        lat = ""+location.getLatitude();
        longi = ""+location.getLongitude();
        Log.e("latnya ", lat);
        try {
            getLocation();
        }catch(Exception e)
        {
            Log.e("errordua", "errornya : " + e.getMessage());

        }
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
//                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {

                    }
                })
                .onSameThread()
                .check();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        getLocation();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(activity_tab_dashboard.this, "Mohon Hidupkan GPS and Internet", Toast.LENGTH_SHORT).show();

    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            if(!lat.equals("") && !longi.equals("")){

            }
        }
        catch(SecurityException e) {
            e.printStackTrace();
            Log.e("errorsatu", "errornya : " + e.getMessage());
        }
    }
}
