package com.example.ts_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class activity_detail_promo extends AppCompatActivity {

    FloatingActionButton btn_share;
    ProgressDialog pd;
    String text;

    ImageView img_detail_promo, img_back;

    TextView txt_title, txt_conten;
    String kd_promo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promo);

        btn_share = (FloatingActionButton) findViewById(R.id.btn_share);
        pd = new ProgressDialog(activity_detail_promo.this);

        Bundle bundle = getIntent().getExtras();
        kd_promo = bundle.getString("kd_promo");

        txt_title = (TextView) findViewById(R.id.txt_title_promo);
        txt_conten = (TextView) findViewById(R.id.txt_ketentuan);

        img_detail_promo = (ImageView) findViewById(R.id.img_detail_promo);

        loadJSON();
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cek_akun();
            }
        });
    }

    private void Cek_akun() {
        pd.setMessage("Tunggu....");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.SHARE_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("respon");
                    pd.cancel();

                    if (data.getBoolean("bool")) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        text = data.getString("link");
                        URL get_link = new URL(text);
                        i.putExtra(Intent.EXTRA_TEXT, "klik link berikut "+get_link);
                        Log.e("linknya", "ini "+get_link);
                        i.putExtra(Intent.EXTRA_SUBJECT, "klik link berikut "+get_link);
                        startActivity(Intent.createChooser(i, "Kirim melalui :"));


                    } else {
                        Toast.makeText(activity_detail_promo.this, "Nomor hp tidak valid " + data.getString("pesan"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_detail_promo.this, "Masuk Gagal" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Erronya ", error.toString(), error);
                Toast.makeText(activity_detail_promo.this, error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kode", authdata.getInstance(activity_detail_promo.this).getAuth());
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void loadJSON() {
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

                    pd.cancel();


                    txt_title.setText(data.getString("judul_promo"));
                    txt_conten.setText(data.getString("syarat_ketentuan"));

                    Picasso.get()
                            .load(ServerAPI.IPSever+data.getString("foto"))
                            .into(img_detail_promo);

                } catch (JSONException e) {
                    Log.e("Erornya", e.getMessage());
                    Toast.makeText(activity_detail_promo.this, "Masuk Gagal" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Erronya ", error.toString(), error);
                Toast.makeText(activity_detail_promo.this, error.getMessage(), Toast.LENGTH_LONG).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kode", authdata.getInstance(activity_detail_promo.this).getAuth());
                params.put("kd_promo", kd_promo);
                params.put("tipe", "detail_promo");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
