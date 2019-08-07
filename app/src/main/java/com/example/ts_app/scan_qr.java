package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ts_app.config.AppController;
import com.example.ts_app.config.ServerAPI;
import com.example.ts_app.kasir.activity_dashboard_kasir;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class scan_qr extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ProgressDialog pd;
    ZXingScannerView Scannerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Scannerview = new ZXingScannerView(this);
        setContentView(Scannerview);
        pd = new ProgressDialog(scan_qr.this);

//        Log.d("berhasil dapat", "kebukak gan");
    }

    @Override
    public void handleResult(Result result) {
//        activity_scan_voucher.txt_get_code.setText(result.getText());
//        get_code.equals(activity_scan_voucher.txt_get_code.toString());

        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Scannerview.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Scannerview.setResultHandler(this);
        Scannerview.startCamera();
    }


}
