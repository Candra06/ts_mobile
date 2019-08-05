package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.ts_app.pelanggan.activity_tab_dashboard;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class activity_scan_voucher extends AppCompatActivity {

    private ZXingScannerView scannerView;
    TextView txt_promo;
    ProgressDialog pd;
    Button btnscan;
    String get_code, kd_promo;
    LinearLayout linear_promo;

    ArrayList<String> data_promo = new ArrayList<String>();
    ArrayList<String> index_promo=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_voucher);
        btnscan = (Button) findViewById(R.id.btn_scan);
        pd = new ProgressDialog(activity_scan_voucher.this);

        btnscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_code();
            }
        });

        txt_promo = (TextView) findViewById(R.id.txt_pilih_promo);
        linear_promo = (LinearLayout) findViewById(R.id.linear_promo);
        linear_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(activity_scan_voucher.this);
                pictureDialog.setTitle("Pilih Kavling Anda");
                pictureDialog.setItems(data_promo.toArray(new String[0]),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                kd_promo = index_promo.get(which);
//                                Log.e("kodenya",""+kodekavling);
                                txt_promo.setText(data_promo.get(which));

                            }
                        });
                pictureDialog.show();
            }
        });


    }

    public void get_promo(){
        StringRequest senddata = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                    JSONObject respon = res.getJSONObject("respon");
                    if(respon.getBoolean("bool")){
                        JSONArray arr = res.getJSONArray("data");
                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject datakom = arr.getJSONObject(i);
                                String ku = datakom.getString("judul_promo");
                                String koded = datakom.getString("kd_promo");
                                data_promo.add(ku);
                                index_promo.add(koded);
                            } catch (Exception ea) {
                                ea.printStackTrace();

                            }
                        }

                    }else {
                        Toast.makeText(activity_scan_voucher.this, respon.getString("pesan"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
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


    public void scan_code(){
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new scan_voucher());

        setContentView(scannerView);
        scannerView.startCamera();
    }

    public void onPause() {

        super.onPause();
        scannerView.stopCamera();
        Intent i = new Intent(activity_scan_voucher.this, activity_scan_voucher.class);
        startActivity(i);
    }

    class scan_voucher implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(Result result){
            String getcode = result.getText();
            Toast.makeText(activity_scan_voucher.this, getcode,Toast.LENGTH_SHORT);
            Log.e("code scan", getcode);
            get_code = getcode;
            update_voucher();
            setContentView(R.layout.activity_scan_voucher);
            scannerView.stopCamera();
        }
    }

    public void promo(){

    }

    public void update_voucher(){
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONObject data = obj.getJSONObject("respon");
                    if (data.getBoolean("savedata")) {
                        Intent i = new Intent(activity_scan_voucher.this, activity_tab_dashboard.class);
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
                params.put("kd_promo", get_code);
                params.put("kode", SharedPrefManager.getInstance(activity_scan_voucher.this).getAuthKey());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

}
