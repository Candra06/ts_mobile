package com.TemanSebangkuApp.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;
import com.TemanSebangkuApp.ts_app.kasir.activity_dashboard_kasir;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_register;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_tab_dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_input_password extends AppCompatActivity {

    TextView txt_title, txt_sub_title;
    EditText password;
    Button btn_konfirmasi;
    String no_hp, title, sub_title, type;
    ProgressDialog pd;

    public String xdatauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_password);


        pd = new ProgressDialog(activity_input_password.this);
        txt_title = findViewById(R.id.txt_title);
        txt_sub_title = findViewById(R.id.txt_sub_title);
        btn_konfirmasi = findViewById(R.id.btn_input_password);
        password = findViewById(R.id.txt_password);
        Log.d("pesan", "halaman input password");
        Bundle bundle = getIntent().getExtras();
        no_hp = bundle.getString("no_hp");
        title = bundle.getString("judul");
        sub_title = bundle.getString("sub_title");
        type = bundle.getString("type");
        txt_title.setText(title);
        txt_sub_title.setText(sub_title);


        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.equals("")){
                    Toast.makeText(activity_input_password.this, "Harap Masukkan password anda ", Toast.LENGTH_LONG).show();
                }else {
                    confirm();
                }
            }
        });
    }

    public void confirm(){
        pd.setMessage("Tunggu....");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    String get_type = type;
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("respon");
                    JSONObject mydata = data.getJSONObject("user");

                    if (data.getBoolean("boollogin")) {

                        authdata.getInstance(activity_input_password.this).setdatauser(
                                mydata.getString("auth_key"),
                                mydata.getString("kd_akses"),
                                mydata.getString("nama"),
                                mydata.getString("level"),
                                mydata.getString("exp_date"),
                                data.getString("kd_outlet")
                        );
//                        Toast.makeText(activity_input_password.this, "levelnya "+mydata.getString("level"), Toast.LENGTH_SHORT).show();
                        Log.d("pesan", "levelnya di input password "+mydata.getString("level"));
                        if (get_type.equals("2")){
                            Toast.makeText(activity_input_password.this, "Selamat Datang "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_input_password.this, activity_dashboard_kasir.class);
                            activity_input_password.this.startActivity(myIntent);
                        }else if (get_type.equals("3")){
                            Toast.makeText(activity_input_password.this, "Selamat Datang "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_input_password.this, activity_tab_dashboard.class);
                            activity_input_password.this.startActivity(myIntent);
                        }else if (get_type.equals("0")){
                            Toast.makeText(activity_input_password.this, "Selamat Datang "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(activity_input_password.this, activity_register.class);
                            activity_input_password.this.startActivity(myIntent);
                        }else {
                            Toast.makeText(activity_input_password.this, "Selamat Goblok "+get_type, Toast.LENGTH_LONG).show();
                        }
                        xdatauth = data.getString("auth_key");
                        Log.e("auth_key", data.getString("auth_key"));
                    } else {
                        Toast.makeText(activity_input_password.this, "Password salah "+data.getString("pesan"), Toast.LENGTH_LONG).show();
                        Log.e("No_hp", no_hp);
                        Log.e("password", password.getText().toString());
                        Log.e("tipe", "first_login");
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
//                    Toast.makeText(activity_input_password.this, "Login Gagal", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.e("No_hp", no_hp);
                    Log.e("password", password.getText().toString());
                    Log.e("tipe", "login");

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_input_password.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_hp", no_hp);
                params.put("tipe", "login");
                params.put("password", password.getText().toString());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


}
