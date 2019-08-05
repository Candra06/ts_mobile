package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_login extends AppCompatActivity {

    EditText txtNoHp;
    Button btn_login;
    ProgressDialog pd;

    public String xdataauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pd = new ProgressDialog(activity_login.this);
        btn_login = findViewById(R.id.btn_join);

        onLogin();
        txtNoHp = findViewById(R.id.txt_no_hp);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String no_hp = txtNoHp.getText().toString();

                if (no_hp.equals("")) {
                    Toast.makeText(activity_login.this, "Harap Masukkan Nomor HP anda", Toast.LENGTH_LONG).show();
                } else {
                    Cek_akun();
                }
            }
        });
    }

    private void Cek_akun() {
        pd.setMessage("Tunggu....");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("respon");
                    pd.cancel();

                    if (data.getBoolean("boollogin")) {

                        if (data.getString("status").equals("1")) {
                            String type = "karyawan";
                            String judul = "Masukkan password";
                            String sub_title = "Silahkan Login";
                            String no_hp = txtNoHp.getText().toString();
                            Toast.makeText(activity_login.this, "Selamat Datang karyawan", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
                            myIntent.putExtra("no_hp", no_hp); //Optional parameters
                            myIntent.putExtra("judul", judul); //Optional parameters
                            myIntent.putExtra("sub_title", sub_title); //Optional parameters
                            myIntent.putExtra("type", type); //Optional parameters
                            activity_login.this.startActivity(myIntent);
                            Log.e("toastnya : ", "selamat datang karyawan");
                        } else if (data.getString("status").equals("0")) {
                            String type = "pelanggan baru";
                            String judul = "Buat password";
                            String sub_title = "Amankan akun anda";
                            String no_hp = txtNoHp.getText().toString();
                            Toast.makeText(activity_login.this, "Selamat Datang pelanggan baru", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
                            myIntent.putExtra("no_hp", no_hp); //Optional parameters
                            myIntent.putExtra("judul", judul); //Optional parameters
                            myIntent.putExtra("sub_title", sub_title); //Optional parameters
                            myIntent.putExtra("type", type); //Optional parameters
                            activity_login.this.startActivity(myIntent);
                            Log.e("toastnya : ", "selamat datang pelanggan baru");
                        } else if (data.getString("status").equals("2")) {
                            String type = "pelanggan";
                            String judul = "Maukkan password";
                            String sub_title = "Silahkan Login";
                            String no_hp = txtNoHp.getText().toString();
                            Toast.makeText(activity_login.this, "Selamat Datang pelanggan", Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
                            myIntent.putExtra("no_hp", no_hp); //Optional parameters
                            myIntent.putExtra("judul", judul); //Optional parameters
                            myIntent.putExtra("sub_title", sub_title); //Optional parameters
                            myIntent.putExtra("type", type); //Optional parameters
                            activity_login.this.startActivity(myIntent);
                            Log.e("toastnya : ", "selamat datangpelanggan");
                        } else {
                            Log.e("toastnya : ", "selamat datangpelangganmnknj");
                        }

                    } else {
//                        String judul = "Amankan akun anda";
//                        String sub_title = "Masukkan password anda";
//                        String no_hp = txtNoHp.getText().toString();
                        Toast.makeText(activity_login.this, "Nomor hp tidak valid " + data.getString("pesan"), Toast.LENGTH_LONG).show();
//                        Intent myIntent = new Intent(activity_login.this, activity_input_password.class);
//                        myIntent.putExtra("no_hp", no_hp); //Optional parameters
//                        myIntent.putExtra("judul", judul); //Optional parameters
//                        myIntent.putExtra("sub_title", sub_title); //Optional parameters
//                        activity_login.this.startActivity(myIntent);
//                        Toast.makeText(activity_login.this, "Akun baru "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                        Log.e("toastnya : ", "selamat gagal");
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_login.this, "Masuk Gagal" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Erronya ", error.toString(), error);
                Log.e("password ", "");
                Log.e("no_hp ", txtNoHp.getText().toString());
                Log.e("Erronya ", "first_login");
                Toast.makeText(activity_login.this, error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_hp", txtNoHp.getText().toString());
//                params.put("aksi", "login");
                params.put("password", "");
                params.put("tipe", "first_login");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void onLogin() {
        if (authdata.getInstance(this).isLoggedIn()) {
            activity_login.this.finish();
            startActivity(new Intent(getBaseContext(), activity_tab_dashboard.class));
        }
    }

}
