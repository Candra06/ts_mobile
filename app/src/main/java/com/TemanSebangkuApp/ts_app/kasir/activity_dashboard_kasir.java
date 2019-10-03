package com.TemanSebangkuApp.ts_app.kasir;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.activity_profil_kasir;
import com.TemanSebangkuApp.ts_app.activity_scan_voucher;
import com.TemanSebangkuApp.ts_app.activity_set_menu;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_dashboard_kasir extends AppCompatActivity {
    CardView scan_qr, profil, menu;
    ProgressDialog pd;
    TextView txt_nama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_kasir);

        scan_qr = (CardView) findViewById(R.id.card_qr);
        profil = (CardView) findViewById(R.id.card_profil_dashboard);
        menu = (CardView) findViewById(R.id.card_menu);

        pd = new ProgressDialog(activity_dashboard_kasir.this);
        txt_nama = (TextView) findViewById(R.id.txt_nama_kasir);

        loadProfil();

        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_dashboard_kasir.this, activity_scan_voucher.class);
                activity_dashboard_kasir.this.startActivity(intent);
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_dashboard_kasir.this, activity_profil_kasir.class);
                activity_dashboard_kasir.this.startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_dashboard_kasir.this, activity_set_menu.class);
                activity_dashboard_kasir.this.startActivity(intent);
            }
        });
    }

    public void loadProfil(){
        pd.setMessage("Tunggu....");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.GET_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray data_profil = object.getJSONArray("data");
                    JSONObject data = data_profil.getJSONObject(0);

                    txt_nama.setText(data.getString("nama"));

                } catch (JSONException e) {
                    Log.e("Erornyacoy", e.getMessage());
                    Log.e("auth", authdata.getInstance(activity_dashboard_kasir.this).getAuth());
                    Toast.makeText(activity_dashboard_kasir.this, "Gagal mengambil data", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_dashboard_kasir.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya boss", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "kasir_profil");
                params.put("kode", authdata.getInstance(activity_dashboard_kasir.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
