package com.TemanSebangkuApp.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TemanSebangkuApp.ts_app.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.TemanSebangkuApp.ts_app.config.AppController;
import com.TemanSebangkuApp.ts_app.config.ImageUtil;
import com.TemanSebangkuApp.ts_app.config.ServerAPI;
import com.TemanSebangkuApp.ts_app.config.authdata;
import com.TemanSebangkuApp.ts_app.pelanggan.acitivity_data_diri;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_profil;
import com.TemanSebangkuApp.ts_app.pelanggan.activity_tab_dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_edit_profil_kasir extends AppCompatActivity {

    ProgressDialog pd;
    EditText txt_nama, txt_email, txt_noHp, txt_alamat, txt_password;
    Button btn_simpan;
    ImageView imgback;
    String kd_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_kasir);

        pd = new ProgressDialog(activity_edit_profil_kasir.this);
        imgback = (ImageView) findViewById(R.id.img_back);

        txt_nama = (EditText) findViewById(R.id.txt_nama);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_alamat = (EditText) findViewById(R.id.txt_alamat);
        txt_noHp = (EditText) findViewById(R.id.txt_Hp);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_simpan = (Button) findViewById(R.id.btn_simpan);

        Bundle bundle = getIntent().getExtras();
        kd_user = bundle.getString("kd_user");

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_edit_profil_kasir.this, activity_profil_kasir.class);
                activity_edit_profil_kasir.this.startActivity(intent);
            }
        });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_profil();
            }
        });

        get_profil();
    }

    public void get_profil() {
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
                        txt_email.setText(data.getString("email"));
                        txt_noHp.setText(data.getString("no_hp"));
                        txt_password.setText("");
                        txt_alamat.setText(data.getString("alamat"));

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_edit_profil_kasir.this, "Gagal memuat data", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(activity_edit_profil_kasir.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Erronya ", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tipe", "profil_kasir");
                params.put("kode", authdata.getInstance(activity_edit_profil_kasir.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void edit_profil(){
        final String nama = txt_nama.getText().toString().trim();
        final String email = txt_email.getText().toString().trim();
        final String no_hp = txt_noHp.getText().toString().trim();
        final String password = txt_password.getText().toString().trim();
        final String alamat = txt_alamat.getText().toString().trim();

        if (password.equals("")) {
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            txt_password.setFocusable(true);
        } else if (no_hp.equals("")) {
            Toast.makeText(this, "Nomor HP Masih Kosong", Toast.LENGTH_SHORT).show();
            txt_noHp.setFocusable(true);
        } else if (email.equals("")) {
            Toast.makeText(this, "Email harus diisi", Toast.LENGTH_SHORT).show();
            txt_email.setFocusable(true);
        } else if (nama.equals("")) {
            Toast.makeText(this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
            txt_nama.setFocusable(true);
        }  else {
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SAVE_DATA, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONObject data = obj.getJSONObject("respon");
                        if (data.getBoolean("savedata")) {
                            Intent i = new Intent(activity_edit_profil_kasir.this, activity_profil.class);
                            Toast.makeText(activity_edit_profil_kasir.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                            startActivity(i);
                        } else {
                            Toast.makeText(activity_edit_profil_kasir.this, data.getString("pesan"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(activity_edit_profil_kasir.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(activity_edit_profil_kasir.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("tipe", "edit_kasir");
                    params.put("nama", nama);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("no_hp", no_hp);
                    params.put("alamat", alamat);
                    params.put("kode_user", SharedPrefManager.getInstance(activity_edit_profil_kasir.this).getKeyUserId());
                    params.put("kode", SharedPrefManager.getInstance(activity_edit_profil_kasir.this).getAuthKey());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        }


    }
}
