package com.TemanSebangkuApp.ts_app.pelanggan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.SharedPrefManager;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_register extends AppCompatActivity {

    EditText txt_nama, txt_email, txt_referal;
    Button btn_simpan;
    ProgressDialog progressDialog;
    String kd_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(activity_register.this);
        txt_nama = findViewById(R.id.txt_nama);
        txt_email = findViewById(R.id.txt_email);
        txt_referal = findViewById(R.id.txt_referall);
        btn_simpan = findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_nama.equals("")){

                }else if (txt_email.equals("")){

                }else {
                    if (txt_referal.equals("")){
                        txt_referal.setText(" ");
                        Log.d("txt",txt_referal.getText().toString());
                        register();
                    }else {
                        register();
                    }

                }
            }
        });
    }

    public void register(){
        progressDialog.setMessage("Tunggu....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("respon");

                    if (data.getBoolean("bool")) {
                        Toast.makeText(activity_register.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(activity_register.this, activity_tab_dashboard.class);
                        activity_register.this.startActivity(myIntent);
//                        Toast.makeText(activity_register.this, "Input Berhasil "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                        SharedPrefManager.getInstance(activity_register.this).register();
                    } else {

//                        Toast.makeText(activity_register.this, "Selamat Datang "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(activity_register.this, activity_register.class);
                        activity_register.this.startActivity(myIntent);
                        Toast.makeText(activity_register.this, "Input Gagal "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
//                    Toast.makeText(activity_register.this, "Masuk Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(activity_register.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", txt_email.getText().toString());
                params.put("nama", txt_nama.getText().toString());
                params.put("referal", txt_referal.getText().toString());
                params.put("tipe", "register");
                params.put("kd_user", authdata.getInstance(activity_register.this).getKd_user());
                params.put("kode", authdata.getInstance(activity_register.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
