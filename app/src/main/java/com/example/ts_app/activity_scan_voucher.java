package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.config.authdata;
import com.example.ts_app.kasir.activity_dashboard_kasir;
import com.example.ts_app.pelanggan.activity_tab_dashboard;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission_group.CAMERA;

public class activity_scan_voucher extends AppCompatActivity {

    private ZXingScannerView scannerView;
    TextView txt_promo;
    ProgressDialog pd;
    Button btnscan;
    String get_code, kd_promo;
    LinearLayout linear_promo;
    View dialogView;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    ImageView img_back;

    CharSequence options[] = new CharSequence[] {"Call", "SMS", "Email"};

    ArrayList<String> data_promo = new ArrayList<String>();
    ArrayList<String> index_promo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_voucher);
        btnscan = (Button) findViewById(R.id.btn_scan);
        pd = new ProgressDialog(activity_scan_voucher.this);

        img_back = (ImageView) findViewById(R.id.img_back);
        requestMultiplePermissions();



        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_code();
            }
        });
        get_promo();
        txt_promo = (TextView) findViewById(R.id.txt_pilih_promo);
        linear_promo = (LinearLayout) findViewById(R.id.linear_promo);
        linear_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_scan_voucher.this);
                builder.setCancelable(false);
                builder.setTitle("Pilih promo");
                builder.setItems(data_promo.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        txt_promo.setText(data_promo.get(which));
                        kd_promo = index_promo.get(which);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the user clicked on Cancel
                    }
                });
                builder.show();

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(activity_scan_voucher.this, activity_dashboard_kasir.class);
                activity_scan_voucher.this.startActivity(in);
            }
        });
    }


    public void get_promo() {
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);

                    JSONArray arr = res.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        try {
                            JSONObject datakom = arr.getJSONObject(i);
                            String ku = datakom.getString("judul_promo");
                            String koded = datakom.getString("kd_promo");
                            Log.e("kodenya", datakom.getString("kd_promo"));
                            data_promo.add(ku);
                            index_promo.add(koded);
                        } catch (Exception ea) {
                            ea.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("daptkan eror", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "errornya : " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("kode", authdata.getInstance(getApplicationContext()).getAuth());
                params.put("tipe", "promo");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(senddata);
    }


    public void scan_code() {
//        requestPermission();
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new scan_voucher());

        setContentView(scannerView);
        scannerView.startCamera();
    }

    public void onPause() {

        super.onPause();
        setContentView(R.layout.activity_scan_voucher);
    }

    class scan_voucher implements ZXingScannerView.ResultHandler {
        @Override
        public void handleResult(Result result) {
            String getcode = result.getText();
            Toast.makeText(activity_scan_voucher.this, getcode, Toast.LENGTH_SHORT);
            Log.e("code scan", getcode);
            get_code = getcode;
            update_voucher();
            setContentView(R.layout.activity_scan_voucher);
            scannerView.stopCamera();
        }
    }

    public void update_voucher() {
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject data = obj.getJSONObject("respon");
                    if (data.getBoolean("savedata")) {
                        Intent i = new Intent(activity_scan_voucher.this, activity_dashboard_kasir.class);
                        Toast.makeText(activity_scan_voucher.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                        startActivity(i);
                    } else {
                        Toast.makeText(activity_scan_voucher.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(activity_scan_voucher.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Toast.makeText(activity_scan_voucher.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "acc_voucher");
                params.put("kd_user", get_code);
                params.put("kd_promo", kd_promo);
                params.put("kode", SharedPrefManager.getInstance(activity_scan_voucher.this).getAuthKey());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
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
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 1);

    }

}
